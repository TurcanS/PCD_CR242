import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import javax.swing.*;

/**
 * Lucrare de laborator nr. 2 - Gruparea firelor de executie. Prioritatea.
 * Varianta 7:
 * Main{ ThA(3), G2{Th1(5), Th2(3), Th33(7)}, Th11(3), Th22(3),
 *       G1{ G3{Thaa(2), Thbb(3), Thcc(8), Thdd(3)} } }
 *
 * Toate firele sunt create ca fire DEMON, FARA a suprascrie run().
 * Informatia (nume, grup, prioritate) este extrasa la consola prin enumerate().
 */
public class Vlad2 {

    // Grupul "main" (radacina structurii) - luat din firul main la pornire
    private static ThreadGroup ROOT;
    private static JTextArea output;

    // Firele create de noi (pentru a nu afisa si firul "main" al JVM-ului)
    private static final Set<Thread> created = new HashSet<>();
    // Grupurile create in rularea curenta (grupurile vechi raman in JVM si ar aparea dublate)
    private static final Set<ThreadGroup> createdGroups = new HashSet<>();
    // Tine firele vii cat timp le enumeram (activeCount numara doar firele pornite)
    private static CountDownLatch release;

    /** Creeaza un fir DEMON (fara run propriu) intr-un grup, cu prioritatea data. */
    private static Thread daemonThread(ThreadGroup g, String name, int prio) {
        Thread t = new Thread(g, () -> {
            try { release.await(); } catch (InterruptedException ignored) { }
        }, name);
        t.setDaemon(true);      // trebuie apelat inainte de start()
        t.setPriority(prio);
        created.add(t);
        return t;
    }

    /** Construieste structura din schema (varianta 7) si porneste firele. */
    private static List<Thread> build() {
        List<Thread> all = new ArrayList<>();

        // Grupuri
        ThreadGroup g1 = new ThreadGroup(ROOT, "G1");
        ThreadGroup g3 = new ThreadGroup(g1, "G3");   // G3 in interiorul lui G1
        ThreadGroup g2 = new ThreadGroup(ROOT, "G2");
        createdGroups.add(g1);
        createdGroups.add(g2);
        createdGroups.add(g3);

        // Fire direct in main
        all.add(daemonThread(ROOT, "ThA", 3));
        all.add(daemonThread(ROOT, "Th11", 3));
        all.add(daemonThread(ROOT, "Th22", 3));

        // G2
        all.add(daemonThread(g2, "Th1", 5));
        all.add(daemonThread(g2, "Th2", 3));
        all.add(daemonThread(g2, "Th33", 7));

        // G3 (in G1)
        all.add(daemonThread(g3, "Thaa", 2));
        all.add(daemonThread(g3, "Thbb", 3));
        all.add(daemonThread(g3, "Thcc", 8));
        all.add(daemonThread(g3, "Thdd", 3));

        for (Thread t : all) t.start();
        return all;
    }

    /** Afiseaza recursiv ierarhia: grup -> fire -> subgrupuri. */
    private static void printGroup(ThreadGroup g, String indent, boolean detailed) {
        log(indent + "[Grup] " + g.getName()
                + (detailed ? " (maxPriority=" + g.getMaxPriority() + ")" : ""));

        Thread[] threads = new Thread[g.activeCount() + 10];
        int n = g.enumerate(threads, false);          // doar firele directe ale grupului
        for (int i = 0; i < n; i++) {
            Thread t = threads[i];
            if (!created.contains(t)) continue;
            log(indent + "    Fir: " + t.getName()
                    + " | grup: " + t.getThreadGroup().getName()
                    + " | prioritate: " + t.getPriority()
                    + (detailed ? " | demon: " + t.isDaemon() : ""));
        }

        ThreadGroup[] subs = new ThreadGroup[g.activeGroupCount() + 10];
        int m = g.enumerate(subs, false);             // doar subgrupurile directe
        for (int i = 0; i < m; i++)
            if (createdGroups.contains(subs[i])) printGroup(subs[i], indent + "    ", detailed);
    }

    /**
     * advanced = false: nume, grup, prioritate.
     * advanced = true : in plus maxPriority al grupului si statutul de demon.
     */
    private static void run(boolean advanced) {
        output.setText("");
        created.clear();
        createdGroups.clear();
        release = new CountDownLatch(1);

        log(advanced ? "=== NIVEL AVANSAT (fire demon, fara run) ===\n"
                     : "=== NIVEL MEDIU (fire demon, fara run) ===\n");

        List<Thread> all = build();

        try { Thread.sleep(300); } catch (InterruptedException ignored) { }

        log("--- Ierarhia grupurilor ---");
        printGroup(ROOT, "", advanced);
        log("\nTotal fire create: " + all.size());

        release.countDown();   // elibereaza firele, ele se termina
    }

    private static void log(String s) {
        System.out.println(s);
        if (output != null) SwingUtilities.invokeLater(() -> output.append(s + "\n"));
    }

    public static void main(String[] args) {
        ROOT = Thread.currentThread().getThreadGroup();   // grupul "main"

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Lab 2 - ThreadGroup si prioritati (Varianta 7)");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            output = new JTextArea();
            output.setEditable(false);
            output.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));

            JButton medium = new JButton("Nivel mediu");
            JButton advanced = new JButton("Nivel avansat");
            JButton clear = new JButton("Curata");

            medium.addActionListener(e -> new Thread(() -> run(false)).start());
            advanced.addActionListener(e -> new Thread(() -> run(true)).start());
            clear.addActionListener(e -> output.setText(""));

            JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
            top.add(medium);
            top.add(advanced);
            top.add(clear);

            frame.add(top, BorderLayout.NORTH);
            frame.add(new JScrollPane(output), BorderLayout.CENTER);
            frame.setSize(720, 520);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}