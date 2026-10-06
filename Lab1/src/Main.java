import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
        output.accept("mas = " + Arrays.toString(mas) + System.lineSeparator());
        output.accept("Pozitii impare = indici Java 1, 3, ..., 99.\n");

        Student1Threads student1 = new Student1Threads(mas, output);
        List<Thread> threads = new ArrayList<>();
        Collections.addAll(threads, student1.getThreads());
        String students = student1Name;

        Student2Threads student2 = new Student2Threads(mas, output);
        Collections.addAll(threads, student2.getThreads());
        students += ", Ganenco Bogdan";

        output.accept("Fire de calcul: " + threads.size()
                + (threads.size() == 2 ? " (partea studentului 1; echipa necesita 4).\n" : ".\n"));

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
