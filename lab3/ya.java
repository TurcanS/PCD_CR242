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

    int sum_t = 0;
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
            sum_t += suma;
            break;
          }
          i += step;
      if((i < to && step < 0) || (i > to && step > 0)) break;
        }while(true);
      }
      i += step;
      if((i < to && step < 0) || (i > to && step > 0)) break;

    }
    
      System.out.println(getName()+ " Suma totala: " + sum_t);
  }
}