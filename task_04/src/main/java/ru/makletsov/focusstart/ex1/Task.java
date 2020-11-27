package ru.makletsov.focusstart.ex1;

import java.util.List;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.function.Function;
import java.util.stream.IntStream;

public class Task extends RecursiveTask<Double> {
    static final int AVAILABLE_CORES_COUNT = Runtime.getRuntime().availableProcessors();

    private final int leftBorder;
    private final int rightBorder;
    private final int forked;
    private final Function<Integer, Double> function;

    public Task(int argumentsCount, Function<Integer, Double> function) {
        this(0, argumentsCount, 0, function);
    }

    private Task(int leftBorder, int rightBorder, int forked, Function<Integer, Double> function) {
        this.leftBorder = leftBorder;
        this.rightBorder = rightBorder;
        this.forked = forked;
        this.function = function;
    }

    @Override
    protected Double compute() {
        if (forked >= AVAILABLE_CORES_COUNT - 1) {
            return processTask(leftBorder, rightBorder);
        }

        return ForkJoinTask
                .invokeAll(getForkedTasks())
                .stream()
                .mapToDouble(ForkJoinTask::join)
                .sum();
    }

    private List<Task> getForkedTasks() {
        int rangeSize = rightBorder - leftBorder;
        int middleBorder = leftBorder + rangeSize / 2;

        Task task1 = new Task(leftBorder, middleBorder, forked + 2, function);
        Task task2 = new Task(middleBorder, rightBorder, forked + 2, function);

        return List.of(task1, task2);
    }

    private Double processTask(int leftBorder, int rightBorder) {
        return IntStream
                .range(leftBorder, rightBorder)
                .mapToDouble(function::apply)
                .sum();
    }
}
