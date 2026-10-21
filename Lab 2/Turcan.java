public class Turcan extends Thread {

  public Turcan(ThreadGroup g, String name) {
    super(g, name);
  }

  public void run() {
    System.out.println("Turcan Stanislav: Thread: " + getName()
        + ", Grupul: " + getThreadGroup().getParent().getName() + "-" + getThreadGroup().getName()
        + ", Prioritatea:: " + getPriority());
  }

  public static void main(String[] args) {
    ThreadGroup sys = Thread.currentThread().getThreadGroup();

    Thread curr = Thread.currentThread();
    curr.setPriority(2);

    ThreadGroup g1 = new ThreadGroup("G1");
    g1.setMaxPriority(Thread.MAX_PRIORITY);
    ThreadGroup g3 = new ThreadGroup(g1, "G3");
    g3.setMaxPriority(Thread.MAX_PRIORITY);
    ThreadGroup g2 = new ThreadGroup("G2");
    g2.setMaxPriority(Thread.MAX_PRIORITY);

    // G3{Tha(3), Thb(3), Thc(3), Thd(3)}
    Thread t = new Turcan(g3, "Tha");
    t.start();
    t = new Turcan(g3, "Thb");
    t.start();
    t = new Turcan(g3, "Thc");
    t.start();
    t = new Turcan(g3, "Thd");
    t.start();
    g3.list(); // (2)

    // G2{Th1(4), Th2(5), Th3(5)}
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

    // Th1(7), Th2(7), ThA(3)
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
}
