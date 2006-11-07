/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.binding;

import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.querydefn.BaseExpression;
import org.eclipse.birt.data.engine.api.querydefn.FilterDefinition;
import org.eclipse.birt.data.engine.api.querydefn.GroupDefinition;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.api.querydefn.SubqueryDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import testutil.ConfigText;


/**
 * Test table schema and data:
 * 		col0	col1	col2	 col3
 * 		0..2	0..2	0..2	 0..2
 * line number: 3*3*3*3
 */
public class SubQueryTest extends APITestCase
{
	/** row expression defined in query defintion*/
	private BaseExpression[] expressions;
	
	private String[] bindingNameRow;
	/*
	 * @see org.eclipse.birt.data.engine.api.APITestCase#getDataSourceInfo()
	 */
	protected DataSourceInfo getDataSourceInfo( )
	{
		return new DataSourceInfo( ConfigText.getString( "Api.TestData1.TableName" ),
				ConfigText.getString( "Api.TestData1.TableSQL" ),
				ConfigText.getString( "Api.TestData1.TestDataFileName" ) );
	}
	
	/**
	 * Sub query test
	 * Normal case: add subquery to GroupDefinition
	 * @throws Exception
	 */
	public void test( ) throws Exception
	{
		// 1 prepare query execution
		Context cx = Context.enter( );
		Scriptable sharedScope = cx.initStandardObjects( );

		Scriptable subScope = cx.newObject( sharedScope );
		subScope.setParentScope( sharedScope );
		
		IQueryDefinition queryDefn = getDefaultQueryDefnWithSubQuery( dataSet.getName( ) );
		expressions = getExpressionsOfDefaultQuery( );
		
		// 2 do query execution
		IResultIterator resultIt = executeQuery( queryDefn );
		
		// 3.1 get sub query data
		resultIt.next( );
		IResultIterator subIterator = resultIt.getSecondaryIterator( "IAMTEST",
				sharedScope );
		
		// 3.2 get sub query of sub query data
		subIterator.next( );
		IResultIterator subSubIterator = subIterator.getSecondaryIterator( "IAMTEST2",
				subScope );

		bindingNameRow = this.getBindingExpressionName( );
		// 4.1 output sub query data
		testPrintln( "sub query data" );
		outputData( subIterator );
		testPrintln( "" );
		
		// 4.2 output sub query of sub query data
		testPrintln( "sub query of sub query data" );
		outputData( subSubIterator );
		testPrintln( "" );
		
		// check whether output is correct
		checkOutputFile();
	}
	
	/**
	 * Boundary case: add subquery to QueryDefinition
	 *  The data operation in subquery should not affect the data of
	 *  outer query.
	 * @throws Exception
	 */
	public void test2( ) throws Exception
	{
		// execute query and return sub query
		IResultIterator resultIt = executeQuery( getAnotherSubQuery( ) );
		resultIt.next( );
		resultIt.getSecondaryIterator( "IAMTEST", null );
		
		testPrintln( "query data" );
		outputData( resultIt );

		// check whether output is correct
		checkOutputFile( );
	}
	
	/**
	 * Nearly same as test2, a little difference is there is no next operation
	 * applied to parent query to get the result iterator of sub query
	 * 
	 * @throws Exception
	 */
	public void test3( ) throws Exception
	{
		// execute query and return sub query
		IResultIterator resultIt = executeQuery( getAnotherSubQuery( ) );
		resultIt.getSecondaryIterator( "IAMTEST", null );

		testPrintln( "query data" );
		outputData( resultIt );

		// check whether output is correct
		checkOutputFile( );
	}
	
	/**
	 * @throws Exception
	 */
	public void test4( ) throws Exception
	{
		// execute query and return sub query
		IResultIterator resultIt = executeQuery( getAnotherSubQuery( false ) );
		
		testPrintln( "query data" );
		while ( resultIt.next( ) )
		{
			IResultIterator resultIt2 = resultIt.getSecondaryIterator( "IAMTEST", null );
			outputData( resultIt2 );
		}
	}
	
	/**
	 * @return
	 */
	private QueryDefinition getAnotherSubQuery( )
	{
		return getAnotherSubQuery( true );
	}
	
