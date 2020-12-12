package ru.makletsov.focusstart.ex2;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

public class Producer implements Runnable {
    private static final Logger LOG = Logger.getLogger("ru.makletsov.focusstart.ex2.Producer");

    private final int producerId;
    private final BlockingQueue<Resource> queue;
    private final int timeout;
    private final AtomicLong idAssigner;
    private final int storageCapacity;

    public Producer(int producerId, BlockingQueue<Resource> queue, int timeout, AtomicLong idAssigner, int storageCapacity) {
        this.producerId = producerId;
        this.queue = queue;
        this.timeout = timeout;
        this.idAssigner = idAssigner;
        this.storageCapacity = storageCapacity;
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

            LOG.info("[Producer#" + producerId + "] produced : " + resource.printInfo());

            if (queue.size() >= storageCapacity) {
                LOG.info("[Producer#" + producerId + "] is waiting for vacancy at the storage...");
            }

            queue.put(resource);
        }

    }
}
