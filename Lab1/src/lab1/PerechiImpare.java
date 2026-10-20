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
        int total = 0;      // totalul curent al acestui fir
        int adunate = 0;    // câte sume conține totalul (după două se începe de la capăt)
        for (int i = from; i >= 0 && i < mas.length; i += step) {
            if (mas[i] % 2 == 0) {
                continue;
            }
            if (prima < 0) {
                prima = i;
                continue;
            }
            if (adunate == 2) {
                // th1 s-a adunat cu th1 (th2 cu th2) - totalul se anulează
                total = 0;
                adunate = 0;
            }
            total += prima + i;
            adunate++;
            out.accept(String.format("pozzzz %2d + %2d = %3d   (valori %3d, %3d)   total = %3d",
                    prima, i, prima + i, mas[prima], mas[i], total));
            prima = -1;
            Thread.sleep(PAUZA_MS);
        }
    }
}
