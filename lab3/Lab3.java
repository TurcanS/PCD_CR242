
import javax.swing.*;
import java.io.*;

/*
 
2 - nu are sincronizare 
1 - foloseste isAlive sa verifice daca 4 mai ruleaza 
3 - foloseste doar sleep 
4 - asteapta ca 2 sa termine prin join



*/
public class Lab3 {

  public static void main(String[] args) {
    JTextArea area = new JTextArea();
    area.setEditable(false);
    JFrame frame = new JFrame("Lab3");
    frame.add(new JScrollPane(area));
    frame.setSize(700, 400);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);

    System.setOut(new PrintStream(new OutputStream() {
      public void write(int b) {
        area.append(String.valueOf((char) b));
      }
    }, true));
    int[] tablou = new int[2112];

    for (int i = 0; i < 2112; i++) {
      tablou[i] = (int) (Math.random() * 99);
    }
    Sar12 s2 = new Sar12(tablou, 2111, 0, -1);
    Thread[] arr2 = { s2 };
    Sarcina s4 = new Sarcina(tablou, 4, arr2);
    Sar12 s1 = new Sar12(tablou, 0, 2111, 1, s4);
    Thread[] arr = { s2, s1, s4 };
    Sarcina s3 = new Sarcina(tablou, 3, arr);
    s1.setName("Thread 1: ");
    s2.setName("Thread 2: ");
    s3.setName("Thread 3: ");
    s4.setName("Thread 4: ");
    s2.start();

    s4.start();

    s1.start();
    s3.start();
  }
}

class Sarcina extends Thread {
  private int sar;
  private int[] tablou;
  private Thread[] Thr;

  public Sarcina(int[] tablou, int sar, Thread[] Thr) {
    this.sar = sar;
    this.tablou = tablou;
    this.Thr = Thr;
  }

  @Override
  public void run() {
    if (sar == 3) {
      System.out.print("Thread 3:");
      System.out.print("\n");
      for (int i = 0; i <= 798; i++) {
        System.out.print(tablou[i] + " ");
      }

      for (Thread i : Thr) {
        while (i.isAlive()) {
        }
      }
      String disciplina = "Programarea Concurenta si Distribuita";

      System.out.print("\n");
      System.out.print(Thread.currentThread().getName());
      for (int i = 0; i < disciplina.length(); i++) {
        System.out.print(disciplina.charAt(i) + "");
        try {

          Thread.sleep(100);
        } catch (InterruptedException e) {
          System.out.print(e);
        }
      }
    } else if (sar == 4) {
      System.out.print("\n");
      System.out.print(Thread.currentThread().getName());
      for (int i = 2111; i >= 1456; i--) {

        System.out.print(tablou[i] + " ");
      }

      try {
        Thr[0].join();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      System.out.print("\n" + Thread.currentThread().getName());
      String Grupa = "CR-242 ";
      for (int i = 0; i < Grupa.length(); i++) {
        System.out.print(Grupa.charAt(i) + "");
        try {

          Thread.sleep(100);
        } catch (InterruptedException e) {
          System.out.print(e);
        }
      }
    }
  }
}

class Sar12 extends Thread {
  private int from, to, step;
  private int[] tablou;
  private Sarcina sar;

  public Sar12(int[] tablou, int from, int to, int step) {
    this.from = from;
    this.to = to;
    this.tablou = tablou;
    this.step = step;
  }

  public Sar12(int[] tablou, int from, int to, int step, Sarcina sar) {
    this.from = from;
    this.to = to;
    this.tablou = tablou;
    this.step = step;
    this.sar = sar;
  }

  public void run() {
    int suma1 = 0, suma2 = 0, suma = 0;
    int i = from;

    int sum_t = 0;
    while (i != to) {
      if (tablou[i] % 2 == 1) {
        suma1 = i;
        i += step;
        if ((i < to && step < 0) || (i > to && step > 0))
          break;

        do {
          if (tablou[i] % 2 == 1) {
            suma2 = i;
            suma = tablou[suma1] + tablou[suma2];
            System.out.println(
                getName() + ": Suma1=" + tablou[suma1] + " ;  Suma2=" + tablou[suma2] + ";  Suma=" + suma + ".");
            sum_t += suma;
            break;
          }
          i += step;
          if ((i < to && step < 0) || (i > to && step > 0))
            break;
        } while (true);
      }
      i += step;
      if ((i < to && step < 0) || (i > to && step > 0))
        break;

    }

    System.out.println(getName() + " Suma totala: " + sum_t);
    if (step == -1) {
      try {

        Thread.sleep(200);
      } catch (InterruptedException e) {
        System.out.print(e);
      }
      System.out.print("\n" + getName());
      String prenume = "Cemirtan Vleju";
      for (int k = 0; k < prenume.length(); k++) {
        System.out.print(prenume.charAt(k) + "");
        try {

          Thread.sleep(100);
        } catch (InterruptedException e) {
          System.out.print(e);
        }
      }

    }

    else {
      while (sar.isAlive()) {
      }
      System.out.print("\n" + getName());
      String nume = "Edgar Dumitru";
      for (int k = 0; k < nume.length(); k++) {
        System.out.print(nume.charAt(k) + "");
        try {

          Thread.sleep(100);
        } catch (InterruptedException e) {
          System.out.print(e);
        }
      }
    }
  }
}
