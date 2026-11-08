
package Trivia;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.List;

/**
 * Main controller for the Trivia game.
 *
 * Handles the game loop, user input, answers,
 * power-ups, and shop interactions.
 *
 * @author Ashutosh
 * @version Nov 2026
 */
public class Main
{
    //~ Fields ................................................................

    private Queue<Question> queue;
    private Player player;
    private Shop shop;
    private QuestionReader qReader;
    private ShopReader sReader;
    private Scanner in;

    /**
     * Determines whether the current question's points
     * should be doubled.
     */
    private boolean doubleCurrentQuestion;


    //~ Constructors ..........................................................

    /**
     * Creates the main game controller.
     */
    public Main()
    {
        queue = new Queue<>();
        player = new Player(3);
        shop = new Shop();
        qReader = new QuestionReader();
        sReader = new ShopReader();
        in = new Scanner(System.in);
        doubleCurrentQuestion = false;
    }


    //~ Public Methods ........................................................

    /**
     * Starts the trivia game.
     *
     * @param args optional question file path
     */
    public static void main(String[] args)
    {
        Main game = new Main();

        String path = "questions.txt";

        if (args.length > 0)
        {
            path = args[0];
        }

        if (game.loadQuestions(path))
        {
            game.run();
        }
    }


    /**
     * Loads questions from a text file into the queue.
     *
     * @param path path to the question file
     * @return true if loading succeeds
     */
    public boolean loadQuestions(String path)
    {
        try
        {
            QuestionList questionList = new QuestionList();

            questionList.loadFromFile(new File(path));

            queue = questionList.toQueue();

            System.out.println(
                "Successfully loaded " +
                questionList.size() + " questions.");

            return true;
        }
        catch (FileNotFoundException e)
        {
            System.out.println(
                "Could not find question file: " + path);

            return false;
        }
        catch (IllegalStateException e)
        {
            System.out.println(e.getMessage());

            return false;
        }
    }


    /**
     * Runs the main game loop.
     *
     * The game ends when there are no questions left
     * or the player runs out of lives.
     */
    public void run()
    {
        System.out.println("Welcome to Trivia!");

        modelChanged();

        while (!queue.isEmpty() && player.isAlive())
        {
            Question current = queue.peek();

            System.out.println();
            System.out.println(
                qReader.format(current, 1));

            String[] choices = current.getChoices();

            for (int i = 0; i < choices.length; i++)
            {
                System.out.println(
                    (i + 1) + ". " + choices[i]);
            }

            System.out.println();
            System.out.println("Enter 1-4 to answer.");
            System.out.println("Enter 5 to visit the shop.");
            System.out.println("Enter 6 to use a power-up.");

            int choice = promptChoice(1, 6);

            if (choice == 5)
            {
                handleShop();
                continue;
            }

            if (choice == 6)
            {
                offerPowerUp();
                continue;
            }

            handleAnswer(choice - 1);

            doubleCurrentQuestion = false;

            modelChanged();
        }

        System.out.println();
        System.out.println("GAME OVER");

        System.out.println(
            "Final score: " + player.getPoints());

        System.out.println(
            "Questions remaining: " + queue.size());

        System.out.println(
            "Lives remaining: " + player.getLives());
    }


    /**
     * Checks the player's answer.
     *
     * Correct answers earn points and remove the question.
     * Incorrect answers cost a life and move the question
     * to the back of the queue.
     *
     * @param choiceIndex zero-based answer index
     */
    public void handleAnswer(int choiceIndex)
    {
        if (queue.isEmpty() || !player.isAlive())
        {
            return;
        }

        Question current = queue.peek();

        if (current.isCorrect(choiceIndex))
        {
            int points = current.getPointValue();

            if (doubleCurrentQuestion)
            {
                points *= 2;
            }

            player.addPoints(points);

            queue.dequeue();

            System.out.println(
                qReader.formatResult(
                    true, current.getCorrectAnswer()));
        }
        else
        {
            player.loseLife();

            queue.dequeue();
            queue.enqueue(current);

            System.out.println(
                qReader.formatResult(
                    false, current.getCorrectAnswer()));
        }
    }


