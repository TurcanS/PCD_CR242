/**
 * PARTEA LUI CATALIN - firele Th2 și Th4
 *
 * Th2 - Suma pozițiilor numerelor pare două câte două,
 *       căutarea și sumarea începând de la ultimul element.
 *       La final afișează NUMELE.
 * Th4 - Parcurgerea de la sfârșit a intervalului [1000, 1567].
 *       La final afișează GRUPA.
 *
 * Metode de sincronizare folosite: sleep() și interrupt()
 *   - Th2 "doarme" cu sleep() până când Th4 își termină sarcina
 *     și îl trezește cu interrupt();
 *   - Th4 "doarme" cu sleep() până când Th2 termină de afișat numele
 *     și îl trezește cu interrupt().
 *
 * Firele sunt pornite din Main.
 */
public class Catalin {

    static final int TH4_DE = 1567;
    static final int TH4_PANA = 1000;

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
            // Th2 doarme până când Th4 îl trezește
            asteaptaSemnal();
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

    // ==========================================================================
    //  Firul 4
    // ==========================================================================
    static class Th4 extends Thread {
        Th4() { super("Th4"); }

        @Override
        public void run() {
            Main.afiseazaFir(getName(), "Pornește");
            parcurgeIntervalDeLaSfarsit();
            Main.afiseazaFir(getName(), "A terminat sarcina. Trezesc Th2 (interrupt) și aștept (sleep)...");

            // SINCRONIZARE - interrupt():
            // Th4 îl anunță pe Th2 că poate afișa numele
            Main.th2.interrupt();

            // SINCRONIZARE - sleep():
            // Th4 doarme până când Th2 termină de afișat numele și îl trezește
            asteaptaSemnal();

            Main.afiseazaLiteraCuLitera(getName() + " - Grupa: ", Main.GRUPA);
        }

        /** Parcurge intervalul [1000, 1567] de la sfârșit (descrescător), câte 10 numere pe rând. */
        void parcurgeIntervalDeLaSfarsit() {
            int numarate = 0;
            StringBuilder rand = new StringBuilder();
            for (int x = TH4_DE; x >= TH4_PANA; x--) {
                rand.append(x).append(" ");
                numarate++;
                if (numarate % 10 == 0) {
                    Main.afiseazaFir(getName(), rand.toString());
                    rand.setLength(0);
                }
                Main.pauza(5);   // ca să se vadă că firele lucrează în paralel
            }
            if (rand.length() > 0) {
                Main.afiseazaFir(getName(), rand.toString());
            }
            Main.afiseazaFir(getName(), "Total numere parcurse: " + numarate);
        }
    }

    // ==========================================================================
    //  Metoda de așteptare folosită de Th2 și Th4
    // ==========================================================================

    /** Firul curent doarme până când un alt fir îi apelează interrupt(). */
    static void asteaptaSemnal() {
        try {
            Thread.sleep(Long.MAX_VALUE);   // doarme "la nesfârșit"...
        } catch (InterruptedException e) {
            // ...până când celălalt fir apelează interrupt() -> poate continua
        }
    }
}