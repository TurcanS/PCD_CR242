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
import java.awt.BorderLayout;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import java.io.ByteArrayOutputStream;


public class Stefanita extends Thread {

	public static void main(String[] args) throws Exception {
		JTextArea[] area = new JTextArea[1];

		SwingUtilities.invokeAndWait(() -> {
			area[0] = new JTextArea(35, 50);
			area[0].setEditable(false);

			JFrame frame = new JFrame("Laborator cu ThreadGroup");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.add(new JScrollPane(area[0]), BorderLayout.CENTER);
			frame.pack();
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
		});

		PrintStream guiOutput = new PrintStream(new OutputStream() {
			private final ByteArrayOutputStream lineBuffer =
					new ByteArrayOutputStream();

			@Override
			public synchronized void write(int value) {
				lineBuffer.write(value);

				if (value == '\n') {
					displayLine();
				}
			}

			@Override
			public synchronized void write(
					byte[] bytes, int offset, int length) {
				for (int i = offset; i < offset + length; i++) {
					write(bytes[i]);
				}
			}

			@Override
			public synchronized void flush() {
				if (lineBuffer.size() > 0) {
					displayLine();
				}
			}

			private void displayLine() {
				String text = new String(
						lineBuffer.toByteArray(),
						StandardCharsets.UTF_8
				);
				lineBuffer.reset();

				if (!text.contains("AWT-EventQueue-0")) {
					SwingUtilities.invokeLater(() -> {
						area[0].append(text);
						area[0].setCaretPosition(
								area[0].getDocument().getLength()
						);
					});
				}
			}
		}, true, StandardCharsets.UTF_8);
		System.setOut(guiOutput);


		ThreadGroup sys = Thread.currentThread().getThreadGroup();

		Thread curr = Thread.currentThread();
		curr.setPriority(4);

		Thread t = new Stefanita(sys, "Th1");
		t.setPriority(8);
		t.start();
		t = new Stefanita(sys, "Th2");
		t.setPriority(8);
		t.start();
		t = new Stefanita(sys, "ThA");
		t.start();

		ThreadGroup g1 = new ThreadGroup("G1");
		g1.setMaxPriority(Thread.MAX_PRIORITY);
		ThreadGroup g3 = new ThreadGroup(g1, "G3");
		g3.setMaxPriority(Thread.MAX_PRIORITY);

		t = new Stefanita(g3, "Tha");
		t.start();
		t = new Stefanita(g3, "Thb");
		t.start();
		t = new Stefanita(g3, "Thc");
		t.start();
		t = new Stefanita(g3, "Thd");
		t.start();

		ThreadGroup g2 = new ThreadGroup("G2");
		g2.setMaxPriority(Thread.MAX_PRIORITY);

		t = new Stefanita(g2, "Th1");
		t.setPriority(5);
		t.start();
		t = new Stefanita(g2, "Th2");
		t.setPriority(6);
		t.start();
		t = new Stefanita(g2, "Th3");
		t.setPriority(6);
		t.start();

		sys.list();

	}

	public Stefanita(ThreadGroup g, String name) {
		super(g, name);
	}

	public void run() {
		try{
			Thread.sleep(100);
		} catch (InterruptedException e){
			System.out.println(e);
		}
		System.out.println( getName() + " are prioritatea " + getPriority() + ", se afla in grupul " +getThreadGroup().getName() + " care are grupul parinte " + getThreadGroup().getParent().getName() );
	}

}
