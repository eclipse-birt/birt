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

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.birt.data.engine.api.ISortDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ConditionalExpression;
import org.eclipse.birt.data.engine.api.querydefn.FilterDefinition;
import org.eclipse.birt.data.engine.api.querydefn.SortDefinition;
import org.eclipse.birt.report.engine.EngineCase;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IDataExtractionTask;
import org.eclipse.birt.report.engine.api.IDataIterator;
import org.eclipse.birt.report.engine.api.IExtractionResults;
import org.eclipse.birt.report.engine.api.IRenderTask;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IResultMetaData;
import org.eclipse.birt.report.engine.api.IResultSetItem;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ListHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.TableHandle;

/**
 * in the report design, we define four listing elements:
 * 219: table with query.
 * 277: list of query
 * 280: a table with sub query
 * 289: a table with nest query.
 */
public class DataExtractionTaskTest extends EngineCase
{

	static final String REPORT_DESIGN_RESOURCE = "org/eclipse/birt/report/engine/api/impl/TestDataExtractionTask.xml";

	public void setUp( ) throws Exception
	{
		super.setUp( );
		removeFile( REPORT_DOCUMENT );
		removeFile( REPORT_DESIGN );
		copyResource( REPORT_DESIGN_RESOURCE, REPORT_DESIGN );
	}

	public void tearDown( )
	{
		removeFile( REPORT_DESIGN );
		removeFile( REPORT_DOCUMENT );
	}

	public void testDataExtraction( ) throws Exception
	{
		/*
		 * API test on interface IDataExtractionTask
		 * 	- setInstanceID( InstanceID )
		 * 	- getResultSetList( )
		 * 	- selectResultSet( String )
		 * 	- selectColumns( String[] )
		 * 	- setFilters( IFilterDefinition[] )
		 * 	- extract( )
		 */
		createReportDocument( );
		doTestExtractionFromInstanceId( );
		doDataExtractionFromRsetName( );
		doDataExtractionWithFilters( );
		doDataExtractionWithSelectedColumns( );
		doTestExtractionFromInstanceIdWithFilter( );
		doDataExtractionWithSorts( );
	}

	protected void doTestExtractionFromInstanceId( ) throws Exception
	{
		// get the instance id
		// open the document in the archive.
		IReportDocument document = engine.openReportDocument( REPORT_DOCUMENT );
		// create an RenderTask using the report document
		IRenderTask task = engine.createRenderTask( document );

		ByteArrayOutputStream ostream = new ByteArrayOutputStream( );
		// create the render options
		HTMLRenderOption option = new HTMLRenderOption( );
		option.setOutputFormat( "html" );
		option.setOutputStream( ostream );
		option.setEnableMetadata( true );
		// set the render options
		task.setRenderOption( option );
		assertTrue(task.getRenderOption( ).equals( option ));
		task.render( );
		task.close( );

		String content = ostream.toString( "utf-8" );
		// get all the instance ids
		Pattern iidPattern = Pattern.compile( "iid=\"([^\"]*)\"" );
		Matcher matcher = iidPattern.matcher( content );
		while ( matcher.find( ) )
		{
			String strIid = matcher.group( 1 );
			InstanceID iid = InstanceID.parse( strIid );
			long designId = iid.getComponentID( );
			IReportRunnable runnable = document.getReportRunnable( );
			ReportDesignHandle report = (ReportDesignHandle) runnable
					.getDesignHandle( );
			DesignElementHandle element = report.getElementByID( designId );
			if ( element instanceof TableHandle )
			{
				// we get the report let
				IDataExtractionTask dataExTask = engine
						.createDataExtractionTask( document );

				dataExTask.setInstanceID( iid );
				ArrayList resultSetList = (ArrayList) dataExTask
						.getResultSetList( );
				assertEquals( 1, resultSetList.size( ) );
				IExtractionResults results = dataExTask.extract( );
				int rowCount = checkExtractionResults( results );
				assertTrue( rowCount > 0 );
				dataExTask.close( );
			}
		}

		document.close( );
	}
	
