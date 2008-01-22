
/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.data.adapter.impl;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.report.data.adapter.api.IColumnValueIterator;

/**
 * 
 */

public class ColumnValueIterator implements IColumnValueIterator
{
	private IResultIterator resultIterator;
	private String boundColumnName;
	private IQueryResults queryResults;
	private Object value;
	
	/**
	 */
	ColumnValueIterator( IQueryResults queryResults, String boundColumnName )
	{
		this.queryResults = queryResults;
		this.boundColumnName = boundColumnName;
	}
	
	/**
	 * 
	 * @return
	 * @throws BirtException 
	 */
	public boolean next( ) throws BirtException
	{
		if( resultIterator == null )
		{
			if( queryResults == null )
				return false;
			resultIterator = queryResults.getResultIterator( );
		}
		if( resultIterator == null || !resultIterator.next( ) )
			return false;
		value = resultIterator.getValue( boundColumnName );
		resultIterator.skipToEnd( 1 );
		return true;
	}
	
	/**
	 * 
	 * @return
	 * @throws BirtException
	 */
	public Object getValue() throws BirtException
	{
		return value;
	}
	
	/**
	 * 
	 * @throws BirtException
	 */
	public void close( ) throws BirtException
	{
		if( resultIterator != null )
			resultIterator.close( );
		if( queryResults != null )
			queryResults.close( );
	}

	/**
	 * 
	 */
	void moveTo( int rowIndex ) throws BirtException
	{
		if( resultIterator == null )
		{
			return;
		}
		resultIterator.moveTo( rowIndex );
		value = resultIterator.getValue( boundColumnName );
		resultIterator.skipToEnd( 1 );
	}
}
