import java.util.Arrays;
import java.util.Random;
import java.util.function.Consumer;

public final class Main {
    public static void main(String[] args) throws Exception {
        boolean console = args.length > 0 && args[0].equals("--console");
        int nameIndex = console ? 1 : 0;
        String student1Name = args.length > nameIndex ? args[nameIndex] : "Andrei Toma";
        LabWindow window = console ? null : LabWindow.open();
        Consumer<String> output = value -> {
            System.out.print(value);
            System.out.flush();
            if (window != null) {
                window.append(value);
            }
        };

        // Un singur tablou comun, generat o singura data, cu valori de la 1 la 100.
        int[] mas = new int[100];
        Random random = new Random();
        for (int i = 0; i < mas.length; i++) {
            mas[i] = random.nextInt(100) + 1;
        }
        output.accept("mas[0..49]  = " + Arrays.toString(Arrays.copyOfRange(mas, 0, 50))
                + System.lineSeparator());
        output.accept("mas[50..99] = " + Arrays.toString(Arrays.copyOfRange(mas, 50, 100))
                + System.lineSeparator());
        output.accept("Pozitii impare = indici Java 1, 3, ..., 99.\n");

        // Toate firele sunt create aici, cu acelasi tablou mas.
        // Clasele studentilor contin doar sarcinile de calcul.
        Thread[] threads = {
            new Thread(new Student1Threads(mas, false, output), "Student1-Th1"),
            new Thread(new Student1Threads(mas, true, output), "Student1-Th2"),
            new Thread(new Student2Threads(mas, false, output), "Student2-Th1"),
            new Thread(new Student2Threads(mas, true, output), "Student2-Th2")
        };
        String students = student1Name + ", Ganenco Bogdan";

        output.accept("Fire de calcul: " + threads.length + ".\n");

        // Pornim TOATE firele inainte de a astepta vreunul.
        for (Thread thread : threads) {
            thread.start();
        }
        // join() asigura ca mesajul final apare dupa terminarea tuturor calculelor.
        for (Thread thread : threads) {
            thread.join();
        }

        String info = "Lucrare realizata de: " + students + ". Grupa CR-242, varianta 6.\n";
        for (int i = 0; i < info.length(); i++) {
            output.accept(String.valueOf(info.charAt(i)));
            if (i + 1 < info.length()) {
                Thread.sleep(100);
            }
        }
    }
}
