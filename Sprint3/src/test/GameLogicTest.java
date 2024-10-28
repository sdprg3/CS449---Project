package test;

import product.Board;
import product.Game; // Abstract class
import product.SimpleGame; // Concrete subclass
import product.GeneralGame; // Concrete subclass
import product.GameController;
import product.GameLogic;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class GameLogicTest {
    private GameController gameController;
    private GridPane boardVisual;
    private Board board;
    private Game game;
    private GameLogic gameLogic;
    private static JFXPanel jfxPanel; // This will initialize the JavaFX toolkit

    // BeforeAll: Initialize JavaFX toolkit
    @BeforeAll
    public static void init() {
        jfxPanel = new JFXPanel(); 
    }

    @BeforeEach
    void setUp() throws Exception {
        // Create latch to synchronize setup of JavaFX components
        CountDownLatch latch = new CountDownLatch(1);

        // Using Platform.runLater() ensures that JavaFX components are created on the JavaFX Application Thread
        Platform.runLater(() -> {
            try {
                // Initialize game controller and UI components
                gameController = new GameController();
                gameController.redScoreDisplay = new Text(); 
                gameController.blueScoreDisplay = new Text(); 
                gameController.infoDisplay = new Text(); 
                gameController.modeDisplay = new Text(); 
                gameController.boardSizeInput = new TextField();
                gameController.simpleModeButton = new RadioButton();
                gameController.generalModeButton = new RadioButton();
                boardVisual = new GridPane();
                gameController.boardVisual = boardVisual;

                // Initialize the RadioButtons
                gameController.blueSButton = new RadioButton();
                gameController.redSButton = new RadioButton();
                gameController.blueOButton = new RadioButton();
                gameController.redOButton = new RadioButton();

                // SimpleGame is being used for now, but can be switched to GeneralGame if needed
                game = new SimpleGame(gameController, boardVisual); 

                // Retrieve the current Board instance from the game
                board = game.getBoard();

                // Access the GameLogic instance associated with the current Board
                gameLogic = board.getGameLogic();

                latch.countDown(); // Signal that setup is complete
            } 
            catch (Exception e) {
                e.printStackTrace(); // Log any setup errors
            }
        });
        
        // Wait for JavaFX thread to complete initialization
        latch.await(3, TimeUnit.SECONDS);
    }

    // Test if a move is valid on an empty board
    @Test
    public void isValidMove_testValidMove() {
        boolean results = gameLogic.isValidMove(0, 1);
        assertTrue(results, "Move should be valid on an empty board");
    }

    // Test if a move is invalid after placing a mark in the cell
    @Test
    public void isValidMove_testInvalidMove() {
        gameLogic.placeMove(0, 1, "S"); // Place an initial move
        boolean results = gameLogic.isValidMove(0, 1);
        assertFalse(results, "Move should be invalid after placing a mark in the cell");
    }

    // Test scoring for SOS in a row
    @Test
    public void checkAndUpdateScore_CheckRowForSOS() {
        gameLogic.placeMove(0, 0, "S");
        gameLogic.placeMove(0, 1, "O");
        gameLogic.placeMove(0, 2, "S");

        assertTrue(gameLogic.checkAndUpdateScore(0, 2), "Player should score for SOS in row");
        assertEquals(1, gameController.getCurrentPlayer().getScore(), "Player score should be 1 after scoring SOS");
    }

    // Test scoring for SOS in a column
    @Test
    public void checkAndUpdateScore_CheckColumnForSOS() {
        gameLogic.placeMove(0, 0, "S");
        gameLogic.placeMove(1, 0, "O");
        gameLogic.placeMove(2, 0, "S");

        assertTrue(gameLogic.checkAndUpdateScore(2, 0), "Player should score for SOS in column");
        assertEquals(1, gameController.getCurrentPlayer().getScore(), "Player score should be 1 after scoring SOS");
    }
    
    // Test scoring for SOS in a diagonal
    @Test
    public void checkAndUpdateScore_CheckDiagonalForSOS() {
        gameLogic.placeMove(0, 0, "S");
        gameLogic.placeMove(1, 1, "O");
        gameLogic.placeMove(2, 2, "S");

        assertTrue(gameLogic.checkAndUpdateScore(2, 2), "Player should score for SOS in diagonal");
        assertEquals(1, gameController.getCurrentPlayer().getScore(), "Player score should be 1 after scoring SOS");
    }

    // Test if the board is full
    @Test
    public void isBoardFull_testFull() {
        // Fill the board completely
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                gameLogic.placeMove(i, j, "S");
            }
        }
        
        assertTrue(gameLogic.isBoardFull(), "The board should be full");
    }

    // Test if the board is not full
    @Test
    public void isBoardFull_testNotFull() {
        // Fill only the first row of the board
        for (int j = 0; j < 3; j++) {
            gameLogic.placeMove(0, j, "S");
        }

        assertFalse(gameLogic.isBoardFull(), "The board should not be full initially");
    }

    // Test player switching
    @Test
    public void switchPlayer_test() {
        assertEquals(gameController.getBluePlayer(), gameController.getCurrentPlayer(), "Initial current player should be Player 1");

        gameController.switchPlayer(); // Switch to Player 2
        assertEquals(gameController.getRedPlayer(), gameController.getCurrentPlayer(), "Current player should now be Player 2");

        gameController.switchPlayer(); // Switch back to Player 1
        assertEquals(gameController.getBluePlayer(), gameController.getCurrentPlayer(), "Current player should now be Player 1 again");
    }

    // Test handling button clicks for valid moves
    @Test
    public void handleButtonClick_testValidMove() {
        Button button = new Button();
        
        // Set the blueSButton selected for Player 1
        gameController.blueSButton.setSelected(true);

        // Simulate a button click
        gameLogic.handleButtonClick(1, 1, button);
        
        assertEquals("S", button.getText(), "Button text should reflect the move made");
        assertEquals("-fx-text-fill: blue; -fx-font-size: 18px;", button.getStyle(), "Button style should be correctly set for Player 1");
    }
}