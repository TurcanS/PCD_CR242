package andrei;

import java.awt.BorderLayout;
import java.awt.Color;
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
                JFrame fereastra = new JFrame("Laboratorul 2 | Andrei Toma");
                JPanel pagina = new JPanel(new BorderLayout(0, 12));
                pagina.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
                pagina.setBackground(new Color(238, 244, 248));
                JLabel titlu = new JLabel("Andrei Toma | CR-242 | Varianta 6");
                titlu.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
                titlu.setForeground(new Color(27, 71, 99));
                pagina.add(titlu, BorderLayout.NORTH);
                JTextArea text = new JTextArea(Lab2.rezultat.toString(), 30, 85);
                text.setEditable(false);
                text.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
                text.setBackground(new Color(250, 252, 254));
                text.setMargin(new java.awt.Insets(12, 12, 12, 12));
                text.setCaretPosition(0);
                pagina.add(new JScrollPane(text), BorderLayout.CENTER);
                pagina.add(new JLabel("10 fire | Thb: 8 | Th9: 5 | Celelalte: 4"), BorderLayout.SOUTH);
                fereastra.setContentPane(pagina);
                fereastra.pack();
                fereastra.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                fereastra.setLocationRelativeTo(null);
                fereastra.setVisible(true);
            });
        }
    }
}
