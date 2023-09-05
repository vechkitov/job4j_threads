package ru.job4j.thread;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class Wget implements Runnable {

    private static final int BUFFER_SIZE = 1024;
    private final String url;
    private final int speed; /* байт в миллисекунду */

    public Wget(String url, int speed) {
        this.url = url;
        this.speed = speed;
    }

    @Override
    public void run() {
        String fileName = "tmp_" + url.substring(url.lastIndexOf('/') + 1);
        File file = new File(fileName);
        try (var in = new URL(url).openStream();
             var out = new FileOutputStream(file)) {
            var dataBuffer = new byte[BUFFER_SIZE];
            int bytesTotal = 0;
            int bytesRead;
            long downloadAt = System.nanoTime();
            while ((bytesRead = in.read(dataBuffer, 0, dataBuffer.length)) != -1) {
                bytesTotal += bytesRead;
                out.write(dataBuffer, 0, bytesRead);
                if (bytesTotal >= speed) {
                    doDelay(bytesTotal, downloadAt);
                    bytesTotal = 0;
                    downloadAt = System.nanoTime();
                }
            }
            doDelay(bytesTotal, downloadAt);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        validateParams(args);
        String url = args[0];
        int speed = Integer.parseInt(args[1]);
        Thread wget = new Thread(new Wget(url, speed));
        wget.start();
        wget.join();
    }

    public static void validateParams(String[] args) {
        if (args.length != 2) {
            throw new IllegalArgumentException("Программа должна принимать 2 параметра: url файла"
                    + " и требуемую скорость. Например: https://raw.githubusercontent.com"
                    + "/peterarsentev/course_test/master/pom.xml 100");
        }
        String url = args[0];
        try {
            new URL(url).toURI();
        } catch (MalformedURLException | URISyntaxException e) {
            throw new IllegalArgumentException("Некорректный url: " + url);
        }
        String speed = args[1];
        if (!speed.matches("^\\d+$")) {
            throw new IllegalArgumentException("Скорость должна быть целым положительным числом. "
                    + "Было передано значение: " + speed);
        }
    }

    private void doDelay(int bytesTotal, long downloadAt) throws InterruptedException {
        long actualSpeed = bytesTotal * 1_000_000L / (System.nanoTime() - downloadAt); /* байт в мс */
        long delay = actualSpeed / speed;
        Thread.sleep(delay);
        System.out.printf("Read %s bytes. Actual speed: %s bytes/ms. Required speed: %s. Delay: %s\n",
                bytesTotal, actualSpeed, speed, delay);
    }
}
