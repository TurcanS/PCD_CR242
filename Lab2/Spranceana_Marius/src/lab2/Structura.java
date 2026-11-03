package lab2;

import java.util.function.Consumer;

/**
 * Varianta 4:
 * Main{GO{GZ{Tha(1), Thb(3), Thc(3), Thd(7)}}, GV{ThA(3)}, GF{Th1(5), Th2(3), Th3(9)}, Th1(3), Th2()}
 * Enumeră grupurile și firele și așteaptă terminarea lor.
 */
final class Structura {

    static final String FORMULA =
            "Main{GO{GZ{Tha(1), Thb(3), Thc(3), Thd(7)}}, GV{ThA(3)}, GF{Th1(5), Th2(3), Th3(9)}, Th1(3), Th2()}";

    private Structura() {
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
