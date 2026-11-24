package lab4;

import java.util.Random;
import java.util.function.Consumer;

/**
 * Producătorul (Cornos Ilie): generează aleatoriu câte F = 2 consoane deodată și le pune în depozit.
 *
 * Distrugerea producătorului: nu se folosesc interrupt() sau stop(). Producătorul lucrează cât timp
 * producția nu s-a încheiat; când depozitul anunță că s-au produs destule obiecte pentru toți
 * consumatorii (pune() întoarce false), producătorul iese din buclă și metoda run() se termină
 * normal - firul de execuție se distruge singur.
 * În run() nu există synchronized - sincronizarea este doar în metodele depozitului.
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
        try {
            boolean continua = true;
            while (continua) {
                sleep(150 + random.nextInt(250)); // timpul de „producere”
                continua = depozit.pune(getName(), consoana(), consoana());
            }
        } catch (InterruptedException e) {
            // nu ar trebui să se întâmple: producătorii nu sunt întrerupți din exterior
        }
        jurnal.accept("--- " + getName() + " și-a terminat lucrul și s-a oprit");
    }

    private char consoana() {
        return CONSOANE.charAt(random.nextInt(CONSOANE.length()));
    }
}
