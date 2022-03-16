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

package org.eclipse.birt.data.engine.cache;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Logger;

import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.data.engine.api.ICloseListener;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.core.security.FileSecurity;
import org.eclipse.birt.data.engine.impl.DataEngineSession;

/**
 * A List class providing the service of reading/writing objects from one file
 * when cache is not enough . It makes the reading/writing objects transparent.
 */

public class BasicCachedList implements List, ICloseListener {
	protected static final int NULL_VALUE = Integer.MAX_VALUE;
	protected static final int OBJECT_VALUE = 1;
	protected int cacheSize = 4000;
	private static Logger logger = Logger.getLogger(BasicCachedList.class.getName());
	private static int UNIQUE_ID;

	private int currentCacheNo;
	private List currentCache;
	private String fileNamePrefix;
	private int size;

	private List fileList = new ArrayList();
	private File dir;

	private String tempDir; // should end with File.Seperator
	protected ClassLoader loader;

	/**
	 *
	 *
	 */
	public BasicCachedList(String tempDir, ClassLoader loader) {
		this.cacheSize = Constants.LIST_BUFFER_SIZE;
		this.tempDir = tempDir;
		this.currentCacheNo = 0;
		this.size = 0;
		setFileNamePrefix();
		this.currentCache = new ArrayList();
		this.loader = loader;
//		DataEngineThreadLocal.getInstance( ).getCloseListener( ).add( this );
	}

	/**
	 * populate the name prefix of cached file
	 *
	 */
	private void setFileNamePrefix() {
		this.fileNamePrefix = "CachedList_" + Long.toString(System.nanoTime()) + "_" + getID() + "_"
				+ Integer.toHexString(hashCode());
	}

	private synchronized static int getID() {
		UNIQUE_ID++;
		return UNIQUE_ID;
	}

