import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.util.Arrays;
import java.util.function.Consumer;
import javax.swing.*;

public class Main {
    // Varianta 6: X producatori, Y consumatori, Z numere/consumator, D capacitate, F = 2.
    static final int X = 2;
    static final int Y = 4;
    static final int Z = 4;
    static final int D = 7;

    public static void main(String[] args) throws Exception {
        boolean consola = Arrays.asList(args).contains("--console");
        JTextArea jurnal = consola || GraphicsEnvironment.isHeadless() ? null : deschideFereastra();
        Consumer<String> output = text -> {
            System.out.print(text);
            if (jurnal != null) SwingUtilities.invokeLater(() -> jurnal.append(text));
        };

        output.accept("LAB 4 | VARIANTA 6 | CR-242\nAndrei Toma + Ganenco Bogdan\n");
        output.accept("X=" + X + " producatori, Y=" + Y + " consumatori, Z=" + Z
                + ", D=" + D + ", F=2, obiecte: numere impare\n");
        output.accept("Total necesar: " + Y + " x " + Z + " = " + (Y * Z) + " numere\n\n");

        Depozit depozit = new Depozit(D, Y * Z, output);

        Producator[] producatori = new Producator[X];
        for (int i = 0; i < X; i++) {
            producatori[i] = new Producator("Producator " + (i + 1), depozit, 60 + i);
        }
        Consumator[] consumatori = new Consumator[Y];
        for (int i = 0; i < Y; i++) {
            consumatori[i] = new Consumator("Consumator " + (i + 1), depozit, Z, 600 + i);
        }

        for (Producator p : producatori) p.start();
        for (Consumator c : consumatori) c.start();
        for (Producator p : producatori) p.join();
        for (Consumator c : consumatori) c.join();

        output.accept("\n--- Rezumat ---\n");
        for (Producator p : producatori) output.accept(p.getName() + ": a pus " + p.getPuse() + "\n");
        for (Consumator c : consumatori) output.accept(c.getName() + ": a primit " + c.getPrimite() + "\n");
        output.accept("FINAL: produse=" + depozit.getProduse() + ", consumate="
                + depozit.getConsumate() + ", ramase in depozit=" + depozit.dimensiune() + "\n");
    }

    /** Interfata grafica simpla: o fereastra cu jurnalul firelor. */
    private static JTextArea deschideFereastra() throws Exception {
        JTextArea jurnal = new JTextArea();
        SwingUtilities.invokeAndWait(() -> {
            JFrame cadru = new JFrame("Lab 4 - Varianta 6 - Andrei Toma / Ganenco Bogdan");
            jurnal.setEditable(false);
            jurnal.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
            cadru.add(new JScrollPane(jurnal));
            cadru.setSize(1000, 700);
            cadru.setLocationRelativeTo(null);
            cadru.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            cadru.setVisible(true);
        });
        return jurnal;
    }
}
