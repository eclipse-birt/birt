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

import org.eclipse.birt.core.archive.RAOutputStream;

public class ArchiveEntryAdapter extends ArchiveEntry {

	protected ArchiveEntry entry;

	public ArchiveEntryAdapter(String name, ArchiveEntry entry) {
		super(name);
		this.entry = entry;
	}

	@Override
	public long getLength() throws IOException {
		return entry.getLength();
	}

	@Override
	public void setLength(long length) throws IOException {
		entry.setLength(length);
	}

	@Override
	public void flush() throws IOException {
		entry.flush();
	}

	@Override
	public void refresh() throws IOException {
		entry.refresh();
	}

	@Override
	public int read(long offset, byte[] b, int off, int size) throws IOException {
		return entry.read(offset, b, off, size);
	}

	@Override
	public void write(long offset, byte[] b, int off, int size) throws IOException {
		entry.write(offset, b, off, size);
	}

	@Override
	public void close() throws IOException {
		entry.close();
	}

	protected void setOutputStream(RAOutputStream output) {
		entry.setOutputStream(output);
	}
}
