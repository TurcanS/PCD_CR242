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
