package gaufre.controller;

import gaufre.model.GaufreGame;
import gaufre.view.GaufreView;

import java.awt.event.*;

public class GaufreController implements MouseListener, ActionListener {
    private GaufreGame model;
    private GaufreView view;

    public GaufreController(GaufreGame model, GaufreView view) {
        this.model = model;
        this.view = view;

        view.addMouseListener(this);
        view.addNewGameListener(this);
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
    public void mousePressed(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if ("Nouvelle partie".equals(command)) {
            model.resetGame();
            view.updateView();
        }
    }
    
}
