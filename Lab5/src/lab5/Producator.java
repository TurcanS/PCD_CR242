package lab5;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * Sarcina producătorului (Cornos Ilie), executată de un fir din pool:
 * produce de fiecare dată câte F = 2 consoane aleatoare, cât timp mai sunt loturi de produs.
 */
class Producator implements Runnable {

    private static final String CONSOANE = "BCDFGHJKLMNPQRSTVWXZ";

    private final String nume;
    private final Depozit depozit;
    private final AtomicInteger loturiRamase;
    private final Consumer<String> jurnal;
    private final Random random = new Random();

    /** @param loturiRamase contorul comun al producătorilor: câte loturi de 2 obiecte mai trebuie produse */
    Producator(String nume, Depozit depozit, AtomicInteger loturiRamase, Consumer<String> jurnal) {
        this.nume = nume;
        this.depozit = depozit;
        this.loturiRamase = loturiRamase;
        this.jurnal = jurnal;
    }

    @Override
    public void run() {
        try {
            // getAndDecrement() este atomică: două fire nu pot rezerva același lot
            while (loturiRamase.getAndDecrement() > 0) {
                Thread.sleep(150 + random.nextInt(250)); // timpul de „producere”
                depozit.pune(nume, consoana());
                depozit.pune(nume, consoana());
            }
            jurnal.accept("<<< " + nume + " și-a terminat lucrul");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private char consoana() {
        return CONSOANE.charAt(random.nextInt(CONSOANE.length()));
    }
}
