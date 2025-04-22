package gaufre.controller;

import gaufre.model.GaufreGame;
import gaufre.view.GaufreView;
import gaufre.model.GaufreIA;
import gaufre.model.GaufreIAMinMax;

import java.awt.event.*;
import java.io.IOException;

public class GaufreController extends MouseAdapter implements ActionListener {
    private GaufreGame model;
    private GaufreView view;
    private GaufreIA ia = new GaufreIAMinMax(false, 6); // IA = joueur B, profondeur 6
    private HistoryManager historyManager;

    public GaufreController(GaufreGame model, GaufreView view) {
        this.model = model;
        this.view = view;
        this.historyManager = new HistoryManager(view);

        view.addMouseListener(this);
        view.addMouseMotionListener(this);

        view.getNewGameButton().addActionListener(this);
        view.getUndoButton().addActionListener(this);
        view.getSaveButton().addActionListener(this);
        view.getLoadButton().addActionListener(this);
        view.getAbandonButton().addActionListener(this);
        view.getClassementButton().addActionListener(this);

        // Ajouter un écouteur pour le bouton "Suggérer un coup"
        view.getSuggestButton().addActionListener(e -> suggestMove());

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
                    historyManager.saveScore(winner, model.getHistory().size());
                    historyManager.saveHistory("Victoire de " + winner, model.getHistory().size(), model.getHistory(), model.isPlayerATurn());
                    historyManager.incrementPartieNumero();
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
        if (!model.getGrid()[0][0]) return;
        int[] coup = ia.calculerMeilleurCoup(model);
        if (coup == null) {
            view.showMessage("IA n'a pas trouvé de coup !");
            return;
        }
        if (model.playMove(coup[0], coup[1])) {
            view.updateView();
            if (model.isLosingMove(coup[0], coup[1])) {
                String winner = model.isPlayerATurn() ? "Joueur A" : "Joueur B";
                view.showMessage(winner + " a gagné !");
                historyManager.saveScore(winner, model.getHistory().size());
                historyManager.saveHistory("Victoire de " + winner, model.getHistory().size(), model.getHistory(), model.isPlayerATurn());
                historyManager.incrementPartieNumero();
            }
        } else {
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
            case "Abandonner":
                String currentPlayer = model.isPlayerATurn() ? "Joueur A" : "Joueur B";
                view.showMessage(currentPlayer + " a abandonné la partie !");
                model.resetGame();
                view.updateView();
                break;
            case "Classement":
                historyManager.showClassement();
                break;
        }
    }

    // Nouvelle méthode pour suggérer un coup
    private void suggestMove() {
        int[] suggestedMove = model.getCoupAleatoire();
        if (suggestedMove != null) {
            view.setHoverCell(suggestedMove[0], suggestedMove[1]); // Mettre en surbrillance le coup suggéré
        } else {
            view.showMessage("Aucun coup valide disponible !");
        }
    }
}
