package ee.jiss.commons;

import java.io.File;
import java.util.Collection;
import java.util.Map;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class CheckUtils {
    public static boolean isTrue(final Boolean value) {
        return TRUE.equals(value);
    }

    public static boolean isTrueOrNull(final Boolean value) {
        return ! FALSE.equals(value);
    }

    public static boolean isFalse(final Boolean value) {
        return FALSE.equals(value);
    }

    public static boolean isFalseOrNull(final Boolean value) {
        return ! TRUE.equals(value);
    }

    public static boolean isEmptyCollection(final Collection<?> value) {
        return value == null || value.isEmpty();
    }

    public static boolean isNotEmptyCollection(final Collection<?> value) {
        return ! isEmptyCollection(value);
    }

    public static boolean isSizeOfCollection(final Collection<?> value, final int size) {
        return size == 0 ? isEmptyCollection(value) : (isNotEmptyCollection(value) && value.size() == size);
    }

    public static boolean isNotSizeOfCollection(final Collection<?> value, final int size) {
        return ! isSizeOfCollection(value, size);
    }

    public static boolean isEmptyMap(final Map<?, ?> value) {
        return value == null || value.isEmpty();
    }

    public static boolean isNotEmptyMap(final Map<?, ?> value) {
        return ! isEmptyMap(value);
    }

    public static boolean isSizeOfMap(final Map<?, ?> value, final int size) {
        return size == 0 ? isEmptyMap(value) : (isNotEmptyMap(value) && value.size() == size);
    }

    public static boolean isNotSizeOfMap(final Map<?, ?> value, final int size) {
        return ! isSizeOfMap(value, size);
    }

    public static boolean isEmptyArray(final Object[] value) {
        return value == null || value.length == 0;
    }

    public static boolean isNotEmptyArray(final Object[] value) {
        return ! isEmptyArray(value);
    }

    public static boolean isSizeOfArray(final Object[] value, final int size) {
        return size == 0 ? isEmptyArray(value) : value.length == size;
    }

    public static boolean isNotSizeOfArray(final Object[] value, final int size) {
        return ! isSizeOfArray(value, size);
    }

    public static boolean isEmptyString(final String value) {
        return value == null || value.isEmpty();
    }

    public static boolean isNotEmptyString(final String value) {
        return ! isEmptyString(value);
    }

    public static boolean isSizeOfString(final String value, final int size) {
        return size == 0 ? isEmptyString(value) : value.length() == size;
    }

    public static boolean isNotSizeOfString(final String value, final int size) {
        return ! isSizeOfString(value, size);
    }

    public static boolean exists(final File value) {
        return value != null && value.exists();
    }

    public static boolean notExists(final File value) {
        return ! exists(value);
    }

    public static boolean isDirectory(final File value) {
        return value != null && value.isDirectory();
    }

    public static boolean isFile(final File value) {
        return value != null && value.isFile();
    }
}