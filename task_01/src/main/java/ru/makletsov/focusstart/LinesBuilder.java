package ru.makletsov.focusstart;

import ru.makletsov.focusstart.markup.Markup;

import java.util.stream.IntStream;

public class LinesBuilder {
    private final Markup markup;
    private final StringBuilder builder = new StringBuilder();

    private static final int HEADERS_MULTIPLIER = 1;

    LinesBuilder(Markup markup) {
        if (markup == null) {
            throw new NullPointerException("Given Markup is null.");
        }

        this.markup = markup;
    }

    String getHeader(int columnsCount) {
        return getRow(columnsCount, HEADERS_MULTIPLIER, true);
    }

    String getRow(int columnsCount, int rowNumber) {
        return getRow(columnsCount, rowNumber, false);
    }

    private String getRow(int cellsCount, int rowNumber, boolean isHeader) {
        builder.setLength(0);

        addSidebarCell(rowNumber, isHeader);
        addRegularCells(cellsCount, rowNumber);

        builder.append(System.lineSeparator())
                .append(markup.getHorizontalDivider());

        return builder.toString();
    }

    private void addSidebarCell(int rowNumber, boolean isHeader) {
        int sidebarPrefixWidth = markup.getIndentWidth(rowNumber, markup.getSidebarWidth());
        int sidebarWidth = markup.getSidebarWidth();

        if (isHeader) {
            builder.append(markup.getPrefix(sidebarWidth));
        } else {
            builder.append(markup.getPrefix(sidebarPrefixWidth))
                    .append(rowNumber);
        }
    }

    private void addRegularCells(int cellsCount, int rowNumber) {
        IntStream.range(1, cellsCount + 1)
                .map(v -> v * rowNumber)
                .forEach(v -> {
                    int indent = markup.getIndentWidth(v, markup.getCellWidth());

                    builder.append(markup.getVerticalDivider())
                            .append(markup.getPrefix(indent))
                            .append(v);
                });
    }
}
