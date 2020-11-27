package ru.makletsov.focusstart.ex2;

import java.nio.file.Files;
import java.util.concurrent.BlockingQueue;

public class Consumer implements Runnable {
    private final int consumerId;
    private final BlockingQueue<Resource> queue;
    private final int timeout;

    public Consumer(int consumerId, BlockingQueue<Resource> queue, int timeout) {
        this.consumerId = consumerId;
        this.queue = queue;
        this.timeout = timeout;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Resource resource = queue.take();
                consume(resource);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void consume(Resource resource) throws InterruptedException {
        System.out.println(TimeFormatter.getCurrentTime() +
                "[Consumer#" + consumerId + "] take resource : " + resource.printInfo());
        Thread.sleep(timeout);
        System.out.println("[Consumer#" + consumerId + "] ready for consuming new resource!");
    }
}
