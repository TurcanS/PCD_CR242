import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class Veniamin_Priority extends Thread {

    public Veniamin_Priority(ThreadGroup grup, String name) {
        super(grup, name);
        setDaemon(true);
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(100);
            } catch (Exception e) {
            }
        }
    }
    @Override
    public String toString() {
        return "Thread[#" + getId() + "," + getName() + "," + getPriority() + "," + getThreadGroup().getName() + ",daemon=" + isDaemon() + "]";
    }

    public static void main(String[] args) {

        ThreadGroup main = new ThreadGroup("main");
        ThreadGroup GK = new ThreadGroup(main, "GK");
        ThreadGroup GE = new ThreadGroup(main, "GE");
        ThreadGroup GH = new ThreadGroup(GE, "GH");

        Veniamin_Priority Th1 = new Veniamin_Priority(main, "Th1");
        Th1.setPriority(2);
        Th1.start();
        Veniamin_Priority Th2 = new Veniamin_Priority(main, "Th2");
        Th2.setPriority(6);
        Th2.start();
        Veniamin_Priority ThA = new Veniamin_Priority(GE, "ThA");
        ThA.setPriority(2);
        ThA.start();
        Veniamin_Priority GKth1 = new Veniamin_Priority(GK, "Th1");
        GKth1.setPriority(2);
        GKth1.start();
        Veniamin_Priority GKth2 = new Veniamin_Priority(GK, "Th2");
        GKth2.setPriority(5);
        GKth2.start();
        Veniamin_Priority GKth3 = new Veniamin_Priority(GK, "Th3");
        GKth3.setPriority(2);
        GKth3.start();
        Veniamin_Priority GHtha = new Veniamin_Priority(GH, "Tha");
        GHtha.setPriority(3);
        GHtha.start();
        Veniamin_Priority GHthb = new Veniamin_Priority(GH, "Thb");
        GHthb.setPriority(2);
        GHthb.start();
        Veniamin_Priority GHthc = new Veniamin_Priority(GH, "Thc");
        GHthc.setPriority(1);
        GHthc.start();
        Veniamin_Priority GHthd = new Veniamin_Priority(GH, "Thd");
        GHthd.setPriority(1);
        GHthd.start();

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        PrintStream consola = System.out;
        System.setOut(new PrintStream(buffer, true, StandardCharsets.UTF_8));
        System.out.println("main:");
        main.list();
        System.out.flush();
        System.setOut(consola);
        String rezultat = buffer.toString(StandardCharsets.UTF_8);

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Grupuri de fire si prioritati");
            JTextArea ta = new JTextArea(rezultat);
            ta.setEditable(false);
            ta.setFont(new Font("Monospaced", Font.PLAIN, 14));
            frame.add(new JScrollPane(ta));
            frame.setSize(600, 400);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}