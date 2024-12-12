package ru.tele2.govorova.otus.java.pro.Singleton;

public class DBStatements {
    public static final String CREATE_ITEM = "INSERT INTO items (title, price) VALUES (?, ?)";
    public static final String GET_ALL_ITEMS = "SELECT id, title, price from items";
    public static final String UPDATE_PRICE = "UPDATE items SET price = ? WHERE id = ?";
}
