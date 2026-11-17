/**
 * PARTEA LUI ANATOL - firele Th1 și Th3
 *
 * Th1 - Suma pozițiilor numerelor pare două câte două,
 *       căutarea și sumarea începând de la primul element.
 *       La final afișează PRENUMELE.
 * Th3 - Parcurgerea de la început a intervalului [120, 690].
 *       La final afișează DISCIPLINA.
 *
 * Metode de sincronizare folosite: isAlive() și join()
 *   - Th1 verifică periodic cu isAlive() dacă Th4 s-a terminat,
 *     abia apoi afișează prenumele.
 *   - Th3 așteaptă cu join() terminarea completă a lui Th1,
 *     abia apoi afișează disciplina.
 */
public class Anatol {

    /** Pornește DOAR partea lui Anatol (Th1 și Th3). */
    public static void main(String[] args) {
        Main.mod = Main.DOAR_ANATOL;
        javax.swing.SwingUtilities.invokeLater(() -> Main.creeazaFereastra());
    }

    static final int TH3_DE = 120;
    static final int TH3_PANA = 690;

    // ==========================================================================
    //  Firul 1
    // ==========================================================================
    static class Th1 extends Thread {
        Th1() { super("Th1"); }

        @Override
        public void run() {
            Main.afiseaza(Main.zonaTh1, "Pornește " + getName() + "\n");
            sumaPozitiilorPareDeLaInceput();
            Main.afiseaza(Main.zonaTh1, getName() + " a terminat sarcina.\n");

            // SINCRONIZARE - isAlive():
            // cât timp Th4 încă lucrează, Th1 așteaptă
            while (Main.th4.isAlive()) {
                Main.pauza(50);
            }
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
                        Main.afiseaza(Main.zonaTh1, "Suma " + nrSuma + ":  poz " + pozInAsteptare
                                + " (" + a[pozInAsteptare] + ")  +  poz " + i
                                + " (" + a[i] + ")  =  " + suma + "\n");
                        pozInAsteptare = -1;
                    }
                }
            }

            if (pozInAsteptare != -1) {
                Main.afiseaza(Main.zonaTh1, "Poziția " + pozInAsteptare + " ("
                        + a[pozInAsteptare] + ") a rămas fără pereche.\n");
            }
        }
    }

    // ==========================================================================
    //  Firul 3
    // ==========================================================================
    static class Th3 extends Thread {
        Th3() { super("Th3"); }

        @Override
        public void run() {
            Main.afiseaza(Main.zonaTh3, "Pornește " + getName() + "\n");
            parcurgeIntervalDeLaInceput();
            Main.afiseaza(Main.zonaTh3, getName() + " a terminat sarcina.\n");

            // SINCRONIZARE - join():
            // Th3 așteaptă până când Th1 se termină complet
            try {
                Main.th1.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Main.afiseazaLiteraCuLitera(getName() + " - Disciplina: ", Main.DISCIPLINA);

            Main.toateFireleAuTerminat();
        }

        /** Parcurge intervalul [120, 690] crescător, câte 10 numere pe rând. */
        void parcurgeIntervalDeLaInceput() {
            int numarate = 0;
            for (int x = TH3_DE; x <= TH3_PANA; x++) {
                Main.afiseaza(Main.zonaTh3, x + " ");
                numarate++;
                if (numarate % 10 == 0) {
                    Main.afiseaza(Main.zonaTh3, "\n");
                }
                Main.pauza(5);   // ca să se vadă că firele lucrează în paralel
            }
            Main.afiseaza(Main.zonaTh3, "\nTotal numere parcurse: " + numarate + "\n");
        }
    }
}