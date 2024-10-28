package product;

import javafx.scene.layout.GridPane;

public class SimpleGame extends Game {

    public SimpleGame(GameController gameController, GridPane boardVisual) {
        super(gameController, boardVisual);
    }

    // Implements the game logic for the simple mode
    @Override
    public void handleGameModeLogic(boolean didPlayerScore) {
        // Update the scoreboard with current player scores
        gameController.refreshScoreBoard();
        
        if (didPlayerScore) {
            // Display win message for the current player and disable board buttons
            gameController.infoDisplay.setText("Game Over! \n" + gameController.getCurrentPlayer().getPlayerColor() + " Player wins!");
            board.disableButtons();
        } else if (board.getGameLogic().isBoardFull()) {
            // Display draw message if the board is full and no player scored
            gameController.infoDisplay.setText("Game Over! \nIt's a draw");
            board.disableButtons();
        } else {
            // Switch to the next player and display the current turn
            gameController.switchPlayer();
            gameController.infoDisplay.setText("Current Turn: " + gameController.getCurrentPlayer().getPlayerColor() + " Player");
        }
    }
}