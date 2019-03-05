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

import static org.eclipse.birt.data.engine.api.IConditionalExpression.OP_BETWEEN;
import static org.eclipse.birt.data.engine.api.IConditionalExpression.OP_BOTTOM_N;
import static org.eclipse.birt.data.engine.api.IConditionalExpression.OP_BOTTOM_PERCENT;
import static org.eclipse.birt.data.engine.api.IConditionalExpression.OP_EQ;
import static org.eclipse.birt.data.engine.api.IConditionalExpression.OP_FALSE;
import static org.eclipse.birt.data.engine.api.IConditionalExpression.OP_GE;
import static org.eclipse.birt.data.engine.api.IConditionalExpression.OP_GT;
import static org.eclipse.birt.data.engine.api.IConditionalExpression.OP_IN;
import static org.eclipse.birt.data.engine.api.IConditionalExpression.OP_LE;
import static org.eclipse.birt.data.engine.api.IConditionalExpression.OP_LIKE;
import static org.eclipse.birt.data.engine.api.IConditionalExpression.OP_LT;
import static org.eclipse.birt.data.engine.api.IConditionalExpression.OP_MATCH;
import static org.eclipse.birt.data.engine.api.IConditionalExpression.OP_NE;
import static org.eclipse.birt.data.engine.api.IConditionalExpression.OP_NOT_BETWEEN;
import static org.eclipse.birt.data.engine.api.IConditionalExpression.OP_NOT_IN;
import static org.eclipse.birt.data.engine.api.IConditionalExpression.OP_NOT_LIKE;
import static org.eclipse.birt.data.engine.api.IConditionalExpression.OP_NOT_MATCH;
import static org.eclipse.birt.data.engine.api.IConditionalExpression.OP_NOT_NULL;
import static org.eclipse.birt.data.engine.api.IConditionalExpression.OP_NULL;
import static org.eclipse.birt.data.engine.api.IConditionalExpression.OP_TOP_N;
import static org.eclipse.birt.data.engine.api.IConditionalExpression.OP_TOP_PERCENT;
import static org.eclipse.birt.data.engine.api.IConditionalExpression.OP_TRUE;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
import org.eclipse.birt.report.engine.api.EngineException;
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
//import org.json.JSONArray;
//import org.json.JSONObject;

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
	
	private final static String JSON = "json.csv";
	
	IReportDocument document;
	IDataExtractionTask dataExTask;
	
	public void setUp( ) throws Exception
	{
		super.setUp( );
		removeFile( REPORT_DOCUMENT );
		removeFile( REPORT_DESIGN );
		copyResource( REPORT_DESIGN_RESOURCE, REPORT_DESIGN );
		createReportDocument( );
		document = engine.openReportDocument( REPORT_DOCUMENT );
		dataExTask = engine.createDataExtractionTask( document );
	}

	public void tearDown( )
	{
		dataExTask.close( );
		document.close( );
		removeFile( REPORT_DESIGN );
		removeFile( REPORT_DOCUMENT );
	}

	public void testExtractionFromInstanceId( ) throws Exception
	{
		// get the instance id
		// open the document in the archive.
		// create an RenderTask using the report document
		Set<InstanceID> instanceIds = getAllInstanceIds( document );
		for ( InstanceID iid : instanceIds )
		{
			long designId = iid.getComponentID( );
			IReportRunnable runnable = document.getReportRunnable( );
			ReportDesignHandle report = (ReportDesignHandle) runnable
					.getDesignHandle( );
			DesignElementHandle element = report.getElementByID( designId );
			if ( element instanceof TableHandle )
			{
				// we get the report let
				dataExTask.setInstanceID( iid );
				ArrayList resultSetList = (ArrayList) dataExTask
						.getResultSetList( );
				assertEquals( 1, resultSetList.size( ) );
				IExtractionResults results = dataExTask.extract( );
				int rowCount = checkExtractionResults( results );
				assertTrue( rowCount > 0 );
			}
		}
	}

	public void testExtractionFromInstanceIdWithFilter( ) throws Exception
	{
		// create an RenderTask using the report document
		
		Set<InstanceID> instanceIds = getAllInstanceIds( document );
		for ( InstanceID iid : instanceIds )
		{
			long designId = iid.getComponentID( );
			IReportRunnable runnable = document.getReportRunnable( );
			ReportDesignHandle report = (ReportDesignHandle) runnable
					.getDesignHandle( );
			DesignElementHandle element = report.getElementByID( designId );
			if ( element instanceof TableHandle )
			{
				// it is a sub query
				if ( iid.getComponentID( ) == 280 )
				{
					dataExTask.setInstanceID( iid );
					ArrayList resultSetList = (ArrayList) dataExTask
							.getResultSetList( );

					assertEquals( 1, resultSetList.size( ) );
					// creat filters
					IFilterDefinition[] FilterExpression = new IFilterDefinition[1];
					FilterExpression[0] = new FilterDefinition(
							new ConditionalExpression(
									"row[\"CUSTOMERNUMBER_1\"]", OP_EQ,
									"\"SubQuery_Name: 128\"" ) );
					// add filters to dataExtractionTask
					dataExTask.setFilters( FilterExpression );

					IExtractionResults results = dataExTask.extract( );
					int rowCount = checkExtractionResults( results );
					assertTrue( rowCount == 1 );
				}
			}
			else if ( element instanceof ListHandle )
			{
				// it is the top most query.
				if ( iid.getComponentID( ) == 277 )
				{
					dataExTask.setInstanceID( iid );
					doTestExtractionTaskWithFilters( 1 );
				}
			}
		}
	}

	public void testDataExtractionFromRsetName( ) throws Exception
	{
		// we have total 3 rsets in the list (doesn't include the sub query)
		ArrayList resultSetList = (ArrayList) dataExTask.getResultSetList( );
		assertEquals( "Result set number error", 4, resultSetList.size( ) );

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
		assertEquals( 5, resultMeta.getColumnCount( ) );

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
	}

	public void testExtractionWithFilters( ) throws Exception
	{
		dataExTask.selectResultSet( "ELEMENT_277" );
		doTestExtractionTaskWithFilters( 4 );
	}
	
	public void testExtractionWithDistinct( ) throws Exception
	{
		dataExTask.selectResultSet( "ELEMENT_339" );
		dataExTask.setDistinctValuesOnly( true );
		dataExTask.selectColumns( new String[]{"COUNTRY"} );
		IExtractionResults results = dataExTask.extract( );
		IDataIterator itr = results.nextResultIterator( );
		HashSet set = new HashSet();
		while(itr.next( ))
		{
			Object value = itr.getValue( 0 );
			if ( set.contains( value ) )
			{
				fail("fail test on DataExtraction's distinct");
			}
			else
			{
				set.add( value );
			}
		}
	}
	
	public void testDataExtractionWithSorts( ) throws Exception
	{
		dataExTask.selectResultSet( "ELEMENT_277" );

		// create sorts
		SortDefinition sort = new SortDefinition( );
		sort.setColumn( "CUSTOMERNUMBER" );
		sort.setSortDirection( ISortDefinition.SORT_DESC );

		dataExTask.setSorts( new ISortDefinition[]{sort} );

		IExtractionResults result = dataExTask.extract( );

		int previous = 0, current = 0;
		if ( result != null )
		{
			IDataIterator iData = result.nextResultIterator( );
			if ( iData != null )
			{
				while ( iData.next( ) )
				{
					current = ( (Integer) iData.getValue( "CUSTOMERNUMBER" ) )
							.intValue( );
					if ( previous != 0 )
					{
						assertTrue( previous >= current );
					}
					previous = current;
				}
			}
		}
	}
	
	public void testDataExtractionWithSelectedColumns() throws Exception
	{
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
	}
	
	/**
	 *   JSON dataextraction not supported for open source

    public void testDataExtractionToJSON( ) throws Exception
    {
        dataExTask.selectResultSet( "ELEMENT_219" );

        String[] columnNames = new String[]{"OFFICECODE", "CITY"};
        dataExTask.selectColumns( columnNames );
        DataExtractionOption option = new DataExtractionOption( );
        option.setOutputFormat( "json" );
        FileOutputStream fos = null;
        try
        {
            fos = new FileOutputStream( JSON );
            option.setOutputStream( fos );
            dataExTask.extract( option );

        }
        finally
        {
            dataExTask.close( );
            if ( fos != null )
                fos.close( );
        }

        FileInputStream fis = new FileInputStream( JSON );
        StringBuffer fileContent = new StringBuffer( "" );

        byte[] buffer = new byte[1024];

        int n = -1;
        while ( ( n = fis.read( buffer ) ) != -1 )
        {
            fileContent.append( new String( buffer, 0, n ) );
        }
        try
        {
            JSONObject jo = new JSONObject( fileContent.toString( ) );
            JSONArray data = jo.getJSONArray( "rows" );
            assertEquals( 7, data.length( ) );
        }
        catch ( Exception e )
        {
            fail( e.getMessage( ) );
        }
    }
*/
	public void testFilters( ) throws BirtException
	{
		dataExTask.selectResultSet( "ELEMENT_219" );
		testFilter( "OFFICECODE", OP_BETWEEN, "1", "4", "OFFICECODE", new String[]{"1", "2",
				"3", "4"} );
		testFilter( "OFFICECODE", OP_BOTTOM_N, "2", new String[]{"1", "2"} );
		testFilter( "OFFICECODE", OP_BOTTOM_PERCENT, "30", new String[]{"1", "2"} );
		testFilter( "CITY", OP_EQ, "\"Boston\"", new String[]{"Boston"} );
		testNoOperandFilter( "row[\"OFFICECODE\"] != 7", OP_FALSE,
				"OFFICECODE", new String[]{"7"} );
		testFilter( "OFFICECODE", OP_GE, "6", new String[]{"6", "7"} );
		testFilter( "OFFICECODE", OP_GT, "6", "7" );
		
		testFilter( "OFFICECODE", OP_IN, new String[]{"1", "3"}, new String[]{"1", "3"} );
		testFilter( "OFFICECODE", OP_LE, "2", new String[]{"1", "2"} );
		testFilter( "OFFICECODE", OP_LT, "2", "1" );
		testFilter( "CITY", OP_LIKE, "\"S%\"", new String[]{"San Francisco", "Sydney"} );
		testFilter( "CITY", OP_MATCH, "\"S+\"", new String[]{"San Francisco", "Sydney"} );
		testFilter( "OFFICECODE", OP_NE, "7", new String[]{"1", "2", "3", "4", "5", "6"} );
		testFilter( "OFFICECODE", OP_NOT_BETWEEN, new String[]{"2", "6"}, new String[]{"1", "7"} );
		testFilter( "OFFICECODE", OP_NOT_IN, new String[]{"2", "6"},
				new String[]{"1", "3", "4", "5", "7"} );
		testFilter( "CITY", OP_NOT_LIKE, "\"S%\"", new String[]{"Boston",
				"NYC", "Paris", "Tokyo", "London"} );
		testFilter( "CITY", OP_NOT_MATCH, "\"S+\"", new String[]{
				"Boston", "NYC", "Paris", "Tokyo", "London"} );
		
		testFilter( "STATE", OP_NOT_NULL, null, new String[]{"CA", "MA", "NY",
				"Chiyoda-Ku"} );
		testFilter( "STATE", OP_NULL, null, "OFFICECODE", new String[]{"4",
				"6", "7"} );
		testFilter( "OFFICECODE", OP_TOP_N, "2", new String[]{"6", "7"} );
		testFilter( "OFFICECODE", OP_TOP_PERCENT, "30", new String[]{"6", "7"} );
		testNoOperandFilter( "row[\"OFFICECODE\"] != 7", OP_TRUE, "OFFICECODE",
				new String[]{"1", "2", "3", "4", "5", "6"} );
	}

	private void testNoOperandFilter( String expression, int operator, String expectedColumn,
			String[] expectedResult ) throws EngineException, BirtException
	{
		IFilterDefinition[] simpleFilterExpression = createFilter( expression,
				operator );
		testFilterCondition( simpleFilterExpression, expectedColumn,
				expectedResult );
	}

	private void testFilter( String columnName, int operator, Object operand1,
			String expectedResult ) throws EngineException, BirtException
	{
		testFilter( columnName, operator, operand1,
				new String[]{expectedResult} );
	}
	
	private void testFilter( String columnName, int operator, Object operand1,
			String[] expectedResult ) throws EngineException,
			BirtException {
		testFilter(columnName, operator, operand1, null, columnName, expectedResult );
	}
	
	private void testFilter( String columnName, int operator, Object operand1,
			String expectedColumn, String[] expectedResult )
			throws EngineException, BirtException
	{
		testFilter( columnName, operator, operand1, null, expectedColumn,
				expectedResult );
	}
	
	private void testFilter( String columnName, int operator, Object operand1,
			Object operand2, String expectedColumn, String[] expectedResult ) throws EngineException,
			BirtException
	{
		IFilterDefinition[] simpleFilterExpression = createFilter( columnName,
				operator, operand1, operand2 );
		testFilterCondition( simpleFilterExpression, expectedColumn,
				expectedResult );
	}

	private void testFilterCondition(
			IFilterDefinition[] simpleFilterExpression, String expectedColumn,
			String[] expectedResult ) throws EngineException, BirtException
	{
		dataExTask.setFilters( simpleFilterExpression );
		IExtractionResults results = dataExTask.extract( );
		IDataIterator iterator = results.nextResultIterator( );
		
		List<String> actualResults = new ArrayList<String>();
		while( iterator.next( ) )
		{
			actualResults.add( iterator.getValue( expectedColumn ).toString() );
		}
		iterator.close( );
		assertEquals( expectedResult.length, actualResults.size( ));
		for ( int i = 0; i < expectedResult.length; i++ )
		{
			assertEquals( expectedResult[i], actualResults.get( i ) );
		}
	}
	
	private void doTestExtractionTaskWithFilters( int resultSetCount ) throws EngineException,
			BirtException
	{
		ArrayList resultSetList = (ArrayList) dataExTask
				.getResultSetList( );

		assertEquals( resultSetCount, resultSetList.size( ) );
		// creat filters
		IFilterDefinition[] FilterExpression = new IFilterDefinition[2];
		FilterExpression[0] = new FilterDefinition(
				new ConditionalExpression(
						"row[\"CUSTOMERNUMBER\"]",
						IConditionalExpression.OP_GE, "201", null ) );
		FilterExpression[1] = new FilterDefinition(
				new ConditionalExpression(
						"row[\"CUSTOMERNUMBER\"]",
						IConditionalExpression.OP_LT, "300", null ) );
		// add filters to dataExtractionTask
		dataExTask.setFilters( FilterExpression );

		IExtractionResults result = dataExTask.extract( );

		String value = null;
		if ( result != null )
		{
			IDataIterator iData = result.nextResultIterator( );
			if ( iData != null )
			{
				iData.next( );
				try
				{
					value = (String) DataTypeUtil.convert( iData
							.getValue( "CUSTOMERNUMBER" ),
							DataType.STRING_TYPE );
				}
				catch ( Exception e )
				{
					value = null;
				}
				assertEquals( "201", value );

				while ( iData.next( ) )
				{
					try
					{
						value = (String) DataTypeUtil.convert(
								iData.getValue( "CUSTOMERNUMBER" ),
								DataType.STRING_TYPE );
					}
					catch ( Exception e )
					{
						value = null;
					}
				}
			}
		}
		assertEquals( "299", value );
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

	private Set<InstanceID> getAllInstanceIds( IReportDocument document )
			throws EngineException, UnsupportedEncodingException
	{
		Set<InstanceID> instanceIds = new HashSet<InstanceID>();
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
			instanceIds.add( iid );
		}
		return instanceIds;
	}
	
	private IFilterDefinition[] createFilter( String columnName, int operator,
			Object operand )
	{
		return createFilter( columnName, operator, operand, null );
	}

	private IFilterDefinition[] createFilter( String expression, int operator ){
		ConditionalExpression conditionalExpression = new ConditionalExpression(
				expression, operator );
		return new IFilterDefinition[]{new FilterDefinition(
				conditionalExpression )};
	}
	
	private IFilterDefinition[] createFilter( String columnName, int operator,
			Object operand1, Object operand2 )
	{
		ConditionalExpression conditionalExpression = null;
		if ( ! (operand1 instanceof String[]) )
		{
			conditionalExpression = new ConditionalExpression( "row[\"" + columnName + "\"]",
					operator, (String)operand1, (String)operand2 );
		}
		else
		{
			List<String> operand = Arrays.asList( (String[])operand1 );
			conditionalExpression = new ConditionalExpression( "row[\"" + columnName + "\"]",
					operator, operand);
		}
		return new IFilterDefinition[]{new FilterDefinition(
				conditionalExpression )};
	}
}