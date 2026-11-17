import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Clasa de baza: depozitul comun de capacitate D, pastrat intr-o colectie (ArrayList).
 * Toata sincronizarea este aici, in metode synchronized, nu in run().
 * ia()   - sincronizarea consumatorilor (Ganenco Bogdan)
 * pune() - sincronizarea producatorilor (Andrei Toma)
 */
public class Depozit {
    private final List<Integer> stoc = new ArrayList<>();
    private final int capacitate;   // D
    private final int totalNecesar; // Y * Z, cate numere trebuie consumate in total
    private final Consumer<String> output;
    private int produse = 0;
    private int consumate = 0;

    public Depozit(int capacitate, int totalNecesar, Consumer<String> output) {
        this.capacitate = capacitate;
        this.totalNecesar = totalNecesar;
        this.output = output;
    }

    // ---------- Sincronizarea consumatorilor (Ganenco Bogdan) ----------

    /** Consumatorul ia un singur numar. Daca depozitul e gol, asteapta. */
    public synchronized int ia(String nume, int nrCurent, int necesar) throws InterruptedException {
        if (stoc.isEmpty()) {
            output.accept(nume + " asteapta: depozitul este gol\n");
        }
        // while, nu if: dupa trezire verificam din nou daca a ramas ceva.
        while (stoc.isEmpty()) {
            wait(); // elibereaza monitorul depozitului pana la notifyAll()
        }
        int numar = stoc.remove(stoc.size() - 1);
        consumate++;
        output.accept(String.format("%s a luat %d (%d/%d) | %s%n",
                nume, numar, nrCurent, necesar, stare()));
        if (stoc.isEmpty()) output.accept("  >> Depozitul este gol\n");
        notifyAll(); // producatorii afla ca s-a eliberat un loc
        return numar;
    }

    // ---------- Sincronizarea producatorilor (Andrei Toma) ----------

    /**
     * Producatorul pune 2 numere. Daca este un singur loc liber, pune primul,
     * iar al doilea se pierde. Daca depozitul e plin, asteapta.
     * Intoarce cate numere a pus (1 sau 2), sau 0 daca productia s-a terminat
     * si producatorul trebuie sa se opreasca.
     */
    public synchronized int pune(String nume, int a, int b) throws InterruptedException {
        if (produse < totalNecesar && stoc.size() == capacitate) {
            output.accept(nume + " asteapta: depozitul este plin\n");
        }
        while (produse < totalNecesar && stoc.size() == capacitate) {
            wait(); // asteapta pana un consumator elibereaza un loc
        }
        if (produse >= totalNecesar) {
            notifyAll(); // trezim si celalalt producator, ca sa se opreasca si el
            return 0;
        }
        stoc.add(a);
        produse++;
        int puse = 1;
        if (stoc.size() < capacitate) {
            stoc.add(b);
            produse++;
            puse = 2;
            output.accept(String.format("%s a pus %d si %d | %s%n", nume, a, b, stare()));
        } else {
            output.accept(String.format("%s a pus %d; %d s-a pierdut (era un singur loc liber) | %s%n",
                    nume, a, b, stare()));
        }
        if (stoc.size() == capacitate) output.accept("  >> Depozitul este plin\n");
        notifyAll(); // consumatorii afla ca au aparut numere noi
        return puse;
    }

    // ---------- Partea comuna ----------

    public synchronized void scrie(String text) {
        output.accept(text);
    }

    public synchronized int dimensiune() {
        return stoc.size();
    }

    public synchronized int getProduse() {
        return produse;
    }

    public synchronized int getConsumate() {
        return consumate;
    }

    private String stare() {
        return "depozit " + stoc.size() + "/" + capacitate + " -> " + stoc;
    }
}
