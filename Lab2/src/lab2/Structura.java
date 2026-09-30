package lab2;

import java.util.function.Consumer;

/**
 * Varianta 4:
 * Main{GO{GZ{Tha(1), Thb(3), Thc(3), Thd(7)}}, GV{ThA(3)}, GF{Th1(5), Th2(3), Th3(9)}, Th1(3), Th2()}
 * Structura de grupuri este comună pentru ambele niveluri; diferă doar modul de creare a firelor.
 */
final class Structura {

    static final String FORMULA =
            "Main{GO{GZ{Tha(1), Thb(3), Thc(3), Thd(7)}}, GV{ThA(3)}, GF{Th1(5), Th2(3), Th3(9)}, Th1(3), Th2()}";

    /** Modul în care un nivel își creează firele de execuție. */
    interface FabricaFire {
        Thread creeaza(ThreadGroup grup, String nume);
    }

    private Structura() {
    }

    /** Creează grupurile și firele variantei 4, stabilește prioritățile și lansează firele. */
    static ThreadGroup construieste(FabricaFire fabrica) {
        ThreadGroup main = new ThreadGroup("Main");
        ThreadGroup go = new ThreadGroup(main, "GO");
        ThreadGroup gz = new ThreadGroup(go, "GZ");
        ThreadGroup gv = new ThreadGroup(main, "GV");
        ThreadGroup gf = new ThreadGroup(main, "GF");

        porneste(fabrica.creeaza(gz, "Tha"), 1);
        porneste(fabrica.creeaza(gz, "Thb"), 3);
        porneste(fabrica.creeaza(gz, "Thc"), 3);
        porneste(fabrica.creeaza(gz, "Thd"), 7);
        porneste(fabrica.creeaza(gv, "ThA"), 3);
        porneste(fabrica.creeaza(gf, "Th1"), 5);
        porneste(fabrica.creeaza(gf, "Th2"), 3);
        porneste(fabrica.creeaza(gf, "Th3"), 9);
        porneste(fabrica.creeaza(main, "Th1"), 3);
        // Th2() nu are prioritate indicată: rămâne cea implicită, moștenită de la firul creator
        fabrica.creeaza(main, "Th2").start();
        return main;
    }

    private static void porneste(Thread fir, int prioritate) {
        fir.setPriority(prioritate);
        fir.start();
    }

    /** Enumeră recursiv firele active și subgrupurile grupului dat. */
    static void enumera(ThreadGroup grup, String indent, Consumer<String> out) {
        out.accept(indent + "Grup " + grup.getName() + " (prioritate maximă " + grup.getMaxPriority() + ")");
        String interior = indent + "    ";

        Thread[] fire = new Thread[grup.activeCount()];
        int nrFire = grup.enumerate(fire, false);
        for (int i = 0; i < nrFire; i++) {
            out.accept(String.format("%sFir %-3s  grup = %-4s  prioritate = %d", interior,
                    fire[i].getName(), fire[i].getThreadGroup().getName(), fire[i].getPriority()));
        }

        ThreadGroup[] subgrupuri = new ThreadGroup[grup.activeGroupCount()];
        int nrGrupuri = grup.enumerate(subgrupuri, false);
        for (int i = 0; i < nrGrupuri; i++) {
            enumera(subgrupuri[i], interior, out);
        }
    }

    /** Așteaptă terminarea tuturor firelor din grup, inclusiv a celor din subgrupuri. */
    static void asteapta(ThreadGroup grup) throws InterruptedException {
        Thread[] fire = new Thread[grup.activeCount()];
        int nrFire = grup.enumerate(fire, true);
        for (int i = 0; i < nrFire; i++) {
            fire[i].join();
        }
    }
}
