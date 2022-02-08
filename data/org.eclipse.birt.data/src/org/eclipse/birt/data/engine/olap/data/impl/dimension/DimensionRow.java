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

import java.util.ArrayList;
import java.util.logging.Logger;

import org.eclipse.birt.data.engine.olap.data.util.CompareUtil;
import org.eclipse.birt.data.engine.olap.data.util.IComparableStructure;
import org.eclipse.birt.data.engine.olap.data.util.IStructure;
import org.eclipse.birt.data.engine.olap.data.util.IStructureCreator;

/**
 * 
 */

public class DimensionRow implements IComparableStructure {
	private static IStructureCreator creator = null;

	private Member[] members;

	private static Logger logger = Logger.getLogger(DimensionRow.class.getName());

	public DimensionRow(Member[] members) {
		logger.entering(DimensionRow.class.getName(), "DimensionRow", members);
		this.members = members;
		logger.exiting(DimensionRow.class.getName(), "DimensionRow");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.olap.data.util.IStructure#getFieldValues()
	 */
	public Object[] getFieldValues() {
		ArrayList fieldList = new ArrayList();

		fieldList.add(Integer.valueOf(members.length));
		for (int i = 0; i < members.length; i++) {
			Object[] fieldValues = members[i].getFieldValues();
			fieldList.add(Integer.valueOf(fieldValues.length));
			for (int j = 0; j < fieldValues.length; j++) {
				fieldList.add(fieldValues[j]);
			}
		}

		return fieldList.toArray();
	}

	/**
	 * 
	 * @return
	 */
	public static IStructureCreator getCreator() {
		if (creator == null)
			creator = new DimesionMemberCreator();
		return creator;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object o) {
		assert o != null;
		assert o instanceof DimensionRow;
		int result = 0;
		DimensionRow other = (DimensionRow) o;
		if (members.length == 0)
			return 0;
		return CompareUtil.compare(members[members.length - 1].getKeyValues(),
				other.members[members.length - 1].getKeyValues());
	}

	/**
	 * 
	 * @return
	 */
	public Member[] getMembers() {
		return members;
	}

	/**
	 * 
	 * @param members
	 */
	public void setMembers(Member[] members) {
		this.members = members;
	}

}

/**
 * 
 * @author Administrator
 *
 */
class DimesionMemberCreator implements IStructureCreator {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.olap.data.util.IStructureCreator#createInstance(java.
	 * lang.Object[])
	 */
	public IStructure createInstance(Object[] fields) {
		Member[] levelMembers = new Member[((Integer) fields[0]).intValue()];

		int pointer = 1;
		for (int i = 0; i < levelMembers.length; i++) {
			Object[] tempObjs = new Object[((Integer) fields[pointer]).intValue()];
			pointer++;
			System.arraycopy(fields, pointer, tempObjs, 0, tempObjs.length);
			levelMembers[i] = (Member) Member.getCreator().createInstance(tempObjs);
			pointer += tempObjs.length;
		}

		return new DimensionRow(levelMembers);
	}
}
