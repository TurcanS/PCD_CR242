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

  public synchronized void put(int value, int value2) throws InterruptedException {
    while (list.size() == 8) {
      wait();
    }

    list.add(value);
    if (list.size() < 8){
      list.add(value2);
      System.out.println("Producatorul " + Thread.currentThread.getName() + " a produs valorile: " + value + " " + value2);
    }
    else{
      System.out.println("Producatorul " + Thread.currentThread.getName() + " a produs valoarea: " + value);
    }

    notifyAll();
  }

  public synchronized ArrayList<Integer> get() throws InterruptedException {
  }

class Producator extends Thread {
}

class Consumator extends Thread {

}
