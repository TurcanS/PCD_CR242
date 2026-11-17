import java.util.Random;

/** Membru 2: Ganenco Bogdan. Consumatorul ia cate 1 numar pana are Z numere. */
public class Consumator extends Thread {
    private final Depozit depozit;
    private final int necesar; // Z
    private final Random random;
    private int primite = 0;

    public Consumator(String nume, Depozit depozit, int necesar, long seed) {
        super(nume);
        this.depozit = depozit;
        this.necesar = necesar;
        this.random = new Random(seed);
    }

    @Override
    public void run() {
        // Aici nu exista sincronizare: asteptarea se face in depozit.ia().
        try {
            while (primite < necesar) {
                depozit.ia(getName(), primite + 1, necesar);
                primite++;
                Thread.sleep(50 + random.nextInt(151)); // timpul de "consum"
            }
            depozit.scrie(getName() + " este indestulat cu " + necesar
                    + " numere. Thread-ul a finalizat.\n");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public int getPrimite() {
        return primite;
    }
}
