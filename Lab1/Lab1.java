
public class Lab1 {
  public static void main(String args[])
  {
    Sum1  Th1_1,Th1_2;
    Sum2  Th2_1,Th2_2;

    int[] tablou = new int[101];
    for(int i=0; i<100; i++){
      tablou[i] = (int)(Math.random()*99);
      System.out.print(tablou[i]+" ");
    }
    System.out.println(" ");

    Th1_1 = new Sum1(tablou, 0, 99, 1);
    Th1_1.setName("Th1.1");
    Th1_1.start();


    Th1_2 = new Sum1(tablou, 99, 0, -1);
    Th1_2.setName("Th1.2");
    Th1_2.start();

    Th2_1 = new Sum2(tablou, 0, 99, 1);
    Th2_1.setName("Th2.1");
    Th2_1.start();

    Th2_2 = new Sum2(tablou, 99, 0, -1);
    Th2_2.setName("Th2.2");
    Th2_2.start();


  String Nume = "Grup-3 Vleju Dumitru Cemirtan Edgar";

try {
    Th1_1.join();
    Th1_2.join();
    Th2_1.join();
    Th2_2.join();

    for (int i = 0; i < Nume.length(); i++) {
        System.out.print(Nume.charAt(i));
        Thread.sleep(100);
    }


    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  
  }}



class Sum1 extends Thread
{
  private int from, to, step;
  private int[] tablou;

  public Sum1( int[] tablou, int from, int to, int step) {
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
        if((i > to && step > 0) || (i < to && step < 0)) break;
      
        do{
          if(tablou[i] % 2 == 1){
            s2=i;
            s = tablou[s1] + tablou[s2];
            System.out.println(getName()+": Sum1=" + tablou[s1] + "  ,  Sum2=" + tablou[s2] + "  ,  Sum=" + s);
            break;
          }
          i += step;
      if((i > to && step > 0) || (i < to && step < 0)) break;
        }while(true);
      }
      i += step;
      if((i > to && step > 0) || (i < to && step < 0)) break;

    }
  }
}

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
    int f=from;
    while(f!=to){
      if(tablou[f] % 2 == 1){
        s1=f;
        f += step;
        if((f > to && step > 0) || (f < to && step < 0)) break;
      
        do{
          if(tablou[f] % 2 == 1){
            s2=f;
            s = tablou[s1] + tablou[s2];
            System.out.println(getName()+": S1=" + tablou[s1] + "  ,  S2=" + tablou[s2] + "  ,  S=" + s);
            break;
          }
          f += step;
      if((f > to && step > 0) || (f < to && step < 0)) break;
        }while(true);
      }
      f += step;
      if((f > to && step > 0) || (f < to && step < 0)) break;

    }
  }
}
