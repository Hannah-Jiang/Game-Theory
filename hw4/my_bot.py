import random
import other_bots
import traders
import run_experiments
import plot_simulation

class MyBot(traders.Trader):
    name = 'my_bot'
    
    def simulation_params(self, timesteps,
                          possible_jump_locations,
                          single_jump_probability):
        """Receive information about the simulation."""
        # Number of trading opportunities
        self.timesteps = timesteps
        # A list of timesteps when there could be a jump
        self.possible_jump_locations = possible_jump_locations
        # For each of the possible jump locations, the probability of
        # actually jumping at that point. Jumps are normally
        # distributed with mean 0 and standard deviation 0.2.
        self.single_jump_probability = single_jump_probability
        # A place to store the information we get
        self.information = []
        self.belief = 50.0
        self.start_block_size = 20
        self.min_block_size = 2
        
        self.inform = 5;
        self.uninform = 2
        #self.alpha = self.inform*1.0/(self.inform + self.uninform)
        self.alpha = 0.9
        
        self.action = ''
    
    def new_information(self, info, time):
        """Get information about the underlying market value.
        
        info: 1 with probability equal to the current
          underlying market value, and 0 otherwise.
        time: The current timestep for the experiment. It
          matches up with possible_jump_locations. It will
          be between 0 and self.timesteps - 1."""
        self.belief = (self.belief * self.alpha
                       + info * 100 * (1 - self.alpha))
        self.information.append(info)

    def trades_history(self, trades, time):
        """A list of everyone's trades, in the following format:
        [(execution_price, 'buy' or 'sell', quantity,
          previous_market_belief), ...]
        Note that this isn't just new trades; it's all of them."""
        if time == 0:
            self.trades = trades
            return
        old_len = len(self.trades)
        self.trades = trades
        new = len(self.trades) - old_len
        if new == 0:
            return
        buy = 0
        sell = 0
        i = 1
        while i < new:
            if self.trades[-i][1] == 'buy':
                buy = buy + self.trades[-i][2]
            else:
                sell = sell + self.trades[-i][2]
            i = i + 1
            
        if buy > sell * 5:
            self.action = 'buy'
        if sell > buy * 5:
            self.action = 'sell'
            
        
    
    def analyze_history(self):
        i = 1
        buy_expect = 0
        buy_count = 0
        sell_expect = 0
        sell_count = 0
        # price is increasing
        while i < min(60, len(self.trades)):
            if self.trades[-i][1] == 'buy':
                buy_expect = buy_expect + self.trades[-i][0] * self.trades[-i][2]
                buy_count = buy_count + self.trades[-i][2]
            else:
                sell_expect = sell_expect + self.trades[-i][0] * self.trades[-i][2]
                sell_count = sell_count + self.trades[-i][2]
            i = i + 1
        if buy_count == 0:
            buy = 0
        else:
            buy = buy_expect/buy_count
            
        if sell_count == 0:
            sell = 100
        else:
            sell = sell_expect/sell_count
        return buy, sell
            
    def trading_opportunity(self, cash_callback, shares_callback,
                            check_callback, execute_callback,
                            market_belief):
        """Called when the bot has an opportunity to trade.
        
        cash_callback(): How much cash the bot has right now.
        shares_callback(): How many shares the bot owns.
        check_callback(buysell, quantity): Returns the per-share
          price of buying or selling the given quantity.
        execute_callback(buysell, quantity): Buy or sell the given
          quantity of shares.
        market_belief: The market maker's current belief.

        Note that a bot can always buy and sell: the bot will borrow
        shares or cash automatically.
        """
        # Place a randomly sized trade in the direction of
        # our last information. What could possibly go wrong?
        if len(self.information) < 10:
            return
        #buy, sell = self.analyze_history()
        block_size = self.start_block_size
        bought = False
        sold = False
        
        p = sum(self.information)*1.0/len(self.information)*100
        alpha = 1.0
        belief = (market_belief * alpha + p)/(1+alpha)
        if belief >= 100.0:
            cash = cash_callback()
            i = 10
            while check_callback('buy', i) <= cash:
                i = i + 10
            execute_callback('buy', i - 10)
            return
        elif belief <= 0.0:
            share = shares_callback()
            execute_callback('sell', share)
            return
        #if check_callback('buy', block_size) > p or check_callback('sell', block_size) < p:
        #        return
        while True:
            #print block_size
            if not sold and check_callback('buy', block_size) < belief - 10:
                print "p = ", belief - 10, " buy ", check_callback('buy', block_size)
                execute_callback('buy', block_size)
                bought = True
            elif not bought and check_callback('sell', block_size) > belief + 10:
                print "p = ", belief + 10, " sell ", check_callback('sell', block_size)
                execute_callback('sell', block_size)
                sold = True
            else:
                if block_size == 2:
                    break
                block_size = block_size // 2
                if block_size < self.min_block_size:
                    block_size = self.min_block_size
                
        #print "market belief = ", market_belief

        
        #print "buy_expect = ", check_callback('buy', quantity)
        #print "sell_expect = ", check_callback('sell', quantity)
        """
        if self.information[-1] == 1:
            execute_callback('buy', quantity)
        elif buy_expect < market_belief:
            execute_callback('buy', quantity)
        elif sell_expect > market_belief:
            execute_callback('sell', quantity)
        """

def main():
    bots = [MyBot()]
    bots.extend(other_bots.get_bots(5,0))
    # Plot a single run. Useful for debugging and visualizing your
    # bot's performance. Also prints the bot's final profit, but this
    # will be very noisy.
    #plot_simulation.run(bots, lmsr_b=250)
    
    # Calculate statistics over many runs. Provides the mean and
    # standard deviation of your bot's profit.
    run_experiments.run(bots, simulations=1000, lmsr_b=250)

# Extra parameters to plot_simulation.run:
#   timesteps=100, lmsr_b=150

# Extra parameters to run_experiments.run:
#   timesteps=100, num_processes=2, simulations=2000, lmsr_b=150

# Descriptions of extra parameters:
# timesteps: The number of trading rounds in each simulation.
# lmsr_b: LMSR's B parameter. Higher means prices change less,
#           and the market maker can lose more money.
# num_processes: In general, set this to the number of cores on your
#                  machine to get maximum performance.
# simulations: The number of simulations to run.

if __name__ == '__main__': # If this file is run directly
    main()
