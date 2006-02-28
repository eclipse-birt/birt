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
import org.eclipse.birt.data.engine.api.IBaseTransform;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.IResultMetaData;
import org.eclipse.birt.report.engine.api.ComponentID;
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
	
	/* 
	 * map valued expr to its report data item name
	 */
	protected HashMap mapExprToDataName;
	
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
		this.mapExprToDataName = report.getExprToNameMap( );
		
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
			
			// DataExtractionHelper helper = new DataExtractionHelper( query, null );
			
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
		/*if ( resultMetaList == null )
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
				// metaData = new ResultMetaData( null,
				// 		selectedColumns );
				// resultMetaList.add( metaData );
			}
		}*/
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
		
		DataExtractionHelper helper = new DataExtractionHelper( 
				(IQueryDefinition)query, null, 
				executionContext.getScope( ),
				this.mapQueryToValueExprs, this.mapExprToDataName );
		
		IResultMetaData resultMeta = helper.getResultMetaData( );

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
		
		if( resultSetName != null )
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
			IQueryDefinition query = (IQueryDefinition)mapResultSetNameToQuery
									.get( resultSetName );
			assert query != null;
			
			IQueryResults queryResults = dataEngine.getQueryResults( resultSetName );
			assert queryResults.getResultIterator() != null;
			
			DataExtractionHelper helper = new DataExtractionHelper( 
					query, queryResults.getResultIterator( ), 
					executionContext.getScope( ),
					this.mapQueryToValueExprs, this.mapExprToDataName );
			currentResult = new ExtractionResults( this.selectedColumns, helper );
			return currentResult;
		}
		catch ( BirtException e )
		{
			e.printStackTrace( );
		}
		return null;
	}
	
	/*
	 * check if the selected columns is valid, if no column is selected, then initialize the
	 * selected column using row expression.
	 */
	/*private void validateSelectedColumns( IBaseQueryDefinition query )
			throws EngineException
	{
		assert query != null;
		ArrayList exprs = new ArrayList( );
		HashMap exprMeta = new HashMap( );
		
		visitQuery( (QueryDefinition)query, exprMeta, exprs );
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
 	}*/
		
	/*private void visitQuery( QueryDefinition query, HashMap exprToMeta,
			ArrayList colExprs )
	{
		int level = 0;
		ArrayList valueExprs = (ArrayList) this.mapQueryToValueExprs
				.get( query );
		setExpressions( (List) query.getBeforeExpressions( ), query,
				ExprConstants.EXPR_BEFORE, level, exprToMeta, colExprs,
				valueExprs );
		setExpressions( (List) query.getRowExpressions( ), query,
				ExprConstants.EXPR_ROW, level, exprToMeta, colExprs, valueExprs );

		Iterator iter = query.getGroups( ).iterator( );
		while ( iter.hasNext( ) )
		{
			IGroupDefinition group = (IGroupDefinition) iter.next( );
			visitGroup( group, level + 1, exprToMeta, colExprs, valueExprs );
		}

		iter = query.getSubqueries( ).iterator( );
		while ( iter.hasNext( ) )
		{
			ISubqueryDefinition subquery = (ISubqueryDefinition) iter.next( );
			visitSubquery( subquery, level + 1, exprToMeta, colExprs );
		}
	}

	private void visitGroup( IGroupDefinition group, int level,
			HashMap exprToMeta, ArrayList colExprs, ArrayList valueExprs )
	{
		setExpressions( (List) group.getBeforeExpressions( ), group,
				ExprConstants.EXPR_BEFORE, level, exprToMeta, colExprs,
				valueExprs );
		setExpressions( (List) group.getRowExpressions( ), group,
				ExprConstants.EXPR_ROW, level, exprToMeta, colExprs, valueExprs );

		Iterator iter = group.getSubqueries( ).iterator( );
		while ( iter.hasNext( ) )
		{
			ISubqueryDefinition subquery = (ISubqueryDefinition) iter.next( );
			visitSubquery( subquery, level + 1, exprToMeta, colExprs );
		}
	}

	private void visitSubquery( ISubqueryDefinition subquery, int level,
			HashMap exprToMeta, ArrayList colExprs )
	{
		ArrayList valueExprs = (ArrayList) this.mapQueryToValueExprs
				.get( subquery );
		setExpressions( (List) subquery.getBeforeExpressions( ), subquery,
				ExprConstants.EXPR_BEFORE, level, exprToMeta, colExprs,
				valueExprs );
		setExpressions( (List) subquery.getRowExpressions( ), subquery,
				ExprConstants.EXPR_ROW, level, exprToMeta, colExprs, valueExprs );

		Iterator iter = subquery.getGroups( ).iterator( );
		while ( iter.hasNext( ) )
		{
			IGroupDefinition group = (IGroupDefinition) iter.next( );
			visitGroup( group, level + 1, exprToMeta, colExprs, valueExprs );
		}

		iter = subquery.getSubqueries( ).iterator( );
		while ( iter.hasNext( ) )
		{
			ISubqueryDefinition subq = (ISubqueryDefinition) iter.next( );
			visitSubquery( subq, level + 1, exprToMeta, colExprs );
		}
	}

	private void setExpressions( List exprs, IBaseTransform origin,
			int exprType, int level, HashMap exprToMeta, ArrayList colExprs,
			ArrayList valueExprs )
	{
		if ( exprs != null )
		{
			if ( valueExprs != null && valueExprs.isEmpty( ) == false )
			{
				Iterator exprIter = exprs.iterator( );
				while ( exprIter.hasNext( ) )
				{
					IBaseExpression expr = (IBaseExpression) exprIter.next( );
					if ( valueExprs.contains( expr ) )
					{
						ExpressionMetaData exprMeta = new ExpressionMetaData(
								origin, exprType, level );
						exprToMeta.put( expr, exprMeta );
						colExprs.add( expr );
					}
				}
			}
		}
	}*/
	
	
}

class ExpressionMetaData 
{
	IBaseTransform originFrom;			// Query, Group or Subquery
	int type;							// beforeExpr or rowExpr 
	int groupLevel;						// the parent level in group
	IResultIterator resultIter;
	public ExpressionMetaData( IBaseTransform origin, int exprType, int level ) 
	{
		this.originFrom = origin;
		this.type = exprType;
		this.groupLevel = level;
	}
}

class ExprConstants {
	public static final int EXPR_BEFORE = 0;
	public static final int EXPR_ROW = 1;
	public static final int EXPR_AFTER = 2;
}
