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

import org.eclipse.birt.report.data.oda.jdbc.ui.JdbcPlugin;
import org.eclipse.birt.report.data.oda.jdbc.ui.util.Column;
import org.eclipse.birt.report.data.oda.jdbc.ui.util.Constants;
import org.eclipse.birt.report.data.oda.jdbc.ui.util.DriverLoader;
import org.eclipse.birt.report.data.oda.jdbc.ui.util.ExceptionHandler;
import org.eclipse.birt.report.data.oda.jdbc.ui.util.Procedure;
import org.eclipse.birt.report.data.oda.jdbc.ui.util.ProcedureParameter;
import org.eclipse.datatools.connectivity.oda.design.DataSourceDesign;
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
	private String pass = null;
	private DatabaseMetaData metaData;
	
	public JdbcMetaDataProvider( Connection connection)
	{
		this.jdbcConnection = connection;
		metaData = null;
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
				    this.pass = password;
				}
			}
			catch ( SQLException e )
			{
				ExceptionHandler.showException( PlatformUI.getWorkbench( )
						.getDisplay( )
						.getActiveShell( ),
						JdbcPlugin.getResourceString( "exceptionHandler.title.error" ),
						e.getLocalizedMessage( ),
						e );
				return null;
			}
			
		}

		return jdbcConnection;

	}
	
	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.birt.report.data.oda.jdbc.ui.provider.IMetadataProvider#connect(org.eclipse.birt.report.model.api.DataSourceHandle)
	 */
	public Connection connect(DataSourceDesign dataSourceHandle)
	{		
		
		DataSourceDesign handle = (DataSourceDesign) dataSourceHandle;
	
		String userName = (String) handle.getPublicProperties( )
				.findProperty( Constants.ODAUser )
				.getValue( );
		String passWord = (String) handle.getPublicProperties( )
				.findProperty( Constants.ODAPassword )
				.getValue( );
		String url = (String) handle.getPublicProperties( )
				.findProperty( Constants.ODAURL )
				.getValue( );
		String driver = (String) handle.getPublicProperties( )
				.findProperty( Constants.ODADriverClass )
				.getValue( );
		
		jdbcConnection = connect( userName,
				passWord,
				url,
				driver,
				handle.getOdaExtensionId( ) ); 
		
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
      		catch ( SQLException e )
			{
				ExceptionHandler.showException( PlatformUI.getWorkbench( )
						.getDisplay( )
						.getActiveShell( ),
						JdbcPlugin.getResourceString( "exceptionHandler.title.error" ),
						e.getLocalizedMessage( ),
						e );
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
			try
			{
				schemaRs = reTryAchieveResultSet("SCHEMAS", null);
			}
			catch ( SQLException e1 )
			{
				e1.printStackTrace();
			}
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
				try
				{
					return reTryAchieveResultSet("TABLES", new Object[]{cataLog, schemaPattern, namePattern, types});
				}
				catch ( SQLException e1 )
				{
					e1.printStackTrace();
				}
			}
		}
		return resultSet;
	}
  
	/**
	 * get all procedure from special catalog,schemaPattern,and namePattern
	 */
 	public ArrayList getAllProcedure( String cataLog, String schemaPattern,
			String namePattern )
	{
 		ArrayList procedureList = new ArrayList();
		if ( metaData == null )
		{

			metaData = getMetaData( );
		}
		ResultSet procedureRs = null;
		if ( cataLog != null && cataLog.trim( ).length( ) == 0 )
		{
			cataLog = null;
		}

		if ( metaData != null )
		{
			try
			{
				try
				{
					procedureRs = metaData.getProcedures( cataLog,
							schemaPattern,
							namePattern );
				}
				catch ( SQLException e )
				{
					procedureRs = reTryAchieveResultSet("PROCEDURE", new Object[]{ cataLog, schemaPattern, namePattern});
				}
				boolean isSame;
				while ( procedureRs.next( ) )
				{
					isSame = false;
					Procedure procedure = new Procedure( );
					//Here the sequence of geting statements are significant for some data base driver do not all the
					//retrievement of an element of high index once an element of low index is retrieved from ResultSet 
					procedure.setCatalog( procedureRs.getString( "PROCEDURE_CAT" ) );
					procedure.setSchema( procedureRs.getString( "PROCEDURE_SCHEM" ) );
					procedure.setProcedureName( procedureRs.getString( "PROCEDURE_NAME" ) );
					
					
					for ( int i = 0; i < procedureList.size( ); i++ )
					{
						if ( ( (Procedure) ( procedureList.get( i ) ) ).isEqualWith( procedure ) )
							isSame = true;
					}
					if ( !isSame )
						procedureList.add( procedure );
				}
			}
			catch ( SQLException e )
			{
				e.printStackTrace();
			}
		}
		return procedureList;
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
				try
				{
					columnsRs = metaData.getProcedureColumns( cataLog,
							schemaPattern,
							procedureNamePattern,
							columnNamePattern );
				}
				catch ( SQLException e )
				{
					columnsRs = reTryAchieveResultSet( "PROCEDURE_COLUMNS", new Object[]{cataLog,
							schemaPattern,
							procedureNamePattern,
							columnNamePattern });
				}
				int n = 0;
				while ( columnsRs.next( ) )
				{
					ProcedureParameter column = new ProcedureParameter( );
					column.setSchema( schemaPattern );
					//Here the sequence of geting statements are significant for some data base driver do not all the
					//retrievement of an element of high index once an element of low index is retrieved from ResultSet 
					column.setProcedureName( columnsRs.getString( "PROCEDURE_NAME" ) );
					String columnName = columnsRs.getString( "COLUMN_NAME" );
					if ( columnName != null )
						column.setName( columnName );
					else
					{
						// if the column name cannot retrieved ,give the unique name for this column
						n++;
						column.setName( "param" + n );
					}
					column.setModeType( columnsRs.getInt( "COLUMN_TYPE" ) );
					column.setDataType( columnsRs.getInt( "DATA_TYPE" ) );
					column.setDataTypeName( columnsRs.getString( "TYPE_NAME" ) );
					
					

					columnList.add( column );
				}
			}
			catch ( SQLException e )
			{
				e.printStackTrace();
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

		ArrayList columnList = new ArrayList( );
		if ( metaData == null )
		{
			metaData = getMetaData( );
		}

		try
		{
			ResultSet columnsRs = null;
			try
			{
				columnsRs = metaData.getColumns( catalog,
						schemaPattern,
						tableNamePattern,
						columnNamePattern );
			}
			catch ( SQLException e )
			{
				columnsRs = reTryAchieveResultSet("TABLE_COLUMNS",new Object[]{ catalog,
						schemaPattern,
						tableNamePattern,
						columnNamePattern });
			}

			while ( columnsRs.next( ) )
			{
				//Here the sequence of geting statements are significant for some data base driver do not all the
				//retrievement of an element of high index once an element of low index is retrieved from ResultSet 

				Column column = new Column( );
				column.setSchemaName( columnsRs.getString( "TABLE_SCHEM" ) );
				column.setTableName( columnsRs.getString( "TABLE_NAME" ) );
				column.setName( columnsRs.getString( "COLUMN_NAME" ) );
				column.setDbType( columnsRs.getString( "TYPE_NAME" ) );

				columnList.add( column );

			}
		}
		catch ( SQLException e )
		{
			e.printStackTrace( );
		}

		return columnList;
	}


	/**
	 * @param catalog
	 * @param schemaPattern
	 * @param tableNamePattern
	 * @param columnNamePattern
	 * @return
	 * @throws SQLException
	 */
	private void resetMetadata( ) throws SQLException
	{
		jdbcConnection = DriverLoader.getConnection( driverClass,
				url,
				userName,
				pass );
		metaData = jdbcConnection.getMetaData();
		
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
				try
				{
					this.closeConnection( );
					this.resetMetadata( );
					cataLog = jdbcConnection.getCatalog( );
				}
				catch ( SQLException e1 )
				{
					e1.printStackTrace( );
				}
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
//	public ArrayList getCrossReferences( String primarySchema,
//			String primaryTable, String foreignSchema, String foreignTable )
//	{
//		if ( metaData == null )
//		{
//			return null;
//		}
//
//		String fullPrimaryTableName = primaryTable;
//		String fullForeignTableName = foreignTable;
//
//		if ( primarySchema != null )
//		{
//			fullPrimaryTableName = primarySchema + "." + fullPrimaryTableName;
//		}
//
//		if ( foreignSchema != null )
//		{
//			fullForeignTableName = foreignSchema + "." + fullForeignTableName;
//		}
//
//		// If already retrieved then, get it from the Hash
//
//		ArrayList crossReferences = (ArrayList) crossReferenceMap.get( fullForeignTableName );
//		if ( crossReferences != null )
//		{
//			return crossReferences;
//		}
//		else
//		{
//			crossReferences = new ArrayList( );
//		}
//
//		ResultSet crossReferencesRs = null;
//
//		try
//		{
//			try {crossReferencesRs = metaData.getCrossReference( null,
//					primarySchema,
//					primaryTable,
//					null,
//					foreignSchema,
//					foreignTable );
//			}catch (SQLException e)
//			{
//				
//				crossReferencesRs = reTryAchieveResultSet( "CROSSREFERENCE", new Object[]{null,
//						primarySchema,
//						primaryTable,
//						null,
//						foreignSchema,
//						foreignTable });
//			}
//			// test
//			// crossReferencesRs = metaData.getCrossReference(null,
//			// primarySchema, "customers", null, foreignSchema, "orders");
//
//			while ( crossReferencesRs.next( ) )
//			{
//				//Here the sequence of geting statements are significant for some data base driver do not all the
//				//retrievement of an element of high index once an element of low index is retrieved from ResultSet 
//				String pkSchema = crossReferencesRs.getString( "PKTABLE_SCHEM" );
//				String pkTable = crossReferencesRs.getString( "PKTABLE_NAME" );
//				String pkColumn = crossReferencesRs.getString( "PKCOLUMN_NAME" );
//				String fkSchema = crossReferencesRs.getString( "FKTABLE_SCHEM" );
//				String fkTable = crossReferencesRs.getString( "FKTABLE_NAME" );
//				String fkColumn = crossReferencesRs.getString( "FKCOLUMN_NAME" );
//
//				String pkFullColumnName = pkTable + "." + pkColumn;
//				String fkFullColumnName = fkTable + "." + fkColumn;
//
//				if ( pkSchema != null && pkSchema.length( ) > 0 )
//				{
//					pkFullColumnName = pkSchema + "." + pkFullColumnName;
//				}
//
//				if ( fkSchema != null && fkSchema.length( ) > 0 )
//				{
//					fkFullColumnName = fkSchema + "." + fkFullColumnName;
//				}
//
//				CrossReference crossReference = new CrossReference( pkSchema,
//						pkTable,
//						pkColumn,
//						fkSchema,
//						fkTable,
//						fkColumn );
//				crossReferences.add( crossReference );
//			}
//			if ( crossReferences != null && crossReferences.size( ) > 0 )
//			{
//				crossReferenceMap.put( fullPrimaryTableName, crossReferences );
//			}
//
//		}
//		catch ( SQLException e )
//		{
//			// TODO Auto-generated catch block
//			//e.printStackTrace();
//		}
//
//		return crossReferences;
//	}
	/**
	 * When using some driver, one connection metaData can only use methods like
	 * "getColumns()" once in its lifecycle, i.e. jdbc-odbc-sqlserver driver.
	 * Some other drivers, such as IBM db2 driver, will occationly throw
	 * exceptions once one method is executed multiple times. Most of these
	 * driver-specific problem can be resolved by re-connecting to the data
	 * source. This method is used to do that job.
	 * 
	 * @param name
	 * @param arguments
	 * @return
	 * @throws SQLException
	 */
	private ResultSet reTryAchieveResultSet( String name, Object[] arguments )
			throws SQLException
	{
		this.closeConnection( );
		this.resetMetadata( );
		if ( name.equalsIgnoreCase( "SCHEMAS" ) )
		{
			return metaData.getSchemas( );
		}
		else if ( name.equalsIgnoreCase( "TABLES" ) )
		{
			return metaData.getTables( String.valueOf( arguments[0] ),
					String.valueOf( arguments[1] ),
					String.valueOf( arguments[2] ),
					arguments[3] == null ? null : (String[]) arguments[3] );
		}
		else if ( name.equalsIgnoreCase( "PROCEDURE" ) )
		{
			return metaData.getProcedures( String.valueOf( arguments[0] ),
					String.valueOf( arguments[1] ),
					String.valueOf( arguments[2] ) );
		}
		else if ( name.equalsIgnoreCase( "PROCEDURE_COLUMNS" ) )
		{
			return metaData.getProcedureColumns( String.valueOf( arguments[0] ),
					String.valueOf( arguments[1] ),
					String.valueOf( arguments[2] ),
					String.valueOf( arguments[3] ) );
		}
		else if ( name.equalsIgnoreCase( "TABLE_COLUMNS" ) )
		{
			return metaData.getColumns( String.valueOf( arguments[0] ),
					String.valueOf( arguments[1] ),
					String.valueOf( arguments[2] ),
					String.valueOf( arguments[3] ) );
		}
		else if ( name.equalsIgnoreCase( "CROSSREFERENCE" ) )
		{
			return metaData.getCrossReference( String.valueOf( arguments[0] ),
					String.valueOf( arguments[1] ),
					String.valueOf( arguments[2] ),
					String.valueOf( arguments[3] ),
					String.valueOf( arguments[4] ),
					String.valueOf( arguments[5] ) );
		}

		return null;
	}
}
