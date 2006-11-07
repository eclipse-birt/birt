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
package org.eclipse.birt.data.engine.impl.rd;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.DataEngine;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.ISortDefinition;
import org.eclipse.birt.data.engine.api.querydefn.BaseQueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.FilterDefinition;
import org.eclipse.birt.data.engine.api.querydefn.GroupDefinition;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.api.querydefn.SortDefinition;
import org.eclipse.birt.data.engine.api.querydefn.SubqueryDefinition;
import org.eclipse.birt.data.engine.core.DataException;

import testutil.ConfigText;

/**
* Test case for interactive viewing
*/
public class ViewingTest extends RDTestCase
{
	private String queryResultID;
	private List expectedValue;

	private String[] rowExprName;
	private String[] totalExprName;

	private String[] subRowExprName;

	private IBaseExpression[] rowBeArray;
	private IBaseExpression[] totalBeArray;
	
	private boolean GEN_add_filter;
	private boolean GEN_add_group;
	private boolean GEN_subquery_on_group;
	private boolean PRE_add_filter;
	private boolean PRE_add_sort;
	private boolean PRE_use_oldbinding;
	private boolean PRE_add_group;
	private FilterDefinition GEN_filterDefn;

	/*
	 * @see org.eclipse.birt.data.engine.api.APITestCase#getDataSourceInfo()
	 */
	protected DataSourceInfo getDataSourceInfo( )
	{
		return new DataSourceInfo( ConfigText.getString( "Api.TestData.TableName" ),
				ConfigText.getString( "Api.TestData.TableSQL" ),
				ConfigText.getString( "Api.TestData.TestDataFileName" ) );
	}

	/*
	 * @see org.eclipse.birt.data.engine.api.APITestCase#setUp()
	 */
	public void setUp( ) throws Exception
	{
		super.setUp( );

		expectedValue = new ArrayList( );
		
		this.GEN_add_filter = false;
		this.GEN_add_group = false;		
		this.GEN_subquery_on_group = false;
		this.PRE_add_filter = false;
		this.PRE_add_sort = false;
		this.PRE_use_oldbinding = false;
		this.PRE_add_group = false;
		
	}

	/**
	 * @throws BirtException
	 */
	public void testBasicIV( ) throws Exception
	{
		this.genBasicIV( );
		this.closeArchiveWriter( );

		DataEngineContext deContext2 = newContext( DataEngineContext.MODE_PRESENTATION,
				fileName );
		myPreDataEngine = DataEngine.newDataEngine( deContext2 );
		
		this.preBasicIV( );
		this.closeArchiveReader( );

		this.checkOutputFile( );
	}

	/**
	 * With filter
	 * @throws BirtException
	 */
	public void testBasicIV2( ) throws Exception
	{
		this.GEN_add_filter = true;
		this.genBasicIV( );
		this.closeArchiveWriter( );

		DataEngineContext deContext2 = newContext( DataEngineContext.MODE_PRESENTATION,
				fileName );
		myPreDataEngine = DataEngine.newDataEngine( deContext2 );

		this.PRE_add_sort = true;		
		this.preBasicIV( );
		this.closeArchiveReader( );

		this.checkOutputFile( );
	}

	/**
	 * With group
	 * @throws BirtException
	 */
	public void testBasicIV3( ) throws Exception
	{
		this.GEN_add_group = true;
		this.genBasicIV( );
		this.closeArchiveWriter( );

		DataEngineContext deContext2 = newContext( DataEngineContext.MODE_PRESENTATION,
				fileName );
		myPreDataEngine = DataEngine.newDataEngine( deContext2 );

		this.PRE_add_sort = true;
		this.PRE_add_group = true;
		this.preBasicIV( );
		this.closeArchiveReader( );

		this.checkOutputFile( );
	}

	/**
	 * With group and filter
	 * @throws BirtException
	 */
	public void testBasicIV4( ) throws Exception
	{
		this.GEN_add_filter = true;
		this.GEN_add_group = true;
		this.genBasicIV( );
		this.closeArchiveWriter( );

		DataEngineContext deContext2 = newContext( DataEngineContext.MODE_PRESENTATION,
				fileName );
		myPreDataEngine = DataEngine.newDataEngine( deContext2 );

		this.PRE_add_filter = true;
		this.PRE_add_sort = true;
		this.PRE_add_group = true;
		this.preBasicIV( );
		this.closeArchiveReader( );

		this.checkOutputFile( );
	}
	
