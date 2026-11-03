package andrei;

import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

public final class LabWindow extends JFrame {
    private static final long serialVersionUID = 1L;
    private final JComboBox<String> nivel = new JComboBox<>(new String[]{"Avansat", "Mediu"});
    private final JTextArea jurnal = new JTextArea();
    private final JButton porneste = new JButton("Ruleaza programul");

    public LabWindow(boolean avansat) {
        super("PCD - Laboratorul 2 - Andrei Toma - Varianta 6 (+1)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 720);
        setLocationRelativeTo(null);
        JPanel continut = new JPanel(new BorderLayout(10, 10));
        continut.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        setContentPane(continut);
        JPanel antet = new JPanel(new BorderLayout());
        JLabel titlu = new JLabel("Gruparea firelor si modificarea prioritatilor");
        titlu.setFont(titlu.getFont().deriveFont(Font.BOLD, 21f));
        antet.add(titlu, BorderLayout.NORTH);
        JPanel comenzi = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        nivel.setSelectedIndex(avansat ? 0 : 1);
        comenzi.add(new JLabel("Nivel:")); comenzi.add(nivel);
        comenzi.add(new JLabel("Prioritati: valoarea din tabel +1")); comenzi.add(porneste);
        antet.add(comenzi, BorderLayout.SOUTH);
        continut.add(antet, BorderLayout.NORTH);

        DefaultMutableTreeNode radacina = new DefaultMutableTreeNode("Main");
        DefaultMutableTreeNode g1 = new DefaultMutableTreeNode("G1");
        DefaultMutableTreeNode g3 = new DefaultMutableTreeNode("G3");
        DefaultMutableTreeNode g2 = new DefaultMutableTreeNode("G2");
        radacina.add(g1); g1.add(g3); g1.add(g2);
        for (String[] date : Lab2.DATE) {
            DefaultMutableTreeNode parinte = date[0].endsWith("G3") ? g3
                    : date[0].endsWith("G2") ? g2
                    : date[0].endsWith("G1") ? g1 : radacina;
            parinte.add(new DefaultMutableTreeNode(date[1]));
        }
        JTree schema = new JTree(radacina);
        for (int i = 0; i < schema.getRowCount(); i++) schema.expandRow(i);
        JScrollPane structura = new JScrollPane(schema);
        structura.setBorder(BorderFactory.createTitledBorder("Schema grupurilor si firelor"));
        jurnal.setEditable(false);
        jurnal.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        JScrollPane rezultat = new JScrollPane(jurnal);
        rezultat.setBorder(BorderFactory.createTitledBorder("Rezultatul executiei (afisat si in consola)"));
        JSplitPane centru = new JSplitPane(JSplitPane.VERTICAL_SPLIT, structura, rezultat);
        centru.setDividerLocation(290);
        continut.add(centru, BorderLayout.CENTER);
        continut.add(new JLabel("10 fire | 4 grupuri | Prioritatea nu garanteaza ordinea executiei."),
                BorderLayout.SOUTH);
        porneste.addActionListener(e -> ruleaza());
    }

    private void ruleaza() {
        boolean avansat = nivel.getSelectedIndex() == 0;
        jurnal.setText("");
        porneste.setEnabled(false); nivel.setEnabled(false);
        new SwingWorker<Void, String>() {
            @Override protected Void doInBackground() throws InterruptedException {
                Lab2.executa(avansat, text -> {
                    System.out.println(text);
                    publish(text);
                });
                return null;
            }
            @Override protected void process(java.util.List<String> mesaje) {
                for (String mesaj : mesaje) jurnal.append(mesaj + "\n");
                jurnal.setCaretPosition(jurnal.getDocument().getLength());
            }
            @Override protected void done() {
                try { get(); }
                catch (Exception e) { jurnal.append("Eroare: " + e + "\n"); }
                porneste.setEnabled(true); nivel.setEnabled(true);
            }
        }.execute();
    }
}
