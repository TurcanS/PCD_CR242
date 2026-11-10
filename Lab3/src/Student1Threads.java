import java.util.function.Consumer;

/** Membru 1: Andrei Toma. Th1 si Th2, sincronizare prin join() si sleep(). */
public class Student1Threads implements Runnable {
    private final int[] mas;
    private final boolean invers;
    private final Thread[] fire;
    private final Consumer<String> output;

    public Student1Threads(int[] mas, boolean invers, Thread[] fire,
                           Consumer<String> output) {
        this.mas = mas;
        this.invers = invers;
        this.fire = fire;
        this.output = output;
    }

    @Override
    public void run() {
        String nume = Thread.currentThread().getName();
        StringBuilder jurnal = new StringBuilder("\n--- " + nume + " | produse pe perechi ---\n");
        long suma = 0;
        int perechi = 0;
        int pas = invers ? -4 : 4;
        // Aceleasi 25 de perechi: (1,3), (5,7), ... sau (99,97), (95,93), ...
        for (int i = invers ? 99 : 1; invers ? i >= 3 : i <= 97; i += pas) {
            int j = i + (invers ? -2 : 2);
            long produs = (long) mas[i] * mas[j];
            suma += produs;
            perechi++;
            jurnal.append(String.format(
                    "%s: mas[%d]=%d * mas[%d]=%d => produs=%d; suma=%d%n",
                    nume, i, mas[i], j, mas[j], produs, suma));
        }
        jurnal.append(nume + ": perechi=" + perechi + "; suma finala=" + suma + "\n");
        try {
            // Th1 asteapta terminarea lui Th2, inclusiv afisarea numelui de familie.
            if (!invers) fire[1].join();
            output.accept(jurnal.toString());
            output.accept(nume + (invers ? " | Numele: " : " | Prenumele: "));
            Main.scrieLent(invers ? "Toma" : "Andrei", output);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
