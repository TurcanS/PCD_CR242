package lab2;

import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

/**
 * Nivelul avansat (Spranceana Marius): metoda run() a clasei Thread este suprascrisă,
 * astfel încât fiecare fir își afișează singur numele, grupul și prioritatea.
 * Prioritățile din formulă sunt micșorate cu 1 (AJUSTARE = -1).
 */
public final class NivelAvansat {

    /** Spranceana Marius: -1 (colegul din echipă folosește +1). */
    private static final int AJUSTARE = -1;

    private static Consumer<String> outFire;
    private static final CountDownLatch enumerareGata = new CountDownLatch(1);

    private NivelAvansat() {
    }

    public static void ruleaza(Consumer<String> fire, Consumer<String> enumerare) throws InterruptedException {
        outFire = fire;

        // grupurile din formulă
        ThreadGroup main = new ThreadGroup("Main");
        ThreadGroup go = new ThreadGroup(main, "GO");
        ThreadGroup gz = new ThreadGroup(go, "GZ");
        ThreadGroup gv = new ThreadGroup(main, "GV");
        ThreadGroup gf = new ThreadGroup(main, "GF");

        // firele din formulă: fiecare fir este creat, apoi i se setează prioritatea
        Fir tha = new Fir(gz, "Tha");
        tha.setPriority(prio(1));
        Fir thb = new Fir(gz, "Thb");
        thb.setPriority(prio(3));
        Fir thc = new Fir(gz, "Thc");
        thc.setPriority(prio(3));
        Fir thd = new Fir(gz, "Thd");
        thd.setPriority(prio(7));

        Fir thAGV = new Fir(gv, "ThA");
        thAGV.setPriority(prio(3));

        Fir th1GF = new Fir(gf, "Th1");
        th1GF.setPriority(prio(5));
        Fir th2GF = new Fir(gf, "Th2");
        th2GF.setPriority(prio(3));
        Fir th3GF = new Fir(gf, "Th3");
        th3GF.setPriority(prio(9));

        Fir th1Main = new Fir(main, "Th1");
        th1Main.setPriority(prio(3));
        // Th2() nu are prioritate în formulă: pornim de la cea implicită (moștenită)
        Fir th2Main = new Fir(main, "Th2");
        th2Main.setPriority(prio(th2Main.getPriority()));

        // după ce toate prioritățile sunt setate, pornim firele
        Fir[] toate = {tha, thb, thc, thd, thAGV, th1GF, th2GF, th3GF, th1Main, th2Main};
        for (Fir fir : toate) {
            fir.start();
        }

        Structura.enumera(main, "", enumerare);
        enumerareGata.countDown();
        Structura.asteapta(main);
    }

    /** Prioritatea din formulă + AJUSTARE, limitată la intervalul valid 1..10. */
    private static int prio(int prioritateFormula) {
        return Math.max(Thread.MIN_PRIORITY,
                Math.min(Thread.MAX_PRIORITY, prioritateFormula + AJUSTARE));
    }

    private static class Fir extends Thread {

        Fir(ThreadGroup grup, String nume) {
            super(grup, nume);
        }

        @Override
        public void run() {
            outFire.accept(String.format("Fir %-3s  grup = %-4s  prioritate = %d",
                    getName(), getThreadGroup().getName(), getPriority()));
            try {
                enumerareGata.await(); // firul rămâne activ până când este enumerat
            } catch (InterruptedException e) {
                interrupt();
            }
        }
    }
}
