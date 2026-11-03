import java.io.*;
import javax.swing.*;

public class Lab2_Edgar extends Thread {

    public static Thread[] t = new Thread[10];
    static JTextArea area = new JTextArea();

    public Lab2_Edgar(ThreadGroup g, String name) {
        super(g, name);
    }

    @Override
        public void run() {
        for (int i = 0; i < 10; i++) {
        while (t[i].isAlive()) {
    }
    }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Lab2");
        area.setEditable(false);
        frame.add(new JScrollPane(area));
        frame.setSize(750, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    
            System.setOut(new PrintStream(new OutputStream() {
            public void write(int b) {
                area.append(String.valueOf((char) b));
            }
            }, true));

            ThreadGroup main = Thread.currentThread().getThreadGroup();
            main.setDaemon(true);
            Thread curr = Thread.currentThread();
            curr.setPriority(3);
            

            ThreadGroup GN = new ThreadGroup(main, "GN");
            ThreadGroup GH = new ThreadGroup(GN, "GH");
            ThreadGroup GM = new ThreadGroup(main, "GM");

                t[0] = new Lab2_Edgar(GH, "Tha");
                t[0].setPriority(4);
                t[0].start();
                

                t[1] = new Lab2_Edgar(GH, "Thb");
                t[1].start();
                

                t[2] = new Lab2_Edgar(GH, "Thc");
                t[2].setPriority(6);
                t[2].start();
                

                t[3] = new Lab2_Edgar(GH, "Thd");
                t[3].start();
                

                t[4] = new Lab2_Edgar(GN, "ThA");
                t[4].start();
                

                t[5] = new Lab2_Edgar(GM, "Th1");
                t[5].setPriority(2);
                t[5].start();
                

                t[6] = new Lab2_Edgar(GM, "Th2");
                t[6].start();
                

                t[7] = new Lab2_Edgar(GM, "Th3");
                t[7].start();
                

                t[8] = new Lab2_Edgar(main, "Th1");
                t[8].setPriority(8);
                t[8].start();
                

                t[9] = new Lab2_Edgar(main, "Th2");
                t[9].start();

                main.list();
    }
}
