package lab5;

import java.util.Random;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

/**
 * Sarcina consumatorului (Spranceana Marius), executată de un fir din pool:
 * ia câte un obiect din depozit până când este îndestulat cu Z obiecte.
 */
class Consumator implements Runnable {

    private final String nume;
    private final Depozit depozit;
    private final int necesar;
    private final Consumer<String> jurnal;
    private final IntConsumer progres;
    private final Random random = new Random();

    /** @param progres primește numărul de obiecte consumate după fiecare obiect luat */
    Consumator(String nume, Depozit depozit, int necesar, Consumer<String> jurnal, IntConsumer progres) {
        this.nume = nume;
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
                luate.append(depozit.ia(nume)).append(' ');
                progres.accept(i);
                Thread.sleep(200 + random.nextInt(300)); // timpul de „consumare”
            }
            jurnal.accept(">>> " + nume + " este îndestulat cu " + necesar + " obiecte: " + luate);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
