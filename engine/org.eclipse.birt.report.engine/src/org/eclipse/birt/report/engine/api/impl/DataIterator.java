package org.eclipse.birt.report.engine.api.impl;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.IResultMetaData;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.report.engine.api.IDataIterator;
import org.eclipse.birt.report.engine.api.IExtractionResults;


public class DataIterator implements IDataIterator
{
	protected IResultIterator resultIter;
	protected IExtractionResults extractResult;
	
	DataIterator( IExtractionResults result, IResultIterator iter )
	{
		extractResult = result;
		resultIter = iter;
	}
	
	public IExtractionResults getQueryResults( )
	{
		return extractResult;
	}

	public IResultMetaData getResultMetaData( ) throws BirtException
	{
		return resultIter.getResultMetaData();
	}

	public boolean next( ) throws BirtException
	{
		return resultIter.next();
	}

	public Object getValue( String columnName ) throws BirtException
	{
		IBaseExpression expr = new ScriptExpression( columnName );
		return resultIter.getValue( expr );
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
}
