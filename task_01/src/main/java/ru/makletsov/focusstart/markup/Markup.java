package ru.makletsov.focusstart.markup;

public abstract class Markup {
    private final int tableSize;

    private static final int MINIMAL_TABLE_FACTOR = 1;
    private static final int MAXIMAL_TABLE_FACTOR = 32;

    protected Markup(int tableSize) {
        if (tableSize < MINIMAL_TABLE_FACTOR) {
            throw new IllegalArgumentException("The given number is less than 1.");
        }

        if (tableSize > MAXIMAL_TABLE_FACTOR) {
            throw new IllegalArgumentException("The given number is greater than 32.");
        }

        this.tableSize = tableSize;
    }

    public int getSidebarWidth() {
        return getNumberWidth(tableSize);
    }

    public int getCellWidth() {
        return getNumberWidth(tableSize * tableSize);
    }

    public int getTableSize() {
        return tableSize;
    }

    public int getIndentWidth(int number, int cellWidth) {
        int numberWidth = getNumberWidth(number);

        if (cellWidth - numberWidth < 0) {
            throw new IllegalArgumentException("The given cell's width is less than the number width.");
        }

        return cellWidth - numberWidth;
    }

    public abstract String getVerticalDivider();

    public abstract String getHorizontalDivider();

    public abstract String getPrefix(int width);

    private int getNumberWidth(int number) {
        int temp = Math.abs(number);
        int res = (number >= 0) ? 1 : 2;

        while (temp / 10 > 0) {
            res++;
            temp /= 10;
        }

        return res;
    }
}
