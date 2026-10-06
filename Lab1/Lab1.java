
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
    int suma1=0,suma2=0,suma=0;
    int i=from;
    while(i!=to){
      if(tablou[i] % 2 == 1){
        suma1=i;
        i += step;
        if((i < to && step < 0) || (i > to && step > 0)) break;
      
        do{
          if(tablou[i] % 2 == 1){
            suma2=i;
            suma = tablou[suma1] + tablou[suma2];
            System.out.println(getName()+": Suma1=" + tablou[suma1] + " ;  Suma2=" + tablou[suma2] + ";  Suma=" + suma + ".");
            break;
          }
          i += step;
      if((i < to && step < 0) || (i > to && step > 0)) break;
        }while(true);
      }
      i += step;
      if((i < to && step < 0) || (i > to && step > 0)) break;

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
    int[] arr = new int[101];
    int k = 0;
    int sum;
    for(int i =from;(i < to && step > 0) || (i > to && step < 0);i+=step){
      if(tablou[i] %2 == 1) {
        arr[k] = tablou[i];
        k++;
      }
    }
    if(k %2 == 0) k--;

    for(int i =0;i<k;i+=2){
      sum = arr[i] + arr[i+1];
      System.out.println(getName()+": Sum1=" + arr[i]+ " ;  Sum2=" + arr[i+1] + ";  Sum=" + sum + ".");
    }
  }
  
}
