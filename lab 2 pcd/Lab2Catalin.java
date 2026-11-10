import java.util.ArrayList;
import java.util.List;

/**
 * Lucrare de laborator nr. 2 - Gruparea firelor de executie. Modificarea prioritatii.
 * Varianta 2. Metoda run() din clasa Thread NU este suprascrisa; firele sunt daemon.
 * Structura este afisata cu metoda list() a clasei ThreadGroup.
 */
public class Lab2Catalin {

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
            t.start();   // doar firele pornite apar in list()
        }
        // Thread.currentThread() obține firul de execuție curent (main). .getThreadGroup() obține grupul din care face parte firul curent.
        System.out.println("\n-- Structura afisata cu list() --");
        Thread.currentThread().getThreadGroup().list();
        System.out.println("Total fire active: " + main.activeCount());

        System.out.println("\n-- Verificare --");
        System.out.println(verifica(main, fire) ? "OK: structura corespunde variantei 2"
                                                : "Structura NU corespunde variantei 2");
    }
}