package gaufre.controller;

import gaufre.model.GaufreGame;
import gaufre.view.GaufreView;

import java.awt.event.*;
import java.io.IOException;

public class GaufreController extends MouseAdapter implements ActionListener, MouseMotionListener {
    private GaufreGame model;
    private GaufreView view;

    public GaufreController(GaufreGame model, GaufreView view) {
        this.model = model;
        this.view = view;

        // les écouteurs pour les clics et les mouvements de la souris
        view.addMouseListener(this);
        view.addMouseMotionListener(this);

        // les écouteurs pour les boutons directement
        System.out.println("Ajout de l'écouteur pour Nouvelle partie");
        view.getNewGameButton().addActionListener(this);
        System.out.println("Ajout de l'écouteur pour Annuler");
        view.getUndoButton().addActionListener(this);
        System.out.println("Ajout de l'écouteur pour Sauvegarder");
        view.getSaveButton().addActionListener(this);
        System.out.println("Ajout de l'écouteur pour Restaurer");
        view.getLoadButton().addActionListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (view.isClickInGrid(e.getX(), e.getY())) {
            int[] cell = view.getCellAt(e.getX(), e.getY());
            int i = cell[0];
            int j = cell[1];
            if (model.playMove(i, j)) {
                view.updateView();
                if (model.isLosingMove(i, j)) {
                    String winner = model.isPlayerATurn() ? "Joueur A" : "Joueur B";
                    view.showMessage(winner + " a gagné !");
                }
            } else {
                view.showMessage("Coup invalide !");
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (view.isClickInGrid(e.getX(), e.getY())) {
            int[] cell = view.getCellAt(e.getX(), e.getY());
            int i = cell[0];
            int j = cell[1];
            // Vérifier si la case est non mangée et n'est pas la case empoisonnée
            if (model.getGrid()[i][j]) {
                view.setHoverCell(i, j);
            } else {
                view.setHoverCell(-1, -1);
            }
        } else {
            view.setHoverCell(-1, -1);
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        view.setHoverCell(-1, -1);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        System.out.println("Action reçue : " + command);
        switch (command) {
            case "Nouvelle partie":
                model.resetGame();
                view.updateView();
                break;
            case "Annuler":
                if (model.undoMove()) {
                    view.updateView();
                } else {
                    view.showMessage("Aucun coup à annuler !");
                }
                break;
            case "Sauvegarder":
                try {
                    model.saveGame("partie.txt");
                    view.showMessage("Partie sauvegardée avec succès !");
                } catch (IOException ex) {
                    view.showMessage("Erreur lors de la sauvegarde : " + ex.getMessage());
                }
                break;
            case "Restaurer":
                try {
                    model.loadGame("partie.txt");
                    view.updateView();
                    view.showMessage("Partie restaurée avec succès !");
                } catch (IOException ex) {
                    view.showMessage("Erreur lors de la restauration : " + ex.getMessage());
                }
                break;
        }
    }
}
