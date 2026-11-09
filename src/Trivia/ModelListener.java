package Trivia;
 
/**
 * Implemented by anything that wants to hear when the model changes.
 *
 * @author Colin Smith
 * @version Nov 8, 2026
 */
public interface ModelListener
{
    /**
     * Called after the model's state changes.
     */
    void modelChanged();
}