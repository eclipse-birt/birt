/*******************************************************************************
 * Copyright (c) 2004,2011 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.core.archive.compound;

import java.io.EOFException;
import java.io.IOException;

import org.eclipse.birt.core.archive.ArchiveUtil;
import org.eclipse.birt.core.archive.RAInputStream;
import org.eclipse.birt.core.i18n.CoreMessages;
import org.eclipse.birt.core.i18n.ResourceConstants;

/**
 * RAInputStream implementation based on the ArchiveEntry.
 * 
 */
public class ArchiveEntryInputStream extends RAInputStream {

	/** the archive entry */
	private ArchiveEntry entry;

	/**
	 * buffer used to read the int/long
	 */
	private byte[] buffer;
	private int buffer_size;
	private int buffer_offset;

	/** the current input position */
	private long offset;

	/**
	 * Constructor
	 * 
	 * @param fs     the compound file system.
	 * @param stream the stream item.
	 */
	ArchiveEntryInputStream(ArchiveEntry entry) {
		this.entry = entry;
		this.offset = 0;
		this.buffer = new byte[4096];
		this.buffer_size = 0;
		this.buffer_offset = 0;
	}

	public void close() throws IOException {
		if (entry != null) {
			try {
				entry.close();
			} finally {
				entry = null;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.InputStream#read()
	 */
	public int read() throws IOException {
		if (buffer_offset >= buffer_size) {
			refreshBuffer();
			if (buffer_offset >= buffer_size) {
				return -1;
			}
		}
		return buffer[buffer_offset++] & 0xff;
	}

	public int available() throws IOException {
		long av = entry.getLength() - getOffset();
		;
		if (av > Integer.MAX_VALUE) {
			return Integer.MAX_VALUE;
		}
		return (int) av;
	}

	public long getOffset() throws IOException {
		return offset + buffer_offset;
	}

	public long length() throws IOException {
		return entry.getLength();
	}

	public void readFully(byte[] b, int off, int len) throws IOException {
		int n = 0;
		do {
			int count = read(b, off + n, len - n);
			if (count < 0)
				throw new EOFException();
			n += count;
		} while (n < len);
	}

	public int read(byte b[], int off, int len) throws IOException {
		// we need first read from the cache
		if (buffer_offset < buffer_size) {
			int size = buffer_size - buffer_offset;
			if (size > len) {
				size = len;
			}
			System.arraycopy(buffer, buffer_offset, b, off, size);
			buffer_offset += size;
			return size;
		}
		offset += buffer_offset;
		buffer_offset = 0;
		buffer_size = 0;
		int size = entry.read(offset, b, off, len);
		if (size != -1) {
			offset += size;
		}
		return size;
	}

	public int readInt() throws IOException {
		if (buffer_offset + 4 > buffer_size) {
			refreshBuffer();
			if (buffer_offset + 4 > buffer_size) {
				throw new EOFException();
			}
		}
		int v = ArchiveUtil.bytesToInteger(buffer, buffer_offset);
		buffer_offset += 4;
		return v;
	}

	public long readLong() throws IOException {
		if (buffer_offset + 8 > buffer_size) {
			refreshBuffer();
			if (buffer_offset + 8 > buffer_size) {
				throw new EOFException();
			}
		}
		long v = ArchiveUtil.bytesToLong(buffer, buffer_offset);
		buffer_offset += 8;
		return v;
	}

	private void refreshBuffer() throws IOException {
		if (buffer_offset < buffer_size) {
			System.arraycopy(buffer, buffer_offset, buffer, 0, buffer_size - buffer_offset);
			offset += buffer_offset;
			buffer_size = buffer_size - buffer_offset;
			buffer_offset = 0;
		} else {
			offset += buffer_size;
			buffer_size = 0;
			buffer_offset = 0;
		}
		int readSize = entry.read(offset + buffer_size, buffer, buffer_size, buffer.length - buffer_size);
		if (readSize != -1) {
			buffer_size += readSize;
		}
	}

	public void refresh() throws IOException {
		offset += buffer_offset;
		buffer_offset = 0;
		buffer_size = 0;
		entry.refresh();
	}

	public void seek(long localPos) throws IOException {
		if (localPos < 0) {
			throw new IOException(
					CoreMessages.getFormattedString(ResourceConstants.INVALID_SEEK_OFFSET, new Object[] { localPos }));
		}

		if (localPos >= entry.getLength()) {
			throw new EOFException(CoreMessages.getString(ResourceConstants.EXCEED_FILE_LENGTH));
		}

		if (localPos < offset || localPos > offset + buffer_size) {
			offset = localPos;
			buffer_size = 0;
			buffer_offset = 0;
			return;
		}

		buffer_offset = (int) (localPos - offset);
	}

}
