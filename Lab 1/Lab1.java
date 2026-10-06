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
    Stefanita fir1;
    fir1 = new Stefanita(tablou, true);
    fir1.start();
    fir1.setName("Clasa stefanita");
  }
}

class Stefanita extends Thread {
  int[] tablou;
  boolean primul;

  public Stefanita(int[] tablou, boolean primul){
    this.tablou = tablou;
    this.primul = primul;
  }

  public void run(){
    int snpc1 = 0, snpc2 = 0, snpc = 0, snpcTotal = 0, crescator = 0;
    int snpd1 = 0, snpd2 = 0, snpd = 0, snpdTotal = 0, descrescator = 99;
    while (crescator <= 99 || descrescator >= 0){
      if (tablou(crescator) %2 == 0 || primul == true){
        snpc1 = crescator;
        crescator += 1;
        do {
          if (tablou(crescator) %2 == 0){
            snpc2 = crescator;
            snpc = tablou[snpc1] + tablou[snpc2];
            snpcTotal += snpc;
            System.out.println(getName() + " poz val 1: " + snpc1 + " " + tablou[snpc1] + ", poz val 2: " + snpc2 + " " + tablou[snpc2] + ", suma: " + snpc + ", totala: "+ snpcTotal);
            break;
          }
        } while (crescator <= 99 || descrescator >= 0);
      }
    }
  }
}
