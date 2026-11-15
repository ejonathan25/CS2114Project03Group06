package Trivia;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for QuestionList (file parsing and queue creation).
 * File format: prompt|choice1|choice2|choice3|choice4|correctIndex|points
 */
public class QuestionListTest
{
    private QuestionList list;

    @BeforeEach
    void setUp()
    {
        list = new QuestionList();
    }

    private static File file(String... lines) throws IOException
    {
        Path f = Files.createTempFile("qlist", ".txt");
        f.toFile().deleteOnExit();
        Files.write(f, List.of(lines), StandardCharsets.UTF_8);
        return f.toFile();
    }

    private static final String GOOD_1 = "2+2?|3|4|5|6|1|10";
    private static final String GOOD_2 = "Sky color?|Red|Blue|Green|Pink|1|20";
    private static final String GOOD_3 = "Largest planet?|Mars|Earth|Jupiter|Venus|2|30";

    @Test
    void newListIsEmpty()
    {
        assertEquals(0, list.size());
        assertTrue(list.toQueue().isEmpty());
    }

    @Test
    void loadsValidLines() throws IOException
    {
        list.loadFromFile(file(GOOD_1, GOOD_2, GOOD_3));
        assertEquals(3, list.size());
    }

    @Test
    void parsesFieldsCorrectly() throws IOException
    {
        list.loadFromFile(file(GOOD_1));
        Question q = list.toQueue().peek();
        assertEquals("2+2?", q.getPrompt());
        assertArrayEquals(new String[] {"3", "4", "5", "6"}, q.getChoices());
        assertTrue(q.isCorrect(1));
        assertEquals("4", q.getCorrectAnswer());
        assertEquals(10, q.getPointValue());
    }

    @Test
    void trimsWhitespaceAroundFields() throws IOException
    {
        list.loadFromFile(file("  2+2?  |  3 | 4 |5| 6 | 1 | 10  "));
        Question q = list.toQueue().peek();
        assertEquals("2+2?", q.getPrompt());
        assertEquals("4", q.getCorrectAnswer());
        assertEquals(10, q.getPointValue());
    }

    @Test
    void skipsBlankAndCommentLines() throws IOException
    {
        list.loadFromFile(file("# header", "", "   ", GOOD_1, "  # indented comment", GOOD_2));
        assertEquals(2, list.size());
    }

    @Test
    void skipsInvalidLinesButKeepsValidOnes() throws IOException
    {
        list.loadFromFile(file(
            GOOD_1,
            "too|few|fields",
            "A?|a|b|c|d|e|1|10",       // too many fields
            "A?|a|b|c|d|x|10",         // non-numeric index
            "A?|a|b|c|d|1|ten",        // non-numeric points
            "A?|a|b|c|d|4|10",         // index out of range
            "A?|a|b|c|d|-1|10",        // negative index
            "A?|a|b|c|d|1|0",          // zero points
            "A?|a|b|c|d|1|-5",         // negative points
            "|a|b|c|d|1|10",           // blank prompt
            "A?|a||c|d|1|10",          // blank choice
            "A?|a|b|c|d|1.5|10",       // decimal index
            GOOD_2));
        assertEquals(2, list.size());
    }

    @Test
    void trailingPipeIsInvalid() throws IOException
    {
        // split with -1 keeps the trailing empty field, so this has 8 parts
        list.loadFromFile(file(GOOD_1, GOOD_2 + "|"));
        assertEquals(1, list.size());
    }

    @Test
    void nullFileThrows()
    {
        assertThrows(IllegalArgumentException.class, () -> list.loadFromFile(null));
    }

    @Test
    void missingFileThrows()
    {
        assertThrows(FileNotFoundException.class,
            () -> list.loadFromFile(new File("no_such_file_98765.txt")));
    }

    @Test
    void emptyFileThrowsIllegalState() throws IOException
    {
        File f = file();
        IllegalStateException e = assertThrows(IllegalStateException.class,
            () -> list.loadFromFile(f));
        assertTrue(e.getMessage().contains(f.getName()));
    }

    @Test
    void allInvalidLinesThrowsIllegalState() throws IOException
    {
        File f = file("# comment", "bad", "A?|a|b|c|d|9|10");
        assertThrows(IllegalStateException.class, () -> list.loadFromFile(f));
    }

    @Test
    void reloadingReplacesPreviousQuestions() throws IOException
    {
        list.loadFromFile(file(GOOD_1, GOOD_2, GOOD_3));
        list.loadFromFile(file(GOOD_1));
        assertEquals(1, list.size());
    }

    @Test
    void failedReloadKeepsPreviousQuestions() throws IOException
    {
        list.loadFromFile(file(GOOD_1, GOOD_2));
        File bad = file("nothing valid");
        assertThrows(IllegalStateException.class, () -> list.loadFromFile(bad));
        assertEquals(2, list.size());
    }

    @Test
    void toQueueContainsEveryQuestionOnce() throws IOException
    {
        list.loadFromFile(file(GOOD_1, GOOD_2, GOOD_3));
        Queue<Question> q = list.toQueue();
        assertEquals(3, q.size());

        Set<String> prompts = new HashSet<>();
        while (!q.isEmpty())
        {
            prompts.add(q.dequeue().getPrompt());
        }
        assertEquals(Set.of("2+2?", "Sky color?", "Largest planet?"), prompts);
    }

    @Test
    void toQueueReturnsIndependentQueues() throws IOException
    {
        list.loadFromFile(file(GOOD_1, GOOD_2));
        Queue<Question> a = list.toQueue();
        a.dequeue();
        a.dequeue();
        assertEquals(2, list.toQueue().size());
        assertEquals(2, list.size());
    }

    @Test
    void toQueueShuffles() throws IOException
    {
        String[] lines = new String[10];
        for (int i = 0; i < 10; i++)
        {
            lines[i] = "Q" + i + "|a|b|c|d|0|1";
        }
        list.loadFromFile(file(lines));

        // With 10 items, 20 shuffles all giving file order is ~1 in 10^130.
        boolean sawDifferentOrder = false;
        for (int t = 0; t < 20 && !sawDifferentOrder; t++)
        {
            Queue<Question> q = list.toQueue();
            for (int i = 0; i < 10; i++)
            {
                if (!q.dequeue().getPrompt().equals("Q" + i))
                {
                    sawDifferentOrder = true;
                    break;
                }
            }
        }
        assertTrue(sawDifferentOrder, "toQueue should shuffle the questions");
    }
}
