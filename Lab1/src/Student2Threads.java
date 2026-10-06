import java.util.Objects;
import java.util.function.Consumer;

/** Partea lui Ganenco Bogdan: doua fire pentru varianta 6. */
public final class Student2Threads implements Runnable {
    private final int[] mas;
    private final Consumer<String> output;
    private final Thread th1;
    private final Thread th2;

    public Student2Threads(int[] mas, Consumer<String> output) {
        this.mas = Objects.requireNonNull(mas, "mas");
        this.output = Objects.requireNonNull(output, "output");
        if (mas.length != 100) {
            throw new IllegalArgumentException("Tabloul trebuie sa aiba 100 de elemente.");
        }
        // Ambele fire ruleaza acelasi Runnable, citind acelasi tablou comun.
        th1 = new Thread(this, "Student2-Th1");
        th2 = new Thread(this, "Student2-Th2");
    }

    public Thread[] getThreads() {
        return new Thread[] { th1, th2 };
    }

    @Override
    public void run() {
        Thread current = Thread.currentThread();
        boolean invers = current == th2;
        // Variabila locala: fiecare fir are propria suma.
        long suma = 0;
        for (int pereche = 0; pereche < 25; pereche++) {
            // Indici impari, numerotati de la 0, grupati fara suprapuneri.
            int i = invers ? 99 - 4 * pereche : 1 + 4 * pereche;
            int j = invers ? i - 2 : i + 2;
            long produs = (long) mas[i] * mas[j];
            suma += produs;
            output.accept(String.format(
                    "%s: mas[%d]=%d * mas[%d]=%d => produs=%d; suma=%d%n",
                    current.getName(), i, mas[i], j, mas[j], produs, suma));
        }
        output.accept(current.getName() + ": suma finala = " + suma + System.lineSeparator());
    }
}
