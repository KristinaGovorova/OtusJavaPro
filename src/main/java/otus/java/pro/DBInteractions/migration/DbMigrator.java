package otus.java.pro.DBInteractions.migration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import otus.java.pro.DBInteractions.dbconnection.DataSource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.List;

public class DbMigrator {
    private static final Logger logger = LogManager.getLogger(DbMigrator.class.getName());
    private DataSource dataSource;


    public DbMigrator(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void migrate() {
        try {
            Connection connection = dataSource.getConnection();
            createMigrationHistoryTable(connection);
            List<File> migrations = getMigrationScripts();
            for (File migration : migrations) {
                String migrationName = migration.getName();
                if (!isScriptExecuted(connection, migrationName)) {
                    executeMigrationScripts(connection, migration);
                    logger.info("Migration {} applied", migrationName);
                } else {
                    logger.info("Migration {} skipped. Already applied!", migration);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

    public void createMigrationHistoryTable(Connection connection) throws SQLException {
        String createMigrationTable = "CREATE TABLE IF NOT EXISTS migration_history (" +
                "    id SERIAL PRIMARY KEY," +
                "    script_name VARCHAR(255) UNIQUE," +
                "    execution_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
        try (PreparedStatement ps = connection.prepareStatement(createMigrationTable)) {
            ps.executeUpdate();
        }
    }

    public void executeMigrationScripts(Connection connection, File migrationScript) throws SQLException {
      try (PreparedStatement ps = connection.prepareStatement(readMigrationScript(migrationScript))) {
          ps.executeUpdate();
      } catch (IOException e) {
          logger.error("Migration failed!");
          throw new SQLException("Migration failed!", e);
      }
      recordMigration(connection, migrationScript.getName());
    }

    private boolean isScriptExecuted(Connection connection, String scriptName) throws SQLException {
        String query = "SELECT COUNT(*) FROM migration_history WHERE script_name = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, scriptName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    private void recordMigration(Connection connection, String scriptName) throws SQLException {
        String insertSQL = "INSERT INTO migration_history (script_name) VALUES (?)";
        try (PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {
            pstmt.setString(1, scriptName);
            pstmt.executeUpdate();
        }
    }

    private static List<File> getMigrationScripts() {
        File folder = new File("src/main/java/otus/java/pro/DBInteractions/migration_scripts");
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".sql"));
        if (files == null) {
            return List.of();
        }
        return Arrays.stream(files).toList();
    }

    private static String readMigrationScript(File migrationScript) throws IOException {
        return Files.readString(migrationScript.toPath());
    }
}
