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

## Reflection
Nothing necessarily changed from our specification document into its implementation as a program as it was thoroughly thought out and planned as a group. All methods remained the same alongside class structure. The model can stand completely alone without the view, as the view solely shows what the model classes send to the view. This includes questions, points, lives, and power ups. Nothing had to be moved from either line to get it to work as the model and view was accurately and appropriately described prior in our specification document. Our unknowns revolved around queues in general to which we had class material and teachings aid in our understanding alongside the lesson modules. StackOverflow also helped majorly in actually programming the Queue class and how it would be programmed rather than its implementation. Everything is working as of right now, however more questions do need adding to be the full game with the difficulty setting. 

## Next Steps
Nothing is left from our MVP as our MVP has been appropriately transcribed into our program accurately from our specification document. Nothing was left behind and all stated goals have been achieved. One non-trivial feature that we will be adding is our time limit on each question alongside a power up that can help in increasing the time margin. It will touch mainly the model, more accurately in the question class but it will be transferred to the view to be seen. Nothing will need much changing to implement this, it would just be an add-on feature that can fit in with the rest of the program fluently. We will be attempting all of them: the difficulty setting, (originally) the shop which was accomplished, more power ups, etc. New test methods will need to be incorporated to detect and establish the new power ups, the time limit and to ensure it actually scales and is detected to be zero, and increasing the shop interface so that selecting new shop items do not result in error. Everyone will test their own items and we will stick to the original specification document when it comes to creating the stretch goals. Colin will work on incorporating the power ups to Player, Jonathan can work on the new questions, Lena can work on the actual new power ups, and Ashutosh can work on the time countdown and incorporation into view.


## Team Work
- **Jonathan:** `Question` and `QuestionList`
- **Colin:** `Player` and power-ups
- **Lena:** `Shop` and purchasing
- **Ashutosh:** `Main`, integration, and testing

## GenAI Note
We used AI to help think through invalid input handling and how the classes should divide responsibilities. The team reviewed those suggestions and used them when planning the project.
