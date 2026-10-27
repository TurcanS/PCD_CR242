import javax.swing.*;
import java.io.*;

public class Turcan extends Thread {
  public Turcan(ThreadGroup g, String name) {
    super(g, name);
  }

  public static void main(String[] args) {
    JTextArea area = new JTextArea();
    area.setEditable(false);
    JFrame frame = new JFrame("Turcan Stanislav");
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
    curr.setPriority(2);

    ThreadGroup g1 = new ThreadGroup("G1");
    ThreadGroup g3 = new ThreadGroup(g1, "G3");
    ThreadGroup g2 = new ThreadGroup("G2");

    // G3{Tha(2), Thb(2), Thc(2), Thd(2)}
    Thread t = new Turcan(g3, "Tha");
    t.start();
    t = new Turcan(g3, "Thb");
    t.start();
    t = new Turcan(g3, "Thc");
    t.start();
    t = new Turcan(g3, "Thd");
    t.start();
    g3.list(); // (2)

    // G2{Th1(3), Th2(4), Th3(4)}
    t = new Turcan(g2, "Th1");
    t.setPriority(3);
    t.start();
    t = new Turcan(g2, "Th2");
    t.setPriority(4);
    t.start();
    t = new Turcan(g2, "Th3");
    t.setPriority(4);
    t.start();
    g2.list(); // (3)

    // Th1(6), Th2(6), ThA(2)
    t = new Turcan(sys, "Th1");
    t.setPriority(6);
    t.start();
    t = new Turcan(sys, "Th2");
    t.setPriority(6);
    t.start();
    t = new Turcan(sys, "ThA");
    t.start();

    sys.list(); // (4)
    System.out.println("Start la threaduri: ");
  }

  @Override
  public void run() {
    while (true) {
        try {
          Thread.sleep(100);
        } catch (InterruptedException e) {
        }
        break;
    }
  }
}
