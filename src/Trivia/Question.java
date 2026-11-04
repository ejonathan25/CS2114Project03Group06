package Trivia;
import java.util.Arrays;
/**
 * // -------------------------------------------------------------------------
/**
 *  Will create a question object.
 * 
 *  @author ejonathan25
 *  @version Nov 3, 2026
 */
public class Question
{
    //~ Fields ................................................................
    /**
     * the amount of choices possible
     */
    public static final int NUM_CHOICES = 4;
   
    private final String prompt;
    private final String[] choices;
    private final int correctIndex;
    private final int pointValue;
    
    //~ Constructors ..........................................................
    
    /**
     * Creates the question object
     * @param prompt the question prompt
     * @param choices the choices for the question
     * @param correctIndex the index for the correct answer
     * @param pointValue the amount of points the question is worth
     */
    public Question(String prompt, String[] 
        choices, int correctIndex, int pointValue) 
    {
        if (prompt == null || prompt.isBlank()) 
        {
            throw new IllegalArgumentException(
                "Prompt must not be null or blank.");
        }
        if (choices == null || choices.length != NUM_CHOICES) 
        {
            throw new IllegalArgumentException(
                "Exactly " + NUM_CHOICES + 
                " choices are required.");
        }
        for (String c : choices) 
        {
            if (c == null || c.isBlank()) 
            {
                throw new IllegalArgumentException(
                    "Choices must not be null or blank.");
            }
        }
        if (correctIndex < 0 || correctIndex >= NUM_CHOICES) 
        {
            throw new IllegalArgumentException(
                "correctIndex must be between 0 and " + 
                (NUM_CHOICES - 1) + ".");
        }
        if (pointValue <= 0) 
        {
            throw new IllegalArgumentException(
                "pointValue must be positive.");
        }
        this.prompt = prompt;
        this.choices = Arrays.copyOf(choices, choices.length);
        this.correctIndex = correctIndex;
        this.pointValue = pointValue;
    }

    //~Public  Methods ........................................................
    
    /**
     * gets the prompt of the question
     * @return the prompt of the question
     */
    public String getPrompt()
    {
        return prompt;
    }
    
    /**
     * gets an array of the choices
     * @return String[] the array of choices
     */
    public String[] getChoices()
    {
        return Arrays.copyOf(choices, choices.length);
    }
    
    /**
     * gets the amount of points the question is worth
     * @return int the amount of points the question is worth
     */
    public int getPointValue()
    {
        return pointValue;
    }
    
    /**
     * determines if the index given matches the
     * correct index
     * @return boolean returns true if index matches
     * correct index, false if otherwise
     * @param choiceIndex the chosen index to be 
     * compared to the correctIndex
     */
    public boolean isCorrect(int choiceIndex)
    {
        if (choiceIndex < 0 || choiceIndex >= NUM_CHOICES)
        {
            throw new IllegalArgumentException(
                "choiceIndex out of range: " + choiceIndex);
        }
        return choiceIndex == correctIndex;
    }
    
    /**
     * returns the choice that is correct
     * @return String the correct choice
     */
    public String getCorrectAnswer()
    {
        return choices[correctIndex];
    }
}
