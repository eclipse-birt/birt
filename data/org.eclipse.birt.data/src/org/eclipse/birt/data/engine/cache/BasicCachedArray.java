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

import org.eclipse.birt.data.engine.impl.DataEngineSession;

/**
 * A array class providing the service of reading/writing objects from one file
 * when cache is not enough . It makes the reading/writing objects transparent.
 */

public class BasicCachedArray {

	BasicCachedList cachedList = null;
	int initialCapacity;

	/**
	 * 
	 * @param initialCapacity
	 */
	public BasicCachedArray(String tempDir, int initialCapacity) {
		if (initialCapacity < 0)
			throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);
		this.cachedList = new BasicCachedList(tempDir, DataEngineSession.getCurrentClassLoader());
		this.initialCapacity = initialCapacity;
	}

	/**
	 * 
	 * @param index
	 * @return
	 */
	public Object get(int index) {
		RangeCheck(index);
		if (cachedList.size() <= index) {
			resize(index + 1);
		}
		return cachedList.get(index);
	}

	/**
	 * 
	 * @param index
	 * @param element
	 */
	public void set(int index, Object element) {
		RangeCheck(index);
		if (cachedList.size() > index) {
			cachedList.set(index, element);
		} else if (cachedList.size() == index) {
			cachedList.add(element);
		} else {
			resize(index + 1);
			cachedList.set(index, element);
		}
	}

	/**
	 * 
	 * @return
	 */
	public int length() {
		return initialCapacity;
	}

	/**
	 * 
	 * @param size
	 */
	private void resize(int totalSize) {
		int oldSize = cachedList.size();
		for (int i = 0; i < totalSize - oldSize; i++) {
			cachedList.add(null);
		}
	}

	/**
	 * 
	 * @param index
	 */
	private void RangeCheck(int index) {
		if (index >= initialCapacity)
			throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + initialCapacity);
	}

}
