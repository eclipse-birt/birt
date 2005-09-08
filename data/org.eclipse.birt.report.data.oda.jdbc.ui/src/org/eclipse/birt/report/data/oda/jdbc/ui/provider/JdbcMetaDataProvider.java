/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.data.oda.jdbc.ui.provider;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.StringTokenizer;

import org.eclipse.birt.report.data.oda.jdbc.ui.JdbcPlugin;
import org.eclipse.birt.report.data.oda.jdbc.ui.model.CrossReference;
import org.eclipse.birt.report.data.oda.jdbc.ui.model.JoinCondition;
import org.eclipse.birt.report.data.oda.jdbc.ui.model.JoinImpl;
import org.eclipse.birt.report.data.oda.jdbc.ui.model.JoinType;
import org.eclipse.birt.report.data.oda.jdbc.ui.model.TableImpl;
import org.eclipse.birt.report.data.oda.jdbc.ui.util.Column;
import org.eclipse.birt.report.data.oda.jdbc.ui.util.Constants;
import org.eclipse.birt.report.data.oda.jdbc.ui.util.DriverLoader;
import org.eclipse.birt.report.data.oda.jdbc.ui.util.ProcedureParameter;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.OdaDataSourceHandle;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.PlatformUI;



/**
 * Class to Manage Jdbc Data Sources, has implementation of utility functions
 * such as getting meta data from a Jdbc Data Source
 */
public class JdbcMetaDataProvider implements IMetaDataProvider
{
	
	private Connection jdbcConnection = null;
	private String userName = null;
	private String url = null;
	private String driverClass = null;
	private DatabaseMetaData metaData;
	private HashMap crossReferenceMap = null;
	
	public JdbcMetaDataProvider( Connection connection)
	{
		this.jdbcConnection = connection;
		metaData = null;
		crossReferenceMap = new HashMap();
	}
	

	/**
	 * @return Returns the jdbcConnection.
	 */
	public Connection getJdbcConnection() {
		return jdbcConnection;
	}
	
	/**
	 * @param connection The jdbcConnection to set.
	 */
	public void setJdbcConnection(Connection jdbcConnection) {
		this.jdbcConnection = jdbcConnection;
	}	
	
