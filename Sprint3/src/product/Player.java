package product;

public class Player {
    private PlayerColor color; // Color of the player (Blue or Red)
    private PlayerType type;   // Type of player (HUMAN or COMPUTER)
    private int score;         // Player's score

    // Enum for player type
    public enum PlayerType {
        HUMAN,
        COMPUTER
    }

    // Enum for player color
    public enum PlayerColor {
        Blue,
        Red
    }

    // Constructor to initialize player with color and type
    public Player(PlayerColor color, PlayerType type) {
        this.color = color;
        this.type = type;
        this.score = 0; // Initialize score to zero
    }

    // Getter for player color
    public PlayerColor getPlayerColor() {
        return color;
    }

    // Getter for player type
    public PlayerType getType() {
        return type;
    }

    // Getter for player score
    public int getScore() {
        return score;
    }

    // Setter for player score
    public void setScore(int score) {
        this.score = score;
    }

    // Increases the player's score by the specified amount
    public void increaseScore(int increment) {
        score += increment;
    }

    // Method for testing human or computer moves (***STILL IN PROGRESS***)
    public void test(Board board) {
        if (type == PlayerType.HUMAN) {
            // Implement logic for a human player's move (e.g., input from UI)
        } else if (type == PlayerType.COMPUTER) {
            // Implement logic for a computer player's move (e.g., AI decision)
        }
    }
}