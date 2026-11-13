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
    System.out.println("Queue");
  }
}

class Depozit {
  ArrayList<Integer> list = new ArrayList<>();

  public synchronized void put(int value) throws InterruptedException {
    while (list.size() == 8){
      wait();
    }

    list.add(value);
    System.out.println("");
    notifyAll();
  }

  public synchronized ArrayList<Integer> get() throws InterruptedException {
    while (list.isEmpty()) {
      wait();
    }

    int ultimul = list.removeLast();
    int penultimul = list.removeLast();

    ArrayList<Integer> value = new ArrayList<>();
    value.add(penultimul);
    value.add(ultimul);

    System.out.println("Consumatorul " + Thread.currentThread().getName() + " a luat din lista valoarea " + value);
    notifyAll();
    return value;
  }
}

class Producator extends Thread {
}

class Consumator extends Thread {

}
