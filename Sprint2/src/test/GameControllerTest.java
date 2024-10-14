package test;

import product.GameController;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GameControllerTest {

    // Fields
    private GameController gameController;
    private static JFXPanel jfxPanel; // This will initialize the JavaFX toolkit

    // BeforeAll: Initialize JavaFX toolkit
    @BeforeAll
    public static void init() {
        jfxPanel = new JFXPanel(); 
    }

    // BeforeEach: Set up for each test
    @BeforeEach
    void setUp() {
        // Initialize the GameController and its components before each test
        gameController = new GameController();

        // Create and set the game board
        GridPane gameBoard = new GridPane();
        gameController.setGameBoard(gameBoard);

        // Initialize all text display components
        gameController.redScoreDisplay = new Text(); 
        gameController.blueScoreDisplay = new Text(); 
        gameController.infoDisplay = new Text(); 
        gameController.modeDisplay = new Text(); 

        // Initialize text field input and radio buttons
        gameController.simpleModeButton = new RadioButton();
        gameController.generalModeButton = new RadioButton();
        gameController.boardSize = new TextField();
    }

    // Test for valid board size input
    @Test
    void testValidateBoardSizeInput_ValidSize() {
        gameController.boardSize.setText("5");
        ActionEvent event = new ActionEvent();

        // Call the method to validate board size input
        gameController.validateBoardSizeInput(event);

        // Test Case Input:
        // Expected Output: 
        assertEquals("Starting Game! Current Turn: Blue", gameController.infoDisplay.getText());
        assertEquals(5, gameController.getSize());
    }

    // Test for invalid board size input (out of range)
    @Test
    void testValidateBoardSizeInput_InvalidSize() {
        gameController.boardSize.setText("15");
        ActionEvent event = new ActionEvent();

        // Call the method to validate board size input
        gameController.validateBoardSizeInput(event);

        // Test Case Input:
        // Expected Output: 
        assertEquals("Board size must be between 3 and 10!", gameController.infoDisplay.getText());
        assertEquals(0, gameController.getSize());
    }

    // Test for non-numeric input in board size
    @Test
    void testValidateBoardSizeInput_NonNumericInput() {
        // Set a non-numeric board size
        gameController.boardSize.setText("abc");
        ActionEvent event = new ActionEvent();

        // Call the method to validate board size input
        gameController.validateBoardSizeInput(event);

        // Test Case Input:
        // Expected Output: 
        assertEquals("Please enter a valid number for board size.", gameController.infoDisplay.getText());
        assertEquals(0, gameController.getSize());
    }

    // Test for starting the game with valid inputs
    @Test
    void testStartGame_ValidInputs() {
        // Set a valid board size
        gameController.boardSize.setText("3");
        gameController.simpleModeButton.setSelected(true); // Set game mode to Simple
        ActionEvent event = new ActionEvent();

        // Start the game
        gameController.startGame(event);

        // Test Case Input:
        // Expected Output:
        assertEquals(3, gameController.getSize());
        assertTrue(gameController.simpleModeButton.isSelected(), "Simple mode should be selected.");
        assertEquals("Starting Game! Current Turn: Blue", gameController.infoDisplay.getText(), 
                     "Info display should show the starting message.");
    }
}
