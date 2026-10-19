package lab4;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.Consumer;

/**
 * Resursa comună (monitorul): depozitul de capacitate D în care producătorii pun obiecte,
 * iar consumatorii le iau. Sincronizarea se face aici, prin synchronized, wait() și notifyAll().
 */
final class Depozit {

    private final int capacitate;
    private final Deque<Character> obiecte = new ArrayDeque<>();
    private final Consumer<String> jurnal;
    private final Consumer<String> stare;
    private int produse, consumate;

    /**
     * @param jurnal primește mesajele despre producere, consumare și cazurile „gol” / „plin”
     * @param stare  primește conținutul depozitului după fiecare modificare
     */
    Depozit(int capacitate, Consumer<String> jurnal, Consumer<String> stare) {
        this.capacitate = capacitate;
        this.jurnal = jurnal;
        this.stare = stare;
    }

    /** Pune două obiecte în depozit; dacă nu este loc pentru ambele, producătorul așteaptă. */
    synchronized void pune(String producator, char a, char b) throws InterruptedException {
        if (obiecte.size() + 2 > capacitate) {
            jurnal.accept("!!! Depozitul este PLIN (" + obiecte.size() + "/" + capacitate + ") - "
                    + producator + " așteaptă");
        }
        while (obiecte.size() + 2 > capacitate) {
            wait(); // eliberează monitorul până când un consumator ia un obiect
        }
        obiecte.addLast(a);
        obiecte.addLast(b);
        produse += 2;
        jurnal.accept(producator + " a pus: " + a + ", " + b + continut());
        stare.accept(sir());
        notifyAll(); // trezește consumatorii care așteaptă obiecte
    }

    /** Ia un obiect din depozit; dacă depozitul este gol, consumatorul așteaptă. */
    synchronized char ia(String consumator) throws InterruptedException {
        if (obiecte.isEmpty()) {
            jurnal.accept("!!! Depozitul este GOL - " + consumator + " așteaptă");
        }
        while (obiecte.isEmpty()) {
            wait(); // eliberează monitorul până când un producător pune obiecte
        }
        char obiect = obiecte.removeFirst();
        consumate++;
        jurnal.accept(consumator + " a luat: " + obiect + continut());
        stare.accept(sir());
        notifyAll(); // trezește producătorii care așteaptă loc liber
        return obiect;
    }

    synchronized String rezumat() {
        return "produse: " + produse + ", consumate: " + consumate + ", rămase în depozit: " + obiecte.size();
    }

    private String continut() {
        return "   | depozit " + obiecte.size() + "/" + capacitate + ": " + sir();
    }

    private String sir() {
        StringBuilder sb = new StringBuilder();
        for (char c : obiecte) {
            sb.append(c);
        }
        return sb.toString();
    }
}
