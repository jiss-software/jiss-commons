package ee.jiss.commons.concurrent;

import ee.jiss.commons.function.ThrowsRunnable;
import ee.jiss.commons.function.ThrowsSupplier;

import java.util.concurrent.locks.Lock;

public class LockUtils {
    public static <T> T withLock(final ThrowsSupplier<T> fun, final Lock lock) {
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

    public static void withLock(final ThrowsRunnable fun, final Lock lock) {
        lock.lock();

        try {
            fun.run();
        } catch (RuntimeException exp) {
            throw exp;
        } catch (Exception exp) {
            throw new RuntimeException(exp);
        } finally {
            lock.unlock();
        }
    }
}
