package product;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class GameController {

    // FXML Components
    @FXML
    public RadioButton blueSButton, blueOButton, redSButton, redOButton, simpleModeButton, generalModeButton;
    @FXML
    public TextField boardSize;
    @FXML
    public GridPane gameBoard;
    @FXML
    public Text infoDisplay, modeDisplay, redScoreDisplay, blueScoreDisplay;

    // Private fields
    private GameBoard board;
    private int size = 3; // Default size
    private char gameMode = 'S'; // 'S' for Simple, 'G' for General

    // Getters and Setters
    public int getSize() {
        return size;
    }

    public void setGameBoard(GridPane board) {
        this.gameBoard = board;
    }

    // FXML Methods
    @FXML
    public void validateBoardSizeInput(ActionEvent event) {
        // Validate board size and provide feedback to the user
        try {
            int inputSize = Integer.parseInt(boardSize.getText());

            if (inputSize < 3 || inputSize > 10) {
                infoDisplay.setText("Board size must be between 3 and 10!");
                size = 0; // Invalidate size to prevent game start
            } 
            else {
                size = inputSize;
                infoDisplay.setText("Starting Game! Current Turn: Blue");
            }
        } 
        catch (NumberFormatException e) {
            infoDisplay.setText("Please enter a valid number for board size.");
            size = 0; // Invalidate size to prevent game start
        }
    }

    @FXML
    public void validateGameMode(ActionEvent event) {
        // Set game mode to Simple or General
        if (simpleModeButton.isSelected()) {
            gameMode = 'S'; // Simple mode
        } 
        else if (generalModeButton.isSelected()) {
            gameMode = 'G'; // General mode
        }
        modeDisplay.setText("Mode: " + (gameMode == 'S' ? "Simple" : "General"));
    }

    @FXML
    public void startGame(ActionEvent event) {
        // Validate gameMode and board size before starting the game
        validateBoardSizeInput(event);
        validateGameMode(event);
        if (size >= 3) {
            // Initialize a new Board instance
            board = new GameBoard(size, gameBoard, gameMode, this);
            board.initializeBoard(); // Initialize the board grid based on size
        } 
        else {
            board.resetBoardState();
            modeDisplay.setText("Mode:");
        }
    }
}
