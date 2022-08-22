package me.ashenguard.reflected;

import org.jetbrains.annotations.NotNull;

public interface Reflected {
    /**
     * Checks if class A is same or an inheritance of class B
     * @param cls the class is question
     * @param type the target class
     * @return if class A is inherited class B
     */
    static boolean isValidType(@NotNull Class<?> cls, @NotNull Class<?> type) {
        if (cls.isPrimitive()) return isValidType(translateClass(cls), type);

        return type.isAssignableFrom(cls);
    }

    static Class<?> translateClass(Class<?> cls) {
        if (cls.equals(boolean.class)) return Boolean.class;
        if (cls.equals(char.class)) return Character.class;

        if (cls.equals(byte.class)) return Byte.class;
        if (cls.equals(short.class)) return Short.class;
        if (cls.equals(int.class)) return Integer.class;
        if (cls.equals(long.class)) return Long.class;

        if (cls.equals(float.class)) return Float.class;
        if (cls.equals(double.class)) return Double.class;

        return cls;
    }

    int getModifiers();

    default boolean testModifier(ReflectedModifier modifier) {
        return modifier.test(getModifiers());
    }

    default boolean isPublic() {
        return testModifier(ReflectedModifier.Public);
    }
    default boolean isProtected() {
        return testModifier(ReflectedModifier.Protected);
    }
    default boolean isPrivate() {
        return testModifier(ReflectedModifier.Private);
    }
    default boolean isFinal() {
        return testModifier(ReflectedModifier.Final);
    }
    default boolean isStatic() {
        return testModifier(ReflectedModifier.Static);
    }
}
