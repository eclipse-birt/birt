
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
package org.eclipse.birt.data.engine.olap.data.util;

import java.util.Date;

import org.eclipse.birt.data.engine.olap.data.util.IComparableStructure;
import org.eclipse.birt.data.engine.olap.data.util.IStructure;
import org.eclipse.birt.data.engine.olap.data.util.IStructureCreator;

/**
 * 
 */

public class MemberForStressTest implements IComparableStructure {
	public int iField;
	public Date dateField;
	public String stringField;

	MemberForStressTest(int iField, Date dateField, String stringField) {
		this.iField = iField;
		this.dateField = dateField;
		this.stringField = stringField;
	}

	public Object[] getFieldValues() {
		Object[] reFields = new Object[3];
		reFields[0] = new Integer(iField);
		reFields[1] = dateField;
		reFields[2] = stringField;
		return reFields;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o) {
		MemberForStressTest other = (MemberForStressTest) o;
		if (this.iField != other.iField) {
			return false;
		}
		if (!this.dateField.equals(other.dateField)) {
			return false;
		}
		if (!this.stringField.equals(other.stringField)) {
			return false;
		}
		return true;
	}

	static public IStructureCreator getMemberCreator() {
		// TODO Auto-generated method stub
		return new MemberForStressTestCreator();
	}

	public int compareTo(Object o) {
		MemberForStressTest other = (MemberForStressTest) o;
		if (this.iField > other.iField) {
			return 1;
		} else if (this.iField == other.iField) {
			return 0;
		} else {
			return -1;
		}
	}

}

class MemberForStressTestCreator implements IStructureCreator {

	public IStructure createInstance(Object[] fields) {
		int iField = ((Integer) (fields[0])).intValue();
		Date dateField = (Date) fields[1];
		String stringField = (String) fields[2];
		return new MemberForStressTest(iField, dateField, stringField);
	}
}