	protected void doTestExtractionFromInstanceIdWithFilter( ) throws Exception
	{
		// get the instance id
		// open the document in the archive.
		IReportDocument document = engine.openReportDocument( REPORT_DOCUMENT );
		// create an RenderTask using the report document
		IRenderTask task = engine.createRenderTask( document );

		ByteArrayOutputStream ostream = new ByteArrayOutputStream( );
		// create the render options
		HTMLRenderOption option = new HTMLRenderOption( );
		option.setOutputFormat( "html" );
		option.setOutputStream( ostream );
		option.setEnableMetadata( true );
		// set the render options
		task.setRenderOption( option );
		assertTrue(task.getRenderOption( ).equals( option ));
		task.render( );
		task.close( );

		String content = ostream.toString( "utf-8" );
		// get all the instance ids
		Pattern iidPattern = Pattern.compile( "iid=\"([^\"]*)\"" );
		Matcher matcher = iidPattern.matcher( content );
		while ( matcher.find( ) )
		{
			String strIid = matcher.group( 1 );
			InstanceID iid = InstanceID.parse( strIid );
			long designId = iid.getComponentID( );
			IReportRunnable runnable = document.getReportRunnable( );
			ReportDesignHandle report = (ReportDesignHandle) runnable
					.getDesignHandle( );
			DesignElementHandle element = report.getElementByID( designId );
			if ( element instanceof TableHandle )
			{
				//it is a sub query
				if (iid.getComponentID( ) == 280)
				{
					// we get the report let
					IDataExtractionTask dataExTask = engine
							.createDataExtractionTask( document );

					dataExTask.setInstanceID( iid );
					ArrayList resultSetList = (ArrayList) dataExTask
							.getResultSetList( );
				
					assertEquals( 1, resultSetList.size( ) );
					//creat filters
					IFilterDefinition[] FilterExpression = new IFilterDefinition[1];
					FilterExpression[0] = new FilterDefinition( new ConditionalExpression( "row[\"CUSTOMERNUMBER\"]",
							IConditionalExpression.OP_EQ, "\"SubQuery_Name: 128\"", null ) );
					// add filters to dataExtractionTask
					dataExTask.setFilters( FilterExpression );
				
					IExtractionResults results = dataExTask.extract( );
					int rowCount = checkExtractionResults( results );
					assertTrue( rowCount == 1 );
					dataExTask.close( );
				}
			}
			else if ( element instanceof ListHandle )
			{
				//it is the top most query.
				if (iid.getComponentID( ) == 277)
				{
					// we get the report let
					IDataExtractionTask dataExTask = engine
							.createDataExtractionTask( document );

					dataExTask.setInstanceID( iid );
					ArrayList resultSetList = (ArrayList) dataExTask
							.getResultSetList( );
				
					assertEquals( 1, resultSetList.size( ) );
					//creat filters
					IFilterDefinition[] FilterExpression = new IFilterDefinition[2];
					FilterExpression[0] = new FilterDefinition( new ConditionalExpression( "row[\"CUSTOMERNUMBER\"]",
							IConditionalExpression.OP_GE, "201", null ) );
					FilterExpression[1] = new FilterDefinition( new ConditionalExpression( "row[\"CUSTOMERNUMBER\"]",
							IConditionalExpression.OP_LT, "300", null ) );
					// add filters to dataExtractionTask
					dataExTask.setFilters( FilterExpression );
				
					IExtractionResults result = dataExTask.extract( );
					
					String value = null;
					if ( result != null )
					{
						IDataIterator iData = result.nextResultIterator( );
						if ( iData != null)
						{
							iData.next( );
							try
							{
								value = (String) DataTypeUtil
										.convert( iData.getValue( "CUSTOMERNUMBER" ),
												DataType.STRING_TYPE ) ;
							}
							catch ( Exception e )
							{
								value = null;
							}
							assertEquals( "201", value );
							
							while(iData.next( ))
							{
								try
								{
									value = (String) DataTypeUtil
											.convert( iData.getValue( "CUSTOMERNUMBER" ),
													DataType.STRING_TYPE ) ;
								}
								catch ( Exception e )
								{
									value = null;
								}
							}
						}
					}
					assertEquals( "299", value );
					dataExTask.close( );
				}
			}
		}

		document.close( );
	}

	protected void doDataExtractionFromRsetName( ) throws Exception
	{
		// open the document in the archive.
		IReportDocument reportDoc = engine.openReportDocument( REPORT_DOCUMENT );

		IDataExtractionTask dataExTask = engine
				.createDataExtractionTask( reportDoc );

		// we have total 3 rsets in the list (doesn't include the sub query)
		ArrayList resultSetList = (ArrayList) dataExTask.getResultSetList( );
		assertEquals( "Result set numer error", 3, resultSetList.size( ) );

		// in this list, we have three resutl set items, one is ELEMENT_219, ELEMENT_277, ELEMENT_289
		IResultSetItem resultItem1 = (IResultSetItem) resultSetList.get( 0 );
		IResultSetItem resultItem2 = (IResultSetItem) resultSetList.get( 1 );
		IResultSetItem resultItem = resultItem1;

		// items in resultSetList may not be in the same order each time called.
		if ( resultItem.getResultSetName( ).equalsIgnoreCase( "ELEMENT_219" ) == false )
		{
			resultItem = resultItem2;
		}
		// the first result set name
		String dispName = resultItem.getResultSetName( );
		assertEquals( "ELEMENT_219", dispName );

		IResultMetaData resultMeta = resultItem.getResultMetaData( );
		assertEquals( 4, resultMeta.getColumnCount( ) );

		dataExTask.selectResultSet( dispName );

		IExtractionResults results = dataExTask.extract( );
		int rowCount = checkExtractionResults( results );
		assertTrue( rowCount > 0 );
		assertEquals( 7, rowCount );

		resultItem = resultItem1;
		if ( resultItem.getResultSetName( ).equalsIgnoreCase( "ELEMENT_277" ) == false )
		{
			resultItem = resultItem2;
		}
		dispName = resultItem.getResultSetName( );
		assertEquals( "ELEMENT_277", dispName );

		resultMeta = resultItem.getResultMetaData( );
		assertEquals( 2, resultMeta.getColumnCount( ) );

		dataExTask.selectResultSet( dispName );
		results = dataExTask.extract( );
		rowCount = checkExtractionResults( results );
		assertEquals( 122, rowCount );

		dataExTask.close( );

		reportDoc.close( );
	}

