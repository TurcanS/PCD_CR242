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
    System.out.println("Array");

    Depozit depozit = new Depozit();

    for (int i = 0; i < Y; i++){
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
    while (list.size() == 8) {
      wait();
    }

    list.add(value);
    if (list.size() < 8) {
      list.add(value2);
      System.out
          .println("Producatorul " + Thread.currentThread().getName() + " a produs valorile: " + value + " " + value2);
    } else {
      System.out.println("Producatorul " + Thread.currentThread().getName() + " a produs valoarea: " + value);
    }

    notifyAll();
  }

  public synchronized int get() throws InterruptedException {
    while (true){
      int val = (0 + (int)(Math.random() * ((999 - 0) + 1)));
      if (val % 2 == 0) {
      return val;
      }
    }
  }
}

class Producator extends Thread {
}

class Consumator extends Thread {
  Depozit dep;

  public Consumator (Depozit dep){
    this.dep = dep;
  }
  int val = 0;
  @Override
  public void run(){
    for (int i = 0; i <= 11; i++){
      try {
        val = dep.get();
        sleep(50);
      } catch (InterruptedException e){
        System.out.println(e);
      }
      System.out.println("Consumatorul " + getName() + " a consumat " + val + " ("+i+"/11)");
    }
  }
}
