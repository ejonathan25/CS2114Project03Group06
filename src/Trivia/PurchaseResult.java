package Trivia;


/**
 * // -------------------------------------------------------------------------
/**
 *  Represents the possible results of a power-up purchase
 *  in the trivia game
 * 
 *  @author lenab
 *  @version Nov 8, 2026
 */
public enum PurchaseResult
{
    /**
     * The purchase was successful.
     */
    OK, 
    
    /**
     * The player does not have enough points
     * to purchase the power-up.
     */
    NOT_ENOUGH_POINTS, 
    
    /**
     * The power-up is out of stock.
     */
    OUT_OF_STOCK

}
