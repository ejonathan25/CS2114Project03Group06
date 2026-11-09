package Trivia;

/**
 * // -------------------------------------------------------------------------
/**
 *  Formats information about the power-up shop
 *  so it can be displayed to the player.
 * 
 *  @author lenab
 *  @version Nov 8, 2026
 */
public class ShopReader
{

    /**
     * Creates a ShopReader to format shop information.
     */
    public ShopReader()
    {
        // TODO Auto-generated constructor stub
    }
    
    /**
     * Formats the shops items, prices, and stock
     * into text that can be displayed to the player.
     * 
     * @param shop the shop to display
     * @param playerPoints the players current points
     * @return the formatted shop information
     */
    public String formate(Shop shop, int playerPoints)
    {
        String result = "===== POWER-UP SHOP ===== \n";
        result += "Your Points: " + playerPoints + "\n\n";
        
        for (PowerUp p : shop.getItems())
        {
            result += p + " - " + shop.getPrice(p) 
                + " points (" + shop.getStock(p)
                + " left)\n";
            
        }
        
        return result;
    }
 
}
