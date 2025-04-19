package gaufre.view;

import gaufre.model.GaufreGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class GaufreView extends JComponent {
    private static final int CELL_SIZE = 40;
    private GaufreGame game;
    private JLabel statusLabel;
    private JButton newGameButton;
    private Rectangle gridBounds;

    public GaufreView(GaufreGame game) {
        this.game = game;

        setLayout(null);

        // Panneau pour le bouton (en haut)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        newGameButton = new JButton("Nouvelle partie");
        buttonPanel.add(newGameButton);

        // Calculer les dimensions
        int rows = game.getGrid().length;
        int cols = game.getGrid()[0].length;

        // Positionner le panneau des boutons
        int buttonPanelHeight = 40;
        buttonPanel.setBounds(0, 0, cols * CELL_SIZE, buttonPanelHeight);
        add(buttonPanel);

        // Définir la zone de la grille pour les clics
        gridBounds = new Rectangle(0, buttonPanelHeight, cols * CELL_SIZE, rows * CELL_SIZE);

        // Positionner le label de statut
        statusLabel = new JLabel("Tour du joueur A");
        statusLabel.setBounds(0, buttonPanelHeight + rows * CELL_SIZE, cols * CELL_SIZE, 30);
        add(statusLabel);
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
                if (grid[i][j]) {
                    g.setColor(Color.WHITE);
                } else {
                    g.setColor(Color.GRAY);
                }
                g.fillRect(j * CELL_SIZE, i * CELL_SIZE + yOffset, CELL_SIZE, CELL_SIZE);
                g.setColor(Color.BLACK);
                g.drawRect(j * CELL_SIZE, i * CELL_SIZE + yOffset, CELL_SIZE, CELL_SIZE);
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        int rows = game.getGrid().length;
        int cols = game.getGrid()[0].length;
        return new Dimension(cols * CELL_SIZE, rows * CELL_SIZE + 40 + 30);
    }

    public void updateView() {
        statusLabel.setText(game.isPlayerATurn() ? "Tour du joueur A" : "Tour du joueur B");
        repaint();
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public void addNewGameListener(ActionListener listener) {
        newGameButton.addActionListener(listener);
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
