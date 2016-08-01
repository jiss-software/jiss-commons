package ee.jiss.commons.function;

/**
 * Reimplementation of runnable which can throws any exception.
 *
 * For more details see <code>java.lang.Function</code>
 *
 * Block of code, which potentially throws any exception.
 */
public interface ThrowsRunnable {
    void run() throws Exception;
}