package product;

import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import product.Player.PlayerType;

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
    public Button startButton, replayButton;

    // Game state variables
    private int boardSize;
    private Game gameMode;
    private GameRecorder gameRecorder;
    private Player bluePlayer;
    private Player redPlayer;
    private Player currentPlayer;
    private boolean isGameInProgress; 
    private boolean isReplayInProgress; 

    // Constructor to initialize default players and the starting player
    public GameController() {
        this.bluePlayer = new Player(Player.PlayerColor.Blue, Player.PlayerType.HUMAN); // Default blue player
        this.redPlayer = new Player(Player.PlayerColor.Red, Player.PlayerType.HUMAN);   // Default red player
        this.currentPlayer = bluePlayer; // Set starting player to blue by default
        this.gameRecorder = new GameRecorder();
        this.isGameInProgress = false; // Flag to track if the game is in progress
        this.isReplayInProgress = false;
        this.boardSize = 3;
        this.gameMode = null; //null
    }

    // Getters Methods
    public GameRecorder getGameRecorder() {
        return gameRecorder;
    }

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

    public boolean getReplayProgress() { 
        return isReplayInProgress; 
    }

    // Switch Player Method
    public void switchPlayer() {
        currentPlayer = (currentPlayer == bluePlayer) ? redPlayer : bluePlayer;
    }

    // Select S/O Button Methods depending current Player turn
    public void selectSButton() {
        if (getCurrentPlayer() == getBluePlayer()) {
            blueOButton.setSelected(false); // Deselect blueOButton
            blueSButton.setSelected(true);
        } else {
            redOButton.setSelected(false); // Deselect redOButton
            redSButton.setSelected(true);
        }
    }

    public void selectOButton() {
        if (getCurrentPlayer() == getBluePlayer()) {
            blueSButton.setSelected(false); // Deselect blueSButton
            blueOButton.setSelected(true);
        } else {
            redSButton.setSelected(false); // Deselect redSButton
            redOButton.setSelected(true);
        }
    }

    // Setter for game progress. 
    public void setGameProgress(boolean inProgress) {
        //Enable or disable some UI component related to starting game
        startButton.setDisable(inProgress); 
        replayButton.setDisable(inProgress); 
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

    // Setter for replay progress. 
    public void setReplayProgress(boolean inProgress) {
        // Set Game Progress
        setGameProgress(inProgress);

        // Set the game replay in progress flag
        isReplayInProgress = inProgress;
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
        simpleModeButton.setOnAction(event -> { modeDisplay.setText("Simple"); }); 
        generalModeButton.setOnAction(event -> { modeDisplay.setText("General"); });

        // Add a listener to the boardSizeInput TextField for real-time validation
        boardSizeInput.textProperty().addListener((observable, oldValue, newValue) -> {
            setBoardSize(boardSizeInput);
        });
    }

    // Set Player Type Method
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

    // Board Size Validation
    public void setBoardSize(TextField boardSizeInput) {
        try {
            boardSize = Integer.parseInt(boardSizeInput.getText());
            if (boardSize >= 3 && boardSize <= 10) {
                infoDisplay.setText("Valid Board Size"); // Clear any previous error message
            } else {
                infoDisplay.setText("Please enter a valid number between 3 and 10.");
            }
        } catch (NumberFormatException e) {
            infoDisplay.setText("Please enter a valid number.");
        }
    }

    // Creates Valid Game by selected Game Mode and board size
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

    // Start Game Method
    @FXML
    public void startGame(ActionEvent event) {
        setGameProgress(true); // Set game progress to true

        // Check if a valid game is created
        if (createValidGame()) {
            //reset gameRecorder text file. 
            gameRecorder.resetFile();
            //* 
            getGameRecorder().initializeRecording(
                Integer.toString(boardSize),            // Convert boardSize to a string
                modeDisplay.getText(),                              // Pass gameMode directly as a string
                bluePlayer.getPlayerType().toString(),  // Convert bluePlayer's type to a string
                redPlayer.getPlayerType().toString()    // Convert redPlayer's type to a string
            );
            //*/
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

    // Replay Game Method
    @FXML
    public void replayGame(ActionEvent event) {
        // Set Replay Progress to true
        setReplayProgress(true);

        //Disable S and O buttons for all players
        blueSButton.setDisable(true);
        blueOButton.setDisable(true);
        redSButton.setDisable(true);
        redOButton.setDisable(true);

        // Loads game log from text file
        List<String> gameLog = gameRecorder.loadFromFile();

        // Set up game configuration from the recorded moves
        setUpGameConfiguration(gameLog);

        if (createValidGame()) {
            gameMode.getBoard().setButtonsDisabled(true);
            
            // Set starting player to blue 
            currentPlayer = bluePlayer;

            // Reset scores and refresh scoreBoard
            bluePlayer.resetScore();
            redPlayer.resetScore();
            refreshScoreBoard();

            // Handle the replay with a Timeline
            replayMovesWithTimeline(gameLog);
        }
    }

    // Method to handle setting up game configuration based on recorded game Mode, players Type, and boardsize
    private void setUpGameConfiguration(List<String> recordedMoves) {
        // Set board size
        String[] boardSizeRecorded = recordedMoves.get(0).split(",");
        boardSizeInput.setText(boardSizeRecorded[1]);
        setBoardSize(boardSizeInput);
        
        // Set game mode
        String[] gameModeRecorded = recordedMoves.get(1).split(",");
        if ("Simple".equals(gameModeRecorded[1])) {
            simpleModeButton.setSelected(true);
        } else {
            generalModeButton.setSelected(true);
        }

        // Set Player 1 type
        String[] blueTypeRecorded = recordedMoves.get(2).split(",");
        if ("HUMAN".equals(blueTypeRecorded[1])) {
            blueHumanButton.setSelected(true);
        } else {
            blueComputerButton.setSelected(true);
        }

        // Set Player 2 type
        String[] redTypeRecorded = recordedMoves.get(3).split(",");
        if ("HUMAN".equals(redTypeRecorded[1])) {
            redHumanButton.setSelected(true);
        } else {
            redComputerButton.setSelected(true);
        }
    }

    // Method to replay player moves using Timeline. ***Spamming replay Button using Java Duration and causes game to glitch out***
    private void replayMovesWithTimeline(List<String> recordedMoves) {
        Timeline timeline = new Timeline();
        int delay = 1; // Delay in seconds between moves
        int startIndex = 5; // Start from index 5

        // Loop through all recorded moves and add a KeyFrame for each
        for (int i = startIndex; i < recordedMoves.size(); i++) {
            String move = recordedMoves.get(i);
            String[] moveData = move.split(",");
            String playerColor = moveData[0];
            String letter = moveData[1];
            int row = Integer.parseInt(moveData[2]);
            int col = Integer.parseInt(moveData[3]);

            // Create a KeyFrame to execute the move after a delay
            KeyFrame keyFrame = new KeyFrame(
                Duration.seconds((i - startIndex) * delay), // Adjust delay from start index
                event -> {
                    // Set the correct button (S or O)
                    if (letter.equals("S")) selectSButton();
                    else selectOButton();

                    // Simulate the move
                    gameMode.getGameLogic().handleButtonClick(row, col, gameMode.getBoard().getButtonAt(row, col));

                    // Output for debugging
                    System.out.println("Replayed Move: " + playerColor + " " + letter + " at (" + row + ", " + col + ")");
                }
            );

            timeline.getKeyFrames().add(keyFrame);
        }

        // Set an action to occur when the timeline finishes. Also, S and O radio button are disabled if players are still computer. If not, enabled.
        timeline.setOnFinished(event -> {
            setReplayProgress(false);
            if (bluePlayer.getPlayerType() == PlayerType.HUMAN) blueSButton.setDisable(false);
            if (bluePlayer.getPlayerType() == PlayerType.HUMAN) blueOButton.setDisable(false);
            if (redPlayer.getPlayerType() == PlayerType.HUMAN) redSButton.setDisable(false);
            if (redPlayer.getPlayerType() == PlayerType.HUMAN) redOButton.setDisable(false);
        });
        
        // Start the timeline
        timeline.play();
    }
}