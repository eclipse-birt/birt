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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.data.engine.api.IBaseDataSetDesign;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IInputParameterBinding;
import org.eclipse.birt.data.engine.api.ISortDefinition;
import org.eclipse.birt.data.engine.api.querydefn.GroupDefinition;
import org.eclipse.birt.data.engine.api.querydefn.InputParameterBinding;
import org.eclipse.birt.data.engine.api.querydefn.OdaDataSetDesign;
import org.eclipse.birt.data.engine.api.querydefn.ParameterDefinition;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.api.querydefn.SortDefinition;

import testutil.ConfigText;

import org.junit.Test;

/**
 * Input parameter binding test. There are two sources of parameter binding.
 * 		parameter binding added on data set
 * 		parameter binding added on query definition
 * The latter has higher prioiry than the former.
 */
public class InputParameterTest extends APITestCase
{

	private static final String TEST_TABLE_NAME = ConfigText.getString( "Api.TestData.TableName" );
	
	/*
	 * @see org.eclipse.birt.data.engine.api.APITestCase#getDataSourceInfo()
	 */
	protected DataSourceInfo getDataSourceInfo( )
	{
		return new DataSourceInfo( TEST_TABLE_NAME,
				ConfigText.getString( "Api.TestData.TableSQL" ),
				ConfigText.getString( "Api.TestData.TestDataFileName" ) );
	}

	/*
	 * @see testutil.BaseTestCase#getTestTableName()
	 */
	protected String getTestTableName( )
	{
		return TEST_TABLE_NAME;
	}

	/**
	 * Test one parameter, larger than in sql statement
	 * @throws Exception
	 */
	@Test
    public void test1( ) throws Exception
	{
		IBaseDataSetDesign baseDataset = newDataSet( "newDataSet", "select * from "
				+ InputParameterTest.TEST_TABLE_NAME + " where AMOUNT > ?" );

		Collection inputParamDefns = new ArrayList( );
		ParameterDefinition inputParamDefn = 
		    new ParameterDefinition( "param1", DataType.INTEGER_TYPE );
		inputParamDefn.setInputMode( true );
		inputParamDefn.setPosition( 1 );
		inputParamDefn.setDefaultInputValue("0");
		inputParamDefns.add( inputParamDefn );

		Collection inputParamBindings = new ArrayList( );
		InputParameterBinding paramBinding = new InputParameterBinding( 1, new ScriptExpression( "100" ) );
		inputParamBindings.add( paramBinding );

		runQuery( baseDataset, inputParamDefns, inputParamBindings );
	}

	/**
	 * Test two parameter
	 * @throws Exception
	 */
	@Test
    public void test2( ) throws Exception
	{
		IBaseDataSetDesign baseDataset = newDataSet( "newDataSet", "select * from "
				+ InputParameterTest.TEST_TABLE_NAME
				+ " where AMOUNT > ? and AMOUNT < ?" );

		Collection inputParamDefns = new ArrayList( );
		ParameterDefinition inputParamDefn = 
		    new ParameterDefinition( "param1", DataType.INTEGER_TYPE, true, false );
		inputParamDefn.setPosition( 1 );
		inputParamDefn.setDefaultInputValue("0");
		inputParamDefns.add( inputParamDefn );
		inputParamDefn = new ParameterDefinition( "param2", DataType.INTEGER_TYPE, true, false );
		inputParamDefn.setPosition( 2 );
		inputParamDefn.setDefaultInputValue("0");
		inputParamDefns.add( inputParamDefn );

		Collection inputParamBindings = new ArrayList( );
		InputParameterBinding paramBinding = new InputParameterBinding( 1, new ScriptExpression( "'100'" ) );
		inputParamBindings.add( paramBinding );
		paramBinding = new InputParameterBinding( "param2",
				new ScriptExpression( "100*70" ) );
		inputParamBindings.add( paramBinding );
		runQuery( baseDataset, inputParamDefns, inputParamBindings );
	}

	/**
	 * Test one parameter, not equal in sql statement
	 * @throws Exception
	 */
	@Test
    public void test3( ) throws Exception
	{
		IBaseDataSetDesign baseDataset = newDataSet( "newDataSet", "select * from "
				+ InputParameterTest.TEST_TABLE_NAME + " where CITY<>? " );

		ParameterDefinition inputParamDefn = 
		    new ParameterDefinition( "param1", DataType.STRING_TYPE, true, false );
		inputParamDefn.setPosition( 1 );
		inputParamDefn.setDefaultInputValue("0");
		Collection inputParamDefns = new ArrayList( );
		inputParamDefns.add( inputParamDefn );

		Collection inputParamBindings = new ArrayList( );
		InputParameterBinding paramBinding = 
		    	new InputParameterBinding( 1,
		    	        		new ScriptExpression( "'Shanghai'" ) );
		inputParamBindings.add( paramBinding );
		runQuery( baseDataset, inputParamDefns, inputParamBindings );
	}

