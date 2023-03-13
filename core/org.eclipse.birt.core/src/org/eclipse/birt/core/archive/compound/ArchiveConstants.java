/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
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

interface ArchiveConstants {

	/** The magic tag of the archive file: "RPTDOC" */
	long DOCUMENT_TAG = 0x525054414243L;

	/**
	 * After support system id in archive file, DOCUMENT_VERSION_1 is writen into
	 * header.
	 */
	long DOCUMENT_VERSION_0 = 0L;
	long DOCUMENT_VERSION_1 = 1L;

	/**
	 * the system property defines the block size, it should be times of 1024.
	 */
	String PROPERTY_DEFAULT_BLOCK_SIZE = "org.eclipse.birt.core.archive.compound.DEFAULT_BLOCK_SIZE";

	/**
	 * Size of a physical block, counted in bytes.
	 */
	int DEFAULT_BLOCK_SIZE = 4096;

	/**
	 * the block of archive header
	 */
	int HEADER_BLOCK = 0;

	/**
	 * the first block of allocation table
	 */
	int ALLOC_TABLE_BLOCK = 1;

	/**
	 * the first block of entry table
	 */
	int ENTRY_TABLE_BLOCK = 2;

	/**
	 * the size of a entry in the entry table, counted in bytes
	 */
	int ENTRY_ITEM_SIZE = 128;

	/** File status value: finished */
	int FILE_STATUS_FINISHED = -1;

	/** default size of buffer pool - number of buffer blocks in buffer pool */
	int DEFAULT_BUFFER_POOL_SIZE = 1024;

	/** Min size of buffer pool - number of buffer blocks in buffer pool */
	int MIN_BUFFER_POOL_SIZE = 2;

}
