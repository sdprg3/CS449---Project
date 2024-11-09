package test;

import product.Board;
import product.Game; // Abstract class
import product.SimpleGame; // Concrete subclass
import product.GameController;
import product.GameLogic;
import product.ComputerLogic;
import javafx.animation.PauseTransition;
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

import javafx.util.Duration;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class ComputerLogicTest {

    private GameController gameController;
    private GridPane boardVisual;
    private Board board;
    private Game game;
    private GameLogic gameLogic;
    private ComputerLogic computerLogic;
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
                gameController.startButton = new Button();
                boardVisual = new GridPane();
                gameController.boardVisual = boardVisual;

                // Initialize the RadioButtons
                gameController.blueSButton = new RadioButton();
                gameController.redSButton = new RadioButton();
                gameController.blueOButton = new RadioButton();
                gameController.redOButton = new RadioButton();
                gameController.blueComputerButton = new RadioButton();
                gameController.redComputerButton = new RadioButton();
                gameController.blueHumanButton = new RadioButton();
                gameController.redHumanButton = new RadioButton();

                // SimpleGame is being used for now, but can be switched to GeneralGame if needed
                game = new SimpleGame(gameController, boardVisual); 

                // Retrieve the current Board instance from the game
                board = game.getBoard();

                // Access the GameLogic instance associated with the game
                gameLogic = game.getGameLogic();

                // Access the ComputerLogic instance associated with the game
                computerLogic = game.getComputerLogic();

                latch.countDown(); // Signal that setup is complete
            } 
            catch (Exception e) {
                e.printStackTrace(); // Log any setup errors
            }
        });
        
        // Wait for JavaFX thread to complete initialization
        latch.await(3, TimeUnit.SECONDS);
    }
    
    @Test
    public void findOpenHorizontalSOSPattern_Test() {
        // Setup the board state
        gameLogic.placeMove(0, 0, "S");
        gameLogic.placeMove(0, 2, "S");

        // Call method
        int[] result = computerLogic.findOpenHorizontalSOSPattern();

        // Assert the result is correct
        assertArrayEquals(new int[]{0, 1}, result);
    }

    @Test
    public void findOpenVerticalSOSPattern_Test() {
        // Setup the board state
        gameLogic.placeMove(0, 0, "S");
        gameLogic.placeMove(1, 0, "O");

        // Call method
        int[] result = computerLogic.findOpenVerticalSOSPattern();

        // Assert the result is correct
        assertArrayEquals(new int[]{2, 0}, result);
    }

    @Test
    public void findOpenPrimaryDiagonalSOSPattern_Test() {
        // Setup the board state
        gameLogic.placeMove(0, 0, "S");
        gameLogic.placeMove(2, 2, "S");

        // Call method
        int[] result = computerLogic.findOpenPrimaryDiagonalSOSPattern();

        // Assert the result is correct
        assertArrayEquals(new int[]{1, 1}, result);
    }

    @Test
    public void findOpenSecondaryDiagonalSOSPattern_Test() {
        // Setup the board state
        gameLogic.placeMove(2, 0, "S");
        gameLogic.placeMove(0, 2, "S");

        // Call method
        int[] result = computerLogic.findOpenSecondaryDiagonalSOSPattern();

        // Assert the result is correct
        assertArrayEquals(new int[]{1, 1}, result);
    }

    @Test
    public void findRandomAvailableSpot_Test() {
        // Fill board except the last row
        gameLogic.placeMove(0, 0, "S");
        gameLogic.placeMove(0, 1, "S");
        gameLogic.placeMove(0, 2, "S");
        gameLogic.placeMove(1, 0, "S");
        gameLogic.placeMove(1, 1, "S");
        gameLogic.placeMove(1, 2, "S");

        // Call method
        int[] result = computerLogic.findRandomAvailableSpot();
        
        // Assert the result is a valid spot
        assertTrue(result[0] == 2 && result[1] < 3);
    }

    @Test
    public void processComputerMove_Test() throws InterruptedException {
        // Set the current player to COMPUTER
        gameController.getCurrentPlayer().setPlayerComputer();

        // Ensure that the game is in progress
        gameController.setGameProgress(true);

        // Setup the board state - "S" moves are already placed
        gameLogic.placeMove(0, 0, "S");
        gameLogic.placeMove(0, 2, "S");

        // Create a CountDownLatch to wait until the PauseTransition finishes
        CountDownLatch latch = new CountDownLatch(1);

        // Call the method that processes the computer move
        computerLogic.manageComputerMove();

        // Create a PauseTransition and set the action to decrement the latch when finished
        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(event -> {
            // Decrement the latch to indicate that the PauseTransition is done
            latch.countDown();
        });

        // Start the pause
        pause.play();

        // Wait for the latch to be decremented (i.e., wait for the PauseTransition to finish)
        latch.await();

        //Checks if computer found a SOS pattern and made a move (by placing O)
        assertEquals("O", board.getBoardState()[0][1]);
    }

    @Test
    public void selectButton_computerTest() {
        // Call selectSButton and verify "S" is selected 
        computerLogic.selectSButton();

        // Check the expected result 
        assertTrue(gameController.blueSButton.isSelected());

        // Call selectSButton and verify "O" is selected 
        computerLogic.selectOButton();

        // Check the expected result 
        assertTrue(gameController.blueOButton.isSelected());

        // switch players
        gameController.switchPlayer();

        // Call selectSButton and verify "S" is selected 
        computerLogic.selectSButton();

        // Check the expected result 
        assertTrue(gameController.redSButton.isSelected());

        // Call selectSButton and verify "O" is selected 
        computerLogic.selectOButton();

        // Check the expected result 
        assertTrue(gameController.redOButton.isSelected());
    }
}