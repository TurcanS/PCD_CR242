/**
 * Varianta 7:
 *  Th1 - sumele produselor numerelor pare, doua cate doua, de la primul element
 *  Th2 - sumele produselor numerelor pare, doua cate doua, de la ultimul element
 */
import javax.swing.*;

public class Adrian {
    private final int[] mas;
    private final JTextArea out;
    private final Thread th1;
    private final Thread th2;

    public Adrian(int[] mas, JTextArea out) {
        this.mas = mas;
        this.out = out;
        // Th1: parcurgere 0 -> n-1 ; Th2: parcurgere n-1 -> 0
        th1 = new Thread(new Worker(0, 1), "A-Th1");
        th2 = new Thread(new Worker(mas.length - 1, -1), "A-Th2");
    }

    public void start() {
        th1.start();
        th2.start();
    }

    public void join() throws InterruptedException {
        th1.join();
        th2.join();
    }

    private void print(String s) {
        SwingUtilities.invokeLater(() -> out.append(s + "\n"));
    }

    // Runnable comun; difera doar punctul de start si sensul (pasul)
    private class Worker implements Runnable {
        private final int start;
        private final int step;

        Worker(int start, int step) {
            this.start = start;
            this.step = step;
        }

        @Override
        public void run() {
            String name = Thread.currentThread().getName();
            int a = 0, b = 0, c = 0; // primele 3 numere pare din grup
            int found = 0; // cate numere pare avem in grupul curent

            for (int i = start; i >= 0 && i < mas.length; i += step) {
                int x = mas[i];
                if (x % 2 != 0) {
                    continue;
                }
                switch (found) {
                    case 0:
                        a = x;
                        found = 1;
                        break;
                    case 1:
                        b = x;
                        found = 2;
                        break;
                    case 2:
                        c = x;
                        found = 3;
                        break;
                    default:
                        // x 
                        print(name + " | " + a + " " + b + " " + c + " " + x
                                + " | " + a + "*" + b + "=" + (a * b)
                                + " | " + c + "*" + x + "=" + (c * x)
                                + " | suma = " + (a * b + c * x));
                        found = 0;
                }
            }
            // grupul incomplet (< 4 numere pare) nu se afiseaza
        }
    }
}