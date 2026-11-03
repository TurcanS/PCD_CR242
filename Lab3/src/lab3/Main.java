package lab3;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.util.Random;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/**
 * Lucrarea de laborator nr. 3 (PCD) - Sincronizarea firelor de execuție prin metodele clasei Thread. Varianta 4.
 * Main doar pregătește datele și fereastra, creează cele patru fire și le pornește.
 * Sarcinile, sincronizarea și textele finale sunt în metodele run() ale claselor Thread1..Thread4.
 */
public class Main {

    private static final int DIMENSIUNE = 100;
    private static final Font MONO = new Font(Font.MONOSPACED, Font.PLAIN, 13);

    private static final boolean GUI = !GraphicsEnvironment.isHeadless();
    private static JPanel panouFire;
    private static JTextArea zonaText;

    public static void main(String[] args) throws Exception {
        // Tabloul comun pentru Th1 și Th2: 100 de numere aleatoare între 1 și 100
        int[] mas = new Random().ints(DIMENSIUNE, 1, 101).toArray();
        StringBuilder tablou = new StringBuilder();
        for (int i = 0; i < mas.length; i++) {
            tablou.append(i).append(':').append(mas[i]).append("  ");
        }
        System.out.println("Tabloul mas (poziție:valoare): " + tablou);
        if (GUI) {
            SwingUtilities.invokeAndWait(() -> creeazaFereastra(tablou.toString()));
        }
        Consumer<String> litere = litere();

        // Panourile din fereastră, în ordinea Th1, Th2, Th3, Th4
        Consumer<String> linii1 = linii("Th1 (Cornos Ilie) - pozițiile numerelor impare, de la primul element");
        Consumer<String> linii2 = linii("Th2 (Cornos Ilie) - pozițiile numerelor impare, de la ultimul element");
        Consumer<String> linii3 = linii("Th3 (Spranceana Marius) - intervalul [234, 987] de la început");
        Consumer<String> linii4 = linii("Th4 (Spranceana Marius) - intervalul [123, 890] de la sfârșit");

        // Creăm firele: fiecare primește referințele la firele pe care le așteaptă sau cărora le trimite semnal
        Thread2 th2 = new Thread2(mas, linii2, litere);
        Thread4 th4 = new Thread4(th2, linii4, litere);
        Thread1 th1 = new Thread1(mas, th2, th4, linii1, litere);
        Thread3 th3 = new Thread3(th1, th2, th4, linii3, litere);

        // Pornim fiecare fir înaintea firelor care îl așteaptă sau îi trimit semnal:
        // Th2 înaintea lui Th4 (isAlive) și Th1 (interrupt), iar Th3 (join pe toate) ultimul.
        th2.start();
        th4.start();
        th1.start();
        th3.start();
    }

    /** Destinația liniilor unui fir: consola și, dacă există, panoul lui din fereastră. */
    private static Consumer<String> linii(String titlu) {
        if (!GUI) {
            return linie -> System.out.println(Thread.currentThread().getName() + ": " + linie);
        }
        JTextArea zona = new JTextArea();
        zona.setEditable(false);
        zona.setFont(MONO);
        JScrollPane derulare = new JScrollPane(zona);
        derulare.setBorder(BorderFactory.createTitledBorder(titlu));
        SwingUtilities.invokeLater(() -> {
            panouFire.add(derulare);
            panouFire.revalidate();
        });
        return linie -> {
            System.out.println(Thread.currentThread().getName() + ": " + linie);
            // componentele Swing se modifică doar din firul de evenimente
            SwingUtilities.invokeLater(() -> zona.append(linie + "\n"));
        };
    }

    /** Destinația textelor afișate literă cu literă după terminarea sarcinilor. */
    private static Consumer<String> litere() {
        return litera -> {
            System.out.print(litera);
            System.out.flush();
            if (GUI) {
                SwingUtilities.invokeLater(() -> zonaText.append(litera));
            }
        };
    }

    private static void creeazaFereastra(String tablou) {
        JTextArea zonaTablou = new JTextArea(tablou, 4, 80);
        zonaTablou.setEditable(false);
        zonaTablou.setLineWrap(true);
        zonaTablou.setWrapStyleWord(true);
        zonaTablou.setFont(MONO);
        zonaTablou.setBorder(BorderFactory.createTitledBorder("Tabloul mas (poziție:valoare, 100 de valori între 1 și 100)"));

        panouFire = new JPanel(new GridLayout(2, 2, 6, 6));

        zonaText = new JTextArea(5, 80);
        zonaText.setEditable(false);
        zonaText.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        zonaText.setBorder(BorderFactory.createTitledBorder("Textele firelor (ordinea Th2, Th4, Th1, Th3), câte o literă la 100 ms"));

        JFrame fereastra = new JFrame("PCD - Lucrarea de laborator nr. 3 - Varianta 4 - Cornos Ilie, Spranceana Marius");
        fereastra.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fereastra.setLayout(new BorderLayout(6, 6));
        fereastra.add(new JScrollPane(zonaTablou), BorderLayout.NORTH);
        fereastra.add(panouFire, BorderLayout.CENTER);
        fereastra.add(zonaText, BorderLayout.SOUTH);
        fereastra.setSize(1100, 780);
        fereastra.setLocationRelativeTo(null);
        fereastra.setVisible(true);
    }
}
