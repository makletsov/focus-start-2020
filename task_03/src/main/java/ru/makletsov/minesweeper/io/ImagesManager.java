package ru.makletsov.minesweeper.io;

import ru.makletsov.minesweeper.view.CellButton;
import ru.makletsov.minesweeper.view.RestartButton;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ImagesManager {
    private static final String RESTART_BUTTON_IMAGES_PATH_TEMPLATE = "/images/face_button/%s.png";
    private static final String CELL_BUTTON_IMAGES_PATH_TEMPLATE = "/images/cell_button/%s.png";
    private static final String NUMBER_PANEL_IMAGES_PATH_TEMPLATE = "/images/numbers/d%s.png";

    private static final String EXCEPTION_TEMPLATE = "File not found: ";

    private static final int MINES_COUNT_IMAGES_COUNT = 9;
    private static final int NUMBERS_IMAGES_COUNT = 11;
    private static final String MAIN_IMAGE_PATH_TEMPLATE = "/images/%s.png";
    private static final String MAIN_IMAGE_NAME = "icon";

    public Map<RestartButton.State, Image> getRestartButtonImages() {
        EnumSet<RestartButton.State> set = EnumSet.allOf(RestartButton.State.class);

        return getImagesThroughEnumSet(RESTART_BUTTON_IMAGES_PATH_TEMPLATE, set);
    }

    public Map<CellButton.State, Image> getCellStateImages() {
        EnumSet<CellButton.State> set = EnumSet.allOf(CellButton.State.class);

        return getImagesThroughEnumSet(CELL_BUTTON_IMAGES_PATH_TEMPLATE, set);
    }

    private <T extends Enum<T>> Map<T, Image> getImagesThroughEnumSet(String pathTemplate, EnumSet<T> set) {
        return set.stream().collect(Collectors.toMap(
                state -> state,
                state -> getImage(pathTemplate, state.toString().toLowerCase())
        ));
    }

    public Map<Integer, Image> getMinesCountImages() {
        return IntStream.range(0, MINES_COUNT_IMAGES_COUNT)
                .boxed()
                .collect(Collectors.toMap(
                        index -> index,
                        index -> getImage(CELL_BUTTON_IMAGES_PATH_TEMPLATE, index.toString())
                ));
    }

    public List<Image> getNumbersImages() {
        return IntStream.range(0, NUMBERS_IMAGES_COUNT)
                .boxed()
                .map(index -> getImage(NUMBER_PANEL_IMAGES_PATH_TEMPLATE, index.toString()))
                .collect(Collectors.toList());
    }

    public Image getMainImage() {
        return getImage(MAIN_IMAGE_PATH_TEMPLATE, MAIN_IMAGE_NAME);
    }

    private Image getImage(String pathTemplate, String suffix) {
        String path = String.format(pathTemplate, suffix);

        try {
            return ImageIO.read(ImagesManager.class.getResource(path));
        } catch (IOException | IllegalArgumentException e) {
            throw new IllegalArgumentException(EXCEPTION_TEMPLATE + path);
        }
    }
}
