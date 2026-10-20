
/*
 Sumele numerelor impare două cate două
începând căutarea şi sumarea de la primul
element

Sumele numerelor impare două cate două
începând căutarea şi sumarea de la ultimul
element



*/
import javax.swing.*;
import java.awt.*;

public class Lab1 {

  private static JTextArea output;
  private static JButton runButton;
    
  public static void main(String[] args) {
    SwingUtilities.invokeLater(Lab1::createInterface);
  }

  private static void createInterface() {
    JFrame frame = new JFrame("Lab 1 - Thread Sums");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(850, 600);
    frame.setMinimumSize(new Dimension(650, 450));
    frame.setLocationRelativeTo(null);
    
    JPanel mainPanel = new JPanel(new BorderLayout(12, 12));
    mainPanel.setBackground(new Color(25, 25, 25));
    mainPanel.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));

    JLabel title = new JLabel("THREAD SUM CALCULATOR");
    title.setForeground(new Color(243, 12, 32));
    title.setFont(new Font("Monospaced", Font.BOLD, 23));

    JLabel subtitle = new JLabel(
        "Lab 1  |  Sum1 and Sum2  |  Multithreading");
    subtitle.setForeground(Color.LIGHT_GRAY);
    subtitle.setFont(new Font("Monospaced", Font.PLAIN, 12));

    JPanel header = new JPanel();
    header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
    header.setBackground(new Color(25, 25, 25));
    header.add(title);
    header.add(Box.createVerticalStrut(6));
    header.add(subtitle);

    output = new JTextArea();
    output.setEditable(false);
    output.setBackground(new Color(15, 15, 15));
    output.setForeground(new Color(230, 230, 230));
    output.setCaretColor(Color.WHITE);
    output.setFont(new Font("Monospaced", Font.PLAIN, 13));
    output.setLineWrap(true);
    output.setWrapStyleWord(true);
    output.setMargin(new Insets(12, 12, 12, 12));

    JScrollPane scrollPane = new JScrollPane(output);
    scrollPane.setBorder(
        BorderFactory.createLineBorder(new Color(65, 65, 65)));

    runButton = new JButton("▶  Run");
    runButton.setFocusPainted(false);
    runButton.setBackground(new Color(243, 12, 32));
    runButton.setForeground(Color.WHITE);
    runButton.setFont(new Font("Monospaced", Font.BOLD, 13));

    JButton clearButton = new JButton("Clear output");
    clearButton.setFocusPainted(false);
    clearButton.setBackground(new Color(45, 45, 45));
    clearButton.setForeground(Color.WHITE);
    clearButton.setFont(new Font("Monospaced", Font.PLAIN, 13));

    JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
    buttons.setBackground(new Color(25, 25, 25));
    buttons.add(runButton);
    buttons.add(clearButton);

    runButton.addActionListener(e -> {
      runButton.setEnabled(false);
      output.setText("");

      Thread task = new Thread(() -> {
        try {
          runLab();
        } finally {
          SwingUtilities.invokeLater(
              () -> runButton.setEnabled(true));
        }
      });

      task.start();

    });

    clearButton.addActionListener(e -> output.setText(""));

    JPanel bottom = new JPanel(new BorderLayout());
    bottom.setBackground(new Color(25, 25, 25));
    bottom.add(buttons, BorderLayout.WEST);

    mainPanel.add(header, BorderLayout.NORTH);
    mainPanel.add(scrollPane, BorderLayout.CENTER);
    mainPanel.add(bottom, BorderLayout.SOUTH);

    frame.setContentPane(mainPanel);
    frame.setVisible(true);

    log("Ready. Press Run to start the calculation.\n");
  }

  public static void log(String message) {
    SwingUtilities.invokeLater(() -> {
      output.append(message);
      output.setCaretPosition(output.getDocument().getLength());
    });
  }

  private static void runLab() {
    int[] tablou = new int[101];

    log("=== GENERATED ARRAY ===\n");

    StringBuilder arrayText = new StringBuilder();

    for (int i = 0; i < 100; i++) {
      tablou[i] = (int) (Math.random() * 99);
      arrayText.append(tablou[i]).append(" ");
    }

    log(arrayText + "\n\n");
    log("=== THREAD EXECUTION ===\n");
    Sum1 Th1_1 = new Sum1(tablou, 0, 99, 1);
    Th1_1.setName("Th1.1");
    Th1_1.start();

    Sum1 Th1_2 = new Sum1(tablou, 99, 0, -1);
    Th1_2.setName("Th1.2");
    Th1_2.start();

    Sum2 task1 = new Sum2(tablou, 0, 99, 1);
    Thread Th2_1 = new Thread(task1);
    Th2_1.setName("Th2.1");
    Th2_1.start();

    
    Sum2 task2 = new Sum2(tablou, 99, 0, -1);
    Thread Th2_2 = new Thread(task2);
    Th2_2.setName("Th2.2");
    Th2_2.start();

    String Nume = "Grup-3 Vleju Dumitru Cemirtan Edgar";

    try {
      Th1_1.join();
      Th1_2.join();
      Th2_1.join();
      Th2_2.join();

      log("\n=== GROUP ===\n");

      for (int i = 0; i < Nume.length(); i++) {
        log(Nume.charAt(i) + "");
        Thread.sleep(100);
      }

      log("\n\n=== EXECUTION COMPLETED ===\n");

    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      log("\nExecution interrupted.\n");
    }
  }
}

