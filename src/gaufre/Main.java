package gaufre;

import gaufre.controller.GaufreController;
import gaufre.model.GaufreGame;
import gaufre.view.GaufreView;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        GaufreGame game = new GaufreGame(5, 7);
        GaufreView view = new GaufreView(game);
        GaufreController controller = new GaufreController(game, view);

        JFrame frame = new JFrame("La gaufre empoisonnée");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(view);
        frame.pack();
        frame.setVisible(true);
    }
}
