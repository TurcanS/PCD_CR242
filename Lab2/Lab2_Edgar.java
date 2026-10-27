import java.awt.*;
import javax.swing.*;

public class Lab2_Edgar extends JFrame {

    private JTextArea terminal;
    private JButton startButton;

    public Lab2_Edgar() {
        setTitle("Cemirtan Edgar");
        setSize(750, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        terminal = new JTextArea();
        terminal.setEditable(false);
        terminal.setBackground(Color.BLACK);
        terminal.setForeground(Color.GREEN);
        terminal.setCaretColor(Color.GREEN);
        terminal.setFont(new Font("Monospaced", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(terminal);

        startButton = new JButton("Porneste executia");
        startButton.addActionListener(e -> {
            startButton.setEnabled(false);
            terminal.setText("");

            new Thread(() -> executeThreads()).start();
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(startButton);

        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void executeThreads() {

        ThreadGroup main = new ThreadGroup("Main");
        ThreadGroup gn = new ThreadGroup(main, "GN");
        ThreadGroup gh = new ThreadGroup(gn, "GH");
        ThreadGroup gm = new ThreadGroup(main, "GM");

        print("=== EXECUTIA FIRELOR ===\n");

        Thread current = Thread.currentThread();
        current.setPriority(3);

        createThread(gh, "Tha", 4);
        createThread(gh, "Thb");
        createThread(gh, "Thc", 6);
        createThread(gh, "Thd");

        createThread(gn, "ThA");

        createThread(gm, "Th1", 2);
        createThread(gm, "Th2");
        createThread(gm, "Th3");

        createThread(main, "Th1", 8);
        createThread(main, "Th2");

        print("=== EXECUTIA S-A TERMINAT ===");

        SwingUtilities.invokeLater(() ->
            startButton.setEnabled(true)
        );
    }

    private void createThread(ThreadGroup group, String name) {
    createThread(group, name, Thread.currentThread().getPriority());
    }

    private void createThread(
            ThreadGroup group, String name, int initialPriority) {

        Thread thread = new Thread(group, name) {
            @Override
            public void run() {

                int initial = getPriority();

                int reduced = Math.max(
                    Thread.MIN_PRIORITY,
                    initial - 1
                );

                setPriority(reduced);

                String rezultat =
                    "Fir: " + getName()
                    + " | Grup: " + getThreadGroup().getParent().getName() + "-" +getThreadGroup().getName()
                    + "\nPrioritate initiala: " + initial
                    + "\nPrioritate dupa reducere: " + getPriority()
                    + "\n--------------------------------";

                print(rezultat);
            }
        };

        if (initialPriority != 3) {
        thread.setPriority(initialPriority);
        }
        thread.start();

        try {
            thread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void print(String text) {
        SwingUtilities.invokeLater(() -> {
            terminal.append(text + "\n");
            terminal.setCaretPosition(
                terminal.getDocument().getLength()
            );
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Lab2_Edgar app = new Lab2_Edgar();
            app.setVisible(true);
        });
    }
}
