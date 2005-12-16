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

import java.util.Collection;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.IResultMetaData;
import org.eclipse.birt.report.engine.api.IDataIterator;
import org.eclipse.birt.report.engine.api.IExtractionResults;


public class ExtractionResults implements IExtractionResults
{
	protected IResultIterator resultIter;
	protected String[] selectedColumns;
	protected Collection expressions;
	protected DataIterator currentDataIterator;
		
	ExtractionResults ( IResultIterator results, String[] selectedColumns, Collection exprs )
	{
		initialize( results, selectedColumns, exprs );
	}
	
	private void initialize( IResultIterator results, String[] selectedColumns, Collection exprs )
	{
		resultIter = results;
		this.selectedColumns = selectedColumns;
		expressions = exprs;
	}
	
	public IResultMetaData getResultMetaData( ) throws BirtException
	{
		if( currentDataIterator == null )
		{
			currentDataIterator = (DataIterator)nextResultIterator( );
		}
		
		if( currentDataIterator == null )
		{
			return null;
		}
		
		return currentDataIterator.getResultMetaData();
	}

	public IDataIterator nextResultIterator( ) throws BirtException
	{
		if( resultIter == null )
			return null;
		if (currentDataIterator == null || currentDataIterator.isAdvanced() == false)
		{
			currentDataIterator = new DataIterator( this, resultIter, selectedColumns, expressions );
			return currentDataIterator;
		}
		
		return null;
	}

	public void close( )
	{
		try
		{
			resultIter.close();
		}
		catch( BirtException be )
		{
			be.printStackTrace( );
		}
	}
}
