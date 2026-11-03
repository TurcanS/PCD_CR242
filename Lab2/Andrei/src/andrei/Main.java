package andrei;

public final class Main {
    public static void main(String[] args) throws InterruptedException {
        boolean consola = false;
        boolean avansat = true;
        for (String arg : args) {
            switch (arg) {
                case "--console": consola = true; break;
                case "--mediu": avansat = false; break;
                case "--avansat": avansat = true; break;
                default: throw new IllegalArgumentException("Optiune necunoscuta: " + arg);
            }
        }
        if (consola) {
            Lab2.executa(avansat, System.out::println);
        } else {
            final boolean nivel = avansat;
            javax.swing.SwingUtilities.invokeLater(() -> new LabWindow(nivel).setVisible(true));
        }
    }
}
