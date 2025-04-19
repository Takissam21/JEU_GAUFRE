package gaufre.model;

public class GaufreGame {
    private int a, b;
    private boolean[][] grid;
    private boolean playerATurn;

    public GaufreGame(int a, int b) {
        this.a = a;
        this.b = b;
        this.grid = new boolean[a + 1][b + 1];
        this.playerATurn = true;

        for (int i = 0; i <= a; i++) {
            for (int j = 0; j <= b; j++) {
                grid[i][j] = true; 
            }
        }
    }

    public boolean playMove(int i, int j) {
        if (i < 0 || i > a || j < 0 || j > b || !grid[i][j]) {
            return false;
        }

        for (int x = i; x <= a; x++) {
            for (int y = j; y <= b; y++) {
                grid[x][y] = false;
            }
        }

        playerATurn = !playerATurn;
        return true;
    }

    public void resetGame() {
        playerATurn = true;
        for (int i = 0; i <= a; i++) {
            for (int j = 0; j <= b; j++) {
                grid[i][j] = true;
            }
        }
    }

    public boolean isLosingMove(int i, int j) {
        return i == 0 && j == 0;
    }

    public boolean[][] getGrid() { 
        return grid; 
    }
    public boolean isPlayerATurn() { 
        return playerATurn; 
    }
}