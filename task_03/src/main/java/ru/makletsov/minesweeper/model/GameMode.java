package ru.makletsov.minesweeper.model;

public enum GameMode {
    BEGINNER {
        @Override
        public int getHeight() {
            return 9;
        }

        @Override
        public int getWidth() {
            return 9;
        }

        @Override
        public int getMinesCount() {
            return 10;
        }

        @Override
        public String getPrettyString() {
            return "Beginner";
        }
    },

    AMATEUR {
        @Override
        public int getHeight() {
            return 16;
        }

        @Override
        public int getWidth() {
            return 16;
        }

        @Override
        public int getMinesCount() {
            return 40;
        }

        @Override
        public String getPrettyString() {
            return "Amateur";
        }
    },

    PROFESSIONAL {
        @Override
        public int getHeight() {
            return 16;
        }

        @Override
        public int getWidth() {
            return 30;
        }

        @Override
        public int getMinesCount() {
            return 99;
        }

        @Override
        public String getPrettyString() {
            return "Professional";
        }
    };

    public abstract int getHeight();

    public abstract int getWidth();

    public abstract int getMinesCount();

    public abstract String getPrettyString();
}
