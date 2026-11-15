package Trivia;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for Player.
 */
public class PlayerTest
{
    private Player p;
    private int notifications;

    @BeforeEach
    void setUp()
    {
        p = new Player(3);
        notifications = 0;
        p.addListener(() -> notifications++);
    }

    // ---- constructor ----

    @Test
    void startsWithGivenLivesAndNoPoints()
    {
        assertEquals(3, p.getLives());
        assertEquals(0, p.getPoints());
        assertTrue(p.isAlive());
    }

    @Test
    void startsWithNoPowerUps()
    {
        for (PowerUp pu : PowerUp.values())
        {
            assertEquals(0, p.getPowerUpCount(pu));
        }
    }

    @Test
    void zeroLivesAllowedButNotAlive()
    {
        Player dead = new Player(0);
        assertEquals(0, dead.getLives());
        assertFalse(dead.isAlive());
    }

    @Test
    void negativeLivesThrows()
    {
        assertThrows(IllegalArgumentException.class, () -> new Player(-1));
    }

    // ---- points ----

    @Test
    void addPoints()
    {
        p.addPoints(10);
        p.addPoints(5);
        assertEquals(15, p.getPoints());
    }

    @Test
    void addZeroPoints()
    {
        p.addPoints(0);
        assertEquals(0, p.getPoints());
    }

    @Test
    void addNegativePointsThrows()
    {
        assertThrows(IllegalArgumentException.class, () -> p.addPoints(-1));
        assertEquals(0, p.getPoints());
    }

    @Test
    void spendPointsSuccess()
    {
        p.addPoints(50);
        assertTrue(p.spendPoints(20));
        assertEquals(30, p.getPoints());
    }

    @Test
    void spendExactBalance()
    {
        p.addPoints(50);
        assertTrue(p.spendPoints(50));
        assertEquals(0, p.getPoints());
    }

    @Test
    void spendMoreThanBalanceFailsAndChangesNothing()
    {
        p.addPoints(10);
        assertFalse(p.spendPoints(11));
        assertEquals(10, p.getPoints());
    }

    @Test
    void spendZero()
    {
        assertTrue(p.spendPoints(0));
        assertEquals(0, p.getPoints());
    }

    @Test
    void spendNegativeThrows()
    {
        assertThrows(IllegalArgumentException.class, () -> p.spendPoints(-5));
    }

    // ---- lives ----

    @Test
    void loseLife()
    {
        p.loseLife();
        assertEquals(2, p.getLives());
        assertTrue(p.isAlive());
    }

    @Test
    void loseAllLives()
    {
        p.loseLife();
        p.loseLife();
        p.loseLife();
        assertEquals(0, p.getLives());
        assertFalse(p.isAlive());
    }

    @Test
    void livesNeverGoNegative()
    {
        for (int i = 0; i < 10; i++)
        {
            p.loseLife();
        }
        assertEquals(0, p.getLives());
    }

    @Test
    void addLife()
    {
        p.addLife();
        assertEquals(4, p.getLives());
    }

    @Test
    void addLifeRevivesDeadPlayer()
    {
        Player dead = new Player(0);
        dead.addLife();
        assertTrue(dead.isAlive());
    }

    // ---- power-ups ----

    @Test
    void addAndCountPowerUps()
    {
        p.addPowerUp(PowerUp.SKIP);
        p.addPowerUp(PowerUp.SKIP);
        p.addPowerUp(PowerUp.DOUBLE);
        assertEquals(2, p.getPowerUpCount(PowerUp.SKIP));
        assertEquals(1, p.getPowerUpCount(PowerUp.DOUBLE));
        assertEquals(0, p.getPowerUpCount(PowerUp.EXTRA_LIFE));
    }

    @Test
    void usePowerUpDecrements()
    {
        p.addPowerUp(PowerUp.SKIP);
        p.addPowerUp(PowerUp.SKIP);
        assertTrue(p.usePowerUp(PowerUp.SKIP));
        assertEquals(1, p.getPowerUpCount(PowerUp.SKIP));
    }

    @Test
    void useUnownedPowerUpFails()
    {
        assertFalse(p.usePowerUp(PowerUp.EXTRA_LIFE));
        assertEquals(0, p.getPowerUpCount(PowerUp.EXTRA_LIFE));
    }

    @Test
    void useLastPowerUpThenFails()
    {
        p.addPowerUp(PowerUp.DOUBLE);
        assertTrue(p.usePowerUp(PowerUp.DOUBLE));
        assertFalse(p.usePowerUp(PowerUp.DOUBLE));
        assertEquals(0, p.getPowerUpCount(PowerUp.DOUBLE));
    }

    @Test
    void usingOneTypeDoesNotAffectOthers()
    {
        p.addPowerUp(PowerUp.SKIP);
        p.addPowerUp(PowerUp.DOUBLE);
        p.usePowerUp(PowerUp.SKIP);
        assertEquals(1, p.getPowerUpCount(PowerUp.DOUBLE));
    }

    @Test
    void nullPowerUpThrowsEverywhere()
    {
        assertThrows(IllegalArgumentException.class, () -> p.addPowerUp(null));
        assertThrows(IllegalArgumentException.class, () -> p.usePowerUp(null));
        assertThrows(IllegalArgumentException.class, () -> p.getPowerUpCount(null));
    }

    // ---- listeners ----

    @Test
    void nullListenerThrows()
    {
        assertThrows(IllegalArgumentException.class, () -> p.addListener(null));
    }

    @Test
    void listenerNotifiedOnEveryChange()
    {
        p.addPoints(100);           // 1
        p.spendPoints(10);          // 2
        p.loseLife();               // 3
        p.addLife();                // 4
        p.addPowerUp(PowerUp.SKIP); // 5
        p.usePowerUp(PowerUp.SKIP); // 6
        assertEquals(6, notifications);
    }

    @Test
    void failedSpendDoesNotNotify()
    {
        p.spendPoints(5);
        assertEquals(0, notifications);
    }

    @Test
    void failedUsePowerUpDoesNotNotify()
    {
        p.usePowerUp(PowerUp.SKIP);
        assertEquals(0, notifications);
    }

    @Test
    void invalidArgumentsDoNotNotify()
    {
        assertThrows(IllegalArgumentException.class, () -> p.addPoints(-1));
        assertThrows(IllegalArgumentException.class, () -> p.spendPoints(-1));
        assertEquals(0, notifications);
    }

    @Test
    void multipleListenersAllNotified()
    {
        int[] second = {0};
        p.addListener(() -> second[0]++);
        p.addPoints(1);
        assertEquals(1, notifications);
        assertEquals(1, second[0]);
    }

    @Test
    void loseLifeAtZeroStillNotifies()
    {
        // Documents current behavior: notifies even though nothing changed.
        Player dead = new Player(0);
        int[] count = {0};
        dead.addListener(() -> count[0]++);
        dead.loseLife();
        assertEquals(1, count[0]);
    }
}
