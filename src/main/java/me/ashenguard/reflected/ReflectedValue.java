package me.ashenguard.reflected;

public class ReflectedValue<R> {
    private final R value;
    private final boolean failed;

    protected static <R> ReflectedValue<R> failure(Class<R> type) {
        return new ReflectedValue<>(null, true);
    }

    public ReflectedValue(R value) {
        this.value = value;
        this.failed = false;
    }
    public ReflectedValue(R value, boolean failed) {
        this.value = value;
        this.failed = failed;
    }

    public final R get() {
        return value;
    }
    public final boolean failed() {
        return failed;
    }
}
