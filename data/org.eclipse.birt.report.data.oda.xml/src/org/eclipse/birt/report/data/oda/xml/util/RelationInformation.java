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
import java.util.Iterator;

import org.eclipse.birt.report.data.oda.xml.impl.DataTypes;
import org.eclipse.birt.report.data.oda.xml.i18n.Messages;
import org.eclipse.datatools.connectivity.oda.OdaException;

/**
 * This class is used to dealing with the strings which are parsed as arguments to 
 * create an XML data source connection.The structure of string must follow the given rule:
 * TableName1#:#[TableRootPath]#:#{columnName1;Type;RelativeXPath},{columnName2;Type;RelativeXPath}...
 * #-#TableName2#:#[TableRootPath]#:#{columnName1;Type;RelativeXpath}.....
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
						tableInfo.getRootPath( ), columnXpath, originalColumnXpath));
			}
			this.tableInfos.put( temp[0].trim( ), tableInfo );
		}
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
	 * Return the back ref number of a column in certain table.
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
	 * Return the array of complex nested column names of certain table.
	 *  
	 * @param tableName
	 * @return
	 */
	public String[] getTableComplexNestedXMLColumnNames( String tableName )
	{
		return  ( (TableInfo) this.tableInfos.get( tableName ) ).getComplexNestXMLColumnNames();
	}
	
	/**
	 * Return the array of simple nested column names of certain table.
	 *  
	 * @param tableName
	 * @return
	 */
	public String[] getTableSimpleNestedXMLColumnNames( String tableName )
	{
		return  ( (TableInfo) this.tableInfos.get( tableName ) ).getSimpleNestXMLColumnNames();
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
	 * Return the table original root path.
	 * 
	 * @param tableName
	 * @return
	 */
	public String getTableOriginalRootPath( String tableName )
	{
		return ( (TableInfo) this.tableInfos.get( tableName ) ).getOriginalRootPath( );
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
	
	public Iterator getTableNames()
	{
		return this.tableInfos.keySet( ).iterator( );
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
	
	//The hashmap which host columnInfos
	private HashMap columnInfos;
	
	//The hashmap which host filterInfos
	private HashMap filterInfos;

	//The original root path of this table
	private String originalRootPath;
	
	//The root path of this table
	private String rootPath;
	
	public TableInfo( String tableName, String rootPath )
	{
		this.tableName = tableName;
		this.originalRootPath = rootPath;
		String temp = SaxParserUtil.processParentAxis( originalRootPath );
		if( "//".equals( temp ))
			this.rootPath = "//*";
		else
			this.rootPath = temp;
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
	 * The complex nested xml columnNames are the names of columns the value of which may be shared by multiple 
	 * columns. The most significant feature of a complex nested xml column is that its xpath expression does not start with 
	 * table root path, nor is it an attribute of parent of root path element.  
	 * 
	 * @return
	 */
	public String[] getComplexNestXMLColumnNames( )
	{
		ArrayList temp = new ArrayList();
		String[] columnNames = getColumnNames();
		String[] simpleNestXMLColumnNames = getSimpleNestXMLColumnNames( );
		for(int i = 0; i < columnNames.length; i++)
		{
			//First filter out all "non-nested xml columns
			if(!((ColumnInfo)columnInfos.get(columnNames[i])).getColumnPath().startsWith(rootPath))
			{
				boolean isComplexNestXMLColumn = true;
				//Then filter out all simple nested xml columns
				for( int j = 0; j < simpleNestXMLColumnNames.length; j++ )
				{
					if( simpleNestXMLColumnNames[j].equals( columnNames[i] ))
					{
						isComplexNestXMLColumn = false;
						break;
					}
				}
				if( isComplexNestXMLColumn )
					temp.add( columnNames[i]);
			}
		}
		return getStringArrayFromList( temp );
	}

	/**
	 * @param temp
	 * @return
	 */
	private String[] getStringArrayFromList( ArrayList temp )
	{
		String[] result = new String[temp.size()];
		for(int i = 0; i < result.length; i ++)
		{
			result[i] = temp.get(i).toString();
		}
		return result;
	}
	
	/**
	 * The simple nested xml columnNames are the names of columns the value of which may be shared by multiple 
	 * columns. The most significant feature of a simple nested xml column is that its xpath expression does not start with 
	 * table root path, and it is an attribute of parent of root path element.  
	 * 
	 * @return
	 */
	public String[] getSimpleNestXMLColumnNames( )
	{
		ArrayList temp = new ArrayList();
		String[] columnNames = getColumnNames();
		for( int i = 0; i < columnNames.length; i++)
		{
			String nestedXMLColumnPathPrefix = ((ColumnInfo)columnInfos.get(columnNames[i])).getColumnPath();
			//All simple nested xml column must be xml attributes rather than xml elements.
			if( !nestedXMLColumnPathPrefix.matches( ".*\\Q@\\E.*" ))
				continue;
			//Remove the attribute so that only 
			nestedXMLColumnPathPrefix = nestedXMLColumnPathPrefix.replaceAll( "\\Q[@\\E.*", "" );
				
			if( isSimpleNestedColumn( rootPath, nestedXMLColumnPathPrefix))
				temp.add( columnNames[i] );
		}
		return getStringArrayFromList( temp );
	}
	
	/**
	 * Test if given treated column path refer to a Simple Nested Column
	 * 
	 * @param rootPath
	 * @param treatedColumPath
	 * @return
	 */
	private boolean isSimpleNestedColumn( String rootPath, String treatedColumPath )
	{
		String[] tempString1 = treatedColumPath.split( "/" );
		String[] tempString2 = rootPath.split( "/" );
		if( tempString2.length <= tempString1.length )
			return false;
		for( int j = 0; j < tempString1.length; j++ )
		{
			if ( !(( ( tempString1[j] == null  || tempString2[j] == null ) )
					|| ( tempString1[j].equals( tempString2[j] )
							|| "*".equals( tempString1[j] ) || "*".equals( tempString2[j] ) ) ))
				return false;
		}
		return true;
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
	 * Return the original root path of this table.
	 * 
	 * @return
	 */
	public String getOriginalRootPath( )
	{
		return this.originalRootPath;
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
	//
	private int index;
	private String name;
	private String type;
	private String path;
	private String originalPath;
	
	//The backRefNumber is the number of parent element should a nested xml column
	//retrieve back from its root XPath to find itself.This is only used in nest xml column
	//mapping.
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
	public ColumnInfo( int index, String name, String type, String rootPath, String relativePath,
			String originalPath ) throws OdaException
	{
		this.index = index;
		this.name = name;
		this.type = type;
		if ( !DataTypes.isValidType( type ) )
			throw new OdaException( Messages.getString( "RelationInformation.InvalidDataTypeName" ) );
		this.path = fixTrailingAttr( SaxParserUtil.processParentAxis( combineColumnPath( rootPath, relativePath ) ) );
		this.originalPath = originalPath;
		generateBackRefNumber( rootPath, originalPath );
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
	 * @param originalPath
	 */
	private void generateBackRefNumber( String rootPath, String originalPath )
	{
		if ( this.originalPath.matches( ".*\\Q..\\E.*" ) )
		{
			String[] originalPathFrags = originalPath.split( UtilConstants.XPATH_SLASH );
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
			{
				backRefNumber = 0;
				return;
			}
			
			//The back reference number cannot larger than the number of elements
			//of root path.
/*			String[] temp = rootPath.split( "\\Q/\\E" );
			int count = 0;
			for( int i = 0; i < temp.length; i ++)
			{
				if( temp[i].trim( ).length( )> 0 )
					count++;
			}
			backRefNumber = backRefNumber>count?count:backRefNumber;*/
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
		if ( path.matches(".*//@.*"))
			path = path.replaceFirst("//@","//[@")+"]";
		else if ( path.matches(".*/@.*"))
			path = path.replaceFirst("/@","[@")+"]";
		/*if ( path.startsWith( "//[" ))
			path = path.replaceFirst( "\\Q//[\\E", "//*[" );
		if ( path.startsWith( "/[" ))
			path = path.replaceFirst( "\\Q/[\\E", "//*[" );*/
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
	 * Return the back ref number.
	 * 
	 * @return
	 */
	public int getBackRefNumber( )
	{
		return this.backRefNumber;
	}
}