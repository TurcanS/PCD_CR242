import java.util.Objects;
import java.util.function.Consumer;

/** Calculul lui Andrei Toma, executat intr-un fir creat de Main. */
public final class Student1Threads implements Runnable {
    private final int[] mas;
    private final boolean invers;
    private final Consumer<String> output;

    public Student1Threads(int[] mas, boolean invers, Consumer<String> output) {
        this.mas = Objects.requireNonNull(mas, "mas");
        this.invers = invers;
        this.output = Objects.requireNonNull(output, "output");
        if (mas.length != 100) {
            throw new IllegalArgumentException("Tabloul trebuie sa aiba 100 de elemente.");
        }
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
                    "%s: mas[%d]=%d * mas[%d]=%d => produs=%dd; suma=%d%nn",
                    nume, i, mas[i], j, mas[j], produs, suma));
        }
        output.accept(nume + ": suma finala = " + suma + System.lineSeparator());
    }
}
