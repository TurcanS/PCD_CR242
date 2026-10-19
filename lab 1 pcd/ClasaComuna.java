import java.util.Random;

// 1. Clasa lui Cătălin - Folosește bucle FOR și salvează prima poziție
class ClasaCatalin extends Thread {
    private int idFir;
    private int[] mas;

    public ClasaCatalin(int idFir, int[] mas) {
        this.idFir = idFir;
        this.mas = mas;
    }

    @Override
    public void run() {
        String eticheta = "Th" + idFir + " (Cătălin) : ";
        int primaPozitie = -1;
        String linie1 = null; // prima adunare din grup
        int suma1 = 0;

        if (idFir == 1) {
            // Firul 1: stânga -> dreapta
            for (int i = 0; i < mas.length; i++) {
                if (mas[i] % 2 == 0) {
                    if (primaPozitie == -1) {
                        primaPozitie = i;
                    } else {
                        int suma = primaPozitie + i;
                        String linie = eticheta + primaPozitie + " + " + i + " = " + suma;
                        if (linie1 == null) {
                            linie1 = linie;
                            suma1 = suma;
                        } else {
                            System.out.println(linie1 + "\n" + linie + "\n"
                                    + eticheta + "Suma: " + suma1 + " + " + suma + " = " + (suma1 + suma));
                            linie1 = null;
                        }
                        primaPozitie = -1;
                    }
                }
            }
        } else if (idFir == 2) {
            // Firul 2: dreapta -> stânga
            for (int i = mas.length - 1; i >= 0; i--) {
                if (mas[i] % 2 == 0) {
                    if (primaPozitie == -1) {
                        primaPozitie = i;
                    } else {
                        int suma = primaPozitie + i;
                        String linie = eticheta + primaPozitie + " + " + i + " = " + suma;
                        if (linie1 == null) {
                            linie1 = linie;
                            suma1 = suma;
                        } else {
                            System.out.println(linie1 + "\n" + linie + "\n"
                                    + eticheta + "Suma: " + suma1 + " + " + suma + " = " + (suma1 + suma));
                            linie1 = null;
                        }
                        primaPozitie = -1;
                    }
                }
            }
        }

        // dacă a rămas o singură adunare fără pereche, o afișăm
        if (linie1 != null) {
            System.out.println(linie1);
        }
    }
}

// 2. ClasaAnatol - Folosește bucle WHILE, vectori temporari și flag-uri
class ClasaAnatol extends Thread {
    private int idFir;
    private int[] date;

    public ClasaAnatol(int idFir, int[] date) {
        this.idFir = idFir;
        this.date = date;
    }

    @Override
    public void run() {
        String eticheta = "Th" + idFir + " (Anatol)   : ";
        String linie1 = null;
        int suma1 = 0;

        if (idFir == 3) {
            // Firul 3: stânga -> dreapta
            int index = 0;
            int[] pozitii = new int[2];
            int gasite = 0;

            while (index < date.length) {
                if (date[index] % 2 == 0) {
                    pozitii[gasite] = index;
                    gasite++;

                    if (gasite == 2) {
                        int suma = pozitii[0] + pozitii[1];
                        String linie = eticheta + pozitii[0] + " + " + pozitii[1] + " = " + suma;
                        if (linie1 == null) {
                            linie1 = linie;
                            suma1 = suma;
                        } else {
                            System.out.println(linie1 + "\n" + linie + "\n"
                                    + eticheta + "Suma: " + suma1 + " + " + suma + " = " + (suma1 + suma));
                            linie1 = null;
                        }
                        gasite = 0;
                    }
                }
                index++;
            }
        } else if (idFir == 4) {
            // Firul 4: dreapta -> stânga
            int pozitieCurenta = date.length - 1;
            int pozitiePrecedenta = -1;
            boolean avemJumatate = false;

            while (pozitieCurenta >= 0) {
                if ((date[pozitieCurenta] & 1) == 0) {
                    if (!avemJumatate) {
                        pozitiePrecedenta = pozitieCurenta;
                        avemJumatate = true;
                    } else {
                        int suma = pozitiePrecedenta + pozitieCurenta;
                        String linie = eticheta + pozitiePrecedenta + " + " + pozitieCurenta + " = " + suma;
                        if (linie1 == null) {
                            linie1 = linie;
                            suma1 = suma;
                        } else {
                            System.out.println(linie1 + "\n" + linie + "\n"
                                    + eticheta + "Suma: " + suma1 + " + " + suma + " = " + (suma1 + suma));
                            linie1 = null;
                        }
                        avemJumatate = false;
                    }
                }
                pozitieCurenta--;
            }
        }

        if (linie1 != null) {
            System.out.println(linie1);
        }
    }
}

// 3. Clasa Comună
public class ClasaComuna {
    public static void main(String[] args) {
        int[] mas = new int[100];
        Random rand = new Random();

        System.out.println("Tabloul generat comun (mas[]): ");
        for (int i = 0; i < mas.length; i++) {
            mas[i] = rand.nextInt(100) + 1;
            System.out.print(mas[i] + " ");
        }
        System.out.println("\n\n--- Start execuție cele 4 fire ---");

        ClasaCatalin th1 = new ClasaCatalin(1, mas);
        ClasaCatalin th2 = new ClasaCatalin(2, mas);
        ClasaAnatol th3 = new ClasaAnatol(3, mas);
        ClasaAnatol th4 = new ClasaAnatol(4, mas);

        th1.start();
        th2.start();
        th3.start();
        th4.start();

        try {
            th1.join();
            th2.join();
            th3.join();
            th4.join();
        } catch (InterruptedException e) {
            System.out.println("Eroare la join: " + e.getMessage());
        }

        System.out.println("--- Toate cele 4 fire și-au terminat execuția ---\n");

        String infoStudenti = "Lucrarea de laborator a fost efectuata de: Borodachi Catalin si Diacov Anatol grupa CR-242.";

        for (char c : infoStudenti.toCharArray()) {
            System.out.print(c);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {}
        }
        System.out.println();
    }
}