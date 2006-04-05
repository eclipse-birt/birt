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

package testutil;

import java.io.File;

import org.eclipse.birt.data.engine.api.querydefn.BaseDataSetDesign;
import org.eclipse.birt.data.engine.api.querydefn.BaseDataSourceDesign;
import org.eclipse.birt.data.engine.api.querydefn.BaseExpression;
import org.eclipse.birt.data.engine.api.querydefn.BaseTransform;
import org.eclipse.birt.data.engine.api.querydefn.GroupDefinition;
import org.eclipse.birt.data.engine.api.querydefn.OdaDataSetDesign;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.api.querydefn.SortDefinition;
import org.eclipse.birt.data.engine.api.querydefn.SubqueryDefinition;


import org.eclipse.birt.data.engine.api.DataEngine;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBaseDataSetDesign;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.ISortDefinition;


import testutil.BaseTestCase;
import testutil.JDBCDataSource;
import testutil.JDBCOdaDataSource;
import testutil.TestDataSource;

/**
 * Base class for test cases that work with Data Engine public API
 */
abstract public class APITestCase extends BaseTestCase
{
	/** connection property */
	protected String DriverClass;
	protected String URL;
	protected String User;
	protected String Password;

	/** test table and util */
	private String tableName;
	protected TestDataSource dataSourceInstance;
	
	/** instance of DataEngine */
	protected DataEngine dataEngine;	
	/**
	 * Every test case might have one datasource and dataset. They are defined
	 * in base class for convinience to use.
	 */
	protected BaseDataSourceDesign dataSource;
	protected BaseDataSetDesign dataSet;
	
	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp( ) throws Exception
	{
		super.setUp( );
		
		dataEngine = DataEngine.newDataEngine( DataEngineContext.newInstance( DataEngineContext.DIRECT_PRESENTATION,
				jsScope,
				null,
				null ) );
		prepareDataSource( );
	}
	
	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown( ) throws Exception
	{
		dataEngine.shutdown( );
		closeDataSource( );
		
		super.tearDown( );
	}
	
	/**
	 * Prepare test table and oda data source and data set design. In most case,
	 * only one data source needs to be defined.
	 * 
	 * @throws Exception
	 */
	private void prepareDataSource( ) throws Exception
	{
		prepareDataSourceProperty( );
		
		DataSourceInfo dataSourceInfo = getDataSourceInfo( );
		if ( dataSourceInfo != null )
		{
			// derived class might want to use other data base rather than derby
			prepareDataSet( dataSourceInfo );

			dataSource = this.dataSourceInstance.getOdaDataSourceDesign( );
			dataSet = this.dataSourceInstance.getOdaDataSetDesign( );
			( (OdaDataSetDesign) dataSet ).setQueryText( "select * from "
					+ dataSourceInfo.tableName );
			dataEngine.defineDataSource( this.dataSource );
			dataEngine.defineDataSet( this.dataSet );
		}
	}
	
	/**
	 * Prepare data source connection property, these properties will be used in
	 * test table preparation and oda data source preparation.
	 */
	private void prepareDataSourceProperty()
	{
		if ( DriverClass == null )
			DriverClass = "org.apache.derby.jdbc.EmbeddedDriver";
		if ( URL == null )
			URL = "jdbc:derby:DTETest";
		if ( User == null )
			User = "user";
		if ( Password == null )
			Password = "password";		
		System.setProperty( "DTETest.driver", DriverClass );		
		System.setProperty( "DTETest.url", URL );
		System.setProperty( "DTETest.user",User);
		System.setProperty( "DTETest.password",Password);	
	}
	
	/**
	 * Prepare test table. This method is defined separatelly since in some test
	 * cases, they might use more than one data set, although they share the
	 * same data source.
	 * 
	 * @param dataSourceInfo
	 * @throws Exception
	 */
	protected void prepareDataSet( DataSourceInfo dataSourceInfo )
			throws Exception
	{
		if ( dataSourceInfo != null
				&& dataSourceInfo.tableName != null
				&& dataSourceInfo.createSql != null
				&& dataSourceInfo.dataFileName != null )
		{
			this.tableName = dataSourceInfo.tableName;
			
			this.prepareTestTable( dataSourceInfo.tableName,
					dataSourceInfo.createSql,
					dataSourceInfo.dataFileName );
		}
	}
	
	/**
	 * Create test table and populate data into table, currently only derby data
	 * base is used.
	 * 
	 * @param tableName
	 * @param createSql
	 * @param dataFileName
	 * @throws Exception
	 */
	private void prepareTestTable( String tableName, String createSql,
			String dataFileName ) throws Exception
	{		
		if ( dataSourceInstance == null )
			dataSourceInstance = JDBCDataSource.newInstance( );
		
		// create table
		this.dataSourceInstance.createTable( tableName,
				createSql,
				true );
		
		// insert data into table
		this.dataSourceInstance.populateTable( tableName,
				new File( getInputFolder(), dataFileName) );
	}
	
