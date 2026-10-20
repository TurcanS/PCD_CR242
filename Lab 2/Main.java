public class Main {
	public static void main(String args[]) throws InterruptedException {
	//Varianta 1:
	//https://docs.google.com/document/d/1n0I33wTCQj8wKV9GSOEtaS8Z0ujU9II6w6PS_DP9MT8/edit?usp=sharing
	/*
	main
		- Th1(7)
		- Th2(7)
		- ThA(3)
		- G1
			- G3
				- Tha(3)
				- Thb(3)
				- Thc(3)
				- Thd(3)
		-G2
			- Th1(4)
			- Th2(5)
			- Th3(5)
	*/
	ThreadGroup sys = Thread.currentThread().getThreadGroup();
	Thread curr = Thread.currentThread();
	curr.setPriority(3);
	}
}
