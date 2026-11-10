package lab3;

import java.util.function.Consumer;

/**
 * Firul 2 (Cornos Ilie) - Sarcina 2, varianta 4:
 * sumele pozițiilor numerelor impare, două câte două, începând căutarea și sumarea de la ultimul element.
 *
 * Sincronizare (metodele clasei Thread):
 *  - sleep() întrerupt prin interrupt(): după sarcina 2, firul „doarme” până când Thread1 îl trezește
 *    cu interrupt(), adică până când și sarcina 1 este gata. Abia apoi afișează numele.
 */
public class Thread2 extends Thread {

    private static final String NUMELE = "Numele: Cornos, Spranceana";

    private final int[] mas;
    private final Consumer<String> linii;
    private final Consumer<String> litere;

    public Thread2(int[] mas, Consumer<String> linii, Consumer<String> litere) {
        super("Th2");
        this.mas = mas;
        this.linii = linii;
        this.litere = litere;
    }

    @Override
    public void run() {
        linii.accept("Starting Thread 2");

        // 1. Sarcina 2: parcurgem tabloul de la ultimul element spre primul
        int prima = -1; // poziția primului număr impar din perechea curentă (-1 = încă nu avem)
        for (int i = mas.length - 1; i >= 0; i--) {
            if (mas[i] % 2 == 0) {
                continue; // numărul este par, îl sărim
            }
            if (prima == -1) {
                prima = i;
            } else {
                linii.accept("poz " + prima + " + poz " + i + " = " + (prima + i)
                        + "   (numerele " + mas[prima] + " și " + mas[i] + ")");
                prima = -1;
                pauzaLinie();
            }
        }
        linii.accept("Sarcina 2 este gata. Aștept semnalul de la Th1...");

        // 2. Aștept semnalul lui Thread1: sleep() se oprește cu InterruptedException când Th1 apelează interrupt().
        //    Dacă Th1 a trimis semnalul mai devreme, indicatorul de întrerupere e deja setat și sleep() iese imediat.
        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            linii.accept("Am primit semnalul: sarcina 1 este gata.");
        }

        // 3. Afișez numele, câte o literă la 100 ms; apoi run() se termină și firul se încheie
        for (char litera : ("Th2 - " + NUMELE + "\n").toCharArray()) {
            litere.accept(String.valueOf(litera));
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                return;
            }
        }
    }

    /**
     * Pauză mică între linii, doar ca să se vadă pe ecran că firele rulează în paralel.
     * Dacă Th1 trimite semnalul chiar în timpul pauzei, îl păstrez (interrupt()), ca să nu se piardă:
     * sleep() de mai jos, care așteaptă semnalul, se va opri imediat.
     */
    private void pauzaLinie() {
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            interrupt();
        }
    }
}
