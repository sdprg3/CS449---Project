package product;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameRecorder {
    // List to store the game moves as strings
    private List<String> gameLog = new ArrayList<>();
    // Game metadata variables (board size, game mode, player info)
    private String boardSize;
    private String gameMode;
    private String player1Info;
    private String player2Info;

    // Initializes the recording by setting game metadata and writing it to the file
    public void initializeRecording(String boardSize, String gameMode, String bluePlayerType, String redPlayer) {
        // Set metadata values for the game
        this.boardSize = "BoardSize," + boardSize;
        this.gameMode = "GameMode," + gameMode;
        this.player1Info = "Blue," + bluePlayerType;
        this.player2Info = "Red," + redPlayer;

        // Write metadata to the game record file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) { // Overwrite the file
            // Create a list of metadata lines
            List<String> metadata = List.of(
                this.boardSize,
                this.gameMode,
                this.player1Info,
                this.player2Info
            );

            // Write each metadata line to the file
            for (String line : metadata) {
                writer.write(line);
                writer.newLine(); // Write a newline after each line
            }
            writer.newLine(); // Add a newline to separate metadata from game moves
            
        } catch (IOException e) {
            // Handle file writing errors
            System.err.println("Error saving metadata: " + e.getMessage());
        }
    }

    // Define the file path where the game log will be saved and loaded
    private String filePath = "C:\\Users\\Steven\\Desktop\\Projects\\Sprint5\\gameRecord.txt"; // You can change this path to your desired location

    // Records a move in the game with player color, letter, and position on the board
    public void recordMove(Player.PlayerColor playerColor, String letter, int row, int col) {
        // Format the move log entry as "color,text,row,col"
        String moveEntry = String.format("%s,%s,%d,%d", playerColor, letter, row, col);

        // Append the move entry to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) { // 'true' to append to the file
            writer.write(moveEntry);
            writer.newLine(); // Write a newline after each move entry
            System.out.println("Move recorded and saved to " + filePath);
        } catch (IOException e) {
            // Handle file writing errors
            System.err.println("Error saving game move: " + e.getMessage());
        }
    }

    // Loads the game moves from the file into memory
    public List<String> loadFromFile() {
        // List to hold the loaded moves from the file
        List<String> loadedMoves = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Add each line (move) from the file to the loadedMoves list
                loadedMoves.add(line);
            }
            System.out.println("Game moves loaded successfully from " + filePath);
        } catch (IOException e) {
            // Handle file reading errors
            System.err.println("Error loading game moves: " + e.getMessage());
        }
        return loadedMoves;
    }

    // Resets (clears) the contents of the text file and the game log in memory
    public void resetFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Opening the file in write mode and immediately closing it will clear the file content
            writer.close();
            System.out.println("File has been reset and cleared: " + filePath);

            // Clear the game log in memory
            gameLog.clear();
            System.out.println("In-memory game log has been reset.");
        } catch (IOException e) {
            // Handle file reset errors
            System.err.println("Error resetting file: " + e.getMessage());
        }
    }

    // Returns all recorded game moves in memory
    public List<String> getGameLog() {
        return gameLog;
    }
}