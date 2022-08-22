package me.ashenguard.reflected;

public enum ReflectedModifier {
    Public(java.lang.reflect.Modifier.PUBLIC),
    Protected(java.lang.reflect.Modifier.PROTECTED),
    Private(java.lang.reflect.Modifier.PRIVATE),
    Final(java.lang.reflect.Modifier.FINAL),
    Static(java.lang.reflect.Modifier.STATIC);

    private final int modifier;

    ReflectedModifier(int modifier) {
        this.modifier = modifier;
    }

    public boolean test(int modifier) {
        return (this.modifier & modifier) != 0;
    }
}
