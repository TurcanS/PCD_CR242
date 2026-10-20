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
