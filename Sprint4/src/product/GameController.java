package product;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
    @FXML
    public Button startButton;

    // Game state variables
    private int boardSize;
    private Game gameMode;
    private Player bluePlayer;
    private Player redPlayer;
    private Player currentPlayer;
    private boolean isGameInProgress; 

    // Constructor to initialize default players and the starting player
    public GameController() {
        this.bluePlayer = new Player(Player.PlayerColor.Blue, Player.PlayerType.HUMAN); // Default blue player
        this.redPlayer = new Player(Player.PlayerColor.Red, Player.PlayerType.HUMAN);   // Default red player
        this.currentPlayer = bluePlayer; // Set starting player to blue by default
        this.isGameInProgress = false; // Flag to track if the game is in progress
        this.boardSize = 3; // Default board size
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

    public boolean getGameProgress() {
        return isGameInProgress;
    }

    // Setters for blue and red players
    public void setBluePlayer(Player bluePlayer) {
        this.bluePlayer = bluePlayer;
    }

    public void setRedPlayer(Player redPlayer) {
        this.redPlayer = redPlayer;
    }

    // Setter for game progress
    public void setGameProgress(boolean inProgress) {
        //Enable or disable some UI component related to starting game
        startButton.setDisable(inProgress); 
        simpleModeButton.setDisable(inProgress); 
        generalModeButton.setDisable(inProgress);
        boardSizeInput.setDisable(inProgress);
        isGameInProgress = inProgress; // Set the game in progress flag
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

    // Initializes listeners for the blue and red player type selection buttons (human or computer). 
    // Listeners allow the program to respond to different events in real time. (Like mouse click for example. but we are using radio button for this case)
    // Also made it anti-spam, meaning you can try to spam the radio buttons for all you want.
    @FXML
    public void initialize() {
        // Add a listener to blueComputerButton to disable blueSButton and blueOButton, and set player to be computer
        // If game in progress, then it automatically process computer Move
        blueComputerButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) { // blueComputerButton is selected
                if (!blueSButton.isDisabled() && !blueOButton.isDisabled()) {
                    blueSButton.setDisable(true);
                    blueOButton.setDisable(true);
                    bluePlayer.setPlayerComputer();
                    if (gameMode != null && getCurrentPlayer() == getBluePlayer()) {
                        gameMode.computerLogic.processComputerMove();
                    }
                }
            }
        });

        // Add a listener to blueHumanButton to enable blueSButton and blueOButton, and set player to be Human
        blueHumanButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) { // blueHumanButton is selected
                if (blueSButton.isDisabled() && blueOButton.isDisabled()) {
                    blueSButton.setDisable(false);
                    blueOButton.setDisable(false);
                    bluePlayer.setPlayerHuman();
                }
            }
        });

        // Add a listener to redComputerButton to disable redSButton and redOButton, and set player to be computer
        // If game in progress, then it automatically process computer Move
        redComputerButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) { // redComputerButton is selected
                if (!redSButton.isDisabled() && !redOButton.isDisabled()) {
                    redSButton.setDisable(true);
                    redOButton.setDisable(true);
                    redPlayer.setPlayerComputer();
                    if (gameMode != null && getCurrentPlayer() == getRedPlayer()) {
                        gameMode.computerLogic.processComputerMove();
                    }
                }
            }
        });

        // Add a listener to redHumanButton to enable redSButton and redOButton, and set player to be Human
        redHumanButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) { // redHumanButton is selected
                if (redSButton.isDisabled() && redOButton.isDisabled()) {
                    redSButton.setDisable(false);
                    redOButton.setDisable(false);
                    redPlayer.setPlayerHuman();
                }
            }
        });
    }

    // Validates and assigns player types (human or computer) based on selected radio buttons
    @FXML
    public void validatePlayers(ActionEvent event) {
        try {
            if (blueHumanButton.isSelected()) {
                bluePlayer = new Player(Player.PlayerColor.Blue, Player.PlayerType.HUMAN);
            } else if (blueComputerButton.isSelected()) {
                bluePlayer = new Player(Player.PlayerColor.Blue, Player.PlayerType.COMPUTER);
            }

            if (redHumanButton.isSelected()) {
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
        if (isGameInProgress) return; // If game in progress, returns so button does nothing
        setGameProgress(true); // Set game progress to true

        validatePlayers(event);
        boolean validBoardSize = validateBoardSize(event);
        boolean validGameMode = validateGameMode(event);

        if (validBoardSize && validGameMode) {
            infoDisplay.setText("Current Turn: " + getCurrentPlayer().getPlayerColor() + " Player");
            infoDisplay.setText(infoDisplay.getText() + "\nStarting Game!");
            gameMode.getComputerLogic().processComputerMove(); // Tries to process the 1st move automatically if player is computer, else it does nothing
        } else {
            // Set game progress to false and reset board if gameMode or boardSize aren't validated
            if (gameMode != null) gameMode.board.resetBoardState(); // Reset board if validations fail
            setGameProgress(false);
        }
    }
}