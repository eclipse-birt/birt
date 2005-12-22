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
import java.util.Iterator;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.IResultMetaData;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.report.engine.api.IDataIterator;
import org.eclipse.birt.report.engine.api.IExtractionResults;


public class DataIterator implements IDataIterator
{
	protected IResultIterator resultIter;
	protected IExtractionResults extractResult;
	protected String[] selectedColumns; 
	protected Collection expressions;
	protected ResultMetaData metaData;
	protected HashMap exprMap;
	private boolean isAdvanced = false;
	
	DataIterator( IExtractionResults result, IResultIterator iter, 
			String[] selectedColumns, Collection exprs )
	{
		initialize( result, iter, selectedColumns, exprs );
	}
	
	private void initialize( IExtractionResults result, IResultIterator iter, 
			String[] selectedColumns, Collection exprs )
	{
		extractResult = result;
		resultIter = iter;
		this.selectedColumns = selectedColumns;
		expressions = exprs;
		populateExprMap( );
	}
	
	private void populateExprMap( )
	{
		assert expressions != null;
		if( exprMap == null )
		{
			exprMap = new HashMap( );
		}
		
		Iterator iter = expressions.iterator();
		while( iter.hasNext() )
		{
			IBaseExpression expr = (IBaseExpression)iter.next();
			IScriptExpression scriptExpr = null;
			if ( expr instanceof IConditionalExpression )
			{
				IConditionalExpression condExpr = ( IConditionalExpression )expr;
				scriptExpr = condExpr.getExpression( );
			}
			else
			{
				scriptExpr = (IScriptExpression)expr;
			}
			exprMap.put( scriptExpr.getText(), scriptExpr );
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
			metaData = new ResultMetaData( resultIter.getResultMetaData(), selectedColumns ); 
		}
		
		return metaData;
	}

	public boolean next( ) throws BirtException
	{
		if( isAdvanced == false)
			isAdvanced = true;
		return resultIter.next();
	}

	public Object getValue( String columnName ) throws BirtException
	{
		IBaseExpression expr = getExpression( columnName );
		
		return resultIter.getValue( expr );
	}
	
	private IBaseExpression getExpression( String columnName )
	{
		assert exprMap != null;
				
		return (IBaseExpression)exprMap.get( columnName );
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
		try
		{
			resultIter.close( );
		}
		catch( BirtException be )
		{
			be.printStackTrace( );
		}
	}
	
	boolean isAdvanced( )
	{
		return isAdvanced;
	}
}
