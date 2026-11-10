import java.util.function.Consumer;

/** Membru 2: Ganenco Bogdan. Th3 si Th4, isAlive() si interrupt(). */
public class Student2Threads implements Runnable {
    private final boolean invers;
    private final Thread[] fire;
    private final Consumer<String> output;

    public Student2Threads(boolean invers, Thread[] fire, Consumer<String> output) {
        this.invers = invers;
        this.fire = fire;
        this.output = output;
    }

    @Override
    public void run() {
        String nume = Thread.currentThread().getName();
        StringBuilder jurnal = new StringBuilder("\n--- " + nume + " | traseul intervalului ---\n");
        int inceput = invers ? 908 : 654;
        int sfarsit = invers ? 123 : 1278;
        int pas = invers ? -1 : 1;
        int total = 0;
        long suma = 0;
        for (int valoare = inceput; invers ? valoare >= sfarsit : valoare <= sfarsit; valoare += pas) {
            jurnal.append(String.format("%4d ", valoare));
            total++;
            suma += valoare;
            if (total % 15 == 0) jurnal.append('\n');
        }
        if (total % 15 != 0) jurnal.append('\n');
        jurnal.append(nume + ": elemente=" + total + "; suma control=" + suma + "\n");
        try {
            Thread asteptat = fire[invers ? 0 : 3];
            // Th4 asteapta Th1; Th3 asteapta Th4. Pauza evita asteptarea activa.
            while (asteptat.isAlive()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // Th4 trezeste Th3; isAlive() verifica terminarea efectiva.
                }
            }
            Thread.interrupted();
            output.accept(jurnal.toString());
            output.accept(nume + (invers ? " | Grupa: " : " | Disciplina: "));
            Main.scrieLent(invers ? "CR-242" : "Programarea concurenta si distribuita", output);
            if (invers) fire[2].interrupt();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
