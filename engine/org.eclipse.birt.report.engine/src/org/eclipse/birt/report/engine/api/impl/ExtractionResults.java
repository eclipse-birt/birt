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
import org.eclipse.birt.data.engine.api.IResultMetaData;
import org.eclipse.birt.report.engine.api.IDataIterator;
import org.eclipse.birt.report.engine.api.IExtractionResults;

public class ExtractionResults implements IExtractionResults
{

	protected String[] selectedColumns;
	protected IQueryResults queryResults;
	protected IResultMetaData metaData;
	protected IDataIterator iterator;

	ExtractionResults( IQueryResults queryResults, IResultMetaData metaData, String[] selectedColumns )
	{
		this.selectedColumns = selectedColumns;
		this.queryResults = queryResults;
		this.metaData = metaData;
	}

	public IResultMetaData getResultMetaData( ) throws BirtException
	{
		return metaData;
	}

	public IDataIterator nextResultIterator( ) throws BirtException
	{
		if ( iterator == null )
		{
			this.iterator = new DataIterator( this, queryResults
					.getResultIterator( ), selectedColumns );
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
