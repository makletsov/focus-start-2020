package ru.makletsov.focusstart.ex2;

public class Resource {
    private final long id;

    public Resource(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String printInfo() {
        return "Resource #" + id;
    }
}
