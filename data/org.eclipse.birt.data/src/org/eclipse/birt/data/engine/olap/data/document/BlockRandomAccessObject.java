
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
package org.eclipse.birt.data.engine.olap.data.document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.data.engine.olap.data.util.BufferedRandomAccessFile;

/**
 * An implementation of the <tt>IRandomAccessObject</tt> interface. The instance
 * of this class allcoates some blocks from a BufferedRandomAccessFile and use
 * these blocks to save data.
 */

public class BlockRandomAccessObject implements IRandomAccessObject {
	private BufferedRandomAccessFile dataFile;
	private String name;
	private long length;
	private IObjectAllocTable documentObjectAllocatedTable;
	private long position;
	private List blockList = null;

	public BlockRandomAccessObject(BufferedRandomAccessFile dataFile, String name, int firstBlock, long length,
			IObjectAllocTable documentObjectAllocatedTable) throws IOException {
		this.name = name;
		this.dataFile = dataFile;
		this.length = length;
		this.documentObjectAllocatedTable = documentObjectAllocatedTable;
		position = 0;
		blockList = new ArrayList();
		int blockNumber = firstBlock;
		do {
			blockList.add(Integer.valueOf(blockNumber));
			blockNumber = documentObjectAllocatedTable.getNextBlock(blockNumber);
		} while (blockNumber > 0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.olap.data.document.IRandomAccessObject#close()
	 */
	public void close() throws IOException {
		seek(0);
		documentObjectAllocatedTable.setObjectLength(name, length);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.olap.data.document.IRandomAccessObject#getFilePointer()
	 */
	public long getFilePointer() throws IOException {
		return position;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.olap.data.document.IRandomAccessObject#length()
	 */
	public long length() throws IOException {
		return length;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.olap.data.document.IRandomAccessObject#read(byte[],
	 * int, int)
	 */
	public int read(byte[] b, int off, int len) throws IOException {
		if (len == 0) {
			return 0;
		}
		if (position >= length) {
			return -1;
		}
		int remainSizeOfCurrentBlock = FileDocumentManager.BLOCK_SIZE
				- (int) (position % FileDocumentManager.BLOCK_SIZE);
		dataFileSeek();
		if (remainSizeOfCurrentBlock >= len || position + remainSizeOfCurrentBlock >= length) {
			int readSize = (int) Math.min(length - position, len);
			position += readSize;
			return dataFile.read(b, off, readSize);
		} else {
			dataFile.read(b, off, remainSizeOfCurrentBlock);
			position += remainSizeOfCurrentBlock;
			int readSize = read(b, off + remainSizeOfCurrentBlock, len - remainSizeOfCurrentBlock);
			if (readSize == -1) {
				return remainSizeOfCurrentBlock;
			} else {
				return remainSizeOfCurrentBlock + readSize;
			}
		}
	}

	/*
	 * 
	 */
	private void dataFileSeek() throws IOException {
		int blockIndex = (int) (position / FileDocumentManager.BLOCK_SIZE);
		int posInBlock = (int) (position % FileDocumentManager.BLOCK_SIZE);

		long dataFilePosition = (long) (((Integer) blockList.get(blockIndex)).intValue())
				* FileDocumentManager.BLOCK_SIZE + posInBlock;
		dataFile.seek(dataFilePosition);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.olap.data.document.IRandomAccessObject#read(byte[])
	 */
	public int read(byte[] b) throws IOException {
		return read(b, 0, b.length);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.olap.data.document.IRandomAccessObject#seek(long)
	 */
	public void seek(long pos) throws IOException {
		this.position = pos;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.olap.data.document.IRandomAccessObject#setLength(long)
	 */
	public void setLength(long newLength) throws IOException {
		assert newLength > 0;
		int needBlockCount = (int) ((newLength - 1) / FileDocumentManager.BLOCK_SIZE) + 1;
		if (needBlockCount > blockList.size()) {
			int lastBlockNumber = ((Integer) blockList.get(blockList.size() - 1)).intValue();
			int moreBlockCount = needBlockCount - blockList.size();
			for (int i = 0; i < moreBlockCount; i++) {
				lastBlockNumber = documentObjectAllocatedTable.allocateBlock(lastBlockNumber);
				blockList.add(Integer.valueOf(lastBlockNumber));
			}
		}
		length = newLength;
		documentObjectAllocatedTable.setObjectLength(name, length);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.olap.data.document.IRandomAccessObject#write(byte[],
	 * int, int)
	 */
	public void write(byte[] b, int off, int len) throws IOException {
		if (len == 0) {
			return;
		}
		if (position + len > length) {
			setLength(position + len);
		}
		int remainSizeOfCurrentBlock = FileDocumentManager.BLOCK_SIZE
				- (int) (position % FileDocumentManager.BLOCK_SIZE);
		dataFileSeek();
		if (remainSizeOfCurrentBlock >= len || position + remainSizeOfCurrentBlock >= length) {
			len = (int) Math.min(length - position, len);
			position += len;
			dataFile.write(b, off, len);
		} else {
			dataFile.write(b, off, remainSizeOfCurrentBlock);
			position += remainSizeOfCurrentBlock;
			write(b, off + remainSizeOfCurrentBlock, len - remainSizeOfCurrentBlock);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.olap.data.document.IRandomAccessObject#flush()
	 */
	public void flush() throws IOException {
		seek(0);
		documentObjectAllocatedTable.setObjectLength(name, length);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.olap.data.document.IRandomAccessObject#read()
	 */
	public int read() throws IOException {
		byte[] b = new byte[1];
		int len = read(b);
		if (len < 0) {
			return -1;
		}
		return b[0] & 0xff;
	}

}
