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

package io.github.slimjar.resolver.data;

import java.net.URL;
import java.util.Objects;

public final class Mirror {

    private final URL mirroring;
    private final URL original;

    public Mirror(URL mirroring, URL original) {
        this.mirroring = mirroring;
        this.original = original;
    }

    public URL getMirroring() {
        return mirroring;
    }

    public URL getOriginal() {
        return original;
    }

    @Override
    public String toString() {
        return "Mirror{" +
                "mirror=" + mirroring +
                ", original=" + original +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Mirror mirror1 = (Mirror) o;
        return Objects.equals(mirroring, mirror1.mirroring) && Objects.equals(original, mirror1.original);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mirroring, original);
    }

}
