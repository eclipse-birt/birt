package org.eclipse.birt.report.engine.api.impl;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.IResultMetaData;
import org.eclipse.birt.report.engine.api.IDataIterator;
import org.eclipse.birt.report.engine.api.IExtractionResults;


public class ExtractionResults implements IExtractionResults
{
	// protected IDataEngine dataEngine;
	protected IResultIterator resultIter;
	// protected LinkedList itemList;
	
	ExtractionResults( IResultIterator results )
	{
		resultIter = results;
	}
		
	public IResultMetaData getResultMetaData( ) throws BirtException
	{
		return resultIter == null ? null : resultIter.getResultMetaData();
	}

	public IDataIterator nextResultIterator( ) throws BirtException
	{
		if( resultIter != null )
		{
			return new DataIterator( this, resultIter );
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
