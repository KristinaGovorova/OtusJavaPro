package otus.java.pro.DBInteractions.dbconnection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import otus.java.pro.ReflectionApi.TestRunner;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DataSource {
    private static final Logger logger = LogManager.getLogger(DataSource.class.getName());
    private String url;
    private Connection connection;
    private Statement statement;

    public Connection getConnection() {
        return connection;
    }

    public Statement getStatement() {
        return statement;
    }

    public DataSource(String url) {
        this.url = url;
    }

    public void connect() throws SQLException {
        connection = DriverManager.getConnection(url);
        statement = connection.createStatement();
        logger.info("Установлено соединение с БД: " + url);
    }

    public void close() {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
                logger.error("Error happened, ", e);
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
                logger.error("Error happened, ", e);
            }
        }
        logger.info("От БД отключились");
    }
}
