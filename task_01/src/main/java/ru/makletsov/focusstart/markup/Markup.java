package ru.makletsov.focusstart.markup;

import java.util.stream.IntStream;

public abstract class Markup {
    private static final int HEADERS_MULTIPLIER = 1;

    private final int tableSize;
    private final int sidebarWidth;
    private final int cellWidth;

    protected final StringBuilder builder = new StringBuilder();

    protected Markup(int tableSize) {
        this.tableSize = tableSize;
        this.sidebarWidth = String.valueOf(tableSize).length();
        this.cellWidth = String.valueOf(tableSize * tableSize).length();
    }

    public int getSidebarWidth() {
        return sidebarWidth;
    }

    public int getCellWidth() {
        return cellWidth;
    }

    public int getTableSize() {
        return tableSize;
    }

    public String buildTable(int size) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(getHeader(size))
                .append(System.lineSeparator());

        IntStream.range(1, size + 1)
                .forEach(multiplier ->
                        stringBuilder.append(getRow(multiplier, size))
                                .append(System.lineSeparator()));

        return stringBuilder.toString();
    }

    public String getHeader(int columnsCount) {
        return getRow(HEADERS_MULTIPLIER, columnsCount, true);
    }

    public String getRow(int rowNumber, int columnsCount) {
        return getRow(rowNumber, columnsCount, false);
    }

    private String getRow(int rowNumber, int cellsCount, boolean isHeader) {
        builder.setLength(0);

        addSidebarCell(rowNumber, isHeader);
        addRegularCells(rowNumber, cellsCount);

        builder.append(System.lineSeparator())
                .append(getHorizontalDivider());

        return builder.toString();
    }

    private void addSidebarCell(int rowNumber, boolean isHeader) {
        int numberWidth = String.valueOf(rowNumber).length();

        int sidebarPrefixWidth = sidebarWidth - numberWidth;
        int sidebarWidth = getSidebarWidth();

        if (isHeader) {
            builder.append(getPrefix(sidebarWidth));
        } else {
            builder.append(getPrefix(sidebarPrefixWidth))
                    .append(rowNumber);
        }
    }

    private void addRegularCells(int rowNumber, int cellsCount) {
        IntStream.range(1, cellsCount + 1)
                .map(v -> v * rowNumber)
                .forEach(v -> {
                    String numberStringValue = String.valueOf(v);

                    int numberWidth = numberStringValue.length();
                    int indent = cellWidth - numberWidth;

                    builder.append(getVerticalDivider())
                            .append(getPrefix(indent))
                            .append(numberStringValue);
                });
    }

    public abstract String getVerticalDivider();

    public abstract String getHorizontalDivider();

    public abstract String getPrefix(int width);
}
