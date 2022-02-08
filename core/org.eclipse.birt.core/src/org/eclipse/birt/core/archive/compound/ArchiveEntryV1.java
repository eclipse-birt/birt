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

import java.io.IOException;

class ArchiveEntryV1 extends ArchiveEntry {

	protected ArchiveFileV1 af;
	protected long start;
	protected long end;
	protected long length;

	ArchiveEntryV1(ArchiveFileV1 af, String name, long start, long length) throws IOException {
		super(name);
		this.af = af;
		this.start = start;
		this.length = length;
	}

	public void close() throws IOException {
	}

	public long getLength() throws IOException {
		return length;
	}

	public void setLength(long length) throws IOException {
		throw new IOException("");
	}

	public int read(long pos, byte[] b, int off, int len) throws IOException {
		if (pos >= length) {
			return -1;
		}

		if (pos + len > length) {
			len = (int) (length - pos);
		}

		if (len == 0) {
			return 0;
		}
		// read first block
		return af.read(start + pos, b, off, len);
	}

	public void write(long pos, byte[] b, int off, int len) throws IOException {
		af.write(pos, b, off, len);
	}
}
