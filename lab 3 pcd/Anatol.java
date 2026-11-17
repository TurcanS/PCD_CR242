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
 * Metode de sincronizare folosite: yield() și join()
 *   - Th2 cedează procesorul cu yield() cât timp Th3 și Th4 nu și-au
 *     terminat sarcinile, apoi afișează primul mesaj final (numele).
 *   - Th1 așteaptă cu join() terminarea completă a lui Th4
 *     (care afișează grupa), abia apoi afișează prenumele.
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

            Main.afiseazaFir(getName(), "Aștept terminarea lui Th4 (join)...");

            // SINCRONIZARE - join():
            // Th1 așteaptă până când Th4 se termină complet (după afișarea grupei)
            try {
                Main.th4.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Main.afiseazaFir(getName(), "Th4 s-a terminat, afișez prenumele.");
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
            Main.afiseazaFir(getName(), "A terminat sarcina. Aștept ca Th3 și Th4 să-și termine sarcinile (yield)...");

            // SINCRONIZARE - yield():
            // cât timp Th3 și Th4 mai lucrează, Th2 cedează procesorul altor fire
            while (!Main.th3SarcinaGata || !Main.th4SarcinaGata) {
                Thread.yield();
            }
            Main.afiseazaFir(getName(), "Th3 și Th4 au terminat sarcinile, afișez numele.");

            Main.afiseazaLiteraCuLitera(getName() + " - Numele: ", Main.NUME);
            // Th2 se termină -> Th3 (Catalin) observă asta și îl trezește pe Th4
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
