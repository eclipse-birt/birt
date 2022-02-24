
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
package org.eclipse.birt.data.engine.olap.data.impl.dimension;

import org.eclipse.birt.data.engine.olap.data.util.CompareUtil;
import org.eclipse.birt.data.engine.olap.data.util.IComparableStructure;
import org.eclipse.birt.data.engine.olap.data.util.IStructure;
import org.eclipse.birt.data.engine.olap.data.util.IStructureCreator;
import org.eclipse.birt.data.engine.olap.data.util.ObjectArrayUtil;

/**
 * Describes a level member which is located at a level.
 */

public class Member implements IComparableStructure {
	private static IStructureCreator creator = null;
	private Object[] keyValues;
	private Object[] attributes;

	public Object[] getFieldValues() {
		Object[][] objects = new Object[2][];
		objects[0] = getKeyValues();
		objects[1] = getAttributes();
		return ObjectArrayUtil.convert(objects);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object o) {
		Member other = (Member) o;
		for (int i = 0; i < getKeyValues().length; i++) {
			int result = CompareUtil.compare(getKeyValues()[i], other.getKeyValues()[i]);
			if (result != 0) {
				return result;
			}
		}
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o) {
		Member other = (Member) o;
		for (int i = 0; i < getKeyValues().length; i++) {
			if (!getKeyValues()[i].equals(other.getKeyValues()[i])) {
				return false;
			}
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		int hashCode = 1;
		for (int i = 0; i < getKeyValues().length; i++) {
			hashCode = 31 * hashCode + (getKeyValues()[i] == null ? 0 : getKeyValues()[i].hashCode());
		}
		return hashCode;
	}

	public static IStructureCreator getCreator() {
		if (creator == null) {
			creator = new LevelMemberCreator();
		}
		return creator;
	}

	/**
	 * 
	 * @param keyValues
	 */
	public void setKeyValues(Object[] keyValues) {
		this.keyValues = keyValues;
	}

	/**
	 * 
	 * @return
	 */
	public Object[] getKeyValues() {
		return keyValues;
	}

	/**
	 * 
	 * @param attributes
	 */
	public void setAttributes(Object[] attributes) {
		this.attributes = attributes;
	}

	/**
	 * 
	 * @return
	 */
	public Object[] getAttributes() {
		return attributes;
	}
}

class LevelMemberCreator implements IStructureCreator {

	public IStructure createInstance(Object[] fields) {
		Member result = new Member();
		Object[][] objects = ObjectArrayUtil.convert(fields);

		result.setKeyValues(objects[0]);
		result.setAttributes(objects[1]);
		return result;
	}
}
