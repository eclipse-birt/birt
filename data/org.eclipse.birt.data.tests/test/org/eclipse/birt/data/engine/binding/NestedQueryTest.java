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

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.data.engine.api.IBaseDataSetDesign;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.ISortDefinition;
import org.eclipse.birt.data.engine.api.querydefn.BaseDataSetDesign;
import org.eclipse.birt.data.engine.api.querydefn.ConditionalExpression;
import org.eclipse.birt.data.engine.api.querydefn.FilterDefinition;
import org.eclipse.birt.data.engine.api.querydefn.GroupDefinition;
import org.eclipse.birt.data.engine.api.querydefn.InputParameterBinding;
import org.eclipse.birt.data.engine.api.querydefn.OdaDataSetDesign;
import org.eclipse.birt.data.engine.api.querydefn.ParameterDefinition;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.api.querydefn.SortDefinition;
import org.mozilla.javascript.Context;

import testutil.ConfigText;

import org.junit.Before;
import org.junit.Test;

/**
 * Nested query test. This test can be used to demonstrate how
 * nested query are constructed and run.
 */
public class NestedQueryTest extends APITestCase
{
	private String callsTableName;
	
	private String[] bindingNameCustomer;
	private ScriptExpression[] expressionsCustomer;
	private QueryDefinition queryDefnCustomer;
	
	// call data, inner query
	private String[] bindingNameCall;
    private ScriptExpression[] expressionsCall;
	private IBaseDataSetDesign datasetCall;
	private QueryDefinition queryDefnCall;	

