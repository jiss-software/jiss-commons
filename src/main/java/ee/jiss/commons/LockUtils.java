package ee.jiss.commons;

import java.util.concurrent.locks.Lock;
import java.util.function.Supplier;

public class LockUtils {
    public static <T> T withLock(final Supplier<T> fun, final Lock lock) {
        lock.lock();
        final T result;

        try {
            result = fun.get();
        } catch (final RuntimeException exp) {
            throw exp;
        } catch (final Exception exp) {
            throw new RuntimeException(exp);
        } finally {
            lock.unlock();
        }

        return result;
    }

    public static void withLock(final VoidExecutor fun, final Lock lock) {
        lock.lock();

        try {
            fun.run();
        } catch (final RuntimeException exp) {
            throw exp;
        } catch (final Exception exp) {
            throw new RuntimeException(exp);
        } finally {
            lock.unlock();
        }
    }
}
