/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.engine.api.impl;

import org.eclipse.birt.data.engine.api.IResultMetaData;
import org.eclipse.birt.report.engine.api.IResultSetItem;

public class ResultSetItem implements IResultSetItem {
	/*
	 * the result set display name
	 */
	private String resultSetName;
	/*
	 * the result set meta data, which contains only column name and column count.
	 */
	private IResultMetaData resultSetMetaData;
	
	/*
	 * prevent default construction.
	 */
	private ResultSetItem( )
	{
		
	}
	
	/**
	 * construct result set meta data from result name and IResultMetaData
	 * @param resultSetName
	 * @param metaData
	 */
	public ResultSetItem( String resultSetName, IResultMetaData metaData )
	{
		this.resultSetName = resultSetName;
		resultSetMetaData = metaData;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.api.impl.IResultSetItem#getResultSetName()
	 */
	public String getResultSetName( )
	{
		return resultSetName;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.api.impl.IResultSetItem#getResultMetaData()
	 */
	public IResultMetaData getResultMetaData( )
	{
		return resultSetMetaData;
	}
}
