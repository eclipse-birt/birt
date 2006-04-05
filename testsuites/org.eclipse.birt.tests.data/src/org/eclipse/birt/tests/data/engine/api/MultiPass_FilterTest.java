package org.eclipse.birt.tests.data.engine.api;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.IGroupDefinition;
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.querydefn.BaseTransform;
import org.eclipse.birt.data.engine.api.querydefn.ConditionalExpression;
import org.eclipse.birt.data.engine.api.querydefn.FilterDefinition;
import org.eclipse.birt.data.engine.api.querydefn.GroupDefinition;
import org.eclipse.birt.data.engine.api.querydefn.OdaDataSetDesign;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.tests.data.DataTestCase;

import testutil.ConfigText;

public class MultiPass_FilterTest extends DataTestCase {
	
	
	protected DataSourceInfo getDataSourceInfo() {
		return new DataSourceInfo( ConfigText.getString( "Api.TestData.TableName" ),
				ConfigText.getString( "Api.TestData.TableSQL" ),
				ConfigText.getString( "Api.TestData.TestDataFileName" ) );
	}
	
	/**
	 * Test feature of
	 * 		aggregation expression 
	 */
	public void test_FilteWithTopN() throws Exception
	{
		// Test a SQL with duplicate column name (quite common with join data sets)
		String testSQL =  "select COUNTRY, AMOUNT from " + getTestTableName( );
		((OdaDataSetDesign)this.dataSet).setQueryText( testSQL );
		
		IBaseExpression[] expressions = new IBaseExpression[]{
				new ScriptExpression( "row.COUNTRY", 0 ),
				new ScriptExpression( "row.AMOUNT", 2 )};

	
		FilterDefinition filterDef = new FilterDefinition (
				new ConditionalExpression("row.AMOUNT",IConditionalExpression.OP_TOP_N,"1") );
		
		// define a query design				
		QueryDefinition queryDefn = newReportQuery( );		
		
		queryDefn.addFilter( filterDef );
		
		queryDefn.addExpression(expressions[0], BaseTransform.ON_EACH_ROW);
		queryDefn.addExpression(expressions[1], BaseTransform.AFTER_LAST_ROW);	
		
		IPreparedQuery preparedQuery = dataEngine.prepare( queryDefn );
		IQueryResults queryResults = preparedQuery.execute( null );
		IResultIterator resultIt = queryResults.getResultIterator( );

		outputQueryResult( resultIt, expressions );	
		checkOutputFile( );	
	}
	
	/**
	 * filter on date time type column with operator Bottom N 
	 */
	
	public void test_FilterWithBottomN( ) throws Exception
	{
		
		String sqlStatement = "select COUNTRY,AMOUNT,SALE_DATE from " + getTestTableName( );
		( ( OdaDataSetDesign )this.dataSet ).setQueryText( sqlStatement );
		
		IBaseExpression[] expressions = new IBaseExpression[]{
			new ScriptExpression( "row.COUNTRY", 0),
			new ScriptExpression( "row.AMOUNT",2 ),	
			new ScriptExpression( "row.SALE_DATE",6 )
		};
		
		
		FilterDefinition filterDefn = new FilterDefinition(
				new ConditionalExpression("row.SALE_DATE", IConditionalExpression.OP_BOTTOM_N,"3"));
		
		QueryDefinition queryDefn = new QueryDefinition( );
		queryDefn.setDataSetName( this.dataSet.getName( ) );
//		QueryDefinition queryDefn = newReportQuery( );
		
		queryDefn.addFilter( filterDefn );
		
		queryDefn.addExpression(expressions[0], BaseTransform.ON_EACH_ROW);
		queryDefn.addExpression(expressions[1], BaseTransform.ON_EACH_ROW);
		queryDefn.addExpression(expressions[2], BaseTransform.ON_EACH_ROW);
		
		IPreparedQuery preparedQuery = dataEngine.prepare( queryDefn );
		IQueryResults queryResults = preparedQuery.execute( null );
		IResultIterator resultIt = queryResults.getResultIterator( );

		outputQueryResult( resultIt, expressions );
		checkOutputFile( );					
		
	}