	public Connection connect( String userName, String password, String url,
			String driverClass, String odaDriverName )
	{
        if( url == null || driverClass == null)
        {
            return null;
        }
		if( ( userName != null && !userName.equals(this.userName))  ||  !url.equals(this.url) || !driverClass.equals(this.driverClass))
		{
			if(jdbcConnection != null)
			{
				closeConnection();
				jdbcConnection = null;
				metaData = null;
			}
			
		}
		if(jdbcConnection == null)
		{
			try
			{
				jdbcConnection = DriverLoader.getConnection( driverClass,
						url,
						userName,
						password );
				if ( jdbcConnection == null )
				{
					MessageDialog.openError( PlatformUI.getWorkbench( )
							.getDisplay( )
							.getActiveShell( ),
							JdbcPlugin.getResourceString("connection.test"),//$NON-NLS-1$
							JdbcPlugin.getResourceString("connection.failed"));//$NON-NLS-1$
				}
				else
				{
				    this.driverClass = driverClass;
				    this.url = url;
				    this.userName = userName;
				}
			}
			catch ( SQLException e )
			{
				ExceptionHandler.handle( e );
			}
			
		}

		return jdbcConnection;

	}
	
	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.birt.report.data.oda.jdbc.ui.provider.IMetadataProvider#connect(org.eclipse.birt.report.model.api.DataSourceHandle)
	 */
	public Connection connect(DataSourceHandle dataSourceHandle)
	{		
		
		OdaDataSourceHandle handle = (OdaDataSourceHandle) dataSourceHandle;
	
		String userName = (String)handle.getProperty(Constants.ODAUser);
		String passWord = (String)handle.getProperty(Constants.ODAPassword);
		String url = (String)handle.getProperty(Constants.ODAURL);
		String driver = (String)handle.getProperty(Constants.ODADriverClass);
		
		jdbcConnection = connect( userName,
				passWord,
				url,
				driver,
				handle.getExtensionID()); 
		
		return jdbcConnection;
	}
	
	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.birt.report.data.oda.jdbc.ui.provider.IMetadataProvider#getConnection()
	 */
	public Connection getConnection( )
	{
		return jdbcConnection;
	}
	
	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.birt.report.data.oda.jdbc.ui.provider.IMetadataProvider#closeConnection()
	 */
	public void closeConnection()
	{
		if ( jdbcConnection != null )
		{
			try {
				if( !jdbcConnection.isClosed())
				{
					jdbcConnection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
    
	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.birt.report.data.oda.jdbc.ui.provider.IMetadataProvider#getDataBaseName()
	 */
    public String getDataBaseName()
    {
    	if( jdbcConnection == null)
    	{
    		return "";
    	}
    	
    	if ( metaData == null)
      	{
      		try
			{
      			metaData = jdbcConnection.getMetaData();
			}
      		catch(SQLException e)
			{
      			ExceptionHandler.handle( e );
			}
      	}
      
    	if(metaData == null)
    	{
    		return "";
    	}
    	
    	String databaseName = "";
    	try {
			databaseName =  metaData.getDatabaseProductName();
		} catch (SQLException e) {

			e.printStackTrace();
		}
		
		if(databaseName == null)
		{
			databaseName = "";
		}
		
		return databaseName;
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.data.oda.jdbc.ui.provider.IMetadataProvider#getMetaData()
	 */
	public DatabaseMetaData getMetaData( )
	{
		if ( jdbcConnection == null )
		{
			return null;
		}

		if ( metaData == null )
		{
			try
			{
				metaData = jdbcConnection.getMetaData( );
			}
			catch ( SQLException e )
			{
				e.printStackTrace( );
			}
		}

		return metaData;
	}
        
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.data.oda.jdbc.ui.provider.IMetadataProvider#getAllSchema()
	 */
	public ResultSet getAllSchema( )
	{
		assert jdbcConnection != null;

		if ( metaData == null )
		{

			metaData = getMetaData( );
		}

		ResultSet schemaRs = null;

		try
		{
			schemaRs = metaData.getSchemas( );
		}
		catch ( SQLException e )
		{
			e.printStackTrace( );
		}

		return schemaRs;
	}
        
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.data.oda.jdbc.ui.provider.IMetadataProvider#isSchemaSupported()
	 */
	public boolean isSchemaSupported( )
	{

		DatabaseMetaData metaData = getMetaData( );
		try
		{
			if ( metaData != null )
			{
				return ( metaData.supportsSchemasInTableDefinitions( ) );
			}
		}
		catch ( SQLException e )
		{
			e.printStackTrace( );
		}

		return false;
	}
  
    /*
     *  (non-Javadoc)
     * @see org.eclipse.birt.report.data.oda.jdbc.ui.provider.IMetadataProvider#getAlltables(java.lang.String, java.lang.String, java.lang.String, java.lang.String[])
     */  
	public ResultSet getAlltables( String cataLog, String schemaPattern,
			String namePattern, String[] types )
	{

		if ( jdbcConnection == null )
		{
			return null;
		}

		if ( metaData == null )
		{

			metaData = getMetaData( );
		}

		ResultSet resultSet = null;
		if ( cataLog != null && cataLog.trim( ).length( ) == 0 )
		{
			cataLog = null;
		}

		if ( metaData != null )
		{
			try
			{
				resultSet = metaData.getTables( cataLog,
						schemaPattern,
						namePattern,
						types );
			}
			catch ( SQLException e )
			{
				e.printStackTrace( );
			}
		}
		return resultSet;
	}
	
	/**
	 * get all procedure from special catalog,schemaPattern,and namePattern
	 */
 	public ResultSet getAllProcedure( String cataLog, String schemaPattern,
			String namePattern )
	{
		if ( metaData == null )
		{

			metaData = getMetaData( );
		}
		ResultSet resultSet = null;
		if ( cataLog != null && cataLog.trim( ).length( ) == 0 )
		{
			cataLog = null;
		}

		if ( metaData != null )
		{
			try
			{
				resultSet = metaData.getProcedures( cataLog,
						schemaPattern,
						namePattern );
			}
			catch ( SQLException e )
			{
			}
		}
		return resultSet;
	}
 	
 	/**
 	 * Get procedure's columns information
 	 */
 	public ArrayList getProcedureColumns(String cataLog,
			  String schemaPattern,
			  String procedureNamePattern, 
			  String columnNamePattern)
	{
 		ArrayList columnList = new ArrayList();
 		if ( metaData == null )
		{
			metaData = getMetaData( );
		}
 		
		ResultSet columnsRs = null;
		if ( cataLog != null && cataLog.trim( ).length( ) == 0 )
		{
			cataLog = null;
		}

		if ( metaData != null )
		{
			try
			{
				columnsRs = metaData.getProcedureColumns( cataLog,
						schemaPattern,
						procedureNamePattern,
						columnNamePattern );
				int n = 0;
				while ( columnsRs.next( ) )
				{
					ProcedureParameter column = new ProcedureParameter( );
					if ( columnsRs.getString( "COLUMN_NAME" )!= null )
						column.setName( columnsRs.getString( "COLUMN_NAME" ) );
					else
					{
						// if the column name cannot retrieved ,give the unique name for this column
						n++;
						column.setName( "param" + n );
					}
					column.setModeType( columnsRs.getInt( "COLUMN_TYPE" ) );
					column.setDataTypeName( columnsRs.getString( "TYPE_NAME" ) );
					column.setDataType( columnsRs.getInt( "DATA_TYPE" ) );
					column.setProcedureName( columnsRs.getString( "PROCEDURE_NAME" ) );

					columnList.add( column );
				}
			}
			catch ( SQLException e )
			{
			}
		}
		return columnList;
	}

 	/**
 	 * is procedure supported
 	 */
	public boolean isProcedureSupported( )
	{
		metaData = getMetaData( );
		try
		{
			if ( metaData != null )
			{
				return ( metaData.supportsStoredProcedures( ) );
			}
		}
		catch ( SQLException e )
		{
			e.printStackTrace( );
		}
		return false;
	}
	
    /*
     * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.data.oda.jdbc.ui.provider.IMetadataProvider#getColumns(java.lang.String,
	 *      java.lang.String, java.lang.String, java.lang.String)
	 */
	public ArrayList getColumns( String catalog, String schemaPattern,
			String tableNamePattern, String columnNamePattern )
        {
        	
        	ArrayList columnList = new ArrayList();
        	if ( metaData == null )
        	{
        		metaData = getMetaData();
        	}
        	
        	try
			{
			ResultSet columnsRs = metaData.getColumns( catalog,
					schemaPattern,
					tableNamePattern,
					columnNamePattern );
        		
        		while( columnsRs.next())
        		{
        			
        			Column column = new Column();
        			column.setSchemaName(columnsRs.getString("TABLE_SCHEM"));
        			column.setTableName(columnsRs.getString("TABLE_NAME"));
        			column.setName(columnsRs.getString("COLUMN_NAME"));
        			column.setDbType(columnsRs.getString("TYPE_NAME"));
        			
        			columnList.add(column);
        			
        		}
			}
        	catch(SQLException e)
			{
        		e.printStackTrace();
			}
        	
        	return columnList;
        }
        
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.data.oda.jdbc.ui.provider.IMetadataProvider#getCatalog()
	 */
	public String getCatalog( )
	{
		String cataLog = null;
		if ( jdbcConnection != null )
		{
			try
			{
				cataLog = jdbcConnection.getCatalog( );
			}
			catch ( SQLException e )
			{
				e.printStackTrace( );
			}
		}

		return cataLog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.data.oda.jdbc.ui.provider.IMetadataProvider#getCatalogSeparator()
	 */
	public String getCatalogSeparator( )
	{

		if ( metaData == null )
		{
			metaData = getMetaData( );
		}

		try
		{
			String separator = metaData.getCatalogSeparator( );
			if ( separator == null || separator.trim( ).length( ) == 0 )
			{
				separator = ".";
			}
			return separator;

		}
		catch ( SQLException e )
		{
			e.printStackTrace( );
		}

		return null;
	}
	
	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.birt.report.data.oda.jdbc.ui.provider.IMetadataProvider#getCrossReferences(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public ArrayList getCrossReferences( String primarySchema,
			String primaryTable, String foreignSchema, String foreignTable )
	{
		if ( metaData == null )
		{
			return null;
		}

		String fullPrimaryTableName = primaryTable;
		String fullForeignTableName = foreignTable;

		if ( primarySchema != null )
		{
			fullPrimaryTableName = primarySchema + "." + fullPrimaryTableName;
		}

		if ( foreignSchema != null )
		{
			fullForeignTableName = foreignSchema + "." + fullForeignTableName;
		}

		// If already retrieved then, get it from the Hash

		ArrayList crossReferences = (ArrayList) crossReferenceMap.get( fullForeignTableName );
		if ( crossReferences != null )
		{
			return crossReferences;
		}
		else
		{
			crossReferences = new ArrayList( );
		}

		ResultSet crossReferencesRs = null;

		try
		{
			crossReferencesRs = metaData.getCrossReference( null,
					primarySchema,
					primaryTable,
					null,
					foreignSchema,
					foreignTable );

			// test
			// crossReferencesRs = metaData.getCrossReference(null,
			// primarySchema, "customers", null, foreignSchema, "orders");

			while ( crossReferencesRs.next( ) )
			{
				String pkSchema = crossReferencesRs.getString( "PKTABLE_SCHEM" );
				String pkTable = crossReferencesRs.getString( "PKTABLE_NAME" );
				String pkColumn = crossReferencesRs.getString( "PKCOLUMN_NAME" );
				String fkSchema = crossReferencesRs.getString( "FKTABLE_SCHEM" );
				String fkTable = crossReferencesRs.getString( "FKTABLE_NAME" );
				String fkColumn = crossReferencesRs.getString( "FKCOLUMN_NAME" );

				String pkFullColumnName = pkTable + "." + pkColumn;
				String fkFullColumnName = fkTable + "." + fkColumn;

				if ( pkSchema != null && pkSchema.length( ) > 0 )
				{
					pkFullColumnName = pkSchema + "." + pkFullColumnName;
				}

				if ( fkSchema != null && fkSchema.length( ) > 0 )
				{
					fkFullColumnName = fkSchema + "." + fkFullColumnName;
				}

				CrossReference crossReference = new CrossReference( pkSchema,
						pkTable,
						pkColumn,
						fkSchema,
						fkTable,
						fkColumn );
				crossReferences.add( crossReference );
			}
			if ( crossReferences != null && crossReferences.size( ) > 0 )
			{
				crossReferenceMap.put( fullPrimaryTableName, crossReferences );
			}

		}
		catch ( SQLException e )
		{
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}

		return crossReferences;
	}

	
	public static String constructSql( ArrayList tableList,
			ArrayList columnList, ArrayList joinList,
			IMetaDataProvider metaDataProvider )
	{

		// String used to quote the identifiers by default
		// when they have to be escaped
		String quoteString = null;
		// String extraNameCharacters = null;
		// String searchEscapeCharacter = null;

		String databaseName = "";
		boolean schemaSupported = true;

		if ( metaDataProvider != null )
		{
			databaseName = metaDataProvider.getDataBaseName( );
			schemaSupported = metaDataProvider.isSchemaSupported( );
		}

		// char escapeChar = '\'';

		if ( metaDataProvider != null )
		{
			DatabaseMetaData metaData = metaDataProvider.getMetaData( );
			if ( metaData != null )
			{
				try
				{
					quoteString = metaData.getIdentifierQuoteString( );
					// extraNameCharacters = metaData.getExtraNameCharacters();
					// searchEscapeCharacter = metaData.getSearchStringEscape();

				}
				catch ( SQLException e )
				{
					e.printStackTrace( );
				}
			}
		}

		if ( quoteString == null || quoteString.trim( ).length( ) == 0 )
		{
			quoteString = "\"";
		}

		String[] delimitors = {
				quoteString, quoteString
		};

		// Handling for special cases
		getQuoteString( databaseName, delimitors );

		String startDelimitor = delimitors[0];
		String endDelimitor = delimitors[1];

		String sqlStatement = null;

		String SELECT_COLUMN_SPACING = "        ";
		String SELECT = " SELECT ";
		if ( tableList != null && columnList != null )
		{

			// The Select clause built from the column list
			StringBuffer selectClause = new StringBuffer( SELECT );//$NON-NLS-1$

			int columnCount = columnList.size( );

			for ( int i = 0; i < columnCount; i++ )
			{
				String columnName = (String) columnList.get( i );
				// selectClause.append(columnName);
				selectClause.append( quoteName( columnName,
						startDelimitor,
						endDelimitor ) );

				if ( i != ( columnCount - 1 ) )
				{
					selectClause.append( "," );//$NON-NLS-1$
					selectClause.append( "\n" );
					selectClause.append( SELECT_COLUMN_SPACING );
				}
			}

			selectClause.append( " " );//$NON-NLS-1$
			// StringBuffer fromClause = new StringBuffer(" FROM
			// ");//$NON-NLS-1$

			String fromAndWhereClause = null;
			fromAndWhereClause = getFromAndOnClause( tableList,
					joinList,
					startDelimitor,
					endDelimitor,
					schemaSupported );
			sqlStatement = selectClause.toString( ) + "\n" + fromAndWhereClause;
		}

		return sqlStatement;
	}

	public static String getFromAndOnClause( ArrayList tableList,
			ArrayList joins, String startDelimitor, String endDelimitor,
			boolean schemaSupported )
	{

		StringBuffer fromClause = new StringBuffer( "" );//$NON-NLS-1$
		String FROM_CLAUSE_SPACING = "      ";

		// ArrayList fromTables = new ArrayList();

		if ( joins == null || joins.size( ) == 0 )
		{
			// No Joins
			// Just add the table names in the FROM clause
			int tableCount = tableList.size( );
			for ( int i = 0; i < tableCount; i++ )
			{
				TableImpl tableItem = (TableImpl) tableList.get( i );
				String tableAlias = tableItem.getTableAlias( );
				String tableName = tableItem.getFullyQualifiedName( );
				if ( tableAlias != null && tableAlias.length( ) > 0 )
				{
					// Table Alias and the actual table name differ
					if ( !tableAlias.equalsIgnoreCase( tableName ) )
					{
						if ( fromClause.length( ) > 0 )
						{
							fromClause.append( FROM_CLAUSE_SPACING );
						}

						// ORIGINAL CODE
						fromClause.append( quoteName( tableName,
								startDelimitor,
								endDelimitor ) );

						fromClause.append( " AS " );
						fromClause.append( tableAlias );
					}
					else
					{
						if ( fromClause.length( ) > 0 )
						{
							fromClause.append( FROM_CLAUSE_SPACING );
							;
						}

						fromClause.append( quoteName( tableName,
								startDelimitor,
								endDelimitor ) );

						// fromClause.append(tableName);
					}
				}
				else
				{
					if ( fromClause.length( ) > 0 )
					{
						fromClause.append( FROM_CLAUSE_SPACING );
					}

					// ORIGINAL CODE
					fromClause.append( quoteName( tableName,
							startDelimitor,
							endDelimitor ) );

					// fromClause.append(tableName);
				}

				if ( i != ( tableCount - 1 ) )
				{
					fromClause.append( " , " );
					fromClause.append( "\n" );
				}
			}

		}

		else
		{
			createJoinClause( joins,
					fromClause,
					startDelimitor,
					endDelimitor,
					tableList );
		}

		String fromClauseStr = "";
		if ( fromClause.length( ) > 0 )
		{
			fromClauseStr = " FROM " + fromClause.toString( );
		}

		return fromClauseStr;
	}
	
	public static String getJoinText(int joinType)
	{
		if((joinType == -1) || (joinType == JoinType.INNER))
		{
			return(" INNER JOIN ");
		}
		else if( joinType == JoinType.LEFT_OUTER )
		{
			 return(" LEFT OUTER JOIN ");
		}
		else if( joinType == JoinType.RIGHT_OUTER )
		{
			return(" RIGHT OUTER JOIN ");
		}
		else if ( joinType == JoinType.FULL_OUTER)
		{
			return(" FULL OUTER JOIN ");
		}
		else
		{
			return( " INNER JOIN ");
		}

	}
	
	private static String quoteName(String name, String startDelimitor, String endDelimitor)
	{
		if ( name == null || startDelimitor == null || endDelimitor ==null )
		{
			return name;
		}
		
		if( name.trim().equals("*"))
		{
			return name;
		}
		
		StringTokenizer st = new StringTokenizer(name, ".");
		
		String delimitedName = "";
		int count = 0;
		int tokenCount = st.countTokens();
	     while (st.hasMoreTokens()) 
	     {
	     	String token = st.nextToken();
	     	// IF the delimitor is a quote , escape the
	     	// quotes within the string with 2 double quotes
	     	if( startDelimitor.equals("\"") && endDelimitor.equals("\""))
	     	{
	     		
	     		if( token.indexOf('"') != -1 )
				{
					StringBuffer escapedToken = new StringBuffer();
					for(int i=0; i< token.length(); i++)
					{
						char c = token.charAt(i);
						if( c == '"')
						{
							escapedToken.append("\"\"");
						}
						else
						{
							escapedToken.append(c);
						}
					}
					token = escapedToken.toString();
				}
	     	}
	     	
			delimitedName = delimitedName + startDelimitor + token + endDelimitor;
			if(  count != tokenCount - 1)
			{
				delimitedName = delimitedName + ".";
			}
			count++;
	     }

		return delimitedName;
	}
	
	
	private static void getQuoteString(String databaseName,  String[] delimitors)
	{
		
		if( delimitors == null || delimitors.length < 2)
		{
			return;
		}
		if("ACCESS".equalsIgnoreCase(databaseName))
		{
			delimitors[0] = "[";
			delimitors[1] = "]";
		}
		else if ( "Informix Dynamic Server".equalsIgnoreCase(databaseName))
		{
			delimitors[0] = null;
			delimitors[1] = null;
		}
		
		
	}
	
	private static void createJoinClause(ArrayList joins, StringBuffer fromClause, 
				String startDelimitor, String endDelimitor, ArrayList tableList)
	{
		
		String FROM_CLAUSE_SPACING = "      ";
		
		LinkedHashMap joinMap = new LinkedHashMap();
		
		Iterator joinIterator = joins.iterator();
		ArrayList fromTables = new ArrayList();
		
		while( joinIterator.hasNext())
		{
			
			JoinImpl join = (JoinImpl)joinIterator.next();
			JoinCondition joinCondition = join.getCondition();
			if(joinCondition == null)
			{
				continue;
			}
			String leftTable = join.getLeft().getFullyQualifiedName();
			String rightTable = join.getRight().getFullyQualifiedName();
			
			
			String leftColumn = joinCondition.getLeftExpr();
			String rightColumn = joinCondition.getRightExpr();
		
	
			int joinType = joinCondition.getJoinType();
			
			String joinTypeTxt = getJoinText(joinType);
			

			String leftTableName = quoteName(leftTable, startDelimitor, endDelimitor );
			String rightTableName = quoteName(rightTable, startDelimitor, endDelimitor);
			String leftColumnName = quoteName(leftColumn, startDelimitor, endDelimitor);
			String rightColumnName = quoteName(rightColumn, startDelimitor, endDelimitor);
	
			
			
	    	String leftColumnFullName = leftTableName + "." + leftColumnName;
	    	String rightColumnFullName = rightTableName + "." + rightColumnName;

 
			String key1 = leftTableName + joinTypeTxt + rightTableName;
			
			if ( joinMap.size() > 0)
			{
				if ( fromTables.contains(leftTable) && fromTables.contains(rightTable))
				{
					key1 = leftTableName + joinTypeTxt + rightTableName;
				}
				else if ( fromTables.contains(leftTable) && joinType == JoinType.INNER )
				{
					key1 = " " + joinTypeTxt + rightTableName ;
				}
				else if ( fromTables.contains(rightTable) && joinType == JoinType.INNER  )
				{
					key1 = " " + joinTypeTxt + leftTableName ;
				}
			}
			
			if ( !fromTables.contains(leftTable) )
			{
				fromTables.add(leftTable);
			}
			
			if ( ! fromTables.contains(rightTable))
			{
				fromTables.add(rightTable);
			}
			
			String joinClause = (String)( joinMap.get(key1));
			
			String condition =  " ( " + leftColumnFullName + " " + joinCondition.getOperatorString() +   " " + rightColumnFullName + " ) ";
			
			if ( joinClause != null )
			{
				condition =  "(" + joinClause + " AND " + condition + ")";
			}
			    	
			joinMap.put(key1, condition);
		}
		
		// Go through the Linked Map and prepare the from clause
		
		Iterator fromTablesIterator = joinMap.keySet().iterator();
		int count = 0;
		while( fromTablesIterator.hasNext())
		{
			String tables = (String)fromTablesIterator.next();
			if ( tables != null )
			{
				if ( count < joinMap.size() - 1)
				{
					//fromClause.append(" ( ");
					fromClause.insert(0, "(");
				}
				else
				{
					fromClause.append(" ");
				}
				fromClause.append( tables );
				fromClause.append( " ON ");
				fromClause.append((String)joinMap.get(tables));
				if ( count < joinMap.size() -1 )
				{
					fromClause.append(" ) ");
				}
				else
				{
					fromClause.append(" ");
				}

				fromClause.append("\n");
				count++;
			}
		}
		
		
     	// After Having added all the join conditions 
    	// Add the other tables
    	Iterator it1 = tableList.iterator();
//    	Iterator it2 = fromTables.iterator();
    	
    	while(it1.hasNext())
    	{
    		TableImpl table = (TableImpl)it1.next();
    		String tableName = table.getFullyQualifiedName();
    		String alias = table.getTableAlias();
    		if(fromTables.contains(tableName))
    		{
    			continue;
    		}
    		fromClause.append(",");
    		fromClause.append(FROM_CLAUSE_SPACING);
    		// ORIGINAL CODE
    		fromClause.append(quoteName(tableName, startDelimitor, endDelimitor ));
    		
    		//fromClause.append(tableName);
    		
    		if(alias != null && alias.length() >0 && !tableName.equalsIgnoreCase(alias))
    		{
    			fromClause.append(" AS ");
				fromClause.append(alias);
    		}
    		fromClause.append("\n");
    	}
	}
}
