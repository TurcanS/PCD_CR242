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

    // Creeaza un fir mediu sau avansat si ii aplica prioritatea din tabel +1.
    static Thread creeaza(boolean avansat, ThreadGroup grup, String nume,
                          int prioritateDinTabel, CountDownLatch terminare) {
        Thread fir;
        if (avansat) {
            fir = new FirAvansat(grup, nume, terminare);
        } else {
            // Folosim Thread cu Runnable, fara sa suprascriem Thread.run().
            fir = new Thread(grup, () -> asteapta(terminare), nume);
        }
        fir.setPriority(prioritateDinTabel + 1);
        return fir;
    }

    static void executa(boolean avansat) throws InterruptedException {
        afiseaza("\nVarianta 6 | nivel " + (avansat ? "avansat" : "mediu") + " | +1");

        // 1. Cream grupurile. Structura este aceeasi ca in partea colegului.
        // Formula din PDF are acolade gresite; interpretarea ramane de confirmat.
        ThreadGroup main = new ThreadGroup("Main");
        ThreadGroup g1 = new ThreadGroup(main, "G1");
        ThreadGroup g3 = new ThreadGroup(g1, "G3");
        ThreadGroup g2 = new ThreadGroup(g1, "G2");

        // Acest semnal tine firele active pana terminam enumerarea.
        CountDownLatch terminare = new CountDownLatch(1);

        // 2. Cream cele 10 fire, cu prioritatile initiale din conditie.
        Thread[] fire = {
            creeaza(avansat, g1, "ThA", 3, terminare),
            creeaza(avansat, g3, "Thf", 3, terminare),
            creeaza(avansat, g3, "Thb", 7, terminare),
            creeaza(avansat, g3, "Thc", 3, terminare),
            creeaza(avansat, g3, "Thd", 3, terminare),
            creeaza(avansat, g2, "Th8", 3, terminare),
            creeaza(avansat, g2, "Th9", 4, terminare),
            creeaza(avansat, g2, "Th3", 3, terminare),
            creeaza(avansat, main, "Th1", 3, terminare),
            creeaza(avansat, main, "Th2", 3, terminare)
        };

        try {
            // 3. Pornim firele si enumeram structura.
            for (Thread fir : fire) {
                fir.start();
            }
            afiseaza("Enumerarea grupurilor si firelor:");
            int total = enumera(main, "");
            afiseaza("Total fire enumerate: " + total);
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
        afiseaza(spatii + "Grup: " + grup.getName());
        // Varianta are exact 10 fire si 3 subgrupe; aceste tablouri sunt suficiente.
        Thread[] fire = new Thread[10];
        int numar = grup.enumerate(fire, false);
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
