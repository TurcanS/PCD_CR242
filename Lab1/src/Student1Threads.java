import java.util.Objects;
import java.util.function.Consumer;

/** Partea studentului 1: doua fire pentru varianta 6. */
public final class Student1Threads {
    private final Thread th1;
    private final Thread th2;

    public Student1Threads(int[] mas, Consumer<String> output) {
        Objects.requireNonNull(mas, "mas");
        Objects.requireNonNull(output, "output");
        if (mas.length != 100) {
            throw new IllegalArgumentException("Tabloul trebuie sa aiba 100 de elemente.");
        }
        // Ambele sarcini pastreaza aceeasi referinta mas; nu copiem tabloul.
        th1 = new Thread(new Calcul(mas, false, output), "Student1-Th1");
        th2 = new Thread(new Calcul(mas, true, output), "Student1-Th2");
    }

    public Thread[] getThreads() {
        return new Thread[] { th1, th2 };
    }

    private static final class Calcul implements Runnable {
        private final int[] mas;
        private final boolean invers;
        private final Consumer<String> output;

        private Calcul(int[] mas, boolean invers, Consumer<String> output) {
            this.mas = mas;
            this.invers = invers;
            this.output = output;
        }

        @Override
        public void run() {
            String nume = Thread.currentThread().getName();
            long suma = 0;
            // Pozitiile sunt indicii Java: 1, 3, ..., 99 (numerotare de la 0).
            // Perechi distincte: (1,3), (5,7), ... sau (99,97), (95,93), ...
            int pas = invers ? -4 : 4;
            int start = invers ? 99 : 1;
            for (int i = start; invers ? i >= 3 : i <= 97; i += pas) {
                int j = i + (invers ? -2 : 2);
                long produs = (long) mas[i] * mas[j];
                suma += produs;
                output.accept(String.format(
                        "%s: mas[%d]=%d * mas[%d]=%d => produs=%d; suma=%d%n",
                        nume, i, mas[i], j, mas[j], produs, suma));
            }
            output.accept(nume + ": suma finala = " + suma + System.lineSeparator());
        }
    }
}
