package ru.tele2.govorova.otus.java.pro.Singleton;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static ru.tele2.govorova.otus.java.pro.Singleton.DBStatements.*;

public class ItemsDao {

    private final DataSource dataSource = DataSource.getInstance();

    public void createItem(Item item) throws SQLException, IOException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(CREATE_ITEM, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, item.getTitle());
            statement.setDouble(2, item.getPrice());
            statement.executeUpdate();

            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    item.setId(resultSet.getInt(1));
                }
            }
        }
    }

    public List<Item> getAllItems() throws SQLException, IOException {
        List<Item> items = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_ALL_ITEMS);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Item item = new Item();
                item.setId(resultSet.getInt("id"));
                item.setTitle(resultSet.getString("title"));
                item.setPrice(resultSet.getDouble("price"));
                items.add(item);
            }
        }
        return items;
    }

    public void makeDoublePrice(Item item) throws SQLException, IOException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_PRICE)) {
            statement.setDouble(1, item.getPrice());
            statement.setInt(2, item.getId());
            statement.executeUpdate();
        }
    }
}
