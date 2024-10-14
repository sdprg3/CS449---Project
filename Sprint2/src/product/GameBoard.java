package product;

import javafx.scene.control.Button;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

public class GameBoard {

  private int size;
  private GridPane gameBoard;
  private GameLogic gameLogic;
  private GameController gameController;
  private char[][] board;

  public GameBoard(int size, GridPane gameBoard, char gameMode, GameController gameController) {
    this.size = size;
    this.gameBoard = gameBoard;
    this.board = new char[size][size];
    this.gameController = gameController;
    this.gameLogic = new GameLogic(size, gameMode);  // Initialize the char board
    
    initializeBoard();
  }

  // Updates the board with the player's move.
  public void updateBoard(int row, int col, char letter) {
    board[row][col] = letter;
  }

  // Resets the board state.
  public void resetBoardState() {
    gameBoard.getChildren().clear();  // Clears existing buttons.
    gameBoard.getColumnConstraints().clear();  // Clears column constraints.
    gameBoard.getRowConstraints().clear();  // Clears row constraints.
    board = new char[size][size];

    // Reset score display.
    gameController.redScoreDisplay.setText("Score: " + gameLogic.getRedScore());
    gameController.blueScoreDisplay.setText("Score: " + gameLogic.getBlueScore());
  }

  // Initializes the game board.
  public void initializeBoard() {
    resetBoardState();  // Clears the board.

    for (int col = 0; col < size; col++) {
      ColumnConstraints colConstr = new ColumnConstraints();
      colConstr.setPercentWidth(100.0 / size);  // Equal column width.
      gameBoard.getColumnConstraints().add(colConstr);

      RowConstraints rowConstr = new RowConstraints();
      rowConstr.setPercentHeight(100.0 / size);  // Equal row height.
      gameBoard.getRowConstraints().add(rowConstr);

      for (int row = 0; row < size; row++) {
        Button button = new Button();
        button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);  // Button expands fully.
        button.setId("button_" + row + "_" + col);  // Unique ID for each button.

        final int finalRow = row;
        final int finalCol = col;
        button.setOnAction(e -> handleButtonClick(finalRow, finalCol, button));

        gameBoard.add(button, col, row);  // Add button to grid.
      }
    }

    gameBoard.setHgap(10);  // Horizontal gap between cells.
    gameBoard.setVgap(10);  // Vertical gap between cells.
    gameBoard.setGridLinesVisible(true);  // Display grid lines for clarity.
    gameBoard.setStyle("-fx-border-color: black; -fx-border-width: 2px;");  // Set black grid border.
  }

  // Returns the current board state.
  public char[][] getBoard() {
    return board;
  }

  // Handles the button click event.
  public void handleButtonClick(int row, int col, Button button) {
    String selectedLetter = "";
    String color = "";

    if (gameLogic.getCurrentPlayer() == 'B') {
      color = "blue";
      if (gameController.blueSButton.isSelected()) {
        selectedLetter = "S";
      } 
      else if (gameController.blueOButton.isSelected()) {
        selectedLetter = "O";
      }
    } 
    else if (gameLogic.getCurrentPlayer() == 'R') {
      color = "red";
      if (gameController.redSButton.isSelected()) {
        selectedLetter = "S";
      } 
      else if (gameController.redOButton.isSelected()) {
        selectedLetter = "O";
      }
    }

    boolean validMove = gameLogic.handlePlayerMove(row, col, selectedLetter);

    if (validMove) {
      updateBoard(row, col, selectedLetter.charAt(0));
      button.setText(selectedLetter);
      button.setStyle(String.format("-fx-text-fill: %s; -fx-font-size: 18px;", color));
      gameController.infoDisplay.setText("Current Turn: " + (gameLogic.getCurrentPlayer() == 'B' ? "Blue" : "Red"));

      if (gameLogic.isGameOver()) {
        disableButtons();
        gameController.infoDisplay.setText(
            "Game Over! Final Scores - Blue: " + gameLogic.getBlueScore() + ", Red: " + gameLogic.getRedScore());
      }
    } 
    else {
      gameController.infoDisplay.setText("Current Turn: " + (gameLogic.getCurrentPlayer() == 'B' ? "Blue" : "Red"));
      gameController.infoDisplay.setText(gameController.infoDisplay.getText() + " Invalid move. Try again.");
    }

    gameController.redScoreDisplay.setText("Score: " + gameLogic.getRedScore());
    gameController.blueScoreDisplay.setText("Score: " + gameLogic.getBlueScore());
  }

  // Disables all buttons on the game board.
  public void disableButtons() {
    for (javafx.scene.Node node : gameBoard.getChildren()) {
      if (node instanceof Button) {
        Button button = (Button) node;
        button.setDisable(true);
      }
    }
  }
}
