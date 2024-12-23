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
        this.boardSize = 3;
        this.gameMode = null; //null
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

    // Method to switch the current player between blue and red
    public void switchPlayer() {
        currentPlayer = (currentPlayer == bluePlayer) ? redPlayer : bluePlayer;
    }

    // Setter for game progress. 
    public void setGameProgress(boolean inProgress) {
        //Enable or disable some UI component related to starting game
        startButton.setDisable(inProgress); 
        boardSizeInput.setDisable(inProgress);
        simpleModeButton.setDisable(inProgress); 
        generalModeButton.setDisable(inProgress);

        //Enable or disable radio buttons for player type
        blueComputerButton.setDisable(inProgress); 
        redComputerButton.setDisable(inProgress); 
        blueHumanButton.setDisable(inProgress); 
        redHumanButton.setDisable(inProgress);

        // Set the game in progress flag
        isGameInProgress = inProgress; 
    }

    // Update the scoreboard display for both players
    public void refreshScoreBoard() {
        blueScoreDisplay.setText("Score: " + getBluePlayer().getScore());
        redScoreDisplay.setText("Score: " + getRedPlayer().getScore());
    }

    // Initializes listeners for the blue and red player type selection buttons (human or computer). 
    // Listeners allow the program to respond to different events in real time. (Like mouse click for example. but we are using radio button for this case)
    @FXML
    public void initialize() {
        // Set player type for blue/red players: configure human or computer options
        setPlayerType(bluePlayer, blueHumanButton, blueComputerButton, blueSButton, blueOButton);
        setPlayerType(redPlayer, redHumanButton, redComputerButton, redSButton, redOButton);

        // when simple or general button is clicked, mode display will show the game
        simpleModeButton.setOnAction(event -> { modeDisplay.setText("Mode: Simple"); }); 
        generalModeButton.setOnAction(event -> { modeDisplay.setText("Mode: General"); });

        // Add a listener to the boardSizeInput TextField for real-time validation
        boardSizeInput.textProperty().addListener((observable, oldValue, newValue) -> {
            setBoardSize();
        });
    }

    // Helper method to set player type (Human or Computer)
    private void setPlayerType(Player player, RadioButton humanButton, RadioButton computerButton, RadioButton sButton, RadioButton oButton) {
        computerButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                sButton.setDisable(true);
                oButton.setDisable(true);
                player.setPlayerComputer();
                if (gameMode != null && getCurrentPlayer().getPlayerColor() == player.getPlayerColor()) {
                    gameMode.computerLogic.manageComputerMove();
                }
            }
        });

        humanButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                sButton.setDisable(false);
                oButton.setDisable(false);
                player.setPlayerHuman();
            }
        });
    }

    // Validates board size input to ensure it falls within the allowed range (3-10)
    public void setBoardSize() {
        try {
            boardSize = Integer.parseInt(boardSizeInput.getText());
            if (boardSize >= 3 && boardSize <= 10) {
                infoDisplay.setText("Valid Size Input"); // Clear any previous error message
            } else {
                infoDisplay.setText("Please enter a valid number between 3 and 10.");
            }
        } catch (NumberFormatException e) {
            infoDisplay.setText("Please enter a valid number.");
        }
    }

    //set Game Mode
    private boolean createValidGame() {
        if (boardSize >= 3 && boardSize <= 10) {
            if (simpleModeButton.isSelected()) {
                gameMode = new SimpleGame(this, boardVisual);
                return true;
            }
            else if (generalModeButton.isSelected()) {
                gameMode = new GeneralGame(this, boardVisual);
                return true;
            }
            return false;
        }
        return false;
    }

    // Starts the game 
    @FXML
    public void startGame(ActionEvent event) {
        if (isGameInProgress) return; // If game in progress, returns so button does nothing
        setGameProgress(true); // Set game progress to true

        // Check if a valid game is created
        if (createValidGame()) {
            // Set starting player to blue 
            currentPlayer = bluePlayer; 

            //Reset scores and refresh scoreBoard
            bluePlayer.resetScore();
            redPlayer.resetScore();
            refreshScoreBoard();
            
            // Display message to game has started.
            infoDisplay.setText("Current Turn: " + getCurrentPlayer().getPlayerColor() + " Player");
            infoDisplay.setText(infoDisplay.getText() + "\nStarting Game!");

            // Tries to process the 1st move automatically if player is computer, else it does nothing
            gameMode.getComputerLogic().manageComputerMove(); 
        } else {
            // Set game progress to false if gameMode or boardSize aren't validated
            setGameProgress(false);
        }
    }
}