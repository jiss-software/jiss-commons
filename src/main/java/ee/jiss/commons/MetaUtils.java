package ee.jiss.commons;

import java.lang.reflect.Field;

public class MetaUtils {
    public static Object getField(Object object, String field) {
        if (object == null || field == null) return null;

        try {
            Field target = object.getClass().getDeclaredField(field);
            target.setAccessible(true);
            return target.get(object);
        } catch (Exception exp) {
            return null;
        }
    }

    public static void setField(Object object, String field, Object value) {
        if (object == null || field == null) return;

        try {
            Field target = object.getClass().getDeclaredField(field);
            target.setAccessible(true);
            target.set(object, value);
        } catch (Exception ignored) {
        }
    }
}
