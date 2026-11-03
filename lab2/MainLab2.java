import javax.swing.*;

public class MainLab2 extends Thread {
    public MainLab2(ThreadGroup g, String name) {
        super(g, name);
        setDaemon(true);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Grupe si fire Daemon");
        JTextArea area = new JTextArea();
        area.setEditable(false);
        frame.add(new JScrollPane(area));
        frame.setSize(700, 450);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        ThreadGroup Main = new ThreadGroup("Main");
        ThreadGroup GE = new ThreadGroup(Main, "GE");
        ThreadGroup GH = new ThreadGroup(GE, "GH");
        ThreadGroup GK = new ThreadGroup(Main, "GK");

        Thread t;
        t = new MainLab2(Main, "Th1");
        t.setPriority(4);
        t.start();

        t = new MainLab2(GH, "Tha");
        t.setPriority(5);
        t.start();

        t = new MainLab2(GH, "Thb");
        t.setPriority(4);
        t.start();

        t = new MainLab2(GH, "Thc");
        t.setPriority(3);
        t.start();

        t = new MainLab2(GH, "Thd");
        t.setPriority(2);
        t.start();

        t = new MainLab2(GE, "ThA");
        t.setPriority(4);
        t.start();

        t = new MainLab2(GK, "Th1");
        t.setPriority(4);
        t.start();

        t = new MainLab2(GK, "Th2");
        t.setPriority(7);
        t.start();

        t = new MainLab2(GK, "Th3");
        t.setPriority(4);
        t.start();

        t = new MainLab2(Main, "Th2");
        t.setPriority(8);
        t.start();

        area.append("ENUMERAREA FIRELOR DE EXECUTIE\n\n");

        Thread[] fire = new Thread[20];
        int numar = Main.enumerate(fire, true);

        for (int i = 0; i < numar; i++) {
            area.append("Nume: " + fire[i].getName() + "\n");
            area.append("Grupa: " + fire[i].getThreadGroup().getName() + "\n");
            area.append("Prioritate: " + fire[i].getPriority() + "\n");
            area.append("Daemon: " + fire[i].isDaemon() + "\n\n");
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(100);
            } catch (Exception e) {
            }
        }
    }
}
