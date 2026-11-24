// Lucrarea de laborator nr. 4 - Sincronizarea thread-urilor in Java
// Varianta 7: X = 3 producatori, Y = 3 consumatori, Z = 5 obiecte per consumator,
//             D = 6 (capacitatea depozitului), obiecte = vocale,
//             F = 2 (fiecare producator pune cate 2 obiecte o data).
//
// Conditii respectate:
//  - depozitul este o colectie (ArrayList), nu un tablou;
//  - sincronizarea se face doar cu forme ale limbajului: synchronized, wait(), notifyAll();
//    nu se folosesc clase din java.util.concurrent (Lock, Semaphore, BlockingQueue etc.);
//  - nu se folosesc colectii sincronizate (Vector, Collections.synchronizedList etc.).

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class Main3 {
    static final int X = 3;          // numarul de producatori
    static final int Y = 3;          // numarul de consumatori
    static final int Z = 5;          // cate obiecte consuma fiecare consumator
    static final int D = 6;          // capacitatea depozitului
    static final int F = 2;          // cate obiecte pune un producator o data
    static final char[] VOCALE = {'A', 'E', 'I', 'O', 'U'};

    public static void main(String[] args) {
        if (GraphicsEnvironment.isHeadless()) {
            pornesteSimularea(null);          // fara ecran: doar consola
        } else {
            SwingUtilities.invokeLater(() -> new Fereastra().setVisible(true));
        }
    }

    // Creeaza si porneste toate thread-urile. Ruleaza intr-un fir separat
    // ca sa nu blocheze interfata grafica.
    static void pornesteSimularea(Fereastra gui) {
        Thread coordonator = new Thread(() -> {
            Depozit depozit = new Depozit(D, gui);

            Producator[] producatori = new Producator[X];
            for (int i = 0; i < X; i++) {
                producatori[i] = new Producator(depozit, i, gui);
                producatori[i].setName("Producatorul " + (i + 1));
            }
            Consumator[] consumatori = new Consumator[Y];
            for (int i = 0; i < Y; i++) {
                consumatori[i] = new Consumator(depozit, i, gui);
                consumatori[i].setName("Consumatorul " + (i + 1));
            }

            for (Producator p : producatori) p.start();
            for (Consumator c : consumatori) c.start();

            try {
                // asteptam ca fiecare consumator sa fie indestulat cu Z obiecte
                for (Consumator c : consumatori) c.join();
                // apoi oprim producatorii (care produc la infinit)
                for (Producator p : producatori) p.interrupt();
                for (Producator p : producatori) p.join();
            } catch (InterruptedException e) {
                return;
            }

            Jurnal.scrie(gui, "=== Toti consumatorii au primit cate " + Z
                    + " vocale. Programul s-a terminat. ===");
            if (gui != null) gui.simulareTerminata();
        }, "Coordonator");
        coordonator.start();
    }
}

// Resursa comuna. Toata sincronizarea se face aici, la nivelul depozitului.
class Depozit {
    private final List<Character> obiecte = new ArrayList<>();   // colectie, nu tablou
    private final int capacitate;
    private final Fereastra gui;

    Depozit(int capacitate, Fereastra gui) {
        this.capacitate = capacitate;
        this.gui = gui;
    }

    // Producatorul pune F obiecte deodata. Asteapta cat timp nu are loc pentru toate.
    // Intoarce false daca firul a fost intrerupt (sfarsitul programului).
    public synchronized boolean put(List<Character> noi) {
        String nume = Thread.currentThread().getName();
        if (obiecte.size() + noi.size() > capacitate) {
            Jurnal.scrie(gui, nume + " asteapta: depozitul este PLIN, nu are loc pentru "
                    + noi.size() + " vocale (" + obiecte.size() + "/" + capacitate + ")");
        }
        while (obiecte.size() + noi.size() > capacitate) {
            try {
                wait();
            } catch (InterruptedException e) {
                return false;
            }
        }
        obiecte.addAll(noi);
        Jurnal.scrie(gui, nume + " a pus in depozit: " + noi + "  -> depozit " + obiecte);
        if (obiecte.size() == capacitate) {
            Jurnal.scrie(gui, "Depozitul este plin " + obiecte.size() + "/" + capacitate);
        }
        if (gui != null) gui.afiseazaDepozit(new ArrayList<>(obiecte));
        notifyAll();      // trezim consumatorii care asteapta
        return true;
    }

