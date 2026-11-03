import java.util.ArrayList;
import java.util.List;

/**
 * Lucrare de laborator nr. 2 - Gruparea firelor de executie. Modificarea prioritatii.
 * Varianta 2. Metoda run() din clasa Thread NU este suprascrisa; firele sunt daemon.
 *
 * Criterii acoperite (numerotarea din fisa lucrarii):
 *   [CRITERIUL 1] Schema conform variantei  -> formula de mai jos + metoda construiesteStructura()
 *   [CRITERIUL 2] Crearea si initializarea grupelor si a thread-urilor, fara suprascrierea run()
 *                 -> construiesteStructura() (se foloseste clasa Thread direct, cu un Runnable)
 *   [CRITERIUL 5] Corectitudinea codului    -> metoda verifica() compara rezultatul cu ce se asteapta
 *   [CRITERIUL 6] Respectarea cerintelor    -> exact 10 fire, prioritatile din tabel, enumerare Main+subgrupe
 *   [CRITERIUL 8] Respectarea termenului    -> nu tine de cod; lucrarea se preda/sustine la timp
 *   [CRITERIUL 9] Evaluarea cunostintelor   -> comentariile "EXPLICATIE" din cod descriu logica
 *
 * [CRITERIUL 1] VARIANTA 2 (Tabelul 1):
 *   Main{ G2{ G4{ G1{Tha(1), Thb(3), Thc(8), Thd(3)} }, ThA(1) },
 *         G3{ Th1(4), Th2(3), Th3(5) },
 *         Th1(3), Th2(6) }
 *
 *   Arborele:
 *   Main
 *   |-- G2
 *   |   |-- G4
 *   |   |   `-- G1: Tha(1), Thb(3), Thc(8), Thd(3)
 *   |   `-- ThA(1)
 *   |-- G3: Th1(4), Th2(3), Th3(5)
 *   |-- Th1(3)
 *   `-- Th2(6)
 */
public class Lab2Anatol {

