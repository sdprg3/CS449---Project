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
    public GameLogic(Game game, GameController gameController, Board board) {
        this.game = game;
        this.gameController = gameController;
        this.boardState = board.getBoardState();
        this.board = board;
        this.boardSize = gameController.getSize();
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
        // Returns if game progress is false.
        if (!gameController.getGameProgress()) return; 

        String selectedLetter = "";
        String color = "";

        // Set color and selected letter based on the current player
        if (gameController.getCurrentPlayer() == gameController.getBluePlayer()) {
            color = "blue";
            selectedLetter = gameController.blueSButton.isSelected() ? "S" : "O";
        } else {
            color = "red";
            selectedLetter = gameController.redSButton.isSelected() ? "S" : "O";
        }

        // Place move if valid, otherwise display an invalid move message
        if (isValidMove(row, col)) {
            placeMove(row, col, selectedLetter);
            button.setText(selectedLetter);
            button.setStyle(String.format("-fx-text-fill: %s; -fx-font-size: 18px;", color));
            game.handleGameModeLogic(checkAndUpdateScore(row, col));  // After checking if player scored or not, then current game mode rule will decide the next course of action.

        } else gameController.infoDisplay.setText(String.format("Current Turn: %s Player\nInvalid move. Try again.", gameController.getCurrentPlayer().getPlayerColor()));
        
    }

    // Checks and updates score based on row, column, and diagonals for an "SOS"
    public boolean checkAndUpdateScore(int row, int col) {
        boolean didPlayerScore = false;

        // Checks if player found/score a SOS sequence or not. However, even if scored, it must still check for other direction.
        if (checkAndScoreRowForSOS(row)) didPlayerScore = true;
        if (checkAndScoreColumnForSOS(col)) didPlayerScore = true;
        if (checkAndScorePrimaryDiagonalForSOS(row, col)) didPlayerScore = true;
        if (checkAndScoreSecondaryDiagonalForSOS(row, col)) didPlayerScore = true;

        return didPlayerScore;
    }

    // Verifies if a given row and column are within board bounds
    private boolean isValidIndex(int row, int col) {
        return row >= 0 && row < boardSize && col >= 0 && col < boardSize;
    }

    // Marks a sequence from (startRow, startCol) to (endRow, endCol) as scored (directional)
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
    private boolean checkAndScoreRowForSOS(int row) {
        boolean foundSOS = false;

        // If sos is found, it can keep checking for more sos sequence.
        for (int j = 0; j <= boardSize - 3; j++) {
            if (isScored(row, j, row, j + 2)) continue;

            String sequence = boardState[row][j] + boardState[row][j + 1] + boardState[row][j + 2];
            if ("SOS".equals(sequence)) {
                foundSOS = true;
                gameController.getCurrentPlayer().increaseScore(1);
                markSequenceAsScored(row, j, row, j + 2);
                board.drawScoredLine(row, j, row, j + 2, gameController.getCurrentPlayer());
            }
        }
        return foundSOS; // return if SOS is found or not.
    }

    // Checks for "SOS" in the specified column
    private boolean checkAndScoreColumnForSOS(int col) {
        boolean foundSOS = false;

        // If sos is found, it can keep checking for more sos sequence.
        for (int i = 0; i <= boardSize - 3; i++) {
            if (isScored(i, col, i + 2, col)) continue;

            String sequence = boardState[i][col] + boardState[i + 1][col] + boardState[i + 2][col];
            if ("SOS".equals(sequence)) {
                foundSOS = true;
                gameController.getCurrentPlayer().increaseScore(1);
                markSequenceAsScored(i, col, i + 2, col);
                board.drawScoredLine(i, col, i + 2, col, gameController.getCurrentPlayer());
            }
        }
        return foundSOS; // return if SOS is found or not.
    }

    // Checks for "SOS" in the primary diagonal (top-left to bottom-right)
    private boolean checkAndScorePrimaryDiagonalForSOS(int row, int col) {
        boolean foundSOS = false;

        // If sos is found, it can keep checking for more sos sequence.
        for (int i = -2; i <= 0; i++) {
            int startRow = row + i;
            int startCol = col + i;
            if (isValidIndex(startRow, startCol) && isValidIndex(startRow + 2, startCol + 2)) {
                if (isScored(startRow, startCol, startRow + 2, startCol + 2)) continue;

                String sequence = boardState[startRow][startCol] + boardState[startRow + 1][startCol + 1] + boardState[startRow + 2][startCol + 2];
                if ("SOS".equals(sequence)) {
                    foundSOS = true;
                    gameController.getCurrentPlayer().increaseScore(1);
                    markSequenceAsScored(startRow, startCol, startRow + 2, startCol + 2);
                    board.drawScoredLine(startRow, startCol, startRow + 2, startCol + 2, gameController.getCurrentPlayer());
                }
            }
        }
        return foundSOS; // return if SOS is found or not.
    }

    // Checks for "SOS" in the secondary diagonal (bottom-left to top-right)
    private boolean checkAndScoreSecondaryDiagonalForSOS(int row, int col) {
        boolean foundSOS = false;

        // If sos is found, it can keep checking for more sos sequence.
        for (int i = -2; i <= 0; i++) {
            int startRow = row + i;
            int startCol = col - i;
            if (isValidIndex(startRow, startCol) && isValidIndex(startRow + 2, startCol - 2)) {
                if (isScored(startRow, startCol, startRow + 2, startCol - 2)) continue;

                String sequence = boardState[startRow][startCol] + boardState[startRow + 1][startCol - 1] + boardState[startRow + 2][startCol - 2];
                if ("SOS".equals(sequence)) {
                    foundSOS = true;
                    gameController.getCurrentPlayer().increaseScore(1);
                    markSequenceAsScored(startRow, startCol, startRow + 2, startCol - 2);
                    board.drawScoredLine(startRow, startCol, startRow + 2, startCol - 2, gameController.getCurrentPlayer());
                }
            }
        }
        return foundSOS; // return if SOS is found or not.
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
        return true; // No empty cells found, board is full
    }
}