import javax.swing.*;
import java.awt.*;
import java.util.Random;

/*
 * Lucrarea de laborator nr. 3 - Sincronizarea firelor de executie prin metodele clasei Thread
 * VARIANTA 7
 *
 * PARTEA COMUNA (Main2):
 *   - datele studentilor si intervalele din Tabelul 3 (varianta 7);
 *   - interfata grafica Swing (activitatea celor 4 fire + rezultatul final);
 *   - referintele statice catre firele de sarcina (th1..th4) si de afisare (p1..p4);
 *   - metodele log() si printSlow() prin care firele scriu in interfata;
 *   - startRun() - porneste cele doua faze.
 *
 * In Main2 se folosesc DOAR metode ale clasei Thread: start(), join(), sleep().
 *
 * FAZA 1 - SARCINILE: Thread1..Thread4 (th1..th4) isi fac sarcina in paralel si se TERMINA.
 *          Firul "coordinator" le asteapta pe toate cu join().
 * FAZA 2 - AFISAREA: abia dupa ce toate sarcinile s-au terminat, coordinatorul porneste firele
 *          de afisare Print1..Print4 (p1..p4), care isi sincronizeaza ordinea intre ele
 *          prin metodele din Vlad3.java si Adrian3.java.
 *
 * Fisiere:
 *   Main2.java   - partea comuna
 *   Vlad3.java   - Thread3, Thread4 (sarcini) + Print3, Print4 (afisare)
 *   Adrian3.java - Thread1, Thread2 (sarcini) + Print1, Print2 (afisare)
 *
 * Ordinea afisarii textelor:
 *   Th2 (Numele) -> Th4 (Grupa) -> Th1 (Prenumele) -> Th3 (Disciplina)
 *
 * Compilare:  javac Main2.java Vlad3.java Adrian3.java
 * Rulare:     java Main2
 */
public class Main2 {

    // ===== DATELE STUDENTILOR (de completat) =====
    static final String NUME       = "Verdes, Neagu";
    static final String PRENUME    = "Vlad, Adrian";
    static final String GRUPA      = "CR-242";
    static final String DISCIPLINA = "Programarea concurenta si distribuita";

    /** Intervalul dintre litere la afisarea textului (ms) - cerinta lucrarii. */
    static final int LETTER_DELAY_MS = 100;
    /** Mica pauza intre pasii sarcinilor, ca in interfata sa se vada lucrul "in paralel". */
    static final int STEP_DELAY_MS = 3;

    // ===== VARIANTA 7 (Tabelul 3) =====
    // Sarcina 3: de parcurs de la inceput intervalul [234, 1000]
    static final int T3_FROM = 234;
    static final int T3_TO   = 1000;
    // Sarcina 4: de parcurs de la sfarsit intervalul [456, 1234] (de la 1234 spre 456)
    static final int T4_FROM = 456;
    static final int T4_TO   = 1234;

    /** Dimensiunea tabloului pentru Sarcinile 1 si 2 (numere aleatoare din [15, 115]). */
    static final int ARRAY_SIZE = 100;

    // ===== FIRELE DE SARCINA (faza 1) =====
    static volatile Thread1 th1;
    static volatile Thread2 th2;
    static volatile Thread3 th3;
    static volatile Thread4 th4;

    // ===== FIRELE DE AFISARE (faza 2) - statice, ca sa poata fi referite din orice parte =====
    static volatile Print1 p1;
    static volatile Print2 p2;
    static volatile Print3 p3;
    static volatile Print4 p4;

    /**
     * Tabloul generat (numere aleatoare din [15, 115]) - il folosesc Th1 si Th2.
     * Filtrarea numerelor pare se face in codul lui Adrian (Adrian3.java).
     */
    static volatile int[] numbers = new int[0];

    // ===== COMPONENTELE INTERFETEI =====
    static final JTextArea arrayArea    = new JTextArea(3, 60);
    static final JTextArea activityArea = new JTextArea();
    static final JTextArea resultArea   = new JTextArea(4, 60);
    static final JButton startButton    = new JButton("Start");
    static final JLabel statusLabel     = new JLabel("Apasa Start pentru a porni cele 4 fire.");

    static {
        arrayArea.setEditable(false);
        arrayArea.setLineWrap(true);
        arrayArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        activityArea.setEditable(false);
        activityArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        resultArea.setEditable(false);
        resultArea.setFont(new Font(Font.MONOSPACED, Font.BOLD, 16));
    }

    // ===================== METODE AJUTATOARE (apelabile din orice fir) =====================

