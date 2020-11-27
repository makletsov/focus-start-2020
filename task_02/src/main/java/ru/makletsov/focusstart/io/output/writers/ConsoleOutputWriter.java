package ru.makletsov.focusstart.io.output.writers;

public class ConsoleOutputWriter implements OutputWriter {
    @Override
    public void write(String outputString) {
        System.out.println(outputString);
    }
}
