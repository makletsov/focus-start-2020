package ru.makletsov.minesweeper.view;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DigitPanel {
    private static final int BORDER_TYPE = 1;
    private static final int HORIZONTAL_INSET = 1;

    private final IconsStorage iconsStorage;
    private final JPanel panel;

    private final List<JLabel> digitLabels;
    private final GridBagConstraints numberBoardConstraints;

    public DigitPanel(int initialValue, int height, int width, IconsStorage iconsStorage) {
        if (height < 0) {
            throw new IllegalArgumentException("Height must be positive.");
        }

        if (width < 0) {
            throw new IllegalArgumentException("Width must be positive.");
        }

        checkNumberValue(initialValue);

        this.iconsStorage = iconsStorage;

        panel = new JPanel(new GridBagLayout());

        numberBoardConstraints = new GridBagConstraints();
        numberBoardConstraints.insets = new Insets(0, HORIZONTAL_INSET, 0, HORIZONTAL_INSET);

        digitLabels = IntStream
            .rangeClosed(0, 2)
            .mapToObj(this::addLabelToPanel)
            .collect(Collectors.toList());

        setValue(initialValue);

        panel.setBackground(Color.BLACK);
        panel.setPreferredSize(new Dimension(width, height));
        panel.setBorder(new BevelBorder(BORDER_TYPE, Color.white, Color.darkGray));
    }

    private JLabel addLabelToPanel(int index) {
        JLabel label = new JLabel();

        numberBoardConstraints.gridx = index;
        panel.add(label, numberBoardConstraints);

        return label;
    }

    private void checkNumberValue(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("Value must be positive.");
        }

        if (value > 999) {
            throw new IllegalArgumentException("Value must be less than 999.");
        }
    }

    public JPanel getPanel() {
        return panel;
    }

    public void setValue(int value) {
        checkNumberValue(value);

        int[] valueByDigits = splitByDigits(value);

        IntStream.range(0, digitLabels.size())
            .forEach(index -> paintIconByIndex(valueByDigits, index));
    }

    private void paintIconByIndex(int[] digits, int index) {
        Icon iconByIndex = iconsStorage.getNumberIcon(digits[index]);
        digitLabels.get(index).setIcon(iconByIndex);
    }

    private int[] splitByDigits(int number) {
        int[] digits = new int[3];
        int temp = number;
        int counter = 2;

        while (counter >= 0) {
            digits[counter] = Math.abs(temp % 10);
            temp = temp / 10;
            counter--;
        }

        return digits;
    }
}
