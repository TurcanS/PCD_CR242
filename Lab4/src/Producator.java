import java.util.Random;

/** Membru 1: Andrei Toma. Producatorul genereaza cate 2 numere impare odata. */
public class Producator extends Thread {
    private final Depozit depozit;
    private final Random random;
    private int puse = 0;

    public Producator(String nume, Depozit depozit, long seed) {
        super(nume);
        this.depozit = depozit;
        this.random = new Random(seed);
    }

    @Override
    public void run() {
        // Aici nu exista sincronizare: asteptarea se face in depozit.pune().
        try {
            while (true) {
                int a = 2 * random.nextInt(50) + 1; // numar impar 1..99
                int b = 2 * random.nextInt(50) + 1;
                int rezultat = depozit.pune(getName(), a, b);
                if (rezultat == 0) break; // s-a produs tot ce trebuie: firul se opreste
                puse += rezultat;
                Thread.sleep(random.nextInt(101)); // productia dureaza 0..100 ms
            }
            // Oprirea producatorului: run() se termina normal, deci firul moare.
            depozit.scrie(getName() + " s-a oprit: productia necesara este gata.\n");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public int getPuse() {
        return puse;
    }
}
