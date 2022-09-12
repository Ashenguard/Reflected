package me.ashenguard.reflected;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static me.ashenguard.reflected.ReflectedException.Type.*;

@SuppressWarnings("unused")
public class ReflectedMethod<R> implements Reflected {
    private final Class<R> type;
    private final Method method;

    public ReflectedMethod(@NotNull Class<R> type, @NotNull Class<?> from, @NotNull String name, ReflectedModifier... modifiers) {
        if (type.isPrimitive())
            throw new IllegalArgumentException("You can not pass a primitive type for reflection");
        try {
            this.type = type;
            this.method = from.getDeclaredMethod(name);

            if (!Reflected.isValidType(method.getReturnType(), this.type))
                throw new ReflectedException(NotMatch, String.format("The return type of method \"%s\", founded in class \"%s\", does not match with \"%s\"", name, from.getName(), type.getName()));

            for (ReflectedModifier modifier: modifiers) if (testModifier(modifier))
                throw new ReflectedException(NotMatch, String.format("Method \"%s\" founded in class \"%s\", does not meet modifier requirements", name, from.getName()));

        } catch (NoSuchMethodException e) {
            throw new ReflectedException(NotFound, String.format("Method \"%s\" was not found in the class \"%s\"", name, from.getName()));
        }
    }

    public void setAccessible() {
        try {
            method.setAccessible(true);
        } catch (Throwable ignored) {}
    }

    private Object invokeMethod(Object instance, Object... args) throws InvocationTargetException, IllegalAccessException {
        if (isStatic()) return method.invoke(null, args);
        else return method.invoke(instance, args);
    }

    @SuppressWarnings("unchecked")
    public @Nullable R invoke(@Nullable Object instance, Object... args) throws ReflectedException {
        try {
            Object value = invokeMethod(instance, args);
            return value == null ? null : (R) value;
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new ReflectedException(Execution, "Unable to invoke method", e);
        } catch (Throwable throwable) {
            throw new ReflectedException(Unknown, "Invoking method failed due an unexpected exception", throwable);
        }
    }

    public @Nullable R safeInvoke(@Nullable Object instance, Object... args) {
        try {
            return invoke(instance, args);
        } catch (ReflectedException ignored) {
            return null;
        }
    }

    @Override public int getModifiers() {
        return method.getModifiers();
    }

    public Class<R> getType() {
        return type;
    }
}
