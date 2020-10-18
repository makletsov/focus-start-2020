package ru.makletsov.focusstart.markup;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class DashedMarkup extends Markup {
    private final String rowsDivider;
    private final Map<Integer, String> prefixes;

    private final static char VERTICAL_DIVIDER_UNIT = '|';
    private final static char HORIZONTAL_DIVIDER_UNIT = '-';
    private final static char CROSS_UNIT = '+';
    private final static char BLANK_PLACE_UNIT = ' ';

    public DashedMarkup(int tableSize) {
        super(tableSize);

        StringBuilder builder = new StringBuilder();

        String regCell = getRegularCellBottomBorder(getCellWidth(), builder);

        rowsDivider = getRowsDivider(getSidebarWidth(), getTableSize(), regCell, builder);
        prefixes = getPrefixes(getCellWidth(), builder);
    }

    @Override
    public String getVerticalDivider() {
        return String.valueOf(VERTICAL_DIVIDER_UNIT);
    }

    @Override
    public String getHorizontalDivider() {
        return rowsDivider;
    }

    @Override
    public String getPrefix(int width) {
        if (!prefixes.containsKey(width)) {
            throw new IllegalArgumentException("Wrong value of prefix length " + width);
        }

        return prefixes.get(width);
    }

    private String getRegularCellBottomBorder(int width, StringBuilder builder) {
        builder.setLength(0);

        builder.append(CROSS_UNIT);
        IntStream.range(0, width)
                .boxed()
                .map(i -> HORIZONTAL_DIVIDER_UNIT)
                .forEach(builder::append);

        return builder.toString();
    }

    private String getRowsDivider(int sidebarWidth, int columnsCount, String regularCellBottomBorder, StringBuilder builder) {
        builder.setLength(0);

        IntStream.range(0, sidebarWidth)
                .boxed()
                .map(i -> HORIZONTAL_DIVIDER_UNIT)
                .forEach(builder::append);
        IntStream.range(0, columnsCount)
                .boxed()
                .map(i -> regularCellBottomBorder)
                .forEach(builder::append);

        return builder.toString();
    }

    private Map<Integer, String> getPrefixes(int regCellWidth, StringBuilder builder) {
        Map<Integer, String> result = new HashMap<>();

        builder.setLength(0);

        IntStream.range(0, regCellWidth + 1)
                .forEach(i -> {
                    result.put(i, builder.toString());
                    builder.append(BLANK_PLACE_UNIT);
                });

        return result;
    }
}
