import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/*
 * Lucrarea de laborator nr. 4 - Sincronizarea thread-urilor in Java
 * Varianta 2: X = 3 producatori, Y = 4 consumatori, Z = 2 obiecte pentru fiecare consumator,
 *             D = 5 (capacitatea depozitului), obiecte = numere impare, F = 2 obiecte per producere.
 *
 * Fara synchronized, wait() sau notify(): depozitul este un container thread-safe
 * (ArrayBlockingQueue) care blocheaza singur producatorii cand e plin si consumatorii cand e gol.
 * Toate mesajele se afiseaza in consola.
 */
public class Main {
    static final int X = 3;   // numarul de producatori
    static final int Y = 4;   // numarul de consumatori
    static final int Z = 2;   // cate obiecte trebuie sa ia fiecare consumator
    static final int D = 5;   // capacitatea depozitului
    static final int F = 2;   // cate obiecte produce un producator o data

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Start: " + X + " producatori, " + Y + " consumatori, Z = " + Z
                + ", D = " + D + ", numere impare");

        Depozit depozit = new Depozit(D);
        List<Producator> producatori = new ArrayList<>();
        List<Consumator> consumatori = new ArrayList<>();
        for (int i = 1; i <= X; i++) producatori.add(new Producator("Producatorul " + i, depozit));
        for (int i = 1; i <= Y; i++) consumatori.add(new Consumator("Consumatorul " + i, depozit));

        for (Producator p : producatori) p.start();
        for (Consumator c : consumatori) c.start();

        for (Consumator c : consumatori) c.join();     // asteapta pana toti consumatorii iau Z obiecte
        for (Producator p : producatori) p.interrupt(); // apoi opreste producatorii
        for (Producator p : producatori) p.join();

        System.out.println("Toate thread-urile au finalizat. In depozit au ramas: " + depozit.continut());
    }
}

/* Resursa comuna. Depozitul este un container thread-safe: ArrayBlockingQueue cu capacitatea D.
   Coada se sincronizeaza singura (intern), deci in program nu exista synchronized/wait/notify. */
class Depozit {
    private final BlockingQueue<Integer> obiecte;

    Depozit(int capacitate) {
        obiecte = new ArrayBlockingQueue<>(capacitate);
    }

    /* Producatorul pune numerele unul cate unul. Daca depozitul este plin,
       put() blocheaza thread-ul pana cand un consumator elibereaza un loc. */
    void pune(String nume, int[] noi) throws InterruptedException {
        for (int n : noi) {
            if (obiecte.remainingCapacity() == 0) {
                System.out.println("Depozitul este PLIN -> " + nume + " asteapta");
            }
            obiecte.put(n);              // asteapta automat cat timp depozitul este plin
            System.out.println(nume + " a pus in depozit: " + n);
        }
    }

    /* Consumatorul ia un numar. Daca depozitul este gol, take() NU intoarce nimic,
       ci blocheaza thread-ul pana cand un producator pune un numar. */
    int ia(String nume) throws InterruptedException {
        if (obiecte.isEmpty()) {
            System.out.println("Depozitul este GOL -> " + nume + " asteapta");
        }
        int obiect = obiecte.take();     // asteapta automat cat timp depozitul este gol
        System.out.println(nume + " a luat din depozit: " + obiect);
        return obiect;
    }

    List<Integer> continut() {
        return new ArrayList<>(obiecte);
    }
}

class Producator extends Thread {
    private final Depozit depozit;
    private final Random rnd = new Random();

    Producator(String nume, Depozit depozit) {
        super(nume);
        this.depozit = depozit;
    }

    @Override
    public void run() {
        try {
            while (!isInterrupted()) {
                int[] noi = new int[Main.F];
                for (int i = 0; i < noi.length; i++) {
                    noi[i] = rnd.nextInt(10) * 2 + 1;   // numar impar 1..19
                }
                depozit.pune(getName(), noi);
                Thread.sleep(300 + rnd.nextInt(700));
            }
        } catch (InterruptedException e) {
            // oprit din main dupa ce toti consumatorii sunt satisfacuti
        }
        System.out.println(getName() + " s-a oprit (toti consumatorii sunt satisfacuti)");
    }
}

class Consumator extends Thread {
    private final Depozit depozit;
    private final Random rnd = new Random();

    Consumator(String nume, Depozit depozit) {
        super(nume);
        this.depozit = depozit;
    }

    @Override
    public void run() {
        try {
            for (int i = 1; i <= Main.Z; i++) {
                depozit.ia(getName());
                Thread.sleep(400 + rnd.nextInt(800));
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println(getName() + " a luat " + Main.Z + " numere. Thread-ul a finalizat");
    }
}