	/**
	 *
	 * @param list
	 */
	public BasicCachedList(String tempDir, ClassLoader loader, List list) {
		this(tempDir, loader);
		if (list == null) {
			return;
		}

		for (int i = 0; i < list.size(); i++) {
			this.add(list.get(i));
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.util.List#add(java.lang.Object)
	 */
	@Override
	public boolean add(Object o) {
		if (this.currentCache.size() >= cacheSize) {
			try {
				saveToDisk();
			} catch (DataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.currentCache.clear();

			if (size % cacheSize == 0) {
				this.currentCacheNo = fileList.size();
			} else {
				try {
					this.currentCacheNo = fileList.size() - 1;
					loadFromDisk();
				} catch (DataException e) {
					// TODO Auto-generated catch block
				}
			}
		}
		this.currentCache.add(o);
		this.size++;
		return true;
	}

	/**
	 * Save the current list in memory to disk.
	 *
	 * @throws DataException
	 *
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void saveToDisk() throws DataException {
		FileOutputStream fos = null;
		DataOutputStream oos = null;
		try {
			File cacheFile = null;
			if (currentCacheNo < fileList.size()) {
				cacheFile = (File) (fileList.get(currentCacheNo));
			} else {
				cacheFile = getCacheFile(this.currentCacheNo);
				fileList.add(cacheFile);
			}

			fos = FileSecurity.createFileOutputStream(cacheFile);
			oos = new DataOutputStream(new BufferedOutputStream(fos));
			writeList(oos, currentCache);
			oos.close();
		} catch (FileNotFoundException e) {
			logger.severe(
					"Exception happened when save data to disk in CachedList. Exception message: " + e.toString());
		} catch (IOException e) {
			try {
				if (oos != null) {
					oos.close();
				}
			} catch (IOException ie) {
			}
			logger.severe(
					"Exception happened when save data to disk in CachedList. Exception message: " + e.toString());
		}

	}

	/**
	 * Write a list to disk
	 *
	 * @param oos
	 * @param list
	 * @throws IOException
	 */
	private void writeList(DataOutputStream oos, List list) throws IOException {
		// write list size
		IOUtil.writeInt(oos, list.size());
		for (int i = 0; i < list.size(); i++) {
			writeObject(oos, list.get(i));
		}
	}

	/**
	 * Write a object to disk
	 *
	 * @param oos
	 * @param object
	 * @throws IOException
	 */
	protected void writeObject(DataOutputStream oos, Object object) throws IOException {
		if (object == null) {
			IOUtil.writeInt(oos, NULL_VALUE);
			return;
		}
		IOUtil.writeInt(oos, OBJECT_VALUE);
		IOUtil.writeObject(oos, object);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.util.List#get(int)
	 */
	@Override
	public Object get(int index) {
		RangeCheck(index);
		if (index / cacheSize != this.currentCacheNo) {
			try {
				saveToDisk();
			} catch (DataException e) {
				// TODO Auto-generated catch block
			}
			this.currentCacheNo = index / cacheSize;
			try {
				loadFromDisk();
			} catch (DataException e) {
				// TODO Auto-generated catch block
			}
		}
		return this.currentCache.get(index - this.currentCacheNo * cacheSize);

	}

	/**
	 * Load the data of currect no from disk.
	 *
	 * @throws DataException
	 *
	 */
	private void loadFromDisk() throws DataException {
		FileInputStream fis = null;
		DataInputStream ois = null;
		try {
			fis = FileSecurity.createFileInputStream(getCacheFile(this.currentCacheNo));
			ois = new DataInputStream(new BufferedInputStream(fis));
			this.currentCache = readList(ois);
			ois.close();
		} catch (FileNotFoundException e) {
			logger.severe(
					"Exception happened when load data from disk in CachedList. Exception message: " + e.toString());
		} catch (IOException e) {
			try {
				if (ois != null) {
					ois.close();
				}
			} catch (IOException ie) {
			}
			logger.severe(
					"Exception happened when load data from disk in CachedList. Exception message: " + e.toString());
		}
	}

	/**
	 * Read a list from disk.
	 *
	 * @param dis
	 * @param list
	 * @return
	 * @throws IOException
	 */
	private List readList(DataInputStream dis) throws IOException {
		List reList = new ArrayList();
		int objectCount = IOUtil.readInt(dis);
		for (int i = 0; i < objectCount; i++) {
			reList.add(readObject(dis));
		}
		return reList;
	}

	/**
	 * Read one object from disk.
	 *
	 * @param oos
	 * @param object
	 * @throws IOException
	 */
	protected Object readObject(DataInputStream dis) throws IOException {
		int fieldCount = IOUtil.readInt(dis);
		if (fieldCount == NULL_VALUE) {
			return null;
		}
		return IOUtil.readObject(dis, DataEngineSession.getCurrentClassLoader());
	}

	/**
	 * Create a file for caching objects.
	 *
	 * @param cacheIndex
	 * @return
	 */
	private File getCacheFile(int cacheIndex) {
		String tempDirStr = tempDir + this.fileNamePrefix;
		if (dir == null) {
			dir = new File(tempDirStr);
			FileSecurity.fileMakeDirs(dir);
		}

		return new File(tempDirStr + File.separatorChar + cacheIndex + ".tmp");
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.executor.cache.ICachedList#size()
	 */
	@Override
	public int size() {
		return this.size;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.util.List#add(int, java.lang.Object)
	 */
	@Override
	public void add(int index, Object element) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException(
				"add( int index, Object element ) method in CacheList is not supported!");
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.util.List#addAll(java.util.Collection)
	 */
	@Override
	public boolean addAll(Collection c) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("addAll method in CacheList is not supported!");
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.util.List#addAll(int, java.util.Collection)
	 */
	@Override
	public boolean addAll(int index, Collection c) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("addAll method in CacheList is not supported!");
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.util.List#clear()
	 */
	@Override
	public void clear() {
		clearTempDir();
		this.currentCacheNo = 0;
		this.size = 0;
		setFileNamePrefix();
		this.currentCache = new ArrayList();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.util.List#contains(java.lang.Object)
	 */
	@Override
	public boolean contains(Object o) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("the contains( Object o ) method in CacheList is not supported!");
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.util.List#containsAll(java.util.Collection)
	 */
	@Override
	public boolean containsAll(Collection c) {
		throw new UnsupportedOperationException(
				"the containsAll( Collection c ) method in CacheList is not supported!");
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.util.List#indexOf(java.lang.Object)
	 */
	@Override
	public int indexOf(Object o) {
		throw new UnsupportedOperationException("the indexOf( Object o ) method in CacheList is not supported!");
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.util.List#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		return this.size == 0;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.util.List#iterator()
	 */
	@Override
	public Iterator iterator() {
		throw new UnsupportedOperationException("the iterator( ) method in CacheList is not supported!");
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.util.List#lastIndexOf(java.lang.Object)
	 */
	@Override
	public int lastIndexOf(Object o) {
		throw new UnsupportedOperationException("the lastIndexOf( Object o ) method in CacheList is not supported!");
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.util.List#listIterator()
	 */
	@Override
	public ListIterator listIterator() {
		throw new UnsupportedOperationException("the listIterator( ) method in CacheList is not supported!");
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.util.List#listIterator(int)
	 */
	@Override
	public ListIterator listIterator(int index) {
		throw new UnsupportedOperationException("the listIterator( int index ) method in CacheList is not supported!");
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.util.List#remove(int)
	 */
	@Override
	public Object remove(int index) {
		throw new UnsupportedOperationException("the remove( int index ) method in CacheList is not supported!");
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.util.List#remove(java.lang.Object)
	 */
	@Override
	public boolean remove(Object o) {
		throw new UnsupportedOperationException("the remove( Object o ) method in CacheList is not supported!");
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.util.List#removeAll(java.util.Collection)
	 */
	@Override
	public boolean removeAll(Collection c) {
		throw new UnsupportedOperationException("the removeAll( Collection c ) method in CacheList is not supported!");
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.util.List#retainAll(java.util.Collection)
	 */
	@Override
	public boolean retainAll(Collection c) {
		throw new UnsupportedOperationException("the retainAll( Collection c ) method in CacheList is not supported!");
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.util.List#set(int, java.lang.Object)
	 */
	@Override
	public Object set(int index, Object element) {
		RangeCheck(index);
		Object oldValue = get(index);
		this.currentCache.set(index - this.currentCacheNo * cacheSize, element);
		return oldValue;
	}

	/**
	 * Check if the given index is in range. If not, throw an appropriate runtime
	 * exception. This method does *not* check if the index is negative: It is
	 * always used immediately prior to an array access, which throws an
	 * ArrayIndexOutOfBoundsException if index is negative.
	 */
	private void RangeCheck(int index) {
		if (index >= size) {
			throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.util.List#subList(int, int)
	 */
	@Override
	public List subList(int fromIndex, int toIndex) {
		throw new UnsupportedOperationException(
				"the subList( int fromIndex, int toIndex ) method in CacheList is not supported!");
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.util.List#toArray()
	 */
	@Override
	public Object[] toArray() {
		throw new UnsupportedOperationException("method in CacheList is not supported!");
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.util.List#toArray(java.lang.Object[])
	 */
	@Override
	public Object[] toArray(Object[] a) {
		throw new UnsupportedOperationException("method in CacheList is not supported!");
	}

	/**
	 * Delete the temporay directory;
	 *
	 */
	public void clearTempDir() {
		for (int i = 0; i < fileList.size(); i++) {
			File file = ((File) fileList.get(i));
			if (FileSecurity.fileExist(file)) {
				FileSecurity.fileDelete(file);
			}
		}
		fileList.clear();
		if (dir != null && FileSecurity.fileExist(dir)) {
			FileSecurity.fileDelete(dir);
			dir = null;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#finalize()
	 */
//	public void finalize( )
//	{
//		clearTempDir( );
//	}

	@Override
	public void close() {
		clearTempDir();
	}
}
