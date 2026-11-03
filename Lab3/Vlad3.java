/*
 * PARTEA MEA: Thread 3 si Thread 4 (Varianta 7)
 *
 * Metode de sincronizare folosite (doar din clasa Thread):
 *   1. join()                - Th4 asteapta Th2 (Numele sa fie afisat primul);
 *                              Th3 asteapta Th1 (Prenumele sa fie afisat inaintea disciplinei)
 *   2. interrupt() + sleep() - Th3, dupa sarcina, "doarme" pana cand Th4 il trezeste cu
 *                              interrupt() (semnal ca Grupa a fost afisata)
 *   (in plus: isAlive() + yield() - Th4 se asigura ca Th3 a pornit inainte de interrupt())
 *   0. BARIERA: dupa sarcina, fiecare fir apeleaza Main2.awaitAllTasks() = join() pe firul "gate",
 *      care se termina doar cand toate 4 firele sunt in starea WAITING (getState()).
 *      Astfel textele (Nume, Grupa, Prenume, Disciplina) apar abia dupa ce TOATE firele
 *      si-au terminat sarcinile.
 *
 * Ordinea globala: Th2 (Nume) -> Th4 (Grupa) -> Th1 (Prenume) -> Th3 (Disciplina)
 *
 * Contract pentru Adrian3.java:
 *   - dupa sarcina, FIECARE fir (si Th1, si Th2) apeleaza Main2.awaitAllTasks()
 *     inainte de orice afisare de text;
 *   - Th2 isi afiseaza Numele imediat dupa bariera (nu asteapta pe nimeni altcineva);
 *   - Th1 trebuie sa faca Main2.th4.join() inainte sa-si afiseze Prenumele;
 *   - in Main2 firele pornesc in ordinea Th2, Th4, Th1, Th3 (a nu se schimba,
 *     altfel join() pe un fir nepornit nu asteapta).
 */

/** Thread 3 - Sarcina 3: parcurge intervalul [234, 1000] de la inceput spre sfarsit. */
class Thread3 extends Thread {

    Thread3() {
        super("Th3");
    }

    @Override
    public void run() {
        Main2.log(3, "Starting Thread 3");

        // Daca Th4 trimite interrupt() cat timp Th3 inca lucreaza, il retinem aici
        boolean signaled = false;

        // ----- Sarcina 3: parcurgere de la inceput -----
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
                signaled = true;
            }
        }
        if (line.length() > 0) {
            Main2.log(3, line.toString());
        }
        Main2.log(3, "--- Sarcina 3 terminata, astept celelalte fire ---");

        // ----- Bariera: join() pe firul "gate" -----
        // Nimic nu se afiseaza pana cand toate cele 4 fire si-au terminat sarcina
        if (Main2.awaitAllTasks()) {
            signaled = true;
        }
        Main2.log(3, "--- toate sarcinile terminate, astept semnalul de la Th4 (interrupt) ---");

        // ----- Sincronizare 1: interrupt() + sleep() -----
        // Asteptam semnalul de la Th4 (Grupa afisata)
        if (!signaled) {
            try {
                Thread.sleep(Long.MAX_VALUE);
            } catch (InterruptedException e) {
                // semnal primit de la Th4
            }
        }

        // ----- Sincronizare 2: join() -----
        // Asteptam ca Th1 sa-si termine tot (inclusiv afisarea Prenumelui)
        try {
            Main2.th1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // ----- Afisarea denumirii disciplinei (ultima in lant) -----
        Main2.printSlow(Main2.DISCIPLINA);
        Main2.log(3, "--- Th3 finalizat ---");
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

        // ----- Sarcina 4: parcurgere de la sfarsit -----
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
        Main2.log(4, "--- Sarcina 4 terminata, astept celelalte fire ---");

        // ----- Bariera: join() pe firul "gate" -----
        // Nimic nu se afiseaza pana cand toate cele 4 fire si-au terminat sarcina
        Main2.awaitAllTasks();
        Main2.log(4, "--- toate sarcinile terminate, astept Th2 (join) ---");

        // ----- Sincronizare 1: join() -----
        // Asteptam ca Th2 sa termine (dupa el se afiseaza Numele, apoi Grupa)
        try {
            Main2.th2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // ----- Afisarea grupei -----
        Main2.printSlow(Main2.GRUPA);

        // ----- Sincronizare 2: interrupt() -----
        // Trezim Th3 (el asteapta in sleep) - Grupa a fost afisata.
        // Ne asiguram ca Th3 a pornit (isAlive + yield), altfel interrupt() pe un fir
        // nepornit poate fi pierdut.
        while (!Main2.th3.isAlive()) {
            Thread.yield();
        }
        Main2.th3.interrupt();
        Main2.log(4, "--- Th4 finalizat (Th3 a fost trezit cu interrupt) ---");
    }
}