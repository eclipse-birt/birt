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
import java.util.Iterator;

import org.eclipse.birt.report.data.oda.jdbc.ui.JdbcPlugin;
import org.eclipse.birt.report.data.oda.jdbc.ui.model.JoinCondition;
import org.eclipse.birt.report.data.oda.jdbc.ui.model.JoinImpl;
import org.eclipse.birt.report.data.oda.jdbc.ui.model.JoinType;
import org.eclipse.birt.report.data.oda.jdbc.ui.model.TableImpl;
import org.eclipse.birt.report.data.oda.jdbc.ui.util.DriverLoader;
import org.eclipse.birt.report.data.oda.jdbc.ui.util.JdbcToolKit;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.PlatformUI;



/**
 * Class to Manage Jdbc Data Sources
 * Has implementation of utility functions such as getting meta data
 * from a Jdbc Data Source 
 */
public class JdbcMetaDataProvider 
{
	
	private Connection jdbcConnection;
	private String userName = null;
	private String password = null;
	private String url = null;
	private String driverClass = null;
	
	private DatabaseMetaData metaData;
	
	public Connection connect( String userName, String password, String url,
			String driverClass, String odaDriverName )
	{
        if(userName == null || url == null || driverClass == null)
        {
            return null;
        }
		if(!userName.equals(this.userName) ||  !url.equals(this.url) || !driverClass.equals(this.driverClass))
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
			String additionalClassPath = JdbcToolKit.getJdbcDriverClassPath( odaDriverName );
		
			try
			{
				jdbcConnection = DriverLoader.getConnection( driverClass,
						additionalClassPath,
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
				    this.password = password;
				}
			}
			catch ( SQLException e )
			{
				e.printStackTrace( );
			}
			
		}

