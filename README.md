# Computer Science Trivia Game

## Overview

This project is a command-line computer science trivia game written in Java. Players answer multiple-choice questions, earn points for correct answers, and lose lives for incorrect answers. Questions are loaded from a text file and placed into a randomized queue.

The project is organized using a Model-View-Controller (MVC) design:

- **Model:** Stores the game's data and implements its rules.
- **View:** Formats questions, player status, and shop information for display.
- **Controller:** Runs the game, receives player input, and coordinates the model and views.

## Features

The project specification defines the following gameplay features:

- Multiple-choice computer science questions loaded from a text file.
- Questions placed into a randomized queue.
- A player score and limited number of lives.
- Correct answers award the question's point value.
- Incorrect answers cost a life and move the question to the back of the queue.
- A shop where players can purchase power-ups using points.
- Three power-up types:
  - `SKIP` — skips the current question.
  - `EXTRA_LIFE` — adds a life.
  - `DOUBLE` — doubles the points awarded for the current question.
- The game ends when the question queue is empty or the player has no lives remaining.

The exact starting lives, shop prices, stock amounts, and user-interface wording are determined by the Java implementation.

## Project Structure

The project uses the `Trivia` Java package.

| Class | Role | Responsibility |
|---|---|---|
| `Main` | Controller | Loads questions, runs the game loop, processes input, and coordinates the other classes. |
| `Queue<T>` | Model | Stores questions in first-in, first-out order. |
| `Question` | Model | Stores a question, its four answer choices, the correct answer index, and its point value. |
| `QuestionList` | Model | Reads questions from a text file and converts them into a randomized queue. |
| `Player` | Model | Tracks points, lives, and the player's power-up inventory. |
| `Shop` | Model | Tracks power-up prices and stock and processes purchases. |
| `QuestionReader` | View | Formats questions, answer choices, status, and answer feedback. |
| `ShopReader` | View | Formats the shop's items, prices, stock, and affordability. |
| `PowerUp` | Model support | Defines the available power-up types. |
| `PurchaseResult` | Model support | Represents the result of a shop purchase. |
| `ModelListener` | Model support | Allows model changes to notify registered listeners, if used by the implementation. |

## Requirements

- Java Development Kit (JDK) installed.
- The project source files must be in the `Trivia` package.
- A valid questions text file, such as `questions.txt`.

## Running the Program

Place `questions.txt` in the working directory from which you launch the program, unless you provide a different file path.

If the source files are in a directory named `Trivia`, open a terminal in the directory containing that folder and compile:

```bash
javac Trivia/*.java
```

Then run:

```bash
java Trivia.Main questions.txt
```

If the program is configured to use `questions.txt` by default, it may also be started with:

```bash
java Trivia.Main
```

If your IDE is configured with a different source layout, run `Trivia.Main` and set the program argument to the path of your questions file.

## Questions File Format

The questions file is a plain-text file. Put exactly one question on each line.

Each line must contain **seven fields**, separated by the pipe character (`|`), in this order:

```text
question prompt|choice 1|choice 2|choice 3|choice 4|correct answer index|point value
```

Important formatting rules:

1. Include one prompt and exactly four answer choices.
2. The correct answer index is **zero-based**:
   - `0` means the first choice.
   - `1` means the second choice.
   - `2` means the third choice.
   - `3` means the fourth choice.
3. The point value must be a positive integer.
4. Do not put extra `|` characters inside a prompt or answer choice, because they separate fields.
5. Put each question on its own line.
6. Blank lines and lines beginning with `#` may be used as comments if supported by the current `QuestionList` implementation.

### Example

```text
What does CPU stand for?|Central Processing Unit|Computer Personal Unit|Central Program Utility|Core Processing User|0|100
Which data structure follows the First In First Out (FIFO) principle?|Stack|Queue|Tree|Graph|1|100
Which Java keyword is used to create a subclass?|implements|super|extends|import|2|100
```

The example above is also provided in `questions.txt`.

## How to Play

Follow the choices displayed by the game. Select an answer by entering its displayed number. Correct answers award points; incorrect answers cost a life. Depending on the implemented menu, players can also visit the shop or use an owned power-up.

The game finishes when all questions have been answered or the player's lives reach zero. The final score is then displayed.

## Class Design (MVC)

### Model

The model classes own the game's data and rules. They do not format the user interface.

- `Question` and `QuestionList` represent and load trivia questions.
- `Queue<T>` manages the order in which questions are presented.
- `Player` tracks the player's score, lives, and power-ups.
- `Shop` manages available power-ups, prices, stock, and purchase validation.

### View

The view classes format information for display.

- `QuestionReader` formats questions, choices, status, and answer feedback.
- `ShopReader` formats shop information.

The views should not be responsible for changing scores, lives, queue contents, or shop inventory.

### Controller

`Main` coordinates the game. It loads the question file, displays formatted information through the view classes, accepts player input, and calls the appropriate model methods.

## Testing

The project should be tested with:

- A valid question file containing multiple questions.
- A missing or unreadable question file.
- Malformed question lines and a file containing no valid questions.
- Correct and incorrect answers.
- Invalid menu input.
- The game-ending conditions (empty question queue and zero remaining lives).
- Each power-up, including attempts to use a power-up the player does not own.
- Shop purchases with sufficient points, insufficient points, and unavailable stock.

Model classes should also be tested independently of the view classes.

## Contributors

Update this section to reflect the final team contributions:

- Jonathan — `Question` and `QuestionList`.
- Colin — `Player` and the power-up system.
- Lena — `Shop` and the purchasing system.
- Ashutosh — `Main`, integration, and testing.

Please confirm these responsibilities and names with the team before submission, and add anyone who made additional contributions.

## Project Status

This README describes the intended project design and usage. Before submission, the team should verify the compile/run commands against the final folder layout, confirm that the menu and power-up behavior match the implementation, run the tests, and update this section if any specified feature was changed or not completed.