    /**
     * [CRITERIUL 2] Creeaza un fir DAEMON fara sa suprascrie run().
     *
     * EXPLICATIE:
     *  - new Thread(grup, target, nume) plaseaza firul direct in grupul indicat
     *    (apartenenta e permanenta, firul nu se poate muta in alt grup dupa creare).
     *  - Nu suprascriem run(): metoda run() a clasei Thread apeleaza run() al Runnable-ului primit
     *    ca target. Daca nu am da niciun target, firul s-ar termina imediat dupa start()
     *    si enumerate() nu l-ar mai gasi. De aceea targetul doar doarme 5 secunde.
     *  - setPriority(p) stabileste prioritatea din paranteze (1..10; implicit 5 = NORM_PRIORITY).
     *  - setDaemon(true): firul daemon nu tine JVM-ul in viata. Cand se termina main(),
     *    programul se inchide, chiar daca firele daemon inca "dorm". Trebuie apelat inainte de start().
     */
    static Thread creeazaFir(ThreadGroup grup, String nume, int prioritate) {
        Runnable target = () -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };
        Thread t = new Thread(grup, target, nume);
        t.setPriority(prioritate);
        t.setDaemon(true);
        return t;
    }

    /**
     * [CRITERIUL 1] + [CRITERIUL 2]: construieste structura din varianta 2.
     *
     * EXPLICATIE: grupurile se creeaza cu new ThreadGroup(parinte, nume); astfel se formeaza
     * ierarhia Main -> G2 -> G4 -> G1 si Main -> G3. Firele se adauga in grupul potrivit,
     * cu prioritatea din tabel.
     *
     * @return grupul radacina "Main"
     */
    static ThreadGroup construiesteStructura(List<Thread> fire) {
        // Main e creat sub grupul curent, ca sa nu se amestece cu grupul "main" al JVM-ului
        ThreadGroup main = new ThreadGroup(Thread.currentThread().getThreadGroup(), "Main");
        ThreadGroup g2 = new ThreadGroup(main, "G2");
        ThreadGroup g4 = new ThreadGroup(g2, "G4");   // G4 este in G2
        ThreadGroup g1 = new ThreadGroup(g4, "G1");   // G1 este in G4
        ThreadGroup g3 = new ThreadGroup(main, "G3");

        // G1{Tha(1), Thb(3), Thc(8), Thd(3)}
        fire.add(creeazaFir(g1, "Tha", 1));
        fire.add(creeazaFir(g1, "Thb", 3));
        fire.add(creeazaFir(g1, "Thc", 8));
        fire.add(creeazaFir(g1, "Thd", 3));

        // G2{ G4{...}, ThA(1) }
        fire.add(creeazaFir(g2, "ThA", 1));

        // G3{Th1(4), Th2(3), Th3(5)}
        fire.add(creeazaFir(g3, "Th1", 4));
        fire.add(creeazaFir(g3, "Th2", 3));
        fire.add(creeazaFir(g3, "Th3", 5));

        // Direct in Main: Th1(3), Th2(6)
        fire.add(creeazaFir(main, "Th1", 3));
        fire.add(creeazaFir(main, "Th2", 6));

        return main;
    }

    /**
     * [CRITERIUL 6] Enumerarea tuturor firelor din grupa principala si din subgrupe.
     *
     * EXPLICATIE: pentru fiecare grup:
     *   1) enumerate(Thread[], false) da firele care apartin DIRECT grupului (fara subgrupe);
     *   2) enumerate(ThreadGroup[], false) da subgrupele directe;
     *   3) pentru fiecare subgrupa apelam din nou metoda (recursivitate), deci se parcurge tot arborele.
     * Se afiseaza numele firului, grupul si prioritatea (getPriority()).
     * Atentie: enumerate() vede doar firele pornite si inca active.
     */
    static void afiseazaArbore(ThreadGroup g, int nivel) {
        String indent = "    ".repeat(nivel);
        System.out.println(indent + "[Grup] " + g.getName());

        Thread[] fireGrup = new Thread[g.activeCount() + 10];
        int nrFire = g.enumerate(fireGrup, false);
        for (int i = 0; i < nrFire; i++) {
            Thread t = fireGrup[i];
            System.out.println(indent + "    - " + t.getName()
                    + " | grup: " + t.getThreadGroup().getName()
                    + " | prioritate: " + t.getPriority()
                    + " | daemon: " + t.isDaemon());
        }

        ThreadGroup[] subgrupe = new ThreadGroup[g.activeGroupCount() + 10];
        int nrSub = g.enumerate(subgrupe, false);
        for (int i = 0; i < nrSub; i++) {
            afiseazaArbore(subgrupe[i], nivel + 1);   // coboram in subgrupa
        }
    }

    /**
     * [CRITERIUL 5] + [CRITERIUL 6]: verifica ca structura obtinuta respecta varianta.
     *
     * EXPLICATIE: comparam numarul total de fire si prioritatea fiecarui fir cu valorile
     * din tabel. Daca ceva nu se potriveste, programul spune explicit unde.
     */
    static boolean verifica(ThreadGroup main, List<Thread> fire) {
        boolean ok = true;

        // numar total de fire cerut de varianta 2: 4 + 1 + 3 + 2 = 10
        int total = main.activeCount();
        if (total != 10) {
            System.out.println("EROARE: se asteptau 10 fire, sunt " + total);
            ok = false;
        }

        // prioritatile cerute, in aceeasi ordine in care au fost create firele
        int[] prioritatiAsteptate = {1, 3, 8, 3, 1, 4, 3, 5, 3, 6};
        for (int i = 0; i < fire.size(); i++) {
            if (fire.get(i).getPriority() != prioritatiAsteptate[i]) {
                System.out.println("EROARE: prioritate gresita la " + fire.get(i).getName());
                ok = false;
            }
        }
        return ok;
    }

    public static void main(String[] args) {
        System.out.println("=== Lab 2 PCD - Varianta 2 (fara run(), fire daemon) ===");

        List<Thread> fire = new ArrayList<>();
        ThreadGroup main = construiesteStructura(fire);

        // firele trebuie pornite ca sa fie vazute de enumerate()
        for (Thread t : fire) {
            t.start();
        }

        System.out.println("\n -- Structura enumerata din grupa Main --");
        afiseazaArbore(main, 0);
        System.out.println("Total fire active: " + main.activeCount());

        System.out.println("\n-- Verificare --");
        System.out.println(verifica(main, fire) ? "OK: structura corespunde variantei 2"
                                                 : "Structura NU corespunde variantei 2");

        // EXPLICATIE: main() se termina aici. Firele sunt daemon, deci JVM-ul se inchide
        // imediat, fara sa astepte cele 5 secunde de sleep ale firelor.
    }
}