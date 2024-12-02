package otus.java.pro.dbinteractions;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

class FieldInfo {
    private final Field field;
    private final Method getter;
    private final Method setter;
    private final String columnName;

    public FieldInfo(Field field, Method getter, Method setter, String columnName) {
        this.field = field;
        this.getter = getter;
        this.setter = setter;
        this.columnName = columnName;
    }

    public Field getField() {
        return field;
    }

    public Method getGetter() {
        return getter;
    }

    public Method getSetter() {
        return setter;
    }

    public String getColumnName() {
        return columnName;
    }
}