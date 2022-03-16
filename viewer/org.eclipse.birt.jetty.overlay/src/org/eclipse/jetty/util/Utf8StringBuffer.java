/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/
//========================================================================
//Copyright 2006 Mort Bay Consulting Pty. Ltd.
//------------------------------------------------------------------------
//Licensed under the Apache License, Version 2.0 (the "License");
//you may not use this file except in compliance with the License.
//You may obtain a copy of the License at 
//http://www.apache.org/licenses/LICENSE-2.0
//Unless required by applicable law or agreed to in writing, software
//distributed under the License is distributed on an "AS IS" BASIS,
//WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//See the License for the specific language governing permissions and
//limitations under the License.
//========================================================================

package org.eclipse.jetty.util;

/* ------------------------------------------------------------ */
/**
 * UTF-8 StringBuffer. <br>
 * <br>
 * <b>This is a mod version over original Jetty implementation to handle the
 * Unicode Extension-B cases. May need update when Jetty changes.</b> <br>
 * <br>
 * This class wraps a standard {@link java.lang.StringBuffer} and provides
 * methods to append UTF-8 encoded bytes, that are converted into characters.
 * 
 * This class is stateful and up to 6 calls to {@link #append(byte)} may be
 * needed before state a character is appended to the string buffer.
 * 
 * The UTF-8 decoding is done by this class and no additional buffers or Readers
 * are used. The UTF-8 code was inspired by http://javolution.org
 * 
 */
public class Utf8StringBuffer {

	StringBuffer _buffer;
	int _more;
	int _bits;
	boolean _errors;

	public Utf8StringBuffer() {
		_buffer = new StringBuffer();
	}

	public Utf8StringBuffer(int capacity) {
		_buffer = new StringBuffer(capacity);
	}

	public void append(byte[] b, int offset, int length) {
		int end = offset + length;
		for (int i = offset; i < end; i++) {
			append(b[i]);
		}
	}

	public void append(byte b) {
		if (b >= 0) {
			if (_more > 0) {
				_buffer.append('?');
				_more = 0;
				_bits = 0;
			} else {
				_buffer.append((char) (0x7f & b));
			}
		} else if (_more == 0) {
			if ((b & 0xc0) != 0xc0) {
				// 10xxxxxx
				_buffer.append('?');
				_more = 0;
				_bits = 0;
			} else if ((b & 0xe0) == 0xc0) {
				// 110xxxxx
				_more = 1;
				_bits = b & 0x1f;
			} else if ((b & 0xf0) == 0xe0) {
				// 1110xxxx
				_more = 2;
				_bits = b & 0x0f;
			} else if ((b & 0xf8) == 0xf0) {
				// 11110xxx
				_more = 3;
				_bits = b & 0x07;
			} else if ((b & 0xfc) == 0xf8) {
				// 111110xx
				_more = 4;
				_bits = b & 0x03;
			} else if ((b & 0xfe) == 0xfc) {
				// 1111110x
				_more = 5;
				_bits = b & 0x01;
			}
		} else if ((b & 0xc0) == 0xc0) { // 11??????
			_buffer.append('?');
			_more = 0;
			_bits = 0;
			_errors = true;
		} else {
			// 10xxxxxx
			_bits = (_bits << 6) | (b & 0x3f);
			if (--_more == 0) {
				if (_bits > 0xffff) {
					// handle Unicode Extension-B cases
					_buffer.append(0xD7C0 + (_bits >> 10));
					_buffer.append(0xDC00 | _bits & 0x3FF);
				} else {
					_buffer.append((char) _bits);
				}
			}
		}
	}

	public int length() {
		return _buffer.length();
	}

	public void reset() {
		_buffer.setLength(0);
		_more = 0;
		_bits = 0;
		_errors = false;
	}

	public StringBuffer getStringBuffer() {
		return _buffer;
	}

	@Override
	public String toString() {
		return _buffer.toString();
	}

	/* ------------------------------------------------------------ */
	/**
	 * @return True if there are non UTF-8 characters or incomplete UTF-8 characters
	 *         in the buffer.
	 */
	public boolean isError() {
		return _errors || _more > 0;
	}
}
