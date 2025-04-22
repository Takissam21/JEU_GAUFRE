package gaufre.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GaufreGame {
    private int a, b;
    private boolean[][] grid;
    private boolean playerATurn;
    private List<int[]> history;

    public GaufreGame(int a, int b) {
        this.a = a;
        this.b = b;
        this.grid = new boolean[a + 1][b + 1];
        this.playerATurn = true;
        this.history = new ArrayList<>();

        resetGrid();
    }
    
    private void resetGrid() {
        for (int i = 0; i <= a; i++) {
            for (int j = 0; j <= b; j++) {
                grid[i][j] = true;
            }
        }
    }

    public boolean playMove(int i, int j) {
        System.out.println("playMove appelé avec : (" + i + ", " + j + ")");
        if (i < 0 || i > a || j < 0 || j > b || !grid[i][j]) {
            return false;
        }

        for (int x = i; x <= a; x++) {
            for (int y = j; y <= b; y++) {
                grid[x][y] = false;
            }
        }

        history.add(new int[]{i, j});
        playerATurn = !playerATurn;
        return true;
    }

    public boolean undoMove() {
        if (history.isEmpty()) {
            return false;
        }

        history.remove(history.size() - 1);

        // Restaurer la grille en rejouant tous les coups sauf le dernier
        resetGrid();
        boolean tempPlayerATurn = true;
        for (int[] move : history) {
            for (int x = move[0]; x <= a; x++) {
                for (int y = move[1]; y <= b; y++) {
                    grid[x][y] = false;
                }
            }
            tempPlayerATurn = !tempPlayerATurn;
        }
        playerATurn = tempPlayerATurn;
        return true;
    }

    public void resetGame() {
        history.clear();
        playerATurn = true;
        resetGrid();
    }

    public void saveGame(String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("a=" + a + "\n");
            writer.write("b=" + b + "\n");
            writer.write("playerATurn=" + playerATurn + "\n");
            writer.write("grid=\n");
            for (int i = 0; i <= a; i++) {
                for (int j = 0; j <= b; j++) {
                    writer.write(grid[i][j] ? "1" : "0");
                    if (j < b) writer.write(" ");
                }
                writer.write("\n");
            }
            writer.write("history=");
            for (int k = 0; k < history.size(); k++) {
                int[] move = history.get(k);
                writer.write(move[0] + "," + move[1]);
                if (k < history.size() - 1) writer.write(";");
            }
            writer.write("\n");
        }
    }

    public void loadGame(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line = reader.readLine();
            a = Integer.parseInt(line.split("=")[1]);
            line = reader.readLine();
            b = Integer.parseInt(line.split("=")[1]);
            line = reader.readLine();
            playerATurn = Boolean.parseBoolean(line.split("=")[1]);
            line = reader.readLine();
            grid = new boolean[a + 1][b + 1];
            for (int i = 0; i <= a; i++) {
                line = reader.readLine();
                String[] values = line.split(" ");
                for (int j = 0; j <= b; j++) {
                    grid[i][j] = values[j].equals("1");
                }
            }
            line = reader.readLine();
            history = new ArrayList<>();
            String historyStr = line.split("=")[1];
            if (!historyStr.isEmpty()) {
                String[] moves = historyStr.split(";");
                for (String move : moves) {
                    String[] coords = move.split(",");
                    int i = Integer.parseInt(coords[0]);
                    int j = Integer.parseInt(coords[1]);
                    history.add(new int[]{i, j});
                }
            }
        }
    }

    public boolean isLosingMove(int i, int j) {
        return i == 0 && j == 0;
    }

    public boolean[][] getGrid() { return grid; }
    public boolean isPlayerATurn() { return playerATurn; }

    public List<int[]> getHistory() {
        return new ArrayList<>(history);
    }

    public int[] getCoupAleatoire() {
        List<int[]> coupsValides = new ArrayList<>();

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j]) {
                    coupsValides.add(new int[]{i, j});
                }
            }
        }

        if (coupsValides.isEmpty()) {
            return null;
        }

        Random random = new Random();
        return coupsValides.get(random.nextInt(coupsValides.size()));
    }
    
}
