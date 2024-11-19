package otus.java.pro.Singleton;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DataSource {
    private Properties properties;
    private static final String CONFIG_PATH = "config.properties";
    public static final DataSource INSTANCE = new DataSource();

    public static DataSource getInstance() {
        return INSTANCE;
    }

    public Connection getConnection() throws SQLException, IOException {
        getProperties();
        return DriverManager.getConnection(properties.getProperty("database_url"),
                properties.getProperty("database_login"),
                properties.getProperty("database_password"));
    }

    private void getProperties() throws IOException {
        properties = new Properties();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream stream = loader.getResourceAsStream(CONFIG_PATH);
        properties.load(stream);
    }
}
