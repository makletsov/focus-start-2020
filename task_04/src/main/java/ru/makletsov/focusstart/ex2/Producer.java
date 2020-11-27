package ru.makletsov.focusstart.ex2;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

public class Producer implements Runnable {
    private final int producerId;
    private final BlockingQueue<Resource> queue;
    private final int timeout;
    private final AtomicLong idAssigner;

    public Producer(int producerId, BlockingQueue<Resource> queue, int timeout, AtomicLong idAssigner) {
        this.producerId = producerId;
        this.queue = queue;
        this.timeout = timeout;
        this.idAssigner = idAssigner;
    }

    @Override
    public void run() {
        try {
            produce();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void produce() throws InterruptedException {
        while (true) {
            long id = idAssigner.incrementAndGet();
            Resource resource = new Resource(id);
            Thread.sleep(timeout);

            System.out.println(TimeFormatter.getCurrentTime() +
                    "[Producer#" + producerId + "] produced : " + resource.printInfo());

            queue.put(resource);

            System.out.println("[Producer#" + producerId + "] ready to produce new resource!");
        }

    }
}
