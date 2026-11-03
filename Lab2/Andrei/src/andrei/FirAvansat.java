package andrei;

import java.util.concurrent.CountDownLatch;

public class FirAvansat extends Thread {
    private final CountDownLatch terminare;

    public FirAvansat(ThreadGroup grup, String nume, CountDownLatch terminare) {
        super(grup, nume);
        this.terminare = terminare;
    }

    @Override
    public void run() {
        // Fiecare fir avansat isi afiseaza singur informatiile.
        Lab2.afiseaza("run(): " + getName()
                + " | grup: " + getThreadGroup().getName()
                + " | prioritate: " + getPriority());
        Lab2.asteapta(terminare);
    }
}
