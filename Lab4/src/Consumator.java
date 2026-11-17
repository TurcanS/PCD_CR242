import java.util.Random;

/** Membru 2: Ganenco Bogdan. Consumatorii, sincronizati prin synchronized, wait() si notifyAll(). */
public class Consumator extends Thread {
    private final Depozit depozit;
    private final int necesar; // Z: cate numere trebuie sa primeasca
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
        try {
            while (primite < necesar) {
                // Sectiunea critica: blocam monitorul depozitului comun.
                synchronized (depozit) {
                    if (depozit.esteGol()) {
                        depozit.scrie(getName() + " asteapta: depozitul este gol\n");
                    }
                    // while, nu if: dupa trezire verificam din nou conditia.
                    while (depozit.esteGol()) {
                        depozit.wait(); // elibereaza monitorul pana la notifyAll()
                    }
                    int numar = depozit.scoate();
                    primite++;
                    depozit.scrie(String.format("%s a luat %d (%d/%d) | %s%n",
                            getName(), numar, primite, necesar, depozit.stare()));
                    if (depozit.esteGol()) depozit.scrie("  >> Depozitul este gol\n");
                    // Anuntam producatorii ca s-a eliberat un loc.
                    depozit.notifyAll();
                }
                // "Consumul" se face in afara blocului, ca ceilalti sa poata lucra.
                Thread.sleep(50 + random.nextInt(151));
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
