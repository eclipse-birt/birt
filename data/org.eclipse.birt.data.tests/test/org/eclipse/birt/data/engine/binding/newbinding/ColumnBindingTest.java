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
package org.eclipse.birt.data.engine.binding.newbinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.core.archive.FileArchiveReader;
import org.eclipse.birt.core.archive.FileArchiveWriter;
import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.APITestCase;
import org.eclipse.birt.data.engine.api.DataEngine;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IComputedColumn;
import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.ISortDefinition;
import org.eclipse.birt.data.engine.api.querydefn.BaseQueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.Binding;
import org.eclipse.birt.data.engine.api.querydefn.ComputedColumn;
import org.eclipse.birt.data.engine.api.querydefn.ConditionalExpression;
import org.eclipse.birt.data.engine.api.querydefn.FilterDefinition;
import org.eclipse.birt.data.engine.api.querydefn.GroupDefinition;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.api.querydefn.SortDefinition;
import org.eclipse.birt.data.engine.api.querydefn.SubqueryDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.DataEngineImpl;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import testutil.ConfigText;

import com.ibm.icu.util.TimeZone;

/**
 * Notice:
 * 
 * dataSetRow -> dataSetRow row -> row
 * 
 * Please notice: dataSetRow can only be used in the column binding. In other
 * cases, only row is allowed to be used.
 * 
 * Here simple or complicated test cases can be added easily.
 */
public class ColumnBindingTest extends APITestCase
{
	private TimeZone currentTimeZone = TimeZone.getDefault( );
	/**
	 * Column info
	 * 
	 * COUNTRY,CITY,SALE_DATE,AMOUNT,ORDERED,NULL_COLUMN
	 * @throws Exception 
	 */
	
	/**
	 * 
	 */
	public void setUp() throws Exception
	{
		super.setUp( );
		
		TimeZone.setDefault( TimeZone.getTimeZone("GMT+0")  );
	}
	
