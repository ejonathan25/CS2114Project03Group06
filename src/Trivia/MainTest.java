package Trivia;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * JUnit 5 tests for the Trivia Main controller.
 *
 * NOTE: Main creates its Scanner on System.in in the constructor,
 * so every test that feeds input must call newGame(input), which
 * swaps System.in BEFORE constructing Main.
 */
public class MainTest
{
    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream out;

    @BeforeEach
    void captureOutput()
    {
        out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out, true, StandardCharsets.UTF_8));
    }

    @AfterEach
    void restoreStreams()
    {
        System.setIn(originalIn);
        System.setOut(originalOut);
    }

    // ------------------------------------------------------------
    // Helpers
    // ------------------------------------------------------------

    private Main newGame(String input)
    {
        System.setIn(new ByteArrayInputStream(
            input.getBytes(StandardCharsets.UTF_8)));
        return new Main();
    }

    private Main newGame()
    {
        return newGame("");
    }

    private String output()
    {
        return out.toString(StandardCharsets.UTF_8);
    }

    /**
     * Builds a question whose correct answer is choice index 0
     * (i.e. the user types "1").
     */
    private Question makeQuestion(String text, int points)
    {
        return new Question(
            text,
            new String[] {"Right", "Wrong A", "Wrong B", "Wrong C"},
            0,
            points);
    }

    @SuppressWarnings("unchecked")
    private static <T> T getField(Object target, String name)
    {
        try
        {
            Field f = target.getClass().getDeclaredField(name);
            f.setAccessible(true);
            return (T) f.get(target);
        }
        catch (ReflectiveOperationException e)
        {
            throw new RuntimeException(e);
        }
    }

    private static void setField(Object target, String name, Object value)
    {
        try
        {
            Field f = target.getClass().getDeclaredField(name);
            f.setAccessible(true);
            f.set(target, value);
        }
        catch (ReflectiveOperationException e)
        {
            throw new RuntimeException(e);
        }
    }

    private void loadQueue(Main game, Question... questions)
    {
        Queue<Question> q = new Queue<>();
        for (Question question : questions)
        {
            q.enqueue(question);
        }
        setField(game, "queue", q);
    }

    private Player player(Main game)
    {
        return getField(game, "player");
    }

    private Queue<Question> queue(Main game)
    {
        return getField(game, "queue");
    }

    private int questionNumber(Main game)
    {
        return getField(game, "questionNumber");
    }

    private boolean doubleFlag(Main game)
    {
        return getField(game, "doubleCurrentQuestion");
    }

    /** Puts one power-up straight into the player's inventory. */
    private void givePowerUp(Main game, PowerUp p)
    {
        player(game).addPowerUp(p);
    }

    /** Writes lines to a temp file and returns its path. */
    private static String tempFile(String... lines) throws IOException
    {
        Path f = Files.createTempFile("questions", ".txt");
        f.toFile().deleteOnExit();
        Files.write(f, List.of(lines), StandardCharsets.UTF_8);
        return f.toString();
    }

    // ------------------------------------------------------------
    // Constructor
    // ------------------------------------------------------------

    @Test
    @DisplayName("New game starts with 3 lives, 0 points, question 1")
    void constructorDefaults()
    {
        Main game = newGame();
        assertEquals(3, player(game).getLives());
        assertEquals(0, player(game).getPoints());
        assertEquals(1, questionNumber(game));
        assertFalse(doubleFlag(game));
        assertTrue(queue(game).isEmpty());
    }

    // ------------------------------------------------------------
    // parseChoice (static, pure)
    // ------------------------------------------------------------

    @Nested
    @DisplayName("parseChoice")
    class ParseChoiceTests
    {
        @Test void validMiddleValue()  { assertEquals(3, Main.parseChoice("3", 1, 6)); }
        @Test void lowerBoundary()     { assertEquals(1, Main.parseChoice("1", 1, 6)); }
        @Test void upperBoundary()     { assertEquals(6, Main.parseChoice("6", 1, 6)); }
        @Test void zeroAllowedInShopRange() { assertEquals(0, Main.parseChoice("0", 0, 3)); }
        @Test void singleValueRange()  { assertEquals(4, Main.parseChoice("4", 4, 4)); }

        @Test void trimsWhitespace()   { assertEquals(2, Main.parseChoice("  2  ", 1, 6)); }
        @Test void trimsTabsNewlines() { assertEquals(2, Main.parseChoice("\t2\n", 1, 6)); }

        @Test void belowRange()        { assertEquals(-1, Main.parseChoice("0", 1, 6)); }
        @Test void aboveRange()        { assertEquals(-1, Main.parseChoice("7", 1, 6)); }
        @Test void negativeNumber()    { assertEquals(-1, Main.parseChoice("-3", 1, 6)); }

        @Test void nullInput()         { assertEquals(-1, Main.parseChoice(null, 1, 6)); }
        @Test void emptyInput()        { assertEquals(-1, Main.parseChoice("", 1, 6)); }
        @Test void blankInput()        { assertEquals(-1, Main.parseChoice("   ", 1, 6)); }
        @Test void letters()           { assertEquals(-1, Main.parseChoice("abc", 1, 6)); }
        @Test void decimal()           { assertEquals(-1, Main.parseChoice("2.5", 1, 6)); }
        @Test void numberWithText()    { assertEquals(-1, Main.parseChoice("2 apples", 1, 6)); }
        @Test void internalSpace()     { assertEquals(-1, Main.parseChoice("1 2", 1, 6)); }
        @Test void overflow()          { assertEquals(-1, Main.parseChoice("99999999999", 1, 6)); }
        @Test void minGreaterThanMax() { assertEquals(-1, Main.parseChoice("3", 6, 1)); }

        @Test
        @DisplayName("Leading '+' is accepted (Integer.parseInt allows it)")
        void plusSign()
        {
            assertEquals(3, Main.parseChoice("+3", 1, 6));
        }

        @Test
        @DisplayName("Negative ranges work for values other than -1")
        void negativeRange()
        {
            assertEquals(-3, Main.parseChoice("-3", -5, 5));
        }

        @Test
        @DisplayName("KNOWN LIMITATION: -1 is both a valid value and the error sentinel")
        void sentinelAmbiguity()
        {
            // "-1" is in range, but the caller can't tell it apart from "invalid".
            assertEquals(-1, Main.parseChoice("-1", -5, 5));
        }
    }

    // ------------------------------------------------------------
    // promptChoice (reads from System.in)
    // ------------------------------------------------------------

    @Nested
    @DisplayName("promptChoice")
    class PromptChoiceTests
    {
        @Test
        void returnsValidInputImmediately()
        {
            Main game = newGame("4\n");
            assertEquals(4, game.promptChoice(1, 6));
            assertFalse(output().contains("Invalid input"));
        }

        @Test
        void repromptsUntilValid()
        {
            Main game = newGame("abc\n9\n\n2\n");
            assertEquals(2, game.promptChoice(1, 6));

            String o = output();
            int errors = o.split("Invalid input", -1).length - 1;
            assertEquals(3, errors, "Expected one error per bad line");
            assertTrue(o.contains("Please enter a number from 1 to 6."));
        }

        @Test
        void returnsMinusOneOnEndOfInput()
        {
            Main game = newGame("");
            assertEquals(-1, game.promptChoice(1, 6));
        }

        @Test
        void returnsMinusOneWhenInputEndsAfterBadLines()
        {
            Main game = newGame("x\ny\n");
            assertEquals(-1, game.promptChoice(1, 6));
        }

        @Test
        void acceptsLastLineWithoutTrailingNewline()
        {
            Main game = newGame("3");
            assertEquals(3, game.promptChoice(1, 6));
        }

        @Test
        void printsPrompt()
        {
            Main game = newGame("1\n");
            game.promptChoice(1, 6);
            assertTrue(output().contains("Your choice: "));
        }
    }

    // ------------------------------------------------------------
    // loadQuestions
    // ------------------------------------------------------------

    @Nested
    @DisplayName("loadQuestions")
    class LoadQuestionsTests
    {
        @Test
        void nullPathFails()
        {
            assertFalse(newGame().loadQuestions(null));
            assertTrue(output().contains("Invalid question file path."));
        }

        @Test
        void emptyPathFails()
        {
            assertFalse(newGame().loadQuestions(""));
            assertTrue(output().contains("Invalid question file path."));
        }

        @Test
        void blankPathFails()
        {
            assertFalse(newGame().loadQuestions("   "));
            assertTrue(output().contains("Invalid question file path."));
        }

        @Test
        void missingFileFails()
        {
            String path = "definitely/does/not/exist_12345.txt";
            assertFalse(newGame().loadQuestions(path));
            assertTrue(output().contains("Could not find question file: " + path));
        }

        @Test
        void failedLoadLeavesQueueEmpty()
        {
            Main game = newGame();
            game.loadQuestions("nope_12345.txt");
            assertTrue(queue(game).isEmpty());
        }

        @Test
        void validFileLoadsQuestions() throws IOException
        {
            Main game = newGame();
            String path = tempFile(
                "2+2?|3|4|5|6|1|10",
                "Capital of France?|Rome|Paris|Berlin|Madrid|1|20");

            assertTrue(game.loadQuestions(path));
            assertEquals(2, queue(game).size());
            assertTrue(output().contains("Successfully loaded 2 questions."));
        }

        @Test
        void fileWithNoValidQuestionsFails() throws IOException
        {
            Main game = newGame();
            String path = tempFile("# only a comment", "", "bad line");

            assertFalse(game.loadQuestions(path));
            assertTrue(output().contains("No valid question found"));
            assertTrue(queue(game).isEmpty());
        }

        @Test
        void directoryPathFails() throws IOException
        {
            Path dir = Files.createTempDirectory("qdir");
            dir.toFile().deleteOnExit();
            assertFalse(newGame().loadQuestions(dir.toString()));
        }

        @Test
        void loadedGameIsPlayable() throws IOException
        {
            Main game = newGame("2\n");
            String path = tempFile("2+2?|3|4|5|6|1|10");

            assertTrue(game.loadQuestions(path));
            game.run();

            assertTrue(output().contains("Final score: 10"));
            assertTrue(output().contains("Question 1: 2+2?"));
        }
    }

    // ------------------------------------------------------------
    // handleAnswer
    // ------------------------------------------------------------

    @Nested
    @DisplayName("handleAnswer")
    class HandleAnswerTests
    {
        @Test
        void correctAnswerAddsPointsAndRemovesQuestion()
        {
            Main game = newGame();
            loadQueue(game, makeQuestion("Q1", 10), makeQuestion("Q2", 20));

            game.handleAnswer(0);

            assertEquals(10, player(game).getPoints());
            assertEquals(3, player(game).getLives());
            assertEquals(1, queue(game).size());
            assertEquals("Q2", queue(game).peek().getPrompt());
        }

        @Test
        void wrongAnswerCostsLifeAndMovesQuestionToBack()
        {
            Main game = newGame();
            Question q1 = makeQuestion("Q1", 10);
            Question q2 = makeQuestion("Q2", 20);
            loadQueue(game, q1, q2);

            game.handleAnswer(2);

            assertEquals(0, player(game).getPoints());
            assertEquals(2, player(game).getLives());
            assertEquals(2, queue(game).size());
            assertSame(q2, queue(game).peek());
            queue(game).dequeue();
            assertSame(q1, queue(game).peek());
        }

        @Test
        void wrongAnswerOnOnlyQuestionKeepsItInQueue()
        {
            Main game = newGame();
            Question q = makeQuestion("Only", 10);
            loadQueue(game, q);

            game.handleAnswer(1);

            assertEquals(1, queue(game).size());
            assertSame(q, queue(game).peek());
        }

        @Test
        void emptyQueueIsNoOp()
        {
            Main game = newGame();
            game.handleAnswer(0);
            assertEquals(0, player(game).getPoints());
            assertEquals(3, player(game).getLives());
        }

        @Test
        void deadPlayerIsNoOp()
        {
            Main game = newGame();
            loadQueue(game, makeQuestion("Q1", 10));
            for (int i = 0; i < 3; i++) player(game).loseLife();

            game.handleAnswer(0);

            assertEquals(0, player(game).getPoints());
            assertEquals(1, queue(game).size());
        }

        @Test
        void losingLastLifeKillsPlayer()
        {
            Main game = newGame();
            loadQueue(game, makeQuestion("Q1", 10));

            game.handleAnswer(1);
            game.handleAnswer(1);
            game.handleAnswer(1);

            assertEquals(0, player(game).getLives());
            assertFalse(player(game).isAlive());
        }

        @Test
        void pointsAccumulateAcrossQuestions()
        {
            Main game = newGame();
            loadQueue(game, makeQuestion("Q1", 10), makeQuestion("Q2", 25));

            game.handleAnswer(0);
            game.handleAnswer(0);

            assertEquals(35, player(game).getPoints());
            assertTrue(queue(game).isEmpty());
        }
    }

    // ------------------------------------------------------------
    // handlePowerUp
    // ------------------------------------------------------------

    @Nested
    @DisplayName("handlePowerUp")
    class HandlePowerUpTests
    {
        @Test
        void nullPowerUpIsNoOp()
        {
            Main game = newGame();
            loadQueue(game, makeQuestion("Q1", 10));
            assertDoesNotThrow(() -> game.handlePowerUp(null));
            assertEquals(1, queue(game).size());
        }

        @Test
        void unownedPowerUpPrintsMessageAndDoesNothing()
        {
            Main game = newGame();
            loadQueue(game, makeQuestion("Q1", 10));

            game.handlePowerUp(PowerUp.SKIP);

            assertTrue(output().contains("You do not own this power-up."));
            assertEquals(1, queue(game).size());
            assertEquals(1, questionNumber(game));
        }

        @Test
        void emptyQueueDoesNotConsumePowerUp()
        {
            Main game = newGame();
            givePowerUp(game, PowerUp.SKIP);
            int before = player(game).getPowerUpCount(PowerUp.SKIP);

            game.handlePowerUp(PowerUp.SKIP);

            assertEquals(before, player(game).getPowerUpCount(PowerUp.SKIP));
        }

        @Test
        void skipRemovesQuestionAndAdvancesNumber()
        {
            Main game = newGame();
            Question q2 = makeQuestion("Q2", 20);
            loadQueue(game, makeQuestion("Q1", 10), q2);
            givePowerUp(game, PowerUp.SKIP);
            int points = player(game).getPoints();
            int count = player(game).getPowerUpCount(PowerUp.SKIP);

            game.handlePowerUp(PowerUp.SKIP);

            assertSame(q2, queue(game).peek());
            assertEquals(1, queue(game).size());
            assertEquals(2, questionNumber(game));
            assertEquals(points, player(game).getPoints(), "Skip should not award points");
            assertEquals(count - 1, player(game).getPowerUpCount(PowerUp.SKIP));
            assertTrue(output().contains("Question skipped!"));
        }

        @Test
        void skipLastQuestionEmptiesQueue()
        {
            Main game = newGame();
            loadQueue(game, makeQuestion("Q1", 10));
            givePowerUp(game, PowerUp.SKIP);

            game.handlePowerUp(PowerUp.SKIP);

            assertTrue(queue(game).isEmpty());
        }

        @Test
        void skipClearsPendingDouble()
        {
            Main game = newGame();
            loadQueue(game, makeQuestion("Q1", 10), makeQuestion("Q2", 20));
            givePowerUp(game, PowerUp.DOUBLE);
            givePowerUp(game, PowerUp.SKIP);

            game.handlePowerUp(PowerUp.DOUBLE);
            game.handlePowerUp(PowerUp.SKIP);

            assertFalse(doubleFlag(game), "DOUBLE must not carry over to the next question");
        }

        @Test
        void extraLifeAddsLife()
        {
            Main game = newGame();
            loadQueue(game, makeQuestion("Q1", 10));
            givePowerUp(game, PowerUp.EXTRA_LIFE);

            game.handlePowerUp(PowerUp.EXTRA_LIFE);

            assertEquals(4, player(game).getLives());
            assertEquals(1, queue(game).size());
            assertTrue(output().contains("You gained an extra life!"));
        }

        @Test
        void doubleSetsFlagWithoutChangingQueue()
        {
            Main game = newGame();
            loadQueue(game, makeQuestion("Q1", 10));
            givePowerUp(game, PowerUp.DOUBLE);

            game.handlePowerUp(PowerUp.DOUBLE);

            assertTrue(doubleFlag(game));
            assertEquals(1, queue(game).size());
            assertTrue(output().contains("worth double points"));
        }

        @Test
        void doubleDoublesPointsOnCorrectAnswer()
        {
            Main game = newGame();
            loadQueue(game, makeQuestion("Q1", 15));
            givePowerUp(game, PowerUp.DOUBLE);
            int before = player(game).getPoints();

            game.handlePowerUp(PowerUp.DOUBLE);
            game.handleAnswer(0);

            assertEquals(before + 30, player(game).getPoints());
            assertFalse(doubleFlag(game));
        }

        @Test
        void doubleOnlyAppliesToOneQuestion()
        {
            Main game = newGame();
            loadQueue(game, makeQuestion("Q1", 10), makeQuestion("Q2", 10));
            givePowerUp(game, PowerUp.DOUBLE);
            int before = player(game).getPoints();

            game.handlePowerUp(PowerUp.DOUBLE);
            game.handleAnswer(0); // +20
            game.handleAnswer(0); // +10

            assertEquals(before + 30, player(game).getPoints());
        }

        @Test
        void doubleIsLostOnWrongAnswer()
        {
            Main game = newGame();
            loadQueue(game, makeQuestion("Q1", 10));
            givePowerUp(game, PowerUp.DOUBLE);
            int before = player(game).getPoints();

            game.handlePowerUp(PowerUp.DOUBLE);
            game.handleAnswer(1);   // wrong, flag consumed
            assertFalse(doubleFlag(game));

            game.handleAnswer(0);   // same question again, normal points
            assertEquals(before + 10, player(game).getPoints());
        }

        @Test
        void deadPlayerCannotUsePowerUp()
        {
            Main game = newGame();
            loadQueue(game, makeQuestion("Q1", 10));
            givePowerUp(game, PowerUp.EXTRA_LIFE);
            for (int i = 0; i < 3; i++) player(game).loseLife();

            game.handlePowerUp(PowerUp.EXTRA_LIFE);

            assertEquals(0, player(game).getLives());
            assertEquals(1, player(game).getPowerUpCount(PowerUp.EXTRA_LIFE));
        }
    }

    // ------------------------------------------------------------
    // handleShop
    // ------------------------------------------------------------

    @Nested
    @DisplayName("handleShop")
    class HandleShopTests
    {
        @Test
        void leaveImmediately()
        {
            Main game = newGame("0\n");
            game.handleShop();
            assertTrue(output().contains("0. Leave shop"));
            assertEquals(0, player(game).getPoints());
        }

        @Test
        void endOfInputExitsShop()
        {
            Main game = newGame("");
            assertDoesNotThrow(game::handleShop);
        }

        @Test
        void cannotAffordWithZeroPoints()
        {
            Main game = newGame("1\n0\n");
            game.handleShop();
            assertTrue(output().contains("You do not have enough points."));
        }

        @Test
        void successfulPurchase()
        {
            Main game = newGame("1\n0\n");
            player(game).addPoints(30); // exactly the SKIP price

            game.handleShop();

            assertTrue(output().contains("Purchase successful!"));
            assertEquals(1, player(game).getPowerUpCount(PowerUp.SKIP));
            assertEquals(0, player(game).getPoints());
        }

        @Test
        void oneShortOfPriceFails()
        {
            Main game = newGame("1\n0\n");
            player(game).addPoints(29);

            game.handleShop();

            assertTrue(output().contains("You do not have enough points."));
            assertEquals(0, player(game).getPowerUpCount(PowerUp.SKIP));
            assertEquals(29, player(game).getPoints());
        }

        @Test
        void invalidChoiceIsRejected()
        {
            Main game = newGame("99\nabc\n0\n");
            game.handleShop();
            assertTrue(output().contains("Invalid input"));
        }

        @Test
        void canBuyMultipleTimesBeforeLeaving()
        {
            Main game = newGame("1\n1\n0\n");
            player(game).addPoints(1000);

            game.handleShop();

            assertEquals(2, player(game).getPowerUpCount(PowerUp.SKIP));
            assertEquals(1000 - 60, player(game).getPoints());
        }

        @Test
        void outOfStockMessage()
        {
            // EXTRA_LIFE (item 2) has 3 in stock; the 4th buy fails.
            Main game = newGame("2\n2\n2\n2\n0\n");
            player(game).addPoints(1000);

            game.handleShop();

            assertEquals(3, player(game).getPowerUpCount(PowerUp.EXTRA_LIFE));
            assertTrue(output().contains("That power-up is out of stock."));
        }

        @Test
        void shopShowsPricesAndStock()
        {
            Main game = newGame("0\n");
            game.handleShop();
            String o = output();
            assertTrue(o.contains("SKIP - 30 points (5 left)"));
            assertTrue(o.contains("EXTRA_LIFE - 50 points (3 left)"));
            assertTrue(o.contains("DOUBLE - 40 points (4 left)"));
        }

        @Test
        void buyThenUseDoubleInGame()
        {
            // shop -> buy DOUBLE (item 3) -> leave -> power-up menu -> use it -> answer
            Main game = newGame("5\n3\n0\n6\n1\n1\n");
            loadQueue(game, makeQuestion("Q1", 25));
            player(game).addPoints(40);

            game.run();

            assertTrue(output().contains("Final score: 50"));
        }
    }

    // ------------------------------------------------------------
    // run (full game loop)
    // ------------------------------------------------------------

    @Nested
    @DisplayName("run")
    class RunTests
    {
        @Test
        void emptyQueueEndsImmediately()
        {
            Main game = newGame("");
            game.run();

            String o = output();
            assertTrue(o.contains("Welcome to Trivia!"));
            assertTrue(o.contains("GAME OVER"));
            assertTrue(o.contains("Final score: 0"));
            assertTrue(o.contains("Questions remaining: 0"));
            assertTrue(o.contains("Lives remaining: 3"));
        }

        @Test
        void answeringAllCorrectlyWins()
        {
            Main game = newGame("1\n1\n");
            loadQueue(game, makeQuestion("Q1", 10), makeQuestion("Q2", 20));

            game.run();

            String o = output();
            assertTrue(o.contains("Final score: 30"));
            assertTrue(o.contains("Questions remaining: 0"));
            assertTrue(o.contains("Lives remaining: 3"));
            assertEquals(3, questionNumber(game));
        }

        @Test
        void runningOutOfLivesEndsGame()
        {
            Main game = newGame("2\n2\n2\n1\n"); // last line never read
            loadQueue(game, makeQuestion("Q1", 10));

            game.run();

            String o = output();
            assertTrue(o.contains("GAME OVER"));
            assertTrue(o.contains("Lives remaining: 0"));
            assertTrue(o.contains("Questions remaining: 1"));
            assertTrue(o.contains("Final score: 0"));
        }

        @Test
        void endOfInputExitsGracefully()
        {
            Main game = newGame("");
            loadQueue(game, makeQuestion("Q1", 10));

            game.run();

            String o = output();
            assertTrue(o.contains("Input ended. Exiting game."));
            assertTrue(o.contains("Questions remaining: 1"));
        }

        @Test
        void invalidInputDoesNotCostLife()
        {
            Main game = newGame("hello\n0\n7\n1\n");
            loadQueue(game, makeQuestion("Q1", 10));

            game.run();

            assertEquals(3, player(game).getLives());
            assertTrue(output().contains("Final score: 10"));
        }

        @Test
        void powerUpMenuWithNoPowerUps()
        {
            Main game = newGame("6\n1\n");
            loadQueue(game, makeQuestion("Q1", 10));

            game.run();

            String o = output();
            assertTrue(o.contains("You do not have any power-ups."));
            assertTrue(o.contains("Final score: 10"));
        }

        @Test
        void shopVisitDoesNotAdvanceQuestion()
        {
            Main game = newGame("5\n0\n1\n");
            loadQueue(game, makeQuestion("Q1", 10));

            game.run();

            assertEquals(2, questionNumber(game));
            assertTrue(output().contains("Final score: 10"));
        }

        @Test
        void cancelPowerUpMenu()
        {
            // Power-up menu: 1 item + "2. Cancel"
            Main game = newGame("6\n2\n1\n");
            loadQueue(game, makeQuestion("Q1", 10));
            givePowerUp(game, PowerUp.EXTRA_LIFE);
            int count = player(game).getPowerUpCount(PowerUp.EXTRA_LIFE);

            game.run();

            assertEquals(count, player(game).getPowerUpCount(PowerUp.EXTRA_LIFE));
            assertEquals(3, player(game).getLives());
        }

        @Test
        void useExtraLifeThroughMenu()
        {
            Main game = newGame("6\n1\n1\n");
            loadQueue(game, makeQuestion("Q1", 10));
            givePowerUp(game, PowerUp.EXTRA_LIFE);

            game.run();

            assertTrue(output().contains("You gained an extra life!"));
            assertTrue(output().contains("Lives remaining: 4"));
        }

        @Test
        void wrongThenRightOnSameQuestion()
        {
            Main game = newGame("3\n1\n");
            loadQueue(game, makeQuestion("Q1", 10));

            game.run();

            String o = output();
            assertTrue(o.contains("Final score: 10"));
            assertTrue(o.contains("Lives remaining: 2"));
            assertTrue(o.contains("Questions remaining: 0"));
        }
    }
}
