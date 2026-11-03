import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Lucrarea de laborator nr. 2 (PCD) - Gruparea firelor de executie. Prioritati.
 * Varianta 7:
 *   Main{ ThA(3), G2{Th1(5), Th2(3), Th33(7)}, Th11(3), Th22(3),
 *         G1{ G3{Thaa(2), Thbb(3), Thcc(8), Thdd(3)} } }
 *
 * Nivel mediu   : structura se enumera si se afiseaza (nume fir, grup, prioritate);
 *                 metoda run() din Thread NU este suprascrisa.
 * Nivel avansat : clasa Worker suprascrie run(): firul isi afiseaza numele si grupul;
 *                 apoi se extrag prioritatile firelor.
 */
public class Adrian2 {

    // ------------------------------------------------------------------
    // Descrierea declarativa a structurii (arbore de grupuri si fire)
    // ------------------------------------------------------------------
    static final class Node {
        final String name;
        final int priority;          // doar pentru fire
        final boolean isGroup;
        final List<Node> children = new ArrayList<>();

        private Node(String name, int priority, boolean isGroup) {
            this.name = name;
            this.priority = priority;
            this.isGroup = isGroup;
        }

        static Node thread(String name, int priority) {
            return new Node(name, priority, false);
        }

        static Node group(String name, Node... kids) {
            Node n = new Node(name, 0, true);
            n.children.addAll(List.of(kids));
            return n;
        }
    }

    static Node variant7() {
        return Node.group("Main",
                Node.thread("ThA", 3),
                Node.group("G2",
                        Node.thread("Th1", 5),
                        Node.thread("Th2", 3),
                        Node.thread("Th33", 7)),
                Node.thread("Th11", 3),
                Node.thread("Th22", 3),
                Node.group("G1",
                        Node.group("G3",
                                Node.thread("Thaa", 2),
                                Node.thread("Thbb", 3),
                                Node.thread("Thcc", 8),
                                Node.thread("Thdd", 3))));
    }

    // ------------------------------------------------------------------
    // Crearea firelor
    // ------------------------------------------------------------------
    interface Maker {
        Thread make(ThreadGroup group, String name);
    }

    static final int ALIVE_MS = 2000;   // cat timp raman active firele

    static void pause(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }

    /** Nivel mediu: firul primeste un Runnable simplu; run() din Thread ramane neschimbat. */
    static Maker mediumMaker() {
        return (g, name) -> new Thread(g, () -> pause(ALIVE_MS), name);
    }

    /** Nivel avansat: run() este suprascrisa. */
    static class Worker extends Thread {
        Worker(ThreadGroup g, String name) {
            super(g, name);
        }

        @Override
        public void run() {
            log("  > run(): firul " + getName() + " din grupul " + getThreadGroup().getName());
            pause(ALIVE_MS);
        }
    }

    static Maker advancedMaker() {
        return Worker::new;
    }

    static ThreadGroup build(Node spec, ThreadGroup parent, Maker maker, List<Thread> created) {
        ThreadGroup g = (parent == null) ? new ThreadGroup(spec.name)
                                         : new ThreadGroup(parent, spec.name);
        for (Node child : spec.children) {
            if (child.isGroup) {
                build(child, g, maker, created);
            } else {
                Thread t = maker.make(g, child.name);
                t.setDaemon(true);
                t.setPriority(child.priority);
                created.add(t);
            }
        }
        return g;
    }

    // ------------------------------------------------------------------
    // Enumerarea
    // ------------------------------------------------------------------
    static Thread[] threadsOf(ThreadGroup g) {
        Thread[] buf = new Thread[g.activeCount() + 4];
        int n = g.enumerate(buf, false);
        return java.util.Arrays.copyOf(buf, n);
    }

    static ThreadGroup[] groupsOf(ThreadGroup g) {
        ThreadGroup[] buf = new ThreadGroup[g.activeGroupCount() + 4];
        int n = g.enumerate(buf, false);
        return java.util.Arrays.copyOf(buf, n);
    }

