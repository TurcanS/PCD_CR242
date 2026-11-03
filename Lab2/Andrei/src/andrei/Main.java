package andrei;

import java.awt.Font;
import javax.swing.*;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        if (args.length > 1 || (args.length == 1 && !args[0].equals("--console"))) {
            throw new IllegalArgumentException("Singura optiune este --console.");
        }

        // Rulam pe rand ambele cerinte ale laboratorului.
        Lab2.executa(false); // Nivel mediu.
        Lab2.executa(true);  // Nivel avansat.

        // Fara --console, afisam rezultatul si intr-o fereastra simpla.
        if (args.length == 0) {
            SwingUtilities.invokeLater(() -> {
                JFrame fereastra = new JFrame("Lab 2 - Andrei Toma - Varianta 6 (+1)");
                JTextArea text = new JTextArea(Lab2.rezultat.toString(), 30, 85);
                text.setEditable(false);
                text.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
                text.setCaretPosition(0);
                fereastra.add(new JScrollPane(text));
                fereastra.pack();
                fereastra.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                fereastra.setLocationRelativeTo(null);
                fereastra.setVisible(true);
            });
        }
    }
}
