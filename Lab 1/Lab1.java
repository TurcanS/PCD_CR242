import java.util.*;
public class Main {
	public static void main(String args[]){
		Counter1 cnt1, cnt2;
		int[] tablou = new int[101];

		for(int i=0; i<100; i++){
			tablou[i] = (int)(Math.random()*99);
			System.out.print(tablou[i]+" ");
			}
		System.out.println(" ");
		cnt1 = new Counter1(0, 99, 1, tablou);
		cnt2 = new Counter1(99, 0, -1, tablou);
		cnt1.start();
		cnt1.setName("Unu");
		cnt2.start();
		cnt2.setName("Doi");

		}
		}

class Counter1 extends Thread {
	private int from, to, step;
	private int[] tablou;
	public Counter1(int from, int to, int step, int[] tablou) {
		this.from = from;
		this.to = to;
		this.step = step;
		this.tablou = tablou;
	}

	public void run() {
		int s1=0, s2=0, s=0;
		int i=from;
		while(i!=to){
			if(tablou[i]<=50) {
				s1=i;
				i+=step;
				do {
				if(tablou[i]<=50) {
					s2=i;
					s=s1+s2;
					System.out.println(getName()+" " + s1+ " " + s2 +" "+s+ " "+ tablou[s1]+""+tablou[s2] );
					break;
				}
			i+=step;
			} while(true);
			// i+=step; what
			}
			i+=step;
		}
	}
}
