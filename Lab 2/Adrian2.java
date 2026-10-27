import javax.swing.*;
import java.awt.*;

/**
 * Lucrare de laborator nr. 2 - Gruparea firelor de executie. Prioritati.
 * Varianta 7:
 * Main{ ThA(3), G2{Th1(5), Th2(3), Th33(7)}, Th11(3), Th22(3),
 *       G1{ G3{Thaa(2), Thbb(3), Thcc(8), Thdd(3)} } }
 */
public class Adrian2 {

    static JTextArea zona;   // zona de text din fereastra (null daca rulam in consola)

    // Scrie un rand in fereastra sau, daca nu exista fereastra, in consola
    static void scrie(String text) {
        if (zona == null) {
            System.out.println(text);
        } else {
            SwingUtilities.invokeLater(() -> zona.append(text + "\n"));
        }
    }

    // ------------------------------------------------------------------
    // NIVEL MEDIU: fire simple, fara run() suprascris
    // ------------------------------------------------------------------
    static void mediu() {
        scrie("=== NIVEL MEDIU ===");

        // grupurile
        ThreadGroup main = new ThreadGroup("Main");
        ThreadGroup g2 = new ThreadGroup(main, "G2");
        ThreadGroup g1 = new ThreadGroup(main, "G1");
        ThreadGroup g3 = new ThreadGroup(g1, "G3");

        // firele din Main
        Thread thA = new Thread(main, "ThA");
        thA.setPriority(3);
        Thread th11 = new Thread(main, "Th11");
        th11.setPriority(3);
        Thread th22 = new Thread(main, "Th22");
        th22.setPriority(3);

        // firele din G2
        Thread th1 = new Thread(g2, "Th1");
        th1.setPriority(5);
        Thread th2 = new Thread(g2, "Th2");
        th2.setPriority(3);
        Thread th33 = new Thread(g2, "Th33");
        th33.setPriority(7);

        // firele din G3 (G3 este in G1)
        Thread thaa = new Thread(g3, "Thaa");
        thaa.setPriority(2);
        Thread thbb = new Thread(g3, "Thbb");
        thbb.setPriority(3);
        Thread thcc = new Thread(g3, "Thcc");
        thcc.setPriority(8);
        Thread thdd = new Thread(g3, "Thdd");
        thdd.setPriority(3);

        Thread[] fire = {thA, th11, th22, th1, th2, th33, thaa, thbb, thcc, thdd};

        for (Thread t : fire) {
            scrie("Fir: " + t.getName()
                    + " | grup: " + t.getThreadGroup().getName()
                    + " | prioritate: " + t.getPriority());
        }
        scrie("Total fire: " + fire.length);
    }

    // ------------------------------------------------------------------
    // NIVEL AVANSAT: clasa care suprascrie run()
    // ------------------------------------------------------------------
    static class Fir extends Thread {
        Fir(ThreadGroup grup, String nume, int prioritate) {
            super(grup, nume);
            setPriority(prioritate);
        }

        @Override
        public void run() {
            scrie("Firul " + getName()
                    + " | grup: " + getThreadGroup().getName()
                    + " | prioritate: " + getPriority());
        }
    }

    static void avansat() {
        scrie("=== NIVEL AVANSAT ===");

        ThreadGroup main = new ThreadGroup("Main");
        ThreadGroup g2 = new ThreadGroup(main, "G2");
        ThreadGroup g1 = new ThreadGroup(main, "G1");
        ThreadGroup g3 = new ThreadGroup(g1, "G3");

        Fir[] fire = {
                new Fir(main, "ThA", 3),
                new Fir(main, "Th11", 3),
                new Fir(main, "Th22", 3),
                new Fir(g2, "Th1", 5),
                new Fir(g2, "Th2", 3),
                new Fir(g2, "Th33", 7),
                new Fir(g3, "Thaa", 2),
                new Fir(g3, "Thbb", 3),
                new Fir(g3, "Thcc", 8),
                new Fir(g3, "Thdd", 3)
        };

        for (Fir f : fire) {
            f.start();   // porneste firul si apeleaza run()
        }
    }

    // ------------------------------------------------------------------
    // Fereastra
    // ------------------------------------------------------------------
    static void fereastra() {
        JFrame frame = new JFrame("Lab 2 - Varianta 7");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        zona = new JTextArea();
        zona.setEditable(false);
        zona.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));

        JButton bMediu = new JButton("Nivel mediu");
        JButton bAvansat = new JButton("Nivel avansat");
        JButton bSterge = new JButton("Sterge");
        bMediu.addActionListener(e -> mediu());
        bAvansat.addActionListener(e -> avansat());
        bSterge.addActionListener(e -> zona.setText(""));

        JPanel butoane = new JPanel(new FlowLayout(FlowLayout.LEFT));
        butoane.add(bMediu);
        butoane.add(bAvansat);
        butoane.add(bSterge);

        frame.add(butoane, BorderLayout.NORTH);
        frame.add(new JScrollPane(zona), BorderLayout.CENTER);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) throws Exception {
        if (args.length > 0 && args[0].equals("console")) {
            mediu();          // rulare fara fereastra, doar pentru verificare
            avansat();
            Thread.sleep(500);
        } else {
            SwingUtilities.invokeLater(Adrian2::fereastra);
        }
    }
}