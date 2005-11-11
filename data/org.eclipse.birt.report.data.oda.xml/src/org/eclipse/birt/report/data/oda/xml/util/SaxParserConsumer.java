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

import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.birt.report.data.oda.xml.Constants;
import org.eclipse.birt.report.data.oda.xml.ResultSet;
import org.eclipse.datatools.connectivity.oda.OdaException;

/**
 * This class is an implementation of ISaxParserConsumer. The instance of this class deligate the communication
 * between ResultSet and SaxParser, and does the majority of result-set population job.
 */
public class SaxParserConsumer implements ISaxParserConsumer
{
	//The ResultSet this instance served for.
	private ResultSet resultSet;
	
	//The SaxParser this instance dealing with.
	private SaxParser sp;
	
	//The thread which hosts the sp.
	private Thread spThread;
	
	//The row number in cachedResultSet.
	private int cachedResultSetRowNo;
	
	//The overall rowNumber that is available currently
	private int currentAvailableMaxLineNo;
	
	//Store the value of most recent nested xml columns.This variable is only used when the 
	//table is using nested xml.
	private HashMap cachedColumns;
	
	//The root path of a table.
	private String rootPath;
	
	//The ancestor path of a talbe.
	private String ancestorPath;
	
	//indicate whether the table is nested xml table. 
	private boolean isNestedXMLTable;
	
	//The names of nested xml columns
	private String[] namesOfCachedColumns;
	
	//The name of a table.
	private String tableName;
	
	private RelationInformation relationInfo;
	
	//The counter which records the times of cachedResultSet being re-initialized.
	private int cachedTimes;
	
	//The array which cache the result set.
	private String[][] cachedResultSet;
	
	//The overall rowNumber that has been parsed
	private int currentRowNo;
	
	/**
	 * 
	 * @param rs
	 * @param rinfo
	 * @param fileName
	 * @param tName
	 * @throws OdaException
	 */
	public SaxParserConsumer( ResultSet rs, RelationInformation rinfo, String fileName, String tName) throws OdaException
	{
		this.resultSet = rs;
		
		//must start from 0
		cachedResultSetRowNo = 0;
		
		//must start from -1
		currentAvailableMaxLineNo = -1;
		tableName = tName;
		relationInfo = rinfo;
		
		//must start from 0
		currentRowNo = 0;
		
		cachedResultSet = new String[Constants.CACHED_RESULT_SET_LENGTH][resultSet.getMetaData().getColumnCount( )];
		this.rootPath = relationInfo.getTableRootPath( tableName );
		this.ancestorPath = relationInfo.getTableAncestor( tableName );
		this.isNestedXMLTable = rootPath.equals( ancestorPath );
		
		this.namesOfCachedColumns = relationInfo.getTableNestedXMLColumnNames( tableName );
		this.cachedColumns = new HashMap( );
		sp = new SaxParser( fileName , this );
		spThread = new Thread( sp );
		spThread.start();
	
	}
	
	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.birt.report.data.oda.xml.util.ISaxParserConsumer#manipulateData(java.lang.String, java.lang.String)
	 */
	public void manipulateData( String path, String value )
	{
		//get the table column names. 
		String[] filter = relationInfo.getTableColumnNames( tableName );
		
		for ( int i = 0; i < filter.length; i++ )
		{
			//If the given path is same to the path of certain column
			if ( isSamePath( relationInfo.getTableColumnPath( tableName, filter[i] ),
					path ) )
			{
				//If the column in certain row has never been assigned the value,
				//then populate the column value.
				if ( cachedResultSet[cachedResultSetRowNo][i] == null )
				{
					//populate the column value.
					cachedResultSet[cachedResultSetRowNo][i] = value;
					
					//populate the cachedColumns. this is only for nested xml.
					if ( cachedColumns.get( new Integer( i ) ) == null
							&& value != null )
					{
						for ( int k = 0; k < namesOfCachedColumns.length; k++ )
						{
							if ( namesOfCachedColumns[k].equals( filter[i] ) )
							{
								cachedColumns.put( new Integer( i ), value );
							}
						}
					}
				}

			}
		}
	}

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.birt.report.data.oda.xml.util.ISaxParserConsumer#detectNewRow(java.lang.String)
	 */
	public void detectNewRow( String path )
	{
		clearCachedColumns( path );
		
		//if the new row started.
		if ( isSamePath( rootPath, path ) )
		{
			populateNestedXMLDataMappingColumns( );
			if ( !isCurrentRowValid( ) )
				return;
			cachedResultSetRowNo++;
			currentAvailableMaxLineNo++;
			if ( cachedResultSetRowNo > Constants.CACHED_RESULT_SET_LENGTH - 1 )
			{
				sp.setStart( false );
				cachedResultSetRowNo = 0;
			}
		}
	}

