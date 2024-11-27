package test;

import product.Board;
import product.Game; // Abstract class
import product.SimpleGame; // Concrete subclass
import product.GeneralGame; // Concrete subclass
import product.GameController;
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

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {

    // Fields
    private GameController gameController;
    private GridPane boardVisual;
    private Board board;
    private Game game;

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

        // Using Platform.runLater() to ensure that JavaFX components are created on the JavaFX Application Thread
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

                // SimpleGame is being used for now, but can be switched to GeneralGame if needed.
                game = new SimpleGame(gameController, boardVisual);
                // Retrieve the current Board instance from the game
                board = game.getBoard();

                latch.countDown(); // Signal that setup is complete
            } catch (Exception e) {
                e.printStackTrace(); // Log any setup errors
            }
        });

        // Wait for JavaFX thread to complete initialization
        latch.await(3, TimeUnit.SECONDS);
    }

    // Test the initialization of the board
    @Test
    void createBoard_test() {
        board.createBoard();
        assertNotNull(board.getBoardVisual(), "Board should be initialized.");
        assertNotNull(board.getBoardState(), "Board should be initialized.");
    }

    // Test if the board updates correctly
    @Test
    public void resetBoard_test() {
        board.createBoard();
        board.resetBoardState();
        assertTrue(boardVisual.getChildren().isEmpty(), "Visual board should be empty after reset.");
    }

    // Test disabling of all buttons
    @Test
    public void disableButtons_test() {
        board.createBoard();
        for (javafx.scene.Node node : boardVisual.getChildren()) {
            if (node instanceof Button) {
                assertFalse(((Button) node).isDisabled(), "All buttons should be enabled.");
            }
        }

        board.setButtonsDisabled(true); // Disable buttons
        for (javafx.scene.Node node : boardVisual.getChildren()) {
            if (node instanceof Button) {
                assertTrue(((Button) node).isDisabled(), "All buttons should be disabled.");
            }
        }
    }
}