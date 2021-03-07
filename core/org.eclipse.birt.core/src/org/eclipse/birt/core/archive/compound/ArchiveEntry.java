/*******************************************************************************
 * Copyright (c) 2004,2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.core.archive.compound;

import java.io.IOException;

import org.eclipse.birt.core.archive.RAOutputStream;

/**
 * the user must close the archive
 */
abstract public class ArchiveEntry {

	protected String name;
	protected RAOutputStream output;

	public ArchiveEntry(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	protected void setOutputStream(RAOutputStream output) {
		if (output != null && this.output != null) {
			throw new IllegalStateException();
		}
		this.output = output;
	}

	abstract public long getLength() throws IOException;

	abstract public void setLength(long length) throws IOException;

	public void flush() throws IOException {
		if (output != null) {
			output.flush();
		}
	}

	public void refresh() throws IOException {
	}

	abstract public int read(long pos, byte[] b, int off, int len) throws IOException;

	abstract public void write(long pos, byte[] b, int off, int len) throws IOException;

	abstract public void close() throws IOException;
}