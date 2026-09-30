package lab5;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * Depozitul comun de capacitate D, construit pe o coadă blocantă (ArrayBlockingQueue).
 * Coada asigură singură sincronizarea: put() așteaptă cât depozitul este plin, take() cât este gol.
 */
final class Depozit {

    private final int capacitate;
    private final BlockingQueue<Character> coada;
    private final AtomicInteger produse = new AtomicInteger();
    private final AtomicInteger consumate = new AtomicInteger();
    private final Consumer<String> jurnal;
    private final Consumer<String> stare;

    /**
     * @param jurnal primește mesajele despre producere, consumare și cazurile „gol” / „plin”
     * @param stare  primește conținutul depozitului după fiecare modificare
     */
    Depozit(int capacitate, Consumer<String> jurnal, Consumer<String> stare) {
        this.capacitate = capacitate;
        this.coada = new ArrayBlockingQueue<>(capacitate);
        this.jurnal = jurnal;
        this.stare = stare;
    }

    /** Pune un obiect în depozit; dacă depozitul este plin, firul așteaptă. */
    void pune(String producator, char obiect) throws InterruptedException {
        // mesajul se scrie înainte de punere, ca în jurnal producerea să apară mereu înaintea consumării
        jurnal.accept(producator + " a produs: " + obiect + "   | total produse: " + produse.incrementAndGet());
        if (!coada.offer(obiect)) { // offer() nu blochează: întoarce false când depozitul este plin
            jurnal.accept("!!! Depozitul este PLIN - " + producator + " așteaptă");
            coada.put(obiect); // put() blochează firul până când apare un loc liber
        }
        stare.accept(sir());
    }

    /** Ia un obiect din depozit; dacă depozitul este gol, firul așteaptă. */
    char ia(String consumator) throws InterruptedException {
        Character obiect = coada.poll(); // poll() nu blochează: întoarce null când depozitul este gol
        if (obiect == null) {
            jurnal.accept("!!! Depozitul este GOL - " + consumator + " așteaptă");
            obiect = coada.take(); // take() blochează firul până când apare un obiect
        }
        jurnal.accept(consumator + " a consumat: " + obiect + " | total consumate: " + consumate.incrementAndGet()
                + continut());
        stare.accept(sir());
        return obiect;
    }

    int produse() {
        return produse.get();
    }

    int consumate() {
        return consumate.get();
    }

    private String continut() {
        String sir = sir();
        return " | depozit " + sir.length() + "/" + capacitate + ": " + sir;
    }

    private String sir() {
        StringBuilder sb = new StringBuilder();
        for (char c : coada) {
            sb.append(c);
        }
        return sb.toString();
    }
}
