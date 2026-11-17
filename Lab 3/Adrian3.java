/*
 * PARTEA COLEGULUI (Adrian): Thread 1 si Thread 2 (Varianta 7)
 *
 * Fiecare fir are doua clase:
 *   - Thread1 / Thread2 : FAZA 1 - sarcina (sume de produse); firul se termina singur;
 *   - Print1  / Print2  : FAZA 2 - afisarea textului (Prenume / Nume) + sincronizarea.
 * Firele Print pornesc abia dupa ce Main2 a facut join() pe toate cele 4 fire de sarcina.
 *
 * Metodele lui Adrian de sincronizare (doar din clasa Thread):
 *   1. join()    - Print1 asteapta terminarea lui Print2 (Numele afisat)  [comuna cu Vlad - singura permisa]
 *   2. isAlive() - Print1 asteapta (polling) terminarea lui Print4 (Grupa afisata)   [doar la Adrian]
 *
 * Vlad foloseste: join() si interrupt(). In total: join, interrupt, isAlive.
 *
 * Contractul cu partea lui Vlad:
 *   - clasele se numesc Thread1, Thread2, Print1, Print2 (extend Thread), constructori fara parametri;
 *   - Thread1/Thread2 fac doar sarcina si se termina (fara afisare de text);
 *   - Print2 isi afiseaza Numele imediat si se TERMINA (Print4 face join() pe el);
 *   - Print1 isi afiseaza Prenumele dupa Print2 si Print4 si se TERMINA (Print3 face join() pe el);
 *   - textele se afiseaza cu Main2.printSlow(...), iar liniile din sarcini cu Main2.log(n, ...);
 *   - Main2 porneste firele Print in ordinea p2, p4, p3, p1 (p4 trebuie pornit inaintea lui p1,
 *     altfel isAlive() ar intoarce false imediat).
 *
 * Sarcinile (Varianta 7): sumele produselor numerelor pare, doua cate doua:
 *   Th1 - incepand cu primul element:  (a[i]*a[i+1] + a[i+2]*a[i+3]), i = 0, 4, 8, ...
 *   Th2 - incepand cu ultimul element: (a[i]*a[i-1] + a[i-2]*a[i-3]), i = n-1, n-5, ...
 * unde a = numerele pare din tabloul generat in Main2 (Main2.numbers).
 * Filtrarea numerelor pare se face AICI (clasa EvenNumbers), nu in Main2.
 */

/** Filtrarea numerelor pare dintr-un tablou (apelata de Th1 si Th2, fiecare pe copia lui). */
class EvenNumbers {
    static int[] of(int[] src) {
        int count = 0;
        for (int v : src) {
            if (v % 2 == 0) {
                count++;
            }
        }
        int[] result = new int[count];
        int k = 0;
        for (int v : src) {
            if (v % 2 == 0) {
                result[k++] = v;
            }
        }
        return result;
    }
}

/** Thread 1 - Sarcina 1: de la primul element (doar numerele pare). */
class Thread1 extends Thread {

    Thread1() {
        super("Th1");
    }

    @Override
    public void run() {
        Main2.log(1, "Starting Thread 1");

        int[] a = EvenNumbers.of(Main2.numbers);
        Main2.log(1, "Numere pare: " + a.length);
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
        Main2.log(1, "--- Sarcina 1 terminata ---");
    }
}

/** Thread 2 - Sarcina 2: de la ultimul element (doar numerele pare). */
class Thread2 extends Thread {

    Thread2() {
        super("Th2");
    }

    @Override
    public void run() {
        Main2.log(2, "Starting Thread 2");

        int[] a = EvenNumbers.of(Main2.numbers);
        Main2.log(2, "Numere pare: " + a.length);
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
        Main2.log(2, "--- Sarcina 2 terminata ---");
    }
}

/** Afisarea prenumelui - Th1. */
class Print1 extends Thread {

    Print1() {
        super("Th1");
    }

    @Override
    public void run() {
        Main2.log(1, "Afisare: astept Th2 (join) si Th4 (isAlive)");

        // ----- Sincronizare 1: join() -----
        // Prenumele se afiseaza dupa Nume => asteptam terminarea lui Print2
        try {
            Main2.p2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // ----- Sincronizare 2: isAlive() -----
        // Prenumele se afiseaza dupa Grupa => asteptam (polling) pana cand Print4 nu mai e viu
        while (Main2.p4.isAlive()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Main2.printSlow(Main2.PRENUME);
        Main2.log(1, "--- Th1 finalizat ---");
    }
}

/** Afisarea numelui - Th2 (primul text afisat). */
class Print2 extends Thread {

    Print2() {
        super("Th2");
    }

    @Override
    public void run() {
        Main2.log(2, "Afisare: nu astept pe nimeni, afisez Numele");
        Main2.printSlow(Main2.NUME);
        Main2.log(2, "--- Th2 finalizat ---");
    }
}