	/**
	 * **This test case is out of date**
	 * Test one parameter, but there are two parameter bindings pointing to it.
	 * @throws Exception
	 * 
	 *//*
	@Test
    public void test4( ) throws Exception
	{
		IBaseDataSetDesign baseDataset = newDataSet( "newDataSet", "select * from "
				+ InputParameterTest.TEST_TABLE_NAME + " where AMOUNT > ? " );

		ParameterDefinition inputParamDefn =
			new ParameterDefinition( "param1", DataType.INTEGER_TYPE, true, false );
		inputParamDefn.setPosition( 1 );
		inputParamDefn.setDefaultInputValue(null);
		Collection inputParamDefns = new ArrayList( );
		inputParamDefns.add( inputParamDefn );

		Collection inputParamBindings = new ArrayList( );
		InputParameterBinding paramBinding = new InputParameterBinding( 1, new ScriptExpression( "10" ) );
		inputParamBindings.add( paramBinding );
		paramBinding = new InputParameterBinding( 1, new ScriptExpression( "10*10" ) );
		inputParamBindings.add( paramBinding );

		runQuery( baseDataset, inputParamDefns, inputParamBindings );
	}

	*//**
	 * Test one parameter, there is no default value defined in parameter
	 * @throws Exception
	 */
	@Test
    public void test5( ) throws Exception
	{
		IBaseDataSetDesign baseDataset = newDataSet( "newDataSet", "select * from "
				+ InputParameterTest.TEST_TABLE_NAME + " where AMOUNT > ?" );

		Collection inputParamDefns = new ArrayList( );
		ParameterDefinition inputParamDefn = 
		    new ParameterDefinition( "param1", DataType.INTEGER_TYPE );
		inputParamDefn.setInputMode( true );
		inputParamDefn.setPosition( 1 );
		inputParamDefns.add( inputParamDefn );

		Collection inputParamBindings = new ArrayList( );
		InputParameterBinding paramBinding = new InputParameterBinding( 1, new ScriptExpression( "100" ) );
		inputParamBindings.add( paramBinding );

		runQuery( baseDataset, inputParamDefns, inputParamBindings );
	}
	
	/**
	 * Test one parameter, there are one parameter bindings, which is from data set,
	 * @throws Exception
	 */
	@Test
    public void test6( ) throws Exception
	{
		IBaseDataSetDesign baseDataset = newDataSet( "newDataSet", "select * from "
				+ InputParameterTest.TEST_TABLE_NAME + " where AMOUNT > ?" );

		Collection inputParamDefns = new ArrayList( );
		ParameterDefinition inputParamDefn = 
		    new ParameterDefinition( "param1", DataType.INTEGER_TYPE );
		inputParamDefn.setInputMode( true );
		inputParamDefn.setPosition( 1 );
		inputParamDefns.add( inputParamDefn );
		
		Collection inputParamBindingsOfDS = new ArrayList( );
		InputParameterBinding paramBinding = new InputParameterBinding( 1, new ScriptExpression( "400" ) );
		inputParamBindingsOfDS.add( paramBinding );

		runQuery( baseDataset, inputParamDefns, inputParamBindingsOfDS, null );
	}
	
