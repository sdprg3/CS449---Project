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

    // Getters for player color, type, and score
    public PlayerColor getPlayerColor() {
        return color;
    }

    // Getter for player type
    public PlayerType getPlayerType() {
        return type;
    }

    // Getter for player score
    public int getScore() {
        return score;
    }

    // Setter for player type and scores
    public void setPlayerHuman() {
        this.type = PlayerType.HUMAN;
    }

    public void setPlayerComputer() {
        this.type = PlayerType.COMPUTER;
    }

    public void setScore(int score) {
        this.score = score;
    }

    // Increases the player's score by the specified amount
    public void increaseScore(int increment) {
        this.score += increment;
    }

    public void resetScore() {
        this.score = 0;
    }

}