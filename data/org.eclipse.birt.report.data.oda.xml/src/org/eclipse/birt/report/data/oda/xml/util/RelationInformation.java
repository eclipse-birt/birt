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

package org.eclipse.birt.report.data.oda.xml.util;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.birt.report.data.oda.xml.DataTypes;
import org.eclipse.birt.report.data.oda.xml.i18n.Messages;
import org.eclipse.datatools.connectivity.oda.OdaException;

/**
 * This class is used to dealing with the strings which are parsed as arguments to 
 * create an XML data source connection.The structure of string must follow the given rule:
 * TableName1:[TableRootPath]:{columnName1;Type;RelativeXPath},{columnName2;Type;RelativeXPath}...
 * #-#TableName2:[TableRootPath]:{columnName1;Type;RelativeXpath}.....
 * 
 */
public class RelationInformation
{
	//
	public static final String CONST_TABLE_DELIMITER = "#-#";
	public static final String CONST_TABLE_COLUMN_DELIMITER = "#:#";
	public static final String CONST_COLUMN_METAINFO_DELIMITER = ";";
	public static final String CONST_COLUMN_DELIMITER = ",";
	
	//
	private HashMap tableInfos;

	/**
	 * 
	 * @param relationString
	 * @throws OdaException
	 */
	public RelationInformation( String relationString ) throws OdaException
	{
		this.tableInfos = new HashMap( );
		initialize( relationString.trim( ) );
	}

	/**
	 * Initialize tableInfos by analyzing the input string.
	 * @param relationString
	 * @throws OdaException 
	 */
	private void initialize( String relationString ) throws OdaException
	{
		if( relationString == null|| relationString.length() == 0)
			throw new OdaException( Messages.getString("RelationInformation.InputStringCannotBeNull"));
		
		String[] tables = relationString.split( CONST_TABLE_DELIMITER );
		for ( int i = 0; i < tables.length; i++ )
		{

			String[] temp = tables[i].trim( )
					.split( CONST_TABLE_COLUMN_DELIMITER );
			assert ( temp.length == 3 );
			// //////////////////////////////
			TableInfo tableInfo = new TableInfo( temp[0].trim( ),
					temp[1].substring( 1, temp[1].length( ) - 1 ).trim( ) );
			// ////////////////////////////////
			String[] columns = temp[2].trim( ).split( CONST_COLUMN_DELIMITER );
			
			for ( int j = 0; j < columns.length; j++ )
			{
				String trimedColumn = columns[j].trim( );
				// remove column info delimiter "{" and "}"
			
				String[] columnInfos = trimedColumn.substring( 1,
						trimedColumn.length( ) - 1 )
						.split( CONST_COLUMN_METAINFO_DELIMITER );
				
				//columnInfos[0]: column name
				//columnInfos[1]: column type
				//columnInfos[2]: column XPath
				String columnXpath = null;
				if( columnInfos.length == 3 )
				{
					columnXpath = columnInfos[2];
				}else
				{
					columnXpath = "";
				}
				for ( int m = 0; m < columnInfos.length; m++ )
					columnInfos[m] = columnInfos[m].trim( );
				String originalColumnXpath = columnXpath;
				//if it is a filter expression
				if ( columnXpath.matches( ".*\\Q[@\\E.*\\Q=\\E.*" ) )
				{
					//get the filter value
					String value = columnXpath.replaceAll( ".*\\Q[@\\E.*\\Q=\\E",
							"" )
							.trim( );
					value = value.substring( 1, value.length( ) - 2 );
				
					//add it to filter
					tableInfo.addFilter( columnInfos[0], value );
					
					columnXpath = columnXpath.replaceAll( "\\Q=\\E.*",
							"]" );
				}
				tableInfo.addColumn( new ColumnInfo( j + 1,
						columnInfos[0],
						columnInfos[1],
						combineColumnPath(tableInfo.getRootPath( ),columnXpath), originalColumnXpath));
			}
			this.tableInfos.put( temp[0].trim( ), tableInfo );
		}
	}

	/**
	 * Combine column root path and the relative column path.
	 * 
	 * @param rootPath
	 * @param declaredPath
	 * @return
	 */
	private String combineColumnPath( String rootPath, String declaredPath)
	{
		if (declaredPath == null || declaredPath.length() == 0)
			return rootPath;
		else if( declaredPath.startsWith("[")||declaredPath.startsWith("/"))
			return rootPath+declaredPath;
		return rootPath + "/" + declaredPath;
		
	}
	
