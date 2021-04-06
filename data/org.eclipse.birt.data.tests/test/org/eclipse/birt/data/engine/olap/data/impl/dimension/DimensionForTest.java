
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.olap.data.impl.dimension;

import java.util.Date;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.olap.data.api.cube.IDatasetIterator;
import org.eclipse.birt.data.engine.olap.data.util.DataType;

/**
 * 
 */

public class DimensionForTest implements IDatasetIterator {
	private int levelCout;
	private String[] levelNames;
	private Object[][] member = null;
	private int ptr = -1;

	public DimensionForTest(String[] levelNames) {
		this.levelCout = levelNames.length;
		this.levelNames = levelNames;
		member = new Object[levelCout][];
	}

	public void setLevelMember(int level, Object[] members) {
		member[level] = members;
	}

	public void setLevelMember(int level, int[] members) {
		member[level] = new Object[members.length];
		for (int i = 0; i < member[level].length; i++) {
			member[level][i] = new Integer(members[i]);
		}

	}

	public void close() throws BirtException {
		// TODO Auto-generated method stub

	}

	public Boolean getBoolean(int fieldIndex) throws BirtException {
		// TODO Auto-generated method stub
		return null;
	}

	public Date getDate(int fieldIndex) throws BirtException {
		// TODO Auto-generated method stub
		return null;
	}

	public Double getDouble(int fieldIndex) throws BirtException {
		// TODO Auto-generated method stub
		return null;
	}

	public int getFieldIndex(String name) throws BirtException {
		for (int i = 0; i < levelNames.length; i++) {
			if (levelNames[i].equals(name)) {
				return i;
			}
		}
		return -1;
	}

	public int getFieldType(String name) throws BirtException {
		int fieldIndex = getFieldIndex(name);
		return DataType.getDataType(member[fieldIndex][0].getClass());
	}

	public Integer getInteger(int fieldIndex) throws BirtException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getString(int fieldIndex) throws BirtException {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getValue(int fieldIndex) throws BirtException {
		return this.member[fieldIndex][ptr];
	}

	public void first() {
		ptr = -1;
	}

	public boolean next() throws BirtException {
		ptr++;
		if (ptr < this.member[0].length)
			return true;
		else
			return false;
	}

}
