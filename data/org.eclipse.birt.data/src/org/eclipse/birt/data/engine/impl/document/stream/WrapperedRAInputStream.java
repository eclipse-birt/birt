
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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
package org.eclipse.birt.data.engine.impl.document.stream;

import java.io.IOException;
import java.util.logging.Logger;

import org.eclipse.birt.core.archive.RAInputStream;
import org.eclipse.birt.data.engine.core.DataException;

/**
 * 
 */

public class WrapperedRAInputStream extends RAInputStream {
	private static Logger logger = Logger.getLogger(WrapperedRAInputStream.class.getName());

	private RAInputStream raIn;
	private long startOffset;
	private long size;

	public WrapperedRAInputStream(RAInputStream input, long startOffset, long size) throws DataException {
		this.raIn = input;
		this.startOffset = startOffset;
		this.size = size;
		long length = 0;
		try {
			length = this.raIn.length();
			this.raIn.seek(this.startOffset);
		} catch (IOException e) {
			logger.warning("Available: " + length + "    Requests: " + this.startOffset);
			throw new DataException(e.getLocalizedMessage());
		}
	}

	public int available() throws IOException {
		return this.raIn.available();
	}

	public long getOffset() throws IOException {
		return this.raIn.getOffset() - this.startOffset;
	}

	public long length() throws IOException {
		return this.size;
	}

	public void readFully(byte[] b, int off, int len) throws IOException {
		this.raIn.readFully(b, off, len);
	}

	public int readInt() throws IOException {
		return this.raIn.readInt();
	}

	public long readLong() throws IOException {
		return this.raIn.readLong();
	}

	public void refresh() throws IOException {
		this.raIn.refresh();
		this.raIn.seek(this.startOffset);
	}

	public void seek(long localPos) throws IOException {
		this.raIn.seek(this.startOffset + localPos);

	}

	public int read() throws IOException {
		return this.raIn.read();
	}

	public void close() throws IOException {
		this.raIn.close();
	}
}
