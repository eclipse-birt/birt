package org.eclipse.birt.core.archive.compound;


class ArchiveConstants
{

	/** The magic tag for Compound RDF File: "ACRPTDOC" */
	static final long DOCUMENT_TAG = 0x4143525054444f43L;

	/** The file format version */
	static final long DOCUMENT_VERSION = 0L;
	/**
	 * Size of a physical block, counted in bytes. 
	 */
	static final int BLOCK_SIZE = 4096;

	/** Size of a stream item, counted in bytes */
	static final int STREAM_ITEM_SIZE = 128;
	
	/** File status value: finished */
	static final int FILE_STATUS_FINISHED = -1;
	
	/** Max size of buffer pool - number of buffer blocks in buffer pool */
	static final int MAX_BUFFER_POOL_SIZE = 1024;

	/** Default size of buffer pool */
	static final int DEFAULT_BUFFER_POOL_SIZE = 128;

	
}
