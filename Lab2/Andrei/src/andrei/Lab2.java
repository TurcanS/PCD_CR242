package andrei;

import java.util.concurrent.CountDownLatch;

public class Lab2 {
    // Pastram mesajele pentru a le arata si in fereastra.
    static final StringBuilder rezultat = new StringBuilder();

    static synchronized void afiseaza(String mesaj) {
        System.out.println(mesaj);
        rezultat.append(mesaj).append("\n");
    }

    static void asteapta(CountDownLatch terminare) {
        try {
            terminare.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Creeaza firul si ii seteaza direct prioritatea finala.
    static Thread creeaza(boolean avansat, ThreadGroup grup, String nume,
                          int prioritate, CountDownLatch terminare) {
        Thread fir;
        if (avansat) {
            fir = new FirAvansat(grup, nume, terminare);
        } else {
            // Folosim Thread cu Runnable, fara sa suprascriem Thread.run().
            fir = new Thread(grup, () -> asteapta(terminare), nume);
        }
        fir.setPriority(prioritate);
        return fir;
    }

    static void executa(boolean avansat) throws InterruptedException {
        afiseaza("\n--- " + (avansat ? "2. NIVEL AVANSAT" : "1. NIVEL MEDIU") + " ---");

        // 1. Cream grupurile. Structura este aceeasi ca in partea colegului.
        // Formula din PDF are acolade gresite; interpretarea ramane de confirmat.
        ThreadGroup main = new ThreadGroup("Main");
        ThreadGroup g1 = new ThreadGroup(main, "G1");
        ThreadGroup g3 = new ThreadGroup(g1, "G3");
        ThreadGroup g2 = new ThreadGroup(g1, "G2");

        // Acest semnal tine firele active pana terminam enumerarea.
        CountDownLatch terminare = new CountDownLatch(1);

        // 2. Cream cele 10 fire cu prioritatile finale: Thb=8, Th9=5, restul=4.
        Thread[] fire = {
            creeaza(avansat, g1, "ThA", 4, terminare),
            creeaza(avansat, g3, "Thf", 4, terminare),
            creeaza(avansat, g3, "Thb", 8, terminare),
            creeaza(avansat, g3, "Thc", 4, terminare),
            creeaza(avansat, g3, "Thd", 4, terminare),
            creeaza(avansat, g2, "Th8", 4, terminare),
            creeaza(avansat, g2, "Th9", 5, terminare),
            creeaza(avansat, g2, "Th3", 4, terminare),
            creeaza(avansat, main, "Th1", 4, terminare),
            creeaza(avansat, main, "Th2", 4, terminare)
        };

        try {
            // 3. Pornim firele si enumeram structura.
            for (Thread fir : fire) {
                fir.start();
            }
            afiseaza("Enumerarea grupurilor si firelor:");
            int total = enumera(main, "");
            afiseaza("Total fire enumerate: " + total);
            // list() afiseaza direct in consola grupul, subgrupele si firele active.
            System.out.println("Structura afisata cu list():");
            main.list();
        } finally {
            // 4. Dam voie firelor sa termine, inclusiv daca apare o eroare.
            terminare.countDown();
            for (Thread fir : fire) {
                fir.join();
            }
        }
        afiseaza("Toate cele 10 fire s-au terminat.");
    }

    // Afisam grupul, firele sale directe, apoi fiecare subgrup.
    static int enumera(ThreadGroup grup, String spatii) {
        // Varianta are exact 10 fire si 3 subgrupe; aceste tablouri sunt suficiente.
        Thread[] fire = new Thread[10];
        int numar = grup.enumerate(fire, false);
        afiseaza(spatii + "Grup: " + grup.getName()
                + " | prioritate maxima: " + grup.getMaxPriority()
                + " | fire directe: " + numar);
        for (int i = 0; i < numar; i++) {
            afiseaza(spatii + "  Fir: " + fire[i].getName()
                    + " | grup: " + fire[i].getThreadGroup().getName()
                    + " | prioritate: " + fire[i].getPriority());
        }

        ThreadGroup[] subgrupe = new ThreadGroup[3];
        int numarGrupe = grup.enumerate(subgrupe, false);
        int total = numar;
        for (int i = 0; i < numarGrupe; i++) {
            total += enumera(subgrupe[i], spatii + "  ");
        }
        return total;
    }
}
