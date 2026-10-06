import java.util.*;

public class Main {
  public static void main(String args[]) {
    int[] tablou = new int[100];
    String studenti = "CR-242 Grup 1: Turcan Stanislav, Stefanita David, Spinu Adrian";
    System.out.print("Numerele din tablou: ");
    for (int i = 0; i < 100; i++) {
      tablou[i] = (int) (Math.random() * 99);
      System.out.print(tablou[i] + " ");
    }
    Stefanita fir1, fir2;
    TurcanS fir3, fir4;
    fir1 = new Stefanita(tablou, true);
    fir2 = new Stefanita(tablou, false);
    fir3 = new TurcanS(0, 99, 1, tablou);
    fir4 = new TurcanS(99, 0, -1, tablou);
    fir1.setName("cresc");
    fir2.setName("desc");
    fir3.setName("Stas Cresc");
    fir4.setName("Stas Desc");
    fir1.start();
    fir2.start();
    fir3.start();
    fir4.start();

    try {
      fir1.join();
      fir2.join();
      fir3.join();
      fir4.join();

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

class Stefanita extends Thread {
  int[] tablou;
  boolean primul;

  public Stefanita(int[] tablou, boolean primul) {
    this.tablou = tablou;
    this.primul = primul;
  }

  public void run() {
    int snpc1 = 0, snpc2 = 0, snpc = 0, snpcTotal = 0, crescator = 0;
    int snpd1 = 0, snpd2 = 0, snpd = 0, snpdTotal = 0, descrescator = 99;
    if (primul == true) {
      while (crescator <= 99) {
        if (tablou[crescator] % 2 == 0 || primul == true) {
          snpc1 = crescator;
          crescator += 1;
          do {
            if (tablou[crescator] % 2 == 0 || primul == true) {
              snpc2 = crescator;
              snpc = tablou[snpc1] + tablou[snpc2];
              snpcTotal += snpc;
              System.out.println(getName() + " poz val 1: " + snpc1 + " " + tablou[snpc1] + ", poz val 2: " + snpc2
                  + " " + tablou[snpc2] + ", suma: " + snpc + ", total: " + snpcTotal);
              break;
            }
            crescator += 1;
          } while (crescator <= 99);
        }
        crescator += 1;
      }
    }

    if (primul == false) {
      while (descrescator > 0) {
        if (tablou[descrescator] % 2 == 0) {
          snpd1 = descrescator;
          descrescator -= 1;
          // System.out.println(descrescator);
          do {
            if (tablou[descrescator] % 2 == 0) {
              // System.out.println("if");
              snpd2 = descrescator;
              snpd = tablou[snpd1] + tablou[snpd2];
              snpdTotal += snpd;
              System.out.println(getName() + " poz val 1: " + snpd1 + " " + tablou[snpd1] + ", poz val 2: " + snpd2
                  + " " + tablou[snpd2] + ", suma: " + snpd + ", total: " + snpdTotal);
              break;
            }
            descrescator -= 1;
          } while (descrescator > 0);
        }
        descrescator -= 1;
      }
    }
  }
}
