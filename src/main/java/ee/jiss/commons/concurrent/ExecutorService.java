package ee.jiss.commons.concurrent;

import ee.jiss.commons.function.ThrowsRunnable;

import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.function.Predicate;

import static ee.jiss.commons.lang.ExceptionUtils.wrap;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class ExecutorService {
	private final ScheduledThreadPoolExecutor executor;

	public ExecutorService(int poolSize) {
		this.executor = new ScheduledThreadPoolExecutor(poolSize);
	}

	public void schedule(ThrowsRunnable task, long delay) {
		this.executor.schedule(() -> wrap(task), delay, MILLISECONDS);
	}

	public void execute(ThrowsRunnable task) {
		this.executor.execute(() -> wrap(task));
	}

	public <T> void run(Callable<T> task, Predicate<T> predicate, long delay) {
		this.executor.execute(() -> this.submitScheduled(task, predicate, delay));
	}

	private <T> void submitScheduled(Callable<T> task, Predicate<T> predicate, long delay) {
		T result = wrap(task::call);
		if (predicate.test(result)) return;

		this.executor.schedule(() -> this.submitScheduled(task, predicate, delay), delay, MILLISECONDS);
	}
}