	/**
	 * Create another subquery
	 * 
	 * @return
	 */
	private QueryDefinition getAnotherSubQuery( boolean onGroup )
	{
		// prepare query and sub query
		QueryDefinition queryDefn = (QueryDefinition) getDefaultQueryDefn( dataSet.getName( ) );

		SubqueryDefinition subqueryDefn = new SubqueryDefinition( "IAMTEST" );
		if ( onGroup == false )
			subqueryDefn.setApplyOnGroupFlag( false );
		
		String[] bindingNameGroup = new String[1];
		bindingNameGroup[0] = "GROUP_COL2";
		IBaseExpression[] bindingExprGroup = new IBaseExpression[1];
		bindingExprGroup[0] = new ScriptExpression( "dataSetRow.COL2" );
		GroupDefinition[] subGroupDefn = new GroupDefinition[]{
				new GroupDefinition( "group1" ) };
		subGroupDefn[0].setKeyExpression( "row.GROUP_COL2" );

		bindingNameRow = new String[4];
		bindingNameRow[0] = "ROW_COL0";
		bindingNameRow[1] = "ROW_COL1";
		bindingNameRow[2] = "ROW_COL2";
		bindingNameRow[3] = "ROW_COL3";
		// 2.3: ExpressionKey
		expressions = new BaseExpression[]{
				new ScriptExpression( "dataSetRow[\"COL0\"]", 0 ),
				new ScriptExpression( "dataSetRow[\"COL1\"]", 0 ),
				new ScriptExpression( "dataSetRow.COL2", 0 ),
				new ScriptExpression( "dataSetRow[\"COL3\"]", 0 )
		};
		for ( int i = 0; i < subGroupDefn.length; i++ )
		{
			subqueryDefn.addGroup( subGroupDefn[i] );
			subqueryDefn.addResultSetExpression(bindingNameGroup[i], 
				bindingExprGroup[i]	);			
		}
		for ( int i = 0; i < bindingNameRow.length; i++ )
		{
			subqueryDefn.addResultSetExpression( bindingNameRow[i],
					expressions[i] );
		}
		
		bindingNameRow = new String[4];
		bindingNameRow[0] = "ROW_COL0";
		bindingNameRow[1] = "ROW_COL1";
		bindingNameRow[2] = "ROW_COL2";
		bindingNameRow[3] = "ROW_COL3";
		// 2.3: ExpressionKey
		expressions = new BaseExpression[]{
				new ScriptExpression( "row[\"ROW_COL0\"]", 0 ),
				new ScriptExpression( "row[\"ROW_COL1\"]", 0 ),
				new ScriptExpression( "dataSetRow.COL2", 0 ),
				new ScriptExpression( "row._outer[\"ROW_COL3\"]", 0 )
		};
		
		queryDefn.addSubquery( subqueryDefn );
		return queryDefn;
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	public void testMaxRow( ) throws Exception
	{
		// prepare query and sub query
		QueryDefinition queryDefn = (QueryDefinition) getDefaultQueryDefn( dataSet.getName( ) );

		SubqueryDefinition subqueryDefn = new SubqueryDefinition( "IAMTEST" );
		subqueryDefn.setMaxRows( 10 );
		
		String[] bindingNameGroup = new String[1];
		bindingNameGroup[0] = "GROUP_COL2";
		IBaseExpression[] bindingExprGroup = new IBaseExpression[1];
		bindingExprGroup[0] = new ScriptExpression( "dataSetRow.COL2" );
		GroupDefinition[] subGroupDefn = new GroupDefinition[]{
				new GroupDefinition( "group1" )	};
		
		subGroupDefn[0].setKeyExpression( "row.GROUP_COL2" );
		
		for (int k = 0; k < subGroupDefn.length; k++) {
			if (bindingNameGroup != null)
				for (int i = 0; i < bindingNameGroup.length; i++)
					subqueryDefn.addResultSetExpression(
							bindingNameGroup[i], bindingExprGroup[i]);
			for (int i = 0; i < subGroupDefn.length; i++)
				subqueryDefn.addGroup(subGroupDefn[i]);
		}
		this.populateQueryExprMapping( subqueryDefn );
		
		FilterDefinition exprFilter = new FilterDefinition(
				new ScriptExpression("row.ROW_COL0+row.ROW_COL2>0"));
		subqueryDefn.addFilter(exprFilter);
		
		bindingNameRow = new String[4];
		bindingNameRow[0] = "ROW_COL0";
		bindingNameRow[1] = "ROW_COL1";
		bindingNameRow[2] = "ROW_COL2";
		bindingNameRow[3] = "ROW_COL3";
		// 2.3: ExpressionKey
		expressions = new BaseExpression[]{
				new ScriptExpression( "dataSetRow.COL0", 0 ),
				new ScriptExpression( "dataSetRow.COL1", 0 ),
				new ScriptExpression( "dataSetRow.COL2", 0 ),
				new ScriptExpression( "dataSetRow.COL3", 0 )
		};
		for ( int i = 0; i < expressions.length; i++ )
			queryDefn.addResultSetExpression(bindingNameRow[i], expressions[i]);
		
		queryDefn.addSubquery( subqueryDefn );
		
		// execute query and return sub query
		IResultIterator resultIt = executeQuery( queryDefn );
		resultIt.next( );
		
		bindingNameRow = this.getBindingExpressionName( );
		IResultIterator subIterator = resultIt.getSecondaryIterator( "IAMTEST",
				null );
		
		testPrintln( "query data" );
		outputData( subIterator );
		
		// check whether output is correct
		checkOutputFile();
	}

	/**
	 * Output row data 
	 * @param resultIt
	 * @throws DataException
	 */
	private void outputData( IResultIterator resultIt ) throws Exception
	{
		while ( resultIt.next( ) )
		{
			String outputStr = "";
			for ( int i = 0; i < expressions.length; i++ )
			{
				Object object = resultIt.getValue( bindingNameRow[i] );
				outputStr +=  object.toString( ) + "    ";			
			}
			testPrintln( outputStr );
		}
	}
}