	/**
	 * Clear all cached columns.
	 * @param path
	 */
	private void clearCachedColumns( String path )
	{
		if ( ( !isNestedXMLTable ) && isSamePath( ancestorPath, path ) )
		{
			cachedColumns.clear( );
		}
	}

	/**
	 * 
	 * @param path
	 *            the path which is stored as column path
	 * @param generatedPath
	 *            the path which is generated by sax parser
	 * @return
	 */
	private boolean isSamePath( String path, String generatedPath )
	{
		// If two path equal
		if ( path.equals( generatedPath ) )
			return true;

		// Test if column path is absolute path. A generatedPath is always
		// absolute.
		boolean isAbsolute = true;
		if ( path.startsWith( "//" ) )
		{
			path = path.replaceFirst( "//", "/" );
			isAbsolute = false;
		}

		String[] paths = path.replaceFirst( "/", "" ).split( "/" );
		String[] generatedPaths = generatedPath.replaceFirst( "/", "" )
				.split( "/" );
		// The generatePaths always contain no less path elements than path.
		if ( paths.length > generatedPaths.length )
			return false;

		// If is absolute path, then two should contain equal numbers of path
		// element
		if ( isAbsolute )
		{
			if ( paths.length != generatedPaths.length )
				return false;

		}

		for ( int i = 0; i < paths.length; i++ )
		{
			String temp1;
			String temp2;
			temp1 = paths[paths.length - i - 1];

			temp2 = generatedPaths[generatedPaths.length - i - 1];
			if ( !isXPathFragEqual( temp1, temp2 ) )
				return false;
		}
		return true;
	}

	/**
	 * Return whether two XPath Fragment refer to same element/attribute.
	 * @param definedPath
	 *            the defined column path
	 * @param generatedPath
	 *            the sax parser generated path
	 * @return
	 */
	private boolean isXPathFragEqual( String definedPath, String generatedPath )
	{
		if ( definedPath.startsWith( "*" ) )
		{
			if ( generatedPath.endsWith( definedPath.replaceFirst( "\\Q*\\E",
					"" ) ) )
				return true;
			return false;
		}

		if ( ( !generatedPath.matches( ".*\\Q[\\E\\d+\\Q]\\E.*" ) )
				|| definedPath.matches( ".*\\Q[\\E\\d+\\Q]\\E.*" ) )
			return generatedPath.equals( definedPath );
		return generatedPath.replaceFirst( "\\Q[\\E\\d+\\Q]\\E", "" )
				.equals( definedPath );
	}

	/**
	 * Populate all the columns that come from Nested XML data in certain row.
	 * 
	 */
	private void populateNestedXMLDataMappingColumns( )
	{
		if ( !isNestedXMLTable )
		{
			Iterator it = cachedColumns.keySet( ).iterator( );
			Integer i = null;
			while ( it.hasNext( ) )
			{
				i = (Integer) it.next( );
				cachedResultSet[cachedResultSetRowNo][i.intValue( )] = cachedColumns.get( i )
						.toString( );
			}
		}
	}

