import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Varianta6_Prioritate_scazuta {

    static final StringBuilder rezultat = new StringBuilder();

    static synchronized void afiseaza(String s) {
        System.out.println(s);
        rezultat.append(s).append("\n");
    }

    static void pauza(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // ===== NIVEL MEDIU: fir daemon, FARA suprascrierea run() =====
    // Corpul firului e dat ca Runnable (lambda), deci run() nu e suprascris.
    static Thread firMediu(ThreadGroup g, String nume, int prioritate) {
        Thread t = new Thread(g, () -> pauza(1500), nume);
        t.setPriority(prioritate);
        t.setDaemon(true); // trebuie apelat inainte de start()
        return t;
    }

    // ===== NIVEL AVANSAT: fir daemon care SUPRASCRIE run() =====
    static class FirAvansat extends Thread {
        FirAvansat(ThreadGroup g, String nume, int prioritate) {
            super(g, nume);
            setPriority(prioritate);
            setDaemon(true);
        }

        @Override
        public void run() {
            afiseaza("  [run] " + getName() + " | grupa: " + getThreadGroup().getName()
                    + " | prioritate: " + getPriority() + " | daemon: " + isDaemon());
            pauza(1500);
        }
    }

    static Thread creeaza(boolean avansat, ThreadGroup g, String nume, int prioritate) {
        return avansat ? new FirAvansat(g, nume, prioritate) : firMediu(g, nume, prioritate);
    }

    // Afiseaza recursiv grupurile, subgrupurile si firele active
    static void afiseazaArbore(ThreadGroup g, String indent) {
        afiseaza(indent + "Grupa " + g.getName() + " (maxPriority=" + g.getMaxPriority() + ")");

        Thread[] fire = new Thread[g.activeCount() + 5];
        int n = g.enumerate(fire, false);
        for (int i = 0; i < n; i++) {
            afiseaza(indent + "    Fir " + fire[i].getName()
                    + " | grupa: " + fire[i].getThreadGroup().getName()
                    + " | prioritate: " + fire[i].getPriority()
                    + " | daemon: " + fire[i].isDaemon());
        }

        ThreadGroup[] subgrupe = new ThreadGroup[g.activeGroupCount() + 5];
        int m = g.enumerate(subgrupe, false);
        for (int j = 0; j < m; j++) {
            afiseazaArbore(subgrupe[j], indent + "    ");
        }
    }

    // Ruleaza structura variantei 6:
    // Main{ G1{ ThA, G3{Thf,Thb,Thc,Thd}, G2{Th8,Th9,Th3} }, Th1, Th2 }
    // Prioritatile din formula sunt scazute cu 1 (partea mea din echipa).
    static void ruleazaNivel(boolean avansat) {
        afiseaza("");
        afiseaza("=== Varianta 6, nivel " + (avansat ? "AVANSAT (run() suprascris)" : "MEDIU (fara run())") + " ===");

        ThreadGroup main = new ThreadGroup("Main");
        ThreadGroup g1 = new ThreadGroup(main, "G1");
        ThreadGroup g3 = new ThreadGroup(g1, "G3");
        ThreadGroup g2 = new ThreadGroup(g1, "G2");

        List<Thread> toate = new ArrayList<>();
        toate.add(creeaza(avansat, g1, "ThA", 2));

        toate.add(creeaza(avansat, g3, "Thf", 2));
        toate.add(creeaza(avansat, g3, "Thb", 6));
        toate.add(creeaza(avansat, g3, "Thc", 2));
        toate.add(creeaza(avansat, g3, "Thd", 2));

        toate.add(creeaza(avansat, g2, "Th8", 2));
        toate.add(creeaza(avansat, g2, "Th9", 3));
        toate.add(creeaza(avansat, g2, "Th3", 2));

        toate.add(creeaza(avansat, main, "Th1", 2));
        toate.add(creeaza(avansat, main, "Th2", 2));

        for (Thread t : toate) t.start();

        pauza(300); // firele trebuie pornite ca sa apara in enumerare
        afiseaza("--- Arborele de grupuri ---");
        afiseazaArbore(main, "");

        // Firele sunt daemon, deci main trebuie sa astepte explicit
        for (Thread t : toate) {
            try {
                t.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        afiseaza("Toate firele au terminat.");
    }

    public static void main(String[] args) {
        ruleazaNivel(false); // nivel mediu
        ruleazaNivel(true);  // nivel avansat

        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("Varianta 6 - grupuri de fire");
            JTextArea area = new JTextArea(rezultat.toString(), 40, 85);
            area.setEditable(false);
            area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
            f.add(new JScrollPane(area));
            f.pack();
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setVisible(true);
        });
    }
}