
public class Lab3 {

  public static void main(String[] args) {
    int[] tablou = new int[2200];

    for (int i = 0; i < 2200; i++) {
      tablou[i] = (int) (Math.random() * 99);
    }
    Thread s2 = new Thread(() -> {
      System.out.println("Thread 2");
    });
    Sarcina s3 = new Sarcina(tablou, 3);
    Sarcina s4 = new Sarcina(tablou, 4);
    s2.start();
    s3.start();
    try {
      s2.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    s4.start();

  }
}

class Sarcina extends Thread {
  private int sar;
  private int[] tablou;

  public Sarcina(int[] tablou, int sar) {
    this.sar = sar;
    this.tablou = tablou;
  }

  @Override
  public void run() {
    if (sar == 3) {
      System.out.print("Thread 3:");
      System.out.print("\n");
      for (int i = 0; i <= 798; i++) {
        System.out.print(tablou[i] + " ");
      }

      try {

        Thread.sleep(3000);
      } catch (InterruptedException e) {
        System.out.print(e);
      }
      String disciplina = "Programarea Concurenta si Distribuita";

      System.out.print("\n");
      System.out.print("Thread 3:");
      for (int i = 0; i < disciplina.length(); i++) {
        System.out.print(disciplina.charAt(i) + "");
        try {

          Thread.sleep(100);
        } catch (InterruptedException e) {
          System.out.print(e);
        }
      }
    } else if (sar == 4) {

      System.out.print("\n");
      System.out.print("Thread 4:");
      for (int i = 2111; i >= 1456; i--) {

        System.out.print(tablou[i] + " ");
      }

      String Grupa = "\n \n CR-242";
      System.out.print("Thread 4:");
      for (int i = 0; i < Grupa.length(); i++) {
        System.out.print(Grupa.charAt(i) + "");
        try {

          Thread.sleep(100);
        } catch (InterruptedException e) {
          System.out.print(e);
        }
      }
    }
  }
}
