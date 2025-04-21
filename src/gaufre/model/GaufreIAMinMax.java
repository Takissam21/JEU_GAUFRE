
package gaufre.model;
import gaufre.model.GaufreGame;
import java.util.List;

public class GaufreIAMinMax implements GaufreIA {

    private final boolean iaEstJoueurA;
    private final int profondeurMax;

    public GaufreIAMinMax(boolean iaEstJoueurA, int profondeurMax) {
        System.out.println(">>> Constructeur GaufreIAMinMax <<<");
        this.iaEstJoueurA = iaEstJoueurA;
        this.profondeurMax = profondeurMax;
    }

    @Override
    public int[] calculerMeilleurCoup(GaufreGame game) {
        System.out.println(">>> GaufreIAMinMax appelée <<<");
        System.out.println(">>> Début calcul IA <<<");

        EtatGaufre etatInitial = new EtatGaufre(game.getGrid(), game.isPlayerATurn());

        int meilleurScore = Integer.MIN_VALUE;
        int[] meilleurCoup = null;

        for (int[] coup : etatInitial.getCoupsPossibles()) {
            EtatGaufre etatFils = etatInitial.jouerCoup(coup[0], coup[1]);
            int score = minmax(etatFils, profondeurMax - 1, false);

            if (score > meilleurScore) {
                meilleurScore = score;
                meilleurCoup = coup;
            }
        }

        if (meilleurCoup == null && !etatInitial.getCoupsPossibles().isEmpty()) {
            meilleurCoup = etatInitial.getCoupsPossibles().get(0);
        }

        System.out.println(">>> Fin calcul IA <<<");
        return meilleurCoup;
    }

    private int minmax(EtatGaufre etat, int profondeur, boolean max) {
        if (etat.estGagnantPourIA(iaEstJoueurA)) return 1000;
        if (etat.estPerdantPourIA(iaEstJoueurA)) return -1000;
        if (profondeur == 0) return evaluer(etat);

        List<int[]> coups = etat.getCoupsPossibles();
        if (coups.isEmpty()) return 0;

        int best = max ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (int[] coup : coups) {
            EtatGaufre fils = etat.jouerCoup(coup[0], coup[1]);
            int score = minmax(fils, profondeur - 1, !max);
            best = max ? Math.max(best, score) : Math.min(best, score);
        }

        return best;
    }

    private int evaluer(EtatGaufre etat) {
        int poids = etat.getCoupsPossibles().size();
        return (etat.isJoueurA() == iaEstJoueurA) ? poids : -poids;
    }
}
