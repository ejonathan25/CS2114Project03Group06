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
Our project includes JUnit tests for the model classes and the generic Queue. The tests run successfully and test the model independently of the view.
The tests cover normal behavior and bad-input cases, including:
- Adding, removing, and viewing items in the queue.
- Attempting to add null items or remove items from an empty queue.
- Creating valid and invalid questions.
- Loading valid question files and handling malformed question data.
- Updating player points and lives.
- Adding and using power-ups.
- Purchasing power-ups with enough points, insufficient points, and unavailable stock.
- Handling invalid arguments and unsuccessful operations.
All current JUnit tests pass.


## Reflection
Our design has not yet fully absorbed the change we were wanting. We still plan for it to touch Question with the timer and Main for the view. So far, our prediction of the timer being done quickly was incorrect as we are still working on it and it may be until next time when it is complete. However, the process is remaining the same, and we just need time to look over it. We believe StackOverflow can still help us majorly here. Furthermore, we are just a little behind due to our sick week last week and will have it all done this go-around. The change will definitely persist in the model and will be transferable, like the rest of our design, to view. The view only has to learn about the timer and countdown and consistently change and showcase the timer counting downwards. Our D3 plan has not changed much as we have solely added more PowerUps and are continuing with our base plan. This is because our plan was detailed appropriately and we just have not had the time to have needed changes show up. Nothing is not working yet, just missing stuff; we just need more questions and the timer to actually be made. We are also needing the difficulty feature to be created and finalized.

## What does not work yet
The main remaining items are:
- Difficulty selection is not implemented.
- Questions do not yet store difficulty information.
- The question-loading process does not filter questions by difficulty.


## Next Steps
D3 Plan:
Nothing is left from our MVP as our MVP has been appropriately transcribed into our program accurately from our specification document. Nothing was left behind and all stated goals have been achieved. One non-trivial feature that we will be adding is our time limit on each question alongside a power up that can help in increasing the time margin. It will touch mainly the model, more accurately in the question class but it will be transferred to the view to be seen. Nothing will need much changing to implement this, it would just be an add-on feature that can fit in with the rest of the program fluently. We will be attempting all of them: the difficulty setting, (originally) the shop which was accomplished, more power ups, etc. New test methods will need to be incorporated to detect and establish the new power ups, the time limit and to ensure it actually scales and is detected to be zero, and increasing the shop interface so that selecting new shop items do not result in error. Everyone will test their own items and we will stick to the original specification document when it comes to creating the stretch goals. Colin will work on incorporating the power ups to Player, Jonathan can work on the new questions, Lena can work on the actual new power ups, and Ashutosh can work on the time countdown and incorporation into view.

D4 update:
What is left for the final build?
- Add a difficulty field to Question.
- Update QuestionList and the question-file format to support difficulty.
- Add Easy, Medium, and Hard selection through Main.
- Make sure the question queue contains questions matching the selected difficulty.



## Team Work
- **Jonathan:** `Question` and `QuestionList`
- **Colin:** `Player` and power-ups
- **Lena:** `Shop` and purchasing
- **Ashutosh:** `Main`, integration, and testing

## Three-Minute Demo Plan
Our demonstration will follow this order:
1. Start the game and show that questions load from the text file.
2. Demonstrate answering a question correctly and incorrectly.
3. Show how points and lives change.
4. Visit the shop and demonstrate purchasing a power-up.
5. Use a power-up, such as SKIP or DOUBLE.
6. Demonstrate the difficulty selection feature once it has been implemented.
7. Briefly show the model tests and explain how they test the model independently of the view.


## GenAI Note
We used AI to help think through invalid input handling and how the classes should divide responsibilities. The team reviewed those suggestions and used them when planning the project.
