package gaufre.view;

import gaufre.model.GaufreGame;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class GaufreView extends JComponent {
    private static final int CELL_SIZE = 40;
    private GaufreGame game;
    private JLabel statusLabel;
    private JButton newGameButton;
    private JButton undoButton;
    private JButton saveButton;
    private JButton loadButton;
    private Rectangle gridBounds;
    private BufferedImage beigeImage;
    private BufferedImage blackImage;
    private BufferedImage vertImage;
    private BufferedImage redImage;
    private int hoverRow = -1;
    private int hoverCol = -1;

    public GaufreView(GaufreGame game) {
        this.game = game;

        // Charger les images
        try {
            beigeImage = ImageIO.read(new File("images/gaufre_Beige.png"));
            blackImage = ImageIO.read(new File("images/gaufre_black.png"));
            vertImage = ImageIO.read(new File("images/gaufre_vert.png"));
            redImage = ImageIO.read(new File("images/gaufre_red.png"));
        } catch (IOException e) {
            e.printStackTrace();
            beigeImage = null;
            blackImage = null;
            vertImage = null;
            redImage = null;
        }

        setLayout(null);

        // Les boutons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        newGameButton = new JButton("Nouvelle partie");
        undoButton = new JButton("Annuler");
        saveButton = new JButton("Sauvegarder");
        loadButton = new JButton("Restaurer");

        Dimension buttonSize = new Dimension(120, 30);
        newGameButton.setPreferredSize(buttonSize);
        undoButton.setPreferredSize(buttonSize);
        saveButton.setPreferredSize(buttonSize);
        loadButton.setPreferredSize(buttonSize);

        buttonPanel.add(newGameButton);
        buttonPanel.add(undoButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(loadButton);

        int rows = game.getGrid().length;
        int cols = game.getGrid()[0].length;

        int buttonPanelHeight = 40;
        int buttonPanelWidth = 4 * (120 + 5) + 10;
        buttonPanel.setBounds(0, 0, buttonPanelWidth, buttonPanelHeight);
        add(buttonPanel);

        gridBounds = new Rectangle(0, buttonPanelHeight, cols * CELL_SIZE, rows * CELL_SIZE);

        statusLabel = new JLabel("Tour du joueur A");
        statusLabel.setBounds(0, buttonPanelHeight + rows * CELL_SIZE, Math.max(cols * CELL_SIZE, buttonPanelWidth), 30);
        add(statusLabel);
    }

    // Getters pour accéder aux boutons
    public JButton getNewGameButton() {
        return newGameButton;
    }

    public JButton getUndoButton() {
        return undoButton;
    }

    public JButton getSaveButton() {
        return saveButton;
    }

    public JButton getLoadButton() {
        return loadButton;
    }

    // Méthodes pour gérer le survol
    public void setHoverCell(int row, int col) {
        this.hoverRow = row;
        this.hoverCol = col;
        repaint();
    }

    public int[] getHoverCell() {
        return new int[]{hoverRow, hoverCol};
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        boolean[][] grid = game.getGrid();
        int rows = grid.length;
        int cols = grid[0].length;

        int yOffset = 40;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                BufferedImage imageToDraw;

                // Case mangée
                if (!grid[i][j]) {
                    imageToDraw = blackImage;
                }
                // Case dans la zone de survol 
                else if (hoverRow != -1 && hoverCol != -1 && i >= hoverRow && j >= hoverCol) {
                    imageToDraw = redImage;
                }
                // Case poison 
                else if (i == 0 && j == 0) {
                    imageToDraw = vertImage;
                }
                // Case non mangée hors survol : beige
                else {
                    imageToDraw = beigeImage;
                }

                // Dessiner l'image ou une couleur de secours si l'image n'est pas chargée
                if (imageToDraw != null) {
                    g.drawImage(imageToDraw, j * CELL_SIZE, i * CELL_SIZE + yOffset, CELL_SIZE, CELL_SIZE, null);
                } else {
                    if (i == 0 && j == 0) {
                        g.setColor(Color.GREEN);
                    } else if (!grid[i][j]) {
                        g.setColor(Color.BLACK);
                    } else if (hoverRow != -1 && hoverCol != -1 && i >= hoverRow && j >= hoverCol) {
                        g.setColor(Color.RED);
                    } else {
                        g.setColor(Color.WHITE);
                    }
                    g.fillRect(j * CELL_SIZE, i * CELL_SIZE + yOffset, CELL_SIZE, CELL_SIZE);
                }

                // Dessiner les bordures de la grille
                g.setColor(Color.BLACK);
                g.drawRect(j * CELL_SIZE, i * CELL_SIZE + yOffset, CELL_SIZE, CELL_SIZE);
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        int rows = game.getGrid().length;
        int cols = game.getGrid()[0].length;
        int buttonPanelWidth = 4 * (120 + 5) + 10;
        int minWidth = Math.max(cols * CELL_SIZE, buttonPanelWidth);
        return new Dimension(minWidth, rows * CELL_SIZE + 40 + 30);
    }

    public void updateView() {
        statusLabel.setText(game.isPlayerATurn() ? "Tour du joueur A" : "Tour du joueur B");
        repaint();
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public int[] getCellAt(int x, int y) {
        int yOffset = 40;
        int i = (y - yOffset) / CELL_SIZE;
        int j = x / CELL_SIZE;
        return new int[]{i, j};
    }

    public boolean isClickInGrid(int x, int y) {
        return gridBounds.contains(x, y);
    }
}
