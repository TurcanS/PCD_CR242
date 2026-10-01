import java.util.*;

public class Main {
  public static void main(String args[])
  {
    Sum2 Th1,Th2;
    int[] tablou = new int[101];
    for(int i=0; i<100; i++){
      tablou[i] = (int)(Math.random()*99);
      System.out.print(tablou[i]+" ");
    }
    System.out.println(" ");
    Th2 = new Sum2(tablou, 99, 0, -1);
    Th2.setName("Th2");
    Th2.start();
  String Student1 = "Vleju Dumitru";
try {
    Th2.join();

    for (int i = 0; i < Student1.length(); i++) {
        System.out.print(Student1.charAt(i));
        Thread.sleep(100);
    }

    System.out.println();

    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  
  }}




class Sum2 extends Thread
{
  private int from, to, step;
  private int[] tablou;

  public Sum2( int[] tablou, int from, int to, int step) {
    this.from = from;
    this.to = to;
    this.tablou = tablou;
    this.step = step;
  }
  public void run() {
    int s1=0, s2=0, s=0;
    int i=from;
    while(i!=to){
      if(tablou[i] % 2 == 1){
        s1=i;
        i += step;
        do{
          if(tablou[i] % 2 == 1){
            s2=i;
            s = tablou[s1] + tablou[s2];
            System.out.println(getName()+": Sum1=" + tablou[s1] + "  ,  Sum2=" + tablou[s2] + "  ,  Sum=" + s);
            break;
          }
          i += step;
        }while(true);
      }
      i += step;

    }
  }
}