	/**
	 * Test one parameter, there are two parameter bindings, one is from data set,
	 * the other is from query defintion. It is the latter which has real effect.
	 * @throws Exception
	 */
	@Test
    public void test7( ) throws Exception
	{
		IBaseDataSetDesign baseDataset = newDataSet( "newDataSet", "select * from "
				+ InputParameterTest.TEST_TABLE_NAME + " where AMOUNT > ?" );

		Collection inputParamDefns = new ArrayList( );
		ParameterDefinition inputParamDefn = 
		    new ParameterDefinition( "param1", DataType.INTEGER_TYPE );
		inputParamDefn.setInputMode( true );
		inputParamDefn.setPosition( 1 );
		inputParamDefns.add( inputParamDefn );

		Collection inputParamBindingsOfQuery = new ArrayList( );
		InputParameterBinding paramBinding = new InputParameterBinding( 1, new ScriptExpression( "400" ) );
		inputParamBindingsOfQuery.add( paramBinding );
		
		Collection inputParamBindingsOfDS = new ArrayList( );
		paramBinding = new InputParameterBinding( 1, new ScriptExpression( "300" ) );
		inputParamBindingsOfDS.add( paramBinding );

		runQuery( baseDataset, inputParamDefns, inputParamBindingsOfDS, inputParamBindingsOfQuery );
	}
	
/*	*//**
	 * Test one parameter, there are one parameter bindings, which is from data set,
	 * @throws Exception
	 *//*
	@Test
    public void test8( ) throws Exception
	{
		IBaseDataSetDesign baseDataset = newDataSet( "newDataSet", "select * from "
				+ InputParameterTest.TEST_TABLE_NAME + " where AMOUNT > ?" );

		Collection inputParamDefns = new ArrayList( );
		ParameterDefinition inputParamDefn = 
		    new ParameterDefinition( "param1", DataType.INTEGER_TYPE );
		inputParamDefn.setInputMode( true );
		inputParamDefn.setPosition( 1 );
		inputParamDefns.add( inputParamDefn );
		
		Collection inputParamBindingsOfDS = new ArrayList( );
		InputParameterBinding paramBinding = new InputParameterBinding( 1, new ScriptExpression( "null" ) );
		inputParamBindingsOfDS.add( paramBinding );

		try
		{
		    runQuery( baseDataset, inputParamDefns, inputParamBindingsOfDS, null );
			fail("Should not arrive here");
		}
		catch ( DataException e)
		{
		}
	}
	
	*//**
	 * Test the parameter with Anytype if the parameter meta type is integer
	 * 
	 * @throws Exception
	 *//*
	@Test
    public void test9( ) throws Exception 
	{
		IBaseDataSetDesign baseDataset = newDataSet( "newDataSet",
				"select * from "
						+ InputParameterTest.TEST_TABLE_NAME
						+ " where ordered = ?" );

		Collection inputParamDefns = new ArrayList( );
		ParameterDefinition inputParamDefn = new ParameterDefinition( "param1",
				DataType.ANY_TYPE );
		inputParamDefn.setInputMode( true );
		inputParamDefn.setPosition( 1 );
		inputParamDefn.setDefaultInputValue( "1" );
		inputParamDefns.add( inputParamDefn );

		Collection inputParamBindingsOfDS = new ArrayList( );
		InputParameterBinding paramBinding = new InputParameterBinding( 1,
				new ScriptExpression( "null" ) );
		inputParamBindingsOfDS.add( paramBinding );

		try
		{
			runQuery( baseDataset,
					inputParamDefns,
					inputParamBindingsOfDS,
					null );
			fail( "Should not arrive here" );
		}
		catch ( DataException e )
		{
			// expect a DataException
		}
	}*/
	
	/**
	 * Test one parameter, only parameter define, dte will new a parameter
	 * binding based on the default value
	 * 
	 * @throws Exception
	 */
	@Test
    public void test10( ) throws Exception
	{
		IBaseDataSetDesign baseDataset = newDataSet( "newDataSet", "select * from "
				+ InputParameterTest.TEST_TABLE_NAME + " where AMOUNT > ?" );

		Collection inputParamDefns = new ArrayList( );
		ParameterDefinition inputParamDefn = 
		    new ParameterDefinition( "param1", DataType.INTEGER_TYPE );
		inputParamDefn.setInputMode( true );
		inputParamDefn.setPosition( 1 );
		inputParamDefn.setDefaultInputValue("400");
		inputParamDefns.add( inputParamDefn );

		runQuery( baseDataset, inputParamDefns, null );
	}
	
	/**
	 * Test one parameter, its default value is a Date Type
	 * It is a regression test of 103982
	 * @throws Exception
	 */
	@Test
    public void testRegression103982( ) throws Exception
	{
		IBaseDataSetDesign baseDataset = newDataSet( "newDataSet", "select * from "
				+ InputParameterTest.TEST_TABLE_NAME + " where SALE_DATE > ?" );

		Collection inputParamDefns = new ArrayList( );
		ParameterDefinition inputParamDefn = 
		    new ParameterDefinition( "param1", DataType.DATE_TYPE);
		inputParamDefn.setInputMode( true );
		inputParamDefn.setPosition( 1 );
		inputParamDefn.setDefaultInputValue("6/1/00 2:00 AM");
		inputParamDefns.add( inputParamDefn );

		runQuery( baseDataset, inputParamDefns, null );
	}
	
	/**
	 * Simple calling method than below one.
	 * @param dataSet
	 * @param inputParamDefns
	 * @param inputParamBindingsOfQuery
	 * @throws Exception
	 */
	private void runQuery( IBaseDataSetDesign dataSet,
			Collection inputParamDefns, Collection inputParamBindingsOfQuery )
			throws Exception
	{
		runQuery( dataSet, inputParamDefns, null, inputParamBindingsOfQuery );
	}
	
