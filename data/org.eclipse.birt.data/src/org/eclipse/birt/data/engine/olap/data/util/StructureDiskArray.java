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

package org.eclipse.birt.data.engine.olap.data.util;

import java.io.IOException;
import java.lang.reflect.Array;

/**
 * A List class providing the service of reading/writing objects from one file
 * when cache is not enough . It makes the reading/writing objects transparent.
 */

public class StructureDiskArray extends BaseDiskArray {

	private IStructureCreator creator;
	private ObjectWriter[] fieldWriters;
	private ObjectReader[] fieldReaders;

	/**
	 * @throws IOException
	 *
	 *
	 */
	public StructureDiskArray(IStructureCreator creator) throws IOException {
		super();
		this.creator = creator;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.cache.BasicCachedList#writeObject(java.io.
	 * DataOutputStream, java.lang.Object)
	 */
	@Override
	protected void writeObject(Object object) throws IOException {
		if (object == null) {
			getRandomAccessFile().writeShort(NULL_VALUE);
			return;
		}
		IStructure cachedObject = (IStructure) object;
		Object[] objects = cachedObject.getFieldValues();
		getRandomAccessFile().writeShort((short) objects.length);
		if (fieldWriters == null || fieldWriters.length < objects.length) {
			createReadersAndWriters(objects.length);
		}
		for (int i = 0; i < objects.length; i++) {
			if (i >= fieldWriters.length) {
				fieldWriters[fieldWriters.length - 1].write(getRandomAccessFile(), objects[i]);
			} else {
				fieldWriters[i].write(getRandomAccessFile(), objects[i]);
			}
		}
	}

	/**
	 *
	 * @param size
	 */
	private void createReadersAndWriters(int size) {
		if (fieldWriters == null && fieldReaders == null) {
			fieldWriters = new ObjectWriter[size];
			fieldReaders = new ObjectReader[size];
			for (int i = 0; i < size; i++) {
				fieldWriters[i] = new ObjectWriter();
				fieldReaders[i] = new ObjectReader();
			}
		} else {
			int i = fieldWriters.length;
			fieldReaders = copyOf(fieldReaders, size);
			fieldWriters = copyOf(fieldWriters, size);
			for (; i < fieldReaders.length; i++) {
				fieldWriters[i] = new ObjectWriter();
				fieldReaders[i] = new ObjectReader();
			}
		}
	}

	public static <T, U> T[] copyOf(U[] original, int newLength) {
		T[] copy = ((Object) original.getClass() == (Object) Object[].class) ? (T[]) new Object[newLength]
				: (T[]) Array.newInstance(original.getClass().getComponentType(), newLength);
		System.arraycopy(original, 0, copy, 0, Math.min(original.length, newLength));
		return copy;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.cache.BasicCachedList#readObject(java.io.
	 * DataInputStream)
	 */
	@Override
	protected Object readObject() throws IOException {
		short fieldCount = getRandomAccessFile().readShort();
		if (fieldCount == NULL_VALUE) {
			return null;
		}
		Object[] objects = new Object[fieldCount];
		for (int i = 0; i < objects.length; i++) {
			if (i < fieldReaders.length && fieldReaders[i].getDataType() != fieldWriters[i].getDataType()) {
				fieldReaders[i].setDataType(fieldWriters[i].getDataType());
			}
			if (i >= fieldReaders.length) {
				objects[i] = fieldReaders[fieldReaders.length - 1].read(getRandomAccessFile());
			} else {
				objects[i] = fieldReaders[i].read(getRandomAccessFile());
			}
		}
		return creator.createInstance(objects);
	}

	@Override
	public void clear() throws IOException {
		fieldWriters = null;
		fieldReaders = null;
		super.clear();
	}

}
