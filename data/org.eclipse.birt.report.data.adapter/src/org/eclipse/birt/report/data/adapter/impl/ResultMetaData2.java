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
	public String getColumnLabel(int index) throws BirtException {
		ResultSetColumnDefinition columnDefn = (ResultSetColumnDefinition) getIndexedColumnDefinition(index);
		return columnDefn.getLableName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.IResultMetaData#isComputedColumn(int)
	 */
	public boolean isComputedColumn(int index) throws BirtException {
		ResultSetColumnDefinition columnDefn = (ResultSetColumnDefinition) getIndexedColumnDefinition(index);
		return columnDefn.isComputedColumn();
	}

}
