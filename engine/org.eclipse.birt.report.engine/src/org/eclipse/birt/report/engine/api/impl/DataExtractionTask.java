package org.eclipse.birt.report.engine.api.impl;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.DataEngine;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.report.engine.api.ComponentID;
import org.eclipse.birt.report.engine.api.DataID;
import org.eclipse.birt.report.engine.api.DataSetID;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.Filter;
import org.eclipse.birt.report.engine.api.IDataExtractionTask;
import org.eclipse.birt.report.engine.api.IExtractionResults;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.api.ReportEngine;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.engine.parser.ReportParser;


public class DataExtractionTask extends EngineTask
		implements
			IDataExtractionTask
{
	protected ReportDocumentReader reportDocReader;
	protected ComponentID componentId;
	protected InstanceID instanceId;
	
	
	public DataExtractionTask( ReportEngine engine, IReportRunnable runnable,
			ReportDocumentReader reader )
	{
		super( engine, runnable );
		
		// load the reportR
		this.reportDocReader = reader;
		executionContext.setReportDocument( reportDocReader );
		executionContext.setFactoryMode( false );
		executionContext.setPresentationMode( true );
		Report report = new ReportParser( ).parse( ( (ReportRunnable) runnable )
				.getReport( ) );
		executionContext.setReport( report );
		setParameterValues( reportDocReader.getParameterValues( ) );
	}
	
	
	public void setItemID( ComponentID cid )
	{
		componentId = cid;
	}

	public void setInstanceID( InstanceID iid )
	{
		instanceId = iid;
	}

	public List getMetaData( )
	{
		try
		{
			IExtractionResults result = extract( );
			if ( result == null )
				return null;

			List listMeta = new ArrayList( );
			listMeta.add( result.getResultMetaData( ) );
			return listMeta;
		}
		catch ( BirtException be )
		{
			be.printStackTrace( );
		}
		return null;
	}

	public void selectColumns( String[] columnNames )
	{
		//TODO
		throw new UnsupportedOperationException( );
	}

	public void setFilters( Filter[] simpleFilterExpression )
	{
		//TODO
		throw new UnsupportedOperationException( );
	}

	public void setSortConditions( String[] columnNames, int[] directions )
	{
		//TODO
		throw new UnsupportedOperationException( );
	}

	public void setQuery( String queryString )
	{
		// TODO Auto-generated method stub	
	}

	public IExtractionResults extract( ) throws EngineException
	{
		if( instanceId == null ) 
			return null;
		DataEngine dataEngine = executionContext.getDataEngine().getDataEngine();
		
		DataID dataId = instanceId.getDataID();
		InstanceID instId = instanceId;
		while( instId != null && (dataId = instId.getDataID( )) == null ){
			instId = instId.getParentID( );
		}
		
		
		if(dataId == null)
			return null;
		
		DataSetID dataSetId = dataId.getDataSetID( );
		assert dataSetId != null;
		String queryResultName = dataSetId.getDataSetName( );
		
		if ( queryResultName != null )
		{
			try
			{
				IQueryResults queryResults = executionContext.getDataEngine( ).getDataEngine( )
						.getQueryResults( queryResultName );
				
				if( queryResults.getResultIterator() == null )
					return null;
				
				return new ExtractionResults( queryResults.getResultIterator() );
				
			}
			catch ( BirtException e )
			{
				e.printStackTrace();
			}
		}
		else
		{
			DataSetID parentId = dataSetId.getParentID( );
			assert parentId != null;
			try
			{
				queryResultName = parentId.getDataSetName( );
				assert queryResultName != null;
				IQueryResults parentQueryResult = dataEngine
						.getQueryResults( queryResultName );
				IResultIterator iter = parentQueryResult.getResultIterator( );
				long rowid = dataSetId.getRowID( );

				int i = 0;
				while ( iter.next( ) && i++ < rowid ) 		;
				
				IResultIterator subIter = iter.getSecondaryIterator( dataSetId
						.getQueryName( ), executionContext.getScope( ) );
				return new ExtractionResults( subIter );
			}
			catch( BirtException be )
			{
				be.printStackTrace( );
			}
		}
		
		return null;
	}

}
