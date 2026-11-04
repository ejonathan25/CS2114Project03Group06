package Trivia;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
/**
 * // -------------------------------------------------------------------------
/**
 *  Creates the questionList by grabbing from the txt file of questions
 *  and converting them.
 * 
 *  @author ejonathan25
 *  @version Nov 3, 2026
 */
public class QuestionList
{
    //~ Fields ................................................................
    
    private final List<Question> questions;
    
    //~ Constructors ..........................................................
    /**
     * creates the questionList
     */
    public QuestionList()
    {
        questions = new ArrayList<>();
    }

    //~Public  Methods ........................................................

    /**
     * Parses the file, adding valid questions to the list
     * @param f the file of questions
     * @throws FileNotFoundException if the file is missing
     * @throws IllegalStateException if no valid question exists
     */
    public void loadFromFile(File f) throws FileNotFoundException
    {
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
        questions.addAll(loaded);
    }
    
    /**
     * Returns the question for one line
     * if line has issue returns null
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
            int points = Integer.parseInt
                (parts[Question.NUM_CHOICES + 2].trim());
            return new Question(prompt, choices, correctIndex, points);
        }
        catch (IllegalArgumentException e)
        {
            return null;
        }       
    }
    
    /**
     * returns the size of the question list
     * @return int the size of the question list
     */
    public int size()
    {
        return questions.size();
    }
    
    /**
     * adds the questions created to the queue and has them shuffled
     * @return Queue<Question> returns the queue of questions shuffled
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

/**
 * Example of question formatting:
 * 
 * 
 * # prompt|A|B|C|D|correctIndex(0-3)|points
 * What is the capital of France?|Berlin|Paris|Rome|Madrid|1|100
 * Which planet is known as the Red Planet?|Venus|Jupiter|Mars|Mercury|2|100
 */
