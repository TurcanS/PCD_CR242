public class Stefanita extends Thread {
	int numbers[];
	int pos;
	boolean asc, even;

	public Stefanita(int numbers[], int pos, boolean asc, boolean even) {
		this.numbers = numbers;
		this.pos = pos;
		this.asc = asc;
		this.even = even;
	}

	public boolean numbersValid(int[] numbers, int position) {
		return numbers != null && position >= 0 && position < numbers.length;
	}

	public int newPosition(boolean asc, int[] numbers, int position) {
		int nextPosition = asc ? position + 1 : position - 1;
		if (numbersValid(numbers, nextPosition)) {
			return nextPosition;
		}
		return -1;
	}

	public boolean matchesParity(int value) {
		return even ? value % 2 == 0 : value % 2 != 0;
	}

	@Override
	public void run() {
		int nr1 = pos;
		int sumTotal = 0;

		while (numbersValid(numbers, nr1)) {
			if (matchesParity(numbers[nr1])) {
				int nr2 = newPosition(asc, numbers, nr1);

				while (nr2 != -1) {
					if (matchesParity(numbers[nr2])) {
						int sum = numbers[nr1] + numbers[nr2];
						sumTotal += sum;

						System.out.println(getName()+" pos val 1: " + nr1 + " " + numbers[nr1]+ ", pos val 2: " + nr2 + " " + numbers[nr2]+ ", sum: " + sum);
						break;
					}
					nr2 = newPosition(asc, numbers, nr2);
				}
			}
			nr1 = newPosition(asc, numbers, nr1);
		}
	}
}
