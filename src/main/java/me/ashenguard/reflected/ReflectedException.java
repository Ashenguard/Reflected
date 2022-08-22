package me.ashenguard.reflected;

import org.jetbrains.annotations.NotNull;

public class ReflectedException extends RuntimeException {
    @NotNull private final Type type;

    protected ReflectedException(@NotNull Type type) {
        super();
        this.type = type;
    }

    protected ReflectedException(@NotNull Type type, String message) {
        super(message);
        this.type = type;
    }

    protected ReflectedException(@NotNull Type type, String message, Throwable cause) {
        super(message, cause);
        this.type = type;
    }

    public @NotNull Type getType() {
        return type;
    }

    public enum Type {
        NotFound, NotMatch, Execution, Unknown
    }
}
