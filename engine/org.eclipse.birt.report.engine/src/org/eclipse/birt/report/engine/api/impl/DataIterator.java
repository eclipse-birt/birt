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

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.IResultMetaData;
import org.eclipse.birt.report.engine.api.IDataIterator;
import org.eclipse.birt.report.engine.api.IExtractionResults;

public class DataIterator implements IDataIterator
{

	protected IExtractionResults results;
	protected IResultIterator iterator;
	protected String[] selectedColumns;
	protected IResultMetaData metaData;

	DataIterator( IExtractionResults results, IResultIterator iterator,
			String[] selectedColumns )
	{
		this.results = results;
		this.iterator = iterator;
		this.selectedColumns = selectedColumns;
	}

	public IExtractionResults getQueryResults( )
	{
		return results;
	}

	public IResultMetaData getResultMetaData( ) throws BirtException
	{
		if ( metaData == null )
		{
			metaData = new ResultMetaData( iterator.getResultMetaData( ),
					selectedColumns );
		}
		return metaData;
	}

	public boolean next( ) throws BirtException
	{
		return iterator.next( );
	}

	public Object getValue( String columnName ) throws BirtException
	{
		return iterator.getValue( columnName );
	}

	public Object getValue( int index ) throws BirtException
	{
		IResultMetaData metaData = getResultMetaData( );
		String columnName = metaData.getColumnName( index );
		return iterator.getValue( columnName );
	}

	public void close( )
	{
		try
		{
			iterator.close( );
		}
		catch ( BirtException ex )
		{
		}
	}
}
