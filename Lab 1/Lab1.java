import java.util.*;

public class Main {
  public static void main(String args[]) {
    Counter1 cnt1, cnt2, cnt3; // cnt 1 Stas, cnt 2 Stefanita, cnt 3 Adrian
    int[] tablou = new int[100]; // Tablou de 100 numbere
    String studenti = "Turcan Stanislav, Stefanita David, Spinu Adrian";
    // Fill tablou cu numere random si printeazal
    System.out.print("Numerele din tablou: ");
    for (int i = 0; i < 100; i++) {
      tablou[i] = (int) (Math.random() * 99);
      System.out.print(tablou[i] + " ");
    }

    // Main
    System.out.println(" ");
    cnt1 = new Counter1(0, 99, 1, tablou); // Stas
    cnt2 = new Counter1(99, 0, -1, tablou); // Stefanita
    // cnt3 = new Counter1(0, 99, 1, tablou); //Adrian

    cnt1.setName("Unu");
    cnt1.start();
    cnt2.setName("Doi");
    cnt2.start();
    // cnt3.start();
    // cnt3.setName("Trei");

    try {
      cnt1.join();
      cnt2.join();

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

  public Counter1(int from, int to, int step, int[] tablou) {
    this.from = from;
    this.to = to;
    this.step = step;
    this.tablou = tablou;
  }

  public void run() {

    // [s]umele [n]umerelor [p]are două câte două începând căutarea si sumarea de la
    // ultimul element
    int snp1 = 0, snp2 = 0, snp = 0;
    int j = from;
    while ((step > 0 && j <= to) || (step < 0 && j >= to)) {
      if (tablou[j] % 2 == 0) {
        snp1 = j;
        j += step;
        if ((step > 0 && j > to) || (step < 0 && j < to)) {
          break;
        }
        do {
          if (tablou[j] % 2 == 0) {
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

    // Ce face exemplu:
    // 1. Cat timp pozitia nu a ajus la sfarsit executa pasul 2.
    // 2. Daca valoarea din tablou <=50 asigneaza pozitia valoarii in s1,
    // incrementeaza pozitia cu un step si executa pasul 3.
    // Daca >50 incrementeaza pozitia cu un step, inapoi la pasul 1.
    // 3. Daca valoarea din tablou <=50 asigneaza pozitia valoarii in s2 si sumeaza
    // s1+s2 in s. Printeaza valorile, incrementeaza pozitia cu un step, executa
    // pasul 2.
    // Daca >50 incrementeaza pozitia cu un step, inapoi la pasul 3.

    /*
     * int s1=0, s2=0, s=0;
     * int i=from;
     * while(i <= to){
     * if(tablou[i]<=50) {
     * s1=i;
     * i+=step;
     * do {
     * if(tablou[i]<=50) {
     * s2=i;
     * s=s1+s2;
     * System.out.println(getName()+" S1:" + s1+ " S2:" + s2 +" Suma:"+s+ " Ts1:"+
     * tablou[s1]+" Ts2:"+tablou[s2] );
     * break;
     * }
     * i+=step;
     * } while(true);
     * }
     * i+=step;
     * }
     */
  }
}
