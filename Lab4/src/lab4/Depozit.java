package lab4;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.Consumer;

/**
 * Clasa de bază - resursa comună: depozitul de capacitate D, păstrat sub formă de colecție (ArrayDeque, nu tablou).
 * Producătorii pun obiecte, consumatorii le iau.
 *
 * Sincronizarea se face DOAR prin metodele synchronized de aici, cu wait() și notifyAll()
 * (fără clase de sincronizare din java.util.concurrent și fără synchronized în run()):
 *  - pune(...)  - sincronizarea producătorilor (Cornos Ilie)
 *  - ia(...)    - sincronizarea consumatorilor (Spranceana Marius)
 */
final class Depozit {

    private final int capacitate;
    private final int totalNecesar; // Y * Z - câte obiecte trebuie consumate în total
    private final Deque<Character> obiecte = new ArrayDeque<>();
    private final Consumer<String> jurnal;
    private final Consumer<String> stare;
    private int produse, consumate, pierdute;

    /**
     * @param totalNecesar numărul total de obiecte de care au nevoie toți consumatorii (Y * Z)
     * @param jurnal       primește mesajele despre producere, consumare și cazurile „gol” / „plin”
     * @param stare        primește conținutul depozitului după fiecare modificare
     */
    Depozit(int capacitate, int totalNecesar, Consumer<String> jurnal, Consumer<String> stare) {
        this.capacitate = capacitate;
        this.totalNecesar = totalNecesar;
        this.jurnal = jurnal;
        this.stare = stare;
    }

    // ===================== Sincronizarea producătorilor (Cornos Ilie) =====================

    /**
     * Producătorul aduce 2 obiecte deodată.
     *  - dacă depozitul este plin (0 locuri), producătorul așteaptă cu wait();
     *  - dacă este cel puțin un loc, PRIMUL obiect intră întotdeauna;
     *  - al doilea intră doar dacă mai este loc, altfel se pierde (permis de condiție).
     *
     * Metoda de distrugere a producătorilor: după ce în depozit au fost puse suficiente obiecte
     * pentru toți consumatorii (Y * Z), producția se încheie, iar pune() întoarce false.
     * Producătorul iese singur din bucla din run() - fără interrupt() și fără stop().
     *
     * @return true dacă producătorul poate continua, false dacă producția s-a încheiat
     */
    synchronized boolean pune(String producator, char a, char b) throws InterruptedException {
        if (!productieIncheiata() && obiecte.size() >= capacitate) {
            jurnal.accept("!!! Depozitul este PLIN (" + obiecte.size() + "/" + capacitate + ") - "
                    + producator + " așteaptă");
        }
        while (!productieIncheiata() && obiecte.size() >= capacitate) {
            wait(); // eliberează monitorul până când un consumator ia un obiect
        }
        if (productieIncheiata()) {
            return false; // s-a produs destul: producătorul se oprește
        }

        obiecte.addLast(a); // primul obiect nu se pierde niciodată
        produse++;
        String mesaj;
        if (obiecte.size() < capacitate) {
            obiecte.addLast(b);
            produse++;
            mesaj = producator + " a pus: " + a + ", " + b;
        } else {
            pierdute++;
            mesaj = producator + " a pus: " + a + "   (nu mai este loc - obiectul " + b + " s-a pierdut)";
        }
        jurnal.accept(mesaj + continut());
        stare.accept(sir());

        if (productieIncheiata()) {
            jurnal.accept("=== S-au produs " + produse + " obiecte (necesar " + totalNecesar
                    + ") - producția se încheie");
        }
        notifyAll(); // trezește consumatorii (au ce lua) și producătorii (să vadă dacă producția s-a încheiat)
        return !productieIncheiata();
    }

    /** Producția se încheie când au intrat în depozit cel puțin Y * Z obiecte. */
    synchronized boolean productieIncheiata() {
        return produse >= totalNecesar;
    }

    // ===================== Sincronizarea consumatorilor (Spranceana Marius) =====================

    /** Consumatorul ia câte UN obiect; dacă depozitul este gol, așteaptă cu wait(). */
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

    // ===================== Afișare =====================

    synchronized String rezumat() {
        return "produse: " + produse + ", pierdute: " + pierdute + ", consumate: " + consumate
                + ", rămase în depozit: " + obiecte.size();
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
