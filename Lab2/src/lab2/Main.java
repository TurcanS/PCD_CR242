package lab2;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/**
 * Lucrarea de laborator nr. 2 (PCD) - Gruparea firelor de execuție. Modificarea priorității. Varianta 4.
 */
public class Main {

    private static final Font MONO = new Font(Font.MONOSPACED, Font.PLAIN, 13);
    private static final boolean GUI = !GraphicsEnvironment.isHeadless();
    private static JPanel panouMediu, panouAvansat;

    public static void main(String[] args) throws Exception {
        System.out.println("Varianta 4: " + Structura.FORMULA);
        if (GUI) {
            SwingUtilities.invokeAndWait(Main::creeazaFereastra);
        }
        Consumer<String> mediu = iesire("Nivelul mediu (Cornos Ilie) - enumerarea firelor", panouMediu);
        Consumer<String> avansatFire = iesire("Nivelul avansat (Spranceana Marius) - afișat de fire în run()", panouAvansat);
        Consumer<String> avansatEnumerare = iesire("Nivelul avansat (Spranceana Marius) - enumerarea firelor", panouAvansat);

        NivelMediu.ruleaza(mediu);
        NivelAvansat.ruleaza(avansatFire, avansatEnumerare);
    }

    /** Creează destinația în care se scriu rezultatele: consola și, dacă există, un panou din fereastră. */
    private static Consumer<String> iesire(String titlu, JPanel panou) {
        if (!GUI) {
            return linie -> System.out.println("[" + titlu + "] " + linie);
        }
        JTextArea zona = new JTextArea();
        zona.setEditable(false);
        zona.setFont(MONO);
        JScrollPane derulare = new JScrollPane(zona);
        derulare.setBorder(BorderFactory.createTitledBorder(titlu));
        SwingUtilities.invokeLater(() -> {
            panou.add(derulare);
            panou.revalidate();
        });
        return linie -> {
            System.out.println("[" + titlu + "] " + linie);
            // componentele Swing se modifică doar din firul de evenimente
            SwingUtilities.invokeLater(() -> zona.append(linie + "\n"));
        };
    }

    private static void creeazaFereastra() {
        JLabel formula = new JLabel("Varianta 4:  " + Structura.FORMULA);
        formula.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
        formula.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        // stânga: nivelul mediu; dreapta: cele două panouri ale nivelului avansat
        panouMediu = new JPanel(new GridLayout(1, 1));
        panouAvansat = new JPanel(new GridLayout(2, 1, 6, 6));
        JPanel panouri = new JPanel(new GridLayout(1, 2, 6, 6));
        panouri.add(panouMediu);
        panouri.add(panouAvansat);

        JLabel studenti = new JLabel("Grupa CR-242, echipa 4: Cornos Ilie și Spranceana Marius");
        studenti.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JFrame fereastra = new JFrame("PCD - Lucrarea de laborator nr. 2 - Varianta 4");
        fereastra.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fereastra.setLayout(new BorderLayout(6, 6));
        fereastra.add(formula, BorderLayout.NORTH);
        fereastra.add(panouri, BorderLayout.CENTER);
        fereastra.add(studenti, BorderLayout.SOUTH);
        fereastra.setSize(1000, 700);
        fereastra.setLocationRelativeTo(null);
        fereastra.setVisible(true);
    }
}
