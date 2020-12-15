package ru.makletsov.minesweeper.view;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.util.Arrays;
import java.util.stream.IntStream;

public class NumberBoard extends JPanel {
    private static final int BORDER_TYPE = 1;

    private final IconsStorage images;
    private int[] digits;

    public NumberBoard(int initialValue, int height, int width, IconsStorage images) {
        if (height < 0) {
            throw new IllegalArgumentException("Height must be positive.");
        }

        if (width < 0) {
            throw new IllegalArgumentException("Width must be positive.");
        }

        digits = getDigits(initialValue);

        this.images = images;

        setPreferredSize(new Dimension(width, height));
        setBorder(new BevelBorder(BORDER_TYPE, Color.white, Color.darkGray));
    }

    public void setValue(int value) {
        digits = getDigits(value);
        repaint();

        System.out.println(Arrays.toString(digits));
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
                .forEach(i -> graphics.drawImage(images.getNumberImage(digits[i]),
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
//        int minIndex = (number >= 0) ? 0 : 1;

        while (counter >= 0) {
            digits[counter] = Math.abs(temp % 10);
            temp = temp / 10;
            counter--;
        }

//        if (number < 0) {
//            //cause index of "-" picture is 10
//            digits[0] = 10;
//        }

        return digits;
    }
}