	/**
	 * A Standard ReportQueryDefn, first add parameter to data set
	 * @param dataSet
	 * @param inputParamDefns
	 * @param inputParamBindingsOfDS added to data set
	 * @param inputParamBindingsOfQuery added to query defintion
	 * @param fileName
	 * @throws Exception
	 */
	private void runQuery( IBaseDataSetDesign dataSet, Collection inputParamDefns,
			Collection inputParamBindingsOfDS, Collection inputParamBindingsOfQuery ) throws Exception
	{
		if ( inputParamDefns != null )
		{
			Iterator iterator = inputParamDefns.iterator( );
			while ( iterator.hasNext( ) )
			{
				ParameterDefinition paramDefn = (ParameterDefinition) iterator.next( );
				if ( paramDefn.isInputMode( ) )
					( (OdaDataSetDesign) dataSet ).addParameter( paramDefn );
			}
		}
		
		if ( inputParamBindingsOfDS != null )
		{
			Iterator iteratorOfDS = inputParamBindingsOfDS.iterator( );
			while ( iteratorOfDS.hasNext( ) )
			{
				IInputParameterBinding paramBinds = (IInputParameterBinding) iteratorOfDS.next( );
				( (OdaDataSetDesign) dataSet ).addInputParamBinding( paramBinds );
			}
		}

		createAndRunQuery( dataSet, inputParamBindingsOfQuery );
		checkOutputFile();
	}
	
	/**
	 *  create query definition and run it
	 */
	private void createAndRunQuery( IBaseDataSetDesign dataSet,
			Collection inputParamBindings ) throws Exception
	{
		
		// --- begin binding
		String[] bindingNameGroup = new String[2];
		bindingNameGroup[0] = "GROUP_COUNTRY";
		bindingNameGroup[1] = "GROUP_CITY";
		IBaseExpression[] bindingExprGroup = new IBaseExpression[2];
		bindingExprGroup[0] = new ScriptExpression( "dataSetRow.COUNTRY" );
		bindingExprGroup[1] = new ScriptExpression( "dataSetRow.CITY" );
		GroupDefinition[] groupDefn = new GroupDefinition[]{
				new GroupDefinition( "group0" ), new GroupDefinition( "group1" )
		};
		groupDefn[0].setKeyExpression( "row.GROUP_COUNTRY" );
		groupDefn[1].setKeyExpression( "row.GROUP_CITY" );
    
		String[] bindingNameSort = new String[1];
		bindingNameSort[0] = "SORT_SALE_DATE";
		IBaseExpression[] bindingExprSort = new IBaseExpression[1];
		bindingExprSort[0] = new ScriptExpression( "dataSetRow.SALE_DATE" );
		SortDefinition[] sortDefn = new SortDefinition[]{
			new SortDefinition( )
		};
		sortDefn[0].setColumn( "SORT_SALE_DATE" );
		sortDefn[0].setSortDirection( ISortDefinition.SORT_DESC );

		
		String[] bindingNameRow = new String[4];
		bindingNameRow[0] = "ROW_COUNTRY";
		bindingNameRow[1] = "ROW_CITY";
		bindingNameRow[2] = "ROW_SALE_DATE";
		bindingNameRow[3] = "ROW_AMOUT";
		ScriptExpression[] bindingExprRow = new ScriptExpression[]{
				new ScriptExpression( "dataSetRow.COUNTRY", 0 ),
				new ScriptExpression( "dataSetRow.CITY", 0 ),
				new ScriptExpression( "dataSetRow.SALE_DATE", 0 ),
				new ScriptExpression( "dataSetRow.AMOUNT", 0 )
		};
		
		QueryDefinition queryDefn = createQuery( bindingNameGroup,
				bindingExprGroup,
				groupDefn,
				bindingNameSort,
				bindingExprSort,
				sortDefn,
				null,
				null,
				null,
				bindingNameRow,
				bindingExprRow
				);
		
		if ( inputParamBindings != null )
		{
			Iterator iterator = inputParamBindings.iterator( );
			while ( iterator.hasNext( ) )
			{
				InputParameterBinding inputParamBinding = (InputParameterBinding) iterator.next( );
				queryDefn.addInputParamBinding( inputParamBinding );
			}
		}
		queryDefn.setDataSetName( dataSet.getName( ) );
		outputQueryResult( executeQuery( queryDefn ),bindingNameRow );
	}
}
