
import javax.swing.*;
import java.io.*;

public class Lab2_Vleju extends Thread {

  public Lab2_Vleju(ThreadGroup g, String name) {
    super(g, name);
  }

  public void run() {
    System.out.println("Vleju : Thread: " + getName()
        + ", Grupul: " + getThreadGroup().getParent().getName() + "-" + getThreadGroup().getName()
        + ", Prioritatea:: " + getPriority());

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
    Thread curr = Thread.currentThread();
    curr.setPriority(4);

    ThreadGroup GM = new ThreadGroup("GM");
    ThreadGroup GN = new ThreadGroup("GN");
    ThreadGroup GH = new ThreadGroup(GN, "GH");

    // GM
    Thread t = new Lab2_Vleju(GM, "Th1");
    t.setPriority(3);
    t.start();

    t = new Lab2_Vleju(GM, "Th2");
    t.start();

    t = new Lab2_Vleju(GM, "Th3");
    t.start();

    GM.list();

    // GN

    t = new Lab2_Vleju(GN, "ThA");
    t.start();
    GN.list();

    // GH

    t = new Lab2_Vleju(GH, "Tha");
    t.setPriority(5);
    t.start();

    t = new Lab2_Vleju(GH, "Thb");
    t.start();

    t = new Lab2_Vleju(GH, "Thc");
    t.setPriority(7);
    t.start();

    t = new Lab2_Vleju(GH, "Thd");
    t.start();

    GH.list();
    // Main

    t = new Lab2_Vleju(sys, "Th2");
    t.start();

    t = new Lab2_Vleju(sys, "Th1");
    t.setPriority(9);
    t.start();

    sys.list();

  }
}
