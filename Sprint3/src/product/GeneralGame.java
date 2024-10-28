package product;

import javafx.scene.layout.GridPane;

public class GeneralGame extends Game {

    public GeneralGame(GameController gameController, GridPane boardVisual) {
        super(gameController, boardVisual);
    }

    // Implements game logic specific to General Game mode
    @Override
    public void handleGameModeLogic(boolean didPlayerScore) {
        gameController.refreshScoreBoard(); // Update score display

        if (board.getGameLogic().isBoardFull()) { 
            // Determine and display winner or draw when board is full
            if (gameController.getBluePlayer().getScore() > gameController.getRedPlayer().getScore()) {
                gameController.infoDisplay.setText("Game Over! \n" + gameController.getBluePlayer().getPlayerColor() + " Player wins!");
                board.disableButtons();
            } else if (gameController.getBluePlayer().getScore() < gameController.getRedPlayer().getScore()) {
                gameController.infoDisplay.setText("Game Over! \n" + gameController.getRedPlayer().getPlayerColor() + " Player wins!");
                board.disableButtons();
            } else {
                gameController.infoDisplay.setText("Game Over! \nIt's a draw");
                board.disableButtons();
            }
        } else if (!didPlayerScore) { 
            // Switch players if no points scored and update turn display
            gameController.switchPlayer();
            gameController.infoDisplay.setText("Current Turn: " + gameController.getCurrentPlayer().getPlayerColor() + " Player");
        } else { 
            // Notify player of scored point and allow additional move
            gameController.infoDisplay.setText("Current Turn: " + gameController.getCurrentPlayer().getPlayerColor() + " Player");
            gameController.infoDisplay.setText(gameController.infoDisplay.getText() + "\nYou scored, make your next move!");
        }
    }
}