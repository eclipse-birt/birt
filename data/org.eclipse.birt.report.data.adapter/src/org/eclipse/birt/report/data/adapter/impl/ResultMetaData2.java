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
package org.eclipse.birt.report.data.adapter.impl;

import java.util.List;

import org.eclipse.birt.core.exception.BirtException;

/**
 * The resultset metaData from datasetHandle's resultset property
 */
class ResultMetaData2 extends ResultMetaData {
	/**
	 * @param columnDefinitions
	 * @throws BirtException
	 */
	ResultMetaData2(List columnDefinitions) throws BirtException {
		super(columnDefinitions);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.api.IResultMetaData#getColumnLabel(int)
	 */
	@Override
	public String getColumnLabel(int index) throws BirtException {
		ResultSetColumnDefinition columnDefn = (ResultSetColumnDefinition) getIndexedColumnDefinition(index);
		return columnDefn.getLableName();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.api.IResultMetaData#isComputedColumn(int)
	 */
	@Override
	public boolean isComputedColumn(int index) throws BirtException {
		ResultSetColumnDefinition columnDefn = (ResultSetColumnDefinition) getIndexedColumnDefinition(index);
		return columnDefn.isComputedColumn();
	}

}
