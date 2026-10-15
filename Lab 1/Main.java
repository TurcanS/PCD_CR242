import javax.swing.*;
import java.awt.*;
import java.io.PrintStream;
import java.io.OutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Main {

  public static void main(String[] args) {

    SwingUtilities.invokeLater(() -> {

      JFrame fereastra = new JFrame("Program cu thread-uri");
      fereastra.setSize(700, 500);
      fereastra.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      fereastra.setLocationRelativeTo(null);

      JPanel panou = new JPanel(new BorderLayout(10, 10));

      JLabel titlu = new JLabel(
          "CR-242 - Grup 1",
          SwingConstants.CENTER);
      titlu.setFont(new Font("Arial", Font.BOLD, 20));

      JButton start = new JButton("START");
      start.setFont(new Font("Arial", Font.BOLD, 16));

      JTextArea rezultat = new JTextArea();
      rezultat.setEditable(false);
      rezultat.setFont(new Font("Monospaced", Font.PLAIN, 13));
      rezultat.setLineWrap(true);
      rezultat.setWrapStyleWord(true);

      JScrollPane scroll = new JScrollPane(rezultat);

      panou.setBorder(BorderFactory.createEmptyBorder(
          10, 10, 10, 10));

      panou.add(titlu, BorderLayout.NORTH);
      panou.add(scroll, BorderLayout.CENTER);
      panou.add(start, BorderLayout.SOUTH);

      fereastra.add(panou);
      fereastra.setVisible(true);

      start.addActionListener(e -> {

        start.setEnabled(false);
        rezultat.setText("");

        // Redirectionam System.out catre JTextArea
        PrintStream consola = new PrintStream(
            new OutputStream() {

              @Override
              public void write(
                  byte[] b, int off, int len) throws IOException {

                String text = new String(
                    b, off, len,
                    StandardCharsets.UTF_8);

                SwingUtilities.invokeLater(() -> rezultat.append(text));
              }

              @Override
              public void write(int b)
                  throws IOException {
                write(
                    new byte[] { (byte) b }, 0, 1);
              }
            },
            true,
            StandardCharsets.UTF_8);

        System.setOut(consola);

        Thread executie = new Thread(() -> {

          try {
            int[] tablou = new int[100];

            String studenti = "CR-242 Grup 1: Turcan Stanislav, " +
                "Stefanita David, Spinu Adrian";

            for (int i = 0; i < 100; i++) {
              tablou[i] = (int) (Math.random() * 99);

              System.out.print(tablou[i] + " ");

              if ((i + 1) % 10 == 0) {
                System.out.println();
              }
            }

            Stefanita fir1 = new Stefanita(tablou, 0, true, true);
            Stefanita fir2 = new Stefanita(tablou, 99, false, true);

            TurcanS fir3 = new TurcanS(0, 99, 1, tablou);
            TurcanS fir4 = new TurcanS(99, 0, -1, tablou);

            Adrian fir5 = new Adrian(0, 99, 1, tablou);
            Adrian fir6 = new Adrian(99, 0, -1, tablou);

            fir1.setName("Stefanita cresc");
            fir2.setName("Stefanita desc");
            fir3.setName("Stas Cresc");
            fir4.setName("Stas Desc");
            fir5.setName("Adrian Cresc");
            fir6.setName("Adrian Desc");

            fir1.start();
            fir2.start();
            fir3.start();
            fir4.start();
            fir5.start();
            fir6.start();

            fir1.join();
            fir2.join();
            fir3.join();
            fir4.join();
            fir5.join();
            fir6.join();

            System.out.println(
                "\nToate thread-urile au terminat!");

            for (int i = 0; i < studenti.length(); i++) {
              System.out.print(studenti.charAt(i));
              Thread.sleep(100);
            }

          } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            System.out.println(
                "Executia a fost intrerupta.");
          } finally {
            SwingUtilities.invokeLater(() -> start.setEnabled(true));
          }
        });

        executie.start();
      });
    });
  }
}
