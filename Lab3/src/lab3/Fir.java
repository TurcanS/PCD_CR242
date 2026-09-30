package lab3;

import java.util.function.Consumer;

/**
 * Partea comună a celor patru fire de execuție. Sincronizarea se face numai cu metodele clasei Thread.
 *
 * Semnalul „sarcina mea este gata” se transmite prin interrupt() pe lanțul Th1 -> Th3 -> Th4 -> Th2,
 * deci Th2 îl primește abia după ce toate cele patru sarcini s-au terminat.
 * Textele se afișează apoi în ordinea Th2, Th4, Th1, Th3: fiecare fir așteaptă terminarea firului precedent.
 */
abstract class Fir extends Thread {

    private static final int INTERVAL_LITERE_MS = 100;

    private final Consumer<String> linii;
    private final Consumer<String> litere;
    private final String text;
    private boolean semnalPrimit;

    /** Firul căruia îi transmit semnalul că sarcinile sunt gata. */
    protected Thread urmatorul;
    /** Firul după terminarea căruia îmi afișez textul. */
    protected Thread precedent;

    Fir(String nume, String text, Consumer<String> linii, Consumer<String> litere) {
        super(nume);
        this.text = text;
        this.linii = linii;
        this.litere = litere;
    }

    void leaga(Thread urmatorul, Thread precedent) {
        this.urmatorul = urmatorul;
        this.precedent = precedent;
    }

    protected void afiseaza(String linie) {
        linii.accept(linie);
    }

    /** Pauză; dacă între timp firul a fost întrerupt, reține că semnalul a sosit. */
    protected void pauza(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            semnalPrimit = true;
        }
    }

    /** Firul „doarme” până când este întrerupt prin interrupt() de firul dinaintea lui din lanț. */
    protected void asteaptaSemnal() {
        while (!semnalPrimit) {
            pauza(Long.MAX_VALUE);
        }
    }

    /** Afișează textul firului, câte o literă la 100 ms. */
    protected void afiseazaText() {
        for (char litera : (getName() + " - " + text + "\n").toCharArray()) {
            litere.accept(String.valueOf(litera));
            pauza(INTERVAL_LITERE_MS);
        }
    }
}
