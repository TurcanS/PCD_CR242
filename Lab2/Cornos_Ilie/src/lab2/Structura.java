package lab2;

import java.util.function.Consumer;

/**
 * Varianta 4:
 * Main{GO{GZ{Tha(1), Thb(3), Thc(3), Thd(7)}}, GV{ThA(3)}, GF{Th1(5), Th2(3), Th3(9)}, Th1(3), Th2()}
 * Creează grupurile și firele, le enumeră și așteaptă terminarea lor.
 */
final class Structura {

    static final String FORMULA =
            "Main{GO{GZ{Tha(1), Thb(3), Thc(3), Thd(7)}}, GV{ThA(3)}, GF{Th1(5), Th2(3), Th3(9)}, Th1(3), Th2()}";

    /**
     * Ajustarea priorității față de valorile din formulă.
     * Cornos Ilie: +1  |  colegul din echipă: -1
     * Rezultatul este limitat la intervalul valid Thread.MIN_PRIORITY..Thread.MAX_PRIORITY (1..10).
     */
    static final int AJUSTARE = +1;

    /** Modul în care un nivel își creează firele de execuție. */
    interface FabricaFire {
        Thread creeaza(ThreadGroup grup, String nume);
    }

    private Structura() {
    }

    /** Valoare specială: firul nu are prioritate în formulă, de ex. Th2(). */
    static final int FARA_PRIORITATE = 0;

    /** Descrierea unui fir din formulă: în ce grup intră, cum se numește și ce prioritate are. */
    record DescriereFir(ThreadGroup grup, String nume, int prioritateFormula) {
    }

    /** Creează grupurile și firele variantei 4: întâi le pregătește pe toate, apoi le pornește. */
    static ThreadGroup construieste(FabricaFire fabrica) {
        // 1. Arborele de grupuri (al doilea parametru = numele, primul = grupul părinte)
        ThreadGroup main = new ThreadGroup("Main");
        ThreadGroup go = new ThreadGroup(main, "GO");
        ThreadGroup gz = new ThreadGroup(go, "GZ");
        ThreadGroup gv = new ThreadGroup(main, "GV");
        ThreadGroup gf = new ThreadGroup(main, "GF");

        // 2. Tabelul firelor, scris exact ca în formulă
        DescriereFir[] tabel = {
                new DescriereFir(gz, "Tha", 1),
                new DescriereFir(gz, "Thb", 3),
                new DescriereFir(gz, "Thc", 3),
                new DescriereFir(gz, "Thd", 7),
                new DescriereFir(gv, "ThA", 3),
                new DescriereFir(gf, "Th1", 5),
                new DescriereFir(gf, "Th2", 3),
                new DescriereFir(gf, "Th3", 9),
                new DescriereFir(main, "Th1", 3),
                new DescriereFir(main, "Th2", FARA_PRIORITATE),
        };

        // 3. Creăm toate firele și le stabilim prioritatea (încă nu rulează)
        Thread[] fire = new Thread[tabel.length];
        for (int i = 0; i < tabel.length; i++) {
            Thread fir = fabrica.creeaza(tabel[i].grup(), tabel[i].nume());
            // Un fir nou are deja prioritatea moștenită de la firul care l-a creat (main = 5)
            int baza = tabel[i].prioritateFormula() == FARA_PRIORITATE
                    ? fir.getPriority()
                    : tabel[i].prioritateFormula();
            fir.setPriority(ajusteaza(baza));
            fire[i] = fir;
        }

        // 4. Abia acum pornim toate firele
        for (Thread fir : fire) {
            fir.start();
        }
        return main;
    }

    /** Adună ajustarea mea (+1) și păstrează rezultatul între 1 și 10. */
    static int ajusteaza(int prioritate) {
        int rezultat = prioritate + AJUSTARE;
        if (rezultat > Thread.MAX_PRIORITY) {
            rezultat = Thread.MAX_PRIORITY;
        }
        if (rezultat < Thread.MIN_PRIORITY) {
            rezultat = Thread.MIN_PRIORITY;
        }
        return rezultat;
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