	/**
	 * Normally, an API test case will be based on a particular data source, so
	 * before test begins, test case provides which data source will be used and
	 * then its data will be prepared and meantime its data source and data set
	 * will be defined in DataEngine. If the return value is null, there is no
	 * any datasource and dataset is defined in dataEngine, which means other
	 * methods such as newDataSet and newReportQuery can not be used.
	 * 
	 * @return which data source will be used in this test case
	 */
	protected abstract DataSourceInfo getDataSourceInfo( );
	
	/**
	 * Wrap the info for the preparation of data source, when derby is used, it
	 * needs to provide the createSql and dataFileName to create a table for
	 * test.
	 */
	public class DataSourceInfo
	{
		private String tableName;
		private String createSql;
		private String dataFileName;

		/**
		 * @param tableName, used table name
		 * @param createSql, sql to create table
		 * @param dataFileName, data to insert table
		 */
		public DataSourceInfo( String tableName, String createSql,
				String dataFileName )
		{
			this.tableName = tableName;
			this.createSql = createSql;
			this.dataFileName = dataFileName;
		}
	}
	
	/**
	 * @throws Exception
	 */
	protected void closeDataSource( ) throws Exception
	{
		if ( this.dataSourceInstance != null )
		{
			if ( tableName != null )
				dataSourceInstance.dropTable( tableName );
			this.dataSourceInstance.close( true );
			this.dataSourceInstance = null;
		}
	}
	
	/**
	 * @return test table name
	 */
	protected String getTestTableName()
	{
		return this.tableName;
	}
	
	/**
	 * new a JDBC dataset with specified datasetname and querytext
	 * 
	 * @param datasetName
	 * @param queryText
	 * @return dataset
	 * @throws Exception
	 */
	protected OdaDataSetDesign newDataSet( String datasetName, String queryText )
			throws Exception
	{
		OdaDataSetDesign dset = new OdaDataSetDesign( datasetName );
		dset.setDataSource( this.dataSource.getName( ) );
		dset.setQueryText( queryText );
		dset.setExtensionID( JDBCOdaDataSource.DATA_SET_TYPE );
		dataEngine.defineDataSet( dset );
		
		return dset;
	}
	
	/**
	 * new a default query, which only has data set information
	 * 
	 * @return queryDefn QueryDefinition
	 */
	protected QueryDefinition newReportQuery( )
	{
		QueryDefinition queryDefn = new QueryDefinition( );
		queryDefn.setDataSetName( this.dataSet.getName( ) );
		
		return queryDefn;
	}
	
	/**
	 * new a simple query with specified dataset
	 * 
	 * @return queryDefn QueryDefinition
	 */
	protected QueryDefinition newReportQuery( IBaseDataSetDesign dataset )
	{
		QueryDefinition queryDefn = new QueryDefinition( );
		if ( dataset == null )
			queryDefn.setDataSetName( this.dataSet.getName( ) );
		else
			queryDefn.setDataSetName( dataset.getName( ) );
		
		return queryDefn;
	}
	
	/**
	 * Execute query definition
	 * 
	 * @param query
	 * @return resultIterator
	 * @throws Exception
	 */
	protected IResultIterator executeQuery( IQueryDefinition query )
			throws Exception
	{
		IPreparedQuery preparedQuery = dataEngine.prepare( query );
		IQueryResults queryResults = preparedQuery.execute( null );
		return queryResults.getResultIterator( );
	}
	
	/**
	 * Output result of executing query defintion
	 * 
	 * @param resultIt
	 * @param expressions
	 * @throws Exception
	 */
	protected void outputQueryResult( IResultIterator resultIt,
			IBaseExpression[] expressions ) throws Exception
	{
		assert testOut!= null;

		// output result
		testPrintln( "*****A new Report Start!*****" );
		while ( resultIt.next( ) )
		{
			testPrint( "S:" );
			testPrint( Integer.toString( resultIt.getStartingGroupLevel( ) ) );
			testPrint( " E:" );
			testPrint( Integer.toString( resultIt.getEndingGroupLevel( ) ) );
			testPrint( " " );
			for ( int i = 0; i < expressions.length; i++ )
			{
				testPrint( evalAsString( expressions[i], resultIt ) );
				testPrint( "    " );
			}
			testPrintln( "" );
		}
		testPrintln( "" );
	}
	
	/**
	 * Evaluates an expression. Returns "EXCEPTION" if an exception happens
	 * 
	 * @return evaluation result of expression
	 */
	protected String evalAsString( IBaseExpression expr, IResultIterator result )
	{
		try
		{
			Object val = result.getValue( expr );
			if ( val == null )
				return "<null>";
			else
				return val.toString( );
		}
		catch ( Exception e )
		{
			// Not all expressions can be evaluated in all rows
			// Print an error if it cannot be
			return "<EXCEPTION>";
		}
	}
	
