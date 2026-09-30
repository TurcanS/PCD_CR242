package lab2;

import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

/**
 * Nivelul mediu (Cornos Ilie): metoda run() a clasei Thread nu este suprascrisă.
 * Firele primesc un obiect Runnable, iar informația despre ele este afișată prin enumerare.
 */
public final class NivelMediu {

    private NivelMediu() {
    }

    public static void ruleaza(Consumer<String> out) throws InterruptedException {
        // firele rămân active până la terminarea enumerării, altfel nu ar mai fi găsite în grup
        CountDownLatch enumerareGata = new CountDownLatch(1);
        Runnable sarcina = () -> {
            try {
                enumerareGata.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };

        ThreadGroup main = Structura.construieste((grup, nume) -> new Thread(grup, sarcina, nume));
        Structura.enumera(main, "", out);
        enumerareGata.countDown();
        Structura.asteapta(main);
    }
}
