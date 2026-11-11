
package Trivia;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

/**
 * Represents the power-up shop in the trivia game.
 * Stores the prices and stock of power-ups and handles
 * player purchases.
 *
 * @author lenab
 * @version Nov 8, 2026
 */
public class Shop
{
    private final EnumMap<PowerUp, Integer> prices;
    private final EnumMap<PowerUp, Integer> stock;

    /**
     * Creates a shop with the prices and starting
     * stock for each available power-up.
     */
    public Shop()
    {
        prices = new EnumMap<>(PowerUp.class);
        stock = new EnumMap<>(PowerUp.class);

        prices.put(PowerUp.SKIP, 30);
        prices.put(PowerUp.EXTRA_LIFE, 50);
        prices.put(PowerUp.DOUBLE, 40);

        stock.put(PowerUp.SKIP, 5);
        stock.put(PowerUp.EXTRA_LIFE, 3);
        stock.put(PowerUp.DOUBLE, 4);
    }

    /**
     * Returns the price of a specific power-up.
     *
     * @param p the power-up to look up
     * @return the price of the power-up
     * @throws IllegalArgumentException if p is null
     */
    public int getPrice(PowerUp p)
    {
        if (p == null || !prices.containsKey(p))
        {
            throw new IllegalArgumentException(
                "Invalid power-up.");
        }

        return prices.get(p);
    }

    /**
     * Returns the number of a specific power-up
     * currently available in the shop.
     *
     * @param p the power-up to look up
     * @return the available stock
     * @throws IllegalArgumentException if p is null
     */
    public int getStock(PowerUp p)
    {
        if (p == null || !stock.containsKey(p))
        {
            throw new IllegalArgumentException(
                "Invalid power-up.");
        }

        return stock.get(p);
    }

    /**
     * Returns a list of all power-ups sold in the shop.
     *
     * @return a list of available power-up types
     */
    public List<PowerUp> getItems()
    {
        return new ArrayList<>(prices.keySet());
    }

    /**
     * Processes a player's power-up purchase.
     * Checks stock and points before completing the purchase.
     *
     * @param pl the player making the purchase
     * @param p the power-up being purchased
     * @return the result of the purchase attempt
     * @throws IllegalArgumentException if pl or p is null
     */
    public PurchaseResult purchase(Player pl, PowerUp p)
    {
        if (pl == null || p == null || !prices.containsKey(p))
        {
            throw new IllegalArgumentException(
                "Invalid purchase.");
        }

        if (stock.get(p) <= 0)
        {
            return PurchaseResult.OUT_OF_STOCK;
        }

        int price = prices.get(p);

        if (pl.getPoints() < price)
        {
            return PurchaseResult.NOT_ENOUGH_POINTS;
        }

        pl.spendPoints(price);
        pl.addPowerUp(p);
        stock.put(p, stock.get(p) - 1);

        return PurchaseResult.OK;
    }
}
