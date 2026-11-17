import java.util.function.Consumer;

/** Membru 2: Ganenco Bogdan. Th3 si Th4, asteptare sleep() si semnal interrupt(). */
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
        output.accept(jurnal.toString());
        Main.calculTerminat(invers ? 3 : 2);
        Main.asteaptaToateCalculele();
        try {
            // Th4 primeste semnal de la Th2; Th3 primeste semnal de la Th4.
            // Daca semnalul a venit deja, sleep() arunca imediat exceptia.
            try {
                while (true) Thread.sleep(Long.MAX_VALUE);
            } catch (InterruptedException semnal) {
                // Semnalul inseamna ca predecesorul a terminat afisarea textului.
            }
            output.accept(nume + (invers ? " | Grupa: " : " | Disciplina: "));
            Main.scrieLent(invers ? "CR-242" : "Programarea concurenta si distribuita", output);
            if (invers) fire[2].interrupt();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
