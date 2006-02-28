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

import java.util.ArrayList;
import java.util.HashSet;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IResultMetaData;
import org.eclipse.birt.report.engine.api.IDataIterator;
import org.eclipse.birt.report.engine.api.IExtractionResults;


public class DataIterator implements IDataIterator
{
	protected IExtractionResults extractResult;
	protected String[] selectedColumns; 
	protected ResultMetaData metaData;
	private boolean isAdvanced = false;
	private DataExtractionHelper helper;
	private HashSet colSet = new HashSet( );
		
	DataIterator( IExtractionResults result, String[] selectedColumns, 
					DataExtractionHelper helper )
	{
		this.extractResult = result;
		this.selectedColumns = selectedColumns;
		this.helper = helper;
		
		if ( this.selectedColumns == null )
		{
			ArrayList aColMeta = helper.validatedColMetas;
			this.selectedColumns = new String[aColMeta.size( )];
			for( int i=0; i<aColMeta.size( ); i++ )
			{
				ExprMetaData meta = (ExprMetaData)aColMeta.get( i );
				this.selectedColumns[i] = meta.getName( );
			}
		}
		
		if ( this.selectedColumns != null ) 
		{
			for ( int i=0; i<this.selectedColumns.length; i++ )
			{
				colSet.add( this.selectedColumns[i] );
			}
		}
	}
	
	public IExtractionResults getQueryResults( )
	{
		return extractResult;
	}

	public IResultMetaData getResultMetaData( ) throws BirtException
	{
		if( metaData == null )
		{
			metaData = new ResultMetaData( helper, this.selectedColumns ); 
		}
		
		return metaData;
	}

	public boolean next( ) throws BirtException
	{
		if( isAdvanced == false)
			isAdvanced = true;
		return helper.next();
	}

	public Object getValue( String columnName ) throws BirtException
	{
		if ( colSet.contains( columnName ) )
		{
			return helper.getValue( columnName );
		}
		return null;
	}
	
	public Object getValue( int index ) throws BirtException
	{
		IResultMetaData metaData = getResultMetaData( );
		String columnName = null;
		if(index >= 0 && index < metaData.getColumnCount() )
		{
			columnName = metaData.getColumnName( index );
			return getValue( columnName );
		}
		
		return null;
	}

	public void close( )
	{
		helper.close( );
	}
	
	boolean isAdvanced( )
	{
		return isAdvanced;
	}
}