    private static void append(final JTextArea area, final String text) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                area.append(text);
                area.setCaretPosition(area.getDocument().getLength());
            }
        });
    }

    /** Scrie o linie (prefixata cu firul n = 1..4) in panoul comun "Activitate fire". */
    static void log(int n, String line) {
        append(activityArea, "[Th" + n + "] " + line + "\n");
    }

    /** Afiseaza textul litera cu litera (100 ms intre litere) in zona "Rezultat final". */
    static void printSlow(String text) {
        for (int i = 0; i < text.length(); i++) {
            try {
                Thread.sleep(LETTER_DELAY_MS);
            } catch (InterruptedException e) {
                // nu ne asteptam la intreruperi aici; continuam afisarea
            }
            append(resultArea, String.valueOf(text.charAt(i)));
        }
        append(resultArea, "\n");
    }

    // ===================== PORNIREA FIRELOR =====================

    /** Genereaza tabloul de numere (pentru Sarcinile 1 si 2) si il afiseaza. */
    private static void generateData() {
        Random rnd = new Random();
        int[] b = new int[ARRAY_SIZE];
        StringBuilder sb = new StringBuilder("Tabloul generat: ");
        for (int i = 0; i < b.length; i++) {
            b[i] = 15 + rnd.nextInt(101); // [15, 115]
            sb.append(b[i]).append(' ');
        }
        numbers = b;
        final String text = sb.toString();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                arrayArea.setText(text);
                arrayArea.setCaretPosition(0);
            }
        });
    }

    private static void setStatus(final String text, final boolean startEnabled) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                statusLabel.setText(text);
                startButton.setEnabled(startEnabled);
            }
        });
    }

    /** Creeaza firele noi si le porneste. Se poate apela de mai multe ori (buton Start). */
    static void startRun() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                activityArea.setText("");
                resultArea.setText("");
                startButton.setEnabled(false);
                statusLabel.setText("Faza 1: firele isi fac sarcinile...");
            }
        });

        generateData();

        // Firele de sarcina (faza 1)
        th1 = new Thread1();
        th2 = new Thread2();
        th3 = new Thread3();
        th4 = new Thread4();
        // Firele de afisare (faza 2) - create acum, pornite abia dupa faza 1
        p1 = new Print1();
        p2 = new Print2();
        p3 = new Print3();
        p4 = new Print4();

        final Thread[] tasks  = { th1, th2, th3, th4 };
        final Thread[] prints = { p1, p2, p3, p4 };

        // Firul coordonator: doar start() si join()
        Thread coordinator = new Thread(new Runnable() {
            @Override
            public void run() {
                // ----- FAZA 1: pornim sarcinile si le asteptam pe toate cu join() -----
                for (Thread t : tasks) {
                    t.start();
                }
                for (Thread t : tasks) {
                    try {
                        t.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                setStatus("Toate sarcinile s-au terminat. Faza 2: se afiseaza textele...", false);

                // ----- FAZA 2: pornim firele de afisare -----
                // ORDINEA DE PORNIRE CONTEAZA: join()/isAlive()/interrupt() pe un fir nepornit
                // nu functioneaza (join se intoarce imediat, isAlive = false, interrupt se pierde).
                // Lantul: p4 -> join(p2); p1 -> join(p2) + isAlive(p4); p4 -> interrupt(p3); p3 -> join(p1)
                // => pornim in ordinea: p2, p4, p3, p1
                p2.start();
                p4.start();
                p3.start();
                p1.start();

                for (Thread t : prints) {
                    try {
                        t.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                setStatus("Toate firele s-au terminat. Poti apasa Start din nou.", true);
            }
        }, "coordinator");
        coordinator.start();
    }

    // ===================== INTERFATA GRAFICA =====================

    private static JScrollPane panel(String title, JTextArea area) {
        JScrollPane sp = new JScrollPane(area);
        sp.setBorder(BorderFactory.createTitledBorder(title));
        return sp;
    }

    private static void buildGui() {
        JFrame frame = new JFrame("Lab 3 PCD - Sincronizarea firelor (Thread) - Varianta 7");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Sus: titlu + tabloul generat + buton Start
        JPanel top = new JPanel(new BorderLayout(8, 4));
        top.setBorder(BorderFactory.createEmptyBorder(8, 8, 4, 8));
        JLabel title = new JLabel("Varianta 7 - sincronizare prin metodele clasei Thread (join, interrupt, isAlive)");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 14f));
        top.add(title, BorderLayout.NORTH);
        top.add(new JScrollPane(arrayArea), BorderLayout.CENTER);
        startButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                startRun();
            }
        });
        top.add(startButton, BorderLayout.EAST);

        // Centru: activitatea tuturor firelor intr-un singur panou
        JPanel center = new JPanel(new BorderLayout());
        center.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        center.add(panel("Activitate fire (Th1 + Th2 + Th3 + Th4)", activityArea), BorderLayout.CENTER);

        // Jos: rezultatul final + status
        JPanel bottom = new JPanel(new BorderLayout(4, 4));
        bottom.setBorder(BorderFactory.createEmptyBorder(4, 8, 8, 8));
        bottom.add(panel("Rezultat final (ordine: Th2 -> Th4 -> Th1 -> Th3, litere la 100 ms)", resultArea),
                BorderLayout.CENTER);
        bottom.add(statusLabel, BorderLayout.SOUTH);

        frame.setLayout(new BorderLayout());
        frame.add(top, BorderLayout.NORTH);
        frame.add(center, BorderLayout.CENTER);
        frame.add(bottom, BorderLayout.SOUTH);
        frame.setSize(1150, 800);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                buildGui();
            }
        });
    }
}