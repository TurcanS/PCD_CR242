package andrei;

import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

public final class FirAvansat extends Thread {
    private final CountDownLatch pregatite;
    private final CountDownLatch eliberare;
    private final Consumer<String> output;

    public FirAvansat(ThreadGroup grup, String nume, CountDownLatch pregatite,
                      CountDownLatch eliberare, Consumer<String> output) {
        super(grup, nume);
        this.pregatite = pregatite;
        this.eliberare = eliberare;
        this.output = output;
    }

    @Override
    public void run() {
        try {
            output.accept("run(): " + getName() + " | grup: " + getThreadGroup().getName()
                    + " | prioritate: " + getPriority());
        } finally {
            pregatite.countDown();
        }
        Lab2.asteapta(eliberare);
    }
}
