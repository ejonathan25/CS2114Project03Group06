package Trivia;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for ShopReader text formatting.
 */
public class ShopReaderTest
{
    private ShopReader reader;
    private Shop shop;

    @BeforeEach
    void setUp()
    {
        reader = new ShopReader();
        shop = new Shop();
    }

    @Test
    void formatExactOutput()
    {
        String expected =
            "===== POWER-UP SHOP =====\n"
            + "Your Points: 25\n"
            + "\n"
            + "SKIP - 30 points (5 left)\n"
            + "EXTRA_LIFE - 50 points (3 left)\n"
            + "DOUBLE - 40 points (4 left)\n";
        assertEquals(expected, reader.format(shop, 25));
    }

    @Test
    void formatReflectsStockChanges()
    {
        Player p = new Player(3);
        p.addPoints(100);
        shop.purchase(p, PowerUp.SKIP);
        shop.purchase(p, PowerUp.SKIP);

        String out = reader.format(shop, p.getPoints());
        assertTrue(out.contains("Your Points: 40"));
        assertTrue(out.contains("SKIP - 30 points (3 left)"));
    }

    @Test
    void formatShowsZeroStock()
    {
        Player p = new Player(3);
        p.addPoints(1000);
        for (int i = 0; i < 3; i++)
        {
            shop.purchase(p, PowerUp.EXTRA_LIFE);
        }
        assertTrue(reader.format(shop, 0).contains("EXTRA_LIFE - 50 points (0 left)"));
    }

    @Test
    void formatNullShopThrows()
    {
        assertThrows(IllegalArgumentException.class, () -> reader.format(null, 0));
    }
}
