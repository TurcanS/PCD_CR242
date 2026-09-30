package lab1;

import java.util.function.Consumer;

/**
 * Clasa lui Spranceana Marius: două fire de execuție create prin extinderea clasei Thread.
 * Th1 - condiția 1 (căutarea începe de la primul element),
 * Th2 - condiția 2 (căutarea începe de la ultimul element).
 */
public class SpranceanaMarius {

    private final Fir th1, th2;

    public SpranceanaMarius(int[] mas, Consumer<String> out1, Consumer<String> out2) {
        th1 = new Fir("Spranceana-Th1", mas, 0, 1, out1);
        th2 = new Fir("Spranceana-Th2", mas, mas.length - 1, -1, out2);
    }

    public void start() {
        th1.start();
        th2.start();
    }

    public void join() throws InterruptedException {
        th1.join();
        th2.join();
    }

    private static class Fir extends Thread {
        private final int[] mas;
        private final int from, step;
        private final Consumer<String> out;

        Fir(String nume, int[] mas, int from, int step, Consumer<String> out) {
            super(nume);
            this.mas = mas;
            this.from = from;
            this.step = step;
            this.out = out;
        }

        @Override
        public void run() {
            try {
                PerechiImpare.cauta(mas, from, step, out);
            } catch (InterruptedException e) {
                interrupt();
            }
        }
    }
}
