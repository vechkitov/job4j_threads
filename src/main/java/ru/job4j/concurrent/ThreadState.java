package ru.job4j.concurrent;

public class ThreadState {
    public static void main(String[] args) {
        Thread first = new Thread(
                () -> {
                }
        );
        Thread second = new Thread(
                () -> {
                }
        );
        printThreadInfo(first);
        printThreadInfo(second);
        first.start();
        second.start();
        while (first.getState() != Thread.State.TERMINATED) {
            printThreadInfo(first);
        }
        while (second.getState() != Thread.State.TERMINATED) {
            printThreadInfo(second);
        }
        printThreadInfo(first);
        printThreadInfo(second);
        System.out.println("Работа завершена");
    }

    private static void printThreadInfo(Thread thread) {
        System.out.println(thread.getName() + ": " + thread.getState());
    }
}
