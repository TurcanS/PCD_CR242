public class Stefanita extends Thread {
	int tablou[];
	int poz;
	boolean asc, pare;

	public Stefanita(int tablou[], int poz, boolean asc, boolean pare) {
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
	@Override
	public void run() {
		int nr1 = poz;
		int sumTotal = 0;

		while (tablouValid(tablou, nr1)) {
			boolean perecheGasita = false;

			if (matchesParity(tablou[nr1])) {
				int nr2 = positieNoua(asc, tablou, nr1);

				while (nr2 != -1) {
					if (matchesParity(tablou[nr2])) {
						int sum = tablou[nr1] + tablou[nr2];
						sumTotal += sum;

						System.out.println(getName() + " poz val 1: " + nr1 + " " + tablou[nr1]+ ", poz val 2: " + nr2 + " " + tablou[nr2]	+ ", sum: " + sum);

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
