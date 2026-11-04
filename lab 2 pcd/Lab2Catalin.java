import java.util.ArrayList;
import java.util.List;

/**
 * Lucrare de laborator nr. 2 - PCD (Varianta 2)
 * Implementare alternativa: Structura compacta cu lambda-helpers si fire daemon.
 */
public class Lab2Catalin {

    // Target comun pentru firele daemon (fara suprascrierea run())
    private static final Runnable SLEEP_TARGET = () -> {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    };

    public static void main(String[] args) {
        System.out.println("=== Lab 2 PCD - Varianta 2 (Alternativa) ===");

        List<Thread> fire = new ArrayList<>();

        // 1. Inregistrare ierarhie ThreadGroup conform Variantei 2
        ThreadGroup rootMain = new ThreadGroup(Thread.currentThread().getThreadGroup(), "Main");
        
        ThreadGroup g2 = new ThreadGroup(rootMain, "G2");
        ThreadGroup g4 = new ThreadGroup(g2, "G4");
        ThreadGroup g1 = new ThreadGroup(g4, "G1");
        
        ThreadGroup g3 = new ThreadGroup(rootMain, "G3");

        // 2. Initializare si adaugare fire daemon in grupurile respective
        // G1: Tha(1), Thb(3), Thc(8), Thd(3)
        fire.add(creeazaDaemon(g1, "Tha", 1));
        fire.add(creeazaDaemon(g1, "Thb", 3));
        fire.add(creeazaDaemon(g1, "Thc", 8));
        fire.add(creeazaDaemon(g1, "Thd", 3));

        // G2: ThA(1)
        fire.add(creeazaDaemon(g2, "ThA", 1));

        // G3: Th1(4), Th2(3), Th3(5)
        fire.add(creeazaDaemon(g3, "Th1", 4));
        fire.add(creeazaDaemon(g3, "Th2", 3));
        fire.add(creeazaDaemon(g3, "Th3", 5));

        // Direct in Main: Th1(3), Th2(6)
        fire.add(creeazaDaemon(rootMain, "Th1", 3));
        fire.add(creeazaDaemon(rootMain, "Th2", 6));

        // 3. Pornirea firelor (necesara pentru a fi detectate de enumerate())
        fire.forEach(Thread::start);

        // 4. Afisarea arborelui de grupuri si fire
        System.out.println("\n-- Structura ierarhica detaliata --");
        parcurgeArbore(rootMain, 0);

        System.out.println("\nNumar total fire active in Main: " + rootMain.activeCount());

        // 5. Verificare conformitate
        System.out.println("\n-- Statut Verificare --");
        boolean esteCorect = valideazaStructura(rootMain, fire);
        System.out.println(esteCorect ? "REZULTAT: Corect (Corespunde Variantei 2)" 
                                     : "REZULTAT: Incorect (Nu corespunde Variantei 2)");
    }

    /**
     * Creeaza un fir de executie Daemon fara suprascrierea run()
     */
    private static Thread creeazaDaemon(ThreadGroup grup, String nume, int prioritate) {
        Thread t = new Thread(grup, SLEEP_TARGET, nume);
        t.setPriority(prioritate);
        t.setDaemon(true);
        return t;
    }

    /**
     * Parcurge recursiv grupurile si afiseaza informatiile despre fire
     */
    private static void parcurgeArbore(ThreadGroup grup, int adancime) {
        String prefix = "    ".repeat(adancime);
        System.out.println(prefix + " -> Group: " + grup.getName());

        // Citire fire din grupul curent (fara subgrupe)
        Thread[] fireLocale = new Thread[grup.activeCount() + 5];
        int countFire = grup.enumerate(fireLocale, false);

        for (int i = 0; i < countFire; i++) {
            Thread t = fireLocale[i];
            System.out.printf("%s    [Thread] %s | Priority: %d | Daemon: %b%n",
                    prefix, t.getName(), t.getPriority(), t.isDaemon());
        }

        // Citire si parcurgere subgrupe
        ThreadGroup[] subgrupe = new ThreadGroup[grup.activeGroupCount() + 5];
        int countSub = grup.enumerate(subgrupe, false);

        for (int i = 0; i < countSub; i++) {
            parcurgeArbore(subgrupe[i], adancime + 1);
        }
    }

    /**
     * Valideaza numarul de fire si prioritatile cerut
     */
    private static boolean valideazaStructura(ThreadGroup mainGroup, List<Thread> fire) {
        if (mainGroup.activeCount() != 10) {
            System.err.println("Eroare: Numarul de fire active este " + mainGroup.activeCount() + " (se asteptau 10).");
            return false;
        }

        int[] prioritatiTinta = {1, 3, 8, 3, 1, 4, 3, 5, 3, 6};
        for (int i = 0; i < fire.size(); i++) {
            if (fire.get(i).getPriority() != prioritatiTinta[i]) {
                System.err.println("Eroare prioritate la firul: " + fire.get(i).getName());
                return false;
            }
        }

        return true;
    }
}