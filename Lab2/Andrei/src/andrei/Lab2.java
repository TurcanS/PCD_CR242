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

        // 2. Elementele de pe aceeasi pozitie descriu acelasi fir.
        // De exemplu, pozitia 4: Thb, grupul g3, prioritatea 8.
        String[] nume = {
            "Th1", "Th2", "ThA", "Thf", "Thb", "Thc", "Thd", "Th8", "Th9", "Th3"
        };
        ThreadGroup[] grupe = { main, main, g1, g3, g3, g3, g3, g2, g2, g2 };
        int[] prioritati =    {    4,    4,  4,  4,  8,  4,  4,  4,  5,  4 };

        Thread[] fire = new Thread[nume.length];
        for (int i = 0; i < nume.length; i++) {
            if (avansat) {
                fire[i] = new FirAvansat(grupe[i], nume[i], terminare);
            } else {
                // Nivel mediu: Thread cu Runnable, fara suprascrierea Thread.run().
                fire[i] = new Thread(grupe[i], () -> asteapta(terminare), nume[i]);
            }
            fire[i].setPriority(prioritati[i]);
        }

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
