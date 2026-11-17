/**
 * PARTEA LUI CATALIN - firele Th3 și Th4
 *
 * Th3 - Parcurgerea de la început a intervalului [120, 690].
 *       La final afișează DISCIPLINA.
 * Th4 - Parcurgerea de la sfârșit a intervalului [1000, 1567].
 *       La final afișează GRUPA.
 *
 * Metode de sincronizare folosite: sleep() și interrupt()
 *   - Th4, după sarcină, "doarme" cu sleep() până când Th3 îl trezește
 *     cu interrupt().
 *   - Th3 verifică periodic (cu sleep()) când s-a terminat Th2,
 *     apoi îl trezește pe Th4 cu interrupt() ca să afișeze grupa.
 *     După aceea verifică periodic (cu sleep()) când s-a terminat Th1
 *     și abia apoi afișează disciplina.
 *
 * Firele sunt pornite din Main.
 */
public class Catalin {

    static final int TH3_DE = 120;
    static final int TH3_PANA = 690;

    static final int TH4_DE = 1567;
    static final int TH4_PANA = 1000;

    /** Pauza dintre două verificări, în ms. */
    static final int INTERVAL_VERIFICARE = 20;

    // ==========================================================================
    //  Firul 3
    // ==========================================================================
    static class Th3 extends Thread {
        Th3() { super("Th3"); }

        @Override
        public void run() {
            Main.afiseazaFir(getName(), "Pornește");
            parcurgeIntervalDeLaInceput();
            Main.th3SarcinaGata = true;
            Main.afiseazaFir(getName(), "A terminat sarcina. Aștept terminarea lui Th2 (sleep)...");

            // SINCRONIZARE - sleep():
            // Th3 doarme câte puțin până când Th2 termină de afișat numele
            dormiCatTimpTraieste(Main.th2);

            // Th4 trebuie să fi ajuns la sleep(); altfel interrupt() ar fi
            // „consumat" de pauzele din sarcina lui.
            while (!Main.th4SarcinaGata) {
                dormi(INTERVAL_VERIFICARE);
            }

            // SINCRONIZARE - interrupt():
            // Th3 îl trezește pe Th4 ca să afișeze grupa
            Main.afiseazaFir(getName(), "Th2 s-a terminat, îl trezesc pe Th4 (interrupt).");
            Main.th4.interrupt();

            // SINCRONIZARE - sleep():
            // Th3 doarme câte puțin până când Th1 termină de afișat prenumele
            Main.afiseazaFir(getName(), "Aștept terminarea lui Th1 (sleep)...");
            dormiCatTimpTraieste(Main.th1);

            Main.afiseazaLiteraCuLitera(getName() + " - Disciplina: ", Main.DISCIPLINA);

            // Th3 este ultimul fir care se termină
            Main.toateFireleAuTerminat();
        }

        /** Doarme câte INTERVAL_VERIFICARE ms cât timp firul dat încă rulează. */
        void dormiCatTimpTraieste(Thread fir) {
            while (fir.isAlive()) {
                dormi(INTERVAL_VERIFICARE);
            }
        }

        void dormi(int ms) {
            try {
                Thread.sleep(ms);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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
            Main.th4SarcinaGata = true;
            Main.afiseazaFir(getName(), "A terminat sarcina. Dorm (sleep) până mă trezește Th3 (interrupt)...");

            // SINCRONIZARE - sleep():
            // Th4 doarme "la nesfârșit" până când Th3 îl trezește cu interrupt()
            try {
                Thread.sleep(Long.MAX_VALUE);
            } catch (InterruptedException e) {
                // Th3 a apelat interrupt() -> Th4 se trezește și continuă
            }
            Main.afiseazaFir(getName(), "Am fost trezit de Th3 (interrupt), afișez grupa.");

            Main.afiseazaLiteraCuLitera(getName() + " - Grupa: ", Main.GRUPA);
            // Th4 se termină -> Th1 (Anatol), care face join() pe Th4, poate continua
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
