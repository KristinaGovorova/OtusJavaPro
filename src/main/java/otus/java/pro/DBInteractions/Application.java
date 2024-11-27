package otus.java.pro.DBInteractions;

import otus.java.pro.DBInteractions.dbconnection.DataSource;
import otus.java.pro.DBInteractions.migration.DbMigrator;

import java.sql.*;
import java.util.List;

public class Application {
    private static Connection connection;
    private static Statement statement;

    public static void main(String[] args) {
        String url = "jdbc:h2:file:./db;MODE=PostgreSQL";
        DataSource dataSource = new DataSource(url);

        try {
            dataSource.connect();
            DbMigrator dbMigrator = new DbMigrator(dataSource);
            dbMigrator.migrate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dataSource.close();
        }

        try {
            dataSource.connect();
            AbstractRepository<User> userRepository = new AbstractRepository<>(dataSource, User.class);

            for (int i = 0; i < 5; i++) {
                User user = new User(null, "login", "password", "nickname" + i);

                userRepository.save(user);
                System.out.println("Создан пользователь: " + user);
            }

            List<User> users = userRepository.findAll();
            System.out.println("\nПользователи в БД:\n" + users + "\n");

            for (User user : users) {
                user.setNickname("Обновлено");
                userRepository.update(user);

                Long id = user.getId();

                System.out.println("Обновленный пользователь: " + userRepository.findById(id).orElseThrow());
            }

            users = userRepository.findAll();
            System.out.println("\nПользователи в БД:\n" + users + "\n");

            for (User user : users) {
                Long id = user.getId();

                userRepository.deleteById(id);

                System.out.println("Удлаленный пользователь: " + user);
            }

            users = userRepository.findAll();
            System.out.println("\nПользователи в БД:\n" + users + "\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            dataSource.close();
        }
    }
}
