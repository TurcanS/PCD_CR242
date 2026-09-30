package lab5;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/**
 * Lucrarea de laborator nr. 5 (PCD) - Pool-uri de fire de execuție în Java. Problema producător-consumator.
 * Varianta 4: X = 3 producători, Y = 2 consumatori, Z = 12 obiecte, D = 11, obiecte: consoane, F = 2.
 */
public class Main {

    private static final int X = 3;  // producători
    private static final int Y = 2;  // consumatori
    private static final int Z = 12; // obiecte pentru fiecare consumator
    private static final int D = 11; // dimensiunea depozitului
    private static final int F = 2;  // obiecte produse de fiecare dată

    private static final boolean GUI = !GraphicsEnvironment.isHeadless();
    private static JTextArea zonaJurnal;
    private static final JLabel[] celule = new JLabel[D];
    private static final JProgressBar[] bare = new JProgressBar[Y];

    public static void main(String[] args) throws Exception {
        if (GUI) {
            SwingUtilities.invokeAndWait(Main::creeazaFereastra);
        }
        Consumer<String> jurnal = Main::scrie;
        Depozit depozit = new Depozit(D, jurnal, Main::arataDepozit);

        // producătorii produc împreună exact atâtea obiecte câte le trebuie consumatorilor
        AtomicInteger loturiRamase = new AtomicInteger(Y * Z / F);

        // pool fix: câte un fir pentru fiecare sarcină (X producători + Y consumatori)
        ExecutorService pool = Executors.newFixedThreadPool(X + Y);
        long start = System.currentTimeMillis();
        for (int i = 0; i < Y; i++) {
            int nr = i;
            pool.execute(new Consumator("Consumator " + (i + 1), depozit, Z, jurnal, luate -> arataProgres(nr, luate)));
        }
        for (int i = 1; i <= X; i++) {
            pool.execute(new Producator("Producător " + i, depozit, loturiRamase, jurnal));
        }

        pool.shutdown(); // nu se mai primesc sarcini noi; cele trimise se execută până la capăt
        if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {
            pool.shutdownNow();
        }
        scrie("RAPORT FINAL: total produse: " + depozit.produse() + ", total consumate: " + depozit.consumate()
                + ", timp: " + (System.currentTimeMillis() - start) + " ms");
    }

    /** Adaugă în jurnal mesajul, precedat de numele firului din pool care l-a produs. */
    private static void scrie(String text) {
        String mesaj = String.format("[%-15s] %s", Thread.currentThread().getName(), text);
        System.out.println(mesaj);
        if (GUI) {
            // componentele Swing se modifică doar din firul de evenimente
            SwingUtilities.invokeLater(() -> {
                zonaJurnal.append(mesaj + "\n");
                zonaJurnal.setCaretPosition(zonaJurnal.getDocument().getLength());
            });
        }
    }

    private static void arataDepozit(String continut) {
        if (!GUI) {
            return;
        }
        SwingUtilities.invokeLater(() -> {
            for (int i = 0; i < D; i++) {
                boolean ocupat = i < continut.length();
                celule[i].setText(ocupat ? String.valueOf(continut.charAt(i)) : " ");
                celule[i].setBackground(ocupat ? new Color(0xCDE7FF) : Color.WHITE);
            }
        });
    }

    private static void arataProgres(int consumator, int luate) {
        if (!GUI) {
            return;
        }
        SwingUtilities.invokeLater(() -> {
            bare[consumator].setValue(luate);
            bare[consumator].setString("Consumator " + (consumator + 1) + ": " + luate + " / " + Z);
        });
    }

    private static void creeazaFereastra() {
        JLabel varianta = new JLabel("Varianta 4:  X = " + X + " producători,  Y = " + Y + " consumatori,  Z = " + Z
                + " obiecte,  D = " + D + ",  obiecte: consoane,  F = " + F + ",  pool fix de " + (X + Y) + " fire");
        varianta.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
        varianta.setBorder(BorderFactory.createEmptyBorder(8, 8, 4, 8));
        varianta.setAlignmentX(0);

        JPanel panouDepozit = new JPanel(new GridLayout(1, D, 4, 4));
        panouDepozit.setBorder(BorderFactory.createTitledBorder("Depozitul (D = " + D + ")"));
        panouDepozit.setAlignmentX(0);
        for (int i = 0; i < D; i++) {
            celule[i] = new JLabel(" ", SwingConstants.CENTER);
            celule[i].setFont(new Font(Font.MONOSPACED, Font.BOLD, 24));
            celule[i].setOpaque(true);
            celule[i].setBackground(Color.WHITE);
            celule[i].setBorder(BorderFactory.createLineBorder(Color.GRAY));
            panouDepozit.add(celule[i]);
        }

        JPanel panouBare = new JPanel(new GridLayout(1, Y, 8, 4));
        panouBare.setBorder(BorderFactory.createTitledBorder("Obiecte consumate"));
        panouBare.setAlignmentX(0);
        for (int i = 0; i < Y; i++) {
            bare[i] = new JProgressBar(0, Z);
            bare[i].setStringPainted(true);
            bare[i].setString("Consumator " + (i + 1) + ": 0 / " + Z);
            panouBare.add(bare[i]);
        }

        JPanel sus = new JPanel();
        sus.setLayout(new BoxLayout(sus, BoxLayout.Y_AXIS));
        sus.add(varianta);
        sus.add(panouDepozit);
        sus.add(panouBare);

        zonaJurnal = new JTextArea();
        zonaJurnal.setEditable(false);
        zonaJurnal.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        JScrollPane derulare = new JScrollPane(zonaJurnal);
        derulare.setBorder(BorderFactory.createTitledBorder("Jurnalul operațiilor"));

        JLabel studenti = new JLabel("Grupa CR-242, echipa 4: Cornos Ilie (producătorii) și Spranceana Marius (consumatorii)");
        studenti.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JFrame fereastra = new JFrame("PCD - Lucrarea de laborator nr. 5 - Varianta 4");
        fereastra.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fereastra.setLayout(new BorderLayout(6, 6));
        fereastra.add(sus, BorderLayout.NORTH);
        fereastra.add(derulare, BorderLayout.CENTER);
        fereastra.add(studenti, BorderLayout.SOUTH);
        fereastra.setSize(1000, 760);
        fereastra.setLocationRelativeTo(null);
        fereastra.setVisible(true);
    }
}
