package product;

/**
 * GameLogic class handles the game logic for an SOS game.
 * It manages the board, player moves, and score tracking for both players.
 */
public class GameLogic {

    private char[][] board;
    private int size;
    private char gameMode; // 'S' for Simple mode, 'G' for General mode
    private char currentPlayer; // 'B' for Blue, 'R' for Red
    private int blueScore, redScore;
    private boolean gameOver;

    // Arrays to track scoring state
    private boolean[] rowScored;
    private boolean[] colScored;
    private boolean diagonal1Scored; // Top-left to bottom-right diagonal
    private boolean diagonal2Scored; // Top-right to bottom-left diagonal

    //Constructor to initialize game logic
    public GameLogic(int size, char gameMode) {
        this.size = size;
        this.board = new char[size][size];
        this.currentPlayer = 'B'; // Blue starts first
        this.gameMode = gameMode; 
        this.blueScore = 0;
        this.redScore = 0;
        this.gameOver = false;

        this.rowScored = new boolean[size]; // Initialize row scoring state
        this.colScored = new boolean[size]; // Initialize column scoring state
        this.diagonal1Scored = false; // Initialize diagonal scoring state
        this.diagonal2Scored = false;
    }

    // Method to handle the player's move by placing the letter and updating scores
    public boolean handlePlayerMove(int row, int col, String selectedLetter) {
        if (board[row][col] == '\u0000') { // If the cell is empty
            char letter = selectedLetter.charAt(0);
            board[row][col] = letter; // Place the letter

            // boolean if there was a sos sequence. upScore also already increment points
            boolean scored = updateScore(row, col, letter);

            if (gameMode == 'G' && scored) {
                System.out.println(currentPlayer + " scored point");
                if (isBoardFull()) return true; // Stop the game
                return true; // Player can continue
            } 
            else if (gameMode == 'S' && scored) {
                // In Simple mode, stop the game if a score is made
                System.out.println("Game Over! " + currentPlayer + " wins!");
                gameOver = true;
                return true; // Stop the game
            } 
            else {
                // Switch player if not scored or in Simple mode without score
                switchPlayer(); // Toggle between Blue and Red
                if (isBoardFull()) return true; // Stop the game
                return true; // Valid move
            }
        }
        return false; // Move was not successful (cell was already occupied)
    }

    // Updates the score based on the current move's row, column, and diagonals. Scored SOS sequence cannot be used again
    private boolean updateScore(int row, int col, char letter) {
        boolean scored = false;

        // Check if the row/column/diagonal has already scored
        if (!rowScored[row] && checkRowForSOS(row)) {
            scored = true;
            rowScored[row] = true; // Mark this row as scored
            increaseScore(currentPlayer); // Increment score for the current player
        }

        if (!colScored[col] && checkColumnForSOS(col)) {
            scored = true;
            colScored[col] = true; // Mark this column as scored
            increaseScore(currentPlayer); // Increment score for the current player
        }

        if (!diagonal1Scored && checkDiagonal1ForSOS()) {
            scored = true;
            diagonal1Scored = true; // Mark the top-left to bottom-right diagonal as scored
            increaseScore(currentPlayer); // Increment score for the current player
        }

        if (!diagonal2Scored && checkDiagonal2ForSOS()) {
            scored = true;
            diagonal2Scored = true; // Mark the top-right to bottom-left diagonal as scored
            increaseScore(currentPlayer); // Increment score for the current player
        }

        return scored;
    }

    // Check for "SOS" in the specified row
    private boolean checkRowForSOS(int row) {
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < size; j++) {
            sb.append(board[row][j]);
        }
        return sb.toString().contains("SOS"); // Check if the row contains "SOS"
    }

    // Check for "SOS" in the specified column
    private boolean checkColumnForSOS(int col) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(board[i][col]);
        }
        return sb.toString().contains("SOS"); // Check if the column contains "SOS"
    }

    // Check for "SOS" in the top-left to bottom-right diagonal
    private boolean checkDiagonal1ForSOS() {
        StringBuilder diagonal = new StringBuilder(); // Top-left to bottom-right
        for (int i = 0; i < size; i++) {
            diagonal.append(board[i][i]);
        }
        return diagonal.toString().contains("SOS"); // Check diagonal
    }

    // Check for "SOS" in the top-right to bottom-left diagonal
    private boolean checkDiagonal2ForSOS() {
        StringBuilder diagonal = new StringBuilder(); // Top-right to bottom-left
        for (int i = 0; i < size; i++) {
            diagonal.append(board[i][size - 1 - i]);
        }
        return diagonal.toString().contains("SOS"); // Check diagonal
    }

    // Method to increment the score for the current player
    private void increaseScore(char player) {
        if (player == 'B') {
            blueScore += 1; // Increase Blue score
        } 
        else {
            redScore += 1; // Increase Red score
        }
    }

    // Check if the board is full
    public boolean isBoardFull() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j] == '\u0000') {
                    return false; // Found an empty cell
                }
            }
        }

        System.out.println("Game Over! The board is full!");
        gameOver = true; //gameOver since board is full
        return true; // No empty cells found, board is full
    }

    public int getBlueScore() {
        return blueScore; // Getter for Blue's score
    }

    public int getRedScore() {
        return redScore; // Getter for Red's score
    }

    public boolean isGameOver() {
        return gameOver; // Getter for game over status
    }

    // Switches the current player between Blue and Red.
    private void switchPlayer() {
        currentPlayer = (currentPlayer == 'B') ? 'R' : 'B';
    }

    // Gets current player
    public char getCurrentPlayer() {
        return currentPlayer; // Return current player
    }
}
