package gaufre.controller;

import gaufre.model.GaufreGame;
import gaufre.view.GaufreView;

import java.awt.event.*;
import java.io.IOException;

public class GaufreController extends MouseAdapter implements ActionListener {
    private GaufreGame model;
    private GaufreView view;

    public GaufreController(GaufreGame model, GaufreView view) {
        this.model = model;
        this.view = view;

        view.addMouseListener(this);
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
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
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
