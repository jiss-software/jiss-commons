package ee.jiss.commons.function;

import java.util.Objects;
import java.util.function.Function;

/**
 * Reimplementation of function which can throws any exception.
 *
 * For more details see <code>java.util.function.Function</code>
 *
 * @param <T> the type of the input to the function
 * @param <R> the type of the result of the function
 */
public interface ThrowsFunction<T, R> {
    R apply(T t) throws Exception;

    default <V> ThrowsFunction<V, R> compose(ThrowsFunction<? super V, ? extends T> before) {
        Objects.requireNonNull(before);
        return (V v) -> apply(before.apply(v));
    }

    default <V> ThrowsFunction<V, R> compose(Function<? super V, ? extends T> before) {
        Objects.requireNonNull(before);
        return (V v) -> apply(before.apply(v));
    }

    default <V> ThrowsFunction<T, V> andThen(ThrowsFunction<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return (T t) -> after.apply(apply(t));
    }

    default <V> ThrowsFunction<T, V> andThen(Function<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return (T t) -> after.apply(apply(t));
    }

    static <T> ThrowsFunction<T, T> identity() {
        return t -> t;
    }
}
