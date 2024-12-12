package ru.tele2.govorova.otus.java.pro.singleton;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class ItemsSertviceProxy {
    private static final Logger logger = LogManager.getLogger(ItemsSertviceProxy.class.getName());

    private final ItemsService itemsService = new ItemsService();
    private final DataSource dataSource = new DataSource();

    public void createItems() throws IOException {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            try {
                logger.info("Creating 1000 items in DB");
                itemsService.create1000Items();
                connection.commit();
                logger.info("Creating completed!");
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException("Operation failed. Transaction rolled back. ", e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error. ", e);
        }
    }

    public void doublePrices() throws IOException {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            try {
                logger.info("Doubling items prices.");
                itemsService.doublePrices();
                connection.commit();
                logger.info("Doubling completed!");
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException("Operation failed. Transaction rolled back. ", e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error", e);
        }
    }
}
