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

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.data.engine.api.DataEngine;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.report.engine.api.ComponentID;
import org.eclipse.birt.report.engine.api.DataID;
import org.eclipse.birt.report.engine.api.DataSetID;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IDataExtractionTask;
import org.eclipse.birt.report.engine.api.IExtractionResults;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.api.ReportEngine;
import org.eclipse.birt.report.engine.data.IDataEngine;
import org.eclipse.birt.report.engine.data.dte.AbstractDataEngine;
import org.eclipse.birt.report.engine.data.dte.DteDataEngine;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.parser.ReportParser;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ReportDesignHandle;


public class DataExtractionTask extends EngineTask
		implements
			IDataExtractionTask
{
	protected ReportDocumentReader reportDocReader;
	
	protected ComponentID componentId;
	
	protected InstanceID instanceId;
	
	protected String[] selectedColumns;
	
	protected Report report;
	
	protected List resultMetaList;
	
	protected IExtractionResults currentResult = null;
	
	/*
	 * map query id to result set name stored in DtE
	 */
	protected HashMap mapQueryIDToResultSetName;
	
	/*
	 * current result set name
	 */
	protected String resultSetName;
	/*
	 * map IBaseQueryDefinition to ReportItemDesign, namely TableItemDesign, ListItemDesign
	 * and ExtendedItemDesign
	 */
	protected HashMap mapQueryToReportItem;
	
	/*
	 * map result set display name to result set name stored by DtE.
	 */
	protected HashMap mapDispNameToResultSetName;
	
	/*
	 * have the metadata be prepared
	 */
	protected boolean isMetaDataPrepared = false;
	
	/*
	 * map query to display name
	 */
	protected HashMap mapQueryToDispName;
	
	/*
	 * map query to value expressions
	 */
	protected HashMap mapQueryToValueExprs;
	
	/*
	 * map result set name to query
	 */
	protected HashMap mapResultSetNameToQuery;
	
	/**
	 * the logger
	 */
	protected static Logger logger = Logger.getLogger( DteDataEngine.class
			.getName( ) );
	
	public DataExtractionTask( ReportEngine engine, IReportRunnable runnable,
			ReportDocumentReader reader ) throws EngineException
	{
		super( engine, runnable );
		
		// load the report
		this.reportDocReader = reader;
		executionContext.setReportDocument( reportDocReader );
		executionContext.setFactoryMode( false );
		executionContext.setPresentationMode( true );
		
		try
		{
			ReportParser parser = new ReportParser( );
			
			ReportDesignHandle reportDesign = parser.getDesignHandle( reader.getDesignName(), 
					reader.getDesignStream() );
			report = parser.parse( reportDesign );
		}
		catch ( DesignFileException e )
		{
			e.printStackTrace( );
		}
		
		executionContext.setReport( report );
		setParameterValues( reportDocReader.getParameterValues( ) );
		
		IDataEngine dataEngine = executionContext.getDataEngine();
		dataEngine.prepare( report, appContext );		
	}
	
	/*
	 * prepare the meta data of DataExtractionTask.
	 */
	private void prepareMetaData( )
	{
		if( isMetaDataPrepared == true ) 
			return;
			
		mapQueryToReportItem = report.getReportItemToQueryMap( );
		mapQueryToValueExprs = report.getQueryToValueExprMap( );
		
		// load query -> result set name
		try
		{
			loadResultSetMetaData( );
		}
		catch( EngineException e )
		{
			e.printStackTrace( );
		}
		assert mapQueryIDToResultSetName != null;

		// set displayName -> result set name
		if (mapDispNameToResultSetName == null)
		{
			mapDispNameToResultSetName = new HashMap();
		}
		else
		{
			mapDispNameToResultSetName.clear( );
		}
		
		// set query -> display name
		if( this.mapQueryToDispName == null )
		{
			this.mapQueryToDispName = new HashMap( );
		}
		else
		{
			this.mapQueryToDispName.clear( );
		}
		
		// set result set name -> query
		if( this.mapResultSetNameToQuery == null )
		{
			this.mapResultSetNameToQuery  = new HashMap( );
		}
		else
		{
			this.mapResultSetNameToQuery.clear( );
		}
		
		ArrayList queryList = report.getQueries();
		int counter = 0;
		for (int i = 0; i < queryList.size(); i++) {
			IQueryDefinition query = (IQueryDefinition) queryList.get(i);
			assert query != null;
			
			String queryId = (String)report.getQueryIDs( ).get( query );
			List resultSetList = (List) mapQueryIDToResultSetName.get(queryId);
			if (resultSetList == null) {
				continue;
			}

			// create display name
			ReportItemDesign reportItem = (ReportItemDesign) mapQueryToReportItem
					.get(query);
			if( reportItem == null )
			{
				continue;
			}
			String displayName = null;
			if (reportItem.getName() != null) {
				displayName = reportItem.getName() + "_" + counter;
			} else {
				displayName = "ELEMENT_" + reportItem.getID() + "_" + counter;
			}
			counter++;
			
			this.mapQueryToDispName.put( query, displayName );
			// get result set name
			Iterator resultSetIter = resultSetList.iterator();
			while (resultSetIter.hasNext()) {
				String resultSetName = (String) resultSetIter.next();
				mapDispNameToResultSetName.put(displayName, resultSetName);
				this.mapResultSetNameToQuery.put( resultSetName, query );
			}
		}
		
		isMetaDataPrepared = true;
	}
	
	/*
	 * load map from query id to result set id from report document.
	 */
	private void loadResultSetMetaData( ) throws EngineException
	{
		IDocArchiveReader reader = reportDocReader.getArchive( );
		try
		{
			DataInputStream dis = new DataInputStream( reader 
					.getStream( AbstractDataEngine.DATA_META_STREAM ));
			
			mapQueryIDToResultSetName = new HashMap( );
			int size = IOUtil.readInt( dis );
			for( int i=0; i<size; i++ )
			{
				String queryId = IOUtil.readString( dis );
				LinkedList ridList = new LinkedList( );
				readStringList( dis, ridList );
				mapQueryIDToResultSetName.put( queryId, ridList );
			}
			dis.close( );
		}
		catch ( IOException ioe )
		{
			executionContext.addException( new EngineException(
					"Can't load the data in report document", ioe ) );
			logger.log( Level.SEVERE, ioe.getMessage( ), ioe );
		}
	}
	
	private void readStringList( DataInputStream dis, List list ) throws IOException
	{
		int size = IOUtil.readInt( dis );
		for( int i=0; i<size; i++ )
		{
			String str = IOUtil.readString( dis );
			list.add( str );
		}
	}
	
	public void setInstanceID( InstanceID iid )
	{
		instanceId = iid;
		currentResult = null;
	}
	
	public void selectResultSet( String displayName )
	{
		assert displayName != null;
		prepareMetaData( );
		
		resultSetName = (String)mapDispNameToResultSetName.get( displayName );
		selectedColumns = null;
		currentResult = null;
	}
	
	public List getMetaData( ) throws EngineException
	{
		if ( resultMetaList == null )
		{
			resultMetaList = new ArrayList( );
			ResultMetaData metaData = null;
			if ( selectedColumns == null && instanceId != null )
			{
				currentResult = extract( );
				try
				{
					resultMetaList.add( currentResult.getResultMetaData( ) );
				}
				catch( BirtException be )
				{
					be.printStackTrace();
				}
			}
			
			if( selectedColumns == null )
			{
				return null;
			}
			else
			{
				metaData = new ResultMetaData( null,
						selectedColumns );
				resultMetaList.add( metaData );
			}
		}
		return resultMetaList;
	}
	
	public List getResultSetList( ) throws EngineException
	{	
		if (resultMetaList == null) 
		{
			prepareMetaData( );
			resultMetaList = new ArrayList();
			String dispName = null;
			if( instanceId != null )
			{
				ReportItemDesign reportItem = (ReportItemDesign)report.getReportItemByID( 
						instanceId.getComponentID( ) );
				IBaseQueryDefinition query = reportItem.getQuery( );
				dispName = (String)mapQueryToDispName.get( query ); 
				
				addToResultSetList( query, dispName );
			}
			else
			{
				Set keySet = mapQueryToDispName.keySet( );
				Iterator keyIter = keySet.iterator( );
				while( keyIter.hasNext( ))
				{
					IBaseQueryDefinition query = (IBaseQueryDefinition)keyIter.next( );
					dispName = (String)mapQueryToDispName.get( query );
					addToResultSetList( query, dispName );
				}
			}
		}
		return resultMetaList;
	}

	/*
	 * create IResultSetItem using display name and IResultMetaData 
	 */
	private void addToResultSetList( IBaseQueryDefinition query, 
			String displayName )
	{
		assert query != null;
		assert displayName != null;
		
		ResultMetaData resultMeta = new ResultMetaData(
				getValueExpressions( query ));

		IResultSetItem resultItem = new ResultSetItem(displayName, resultMeta);
		
		resultMetaList.add( resultItem );
	}
	
	public void selectColumns( String[] columnNames )
	{
		selectedColumns = columnNames;
		currentResult = null;
	}
	
	public IExtractionResults extract( ) throws EngineException
	{
		if ( currentResult != null )
			return currentResult;
		
		if( instanceId != null )
		{
			return extractByInstanceId( );
		}
		else if( resultSetName != null )
		{
			return extractByResultSetName( );
		}
		return null;
	}
	
	/*
	 * export result directly from result set name
	 */
	private IExtractionResults extractByResultSetName( ) throws EngineException
	{
		assert resultSetName != null;
		assert executionContext.getDataEngine() != null;
		
		prepareMetaData( );
		
		DataEngine dataEngine = executionContext.getDataEngine().getDataEngine();
		try
		{
			IBaseQueryDefinition query = (IBaseQueryDefinition)mapResultSetNameToQuery
									.get( resultSetName );
			assert query != null;
			validateSelectedColumns( query );
			
			IQueryResults queryResults = dataEngine.getQueryResults( resultSetName );
			assert queryResults.getResultIterator() != null;
			 
			currentResult = new ExtractionResults( queryResults.getResultIterator() 
					, selectedColumns, getValueExpressions( query ) );
			return currentResult;
		}
		catch ( BirtException e )
		{
			e.printStackTrace( );
		}
		return null;
	}
	
	/*
	 * data export by report item instance
	 */
	private IExtractionResults extractByInstanceId( ) throws EngineException
	{
		assert instanceId != null;
		
		assert executionContext.getDataEngine() != null;
		DataEngine dataEngine = executionContext.getDataEngine().getDataEngine();
		
		ReportItemDesign rptItem = (ReportItemDesign) report
				.getReportItemByID( instanceId.getComponentID( ) );
		assert rptItem != null;
		
		validateSelectedColumns( rptItem.getQuery( ) );
		
		
		DataID dataId = instanceId.getDataID();
		InstanceID instId = instanceId;
		while( instId != null && dataId == null ){
			instId = instId.getParentID( );
			if( instId != null )
			{
				dataId = instId.getDataID();
			}
		}
		
		if(dataId == null)
			return null;
		
		DataSetID dataSetId = dataId.getDataSetID( );
		assert dataSetId != null;
		
		String queryResultName = dataSetId.getDataSetName( );
		
		if( resultMetaList == null )
		{
			resultMetaList = new ArrayList( );
		}
		else
		{
			resultMetaList.clear();
		}
		
		if ( queryResultName != null )
		{
			try
			{
				IQueryResults queryResults = dataEngine.getQueryResults( queryResultName );
				
				assert queryResults.getResultIterator() != null;
				
				currentResult = new ExtractionResults( queryResults.getResultIterator() 
						, selectedColumns, getValueExpressions( rptItem.getQuery( ) ) );
				return currentResult;
				
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
				DataSetID parId = parentId;
				while( queryResultName == null && parId != null )
				{
					parId = parId.getParentID();
					if( parId != null )
						queryResultName = parId.getDataSetName();
				}
				assert queryResultName != null;
				
				IQueryResults parentQueryResult = dataEngine
						.getQueryResults( queryResultName );
				assert parentQueryResult != null;
				
				IResultIterator iter = parentQueryResult.getResultIterator( );
				long rowid = dataSetId.getRowID( );
				
				int i = 0;
				while ( iter.next( ) && i++ < rowid ) 		;
				
				IResultIterator subIter = iter.getSecondaryIterator( dataSetId
						.getQueryName( ), executionContext.getScope( ) );
				
				currentResult = new ExtractionResults( subIter, 
						selectedColumns, getValueExpressions ( rptItem.getQuery( ) ) );
				return currentResult;
			}
			catch( BirtException be )
			{
				be.printStackTrace( );
			}
		}
		return null;
	}
	
	/*
	 * check if the selected columns is valid, if no column is selected, then initialize the
	 * selected column using row expression.
	 */
	private void validateSelectedColumns( IBaseQueryDefinition query )
			throws EngineException
	{
		assert query != null;
		
		Collection exprs = getValueExpressions( query );

		if ( selectedColumns != null )
		{
			for ( int i = 0; i < selectedColumns.length; i++ )
			{
				boolean findColumn = false;
				String selectColumn = selectedColumns[i].replaceAll( "\\s", "" );
				Iterator iter = exprs.iterator( );
				while ( iter.hasNext( ) )
				{
					IScriptExpression expr = (IScriptExpression) iter.next( );
					String exprText = expr.getText( ).replaceAll( "\\s", "" );
					if( exprText.equalsIgnoreCase( selectColumn ))
					{
						findColumn = true;
						break;
					}
				}
				if ( findColumn == false )
				{
					throw new EngineException( "Invalid Columns" );
				}
			}
		}
		else
		{
			if ( exprs.size( ) <= 0 )
			{
				throw new EngineException( "Can't exported data in groups" );
			}
			else
			{
				selectedColumns = new String[exprs.size( )];
				Iterator iter = exprs.iterator( );
				
				for ( int i = 0; i < selectedColumns.length; i++ )
				{
					IScriptExpression expr = (IScriptExpression) iter.next( );
					selectedColumns[i] = expr.getText( );
				}
			}
		}
 	}

	private Collection getValueExpressions( IBaseQueryDefinition query )
	{
		ArrayList valueExprs = (ArrayList)mapQueryToValueExprs.get( query );
		return valueExprs;
	}
}
