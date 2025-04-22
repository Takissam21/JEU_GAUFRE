package gaufre;

import gaufre.controller.GaufreController;
import gaufre.model.GaufreGame;
import gaufre.view.GaufreView;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        System.out.println(">>> DÉMARRAGE JEU <<<");

        SwingUtilities.invokeLater(() -> {
            GaufreGame game = new GaufreGame(5, 7);
            GaufreView view = new GaufreView(game);
            GaufreController controller = new GaufreController(game, view);

            JFrame frame = new JFrame("La gaufre empoisonnée");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().setBackground(new Color(245, 245, 220)); // Fond beige pour le JFrame
            frame.add(view);
            frame.pack();
            frame.setMinimumSize(new java.awt.Dimension(600, 400)); // Taille minimale pour éviter une fenêtre trop petite
            frame.setLocationRelativeTo(null); // Centrer la fenêtre
            frame.setVisible(true);
        });
    }
}