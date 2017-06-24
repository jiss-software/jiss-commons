package ee.jiss.commons.lang;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static ee.jiss.commons.lang.CheckUtils.isEquals;
import static java.util.Objects.requireNonNull;

public class Optional<T> {
    private static final Optional<?> EMPTY = new Optional<>(null, null);
    private static final Predicate<?> NULL_PREDICATE = Objects::nonNull;
    private final T value;
    private final Predicate<T> predicate;

    @SuppressWarnings("unchecked")
    private Optional(T value, Predicate<T> predicate) {
        this.predicate = predicate == null ? (Predicate<T>) NULL_PREDICATE : predicate;
        this.value = value;
    }

    @SuppressWarnings("unchecked")
    public static<T> Optional<T> empty() {
        return (Optional<T>) EMPTY;
    }

    public static <T> Optional<T> opt(T value) {
        if (value == null) return empty();
        return new Optional<>(value, null);
    }

    public static <T> Optional<T> opt(T value, Predicate<T> predicate) {
        requireNonNull(predicate);
        if (value == null) return empty();
        return new Optional<>(value, predicate);
    }

    public T get() {
        if (! isPresent()) throw new NoSuchElementException("No value present");
        return value;
    }

    public boolean isPresent() {
        return this != EMPTY && this.predicate.test(value);
    }

    public void ifPresent(Consumer<? super T> consumer) {
        if (isPresent()) consumer.accept(value);
    }

    public Optional<T> filter(Predicate<? super T> predicate) {
        requireNonNull(predicate);
        if (! isPresent()) return this;
        return predicate.test(value) ? this : empty();
    }

    public<U> Optional<U> map(Function<? super T, ? extends U> mapper) {
        requireNonNull(mapper);
        if (! isPresent()) return empty();
        return Optional.opt(mapper.apply(value));
    }

    public<U> Optional<U> flatMap(Function<? super T, Optional<U>> mapper) {
        requireNonNull(mapper);
        if (! isPresent()) return empty();
        return requireNonNull(mapper.apply(value));
    }

    public T or(T other) {
        return isPresent() ? value : other;
    }

    public T orNull() {
        return isPresent() ? value : null;
    }

    public T orGet(Supplier<? extends T> other) {
        return isPresent() ? value : other.get();
    }

    public <X extends Throwable> T orThrow(Supplier<? extends X> exceptionSupplier) throws X {
        if (! isPresent()) throw exceptionSupplier.get();
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Optional)) return false;
        Optional<?> other = (Optional<?>) obj;
        return isEquals(value, other.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public String toString() {
        return isPresent() ? String.format("{\"value\":\"%s\"}", value) : "{}";
    }
}