	/* 
	 * @see org.eclipse.birt.data.engine.api.APITestCase#setUp()
	 */
	@Before
    public void nestedQuerySetUp() throws Exception
	{

		
		prepareDataSet( new DataSourceInfo( ConfigText.getString( "Api.TestDataCalls.TableName" ),
				ConfigText.getString( "Api.TestDataCalls.TableSQL" ),
				ConfigText.getString( "Api.TestDataCalls.TestDataFileName" ) ) );
		
		callsTableName = ConfigText.getString( "Api.TestDataCalls.TableName" );
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.api.APITestCase#getDataSourceInfo()
	 */
	protected DataSourceInfo getDataSourceInfo( )
	{
		return new DataSourceInfo( ConfigText.getString( "Api.TestDataCustomer.TableName" ),
				ConfigText.getString( "Api.TestDataCustomer.TableSQL" ),
				ConfigText.getString( "Api.TestDataCustomer.TestDataFileName" ) );
	}
	
	/**
	 * The inner query with a parameter rows[0].CustomerID
	 * , which will be prepared.
	 * @throws Exception
	 */
	@Test
    public void test1( ) throws Exception
	{
		queryDefnCustomer = createCustomerQueryDefn( );
		
		datasetCall = newDataSet( "data set calls",
				" SELECT * FROM " + callsTableName + " WHERE CustomerID = ?" );
		queryDefnCall = createCallQueryDefn( );

		// add parameter to query of call
		// use the expression of row[outerQueryIndex].column_name
		// defined in query
		addParameterToQueryCall(expressionsCall[5]);

		// run query
		runNestedQuery( );
	}

	/**
	 * The inner query with a parameter row.CustomerID of outer query.
	 * @throws Exception
	 */
	@Test
    public void test2( ) throws Exception
	{
		queryDefnCustomer = createCustomerQueryDefn( );
		
		datasetCall = newDataSet( "data set calls",
				" SELECT * FROM " + callsTableName + " WHERE CustomerID = ?" );
		queryDefnCall = createCallQueryDefn( );

		// add parameter to query of call
		// directly use the expression of outer parent query
		addParameterToQueryCall(expressionsCustomer[0]);

		// run query
		runNestedQuery();
	}
	
	
	/**
	 * The inner query with a parameter rows[0].CustomerID
	 * , which will not be prepared.
	 * @throws Exception
	 */
	@Test
    public void test3( ) throws Exception
	{
		queryDefnCustomer = createCustomerQueryDefn( );
		
		datasetCall = newDataSet( "data set calls",
				" SELECT * FROM " + callsTableName + " WHERE CustomerID = ?" );
		queryDefnCall = createCallQueryDefn( );

		// add parameter to query of call
		// use the expression of row[outerQueryIndex].column_name
		// not defined in query
		addParameterToQueryCall(new ScriptExpression( "rows[0].CUSTOMERID", 0 ));

		// run query
		runNestedQuery( );
	}
	
	/**
	 * The inner query with a filter row["CustomerID"] equals rows[0].CustomerID
	 * 
	 * @throws Exception
	 */
	@Test
    public void test4( ) throws Exception
	{
		queryDefnCustomer = createCustomerQueryDefn();

		datasetCall = newDataSet("data set calls", " SELECT * FROM "
				+ callsTableName);
		queryDefnCall = createCallQueryDefn( );

		String[] bindingNameFilter = new String[1];
		bindingNameFilter[0] = "FILTER_CUSTOMERID";
		IBaseExpression[] bindingExprFilter = new IBaseExpression[1];
		bindingExprFilter[0] = new ScriptExpression( "dataSetRow.CU" );
		FilterDefinition filterDefn = new FilterDefinition( new ConditionalExpression( "row[\"ROW_CUSTOMERID\"]",
				ConditionalExpression.OP_EQ,
				"rows[0].CUSTOMERID" ) );
		// add filter to query of call
		// row["officeCode"] equals rows[0].officeCode
		queryDefnCall.addFilter( filterDefn );

		// run query
		runNestedQuery();
	}
	
	/**
	 * The inner query with a filter row["CustomerID"] equals row._outer["ROW_CUSTOMERID"]
	 * 
	 * @throws Exception
	 */
	@Test
    public void test5( ) throws Exception
	{
		queryDefnCustomer = createCustomerQueryDefn();

		datasetCall = newDataSet("data set calls", " SELECT * FROM "
				+ callsTableName);
		queryDefnCall = createCallQueryDefn( );

		String[] bindingNameFilter = new String[1];
		bindingNameFilter[0] = "FILTER_CUSTOMERID";
		IBaseExpression[] bindingExprFilter = new IBaseExpression[1];
		bindingExprFilter[0] = new ScriptExpression( "dataSetRow.CU" );
		FilterDefinition filterDefn = new FilterDefinition( new ConditionalExpression( "row._outer[\"ROW_CUSTOMERID\"]",
				ConditionalExpression.OP_EQ,"2"
				 ) );
		FilterDefinition filterDefn1 = new FilterDefinition( new ConditionalExpression( "row[\"ROW_CUSTOMERID\"]",
				ConditionalExpression.OP_EQ,"row._outer[\"ROW_CUSTOMERID\"]"
				 ) );
		// add filter to query of call
		// row["officeCode"] equals rows[0].officeCode
		queryDefnCall.addFilter( filterDefn );
		queryDefnCall.addFilter( filterDefn1 );

		// run query
		runNestedQuery();
	}
	
	/**
	 * The inner query with a group using column in outer group as group key.
	 * 
	 * @throws Exception
	 */
	@Test
    public void test6( ) throws Exception
	{
		queryDefnCustomer = createCustomerQueryDefn();

		datasetCall = newDataSet("data set calls", " SELECT * FROM "
				+ callsTableName);
		queryDefnCall = createCallQueryDefn( );

		
		GroupDefinition gd = new GroupDefinition("group1");
		gd.setKeyColumn( "ROW_OUTER_GROUPKEY" );
		queryDefnCall.addGroup( gd );
		// run query
		runNestedQuery();
	}
	
	/**
	 * The inner query with a TopN filter. The TopN filter will lead to a multipass processing.
	 * 
	 * @throws Exception
	 */
	@Test
    public void test7( ) throws Exception
	{
		queryDefnCustomer = createCustomerQueryDefn();

		datasetCall = newDataSet("data set calls", " SELECT * FROM "
				+ callsTableName);
		queryDefnCall = createCallQueryDefn( );

		String[] bindingNameFilter = new String[1];
		bindingNameFilter[0] = "FILTER_CUSTOMERID";
		IBaseExpression[] bindingExprFilter = new IBaseExpression[1];
		bindingExprFilter[0] = new ScriptExpression( "dataSetRow.CU" );
		FilterDefinition filterDefn = new FilterDefinition( new ConditionalExpression( "Total.isTopN(row[\"ROW_DURATION\"],5)",
				ConditionalExpression.OP_TRUE
				 ) );

		queryDefnCall.addFilter( filterDefn );

		// run query
		runNestedQuery();
	}
	@Test
    public void test8( ) throws Exception
	{
		queryDefnCustomer = createCustomerQueryDefn( );
		queryDefnCustomer.getGroups( ).clear( );
		queryDefnCustomer.getSorts( ).clear( );
		datasetCall = newDataSet( "data set calls",
				" SELECT * FROM " + callsTableName + " WHERE CustomerID = ?" );
		queryDefnCall = createCallQueryDefn( );

		// add parameter to query of call
		// directly use the expression of outer parent query
		addParameterToQueryCall(expressionsCustomer[0]);

		// run query
		runNestedQuery();
	}
	
	/**
	 * Create customer query definition
	 * 
	 * @return QueryDefinition
	 */
	private QueryDefinition createCustomerQueryDefn() throws Exception {

		String[] bindingNameGroup = new String[2];
		bindingNameGroup[0] = "GROUP_CUSTOMERID";
		bindingNameGroup[1] = "GROUP_NAME";
		IBaseExpression[] bindingExprGroup = new IBaseExpression[2];
		bindingExprGroup[0] = new ScriptExpression("dataSetRow.CUSTOMERID");
		bindingExprGroup[1] = new ScriptExpression("dataSetRow.NAME");
		GroupDefinition[] groupDefn = new GroupDefinition[] {
				new GroupDefinition("group0"), new GroupDefinition("group1") };
		groupDefn[0].setKeyExpression("row.GROUP_CUSTOMERID");
		groupDefn[1].setKeyExpression("row.GROUP_NAME");

		String[] bindingNameSort = new String[1];
		bindingNameSort[0] = "SORT_CUSTOMERID";
		IBaseExpression[] bindingExprSort = new IBaseExpression[1];
		bindingExprSort[0] = new ScriptExpression("dataSetRow.CUSTOMERID");
		SortDefinition[] sortDefn = new SortDefinition[] { new SortDefinition() };
		sortDefn[0].setColumn("SORT_CUSTOMERID");
		sortDefn[0].setSortDirection(ISortDefinition.SORT_DESC);

		bindingNameCustomer = new String[4];
		bindingNameCustomer[0] = "ROW_CUSTOMERID";
		bindingNameCustomer[1] = "ROW_NAME";
		bindingNameCustomer[2] = "ROW_ADDRESS";
		bindingNameCustomer[3] = "ROW_CURRENTBALANCE";
		expressionsCustomer = new ScriptExpression[] {
				new ScriptExpression("dataSetRow.CUSTOMERID", 0),
				new ScriptExpression("dataSetRow.NAME", 0),
				new ScriptExpression("dataSetRow.ADDRESS", 0),
				new ScriptExpression("dataSetRow.CURRENTBALANCE", 0) };

		return createQuery(bindingNameGroup, bindingExprGroup, groupDefn,
				bindingNameSort, bindingExprSort, sortDefn, null, null, null,
				bindingNameCustomer, expressionsCustomer);

	}

	/**
	 * Create call query definition
	 * 
	 * @return QueryDefinition
	 */
	private QueryDefinition createCallQueryDefn() throws Exception
	{

		bindingNameCall = new String[9];
		bindingNameCall[0] = "ROW_CUSTOMERID";
		bindingNameCall[1] = "ROW_CALLTIME";
		bindingNameCall[2] = "ROW_TONUMBER";
		bindingNameCall[3] = "ROW_DURATION";
		bindingNameCall[4] = "ROW_CHARGE";
		bindingNameCall[5] = "ROW_[0]_CUSTOMERID";
		bindingNameCall[6] = "ROW_OUTER_CUSTOMERID";
		bindingNameCall[7] = "ROW_OUTER_TONUMBER";
		bindingNameCall[8] = "ROW_OUTER_GROUPKEY";

		expressionsCall = new ScriptExpression[] {
				new ScriptExpression("dataSetRow.CUSTOMERID", 0),
				new ScriptExpression("dataSetRow.CALLTIME", 0),
				new ScriptExpression("dataSetRow.TONUMBER", 0),
				new ScriptExpression("dataSetRow.DURATION", 0),
				new ScriptExpression("dataSetRow.CHARGE", 0),
				new ScriptExpression("rows[0].CUSTOMERID", 0 ),
				new ScriptExpression("row._outer[\"ROW_CUSTOMERID\"]" ),
				new ScriptExpression("row._outer[\"ROW_CURRENTBALANCE\"]"),
				new ScriptExpression("row._outer[\"ROW_CUSTOMERID\"] == row[\"ROW_CUSTOMERID\"]?true:false;")
		};

		this.dataSet = (BaseDataSetDesign) this.datasetCall;
		return createQuery( null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				bindingNameCall,
				expressionsCall );
	}

	/**
	 * Add parameter to query defintion of call
	 */
	private void addParameterToQueryCall(ScriptExpression expression)
	{
		// define parameter and parameter binding definition
		ParameterDefinition inputParamDefn = new ParameterDefinition( "param1",
				DataType.INTEGER_TYPE,
				true,
				false );
		inputParamDefn.setPosition( 1 );
		inputParamDefn.setDefaultInputValue( "0" );
		( (OdaDataSetDesign) datasetCall ).addParameter( inputParamDefn );

		InputParameterBinding inputParamBinding = new InputParameterBinding( 1,
				 expression);
		queryDefnCall.addInputParamBinding( inputParamBinding );
	}
	
	/**
	 * Run nested query.
	 */
	private void runNestedQuery( ) throws Exception
	{
		IPreparedQuery preparedQueryCustomer = dataEngine.prepare( queryDefnCustomer, this.getAppContext( ) );
		IPreparedQuery preparedQueryCall = dataEngine.prepare( queryDefnCall, this.getAppContext( ) );
		
		ScriptContext scriptContext = new ScriptContext( ).newContext( Context.getCurrentContext( ).initStandardObjects( ) );
		
		IQueryResults queryResultsCustomer = preparedQueryCustomer.execute( null );
		IResultIterator resultItCustomer = queryResultsCustomer.getResultIterator( );
		
		// output result
		testPrintln( "*****A new Report Start!*****" );
		while ( resultItCustomer.next( ) )
		{
			resultItCustomer.getStartingGroupLevel( );
			resultItCustomer.getEndingGroupLevel( );
			testPrint( "Customer Name:" );
			testPrint( evalAsString( bindingNameCustomer[1], resultItCustomer ) );
			testPrint( "  Address:" );
			testPrint( evalAsString( bindingNameCustomer[2], resultItCustomer ) );
			testPrintln( "" );
			testPrint( "Starting Balance: $" );
			testPrint( evalAsString( bindingNameCustomer[3], resultItCustomer ) );
			testPrintln( "" );
			//scriptContext.enterScope();
			// here note: nested query is done
			IQueryResults queryResultsCalls = preparedQueryCall.execute( queryResultsCustomer,
					null );
			IResultIterator resultItCalls = queryResultsCalls.getResultIterator( );
			while ( resultItCalls.next( ) )
			{
				for ( int i = 1; i < expressionsCall.length; i++ )
				{
					testPrint( evalAsString( bindingNameCall[i], resultItCalls ) );
					testPrint( " " );
				}
				testPrintln( "" );
			}
			testPrintln( "" );

		}
		
		scriptContext.close();
		checkOutputFile();
	}
}
