package ru.makletsov.focusstart.io.output;

import ru.makletsov.focusstart.io.output.writers.OutputWriter;

public interface OutputFactory {
    OutputWriter getWriter(String writerKey, String writerPath);
}
