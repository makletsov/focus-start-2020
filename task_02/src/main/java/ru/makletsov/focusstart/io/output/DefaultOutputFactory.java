package ru.makletsov.focusstart.io.output;

import ru.makletsov.focusstart.io.output.writers.ConsoleOutputWriter;
import ru.makletsov.focusstart.io.output.writers.FileOutputWriter;
import ru.makletsov.focusstart.io.output.writers.OutputWriter;

public class DefaultOutputFactory implements OutputFactory {
    public OutputWriter getWriter(String writerKey, String writerPath) {
        switch (writerKey) {
            case "c":
                return new ConsoleOutputWriter();
            case "f":
                return new FileOutputWriter(writerPath);
            default:
                throw new IllegalArgumentException("Unknown output source type has been entered");
        }
    }
}
