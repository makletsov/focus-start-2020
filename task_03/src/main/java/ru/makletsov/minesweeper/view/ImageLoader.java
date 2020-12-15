package ru.makletsov.minesweeper.view;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ImageLoader {
    private static final String RESTART_BUTTON_IMAGES_PATH_TEMPLATE = "/images/face_button/%s.png";
    private static final String CELL_BUTTON_IMAGES_PATH_TEMPLATE = "/images/cell_button/%s.png";
    private static final String NUMBER_PANEL_IMAGES_PATH_TEMPLATE = "/images/numbers/d%s.png";

    private static final String EXCEPTION_TEMPLATE = "File not found: ";

    private static final int MINES_COUNT_IMAGES_COUNT = 9;
    private static final int NUMBERS_IMAGES_COUNT = 11;
    private static final String MAIN_IMAGE_PATH_TEMPLATE = "/images/%s.png";
    private static final String WINDOW_ICON_IMAGE_NAME = "icon";

    public Map<RestartButton.State, Image> loadRestartButtonImages() {
        EnumSet<RestartButton.State> set = EnumSet.allOf(RestartButton.State.class);

        return set.stream().collect(Collectors.toMap(
            state -> state,
            state -> loadImage(RESTART_BUTTON_IMAGES_PATH_TEMPLATE, state.getIconName())
        ));
    }

    public Map<CellButton.State, Image> loadCellStateImages() {
        EnumSet<CellButton.State> set = EnumSet.allOf(CellButton.State.class);

        return set.stream().collect(Collectors.toMap(
            state -> state,
            state -> loadImage(CELL_BUTTON_IMAGES_PATH_TEMPLATE, state.getIconName())
        ));
    }

    public Map<Integer, Image> loadMinesCountImages() {
        return IntStream.range(0, MINES_COUNT_IMAGES_COUNT)
            .boxed()
            .collect(Collectors.toMap(
                index -> index,
                index -> loadImage(CELL_BUTTON_IMAGES_PATH_TEMPLATE, String.valueOf(index))
            ));
    }

    public List<Image> loadNumbersImages() {
        return IntStream.range(0, NUMBERS_IMAGES_COUNT)
            .mapToObj(index -> loadImage(
                NUMBER_PANEL_IMAGES_PATH_TEMPLATE,
                String.valueOf(index)
            ))
            .collect(Collectors.toList());
    }

    public Image loadWindowIconImage() {
        return loadImage(MAIN_IMAGE_PATH_TEMPLATE, WINDOW_ICON_IMAGE_NAME);
    }

    private Image loadImage(String pathTemplate, String suffix) {
        String path = String.format(pathTemplate, suffix);

        try {
            return ImageIO.read(ImageLoader.class.getResource(path));
        } catch (IOException | IllegalArgumentException e) {
            throw new IllegalArgumentException(EXCEPTION_TEMPLATE + path);
        }
    }
}
