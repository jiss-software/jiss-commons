package ee.jiss.commons.reflect;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;

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
import java.util.function.Function;
import java.util.stream.Stream;

import static ee.jiss.commons.lang.ExceptionUtils.wrap;
import static java.io.File.pathSeparatorChar;
import static java.lang.Class.forName;
import static java.lang.Thread.currentThread;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static org.reflections.util.ClasspathHelper.forPackage;

public class MetaUtils {
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

            final R result = (R) field.get(instance);

            return result;
        } catch (final Exception exp) {
            throw new IllegalStateException("Bad attempt to copy", exp);
        }
    }

    public static Object call(Object object, String method, Object... args)
    {
        return wrap(() -> object.getClass().getMethod(method).invoke(object, args));
    }


    public static Function<Object, ?> call(Method method, Object object)
    {

        return arg -> wrap(() -> {
            if (Object[].class.isInstance(arg))
            {
                return method.invoke(object, (Object[]) arg);
            }
            else
            {
                return method.invoke(object, arg);
            }
        });
    }

    public static Object[] args(Object... args)
    {
        return args;
    }

    public static Method methodByReturn(Class<?> type, String name, Class<?> returnType)
    {
        for (Method method : type.getMethods())
        {
            if (method.getName().contains(name) && returnType.isAssignableFrom(method.getReturnType()))
            {
                return method;
            }
        }

        throw new IllegalStateException("Method with name: " + name + " and return type "
                + returnType.getCanonicalName() + " not found in " + type.getCanonicalName());
    }

    public static Stream<Method> findMethodByAnnotation(Class<?> type, Class<? extends Annotation> annotation)
    {
        return asList(type.getDeclaredMethods()).stream()
                .filter(method -> annotation == null || method.isAnnotationPresent(annotation));
    }

    public static Stream<Class<?>> findByAnnotation(Class<? extends Annotation> type, String location)
    {
        return new Reflections(forPackage(location), new TypeAnnotationsScanner(), new SubTypesScanner())
                .getTypesAnnotatedWith(type)
                .stream().filter(found -> found.getPackage().getName().startsWith(location));
    }

    public static <T> T create(Class<T> type)
    {
        return wrap(type::newInstance);
    }

    public static Class<?> paramType(Method m, int i)
    {
        if (m.getParameterTypes().length <= i)
        {
            return null;
        }

        return m.getParameterTypes()[i];
    }

    public static boolean isParamType(Method m, int i, Class<?> type)
    {
        Class<?> paramType = paramType(m, i);
        return paramType != null && paramType.isAssignableFrom(type);
    }

    public static boolean isPublic(final Field field) {
        return Modifier.isPublic(field.getModifiers());
    }
}
