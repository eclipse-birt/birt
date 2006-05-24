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

import org.eclipse.birt.report.data.oda.xml.Constants;
import org.eclipse.birt.report.data.oda.xml.impl.ResultSet;
import org.eclipse.datatools.connectivity.oda.OdaException;

/**
 * This class is an implementation of ISaxParserConsumer. The instance of this class deligate the communication
 * between ResultSet and SaxParser, and does the majority of result-set population job.
 */
public class SaxParserConsumer implements ISaxParserConsumer
{
	private static final int INVALID_COLUMN_INDEX = -1;
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
	
	//The root path of a table.
	private String rootPath;
	
	//The names of complex nested xml columns
	private String[] namesOfCachedComplexNestedColumns;
	
	//The names of simple xml columns
	private String[] namesOfCachedSimpleNestedColumns;
	
	private String[] namesOfColumns;
	//The name of a table.
	private String tableName;
	
	private RelationInformation relationInfo;
	
	//The counter which records the times of cachedResultSet being re-initialized.
	private int cachedTimes;
	
	//The array which cache the result set.
	private String[][] cachedResultSet;
	
	//The overall rowNumber that has been parsed
	private int currentRowNo;
	
	private SaxParserComplexNestedQueryHelper spNestedQueryHelper;
	
	private NestedColumnUtil nestedColumnUtil;
	
	/**
	 * 
	 * @param rs
	 * @param rinfo
	 * @param fileName
	 * @param tName
	 * @throws OdaException
	 */
	public SaxParserConsumer( ResultSet rs, RelationInformation rinfo, XMLDataInputStream is, String tName) throws OdaException
	{
		this.resultSet = rs;
		                                 
		//must start from 0
		cachedResultSetRowNo = 0;
		
		//must start from -1
		currentAvailableMaxLineNo = -1;
		tableName = tName;
		relationInfo = rinfo;
		nestedColumnUtil = new NestedColumnUtil( relationInfo, tableName, true);
		
		//must start from 0
		currentRowNo = 0;

		cachedResultSet = new String[Constants.CACHED_RESULT_SET_LENGTH][resultSet.getMetaData().getColumnCount( )];
		this.rootPath = relationInfo.getTableRootPath( tableName );
		
		this.namesOfCachedComplexNestedColumns = relationInfo.getTableComplexNestedXMLColumnNames( tableName );
		this.namesOfCachedSimpleNestedColumns = relationInfo.getTableSimpleNestedXMLColumnNames( tableName );
		
		this.namesOfColumns = relationInfo.getTableColumnNames( tableName );
		
		XMLDataInputStream xdis = is;
		
		if( namesOfCachedComplexNestedColumns.length > 0)
		{
			spNestedQueryHelper = new SaxParserComplexNestedQueryHelper(rinfo, xdis, tName);
			while ( !spNestedQueryHelper.isPrepared( ) )
			{
				try
				{
					synchronized ( this )
					{
						wait( 1000 );
					}
				}
				catch ( InterruptedException e )
				{
					throw new OdaException( e.getLocalizedMessage() );
				}
			}
		}
		sp = new SaxParser( xdis , this );
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
				
		for ( int i = 0; i < namesOfColumns.length; i++ )
		{
			//If the given path is same to the path of certain column
			if ( SaxParserUtil.isSamePath( relationInfo.getTableColumnPath( tableName, namesOfColumns[i] ),
					path ) )
			{
				if ( isSimpleNestedColumn( namesOfColumns[i] ))
				{
					this.nestedColumnUtil.update(namesOfColumns[i], path, value);
					continue;
				}
				//If the column in certain row has never been assigned the value,
				//then populate the column value.
				if ( cachedResultSet[cachedResultSetRowNo][i] == null )
				{
					//populate the column value.
					cachedResultSet[cachedResultSetRowNo][i] = value;
				}
			}
		}
	}

	private boolean isSimpleNestedColumn( String columnName )
	{
		for( int i = 0; i < this.namesOfCachedSimpleNestedColumns.length; i++ )
		{
			if( this.namesOfCachedSimpleNestedColumns[i].equals(columnName))
				return true;
		}
		return false;
	}
	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.birt.report.data.oda.xml.util.ISaxParserConsumer#detectNewRow(java.lang.String)
	 */
	public void detectNewRow( String path )
	{
		//if the new row started.
		if ( SaxParserUtil.isSamePath( rootPath, path ) )
		{
			populateNestedXMLDataMappingColumns( path );
			
			
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
	 * Populate all the columns that come from Nested XML data in certain row.
	 * 
	 */
	private void populateNestedXMLDataMappingColumns( String currentRootPath )
	{
		for ( int i = 0; i < namesOfCachedComplexNestedColumns.length; i++)
		{
			int j = getColumnIndex( namesOfCachedComplexNestedColumns[i]);
			if ( j!= INVALID_COLUMN_INDEX)
				cachedResultSet[cachedResultSetRowNo][j] = this.spNestedQueryHelper.getNestedColumnUtil().getNestedColumnValue(namesOfCachedComplexNestedColumns[i],currentRootPath);
		}

		for ( int i = 0; i < namesOfCachedSimpleNestedColumns.length; i++)
		{
			int j = getColumnIndex( namesOfCachedSimpleNestedColumns[i]);
			if( j!= INVALID_COLUMN_INDEX )
				cachedResultSet[cachedResultSetRowNo][j] = this.nestedColumnUtil.getNestedColumnValue( namesOfCachedSimpleNestedColumns[i], currentRootPath );
		}
	}

	private int getColumnIndex( String columnName )
	{
		for ( int j = 0; j < namesOfColumns.length; j++ )
		{
			if( columnName.equals(namesOfColumns[j]))
			{
				return j;
			}
		}
	
		return INVALID_COLUMN_INDEX;
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
	 * @param i Column Index
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
				throw new OdaException( e.getLocalizedMessage() );
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
		//TODO add comments.
		if( this.sp != null )
			this.sp.stopParsing();
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
		return currentRowNo - this.cachedTimes * Constants.CACHED_RESULT_SET_LENGTH - 1;
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

