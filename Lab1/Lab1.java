
public class Lab1 {
  public static void main(String args[])
  {
    Sum2 Th3,Th4;
    Sum1 Th1,Th2;

    int[] tablou = new int[101];
    for(int i=0; i<100; i++){
      tablou[i] = (int)(Math.random()*99);
      System.out.print(tablou[i]+" ");
    }
    System.out.println(" ");

    Th1 = new Sum1(tablou, 0, 49, 1);
    Th1.setName("Th1");
    Th1.start();

    Th2 = new Sum1(tablou, 50, 99, 1);
    Th2.setName("Th2");
    Th2.start();

    Th3 = new Sum2(tablou, 99, 50, -1);
    Th3.setName("Th3");
    Th3.start();

    Th4 = new Sum2(tablou, 49, 0, -1);
    Th4.setName("Th4");
    Th4.start();


  String Nume = "Grup-3 Vleju Dumitru Cemirtan Edgar";

try {
    Th1.join();
    Th2.join();
    Th3.join();
    Th4.join();

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
      if(i > to) break;
        do{
          if(tablou[i] % 2 == 1){
            s2=i;
            s = tablou[s1] + tablou[s2];
            System.out.println(getName()+": Sum1=" + tablou[s1] + "  ,  Sum2=" + tablou[s2] + "  ,  Sum=" + s);
            break;
          }
          i += step;
          if(i > to) break;
        }while(true);
      }
      i += step;
      if(i > to) break;

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
    int i=from;
    while(i!=to){
      if(tablou[i] % 2 == 1){
        s1=i;
        i += step;
        if(i < to) break;
        do{
          if(tablou[i] % 2 == 1){
            s2=i;
            s = tablou[s1] + tablou[s2];
            System.out.println(getName()+": Sum1=" + tablou[s1] + "  ,  Sum2=" + tablou[s2] + "  ,  Sum=" + s);
            break;
          }
          i += step;
          if(i < to) break;
        }while(true);
      }
      i += step;

      if(i < to) break;
    }
  }
}
