
package Trivia;

/**
 * Formats information about the power-up shop
 * so it can be displayed to the player.
 *
 * @author lenab
 * @version Nov 8, 2026
 */
public class ShopReader
{
    /**
     * Creates a ShopReader.
     */
    public ShopReader()
    {
        // No initialization needed.
    }

    /**
     * Formats the shop's items, prices, and stock.
     *
     * @param shop the shop to display
     * @param playerPoints the player's current points
     * @return the formatted shop information
     * @throws IllegalArgumentException if shop is null
     */
    public String format(Shop shop, int playerPoints)
    {
        if (shop == null)
        {
            throw new IllegalArgumentException(
                "Shop must not be null.");
        }

        StringBuilder result = new StringBuilder();

        result.append("===== POWER-UP SHOP =====\n");
        result.append("Your Points: ")
            .append(playerPoints)
            .append("\n\n");

        for (PowerUp p : shop.getItems())
        {
            result.append(p)
                .append(" - ")
                .append(shop.getPrice(p))
                .append(" points (")
                .append(shop.getStock(p))
                .append(" left)\n");
        }

        return result.toString();
    }
}
