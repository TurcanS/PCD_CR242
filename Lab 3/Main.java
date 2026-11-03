/*
 * 5 - Print "Salut colegi" - Adrian sleep()
 * 2 -numele - Stas sleep interrupt
 * 4- grupa - David join()
 * 1 - familile - Stas yield()
 * 6 - "Realizarea lucrarea de laborator pentru" - Adrian join()
 * 3 - Disciplina - David yield()
 */

import javax.swing.*;
import java.io.*;
import java.util.concurrent.ThreadLocalRandom;

public class Main {
  public static void main(String[] args) {

    JTextArea area = new JTextArea();
    area.setEditable(false);
    JFrame frame = new JFrame("Turcan Stanislav");
    frame.add(new JScrollPane(area));
    frame.setSize(700, 400);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);

    System.setOut(new PrintStream(new OutputStream() {
      public void write(int b) {
        area.append(String.valueOf((char) b));
      }
    }, true));

    // generates random numbers actually
    int[] a = new int[100];
    for (int i = 0; i < a.length; i++) {
      a[i] = ThreadLocalRandom.current().nextInt(0, 101); // 0–100 inclusive
    }

    int[] intervalS3 = new int[201];
    int idx = 0;
    for (int i = 100; i <= 300; i++)
      intervalS3[idx++] = i;

    StringBuilder sb = new StringBuilder("Tablou a[100]:\n");
    for (int i = 0; i < a.length; i++) {
      sb.append(a[i]).append(' ');
      if ((i + 1) % 30 == 0)
        sb.append('\n');
    }

    sb.append("\n\n");
    String head = sb.toString();
    System.out.print(head);

    ThreadS1_S2 t2 = new ThreadS1_S2(a, "David, Stas, Adrian", null, 99, 0, -1);
    ThreadS4 t4 = new ThreadS4(t2, "CR-242");
    ThreadS1_S2 t1 = new ThreadS1_S2(a, "Stefanita, Turcan, Spinu", t4, 0, 99, 1);
    ThreadS5 t5 = new ThreadS5("Salut colegi", t2);
    ThreadS6 t6 = new ThreadS6("Realizarea lucrarii de laborator pentru", t1);
    ThreadS3 t3 = new ThreadS3(intervalS3, "Programarea Concurenta Distribuita", t6);

    t1.setName("Th1");
    t2.setName("Th2");
    t3.setName("Th3");
    t4.setName("Th4");
    t5.setName("Th5");
    t6.setName("Th6");

    t5.start();
    t2.start();
    t4.start();
    t1.start();
    t6.start();
    t3.start();
  }
}

class ThreadS1_S2 extends Thread {
  private int start, stop, pas;
  private int[] tablou;
  private String text;
  private Thread thread;

  public ThreadS1_S2(int[] tablou, String text, Thread thread, int start, int stop, int pas) {
    this.start = start;
    this.stop = stop;
    this.pas = pas;
    this.tablou = tablou;
    this.text = text;
    this.thread = thread;
  }

  @Override
  public void run() {
    int sumFin = 0;
    int primPar = -1;
    int sumaPrecedenta = -1;
    int nrSume = 0;

    for (int pozitie = start; pozitie != stop + pas; pozitie += pas) {
      if (tablou[pozitie] % 2 != 0) {
        continue;
      }

      if (primPar == -1) {
        primPar = pozitie;
      } else {
        int suma = tablou[primPar] + tablou[pozitie];
        sumFin += suma;

        System.out.println(Thread.currentThread().getName() + " poz val 1: " + primPar + " " + tablou[primPar]
            + ", poz val 2 complet: " + pozitie + " " + tablou[pozitie] + ", suma: " + suma);

        nrSume++;

        if (sumaPrecedenta == -1) {
          sumaPrecedenta = suma;
        } else {
          int sumaSume = sumaPrecedenta + suma;

          System.out.println(
              Thread.currentThread().getName() + " suma " + (nrSume - 1) + " + suma " + nrSume + " = " + sumaSume);
          sumaPrecedenta = -1;
        }
        primPar = -1;
      }
    }
    System.out.println("Suma totala " + Thread.currentThread().getName() + ": " + sumFin);

    if (thread != null) {
      while (thread.isAlive()) {
        Thread.yield();
      }
    } else {
      while (true) {
        try {
          Thread.sleep(100);
        } catch (Exception e) {
          break;
        }
      }
    }

    System.out.print("\n" + getName() + " - ");
    for (int i = 0; i < text.length(); i++) {
      System.out.print(text.charAt(i));
      try {
        Thread.sleep(100);
      } catch (Exception e) {
      }
    }
    System.out.println();
  }
}

class ThreadS3 extends Thread {
  private int[] interval;
  private String disciplina;
  private Thread t6;

  public ThreadS3(int[] interval, String disciplina, Thread t6) {
    this.interval = interval;
    this.disciplina = disciplina;
    this.t6 = t6;
  }

  @Override
  public void run() {

    for (int i = 0; i < interval.length; i++) {
      String hdr = currentThread().getName() + " - " + interval[i] + " ";
      System.out.print(hdr);
      try {
        Thread.sleep(5);
      } catch (Exception e) {
      }
    }
    System.out.println();

    while (t6.isAlive()) {
      Thread.yield();
    }

    System.out.print("\n" + currentThread().getName() + " - ");
    for (char ch : disciplina.toCharArray()) {
      System.out.print(ch);
      try {
        Thread.sleep(100);
      } catch (Exception e) {
      }
    }
    System.out.println();
  }
}

class ThreadS4 extends Thread {
  private ThreadS1_S2 t2;
  private String grupa;

  public ThreadS4(ThreadS1_S2 t2, String grupa) {
    this.t2 = t2;
    this.grupa = grupa;
  }

  @Override
  public void run() {
    for (int i = 700; i >= 300; i--) {

      String hdr = " " + currentThread().getName() + " - ";
      System.out.print(hdr);

      String tok = i + " ";
      System.out.print(tok);
      try {
        Thread.sleep(5);
      } catch (Exception e) {
      }
    }
    System.out.println();

    try {
      t2.join();
    } catch (Exception e) {
    }

    System.out.print("\n" + currentThread().getName() + " - ");
    for (char ch : grupa.toCharArray()) {
      System.out.print(ch);
      try {
        Thread.sleep(100);
      } catch (Exception e) {
      }
    }
    System.out.println();
  }
}

class ThreadS5 extends Thread {
  String text;
  Thread t2;

  public ThreadS5(String text, Thread t2) {
    this.text = text;
    this.t2 = t2;
  }

  @Override
  public void run() {
    try {
      Thread.sleep(3500);
    } catch (InterruptedException e) {
    }

    System.out.print("\n" + getName() + " - ");
    for (char ch : text.toCharArray()) {
      System.out.print(ch);
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
      }
    }
    t2.interrupt();
  }
}

class ThreadS6 extends Thread {
  String text;
  Thread t1;

  public ThreadS6(String text, Thread t1) {
    this.text = text;
    this.t1 = t1;
  }

  @Override
  public void run() {
    try {
      t1.join();
    } catch (InterruptedException e) {
    }

    System.out.print("\n" + getName() + " - ");
    for (char ch : text.toCharArray()) {
      System.out.print(ch);
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
      }
    }
    System.out.println();
  }
}
