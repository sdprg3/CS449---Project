package product;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Button;

public class GameLogic {

    private GameController gameController;
    private Board board;
    private String[][] boardState;
    private Game game;
    private int boardSize;
    
    // Tracks already scored sequences (stores starting and ending positions)
    private List<int[][]> scoredSequences = new ArrayList<>();

    // Constructor initializes game logic components
    public GameLogic(Board gameBoard, GameController gameController, Game game) {
        this.board = gameBoard;
        this.boardState = gameBoard.getBoardState();
        this.boardSize = gameController.getSize();
        this.gameController = gameController;
        this.game = game;
    }

    // Checks if a move is valid by verifying if the cell is empty
    public boolean isValidMove(int row, int col) {
        return boardState[row][col].isEmpty();
    }

    // Places the move on the board if valid
    public void placeMove(int row, int col, String move) {
        if (isValidMove(row, col)) {
            boardState[row][col] = move; // Update board with player's move
        }
    }

    // Handles button click logic within the board
    public void handleButtonClick(int row, int col, Button button) {
        String selectedLetter = "";
        String color = "";

        // Set color and selected letter based on the current player
        if (gameController.getCurrentPlayer() == gameController.getBluePlayer()) {
            color = "blue";
            selectedLetter = gameController.blueSButton.isSelected() ? "S" : "O";
        } else if (gameController.getCurrentPlayer() == gameController.getRedPlayer()) {
            color = "red";
            selectedLetter = gameController.redSButton.isSelected() ? "S" : "O";
        }

        // Place move if valid, otherwise display an invalid move message
        if (isValidMove(row, col)) {
            placeMove(row, col, selectedLetter);
            button.setText(selectedLetter);
            button.setStyle(String.format("-fx-text-fill: %s; -fx-font-size: 18px;", color));

            game.handleGameModeLogic(checkAndUpdateScore(row, col));
        } else {
            gameController.infoDisplay.setText(String.format("Current Turn: %s Player\nInvalid move. Try again.",
                gameController.getCurrentPlayer().getPlayerColor()));
        }
    }

    // Checks and updates score based on row, column, and diagonals for an "SOS"
    public boolean checkAndUpdateScore(int row, int col) {
        boolean didPlayerScore = false;

        if (checkRowForSOS(row)) didPlayerScore = true;
        if (checkColumnForSOS(col)) didPlayerScore = true;
        if (checkPrimaryDiagonalForSOS(row, col)) didPlayerScore = true;
        if (checkSecondaryDiagonalForSOS(row, col)) didPlayerScore = true;

        return didPlayerScore;
    }

    // Verifies if a given row and column are within board bounds
    private boolean isValidIndex(int row, int col) {
        return row >= 0 && row < boardSize && col >= 0 && col < boardSize;
    }

    // Marks a sequence from (startRow, startCol) to (endRow, endCol) as scored
    private void markSequenceAsScored(int startRow, int startCol, int endRow, int endCol) {
        scoredSequences.add(new int[][]{{startRow, startCol}, {endRow, endCol}});
    }

    // Checks if a sequence from (startRow, startCol) to (endRow, endCol) has already been scored
    private boolean isScored(int startRow, int startCol, int endRow, int endCol) {
        for (int[][] sequence : scoredSequences) {
            if (sequence[0][0] == startRow && sequence[0][1] == startCol &&
                sequence[1][0] == endRow && sequence[1][1] == endCol) {
                return true;
            }
        }
        return false;
    }

    // Checks for "SOS" in the specified row
    private boolean checkRowForSOS(int row) {
        boolean foundSOS = false;

        for (int j = 0; j <= boardSize - 3; j++) {
            if (isScored(row, j, row, j + 2)) continue;

            String sequence = boardState[row][j] + boardState[row][j + 1] + boardState[row][j + 2];
            if ("SOS".equals(sequence)) {
                foundSOS = true;
                markSequenceAsScored(row, j, row, j + 2);
                gameController.getCurrentPlayer().increaseScore(1);
                board.drawWinningLine(row, j, row, j + 2, gameController.getCurrentPlayer());
            }
        }
        return foundSOS;
    }

    // Checks for "SOS" in the specified column
    private boolean checkColumnForSOS(int col) {
        boolean foundSOS = false;

        for (int i = 0; i <= boardSize - 3; i++) {
            if (isScored(i, col, i + 2, col)) continue;

            String sequence = boardState[i][col] + boardState[i + 1][col] + boardState[i + 2][col];
            if ("SOS".equals(sequence)) {
                foundSOS = true;
                markSequenceAsScored(i, col, i + 2, col);
                gameController.getCurrentPlayer().increaseScore(1);
                board.drawWinningLine(i, col, i + 2, col, gameController.getCurrentPlayer());
            }
        }
        return foundSOS;
    }

    // Checks for "SOS" in the primary diagonal (top-left to bottom-right)
    private boolean checkPrimaryDiagonalForSOS(int row, int col) {
        boolean foundSOS = false;

        for (int i = -2; i <= 0; i++) {
            int startRow = row + i;
            int startCol = col + i;
            if (isValidIndex(startRow, startCol) && isValidIndex(startRow + 2, startCol + 2)) {
                if (isScored(startRow, startCol, startRow + 2, startCol + 2)) continue;

                String sequence = boardState[startRow][startCol] + boardState[startRow + 1][startCol + 1] + boardState[startRow + 2][startCol + 2];
                if ("SOS".equals(sequence)) {
                    foundSOS = true;
                    markSequenceAsScored(startRow, startCol, startRow + 2, startCol + 2);
                    gameController.getCurrentPlayer().increaseScore(1);
                    board.drawWinningLine(startRow, startCol, startRow + 2, startCol + 2, gameController.getCurrentPlayer());
                }
            }
        }
        return foundSOS;
    }

    // Checks for "SOS" in the secondary diagonal (bottom-left to top-right)
    private boolean checkSecondaryDiagonalForSOS(int row, int col) {
        boolean foundSOS = false;

        for (int i = -2; i <= 0; i++) {
            int startRow = row + i;
            int startCol = col - i;
            if (isValidIndex(startRow, startCol) && isValidIndex(startRow + 2, startCol - 2)) {
                if (isScored(startRow, startCol, startRow + 2, startCol - 2)) continue;

                String sequence = boardState[startRow][startCol] + boardState[startRow + 1][startCol - 1] + boardState[startRow + 2][startCol - 2];
                if ("SOS".equals(sequence)) {
                    foundSOS = true;
                    markSequenceAsScored(startRow, startCol, startRow + 2, startCol - 2);
                    gameController.getCurrentPlayer().increaseScore(1);
                    board.drawWinningLine(startRow, startCol, startRow + 2, startCol - 2, gameController.getCurrentPlayer());
                }
            }
        }
        return foundSOS;
    }

    // Checks if the board is full
    public boolean isBoardFull() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (boardState[i][j].isEmpty()) {
                    return false; // Found an empty cell
                }
            }
        }
        System.out.println("Game Over! The board is full!");
        return true; // No empty cells found, board is full
    }
}