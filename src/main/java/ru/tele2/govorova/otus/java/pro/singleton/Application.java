package ru.tele2.govorova.otus.java.pro.singleton;


import java.io.IOException;

public class Application {
    public static void main(String[] args) throws IOException {
        ItemsSertviceProxy itemsSertviceProxy = new ItemsSertviceProxy();

        itemsSertviceProxy.createItems();
        itemsSertviceProxy.doublePrices();
    }
}
