import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Lucrare de laborator nr. 2 - Gruparea firelor de executie. Prioritati.
 * Varianta 7:
 * Main{ ThA(3), G2{Th1(5), Th2(3), Th33(7)}, Th11(3), Th22(3),
 *       G1{ G3{Thaa(2), Thbb(3), Thcc(8), Thdd(3)} } }
 */
public class Adrian2 {

    // ------------------------------------------------------------------
    // 1. Descrierea structurii (modelul), independenta de Thread/ThreadGroup
    // ------------------------------------------------------------------
    static class Node {
        final String name;
        final boolean isGroup;
        final int priority;               // doar pentru fire
        final List<Node> children = new ArrayList<>();

        private Node(String name, boolean isGroup, int priority) {
            this.name = name;
            this.isGroup = isGroup;
            this.priority = priority;
        }

        static Node group(String name, Node... kids) {
            Node n = new Node(name, true, 0);
            for (Node k : kids) n.children.add(k);
            return n;
        }

        static Node thread(String name, int priority) {
            return new Node(name, false, priority);
        }
    }

    /** Formula variantei 7 din tabelul 1. */
    static Node buildFormula() {
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
    // 2. Fire de executie
    // ------------------------------------------------------------------

    /** Nivel avansat: clasa care SUPRASCRIE run(). */
    static class AdvancedThread extends Thread {
        private final Consumer<String> out;

        AdvancedThread(ThreadGroup group, String name, Consumer<String> out) {
            super(group, name);          // plasare in grup + nume
            this.out = out;
        }

        @Override
        public void run() {
            out.accept("Firul " + getName()
                    + " | grup: " + getThreadGroup().getName()
                    + " | prioritate: " + getPriority());
        }
    }

    /** Creeaza un fir; pentru nivelul mediu se foloseste Thread simplu, fara run(). */
    interface Factory {
        Thread create(ThreadGroup group, String name);
    }

    // ------------------------------------------------------------------
    // 3. Instantierea structurii: Node -> ThreadGroup / Thread reale
    // ------------------------------------------------------------------
    static void instantiate(Node node, ThreadGroup parent, Factory factory,
                            List<Thread> allThreads) {
        if (node.isGroup) {
            ThreadGroup g = (parent == null)
                    ? new ThreadGroup("Main")
                    : new ThreadGroup(parent, node.name);
            for (Node child : node.children) {
                instantiate(child, g, factory, allThreads);
            }
        } else {
            Thread t = factory.create(parent, node.name);
            t.setPriority(node.priority);
            allThreads.add(t);
        }
    }

    // ------------------------------------------------------------------
    // 4. Nivel mediu si nivel avansat
    // ------------------------------------------------------------------

    /**
     * Nivel mediu: firele sunt create (fara run() suprascris), apoi
     * enumerate cu numele, grupul si prioritatea.
     * Firele nu sunt pornite, deci le tinem in lista proprie:
     * ThreadGroup.enumerate() returneaza doar firele active (pornite).
     */
    static void runMedium(Consumer<String> out) {
        out.accept("=== NIVEL MEDIU ===");
        List<Thread> threads = new ArrayList<>();
        instantiate(buildFormula(), null, (g, n) -> new Thread(g, n), threads);
        for (Thread t : threads) {
            out.accept("Fir: " + t.getName()
                    + " | grup: " + t.getThreadGroup().getName()
                    + " | prioritate: " + t.getPriority());
        }
        out.accept("Total fire: " + threads.size());
    }

    /** Nivel avansat: firele cu run() suprascris sunt pornite. */
    static void runAdvanced(Consumer<String> out) {
        out.accept("=== NIVEL AVANSAT ===");
        List<Thread> threads = new ArrayList<>();
        instantiate(buildFormula(), null,
                (g, n) -> new AdvancedThread(g, n, out), threads);
        for (Thread t : threads) t.start();

        // asteptam terminarea intr-un fir separat, ca sa nu blocam interfata
        Thread waiter = new Thread(() -> {
            for (Thread t : threads) {
                try {
                    t.join();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
            out.accept("Toate cele " + threads.size() + " fire s-au terminat.");
        });
        waiter.start();
    }

    // ------------------------------------------------------------------
    // 5. Interfata grafica
    // ------------------------------------------------------------------
    static DefaultMutableTreeNode toTree(Node n) {
        String label = n.isGroup ? n.name + "  [grup]"
                                 : n.name + "  (prioritate " + n.priority + ")";
        DefaultMutableTreeNode t = new DefaultMutableTreeNode(label);
        for (Node c : n.children) t.add(toTree(c));
        return t;
    }

    static void showGui() {
        JFrame frame = new JFrame("Lab 2 - Varianta 7 - Grupuri de fire si prioritati");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTree tree = new JTree(toTree(buildFormula()));
        for (int i = 0; i < tree.getRowCount(); i++) tree.expandRow(i);

        JTextArea console = new JTextArea();
        console.setEditable(false);
        console.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));

        // iesirea poate veni din fire diferite, de aceea o trimitem pe firul Swing
        Consumer<String> out = s -> SwingUtilities.invokeLater(() -> {
            console.append(s + "\n");
            console.setCaretPosition(console.getDocument().getLength());
        });

        JButton medium = new JButton("Nivel mediu");
        JButton advanced = new JButton("Nivel avansat");
        JButton clear = new JButton("Sterge");
        medium.addActionListener(e -> runMedium(out));
        advanced.addActionListener(e -> runAdvanced(out));
        clear.addActionListener(e -> console.setText(""));

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttons.add(medium);
        buttons.add(advanced);
        buttons.add(clear);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                new JScrollPane(tree), new JScrollPane(console));
        split.setDividerLocation(280);

        frame.add(buttons, BorderLayout.NORTH);
        frame.add(split, BorderLayout.CENTER);
        frame.setSize(900, 480);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) throws Exception {
        if (args.length > 0 && args[0].equals("console")) {
            // mod fara interfata grafica, util pentru verificare
            Consumer<String> out = System.out::println;
            runMedium(out);
            runAdvanced(out);
            Thread.sleep(500);
        } else {
            SwingUtilities.invokeLater(Adrian2::showGui);
        }
    }
}