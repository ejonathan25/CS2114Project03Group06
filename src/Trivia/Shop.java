package Trivia;
import java.util.EnumMap;
import java.util.ArrayList;
import java.util.List;

/**
 * // -------------------------------------------------------------------------
/**
 *  Represents the power-up show in the trivia game.
 *  Stores the prices and stock of power-ups and handles
 *  player purchases.
 * 
 *  @author lenab
 *  @version Nov 8, 2026
 */
public class Shop
{

    /**
     * Creates a shop with the prices and starting
     * stock for each available power-up.
     */
    public Shop()
    {
        prices = new EnumMap<>(PowerUp.class);
        stock = new EnumMap<> (PowerUp.class);
        
        prices.put(PowerUp.SKIP, 30);
        prices.put(PowerUp.EXTRA_LIFE, 50);
        prices.put(PowerUp.DOUBLE, 40);
        
        stock.put(PowerUp.SKIP, 5);
        stock.put(PowerUp.EXTRA_LIFE, 3);
        stock.put(PowerUp.DOUBLE, 4);
    }
    //~ Fields ................................................................

    private EnumMap<PowerUp, Integer> prices;
    private EnumMap<PowerUp, Integer> stock;
    
    //~ Constructors ..........................................................

    
    
    //~Public  Methods ........................................................

    /**
     * Returns the price of a specific power-up.
     * @param p the power-up to look up
     * @return the price of the power-up
     */    
    public int getPrice(PowerUp p)
    {
        return prices.get(p);
    }
    
    /**
     * Returns the number of a specific power-up
     * currently available in the shop
     */
    public int getStock(PowerUp p)
    {
        return stock.get(p);
    }
    
    /**
     * Returns a list of all power-ups sold in the shop.
     */
    public List<PowerUp> getItems()
    {
        return new ArrayList<>(prices.keySet());
    }
    
    /**
     * Processes a players power-up purchase.
     * Checks stock and points before completing the purchase.
     * @param pl the player making the purchase
     * @param p the power-up being purchased
     * @return the result of the purchase attempt
     */
    public PurchaseResult purchase(Player pl, PowerUp p)
    {
        if (pl == null || p == null || !prices.containsKey(p))
        {
            throw new IllegalArgumentException("Invalid purchase.");
        }
        
        if (stock.get(p) <= 0)
        {
            return PurchaseResult.OUT_OF_STOCK;
        }
        
        int price = prices.get(p);
        
        if (!pl.spendPoints(price))
        {
            return PurchaseResult.NOT_ENOUGH_POINTS;
        }
        
        stock.put(p, stock.get(p) - 1);
        pl.addPowerUp(p);
        
        return PurchaseResult.OK;
    }
}
