package io.github.slimjar.resolver.reader.facade;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class TypeToken<T> {
    private final Type rawType;
    public TypeToken() {
        this.rawType = getSuperclassTypeParameter(getClass());
    }

    public Type getRawType() {
        return rawType;
    }

    private static Type getSuperclassTypeParameter(final Class<?> subclass) {
        final Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Type parameter not found");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return parameterized.getActualTypeArguments()[0];
    }
}