    // Consumatorul ia un obiect. Asteapta cat timp depozitul este gol.
    public synchronized char get() throws InterruptedException {
        String nume = Thread.currentThread().getName();
        if (obiecte.isEmpty()) {
            Jurnal.scrie(gui, nume + " asteapta: depozitul este GOL");
        }
        while (obiecte.isEmpty()) {
            wait();
        }
        char obiect = obiecte.remove(0);
        Jurnal.scrie(gui, nume + " a luat din depozit: " + obiect + "  -> depozit " + obiecte);
        if (obiecte.isEmpty()) {
            Jurnal.scrie(gui, "Depozitul este gol");
        }
        if (gui != null) gui.afiseazaDepozit(new ArrayList<>(obiecte));
        notifyAll();      // trezim producatorii care asteapta loc
        return obiect;
    }
}

class Producator extends Thread {
    private final Depozit depozit;
    private final int index;
    private final Fereastra gui;

    Producator(Depozit depozit, int index, Fereastra gui) {
        this.depozit = depozit;
        this.index = index;
        this.gui = gui;
    }

    @Override
    public void run() {
        int totalProduse = 0;
        while (!isInterrupted()) {
            List<Character> noi = new ArrayList<>();
            for (int i = 0; i < Main3.F; i++) {
                noi.add(Main3.VOCALE[(int) (Math.random() * Main3.VOCALE.length)]);
            }
            Jurnal.scrie(gui, getName() + " a produs: " + noi);

            if (!depozit.put(noi)) break;              // intrerupt in timp ce astepta
            totalProduse += noi.size();
            if (gui != null) gui.actualizeazaProducator(index, totalProduse);

            try {
                sleep((int) (Math.random() * 500) + 200);
            } catch (InterruptedException e) {
                break;                                 // intrerupt in timp ce dormea
            }
        }
        Jurnal.scrie(gui, getName() + " s-a oprit dupa " + totalProduse + " vocale produse.");
    }
}

class Consumator extends Thread {
    private final Depozit depozit;
    private final int index;
    private final Fereastra gui;

    Consumator(Depozit depozit, int index, Fereastra gui) {
        this.depozit = depozit;
        this.index = index;
        this.gui = gui;
    }

    @Override
    public void run() {
        StringBuilder primite = new StringBuilder();
        try {
            for (int i = 1; i <= Main3.Z; i++) {
                char v = depozit.get();
                primite.append(v);
                Jurnal.scrie(gui, getName() + " a consumat " + v
                        + " (" + i + "/" + Main3.Z + ")");
                if (gui != null) gui.actualizeazaConsumator(index, i);
                sleep((int) (Math.random() * 800) + 300);
            }
        } catch (InterruptedException e) {
            return;
        }
        Jurnal.scrie(gui, getName() + " este indestulat cu " + Main3.Z
                + " vocale: " + primite);
    }
}

// Scrie mesajele in consola si, daca exista, in fereastra.
class Jurnal {
    static void scrie(Fereastra gui, String mesaj) {
        System.out.println(mesaj);
        if (gui != null) gui.adaugaInJurnal(mesaj);
    }
}

// Interfata grafica (Swing). Componentele Swing se modifica doar din firul
// grafic, de aceea toate actualizarile trec prin SwingUtilities.invokeLater.
class Fereastra extends JFrame {
    private final JLabel[] sloturi = new JLabel[Main3.D];
    private final JLabel[] producatori = new JLabel[Main3.X];
    private final JProgressBar[] consumatori = new JProgressBar[Main3.Y];
    private final JLabel stare = new JLabel("Depozit: 0/" + Main3.D);
    private final JTextArea jurnal = new JTextArea(20, 80);
    private final JButton start = new JButton("Start");

