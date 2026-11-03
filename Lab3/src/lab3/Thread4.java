package lab3;

import java.util.function.Consumer;

/**
 * Firul 4 (Spranceana Marius) - Sarcina 4, varianta 4: de parcurs de la sfârșit intervalul [123, 890].
 *
 * Sincronizare (metodele clasei Thread):
 *  - isAlive() + sleep(): înainte de grupă verifică la fiecare 100 ms dacă Thread2 mai trăiește
 *    și continuă abia după ce Thread2 s-a terminat (adică după ce a afișat numele).
 */
public class Thread4 extends Thread {

    private static final String GRUPA = "Grupa: CR-242";
    private static final int NUMERE_PE_LINIE = 15;

    private final Thread thread2; // după terminarea lui afișez grupa
    private final Consumer<String> linii;
    private final Consumer<String> litere;

    public Thread4(Thread thread2, Consumer<String> linii, Consumer<String> litere) {
        super("Th4");
        this.thread2 = thread2;
        this.linii = linii;
        this.litere = litere;
    }

    @Override
    public void run() {
        linii.accept("Starting Thread 4");

        // 1. Sarcina 4: parcurgem intervalul [123, 890] de la sfârșit (descrescător)
        StringBuilder linie = new StringBuilder();
        int peLinie = 0;
        for (int i = 890; i >= 123; i--) {
            linie.append(i).append(' ');
            peLinie++;
            if (peLinie == NUMERE_PE_LINIE || i == 123) {
                linii.accept(linie.toString());
                linie.setLength(0);
                peLinie = 0;
            }
        }
        linii.accept("Sarcina 4 este gata. Aștept terminarea lui Th2...");

        // 2. Cât timp Thread2 este încă activ, verific din nou peste 100 ms
        while (thread2.isAlive()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                return; // nu ar trebui să se întâmple: nimeni nu îl întrerupe pe Thread4
            }
        }

        // 3. Afișez grupa, câte o literă la 100 ms; apoi firul se încheie
        for (char litera : ("Th4 - " + GRUPA + "\n").toCharArray()) {
            litere.accept(String.valueOf(litera));
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                return;
            }
        }
    }
}
