import java.util.Objects;
import java.util.function.Consumer;

/** Calculul lui Ganenco Bogdan, executat intr-un fir creat de Main. */
public final class Student2Threads implements Runnable {
    private final int[] mas;
    private final boolean invers;
    private final Consumer<String> output;

    public Student2Threads(int[] mas, boolean invers, Consumer<String> output) {
        this.mas = Objects.requireNonNull(mas, "mas");
        this.invers = invers;
        this.output = Objects.requireNonNull(output, "output");
        if (mas.length != 100) {
            throw new IllegalArgumentException("Tabloul trebuie sa aiba 100 de elemente.");
        }
    }

    @Override
    public void run() {
        Thread current = Thread.currentThread();
        // Variabila locala: fiecare fir are propria suma.
        long suma = 0;
        for (int pereche = 0; pereche < 25; pereche++) {
            // Indici impari, numerotati de la 0, grupati fara suprapuneri.
            int i = invers ? 99 - 4 * pereche : 1 + 4 * pereche;
            int j = invers ? i - 2 : i + 2;
            long produs = (long) mas[i] * mas[j];
            suma += produs;
            output.accept(String.format(
                    "%s: mas[%d]=%d * mas[%d]=%d => produs=%dd; suma=%d%nn",
                    current.getName(), i, mas[i], j, mas[j], produs, suma));
        }
        output.accept(current.getName() + ": suma finala = " + suma + System.lineSeparator());
    }
}
