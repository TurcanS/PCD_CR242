import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Random;

/*
 * Lucrarea de laborator nr. 3 - Sincronizarea firelor de executie prin metodele clasei Thread
 * VARIANTA 7
 *
 * PARTEA COMUNA (Main2):
 *   - datele studentilor si intervalele din Tabelul 3 (varianta 7);
 *   - interfata grafica Swing: UN SINGUR TABEL in care toate cele 4 fire scriu pe rand
 *     (coloanele: #, Fir, Mesaj), fiecare fir avand culoarea lui;
 *   - referintele statice catre cele 4 fire (folosite de ambele parti pentru join/interrupt);
 *   - metodele log() si printSlow() prin care firele scriu in interfata (API neschimbat);
 *   - startRun() - creeaza si porneste firele (in ordinea corecta).
 *
 * Fisiere:
 *   Main2.java   - partea comuna
 *   Vlad3.java   - Thread3 si Thread4
 *   Adrian3.java - Thread1 si Thread2
 *
 * Ordinea afisarii textelor (dupa terminarea sarcinilor):
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

    // ===== FIRELE (statice, ca sa poata fi referite din orice parte) =====
    static volatile Thread1 th1;
    static volatile Thread2 th2;
    static volatile Thread3 th3;
    static volatile Thread4 th4;

    /**
     * Firul-bariera "gate": se termina abia cand toate cele 4 fire si-au terminat sarcina
     * si asteapta la gate.join(). Pana atunci nimeni nu are voie sa afiseze textele.
     */
    static volatile Thread gate;

    /**
     * Tabloul generat (numere aleatoare din [15, 115]) - il folosesc Th1 si Th2.
     * Filtrarea numerelor pare se face in codul lui Adrian (Adrian3.java).
     */
    static volatile int[] numbers = new int[0];

    // ===== COMPONENTELE INTERFETEI =====
    /** Tabelul unic: fiecare apel log() adauga un rand (#, Fir, Mesaj). */
    static final DefaultTableModel model = new DefaultTableModel(new String[] { "#", "Fir", "Mesaj" }, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    static final JTable table = new JTable(model);

    static final JTextArea arrayArea  = new JTextArea(3, 60);
    static final JTextArea resultArea = new JTextArea(3, 60);
    static final JButton startButton  = new JButton("Start");
    static final JLabel statusLabel   = new JLabel("Apasa Start pentru a porni cele 4 fire.");

    /** Culori pentru randurile celor 4 fire (Th1..Th4). */
    private static final Color[] COLORS = {
        new Color(255, 235, 235),   // Th1
        new Color(235, 245, 255),   // Th2
        new Color(235, 255, 235),   // Th3
        new Color(255, 250, 225)    // Th4
    };

    static {
        arrayArea.setEditable(false);
        arrayArea.setLineWrap(true);
        arrayArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        resultArea.setEditable(false);
        resultArea.setFont(new Font(Font.MONOSPACED, Font.BOLD, 16));
    }

    // ===================== METODE AJUTATOARE (apelabile din orice fir) =====================

    /** Adauga un rand in tabelul unic: firul n (1..4) a scris "line". */
    static void log(final int n, final String line) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                model.addRow(new Object[] { model.getRowCount() + 1, "Th" + n, line });
                int last = model.getRowCount() - 1;
                table.scrollRectToVisible(table.getCellRect(last, 0, true));
            }
        });
    }

    private static void appendResult(final String text) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                resultArea.append(text);
                resultArea.setCaretPosition(resultArea.getDocument().getLength());
            }
        });
    }

    /** Afiseaza textul litera cu litera (100 ms intre litere) in zona "Rezultat final". */
    static void printSlow(String text) {
        boolean interrupted = false;
        for (int i = 0; i < text.length(); i++) {
            try {
                Thread.sleep(LETTER_DELAY_MS);
            } catch (InterruptedException e) {
                interrupted = true; // nu repunem flagul in bucla, ca sa pastram cei 100 ms intre litere
            }
            appendResult(String.valueOf(text.charAt(i)));
        }
        appendResult("\n");
        if (interrupted) {
            Thread.currentThread().interrupt(); // pastram semnalul de intrerupere, o singura data
        }
    }

    /**
     * BARIERA: fiecare fir o apeleaza dupa ce si-a terminat sarcina si INAINTE de a afisa
     * orice text. Firul ramane blocat in gate.join() pana cand toate cele 4 fire au ajuns aici.
     * Returneaza true daca firul a fost intrerupt cu interrupt() in timpul asteptarii.
     */
    static boolean awaitAllTasks() {
        try {
            gate.join();
            return false;
        } catch (InterruptedException e) {
            return true;
        }
    }

    /** true daca toate firele din vector sunt in starea WAITING (adica stau in gate.join()). */
    private static boolean allWaiting(Thread[] workers) {
        for (Thread t : workers) {
            if (t.getState() != Thread.State.WAITING) {
                return false;
            }
        }
        return true;
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

    /** Creeaza firele noi si le porneste. Se poate apela de mai multe ori (buton Start). */
    static void startRun() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                model.setRowCount(0);
                resultArea.setText("");
                startButton.setEnabled(false);
                statusLabel.setText("Firele ruleaza...");
            }
        });

        generateData();

        th1 = new Thread1();
        th2 = new Thread2();
        th3 = new Thread3();
        th4 = new Thread4();

        final Thread[] all = { th1, th2, th3, th4 };

        // Firul-bariera: se termina cand toate cele 4 fire si-au terminat sarcina
        // si asteapta in gate.join() (starea WAITING). Se porneste PRIMUL.
        gate = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!allWaiting(all)) {
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        // continuam sa asteptam
                    }
                }
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        statusLabel.setText("Toate firele si-au terminat sarcinile - se afiseaza textele...");
                    }
                });
            }
        }, "gate");
        gate.setDaemon(true);
        gate.start();

        // ORDINEA DE PORNIRE CONTEAZA: join() pe un fir care inca nu a pornit
        // (isAlive() == false) se intoarce imediat si sincronizarea nu functioneaza.
        // Lantul de join-uri: Th4 -> Th2, Th1 -> Th4, Th3 -> Th1
        // => pornim in ordinea: Th2, Th4, Th1, Th3
        th2.start();
        th4.start();
        th1.start();
        th3.start();

        // Fir "paznic": asteapta (join) terminarea tuturor firelor si reactiveaza butonul
        Thread watcher = new Thread(new Runnable() {
            @Override
            public void run() {
                for (Thread t : all) {
                    try {
                        t.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        startButton.setEnabled(true);
                        statusLabel.setText("Toate firele s-au terminat. Poti apasa Start din nou.");
                    }
                });
            }
        }, "watcher");
        watcher.setDaemon(true);
        watcher.start();
    }

    // ===================== INTERFATA GRAFICA =====================

    private static JScrollPane panel(String title, Component c) {
        JScrollPane sp = new JScrollPane(c);
        sp.setBorder(BorderFactory.createTitledBorder(title));
        return sp;
    }

    private static void buildGui() {
        JFrame frame = new JFrame("Lab 3 PCD - Sincronizarea firelor (Thread) - Varianta 7");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Sus: titlu + tabloul generat + buton
        JPanel top = new JPanel(new BorderLayout(8, 4));
        top.setBorder(BorderFactory.createEmptyBorder(8, 8, 4, 8));
        JLabel title = new JLabel("Varianta 7 - sincronizare prin metodele clasei Thread (join, interrupt, isAlive, getState)");
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

        // Centru: UN SINGUR tabel cu toate firele
        table.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        table.setRowHeight(20);
        table.setShowGrid(true);
        table.getColumnModel().getColumn(0).setPreferredWidth(60);
        table.getColumnModel().getColumn(0).setMaxWidth(80);
        table.getColumnModel().getColumn(1).setPreferredWidth(60);
        table.getColumnModel().getColumn(1).setMaxWidth(80);
        table.getColumnModel().getColumn(2).setPreferredWidth(950);
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value, boolean selected,
                                                           boolean focus, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, value, selected, focus, row, col);
                if (!selected) {
                    String fir = String.valueOf(t.getValueAt(row, 1)); // "Th1".."Th4"
                    int idx = fir.length() == 3 ? fir.charAt(2) - '1' : -1;
                    c.setBackground(idx >= 0 && idx < 4 ? COLORS[idx] : Color.WHITE);
                }
                return c;
            }
        });
        JScrollPane tablePane = panel("Toate firele (Th1..Th4) - in ordinea in care scriu", table);

        // Jos: rezultatul final + status
        JPanel bottom = new JPanel(new BorderLayout(4, 4));
        bottom.setBorder(BorderFactory.createEmptyBorder(4, 8, 8, 8));
        bottom.add(panel("Rezultat final (ordine: Th2 -> Th4 -> Th1 -> Th3, litere la 100 ms)", resultArea),
                BorderLayout.CENTER);
        bottom.add(statusLabel, BorderLayout.SOUTH);

        JPanel middle = new JPanel(new BorderLayout());
        middle.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        middle.add(tablePane, BorderLayout.CENTER);

        frame.setLayout(new BorderLayout());
        frame.add(top, BorderLayout.NORTH);
        frame.add(middle, BorderLayout.CENTER);
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