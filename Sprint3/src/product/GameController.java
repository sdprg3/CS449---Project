package product;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class GameController {

    // FXML UI Components
    @FXML
    public RadioButton blueSButton, blueOButton, redSButton, redOButton, simpleModeButton, generalModeButton;
    @FXML
    public RadioButton redHumanButton, redComputerButton, blueHumanButton, blueComputerButton;
    @FXML
    public TextField boardSizeInput;
    @FXML
    public GridPane boardVisual;
    @FXML
    public Text infoDisplay, modeDisplay, redScoreDisplay, blueScoreDisplay;

    // Game state variables
    private int boardSize = 3; // Default board size
    private Game gameMode;
    private Player bluePlayer;
    private Player redPlayer;
    private Player currentPlayer;

    // Constructor to initialize default players and the starting player
    public GameController() {
        this.bluePlayer = new Player(Player.PlayerColor.Blue, Player.PlayerType.HUMAN); // Default blue player
        this.redPlayer = new Player(Player.PlayerColor.Red, Player.PlayerType.HUMAN);   // Default red player
        this.currentPlayer = bluePlayer; // Set starting player to blue by default
    }

    // Getters for controller properties
    public int getSize() {
        return boardSize;
    }

    public Player getCurrentPlayer() {
        return currentPlayer; 
    }

    public Player getBluePlayer() {
        return bluePlayer; 
    }

    public Player getRedPlayer() {
        return redPlayer; 
    }

    // Setters for blue and red players
    public void setBluePlayer(Player bluePlayer) {
        this.bluePlayer = bluePlayer;
    }

    public void setRedPlayer(Player redPlayer) {
        this.redPlayer = redPlayer;
    }

    // Method to switch the current player between blue and red
    public void switchPlayer() {
        currentPlayer = (currentPlayer == bluePlayer) ? redPlayer : bluePlayer;
    }

    // Update the scoreboard display for both players
    public void refreshScoreBoard() {
        blueScoreDisplay.setText("Score: " + getBluePlayer().getScore());
        redScoreDisplay.setText("Score: " + getRedPlayer().getScore());
    }

    // FXML Event Handlers

    // Validates and assigns player types (human or computer) based on selected radio buttons
    @FXML
    public void validatePlayers(ActionEvent event) {
        try {
            if (redHumanButton.isSelected()) {
                bluePlayer = new Player(Player.PlayerColor.Blue, Player.PlayerType.HUMAN);
            } else if (redComputerButton.isSelected()) {
                bluePlayer = new Player(Player.PlayerColor.Blue, Player.PlayerType.COMPUTER);
            }

            if (blueHumanButton.isSelected()) {
                redPlayer = new Player(Player.PlayerColor.Red, Player.PlayerType.HUMAN);
            } else if (blueComputerButton.isSelected()) {
                redPlayer = new Player(Player.PlayerColor.Red, Player.PlayerType.COMPUTER);
            }

            // Set the starting player to blue by default
            this.currentPlayer = bluePlayer;

        } catch (Exception e) {
            e.printStackTrace(); // Log error details for debugging
            infoDisplay.setText("An error occurred while validating players.");
        }
    }

    // Validates board size input to ensure it falls within the allowed range (3-10)
    @FXML
    public boolean validateBoardSize(ActionEvent event) {
        try {
            int inputSize = Integer.parseInt(boardSizeInput.getText());
            boardSize = inputSize;

            if (inputSize < 3 || inputSize > 10) {
                infoDisplay.setText("Please enter a valid number between 3 and 10.");
                return false;
            } else {
                infoDisplay.setText(""); // Clear any previous error message
                return true;
            }
        } catch (NumberFormatException e) {
            infoDisplay.setText("Please enter a valid number for board size.");
            return false;
        }
    }

    // Validates and sets the game mode based on selected mode radio buttons
    @FXML
    public boolean validateGameMode(ActionEvent event) {
        if (simpleModeButton.isSelected()) {
            gameMode = new SimpleGame(this, boardVisual);
            modeDisplay.setText("Mode: Simple");
            return true;
        } 
        else if (generalModeButton.isSelected()) {
            gameMode = new GeneralGame(this, boardVisual);
            modeDisplay.setText("Mode: General");
            return true;
        }
        else {
            modeDisplay.setText("Mode: Not Selected");
            return false;
        }
    }

    // Starts the game after validating game mode, players, and board size
    @FXML
    public void startGame(ActionEvent event) {
        validatePlayers(event);
        boolean validBoardSize = validateBoardSize(event);
        boolean validGameMode = validateGameMode(event);

        if (validBoardSize && validGameMode) {
            infoDisplay.setText("Current Turn: " + getCurrentPlayer().getPlayerColor() + " Player");
            infoDisplay.setText(infoDisplay.getText() + "\nStarting Game!");
        } else {
            if (gameMode != null) {
                gameMode.board.resetBoardState(); // Reset board if validations fail
            }
        }
    }
}