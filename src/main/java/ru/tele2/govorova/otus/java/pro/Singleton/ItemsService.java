package ru.tele2.govorova.otus.java.pro.Singleton;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ItemsService {
    private final ItemsDao itemsDao = new ItemsDao();

    public void create1000Items() throws SQLException, IOException {
        for (int i = 0; i < 100; i++) {
            Item item = new Item();
            item.setTitle("Item # " + i);
            item.setPrice(100);
            itemsDao.createItem(item);
        }
    }

    public void doublePrices() throws SQLException, IOException {
        List<Item> items = itemsDao.getAllItems();
        for (Item item : items) {
            item.setPrice(item.getPrice() * 2);
            itemsDao.makeDoublePrice(item);
        }
    }
}
