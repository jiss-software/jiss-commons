package ee.jiss.commons;

import java.beans.PropertyDescriptor;
import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import static java.io.File.pathSeparatorChar;
import static java.lang.Class.forName;
import static java.lang.Thread.currentThread;
import static java.util.Arrays.copyOfRange;
import static java.util.Arrays.stream;

public class MetaUtils {
    public static final String TARGET_DELIMITER = "->";

    public static final char PACKAGE_DELIMITER = '.';
    public static final char DIRECTORY_DELIMITER = pathSeparatorChar;
    public static final String CLASS_POSTFIX = ".class";

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

    public static <T> T copy(final Object donor, final T target, final String... fields) {
        if (donor == null || target == null || fields == null || fields.length == 0) {
            return target;
        }

        stream(fields).forEach(property -> {
            writeField(target, property, readField(donor, property));
        });

        return target;
    }

    public static <T> T copy(final Object donor, final T target, final boolean symmetric, final String... fields) {
        try {
            if (symmetric) return copy(donor, target, fields);
            if (donor == null || target == null || fields == null || fields.length == 0) return target;

            stream(fields).forEach(expression -> {
                final String[] properties = expression.split(TARGET_DELIMITER);
                final Object donorValue = readField(donor, properties[0]);

                if (properties.length <= 1) {
                    writeField(target, properties[0], donorValue);
                    return;
                }

                stream(copyOfRange(properties, 1, properties.length)).forEach(targetProperty -> {
                    writeField(target, targetProperty, donorValue);
                });
            });

            return target;
        } catch (final Exception e) {
            throw new IllegalStateException("Packages scan fail!", e);
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    public static Iterable<Class> getClasses(final String packageName) {
        try {
            final String path = packageName.replace(PACKAGE_DELIMITER, DIRECTORY_DELIMITER);
            final List<File> dirs = new ArrayList<>();
            final List<Class> classes = new ArrayList<>();

            final Enumeration<URL> resources = currentThread().getContextClassLoader().getResources(path);

            while (resources.hasMoreElements()) dirs.add(new File(resources.nextElement().getFile()));
            for (final File dir : dirs) classes.addAll(getClasses(dir, packageName));

            return classes;
        } catch (final Exception e) {
            throw new IllegalStateException("Packages scan fail!", e);
        }
    }

    public static List<Class> getClasses(final File directory, final String packageName) {
        final List<Class> classes = new ArrayList<>();

        if (directory == null || ! directory.exists() || ! directory.isDirectory()) return classes;

        try {
            //noinspection ConstantConditions
            for (final File file : directory.listFiles()) {
                if (file.isDirectory()) {
                    classes.addAll(getClasses(file, packageName + PACKAGE_DELIMITER + file.getName()));
                    continue;
                }

                if (file.getName().endsWith(CLASS_POSTFIX))
                    classes.add(forName(packageName + PACKAGE_DELIMITER
                            + file.getName().substring(0, file.getName().length() - CLASS_POSTFIX.length())));
            }
        } catch (final Exception e) {
            throw new IllegalStateException("Package scan fail!", e);
        }

        return classes;
    }

    public static String[] getAnnotatedFieldsNames(final Class<?> type, final Class<? extends Annotation> annotation) {
        final List<String> names = new ArrayList<>();

        stream(type.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(annotation))
                .forEach(field -> names.add(field.getName()));

        return names.toArray(new String[names.size()]);
    }

    public static <T> T writeField(final T instance, final String property, final Object value) {
        try {
            //noinspection unchecked
            final Class<T> instanceType = (Class<T>) instance.getClass();

            final Field field = instance.getClass().getDeclaredField(property);
            if (isPublic(field)) {
                field.set(instance, value);
                return instance;
            }

            final Method setter = new PropertyDescriptor(property, instanceType).getWriteMethod();

            if (setter != null) {
                setter.invoke(instance, value);
                return instance;
            }

            if (! field.isAccessible()) field.setAccessible(true);
            field.set(instance, value);

            return instance;
        } catch (final Exception exp) {
            throw new IllegalStateException("Bad attempt to copy", exp);
        }
    }

    public static <T, R> R readField(final T instance, final String property) {
        try {
            final Field field = instance.getClass().getDeclaredField(property);
            if (isPublic(field)) field.get(instance);

            try {
                final Method getter = new PropertyDescriptor(property, instance.getClass()).getReadMethod();

                if (getter != null) return (R) getter.invoke(instance);
            } catch (final Exception ignored) {

            }

            if (! field.isAccessible()) field.setAccessible(true);

            @SuppressWarnings("unchecked") final R result = (R) field.get(instance);

            return result;
        } catch (final Exception exp) {
            throw new IllegalStateException("Bad attempt to copy", exp);
        }
    }

    public static boolean isPublic(final Field field) {
        return Modifier.isPublic(field.getModifiers());
    }
}
