package lab3;

import java.util.function.Consumer;

/**
 * Firele 3 și 4 (Spranceana Marius), varianta 4: parcurgerea unui interval de numere.
 * Th3 parcurge de la început intervalul [234, 987], Th4 parcurge de la sfârșit intervalul [123, 890].
 * Metode de sincronizare folosite: interrupt() (primit în sleep()) și isAlive().
 */
final class SpranceanaMarius {

    private static final int NUMERE_PE_LINIE = 12;
    private static final int PAUZA_MS = 15;
    private static final int VERIFICARE_MS = 50;

    private SpranceanaMarius() {
    }

    static class FirInterval extends Fir {
        private final int from, to, step;

        /** Parcurge numerele de la from până la to inclusiv, cu pasul step (+1 sau -1). */
        FirInterval(String nume, String text, int from, int to, int step,
                Consumer<String> linii, Consumer<String> litere) {
            super(nume, text, linii, litere);
            this.from = from;
            this.to = to;
            this.step = step;
        }

        @Override
        public void run() {
            parcurge();
            asteaptaSemnal();      // firul dinaintea mea din lanț și-a terminat sarcina
            urmatorul.interrupt(); // transmit semnalul mai departe
            while (precedent.isAlive()) { // aștept până când firul precedent își termină textul
                pauza(VERIFICARE_MS);
            }
            afiseazaText();
        }

        private void parcurge() {
            StringBuilder linie = new StringBuilder();
            int peLinie = 0;
            for (int i = from; i != to + step; i += step) {
                linie.append(String.format("%4d", i));
                if (++peLinie == NUMERE_PE_LINIE || i == to) {
                    afiseaza(linie.toString());
                    linie.setLength(0);
                    peLinie = 0;
                    pauza(PAUZA_MS);
                }
            }
        }
    }
}
