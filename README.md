# QuizCardPlayer

QuizCardPlayer is a Java Swing application that allows users to load a set of flashcards and review them. Users can mark cards as "unknown" and review those cards separately.

## Features

- Load a set of flashcards from a file.
- Display questions and answers one by one.
- Mark cards as "unknown" for further review.
- Review the entire card set again.
- Review only the "unknown" cards.

## Requirements

- Java Development Kit (JDK) 8 or higher

## How to Run

1. Clone the repository or download the source code.
2. Open a terminal and navigate to the directory containing the source code.
3. Compile the Java files:
    ```sh
    javac QuizCardPlayer.java
    ```
4. Run the application:
    ```sh
    java QuizCardPlayer
    ```

## Usage

1. When the application starts, it will display the first question from the loaded flashcard set.
2. Click the "Show Answer" button to display the answer to the current question.
3. If you do not know the answer, click the "Unknown" button to mark the card for further review.
4. Continue through the flashcards by clicking "Show Answer" and marking unknown cards as needed.
5. Once you reach the end of the card set, you can choose to:
    - Review the entire card set again by clicking "Review Cards Again".
    - Review only the unknown cards by clicking "Review Unknown Cards".

## Code Structure

- `QuizCardPlayer.java`: Main class that sets up the GUI and handles the logic for displaying and reviewing flashcards.
- `QuizCard.java`: Class representing a single flashcard with a question and an answer.