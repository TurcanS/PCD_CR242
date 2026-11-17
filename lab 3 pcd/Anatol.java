/**
 * PARTEA LUI ANATOL - firele Th1 și Th2
 *
 * Th1 - Suma pozițiilor numerelor pare două câte două,
 *       căutarea și sumarea începând de la primul element.
 *       La final afișează PRENUMELE.
 * Th2 - Suma pozițiilor numerelor pare două câte două,
 *       căutarea și sumarea începând de la ultimul element.
 *       La final afișează NUMELE.
 *
 * Metode de sincronizare folosite:
 *   - Th1 cedează procesorul cu yield() cât timp Th4 nu a terminat
 *     de afișat grupa, abia apoi afișează prenumele.
 *   - Th2 "doarme" cu sleep() până când Th4 își termină sarcina
 *     și îl trezește cu interrupt(); după ce afișează numele,
 *     îl trezește pe Th4 cu interrupt().
 *
 * Firele sunt pornite din Main.
 */
public class Anatol {

    // ==========================================================================
    //  Firul 1
    // ==========================================================================
    static class Th1 extends Thread {
        Th1() { super("Th1"); }

        @Override
        public void run() {
            Main.afiseazaFir(getName(), "Pornește");
            sumaPozitiilorPareDeLaInceput();
            Main.afiseazaFir(getName(), "A terminat sarcina.");

            Main.afiseazaFir(getName(), "Aștept ca Th4 să termine (yield)...");

            // SINCRONIZARE - yield():
            // cât timp Th4 nu a terminat, Th1 cedează procesorul altor fire
            while (!Main.th4Terminat) {
                Thread.yield();
            }
            Main.afiseazaFir(getName(), "Th4 a terminat, afișez prenumele.");
            Main.afiseazaLiteraCuLitera(getName() + " - Prenumele: ", Main.PRENUME);
        }

        /** Caută numerele pare de la primul element și adună pozițiile lor două câte două. */
        void sumaPozitiilorPareDeLaInceput() {
            int[] a = Main.tablou;
            int pozInAsteptare = -1;   // poziția numărului par care încă nu are pereche
            int nrSuma = 0;

            for (int i = 0; i < a.length; i++) {
                if (a[i] % 2 == 0) {
                    if (pozInAsteptare == -1) {
                        pozInAsteptare = i;            // primul număr din pereche
                    } else {
                        nrSuma++;                      // al doilea număr din pereche
                        int suma = pozInAsteptare + i;
                        Main.afiseazaFir(getName(), "Suma " + nrSuma + ":  poz " + pozInAsteptare
                                + " (" + a[pozInAsteptare] + ")  +  poz " + i
                                + " (" + a[i] + ")  =  " + suma);
                        pozInAsteptare = -1;
                    }
                }
            }

            if (pozInAsteptare != -1) {
                Main.afiseazaFir(getName(), "Poziția " + pozInAsteptare + " ("
                        + a[pozInAsteptare] + ") a rămas fără pereche.");
            }
        }
    }

    // ==========================================================================
    //  Firul 2
    // ==========================================================================
    static class Th2 extends Thread {
        Th2() { super("Th2"); }

        @Override
        public void run() {
            Main.afiseazaFir(getName(), "Pornește");
            sumaPozitiilorPareDeLaSfarsit();
            Main.afiseazaFir(getName(), "A terminat sarcina. Aștept semnal de la Th4 (sleep)...");

            // SINCRONIZARE - sleep():
            // Th2 doarme "la nesfârșit" până când Th4 îl trezește cu interrupt()
            try {
                Thread.sleep(Long.MAX_VALUE);
            } catch (InterruptedException e) {
                // Th4 a apelat interrupt() -> Th2 se trezește și continuă
            }
            Main.afiseazaFir(getName(), "A primit semnalul de la Th4 (interrupt), afișez numele.");

            Main.afiseazaLiteraCuLitera(getName() + " - Numele: ", Main.NUME);

            // SINCRONIZARE - interrupt():
            // Th2 îl trezește pe Th4 ca să afișeze grupa
            Main.th4.interrupt();
        }

        /** Caută numerele pare de la ultimul element și adună pozițiile lor două câte două. */
        void sumaPozitiilorPareDeLaSfarsit() {
            int[] a = Main.tablou;
            int pozInAsteptare = -1;   // poziția numărului par care încă nu are pereche
            int nrSuma = 0;

            for (int i = a.length - 1; i >= 0; i--) {
                if (a[i] % 2 == 0) {
                    if (pozInAsteptare == -1) {
                        pozInAsteptare = i;            // primul număr din pereche
                    } else {
                        nrSuma++;                      // al doilea număr din pereche
                        int suma = pozInAsteptare + i;
                        Main.afiseazaFir(getName(), "Suma " + nrSuma + ":  poz " + pozInAsteptare
                                + " (" + a[pozInAsteptare] + ")  +  poz " + i
                                + " (" + a[i] + ")  =  " + suma);
                        pozInAsteptare = -1;
                    }
                }
            }

            if (pozInAsteptare != -1) {
                Main.afiseazaFir(getName(), "Poziția " + pozInAsteptare + " ("
                        + a[pozInAsteptare] + ") a rămas fără pereche.");
            }
        }
    }
}
