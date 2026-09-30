package lab6;

import java.util.Random;
import java.util.function.Consumer;

/**
 * Consumatorul (Spranceana Marius): ia câte un obiect din depozit,
 * până când depozitul anunță că au fost consumate toate obiectele.
 */
class Consumator extends Thread {

    private final Depozit depozit;
    private final Consumer<String> jurnal;
    private final Random random = new Random();

    Consumator(String nume, Depozit depozit, Consumer<String> jurnal) {
        super(nume);
        this.depozit = depozit;
        this.jurnal = jurnal;
    }

    @Override
    public void run() {
        int consumate = 0;
        try {
            while (depozit.ia(getName()) != null) {
                consumate++;
                sleep(60 + random.nextInt(100)); // timpul de „consumare”
            }
            jurnal.accept("=== " + getName() + " și-a terminat lucrul: a consumat " + consumate + " obiecte");
        } catch (InterruptedException e) {
            interrupt();
        }
    }
}
