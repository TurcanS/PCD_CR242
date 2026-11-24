import javax.swing.*;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class lab4 {
    private static JTextArea textArea;

    public static void log(String msg) {
        SwingUtilities.invokeLater(() -> {
            if (textArea != null) {
                textArea.append(msg + "\n");
                textArea.setCaretPosition(textArea.getDocument().getLength());
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Lab 4 - Producator / Consumator");
            frame.setSize(900, 650);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            textArea = new JTextArea();
            textArea.setEditable(false);

            frame.setContentPane(new JScrollPane(textArea));
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            Runnable sarcina = () -> {
                try {
                    runSimulation();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    log("Simularea a fost intrerupta.");
                }
            };
            Thread controller = new Thread(sarcina, "Controller");
            controller.start();
        });
    }

    private static void runSimulation() throws InterruptedException {
        final int X = 2;
        final int Y = 5;
        final int Z = 3;
        final int D = 12;
        Store store = new Store(D);

        log("Aplicatia a pornit.");
        log("Parametri: X=" + X + ", Y=" + Y + ", Z=" + Z + ", D=" + D + "\n");
        Producer[] producers = new Producer[X];
        Consumer[] consumers = new Consumer[Y];

        for (int i = 0; i < X; i++) {
            producers[i] = new Producer(store, "Producator #" + (i + 1));
            producers[i].start();
        }
        for (int i = 0; i < Y; i++) {
            consumers[i] = new Consumer(store, Z, "Consumator #" + (i + 1));
            consumers[i].start();
        }

        for (Consumer c : consumers) {
            c.join();
        }
        log("\nToti consumatorii au fost indestulati cu " + Z + " obiecte.");
        store.finish();

        for (Producer p : producers) {
            p.join();
        }
        log("Programul se incheie.");
    }
}

class Store {
    private final ArrayList<Integer> stockList = new ArrayList<>();
    private final int capacity;
    private boolean finished = false;

    public Store(int capacity) {
        this.capacity = capacity;
    }

    public synchronized void finish() {
        finished = true;
        notifyAll();
    }

    public synchronized boolean isFinished() {
        return finished;
    }

    public synchronized int get(String name) {
        while (stockList.isEmpty()) {
            if (finished) {
                return -1;
            }
            lab4.log(name + ": depozitul este gol, astept...");
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return -1;
            }
        }
        int value = stockList.remove(stockList.size() - 1);

        lab4.log(name + " a luat: " + value + " | depozit=" + snapshot());
        notifyAll();
        return value;
    }

    public synchronized boolean putTwo(String name, int v1, int v2) {
        while (!finished && stockList.size() > capacity - 2) {
            lab4.log(name + ": depozitul este plin, astept... | depozit=" + snapshot());

            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }

        if (finished) {
            return false;
        }
        stockList.add(v1);
        stockList.add(v2);
        lab4.log(name + " a pus 2 obiecte: [" + v1 + ", " + v2 + "] | depozit=" + snapshot());
        notifyAll();
        return true;
    }

    private String snapshot() {
        return stockList.size() + "/" + capacity + " " + stockList;
    }
}

class Producer extends Thread {
    private final Store store;

    public Producer(Store store, String name) {
        super(name);
        this.store = store;
    }

    @Override
    public void run() {
        while (!store.isFinished()) {
            int v1 = 2 * (1 + ThreadLocalRandom.current().nextInt(20));
            int v2 = 2 * (1 + ThreadLocalRandom.current().nextInt(20));

            if (!store.putTwo(getName(), v1, v2)) {
                break;
            }

            try {
                Thread.sleep(120);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
        lab4.log(getName() + ": s-a oprit.");
    }
}

class Consumer extends Thread {
    private final Store store;
    private final int luat;

    public Consumer(Store store, int luat, String name) {
        super(name);
        this.store = store;
        this.luat = luat;
    }

    @Override
    public void run() {
        int preluat = 0;

        for (int i = 0; i < luat; i++) {
            int value = store.get(getName());
            if (value == -1) {
                return;
            }
            preluat++;

            try {
                Thread.sleep(80);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
        lab4.log(getName() + " a luat " + preluat + " obiecte. Este indestulat.");
    }
}
