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
    System.out.println("START PROGRAMUL");

    Depozit depozit = new Depozit();

    for (int i = 0; i < X; i++) {
      Producator p = new Producator(depozit);
      p.setName("ProducatorThread-" + i);
      p.start();
    }

    for (int i = 0; i < Y; i++) {
      Consumator p = new Consumator(depozit);
      String nume = "ConsumatorThread-" + i;
      p.setName(nume);
      p.start();
    }

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

    System.out.println(
        "Producatorul " + Thread.currentThread().getName()
            + " a produs valorile: " + value + " " + value2);

    notifyAll();
  }

  public synchronized int get() throws InterruptedException {
    while (list.isEmpty()) {
      wait();
    }

    int val = list.remove(0);
    notifyAll();
    return val;
  }
}

class Producator extends Thread {
  Depozit dep;

  public Producator(Depozit dep) {
    this.dep = dep;
  }

  @Override
  public void run() {
    for (int i = 0; i < Main.Z; i++) {
      int value = (int) (Math.random() * 1000);
      int value2 = (int) (Math.random() * 1000);

      try {
        dep.put(value, value2);
        sleep(50);

      } catch (InterruptedException e) {
        System.out.println(e);
        return;
      }
    }
  }
}

class Consumator extends Thread {
  Depozit dep;

  public Consumator(Depozit dep) {
    this.dep = dep;
  }

  int val = 0;

  @Override
  public void run() {
    for (int i = 1; i <= Main.Z; i++) {
      try {
        val = dep.get();
        sleep(50);
      } catch (InterruptedException e) {
        System.out.println(e);
      }
      System.out.println("Consumatorul " + getName() + " a consumat " + val + " (" + i + "/11)");
    }
  }
}
