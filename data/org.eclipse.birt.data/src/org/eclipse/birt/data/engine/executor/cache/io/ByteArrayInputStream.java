/*
 * Copyright (c) 1994, 2023, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

/*
 * Modifications copyright (c) 2025 Your Name or Your Company.
 *
 * Modifications:
 * - Based on java.io.ByteArrayInputStream from OpenJDK.
 * - Modified to [Remove syncronized blocks to improve performance in single Threaded scenarios].
 */

package org.eclipse.birt.data.engine.executor.cache.io;

import java.util.Arrays;
import java.util.Objects;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

public class ByteArrayInputStream extends InputStream {
    private static final int MAX_TRANSFER_SIZE = 131072;
    protected byte[] buf;
    protected int pos;
    protected int mark = 0;
    protected int count;

    public ByteArrayInputStream(byte[] buf) {
        this.buf = buf;
        this.pos = 0;
        this.count = buf.length;
    }

    public ByteArrayInputStream(byte[] buf, int offset, int length) {
        this.buf = buf;
        this.pos = offset;
        this.count = Math.min(offset + length, buf.length);
        this.mark = offset;
    }

    public int read() {
        return this.pos < this.count ? this.buf[this.pos++] & 255 : -1;
    }

    public int read(byte[] b, int off, int len) {
        if (this.pos >= this.count) {
            return -1;
        } else {
            int avail = this.count - this.pos;
            if (len > avail) {
                len = avail;
            }

            if (len <= 0) {
                return 0;
            } else {
                System.arraycopy(this.buf, this.pos, b, off, len);
                this.pos += len;
                return len;
            }
        }
    }

    public byte[] readAllBytes() {
        byte[] result = Arrays.copyOfRange(this.buf, this.pos, this.count);
        this.pos = this.count;
        return result;
    }

    public int readNBytes(byte[] b, int off, int len) {
        int n = this.read(b, off, len);
        return n == -1 ? 0 : n;
    }

    public long transferTo(OutputStream out) throws IOException {
        int len = this.count - this.pos;
        if (len > 0) {
            int nbyte;
            for(int nwritten = 0; nwritten < len; nwritten += nbyte) {
                nbyte = Integer.min(len - nwritten, 131072);
                out.write(this.buf, this.pos, nbyte);
                this.pos += nbyte;
            }

            assert this.pos == this.count;
        }

        return (long)len;
    }

    public long skip(long n) {
        long k = (long)(this.count - this.pos);
        if (n < k) {
            k = n < 0L ? 0L : n;
        }

        this.pos += (int)k;
        return k;
    }

    public int available() {
        return this.count - this.pos;
    }

    public boolean markSupported() {
        return true;
    }

    public void mark(int readAheadLimit) {
        this.mark = this.pos;
    }

    public void reset() {
        this.pos = this.mark;
    }

    public void close() throws IOException {
    }
}