	/**
	 * Return the path of a column in certain table.
	 * 
	 * @param tableName
	 * @param columnName
	 * @return
	 */
	public String getTableColumnPath( String tableName, String columnName )
	{
		return ( (TableInfo) this.tableInfos.get( tableName ) ).getPath( columnName );
	}

	/**
	 * Return the path of a column in certain table.
	 * 
	 * @param tableName
	 * @param columnName
	 * @return
	 */
	public String getTableOriginalColumnPath( String tableName, String columnName )
	{
		return ( (TableInfo) this.tableInfos.get( tableName ) ).getOriginalPath( columnName );
	}
	
	/**
	 * Return the path of a column in certain table.
	 * 
	 * @param tableName
	 * @param columnName
	 * @return
	 */
	public int getTableNestedColumnBackRefNumber( String tableName, String columnName )
	{
		return ( (TableInfo) this.tableInfos.get( tableName ) ).getBackRefNumber( columnName );
	}
	
	/**
	 * Return the type of a column in certain table.
	 * 
	 * @param tableName
	 * @param columnName
	 * @return
	 */
	public String getTableColumnType( String tableName, String columnName )
	{
		return ( (TableInfo) this.tableInfos.get( tableName ) ).getType( columnName );
	}

	/**
	 * Return the array of column names of certain table.
	 *  
	 * @param tableName
	 * @return
	 */
	public String[] getTableColumnNames( String tableName )
	{
		return ( (TableInfo) this.tableInfos.get( tableName ) ).getColumnNames( );
	}

	/**
	 * Return the array of nested column names of certain table.
	 *  
	 * @param tableName
	 * @return
	 */
	public String[] getTableNestedXMLColumnNames( String tableName )
	{
		return  ( (TableInfo) this.tableInfos.get( tableName ) ).getNestXMLColumnNames();
	}
	
	/**
	 * Return the table root path.
	 * 
	 * @param tableName
	 * @return
	 */
	public String getTableRootPath( String tableName )
	{
		return ( (TableInfo) this.tableInfos.get( tableName ) ).getRootPath( );
	}

	/**
	 * Return the table filter.
	 * 
	 * @param tableName
	 * @return
	 */
	public HashMap getTableFilter( String tableName )
	{
		return ( (TableInfo) this.tableInfos.get( tableName ) ).getFilter( );
	}

	/**
	 * Return the ancestor path of certain table.
	 * 
	 * @param tableName
	 * @return
	 */
	public String getTableAncestor( String tableName )
	{
		return ( (TableInfo) this.tableInfos.get( tableName ) ).getAncestor( );
	}
}

/**
 * The instance of this class describe a table.
 *
 */
class TableInfo
{
	//The name of the table.
	private String tableName;
	
	//The root path of the table.
	private String rootPath;
	
	//The hashmap which host columnInfos
	private HashMap columnInfos;
	
	//The hashmap which host filterInfos
	private HashMap filterInfos;

	public TableInfo( String tableName, String rootPath )
	{
		this.tableName = tableName;
		this.rootPath = rootPath;
		this.columnInfos = new HashMap( );
		this.filterInfos = new HashMap( );
	}

	/**
	 * Return the name of the table.
	 * 
	 * @return
	 */
	public String getTableName( )
	{
		return this.tableName;
	}

	/**
	 * Return the path of certain column.
	 * 
	 * @param columnName
	 * @return
	 */
	public String getPath( String columnName )
	{
		return ( (ColumnInfo) this.columnInfos.get( columnName ) ).getColumnPath( );
	}
	
	/**
	 * Return the original path of certain column.
	 * 
	 * @param columnName
	 * @return
	 */
	public String getOriginalPath( String columnName )
	{
		return ( (ColumnInfo) this.columnInfos.get( columnName ) ).getColumnOriginalPath();
	}

	/**
	 * Return the back reference number of the column, this only applys to nested xml columns.
	 * 
	 * @param columnName
	 * @return
	 */
	public int getBackRefNumber( String columnName )
	{
		return ( (ColumnInfo) this.columnInfos.get( columnName ) ).getBackRefNumber();
	}
	/**
	 * Return the defined data type of certain column.
	 * 
	 * @param columnName
	 * @return
	 */
	public String getType( String columnName )
	{
		return ( (ColumnInfo) this.columnInfos.get( columnName ) ).getColumnType( );
	}

