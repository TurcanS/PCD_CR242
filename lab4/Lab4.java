import java.util.ArrayDeque;
import java.util.Deque;

public class Lab4 {
  public static void main(String[] args) {

  }
}

class Depozit {
  Deque<Character> dep = new ArrayDeque<>();

  public synchronized void alimentare(String name, Character c1, Character c2) {
    while (dep.size() >= 10) {
      try {
        wait();
      } catch (InterruptedException e) {
      }

    }
    System.out.print(name + "a adaugat in depozit doua vocale: ");
    dep.add(c1);
    if (dep.size() != 10) {
      dep.add(c2);
    }

  }
}
