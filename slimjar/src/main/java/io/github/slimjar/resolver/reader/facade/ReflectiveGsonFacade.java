//
// MIT License
//
// Copyright (c) 2021 Vaishnav Anil
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.
//

package io.github.slimjar.resolver.reader.facade;

import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

public final class ReflectiveGsonFacade implements GsonFacade {
    private final Object gson;
    private final Method gsonFromJsonMethod;
    private final Method gsonFromJsonTypeMethod;
    private final Method canonicalizeMethod;

    ReflectiveGsonFacade(final Object gson, final Method gsonFromJsonMethod, final Method gsonFromJsonTypeMethod, final Method canonicalizeMethod) {
        this.gson = gson;
        this.gsonFromJsonMethod = gsonFromJsonMethod;
        this.gsonFromJsonTypeMethod = gsonFromJsonTypeMethod;
        this.canonicalizeMethod = canonicalizeMethod;
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

    @Override
    public <T> T fromJson(InputStreamReader reader, Type rawType) throws ReflectiveOperationException {
        final Object canonicalizedType = canonicalizeMethod.invoke(null, rawType);
        final Object result = gsonFromJsonTypeMethod.invoke(gson, reader, canonicalizedType);
        return (T) result;
    }
}