	protected void doDataExtractionWithFilters( ) throws Exception
	{
		// open the document in the archive.
		IReportDocument reportDoc = engine.openReportDocument( REPORT_DOCUMENT );

		IDataExtractionTask dataExTask = engine
				.createDataExtractionTask( reportDoc );
		
		dataExTask.selectResultSet( "ELEMENT_277" );
		
		// creat filters
		IFilterDefinition[] FilterExpression = new IFilterDefinition[2];
		FilterExpression[0] = new FilterDefinition( new ConditionalExpression( "row[\"CUSTOMERNUMBER\"]",
				IConditionalExpression.OP_GE, "201", null ) );
		FilterExpression[1] = new FilterDefinition( new ConditionalExpression( "row[\"CUSTOMERNUMBER\"]",
				IConditionalExpression.OP_LT, "300", null ) );
		// add filters to dataExtractionTask
		dataExTask.setFilters( FilterExpression );
		
		IExtractionResults result = dataExTask.extract( );
		
		String value = null;
		if ( result != null )
		{
			IDataIterator iData = result.nextResultIterator( );
			if ( iData != null)
			{
				iData.next( );
				try
				{
					value = (String) DataTypeUtil
							.convert( iData.getValue( "CUSTOMERNUMBER" ),
									DataType.STRING_TYPE ) ;
				}
				catch ( Exception e )
				{
					value = null;
				}
				assertEquals( "201", value );
				
				while(iData.next( ))
				{
					try
					{
						value = (String) DataTypeUtil
								.convert( iData.getValue( "CUSTOMERNUMBER" ),
										DataType.STRING_TYPE ) ;
					}
					catch ( Exception e )
					{
						value = null;
					}
				}
			}
		}
		assertEquals( "299", value );
		dataExTask.close( );
		reportDoc.close( );
	}
	
	protected void doDataExtractionWithSorts( ) throws Exception
	{
		// open the document in the archive.
		IReportDocument reportDoc = engine.openReportDocument( REPORT_DOCUMENT );

		IDataExtractionTask dataExTask = engine
				.createDataExtractionTask( reportDoc );

		dataExTask.selectResultSet( "ELEMENT_277" );

		// create sorts
		SortDefinition sort = new SortDefinition( );
		sort.setColumn( "CUSTOMERNUMBER" );
		sort.setSortDirection( ISortDefinition.SORT_DESC );

		dataExTask.setSorts( new ISortDefinition[]{sort} );

		IExtractionResults result = dataExTask.extract( );

		int previous = Integer.MIN_VALUE, current = 0;
		if ( result != null )
		{
			IDataIterator iData = result.nextResultIterator( );
			if ( iData != null )
			{
				while ( iData.next( ) )
				{
					current = ( (Integer) iData.getValue( "CUSTOMERNUMBER" ) )
							.intValue( );

					if ( previous == Integer.MIN_VALUE )
					{
						previous = current;
					}
					else
					{
						assertTrue( previous >= current );
					}
				}
			}
		}
		dataExTask.close( );
		reportDoc.close( );
	}
	
	protected void doDataExtractionWithSelectedColumns() throws Exception
	{
		// open the document in the archive.
		IReportDocument reportDoc = engine.openReportDocument( REPORT_DOCUMENT );

		IDataExtractionTask dataExTask = engine
				.createDataExtractionTask( reportDoc );
		
		dataExTask.selectResultSet( "ELEMENT_219" );
		
		String[] columnNames = new String[]{"OFFICECODE" , "CITY"};
		dataExTask.selectColumns( columnNames );
		IExtractionResults result = dataExTask.extract( );
		IResultMetaData metaData = result.getResultMetaData( );
		int count = metaData.getColumnCount( );
		assertEquals( 2, count );
		String columnName = metaData.getColumnName( 0 );
		assertEquals( "OFFICECODE", columnName );
		columnName = metaData.getColumnName( 1 );
		assertEquals( "CITY", columnName );
		reportDoc.close( );
	}

	/**
	 * access all the data in the results, no exception should be throw out.
	 * 
	 * @param results
	 * @return row count in the result.
	 * @throws BirtException
	 */
	protected int checkExtractionResults( IExtractionResults results )
			throws Exception
	{
		int rowCount = 0;
		IDataIterator dataIter = results.nextResultIterator( );
		if ( dataIter != null )
		{
			while ( dataIter.next( ) )
			{
				IResultMetaData resultMeta = dataIter.getResultMetaData( );
				for ( int i = 0; i < resultMeta.getColumnCount( ); i++ )
				{
					Object obj = dataIter.getValue( resultMeta
							.getColumnName( i ) );
					assertTrue( obj != null );
					String type = resultMeta.getColumnTypeName( i );
					assertTrue( type != null );
				}
				rowCount++;
			}
			dataIter.close( );
		}
		results.close( );
		return rowCount;
	}

}