package Trivia;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for Shop.
 */
public class ShopTest
{
    private Shop shop;
    private Player player;

    @BeforeEach
    void setUp()
    {
        shop = new Shop();
        player = new Player(3);
    }

    // ---- prices / stock / items ----

    @Test
    void startingPrices()
    {
        assertEquals(30, shop.getPrice(PowerUp.SKIP));
        assertEquals(50, shop.getPrice(PowerUp.EXTRA_LIFE));
        assertEquals(40, shop.getPrice(PowerUp.DOUBLE));
    }

    @Test
    void startingStock()
    {
        assertEquals(5, shop.getStock(PowerUp.SKIP));
        assertEquals(3, shop.getStock(PowerUp.EXTRA_LIFE));
        assertEquals(4, shop.getStock(PowerUp.DOUBLE));
    }

    @Test
    void nullLookupsThrow()
    {
        assertThrows(IllegalArgumentException.class, () -> shop.getPrice(null));
        assertThrows(IllegalArgumentException.class, () -> shop.getStock(null));
    }

    @Test
    void itemsInEnumOrder()
    {
        assertEquals(List.of(PowerUp.SKIP, PowerUp.EXTRA_LIFE, PowerUp.DOUBLE),
            shop.getItems());
    }

    @Test
    void getItemsReturnsCopy()
    {
        List<PowerUp> items = shop.getItems();
        items.clear();
        assertEquals(3, shop.getItems().size());
    }

    // ---- purchase ----

    @Test
    void purchaseSuccess()
    {
        player.addPoints(100);
        assertEquals(PurchaseResult.OK, shop.purchase(player, PowerUp.SKIP));
        assertEquals(70, player.getPoints());
        assertEquals(1, player.getPowerUpCount(PowerUp.SKIP));
        assertEquals(4, shop.getStock(PowerUp.SKIP));
    }

    @Test
    void purchaseWithExactPoints()
    {
        player.addPoints(50);
        assertEquals(PurchaseResult.OK, shop.purchase(player, PowerUp.EXTRA_LIFE));
        assertEquals(0, player.getPoints());
    }

    @Test
    void purchaseDoesNotUsePowerUp()
    {
        player.addPoints(50);
        shop.purchase(player, PowerUp.EXTRA_LIFE);
        assertEquals(3, player.getLives(), "Buying should not apply the effect");
    }

    @Test
    void notEnoughPoints()
    {
        player.addPoints(39);
        assertEquals(PurchaseResult.NOT_ENOUGH_POINTS,
            shop.purchase(player, PowerUp.DOUBLE));
        assertEquals(39, player.getPoints());
        assertEquals(0, player.getPowerUpCount(PowerUp.DOUBLE));
        assertEquals(4, shop.getStock(PowerUp.DOUBLE));
    }

    @Test
    void zeroPointsNotEnough()
    {
        assertEquals(PurchaseResult.NOT_ENOUGH_POINTS,
            shop.purchase(player, PowerUp.SKIP));
    }

    @Test
    void outOfStockAfterBuyingAll()
    {
        player.addPoints(1000);
        for (int i = 0; i < 3; i++)
        {
            assertEquals(PurchaseResult.OK, shop.purchase(player, PowerUp.EXTRA_LIFE));
        }
        assertEquals(0, shop.getStock(PowerUp.EXTRA_LIFE));

        int pointsBefore = player.getPoints();
        assertEquals(PurchaseResult.OUT_OF_STOCK,
            shop.purchase(player, PowerUp.EXTRA_LIFE));
        assertEquals(pointsBefore, player.getPoints());
        assertEquals(3, player.getPowerUpCount(PowerUp.EXTRA_LIFE));
    }

    @Test
    void outOfStockCheckedBeforePoints()
    {
        Player rich = new Player(3);
        rich.addPoints(1000);
        for (int i = 0; i < 5; i++)
        {
            shop.purchase(rich, PowerUp.SKIP);
        }
        // A broke player should be told "out of stock", not "not enough points".
        assertEquals(PurchaseResult.OUT_OF_STOCK, shop.purchase(player, PowerUp.SKIP));
    }

    @Test
    void stockIsPerItem()
    {
        player.addPoints(1000);
        shop.purchase(player, PowerUp.SKIP);
        assertEquals(3, shop.getStock(PowerUp.EXTRA_LIFE));
        assertEquals(4, shop.getStock(PowerUp.DOUBLE));
    }

    @Test
    void stockIsPerShop()
    {
        player.addPoints(1000);
        shop.purchase(player, PowerUp.SKIP);
        assertEquals(5, new Shop().getStock(PowerUp.SKIP));
    }

    @Test
    void buyEverything()
    {
        player.addPoints(5 * 30 + 3 * 50 + 4 * 40); // 460
        for (PowerUp p : shop.getItems())
        {
            while (shop.purchase(player, p) == PurchaseResult.OK)
            {
                // keep buying
            }
        }
        assertEquals(0, player.getPoints());
        assertEquals(5, player.getPowerUpCount(PowerUp.SKIP));
        assertEquals(3, player.getPowerUpCount(PowerUp.EXTRA_LIFE));
        assertEquals(4, player.getPowerUpCount(PowerUp.DOUBLE));
    }

    @Test
    void nullArgumentsThrow()
    {
        assertThrows(IllegalArgumentException.class, () -> shop.purchase(null, PowerUp.SKIP));
        assertThrows(IllegalArgumentException.class, () -> shop.purchase(player, null));
    }
}
