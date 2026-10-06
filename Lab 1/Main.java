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
    fir1 = new Stefanita(tablou, 0, true, true);
    fir2 = new Stefanita(tablou, 99, false, true);
    fir3 = new TurcanS(0, 99, 1, tablou);
    fir4 = new TurcanS(99, 0, -1, tablou);
    fir1.setName("Stefanita cresc");
    fir2.setName("Stefanita desc");
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