	/**
	 * Apply the filter to current row. Return whether should current row be filtered out.
	 * 
	 */
	private boolean isCurrentRowValid( )
	{
		for ( int i = 0; i < cachedResultSet[cachedResultSetRowNo].length; i++ )
		{
			if ( relationInfo.getTableFilter( tableName )
					.containsKey( relationInfo.getTableColumnNames( tableName )[i] ) )
			{
				if ( isCurrentColumnValueNotMatchFilterValue( i ) )

				{
					for ( int j = 1; j <= cachedResultSet[cachedResultSetRowNo].length; j++ )
					{
						cachedResultSet[cachedResultSetRowNo][getColumnPosition( j )] = null;
					}
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * @param i
	 *            Column Index
	 * @return
	 */
	private boolean isCurrentColumnValueNotMatchFilterValue( int i )
	{
		return !( relationInfo.getTableFilter( tableName )
				.get( relationInfo.getTableColumnNames( tableName )[i] ) == cachedResultSet[cachedResultSetRowNo][i] || relationInfo.getTableFilter( tableName )
				.get( relationInfo.getTableColumnNames( tableName )[i] )
				.equals( cachedResultSet[cachedResultSetRowNo][i] ) );
	}

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.birt.report.data.oda.xml.util.ISaxParserConsumer#wakeup()
	 */
	public synchronized void wakeup( )
	{
		notify();
	}

	/**
	 * Transform 1-based column index to 0-based column position in the array.
	 * 
	 * @param index
	 * @return
	 */
	private int getColumnPosition( int index )
	{
		return index - 1;
	}

	/**
	 * Make the cursor forward. If the end of data reached then return false.
	 * 
	 * @return
	 * @throws OdaException
	 */
	public boolean next( ) throws OdaException
	{
		//If the sax parser is still alive and has not been suspended yet, then
		//block the current thread. The current thread will be re-active by sax
		//parser.
		while ( sp.isAlive( ) && !sp.isSuspended( ) )
		{
			try
			{
				synchronized ( this )
				{
					wait( );
				}
			}
			catch ( InterruptedException e )
			{
				// TODO Auto-generated catch block
				e.printStackTrace( );
			}
		}

		//If the cursor will move to the row that is not currently available,
		//then resume the sp thread so that it can proceed to fetch more data to 
		//result set.
		if ( currentRowNo > currentAvailableMaxLineNo )
		{
			if ( sp.isAlive( ) )
			{
				this.resumeThread( );
				return next( );
			}
			else
				return false;
		}

		currentRowNo++;

		return true;
	}
	
	/**
	 * Resume the thread, if SaxParser is suspended then restart it.
	 * 
	 * @throws OdaException
	 * 
	 */
	private void resumeThread( ) throws OdaException
	{
		if ( sp.isSuspended( ) )
		{
			cachedTimes++;
			// Recache the result set.
			cachedResultSetRowNo = 0;
			cachedResultSet = new String[Constants.CACHED_RESULT_SET_LENGTH][resultSet.getMetaData().getColumnCount( )];
			sp.setStart( true );
		}
	}

	/**
	 * Close the SaxParserConsumer.
	 *
	 */
	public void close( )
	{
		if( this.spThread != null )
			this.spThread.stop();
		this.cachedColumns = null;
		this.namesOfCachedColumns = null;
		
	}

	/**
	 * Return the array that cached the result set data.
	 * 
	 * @return
	 */
	public String[][] getResultSet( )
	{
		return this.cachedResultSet;
	}
	
	/**
	 * Return Current row position. The row position is the position of a row
	 * in the result set arrary rather than overall row number.
	 *  
	 * @return
	 */
	public int getRowPosition( )
	{
		return currentRowNo
		- this.cachedTimes * Constants.CACHED_RESULT_SET_LENGTH - 1;
	}

	/**
	 * Return overall row number.
	 *  
	 * @return
	 */
	public int getCurrentRowNo( )
	{
		return this.currentRowNo;
	}
}
