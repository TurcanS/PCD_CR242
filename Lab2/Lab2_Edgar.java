
import javax.swing.*;

public class Lab2_Edgar extends Thread {

    static JTextArea area = new JTextArea();

    public Lab2_Edgar(ThreadGroup g, String name) {
        super(g, name);
    }

    @Override
    public void run() {
        int initial = getPriority();
        setPriority(initial - 1);

        String text = "Fir: " + getName()
                + " | Grup: " + getThreadGroup().getParent().getName() + "-" + getThreadGroup().getName()
                + "\nPrioritate initiala: " + initial
                + "\nPrioritate dupa reducere: " + getPriority()
                + "\n----------------------\n";

        SwingUtilities.invokeLater(() -> area.append(text));
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Cemirtan Edgar");
        area.setEditable(false);
        frame.add(new JScrollPane(area));
        frame.setSize(750, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        Thread cur = new Thread(() -> {
            Thread.currentThread().setPriority(3);

            ThreadGroup main = new ThreadGroup("Main");
            ThreadGroup GN = new ThreadGroup(main, "GN");
            ThreadGroup GH = new ThreadGroup(GN, "GH");
            ThreadGroup GM = new ThreadGroup(main, "GM");

            try {
                Thread t = new Lab2_Edgar(GH, "Tha");
                t.setPriority(4);
                t.setDaemon(false);
                t.start();
                t.join();

                t = new Lab2_Edgar(GH, "Thb");
                t.setDaemon(false);
                t.start();
                t.join();

                t = new Lab2_Edgar(GH, "Thc");
                t.setPriority(6);
                t.setDaemon(false);
                t.start();
                t.join();

                t = new Lab2_Edgar(GH, "Thd");
                t.setDaemon(false);
                t.start();
                t.join();

                t = new Lab2_Edgar(GN, "ThA");
                t.setDaemon(false);
                t.start();
                t.join();

                t = new Lab2_Edgar(GM, "Th1");
                t.setPriority(2);
                t.setDaemon(false);
                t.start();
                t.join();

                t = new Lab2_Edgar(GM, "Th2");
                t.setDaemon(false);
                t.start();
                t.join();

                t = new Lab2_Edgar(GM, "Th3");
                t.setDaemon(false);
                t.start();
                t.join();

                t = new Lab2_Edgar(main, "Th1");
                t.setPriority(8);
                t.setDaemon(false);
                t.start();
                t.join();

                t = new Lab2_Edgar(main, "Th2");
                t.setDaemon(false);
                t.start();
                t.join();

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        cur.setDaemon(true);
        cur.start();
    }
}
