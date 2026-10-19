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