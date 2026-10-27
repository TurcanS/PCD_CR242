import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;



public class Adrian extends Thread {

    private static final List<Thread> fireCreate = new ArrayList<>();
    private static final List<ThreadGroup> grupeCreate = new ArrayList<>();
    private static int numarFire;

    public Adrian(ThreadGroup g, String name) {
        super(g, name);
    }


    @Override
    public void run() {
        System.out.println("Adrian: Thread: " + getName()
                + ", Grupa: " + getThreadGroup().getName()
                + ", Prioritatea: " + getPriority());
        pauza();
    }


    static void pauza() {
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }


    static Thread fir(boolean avansat, ThreadGroup g, String nume, int prioritate) {
        Thread t = avansat ? new Adrian(g, nume) : new Thread(g, Adrian::pauza, nume);
        t.setPriority(prioritate);
        fireCreate.add(t);
        return t;
    }

    static ThreadGroup grupa(ThreadGroup parinte, String nume) {
        ThreadGroup g = new ThreadGroup(parinte, nume);
        grupeCreate.add(g);
        return g;
    }

    static void construieste(boolean avansat, ThreadGroup main) {
        fireCreate.clear();
        grupeCreate.clear();

        ThreadGroup g1 = grupa(main, "G1");
        ThreadGroup g3 = grupa(g1, "G3");
        ThreadGroup g2 = grupa(main, "G2");

        // G3{Tha(3), Thb(3), Thc(3), Thd(3)}
        for (String nume : new String[]{"Tha", "Thb", "Thc", "Thd"}) {
            fir(avansat, g3, nume, 3);
        }
        // G2{Th1(4), Th2(5), Th3(5)}
        fir(avansat, g2, "Th1", 4);
        fir(avansat, g2, "Th2", 5);
        fir(avansat, g2, "Th3", 5);
        // main{Th1(7), Th2(7), ThA(3)}
        fir(avansat, main, "Th1", 7);
        fir(avansat, main, "Th2", 7);
        fir(avansat, main, "ThA", 3);

        for (Thread t : fireCreate) {
            t.start();
        }
    }


    static void afiseaza(ThreadGroup g, String indent) {
        System.out.println(indent + "Grupa " + g.getName() + " (prioritate maxima " + g.getMaxPriority() + ")");

        Thread[] fire = new Thread[g.activeCount() + 10];
        int n = g.enumerate(fire, false);
        for (int i = 0; i < n; i++) {
            if (!fireCreate.contains(fire[i])) {
                continue;
            }
            numarFire++;
            System.out.println(indent + "    Fir: " + fire[i].getName()
                    + " | Grupa: " + g.getName()
                    + " | Prioritate: " + fire[i].getPriority());
        }

        ThreadGroup[] subgrupe = new ThreadGroup[g.activeGroupCount() + 5];
        int m = g.enumerate(subgrupe, false);
        for (int i = 0; i < m; i++) {
            if (grupeCreate.contains(subgrupe[i])) {
                afiseaza(subgrupe[i], indent + "    ");
            }
        }
    }

    static void ruleaza(boolean avansat, ThreadGroup main) throws InterruptedException {
        System.out.println(avansat
                ? "===== NIVEL AVANSAT (run() suprascris) ====="
                : "===== NIVEL MEDIU (run() nesuprascris) =====");
        construieste(avansat, main);
        Thread.sleep(300);

        numarFire = 0;
        System.out.println("--- Enumerarea firelor ---");
        afiseaza(main, "");
        System.out.println("Total fire: " + numarFire + " (trebuie 10)");
        System.out.println();

        for (Thread t : fireCreate) {
            t.join();
        }
    }


    public static void main(String[] args) throws Exception {
        JTextArea area = new JTextArea(35, 80);
        area.setEditable(false);
        SwingUtilities.invokeAndWait(() -> {
            JFrame frame = new JFrame("Laborator 2, varianta 1 - Spinu Adrian");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new JScrollPane(area), BorderLayout.CENTER);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });


        System.setOut(new PrintStream(new OutputStream() {
            private final ByteArrayOutputStream linie = new ByteArrayOutputStream();

            @Override
            public synchronized void write(int b) {
                linie.write(b);
                if (b == '\n') {
                    String text = new String(linie.toByteArray(), StandardCharsets.UTF_8);
                    linie.reset();
                    SwingUtilities.invokeLater(() -> {
                        area.append(text);
                        area.setCaretPosition(area.getDocument().getLength());
                    });
                }
            }
        }, true, StandardCharsets.UTF_8));

        ThreadGroup main = Thread.currentThread().getThreadGroup();   // grupa "main"
        ruleaza(false, main);
        ruleaza(true, main);
    }
}