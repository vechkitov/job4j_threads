package ru.job4j.concurrent;

public class ConsoleProgress implements Runnable {

    public static void main(String[] args) throws InterruptedException {
        Thread progress = new Thread(new ConsoleProgress());
        progress.start();
        Thread.sleep(5000); /* симулируем выполнение параллельной задачи в течение 5 секунд. */
        progress.interrupt();
    }

    @Override
    public void run() {
        var process = new char[]{'-', '\\', '|', '/'};
        int index = 0;
        while (!Thread.currentThread().isInterrupted()) {
            try {
                System.out.print("\rLoading ... " + process[index % process.length]);
                Thread.sleep(500);
                index++;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
