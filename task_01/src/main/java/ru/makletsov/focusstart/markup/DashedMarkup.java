package ru.makletsov.focusstart.markup;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class DashedMarkup extends Markup {
    private static final String VERTICAL_DIVIDER_UNIT = "|";
    private static final String HORIZONTAL_DIVIDER_UNIT = "-";
    private static final String CROSS_UNIT = "+";
    private static final String BLANK_PLACE_UNIT = " ";

    private final String rowsDivider;
    private final Map<Integer, String> prefixes;

    public DashedMarkup(int tableSize) {
        super(tableSize);

        String regCell = getRegularCellBottomBorder(getCellWidth());

        rowsDivider = getRowsDivider(getSidebarWidth(), getTableSize(), regCell);
        prefixes = getPrefixes(getCellWidth());
    }

    private String getRegularCellBottomBorder(int width) {
        builder.setLength(0);

        builder.append(CROSS_UNIT)
                .append(HORIZONTAL_DIVIDER_UNIT.repeat(width));

        return builder.toString();
    }

    private String getRowsDivider(int sidebarWidth, int columnsCount, String regularCellBottomBorder) {
        builder.setLength(0);

        builder.append(HORIZONTAL_DIVIDER_UNIT.repeat(sidebarWidth))
                .append(regularCellBottomBorder.repeat(columnsCount));

        return builder.toString();
    }

    private Map<Integer, String> getPrefixes(int regCellWidth) {
        Map<Integer, String> result = new HashMap<>();

        builder.setLength(0);

        IntStream.range(0, regCellWidth + 1)
                .forEach(i -> {
                    result.put(i, builder.toString());
                    builder.append(BLANK_PLACE_UNIT);
                });

        return result;
    }

    @Override
    protected String getVerticalDivider() {
        return VERTICAL_DIVIDER_UNIT;
    }

    @Override
    protected String getHorizontalDivider() {
        return rowsDivider;
    }

    @Override
    protected String getPrefix(int width) {
        return prefixes.get(width);
    }
}
