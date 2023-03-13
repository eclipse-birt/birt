
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

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.core.security.FileSecurity;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.olap.data.util.BufferedRandomAccessFile;

/**
 * An implementation of the <tt>IDocumentManager</tt> interface. This class use
 * three files to save any number of document objects.
 */

public class FileDocumentManager implements IDocumentManager, IObjectAllocTable {
	private int dataFileCacheSize = 0;
	private BufferedRandomAccessFile objectAccessFile = null;
	private BufferedRandomAccessFile oatAccessFile = null;
	private BufferedRandomAccessFile dataAccessFile = null;
	private File objectFile = null;
	private File oatFile = null;
	private File dataFile = null;
	private HashMap documentObjectMap = null;

	/**
	 *
	 * @param dirName
	 * @param managerName
	 * @return
	 * @throws DataException
	 * @throws IOException
	 */
	static FileDocumentManager createManager(String dirName, String managerName) throws DataException, IOException {
		return createManager(dirName, managerName, 0);
	}

	/**
	 *
	 * @param dirName
	 * @param managerName
	 * @return
	 * @throws DataException
	 * @throws IOException
	 */
	static FileDocumentManager createManager(String dirName, String managerName, int cacheSize)
			throws DataException, IOException {
		File tmpDir = new File(dirName);
		if (!FileSecurity.fileExist(tmpDir) || !FileSecurity.fileIsDirectory(tmpDir)) {
			FileSecurity.fileMakeDirs(tmpDir);
		}
		FileDocumentManager manager = new FileDocumentManager(cacheSize);
		manager.create(dirName, managerName);
		return manager;
	}

	/**
	 *
	 * @param dirName
	 * @param managerName
	 * @return
	 * @throws DataException
	 * @throws IOException
	 */
	static FileDocumentManager loadManager(String dirName, String managerName) throws DataException, IOException {
		FileDocumentManager manager = new FileDocumentManager();
		manager.load(dirName, managerName);
		return manager;
	}

	/**
	 *
	 */
	private FileDocumentManager() {
		this.dataFileCacheSize = 0;
	}

	/**
	 *
	 * @param fileCacheSize
	 */
	private FileDocumentManager(int fileCacheSize) {
		this.dataFileCacheSize = fileCacheSize * 1024 * 1024;
	}

	/**
	 *
	 * @param dirName
	 * @param managerName
	 * @throws IOException
	 * @throws DataException
	 */
	private void create(String dirName, String managerName) throws IOException, DataException {
		documentObjectMap = new HashMap();

		objectFile = new File(dirName + File.separatorChar + managerName + "obj");
		objectAccessFile = new BufferedRandomAccessFile(objectFile, "rw", 1024, dataFileCacheSize / 5);
		objectAccessFile.setLength(0);
		oatFile = new File(dirName + File.separatorChar + managerName + "Oat");
		oatAccessFile = new BufferedRandomAccessFile(oatFile, "rw", 1024, dataFileCacheSize / 10);
		oatAccessFile.setLength(0);
		dataFile = new File(dirName + File.separatorChar + managerName + "data");
		dataAccessFile = new BufferedRandomAccessFile(dataFile, "rw", 1024, dataFileCacheSize);
		dataAccessFile.setLength(0);
	}