	/**
	 * Return the hash map which defines the filters.
	 * 
	 * @return
	 */
	public HashMap getFilter( )
	{
		return this.filterInfos;
	}

	/**
	 * Add a column to a table.
	 * 
	 * @param ci
	 */
	public void addColumn( ColumnInfo ci )
	{
		this.columnInfos.put( ci.getColumnName( ), ci );
	}

	/**
	 * Add a filter to a table.
	 * 
	 * @param columnName
	 * @param value
	 */
	public void addFilter( String columnName, String value )
	{
		this.filterInfos.put( columnName, value );
	}

	/**
	 * Return the column name array.
	 * 
	 * @return
	 */
	public String[] getColumnNames( )
	{
		Object[] names = this.columnInfos.keySet( ).toArray( );
		String[] result = new String[names.length];
		for ( int i = 0; i < names.length; i++ )
		{
			result[( (ColumnInfo) columnInfos.get( names[i] ) ).getColumnIndex( ) - 1] = names[i].toString( );
		}
		return result;
	}

	/**
	 * The nested xml columnNames are the names of columns the value of which may be shared by multiple 
	 * columns. The most significant feature of a nested xml column is that its xpath expression start will 
	 * table ancestor path rather than table root path.  
	 * 
	 * @return
	 */
	public String[] getNestXMLColumnNames( )
	{
		ArrayList temp = new ArrayList();
		String[] columnNames = getColumnNames();
		for(int i = 0; i < columnNames.length; i++)
		{
			if(!((ColumnInfo)columnInfos.get(columnNames[i])).getColumnPath().startsWith(rootPath))
			{
				temp.add( columnNames[i]);
			}
		}
		String[] result = new String[temp.size()];
		for(int i = 0; i < result.length; i ++)
		{
			result[i] = temp.get(i).toString();
		}
		return result;
	}
	
	/**
	 * Return the root path of that table.
	 * 
	 * @return
	 */
	public String getRootPath( )
	{
		return this.rootPath;
	}

	/**
	 * Return the table's ancestor path. A table's ancestor path is the common prefix that all table columns'
	 * pathes shared. Table's rootpath should only equal to, or prefixed with a table's ancestor path.
	 * @return
	 */
	public String getAncestor( )
	{
		String[] columnNames = this.getColumnNames( );
		
		// If the table only contains one column. Then the root path of that
		// table is the path of
		// the column without the tailing attribute path(if exists)
		if ( columnNames.length == 1 )
			return getPath( columnNames[0] );// .replaceFirst("@.*","");
		String[] paths = new String[columnNames.length];
		for ( int i = 0; i < paths.length; i++ )
		{
			paths[i] = getPath( columnNames[i] ).replaceFirst("\\Q\\[@\\E.*\\Q\\]\\E","");
		}

		String theLongestPath = "";
		for ( int i = 0; i < paths.length; i++ )
		{
			if ( paths[i].split( "\\Q/\\E" ).length > theLongestPath.split( "\\Q/\\E" ).length )
				theLongestPath = paths[i];
		}
		boolean isAbsolutePath = false;

		if ( theLongestPath.startsWith( "//" ) )
		{
			isAbsolutePath = false;
			theLongestPath = theLongestPath.replaceFirst( "\\Q//\\E", "" );
		}
		else
		{
			isAbsolutePath = true;
			theLongestPath = theLongestPath.replaceFirst( "\\Q/\\E", "" );
		}

		String[] temp = theLongestPath.split( "\\Q/\\E" );
		String prefix = isAbsolutePath ? "/" : "//";
		for ( int j = 0; j < temp.length; j++ )
		{
			String attempedPrefix = j == 0 ? prefix + temp[j] : prefix + "/" + temp[j];
			for ( int i = 0; i < paths.length; i++ )
			{
				if ( !paths[i].startsWith( attempedPrefix ) )
					return prefix;

			}
			prefix = attempedPrefix;
		}
		
		return prefix;
	}
}

/**
 * The instance of this class describe a single column.
 *
 */
class ColumnInfo
{

