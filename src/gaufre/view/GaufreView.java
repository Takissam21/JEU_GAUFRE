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
    private JButton abandonButton;
    private JButton classementButton;
    private JButton suggestButton; // Nouveau bouton pour suggérer un coup
    private JCheckBox iaCheckBox;
    private Rectangle gridBounds;
    private BufferedImage beigeImage, blackImage, vertImage, redImage;
    private int hoverRow = -1;
    private int hoverCol = -1;

    public GaufreView(GaufreGame game) {
        this.game = game;

        // Chargement des images 
        try {
            beigeImage = ImageIO.read(new File("images/gaufre_Beige.png"));
            blackImage = ImageIO.read(new File("images/gaufre_black.png"));
            vertImage = ImageIO.read(new File("images/gaufre_vert.png"));
            redImage = ImageIO.read(new File("images/gaufre_red.png"));
        } catch (IOException e) {
            e.printStackTrace();
            beigeImage = blackImage = vertImage = redImage = null;
        }

        setLayout(null);

        int rows = game.getGrid().length;
        int cols = game.getGrid()[0].length;

        // Hauteur et largeur des panneaux
        int statusPanelHeight = 30;
        int buttonPanelHeight = 40;
        int buttonPanelWidth = 8 * (120 + 5) + 10; // Ajusté pour le nouveau bouton

        // Panneau du statut (placé en haut)
        JPanel statusPanel = new JPanel();
        statusLabel = new JLabel("Tour du joueur A");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 14)); // Police plus grande et en gras
        statusPanel.add(statusLabel);
        statusPanel.setBackground(new Color(220, 220, 220)); // Fond gris clair
        statusPanel.setBounds(0, 0, Math.max(cols * CELL_SIZE, buttonPanelWidth), statusPanelHeight);
        add(statusPanel);

        // Espacement entre le statut et la grille
        int verticalSpacing = 20; // Espacement vertical entre les éléments
        int gridWidth = cols * CELL_SIZE;
        int gridHeight = rows * CELL_SIZE;
        int gridX = (buttonPanelWidth - gridWidth) / 2; // Centrer horizontalement par rapport au panneau des boutons
        int gridY = statusPanelHeight + verticalSpacing; // Positionner la grille en dessous du statut avec espacement
        gridBounds = new Rectangle(gridX, gridY, gridWidth, gridHeight);

        // Panneau des boutons (placé en bas)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5)); // Centrer les boutons
        buttonPanel.setBackground(new Color(220, 220, 220)); // Fond gris clair pour le panneau

        // Initialisation des boutons
        newGameButton = new JButton("Nouvelle partie");
        undoButton = new JButton("Annuler");
        saveButton = new JButton("Sauvegarder");
        loadButton = new JButton("Restaurer");
        abandonButton = new JButton("Abandonner");
        classementButton = new JButton("Classement");
        suggestButton = new JButton("Suggérer un coup"); // Nouveau bouton
        iaCheckBox = new JCheckBox("Joueur B = IA");
        iaCheckBox.setSelected(true);

        // Style des boutons
        Dimension buttonSize = new Dimension(120, 30);
        newGameButton.setPreferredSize(buttonSize);
        undoButton.setPreferredSize(buttonSize);
        saveButton.setPreferredSize(buttonSize);
        loadButton.setPreferredSize(buttonSize);
        abandonButton.setPreferredSize(buttonSize);
        classementButton.setPreferredSize(buttonSize);
        suggestButton.setPreferredSize(buttonSize); // Ajustement de la taille du nouveau bouton
        iaCheckBox.setPreferredSize(new Dimension(120, 30));

        // Ajout d'un style aux boutons (couleurs que tu as aimées)
        newGameButton.setBackground(new Color(50, 150, 50)); // Vert pour "Nouvelle partie"
        newGameButton.setForeground(Color.WHITE);
        undoButton.setBackground(new Color(200, 100, 50)); // Orange pour "Annuler"
        undoButton.setForeground(Color.WHITE);
        saveButton.setBackground(new Color(50, 100, 200)); // Bleu pour "Sauvegarder"
        saveButton.setForeground(Color.WHITE);
        loadButton.setBackground(new Color(50, 100, 200)); // Bleu pour "Restaurer"
        loadButton.setForeground(Color.WHITE);
        abandonButton.setBackground(new Color(200, 50, 50)); // Rouge pour "Abandonner"
        abandonButton.setForeground(Color.WHITE);
        classementButton.setBackground(new Color(100, 100, 100)); // Gris pour "Classement"
        classementButton.setForeground(Color.WHITE);
        iaCheckBox.setBackground(new Color(220, 220, 220)); // Fond assorti pour la case à cocher
                
        // Ajout des boutons au panneau
        buttonPanel.add(newGameButton);
        buttonPanel.add(undoButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(loadButton);
        buttonPanel.add(abandonButton);
        buttonPanel.add(classementButton);
        buttonPanel.add(suggestButton); // Ajout du nouveau bouton
        buttonPanel.add(iaCheckBox);

        // Positionner le panneau des boutons en bas avec espacement
        int buttonPanelY = gridY + gridHeight + verticalSpacing;
        buttonPanel.setBounds(0, buttonPanelY, buttonPanelWidth, buttonPanelHeight);
        add(buttonPanel);
    }

    public JButton getAbandonButton() { return abandonButton; }
    public JButton getClassementButton() { return classementButton; }
    public JButton getSuggestButton() { return suggestButton; } // Getter pour le nouveau bouton

    public JButton getNewGameButton() { return newGameButton; }
    public JButton getUndoButton() { return undoButton; }
    public JButton getSaveButton() { return saveButton; }
    public JButton getLoadButton() { return loadButton; }
    public boolean isIAChecked() { return iaCheckBox.isSelected(); }

    public void setHoverCell(int row, int col) {
        hoverRow = row;
        hoverCol = col;
        repaint();
    }

    public int[] getHoverCell() { return new int[]{hoverRow, hoverCol}; }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        boolean[][] grid = game.getGrid();
        int rows = grid.length;
        int cols = grid[0].length;

        int yOffset = 30 + 20; // Ajustement pour le statut et l'espacement
        int xOffset = (8 * (120 + 5) + 10 - cols * CELL_SIZE) / 2; // Ajusté pour le nouveau bouton
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                BufferedImage img;
                if (!grid[i][j]) img = blackImage;
                else if (hoverRow != -1 && hoverCol != -1 && i >= hoverRow && j >= hoverCol) img = redImage;
                else if (i == 0 && j == 0) img = vertImage;
                else img = beigeImage;

                if (img != null)
                    g.drawImage(img, j * CELL_SIZE + xOffset, i * CELL_SIZE + yOffset, CELL_SIZE, CELL_SIZE, null);
                else {
                    if (i == 0 && j == 0) g.setColor(Color.GREEN);
                    else if (!grid[i][j]) g.setColor(Color.BLACK);
                    else if (hoverRow != -1 && hoverCol != -1 && i >= hoverRow && j >= hoverCol) g.setColor(Color.RED);
                    else g.setColor(Color.WHITE);
                    g.fillRect(j * CELL_SIZE + xOffset, i * CELL_SIZE + yOffset, CELL_SIZE, CELL_SIZE);
                }
                g.setColor(Color.BLACK);
                g.drawRect(j * CELL_SIZE + xOffset, i * CELL_SIZE + yOffset, CELL_SIZE, CELL_SIZE);
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        int rows = game.getGrid().length;
        int cols = game.getGrid()[0].length;
        int buttonPanelWidth = 8 * (120 + 5) + 10; // Ajusté pour le nouveau bouton
        int minWidth = Math.max(cols * CELL_SIZE, buttonPanelWidth);
        return new Dimension(minWidth, rows * CELL_SIZE + 30 + 40 + 40); // Ajustement pour le statut, espacement et boutons
    }

    public void updateView() {
        // Mettre à jour le statut avec des couleurs pour le joueur actif
        if (game.isPlayerATurn()) {
            statusLabel.setText("Tour du joueur A");
            statusLabel.setForeground(new Color(0, 102, 204)); // Bleu pour Joueur A
        } else {
            statusLabel.setText("Tour du joueur B");
            statusLabel.setForeground(new Color(204, 0, 0)); // Rouge pour Joueur B
        }
        repaint();
    }

    public void showMessage(String message) { JOptionPane.showMessageDialog(this, message); }

    public int[] getCellAt(int x, int y) {
        int yOffset = 30 + 20; // Ajustement pour le statut et l'espacement
        int xOffset = (8 * (120 + 5) + 10 - game.getGrid()[0].length * CELL_SIZE) / 2; // Ajusté pour le nouveau bouton
        return new int[]{(y - yOffset) / CELL_SIZE, (x - xOffset) / CELL_SIZE};
    }

    public boolean isClickInGrid(int x, int y) { return gridBounds.contains(x, y); }
}
