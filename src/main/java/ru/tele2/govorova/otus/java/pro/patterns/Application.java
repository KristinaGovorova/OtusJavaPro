package ru.tele2.govorova.otus.java.pro.patterns;

import java.util.Iterator;

public class Application {
    public static void main(String[] args) {
        Box box = new Box();

        System.out.println("Small first iterator:");
        Iterator<String> smallFirstIterator = box.getSmallFirstIterator();
        while (smallFirstIterator.hasNext()) {
            System.out.println(smallFirstIterator.next());
        }

        System.out.println("\nColor first iterator:");
        Iterator<String> colorFirstIterator = box.getColorFirstIterator();
        while (colorFirstIterator.hasNext()) {
            System.out.println(colorFirstIterator.next());
        }
    }
}
