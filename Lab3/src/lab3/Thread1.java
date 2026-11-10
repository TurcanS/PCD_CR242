package lab3;

import java.util.function.Consumer;

/**
 * Firul 1 (Cornos Ilie) - Sarcina 1, varianta 4:
 * sumele pozițiilor numerelor impare, două câte două, începând căutarea și sumarea de la primul element.
 *
 * Sincronizare (metodele clasei Thread):
 *  - interrupt(): după sarcină îi transmite lui Thread2 semnalul „sarcina 1 este gata”;
 *  - join():      înainte de prenume așteaptă terminarea lui Thread4 (care a afișat grupa).
 */
public class Thread1 extends Thread {

    private static final String PRENUMELE = "Prenumele: Ilie, Marius";

    private final int[] mas;
    private final Thread thread2; // perechea mea: îi trimit semnal prin interrupt()
    private final Thread thread4; // după terminarea lui afișez prenumele
    private final Consumer<String> linii;
    private final Consumer<String> litere;

    public Thread1(int[] mas, Thread thread2, Thread thread4, Consumer<String> linii, Consumer<String> litere) {
        super("Th1");
        this.mas = mas;
        this.thread2 = thread2;
        this.thread4 = thread4;
        this.linii = linii;
        this.litere = litere;
    }

    @Override
    public void run() {
        linii.accept("Starting Thread 1");

        // 1. Sarcina 1: parcurgem tabloul de la primul element spre ultimul
        int prima = -1; // poziția primului număr impar din perechea curentă (-1 = încă nu avem)
        for (int i = 0; i < mas.length; i++) {
            if (mas[i] % 2 == 0) {
                continue; // numărul este par, îl sărim
            }
            if (prima == -1) {
                prima = i; // am găsit primul număr impar din pereche
            } else {
                linii.accept("poz " + prima + " + poz " + i + " = " + (prima + i)
                        + "   (numerele " + mas[prima] + " și " + mas[i] + ")");
                prima = -1; // începem o pereche nouă
                pauzaLinie();
            }
        }
        linii.accept("Sarcina 1 este gata.");

        // ==================================================================
        // SINCRONIZARE 1 (interrupt): TRIMIT SEMNALUL către Th2.
        // Sarcina 1 e gata, deci îi spun lui Th2 „poți afișa numele”.
        // Th2 doarme în sleep() și se trezește când primește interrupt().
        // ==================================================================
        thread2.interrupt();   // <<< AICI: Th1 îl trezește pe Th2

        // ==================================================================
        // SINCRONIZARE 2 (join): AȘTEPT să se termine Th4.
        // Th4 a afișat deja grupa; abia după el afișez eu prenumele.
        // ==================================================================
        try {
            // <<< AICI: Th1 stă blocat până când Th4 se termină
            thread4.join();       // <<< AICI: așteptarea (join)
        } catch (InterruptedException e) {
            return; // nu ar trebui să se întâmple: nimeni nu îl întrerupe pe Thread1
        }

        // 4. Afișez prenumele, câte o literă la 100 ms; apoi run() se termină și firul se încheie
        for (char litera : ("Th1 - " + PRENUMELE + "\n").toCharArray()) {
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
            Thread.sleep(20);
        } catch (InterruptedException e) {
            // nimeni nu întrerupe acest fir în timpul sarcinii
        }
    }
}
