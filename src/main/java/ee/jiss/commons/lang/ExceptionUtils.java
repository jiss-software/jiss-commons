package ee.jiss.commons.lang;

import ee.jiss.commons.function.ThrowsRunnable;
import ee.jiss.commons.function.ThrowsSupplier;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.apache.commons.lang3.exception.ExceptionUtils.getThrowables;

public class ExceptionUtils {
    public static <T extends Throwable> T findCaused(Exception exp, Class<T> type) {
        for (Throwable it : getThrowables(exp)) {
            if (type.isInstance(it)) return (T) it;
        }
        return null;
    }

    public static <T> T wrap(ThrowsSupplier<T> fun) {
        try {
            return fun.get();
        } catch (RuntimeException exp) {
            throw exp;
        } catch (Exception exp) {
            throw new RuntimeException(exp);
        }
    }

    public static <T> T wrap(ThrowsSupplier<T> fun, T def) {
        try {
            return fun.get();
        } catch (Exception exp) {
            return def;
        }
    }

    public static void wrap(ThrowsRunnable fun) {
        try {
            fun.run();
        } catch (RuntimeException exp) {
            throw exp;
        } catch (Exception exp) {
            throw new RuntimeException(exp);
        }
    }

    public static void ignore(ThrowsRunnable fun) {
        try {
            fun.run();
        } catch (Exception ignored) { }
    }

    public static <T> T ignore(ThrowsSupplier<T> fun) {
        try {
            return fun.get();
        } catch (Exception ignored) {
            return null;
        }
    }

    public static void ignore(ThrowsRunnable fun, Consumer<Exception> onException) {
        try {
            fun.run();
        } catch (Exception exp) {
            onException.accept(exp);
        }
    }

    public static <T> T ignore(ThrowsSupplier<T> fun, Function<Exception, T> onException) {
        try {
            return fun.get();
        } catch (Exception exp) {
            return onException.apply(exp);
        }
    }

    public static void throwIf(boolean condition, Supplier<RuntimeException> exceptionProvider) {
        if (condition) throw exceptionProvider.get();
    }
}
