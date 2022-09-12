package me.ashenguard.reflected;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;

import static me.ashenguard.reflected.ReflectedException.Type.NotFound;
import static me.ashenguard.reflected.ReflectedException.Type.NotMatch;

@SuppressWarnings("unchecked")
public class ReflectedField<R> implements Reflected {
    private final Class<R> type;
    private final Field field;

    public ReflectedField(@NotNull Class<R> type, @NotNull Class<?> from, @NotNull String name, ReflectedModifier... modifiers) {
        if (type.isPrimitive())
            throw new IllegalArgumentException("You can not pass a primitive type for reflection");
        try {
            this.type = type;
            this.field = from.getDeclaredField(name);

            if (!Reflected.isValidType(field.getType(), this.type))
                throw new ReflectedException(NotMatch, String.format("The type of field \"%s\", founded in class \"%s\", does not match with \"%s\"", name, from.getName(), type.getName()));

            for (ReflectedModifier modifier: modifiers) if (testModifier(modifier))
                throw new ReflectedException(NotMatch, String.format("Field \"%s\" founded in class \"%s\", does not meet modifier requirements", name, from.getName()));

        } catch (NoSuchFieldException e) {
            throw new ReflectedException(NotFound, String.format("Field \"%s\" was not found in the class \"%s\"", name, from.getName()));
        }
    }

    private ReflectedField(Field field) {
        this.field = field;
        this.type = (Class<R>) field.getType();
    }

    public void setAccessible() {
        try {
            field.setAccessible(true);
        } catch (Throwable ignored) {}
    }

    @SuppressWarnings("unchecked")
    public R getValue(Object instance) throws IllegalAccessException {
        return (R) (isStatic() ? field.get(null) : field.get(instance));
    }

    public void setValue(Object instance, R value) throws IllegalAccessException {
        field.set(instance, value);
    }

    public ReflectedValue<R> getSafeValue(Object instance) {
        try {
            return new ReflectedValue<>(getValue(instance));
        } catch (IllegalAccessException ignored) {
            return ReflectedValue.failure(type);
        }
    }

    public Field getField() {
        return field;
    }

    @Override public int getModifiers() {
        return field.getModifiers();
    }

    public Class<R> getType() {
        return type;
    }
}
