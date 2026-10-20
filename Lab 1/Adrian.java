class Adrian extends Thread {
    int start, stop, pas;
    int tablou[];

    public Adrian(int start, int stop, int pas, int tablou[]) {
        this.start = start;
        this.stop = stop;
        this.pas = pas;
        this.tablou = tablou;
    }

    @Override
    public void run() {
        int sumFin = 0;
        int primImpar = -1;
        int sumaPrecedenta = -1;
        int nrSume = 0;

        for (int positie = start; positie != stop + pas; positie += pas) {

            if (tablou[positie] % 2 == 0) {
                continue;
            }

            if (primImpar == -1) {
                primImpar = positie;
            } else {
                int suma = tablou[primImpar] + tablou[positie];
                sumFin += suma;

                System.out.println(
                        getName() + " poz val 1: " + primImpar + " "
                                + tablou[primImpar] + ", poz val 2: " + positie
                                + " " + tablou[positie] + ", suma: " + suma);

                nrSume++;

                if (sumaPrecedenta == -1) {
                    sumaPrecedenta = suma;
                } else {
                    int sumaSume = sumaPrecedenta + suma;

                    System.out.println(
                            getName() + " suma " + (nrSume - 1)
                                    + " + suma " + nrSume + " = " + sumaSume);

                    sumaPrecedenta = -1;
                }

                primImpar = -1;
            }
        }
        System.out.println("Suma totală " + getName() + ": " + sumFin);
    }
}