/**
 * PARTEA LUI CATALIN - firele Th3 și Th4
 *
 * Th3 - Parcurgerea de la început a intervalului [120, 690].
 *       La final afișează DISCIPLINA.
 * Th4 - Parcurgerea de la sfârșit a intervalului [1000, 1567].
 *       La final afișează GRUPA.
 *
 * Metode de sincronizare folosite:
 *   - Th3 așteaptă cu join() terminarea completă a lui Th1,
 *     abia apoi afișează disciplina.
 *   - Th4 îl trezește pe Th2 cu interrupt() după ce își termină sarcina,
 *     apoi "doarme" cu sleep() până când Th2 termină de afișat numele
 *     și îl trezește cu interrupt().
 *
 * Firele sunt pornite din Main.
 */
public class Catalin {

    static final int TH3_DE = 120;
    static final int TH3_PANA = 690;

    static final int TH4_DE = 1567;
    static final int TH4_PANA = 1000;

    // ==========================================================================
    //  Firul 3
    // ==========================================================================
    static class Th3 extends Thread {
        Th3() { super("Th3"); }

        @Override
        public void run() {
            Main.afiseazaFir(getName(), "Pornește");
            parcurgeIntervalDeLaInceput();
            Main.afiseazaFir(getName(), "A terminat sarcina. Aștept terminarea lui Th1 (join)...");

            // SINCRONIZARE - join():
            // Th3 așteaptă până când Th1 se termină complet
            try {
                Main.th1.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Main.afiseazaLiteraCuLitera(getName() + " - Disciplina: ", Main.DISCIPLINA);

            // Th3 este ultimul fir care se termină
            Main.toateFireleAuTerminat();
        }

        /** Parcurge intervalul [120, 690] crescător, câte 10 numere pe rând. */
        void parcurgeIntervalDeLaInceput() {
            int numarate = 0;
            StringBuilder rand = new StringBuilder();
            for (int x = TH3_DE; x <= TH3_PANA; x++) {
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
            // Th4 doarme "la nesfârșit" până când Th2 termină de afișat numele
            // și îl trezește cu interrupt()
            try {
                Thread.sleep(Long.MAX_VALUE);
            } catch (InterruptedException e) {
                // Th2 a apelat interrupt() -> Th4 se trezește și continuă
            }

            Main.afiseazaLiteraCuLitera(getName() + " - Grupa: ", Main.GRUPA);

            // Th4 marchează că a terminat -> Th1 poate afișa prenumele
            Main.th4Terminat = true;
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
}