	/**
	 * @throws Exception 
	 * 
	 */
	public void tearDown() throws Exception
	{
		super.tearDown();
		TimeZone.setDefault( this.currentTimeZone  );
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.api.APITestCase#getDataSourceInfo()
	 */
	protected DataSourceInfo getDataSourceInfo( )
	{
		return new DataSourceInfo( ConfigText.getString( "Binding.TestData.TableName" ),
				ConfigText.getString( "Binding.TestData.TableSQL" ),
				ConfigText.getString( "Binding.TestData.TestDataFileName" ) );
	}
	
	/**
	 * Without any transformation
	 * 
	 * @throws Exception
	 */
	public void testBasic( ) throws Exception
	{
		QueryDefinition queryDefn = newReportQuery( );

		// column mapping
		String[] name = new String[]{
				"testColumn1", "testColumn2", "AMOUNT1"
		};
		ScriptExpression[] se = new ScriptExpression[name.length];
		se[0] = new ScriptExpression( "dataSetRow.COUNTRY" );
		se[1] = new ScriptExpression( "dataSetRow.CITY" );
		se[2] = new ScriptExpression( "dataSetRow.AMOUNT" );
		for ( int i = 0; i < name.length; i++ )
			queryDefn.addBinding( new Binding(name[i], se[i] ));

		IResultIterator ri = executeQuery( queryDefn );
		while ( ri.next( ) )
		{
			String str = "";
			for ( int i = 0; i < name.length; i++ )
			{
				str += ri.getValue( name[i] );

				if ( i < name.length - 1 )
					str += ", ";
			}
			testPrintln( str );
		}
		
		checkOutputFile( );
	}
	
	/**
	 * Test the consistency of Data Type Info.
	 * 
	 * @throws Exception
	 */
	public void testBasic1( ) throws Exception
	{
		QueryDefinition queryDefn = newReportQuery( );

		// column mapping
		String[] name = new String[]{
				"AMOUNT1",
				"AMOUNT2"
		};
		ScriptExpression[] se = new ScriptExpression[name.length];
		se[0] = new ScriptExpression( "dataSetRow.AMOUNT" );
		se[0].setDataType( DataType.STRING_TYPE );
		se[1] = new ScriptExpression( "row.AMOUNT1");
		se[1].setDataType( DataType.UNKNOWN_TYPE );
		for ( int i = 0; i < name.length; i++ )
			queryDefn.addBinding( new Binding(name[i], se[i] ));

		IResultIterator ri = executeQuery( queryDefn );
		while ( ri.next( ) )
		{
			assertTrue(ri.getValue( "AMOUNT2" ) instanceof String );
		}
		
	}
	
	/**
	 * @throws Exception
	 */
	public void testBasic2( ) throws Exception
	{
		QueryDefinition queryDefn = newReportQuery( );

		// column mapping
		String[] name = new String[]{
				"testColumn1", "testColumn2", "AMOUNT1"
		};
		ScriptExpression[] se = new ScriptExpression[name.length];
		se[0] = new ScriptExpression( "dataSetRow.COUNTRY" );
		se[1] = new ScriptExpression( "row." + name[0] );
		se[2] = new ScriptExpression( "dataSetRow.AMOUNT" );
		for ( int i = 0; i < name.length; i++ )
			queryDefn.addBinding( new Binding(name[i], se[i] ));

		IResultIterator ri = executeQuery( queryDefn );
		while ( ri.next( ) )
		{
			String str = "";
			for ( int i = 0; i < name.length; i++ )
			{
				str += ri.getValue( name[i] );

				if ( i < name.length - 1 )
					str += ", ";
			}
			testPrintln( str );
		}
		
		checkOutputFile( );
	}
	
	/**
	 * @throws Exception
	 */
	public void testBindingNameWithDoubleQuote( ) throws Exception
	{
		QueryDefinition queryDefn = newReportQuery( );

		// column mapping
		String[] name = new String[]{
				"test\"Column1", "test\"Column2", "AMOUNT1"
		};
		ScriptExpression[] se = new ScriptExpression[name.length];
		se[0] = new ScriptExpression( "dataSetRow.COUNTRY" );
		se[1] = new ScriptExpression( "row[\"test\\\"Column1\"]" );
		se[2] = new ScriptExpression( "dataSetRow.AMOUNT" );
		for ( int i = 0; i < name.length; i++ )
			queryDefn.addBinding( new Binding(name[i], se[i] ));

		IResultIterator ri = executeQuery( queryDefn );
		while ( ri.next( ) )
		{
			String str = "";
			for ( int i = 0; i < name.length; i++ )
			{
				str += ri.getValue( name[i] );

				if ( i < name.length - 1 )
					str += ", ";
			}
			testPrintln( str );
		}
		
		checkOutputFile( );
	}
	
	/**
	 * Filtering on data set, without total
	 * 
	 * @throws Exception
	 */
	public void testFilterOnDataSet( ) throws Exception
	{
		IBaseExpression baseExpr = new ScriptExpression( "row.AMOUNT > 100" );
		IFilterDefinition filterDefn = new FilterDefinition( baseExpr );
		this.dataSet.addFilter( filterDefn );
		
		QueryDefinition queryDefn = newReportQuery( );

		// column mapping
		String[] name = new String[]{
				"testColumn1", "testColumn2", "AMOUNT1"
		};
		ScriptExpression[] se = new ScriptExpression[name.length];
		se[0] = new ScriptExpression( "dataSetRow.COUNTRY" );
		se[1] = new ScriptExpression( "dataSetRow.CITY" );
		se[2] = new ScriptExpression( "dataSetRow.AMOUNT" );
		for ( int i = 0; i < name.length; i++ )
			queryDefn.addBinding( new Binding(name[i], se[i] ));

		IResultIterator ri = executeQuery( queryDefn );
		while ( ri.next( ) )
		{
			String str = "";
			for ( int i = 0; i < name.length; i++ )
			{
				str += ri.getValue( name[i] );

				if ( i < name.length - 1 )
					str += ", ";
			}
			testPrintln( str );
		}
		
		checkOutputFile( );
	}
	
	/**
	 * Filtering on data set, without total
	 * 
	 * @throws Exception
	 */
	public void testFilterOnDateType( ) throws Exception
	{
		
		FilterDefinition filterDefn = new FilterDefinition( new ConditionalExpression( "row.SALE_DATE",
				ConditionalExpression.OP_BETWEEN,
				"\'2004-05-01 00:00:00\'",
				"\'2004-06-05 00:00:00\'" ) );
		this.dataSet.addFilter( filterDefn );
		
		QueryDefinition queryDefn = newReportQuery( );

		// column mapping
		String[] name = new String[]{
				"testColumn1", "testColumn2", "SALE_DATE"
		};
		ScriptExpression[] se = new ScriptExpression[name.length];
		se[0] = new ScriptExpression( "dataSetRow.COUNTRY" );
		se[1] = new ScriptExpression( "dataSetRow.CITY" );
		se[2] = new ScriptExpression( "dataSetRow.SALE_DATE" );
		for ( int i = 0; i < name.length; i++ )
			queryDefn.addBinding( new Binding(name[i], se[i] ));
		
		IResultIterator ri = executeQuery( queryDefn );
		while ( ri.next( ) )
		{
			String str = "";
			for ( int i = 0; i < name.length; i++ )
			{
				str += ri.getValue( name[i] );

				if ( i < name.length - 1 )
					str += ", ";
			}
			testPrintln( str );
		}
		
		checkOutputFile( );
	}
	
	/**
	 * TODO: Filtering on data set, with total
	 * 
	 * @throws Exception
	 */
	public void testFilterOnDataSet2( ) throws Exception
	{
	}
	
	/**
	 * Computed column on data set, without total
	 * 
	 * @throws Exception
	 */
	public void testComputedOnDataSet( ) throws Exception
	{
		IComputedColumn cc = new ComputedColumn( "AMOUNT2", "row.AMOUNT*2" );
		this.dataSet.addComputedColumn( cc );
		
		QueryDefinition queryDefn = newReportQuery( );
		
		// column mapping
		String[] name = new String[]{
				"testColumn1", "testColumn2", "AMOUNT1", "AMOUNT2"
		};
		ScriptExpression[] se = new ScriptExpression[name.length];
		se[0] = new ScriptExpression( "dataSetRow.COUNTRY" );
		se[1] = new ScriptExpression( "dataSetRow.CITY" );
		se[2] = new ScriptExpression( "dataSetRow.AMOUNT" );
		se[3] = new ScriptExpression( "dataSetRow.AMOUNT2" );
		for ( int i = 0; i < name.length; i++ )
			queryDefn.addBinding( new Binding(name[i], se[i] ));

		IResultIterator ri = executeQuery( queryDefn );
		while ( ri.next( ) )
		{
			String str = "";
			for ( int i = 0; i < name.length; i++ )
			{
				str += ri.getValue( name[i] );

				if ( i < name.length - 1 )
					str += ", ";
			}
			testPrintln( str );
		}
		
		checkOutputFile( );
	}
	
	/**
	 * TODO: Filtering on data set, with total
	 * 
	 * @throws Exception
	 */
	public void testComputedOnDataSet2( ) throws Exception
	{		
	}
	
	/**
	 * Sort on table
	 * 
	 * @throws Exception
	 */
	public void testFilterOnTable( ) throws Exception
	{
		QueryDefinition queryDefn = newReportQuery( );

		// column mapping
		String[] name = new String[]{
				"testColumn1", "testColumn2", "AMOUNT1"
		};
		ScriptExpression[] se = new ScriptExpression[name.length];
		se[0] = new ScriptExpression( "dataSetRow.COUNTRY" );
		se[1] = new ScriptExpression( "dataSetRow.CITY" );
		se[2] = new ScriptExpression( "dataSetRow.AMOUNT" );
		for ( int i = 0; i < name.length; i++ )
			queryDefn.addBinding( new Binding(name[i], se[i] ));
		
		ScriptExpression filterExpr = new ScriptExpression( "row.AMOUNT1>100" );
		FilterDefinition filterDefn = new FilterDefinition( filterExpr );
		queryDefn.addFilter( filterDefn );
		
		IResultIterator ri = executeQuery( queryDefn );
		while ( ri.next( ) )
		{
			String str = "";
			for ( int i = 0; i < name.length; i++ )
			{
				str += ri.getValue( name[i] );

				if ( i < name.length - 1 )
					str += ", ";
			}
			testPrintln( str );
		}
	}
	
	/**
	 * Test without data set
	 * 
	 * @throws Exception
	 */
	public void testNoDataSet( ) throws Exception
	{
		String[] name = new String[]{
				"testColumn1", "testColumn2"
		};
		int[] dataType = new int[]{
				DataType.INTEGER_TYPE, DataType.DOUBLE_TYPE
		};
		ScriptExpression[] se = new ScriptExpression[]{
				new ScriptExpression( "i=10", dataType[0] ),
				new ScriptExpression( "i=20", dataType[1] ),
		};

		basicTestNoDataSet( name, dataType, se );

		this.checkOutputFile( );
	}
	
	/**
	 * Test without data set, with Java Script Object of NativeDate
	 * 
	 * @throws Exception
	 */
	public void testNoDataSet2( ) throws Exception
	{
		String[] name = new String[]{
				"testColumn1", "testColumn2"
		};
		int[] dataType = new int[]{
				DataType.ANY_TYPE, DataType.ANY_TYPE
		};
		ScriptExpression[] se = new ScriptExpression[]{
				new ScriptExpression( "new Date()", dataType[0] ),
				new ScriptExpression( "row[\"testColumn1\"].getFullYear( )",
						dataType[1] ),
		};

		basicTestNoDataSet( name, dataType, se );
	}
	
	/**
	 * Test without data set, with Java Script Object of NativeDate and
	 * DataType.DATE_TYPE
	 * 
	 * @throws Exception
	 */
	public void testNoDataSet3( ) throws Exception
	{
		String[] name = new String[]{
			"testColumn1"
		};
		int[] dataType = new int[]{
			DataType.DATE_TYPE
		};
		ScriptExpression[] se = new ScriptExpression[]{
			new ScriptExpression( "new Date()", dataType[0] ),
		};

		basicTestNoDataSet( name, dataType, se );
	}
	
	/**
	 * @param name
	 * @param dataType
	 * @param se
	 * @throws BirtException
	 */
	private void basicTestNoDataSet( String[] name, int[] dataType,
			ScriptExpression[] se ) throws BirtException
	{
		DataEngine dataEngine = new DataEngineImpl( DataEngineContext.newInstance( DataEngineContext.DIRECT_PRESENTATION,
				null,
				null,
				null ) );
		QueryDefinition queryDefn = new QueryDefinition( );

		for ( int i = 0; i < name.length; i++ )
			queryDefn.addBinding( new Binding(name[i], se[i] ));

		IResultIterator ri = dataEngine.prepare( queryDefn )
				.execute( null )
				.getResultIterator( );
		if ( ri.next( ) )
		{
			String str = "";
			for ( int i = 0; i < name.length; i++ )
			{
				Object value = ri.getValue( name[i] );
				str += value;

				if ( i < name.length - 1 )
					str += ", ";

				if ( dataType[i] == DataType.INTEGER_TYPE )
					assertTrue( value.getClass( ).equals( Integer.class ) );
				else if ( dataType[i] == DataType.DOUBLE_TYPE )
					assertTrue( value.getClass( ).equals( Double.class ) );
				else if ( dataType[i] == DataType.DATE_TYPE )
					assertTrue( value.getClass( ).equals( Date.class ) );
			}
			testPrintln( str );
		}
	}
	
	/**
	 * @throws Exception
	 */
	public void testNoDataSetWithNestedQuery( ) throws Exception
	{
		// outer query without data set
		String[] name = new String[]{
			"testColumn1"
		};
		IQueryResults queryResult = null;
		
		{
			int[] dataType = new int[]{
				DataType.DATE_TYPE
			};
			ScriptExpression[] se = new ScriptExpression[]{
				new ScriptExpression( "new Date()", dataType[0] ),
			};

			DataEngine myDataEngine = new DataEngineImpl( DataEngineContext.newInstance( DataEngineContext.DIRECT_PRESENTATION,
					null,
					null,
					null ) );
			QueryDefinition queryDefn = new QueryDefinition( );

			for ( int i = 0; i < name.length; i++ )
				queryDefn.addBinding( new Binding(name[i], se[i] ));

			IResultIterator ri = myDataEngine.prepare( queryDefn )
					.execute( null )
					.getResultIterator( );
			queryResult = ri.getQueryResults( );
		}
		
		// inner query with data set
		QueryDefinition queryDefn2 = this.newReportQuery( );
		for ( int i = 0; i < name.length; i++ )
			queryDefn2.addBinding( new Binding(name[i], new ScriptExpression( "row._outer." + name[i]) ));
			
		IResultIterator ri2 = this.dataEngine.prepare( queryDefn2 )
				.execute( queryResult, null )
				.getResultIterator( );
		if ( ri2.next( ) )
		{
			String str = "";
			for ( int i = 0; i < name.length; i++ )
			{
				Object value = ri2.getValue( name[i] );
				str += value;
				
				if ( i < name.length - 1 )
					str += ", ";
			}
			testPrintln( str );
		}
		ri2.close();
	}
	
	/**
	 * @throws Exception
	 */
	public void testNoDataSetWithSubQuery( ) throws Exception
	{
		// outer query without data set
		int[] dataType = new int[]{
			DataType.DATE_TYPE
		};
		String[] name = new String[]{
			"testColumn1"
		};
		
		IResultIterator ri2 = null;
		{
			ScriptExpression[] se = new ScriptExpression[]{
				new ScriptExpression( "new Date()", dataType[0] ),
			};

			QueryDefinition queryDefn = new QueryDefinition( );
			for ( int i = 0; i < name.length; i++ )
				queryDefn.addBinding( new Binding(name[i], se[i] ));
			
			// sub query
			String subQueryName = "TEST";
			SubqueryDefinition subQueryDefn = new SubqueryDefinition( subQueryName, queryDefn );
			for ( int i = 0; i < name.length; i++ )
				subQueryDefn.addBinding( new Binding(name[i], 
						new ScriptExpression( "row._outer." + name[i],
								dataType[i] ) ));
			queryDefn.addSubquery( subQueryDefn );

			DataEngine myDataEngine = new DataEngineImpl( DataEngineContext.newInstance( DataEngineContext.DIRECT_PRESENTATION,
					null,
					null,
					null ) );
			IResultIterator ri = myDataEngine.prepare( queryDefn )
					.execute( null )
					.getResultIterator( );
			ri.next( );
			ri2 = ri.getSecondaryIterator( subQueryName, null );
		}
		
		if ( ri2.next( ) )
		{
			String str = "";
			for ( int i = 0; i < name.length; i++ )
			{
				Object value = ri2.getValue( name[i] );
				str += value;

				if ( i < name.length - 1 )
					str += ", ";

				if ( dataType[0] == DataType.DATE_TYPE )
					assertTrue( value.getClass( ).equals( Date.class ) );
			}
			testPrintln( str );
		}
		ri2.close();
	}
	
	/**
	 * @throws Exception
	 */
	public void testAutoBinding( ) throws Exception
	{
		QueryDefinition queryDefn = newReportQuery( true );
		this.dataSet.addComputedColumn( new ComputedColumn("COUN\"TRY", "row[\"COUNTRY\"]") );

		// column mapping
		String[] name = new String[]{
				"COUN\"TRY", "CITY", "AMOUNT"
		};
		
		IResultIterator ri = executeQuery( queryDefn );
		while ( ri.next( ) )
		{
			String str = "";
			for ( int i = 0; i < name.length; i++ )
			{
				str += ri.getValue( name[i] );

				if ( i < name.length - 1 )
					str += ", ";
			}
			testPrintln( str );
		}
		ri.close();
		checkOutputFile( );
	}
	
	/**
	 * @throws Exception
	 */
	public void testAccessGroupColumn( ) throws Exception
	{
		QueryDefinition queryDefn = newReportQuery( );

		// column mapping
		String[] name = new String[]{
				"testColumn1", "testColumn2", "AMOUNT1"
		};
		ScriptExpression[] se = new ScriptExpression[name.length];
		se[0] = new ScriptExpression( "dataSetRow.COUNTRY" );
		se[1] = new ScriptExpression( "dataSetRow.CITY" );
		se[2] = new ScriptExpression( "dataSetRow.AMOUNT" );
		for ( int i = 0; i < name.length; i++ )
			queryDefn.addBinding( new Binding(name[i], se[i] ));

		GroupDefinition groupDefn = new GroupDefinition( "group1" );
		groupDefn.setKeyColumn( "testColumn1" );
		String name2 = "testColumn3";
		ScriptExpression se2 = new ScriptExpression( "Total.sum(dataSetRow.AMOUNT)" );
		se2.setGroupName("group1");
		//groupDefn.addResultSetExpression( name2, se2 );
		queryDefn.addBinding( new Binding(name2, se2) );
		
		queryDefn.addGroup( groupDefn );
		
		IResultIterator ri = executeQuery( queryDefn );
		while ( ri.next( ) )
		{
			String str = "";
			for ( int i = 0; i < name.length; i++ )
			{
				str += ri.getValue( name[i] );
				str += ", ";
			}
			str += ri.getValue( name2 );
				
			testPrintln( str );
		}
		
		checkOutputFile( );
	}
	
	/**
	 * @throws Exception
	 */
	public void testSpecialExpression( ) throws Exception
	{
		IComputedColumn cc = new ComputedColumn( "AMOUNT2", "row.AMOUNT*2" );
		this.dataSet.addComputedColumn( cc );

		QueryDefinition queryDefn = newReportQuery( );

		// column mapping
		String[] name = new String[]{
			"testColumn1"
		};
		ScriptExpression[] se = new ScriptExpression[name.length];
		se[0] = new ScriptExpression( "if ( 2<1 ){ true;  }else{ false;}" );

		SortDefinition[] sortDefn = new SortDefinition[]{
			new SortDefinition( )
		};
		sortDefn[0].setExpression( "row.testColumn1" );
		sortDefn[0].setSortDirection( ISortDefinition.SORT_DESC );

		for ( int i = 0; i < name.length; i++ )
			queryDefn.addBinding( new Binding(name[i], se[i] ));
		for ( int i = 0; i < sortDefn.length; i++ )
		{
			queryDefn.addSort( sortDefn[i] );
		}

		IResultIterator ri = executeQuery( queryDefn );
		while ( ri.next( ) )
		{
			String str = "";
			for ( int i = 0; i < name.length; i++ )
			{
				str += ri.getValue( name[i] );

				if ( i < name.length - 1 )
					str += ", ";
			}
			testPrintln( str );
		}
		
		checkOutputFile( );
	}
	
	/**
	 * @throws Exception
	 */
	public void testSpecialExpression2( ) throws Exception
	{
		QueryDefinition queryDefn = newReportQuery( );

		// column mapping
		String[] name = new String[]{"AMOUNT",
			"testColumn1"
		};
		ScriptExpression[] se = new ScriptExpression[name.length];
		se[0] = new ScriptExpression( "dataSetRow.AMOUNT" );
		se[1] = new ScriptExpression( "if ( row.AMOUNT >200 ){ Total.runningSum(row.AMOUNT);  }else{ row.AMOUNT;}" );

		for ( int i = 0; i < name.length; i++ )
			queryDefn.addBinding( new Binding(name[i], se[i] ));

		IResultIterator ri = executeQuery( queryDefn );
		while ( ri.next( ) )
		{
			String str = "";
			for ( int i = 0; i < name.length; i++ )
			{
				str += ri.getValue( name[i] );

				if ( i < name.length - 1 )
					str += ", ";
			}
			testPrintln( str );
		}
		
		checkOutputFile( );
	}
	
	/**
	 * @throws Exception 
	 * 
	 * 
	 */
	public void testSpecialExpression3( ) throws Exception
	{
		QueryDefinition queryDefn = newReportQuery( );
		// column mapping
		String[] name = new String[]{
				"AMOUNT", "testColumn1", "testColumn2"
		};
		ScriptExpression[] se = new ScriptExpression[name.length];
		se[0] = new ScriptExpression( "dataSetRow.AMOUNT" );
		se[1] = new ScriptExpression( "var p=dataSetRow.AMOUNT+1;if( p >200 ){\"A large amount!\";  } else{ \"A small amount!\";}" );
		se[2] = new ScriptExpression( "row[\"testColumn1\"]+dataSetRow.AMOUNT" );

		for ( int i = 0; i < name.length; i++ )
			queryDefn.addBinding( new Binding(name[i], se[i] ));
		
		IResultIterator ri = executeQuery( queryDefn );
		while ( ri.next( ) )
		{
			String str = "";
			for ( int i = 0; i < name.length; i++ )
			{
				str += ri.getValue( name[i] );

				if ( i < name.length - 1 )
					str += ", ";
			}
			testPrintln( str );
		}
		
		checkOutputFile( );

	}
	
	/**
	 * 
	 * @throws Exception
	 */
	public void testInvalidSort( ) throws Exception
	{
		for( int i = 0; i < 4; i++ )
			this.testInvalidSort( i );
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	private void testInvalidSort( int sortIndex ) throws Exception
	{
		QueryDefinition queryDefn = newReportQuery( );
		// column mapping
		String[] name = new String[]{
				"rownum1", "rownum2", "rownum3"
		};

		ScriptExpression[] se = new ScriptExpression[name.length];
		se[0] = new ScriptExpression( "row.__rownum" );
		se[1] = new ScriptExpression( "row.rownum1" );
		se[2] = new ScriptExpression( "row[\"rownum2\"]" );

		for ( int i = 0; i < name.length; i++ )
			queryDefn.addBinding( new Binding( name[i], se[i] ) );

		SortDefinition[] sort = new SortDefinition[name.length+1];
		sort[0] = new SortDefinition();
		sort[0].setExpression( "row.rownum1" );
		sort[1] = new SortDefinition();
		sort[1].setExpression( "row.rownum2" );
		sort[2] = new SortDefinition();
		sort[2].setExpression( "row.rownum3" );
		sort[3] = new SortDefinition();
		sort[3].setExpression( "row.__rownum" );
		
		queryDefn.addSort( sort[sortIndex] );
			
		try
		{
			executeQuery( queryDefn );
			fail( "Should not arrive here" );
		}
		catch ( DataException e )
		{
		}
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	public void testInvalidFilter( ) throws Exception
	{
		for( int i = 0; i < 4; i++ )
			this.testInvalidFilter( i );
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	private void testInvalidFilter( int filterIndex ) throws Exception
	{
		QueryDefinition queryDefn = newReportQuery( );
		// column mapping
		String[] name = new String[]{
				"rownum1", "rownum2", "rownum3"
		};

		ScriptExpression[] se = new ScriptExpression[name.length];
		se[0] = new ScriptExpression( "row.__rownum" );
		se[1] = new ScriptExpression( "row.rownum1" );
		se[2] = new ScriptExpression( "row[\"rownum2\"]" );

		for ( int i = 0; i < name.length; i++ )
			queryDefn.addBinding( new Binding( name[i], se[i] ) );

		FilterDefinition[] filter = new FilterDefinition[name.length+1];
		filter[0] = new FilterDefinition(new ScriptExpression("row.rownum1 == 1"));
		filter[1] = new FilterDefinition(new ScriptExpression("row.rownum2 == 1"));
		filter[2] = new FilterDefinition(new ScriptExpression("row.rownum3 == 1"));
		filter[3] = new FilterDefinition(new ScriptExpression("row.__rownum == 1"));
		queryDefn.addFilter( filter[filterIndex] );
			
		try
		{
			executeQuery( queryDefn );
			fail( "Should not arrive here" );
		}
		catch ( DataException e )
		{
		}
	}
	/**
	 * @throws Exception
	 */
	public void testGroup( ) throws Exception
	{
		QueryDefinition queryDefn = newReportQuery( );

		// column mapping
		String[] name = new String[]{
				"testColumn1", "testColumn2", "AMOUNT1"
		};
		ScriptExpression[] se = new ScriptExpression[name.length];
		se[0] = new ScriptExpression( "dataSetRow.COUNTRY" );
		se[1] = new ScriptExpression( "dataSetRow.CITY" );
		se[2] = new ScriptExpression( "dataSetRow.AMOUNT" );
		for ( int i = 0; i < name.length; i++ )
			queryDefn.addBinding( new Binding(name[i], se[i] ));

		GroupDefinition groupDefn = new GroupDefinition( "group1" );
		groupDefn.setKeyColumn( "testColumn1" );
		String name2 = "testColumn3";
		ScriptExpression se2 = new ScriptExpression( "Total.sum(dataSetRow.AMOUNT)" );
		se2.setGroupName("group1");
		//groupDefn.addResultSetExpression( name2, new ColumnBindingExpression(se2) );
		queryDefn.addBinding( new Binding( name2,se2 ));
		queryDefn.addGroup( groupDefn );
		String name3 = "testColumn4";
		ScriptExpression se3 = new ScriptExpression( "row[\"testColumn1\"]");
		GroupDefinition groupDefn1 = new GroupDefinition("group2");
		groupDefn1.setKeyColumn( "testColumn4" );
		se3.setGroupName("group2");
		//groupDefn1.addResultSetExpression( name3, new ColumnBindingExpression(se3) );
		queryDefn.addBinding( new Binding(name3, se3) );
		queryDefn.addGroup( groupDefn1 );
		
		IResultIterator ri = executeQuery( queryDefn );
		while ( ri.next( ) )
		{
			String str = "";
			for ( int i = 0; i < name.length; i++ )
			{
				str += ri.getValue( name[i] );
				str += ", ";
			}
			str += ri.getValue( name2 );
				
			testPrintln( str );
		}
		
		checkOutputFile( );
	}
	
	/**
	 * @throws Exception
	 */
	public void testInvalidBinding( ) throws Exception
	{
		QueryDefinition queryDefn = newReportQuery( );

		// column mapping
		String[] name = new String[]{
				"testColumn1"
		};
		ScriptExpression[] se = new ScriptExpression[name.length];
		se[0] = new ScriptExpression( "dataSetRow.COUNTRY" );
		for ( int i = 0; i < name.length; i++ )
			queryDefn.addBinding( new Binding(name[i], se[i] ));

		IResultIterator ri = executeQuery( queryDefn );
		if ( ri.next( ) )
		{
			try
			{
				ri.getValue( name[0] );
			}
			catch ( BirtException e )
			{
				assertTrue( e.getErrorCode( ) == ResourceConstants.INVALID_JS_EXPR );
			}
		}
		ri.close();
	}
	
	/**
	 * @throws Exception
	 */
	public void testBlankExpression( ) throws Exception
	{
		QueryDefinition queryDefn = newReportQuery( );

		// column mapping
		String[] name = new String[]{
				"testColumn1"
		};
		ScriptExpression[] se = new ScriptExpression[name.length];
		se[0] = new ScriptExpression( null );
		
		for ( int i = 0; i < name.length; i++ )
			queryDefn.addBinding( new Binding(name[i], se[i] ));
		
		DataException error = null;
		try
		{
			executeQuery( queryDefn );
			fail( "Should not arrive here");
		}
		catch ( DataException e )
		{
//			assertTrue( e.getErrorCode( ) == ResourceConstants.EXPRESSION_CANNOT_BE_NULL_OR_BLANK );
			error = e;
		}
		
		assertNotNull( error );
	
	}
	
	//----------------report document test---------------------
	
	private FileArchiveWriter archiveWriter;
	private FileArchiveReader archiveReader;
	
	private String[] rowExprName;
	private String[] totalExprName;
	
	private String queryResultID;
	private List expectedValue;
	
	private DataEngine myGenDataEngine;
	private DataEngine myPreDataEngine;
	
	/**
	 * @throws Exception
	 */
	public void testBasicReportDocument( ) throws Exception
	{
		String fileName = getOutputFolder( ) + "testData";
		DataEngineContext deContext1 = newContext( DataEngineContext.MODE_GENERATION,
				fileName );
		myGenDataEngine = DataEngine.newDataEngine( deContext1 );
		
		myGenDataEngine.defineDataSource( this.dataSource );
		myGenDataEngine.defineDataSet( this.dataSet );
		
		this.genBasic( );
		this.closeArchiveWriter( );
		
		DataEngineContext deContext2 = newContext( DataEngineContext.MODE_PRESENTATION,
				fileName );
		myPreDataEngine = DataEngine.newDataEngine( deContext2 );
		
		this.preBasic( );
		this.closeArchiveReader( );
		this.checkOutputFile( );
	}
	
	/**
	 * @throws Exception
	 */
	private void genBasic( ) throws Exception
	{	
		expectedValue = new ArrayList( );
		
		Context context = Context.enter( );		
		Scriptable scope = context.initStandardObjects( );		
		Context.exit( );
		
		//------------generation----------------
		QueryDefinition qd = newReportQuery( );
		
		// prepare
		IBaseExpression[] rowBeArray = getRowExpr( );
		IBinding[] totalBeArray = getAggrBinding( );
		prepareExprNameAndQuery( rowBeArray, totalBeArray, qd );
		
		// generation
		IQueryResults qr = myGenDataEngine.prepare( qd ).execute( scope );
		
		// important step
		queryResultID = qr.getID( );
		
		IResultIterator ri = qr.getResultIterator( );		
		while ( ri.next( ) )
		{
			for ( int i = 0; i < rowBeArray.length; i++ )
				expectedValue.add( ri.getValue( this.rowExprName[i] ) );
				
			for ( int i = 0; i < totalBeArray.length; i++ )
				expectedValue.add( ri.getValue( this.totalExprName[i] ) );
		}
		
		ri.close( );
		qr.close( );
		myGenDataEngine.shutdown( );
	}
	
	/**
	 * @throws Exception
	 */
	private void genSerializable( ) throws Exception
	{	
		Context context = Context.enter( );		
		Scriptable scope = context.initStandardObjects( );		
		Context.exit( );
		
		QueryDefinition queryDefn = newReportQuery( );

		// column mapping
		String[] name = new String[]{
				"serializable"
		};
		ScriptExpression[] se = new ScriptExpression[name.length];
		se[0] = new ScriptExpression( "new java.lang.StringBuffer(\"ss\")" );
		se[0].setDataType( DataType.JAVA_OBJECT_TYPE );
		for ( int i = 0; i < name.length; i++ )
			queryDefn.addBinding( new Binding(name[i], se[i] ));
		
		// generation
		IQueryResults qr = myGenDataEngine.prepare( queryDefn ).execute( scope );
		
		// important step
		queryResultID = qr.getID( );
		
		IResultIterator ri = qr.getResultIterator( );	
		assertEquals( DataType.JAVA_OBJECT_TYPE, ri.getResultMetaData( ).getColumnType( 1 ));
		while ( ri.next( ) )
		{
			assertTrue( ri.getValue( "serializable" ) instanceof StringBuffer );
			assertEquals( "ss", ri.getValue( "serializable").toString( ) );
		}
		
		ri.close( );
		qr.close( );
		myGenDataEngine.shutdown( );
	}
	
	/**
	 * @throws Exception
	 */
	private void genUnserializable( ) throws Exception
	{	
		Context context = Context.enter( );		
		Scriptable scope = context.initStandardObjects( );		
		Context.exit( );
		
		QueryDefinition queryDefn = newReportQuery( );

		// column mapping
		String[] name = new String[]{
				"unserializable"
		};
		ScriptExpression[] se = new ScriptExpression[name.length];
		se[0] = new ScriptExpression( "new java.lang.ThreadGroup(\"ss\")" );
		se[0].setDataType( DataType.JAVA_OBJECT_TYPE );
		for ( int i = 0; i < name.length; i++ )
			queryDefn.addBinding( new Binding(name[i], se[i] ));
		
		// generation
		IQueryResults qr = myGenDataEngine.prepare( queryDefn ).execute( scope );
		
		// important step
		queryResultID = qr.getID( );
		
		try 
		{
		
			IResultIterator ri = qr.getResultIterator( );	
			assertEquals( DataType.JAVA_OBJECT_TYPE, ri.getResultMetaData( ).getColumnType( 1 ));
			while ( ri.next( ) )
			{
				assertTrue( ri.getValue( "unserializable" ) instanceof ThreadGroup );
			}
			ri.close( );
			assertTrue( false );
		} 
		catch ( Exception e )
		{
			//Currently, unserializable objects can't be saved in report doc
			e.printStackTrace( );
		}
		finally 
		{
			qr.close( );
			myGenDataEngine.shutdown( );
		}
	}
	
	/**
	 * @throws Exception
	 */
	private void preSerializable( ) throws Exception
	{
		IQueryResults qr = myPreDataEngine.getQueryResults( queryResultID );
		
		IResultIterator ri = qr.getResultIterator( );
		//Currently, org.eclipse.birt.data.engine.impl.document.ResultIterator#getResultMetaData() has bug:
		//It returns meta data of data set instead of meta data of query
		//assertEquals( DataType.OBJECT_TYPE, ri.getResultMetaData( ).getColumnType( 1 ));
		int rowCount = 0;
		while ( ri.next( ) )
		{
			assertTrue( ri.getValue( "serializable" ) instanceof StringBuffer );
			assertEquals( "ss", ri.getValue( "serializable").toString( ) );
			rowCount++;
		}
		assertTrue( rowCount > 0 );
		ri.close( );
		myPreDataEngine.shutdown( );
	}
	
	/**
	 * @throws Exception
	 */
	private void preBasic( ) throws Exception
	{
		IQueryResults qr = myPreDataEngine.getQueryResults( queryResultID );
		assert ( qr.getResultMetaData( ) != null );
		
		IResultIterator ri = qr.getResultIterator( );
		assert ( ri.getResultMetaData( ) != null );
		
		checkResult1( ri );
		
		ri.close( );
		myPreDataEngine.shutdown( );
	}
	
	/**
	 * @throws Exception
	 */
	public void testDummy1Document( ) throws Exception
	{
		String fileName = getOutputFolder( ) + "testData";
		DataEngineContext deContext1 = newContext( DataEngineContext.MODE_GENERATION,
				fileName );
		myGenDataEngine = DataEngine.newDataEngine( deContext1 );
		
		myGenDataEngine.defineDataSource( this.dataSource );
		myGenDataEngine.defineDataSet( this.dataSet );
		
		this.genDummy1( );
		this.closeArchiveWriter( );
		
		DataEngineContext deContext2 = newContext( DataEngineContext.MODE_PRESENTATION,
				fileName );
		myPreDataEngine = DataEngine.newDataEngine( deContext2 );
		
		this.preDummy1( );
		this.closeArchiveReader( );
	}
	
	
	/**
	 * Test Java Object data type
	 * 
	 * @throws Exception
	 */
	public void testObjectTypeBasic( ) throws Exception
	{
		QueryDefinition queryDefn = newReportQuery( );

		// column mapping
		String[] name = new String[]{
				"ObjectType",
		};
		ScriptExpression[] se = new ScriptExpression[name.length];
		se[0] = new ScriptExpression( "new java.lang.StringBuffer(\"ss\")" );
		se[0].setDataType( DataType.JAVA_OBJECT_TYPE );
		for ( int i = 0; i < name.length; i++ )
			queryDefn.addBinding( new Binding(name[i], se[i] ));

		IResultIterator ri = executeQuery( queryDefn );
		assertEquals( DataType.JAVA_OBJECT_TYPE, ri.getResultMetaData( ).getColumnType( 1 ) );
		while ( ri.next( ) )
		{
			assertTrue(ri.getValue( "ObjectType" ) instanceof StringBuffer );
			assertEquals( "ss", ri.getValue( "ObjectType" ).toString( ) );
		}
	}
	
	/**
	 * @throws Exception
	 */
	public void testSerializableObjectTypeInReportDocument( ) throws Exception
	{
		String fileName = getOutputFolder( ) + "testData";
		DataEngineContext deContext1 = newContext( DataEngineContext.MODE_GENERATION,
				fileName );
		myGenDataEngine = DataEngine.newDataEngine( deContext1 );
		
		myGenDataEngine.defineDataSource( this.dataSource );
		myGenDataEngine.defineDataSet( this.dataSet );
		
		genSerializable( );
		this.closeArchiveWriter( );
		
		DataEngineContext deContext2 = newContext( DataEngineContext.MODE_PRESENTATION,
				fileName );
		myPreDataEngine = DataEngine.newDataEngine( deContext2 );
		
		this.preSerializable( );
		this.closeArchiveReader( );
	}
	
	/**
	 * @throws Exception
	 */
	public void testUnserializableObjectTypeInReportDocument( ) throws Exception
	{
		String fileName = getOutputFolder( ) + "testData";
		DataEngineContext deContext1 = newContext( DataEngineContext.MODE_GENERATION,
				fileName );
		myGenDataEngine = DataEngine.newDataEngine( deContext1 );
		
		myGenDataEngine.defineDataSource( this.dataSource );
		myGenDataEngine.defineDataSet( this.dataSet );
		
		
		genUnserializable( );

		//this.closeArchiveWriter( );
//		
//		DataEngineContext deContext2 = newContext( DataEngineContext.MODE_PRESENTATION,
//				fileName );
//		myPreDataEngine = DataEngine.newDataEngine( deContext2 );
//		
//		this.preSerializable( );
		//this.closeArchiveReader( );
	}
	
	/**
	 * @throws Exception
	 */
	private void genDummy1( ) throws Exception
	{	
		expectedValue = new ArrayList( );
		
		Context context = Context.enter( );		
		Scriptable scope = context.initStandardObjects( );		
		Context.exit( );
		
		//------------generation----------------
		QueryDefinition qd = new QueryDefinition( );
		
		// prepare
		IBaseExpression[] rowBeArray = getDummyRowExpr( );
		prepareExprNameAndQuery( rowBeArray, null, qd );
		
		// generation
		IQueryResults qr = myGenDataEngine.prepare( qd ).execute( scope );
		
		// important step
		queryResultID = qr.getID( );
		
		IResultIterator ri = qr.getResultIterator( );		
		while ( ri.next( ) )
		{
			for ( int i = 0; i < rowBeArray.length; i++ )
				expectedValue.add( ri.getValue( this.rowExprName[i] ) );
		}
		
		ri.close( );
		qr.close( );
		myGenDataEngine.shutdown( );
	}
	
	/**
	 * @throws Exception
	 */
	private void preDummy1( ) throws Exception
	{
		IQueryResults qr = myPreDataEngine.getQueryResults( queryResultID );
		assert ( qr.getResultMetaData( ) != null );
		
		IResultIterator ri = qr.getResultIterator( );
		assert ( ri.getResultMetaData( ) != null );
		
		checkResult1( ri );
		
		ri.close( );
		myPreDataEngine.shutdown( );
	}
	
	/**
	 * @return row expression array
	 */
	private IBaseExpression[] getDummyRowExpr( )
	{
		// row test
		int num = 1;
		IBaseExpression[] rowBeArray = new IBaseExpression[num];
		rowBeArray[0] = new ScriptExpression( "new Date()", DataType.DATE_TYPE );
		
		this.rowExprName = new String[rowBeArray.length];
		this.rowExprName[0] = "Date";
		
		return rowBeArray;
	}
	
	/**
	 * @throws Exception
	 */
	public void testDummy2Document( ) throws Exception
	{
		String fileName = getOutputFolder( ) + "testData";
		DataEngineContext deContext1 = newContext( DataEngineContext.MODE_GENERATION,
				fileName );
		myGenDataEngine = DataEngine.newDataEngine( deContext1 );
		
		myGenDataEngine.defineDataSource( this.dataSource );
		myGenDataEngine.defineDataSet( this.dataSet );
		
		this.genDummy2( );
		this.closeArchiveWriter( );
		
		DataEngineContext deContext2 = newContext( DataEngineContext.MODE_PRESENTATION,
				fileName );
		myPreDataEngine = DataEngine.newDataEngine( deContext2 );
		
		this.preDummy2( );
		this.closeArchiveReader( );
	}
	
	private String subName = "subName";
	
	/**
	 * @throws Exception
	 */
	private void genDummy2( ) throws Exception
	{	
		expectedValue = new ArrayList( );
		
		Context context = Context.enter( );		
		Scriptable scope = context.initStandardObjects( );		
		Context.exit( );
		
		//------------generation----------------
		QueryDefinition qd = new QueryDefinition( );
		
		// prepare
		IBaseExpression[] rowBeArray = getDummyRowExpr( );
		prepareExprNameAndQuery( rowBeArray, null, qd );
		
		// prepare sub query		
		SubqueryDefinition subQueryDefn = new SubqueryDefinition( subName, qd );
		qd.addSubquery( subQueryDefn );
		IBaseExpression[] rowBeArray2 = getDummyRowExpr( );
		prepareExprNameAndQuery( rowBeArray2, null, subQueryDefn );
				
		// generation
		IQueryResults qr = myGenDataEngine.prepare( qd ).execute( scope );
		
		// important step
		queryResultID = qr.getID( );
		
		IResultIterator ri = qr.getResultIterator( );		
		while ( ri.next( ) )
		{
			for ( int i = 0; i < rowBeArray.length; i++ )
				expectedValue.add( ri.getValue( this.rowExprName[i] ) );
			
			IResultIterator ri2 = ri.getSecondaryIterator( subName, scope );
			while ( ri2.next( ) )
			{
				for ( int i = 0; i < rowBeArray2.length; i++ )
					expectedValue.add( ri2.getValue( this.rowExprName[i] ) );
			}
			ri2.close( );
		}
		
		ri.close( );		
		qr.close( );
		myGenDataEngine.shutdown( );
	}
	
	/**
	 * @throws Exception
	 */
	private void preDummy2( ) throws Exception
	{
		IQueryResults qr = myPreDataEngine.getQueryResults( queryResultID );
		assert ( qr.getResultMetaData( ) != null );
		
		IResultIterator ri = qr.getResultIterator( );
		assert ( ri.getResultMetaData( ) != null );
		
		checkResult2( ri );
		
		ri.close( );
		myPreDataEngine.shutdown( );
	}
	
	/**
	 * @param type
	 * @param fileName
	 * @return
	 * @throws BirtException
	 */
	private DataEngineContext newContext( int type, String fileName ) throws BirtException
	{
		
		switch ( type )
		{
			case DataEngineContext.MODE_GENERATION :
			{
				try
				{
					archiveWriter = new FileArchiveWriter(fileName);
					archiveWriter.initialize();
				}
				catch (IOException e) 
				{
					throw new IllegalArgumentException( e.getMessage() );
				}
				return DataEngineContext.newInstance( DataEngineContext.MODE_GENERATION,
						null,
						null,
						archiveWriter );
			}
			case DataEngineContext.MODE_PRESENTATION :
			{
				try 
				{
					archiveReader = new FileArchiveReader(fileName);
					archiveReader.open();
				} 
				catch (IOException e) 
				{
					throw new IllegalArgumentException( e.getMessage() );
				}
				return DataEngineContext.newInstance( DataEngineContext.MODE_PRESENTATION,
						null,
						archiveReader,
						null );
			}
			default :
				throw new IllegalArgumentException( "" + type );
		}
	}
	
	/**
	 * @return row expression array
	 */
	private IBaseExpression[] getRowExpr( )
	{
		// row test
		int num = 4;
		IBaseExpression[] rowBeArray = new IBaseExpression[num];
		rowBeArray[0] = new ScriptExpression( "dataSetRow.COUNTRY" );
		rowBeArray[1] = new ScriptExpression( "dataSetRow.CITY" );
		rowBeArray[2] = new ScriptExpression( "dataSetRow.SALE_DATE" );
		rowBeArray[3] = new ScriptExpression( "dataSetRow.AMOUNT" );
		
		this.rowExprName = new String[rowBeArray.length];
		this.rowExprName[0] = "COUNTRY_1";
		this.rowExprName[1] = "CITY_1";
		this.rowExprName[2] = "SALE_NAME_1";
		this.rowExprName[3] = "AMOUNT_1";
		
		return rowBeArray;
	}
	
	/**
	 * @return aggregation expression array
	 * @throws DataException 
	 */
	private IBinding[] getAggrBinding( ) throws DataException
	{
		int num2 = 2;
		totalExprName = new String[num2];
		this.totalExprName[0] = "TOTAL_COUNT_1";
		this.totalExprName[1] = "TOTAL_AMOUNT_1";
		
		
		IBinding[] totalBeArray = new IBinding[num2];
		totalBeArray[0] = new Binding(this.totalExprName[0]  );
		totalBeArray[0].setAggrFunction( "COUNT" );
		totalBeArray[1] = new Binding( this.totalExprName[1], new ScriptExpression("row.AMOUNT_1")  );
		totalBeArray[1].setAggrFunction( "SUM" );
		
		
		return totalBeArray;
	}
	
	/**
	 * Add expression on the row of group
	 * @param rowBeArray
	 * @param totalBeArray
	 * @param qd
	 * @throws DataException 
	 */
	private void prepareExprNameAndQuery( IBaseExpression[] rowBeArray,
			IBinding[] totalBeArray, BaseQueryDefinition qd ) throws DataException
	{
		int num = rowBeArray.length;
		
		for ( int i = 0; i < num; i++ )
			qd.addBinding( new Binding(this.rowExprName[i], rowBeArray[i] ));

		if ( totalBeArray != null )
		{
			int num2 = totalBeArray.length;
			for ( int i = 0; i < num2; i++ )
				qd.addBinding( totalBeArray[i] );
		}
	}
	
	/**
	 * Only check the result of the expectedValue of the result set
	 * 
	 * @param data.it
	 * @param ri
	 * @throws DataException
	 * @throws BirtException
	 */
	private void checkResult1( IResultIterator ri )
			throws BirtException
	{
		Iterator it = this.expectedValue.iterator( );
		
		while ( ri.next( ) )
		{
			String str = "";
			for ( int i = 0; i < rowExprName.length; i++ )
			{
				Object ob1 = it.next( );
				Object ob2 = ri.getValue( this.rowExprName[i] );
				assertEquals( ob1, ob2 );
				str += " " + ob2.toString( );
			}
			
			if ( totalExprName != null )
			{
				for ( int i = 0; i < totalExprName.length; i++ )
				{
					Object ob1 = it.next( );
					Object ob2 = ri.getValue( this.totalExprName[i] );
					assertEquals( ob1, ob2 );
					str += " " + ob2.toString( );
				}
			}
			
			this.testPrintln( "row result set: " + str );
		}
	}
	
	/**
	 * @param ri
	 * @throws BirtException
	 */
	private void checkResult2( IResultIterator ri ) throws BirtException
	{
		Iterator it = this.expectedValue.iterator( );

		while ( ri.next( ) )
		{
			String str = "";
			for ( int i = 0; i < rowExprName.length; i++ )
			{
				Object ob1 = it.next( );
				Object ob2 = ri.getValue( this.rowExprName[i] );
				assertEquals( ob1, ob2 );
				str += " " + ob2.toString( );
			}

			IResultIterator ri2 = ri.getSecondaryIterator( subName, null );
			while ( ri2.next( ) )
			{
				for ( int i = 0; i < rowExprName.length; i++ )
				{
					Object ob1 = it.next( );
					Object ob2 = ri2.getValue( this.rowExprName[i] );
					assertEquals( ob1, ob2 );
					str += " " + ob2.toString( );
				}
			}
			
			if ( totalExprName != null )
			{
				for ( int i = 0; i < totalExprName.length; i++ )
				{
					Object ob1 = it.next( );
					Object ob2 = ri.getValue( this.totalExprName[i] );
					assertEquals( ob1, ob2 );
					str += " " + ob2.toString( );
				}
			}

			this.testPrintln( "row result set: " + str );
		}
	}
	
	/**
	 * @throws DataException
	 */
	private void closeArchiveWriter( ) throws DataException
	{
		if ( archiveWriter != null )
			try
			{
				archiveWriter.finish( );
			}
			catch ( IOException e )
			{
				throw new DataException( "error", e );
			}
	}
	
	/**
	 * @throws DataException
	 */
	private void closeArchiveReader( ) throws DataException
	{
		if ( archiveReader != null )
			try
			{
				archiveReader.close( );
			}
			catch ( IOException e )
			{
				throw new DataException( "error", e );
			}
	}
	
}
