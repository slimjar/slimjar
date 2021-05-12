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

package io.github.slimjar.app;

import java.util.HashMap;
import java.util.Map;

public abstract class Application {
    private final Map<Class<?>, Object> facadeMapping = new HashMap<>();

    @SuppressWarnings("unchecked")
    protected final <T, U extends T> T registerFacade(Class<T> clazz, U t) {
        Object obj = facadeMapping.put(clazz, t);
        if (obj != null && !(clazz.isAssignableFrom(obj.getClass()))) {
            throw new IllegalStateException("Previous facade value did not conform to type restriction!");
        }
        return (T) obj;
    }

    @SuppressWarnings("unchecked")
    public final <T> T getFacade(Class<T> clazz) {
        Object obj = facadeMapping.get(clazz);
        if (obj == null) return null;
        if (!(clazz.isAssignableFrom(obj.getClass()))) {
            throw new IllegalStateException("Current facade value does not conform to type restriction!");
        }
        return (T) obj;
    }

    public boolean start() { return false; }

    public boolean stop() { return false; }
}
