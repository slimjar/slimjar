package io.github.slimjar.resolver.reader.facade;

import java.io.InputStreamReader;
import java.lang.reflect.Method;

public final class ReflectiveGsonFacade implements GsonFacade {
    private final Object gson;
    private final Method gsonFromJsonMethod;

    ReflectiveGsonFacade(Object gson, Method gsonFromJsonMethod) {
        this.gson = gson;
        this.gsonFromJsonMethod = gsonFromJsonMethod;
    }

    @Override
    public <T> T fromJson(InputStreamReader reader, Class<T> clazz) throws ReflectiveOperationException {
        final Object result = gsonFromJsonMethod.invoke(gson, reader, clazz);
        if (clazz.isAssignableFrom(result.getClass())) {
            return (T) result;
        } else {
            throw new AssertionError("Gson returned wrong type!");
        }
    }
}
