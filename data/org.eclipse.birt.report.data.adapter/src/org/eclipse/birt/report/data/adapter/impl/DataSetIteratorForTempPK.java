
/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
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
package org.eclipse.birt.report.data.adapter.impl;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.olap.data.api.cube.IDatasetIterator;

/**
 *
 */

public class DataSetIteratorForTempPK implements IDatasetIterator {
	private int rowCount;
	private int currRowNum = -1; // row.__rownum

	@SuppressWarnings("unchecked")
	DataSetIteratorForTempPK(int rowCount) throws BirtException {
		this.rowCount = rowCount;
	}

	@Override
	public void close() throws BirtException {
		// nothing to do

	}

	@Override
	public int getFieldIndex(String name) throws BirtException {
		return 1;
	}

	@Override
	public int getFieldType(String name) throws BirtException {
		return DataType.INTEGER_TYPE;
	}

	@Override
	public Object getValue(int fieldIndex) throws BirtException {
		return currRowNum;
	}

	@Override
	public boolean next() throws BirtException {
		if (rowCount > 0 && currRowNum < rowCount) {
			currRowNum++;
			return true;
		}
		return false;

	}

}
