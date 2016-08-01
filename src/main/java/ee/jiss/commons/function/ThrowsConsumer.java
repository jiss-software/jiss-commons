package ee.jiss.commons.function;


import java.util.Objects;
import java.util.function.Consumer;

/**
 * Reimplementation of java consumer which can throws any exception.
 *
 * For more details see <code>java.util.function.Consumer</code>
 *
 * @param <T> the type of the input to the operation
 */
public interface ThrowsConsumer<T> {
    void accept(T t) throws Exception;

    default ThrowsConsumer<T> andThen(ThrowsConsumer<? super T> after) {
        Objects.requireNonNull(after);
        return (T t) -> { accept(t); after.accept(t); };
    }

    default ThrowsConsumer<T> andThen(Consumer<? super T> after) {
        Objects.requireNonNull(after);
        return (T t) -> { accept(t); after.accept(t); };
    }
}
