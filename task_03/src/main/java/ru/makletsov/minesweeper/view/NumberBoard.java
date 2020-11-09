package ru.makletsov.minesweeper.view;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.util.List;
import java.util.stream.IntStream;

public class NumberBoard extends JPanel {
    private static final int MIN_VALUE = -99;
    private static final int MAX_VALUE = 999;
    private static final int BORDER_TYPE = 1;

    private final List<Image> images;

    private int value;
    private int[] digits;

    public NumberBoard(int initialValue, int height, int width, List<Image> images) {
        if (initialValue < MIN_VALUE || initialValue > MAX_VALUE) {
            throw new IllegalArgumentException("Value = " + initialValue +
                    ". Value must be between" + MIN_VALUE + " and " + MAX_VALUE + ".");
        }

        if (height < 0) {
            throw new IllegalArgumentException("Height must be positive.");
        }

        if (width < 0) {
            throw new IllegalArgumentException("Width must be positive.");
        }

        value = initialValue;
        digits = getDigits(initialValue);

        this.images = images;

        setPreferredSize(new Dimension(width, height));
        setBorder(new BevelBorder(BORDER_TYPE, Color.white, Color.darkGray));
    }

    public void increaseValue() {
        if (value != MAX_VALUE) {
            value++;
            digits = getDigits(value);

            repaint();
        }
    }

    public void decreaseValue() {
        if (value != MIN_VALUE) {
            value--;
            digits = getDigits(value);

            repaint();
        }
    }

    public void setValue(long value) {
        if (value < MIN_VALUE || value > MAX_VALUE) {
            throw new IllegalArgumentException("Value = " + value +
                    ". Value must be between" + MIN_VALUE + " and " + MAX_VALUE + ".");
        }

        this.value = (int) value;
        digits = getDigits(this.value);
        repaint();
    }

    public boolean canDecrease() {
        return value > MIN_VALUE;
    }

    @Override
    public void paintComponent(Graphics graphics) {
        Rectangle rectangle = AbstractBorder.getInteriorRectangle(this, getBorder(), 0, 0, getWidth(), getHeight());

        graphics.setColor(Color.BLACK);
        graphics.fillRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);

        int gap = rectangle.height / 23 * 2;
        int digitWidth = (rectangle.width - 4 * gap) / 3;
        int leftBoundX = rectangle.x + gap;
        int upperBoundY = rectangle.y + gap;
        int lowerBoundY = rectangle.height - gap * 2;

        IntStream.range(0, digits.length)
                .forEach(i -> graphics.drawImage(images.get(digits[i]),
                        leftBoundX + i * (gap + digitWidth),
                        upperBoundY,
                        digitWidth,
                        lowerBoundY,
                        null)
                );
    }

    private int[] getDigits(int number) {
        int[] digits = new int[3];
        int temp = number;
        int counter = 2;
        int minIndex = (number >= 0) ? 0 : 1;

        while (counter >= minIndex) {
            digits[counter] = Math.abs(temp % 10);
            temp = temp / 10;
            counter--;
        }

        if (number < 0) {
            //cause index of "-" picture is 10
            digits[0] = 10;
        }

        return digits;
    }
}
