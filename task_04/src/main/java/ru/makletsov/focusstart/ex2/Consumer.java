package ru.makletsov.focusstart.ex2;

import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;

public class Consumer implements Runnable {
    private static final Logger LOG = Logger.getLogger("ru.makletsov.focusstart.ex2.Consumer");

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
                if (queue.isEmpty()) {
                    LOG.info("[Consumer#" + consumerId + "] is waiting for resource availability...");
                }

                Resource resource = queue.take();
                consume(resource);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void consume(Resource resource) throws InterruptedException {
        LOG.info("[Consumer#" + consumerId + "] take resource : " + resource.printInfo());
        Thread.sleep(timeout);
    }
}
