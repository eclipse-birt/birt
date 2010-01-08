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
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.report.engine.api.IDataIterator;
import org.eclipse.birt.report.engine.api.IExtractionResults;
import org.eclipse.birt.report.engine.api.IResultMetaData;

public class ExtractionResults implements IExtractionResults
{
	protected IQueryResults queryResults;
	protected IResultMetaData metaData;
	protected IDataIterator iterator;
	protected IResultIterator resultIterator;
	protected int startRow;
	protected int maxRows;

	public ExtractionResults( IQueryResults queryResults, IResultMetaData metaData,
			String[] selectedColumns, int startRow, int maxRows )
	{
		this.queryResults = queryResults;
		if( null == selectedColumns)
		{
			this.metaData = metaData;
		}
		else
		{
			this.metaData = new ResultMetaData( metaData, selectedColumns );
		}
		this.startRow = startRow;
		this.maxRows = maxRows;
	}

	public ExtractionResults( IResultIterator resultIterator,
			IResultMetaData metaData, String[] selectedColumns, int startRow,
			int maxRows )
	{
		this.resultIterator = resultIterator;
		if( null == selectedColumns)
		{
			this.metaData = metaData;
		}
		else
		{
			this.metaData = new ResultMetaData( metaData, selectedColumns );
		}
		this.startRow = startRow;
		this.maxRows = maxRows;
	}
	
	public IResultMetaData getResultMetaData( ) throws BirtException
	{
		return metaData;
	}

	public IDataIterator nextResultIterator( ) throws BirtException
	{
		if ( iterator == null )
		{
			if( null == resultIterator && null != queryResults )
			{
				resultIterator = queryResults.getResultIterator( );
			}
			this.iterator = new DataIterator( this, resultIterator, startRow,
					maxRows );
		}
		return iterator;
	}

	public void close( )
	{
		if ( iterator != null )
		{
			iterator.close( );
		}
		iterator = null;
	}
}
