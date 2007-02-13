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

package org.eclipse.birt.core.archive.compound;

interface ArchiveConstants
{

	/** The magic tag of the archive file: "RPTDOC" */
	static final long DOCUMENT_TAG = 0x525054414243L;

	/** The file format version */
	static final long DOCUMENT_VERSION = 0L;
	/**
	 * Size of a physical block, counted in bytes.
	 */
	static final int BLOCK_SIZE = 4096;

	/**
	 * the block of archive header
	 */
	static final int HEADER_BLOCK = 0;

	/**
	 * the first block of allocation table
	 */
	static final int ALLOC_TABLE_BLOCK = 1;

	/**
	 * the first block of entry table
	 */
	static final int ENTRY_TABLE_BLOCK = 2;

	/**
	 * the size of a entry in the entry table, counted in bytes
	 */
	static final int ENTRY_ITEM_SIZE = 128;

	/** File status value: finished */
	static final int FILE_STATUS_FINISHED = -1;

	/** Max size of buffer pool - number of buffer blocks in buffer pool */
	static final int MAX_BUFFER_POOL_SIZE = 1024;
}
