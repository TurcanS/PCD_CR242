package lab6;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
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
 * Lucrarea de laborator nr. 6 (PCD) - Sincronizarea firelor de execuție prin clase de sincronizare
 * în problema producător-consumator.
 * Varianta 4: X = 3 producători, Y = 2 consumatori, Z = 66 obiecte, D = 11, obiecte: consoane, F = 1.
 */
public class Main {

    private static final int X = 3;  // producători
    private static final int Y = 2;  // consumatori
    private static final int Z = 66; // obiecte produse și consumate în total
    private static final int D = 11; // dimensiunea depozitului

    private static final boolean GUI = !GraphicsEnvironment.isHeadless();
    private static JTextArea zonaJurnal;
    private static JLabel etichetaFaza;
    private static JProgressBar baraProduse, baraConsumate;
    private static final JLabel[] celule = new JLabel[D];

    public static void main(String[] args) throws Exception {
        if (GUI) {
            SwingUtilities.invokeAndWait(Main::creeazaFereastra);
        }
        Depozit depozit = new Depozit(D, Z, Main::scrie, Main::arataStare);

        List<Thread> fire = new ArrayList<>();
        for (int i = 1; i <= X; i++) {
            fire.add(new Producator("Producător " + i, depozit, Main::scrie));
        }
        for (int i = 1; i <= Y; i++) {
            fire.add(new Consumator("Consumator " + i, depozit, Main::scrie));
        }
        for (Thread fir : fire) {
            fir.start();
        }
        for (Thread fir : fire) {
            fir.join();
        }
        scrie("RAPORT FINAL: " + depozit.rezumat());
    }

    private static void scrie(String mesaj) {
        System.out.println(mesaj);
        if (GUI) {
            // componentele Swing se modifică doar din firul de evenimente
            SwingUtilities.invokeLater(() -> {
                zonaJurnal.append(mesaj + "\n");
                zonaJurnal.setCaretPosition(zonaJurnal.getDocument().getLength());
            });
        }
    }

    private static void arataStare(String continut, int produse, int consumate, boolean umplere) {
        if (!GUI) {
            return;
        }
        SwingUtilities.invokeLater(() -> {
            for (int i = 0; i < D; i++) {
                boolean ocupat = i < continut.length();
                celule[i].setText(ocupat ? String.valueOf(continut.charAt(i)) : " ");
                celule[i].setBackground(ocupat ? new Color(0xCDE7FF) : Color.WHITE);
            }
            baraProduse.setValue(produse);
            baraProduse.setString("Produse: " + produse + " / " + Z);
            baraConsumate.setValue(consumate);
            baraConsumate.setString("Consumate: " + consumate + " / " + Z);
            if (consumate == Z) {
                etichetaFaza.setText("Faza: terminat");
            } else {
                etichetaFaza.setText(umplere ? "Faza: UMPLERE (lucrează producătorii)"
                        : "Faza: GOLIRE (lucrează consumatorii)");
            }
        });
    }

    private static void creeazaFereastra() {
        JLabel varianta = new JLabel("Varianta 4:  X = " + X + " producători,  Y = " + Y + " consumatori,  Z = " + Z
                + " obiecte,  D = " + D + ",  obiecte: consoane,  F = 1");
        varianta.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
        varianta.setBorder(BorderFactory.createEmptyBorder(8, 8, 4, 8));
        varianta.setAlignmentX(0);

        etichetaFaza = new JLabel("Faza: UMPLERE (lucrează producătorii)");
        etichetaFaza.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        etichetaFaza.setBorder(BorderFactory.createEmptyBorder(2, 8, 6, 8));
        etichetaFaza.setAlignmentX(0);

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

        baraProduse = bara("Produse: 0 / " + Z);
        baraConsumate = bara("Consumate: 0 / " + Z);
        JPanel panouBare = new JPanel(new GridLayout(1, 2, 8, 4));
        panouBare.setBorder(BorderFactory.createTitledBorder("Progres"));
        panouBare.setAlignmentX(0);
        panouBare.add(baraProduse);
        panouBare.add(baraConsumate);

        JPanel sus = new JPanel();
        sus.setLayout(new BoxLayout(sus, BoxLayout.Y_AXIS));
        sus.add(varianta);
        sus.add(etichetaFaza);
        sus.add(panouDepozit);
        sus.add(panouBare);

        zonaJurnal = new JTextArea();
        zonaJurnal.setEditable(false);
        zonaJurnal.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        JScrollPane derulare = new JScrollPane(zonaJurnal);
        derulare.setBorder(BorderFactory.createTitledBorder("Jurnalul operațiilor"));

        JLabel studenti = new JLabel("Grupa CR-242, echipa 4: Cornos Ilie (producătorii) și Spranceana Marius (consumatorii)");
        studenti.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JFrame fereastra = new JFrame("PCD - Lucrarea de laborator nr. 6 - Varianta 4");
        fereastra.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fereastra.setLayout(new BorderLayout(6, 6));
        fereastra.add(sus, BorderLayout.NORTH);
        fereastra.add(derulare, BorderLayout.CENTER);
        fereastra.add(studenti, BorderLayout.SOUTH);
        fereastra.setSize(1000, 760);
        fereastra.setLocationRelativeTo(null);
        fereastra.setVisible(true);
    }

    private static JProgressBar bara(String text) {
        JProgressBar bara = new JProgressBar(0, Z);
        bara.setStringPainted(true);
        bara.setString(text);
        return bara;
    }
}
