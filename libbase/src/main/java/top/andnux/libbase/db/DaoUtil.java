package top.andnux.libbase.db;

import android.text.TextUtils;

import java.util.List;
import java.util.Locale;

public class DaoUtil {

    private DaoUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static String getTableName(Class<?> clazz) {
        return clazz.getSimpleName();
    }

    public static String getColumnType(String type) {
        String value = null;
        if (type.contains("String")) {
            value = " text";
        } else if (type.contains("int")) {
            value = " integer";
        } else if (type.contains("boolean")) {
            value = " boolean";
        } else if (type.contains("float")) {
            value = " float";
        } else if (type.contains("double")) {
            value = " double";
        } else if (type.contains("char")) {
            value = " varchar";
        } else if (type.contains("long")) {
            value = " long";
        }
        return value;
    }

    public static <T> T[] asArray(List<T> list) {
        Object[] data = new Object[list.size()];
        for (int i = 0; i < list.size(); i++) {
            data[i] = list.get(i);
        }
        return (T[]) data;
    }

    public static String capitalize(String string) {
        if (!TextUtils.isEmpty(string)) {
            return string.substring(0, 1).toUpperCase(Locale.US) + string.substring(1);
        }
        return string == null ? null : "";
    }
}
