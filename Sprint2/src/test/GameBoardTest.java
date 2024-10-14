package test;

import product.GameBoard;
import product.GameController;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GameBoardTest {

    // Fields
    private GameBoard board;
    private GameController gameController;
    private GridPane gameBoard;
    private static JFXPanel jfxPanel; // This will initialize the JavaFX toolkit

    // BeforeAll: Initialize JavaFX toolkit
    @BeforeAll
    public static void init() {
        jfxPanel = new JFXPanel(); 
    }

    // BeforeEach: Set up for each test
    @BeforeEach
    void setUp() {
        // Create the necessary objects for the Board test
        gameController = new GameController();
        gameBoard = new GridPane();

        // Initialize all text display
        gameController.redScoreDisplay = new Text();
        gameController.blueScoreDisplay = new Text();
        gameController.infoDisplay = new Text();
        gameController.modeDisplay = new Text();

        // Initialize the buttons for selection
        gameController.blueSButton = new RadioButton();
        gameController.redSButton = new RadioButton();
        gameController.blueOButton = new RadioButton();
        gameController.redOButton = new RadioButton();

        // Create the Board with a valid size (e.g., 5) and a game mode (e.g., 'S')
        board = new GameBoard(5, gameBoard, 'S', gameController);
    }

    // Test the initialization of the board
    @Test
    void testInitializeBoard() {
        // Check if the board has been initialized correctly
        assertNotNull(board.getBoard(), "Board should be initialized.");
        assertEquals(5, board.getBoard().length, "Board should be of size 5.");
    }

    // Test if the board updates correctly
    @Test
    public void testUpdateBoard() {
        board.updateBoard(1, 1, 'S');
        assertEquals('S', board.getBoard()[1][1], "Board should be updated at position (1, 1).");
    }

    // Test if the board resets correctly
    @Test
    public void testResetBoardState() {
        board.updateBoard(0, 0, 'S'); // Update some state
        board.resetBoardState(); // Reset the board state
        
        assertEquals(5, board.getBoard().length, "Board should still be of size 5.");
        assertEquals("Score: 0", gameController.redScoreDisplay.getText(), "Red score should reset to 0.");
        assertEquals("Score: 0", gameController.blueScoreDisplay.getText(), "Blue score should reset to 0.");
    }

    // Test handling of button click for a valid move
    @Test
    public void testHandleButtonClick_ValidMove() {
        // Simulate a button click
        Button button = new Button(); // Simulate a button
        gameController.blueSButton.setSelected(true); // Player chooses 'S'
        board.handleButtonClick(2, 2, button); // Player clicks button at (2, 2)

        // Verify the board and button are updated correctly
        assertEquals('S', board.getBoard()[2][2], "The board should be updated with 'S' at (2,2).");
        assertEquals("S", button.getText(), "The button text should be updated to 'S'.");
    }

    // Test disabling of all buttons
    @Test
    public void testDisableButtons() {
        board.disableButtons(); // Disable buttons

        for (javafx.scene.Node node : gameBoard.getChildren()) {
            if (node instanceof Button) {
                assertTrue(((Button) node).isDisabled(), "All buttons should be disabled.");
            }
        }
    }
}
