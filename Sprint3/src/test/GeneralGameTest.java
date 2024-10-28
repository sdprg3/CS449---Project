package test;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
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

import product.GameController;
import product.GameLogic;
import product.GeneralGame;
import product.Board;
import product.Game;

class GeneralGameTest {

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

                // General Game Mode
                game = new GeneralGame(gameController, boardVisual); 

                // Retrieve the current Board instance from the game
                board = game.getBoard();
                // Access the GameLogic instance associated with the current Board
                gameLogic = board.getGameLogic();

                latch.countDown(); // Signal that setup is complete
            } catch (Exception e) {
                e.printStackTrace(); // Log any setup errors
            }
        });

        // Wait for JavaFX thread to complete initialization
        latch.await(3, TimeUnit.SECONDS);
    }

    @Test
    public void handleGameModeLogic_testBlueWin() {
        // Simulate a full board scenario where blue wins
        gameController.getBluePlayer().setScore(5);
        gameController.getRedPlayer().setScore(3);

        // Fill the board with moves 
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                gameLogic.placeMove(i, j, "S"); // Place "S" move made by only blue player, doesn't matter for this test
            }
        }

        game.handleGameModeLogic(false);

        assertEquals("Game Over! \nBlue Player wins!", gameController.infoDisplay.getText());
        // assertTrue(board.isButtonsDisabled(), "Buttons should be disabled after game over");
    }

    @Test
    public void handleGameModeLogic_testRedWin() {
        // Simulate a full board scenario where Player 2 wins
        gameController.getBluePlayer().setScore(2);
        gameController.getRedPlayer().setScore(4);

        // Fill the board with moves 
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                gameLogic.placeMove(i, j, "S"); // Place "S" move made by only blue player, doesn't matter for this test
            }
        }

        game.handleGameModeLogic(false);

        assertEquals("Game Over! \nRed Player wins!", gameController.infoDisplay.getText());
    }

    @Test
    public void handleGameModeLogic_testPlayersDraw() {
        // Simulate a full board scenario where it's a draw
        gameController.getBluePlayer().setScore(3);
        gameController.getRedPlayer().setScore(3);

        // Fill the board with moves 
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                gameLogic.placeMove(i, j, "S"); // Place "S" move made by only blue player, doesn't matter for this test
            }
        }

        game.handleGameModeLogic(false);

        assertEquals("Game Over! \nIt's a draw", gameController.infoDisplay.getText());
    }

    @Test
    public void handleGameModeLogic_switchPlayerNotScoring() {
        // Simulate a situation where no player scored
        // Blue Player fills the first slot
        gameLogic.placeMove(0, 0, "S"); 
        game.handleGameModeLogic(false);

        assertEquals("Current Turn: Red Player", gameController.infoDisplay.getText());
    }

    @Test
    public void handleGameModeLogic_testPlayerScoring() {
        // Simulate a situation where a player scored
        gameLogic.placeMove(0, 0, "S"); 
        gameController.switchPlayer();  // Blue to red

        gameLogic.placeMove(0, 1, "O"); 
        gameController.switchPlayer(); // Red to blue

        gameLogic.placeMove(0, 2, "S"); 
        game.handleGameModeLogic(true); // Blue player score

        assertEquals("Current Turn: Blue Player\nYou scored, make your next move!", gameController.infoDisplay.getText());
    }
}
