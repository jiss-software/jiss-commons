package ee.jiss.commons;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ExceptionUtils {
    public static <T> T wrap(final Supplier<T> fun) {
        try {
            return fun.get();
        } catch (final RuntimeException exp) {
            throw exp;
        } catch (final Exception exp) {
            throw new RuntimeException(exp);
        }
    }

    public static <T> T wrap(final Supplier<T> fun, final T def) {
        try {
            return fun.get();
        } catch (final Exception exp) {
            return def;
        }
    }

    public static void wrap(final VoidExecutor fun) {
        try {
            fun.run();
        } catch (final RuntimeException exp) {
            throw exp;
        } catch (final Exception exp) {
            throw new RuntimeException(exp);
        }
    }

    public static void ignore(final VoidExecutor fun) {
        try {
            fun.run();
        } catch (final Exception ignored) { }
    }

    public static void ignore(final VoidExecutor fun, final Consumer<Exception> onException) {
        try {
            fun.run();
        } catch (final Exception exp) {
            onException.accept(exp);
        }
    }

    public static <T> T ignore(final Supplier<T> fun, final Function<Exception, T> onException) {
        try {
            return fun.get();
        } catch (final Exception exp) {
            return onException.apply(exp);
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    public static void throwIf(final boolean condition, final Supplier<RuntimeException> exceptionProvider) {
        if (condition) throw exceptionProvider.get();
    }
}
