import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Varianta6 {

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

    // Fir daemon, fără suprascrierea run(): corpul e dat ca Runnable
    static Thread creeaza(ThreadGroup g, String nume, int prioritate) {
        Thread t = new Thread(g, () -> pauza(1500), nume);
        t.setPriority(prioritate);
        t.setDaemon(true); // trebuie apelat înainte de start()
        return t;
    }

    // Afișează recursiv grupurile, subgrupurile și firele active
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

    public static void main(String[] args) {
        ThreadGroup main = Thread.currentThread().getThreadGroup();
        ThreadGroup g1 = new ThreadGroup(main, "G1");
        ThreadGroup g3 = new ThreadGroup(g1, "G3");
        ThreadGroup g2 = new ThreadGroup(g1, "G2");

        List<Thread> toate = new ArrayList<>();
        toate.add(creeaza(g1, "ThA", 3));

        toate.add(creeaza(g3, "Thf", 3));
        toate.add(creeaza(g3, "Thb", 7));
        toate.add(creeaza(g3, "Thc", 3));
        toate.add(creeaza(g3, "Thd", 3));

        toate.add(creeaza(g2, "Th8", 3));
        toate.add(creeaza(g2, "Th9", 4));
        toate.add(creeaza(g2, "Th3", 3));

        toate.add(creeaza(main, "Th1", 3));
        toate.add(creeaza(main, "Th2", 3));

        afiseaza("=== Varianta 6, nivel mediu ===");
        for (Thread t : toate) t.start();

        pauza(300); // firele trebuie pornite ca să apară în enumerare
        afiseazaArbore(main, "");

        // Firele sunt daemon, deci main trebuie să aștepte explicit
        for (Thread t : toate) {
            try {
                t.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        afiseaza("Toate firele au terminat.");

        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("Varianta 6 - grupuri de fire");
            JTextArea area = new JTextArea(rezultat.toString(), 30, 75);
            area.setEditable(false);
            area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
            f.add(new JScrollPane(area));
            f.pack();
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setVisible(true);
        });
    }
}
