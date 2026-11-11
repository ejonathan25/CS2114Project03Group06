
package Trivia;

/**
 * Formats trivia questions and their answer choices into
 * text that can be displayed to the player.
 *
 * @author lenab
 * @version Nov 8, 2026
 */
public class QuestionReader
{
    /**
     * Creates a QuestionReader.
     */
    public QuestionReader()
    {
        // No initialization needed.
    }

    /**
     * Formats a question and its answer choices.
     *
     * @param q the question to display
     * @param number the question number
     * @return the formatted question and choices
     * @throws IllegalArgumentException if q is null
     */
    public String format(Question q, int number)
    {
        if (q == null)
        {
            throw new IllegalArgumentException(
                "Question must not be null.");
        }

        StringBuilder result = new StringBuilder();
        result.append("Question ")
            .append(number)
            .append(": ")
            .append(q.getPrompt())
            .append("\n");

        String[] choices = q.getChoices();

        for (int i = 0; i < choices.length; i++)
        {
            result.append((char) ('A' + i))
                .append(". ")
                .append(choices[i])
                .append("\n");
        }

        return result.toString();
    }

    /**
     * Formats the player's current game status.
     *
     * @param points the player's current points
     * @param lives the player's remaining lives
     * @return the formatted game status
     */
    public String formatStatus(int points, int lives)
    {
        return "Points: " + points + " | Lives: " + lives;
    }

    /**
     * Formats the player's status with the question number.
     *
     * @param points the player's current points
     * @param lives the player's remaining lives
     * @param questionNumber the current question number
     * @return the formatted game status
     */
    public String formatStatus(
        int points, int lives, int questionNumber)
    {
        return formatStatus(points, lives)
            + " | Question: " + questionNumber;
    }

    /**
     * Formats the result of the player's answer.
     *
     * @param correct whether the answer was correct
     * @param correctAnswer the correct answer
     * @return the formatted answer result
     */
    public String formatResult(
        boolean correct, String correctAnswer)
    {
        if (correct)
        {
            return "Correct!";
        }

        return "Incorrect! The correct answer is: "
            + correctAnswer;
    }
}
