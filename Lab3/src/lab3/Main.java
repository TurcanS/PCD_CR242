package lab3;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.util.Random;
import java.util.function.Consumer;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.text.DefaultCaret;

/**
 * Lucrarea de laborator nr. 3 (PCD) - Sincronizarea firelor de execuție prin metodele clasei Thread. Varianta 4.
 * Main doar pregătește datele și fereastra, creează cele patru fire și le pornește.
 * Sarcinile, sincronizarea și textele finale sunt în metodele run() ale claselor Thread1..Thread4.
 */
public class Main {

    private static final int DIMENSIUNE = 100;
    private static final Font MONO = new Font(Font.MONOSPACED, Font.PLAIN, 13);

    private static final boolean GUI = !GraphicsEnvironment.isHeadless();
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

        // Toate liniile firelor se scriu în aceeași zonă de text, una sub alta
        Consumer<String> linii = linii();

        // Creăm firele: fiecare primește referințele la firele pe care le așteaptă sau cărora le trimite semnal
        Thread2 th2 = new Thread2(mas, linii, litere);
        Thread4 th4 = new Thread4(th2, linii, litere);
        Thread1 th1 = new Thread1(mas, th2, th4, linii, litere);
        Thread3 th3 = new Thread3(th1, th2, th4, linii, litere);

        // Pornim fiecare fir înaintea firelor care îl așteaptă sau îi trimit semnal:
        // Th2 înaintea lui Th4 (isAlive) și Th1 (interrupt), iar Th3 (join pe toate) ultimul.
        th2.start();
        th4.start();
        th1.start();
        th3.start();
    }

    /** Destinația liniilor tuturor firelor: consola și zona unică din fereastră, cu numele firului în față. */
    private static Consumer<String> linii() {
        return linie -> {
            String text = Thread.currentThread().getName() + ": " + linie;
            System.out.println(text);
            if (GUI) {
                // componentele Swing se modifică doar din firul de evenimente
                SwingUtilities.invokeLater(() -> zonaText.append(text + "\n"));
            }
        };
    }

    /** Destinația textelor afișate literă cu literă după terminarea sarcinilor (aceeași zonă, la sfârșit). */
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
        zonaText = new JTextArea();
        zonaText.setEditable(false);
        zonaText.setLineWrap(true);
        zonaText.setFont(MONO);
        zonaText.setText("Tabloul mas (poziție:valoare, 100 de valori între 1 și 100):\n" + tablou + "\n\n");
        ((DefaultCaret) zonaText.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE); // derulare automată în jos

        JFrame fereastra = new JFrame("PCD - Lucrarea de laborator nr. 3 - Varianta 4 - Cornos Ilie, Spranceana Marius");
        fereastra.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fereastra.add(new JScrollPane(zonaText));
        fereastra.setSize(1000, 780);
        fereastra.setLocationRelativeTo(null);
        fereastra.setVisible(true);
    }
}
