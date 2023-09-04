package ru.job4j.threadlocal;

public class ThreadLocalDemo {
    public static final ThreadLocal<String> TL = new ThreadLocal<>();

    public static void main(String[] args) {
        TL.set("Это главный поток");
        new FirstThread().start();
        new SecondThread().start();
        System.out.println(TL.get());
    }
}
