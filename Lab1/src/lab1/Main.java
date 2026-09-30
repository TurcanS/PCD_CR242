package lab1;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.util.Random;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/**
 * Lucrarea de laborator nr. 1 (PCD) - Crearea thread-urilor, varianta 4.
 * Patru fire de execuție citesc același tablou mas[]; după terminarea lor
 * firul principal afișează literă cu literă informația despre studenți.
 */
public class Main {

    private static final int DIMENSIUNE = 100;
    private static final int INTERVAL_LITERE_MS = 100;
    private static final String STUDENTI =
            "Lucrarea de laborator nr. 1 a fost efectuată de studenții grupei CR-242: "
            + "Cornos Ilie și Spranceana Marius (echipa 4, varianta 4)";
    private static final Font MONO = new Font(Font.MONOSPACED, Font.PLAIN, 13);

    private static final boolean GUI = !GraphicsEnvironment.isHeadless();
    private static JPanel panouFire;
    private static JLabel etichetaStudenti;

    public static void main(String[] args) throws Exception {
        int[] mas = new Random().ints(DIMENSIUNE, 1, 101).toArray();
        StringBuilder text = new StringBuilder();
        for (int x : mas) {
            text.append(x).append(' ');
        }
        System.out.println("mas[]: " + text);
        if (GUI) {
            SwingUtilities.invokeAndWait(() -> creeazaFereastra(text.toString()));
        }

        CornosIlie cornos = new CornosIlie(mas,
                iesire("Cornos Ilie - Th1 (de la primul element)"),
                iesire("Cornos Ilie - Th2 (de la ultimul element)"));
        SpranceanaMarius spranceana = new SpranceanaMarius(mas,
                iesire("Spranceana Marius - Th1 (de la primul element)"),
                iesire("Spranceana Marius - Th2 (de la ultimul element)"));
        if (GUI) {
            SwingUtilities.invokeLater(panouFire::revalidate);
        }

        cornos.start();
        spranceana.start();
        cornos.join();
        spranceana.join();

        // firul principal: informația despre studenți, câte o literă la 100 ms
        for (int i = 1; i <= STUDENTI.length(); i++) {
            System.out.print(STUDENTI.charAt(i - 1));
            System.out.flush();
            if (GUI) {
                String afisat = STUDENTI.substring(0, i);
                SwingUtilities.invokeLater(() -> etichetaStudenti.setText(afisat));
            }
            Thread.sleep(INTERVAL_LITERE_MS);
        }
        System.out.println();
    }

    /** Creează destinația în care un fir își scrie rezultatele: consola și, dacă există, un panou din fereastră. */
    private static Consumer<String> iesire(String titlu) {
        if (!GUI) {
            return linie -> System.out.println(Thread.currentThread().getName() + ": " + linie);
        }
        JTextArea zona = new JTextArea();
        zona.setEditable(false);
        zona.setFont(MONO);
        JScrollPane derulare = new JScrollPane(zona);
        derulare.setBorder(BorderFactory.createTitledBorder(titlu));
        SwingUtilities.invokeLater(() -> panouFire.add(derulare));
        return linie -> {
            System.out.println(Thread.currentThread().getName() + ": " + linie);
            // componentele Swing se modifică doar din firul de evenimente
            SwingUtilities.invokeLater(() -> zona.append(linie + "\n"));
        };
    }

    private static void creeazaFereastra(String tablou) {
        JTextArea zonaTablou = new JTextArea(tablou, 4, 80);
        zonaTablou.setEditable(false);
        zonaTablou.setLineWrap(true);
        zonaTablou.setWrapStyleWord(true);
        zonaTablou.setFont(MONO);
        zonaTablou.setBorder(BorderFactory.createTitledBorder("Tabloul mas (100 de valori între 1 și 100)"));

        panouFire = new JPanel(new GridLayout(2, 2, 6, 6));

        etichetaStudenti = new JLabel(" ");
        etichetaStudenti.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        etichetaStudenti.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JFrame fereastra = new JFrame("PCD - Lucrarea de laborator nr. 1 - Varianta 4");
        fereastra.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fereastra.setLayout(new BorderLayout(6, 6));
        fereastra.add(zonaTablou, BorderLayout.NORTH);
        fereastra.add(panouFire, BorderLayout.CENTER);
        fereastra.add(etichetaStudenti, BorderLayout.SOUTH);
        fereastra.setSize(1000, 700);
        fereastra.setLocationRelativeTo(null);
        fereastra.setVisible(true);
    }
}
