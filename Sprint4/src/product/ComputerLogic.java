package product;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.animation.PauseTransition;
import javafx.util.Duration;
import product.Player.PlayerType;

public class ComputerLogic {
    private Board board;
    private GameController gameController;
    private Game game;
    private String[][] boardState;
    private int boardSize;

    //Constructor to initialize computer AI logic
    public ComputerLogic(Game game, GameController gameController, Board board) {
        this.game = game;
        this.gameController = gameController;
        this.boardState = board.getBoardState();
        this.board = board;
        this.boardSize = gameController.getSize();
    }
    
    // Method to handle the computer's turn by finding and making a strategic move> Also includes a brief delay of 1 second for visual effect
    public void manageComputerMove() {
        // Returns if game is not in progress.
        if (!gameController.getGameProgress()) return; 

        //Check if current player is computer
        if (gameController.getCurrentPlayer().getPlayerType() == PlayerType.COMPUTER) {
            // Disable board buttons
            board.setButtonsDisabled(true); 

            // Define an array to hold the SOS position
            int[] position = new int[]{-1, -1}; 
            
            // Check each SOS search method in sequence and stop if a valid move is found
            if (isValidPosition(position = findOpenHorizontalSOSPattern()) ||
                isValidPosition(position = findOpenVerticalSOSPattern()) ||
                isValidPosition(position = findOpenPrimaryDiagonalSOSPattern()) ||
                isValidPosition(position = findOpenSecondaryDiagonalSOSPattern()) ||
                isValidPosition(position = findRandomAvailableSpot())) {
                
                int row = position[0];
                int col = position[1];
    
                // Create a PauseTransition for 1 second (1000 milliseconds)
                PauseTransition pause = new PauseTransition(Duration.seconds(1));
                
                // Set the action to be performed after the pause
                pause.setOnFinished(event -> {
                    game.getGameLogic().handleButtonClick(row, col, board.getButtonAt(row, col)); // Handle the button click after the delay
                    manageComputerMove(); // Recursive call to check for additional moves
                });
    
                // Start the pause
                pause.play();
            }
        }
        // Enable board buttons
        else board.setButtonsDisabled(false);
    }

    // Helper method to check if a position is valid
    private boolean isValidPosition(int[] position) {
        return position[0] != -1 && position[1] != -1;
    }

