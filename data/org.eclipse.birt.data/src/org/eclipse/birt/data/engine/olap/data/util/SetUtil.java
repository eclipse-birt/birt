
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

import org.eclipse.birt.data.engine.cache.Constants;

/**
 * A utility class which provide several set functions.
 */

public class SetUtil {
	/**
	 * Get intersection from stacks.
	 * 
	 * @param stacks
	 * @return
	 * @throws IOException
	 */
	public static IDiskArray getIntersection(PrimitiveDiskSortedStack[] stacks) throws IOException {
		return getIntersection(stacks, Constants.MAX_LIST_BUFFER_SIZE);
	}

	/**
	 * Get intersection from stacks.
	 * 
	 * @param stacks
	 * @return
	 * @throws IOException
	 */
	public static IDiskArray getIntersection(PrimitiveDiskSortedStack[] stacks, int bufferSize) throws IOException {
		if (stacks == null || stacks.length < 1) {
			bufferSize = 0;
		}
		int i = 0;
		for (i = 0; i < stacks.length; i++) {
			if (stacks[i].size() < bufferSize) {
				bufferSize = stacks[i].size();
			}
		}
		IDiskArray result = new BufferedPrimitiveDiskArray(bufferSize + 1);
		Object[] tmpObjects = new Object[stacks.length];

		Object currentObject = null;

		for (i = 0; i < tmpObjects.length; i++) {
			tmpObjects[i] = stacks[i].pop();
			if (tmpObjects[i] == null) {
				return result;
			}
		}
		currentObject = tmpObjects[0];

		while (true) {
			for (i = 0; i < tmpObjects.length; i++) {
				while (((Comparable) tmpObjects[i]).compareTo(currentObject) < 0) {
					tmpObjects[i] = stacks[i].pop();
					if (tmpObjects[i] == null) {
						return result;
					}
				}
				if (((Comparable) tmpObjects[i]).compareTo(currentObject) > 0) {
					break;
				}
			}
			if (i == tmpObjects.length) {
				i--;
			}
			if (((Comparable) tmpObjects[i]).compareTo(currentObject) > 0) {
				currentObject = tmpObjects[i];
				continue;
			}
			result.add(currentObject);
			tmpObjects[0] = stacks[0].pop();
			if (tmpObjects[0] == null) {
				return result;
			}
			currentObject = tmpObjects[0];
		}
	}

	/**
	 * Get intersection from disk arrays.
	 * 
	 * @param stacks
	 * @return
	 * @throws IOException
	 */
	public static IDiskArray getIntersection(IDiskArray[] arrays) throws IOException {
		PrimitiveDiskSortedStack[] stacks = new PrimitiveDiskSortedStack[arrays.length];
		for (int i = 0; i < arrays.length; i++) {
			stacks[i] = new PrimitiveDiskSortedStack(Math.min(arrays[i].size(), Constants.MAX_LIST_BUFFER_SIZE), true,
					true);
			if (arrays[i] == null || arrays[i].size() == 0) {
				return null;
			}
			for (int j = 0; j < arrays[i].size(); j++) {
				stacks[i].push(arrays[i].get(j));
			}
		}
		return getIntersection(stacks);
	}

	/**
	 * get the intersection from two disk arrays which have been sorted.
	 * 
	 * @param array1
	 * @param array2
	 * @return
	 * @throws IOException
	 */
	public static IDiskArray getIntersection(IDiskArray array1, IDiskArray array2) throws IOException {
		IDiskArray result = new BufferedPrimitiveDiskArray(
				min(array1.size(), array2.size(), Constants.LIST_BUFFER_SIZE));
		int i = 0, j = 0;
		while (i < array1.size() && j < array2.size()) {
			Comparable key1 = (Comparable) array1.get(i);
			Comparable key2 = (Comparable) array2.get(j);
			int ret = key1.compareTo(key2);
			if (ret == 0) {
				result.add(key1);
				i++;
				j++;
			} else if (ret < 0) {
				i++;
			} else
				j++;
		}
		array1.close();
		array2.close();
		return result;
	}

	/**
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @return
	 */
	private static int min(int a, int b, int c) {
		int min = a;
		if (b < min)
			min = b;
		if (c < min)
			min = c;
		return min;
	}
}
