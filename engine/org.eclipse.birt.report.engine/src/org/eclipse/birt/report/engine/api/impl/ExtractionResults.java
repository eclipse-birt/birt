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
import java.util.HashMap;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IResultMetaData;
import org.eclipse.birt.report.engine.api.IDataIterator;
import org.eclipse.birt.report.engine.api.IExtractionResults;


public class ExtractionResults implements IExtractionResults
{
	protected String[] selectedColumns;
	protected Collection expressions;
	protected DataIterator currentDataIterator;
	protected HashMap exprMeta;
	protected DataExtractionHelper helper;
		
	ExtractionResults ( String[] selectedColumns, DataExtractionHelper helper )
	{
		this.selectedColumns = selectedColumns;
		this.helper = helper;
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
		if (currentDataIterator == null || currentDataIterator.isAdvanced() == false)
		{
			currentDataIterator = new DataIterator( this, this.selectedColumns, 
					this.helper );
		}
		
		return this.currentDataIterator;
	}

	public void close( )
	{
		this.currentDataIterator.close( );
	}
}
