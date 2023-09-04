package ru.job4j.concurrent;

import java.util.Arrays;
import java.util.List;

public class ParallelStreamExample {

    public static void main(String[] args) {
        /*
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        Stream<Integer> stream = list.parallelStream();
        System.out.println(stream.isParallel());
        Optional<Integer> multiplication = stream.reduce((a, b) -> a * b);
        System.out.println(multiplication.get());
         */

        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);

        /* параллельный поток не гарантирует, что исходный порядок элементов будет сохраняться */
        list.stream().parallel().peek(System.out::println);
        list.stream().parallel().forEach(System.out::println);

        /* исходный порядок будет сохранен */
        list.stream().parallel().forEachOrdered(System.out::println);
    }
}
