import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public final class Student1ThreadsTest {
    public static void main(String[] args) throws Exception {
        int[] mas = new int[100];
        for (int i = 0; i < mas.length; i++) {
            mas[i] = i + 1;
        }
        Collector collector = new Collector();
        List<String> lines = run(new Student1Threads(mas, collector), collector);
        checkResult(lines, "Student1-Th1", 85800);
        checkResult(lines, "Student1-Th2", 85800);
        check(lines.stream().anyMatch(s -> s.contains("Student1-Th1: mas[1]=2 * mas[3]=4")), "prima pereche directa");
        check(lines.stream().anyMatch(s -> s.contains("Student1-Th2: mas[99]=100 * mas[97]=98")), "prima pereche inversa");
        for (int i = 0; i < mas.length; i++) {
            check(mas[i] == i + 1, "tabloul nu trebuie modificat");
        }

        // Modificarea inainte de start dovedeste ca sarcinile citesc tabloul comun.
        collector = new Collector();
        Student1Threads student = new Student1Threads(mas, collector);
        Arrays.fill(mas, 100);
        lines = run(student, collector);
        checkResult(lines, "Student1-Th1", 250000);
        checkResult(lines, "Student1-Th2", 250000);
        Arrays.fill(mas, 1);
        collector = new Collector();
        lines = run(new Student1Threads(mas, collector), collector);
        checkResult(lines, "Student1-Th1", 25);
        checkResult(lines, "Student1-Th2", 25);
        System.out.println("OK: formule, 25 perechi/fir, ambele sensuri, limite 1/100, tablou comun nemodificat.");
    }

    private static List<String> run(Student1Threads student, Collector collector) throws Exception {
        Thread[] threads = student.getThreads();
        check(threads.length == 2 && threads[0] != threads[1], "doua fire distincte");
        for (Thread thread : threads) {
            check(thread.getState() == Thread.State.NEW, "constructorul nu porneste firele");
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join(5000);
            check(!thread.isAlive(), "firul trebuie sa termine");
        }
        return collector.lines;
    }

    private static void checkResult(List<String> lines, String name, long expected) {
        check(lines.stream().filter(s -> s.startsWith(name + ": mas[")).count() == 25, "25 perechi pentru " + name);
        check(lines.contains(name + ": suma finala = " + expected + System.lineSeparator()), "suma pentru " + name);
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    private static final class Collector implements java.util.function.Consumer<String> {
        private final List<String> lines = new CopyOnWriteArrayList<>();

        @Override
        public void accept(String line) {
            lines.add(line);
        }
    }
}
