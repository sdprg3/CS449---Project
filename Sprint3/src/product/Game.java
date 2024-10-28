package product;

import javafx.scene.layout.GridPane;

public abstract class Game {

    protected Board board;
    protected GameController gameController;

    public Game(GameController gameController, GridPane boardVisual) {
        this.gameController = gameController;
        this.board = new Board(this, gameController, boardVisual);
        board.createBoard(); // Initialize board state and visuals
    }

    // Defines the game mode logic, to be implemented by subclasses
    public abstract void handleGameModeLogic(boolean didPlayerScore);

    // Returns the current game board instance
    public Board getBoard() {
        return board;
    }
}