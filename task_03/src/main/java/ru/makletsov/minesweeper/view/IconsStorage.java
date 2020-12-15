package ru.makletsov.minesweeper.view;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class IconsStorage {
    private final Image windowIconImage;
    private final Map<RestartButton.State, Icon> restartButtonIcons;
    private final Map<CellButton.State, Icon> cellStateIcons;
    private final Map<Integer, Icon> minesCountIcons;
    private final List<Image> numbersImages;

    public IconsStorage(ImageLoader imagesLoader) {
        this.windowIconImage = imagesLoader.loadWindowIconImage();
        this.restartButtonIcons = getRestartButtonIcons(imagesLoader.loadRestartButtonImages());
        this.cellStateIcons = getCellIcons(imagesLoader.loadCellStateImages());
        this.minesCountIcons = getCellIcons(imagesLoader.loadMinesCountImages());
        this.numbersImages = imagesLoader.loadNumbersImages();
    }

    public Image getWindowIconImage() {
        return windowIconImage;
    }

    public Icon getCellButtonIcon(int minesCount) {
        return minesCountIcons.get(minesCount);
    }

    public Icon getCellButtonIcon(CellButton.State state) {
        return cellStateIcons.get(state);
    }

    public Icon getRestartButtonIcon(RestartButton.State state) {
        return restartButtonIcons.get(state);
    }

    public Image getNumberImage(int number) {
        return numbersImages.get(number);
    }

    private Map<RestartButton.State, Icon> getRestartButtonIcons(Map<RestartButton.State, Image> imagesMap) {
        return imagesMap.entrySet().stream().collect(
            Collectors.toMap(Map.Entry::getKey, this::getRestartButtonIcon)
        );
    }

    private Icon getRestartButtonIcon(Map.Entry<RestartButton.State, Image> entry) {
        int size = View.MARKUP_PITCH * View.CONTROL_PANEL_ELEMENTS_FACTOR;

        return new ImageIcon(entry.getValue().getScaledInstance(size, size, Image.SCALE_SMOOTH));
    }

    private <T> Map<T, Icon> getCellIcons(Map<T, Image> imagesMap) {
        return imagesMap.entrySet().stream().collect(
            Collectors.toMap(Map.Entry::getKey, this::getCellButtonIcon)
        );
    }

    private Icon getCellButtonIcon(Map.Entry<?, Image> entry) {
        int size = View.MARKUP_PITCH * View.PLAYGROUND_ELEMENTS_FACTOR;

        return new ImageIcon(entry.getValue().getScaledInstance(size, size, Image.SCALE_SMOOTH));
    }
}