    /**
     * Displays the shop and processes purchases.
     */
    public void handleShop()
    {
        boolean shopping = true;

        while (shopping)
        {
            System.out.println();
            System.out.println(
                sReader.format(shop, player.getPoints()));

            List<PowerUp> items = shop.getItems();

            System.out.println("0. Leave shop");

            for (int i = 0; i < items.size(); i++)
            {
                System.out.println(
                    (i + 1) + ". " + items.get(i));
            }

            int choice = promptChoice(0, items.size());

            if (choice == 0)
            {
                shopping = false;
            }
            else
            {
                PowerUp selected = items.get(choice - 1);

                PurchaseResult result =
                    shop.purchase(player, selected);

                if (result == PurchaseResult.OK)
                {
                    System.out.println(
                        "Purchase successful!");
                }
                else if (
                    result == PurchaseResult.NOT_ENOUGH_POINTS)
                {
                    System.out.println(
                        "You do not have enough points.");
                }
                else if (
                    result == PurchaseResult.OUT_OF_STOCK)
                {
                    System.out.println(
                        "That power-up is out of stock.");
                }
            }
        }
    }


    /**
     * Uses a power-up owned by the player.
     *
     * @param p power-up to use
     */
    public void handlePowerUp(PowerUp p)
    {
        if (p == null)
        {
            return;
        }

        if (!player.usePowerUp(p))
        {
            System.out.println(
                "You do not own this power-up.");

            return;
        }

        if (p == PowerUp.SKIP)
        {
            if (!queue.isEmpty())
            {
                queue.dequeue();

                System.out.println(
                    "Question skipped!");
            }
        }
        else if (p == PowerUp.EXTRA_LIFE)
        {
            player.addLife();

            System.out.println(
                "You gained an extra life!");
        }
        else if (p == PowerUp.DOUBLE)
        {
            doubleCurrentQuestion = true;

            System.out.println(
                "Your next correct answer is worth double points!");
        }
    }


    /**
     * Prompts the user until a valid number is entered.
     *
     * @param min minimum accepted number
     * @param max maximum accepted number
     * @return valid selection
     */
    public int promptChoice(int min, int max)
    {
        while (true)
        {
            System.out.print("Your choice: ");

            if (!in.hasNextLine())
            {
                return min;
            }

            String input = in.nextLine();

            int choice = parseChoice(input, min, max);

            if (choice != -1)
            {
                return choice;
            }

            System.out.println(
                "Invalid input. Please enter a number from " +
                min + " to " + max + ".");
        }
    }


    /**
     * Converts user input into a valid integer selection.
     *
     * @param s user input
     * @param min minimum accepted number
     * @param max maximum accepted number
     * @return parsed number or -1 if invalid
     */
    public static int parseChoice(
        String s, int min, int max)
    {
        if (s == null || s.isBlank())
        {
            return -1;
        }

        try
        {
            int choice = Integer.parseInt(s.trim());

            if (choice < min || choice > max)
            {
                return -1;
            }

            return choice;
        }
        catch (NumberFormatException e)
        {
            return -1;
        }
    }


    /**
     * Refreshes the displayed player status.
     */
    public void modelChanged()
    {
        System.out.println(
            qReader.formatStatus(
                player.getPoints(),
                player.getLives()));
    }


    //~ Private Methods .......................................................

    /**
     * Displays available power-ups and lets the player
     * select one to use.
     */
    private void offerPowerUp()
    {
        PowerUp[] powerUps = PowerUp.values();

        System.out.println();
        System.out.println("Your power-ups:");

        int available = 0;

        for (PowerUp p : powerUps)
        {
            int count = player.getPowerUpCount(p);

            if (count > 0)
            {
                available++;

                System.out.println(
                    p + " - Quantity: " + count);
            }
        }

        if (available == 0)
        {
            System.out.println(
                "You do not have any power-ups.");

            return;
        }

        System.out.println();

        for (int i = 0; i < powerUps.length; i++)
        {
            System.out.println(
                (i + 1) + ". " + powerUps[i]);
        }

        System.out.println(
            (powerUps.length + 1) + ". Cancel");

        int choice = promptChoice(
            1, powerUps.length + 1);

        if (choice == powerUps.length + 1)
        {
            return;
        }

        handlePowerUp(powerUps[choice - 1]);
    }
}
