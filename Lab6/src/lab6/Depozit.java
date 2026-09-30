package lab6;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

/**
 * Depozitul comun de capacitate D, sincronizat cu ReentrantLock și două obiecte Condition.
 *
 * Lucrul decurge în faze care alternează:
 *   UMPLERE - produc numai producătorii, până când depozitul este plin;
 *   GOLIRE  - consumă numai consumatorii, până când depozitul este gol.
 * Operațiile se opresc după ce au fost produse și consumate Z obiecte.
 */
final class Depozit {

    /** Primește starea depozitului după fiecare modificare (pentru interfața grafică). */
    interface Observator {
        void stare(String continut, int produse, int consumate, boolean umplere);
    }

    private final int capacitate;
    private final int total;
    private final Deque<Character> obiecte = new ArrayDeque<>();
    private final Consumer<String> jurnal;
    private final Observator observator;

    private final ReentrantLock lacat = new ReentrantLock();
    private final Condition poateProduce = lacat.newCondition(); // aici așteaptă producătorii
    private final Condition poateConsuma = lacat.newCondition(); // aici așteaptă consumatorii

    private boolean umplere = true; // faza curentă: true = UMPLERE, false = GOLIRE
    private int produse, consumate, cicluri;

    Depozit(int capacitate, int total, Consumer<String> jurnal, Observator observator) {
        this.capacitate = capacitate;
        this.total = total;
        this.jurnal = jurnal;
        this.observator = observator;
    }

    /**
     * Pune un obiect în depozit. În faza de golire producătorul așteaptă.
     *
     * @return false dacă au fost deja produse toate cele Z obiecte (producătorul se oprește)
     */
    boolean pune(String producator, char obiect) throws InterruptedException {
        lacat.lock();
        try {
            while (!umplere && produse < total) {
                poateProduce.await(); // eliberează lacătul până când depozitul este golit
            }
            if (produse >= total) {
                return false;
            }
            obiecte.addLast(obiect);
            produse++;
            jurnal.accept(producator + " a produs: " + obiect + "   | produse " + produse + "/" + total + continut());
            if (obiecte.size() == capacitate || produse == total) {
                jurnal.accept(">>> Depozitul este PLIN (" + obiecte.size() + "/" + capacitate
                        + ") - producătorii așteaptă, consumatorii pot consuma");
                umplere = false;
                poateConsuma.signalAll(); // trezește consumatorii
                if (produse == total) {
                    poateProduce.signalAll(); // producătorii care așteaptă află că s-a produs tot
                }
            }
            observator.stare(sir(), produse, consumate, umplere);
            return true;
        } finally {
            lacat.unlock(); // lacătul se eliberează întotdeauna, chiar dacă apare o excepție
        }
    }

    /**
     * Ia un obiect din depozit. În faza de umplere consumatorul așteaptă.
     *
     * @return obiectul luat sau null dacă au fost deja consumate toate cele Z obiecte (consumatorul se oprește)
     */
    Character ia(String consumator) throws InterruptedException {
        lacat.lock();
        try {
            while (umplere && consumate < total) {
                poateConsuma.await(); // eliberează lacătul până când depozitul este umplut
            }
            if (consumate >= total) {
                return null;
            }
            char obiect = obiecte.removeFirst();
            consumate++;
            jurnal.accept(consumator + " a consumat: " + obiect + " | consumate " + consumate + "/" + total + continut());
            if (obiecte.isEmpty()) {
                cicluri++;
                if (consumate == total) {
                    jurnal.accept("<<< Depozitul este GOL - au fost produse și consumate toate cele " + total + " obiecte");
                    poateConsuma.signalAll(); // consumatorii care așteaptă află că s-a consumat tot
                } else {
                    jurnal.accept("<<< Depozitul este GOL - consumatorii așteaptă, producătorii pot produce");
                    umplere = true;
                    poateProduce.signalAll(); // trezește producătorii
                }
            }
            observator.stare(sir(), produse, consumate, umplere);
            return obiect;
        } finally {
            lacat.unlock();
        }
    }

    String rezumat() {
        lacat.lock();
        try {
            return "produse: " + produse + ", consumate: " + consumate + ", cicluri umplere-golire: " + cicluri;
        } finally {
            lacat.unlock();
        }
    }

    private String continut() {
        return " | depozit " + obiecte.size() + "/" + capacitate + ": " + sir();
    }

    private String sir() {
        StringBuilder sb = new StringBuilder();
        for (char c : obiecte) {
            sb.append(c);
        }
        return sb.toString();
    }
}
