package lab6;

import java.util.Random;
import java.util.function.Consumer;

/**
 * Producătorul (Cornos Ilie): generează aleatoriu câte F = 1 consoană și o pune în depozit,
 * până când depozitul anunță că au fost produse toate obiectele.
 */
class Producator extends Thread {

    private static final String CONSOANE = "BCDFGHJKLMNPQRSTVWXZ";

    private final Depozit depozit;
    private final Consumer<String> jurnal;
    private final Random random = new Random();

    Producator(String nume, Depozit depozit, Consumer<String> jurnal) {
        super(nume);
        this.depozit = depozit;
        this.jurnal = jurnal;
    }

    @Override
    public void run() {
        int produse = 0;
        try {
            while (depozit.pune(getName(), CONSOANE.charAt(random.nextInt(CONSOANE.length())))) {
                produse++;
                sleep(40 + random.nextInt(80)); // timpul de „producere”
            }
            jurnal.accept("=== " + getName() + " și-a terminat lucrul: a produs " + produse + " obiecte");
        } catch (InterruptedException e) {
            interrupt();
        }
    }
}