class Sum1 extends Thread {
  private int from, to, step;
  private int[] tablou;

  public Sum1(int[] tablou, int from, int to, int step) {
    this.from = from;
    this.to = to;
    this.tablou = tablou;
    this.step = step;
  }

  public void run() {
    int suma1 = 0, suma2 = 0, suma = 0;
    int i = from;
    int k = 0;
    int sum_t = 0;

    while (i != to) {
      if (tablou[i] % 2 == 1) {
        suma1 = i;
        i += step;

        if ((i < to && step < 0) ||
            (i > to && step > 0))
          break;

        do {
          if (tablou[i] % 2 == 1) {
            suma2 = i;
            k++;
            suma = tablou[suma1] + tablou[suma2];

            Lab1.log(getName() + ": Suma1=" +
                tablou[suma1] + " ; Suma2=" +
                tablou[suma2] + "; Suma=" +
                suma + ".\n");

            sum_t += suma;

            if (k == 2) {
              Lab1.log(getName() +
                  " Suma totala: " + sum_t + "\n");
              k = 0;
              sum_t = 0;
            }

            break;
          }

          i += step;

          if ((i < to && step < 0) ||
              (i > to && step > 0))
            break;

        } while (true);
      }

      i += step;

      if ((i < to && step < 0) ||
          (i > to && step > 0))
        break;
    }
  }
}

class Sum2 implements Runnable{
  private int from, to, step;
  private int[] tablou;

  public Sum2(int[] tablou, int from, int to, int step) {
    this.from = from;
    this.to = to;
    this.tablou = tablou;
    this.step = step;
  }
  @Override
  public void run() {
    int[] arr = new int[101];
    int k = 0;
    int sum_t = 0;
    int sum;

    for (int i = from; (i <= to && step > 0) || (i >= to && step < 0); i += step) {

      if (tablou[i] % 2 == 1) {
        arr[k] = tablou[i];
        k++;
      }
    }

    if (k % 2 == 1)
      k--;

    for (int i = 0; i < k; i += 2) {
      sum = arr[i] + arr[i + 1];

      Lab1.log(Thread.currentThread().getName() + ": Sum1=" + arr[i] +
          " ; Sum2=" + arr[i + 1] +
          "; Sum=" + sum + ".\n");

      sum_t += sum;

      if (i % 4 == 0 && i != 0) {
        Lab1.log(Thread.currentThread().getName() + "Suma Suma totala: " + sum_t + ".\n");
        sum_t = 0;
      }
    }
  }
}
