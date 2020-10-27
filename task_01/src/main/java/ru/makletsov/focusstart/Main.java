package ru.makletsov.focusstart;

import ru.makletsov.focusstart.markup.DashedMarkup;
import ru.makletsov.focusstart.markup.Markup;

import java.util.Scanner;

public class Main {
    private static final int MINIMAL_TABLE_SIZE = 1;
    private static final int MAXIMAL_TABLE_SIZE = 32;
    private static final int ATTEMPTS_COUNT = 5;
    private static final String POSITIVE_NUMBER_REGEXP = "^\\d+$";

    private static final String INITIAL_PROMPT = "Введите размер таблицы умножения от " +
            MINIMAL_TABLE_SIZE + " до " + MAXIMAL_TABLE_SIZE + ":";

    public static void main(String[] args) {
        int tableSize = getTableSize();

        if (tableSize <= 0) {
            System.out.println("У вас не осталось попыток!");
        } else {
            Markup markup = new DashedMarkup(tableSize);

            String table = markup.buildTable(tableSize);

            System.out.println(table);
        }
    }

    private static int getTableSize() {
        System.out.println(INITIAL_PROMPT);

        int currentValue = -1;
        int remainingAttempts = ATTEMPTS_COUNT;

        Scanner scanner = new Scanner(System.in);
        String input = scanner.next();

        while (true) {
            if (input.matches(POSITIVE_NUMBER_REGEXP)) {
                currentValue = Integer.parseInt(input);

                if (isAllowed(currentValue)) {
                    break;
                }

                currentValue = -1;
            }

            if (remainingAttempts <= 0) {
                break;
            }

            System.out.println(getRepeatedPrompt(remainingAttempts));

            input = scanner.next();
            remainingAttempts--;
        }

        return currentValue;
    }

    private static String getRepeatedPrompt(int attemptsCount) {
        return "(" + attemptsCount + ") Введите натуральное число от " +
                MINIMAL_TABLE_SIZE + " до " + MAXIMAL_TABLE_SIZE + "!";
    }

    private static boolean isAllowed(int number) {
        return number >= MINIMAL_TABLE_SIZE && number <= MAXIMAL_TABLE_SIZE;
    }
}