	/**
	 * add a filter to group
	 *  
	 */
	
	public void test_FilterGroup( ) throws Exception
	{
		String sqlStatement = "select COUNTRY,AMOUNT, SALE_DATE from " + getTestTableName( );
		( ( OdaDataSetDesign )this.dataSet ).setQueryText( sqlStatement );
		
		IBaseExpression[] expressions = new IBaseExpression[]{
				new ScriptExpression( "row.COUNTRY" ),
				new ScriptExpression( "row.AMOUNT" ),
				new ScriptExpression( "row.SALE_DATE" )
		};
		
		QueryDefinition queryDefn = new QueryDefinition( );
		queryDefn.setDataSetName( this.dataSet.getName( ) );
		int expreLength = expressions.length;
		int i = 0;
		while ( i < expreLength )
		{
			queryDefn.addExpression( expressions[ i ],BaseTransform.ON_EACH_ROW );
			i++;			
		}
		
		FilterDefinition filterDefn = new FilterDefinition( 
				new ConditionalExpression( "Total.sum(row.AMOUNT,null,1)", IConditionalExpression.OP_TOP_PERCENT, "50" ) );
		
//		FilterDefinition filterDefn = new FilterDefinition(
//				new ScriptExpression(   "Total.sum(row.Amount,null,1) > 7000" )
//					);
		GroupDefinition groupDefn = new GroupDefinition( ); 
		groupDefn.setKeyExpression( "row.COUNTRY" );
		groupDefn.addFilter( filterDefn );
		
		queryDefn.addGroup( groupDefn );
		IPreparedQuery preparedQuery = dataEngine.prepare( queryDefn );
		IQueryResults queryResults = preparedQuery.execute( null );
		IResultIterator resultIt = queryResults.getResultIterator( );
		
		outputQueryResult( resultIt, expressions );
		checkOutputFile( );
		
	}
	
	/**
	 * filter on group with bottom N
	 * 
	 * @throws Exception
	 */
	
	public void test_MultiPassFilterGroup( ) throws Exception
	{
		String sqlStatement = "select COUNTRY,AMOUNT, SALE_DATE from " + getTestTableName( );
		( ( OdaDataSetDesign )this.dataSet ).setQueryText( sqlStatement );
		
		
		IBaseExpression[] expressions = new IBaseExpression[]{
				new ScriptExpression( "row.COUNTRY" ),
				new ScriptExpression( "row.AMOUNT" ),
				new ScriptExpression( "row.SALE_DATE" )				
		};
		
		QueryDefinition queryDefn = new QueryDefinition( );
		queryDefn.setDataSetName( this.dataSet.getName( ) );
		int expreLength = expressions.length;
		int i = 0;
		while ( i < expreLength )
		{
			queryDefn.addExpression( expressions[ i ],BaseTransform.ON_EACH_ROW );
			i++;			
		}
		
		
		FilterDefinition filterDefn = new FilterDefinition( 
				new ConditionalExpression( "Total.sum(row.AMOUNT,null,1)", IConditionalExpression.OP_BOTTOM_PERCENT, "25" ) 
					);
		GroupDefinition groupDefn = new GroupDefinition();
		groupDefn.setKeyExpression( "row.SALE_DATE" );		
		groupDefn.setInterval( 2 );
		groupDefn.setIntervalRange( IGroupDefinition.MONTH_INTERVAL);		
		
		groupDefn.addFilter( filterDefn );
		
		queryDefn.addGroup( groupDefn );
		IPreparedQuery preparedQuery = dataEngine.prepare( queryDefn );
		IQueryResults queryResults = preparedQuery.execute( null );
		IResultIterator resultIt = queryResults.getResultIterator( );
		
		outputQueryResult( resultIt, expressions );
		checkOutputFile( );
		
	}
	
	

