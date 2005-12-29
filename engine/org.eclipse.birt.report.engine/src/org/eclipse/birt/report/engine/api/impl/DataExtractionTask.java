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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.DataEngine;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
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
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.parser.ReportParser;


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
	
	
	public DataExtractionTask( ReportEngine engine, IReportRunnable runnable,
			ReportDocumentReader reader )
	{
		super( engine, runnable );
		
		// load the reportR
		this.reportDocReader = reader;
		executionContext.setReportDocument( reportDocReader );
		executionContext.setFactoryMode( false );
		executionContext.setPresentationMode( true );
		report = new ReportParser( ).parse( ( (ReportRunnable) runnable )
				.getReport( ) );
		
		executionContext.setReport( report );
		setParameterValues( reportDocReader.getParameterValues( ) );
		IDataEngine dataEngine = executionContext.getDataEngine();
		dataEngine.prepare( report, appContext );
	}
	
	public void setInstanceID( InstanceID iid )
	{
		instanceId = iid;
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

	public void selectColumns( String[] columnNames )
	{
		selectedColumns = columnNames;
		currentResult = null;
	}

	public IExtractionResults extract( ) throws EngineException
	{
		if( instanceId == null ) 
			return null;
		
		if ( currentResult != null )
			return currentResult;
		
		assert executionContext.getDataEngine() != null;
		DataEngine dataEngine = executionContext.getDataEngine().getDataEngine();
		
		ReportItemDesign rptItem = (ReportItemDesign) report
				.getReportItemByID( instanceId.getComponentID( ) );
		assert rptItem != null;
		
		IBaseQueryDefinition query = rptItem.getQuery();
		validateSelectedColumns( query );
		
		
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
						, selectedColumns, query.getRowExpressions() );
				resultMetaList.add( currentResult.getResultMetaData() );
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
						selectedColumns, query.getRowExpressions() );
				
				resultMetaList.add( currentResult.getResultMetaData());
				return currentResult;
			}
			catch( BirtException be )
			{
				be.printStackTrace( );
			}
		}
		
		return null;
	}
	
	private void validateSelectedColumns( IBaseQueryDefinition query )
			throws EngineException
	{
		assert query != null;

		Collection exprs = query.getRowExpressions( );

		if ( selectedColumns != null )
		{
			for ( int i = 0; i < selectedColumns.length; i++ )
			{
				boolean findColumn = false;
				Iterator iter = exprs.iterator( );
				while ( iter.hasNext( ) )
				{
					IBaseExpression expr = (IBaseExpression) iter.next( );
					String exprText = null;
					if ( expr instanceof IConditionalExpression )
					{
						IConditionalExpression condExpr = ( IConditionalExpression ) expr;
						exprText = DataIterator.getConditionalExpressionText( condExpr );
					}
					else if ( expr instanceof IScriptExpression )
					{
						exprText = (( IScriptExpression ) expr).getText( );
					}
					
					assert exprText != null;
					
					if ( exprText.equalsIgnoreCase( selectedColumns[i] ) )
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
					IBaseExpression expr = (IBaseExpression) iter.next( );
					if ( expr instanceof IConditionalExpression )
					{
						IConditionalExpression condExpr = (IConditionalExpression) expr;
						selectedColumns[i] = DataIterator.getConditionalExpressionText( condExpr );
					}
					else if ( expr instanceof IScriptExpression )
					{
						IScriptExpression scriptExpr = (IScriptExpression) expr;
						selectedColumns[i] = scriptExpr.getText( );
					}
				}
			}
		}
 	}

}
