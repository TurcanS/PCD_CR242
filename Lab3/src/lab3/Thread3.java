package lab3;

import java.util.function.Consumer;

/**
 * Firul 3 (Spranceana Marius) - Sarcina 3, varianta 4: de parcurs de la început intervalul [234, 987].
 *
 * Sincronizare (metodele clasei Thread):
 *  - join(): înainte de denumirea disciplinei așteaptă terminarea firelor Thread1, Thread2 și Thread4.
 */
public class Thread3 extends Thread {

    private static final String DISCIPLINA = "Programarea concurentă și distribuită";
    private static final int NUMERE_PE_LINIE = 15;

    private final Thread thread1;
    private final Thread thread2;
    private final Thread thread4;
    private final Consumer<String> linii;
    private final Consumer<String> litere;

    public Thread3(Thread thread1, Thread thread2, Thread thread4, Consumer<String> linii, Consumer<String> litere) {
        super("Th3");
        this.thread1 = thread1;
        this.thread2 = thread2;
        this.thread4 = thread4;
        this.linii = linii;
        this.litere = litere;
    }

    @Override
    public void run() {
        linii.accept("Starting Thread 3");

        // 1. Sarcina 3: parcurgem intervalul [234, 987] de la început (crescător)
        StringBuilder linie = new StringBuilder();
        int peLinie = 0;
        for (int i = 234; i <= 987; i++) {
            linie.append(i).append(' ');
            peLinie++;
            if (peLinie == NUMERE_PE_LINIE || i == 987) {
                linii.accept(linie.toString());
                linie.setLength(0);
                peLinie = 0;
                pauzaLinie();
            }
        }
        linii.accept("Sarcina 3 este gata. Aștept terminarea firelor Th1, Th2 și Th4...");

        // 2. Aștept terminarea tuturor celorlalte trei fire
        try {
            thread1.join();
            thread2.join();
            thread4.join();
        } catch (InterruptedException e) {
            return; // nu ar trebui să se întâmple: nimeni nu îl întrerupe pe Thread3
        }

        // 3. Afișez denumirea disciplinei, câte o literă la 100 ms; apoi firul se încheie
        for (char litera : ("Th3 - Disciplina: " + DISCIPLINA + "\n").toCharArray()) {
            litere.accept(String.valueOf(litera));
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                return;
            }
        }
    }

    /** Pauză mică între linii, doar ca să se vadă pe ecran că firele rulează în paralel (nu sincronizează nimic). */
    private static void pauzaLinie() {
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            // nimeni nu întrerupe acest fir în timpul sarcinii
        }
    }
}
