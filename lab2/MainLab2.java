import javax.swing.*;
import java.io.*;

public class MainLab2 extends Thread {
    public MainLab2(ThreadGroup grupa, String nume) {
        super(grupa, nume);
        setDaemon(true);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Grupe si fire Daemon");
        JTextArea area = new JTextArea();
        area.setEditable(false);
        frame.add(new JScrollPane(area));
        frame.setSize(700, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        System.setOut(new PrintStream(new OutputStream() {
            public void write(int b) {
                SwingUtilities.invokeLater(() ->
                        area.append(String.valueOf((char) b))
                );
            }
        }, true));

        ThreadGroup Main = new ThreadGroup("Main");
        ThreadGroup GE = new ThreadGroup(Main, "GE");
        ThreadGroup GH = new ThreadGroup(GE, "GH");
        ThreadGroup GK = new ThreadGroup(Main, "GK");
        Thread.currentThread().setPriority(4);

        MainLab2 firTh1Main = new MainLab2(Main, "Th1");
        firTh1Main.setPriority(Thread.currentThread().getPriority());
        firTh1Main.start();

        MainLab2 firTha = new MainLab2(GH, "Tha");
        firTha.setPriority(5);
        firTha.start();

        MainLab2 firThb = new MainLab2(GH, "Thb");
        firThb.setPriority(Thread.currentThread().getPriority());
        firThb.start();

        MainLab2 firThc = new MainLab2(GH, "Thc");
        firThc.setPriority(3);
        firThc.start();

        MainLab2 firThd = new MainLab2(GH, "Thd");
        firThd.setPriority(2);
        firThd.start();

        MainLab2 firThA = new MainLab2(GE, "ThA");
        firThA.setPriority(Thread.currentThread().getPriority());
        firThA.start();

        MainLab2 firTh1GK = new MainLab2(GK, "Th1");
        firTh1GK.setPriority(Thread.currentThread().getPriority());
        firTh1GK.start();

        MainLab2 firTh2GK = new MainLab2(GK, "Th2");
        firTh2GK.setPriority(7);
        firTh2GK.start();

        MainLab2 firTh3GK = new MainLab2(GK, "Th3");
        firTh3GK.setPriority(Thread.currentThread().getPriority());
        firTh3GK.start();

        MainLab2 firTh2Main = new MainLab2(Main, "Th2");
        firTh2Main.setPriority(8);
        firTh2Main.start();

        System.out.println("\nGrupul MAIN:");
        Main.list();
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
