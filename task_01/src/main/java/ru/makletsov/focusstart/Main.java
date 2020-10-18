package ru.makletsov.focusstart;

import ru.makletsov.focusstart.markup.DashedMarkup;
import ru.makletsov.focusstart.markup.Markup;

import java.util.Scanner;
import java.util.stream.IntStream;

public class Main {
    private static final int MINIMAL_TABLE_FACTOR = 1;
    private static final int MAXIMAL_TABLE_FACTOR = 32;
    private static final String POSITIVE_NUMBER_REGEXP = "^\\d+$";

    public static void main(String[] args) {
        int number = getTableFactor();

        StringBuilder stringBuilder = new StringBuilder();
        Markup markup = new DashedMarkup(number);
        LinesBuilder linesBuilder = new LinesBuilder(markup);

        buildTable(number, linesBuilder, stringBuilder);

        System.out.println(stringBuilder.toString());
    }

    private static int getTableFactor() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите размер таблицы умножения от " +
                MINIMAL_TABLE_FACTOR + " до " + MAXIMAL_TABLE_FACTOR + ":");
        String input = scanner.next();

        int number;

        while (!input.matches(POSITIVE_NUMBER_REGEXP)
                || (number = Integer.parseInt(input)) > MAXIMAL_TABLE_FACTOR
                || number < MINIMAL_TABLE_FACTOR) {
            System.out.println("Введите натуральное число от " +
                    MINIMAL_TABLE_FACTOR + " до " + MAXIMAL_TABLE_FACTOR + "!");
            input = scanner.next();
        }

        return number;
    }

    private static void buildTable(int number, LinesBuilder linesBuilder, StringBuilder stringBuilder) {
        stringBuilder.append(linesBuilder.getHeader(number))
                .append(System.lineSeparator());

        IntStream.range(1, number + 1)
                .forEach(multiplier -> stringBuilder
                        .append(linesBuilder.getRow(number, multiplier))
                        .append(System.lineSeparator()));
    }
}
