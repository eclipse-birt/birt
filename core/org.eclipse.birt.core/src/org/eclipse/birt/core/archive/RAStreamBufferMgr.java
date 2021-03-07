/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.core.archive;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.util.IOUtil;

public class RAStreamBufferMgr {

	private RandomAccessFile randomFile;

	// The total number of buffer has been allocated.
	private int totalBuffer;

	// the lengh of the buffer
	private long length;

	// An arraylist to keep the buffer in order.
	// When MAX_NUMBER_OF_STREAM_BUFFER has been reached, we reuse the
	// buffer from the beginning of the list. There is less chance they
	// are going to be visited again.
	private List bufferList = new ArrayList(IOUtil.MAX_NUMBER_OF_STREAM_BUFFER);

	// A hash table to map between offset and buffer
	private Map bufferMap = new HashMap();

	// The buffer will be used for next operation
	private RAStreamBuffer currentBuffer;

	public RAStreamBufferMgr(RandomAccessFile randomFile) throws IOException {
		this.randomFile = randomFile;
		this.length = randomFile.length();
		this.totalBuffer = 0;
		this.currentBuffer = null;
	}

	/*
	 * The file pointer in the underlying file if no buffer is used.
	 */
	public long getFilePointer() {
		return currentBuffer == null ? 0 : currentBuffer.getOffset() + currentBuffer.getBufCur();
	}

	/*
	 * Write the data in array b[], if current buffer is not enough to hold all the
	 * data, a new buffer will be allocated or an old buffer will be reused.
	 */
	public void write(byte b[], int off, int len) throws IOException {
		if (currentBuffer == null) {
			currentBuffer = getBuffer(0);
		}
		while (len > 0) {
			int ret = currentBuffer.write(b, off, len);
			len -= ret;
			off += ret;
			if (len > 0) {
				currentBuffer = getBuffer(currentBuffer.getOffset() + IOUtil.RA_STREAM_BUFFER_LENGTH);
				currentBuffer.setBufCur(0);
			}
		}
		long fp = getFilePointer();
		if (fp > length) {
			length = fp;
		}
	}

	public void seek(long localPos) throws IOException {
		long offset = localPos / IOUtil.RA_STREAM_BUFFER_LENGTH * IOUtil.RA_STREAM_BUFFER_LENGTH;
		if (currentBuffer == null || currentBuffer.getOffset() != offset) {
			currentBuffer = getBuffer(offset);
		}
		currentBuffer.setBufCur((int) (localPos - offset));
		if (localPos > length) {
			length = localPos;
		}
	}

	public long length() {
		return length;
	}

	/*
	 * Flush all the buffers
	 */
	public void flushBuffer() throws IOException {
		for (int i = 0; i < totalBuffer; i++) {
			RAStreamBuffer buffer = (RAStreamBuffer) bufferList.get(i);
			buffer.flushBuffer();
		}
	}

	/**
	 * Get next available buffer for the data from position offset.
	 *
	 * @param offset
	 * @return
	 * @throws IOException
	 */
	private RAStreamBuffer getBuffer(long offset) throws IOException {
		// If we already have a buffer allocated for that offset, just
		// return it.
		Long offsetKey = new Long(offset);
		RAStreamBuffer buffer = (RAStreamBuffer) bufferMap.get(offsetKey);
		if (buffer != null) {
			return buffer;
		}
		// If not, and MAX_NUMBER_OF_STREAM_BUFFER has not been reached,
		// allocate a new buffer for it.
		if (totalBuffer < IOUtil.MAX_NUMBER_OF_STREAM_BUFFER) {
			buffer = new RAStreamBuffer(this.randomFile);
			buffer.resetBuffer(offset);
			totalBuffer++;
			bufferList.add(buffer);
			bufferMap.put(offsetKey, buffer);
			return buffer;
		}

		// If no buffer has been found, and MAX_NUMBER_OF_STREAM_BUFFER has
		// been reached, reuse the buffer from the beginning of the list and
		// put the buffer to the end of the list.
		buffer = (RAStreamBuffer) bufferList.get(0);
		buffer.flushBuffer();
		bufferMap.remove(new Long(buffer.getOffset()));
		buffer.resetBuffer(offset);
		bufferMap.put(offsetKey, buffer);
		bufferList.remove(0);
		bufferList.add(buffer);

		return buffer;
	}

}