    //Find valid SOS pattern (horizontal)
    public int[] findOpenHorizontalSOSPattern() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j <= boardSize - 3; j++) {
                // Extract the sequence of three cells
                String firstCell = boardState[i][j];
                String secondCell = boardState[i][j + 1];
                String thirdCell = boardState[i][j + 2];
    
                // Check for the specified patterns
                if (firstCell.isEmpty() && secondCell.equals("O") && thirdCell.equals("S")) {
                    selectSButton();
                    return new int[]{i, j}; // Return position of _
                } 
                else if (firstCell.equals("S") && secondCell.isEmpty() && thirdCell.equals("S")) {
                    selectOButton();
                    return new int[]{i, j + 1}; // Return position of _
                } 
                else if (firstCell.equals("S") && secondCell.equals("O") && thirdCell.isEmpty()) {
                    selectSButton();
                    return new int[]{i, j + 2}; // Return position of _
                }
            }
        }
        return new int[]{-1, -1}; // Return -1, -1 if no "SOS" is found
    }
    
    //Find valid SOS pattern (Vertical)
    public int[] findOpenVerticalSOSPattern() {
        for (int j = 0; j < boardSize; j++) {
            for (int i = 0; i <= boardSize - 3; i++) {
                // Extract the sequence of three cells in a column
                String firstCell = boardState[i][j];
                String secondCell = boardState[i + 1][j];
                String thirdCell = boardState[i + 2][j];
    
                // Check for the specified patterns
                if (firstCell.isEmpty() && secondCell.equals("O") && thirdCell.equals("S")) {
                    selectSButton();
                    return new int[]{i, j}; // Return position of _
                } 
                else if (firstCell.equals("S") && secondCell.isEmpty() && thirdCell.equals("S")) {
                    selectOButton();
                    return new int[]{i + 1, j}; // Return position of _
                } 
                else if (firstCell.equals("S") && secondCell.equals("O") && thirdCell.isEmpty()) {
                    selectSButton();
                    return new int[]{i + 2, j}; // Return position of _
                }
            }
        }
        return new int[]{-1, -1}; // Return -1, -1 if no "SOS" is found
    }
    
    //Find valid SOS pattern (Diagonal. Topleft to bottomRight)
    public int[] findOpenPrimaryDiagonalSOSPattern() {
        for (int i = 0; i <= boardSize - 3; i++) {
            for (int j = 0; j <= boardSize - 3; j++) {
                // Extract the sequence of three cells in the primary diagonal
                String firstCell = boardState[i][j];
                String secondCell = boardState[i + 1][j + 1];
                String thirdCell = boardState[i + 2][j + 2];
    
                // Check for the specified patterns
                if (firstCell.isEmpty() && secondCell.equals("O") && thirdCell.equals("S")) {
                    selectSButton();
                    return new int[]{i, j}; // Return position of _
                } 
                else if (firstCell.equals("S") && secondCell.isEmpty() && thirdCell.equals("S")) {
                    selectOButton();
                    return new int[]{i + 1, j + 1}; // Return position of _
                } 
                else if (firstCell.equals("S") && secondCell.equals("O") && thirdCell.isEmpty()) {
                    selectSButton();
                    return new int[]{i + 2, j + 2}; // Return position of _
                }
            }
        }
        return new int[]{-1, -1}; // Return -1, -1 if no "SOS" is found
    }

    //Find valid SOS pattern (Diagonal. BottomLeft to topRight)
    public int[] findOpenSecondaryDiagonalSOSPattern() {
        for (int i = 0; i <= boardSize - 3; i++) {
            for (int j = 2; j < boardSize; j++) { // Start from column 2 to ensure diagonal stays within bounds
                // Extract the sequence of three cells in the secondary diagonal
                String firstCell = boardState[i][j];
                String secondCell = boardState[i + 1][j - 1];
                String thirdCell = boardState[i + 2][j - 2];
    
                // Check for the specified patterns
                if (firstCell.isEmpty() && secondCell.equals("O") && thirdCell.equals("S")) {
                    selectSButton();
                    return new int[]{i, j}; // Return position of _
                } 
                else if (firstCell.equals("S") && secondCell.isEmpty() && thirdCell.equals("S")) {
                    selectOButton();
                    return new int[]{i + 1, j - 1}; // Return position of _
                } 
                else if (firstCell.equals("S") && secondCell.equals("O") && thirdCell.isEmpty()) {
                    selectSButton();
                    return new int[]{i + 2, j - 2}; // Return position of _
                }
            }
        }
        return new int[]{-1, -1}; // Return -1, -1 if no "SOS" is found
    }
    
    //Find available random spot
    public int[] findRandomAvailableSpot() {
        List<int[]> availableSpots = new ArrayList<>();
        Random random = new Random();
    
        // Collect all available spots
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                if (boardState[row][col].isEmpty()) availableSpots.add(new int[]{row, col});
            }
        }
    
        // If no open spots are available, return [-1, -1]
        if (availableSpots.isEmpty()) return new int[]{-1, -1};
        
    
        // Randomly choose between the two buttons
        if (random.nextInt(2) == 0) selectSButton();
        else selectOButton();
    
        // Select a random spot from the list of available spots
        int randomPosition = random.nextInt(availableSpots.size());
        return availableSpots.get(randomPosition);
    }
    
    //select S Button depending on current Player turn
    public void selectSButton() {
        if (gameController.getCurrentPlayer() == gameController.getBluePlayer()) {
            gameController.blueOButton.setSelected(false); // Deselect blueOButton
            gameController.blueSButton.setSelected(true);
        } else {
            gameController.redOButton.setSelected(false); // Deselect redOButton
            gameController.redSButton.setSelected(true);
        }
    }

    //select O Button depending on current Player turn
    public void selectOButton() {
        if (gameController.getCurrentPlayer() == gameController.getBluePlayer()) {
            gameController.blueSButton.setSelected(false); // Deselect blueSButton
            gameController.blueOButton.setSelected(true);
        } else {
            gameController.redSButton.setSelected(false); // Deselect redSButton
            gameController.redOButton.setSelected(true);
        }
    }
}