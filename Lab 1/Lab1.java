import java.util.*;

public class Main {
  public static void main(String args[]) {
    Counter1 cnt1_1, cnt1_2, cnt2_1, cnt2_2, cnt3_1, cnt3_2;
    int[] tablou = new int[100];
    String studenti = "CR-242 Grup 1: Turcan Stanislav, Stefanita David, Spinu Adrian";

    System.out.print("Numerele din tablou: ");
    for (int i = 0; i < 100; i++) {
      tablou[i] = (int) (Math.random() * 99);
      System.out.print(tablou[i] + " ");
    }

    // Main
    System.out.println(" ");
    cnt1_1 = new Counter1(0, 99, 1, tablou); // Stas
    cnt1_2 = new Counter1(99, 0, -1, tablou);
    cnt2_1 = new Counter1(0, 99, 1, tablou); // Stefanita
    cnt2_2 = new Counter1(99, 0, -1, tablou);
    cnt3_1 = new Counter1(0, 99, 1, tablou, 1); // Adrian
    cnt3_2 = new Counter1(99, 0, -1, tablou, 1);

    cnt1_1.setName("Stas cond1:");
    cnt1_1.start();
    cnt1_2.setName("Stas cond2:");
    cnt1_2.start();
    cnt2_1.setName("Stefanita cond1:");
    cnt2_1.start();
    cnt2_2.setName("Stefanita cond2:");
    cnt2_2.start();
    cnt3_1.setName("Adrian cond1:");
    cnt3_1.start();
    cnt3_2.setName("Adrian cond2:");
    cnt3_2.start();

    try {
      cnt1_1.join();
      cnt1_2.join();
      cnt2_1.join();
      cnt2_2.join();
      cnt3_1.join();
      cnt3_2.join();

      for (int i = 0; i < studenti.length(); i++) {
        System.out.print(studenti.charAt(i));
        Thread.sleep(100);
      }

      System.out.println();

    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}

class Counter1 extends Thread {
  private int from, to, step;
  private int[] tablou;
  private int rest;


  public Counter1(int from, int to, int step, int[] tablou) {
    this(from, to, step, tablou, 0);
  }


  public Counter1(int from, int to, int step, int[] tablou, int rest) {
    this.from = from;
    this.to = to;
    this.step = step;
    this.tablou = tablou;
    this.rest = rest;
  }

  public void run() {
    // sumele numerelor pare/impare două câte două
    int sumaNR1 = 0, sumaNR2 = 0, sumaNR = 0;
    int j = from;
    while ((step > 0 && j <= to) || (step < 0 && j >= to)) {
      if (tablou[j] % 2 == rest) {
        sumaNR1 = j;
        j += step;
        if ((step > 0 && j > to) || (step < 0 && j < to)) {
          break;
        }
        do {
          if (tablou[j] % 2 == rest) {
            sumaNR2 = j;
            sumaNR = tablou[sumaNR1] + tablou[sumaNR2];
            System.out.println(getName() + " poz val 1: " + sumaNR1 + " " + tablou[sumaNR1] + ", poz val 2: " + sumaNR2 + " " + tablou[sumaNR2] + ", suma: " + sumaNR);
            break;
          }
          j += step;
          if ((step > 0 && j > to) || (step < 0 && j < to)) {
            break;
          }
        } while (true);
      }
      j += step;
      if ((step > 0 && j > to) || (step < 0 && j < to)) {
        break;
      }
    }
  }
}