	/**
	 * Return default query definition
	 * 
	 * @param dataSetName
	 * @return default query definition
	 */
	protected IQueryDefinition getDefaultQueryDefn( String dataSetName )
	{
		return Util.instance.getDefaultQueryDefn( dataSetName );
	}
	
	/**
	 * Return default query definition with subquery
	 * @param dataSetName
	 * @return default query definition with subquery
	 */
	protected IQueryDefinition getDefaultQueryDefnWithSubQuery(
			String dataSetName )
	{
		return Util.instance.getDefaultQueryDefnWithSubQuery( dataSetName );
	}
	
	/**
	 * Return expression of default query
	 * @return expression of default query
	 */
	protected BaseExpression[] getExpressionsOfDefaultQuery( )
	{
		return Util.instance.getExpressionsOfDefaultQuery( );
	}
	
	/**
	 * Utility
	 */
	private static class Util
	{
		private static Util instance = new Util( );
		
		private static QueryDefinition queryDefn;
		private static BaseExpression[] expressions;
		
		/**
		 * Create a general Query with groups,sorts and subquery
		 * @param dataSetName
		 * @return queryDefn
		 */
		protected IQueryDefinition getDefaultQueryDefn( String dataSetName ) 
		{	
			// 2.1 GroupKey
			GroupDefinition[] groupDefn = new GroupDefinition[]{
					new GroupDefinition( "group1" ),
					new GroupDefinition( "group2"),
					new GroupDefinition( "group3")
			};
			groupDefn[0].setKeyExpression( "row.col0" );
			groupDefn[1].setKeyExpression( "row.col1" );
			groupDefn[2].setKeyExpression( "row.col2" );

			// 2.2 SortKey
			SortDefinition[] sortDefn = new SortDefinition[]{
				new SortDefinition( )
			};
			sortDefn[0].setExpression( "row.col3" );//.setColumn("col3");
			sortDefn[0].setSortDirection( ISortDefinition.SORT_ASC );

			// 2.3: ExpressionKey
			expressions = new BaseExpression[]{
					new ScriptExpression( "row.col0", 0 ),
					new ScriptExpression( "row.col1", 0 ),
					new ScriptExpression( "row.col2", 0 ),
					new ScriptExpression( "row.col3", 0 )
			};

			queryDefn = new QueryDefinition( null );

			queryDefn.setDataSetName( dataSetName );

			for ( int i = 0; i < groupDefn.length; i++ )
				queryDefn.addGroup( groupDefn[i] );
			for ( int i = 0; i < sortDefn.length; i++ )
				queryDefn.addSort( sortDefn[i] );
			for ( int i = 0; i < expressions.length; i++ )
				queryDefn.addExpression( expressions[i], BaseTransform.ON_EACH_ROW );

			return queryDefn;
		}
		
		/**
		 * Get query definition with sub query
		 * @return queryDefn
		 */
		protected IQueryDefinition getDefaultQueryDefnWithSubQuery( String dataSetName )
		{
			IQueryDefinition queryDefn = getDefaultQueryDefn( dataSetName );
			
			// row.Col1
			GroupDefinition groupDefn = (GroupDefinition) queryDefn.getGroups().get(1);
			
			// ---------- begin sub query ----------
			SubqueryDefinition subqueryDefn = new SubqueryDefinition( "IAMTEST" );
			groupDefn.addSubquery( subqueryDefn );
			
			GroupDefinition[] subGroupDefn = new GroupDefinition[]{
				new GroupDefinition( "group2" )
			};
			subGroupDefn[0].setKeyExpression( "row.col2" );		
			for ( int i = 0; i < subGroupDefn.length; i++ )
			{
				subqueryDefn.addGroup( subGroupDefn[i] );
			}
			
				// --- sub query of sub query
				SubqueryDefinition subSubqueryDefn = new SubqueryDefinition( "IAMTEST2" );
				subGroupDefn[0].addSubquery( subSubqueryDefn );
				
				GroupDefinition[] subSubGroupDefn = new GroupDefinition[]{
					new GroupDefinition("group3")
				};
				subSubGroupDefn[0].setKeyExpression( "row.col3" );			
				for ( int i = 0; i < subSubGroupDefn.length; i++ )
				{
					subSubqueryDefn.addGroup( subSubGroupDefn[i] );
				}			
				// --- sub query of sub query
			
			// ---------- end sub query ----------
			
			return queryDefn;
		}
		
		/**
		 * Get default query expressions
		 * @return expressions BaseExpression[]
		 */
		protected BaseExpression[] getExpressionsOfDefaultQuery() {
			return expressions;
		}
	}
	
	
}