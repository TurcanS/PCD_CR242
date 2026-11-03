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
                SwingUtilities.invokeLater(() -> area.append(String.valueOf((char) b))
                );
            }
        }, true));

        ThreadGroup Main = new ThreadGroup("Main");
        ThreadGroup GE = new ThreadGroup(Main, "GE");
        ThreadGroup GH = new ThreadGroup(GE, "GH");
        ThreadGroup GK = new ThreadGroup(Main, "GK");

        MainLab2 firTh1Main = new MainLab2(Main, "Th1");
        firTh1Main.setPriority(4);
        firTh1Main.start();

        MainLab2 firTha = new MainLab2(GH, "Tha");
        firTha.setPriority(5);
        firTha.start();

        MainLab2 firThb = new MainLab2(GH, "Thb");
        firThb.setPriority(4);
        firThb.start();

        MainLab2 firThc = new MainLab2(GH, "Thc");
        firThc.setPriority(3);
        firThc.start();

        MainLab2 firThd = new MainLab2(GH, "Thd");
        firThd.setPriority(2);
        firThd.start();

        MainLab2 firThA = new MainLab2(GE, "ThA");
        firThA.setPriority(4);
        firThA.start();

        MainLab2 firTh1GK = new MainLab2(GK, "Th1");
        firTh1GK.setPriority(4);
        firTh1GK.start();

        MainLab2 firTh2GK = new MainLab2(GK, "Th2");
        firTh2GK.setPriority(7);
        firTh2GK.start();

        MainLab2 firTh3GK = new MainLab2(GK, "Th3");
        firTh3GK.setPriority(4);
        firTh3GK.start();

        MainLab2 firTh2Main = new MainLab2(Main, "Th2");
        firTh2Main.setPriority(8);
        firTh2Main.start();

        System.out.println("Grupu GH ");
        GH.list();
        System.out.println("\nGrupu GE ");
        GE.list();
        System.out.println("\nGrupu GK ");
        GK.list();
        System.out.println("\nGrupu MAIN ");
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