package lab1;

import java.util.function.Consumer;

/**
 * Clasa lui Cornos Ilie: două fire de execuție create prin interfața Runnable.
 * Th1 - condiția 1 (căutarea începe de la primul element),
 * Th2 - condiția 2 (căutarea începe de la ultimul element).
 */
public class CornosIlie implements Runnable {

    private final int[] mas;
    private final Consumer<String> out1, out2;
    private final Thread th1, th2;

    public CornosIlie(int[] mas, Consumer<String> out1, Consumer<String> out2) {
        this.mas = mas;
        this.out1 = out1;
        this.out2 = out2;
        // ambele fire execută metoda run() a aceluiași obiect
        th1 = new Thread(this, "Cornos-Th1");
        th2 = new Thread(this, "Cornos-Th2");
    }

    public void start() {
        th1.start();
        th2.start();
    }

    public void join() throws InterruptedException {
        th1.join();
        th2.join();
    }

    @Override
    public void run() {
        try {
            if (Thread.currentThread() == th1) {
                PerechiImpare.cauta(mas, 0, 1, out1);
            } else {
                PerechiImpare.cauta(mas, mas.length - 1, -1, out2);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
