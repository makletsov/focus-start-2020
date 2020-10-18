package ru.makletsov.focusstart.markup;

import java.util.stream.IntStream;

public abstract class Markup {
    private final int tableSize;
    protected final StringBuilder builder = new StringBuilder();

    private static final int MINIMAL_TABLE_FACTOR = 1;
    private static final int MAXIMAL_TABLE_FACTOR = 32;
    private static final int HEADERS_MULTIPLIER = 1;

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

    public String getHeader(int columnsCount) {
        return getRow(columnsCount, HEADERS_MULTIPLIER, true);
    }

    public String getRow(int columnsCount, int rowNumber) {
        return getRow(columnsCount, rowNumber, false);
    }

    public abstract String getVerticalDivider();

    public abstract String getHorizontalDivider();

    public abstract String getPrefix(int width);

    private String getRow(int cellsCount, int rowNumber, boolean isHeader) {
        builder.setLength(0);

        addSidebarCell(rowNumber, isHeader);
        addRegularCells(cellsCount, rowNumber);

        builder.append(System.lineSeparator())
                .append(getHorizontalDivider());

        return builder.toString();
    }

    private void addSidebarCell(int rowNumber, boolean isHeader) {
        int sidebarPrefixWidth = getIndentWidth(rowNumber, getSidebarWidth());
        int sidebarWidth = getSidebarWidth();

        if (isHeader) {
            builder.append(getPrefix(sidebarWidth));
        } else {
            builder.append(getPrefix(sidebarPrefixWidth))
                    .append(rowNumber);
        }
    }

    private void addRegularCells(int cellsCount, int rowNumber) {
        IntStream.range(1, cellsCount + 1)
                .map(v -> v * rowNumber)
                .forEach(v -> {
                    int indent = getIndentWidth(v, getCellWidth());

                    builder.append(getVerticalDivider())
                            .append(getPrefix(indent))
                            .append(v);
                });
    }

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
