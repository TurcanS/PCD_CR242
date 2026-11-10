import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import javax.swing.*;

public class LabWindow {
    private final JTextArea jurnal = new JTextArea();
    private final JLabel stare = new JLabel("  4 fire | calcule paralele, afisare ordonata");

    private LabWindow() {
        JFrame cadru = new JFrame("Lab 3 - Varianta 6 - Andrei Toma / Ganenco Bogdan");
        jurnal.setEditable(false);
        jurnal.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        jurnal.setBackground(new Color(20, 27, 38));
        jurnal.setForeground(new Color(218, 239, 224));
        jurnal.setMargin(new java.awt.Insets(15, 15, 15, 15));
        JLabel titlu = new JLabel("  VARIANTA 6  |  Atelier de fire: produse si intervale");
        titlu.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        titlu.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        cadru.add(titlu, BorderLayout.NORTH);
        cadru.add(new JScrollPane(jurnal), BorderLayout.CENTER);
        cadru.add(stare, BorderLayout.SOUTH);
        cadru.setSize(1120, 740);
        cadru.setLocationRelativeTo(null);
        cadru.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        cadru.setVisible(true);
    }

    public static LabWindow deschide() throws Exception {
        LabWindow[] rezultat = new LabWindow[1];
        SwingUtilities.invokeAndWait(() -> rezultat[0] = new LabWindow());
        return rezultat[0];
    }

    public void adauga(String text) {
        SwingUtilities.invokeLater(() -> {
            jurnal.append(text);
            jurnal.setCaretPosition(jurnal.getDocument().getLength());
            if (text.contains("FINAL:")) stare.setText("  Terminat | toate cele 4 fire au fost asteptate");
        });
    }
}
