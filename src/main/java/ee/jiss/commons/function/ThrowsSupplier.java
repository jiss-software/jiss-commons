package ee.jiss.commons.function;

/**
 * Reimplementation of java supplier which can throws any exception.
 *
 * For more details see <code>java.util.function.Supplier</code>
 *
 * @param <T> the type of results supplied by this supplier
 */
public interface ThrowsSupplier<T> {
    T get() throws Exception;
}
