
/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.data.adapter.impl;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.olap.data.api.cube.IDatasetIterator;

/**
 * 
 */

public class DataSetIteratorForTempPK implements IDatasetIterator {
	private static String COUNT_BINDING_NAME = "COUNT"; //$NON-NLS-1$
	private int rowCount;
	private int currRowNum = -1; // row.__rownum

	@SuppressWarnings("unchecked")
	DataSetIteratorForTempPK(int rowCount) throws BirtException {
		this.rowCount = rowCount;
	}

	public void close() throws BirtException {
		// nothing to do

	}

	public int getFieldIndex(String name) throws BirtException {
		return 1;
	}

	public int getFieldType(String name) throws BirtException {
		return DataType.INTEGER_TYPE;
	}

	public Object getValue(int fieldIndex) throws BirtException {
		return currRowNum;
	}

	public boolean next() throws BirtException {
		if (rowCount > 0 && currRowNum < rowCount) {
			currRowNum++;
			return true;
		}
		return false;

	}

}
