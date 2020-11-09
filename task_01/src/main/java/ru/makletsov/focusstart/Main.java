package ru.makletsov.focusstart;

import ru.makletsov.focusstart.markup.DashedMarkup;
import ru.makletsov.focusstart.markup.Markup;
import ru.makletsov.focusstart.printer.ConsolePrinter;
import ru.makletsov.focusstart.printer.Printer;

import java.util.Scanner;

public class Main {
    private static final int MINIMAL_TABLE_SIZE = 1;
    private static final int MAXIMAL_TABLE_SIZE = 32;
    private static final int INITIAL_ATTEMPTS_COUNT = 6;
    private static final String POSITIVE_NUMBER_REGEXP = "^\\d+$";

    private static final String INITIAL_PROMPT = "Введите размер таблицы умножения от " +
            MINIMAL_TABLE_SIZE + " до " + MAXIMAL_TABLE_SIZE + ":";

    public static void main(String[] args) {
        int tableSize = getTableSize();

        if (tableSize <= 0) {
            System.out.println("У вас не осталось попыток!");
        } else {
            Markup markup = new DashedMarkup(tableSize);
            String table = markup.buildTable();

            Printer printer = new ConsolePrinter();
            printer.print(table);
        }
    }

    private static int getTableSize() {
        int remainingAttempts = INITIAL_ATTEMPTS_COUNT;
        int tableSize = -1;

        Scanner scanner = new Scanner(System.in);

        while (remainingAttempts > 0) {
            System.out.println(getPrompt(remainingAttempts));

            String input = scanner.next();
            remainingAttempts--;

            if (input.matches(POSITIVE_NUMBER_REGEXP)) {
                int inputNumber = Integer.parseInt(input);

                tableSize = getValidNumberOrSpecialValue(inputNumber);
            }

            if (isAllowed(tableSize)) {
                break;
            }
        }

        return tableSize;
    }

    private static String getPrompt(int attemptsRemain) {
        return attemptsRemain == INITIAL_ATTEMPTS_COUNT ?
                INITIAL_PROMPT :
                getRepeatedPrompt(attemptsRemain);
    }

    private static int getValidNumberOrSpecialValue(int number) {
        return isAllowed(number) ? number : -1;
    }

    private static boolean isAllowed(int number) {
        return number >= MINIMAL_TABLE_SIZE && number <= MAXIMAL_TABLE_SIZE;
    }

    private static String getRepeatedPrompt(int attemptsCount) {
        return "(" + attemptsCount + ") Введите натуральное число от " +
                MINIMAL_TABLE_SIZE + " до " + MAXIMAL_TABLE_SIZE + "!";
    }
}