	private int index;
	private String name;
	private String type;
	private String path;
	private String originalPath;
	private int backRefNumber;
	
	/**
	 * 
	 * @param index
	 * @param name
	 * @param type
	 * @param path
	 * @param originalPath
	 * @throws OdaException
	 */
	public ColumnInfo( int index, String name, String type, String path,
			String originalPath ) throws OdaException
	{
		this.index = index;
		this.name = name;
		this.type = type;
		if ( !DataTypes.isValidType( type ) )
			throw new OdaException( Messages.getString( "RelationInformation.InvalidDataTypeName" ) );
		this.path = fixTrailingAttr( buildPath( path ) );
		this.originalPath = originalPath;
		if ( originalPath.matches( ".*\\Q..\\E.*" ) )
		{
			String[] originalPathFrags = originalPath.split( "/" );
			int lastTwoDotAbbrevationPosition = 0;
			int numberOfConcretePathFragsBefore2DotAbb = 0;

			for ( int i = 0; i < originalPathFrags.length; i++ )
			{
				if ( originalPathFrags[i].equals( ".." ) )
					lastTwoDotAbbrevationPosition = i;
			}
			for ( int i = 0; i < lastTwoDotAbbrevationPosition; i++ )
			{
				if ( !originalPathFrags[i].equals( ".." ) )
					numberOfConcretePathFragsBefore2DotAbb++;
			}

			int numberOf2DotAbb = lastTwoDotAbbrevationPosition
					- numberOfConcretePathFragsBefore2DotAbb + 1;
			backRefNumber = numberOf2DotAbb
					- numberOfConcretePathFragsBefore2DotAbb;
			if ( backRefNumber < 0 )
				backRefNumber = 0;
		}
		else
		{
			backRefNumber = 0;
		}
	}
	
	/**
	 * If the path is refer to an attribute, use syntax /elementName/@attributeName
	 * then we change it to /elementName[@attributeName] to compliment the standard xpath syntax.
	 * 
	 */ 
	private String fixTrailingAttr( String path )
	{
		if ( path.matches(".*/@.*"))
			return path.replaceFirst("/@","[@")+"]";
		else
			return path;
	}
	/**
	 * Dealing with ".." in a column path. Here the column path is the combination of root path
	 * and the give column path expression.
	 * 
	 * @param path
	 * @return
	 */
	private String buildPath( String path )
	{
		String prefix = "";
		
		//First remove the leading "//" or "/"
		if ( path.startsWith( "//" ) )
		{
			path = path.replaceFirst( "//", "" );
			prefix = "//";
		}
		else if ( path.startsWith( "/" ) )
		{
			path = path.replaceFirst( "/", "" );
			prefix = "/";
		}
		String[] temp = path.split( "/" );
		for ( int i = 0; i < temp.length; i++ )
		{
			if ( temp[i].equals( ".." ) )
			{
				temp[i] = null;
				for ( int j = i - 1; j >= 0; j-- )
				{
					if ( temp[j] != null )
					{
						temp[j] = null;
						break;
					}
				}
			}
		}
		
		//Rebuild the path.
		path = prefix;
		for ( int i = 0; i < temp.length; i++ )
		{
			if ( temp[i] != null )
				path = i == 0 ? path + temp[i] : path + (temp[i].startsWith("[")?"":"/") + temp[i];
		}
		return path;
	}

	/**
	 * Return the columnName.
	 * 
	 * @return
	 */
	public String getColumnName( )
	{
		return this.name;
	}

	/**
	 * Return the columnType.
	 * 
	 * @return
	 */
	public String getColumnType( )
	{
		return this.type;
	}

	/**
	 * Return the column xPath.
	 * 
	 * @return
	 */
	public String getColumnPath( )
	{
		return this.path;
	}

	/**
	 * Return the colum index.
	 * 
	 * @return
	 */
	public int getColumnIndex( )
	{
		return this.index;
	}
	
	/**
	 * Return the original path of the column. The original path of a column is the path
	 * directly get from relation information String without building it to an absolute path.
	 * This method is mainly used by UI.
	 * 
	 * @return
	 */
	public String getColumnOriginalPath()
	{
		return this.originalPath;
	}
	
	/**
	 * Return the colum index.
	 * 
	 * @return
	 */
	public int getBackRefNumber( )
	{
		return this.backRefNumber;
	}
}