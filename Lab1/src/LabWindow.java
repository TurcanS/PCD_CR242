import java.awt.Font;
import java.awt.GraphicsEnvironment;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/** Afisarea este actualizata exclusiv pe firul grafic Swing. */
public final class LabWindow {
    private final JTextArea text = new JTextArea(30, 110);

    private LabWindow() {
        text.setEditable(false);
        text.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        JFrame frame = new JFrame("PCD - Lab 1 - Varianta 6 - Grup-6");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new JScrollPane(text));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static LabWindow open() throws Exception {
        if (GraphicsEnvironment.isHeadless()) {
            throw new IllegalStateException("Foloseste --console in lipsa unui ecran.");
        }
        LabWindow[] result = new LabWindow[1];
        SwingUtilities.invokeAndWait(() -> result[0] = new LabWindow());
        return result[0];
    }

    public void append(String value) {
        SwingUtilities.invokeLater(() -> {
            text.append(value);
            text.setCaretPosition(text.getDocument().getLength());
        });
    }
}
