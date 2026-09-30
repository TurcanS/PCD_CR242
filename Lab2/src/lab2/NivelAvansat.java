package lab2;

import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

/**
 * Nivelul avansat (Spranceana Marius): metoda run() a clasei Thread este suprascrisă,
 * astfel încât fiecare fir își afișează singur numele, grupul și prioritatea.
 */
public final class NivelAvansat {

    private NivelAvansat() {
    }

    public static void ruleaza(Consumer<String> outFire, Consumer<String> outEnumerare) throws InterruptedException {
        CountDownLatch enumerareGata = new CountDownLatch(1);
        ThreadGroup main = Structura.construieste((grup, nume) -> new Fir(grup, nume, outFire, enumerareGata));
        Structura.enumera(main, "", outEnumerare);
        enumerareGata.countDown();
        Structura.asteapta(main);
    }

    private static class Fir extends Thread {
        private final Consumer<String> out;
        private final CountDownLatch enumerareGata;

        Fir(ThreadGroup grup, String nume, Consumer<String> out, CountDownLatch enumerareGata) {
            super(grup, nume);
            this.out = out;
            this.enumerareGata = enumerareGata;
        }

        @Override
        public void run() {
            out.accept(String.format("Fir %-3s  grup = %-4s  prioritate = %d",
                    getName(), getThreadGroup().getName(), getPriority()));
            try {
                enumerareGata.await(); // firul rămâne activ până când este enumerat
            } catch (InterruptedException e) {
                interrupt();
            }
        }
    }
}
