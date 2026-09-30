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
 */
public class Main {

    private static final int DIMENSIUNE = 100;
    private static final Font MONO = new Font(Font.MONOSPACED, Font.PLAIN, 13);

    private static final boolean GUI = !GraphicsEnvironment.isHeadless();
    private static JPanel panouFire;
    private static JTextArea zonaText;

    public static void main(String[] args) throws Exception {
        int[] mas = new Random().ints(DIMENSIUNE, 1, 101).toArray();
        StringBuilder tablou = new StringBuilder();
        for (int x : mas) {
            tablou.append(x).append(' ');
        }
        System.out.println("mas[]: " + tablou);
        if (GUI) {
            SwingUtilities.invokeAndWait(() -> creeazaFereastra(tablou.toString()));
        }
        Consumer<String> litere = litere();

        Fir th1 = new CornosIlie.Th1(mas, linii("Th1 (Cornos Ilie) - numere impare, de la primul element"), litere);
        Fir th2 = new CornosIlie.Th2(mas, linii("Th2 (Cornos Ilie) - numere impare, de la ultimul element"), litere);
        Fir th3 = new SpranceanaMarius.FirInterval("Th3", "Disciplina: Programarea Concurentă și Distribuită",
                234, 987, 1, linii("Th3 (Spranceana Marius) - intervalul [234, 987] de la început"), litere);
        Fir th4 = new SpranceanaMarius.FirInterval("Th4", "Grupa: CR-242",
                890, 123, -1, linii("Th4 (Spranceana Marius) - intervalul [123, 890] de la sfârșit"), litere);

        // semnalul „sarcinile sunt gata”: Th1 -> Th3 -> Th4 -> Th2; ordinea textelor: Th2, Th4, Th1, Th3
        th1.leaga(th3, th4);
        th2.leaga(null, null);
        th3.leaga(th4, th1);
        th4.leaga(th2, th2);

        // fiecare fir este lansat după firul pe care îl va aștepta
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
        zonaTablou.setBorder(BorderFactory.createTitledBorder("Tabloul mas (100 de valori între 1 și 100)"));

        panouFire = new JPanel(new GridLayout(2, 2, 6, 6));

        zonaText = new JTextArea(5, 80);
        zonaText.setEditable(false);
        zonaText.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        zonaText.setBorder(BorderFactory.createTitledBorder("Textele firelor, după terminarea sarcinilor"));

        JFrame fereastra = new JFrame("PCD - Lucrarea de laborator nr. 3 - Varianta 4");
        fereastra.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fereastra.setLayout(new BorderLayout(6, 6));
        fereastra.add(zonaTablou, BorderLayout.NORTH);
        fereastra.add(panouFire, BorderLayout.CENTER);
        fereastra.add(zonaText, BorderLayout.SOUTH);
        fereastra.setSize(1000, 760);
        fereastra.setLocationRelativeTo(null);
        fereastra.setVisible(true);
    }
}
