package gaufre.model;


import java.util.ArrayList;
import java.util.List;

/* représentation d’un état du jeu de la gaufre */
public class EtatGaufre {
    private final boolean[][] grille;
    private final boolean joueurA; // true si c’est au joueur A de jouer

    public EtatGaufre(boolean[][] grille, boolean joueurA) {
        this.grille = copierGrille(grille);
        this.joueurA = joueurA;
    }

    /* retourne un nouvel état après avoir joué un coup */
    public EtatGaufre jouerCoup(int i, int j) {
        int rows = grille.length;
        int cols = grille[0].length;
        boolean[][] nouvelleGrille = copierGrille(grille);

        for (int x = i; x < rows; x++) {
            for (int y = j; y < cols; y++) {
                nouvelleGrille[x][y] = false;
            }
        }

        return new EtatGaufre(nouvelleGrille, !joueurA);
    }

    /* liste de tous les coups valides */
    public List<int[]> getCoupsPossibles() {
        List<int[]> coups = new ArrayList<>();
        for (int i = 0; i < grille.length; i++) {
            for (int j = 0; j < grille[0].length; j++) {
                if (grille[i][j]) {
                    coups.add(new int[]{i, j});
                }
            }
        }
        return coups;
    }

    /* retourne vrai si la partie est perdue (case poison mangée) */
    public boolean estPerdu() {
        return !grille[0][0];
    }

    /* retourne vrai si l’IA a gagné dans cet état */
    public boolean estGagnantPourIA(boolean iaEstJoueurA) {
        return estPerdu() && joueurA == iaEstJoueurA; // CORRIGÉ
    }

    /* retourne vrai si l’IA a perdu dans cet état */
    public boolean estPerdantPourIA(boolean iaEstJoueurA) {
        return estPerdu() && joueurA != iaEstJoueurA; // CORRIGÉ
    }

    public boolean[][] getGrille() {
        return copierGrille(grille);
    }

    public boolean isJoueurA() {
        return joueurA;
    }

    private boolean[][] copierGrille(boolean[][] original) {
        int rows = original.length;
        int cols = original[0].length;
        boolean[][] copie = new boolean[rows][cols];
        for (int i = 0; i < rows; i++) {
            System.arraycopy(original[i], 0, copie[i], 0, cols);
        }
        return copie;
    }
}
