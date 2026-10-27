import javax.swing.*;
import java.awt.*;
public class Main {

    // o linie completa e adaugata dintr-o singura bucata, deci nu se amesteca intre fire
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
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);


        //  Sum2  fir3,fir4;

        int[] t = new int[100];
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<100; i++){
            t[i] = (int)(Math.random()*99);
            sb.append(t[i]).append(" ");
        }
        sb.append("\n");
        scrie(ta, sb.toString());

        Thread fir1 = new Thread(new Veniamin(t, 0, 99, 1, ta));
        fir1.setName("Veniamin_cresc");
        fir1.start();

        Thread fir2 = new Thread(new Veniamin(t, 99, 0, -1, ta));
        fir2.setName("Veniamin_desc");
        fir2.start();
        // fir3 = new Sum2(t, 0, 99, 1);
        // fir3.setName("Th2.1");
        // fir3.start();
        // fir4 = new Sum2(t, 99, 0, -1);
        // fir4.setName("Th2.2");
        // fir4.start();

        try {
            fir1.join();
            fir2.join();
            //fir3.join();
            //fir4.join();
            String name = "Veniamin Spînu și Demenco Octavian si cineva";
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
    public void run() {
        int suma = 0;
        String nume = Thread.currentThread().getName();

        int i = start;
        while ((pas > 0 && i + 2 * pas <= end) || (pas < 0 && i + 2 * pas >= end)) {
            int poz1 = i;
            int poz2 = i + 2 * pas;
            int produs = t[poz1] * t[poz2];
            suma += produs;

            String linie = "\n" + nume + " pozitii: " + poz1 + " si " + poz2 +
                    " valori: " + t[poz1] + " * " + t[poz2] + " = " + produs + "\n";
            Main.scrie(ta, linie);
            i += 4 * pas;
        }

        Main.scrie(ta, "\n" + nume + " suma = " + suma + "\n");
    }
}