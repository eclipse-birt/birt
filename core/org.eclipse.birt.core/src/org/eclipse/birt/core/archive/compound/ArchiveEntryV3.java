/*******************************************************************************
 * Copyright (c) 2011 Actuate Corporation.
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

import org.eclipse.birt.core.archive.compound.v3.Ext2File;

public class ArchiveEntryV3 extends ArchiveEntry {

	protected ArchiveFileV3 archive;
	protected Ext2File file;

	ArchiveEntryV3(ArchiveFileV3 archive, Ext2File file) {
		super(file.getName());
		this.archive = archive;
		this.file = file;
		this.archive.openEntry(this);
	}

	public long getLength() throws IOException {
		return file.length();
	}

	public void close() throws IOException {
		archive.closeEntry(this);
		file.close();
	}

	@Override
	public int read(long pos, byte[] b, int off, int len) throws IOException {
		file.seek(pos);
		return file.read(b, off, len);
	}

	@Override
	public void setLength(long length) throws IOException {
		file.setLength(length);
	}

	@Override
	public void write(long pos, byte[] b, int off, int len) throws IOException {
		file.seek(pos);
		file.write(b, off, len);
	}
}
