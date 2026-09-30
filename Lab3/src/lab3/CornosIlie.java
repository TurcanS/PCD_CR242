package lab3;

import java.util.function.Consumer;

/**
 * Firele 1 și 2 (Cornos Ilie), varianta 4: sumele pozițiilor numerelor impare, două câte două.
 * Metode de sincronizare folosite: interrupt() și join().
 */
final class CornosIlie {

    private static final int PAUZA_MS = 30;

    private CornosIlie() {
    }

    /** Sarcina 1: căutarea începe de la primul element. După sarcini afișează prenumele. */
    static class Th1 extends Fir {
        private final int[] mas;

        Th1(int[] mas, Consumer<String> linii, Consumer<String> litere) {
            super("Th1", "Prenumele: Ilie, Marius", linii, litere);
            this.mas = mas;
        }

        @Override
        public void run() {
            cauta(this, mas, 0, 1);
            urmatorul.interrupt(); // pornește lanțul de semnale: sarcina lui Th1 este gata
            try {
                precedent.join(); // așteaptă până când Th4 își termină textul
            } catch (InterruptedException e) {
                return;
            }
            afiseazaText();
        }
    }

    /** Sarcina 2: căutarea începe de la ultimul element. După sarcini afișează numele. */
    static class Th2 extends Fir {
        private final int[] mas;

        Th2(int[] mas, Consumer<String> linii, Consumer<String> litere) {
            super("Th2", "Numele: Cornos, Spranceana", linii, litere);
            this.mas = mas;
        }

        @Override
        public void run() {
            cauta(this, mas, mas.length - 1, -1);
            asteaptaSemnal(); // semnalul ajunge la Th2 după ce toate sarcinile sunt gata
            afiseazaText();
        }
    }

    /** Parcurge tabloul de la poziția from cu pasul step și afișează perechile de numere impare. */
    private static void cauta(Fir fir, int[] mas, int from, int step) {
        int prima = -1; // poziția primului număr impar din perechea curentă
        for (int i = from; i >= 0 && i < mas.length; i += step) {
            if (mas[i] % 2 == 0) {
                continue;
            }
            if (prima < 0) {
                prima = i;
                continue;
            }
            fir.afiseaza(String.format("poz %2d + %2d = %3d   (valori %3d, %3d)",
                    prima, i, prima + i, mas[prima], mas[i]));
            prima = -1;
            fir.pauza(PAUZA_MS);
        }
    }
}
