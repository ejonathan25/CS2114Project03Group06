package Trivia;

/**
 * // -------------------------------------------------------------------------
/**
 *  Formats trivia questions and their answer choices into
 *  text that can be displayed to the player.
 * 
 *  @author lenab
 *  @version Nov 8, 2026
 */
public class QuestionReader
{

    /**
     * Creates a QuestionReader to format question information.
     */
    public QuestionReader()
    {
        // TODO Auto-generated constructor stub
    }
    
    /**
     * Formats a question and its answer choices
     * into text that can be displayed to the player.
     * @param q the question to display
     * @param number the question number
     * @return the formatted question and choices
     */
    public String format(Question q, int number)
    {
        String result = "Question " + number + ": " + q.getPrompt() + "\n";
        
        String[] choices = q.getChoices();
        
        for (int i = 0; i < choices.length; i++)
        {
            result += (char)('A' + i) + ". " + choices[i] + "\n";
        }
        
        return result;
    }

}
