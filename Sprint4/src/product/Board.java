package product;

import javafx.scene.control.Button;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class Board {

    private int size; // Size of the board
    private GridPane boardVisual; // Visual representation of the board
    private Pane linePane; // Pane for drawing winning lines
    private GameController gameController; // Game controller to manage the game
    private String[][] boardState; // Board state array to track moves
    private Game game;

    // Constructor initializes the board with specified game, game controller, and visual grid
    public Board(Game game, GameController gameController, GridPane boardVisual) {
        this.game = game;
        this.gameController = gameController;
        this.boardVisual = boardVisual;
        this.size = gameController.getSize();

        linePane = new Pane(); // Pane for drawing lines
        linePane.setMouseTransparent(true); // Ensure mouse events are ignored on this Pane

        this.boardState = new String[size][size]; // Initialize the board state
        setBoardStateEmpty(boardState);
    }

    // Getter for board visual
    public GridPane getBoardVisual() {
        return boardVisual;
    }

    // Getter for board state
    public String[][] getBoardState() {
        return boardState;
    }

    // Sets board State
    public void setBoardState(String[][] boardState) {
        this.boardState = boardState;
    }

    // Sets up initial board state with empty cells
    public void setBoardStateEmpty(String[][] boardState) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                boardState[i][j] = ""; // Initialize each cell with an empty string
            }
        }
    }

    // Resets the board state by clearing buttons and lines
    public void resetBoardState() {
        linePane.getChildren().clear(); // Remove all lines from Pane
        boardVisual.getChildren().clear(); // Remove buttons from the GridPane
        boardVisual.getColumnConstraints().clear(); // Clear column constraints
        boardVisual.getRowConstraints().clear(); // Clear row constraints

        gameController.refreshScoreBoard(); // Update score display
    }

    // Creates the game board with buttons and sets up the grid structure
    public void createBoard() {
        resetBoardState(); // Clear existing board setup
        boardState = new String[size][size];
        setBoardStateEmpty(boardState);

        for (int col = 0; col < size; col++) {
            ColumnConstraints colConstr = new ColumnConstraints();
            colConstr.setPercentWidth(100.0 / size); // Set equal column width
            boardVisual.getColumnConstraints().add(colConstr);

            RowConstraints rowConstr = new RowConstraints();
            rowConstr.setPercentHeight(100.0 / size); // Set equal row height
            boardVisual.getRowConstraints().add(rowConstr);

            for (int row = 0; row < size; row++) {
                Button button = new Button();
                button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE); // Make button fully expandable
                button.setId("button_" + row + "_" + col); // Unique ID for button
                button.setText("");

                final int finalRow = row;
                final int finalCol = col;
                button.setOnAction(e -> {
                    // Handle the player's move
                    game.getGameLogic().handleButtonClick(finalRow, finalCol, button);
                    
                    // Update computer move after each player move
                    game.computerLogic.processComputerMove();
                });

                boardVisual.add(button, col, row); // Add button to the grid at specified position
            }
        }

        boardVisual.setHgap(10); // Set horizontal gap between cells
        boardVisual.setVgap(10); // Set vertical gap between cells
        boardVisual.setGridLinesVisible(true); // Enable grid lines for visual clarity
        boardVisual.setStyle("-fx-border-color: black; -fx-border-width: 2px;"); // Set border color and width

        boardVisual.getChildren().add(linePane); // Add linePane to boardVisual for drawing lines
    }

    public Button getButtonAt(int row, int col) {
        // Check if the indices are within the bounds of the grid
        if (row < 0 || row >= size || col < 0 || col >= size) {
            return null; // Return null if indices are out of bounds
        }
        
        // Calculate the index in the children list based on row and column
        for (javafx.scene.Node node : boardVisual.getChildren()) {
            // Check if the node is a button and its position matches the requested row and column
            if (node instanceof Button && 
                GridPane.getRowIndex(node) == row && 
                GridPane.getColumnIndex(node) == col) {
                return (Button) node; // Return the button if found
            }
        }
        
        return null; // Return null if no button is found at the specified position
    }
    
    
    // Enables all buttons on the board (for end of game or other cases)
    public void enableButtons() {
        for (javafx.scene.Node node : boardVisual.getChildren()) {
            if (node instanceof Button) {
                Button button = (Button) node;
                button.setDisable(false);
            }
        }
    }

    // Disables all buttons on the board (for end of game or other cases)
    public void disableButtons() {
        for (javafx.scene.Node node : boardVisual.getChildren()) {
            if (node instanceof Button) {
                Button button = (Button) node;
                button.setDisable(true);
            }
        }
    }

    // Draws a winning line from the center of one button to another
    public void drawWinningLine(int startRow, int startCol, int endRow, int endCol, Player player) {
        double buttonWidth = boardVisual.getWidth() / size; // Width of each button
        double buttonHeight = boardVisual.getHeight() / size; // Height of each button

        double startX = startCol * buttonWidth + buttonWidth / 2; // X coordinate of start button's center
        double startY = startRow * buttonHeight + buttonHeight / 2; // Y coordinate of start button's center
        double endX = endCol * buttonWidth + buttonWidth / 2; // X coordinate of end button's center
        double endY = endRow * buttonHeight + buttonHeight / 2; // Y coordinate of end button's center

        Line line = new Line(startX, startY, endX, endY); // Create line from start to end

        // Set line color based on player color
        if (player.getPlayerColor() == Player.PlayerColor.Blue) {
            line.setStroke(Color.BLUE);
        } else if (player.getPlayerColor() == Player.PlayerColor.Red) {
            line.setStroke(Color.RED);
        }
        line.setStrokeWidth(1); // Set width of the line
        line.setMouseTransparent(true); // Make line not interactable

        linePane.getChildren().add(line); // Add line to linePane
    }
}
