package andrei;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

public final class Lab2 {
    // Varianta 6: aceeasi interpretare a structurii ca in partea colegului.
    // In PDF, secventa G1{ThA(3)}{G3 are o acolada suplimentara;
    // folosim Main{G1{ThA,G3{...},G2{...}},Th1,Th2}.
    public static final String[][] DATE = {
        {"Main/G1", "ThA", "3"},
        {"Main/G1/G3", "Thf", "3"}, {"Main/G1/G3", "Thb", "7"},
        {"Main/G1/G3", "Thc", "3"}, {"Main/G1/G3", "Thd", "3"},
        {"Main/G1/G2", "Th8", "3"}, {"Main/G1/G2", "Th9", "4"},
        {"Main/G1/G2", "Th3", "3"}, {"Main", "Th1", "3"},
        {"Main", "Th2", "3"}
    };

    public static void executa(boolean avansat, Consumer<String> output)
            throws InterruptedException {
        ThreadGroup main = new ThreadGroup("Main");
        ThreadGroup g1 = new ThreadGroup(main, "G1");
        ThreadGroup g3 = new ThreadGroup(g1, "G3");
        ThreadGroup g2 = new ThreadGroup(g1, "G2");
        List<Thread> fire = new ArrayList<>();
        CountDownLatch pregatite = new CountDownLatch(DATE.length);
        CountDownLatch eliberare = new CountDownLatch(1);

        output.accept("Varianta 6 | nivel " + (avansat ? "avansat" : "mediu")
                + " | modificare +1");
        for (String[] date : DATE) {
            ThreadGroup grup;
            switch (date[0]) {
                case "Main/G1": grup = g1; break;
                case "Main/G1/G3": grup = g3; break;
                case "Main/G1/G2": grup = g2; break;
                default: grup = main;
            }
            Thread fir;
            if (avansat) {
                fir = new FirAvansat(grup, date[1], pregatite, eliberare, output);
            } else {
                // Nivel mediu: obiect Thread obisnuit, fara suprascrierea Thread.run().
                // Runnable mentine firul activ doar pana la enumerarea structurii.
                fir = new Thread(grup, () -> {
                    pregatite.countDown();
                    asteapta(eliberare);
                }, date[1]);
            }
            int prioritate = Integer.parseInt(date[2]) + 1;
            fir.setPriority(prioritate); // Inainte de start, pentru a evita o cursa.
            if (fir.getPriority() != prioritate) {
                throw new IllegalStateException("Grupul limiteaza prioritatea ceruta: " + date[1]);
            }
            fire.add(fir);
        }

        try {
            for (Thread fir : fire) {
                fir.start();
            }
            pregatite.await(); // Toate cele 10 fire sunt active la enumerare.
            output.accept("\nENUMERAREA RECURSIVA A STRUCTURII:");
            int numar = enumera(main, "", output);
            output.accept("Total fire enumerate: " + numar);
            if (numar != DATE.length) {
                throw new IllegalStateException("Numar incorect de fire enumerate: " + numar);
            }
        } finally {
            eliberare.countDown(); // Deblocheaza firele inclusiv in caz de eroare.
            boolean intrerupt = false;
            for (Thread fir : fire) {
                while (fir.isAlive()) {
                    try {
                        fir.join();
                    } catch (InterruptedException e) {
                        intrerupt = true;
                    }
                }
            }
            if (intrerupt) {
                Thread.currentThread().interrupt();
            }
        }
        output.accept("Toate cele 10 fire s-au terminat.");
    }

    private static int enumera(ThreadGroup grup, String indent, Consumer<String> output) {
        output.accept(indent + "Grup: " + grup.getName() + " | prioritate maxima: " + grup.getMaxPriority());
        // activeCount() este estimativ: marim tabloul daca s-a umplut.
        Thread[] fire = new Thread[Math.max(1, grup.activeCount() + 1)];
        int n;
        while ((n = grup.enumerate(fire, false)) == fire.length) {
            fire = new Thread[fire.length * 2];
        }
        java.util.Arrays.sort(fire, 0, n, Comparator.comparing(Thread::getName));
        for (int i = 0; i < n; i++) {
            Thread fir = fire[i];
            output.accept(indent + "  Fir: " + fir.getName() + " | grup: "
                    + fir.getThreadGroup().getName() + " | prioritate: " + fir.getPriority());
        }
        int total = n;
        ThreadGroup[] subgrupe = new ThreadGroup[Math.max(1, grup.activeGroupCount() + 1)];
        while ((n = grup.enumerate(subgrupe, false)) == subgrupe.length) {
            subgrupe = new ThreadGroup[subgrupe.length * 2];
        }
        java.util.Arrays.sort(subgrupe, 0, n, Comparator.comparing(ThreadGroup::getName));
        for (int i = 0; i < n; i++) {
            total += enumera(subgrupe[i], indent + "  ", output);
        }
        return total;
    }

    static void asteapta(CountDownLatch semnal) {
        try {
            semnal.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
