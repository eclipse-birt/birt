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

import java.util.Comparator;

/**
 *
 */

public class IndexKey implements IComparableStructure {

	private Object[] key;
	private int[] offset;
	private int[] dimensionPos;

	public IndexKey() {

	}

	@Override
	public Object[] getFieldValues() {
		Object[][] objectArrays = new Object[3][];
		objectArrays[0] = key;
		objectArrays[1] = new Integer[offset.length];
		for (int i = 0; i < offset.length; i++) {
			objectArrays[1][i] = Integer.valueOf(offset[i]);
		}
		objectArrays[2] = new Integer[dimensionPos.length];
		for (int i = 0; i < dimensionPos.length; i++) {
			objectArrays[2][i] = Integer.valueOf(dimensionPos[i]);
		}

		return ObjectArrayUtil.convert(objectArrays);
	}

	@Override
	public int compareTo(Object o) {
		assert o instanceof IndexKey;
		IndexKey target = (IndexKey) o;

		for (int i = 0; i < getKey().length; i++) {
			if (getKey()[i] == null && target.getKey()[i] != null) {
				return -1;
			}
			if (getKey()[i] == null && target.getKey()[i] == null) {
				return 0;
			}
			if (getKey()[i] != null && target.getKey()[i] == null) {
				return 1;
			}
			int result = 0;
			if (getKey()[i] instanceof Comparable) {
				result = ((Comparable) getKey()[i]).compareTo(target.getKey()[i]);
			} else {
				result = (getKey()[i].toString()).compareTo(target.getKey()[i].toString());
			}
			if (result != 0) {
				return result;
			}
		}
		return 0;
	}

	public static Comparator getKeyComparator() {
		return new Comparator() {

			@Override
			public int compare(Object obj1, Object obj2) {
				return ((IndexKey) obj1).compareTo(obj2);
			}
		};
	}

	public static Comparator getIndexComparator() {
		return new Comparator() {

			@Override
			public int compare(Object obj1, Object obj2) {
				int[] index1 = ((IndexKey) obj1).getDimensionPos();
				int[] index2 = ((IndexKey) obj2).getDimensionPos();
				for (int i = 0; i < Math.min(index1.length, index2.length); i++) {
					if (index1[i] < index2[i]) {
						return -1;
					} else if (index1[i] > index2[i]) {
						return 1;
					}
				}
				if (index1.length < index2.length) {
					return -1;
				}
				if (index1.length == index2.length) {
					return 0;
				}
				return 1;
			}
		};
	}

	public static IStructureCreator getCreator() {
		return new IndexKeyObjectCreator();
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(Object[] key) {
		this.key = key;
	}

	/**
	 * @return the key
	 */
	public Object[] getKey() {
		return key;
	}

	/**
	 * @param offset the offset to set
	 */
	public void setOffset(int[] offset) {
		this.offset = offset;
	}

	/**
	 * @return the offset
	 */
	public int[] getOffset() {
		return offset;
	}

	/**
	 * @param dimensionPos the dimensionPos to set
	 */
	public void setDimensionPos(int dimensionPos[]) {
		this.dimensionPos = dimensionPos;
	}

	/**
	 * @return the dimensionPos
	 */
	public int[] getDimensionPos() {
		return dimensionPos;
	}

}

class IndexKeyObjectCreator implements IStructureCreator {

	@Override
	public IStructure createInstance(Object[] fields) {
		Object[][] objectArrays = ObjectArrayUtil.convert(fields);
		IndexKey obj = new IndexKey();
		obj.setKey(objectArrays[0]);
		int[] offset = new int[objectArrays[1].length];
		for (int i = 0; i < offset.length; i++) {
			offset[i] = ((Integer) objectArrays[1][i]).intValue();
		}
		obj.setOffset(offset);
		int[] dimensionPos = new int[objectArrays[2].length];
		for (int i = 0; i < dimensionPos.length; i++) {
			dimensionPos[i] = ((Integer) objectArrays[2][i]).intValue();
		}
		obj.setDimensionPos(dimensionPos);

		return obj;
	}
}
