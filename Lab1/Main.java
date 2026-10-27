import javax.swing.*;
import java.awt.*;

public class Main {

    static void scrie(JTextArea ta, String text) {
        SwingUtilities.invokeLater(() -> {
            ta.append(text);
            ta.setCaretPosition(ta.getDocument().getLength());
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Rezultat");
        JTextArea ta = new JTextArea();
        ta.setEditable(false);
        ta.setFont(new Font("Monospaced", Font.PLAIN, 13));
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        frame.add(new JScrollPane(ta));
        frame.setSize(900, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        int[] t = new int[100];
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            t[i] = (int) (Math.random() * 99);
            sb.append(t[i]).append(" ");
        }
        sb.append("\n");
        scrie(ta, sb.toString());

        Thread fir1 = new Thread(new Veniamin(t, 0, 99, 1, ta));
        fir1.setName("Veniamin_cresc");
        scrie(ta, "\nS-a creat firul " + fir1.getName() + "\n");

        Thread fir2 = new Thread(new Veniamin(t, 98, 0, -1, ta));
        fir2.setName("Veniamin_desc");
        scrie(ta, "S-a creat firul " + fir2.getName() + "\n");

        Octavian fir3 = new Octavian(t, 0, 99, 1, ta);
        fir3.setName("Octavian_cresc");
        scrie(ta, "S-a creat firul " + fir3.getName() + "\n");

        Octavian fir4 = new Octavian(t, 99, 0, -1, ta);
        fir4.setName("Octavian_desc");
        scrie(ta, "S-a creat firul " + fir4.getName() + "\n");

        fir1.start();
        fir2.start();
        fir3.start();
        fir4.start();

        try {
            fir1.join();
            fir2.join();
            fir3.join();
            fir4.join();
            String name = "Veniamin Spînu și Demenco Octavian";
            scrie(ta, "\n");
            for (int i = 0; i < name.length(); i++) {
                scrie(ta, String.valueOf(name.charAt(i)));
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Veniamin implements Runnable {
    private int[] t;
    private int start;
    private int end;
    private int pas;
    private JTextArea ta;

    Veniamin(int[] t, int start, int end, int pas, JTextArea ta) {
        this.t = t;
        this.start = start;
        this.end = end;
        this.pas = pas;
        this.ta = ta;
    }

    @Override
    public void run() {
        int suma = 0;
        String nume = Thread.currentThread().getName();

        if (pas > 0) {
            Main.scrie(ta, "\n" + nume + " crescator:\n");
        } else {
            Main.scrie(ta, "\n" + nume + " descrescator:\n");
        }

        int i = start;
        while ((pas > 0 && i + 2 * pas <= end) || (pas < 0 && i + 2 * pas >= end)) {
            int poz1 = i;
            int poz2 = i + 2 * pas;
            int produs = t[poz1] * t[poz2];
            suma += produs;

            String linie = "\n" + nume + " pozitii: " + poz1 + " si " + poz2
                    + " valori: " + t[poz1] + " * " + t[poz2] + " = " + produs + "\n";
            Main.scrie(ta, linie);

            i += 4 * pas;
        }

        Main.scrie(ta, "\n" + nume + " suma = " + suma + "\n");
    }
}

class Octavian extends Thread {
    private int[] t;
    private int start;
    private int end;
    private int pas;
    private JTextArea ta;

    Octavian(int[] t, int start, int end, int pas, JTextArea ta) {
        this.t = t;
        this.start = start;
        this.end = end;
        this.pas = pas;
        this.ta = ta;
    }

    @Override
    public void run() {
        int suma = 0;
        String nume = getName();

        if (pas > 0) {
            Main.scrie(ta, "\n" + nume + " crescator:\n");
        } else {
            Main.scrie(ta, "\n" + nume + " descrescator:\n");
        }

        int rest = (pas > 0) ? 0 : 2;
        int min = Math.min(start, end);
        int max = Math.max(start, end);

        for (int i = start; i != end + pas; i += pas) {
            if (i % 4 == rest) {
                int poz1 = i;
                int poz2 = i + 2 * pas;

                if (poz2 < min || poz2 > max) {
                    continue;
                }

                int produs = t[poz1] * t[poz2];
                suma += produs;

                String linie = "\n" + nume + " pozitii: " + poz1 + " si " + poz2
                        + " valori: " + t[poz1] + " * " + t[poz2] + " = " + produs + "\n";
                Main.scrie(ta, linie);
            }
        }

        Main.scrie(ta, "\n" + nume + " suma = " + suma + "\n");
    }
}