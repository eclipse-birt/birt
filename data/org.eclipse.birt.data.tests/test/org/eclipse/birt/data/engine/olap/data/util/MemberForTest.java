
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

import java.math.BigDecimal;
import java.util.Date;

import org.eclipse.birt.data.engine.olap.data.util.IComparableStructure;
import org.eclipse.birt.data.engine.olap.data.util.IStructure;
import org.eclipse.birt.data.engine.olap.data.util.IStructureCreator;

/**
 * 
 */

public class MemberForTest implements IComparableStructure {
	public int iField;
	public Date dateField;
	public String stringField;
	public double doubleField;
	public BigDecimal bigDecimalField;
	public boolean booleanField;

	MemberForTest(int iField, Date dateField, String stringField, double doubleField, BigDecimal bigDecimalField,
			boolean booleanField) {
		this.iField = iField;
		this.dateField = dateField;
		this.stringField = stringField;
		this.doubleField = doubleField;
		this.bigDecimalField = bigDecimalField;
		this.booleanField = booleanField;
	}

	public Object[] getFieldValues() {
		Object[] reFields = new Object[6];
		reFields[0] = new Integer(iField);
		reFields[1] = dateField;
		reFields[2] = stringField;
		reFields[3] = new Double(doubleField);
		reFields[4] = bigDecimalField;
		reFields[5] = new Boolean(booleanField);
		return reFields;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o) {
		MemberForTest other = (MemberForTest) o;
		if (this.iField != other.iField) {
			return false;
		}

		if ((this.bigDecimalField != null && !this.bigDecimalField.equals(other.bigDecimalField))
				|| (this.bigDecimalField == null && other.bigDecimalField != null)) {
			return false;
		}
		if (this.booleanField != other.booleanField) {
			return false;
		}
		if ((this.dateField != null && !this.dateField.equals(other.dateField))
				|| (this.dateField == null && other.dateField != null)) {
			return false;
		}

		if ((this.stringField != null && !this.stringField.equals(other.stringField))
				|| (this.stringField == null && other.stringField != null)) {
			return false;
		}

		if (this.doubleField != other.doubleField) {
			return false;
		}
		return true;
	}

	static public IStructureCreator getMemberCreator() {
		// TODO Auto-generated method stub
		return new MemberForTestCreator();
	}

	public int compareTo(Object o) {
		MemberForTest other = (MemberForTest) o;
		if (this.iField > other.iField) {
			return 1;
		} else if (this.iField == other.iField) {
			return 0;
		} else {
			return -1;
		}
	}

}

class MemberForTestCreator implements IStructureCreator {

	public IStructure createInstance(Object[] fields) {
		int iField = ((Integer) (fields[0])).intValue();
		Date dateField = (Date) fields[1];
		String stringField = (String) fields[2];
		double doubleField = ((Double) fields[3]).doubleValue();
		BigDecimal bigDecimalField = (BigDecimal) fields[4];
		boolean booleanField = ((Boolean) fields[5]).booleanValue();
		return new MemberForTest(iField, dateField, stringField, doubleField, bigDecimalField, booleanField);
	}
}
