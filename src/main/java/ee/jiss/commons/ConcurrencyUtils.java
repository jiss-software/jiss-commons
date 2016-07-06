package ee.jiss.commons;

import org.slf4j.Logger;

import static ee.jiss.commons.ExceptionUtils.ignore;
import static java.lang.Thread.MIN_PRIORITY;
import static java.lang.Thread.sleep;
import static org.slf4j.LoggerFactory.getLogger;

public class ConcurrencyUtils {
    public static void background(String name, VoidExecutor task) {
        background(name, task, 10_000, MIN_PRIORITY);
    }

    public static void background(String name, VoidExecutor task, long period) {
        background(name, task, period, MIN_PRIORITY);
    }

    public static void background(String name, VoidExecutor task, long period, int priority) {
        final Thread thread = new Thread(() -> {
            Logger logger = getLogger(name);
            while (true) ignore(() -> {
                task.run();
                sleep(period);
            }, exp -> logger.error("Problems while background execution: ", exp));
        });

        thread.setName(name);
        thread.setPriority(priority);
        thread.start();
    }
}
