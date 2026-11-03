/*
 * PARTEA COLEGULUI (Adrian): Thread 1 si Thread 2 (Varianta 7)
 *
 * !!! Versiune de lucru, ca programul sa ruleze - Adrian o inlocuieste/completeaza cu codul lui. !!!
 *
 * Ce trebuie sa respecte codul final:
 *   - clasele se numesc Thread1 si Thread2 (extend Thread), constructori fara parametri;
 *   - dupa sarcina, fiecare fir apeleaza Main2.awaitAllTasks() (bariera) INAINTE de orice text,
 *     ca Numele/Grupa/Prenumele/Disciplina sa apara abia dupa ce toate firele au terminat;
 *   - Th2 isi afiseaza Numele (Main2.NUME) imediat dupa bariera, fara sa astepte pe nimeni altcineva;
 *   - Th1 face Main2.th4.join() inainte sa-si afiseze Prenumele (Main2.PRENUME);
 *   - textele se afiseaza cu Main2.printSlow(...), iar liniile din sarcini cu Main2.log(n, ...);
 *   - cele 2 metode de sincronizare ale lui Adrian trebuie sa fie diferite si doar din clasa Thread
 *     (aici e folosit doar join(); de adaugat inca una, de ex. isAlive()/interrupt()/setPriority()).
 *
 * Sarcinile (Varianta 7): sumele produselor numerelor pare, doua cate doua:
 *   Th1 - incepand cu primul element:  (a[i]*a[i+1] + a[i+2]*a[i+3]), i = 0, 4, 8, ...
 *   Th2 - incepand cu ultimul element: (a[i]*a[i-1] + a[i-2]*a[i-3]), i = n-1, n-5, ...
 * unde a = numerele pare din tabloul generat in Main2 (Main2.evens).
 */

class Thread1 extends Thread {

    Thread1() {
        super("Th1");
    }

    @Override
    public void run() {
        Main2.log(1, "Starting Thread 1");

        // ----- Sarcina 1: de la primul element -----
        int[] a = Main2.evens;
        for (int i = 0; i + 3 < a.length; i += 4) {
            int p = a[i] * a[i + 1];
            int q = a[i + 2] * a[i + 3];
            Main2.log(1, "(" + a[i] + "*" + a[i + 1] + " + " + a[i + 2] + "*" + a[i + 3]
                    + ") = " + (p + q));
            try {
                Thread.sleep(Main2.STEP_DELAY_MS * 10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Main2.log(1, "--- Sarcina 1 terminata, astept celelalte fire ---");

        // ----- Bariera: textele apar abia dupa ce TOATE firele si-au terminat sarcina -----
        Main2.awaitAllTasks();
        Main2.log(1, "--- toate sarcinile terminate, astept Th4 (join) ---");

        // ----- Sincronizare: join() -----
        try {
            Main2.th4.join(); // Th1 afiseaza Prenumele dupa Th4 (Grupa)
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Main2.printSlow(Main2.PRENUME);
        Main2.log(1, "--- Th1 finalizat ---");
    }
}

class Thread2 extends Thread {

    Thread2() {
        super("Th2");
    }

    @Override
    public void run() {
        Main2.log(2, "Starting Thread 2");

        // ----- Sarcina 2: de la ultimul element -----
        int[] a = Main2.evens;
        for (int i = a.length - 1; i - 3 >= 0; i -= 4) {
            int p = a[i] * a[i - 1];
            int q = a[i - 2] * a[i - 3];
            Main2.log(2, "(" + a[i] + "*" + a[i - 1] + " + " + a[i - 2] + "*" + a[i - 3]
                    + ") = " + (p + q));
            try {
                Thread.sleep(Main2.STEP_DELAY_MS * 10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Main2.log(2, "--- Sarcina 2 terminata, astept celelalte fire ---");

        // ----- Bariera: textele apar abia dupa ce TOATE firele si-au terminat sarcina -----
        Main2.awaitAllTasks();

        Main2.printSlow(Main2.NUME); // primul text afisat
        Main2.log(2, "--- Th2 finalizat ---");
    }
}