    Fereastra() {
        super("Lab 4 - Producator / Consumator (varianta 7: vocale)");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));

        // depozitul
        JPanel panouDepozit = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 6));
        panouDepozit.setBorder(BorderFactory.createTitledBorder("Depozit (capacitate " + Main3.D + ")"));
        for (int i = 0; i < sloturi.length; i++) {
            sloturi[i] = new JLabel(" ", SwingConstants.CENTER);
            sloturi[i].setPreferredSize(new Dimension(50, 50));
            sloturi[i].setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
            sloturi[i].setOpaque(true);
            sloturi[i].setBackground(Color.WHITE);
            sloturi[i].setBorder(BorderFactory.createLineBorder(Color.GRAY));
            panouDepozit.add(sloturi[i]);
        }
        panouDepozit.add(stare);

        // producatori si consumatori
        JPanel panouFire = new JPanel(new GridLayout(1, 2, 8, 8));
        JPanel pp = new JPanel(new GridLayout(Main3.X, 1));
        pp.setBorder(BorderFactory.createTitledBorder("Producatori"));
        for (int i = 0; i < producatori.length; i++) {
            producatori[i] = new JLabel();
            pp.add(producatori[i]);
        }
        JPanel pc = new JPanel(new GridLayout(Main3.Y, 1));
        pc.setBorder(BorderFactory.createTitledBorder("Consumatori (cate " + Main3.Z + " vocale)"));
        for (int i = 0; i < consumatori.length; i++) {
            consumatori[i] = new JProgressBar(0, Main3.Z);
            consumatori[i].setStringPainted(true);
            pc.add(consumatori[i]);
        }
        panouFire.add(pp);
        panouFire.add(pc);

        JPanel sus = new JPanel(new BorderLayout());
        sus.add(panouDepozit, BorderLayout.NORTH);
        sus.add(panouFire, BorderLayout.CENTER);

        jurnal.setEditable(false);
        jurnal.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

        start.addActionListener(e -> {
            start.setEnabled(false);
            reseteaza();
            Main3.pornesteSimularea(this);
        });

        add(sus, BorderLayout.NORTH);
        add(new JScrollPane(jurnal), BorderLayout.CENTER);
        add(start, BorderLayout.SOUTH);
        reseteaza();
        pack();
        setLocationRelativeTo(null);
    }

    private void reseteaza() {
        jurnal.setText("");
        afiseazaDepozitDirect(new ArrayList<>());
        for (int i = 0; i < producatori.length; i++) producatori[i].setText("Producatorul " + (i + 1) + ": 0 vocale produse");
        for (int i = 0; i < consumatori.length; i++) {
            consumatori[i].setValue(0);
            consumatori[i].setString("Consumatorul " + (i + 1) + ": 0/" + Main3.Z);
        }
    }

    void adaugaInJurnal(String mesaj) {
        SwingUtilities.invokeLater(() -> {
            jurnal.append(mesaj + "\n");
            jurnal.setCaretPosition(jurnal.getDocument().getLength());
        });
    }

    void afiseazaDepozit(List<Character> copie) {
        SwingUtilities.invokeLater(() -> afiseazaDepozitDirect(copie));
    }

    private void afiseazaDepozitDirect(List<Character> copie) {
        for (int i = 0; i < sloturi.length; i++) {
            boolean ocupat = i < copie.size();
            sloturi[i].setText(ocupat ? String.valueOf(copie.get(i)) : " ");
            sloturi[i].setBackground(ocupat ? new Color(255, 220, 120) : Color.WHITE);
        }
        String text = "Depozit: " + copie.size() + "/" + Main3.D;
        if (copie.isEmpty()) text += " (GOL)";
        else if (copie.size() == Main3.D) text += " (PLIN)";
        stare.setText(text);
    }

    void actualizeazaProducator(int i, int total) {
        SwingUtilities.invokeLater(() ->
                producatori[i].setText("Producatorul " + (i + 1) + ": " + total + " vocale produse"));
    }

    void actualizeazaConsumator(int i, int luate) {
        SwingUtilities.invokeLater(() -> {
            consumatori[i].setValue(luate);
            consumatori[i].setString("Consumatorul " + (i + 1) + ": " + luate + "/" + Main3.Z);
        });
    }

    void simulareTerminata() {
        SwingUtilities.invokeLater(() -> start.setEnabled(true));
    }
}