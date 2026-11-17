import javax.swing.*;
import java.io.*;
import java.util.*;

public class Main {
  public static final int X = 2; // X producători
  public static final int Y = 3; // sunt consumate de Y consumator
  public static final int Z = 11; // este îndestulat cu Z obiecte
  public static final int D = 8; // Dimensiunea depozitului este D
  public static final int F = 2; // generează aleatoriu F obiecte

  public static void main(String[] args) {
    JTextArea area = new JTextArea();
    area.setEditable(false);
    JFrame frame = new JFrame("LAB 4 GRUP 1");
    frame.add(new JScrollPane(area));
    frame.setSize(600, 1030);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);

    System.setOut(new PrintStream(new OutputStream() {
      public void write(int b) {
        area.append(String.valueOf((char) b));
      }
    }, true));

    System.out.print("START PROGRAMUL");

    Depozit depozit = new Depozit();

    Producator p1 = new Producator(depozit, "P1");
    Producator p2 = new Producator(depozit, "P2");
    p1.start();
    p2.start();

    Consumator c1 = new Consumator(depozit, "C1");
    Consumator c2 = new Consumator(depozit, "C2");
    Consumator c3 = new Consumator(depozit, "C3");
    c1.start();
    c2.start();
    c3.start();

    try {
      c1.join();
      c2.join();
      c3.join();

      p1.interrupt();
      p2.interrupt();
    } catch (InterruptedException e) {
      System.out.println("A aparut o eroare!!!!");
    }

    System.out.println("PROGRAMUL SA FINALIZAT");
  }
}

class Depozit {
  ArrayList<Integer> list = new ArrayList<>();

  public synchronized void put(int value, int value2) throws InterruptedException {
    while (list.size() + 2 > Main.D) {
      wait();
    }

    list.add(value);
    list.add(value2);

    System.out.println("Producatorul " + Thread.currentThread().getName() + " a produs valorile: " + value + " "
        + value2 + " {" + list.size() + "/8}");

    notifyAll();
  }

  public synchronized int get(int i) throws InterruptedException {
    while (list.isEmpty()) {
      wait();
    }

    int val = list.remove(0);

    System.out.println("Consumatorul " + Thread.currentThread().getName() + " a consumat " + val + " (" + i + "/11)");

    notifyAll();
    return val;
  }
}

class Producator extends Thread {
  Depozit dep;

  public Producator(Depozit dep, String name) {
    super(name);
    this.dep = dep;
  }

  @Override
  public void run() {
    while (!Thread.currentThread().isInterrupted()) {
      int value = (int) (Math.random() * 500) * 2;
      int value2 = (int) (Math.random() * 500) * 2;

      try {
        dep.put(value, value2);
        sleep((int) (Math.random() * 600));
      } catch (InterruptedException e) {
        break;
      }
    }
  }
}

class Consumator extends Thread {
  Depozit dep;
  int val = 0;

  public Consumator(Depozit dep, String name) {
    super(name);
    this.dep = dep;
  }

  @Override
  public void run() {
    for (int i = 1; i <= Main.Z; i++) {
      try {
        val = dep.get(i);
        sleep((int) (Math.random() * 300)); // sleep [0-300]ms
      } catch (InterruptedException e) {
        return;
      }
    }
  }
}