	/**
	 * @throws Exception
	 */
	public void testBasicIV5( ) throws Exception
	{
		this.GEN_add_filter = true;
		this.GEN_add_group = true;
		this.genBasicIV( );
		this.closeArchiveWriter( );

		DataEngineContext deContext2 = newContext( DataEngineContext.MODE_PRESENTATION,
				fileName );
		myPreDataEngine = DataEngine.newDataEngine( deContext2 );

		this.PRE_add_filter = true;
		this.PRE_add_sort = true;
		this.PRE_use_oldbinding = true;
		this.PRE_add_group = true;
		this.preBasicIV( );
		this.closeArchiveReader( );

		this.checkOutputFile( );
	}
	
	/**
	 * @throws BirtException
	 */
	private void genBasicIV( )
			throws BirtException
	{
		QueryDefinition qd = newGenIVReportQuery( );

		// prepare
		IBaseExpression[] rowBeArray = getRowExpr( );
		IBaseExpression[] totalBeArray = getAggrExpr( );
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
	 * @return
	 */
	private QueryDefinition newGenIVReportQuery( )
	{
		QueryDefinition qd = newReportQuery( );

		if ( GEN_add_filter == true )
		{
			// do filtering on column 4
			String columnBindingNameFilter = "AMOUNT_1";
			IBaseExpression columnBindingExprFilter = new ScriptExpression( "dataSetRow.AMOUNT" );
			ScriptExpression filterExpr = new ScriptExpression( "row.AMOUNT_1>50" );
			GEN_filterDefn = new FilterDefinition( filterExpr );
			qd.addResultSetExpression( columnBindingNameFilter,
					columnBindingExprFilter );
			qd.addFilter( GEN_filterDefn );
		}

		// do sorting on column 2
		String columnBindingNameSort = "CITY_1";
		IBaseExpression columnBindingExprSort = new ScriptExpression( "dataSetRow.CITY" );
		SortDefinition sortDefn = new SortDefinition( );
		sortDefn.setColumn( columnBindingNameSort );
		qd.addResultSetExpression( columnBindingNameSort, columnBindingExprSort );
		qd.addSort( sortDefn );

		if ( GEN_add_group == true )
		{
			// add grouping on column1
			String columnBindingNameGroup = "COUNTRY2";
			IBaseExpression columnBindingExprGroup = new ScriptExpression( "dataSetRow.COUNTRY" );
			GroupDefinition gd = new GroupDefinition( );
			gd.setKeyColumn( "COUNTRY2" );
			qd.addResultSetExpression( columnBindingNameGroup,
					columnBindingExprGroup );
			qd.addGroup( gd );
		}

		return qd;
	}
	
	/**
	 * @throws BirtException
	 */
	private void preBasicIV( )
			throws BirtException
	{
		// here queryResultID needs to set as the data set
		QueryDefinition qd = newPreIVReportQuery( );
		_preBasicIV( qd );
	}
	
	/**
	 * @param PRE_add_filter
	 * @param PRE_add_sort
	 * @param PRE_use_oldbinding
	 * @return
	 */
	private QueryDefinition newPreIVReportQuery( )
	{
		QueryDefinition qd = new QueryDefinition( );

		if ( GEN_add_filter == true )
		{
			qd.addFilter( GEN_filterDefn );
		}
		
		if ( PRE_add_filter == true )
		{
			// do filtering on column 4
			ScriptExpression filterExpr = new ScriptExpression( "row.AMOUNT_1>200" );
			FilterDefinition fd = new FilterDefinition( filterExpr );
			qd.addFilter( fd );
		}

		if ( PRE_add_sort )
		{
			// do sorting on column 4
			SortDefinition sd = new SortDefinition( );
			sd.setExpression( "row.AMOUNT_1" );
			sd.setSortDirection( ISortDefinition.SORT_ASC );
			qd.addSort( sd );
		}

		if ( PRE_add_group == true )
		{
			// add grouping on column1
			String columnBindingNameGroup = "COUNTRY2";
			IBaseExpression columnBindingExprGroup = new ScriptExpression( "dataSetRow.COUNTRY" );
			GroupDefinition gd = new GroupDefinition( );
			gd.setKeyColumn( "COUNTRY2" );
			qd.addResultSetExpression( columnBindingNameGroup,
					columnBindingExprGroup );
			qd.addGroup( gd );
		}
		for ( int i = 0; i < rowExprName.length; i++ )
		{
			if ( PRE_use_oldbinding )
			{
				qd.addResultSetExpression( this.rowExprName[i],
						this.rowBeArray[i] );
			}
			else
			{
				qd.addResultSetExpression( this.rowExprName[i],
						new ScriptExpression( "row[\""
								+ this.rowExprName[i] + "\"]" ) );
			}
		}

		for ( int i = 0; i < totalExprName.length; i++ )
		{
			if ( PRE_use_oldbinding )
			{
				qd.addResultSetExpression( this.totalExprName[i],
						this.totalBeArray[i] );
			}
			else
			{
				qd.addResultSetExpression( this.totalExprName[i],
						new ScriptExpression( "dataSetRow[\""
								+ this.totalExprName[i] + "\"]" ) );
			}
		}
		return qd;
	}
	
	/**
	 * @param GEN_add_filter
	 * @param GEN_add_group
	 * @param qd
	 * @throws BirtException
	 */
	private void _preBasicIV( QueryDefinition qd ) throws BirtException
	{
		qd.setQueryResultsID( this.queryResultID );

		IQueryResults qr = myPreDataEngine.prepare( qd ).execute( null );
		IResultIterator ri = qr.getResultIterator( );

		ri.moveTo( 0 );
		do
		{
			String abc = "";
			for ( int i = 0; i < rowExprName.length; i++ )
				abc += ri.getValue( rowExprName[i] ) + "  ";
			for ( int i = 0; i < totalExprName.length; i++ )
				abc += ri.getValue( totalExprName[i] ) + "  ";
			this.testPrintln( abc + ri.getRowId( ) );
		} while ( ri.next( ) );

		ri.close( );
		myPreDataEngine.shutdown( );
	}
	
	/**
	 * With group and filter
	 * @throws BirtException
	 */
	public void atestBasicIVSubQuery1( ) throws Exception
	{
		this.GEN_add_filter = true;
		this.GEN_add_group = true;
		this.genBasicIV( );
		this.closeArchiveWriter( );

		DataEngineContext deContext2 = newContext( 
				DataEngineContext.MODE_PRESENTATION,
				fileName );
		myPreDataEngine = DataEngine.newDataEngine( deContext2 );

		this.PRE_add_filter = true;
		this.PRE_add_sort = true;
		this._preBasicIVWithSubQuery( );
		this.closeArchiveReader( );
	}

	/**
	 * With group and filter
	 * @throws BirtException
	 */
	public void testBasicIVSubQuery( ) throws Exception
	{
		this.GEN_add_filter = false;
		this.GEN_add_group = true;
		this.GEN_subquery_on_group = true;
		this._genBasicIVWithSubQuery( );
		this.closeArchiveWriter( );

		DataEngineContext deContext2 = newContext( 
				DataEngineContext.MODE_PRESENTATION,
				fileName );
		myPreDataEngine = DataEngine.newDataEngine( deContext2 );

		this.PRE_add_filter = true;
		this.PRE_add_sort = true;
		this.PRE_add_group = true;
		this.PRE_use_oldbinding = true;
		this._preBasicIVWithSubQuery( );
		this.closeArchiveReader( );
		
		this.checkOutputFile( );
	}

	/**
	 * @throws BirtException
	 */
	private void _genBasicIVWithSubQuery( ) throws BirtException
	{
		QueryDefinition qd = newGenIVReportQuery( );

		// prepare
		IBaseExpression[] rowBeArray = getRowExpr( );
		IBaseExpression[] totalBeArray = getAggrExpr( );
		prepareExprNameAndQuery( rowBeArray, totalBeArray, qd );

		//		 ---------- begin sub query ----------
		SubqueryDefinition subqueryDefn = new SubqueryDefinition( "IAMTEST" );
		if( GEN_subquery_on_group )
		{
			((GroupDefinition)qd.getGroups().get(0)).addSubquery( subqueryDefn );
			subqueryDefn.setApplyOnGroupFlag(true);
		}
		else
		{
			qd.addSubquery( subqueryDefn );
			subqueryDefn.setApplyOnGroupFlag( false );
		}
		subRowExprName = new String[3];
		subRowExprName[0] = "sub1";
		subRowExprName[1] = "sub2";
		subRowExprName[2] = "sub3";
		ScriptExpression[] exprs = new ScriptExpression[3];
		exprs[0] = new ScriptExpression( "row.__rownum");
		exprs[1] = new ScriptExpression( "dataSetRow[\"AMOUNT\"]");
		exprs[2] = new ScriptExpression( "dataSetRow[\"CITY\"]");

		for( int i = 0; i < subRowExprName.length; i++ )
			subqueryDefn.addResultSetExpression(subRowExprName[i], exprs[i]);
		

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

			IResultIterator subRi = ri.getSecondaryIterator("IAMTEST", scope);
			
			while (subRi.next())
			{
				for (int i = 0; i < subRowExprName.length; i++) 
				{
					subRi.getValue(subRowExprName[i]);
				}
			}
			subRi.close();
		}


		ri.close( );
		qr.close( );
		myGenDataEngine.shutdown( );
	}
	
