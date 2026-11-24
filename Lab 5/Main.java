import javax.swing.*;
import java.awt.Font;
import java.io.*;
import java.util.*;

public class Main {
  public static final int X = 2; // X producători
  public static final int Y = 3; // sunt consumate de Y consumator
  public static final int Z = 11; // este îndestulat cu Z obiecte
  public static final int D = 8; // Dimensiunea depozitului este D
  public static final int F = 2; // generează aleatoriu F obiecte

  public static void main(String[] args) {
    JTextArea area = new JTextArea();
    area.setEditable(false);
    area.setFont(new Font("Hack Nerd Font Mono", Font.PLAIN, 16));
    JFrame frame = new JFrame("LAB 4 GRUP 1");
    frame.add(new JScrollPane(area));
    frame.setSize(600, 1030);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);

    System.setOut(new PrintStream(new OutputStream() {
      public void write(int b) {
        area.append(String.valueOf((char) b));
      }
    }, true));

    System.out.println("START PROGRAMUL");

    Depozit depozit = new Depozit();


    System.out.print("PROGRAMUL SA FINALIZAT");
  }
}

class Depozit {

}

class Producator extends Thread {
  Depozit dep;

  public Producator(Depozit dep, String name) {
    super(name);
    this.dep = dep;
  }

}

class Consumator extends Thread {
  Depozit dep;
  int val = 0;

  public Consumator(Depozit dep, String name) {
    super(name);
    this.dep = dep;
  }

}
