package lab4;

import java.util.Random;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

/**
 * Consumatorul (Spranceana Marius): ia câte un obiect din depozit până când este îndestulat cu Z obiecte.
 */
class Consumator extends Thread {

    private final Depozit depozit;
    private final int necesar;
    private final Consumer<String> jurnal;
    private final IntConsumer progres;
    private final Random random = new Random();

    /** @param progres primește numărul de obiecte consumate după fiecare obiect luat */
    Consumator(String nume, Depozit depozit, int necesar, Consumer<String> jurnal, IntConsumer progres) {
        super(nume);
        this.depozit = depozit;
        this.necesar = necesar;
        this.jurnal = jurnal;
        this.progres = progres;
    }

    @Override
    public void run() {
        StringBuilder luate = new StringBuilder();
        try {
            for (int i = 1; i <= necesar; i++) {
                luate.append(depozit.ia(getName())).append(' ');
                progres.accept(i);
                sleep(200 + random.nextInt(300)); // timpul de „consumare”
            }
            jurnal.accept(">>> " + getName() + " este îndestulat cu " + necesar + " obiecte: " + luate);
        } catch (InterruptedException e) {
            interrupt();
        }
    }
}