	/**
	 * @throws BirtException
	 */
	private void _preBasicIVWithSubQuery( )
			throws BirtException
	{
		QueryDefinition qd = newPreIVReportQuery( );
		qd.setQueryResultsID( this.queryResultID );
		
		SubqueryDefinition subqueryDefn = new SubqueryDefinition( "IAMTEST" );
		((GroupDefinition)qd.getGroups( ).get(0)).addSubquery( subqueryDefn );
		subRowExprName = new String[3];
		subRowExprName[0] = "sub1";
		subRowExprName[1] = "sub2";
		subRowExprName[2] = "sub3";
		ScriptExpression[] exprs = new ScriptExpression[3];
		exprs[0] = new ScriptExpression( "row.__rownum");
		exprs[1] = new ScriptExpression( "dataSetRow.AMOUNT");
		exprs[2] = new ScriptExpression( "dataSetRow.CITY");

		for( int i = 0; i < subRowExprName.length; i++ )
			subqueryDefn.addResultSetExpression(subRowExprName[i], exprs[i]);
		subqueryDefn.setApplyOnGroupFlag(true);

		IQueryResults qr = myPreDataEngine.prepare( qd ).execute( null );
		IResultIterator ri = qr.getResultIterator( );

		ri.moveTo( 0 );
		do
		{
			String abc = "";
			for ( int i = 0; i < rowExprName.length; i++ )
				abc += ri.getValue( rowExprName[i] ) + "  ";
			for ( int i = 0; i < totalExprName.length; i++ )
				abc += ri.getValue( totalExprName[i] ) + "  ";
			
			this.testPrintln( abc + ri.getRowId( ) );
			
			IResultIterator subRi = ri.getSecondaryIterator("IAMTEST", scope);
			while( subRi.next() )
			{
				abc = "          [" + subRi.getValue("sub1") + "]" + "["
						+ subRi.getValue("sub2") + "]" + "["
						+ subRi.getValue("sub2") + "]";
				this.testPrintln( abc );
			}
		} while ( ri.next( ) );

		ri.close( );
		myPreDataEngine.shutdown( );
	}
	
