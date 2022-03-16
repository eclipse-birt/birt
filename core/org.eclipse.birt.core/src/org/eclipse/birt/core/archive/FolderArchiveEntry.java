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
package org.eclipse.birt.core.archive;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;

import org.eclipse.birt.core.archive.compound.ArchiveEntry;

public class FolderArchiveEntry extends ArchiveEntry {
	protected File file;
	RAInputStream in;
	RAOutputStream out;

	public FolderArchiveEntry(String name, File file, HashSet<RAFolderInputStream> inputs,
			HashSet<RAFolderOutputStream> outputs) throws IOException {
		super(name);
		this.file = file;
		this.out = new RAFolderOutputStream(outputs, file, true);
		this.in = new RAFolderInputStream(inputs, file);

	}

	@Override
	public long getLength() throws IOException {
		return file.length();
	}

	@Override
	public void setLength(long length) throws IOException {

	}

	@Override
	public int read(long pos, byte[] b, int off, int len) throws IOException {
		in.seek(pos);
		return in.read(b, off, len);
	}

	@Override
	public void write(long pos, byte[] b, int off, int len) throws IOException {
		out.seek(pos);
		out.write(b, off, len);
		out.flush();
	}

	@Override
	public void close() throws IOException {
		if (in != null) {
			in.close();
			in = null;
		}
		if (out != null) {
			out.close();
			out = null;
		}
	}

}
