class TurcanS extends Thread {
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

    for (int positie = start; positie != stop + pas; positie += pas) {
      if (tablou[positie] % 2 != 0)
        continue;

      if (primPar == -1) {
        primPar = positie;
      } else {
        int suma = tablou[primPar] + tablou[positie];
        sumFin += suma;
        System.out.println(getName() + " poz val 1: " + primPar + " " + tablou[primPar] + ", poz val 2: "
            + positie + " " + tablou[positie] + ", suma: " + suma);
        primPar = -1;
      }
    }
    System.out.println("Sum Total " + getName() + ": " + sumFin);
  }
}