	/**
	 *
	 * @param dirName
	 * @param managerName
	 * @throws IOException
	 * @throws DataException
	 */
	private void load(String dirName, String managerName) throws IOException, DataException {
		documentObjectMap = new HashMap();

		File file = new File(dirName + File.separatorChar + managerName + "obj");
		objectAccessFile = new BufferedRandomAccessFile(file, "rw", 1024, dataFileCacheSize / 5);
		if (!FileSecurity.fileExist(file)) {
			throw new DataException(ResourceConstants.OLAPFILE_NOT_FOUND, file.getAbsolutePath());
		}

		file = new File(dirName + File.separatorChar + managerName + "Oat");
		if (!FileSecurity.fileExist(file)) {
			throw new DataException(ResourceConstants.OLAPFILE_NOT_FOUND, file.getAbsolutePath());
		}
		oatAccessFile = new BufferedRandomAccessFile(file, "rw", 1024, dataFileCacheSize / 10);

		file = new File(dirName + File.separatorChar + managerName + "data");
		if (!FileSecurity.fileExist(file)) {
			throw new DataException(ResourceConstants.OLAPFILE_NOT_FOUND, file.getAbsolutePath());
		}
		dataAccessFile = new BufferedRandomAccessFile(file, "rw", 1024, dataFileCacheSize);

		objectAccessFile.seek(0);
		while (true) {
			try {
				ObjectStructure structure = readObjectStructure();
				if (structure.firstBlock >= 0) {
					documentObjectMap.put(structure.name, structure);
				}
			} catch (EOFException e) {
				return;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.olap.data.document.IDocumentManager#close()
	 */
	@Override
	public void close() throws IOException {
		objectAccessFile.close();
		oatAccessFile.close();
		dataAccessFile.close();
		clearTmpFile();
	}

	/**
	 *
	 */
	public void clearTmpFile() {
		if (objectFile != null) {
//			FileSecurity.fileDeleteOnExit( objectFile );
			objectFile = null;
		}
		if (oatFile != null) {
//			FileSecurity.fileDeleteOnExit( oatFile );
			oatFile = null;
		}
		if (dataFile != null) {
//			FileSecurity.fileDeleteOnExit( dataFile );
			dataFile = null;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.olap.data.document.IDocumentManager#
	 * createDocumentObject(java.lang.String)
	 */
	@Override
	public IDocumentObject createDocumentObject(String documentObjectName) throws IOException {
		ObjectStructure objectStructure = new ObjectStructure();
		objectStructure.name = documentObjectName;
		objectStructure.firstBlock = findFreeBlock();
		objectStructure.length = 0;
		writeObjectStructure(objectStructure);
		this.documentObjectMap.put(objectStructure.name, objectStructure);
		return new DocumentObject(new BufferedRandomDataAccessObject(new BlockRandomAccessObject(dataAccessFile,
				documentObjectName, objectStructure.firstBlock, objectStructure.length, this), 1024));
	}

	/**
	 *
	 * @param structure
	 * @throws IOException
	 */
	private void writeObjectStructure(ObjectStructure structure) throws IOException {
		objectAccessFile.seek(objectAccessFile.length());
		structure.fileOffset = (int) objectAccessFile.getFilePointer();
		objectAccessFile.writeLong(structure.length);
		objectAccessFile.writeInt(structure.firstBlock);
		objectAccessFile.writeUTF(structure.name);
	}

	/**
	 *
	 * @return
	 * @throws IOException
	 */
	private ObjectStructure readObjectStructure() throws IOException {
		ObjectStructure structure = new ObjectStructure();
		structure.fileOffset = (int) objectAccessFile.getFilePointer();
		structure.length = objectAccessFile.readLong();
		structure.firstBlock = objectAccessFile.readInt();
		structure.name = objectAccessFile.readUTF();
		return structure;
	}

	/**
	 *
	 * @return
	 * @throws IOException
	 */
	private int findFreeBlock() throws IOException {
		int oldLength = (int) oatAccessFile.length();
		oatAccessFile.setLength(oldLength + 4);
		return (int) (oldLength / 4);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.olap.data.document.IDocumentManager#openDocumentObject(
	 * java.lang.String)
	 */
	@Override
	public IDocumentObject openDocumentObject(String documentObjectName) throws IOException {
		ObjectStructure objectStructure = (ObjectStructure) this.documentObjectMap.get(documentObjectName);
		if (objectStructure == null) {
			return null;
		}
		return new DocumentObject(new BufferedRandomDataAccessObject(new BlockRandomAccessObject(dataAccessFile,
				documentObjectName, objectStructure.firstBlock, objectStructure.length, this), 1024));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.olap.data.document.IDocumentManager#exist(java.lang.
	 * String)
	 */
	@Override
	public boolean exist(String documentObjectName) {
		return this.documentObjectMap.get(documentObjectName) != null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.olap.data.document.IObjectAllocTable#getNextBlock(int)
	 */
	@Override
	public int getNextBlock(int blockNo) throws IOException {
		oatAccessFile.seek(blockNo * 4L);
		return oatAccessFile.readInt();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.olap.data.document.IObjectAllocTable#allocateBlock(int)
	 */
	@Override
	public int allocateBlock(int blockNo) throws IOException {
		int newBlock = findFreeBlock();
		oatAccessFile.seek(blockNo * 4L);
		oatAccessFile.writeInt(newBlock);
		return newBlock;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.olap.data.document.IObjectAllocTable#setObjectLength(
	 * java.lang.String, long)
	 */
	@Override
	public void setObjectLength(String documentObjectName, long length) throws IOException {
		ObjectStructure objectStructure = (ObjectStructure) documentObjectMap.get(documentObjectName);
		if (objectStructure == null) {
			return;
		}
		objectStructure.length = length;
		objectAccessFile.seek(objectStructure.fileOffset);
		objectAccessFile.writeLong(length);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.olap.data.document.IDocumentManager#flush()
	 */
	@Override
	public void flush() throws IOException {
		objectAccessFile.flush();
		oatAccessFile.flush();
		dataAccessFile.flush();
	}

}

class ObjectStructure {
	int fileOffset;
	long length;
	int firstBlock;
	String name;
}
