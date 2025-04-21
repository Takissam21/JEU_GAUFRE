package gaufre.controller;

import gaufre.model.GaufreGame;
import gaufre.view.GaufreView;

import gaufre.model.GaufreIA;
import gaufre.model.GaufreIAMinMax;

import java.awt.event.*;
import java.io.IOException;

public class GaufreController extends MouseAdapter implements ActionListener, MouseMotionListener {
    private GaufreGame model;
    private GaufreView view;

    private GaufreIA ia = new GaufreIAMinMax(false, 6); // IA = joueur B, profondeur 6

    public GaufreController(GaufreGame model, GaufreView view) {
        this.model = model;
        this.view = view;

        view.addMouseListener(this);
        view.addMouseMotionListener(this);

        System.out.println("Ajout de l'écouteur pour Nouvelle partie");
        view.getNewGameButton().addActionListener(this);
        System.out.println("Ajout de l'écouteur pour Annuler");
        view.getUndoButton().addActionListener(this);
        System.out.println("Ajout de l'écouteur pour Sauvegarder");
        view.getSaveButton().addActionListener(this);
        System.out.println("Ajout de l'écouteur pour Restaurer");
        view.getLoadButton().addActionListener(this);

        // Appel initial de l'IA 
        if (!model.isPlayerATurn() && view.isIAChecked()) {
            javax.swing.Timer timer = new javax.swing.Timer(200, evt -> jouerIA());
            timer.setRepeats(false);
            timer.start();
        }
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
                    return;
                }

                if (!model.isPlayerATurn() && view.isIAChecked()) {
                    javax.swing.Timer timer = new javax.swing.Timer(200, evt -> jouerIA());
                    timer.setRepeats(false);
                    timer.start();
                }
            } else {
                view.showMessage("Coup invalide !");
            }
        }
    }

    private void jouerIA() {
        System.out.println("IA joue...");
        System.out.println(">>> ia.getClass() = " + ia.getClass().getName());

        if (!model.getGrid()[0][0]) return;

        int[] coup = ia.calculerMeilleurCoup(model);

        if (coup == null) {
            System.out.println("Coup null !");
            view.showMessage("IA n'a pas trouvé de coup !");
            return;
        }

        System.out.println("Coup choisi : (" + coup[0] + ", " + coup[1] + ")");

        if (model.playMove(coup[0], coup[1])) {
            System.out.println("Coup accepté");
            view.updateView();
            if (model.isLosingMove(coup[0], coup[1])) {
                String winner = model.isPlayerATurn() ? "Joueur A" : "Joueur B";
                view.showMessage(winner + " a gagné !");
            }
        } else {
            System.out.println("Coup refusé par le modèle !");
            view.showMessage("Coup IA invalide : (" + coup[0] + ", " + coup[1] + ")");
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (view.isClickInGrid(e.getX(), e.getY())) {
            int[] cell = view.getCellAt(e.getX(), e.getY());
            int i = cell[0];
            int j = cell[1];
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

