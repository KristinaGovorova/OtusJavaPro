package ru.tele2.govorova.otus.java.pro.patterns;

import java.util.ArrayList;
import java.util.List;

public final class Matryoshka {
    // [0] - the smallest / [9] - the biggest
    private final List<String> items;

    public Matryoshka(String color) {
        items = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            items.add(color + i);
        }
    }

    public List<String> getItems() {
        return items;
    }
}
