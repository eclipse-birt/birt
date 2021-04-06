/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.core.archive.compound;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

import org.eclipse.birt.core.i18n.CoreMessages;
import org.eclipse.birt.core.i18n.ResourceConstants;
import org.eclipse.birt.core.util.IOUtil;

/**
 * Contains the report archive file 's head information.
 */
class ArchiveHeader implements ArchiveConstants {
	protected static final int TAG_OFFSET = 0;
	protected static final int VERSION_OFFSET = 8;
	protected static final int STATUS_OFFSET = 16;
	protected static final int BLOCK_SIZE_OFFSET = 20;
	protected static final int HEADER_LENGTH = DEFAULT_BLOCK_SIZE;

	/**
	 * the file status of the archive
	 */
	protected int fileStatus;

	protected int blockSize;

	/**
	 * the systemId of the archive file/view.
	 */
	protected String systemId;

	/**
	 * the systemId of the parent of the current archive view.
	 */
	protected String dependId;

	/**
	 * Constructor
	 * 
	 * @param fs the corresponding compound file system
	 */
	ArchiveHeader() {
		this.fileStatus = 0;
		this.blockSize = DEFAULT_BLOCK_SIZE;
	}

	ArchiveHeader(int blockSize) {
		this.fileStatus = 0;
		this.blockSize = blockSize;
	}

	int getStatus() {
		return fileStatus;
	}

	void setStatus(int status) {
		this.fileStatus = status;
	}

	int getBlockSize() {
		return this.blockSize;
	}

	static ArchiveHeader read(RandomAccessFile rf) throws IOException {
		ArchiveHeader header = new ArchiveHeader();

		byte[] b = new byte[HEADER_LENGTH];
		rf.seek(0);
		rf.read(b);
		DataInputStream in = new DataInputStream(new ByteArrayInputStream(b));
		long magicTag = in.readLong();
		if (magicTag != DOCUMENT_TAG) {
			throw new IOException(
					CoreMessages.getFormattedString(ResourceConstants.NOT_COMPOUND_FILE, new Object[] { magicTag }));
		}
		long version = in.readLong();
		if (version != DOCUMENT_VERSION_0 && version != DOCUMENT_VERSION_1) {
			throw new IOException(CoreMessages.getFormattedString(ResourceConstants.UNSUPPORTED_ARCHIVE_VERSION,
					new Object[] { version }));
		}

		header.fileStatus = in.readInt();
		header.blockSize = in.readInt();
		if (header.blockSize == 0) {
			header.blockSize = DEFAULT_BLOCK_SIZE;
		}
		if (version == DOCUMENT_VERSION_1) {
			header.systemId = IOUtil.readString(in);
			header.dependId = IOUtil.readString(in);
		}
		return header;
	}

	/**
	 * Read the header information from disk.
	 * 
	 * @throws IOException
	 */
	void refresh(ArchiveFileV2 af) throws IOException {
		byte[] b = new byte[4];
		af.read(0, STATUS_OFFSET, b, 0, 4);
		fileStatus = IOUtil.bytesToInteger(b);
	}

	/**
	 * Write header information to disk
	 * 
	 * @throws IOException
	 */
	void flush(ArchiveFileV2 af) throws IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(buffer);
		out.writeLong(DOCUMENT_TAG);
		out.writeLong(DOCUMENT_VERSION_1);
		out.writeInt(fileStatus);
		out.writeInt(blockSize);
		IOUtil.writeString(out, af.systemId);
		IOUtil.writeString(out, af.dependId);

		byte[] b = buffer.toByteArray();
		if (b.length > DEFAULT_BLOCK_SIZE) {
			throw new IOException(CoreMessages.getFormattedString(ResourceConstants.EXCEED_MAX_BLOCK_SIZE,
					new Object[] { DEFAULT_BLOCK_SIZE }));
		}
		af.write(0, 0, b, 0, b.length);
	}
}
