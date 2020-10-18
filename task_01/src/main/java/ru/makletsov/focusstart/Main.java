package ru.makletsov.focusstart;

import ru.makletsov.focusstart.markup.DashedMarkup;
import ru.makletsov.focusstart.markup.Markup;

import java.util.Scanner;
import java.util.stream.IntStream;

public class Main {
    private static final int MINIMAL_TABLE_FACTOR = 1;
    private static final int MAXIMAL_TABLE_FACTOR = 32;
    private static final int ATTEMPTS_COUNT = 5;
    private static final String POSITIVE_NUMBER_REGEXP = "^\\d+$";

    public static void main(String[] args) {
        int number = getTableFactor();

        if (number <= 0) {
            System.out.println("У вас не осталось попыток!");
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            Markup markup = new DashedMarkup(number);

            buildTable(number, markup, stringBuilder);

            System.out.println(stringBuilder.toString());
        }
    }

    private static int getTableFactor() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите размер таблицы умножения от " +
                MINIMAL_TABLE_FACTOR + " до " + MAXIMAL_TABLE_FACTOR + ":");
        String input = scanner.next();

        int number = 0;
        int remainingAttempts = ATTEMPTS_COUNT;

        while (!input.matches(POSITIVE_NUMBER_REGEXP)
                || (number = Integer.parseInt(input)) > MAXIMAL_TABLE_FACTOR
                || number < MINIMAL_TABLE_FACTOR) {
            if (remainingAttempts <= 0) {
                break;
            }

            System.out.println("(" + remainingAttempts + ") Введите натуральное число от " +
                    MINIMAL_TABLE_FACTOR + " до " + MAXIMAL_TABLE_FACTOR + "!");
            input = scanner.next();
            remainingAttempts--;
        }

        return number;
    }

    private static void buildTable(int number, Markup markup, StringBuilder stringBuilder) {
        stringBuilder.append(markup.getHeader(number))
                .append(System.lineSeparator());

        IntStream.range(1, number + 1)
                .forEach(multiplier -> stringBuilder
                        .append(markup.getRow(number, multiplier))
                        .append(System.lineSeparator()));
    }
}