		return jdbcConnection;

	}
	
	public JdbcMetaDataProvider( Connection connection)
	{
		jdbcConnection = connection;
		metaData = null;
	}
	
	/*
	 * Gets the CataLog for the Jdbc connection
	 */
    public String getCatalog()
    {
        String cataLog = null;
        if(jdbcConnection != null)
        {
            try
            {
                cataLog = jdbcConnection.getCatalog();
            }
            catch( SQLException e)
            {
                e.printStackTrace();
            }
        }
    	
    	return cataLog;
    }
    
	/*
	 * Gets the CataLog for the Jdbc connection
	 */
    public String getDataBaseName()
    {
    	if( jdbcConnection == null)
    	{
    		return null;
    	}
    	
    	if ( metaData == null)
      	{
      		try
			{
      			metaData = jdbcConnection.getMetaData();
			}
      		catch(SQLException e)
			{
      			e.printStackTrace();
			}
      	}
      
    	if(metaData == null)
    	{
    		return null;
    	}
    	
    	String databaseName = "";
    	try {
			databaseName =  metaData.getDatabaseProductName();
		} catch (SQLException e) {

			e.printStackTrace();
		}
		
		return databaseName;
		
    	
    	
    }

    /**
     *  Gets the Meta Data for the connection 
     * 
     */
      public DatabaseMetaData getMetaData()
      {
      	assert jdbcConnection != null;
      	
      	if ( metaData == null)
      	{
      		try
			{
      			metaData = jdbcConnection.getMetaData();
			}
      		catch(SQLException e)
			{
      			e.printStackTrace();
			}
      	}
      	
      	return metaData;
      }
      
      
        /**
	 * @param schemaPattern:
	 *            The schem from which the tables will be selected
	 * @param namePattern:
	 *            Will be used to return all objects matching this string
	 * @param Type:
	 *            All the standard Jdbc Types such as "TABLE", "VIEW", "SYSTEM
	 *            TABLE"
         * @return : The ResultSet containing the Tables
         */
	public ResultSet getAlltables( String cataLog, String schemaPattern,
			String namePattern, String[] types )
        {
        	assert jdbcConnection != null;
        	
        	if ( metaData == null )
        	{
        	
        		metaData = getMetaData();
        	}
        	
        	ResultSet resultSet = null;
        	if( cataLog != null && cataLog.trim().length() == 0)
        	{
        		cataLog = null;
        	}
        	
        	if (metaData != null )
        	{
        		try
				{
				resultSet = metaData.getTables( cataLog,
						schemaPattern,
						namePattern,
						types );
				}
        		catch(SQLException e)
				{
        			e.printStackTrace();
				}
        	}
        	return resultSet;
        }
        
        public ResultSet getAllSchema()
        {
        	assert jdbcConnection != null;
        	
        	if ( metaData == null )
        	{
        	
        		metaData = getMetaData();
        	}
        	
        	ResultSet schemaRs = null;
        	
        	try
			{
        		schemaRs = metaData.getSchemas();
			}
        	catch( SQLException e)
			{
        		e.printStackTrace();
			}
        	
        	return schemaRs;
        }
        
        public boolean isSchemaSupported()
        {
        	
        	DatabaseMetaData metaData = getMetaData();
        	try
			{
        		if ( metaData != null )
        		{
        			return ( metaData.supportsSchemasInTableDefinitions());
        		}
			}
        	catch( SQLException e)
			{
        		e.printStackTrace();
			}
        	
        	return false;
        }
        
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
        			columnList.add( columnsRs.getString("COLUMN_NAME"));//$NON-NLS-1$
        		}
			}
        	catch(SQLException e)
			{
        		e.printStackTrace();
			}
        	
        	return columnList;
        }
        
        public String getCatalogSeparator()
        {
        	
        	if ( metaData == null )
        	{
        		metaData = getMetaData();
        	}
        	
        	try
			{
        		String separator =  metaData.getCatalogSeparator();
        		if(separator == null || separator.trim().length() ==0)
        		{
        			separator = ".";
        		}
        		return separator;

			}
        	catch(SQLException e)
			{
        		e.printStackTrace();
			}
        	
        	
        	return null;
        }
        
	public static String constructSql( ArrayList tableList,
			ArrayList columnList, ArrayList joinList )
        {
        	String sqlStatement = null;
        	String SELECT = " SELECT ";
        	String SELECT_COLUMN_SPACING = "        ";
        	if ( tableList != null && columnList != null)
        	{
        		
        		// The Select clause built from the column list
        		StringBuffer selectClause = new StringBuffer(SELECT);//$NON-NLS-1$
        		
        		int columnCount = columnList.size();
        		
        		for ( int i=0; i < columnCount; i++)
        		{
        			selectClause.append((String)columnList.get(i));
        			if ( i != (columnCount -1))
        			{
        				 selectClause.append(",");//$NON-NLS-1$
        				 selectClause.append("\n");
        				 selectClause.append(SELECT_COLUMN_SPACING);
        			}
        		}
        		
        		selectClause.append(" ");//$NON-NLS-1$
        		StringBuffer fromClause = new StringBuffer(" FROM ");//$NON-NLS-1$
        		
        		String fromAndWhereClause = null;
       			fromAndWhereClause = getFromAndOnClause(tableList, joinList);
        		sqlStatement = selectClause.toString() + "\n" + fromAndWhereClause;
        	}
        	
        	return sqlStatement;
        }

        
        public static String  getFromAndOnClause(ArrayList tableList, ArrayList joins)
        {
        	
        	StringBuffer fromClause = new StringBuffer("");//$NON-NLS-1$
        	String FROM_CLAUSE_SPACING = "      ";
        	
        	ArrayList fromTables = new ArrayList();
        	
        	if(joins == null || joins.size() ==0)
        	{
        		// No Joins
        		// Just add the table names in the FROM clause
        		int tableCount = tableList.size();
        		for ( int i=0; i < tableCount; i++)
        		{
        			TableImpl  tableItem = (TableImpl)tableList.get(i);
        			String tableAlias = tableItem.getTableAlias();
        			String tableName = tableItem.getFullyQualifiedName();
        			if( tableAlias != null && tableAlias.length() > 0)
        			{
        				// Table Alias and the actual table name differ
        				if( !tableAlias.equalsIgnoreCase(tableName))
        				{
        					if(fromClause.length() > 0)
        					{
        						fromClause.append(FROM_CLAUSE_SPACING);
        					}
        					fromClause.append(tableName);
        					fromClause.append( " AS ");
							fromClause.append(tableAlias);
						}
        				else
        				{
        					if(fromClause.length() > 0)
        					{
        						fromClause.append(FROM_CLAUSE_SPACING);;
        					}
        					fromClause.append(tableName);
        				}
        			}
        			else
        			{
        				if( fromClause.length() > 0)
        				{
        					fromClause.append(FROM_CLAUSE_SPACING);
        				}
    					fromClause.append(tableName);
        			}
        			
        			if ( i != (tableCount -1))
        			{
        				fromClause.append(" , ");
        				fromClause.append("\n");
        			}
        		}

        	}
        	if(joins != null && joins.size()  > 0)
        	{
	        	Iterator joinIterator = joins.iterator();
	        	while(joinIterator.hasNext())
	        	{
	        		boolean addParanthesis = false;
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
	        		int operation = joinCondition.getOperationType();
	        		
	        		
	        		
	        		if( ! fromTables.contains(leftTable) && 
	    	        	! fromTables.contains(rightTable)	)
	    	        {
		        		addParanthesis = true;
	    	        }
	        		
	        		if( addParanthesis )
	        		{
	        			fromClause.append("(");
	        		}
	        		//else
	        		//{
	        		//	fromClause.append(",");
	        		//}

	        		
	        		//if(fromClause.length() > 0)
	    			//{
	        		//	if( ! fromTables.contains(leftTable) && 
	        		//			! fromTables.contains(rightTable)	)
	        		//	{
	        		//		fromClause.append(" , ");
	        		//	}
	    			//}
	        		
	        		if(!fromTables.contains(leftTable))
	    			{
	    				fromTables.add(leftTable);
	    				fromClause.append(leftTable);
	    			}
	    			

	        		
	        		//fromClause.append(leftTable);
	        		if((joinType == -1) || (joinType == JoinType.INNER))
	        		{
	        			// Inner join, do no tneed any keyword
	        			// just add the tables
	       				fromClause.append(" INNER JOIN ");
	        		}
	        		else if( joinType == JoinType.LEFT_OUTER )
	        		{
	        			 fromClause.append(" LEFT OUTER JOIN ");
	        		}
	        		else if( joinType == JoinType.RIGHT_OUTER )
	        		{
	        			fromClause.append(" RIGHT OUTER JOIN ");
	        		}
	        		else if ( joinType == JoinType.FULL_OUTER)
	        		{
	        			fromClause.append(" FULL OUTER JOIN ");
	        		}
	        		
	    			if(!fromTables.contains(rightTable))
	    			{
	    				fromTables.add(rightTable);
	    				fromClause.append(rightTable);
	    			}

					
					
					// ON condition
					fromClause.append(" ON ");
					
		        	// Appending the Operator
		        	String leftColumnFullName = leftTable + "." + leftColumn;
		        	String rightColumnFullName = rightTable + "." + rightColumn;
		        	
		        	fromClause.append(leftColumnFullName);
		        	fromClause.append(" ");
		        	fromClause.append(joinCondition.getOperatorString());
		        	fromClause.append(" ");
		        	fromClause.append(rightColumnFullName);
		        	fromClause.append(" ");
		        	
		        	
		        	if( addParanthesis )
		        	{
		        		fromClause.append(")");
		        	}
		        	
		        	fromClause.append("\n");
		        		
	        	}
	        	
	         	// After Having added all the join conditions 
	        	// Add the other tables
	        	Iterator it1 = tableList.iterator();
	        	Iterator it2 = fromTables.iterator();
	        	
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
	        		fromClause.append(tableName);
	        		if(alias != null && alias.length() >0 && !tableName.equalsIgnoreCase(alias))
	        		{
	        			fromClause.append(" AS ");
						fromClause.append(alias);
	        		}
	        		fromClause.append("\n");
	        	}
	       
        	}
        	
        	String fromClauseStr = "";
        	if( fromClause.length() > 0)
        	{
        		fromClauseStr = " FROM " + fromClause.toString();
        	}
        	
        	
        	return fromClauseStr ;
        }

	/**
	 * @return Returns the jdbcConnection.
	 */
	public Connection getJdbcConnection() {
		return jdbcConnection;
	}
	/**
	 * @param jdbcConnection The jdbcConnection to set.
	 */
	public void setJdbcConnection(Connection jdbcConnection) {
		this.jdbcConnection = jdbcConnection;
	}
	
	public void closeConnection()
	{
		if ( jdbcConnection != null )
		{
			try {
				jdbcConnection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
