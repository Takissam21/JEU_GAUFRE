package gaufre.controller;

import gaufre.view.GaufreView;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class HistoryManager {
    private String scoresFile = "scores.txt";
    private String historyFile = "historique.txt";
    private int partieNumero = 1;
    private GaufreView view;

    public HistoryManager(GaufreView view) {
        this.view = view;
        loadPartieNumero();
    }

    // Getter pour le numéro de partie
    public int getPartieNumero() {
        return partieNumero;
    }

    // Incrémenter le numéro de partie
    public void incrementPartieNumero() {
        partieNumero++;
    }

    // Charger le dernier numéro de partie utilisé
    private void loadPartieNumero() {
        try (BufferedReader reader = new BufferedReader(new FileReader(historyFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(" \\| ");
                if (parts.length > 0) {
                    try {
                        int num = Integer.parseInt(parts[0].trim());
                        partieNumero = Math.max(partieNumero, num + 1);
                    } catch (NumberFormatException e) {
                        System.err.println("Ligne mal formatée dans historique.txt : " + line);
                        continue;
                    }
                }
            }
        } catch (IOException ex) {
            partieNumero = 1;
        }
    }

    // Sauvegarde du score dans scores.txt
    public void saveScore(String winner, int moveCount) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(scoresFile, true))) {
            writer.write(winner + " a gagné en " + moveCount + " coups\n");
        } catch (IOException ex) {
            view.showMessage("Erreur lors de l'enregistrement du score : " + ex.getMessage());
        }
    }

    // Sauvegarde de l'historique dans historique.txt
    public void saveHistory(String result, int score, List<int[]> history, boolean isPlayerATurn) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(historyFile, true))) {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            String date = now.format(formatter);

            StringBuilder moves = new StringBuilder("[");
            for (int k = 0; k < history.size(); k++) {
                int[] move = history.get(k);
                moves.append("(").append(move[0]).append(",").append(move[1]).append(")");
                if (k < history.size() - 1) {
                    moves.append(", ");
                }
            }
            moves.append("]");

            writer.write(partieNumero + " | " + date + " | " + result + " | Score: " + score + " | Coups: " + moves.toString() + "\n");
        } catch (IOException ex) {
            view.showMessage("Erreur lors de l'enregistrement de l'historique : " + ex.getMessage());
        }
    }

// Affichage du classement : nombre de victoires pour chaque joueur
public void showClassement() {
    try (BufferedReader reader = new BufferedReader(new FileReader(scoresFile))) {
        int playerAWins = 0;
        int playerBWins = 0;
        String line;

        // Compter les victoires de chaque joueur
        while ((line = reader.readLine()) != null) {
            if (line.trim().isEmpty()) continue;
            if (line.startsWith("Joueur A")) {
                playerAWins++;
            } else if (line.startsWith("Joueur B")) {
                playerBWins++;
            }
        }

        // Construire le message
        String message = "Classement :\nLe joueur A a gagné " + playerAWins + " fois et le joueur B a gagné " + playerBWins + " fois.";
        if (playerAWins == 0 && playerBWins == 0) {
            message = "Classement :\nAucune victoire enregistrée.";
        }
        view.showMessage(message);
    } catch (IOException ex) {
        view.showMessage("Erreur lors de la lecture du classement : " + ex.getMessage());
    }
}
} 
