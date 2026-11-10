import javax.swing.*;
import java.awt.*;

public class Veniamin_Priority extends Thread {

    public Veniamin_Priority(ThreadGroup grup, String name) {
        super(grup, name);
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

    private static void afiseaza(ThreadGroup g, int nivel, StringBuilder sb) {
        String tab = "".repeat(nivel);

        sb.append(tab).append("ThreadGroup[name=").append(g.getName())
                .append(",maxpri=").append(g.getMaxPriority()).append("]\n");

        Thread[] fire = new Thread[g.activeCount() + 10];
        int n = g.enumerate(fire, false);
        for (int i = 0; i < n; i++) {
            Thread t = fire[i];
            sb.append(tab).append("Thread[").append(t.getName())
                    .append(",").append(t.getPriority())
                    .append(",").append(t.getThreadGroup().getName())
                    .append(",daemon=").append(t.isDaemon())
                    .append("]\n");
        }

        ThreadGroup[] subgrupuri = new ThreadGroup[g.activeGroupCount() + 10];
        int m = g.enumerate(subgrupuri, false);
        for (int i = 0; i < m; i++) {
            afiseaza(subgrupuri[i], nivel + 1, sb);
        }
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
        Th2.setDaemon(true);
        Th2.start();
        Veniamin_Priority ThA = new Veniamin_Priority(GE, "ThA");
        ThA.setPriority(2);
        ThA.start();
        Veniamin_Priority GKth1 = new Veniamin_Priority(GK, "Th1");
        GKth1.setPriority(2);
        GKth1.start();
        Veniamin_Priority GKth2 = new Veniamin_Priority(GK, "Th2");
        GKth2.setPriority(5);
        GKth2.setDaemon(true);
        GKth2.start();
        Veniamin_Priority GKth3 = new Veniamin_Priority(GK, "Th3");
        GKth3.setPriority(2);
        GKth3.start();
        Veniamin_Priority GHtha = new Veniamin_Priority(GH, "Tha");
        GHtha.setPriority(3);
        GHtha.setDaemon(true);
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

        StringBuilder sb = new StringBuilder("Grupul MAIN:\n");
        afiseaza(main, 0, sb);
        String rezultat = sb.toString();

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