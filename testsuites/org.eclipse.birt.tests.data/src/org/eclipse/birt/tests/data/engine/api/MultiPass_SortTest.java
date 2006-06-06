package org.eclipse.birt.tests.data.engine.api;

import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.ISortDefinition;
import org.eclipse.birt.data.engine.api.querydefn.GroupDefinition;
import org.eclipse.birt.data.engine.api.querydefn.OdaDataSetDesign;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.api.querydefn.SortDefinition;

import testutil.APITestCase;
import testutil.ConfigText;
import testutil.APITestCase.DataSourceInfo;

public class MultiPass_SortTest extends APITestCase {

	protected DataSourceInfo getDataSourceInfo() {
		return new DataSourceInfo( ConfigText.getString( "Api.TestData.TableName" ),
				ConfigText.getString( "Api.TestData.TableSQL" ),
				ConfigText.getString( "Api.TestData.TestDataFileName" ) );
	}
	/**
	 * Test feature of
	 * 		aggregation expression 
	 */
	public void test_SortOnAggregationExpression() throws Exception
	{
		// Test a SQL with duplicate column name (quite common with join data sets)
		String testSQL =  "select COUNTRY, AMOUNT from " + getTestTableName( );
		((OdaDataSetDesign)this.dataSet).setQueryText( testSQL );
		
		IBaseExpression[] expressions = new IBaseExpression[]{
				new ScriptExpression( "row.COUNTRY", 0 ),
				new ScriptExpression( "row.AMOUNT", 2 ),
				new ScriptExpression( "Total.runningSum(row.AMOUNT)", 2 ) };

		String names[] = { "COL_COUNTRY", "COL_AMOUNT" };
		
		SortDefinition[] sortDef = new SortDefinition[]{ new SortDefinition() };
		sortDef[0].setExpression( "row.AMOUNT/Total.sum(row.AMOUNT)" );
		sortDef[0].setSortDirection( ISortDefinition.SORT_DESC );		
		
		// define a query design				
		QueryDefinition queryDefn = newReportQuery( );
		queryDefn.addSort(sortDef[0]);		
		
		for( int i = 0; i < 2; i ++ )
		{
			queryDefn.addResultSetExpression( names[i], expressions[ i ] );
		}
		
		IPreparedQuery preparedQuery = dataEngine.prepare( queryDefn );
		IQueryResults queryResults = preparedQuery.execute( null );
		IResultIterator resultIt = queryResults.getResultIterator( );
		assertTrue( resultIt.next() );		
		resultIt.getValue( names[0] );
		resultIt.getValue( names[1] );	
		System.out.print( resultIt.getValue( names[0] ) );
		System.out.print( resultIt.getValue( names[1] ) );		
		resultIt.next();
		System.out.print( resultIt.getValue( names[0] ) );
		System.out.print( resultIt.getValue( names[1] ) );
		resultIt.next();
		System.out.print( resultIt.getValue( names[0] ) );
		System.out.print( resultIt.getValue( names[1] ) );
		resultIt.next();
		System.out.print( resultIt.getValue( names[0] ) );
		System.out.print( resultIt.getValue( names[1] ) );
		resultIt.next();
		System.out.print( resultIt.getValue( names[0] ) );
		System.out.print( resultIt.getValue( names[1] ) );	
	}


	/**
	 * Test feature of
	 * 		aggregation expression 
	 */
	public void test_sortGroup( ) throws Exception
	{
		// Test a SQL with duplicate column name (quite common with join data sets)
		String testSQL =  "select COUNTRY, AMOUNT from " + getTestTableName( );
		((OdaDataSetDesign)this.dataSet).setQueryText( testSQL );
		
		IBaseExpression[] expressions = new IBaseExpression[]{
				new ScriptExpression( "row.COUNTRY", 0 ),
				new ScriptExpression( "row.AMOUNT", 2 ) };
		
		String names[] = { "COL_COUNTRY", "COL_AMOUNT" };
		
		SortDefinition sortDefn = new SortDefinition();
		sortDefn.setExpression( "Total.sum( row.AMOUNT )" );
		sortDefn.setSortDirection( sortDefn.SORT_DESC );	
		
		GroupDefinition[] groupDefn = new GroupDefinition[]{new GroupDefinition( )};
		groupDefn[0].setKeyExpression( "row.COUNTRY" );	
		groupDefn[0].addSort( sortDefn );
				
		// define a query design				
		QueryDefinition queryDefn = newReportQuery( );
		queryDefn.addResultSetExpression(names[0], expressions[0]);
		queryDefn.addResultSetExpression(names[1], expressions[1]);	
		
		queryDefn.addGroup( groupDefn[0]);			
		
		
		IPreparedQuery preparedQuery = dataEngine.prepare( queryDefn );
		IQueryResults queryResults = preparedQuery.execute( null );
		IResultIterator resultIt = queryResults.getResultIterator( );
		assertTrue( resultIt.next() );

		outputQueryResult( executeQuery( queryDefn ), names );
		checkOutputFile();
		
	}

	public void test_sortOnGroupKey( ) throws Exception
	{
		String sqlStatement = "select COUNTRY, AMOUNT from " + getTestTableName( );
		((OdaDataSetDesign)this.dataSet).setQueryText( sqlStatement );
		
		IBaseExpression[] expressions = new IBaseExpression[]{
				new ScriptExpression( "row.COUNTRY", 0 ),
				new ScriptExpression( "row>AMOUNT", 2)		};
		GroupDefinition[] groupDefn = new GroupDefinition[]{new GroupDefinition( ),
				};
		
		
		
		
	
	}
}
