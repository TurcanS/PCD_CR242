import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Lucrarea de laborator nr. 3 - PCD
 * Tema: Sincronizarea firelor de execuție utilizând metodele clasei Thread
 * Varianta 2
 *
 * MAIN - partea comună: datele, interfața grafică și pornirea firelor.
 *   Anatol.java  -> Th1, Th3 (sincronizare: isAlive(), join())
 *   Catalin.java -> Th2, Th4 (sincronizare: sleep(), interrupt())
 *
 * Ordinea mesajelor finale (literă cu literă, la 100 ms):
 *     Th2 (Numele) -> Th4 (Grupa) -> Th1 (Prenumele) -> Th3 (Disciplina)
 */
public class Main {

    // ===================== Date pentru mesajele finale =====================
    // Completați cu datele voastre reale
    static final String PRENUME    = "Anatol, Catalin";
    static final String NUME       = "NumeAnatol, NumeCatalin";
    static final String GRUPA      = "XX-000";
    static final String DISCIPLINA = "Programarea concurentă și distribuită";

    // ===================== Datele de intrare =====================
    static final int DIM = 40;
    static int[] tablou = new int[DIM];

    // ===================== Firele de execuție =====================
    static Anatol.Th1 th1;
    static Catalin.Th2 th2;
    static Anatol.Th3 th3;
    static Catalin.Th4 th4;

    // ===================== Interfața grafică =====================
    static JTextArea zonaTablou, zonaTh1, zonaTh2, zonaTh3, zonaTh4, zonaFinal;
    static JButton butonStart, butonTablouNou;

    // ==========================================================================
    //  Pornirea firelor
    // ==========================================================================
    static void pornesteFirele() {
        butonStart.setEnabled(false);
        butonTablouNou.setEnabled(false);
        zonaTh1.setText("");
        zonaTh2.setText("");
        zonaTh3.setText("");
        zonaTh4.setText("");
        zonaFinal.setText("");

        // Se creează toate firele înainte de pornire,
        // ca fiecare să aibă deja referință la celelalte.
        th1 = new Anatol.Th1();
        th2 = new Catalin.Th2();
        th3 = new Anatol.Th3();
        th4 = new Catalin.Th4();

        th1.start();
        th2.start();
        th3.start();
        th4.start();
    }

    /** Apelată de ultimul fir (Th3) - programul poate fi rulat din nou. */
    static void toateFireleAuTerminat() {
        afiseaza(zonaFinal, "\nToate firele și-au terminat execuția.\n");
        SwingUtilities.invokeLater(() -> {
            butonStart.setEnabled(true);
            butonTablouNou.setEnabled(true);
        });
    }

    static void genereazaTablou() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < DIM; i++) {
            tablou[i] = (int) (Math.random() * 100) + 1;   // numere între 1 și 100
            sb.append("[").append(i).append("]=").append(tablou[i]).append("   ");
            if ((i + 1) % 10 == 0) {
                sb.append("\n");
            }
        }
        zonaTablou.setText(sb.toString());
    }

    // ==========================================================================
    //  Metode ajutătoare folosite de toate firele
    // ==========================================================================

    /** Componentele Swing se modifică doar din firul grafic (EDT). */
    static void afiseaza(JTextArea zona, String text) {
        SwingUtilities.invokeLater(() -> zona.append(text));
    }

    /** Afișează textul literă cu literă, la interval de 100 ms. */
    static void afiseazaLiteraCuLitera(String eticheta, String text) {
        afiseaza(zonaFinal, eticheta);
        for (int i = 0; i < text.length(); i++) {
            afiseaza(zonaFinal, String.valueOf(text.charAt(i)));
            pauza(100);
        }
        afiseaza(zonaFinal, "\n");
    }

    static void pauza(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // ==========================================================================
    //  Construirea ferestrei
    // ==========================================================================
    static JTextArea creeazaZona(int randuri) {
        JTextArea zona = new JTextArea(randuri, 30);
        zona.setEditable(false);
        zona.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        return zona;
    }

    static JScrollPane cuTitlu(JTextArea zona, String titlu) {
        JScrollPane panou = new JScrollPane(zona);
        panou.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), titlu,
                TitledBorder.LEFT, TitledBorder.TOP));
        return panou;
    }

    static void creeazaFereastra() {
        JFrame fereastra = new JFrame("Laborator 3 PCD - Varianta 2 - Sincronizarea firelor prin metodele clasei Thread");
        fereastra.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fereastra.setLayout(new BorderLayout(5, 5));

        // Sus: tabloul generat + butoane
        zonaTablou = creeazaZona(4);
        butonStart = new JButton("Pornește firele");
        butonTablouNou = new JButton("Tablou nou");
        butonStart.addActionListener(e -> pornesteFirele());
        butonTablouNou.addActionListener(e -> genereazaTablou());

        JPanel panouButoane = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panouButoane.add(butonStart);
        panouButoane.add(butonTablouNou);

        JPanel panouSus = new JPanel(new BorderLayout());
        panouSus.add(cuTitlu(zonaTablou, "Tabloul de numere (indice = poziția)"), BorderLayout.CENTER);
        panouSus.add(panouButoane, BorderLayout.SOUTH);

        // Centru: câte o zonă pentru fiecare fir
        zonaTh1 = creeazaZona(12);
        zonaTh2 = creeazaZona(12);
        zonaTh3 = creeazaZona(12);
        zonaTh4 = creeazaZona(12);

        JPanel panouFire = new JPanel(new GridLayout(2, 2, 5, 5));
        panouFire.add(cuTitlu(zonaTh1, "Th1 (Anatol) - suma pozițiilor pare, de la primul element"));
        panouFire.add(cuTitlu(zonaTh2, "Th2 (Catalin) - suma pozițiilor pare, de la ultimul element"));
        panouFire.add(cuTitlu(zonaTh3, "Th3 (Anatol) - interval [" + Anatol.TH3_DE + ", "
                + Anatol.TH3_PANA + "] de la început"));
        panouFire.add(cuTitlu(zonaTh4, "Th4 (Catalin) - interval [" + Catalin.TH4_PANA + ", "
                + Catalin.TH4_DE + "] de la sfârșit"));

        // Jos: mesajele finale
        zonaFinal = creeazaZona(6);
        zonaFinal.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));

        fereastra.add(panouSus, BorderLayout.NORTH);
        fereastra.add(panouFire, BorderLayout.CENTER);
        fereastra.add(cuTitlu(zonaFinal, "Mesaje finale (literă cu literă, la 100 ms)"), BorderLayout.SOUTH);

        genereazaTablou();

        fereastra.setSize(1150, 800);
        fereastra.setLocationRelativeTo(null);
        fereastra.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> creeazaFereastra());
    }
}