    static void report(ThreadGroup g, int depth, boolean advanced, StringBuilder sb) {
        String pad = "    ".repeat(depth);
        sb.append(pad).append("[Grup] ").append(g.getName())
          .append(" (prioritate maxima = ").append(g.getMaxPriority()).append(")\n");
        for (Thread t : threadsOf(g)) {
            sb.append(pad).append("    - ").append(t.getName());
            if (advanced) {
                sb.append("  prioritate = ").append(t.getPriority());
            } else {
                sb.append("  | grup = ").append(t.getThreadGroup().getName())
                  .append("  | prioritate = ").append(t.getPriority());
            }
            sb.append("  | demon = ").append(t.isDaemon());
            sb.append('\n');
        }
        for (ThreadGroup sub : groupsOf(g)) {
            report(sub, depth + 1, advanced, sb);
        }
    }

    static DefaultMutableTreeNode toTree(ThreadGroup g) {
        DefaultMutableTreeNode node =
                new DefaultMutableTreeNode(g.getName() + "  [max=" + g.getMaxPriority() + "]");
        for (Thread t : threadsOf(g)) {
            node.add(new DefaultMutableTreeNode(t.getName() + "  (prio " + t.getPriority() + ")"));
        }
        for (ThreadGroup sub : groupsOf(g)) {
            node.add(toTree(sub));
        }
        return node;
    }

    // ------------------------------------------------------------------
    // Interfata grafica
    // ------------------------------------------------------------------
    private static JTextArea out;
    private static JTree tree;
    private static JButton mediumBtn;
    private static JButton advancedBtn;

    static void log(String s) {
        System.out.println(s);
        if (out != null) {
            SwingUtilities.invokeLater(() -> {
                out.append(s + "\n");
                out.setCaretPosition(out.getDocument().getLength());
            });
        }
    }

    /** Porneste structura; dupa 300 ms (firele sunt active) o enumera. */
    private static void execute(boolean advanced) {
        log(advanced ? "=== NIVEL AVANSAT (fire demon, run() suprascrisa) ==="
                     : "=== NIVEL MEDIU (fire demon, fara run) ===");
        List<Thread> created = new ArrayList<>();
        ThreadGroup root = build(variant7(), null, advanced ? advancedMaker() : mediumMaker(), created);
        for (Thread t : created) {
            t.start();
        }
        pause(300);
        StringBuilder sb = new StringBuilder();
        report(root, 0, advanced, sb);
        log(sb.toString());
        if (tree != null) {
            DefaultMutableTreeNode top = toTree(root);
            SwingUtilities.invokeLater(() -> {
                tree.setModel(new DefaultTreeModel(top));
                for (int i = 0; i < tree.getRowCount(); i++) {
                    tree.expandRow(i);
                }
            });
        }
    }

    private static void startAsync(boolean advanced) {
        mediumBtn.setEnabled(false);
        advancedBtn.setEnabled(false);
        out.setText("");
        // firul care lanseaza structura nu blocheaza interfata
        new Thread(() -> {
            execute(advanced);
            SwingUtilities.invokeLater(() -> {
                mediumBtn.setEnabled(true);
                advancedBtn.setEnabled(true);
            });
        }, "launcher").start();
    }

    private static void buildGui() {
        JFrame f = new JFrame("Lab 2 PCD - ThreadGroup si prioritati (varianta 7)");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        out = new JTextArea();
        out.setEditable(false);
        out.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));

        tree = new JTree(new DefaultMutableTreeNode("(apasa un buton)"));

        mediumBtn = new JButton("Nivel mediu");
        advancedBtn = new JButton("Nivel avansat");
        mediumBtn.addActionListener(e -> startAsync(false));
        advancedBtn.addActionListener(e -> startAsync(true));

        JButton clearBtn = new JButton("Curata");
        clearBtn.addActionListener(e -> {
            out.setText("");
            tree.setModel(new DefaultTreeModel(new DefaultMutableTreeNode("(apasa un buton)")));
        });

        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bar.add(mediumBtn);
        bar.add(advancedBtn);
        bar.add(clearBtn);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                new JScrollPane(tree), new JScrollPane(out));
        split.setDividerLocation(240);

        f.add(bar, BorderLayout.NORTH);
        f.add(split, BorderLayout.CENTER);
        f.setSize(900, 560);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

    public static void main(String[] args) {
        if (GraphicsEnvironment.isHeadless()) {   // rulare in consola, fara fereastra
            execute(false);
            pause(ALIVE_MS);
            execute(true);
            pause(ALIVE_MS);
        } else {
            SwingUtilities.invokeLater(GroupsLab::buildGui);
        }
    }
}