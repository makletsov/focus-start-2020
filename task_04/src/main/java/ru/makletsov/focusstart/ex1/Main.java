package ru.makletsov.focusstart.ex1;

import java.util.concurrent.ForkJoinPool;
import java.util.function.Function;

class Main {
    public static Function<Integer, Double> COMPLEX_FUNCTION = Math::atan;
    public static String ERROR_MESSAGE_PREFIX = "Input is incorrect! ";

    public static void main(String[] args) {
        try {
            int inputArgument = getInputArgument(args);

            Task task = new Task(inputArgument, COMPLEX_FUNCTION);

            ForkJoinPool
                    .commonPool()
                    .execute(task);

            System.out.println("Result is: " + task.join());
        } catch (IndexOutOfBoundsException | NumberFormatException e) {
            System.out.printf(ERROR_MESSAGE_PREFIX, "Is not a number.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private static int getInputArgument(String[] args) {
        int n = Integer.parseInt(args[0]);

        if (n < 0) {
            throw new IllegalArgumentException(ERROR_MESSAGE_PREFIX + "Number should be positive.");
        }

        return n;
    }
}