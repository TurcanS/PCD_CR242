public class Stefanita extends Thread {
	int[] tablou;
	int poz;
	boolean asc, pare;

	public Stefanita(int[] tablou, int poz, boolean asc, boolean pare) {
		this.tablou = tablou;
		this.poz = poz;
		this.asc = asc;
		this.pare = pare;
	}

	public boolean tablouValid(int[] tablou, int pozitie) {
		return tablou != null && pozitie >= 0 && pozitie < tablou.length;
	}

	public int positieNoua(boolean asc, int[] tablou, int pozitie) {
		int pozitieUrmatoare = asc ? pozitie + 1 : pozitie - 1;
		if (tablouValid(tablou, pozitieUrmatoare)) {
			return pozitieUrmatoare;
		}
		return -1;
	}

	public boolean matchesParity(int value) {
		return pare ? value % 2 == 0 : value % 2 != 0;
	}

	private void afiseazaGrup(int[] valori1, int[] valori2) {
		int suma1 = (valori1[0] + valori2[0]);
		int suma2 = (valori1[1] + valori2[1]);
		int suma = suma1 + suma2;
		System.out.println(
			getName() + " " + valori1[0] + " + " + valori2[0] + " = "+ suma1 +
			"  " + valori1[1] + " + " + valori2[1] + " = " + suma2 + "  "+
			suma1 + " + " + suma2 + " = "+ suma
		);
	}


	@Override
	public void run() {
		int nr1 = poz;

		int[] valori1Grup = new int[2];
		int[] valori2Grup = new int[2];
		int nrPerechiGrup = 0;

		while (tablouValid(tablou, nr1)) {
			boolean perecheGasita = false;

			if (matchesParity(tablou[nr1])) {
				int nr2 = positieNoua(asc, tablou, nr1);

				while (nr2 != -1) {
					if (matchesParity(tablou[nr2])) {
						valori1Grup[nrPerechiGrup] = tablou[nr1];
						valori2Grup[nrPerechiGrup] = tablou[nr2];
						nrPerechiGrup++;

						if (nrPerechiGrup == 2) {
							afiseazaGrup(valori1Grup, valori2Grup);
							nrPerechiGrup = 0;
						}

						nr1 = positieNoua(asc, tablou, nr2);
						perecheGasita = true;
						break;
					}

					nr2 = positieNoua(asc, tablou, nr2);
				}
			}

			if (!perecheGasita) {
				nr1 = positieNoua(asc, tablou, nr1);
			}
		}
	}





}