	/**
	 * @return row expression array
	 */
	private IBaseExpression[] getRowExpr( )
	{
		// row test
		int num = 4;
		rowBeArray = new IBaseExpression[num];
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
	 */
	private IBaseExpression[] getAggrExpr( )
	{
		int num2 = 2;
		totalBeArray = new IBaseExpression[num2];
		totalBeArray[0] = new ScriptExpression( "Total.Count( )" );
		totalBeArray[1] = new ScriptExpression( "Total.Sum( dataSetRow.AMOUNT )" );

		totalExprName = new String[totalBeArray.length];
		this.totalExprName[0] = "TOTAL_COUNT_1";
		this.totalExprName[1] = "TOTAL_AMOUNT_1";

		return totalBeArray;
	}

	/**
	 * Add expression on the row of group
	 * @param rowBeArray
	 * @param totalBeArray
	 * @param qd
	 */
	private void prepareExprNameAndQuery( IBaseExpression[] rowBeArray,
			IBaseExpression[] totalBeArray, BaseQueryDefinition qd )
	{
		int num = rowBeArray.length;
		int num2 = totalBeArray.length;

		for ( int i = 0; i < num; i++ )
			qd.addResultSetExpression( this.rowExprName[i], rowBeArray[i] );

		for ( int i = 0; i < num2; i++ )
			qd.addResultSetExpression( this.totalExprName[i], totalBeArray[i] );
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

