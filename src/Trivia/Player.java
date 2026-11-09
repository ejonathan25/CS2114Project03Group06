package Trivia;
 
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
 
/**
 * Model class for the player: points, lives, and power-ups.
 * Has no knowledge of any view or of the Scanner/console.
 *
 * @author Colin Smith
 * @version Nov 8, 2026
 */
public class Player
{.........
 
    private int points;
    private int lives;
    private final EnumMap<PowerUp, Integer> powerUps;
    private final List<ModelListener> listeners;
 
    /**
     * Creates a player with the given number of lives.
     *
     * @param startingLives lives to start with
     * @throws IllegalArgumentException if startingLives is negative
     */
    public Player(int startingLives)
    {
        if (startingLives < 0)
        {
            throw new IllegalArgumentException(
                "startingLives must not be negative.");
        }
        this.points = 0;
        this.lives = startingLives;
        this.powerUps = new EnumMap<>(PowerUp.class);
        for (PowerUp p : PowerUp.values())
        {
            powerUps.put(p, 0);
        }
        this.listeners = new ArrayList<>();
    }
 
    /**
     * @return current points
     */
    public int getPoints()
    {
        return points;
    }
 
    /**
     * @return current lives
     */
    public int getLives()
    {
        return lives;
    }
 
    /**
     * @return true if lives > 0
     */
    public boolean isAlive()
    {
        return lives > 0;
    }
 
    /**
     * Adds points.
     *
     * @param n points to add
     * @throws IllegalArgumentException if n is negative
     */
    public void addPoints(int n)
    {
        if (n < 0)
        {
            throw new IllegalArgumentException("n must not be negative.");
        }
        points += n;
        notifyListeners();
    }
 
    /**
     * Spends points if the player can afford it.
     *
     * @param n points to spend
     * @return false (and changes nothing) if points < n
     * @throws IllegalArgumentException if n is negative
     */
    public boolean spendPoints(int n)
    {
        if (n < 0)
        {
            throw new IllegalArgumentException("n must not be negative.");
        }
        if (points < n)
        {
            return false;
        }
        points -= n;
        notifyListeners();
        return true;
    }
 
    /**
     * Removes one life, never going below 0.
     */
    public void loseLife()
    {
        if (lives > 0)
        {
            lives--;
        }
        notifyListeners();
    }
 
    /**
     * Adds one life.
     */
    public void addLife()
    {
        lives++;
        notifyListeners();
    }
 
    /**
     * Adds one of the given power-up to the inventory.
     *
     * @param p the power-up
     * @throws IllegalArgumentException if p is null
     */
    public void addPowerUp(PowerUp p)
    {
        if (p == null)
        {
            throw new IllegalArgumentException("power-up must not be null.");
        }
        powerUps.put(p, powerUps.get(p) + 1);
        notifyListeners();
    }
 
    /**
     * Uses one of the given power-up if owned.
     *
     * @param p the power-up
     * @return false if the player has none, otherwise true
     * @throws IllegalArgumentException if p is null
     */
    public boolean usePowerUp(PowerUp p)
    {
        if (p == null)
        {
            throw new IllegalArgumentException("power-up must not be null.");
        }
        int count = powerUps.get(p);
        if (count == 0)
        {
            return false;
        }
        powerUps.put(p, count - 1);
        notifyListeners();
        return true;
    }
 
    /**
     * @param p the power-up
     * @return how many of p the player owns
     * @throws IllegalArgumentException if p is null
     */
    public int getPowerUpCount(PowerUp p)
    {
        if (p == null)
        {
            throw new IllegalArgumentException("power-up must not be null.");
        }
        return powerUps.get(p);
    }
 
    /**
     * Registers a listener to be told when the player changes.
     *
     * @param l the listener
     * @throws IllegalArgumentException if l is null
     */
    public void addListener(ModelListener l)
    {
        if (l == null)
        {
            throw new IllegalArgumentException("listener must not be null.");
        }
        listeners.add(l);
    }

    private void notifyListeners()
    {
        for (ModelListener l : listeners)
        {
            l.modelChanged();
        }
    }
}