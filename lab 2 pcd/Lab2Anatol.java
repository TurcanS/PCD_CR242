import java.util.ArrayList;
import java.util.List;

/**
 * Lucrare de laborator nr. 2 - Gruparea firelor de executie. Modificarea prioritatii.
 * Varianta 2. Metoda run() din clasa Thread NU este suprascrisa; firele sunt daemon.
 *
 * VARIANTA 2 (Tabelul 1):
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

    // Creeaza un fir daemon fara sa suprascrie run(); firul doar doarme 5 secunde
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
        t.setDaemon(true);   // trebuie apelat inainte de start()
        return t;
    }

    // Construieste grupurile si firele din varianta 2
    static ThreadGroup construiesteStructura(List<Thread> fire) {
        ThreadGroup main = new ThreadGroup(Thread.currentThread().getThreadGroup(), "Main");
        ThreadGroup g2 = new ThreadGroup(main, "G2");
        ThreadGroup g4 = new ThreadGroup(g2, "G4");
        ThreadGroup g1 = new ThreadGroup(g4, "G1");
        ThreadGroup g3 = new ThreadGroup(main, "G3");

        fire.add(creeazaFir(g1, "Tha", 1));
        fire.add(creeazaFir(g1, "Thb", 3));
        fire.add(creeazaFir(g1, "Thc", 8));
        fire.add(creeazaFir(g1, "Thd", 3));

        fire.add(creeazaFir(g2, "ThA", 1));

        fire.add(creeazaFir(g3, "Th1", 4));
        fire.add(creeazaFir(g3, "Th2", 3));
        fire.add(creeazaFir(g3, "Th3", 5));

        fire.add(creeazaFir(main, "Th1", 3));
        fire.add(creeazaFir(main, "Th2", 6));

        return main;
    }

    // Enumerare recursiva: firele grupului, apoi subgrupele
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
            afiseazaArbore(subgrupe[i], nivel + 1);
        }
    }

    // Verifica numarul de fire si prioritatile
    static boolean verifica(ThreadGroup main, List<Thread> fire) {
        boolean ok = true;

        int total = main.activeCount();
        if (total != 10) {
            System.out.println("EROARE: se asteptau 10 fire, sunt " + total);
            ok = false;
        }

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

        for (Thread t : fire) {
            t.start();   // doar firele pornite sunt vazute de enumerate()
        }

        System.out.println("\n -- Structura enumerata din grupa Main --");
        afiseazaArbore(main, 0);
        System.out.println("Total fire active: " + main.activeCount());

        System.out.println("\n-- Verificare --");
        System.out.println(verifica(main, fire) ? "OK: structura corespunde variantei 2"
                                                 : "Structura NU corespunde variantei 2");
    }
}