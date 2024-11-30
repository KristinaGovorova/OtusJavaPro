package otus.java.pro.DBInteractions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import otus.java.pro.DBInteractions.annotation.RepositoryField;
import otus.java.pro.DBInteractions.annotation.RepositoryIdField;
import otus.java.pro.DBInteractions.annotation.RepositoryTable;
import otus.java.pro.DBInteractions.dbconnection.DataSource;
import otus.java.pro.DBInteractions.exception.ORMException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AbstractRepository<T> {
    private static final Logger logger = LogManager.getLogger(AbstractRepository.class.getName());
    private DataSource dataSource;
    private final Class<T> cls;
    private PreparedStatement psInsert;
    private PreparedStatement psUpdate;
    private PreparedStatement psDeleteById;
    private PreparedStatement psFindAll;
    private PreparedStatement psFindById;
    private List<Field> cachedFields;
    private final List<Method> fieldsGetters = new ArrayList<>();
    private final List<Method> fieldsSetters = new ArrayList<>();
    private Field idField;
    private Method idGetter;
    private Method idSetter;

    public AbstractRepository(DataSource dataSource, Class<T> cls) {
        this.dataSource = dataSource;
        this.cls = cls;
        this.prepareStatements();
    }

    public void save(T entity) {
        try {
            for (int i = 0; i < fieldsGetters.size(); i++) {
                Method getter = fieldsGetters.get(i);
                Object value = getter.invoke(entity);
                psInsert.setObject(i + 1, value);
            }
            psInsert.executeUpdate();
        } catch (SQLException | IllegalAccessException | InvocationTargetException e) {
            throw new ORMException("Не удалось сохранить объект: " + entity, e);
        }
    }

    private void prepareInsert(Class cls) {
        if (!cls.isAnnotationPresent(RepositoryTable.class)) {
            throw new ORMException("Класс не предназначен для создания репозитория, не хватает аннотации @RepositoryTable");
        }
        String tableName = ((RepositoryTable) cls.getAnnotation(RepositoryTable.class)).value();
        StringBuilder query = new StringBuilder("insert into ");
        query.append(tableName).append(" (");
        // 'insert into users ('
        cachedFields = Arrays.stream(cls.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(RepositoryField.class))
                .filter(f -> !f.isAnnotationPresent(RepositoryIdField.class))
                .collect(Collectors.toList());
        for (Field f : cachedFields) { // TODO заменить на использование геттеров
            f.setAccessible(true);
        }
        for (Field f : cachedFields) {
            query.append(f.getName()).append(", ");
        }
        // 'insert into users (login, password, nickname, '
        query.setLength(query.length() - 2);
        query.append(") values (");
        // 'insert into users (login, password, nickname) values ('
        for (Field f : cachedFields) {
            query.append("?, ");
        }
        query.setLength(query.length() - 2);
        query.append(");");
        // 'insert into users (login, password, nickname) values (?, ?, ?);'
        try {
            psInsert = dataSource.getConnection().prepareStatement(query.toString());
        } catch (SQLException e) {
            throw new ORMException("Не удалось проинициализировать репозиторий для класса " + cls.getName());
        }
    }
    
    public void update(T entity) {
        try {
            for (int i = 0; i < fieldsGetters.size(); i++) {
                Method getter = fieldsGetters.get(i);
                Object value = getter.invoke(entity);
                psUpdate.setObject(i + 1, value);
            }
            psUpdate.setObject(fieldsGetters.size() + 1, idGetter.invoke(entity));
            psUpdate.executeUpdate();
        } catch (SQLException | IllegalAccessException | InvocationTargetException e) {
            throw new ORMException("Не удалось обновить объект: " + entity, e);
        }
    }

    public Optional<T> findById(Long id) {
        try {
            psFindById.setLong(1, id);
            try (ResultSet rs = psFindById.executeQuery()) {
                if (rs.next()) {
                    T entity = mapResultSetToEntity(rs);
                    return Optional.of(entity);
                }
            }
        } catch (SQLException e) {
            throw new ORMException("Не удалось найти объект с id = " + id, e);
        }
        return Optional.empty();
    }

    public List<T> findAll() {
        try (ResultSet rs = psFindAll.executeQuery()) {
            return mapResultSetToList(rs);
        } catch (SQLException e) {
            throw new ORMException("Не удалось найти все записи", e);
        }
    }

    public void deleteById(Long id) {
        try {
            psDeleteById.setLong(1, id);
            psDeleteById.executeUpdate();
        } catch (SQLException e) {
            throw new ORMException("Не удалось удалить объект с id = " + id, e);
        }
    }
    
        private void prepareStatements() {
        if (!cls.isAnnotationPresent(RepositoryTable.class)) {
            throw new ORMException("Class " + cls.getName() + " not annotated with @RepositoryTable");
        }

        String tableName = getTableName();
        if (tableName.isBlank()) {
            tableName = cls.getSimpleName();
        }

        cachedFields = Arrays.stream(cls.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(RepositoryField.class))
                .collect(Collectors.toList());

        if (cachedFields.isEmpty()) {
            throw new ORMException("No fields mapped with annotation @RepositoryField in class: "
                    + cls.getName());
        }

        for (Field field : cachedFields) {
            String fieldName = field.getName();
            String getterMethodName = "get" + capitalizeFirstLetter(fieldName);
            String setterMethodName = "set" + capitalizeFirstLetter(fieldName);
            try {
                Method getterMethod = cls.getMethod(getterMethodName);
                fieldsGetters.add(getterMethod);
                Method setterMethod = cls.getMethod(setterMethodName, field.getType());
                fieldsSetters.add(setterMethod);
            } catch (NoSuchMethodException e) {
                throw new ORMException("No getter or setter method found for field: " + fieldName, e);
            }
        }

        idField = Arrays.stream(cls.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(RepositoryIdField.class))
                .findFirst()
                .orElseThrow(() -> new ORMException("Class " + cls.getName() + " doesn't have id"));

        try {
            String idFieldName = idField.getName();
            idGetter = cls.getMethod("get" + capitalizeFirstLetter(idFieldName));
            idSetter = cls.getMethod("set" + capitalizeFirstLetter(idFieldName), idField.getType());
        } catch (NoSuchMethodException e) {
            throw new ORMException("No getter or setter method found for id field: " + idField.getName(), e);
        }

        try {
            Connection connection = dataSource.getConnection();

            String insertQuery = generateInsertQuery(tableName);
            psInsert = connection.prepareStatement(insertQuery);

            String updateQuery = generateUpdateQuery(tableName);
            psUpdate = connection.prepareStatement(updateQuery);

            String findByIdQuery = generateFindByIdQuery(tableName);
            psFindById = connection.prepareStatement(findByIdQuery);

            String findAllQuery = generateFindAllQuery(tableName);
            psFindAll = connection.prepareStatement(findAllQuery);

            String deleteByIdQuery = generateDeleteByIdQuery(tableName);
            psDeleteById = connection.prepareStatement(deleteByIdQuery);
        } catch (SQLException e) {
            throw new ORMException("Error preparing SQL-queries for class " + cls.getName(), e);
        }
    }

    private String getTableName() {
        RepositoryTable annotation = cls.getAnnotation(RepositoryTable.class);
        return annotation.value();
    }

    private String generateInsertQuery(String tableName) {
        StringBuilder query = new StringBuilder("INSERT INTO " + tableName + " (");
        cachedFields.forEach(field -> query.append(getColumnName(field)).append(", "));
        query.setLength(query.length() - 2);
        query.append(") VALUES (");
        cachedFields.forEach(field -> query.append("?, "));
        query.setLength(query.length() - 2);
        query.append(")");
        return query.toString();
    }

    private String generateUpdateQuery(String tableName) {
        StringBuilder query = new StringBuilder("UPDATE " + tableName + " SET ");
        cachedFields.forEach(field -> query.append(getColumnName(field)).append(" = ?, "));
        query.setLength(query.length() - 2);
        query.append(" WHERE ").append(getIdName()).append(" = ?");
        return query.toString();
    }

    private String generateFindByIdQuery(String tableName) {
        return "SELECT * FROM " + tableName + " WHERE id = ?";
    }

    private String generateFindAllQuery(String tableName) {
        return "SELECT * FROM " + tableName;
    }

    private String generateDeleteByIdQuery(String tableName) {
        return "DELETE FROM " + tableName + " WHERE id = ?";
    }

    private String getColumnName(Field field) {
        String name = field.getAnnotation(RepositoryField.class).value();

        return name.isBlank() ? field.getName() : name;
    }

    private String getIdName() {
        String name = idField.getAnnotation(RepositoryIdField.class).value();

        return name.isBlank() ? idField.getName() : name;
    }

    private String capitalizeFirstLetter(String str) {
        if (str == null || str.isBlank()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private T mapResultSetToEntity(ResultSet rs) throws SQLException {
        try {
            T entity = cls.getDeclaredConstructor().newInstance();
            for (int i = 0; i < cachedFields.size(); i++) {
                String columnName = getColumnName(cachedFields.get(i));
                Object value = rs.getObject(columnName);
                fieldsSetters.get(i).invoke(entity, value);
            }
            idSetter.invoke(entity, rs.getObject(getIdName()));
            return entity;
        } catch (Exception e) {
            throw new SQLException("Ошибка при приобразовании результата: " + rs, e);
        }
    }

    private List<T> mapResultSetToList(ResultSet rs) throws SQLException {
        List<T> entities = new ArrayList<>();
        while (rs.next()) {
            T entity = mapResultSetToEntity(rs);
            entities.add(entity);
        }
        return entities;
    }
}