	/**
	 * filter on group with bottom N with negative value
	 * 
	 * @throws Exception
	 */
	
	public void test_NegativeValueFilterGroup( ) throws Exception
	{
		String sqlStatement = "select COUNTRY,AMOUNT, SALE_DATE from " + getTestTableName( );
		( ( OdaDataSetDesign )this.dataSet ).setQueryText( sqlStatement );
		
		
		IBaseExpression[] expressions = new IBaseExpression[]{
				new ScriptExpression( "row.COUNTRY" ),
				new ScriptExpression( "row.AMOUNT" ),
				new ScriptExpression( "row.SALE_DATE" )				
		};
		
		QueryDefinition queryDefn = new QueryDefinition( );
		queryDefn.setDataSetName( this.dataSet.getName( ) );
		int expreLength = expressions.length;
		int i = 0;
		while ( i < expreLength )
		{
			queryDefn.addExpression( expressions[ i ],BaseTransform.ON_EACH_ROW );
			i++;			
		}
		
		
		FilterDefinition filterDefn = new FilterDefinition( 
				new ConditionalExpression( "Total.sum(row.Amount,null,1)", IConditionalExpression.OP_BOTTOM_PERCENT, "-10" ) 
					);
		GroupDefinition groupDefn = new GroupDefinition();
		groupDefn.setKeyExpression( "row.SALE_DATE" );		
		groupDefn.setInterval( 2 );
		groupDefn.setIntervalRange( IGroupDefinition.MONTH_INTERVAL);		
		
		groupDefn.addFilter( filterDefn );
		try
		{
			queryDefn.addGroup( groupDefn );
			IPreparedQuery preparedQuery = dataEngine.prepare( queryDefn );
			IQueryResults queryResults = preparedQuery.execute( null );
			IResultIterator resultIt = queryResults.getResultIterator( );
			fail("should throw out exception here");
		}	
		catch( DataException e )
		{
			
		}		
		
	}

	
	
	
	/**
	 * filter on group with bottom N with invalid value
	 * 
	 * @throws Exception
	 */
	
	public void test_InvalidValueFilterGroup( ) throws Exception
	{
		String sqlStatement = "select COUNTRY,AMOUNT, SALE_DATE from " + getTestTableName( );
		( ( OdaDataSetDesign )this.dataSet ).setQueryText( sqlStatement );
		
		
		IBaseExpression[] expressions = new IBaseExpression[]{
				new ScriptExpression( "row.COUNTRY" ),
				new ScriptExpression( "row.AMOUNT" ),
				new ScriptExpression( "row.SALE_DATE" )				
		};
		
		QueryDefinition queryDefn = new QueryDefinition( );
		queryDefn.setDataSetName( this.dataSet.getName( ) );
		int expreLength = expressions.length;
		int i = 0;
		while ( i < expreLength )
		{
			queryDefn.addExpression( expressions[ i ],BaseTransform.ON_EACH_ROW );
			i++;			
		}
		
		
		FilterDefinition filterDefn = new FilterDefinition( 
				new ConditionalExpression( "Total.sum(row.Amount,null,1)", IConditionalExpression.OP_BOTTOM_PERCENT, "abc" ) 
					);
		GroupDefinition groupDefn = new GroupDefinition();
		groupDefn.setKeyExpression( "row.SALE_DATE" );		
		groupDefn.setInterval( 2 );
		groupDefn.setIntervalRange( IGroupDefinition.MONTH_INTERVAL);		
		
		groupDefn.addFilter( filterDefn );
		try
		{
			queryDefn.addGroup( groupDefn );
			IPreparedQuery preparedQuery = dataEngine.prepare( queryDefn );
			IQueryResults queryResults = preparedQuery.execute( null );
			IResultIterator resultIt = queryResults.getResultIterator( );
			fail("should throw out exception here");
		}	
		catch( DataException e )
		{
			
		}		
		
	}
	
	
	
}
