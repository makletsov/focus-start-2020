package ru.makletsov.focusstart.ex2;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Process {
    private final List<Thread> producersPool;
    private final List<Thread> consumersPool;
    private final int producersCount;
    private final int producersTimeout;
    private final int consumersCount;
    private final int consumersTimeout;
    private final AtomicLong idAssigner;
    private final BlockingQueue<Resource> storage;
    private final int storageCapacity;

    public Process(PropertiesManager manager) {
        producersCount = manager.getProducersCount();
        producersTimeout = manager.getProducersTimeout();
        consumersCount = manager.getConsumersCount();
        consumersTimeout = manager.getConsumersTimeout();
        storageCapacity = manager.getStorageCapacity();

        idAssigner = new AtomicLong(0);
        storage = new ArrayBlockingQueue<>(storageCapacity);

        producersPool = createProducersPool();
        consumersPool = createConsumersPool();
    }

    public void start() {
        producersPool.forEach(Thread::start);
        consumersPool.forEach(Thread::start);
    }

    public void stop() {
        producersPool.forEach(Thread::interrupt);
        consumersPool.forEach(Thread::interrupt);
    }

    private List<Thread> createProducersPool() {
        return IntStream
                .range(0, producersCount)
                .mapToObj(this::createProducerThread)
                .collect(Collectors.toList());
    }

    private Thread createProducerThread(int producerId) {
        Producer producer = new Producer(producerId, storage, producersTimeout, idAssigner, storageCapacity);

        return new Thread(producer);
    }

    private List<Thread> createConsumersPool() {
        return IntStream
                .range(0, consumersCount)
                .mapToObj(this::createConsumerThread)
                .collect(Collectors.toList());
    }

    private Thread createConsumerThread(int consumerId) {
        Consumer consumer = new Consumer(consumerId, storage, consumersTimeout);

        return new Thread(consumer);
    }
}
