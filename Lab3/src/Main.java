import java.awt.GraphicsEnvironment;
import java.util.Arrays;
import java.util.Random;
import java.util.function.Consumer;

public class Main {
    public static void main(String[] args) throws Exception {
        boolean consola = Arrays.asList(args).contains("--console");
        LabWindow fereastra = consola || GraphicsEnvironment.isHeadless()
                ? null : LabWindow.deschide();
        Consumer<String> output = text -> {
            System.out.print(text);
            System.out.flush();
            if (fereastra != null) fereastra.adauga(text);
        };

        int[] mas = new int[100];
        Random random = new Random(6);
        for (int i = 0; i < mas.length; i++) mas[i] = random.nextInt(100) + 1;
        output.accept("LAB 3 | VARIANTA 6 | CR-242\nAndrei Toma + Ganenco Bogdan\n");
        output.accept("mas[0..49]  = " + Arrays.toString(Arrays.copyOfRange(mas, 0, 50)) + "\n");
        output.accept("mas[50..99] = " + Arrays.toString(Arrays.copyOfRange(mas, 50, 100)) + "\n");
        output.accept("Pozitii impare = indici Java 1, 3, ..., 99.\n\n");

        // Main creeaza toate cele patru fire; fiecare membru are clasa sa.
        Thread[] fire = new Thread[4];
        fire[0] = new Thread(new Student1Threads(mas, false, fire, output), "Th1");
        fire[1] = new Thread(new Student1Threads(mas, true, fire, output), "Th2");
        fire[2] = new Thread(new Student2Threads(false, fire, output), "Th3");
        fire[3] = new Thread(new Student2Threads(true, fire, output), "Th4");

        // Pornim intai firele asteptate, pentru ca isAlive() sa fie relevant.
        for (int i : new int[] {1, 0, 3, 2}) fire[i].start();
        for (Thread fir : fire) fir.join();
        output.accept("\nFINAL: toate cele 4 fire s-au terminat.\n");
    }

    public static void scrieLent(String text, Consumer<String> output)
            throws InterruptedException {
        for (int i = 0; i < text.length(); i++) {
            output.accept(String.valueOf(text.charAt(i)));
            Thread.sleep(100);
        }
        output.accept("\n");
    }
}
