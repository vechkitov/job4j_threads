package ru.job4j.threads;

/**
 * https://job4j.ru/profile/exercise/73/task/989/452093
 */
public class RaceConditionExample {
    public static int num = 0;

    public static synchronized void incrementNum() {
        num++;
    }

    public static void main(String[] args) throws InterruptedException {
        Runnable task = () -> {
            for (int i = 0; i < 100_000; i++) {
                //num++;
                incrementNum();
//                int current = num;
//                int next = ++num;
//                if (current + 1 != next) {
//                    throw new IllegalStateException("Некорректное сравнение: " + current + " + 1 = " + next);
//                }
            }
        };
        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println("num = " + num);
    }
}
