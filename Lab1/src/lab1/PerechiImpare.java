package lab1;

import java.util.function.Consumer;

/**
 * Varianta 4: sumele pozițiilor numerelor impare, luate două câte două.
 * Logica de căutare este comună pentru toate cele patru fire de execuție.
 */
final class PerechiImpare {

    /** Pauza dintre două perechi, ca intercalarea firelor să fie vizibilă. */
    private static final int PAUZA_MS = 30;

    private PerechiImpare() {
    }

    /**
     * Parcurge tabloul de la poziția {@code from} cu pasul {@code step} (+1 sau -1)
     * și transmite către {@code out} câte o linie pentru fiecare pereche găsită.
     */
    static void cauta(int[] mas, int from, int step, Consumer<String> out) throws InterruptedException {
        int prima = -1; // poziția primului număr impar din perechea curentă
        for (int i = from; i >= 0 && i < mas.length; i += step) {
            if (mas[i] % 2 == 0) {
                continue;
            }
            if (prima < 0) {
                prima = i;
                continue;
            }
            out.accept(String.format("poz %2d + %2d = %3d   (valori %3d, %3d)",
                    prima, i, prima + i, mas[prima], mas[i]));
            prima = -1;
            Thread.sleep(PAUZA_MS);
        }
    }
}
