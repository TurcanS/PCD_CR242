/*
 * PARTEA MEA: Thread 3 si Thread 4 (Varianta 7)
 *
 * Fiecare fir are doua clase:
 *   - Thread3 / Thread4 : FAZA 1 - sarcina (parcurgerea intervalului); firul se termina singur;
 *   - Print3  / Print4  : FAZA 2 - afisarea textului (Disciplina / Grupa) + sincronizarea.
 * Firele Print pornesc abia dupa ce Main2 a facut join() pe toate cele 4 fire de sarcina.
 *
 * Metodele mele de sincronizare (doar din clasa Thread):
 *   1. join()                - Print4 asteapta Print2 (Numele sa fie afisat primul);
 *                              Print3 asteapta Print1 (Prenumele sa fie afisat inaintea disciplinei)
 *   2. interrupt() + sleep() - Print3 "doarme" pana cand Print4 il trezeste cu interrupt()
 *                              (semnal ca Grupa a fost afisata)
 *
 * Metodele lui Adrian: join() (comuna cu mine) si isAlive() (a lui).
 * Deci in total: join, interrupt, isAlive. Main2 foloseste doar start() si join().
 *
 * Ordinea globala: Th2 (Nume) -> Th4 (Grupa) -> Th1 (Prenume) -> Th3 (Disciplina)
 *
 * Contract pentru Adrian3.java:
 *   - Thread1/Thread2 fac doar sarcina si se termina (fara afisare de text);
 *   - Print2 isi afiseaza Numele imediat si se TERMINA (Print4 face join() pe el);
 *   - Print1 se termina abia dupa ce afiseaza Prenumele (Print3 face join() pe el);
 *   - Print1 asteapta terminarea lui Print4 inainte sa-si afiseze Prenumele;
 *   - Main2 porneste firele Print in ordinea p2, p4, p3, p1 (a nu se schimba,
 *     altfel join()/isAlive()/interrupt() pe un fir nepornit nu functioneaza).
 */

/** Thread 3 - Sarcina 3: parcurge intervalul [234, 1000] de la inceput spre sfarsit. */
class Thread3 extends Thread {

    Thread3() {
        super("Th3");
    }

    @Override
    public void run() {
        Main2.log(3, "Starting Thread 3");

        StringBuilder line = new StringBuilder();
        int inLine = 0;
        for (int i = Main2.T3_FROM; i <= Main2.T3_TO; i++) {
            line.append(getName()).append(':').append(i).append(' ');
            inLine++;
            if (inLine == 10) {
                Main2.log(3, line.toString());
                line.setLength(0);
                inLine = 0;
            }
            try {
                Thread.sleep(Main2.STEP_DELAY_MS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (line.length() > 0) {
            Main2.log(3, line.toString());
        }
        Main2.log(3, "--- Sarcina 3 terminata ---");
    }
}

/** Thread 4 - Sarcina 4: parcurge intervalul [456, 1234] de la sfarsit spre inceput. */
class Thread4 extends Thread {

    Thread4() {
        super("Th4");
    }

    @Override
    public void run() {
        Main2.log(4, "Starting Thread 4");

        StringBuilder line = new StringBuilder();
        int inLine = 0;
        for (int i = Main2.T4_TO; i >= Main2.T4_FROM; i--) {
            line.append(getName()).append(':').append(i).append(' ');
            inLine++;
            if (inLine == 10) {
                Main2.log(4, line.toString());
                line.setLength(0);
                inLine = 0;
            }
            try {
                Thread.sleep(Main2.STEP_DELAY_MS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (line.length() > 0) {
            Main2.log(4, line.toString());
        }
        Main2.log(4, "--- Sarcina 4 terminata ---");
    }
}

/** Afisarea disciplinei (ultima in lant) - Th3. */
class Print3 extends Thread {

    Print3() {
        super("Th3");
    }

    @Override
    public void run() {
        Main2.log(3, "Afisare: astept semnalul de la Th4 (interrupt)");

        // ----- Sincronizare 1: interrupt() + sleep() -----
        // Print3 doarme pana cand Print4 il trezeste cu interrupt() (Grupa afisata).
        // Daca interrupt() a venit deja, flagul e setat si sleep() arunca imediat exceptia,
        // deci semnalul nu se pierde.
        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            // semnal primit de la Th4
        }
        Main2.log(3, "Semnal primit de la Th4; astept Th1 (join)");

        // ----- Sincronizare 2: join() -----
        // Asteptam ca Print1 sa-si termine tot (inclusiv afisarea Prenumelui)
        try {
            Main2.p1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Main2.printSlow(Main2.DISCIPLINA);
        Main2.log(3, "--- Th3 finalizat ---");
    }
}

/** Afisarea grupei - Th4. */
class Print4 extends Thread {

    Print4() {
        super("Th4");
    }

    @Override
    public void run() {
        Main2.log(4, "Afisare: astept Th2 (join)");

        // ----- Sincronizare 1: join() -----
        // Asteptam ca Print2 sa termine (dupa el se afiseaza Numele, apoi Grupa)
        try {
            Main2.p2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Main2.printSlow(Main2.GRUPA);

        // ----- Sincronizare 2: interrupt() -----
        // Trezim Print3 (el doarme in sleep) - Grupa a fost afisata.
        // Print3 a fost pornit inaintea noastra (ordinea p2, p4, p3, p1), iar daca nu a ajuns
        // inca in sleep(), flagul de intrerupere ramane setat si semnalul nu se pierde.
        Main2.p3.interrupt();
        Main2.log(4, "--- Th4 finalizat (Th3 a fost trezit cu interrupt) ---");
    }
}