package ru.job4j.concurrent;

public class ConsoleProgress implements Runnable {

    private static final char[] PROCESS = new char[]{'-', '\\', '|', '/'};
    private static int index = 0;

    public static void main(String[] args) throws InterruptedException {
        Thread progress = new Thread(new ConsoleProgress());
        progress.start();
        Thread.sleep(5000); /* симулируем выполнение параллельной задачи в течение 5 секунд. */
        progress.interrupt();
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                System.out.print("\rLoading ... " + PROCESS[index++ % PROCESS.length]);
                Thread.sleep(500);
            } catch (InterruptedException e) { /* сбрасывает флаг interrupted */
                Thread.currentThread().interrupt();
            }
        }
    }
}
