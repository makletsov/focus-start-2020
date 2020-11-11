package ru.makletsov.focusstart.io.input.readers;

import java.util.List;

public interface InputReader {
    String getShapeTypeString();

    List<Double> getParameters();
}
