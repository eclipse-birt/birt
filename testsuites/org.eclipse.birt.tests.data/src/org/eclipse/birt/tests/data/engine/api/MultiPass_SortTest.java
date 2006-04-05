package org.eclipse.birt.tests.data.engine.api;

import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.ISortDefinition;
import org.eclipse.birt.data.engine.api.querydefn.BaseTransform;
import org.eclipse.birt.data.engine.api.querydefn.GroupDefinition;
import org.eclipse.birt.data.engine.api.querydefn.OdaDataSetDesign;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.api.querydefn.SortDefinition;
import org.eclipse.birt.tests.data.DataTestCase;

import testutil.ConfigText;

public class MultiPass_SortTest extends DataTestCase {

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

		SortDefinition[] sortDef = new SortDefinition[]{ new SortDefinition() };
		sortDef[0].setExpression( "row.AMOUNT/Total.sum(row.AMOUNT)" );
		sortDef[0].setSortDirection( ISortDefinition.SORT_DESC );		
		
		// define a query design				
		QueryDefinition queryDefn = newReportQuery( );
		queryDefn.addSort(sortDef[0]);		
		
		queryDefn.addExpression(expressions[0], BaseTransform.ON_EACH_ROW);
		queryDefn.addExpression(expressions[1], BaseTransform.AFTER_LAST_ROW);	
		
		IPreparedQuery preparedQuery = dataEngine.prepare( queryDefn );
		IQueryResults queryResults = preparedQuery.execute( null );
		IResultIterator resultIt = queryResults.getResultIterator( );
		assertTrue( resultIt.next() );		
		resultIt.getValue( expressions[0] );
		resultIt.getValue( expressions[1] );	
		System.out.print( resultIt.getValue( expressions[0] ) );
		System.out.print( resultIt.getValue( expressions[1] ) );		
		resultIt.next();
		System.out.print( resultIt.getValue( expressions[0] ) );
		System.out.print( resultIt.getValue( expressions[1] ) );
		resultIt.next();
		System.out.print( resultIt.getValue( expressions[0] ) );
		System.out.print( resultIt.getValue( expressions[1] ) );
		resultIt.next();
		System.out.print( resultIt.getValue( expressions[0] ) );
		System.out.print( resultIt.getValue( expressions[1] ) );
		resultIt.next();
		System.out.print( resultIt.getValue( expressions[0] ) );
		System.out.print( resultIt.getValue( expressions[1] ) );	
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
		
		SortDefinition sortDefn = new SortDefinition();
		sortDefn.setExpression( "Total.sum( row.AMOUNT )" );
		sortDefn.setSortDirection( sortDefn.SORT_DESC );	
		
		GroupDefinition[] groupDefn = new GroupDefinition[]{new GroupDefinition( )};
		groupDefn[0].setKeyExpression( "row.COUNTRY" );	
		groupDefn[0].addSort( sortDefn );
				
		// define a query design				
		QueryDefinition queryDefn = newReportQuery( );
		queryDefn.addExpression(expressions[0], BaseTransform.ON_EACH_ROW);
		queryDefn.addExpression(expressions[1], BaseTransform.AFTER_LAST_ROW);	
		
		queryDefn.addGroup( groupDefn[0]);			
		
		
		IPreparedQuery preparedQuery = dataEngine.prepare( queryDefn );
		IQueryResults queryResults = preparedQuery.execute( null );
		IResultIterator resultIt = queryResults.getResultIterator( );
		assertTrue( resultIt.next() );

		outputQueryResult( executeQuery( queryDefn ), expressions );
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
