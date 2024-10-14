package test;

import product.GameLogic;
import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GameLogicTest {

    // Fields
    private GameLogic gameLogic;
    private static JFXPanel jfxPanel; // This will initialize the JavaFX toolkit

    // BeforeAll: Initialize JavaFX toolkit
    @BeforeAll
    public static void init() {
        jfxPanel = new JFXPanel(); // Initialize JavaFX
    }

    // Test valid player moves and player switching.
    @Test
    public void testHandlePlayerMove_ValidRow_General() {
        gameLogic = new GameLogic(3, 'S'); // Initialize a game with size 3 in game mode 'S'

        // Test Case Input:
        assertTrue(gameLogic.handlePlayerMove(0, 0, "S")); // Valid move
        // Expected Output: 
        assertEquals('R', gameLogic.getCurrentPlayer()); // Current player should switch to 'R'

        assertTrue(gameLogic.handlePlayerMove(1, 0, "O")); 
        // Expected Output: 
        assertEquals('B', gameLogic.getCurrentPlayer()); // Current player should switch to 'B'

        assertTrue(gameLogic.handlePlayerMove(2, 0, "S")); 
        // Expected Output: 
        assertEquals('B', gameLogic.getCurrentPlayer()); // Player scores, no switch
    }

    // Test handling of valid diagonal moves.
    @Test
    public void testHandlePlayerMove_ValidDiagonal() {
        gameLogic = new GameLogic(3, 'S'); // Initialize a game with size 3 in game mode 'S'

        // Test Case Input:
        assertTrue(gameLogic.handlePlayerMove(0, 0, "S")); // Valid move
        // Expected Output: 
        assertTrue(gameLogic.handlePlayerMove(1, 1, "O")); // Valid move
        // Expected Output: 
        assertTrue(gameLogic.handlePlayerMove(2, 0, "S")); // Valid move
    }

    // Test game over conditions in simple mode.
    @Test
    public void testGameOverConditions_simple() {
        gameLogic = new GameLogic(3, 'S'); // Initialize a game with size 3 in game mode 'S'

        // Test Case Input:
        gameLogic.handlePlayerMove(0, 0, "S");
        gameLogic.handlePlayerMove(0, 1, "O");
        gameLogic.handlePlayerMove(0, 2, "S"); // Blue scores
        // Expected Output: 
        assertTrue(gameLogic.isGameOver()); // Game should be over in 'S' or simple mode
    }

    // Test handling of invalid moves.
    @Test
    public void testHandlePlayerMove_InvalidMove() {
        gameLogic = new GameLogic(3, 'S'); // Initialize a game with size 3 in game mode 'S'

        // Test Case Input:
        gameLogic.handlePlayerMove(0, 0, "S"); // Valid move
        // Expected Output: 
        assertFalse(gameLogic.handlePlayerMove(0, 0, "O")); // Invalid move (cell occupied)
    }

    // Test if the board is full.
    @Test
    public void testIsBoardFull() {
        gameLogic = new GameLogic(3, 'S'); // Initialize a game with size 3 in game mode 'S'

        // Test Case Input:
        gameLogic.handlePlayerMove(0, 0, "S");
        gameLogic.handlePlayerMove(0, 1, "O");
        gameLogic.handlePlayerMove(0, 2, "S");
        gameLogic.handlePlayerMove(1, 0, "S");
        gameLogic.handlePlayerMove(1, 1, "O");
        gameLogic.handlePlayerMove(1, 2, "S");
        gameLogic.handlePlayerMove(2, 0, "S");
        gameLogic.handlePlayerMove(2, 1, "O");
        gameLogic.handlePlayerMove(2, 2, "S"); // Fill the board

        // Expected Output: 
        assertTrue(gameLogic.isBoardFull()); // Board should be full
    }
}
