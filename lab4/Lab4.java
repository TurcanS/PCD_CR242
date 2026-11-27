import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
// Producatori - 4 
// Consumatori - 3 
// Obiecte per consumator - 3
// Depozitul - 10 

public class Lab4 {
  public static void main(String[] args) {
    Depozit depozit = new Depozit();
    Producator p1 = new Producator(depozit);
    Producator p2 = new Producator(depozit);
    Producator p3 = new Producator(depozit);
    Producator p4 = new Producator(depozit);

    Consumator c1 = new Consumator(depozit);
    Consumator c2 = new Consumator(depozit);
    Consumator c3 = new Consumator(depozit);

    p1.start();
    p2.start();
    p3.start();
    p4.start();

    c1.start();
    c2.start();
    c3.start();

    try {
      p1.join();
      p2.join();
      p3.join();
      p4.join();

      c1.join();
      c2.join();
      c3.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.print(" PROGRAMUL S-A FINISAT");
  }
}

class Depozit {
  Deque<Character> dep = new ArrayDeque<>();

  public synchronized void alimentare(Character c1, Character c2) {
    while (dep.size() >= 10) {
      try {
        wait();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

    }
    dep.add(c1);
    if (dep.size() != 10) {

      dep.add(c2);

      System.out
          .println("Producatorul " + Thread.currentThread().getName() + " a depus " + c1 + " , " + c2 + " in depozit");
    } else {

      System.out
          .println(
              "Producatorul " + Thread.currentThread().getName() + " a depus doar " + c1 + " in depozit");
    }
    notifyAll();

  }

  public synchronized Character vinzare() {

    while (dep.isEmpty()) {
      try {
        wait();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

    }
    Character val = dep.pop();
    notifyAll();
    return val;
  }
}

class Producator extends Thread {
  private Depozit depozit;
  private int items = 0;
  private List<Character> consoane = List.of('b', 'c', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'm', 'n',
      'p', 'q', 'r', 's', 't', 'v', 'w', 'x', 'y', 'z');
  Character c1;
  Character c2;

  public Producator(Depozit dep) {
    this.depozit = dep;
  }

  @Override
  public void run() {
    while (items < 3) {
      c1 = consoane.get(ThreadLocalRandom.current().nextInt(consoane.size()));
      c2 = consoane.get(ThreadLocalRandom.current().nextInt(consoane.size()));
      depozit.alimentare(c1, c2);
      items++;

      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
      }
    }
  }

}

class Consumator extends Thread {
  Depozit depozit;
  int counter = 0;

  public Consumator(Depozit dep) {
    this.depozit = dep;
  }

  @Override
  public void run() {
    while (counter < 3) {

      System.out.println("Consumatorul " + getName() + " a Consumat: " + depozit.vinzare());
      counter++;
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
      }

    }
  }
}
