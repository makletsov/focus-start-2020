package ru.makletsov.focusstart.printer;

public class ConsolePrinter implements Printer {
    @Override
    public void print(String string) {
        System.out.println(string);
    }
}
