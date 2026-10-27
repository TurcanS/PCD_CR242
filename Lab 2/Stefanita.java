import java.awt.BorderLayout;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

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

		CountDownLatch ready = new CountDownLatch(10);
		CountDownLatch release = new CountDownLatch(1);

		Runnable holdUntilListed = () -> {
			ready.countDown();
			try {
				release.await();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		};

		ThreadGroup sys = Thread.currentThread().getThreadGroup();
		Thread.currentThread().setPriority(4);
		sys.setDaemon(true);


		Thread t = new Stefanita(sys, "Th1", holdUntilListed);
		t.setPriority(8);
		t.start();

		t = new Stefanita(sys, "Th2", holdUntilListed);
		t.setPriority(8);
		t.start();

		t = new Stefanita(sys, "ThA", holdUntilListed);
		t.start();

		ThreadGroup g1 = new ThreadGroup("G1");
		ThreadGroup g3 = new ThreadGroup(g1, "G3");

		t = new Stefanita(g3, "Tha", holdUntilListed);
		t.start();

		t = new Stefanita(g3, "Thb", holdUntilListed);
		t.start();

		t = new Stefanita(g3, "Thc", holdUntilListed);
		t.start();

		t = new Stefanita(g3, "Thd", holdUntilListed);
		t.start();

		ThreadGroup g2 = new ThreadGroup("G2");

		t = new Stefanita(g2, "Th1", holdUntilListed);
		t.setPriority(5);
		t.start();

		t = new Stefanita(g2, "Th2", holdUntilListed);
		t.setPriority(6);
		t.start();

		t = new Stefanita(g2, "Th3", holdUntilListed);
		t.setPriority(6);
		t.start();

		ready.await();
		sys.list();
		release.countDown();
	}

	public Stefanita(ThreadGroup group, String name, Runnable task) {
		super(group, task, name);
	}
}
