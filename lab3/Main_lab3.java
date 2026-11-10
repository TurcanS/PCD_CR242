import javax.swing.*;
import java.awt.*;

public class Main_lab3 {

    static void scrie(JTextArea ta, String text) {
        SwingUtilities.invokeLater(() -> {
            ta.append(text);
            ta.setCaretPosition(ta.getDocument().getLength());
        });
    }

    static void scrieLitere(JTextArea ta, String text) throws InterruptedException {
        for (int i = 0; i < text.length(); i++) {
            scrie(ta, String.valueOf(text.charAt(i)));
            Thread.sleep(100);
        }
        scrie(ta, "\n");
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

        Fir1 fir1 = new Fir1(t, ta);
        Fir2 fir2 = new Fir2(t, ta);
        Fir3 fir3 = new Fir3(ta);
        Fir4 fir4 = new Fir4(ta);

        fir1.setName("fir1");
        fir2.setName("fir2");
        fir3.setName("fir3");
        fir4.setName("fir4");

        fir4.setPrecedent(fir2);
        fir1.setPrecedent(fir4);
        fir3.setPrecedent(fir1);

        fir1.start();
        fir2.start();
        fir3.start();
        fir4.start();
    }
}

class Fir1 extends Thread {
    private int[] t;
    private JTextArea ta;
    private Thread precedent;

    Fir1(int[] t, JTextArea ta) {
        this.t = t;
        this.ta = ta;
    }

    void setPrecedent(Thread precedent) {
        this.precedent = precedent;
    }

    @Override
    public void run() {
        String nume = getName();
        int start = 0, end = 99, pas = 1;
        Main_lab3.scrie(ta, "\n" + nume + " crescator:\n");

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

                Main_lab3.scrie(ta, "\n" + nume + " pozitii: " + poz1 + " si " + poz2
                        + " valori: " + t[poz1] + " * " + t[poz2] + " = " + produs + "\n");
            }
        }
        Main_lab3.scrie(ta, "\n" + nume +  "\n");

        try {
            precedent.join();
            Main_lab3.scrieLitere(ta, "Prenume: Veniamin și Octavian");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Fir2 extends Thread {
    private int[] t;
    private JTextArea ta;

    Fir2(int[] t, JTextArea ta) {
        this.t = t;
        this.ta = ta;
    }

    @Override
    public void run() {
        String nume = getName();
        int start = 99, end = 0, pas = -1;
        Main_lab3.scrie(ta, "\n" + nume + " descrescator:\n");

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
                Main_lab3.scrie(ta, "\n" + nume + " pozitii: " + poz1 + " si " + poz2
                        + " valori: " + t[poz1] + " * " + t[poz2] + " = " + produs + "\n");
            }
        }
        Main_lab3.scrie(ta, "\n" + nume + "\n");

        try {
            Main_lab3.scrieLitere(ta, "Nume: Spînu și Demenco");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Fir3 extends Thread {
    private JTextArea ta;
    private Thread precedent;

    Fir3(JTextArea ta) {
        this.ta = ta;
    }

    void setPrecedent(Thread precedent) {
        this.precedent = precedent;
    }

    @Override
    public void run() {
        String nume = getName();
        Main_lab3.scrie(ta, "\n" + nume + " parcurge [567, 1002] de la inceput:\n");

        StringBuilder rand = new StringBuilder();
        int numarate = 0;
        for (int i = 567; i <= 1002; i++) {
            rand.append(i).append(" ");
            numarate++;
            if (numarate % 10 == 0) {
                Main_lab3.scrie(ta, rand + "\n");
                rand.setLength(0);
            }
            Thread.yield();
        }
        if (rand.length() > 0) {
            Main_lab3.scrie(ta, rand + "\n");
        }
        Main_lab3.scrie(ta, "\n" + nume + " a terminat parcurgerea\n");

        try {
            precedent.join();
            Main_lab3.scrieLitere(ta, "Disciplina: Programarea concurentă și distribuită");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Fir4 extends Thread {
    private JTextArea ta;
    private Thread precedent;

    Fir4(JTextArea ta) {
        this.ta = ta;
    }

    void setPrecedent(Thread precedent) {
        this.precedent = precedent;
    }

    @Override
    public void run() {
        String nume = getName();
        Main_lab3.scrie(ta, "\n" + nume + " parcurge [567, 1100] de la sfarsit:\n");

        try {
            StringBuilder rand = new StringBuilder();
            int numarate = 0;
            for (int i = 1100; i >= 567; i--) {
                rand.append(i).append(" ");
                numarate++;
                if (numarate % 10 == 0) {
                    Main_lab3.scrie(ta, rand + "\n");
                    rand.setLength(0);
                }
                Thread.sleep(1);
            }
            if (rand.length() > 0) {
                Main_lab3.scrie(ta, rand + "\n");
            }
            Main_lab3.scrie(ta, "\n" + nume + " a terminat parcurgerea\n");

            precedent.join();
            Main_lab3.scrieLitere(ta, "Grupa: CR-242");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}