package lab4;

import java.util.Random;

/**
 * Producătorul (Cornos Ilie): generează aleatoriu câte F = 2 consoane și le pune în depozit.
 * Lucrează până când este întrerupt de firul principal, adică până când toți consumatorii sunt îndestulați.
 */
class Producator extends Thread {

    private static final String CONSOANE = "BCDFGHJKLMNPQRSTVWXZ";

    private final Depozit depozit;
    private final Random random = new Random();

    Producator(String nume, Depozit depozit) {
        super(nume);
        this.depozit = depozit;
    }

    @Override
    public void run() {
        try {
            while (!isInterrupted()) {
                sleep(150 + random.nextInt(250)); // timpul de „producere”
                depozit.pune(getName(), consoana(), consoana());
            }
        } catch (InterruptedException e) {
            // întrerupt în sleep() sau în wait(): producătorul își încheie lucrul
        }
    }

    private char consoana() {
        return CONSOANE.charAt(random.nextInt(CONSOANE.length()));
    }
}
