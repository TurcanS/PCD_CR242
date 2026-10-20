//Varianta 1:
	//https://drive.google.com/file/d/1ApeDaoUO_xtDDUAm209k39KqPsQiT4bk/view?usp=sharing
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
public class Stefanita extends Thread {

    public Stefanita() {
        super("Stefanita");
        setPriority(Thread.NORM_PRIORITY);
    }

    @Override
    public void run() {
        runGroup();
    }

    private void runGroup() {
        ThreadGroup group = new ThreadGroup(getName() + "-group");
        group.setMaxPriority(getPriority());

        for (int i = 0; i < 5; i++) {
            Thread t = new Thread(group, () ->
                System.out.println(Thread.currentThread().getName()
                    + " priority=" + Thread.currentThread().getPriority())
            , getName() + "-worker-" + i);

            t.setPriority(getPriority()); // Set before start()
            t.start();
        }

        group.list();
    }
}
