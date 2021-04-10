
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
package org.eclipse.birt.data.engine.olap.data.impl.facttable;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.data.engine.olap.data.impl.dimension.DimensionKey;
import org.eclipse.birt.data.engine.olap.data.util.IComparableStructure;
import org.eclipse.birt.data.engine.olap.data.util.IStructure;
import org.eclipse.birt.data.engine.olap.data.util.IStructureCreator;

/**
 * Describes a row in a fact table. It includes dimension key columns and
 * measure columns.
 */

public class FactTableRow implements IComparableStructure {
	private static IStructureCreator creator = null;
	private DimensionKey[] dimensionKeys;
	private Object[] measures;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.olap.data.util.IStructure#getFieldValues()
	 */
	public Object[] getFieldValues() {
		assert getDimensionKeys() != null && getMeasures() != null;
		assert getDimensionKeys().length > 0 && getMeasures().length > 0;

		List result = new ArrayList();
		result.add(Integer.valueOf(getDimensionKeys().length));
		for (int i = 0; i < getDimensionKeys().length; i++) {
			Object[] dimensionFields = getDimensionKeys()[i].getFieldValues();
			result.add(Integer.valueOf(dimensionFields.length));
			for (int j = 0; j < dimensionFields.length; j++) {
				result.add(dimensionFields[j]);
			}
		}

		result.add(Integer.valueOf(getMeasures().length));

		for (int i = 0; i < getMeasures().length; i++) {
			result.add(getMeasures()[i]);
		}
		return result.toArray();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object o) {
		FactTableRow other = (FactTableRow) o;

		assert other.getDimensionKeys().length == this.getDimensionKeys().length;

		for (int i = 0; i < getDimensionKeys().length; i++) {
			int result = (getDimensionKeys()[i]).compareTo(other.getDimensionKeys()[i]);
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
		if (o == null)
			return false;
		FactTableRow other = (FactTableRow) o;

		if (other.getDimensionKeys().length != this.getDimensionKeys().length) {
			return false;
		}

		for (int i = 0; i < getDimensionKeys().length; i++) {
			if (!getDimensionKeys()[i].equals(other.getDimensionKeys()[i])) {
				return false;
			}
		}
		return true;
	}

	/*
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		int result = 17;
		for (int i = 0; i < getDimensionKeys().length; i++) {
			result = 37 * result + getDimensionKeys()[i].hashCode();
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < getDimensionKeys().length; i++) {
			buffer.append(getDimensionKeys()[i]);
		}
		for (int i = 0; i < getMeasures().length; i++) {
			if (getMeasures()[i] == null)
				buffer.append("null");
			else
				buffer.append(getMeasures()[i]);
			buffer.append(' ');
		}
		return buffer.toString();
	}

	public static IStructureCreator getCreator() {
		if (creator == null) {
			creator = new FactTableRowCreator();
		}
		return creator;
	}

	public void setDimensionKeys(DimensionKey[] dimensionKeys) {
		this.dimensionKeys = dimensionKeys;
	}

	public DimensionKey[] getDimensionKeys() {
		return dimensionKeys;
	}

	public void setMeasures(Object[] measures) {
		this.measures = measures;
	}

	public Object[] getMeasures() {
		return measures;
	}
}

/**
 * 
 * @author Administrator
 *
 */
class FactTableRowCreator implements IStructureCreator {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.olap.data.util.IStructureCreator#createInstance(java.
	 * lang.Object[])
	 */
	public IStructure createInstance(Object[] fields) {
		IStructureCreator dimensionCreator = DimensionKey.getCreator();
		FactTableRow result = new FactTableRow();

		int pointer = 0;
		int dimensionCount = ((Integer) fields[pointer]).intValue();
		pointer++;
		result.setDimensionKeys(new DimensionKey[dimensionCount]);

		for (int i = 0; i < dimensionCount; i++) {
			Object[] dimensionFields = new Object[((Integer) fields[pointer]).intValue()];
			pointer++;
			System.arraycopy(fields, pointer, dimensionFields, 0, dimensionFields.length);
			pointer += dimensionFields.length;
			result.getDimensionKeys()[i] = (DimensionKey) dimensionCreator.createInstance(dimensionFields);
		}

		result.setMeasures(new Object[((Integer) fields[pointer]).intValue()]);
		pointer++;
		System.arraycopy(fields, pointer, result.getMeasures(), 0, result.getMeasures().length);
		return result;
	}
}