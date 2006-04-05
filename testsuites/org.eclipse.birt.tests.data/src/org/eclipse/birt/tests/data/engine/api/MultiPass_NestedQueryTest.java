package org.eclipse.birt.tests.data.engine.api;

import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
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
import org.eclipse.birt.tests.data.DataTestCase;

import testutil.ConfigText;

import junit.framework.TestCase;

public class MultiPass_NestedQueryTest extends  DataTestCase{

	protected DataSourceInfo getDataSourceInfo() {
		return new DataSourceInfo( ConfigText.getString( "Api.TestData.TableName" ),
				ConfigText.getString( "Api.TestData.TableSQL" ),
				ConfigText.getString( "Api.TestData.TestDataFileName" ) );
	}
		
	/**
	 * filter on group with nested query expression
	 * 
	 * @throws Exception
	 */
	public void test_NestedAggregation( ) throws Exception
	{
		String sqlStatement = "select COUNTRY,AMOUNT,SALE_DATE from " + getTestTableName( );
		( ( OdaDataSetDesign )this.dataSet ).setQueryText( sqlStatement );
		
		IBaseExpression[] expressions = new IBaseExpression[]{
			new ScriptExpression( "row.COUNTRY" ,0 ),
			new ScriptExpression( "row.AMOUNT",0 ),
			new ScriptExpression( "row.SALE_DATE",0 )
		};
		
		FilterDefinition filterDefn = new FilterDefinition(
				new ConditionalExpression( "Total.sum( Total.ave( row.AMOUNT,null,1 ),null,1)" ,
						IConditionalExpression.OP_GT , "0.2") 
							);
		
//		FilterDefinition filterDefn = new FilterDefinition (
//				new ConditionalExpression("row.AMOUNT",IConditionalExpression.OP_TOP_N,"4") 
//					);
		
		GroupDefinition groupDefn = new GroupDefinition( );
		groupDefn.setKeyExpression( "row.COUNTRY" );
		groupDefn.addFilter( filterDefn );
		
		QueryDefinition queryDefn = new QueryDefinition( );		
		queryDefn.setDataSetName( this.dataSet.getName( ) );
		
		queryDefn.addExpression(expressions[0], BaseTransform.ON_EACH_ROW);
		queryDefn.addExpression(expressions[1], BaseTransform.ON_EACH_ROW);
		queryDefn.addExpression(expressions[2], BaseTransform.ON_EACH_ROW);
		
//		queryDefn.addFilter( filterDefn );
		
		IPreparedQuery preparedQuery = dataEngine.prepare( queryDefn );
		IQueryResults queryResult = preparedQuery.execute( null );
		IResultIterator resultIt = queryResult.getResultIterator( );
		
		outputQueryResult( resultIt, expressions );
		checkOutputFile( );
						
		
	}
	
	
}
