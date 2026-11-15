package Trivia;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for Question.
 */
public class QuestionTest
{
    private String[] choices;
    private Question q;

    @BeforeEach
    void setUp()
    {
        choices = new String[] {"Red", "Blue", "Green", "Yellow"};
        q = new Question("Color of the sky?", choices, 1, 10);
    }

    // ---- valid construction ----

    @Test
    void gettersReturnConstructorValues()
    {
        assertEquals("Color of the sky?", q.getPrompt());
        assertArrayEquals(choices, q.getChoices());
        assertEquals(10, q.getPointValue());
        assertEquals("Blue", q.getCorrectAnswer());
    }

    @Test
    void correctIndexBoundaries()
    {
        assertEquals("Red", new Question("P", choices, 0, 1).getCorrectAnswer());
        assertEquals("Yellow", new Question("P", choices, 3, 1).getCorrectAnswer());
    }

    @Test
    void minimumPointValue()
    {
        assertEquals(1, new Question("P", choices, 0, 1).getPointValue());
    }

    @Test
    void numChoicesIsFour()
    {
        assertEquals(4, Question.NUM_CHOICES);
    }

    // ---- isCorrect ----

    @Test
    void isCorrectOnlyForCorrectIndex()
    {
        assertFalse(q.isCorrect(0));
        assertTrue(q.isCorrect(1));
        assertFalse(q.isCorrect(2));
        assertFalse(q.isCorrect(3));
    }

    @Test
    void isCorrectOutOfRangeThrows()
    {
        assertThrows(IllegalArgumentException.class, () -> q.isCorrect(-1));
        assertThrows(IllegalArgumentException.class, () -> q.isCorrect(4));
    }

    // ---- defensive copies ----

    @Test
    void changingOriginalArrayDoesNotAffectQuestion()
    {
        choices[1] = "CHANGED";
        assertEquals("Blue", q.getCorrectAnswer());
        assertEquals("Blue", q.getChoices()[1]);
    }

    @Test
    void changingReturnedArrayDoesNotAffectQuestion()
    {
        String[] got = q.getChoices();
        got[1] = "CHANGED";
        assertEquals("Blue", q.getCorrectAnswer());
    }

    @Test
    void getChoicesReturnsNewArrayEachTime()
    {
        assertNotSame(q.getChoices(), q.getChoices());
    }

    // ---- invalid construction ----

    @Test
    void nullPromptThrows()
    {
        assertThrows(IllegalArgumentException.class,
            () -> new Question(null, choices, 0, 10));
    }

    @Test
    void emptyPromptThrows()
    {
        assertThrows(IllegalArgumentException.class,
            () -> new Question("", choices, 0, 10));
    }

    @Test
    void blankPromptThrows()
    {
        assertThrows(IllegalArgumentException.class,
            () -> new Question("   ", choices, 0, 10));
    }

    @Test
    void nullChoicesThrows()
    {
        assertThrows(IllegalArgumentException.class,
            () -> new Question("P", null, 0, 10));
    }

    @Test
    void tooFewChoicesThrows()
    {
        assertThrows(IllegalArgumentException.class,
            () -> new Question("P", new String[] {"a", "b", "c"}, 0, 10));
    }

    @Test
    void tooManyChoicesThrows()
    {
        assertThrows(IllegalArgumentException.class,
            () -> new Question("P", new String[] {"a", "b", "c", "d", "e"}, 0, 10));
    }

    @Test
    void nullChoiceThrows()
    {
        assertThrows(IllegalArgumentException.class,
            () -> new Question("P", new String[] {"a", null, "c", "d"}, 0, 10));
    }

    @Test
    void blankChoiceThrows()
    {
        assertThrows(IllegalArgumentException.class,
            () -> new Question("P", new String[] {"a", "b", " ", "d"}, 0, 10));
    }

    @Test
    void negativeCorrectIndexThrows()
    {
        assertThrows(IllegalArgumentException.class,
            () -> new Question("P", choices, -1, 10));
    }

    @Test
    void correctIndexTooHighThrows()
    {
        assertThrows(IllegalArgumentException.class,
            () -> new Question("P", choices, 4, 10));
    }

    @Test
    void zeroPointsThrows()
    {
        assertThrows(IllegalArgumentException.class,
            () -> new Question("P", choices, 0, 0));
    }

    @Test
    void negativePointsThrows()
    {
        assertThrows(IllegalArgumentException.class,
            () -> new Question("P", choices, 0, -5));
    }
}