import javax.swing.*;
import java.io.*;

public class Lab2_Vleju extends Thread {

  public static Thread[] t = new Thread[10];

  public Lab2_Vleju(ThreadGroup g, String name) {
    super(g, name);
  }

  public static void main(String[] args) {
    JTextArea area = new JTextArea();
    area.setEditable(false);
    JFrame frame = new JFrame("Lab2");
    frame.add(new JScrollPane(area));
    frame.setSize(700, 400);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);

    System.setOut(new PrintStream(new OutputStream() {
      public void write(int b) {
        area.append(String.valueOf((char) b));
      }
    }, true));
    ThreadGroup sys = Thread.currentThread().getThreadGroup();
    sys.setDaemon(true);
    Thread curr = Thread.currentThread();
    curr.setPriority(4);

    ThreadGroup GM = new ThreadGroup("GM");
    ThreadGroup GN = new ThreadGroup("GN");
    ThreadGroup GH = new ThreadGroup(GN, "GH");
    // GM
    t[0] = new Lab2_Vleju(GM, "Th1");
    t[0].setPriority(3);
    t[0].start();

    t[1] = new Lab2_Vleju(GM, "Th2");
    t[1].start();

    t[2] = new Lab2_Vleju(GM, "Th3");
    t[2].start();

    // GN

    t[3] = new Lab2_Vleju(GN, "ThA");
    t[3].start();

    // GH

    t[4] = new Lab2_Vleju(GH, "Tha");
    t[4].setPriority(5);
    t[4].start();

    t[5] = new Lab2_Vleju(GH, "Thb");
    t[5].start();

    t[6] = new Lab2_Vleju(GH, "Thc");
    t[6].setPriority(7);
    t[6].start();

    t[7] = new Lab2_Vleju(GH, "Thd");
    t[7].start();

    // Main

    t[8] = new Lab2_Vleju(sys, "Th2");
    t[8].start();

    t[9] = new Lab2_Vleju(sys, "Th1");
    t[9].setPriority(9);
    t[9].start();

    sys.list();

  }

  @Override
  public void run() {
    for (int i = 0; i < 10; i++) {
      while (t[i].isAlive()) {
      }
    }
  }
}
