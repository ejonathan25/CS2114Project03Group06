package Trivia;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for QuestionReader text formatting.
 */
public class QuestionReaderTest
{
    private QuestionReader reader;
    private Question q;

    @BeforeEach
    void setUp()
    {
        reader = new QuestionReader();
        q = new Question("2+2?", new String[] {"3", "4", "5", "6"}, 1, 10);
    }

    @Test
    void formatExactOutput()
    {
        String expected =
            "Question 1: 2+2?\n"
            + "1. 3\n"
            + "2. 4\n"
            + "3. 5\n"
            + "4. 6\n";
        assertEquals(expected, reader.format(q, 1));
    }

    @Test
    void formatUsesGivenNumber()
    {
        assertTrue(reader.format(q, 42).startsWith("Question 42: 2+2?"));
    }

    @Test
    void formatAllowsZeroAndNegativeNumbers()
    {
        assertTrue(reader.format(q, 0).startsWith("Question 0:"));
        assertTrue(reader.format(q, -1).startsWith("Question -1:"));
    }

    @Test
    void formatDoesNotRevealAnswer()
    {
        assertFalse(reader.format(q, 1).toLowerCase().contains("correct"));
    }

    @Test
    void formatNullThrows()
    {
        assertThrows(IllegalArgumentException.class, () -> reader.format(null, 1));
    }

    @Test
    void formatStatusTwoArgs()
    {
        assertEquals("Points: 50 | Lives: 2", reader.formatStatus(50, 2));
    }

    @Test
    void formatStatusThreeArgs()
    {
        assertEquals("Points: 50 | Lives: 2 | Question: 7",
            reader.formatStatus(50, 2, 7));
    }

    @Test
    void formatStatusZeros()
    {
        assertEquals("Points: 0 | Lives: 0 | Question: 0",
            reader.formatStatus(0, 0, 0));
    }

    @Test
    void formatResultCorrect()
    {
        assertEquals("Correct!", reader.formatResult(true, "4"));
    }

    @Test
    void formatResultIncorrect()
    {
        assertEquals("Incorrect! The correct answer is: 4",
            reader.formatResult(false, "4"));
    }

    @Test
    void formatResultCorrectIgnoresAnswer()
    {
        assertEquals("Correct!", reader.formatResult(true, null));
    }

    @Test
    void formatResultIncorrectWithNullAnswer()
    {
        // Documents current behavior: no null check, prints "null".
        assertEquals("Incorrect! The correct answer is: null",
            reader.formatResult(false, null));
    }
}
