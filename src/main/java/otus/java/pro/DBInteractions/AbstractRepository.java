package otus.java.pro.dbinteractions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import otus.java.pro.dbinteractions.annotation.RepositoryField;
import otus.java.pro.dbinteractions.annotation.RepositoryIdField;
import otus.java.pro.dbinteractions.annotation.RepositoryTable;
import otus.java.pro.dbinteractions.dbconnection.DataSource;
import otus.java.pro.dbinteractions.exception.ORMException;

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
    private List<FieldInfo> cachedFields;
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
            int paramIndex = 1;
            for (FieldInfo fieldInfo : cachedFields) {
                Object value = fieldInfo.getGetter().invoke(entity);
                psInsert.setObject(paramIndex++, value);
            }
            psInsert.executeUpdate();
        } catch (SQLException | IllegalAccessException | InvocationTargetException e) {
            throw new ORMException("Не удалось сохранить объект: " + entity, e);
        }
    }

    public void update(T entity) {
        try {
            int paramIndex = 1;
            for (FieldInfo fieldInfo : cachedFields) {
                Object value = fieldInfo.getGetter().invoke(entity);
                psUpdate.setObject(paramIndex++, value);
            }
            psUpdate.setObject(paramIndex, idGetter.invoke(entity));
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
                    T entity = cls.getDeclaredConstructor().newInstance();
                    for (FieldInfo fieldInfo : cachedFields) {
                        Object value = rs.getObject(fieldInfo.getColumnName());
                        fieldInfo.getSetter().invoke(entity, value);
                    }
                    idSetter.invoke(entity, rs.getObject(getIdName()));
                    return Optional.of(entity);
                }
            }
        } catch (SQLException | InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
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
            throw new ORMException("У класса " + cls.getName() + " нет аннотации @RepositoryTable");
        }

        String tableName = getTableName();
        if (tableName.isBlank()) {
            tableName = cls.getSimpleName();
        }

        cachedFields = Arrays.stream(cls.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(RepositoryField.class))
                .map(f -> {
                    String fieldName = f.getName();
                    String getterMethodName = "get" + capitalizeFirstLetter(fieldName);
                    String setterMethodName = "set" + capitalizeFirstLetter(fieldName);
                    String columnName = f.getAnnotation(RepositoryField.class).value();
                    try {
                        Method getterMethod = cls.getMethod(getterMethodName);
                        Method setterMethod = cls.getMethod(setterMethodName, f.getType());
                        return new FieldInfo(f, getterMethod, setterMethod, columnName.isBlank() ? fieldName : columnName);
                    } catch (NoSuchMethodException e) {
                        throw new ORMException("Не объявлен getter или setter для поля: " + fieldName, e);
                    }
                })
                .collect(Collectors.toList());

        if (cachedFields.isEmpty()) {
            throw new ORMException("Не существует полей с аннотацией @RepositoryField в классе: "
                    + cls.getName());
        }

        idField = Arrays.stream(cls.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(RepositoryIdField.class))
                .findFirst()
                .orElseThrow(() -> new ORMException("У класса " + cls.getName() + " нет поля id"));

        try {
            String idFieldName = idField.getName();
            idGetter = cls.getMethod("get" + capitalizeFirstLetter(idFieldName));
            idSetter = cls.getMethod("set" + capitalizeFirstLetter(idFieldName), idField.getType());
        } catch (NoSuchMethodException e) {
            throw new ORMException("Не объявлен getter или setter для поля: " + idField.getName(), e);
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
            throw new ORMException("Произошла ошибка при выполнения SQL-запроса " + cls.getName(), e);
        }
    }

    private String getTableName() {
        RepositoryTable annotation = cls.getAnnotation(RepositoryTable.class);
        return annotation.value();
    }

    private String generateInsertQuery(String tableName) {
        StringBuilder query = new StringBuilder("INSERT INTO " + tableName + " (");
        StringBuilder values = new StringBuilder(") VALUES (");
        for (FieldInfo fieldInfo : cachedFields) {
            query.append(fieldInfo.getColumnName()).append(", ");
            values.append("?, ");
        }
        query.setLength(query.length() - 2);
        values.setLength(values.length() - 2);
        query.append(values).append(")");
        return query.toString();
    }

    private String generateUpdateQuery(String tableName) {
        StringBuilder query = new StringBuilder("UPDATE " + tableName + " SET ");
        for (FieldInfo fieldInfo : cachedFields) {
            query.append(fieldInfo.getColumnName()).append(" = ?, ");
        }
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
            for (FieldInfo fieldInfo : cachedFields) {
                Object value = rs.getObject(fieldInfo.getColumnName());
                fieldInfo.getSetter().invoke(entity, value);
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
