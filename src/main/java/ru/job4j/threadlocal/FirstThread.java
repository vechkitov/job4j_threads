package ru.job4j.threadlocal;

public class FirstThread extends Thread {

    @Override
    public void run() {
        ThreadLocalDemo.TL.set("Это поток 1.");
        System.out.println(ThreadLocalDemo.TL.get());
    }
}
