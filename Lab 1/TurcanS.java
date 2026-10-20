class TurcanS implements Runnable {
  int start, stop, pas;
  int tablou[];

  public TurcanS(int start, int stop, int pas, int tablou[]) {
    this.start = start;
    this.stop = stop;
    this.pas = pas;
    this.tablou = tablou;
  }

  @Override
  public void run() {
    int sumFin = 0;
    int primPar = -1;
    int sumaPrecedenta = -1;
    int nrSume = 0;

    for (int pozitie = start; pozitie != stop + pas; pozitie += pas) {
      if (tablou[pozitie] % 2 != 0) {
        continue;
      }

      if (primPar == -1) {
        primPar = pozitie;
      } else {
        int suma = tablou[primPar] + tablou[pozitie];
        sumFin += suma;

        System.out.println(Thread.currentThread().getName() + " poz val 1: " + primPar + " " + tablou[primPar]
            + ", poz val 2 complet: " + pozitie + " " + tablou[pozitie] + ", suma: " + suma);

        nrSume++;

        if (sumaPrecedenta == -1) {
          sumaPrecedenta = suma;
        } else {
          int sumaSume = sumaPrecedenta + suma;

          System.out.println(
              Thread.currentThread().getName() + " suma " + (nrSume - 1) + " + suma " + nrSume + " = " + sumaSume);

          sumaPrecedenta = -1;
        }

        primPar = -1;
      }
    }
    System.out.println("Suma totală " + Thread.currentThread().getName() + ": " + sumFin);
  }
}
