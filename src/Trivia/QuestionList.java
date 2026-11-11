
package Trivia;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * Creates the question list by reading questions
 * from a text file and converting them into Question objects.
 *
 * @author ejonathan25
 * @version Nov 3, 2026
 */
public class QuestionList
{
    private final List<Question> questions;

    /**
     * Creates an empty question list.
     */
    public QuestionList()
    {
        questions = new ArrayList<>();
    }

    /**
     * Parses the file, adding valid questions to the list.
     *
     * @param f the file of questions
     * @throws FileNotFoundException if the file is missing
     * @throws IllegalStateException if no valid question exists
     * @throws IllegalArgumentException if the file is null
     */
    public void loadFromFile(File f) throws FileNotFoundException
    {
        if (f == null)
        {
            throw new IllegalArgumentException(
                "File must not be null.");
        }

        List<Question> loaded = new ArrayList<>();

        try (Scanner sc = new Scanner(f))
        {
            while (sc.hasNextLine())
            {
                String line = sc.nextLine().trim();

                if (line.isEmpty() || line.startsWith("#"))
                {
                    continue;
                }

                Question q = parseLine(line);

                if (q != null)
                {
                    loaded.add(q);
                }
            }
        }

        if (loaded.isEmpty())
        {
            throw new IllegalStateException(
                "No valid question found in " + f.getName());
        }

        questions.clear();
        questions.addAll(loaded);
    }

    /**
     * Parses one line into a Question.
     * Returns null if the line is invalid.
     *
     * @param line the line to parse
     * @return the Question, or null if invalid
     */
    private static Question parseLine(String line)
    {
        String[] parts = line.split("\\|", -1);

        if (parts.length != Question.NUM_CHOICES + 3)
        {
            return null;
        }

        try
        {
            String prompt = parts[0].trim();

            String[] choices = new String[Question.NUM_CHOICES];

            for (int i = 0; i < Question.NUM_CHOICES; i++)
            {
                choices[i] = parts[i + 1].trim();
            }

            int correctIndex = Integer.parseInt(
                parts[Question.NUM_CHOICES + 1].trim());

            int points = Integer.parseInt(
                parts[Question.NUM_CHOICES + 2].trim());

            return new Question(
                prompt, choices, correctIndex, points);
        }
        catch (IllegalArgumentException e)
        {
            return null;
        }
    }

    /**
     * Returns the size of the question list.
     *
     * @return the number of questions
     */
    public int size()
    {
        return questions.size();
    }

    /**
     * Creates a shuffled queue containing the questions.
     *
     * @return a queue of shuffled questions
     */
    public Queue<Question> toQueue()
    {
        List<Question> shuffled = new ArrayList<>(questions);

        Collections.shuffle(shuffled);

        Queue<Question> queue = new Queue<>();

        for (Question q : shuffled)
        {
            queue.enqueue(q);
        }

        return queue;
    }
}
