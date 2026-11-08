# CS Trivia Game

## About
This is a Java trivia game with computer science questions. The player answers questions, earns points, and has a limited number of lives. The game also includes a shop with power-ups.

## Project Structure
The project uses MVC:

- **Model:** `Queue`, `Question`, `QuestionList`, `Player`, and `Shop` store the game data and handle the game rules.
- **View:** `QuestionReader` and `ShopReader` format information for the player.
- **Controller:** `Main` runs the game and connects the model and view.

## Questions File
The questions are stored in `src/questions.txt`. Each question goes on its own line and uses this format:

```text
question|choice1|choice2|choice3|choice4|correctIndex|pointValue
```

The correct answer index starts at `0`:
- `0` = first choice
- `1` = second choice
- `2` = third choice
- `3` = fourth choice

Example:

```text
What does CPU stand for?|Central Processing Unit|Computer Personal Unit|Central Program Utility|Core Processing User|0|100
Which data structure follows FIFO?|Stack|Queue|Tree|Graph|1|100
Which Java keyword is used to create a subclass?|implements|super|extends|import|2|100
```

## Running the Game
Run `Trivia.Main` from the project in your IDE. Make sure the program's working directory is the project folder so it can find `src/questions.txt`.

## Testing
Test the game with valid and invalid answer choices, correct and incorrect answers, an empty or missing questions file, and power-up purchases with enough and not enough points.

## Team Work
- **Jonathan:** `Question` and `QuestionList`
- **Colin:** `Player` and power-ups
- **Lena:** `Shop` and purchasing
- **Ashutosh:** `Main`, integration, and testing

## GenAI Note
We used AI to help think through invalid input handling and how the classes should divide responsibilities. The team reviewed those suggestions and used them when planning the project.
