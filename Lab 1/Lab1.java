import java.util.*;

public class Main {
  public static void main(String args[]) {
    Counter1 cnt1_1, cnt1_2, cnt2_1, cnt2_2, cnt3_1, cnt3_2; // cnt 1 Stas, cnt 2 Stefanita, cnt 3 Adrian
    int[] tablou = new int[100]; // Tablou de 100 numbere
    String studenti = "CR-242 Grup 1: Turcan Stanislav, Stefanita David, Spinu Adrian";

    // Fill tablou cu numere random si printeazal
    System.out.print("Numerele din tablou: ");
    for (int i = 0; i < 100; i++) {
      tablou[i] = (int) (Math.random() * 99);
      System.out.print(tablou[i] + " ");
    }

    // Main
    System.out.println(" ");
    cnt1_1 = new Counter1(0, 49, 1, tablou); // Stas
    cnt1_2 = new Counter1(50, 99, 1, tablou);
    cnt2_1 = new Counter1(99, 50, -1, tablou); // Stefanita
    cnt2_2 = new Counter1(49, 0, -1, tablou);
    cnt3_1 = new Counter1(99, 50, -1, tablou, 1); // Adrian
    cnt3_2 = new Counter1(49, 0, -1, tablou, 1);

    cnt1_1.setName("1.1:");
    cnt1_1.start();
    cnt1_2.setName("1.2:");
    cnt1_2.start();
    cnt2_1.setName("2.1:");
    cnt2_1.start();
    cnt2_2.setName("2.2:");
    cnt2_2.start();
    cnt3_1.setName("3.1:");
    cnt3_1.start();
    cnt3_2.setName("3.2:");
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

    // [s]umele [n]umerelor [p]are/impare două câte două începând căutarea si sumarea de la
    // ultimul element
    int snp1 = 0, snp2 = 0, snp = 0;
    int j = from;
    while ((step > 0 && j <= to) || (step < 0 && j >= to)) {
      if (tablou[j] % 2 == rest) {
        snp1 = j;
        j += step;
        if ((step > 0 && j > to) || (step < 0 && j < to)) {
          break;
        }
        do {
          if (tablou[j] % 2 == rest) {
            snp2 = j;
            snp = tablou[snp1] + tablou[snp2];
            System.out.println(getName() + " poz val 1: " + snp1 + " " + tablou[snp1] + ", poz val 2: " + snp2 + " "
                    + tablou[snp2] + ", suma: " + snp);
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