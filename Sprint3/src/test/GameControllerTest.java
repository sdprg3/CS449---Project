package test;

import product.GameController;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class GameControllerTest {

    // Fields for the GameController and related UI components
    private GameController gameController;
    private GridPane boardVisual;
    private ActionEvent event; // Action event for simulating user interactions
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

        // Using Platform.runLater() ensure that JavaFX components are created on the JavaFX Application Thread
        Platform.runLater(() -> {
            try {
                // Initialize game controller and UI components
                gameController = new GameController();
                event = new ActionEvent();

                gameController.redScoreDisplay = new Text();
                gameController.blueScoreDisplay = new Text();
                gameController.infoDisplay = new Text();
                gameController.modeDisplay = new Text();
                gameController.boardSizeInput = new TextField();
                gameController.simpleModeButton = new RadioButton();
                gameController.generalModeButton = new RadioButton();
                boardVisual = new GridPane();
                gameController.boardVisual = boardVisual;

                latch.countDown(); // Signal that setup is complete
            } catch (Exception e) {
                e.printStackTrace(); // Log any setup errors
            }
        });

        // Wait for JavaFX thread to complete initialization
        latch.await(3, TimeUnit.SECONDS);
    }

    // Test for valid board size input
    @Test
    void validateBoardSize_testValidSize() {
        gameController.boardSizeInput.setText("5"); // Set valid input
        boolean result = gameController.validateBoardSize(event); // Validate input
        assertTrue(result); // Check results
    }

    // Test for invalid board size input (out of range)
    @Test
    void validateBoardSize_testInvalidSize() {
        gameController.boardSizeInput.setText("15"); // Set invalid input
        boolean result = gameController.validateBoardSize(event); // Validate input
        assertEquals("Please enter a valid number between 3 and 10.", gameController.infoDisplay.getText()); // Check error message for out-of-bound value
        assertFalse(result); // Check results
    }

    // Test for non-numeric input in board size
    @Test
    void validateBoardSize_testNonNumericInput() {
        gameController.boardSizeInput.setText("abc"); // Set non-numeric input
        boolean result = gameController.validateBoardSize(event); // Validate input
        assertEquals("Please enter a valid number for board size.", gameController.infoDisplay.getText()); // Check error message for non-numeric value
        assertFalse(result); // Check results
    }

    // Test for selecting simple game mode
    @Test
    void validateGameMode_testSimpleMode() {
        gameController.simpleModeButton.setSelected(true); // Select simple mode
        boolean result = gameController.validateGameMode(event); // Validate mode
        assertEquals("Mode: Simple", gameController.modeDisplay.getText()); // Check if modeDisplay shows correct gameMode
        assertTrue(result); // Check results
    }

    // Test for selecting general game mode
    @Test
    void validateGameMode_testGeneralMode() {
        gameController.generalModeButton.setSelected(true); // Select general mode
        boolean result = gameController.validateGameMode(event); // Validate mode
        assertEquals("Mode: General", gameController.modeDisplay.getText()); // Check if modeDisplay shows correct gameMode
        assertTrue(result); // Check results
    }

    // Test for starting the game with valid inputs
    @Test
    void startGame_TestValidInputs() {
        gameController.boardSizeInput.setText("3"); // Set valid board size
        gameController.simpleModeButton.setSelected(true); // Set game mode to Simple

        gameController.startGame(event); // Start the game

        // Verify board size, game mode, and info display
        assertEquals(3, gameController.getSize());
        assertTrue(gameController.simpleModeButton.isSelected(), "Simple mode should be selected.");
        assertEquals("Current Turn: Blue Player\nStarting Game!", gameController.infoDisplay.getText());
    }

    // Test for starting the game with invalid inputs
    @Test
    void startGame_TestInvalidInputs() {
        gameController.boardSizeInput.setText("50"); // Set invalid board size
        gameController.simpleModeButton.setSelected(false);
        gameController.generalModeButton.setSelected(false);

        gameController.startGame(event); // Start the game

        // Verify board size, game mode, and info display
        assertEquals(50, gameController.getSize());
        assertFalse(gameController.simpleModeButton.isSelected(), "Simple mode should not be selected.");
        assertEquals("Please enter a valid number between 3 and 10.", gameController.infoDisplay.getText()); // Check error message for out-of-bound value
        assertEquals("Mode: Not Selected", gameController.modeDisplay.getText()); // Test for correct game mode
    }
}