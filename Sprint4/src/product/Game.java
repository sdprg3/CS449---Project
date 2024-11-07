package product;

import javafx.scene.layout.GridPane;

//abstract class for Game: simple and general.
public abstract class Game {

    protected Board board;
    protected GameController gameController;
    protected ComputerLogic computerLogic;
    protected GameLogic gameLogic; // Game logic handler

    public Game(GameController gameController, GridPane boardVisual) {
        this.gameController = gameController;
        this.board = new Board(this, gameController, boardVisual);
        this.computerLogic = new ComputerLogic(this, gameController, board);
        this.gameLogic = new GameLogic(this, gameController, board); // Initialize game logic

        board.createBoard(); // Initialize board state and visuals
    }

    // Defines the game mode logic, to be implemented by subclasses
    public abstract void handleGameModeLogic(boolean didPlayerScore);

    // Getter for game logic
    public GameLogic getGameLogic() {
        return gameLogic;
    }

    // Getter for computer AI logic
    public ComputerLogic getComputerLogic() {
        return computerLogic;
    }

    // Getter for board instance
    public Board getBoard() {
        return board;
    }
    
    // Method to endGame
    public void endGame() {
        gameController.setGameProgress(false);
    }

    
}