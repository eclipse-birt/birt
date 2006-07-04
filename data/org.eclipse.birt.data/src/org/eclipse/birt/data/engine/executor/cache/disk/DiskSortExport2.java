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

package org.eclipse.birt.data.engine.executor.cache.disk;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.cache.IRowResultSet;
import org.eclipse.birt.data.engine.executor.cache.ResultObjectUtil;
import org.eclipse.birt.data.engine.odi.IResultObject;

/**
 * One implemenation of DataBaseExport. This class will read data from data base
 * and export to file with sort operation done.
 */
class DiskSortExport2 extends DiskDataExport
{
	private int dataCountOfUnit;
	private int dataCountOfTotal;

	private MergeTempFileUtil tempFileUtil;

	private List tempRowFiles;
	
	// private SortDataProvider dataProvider;
	private MergeSortUtil mergeSortUtil;

	// buffer for merger sort. The lentgh of objectBuffer is dataCountOfUnit
	private IResultObject[] rowBuffer = null;
	
	// The positions from 0 to rowBufferPtr of buffer are free.
	private int inMemoryPos;

	// the goal file
	private IRowIterator goalRowIterator = null;

	/**
	 * @param dataProvider
	 */
	DiskSortExport2( Map infoMap, Comparator comparator,
			ResultObjectUtil resultObjectUtil )
	{
		dataCountOfUnit = Integer.parseInt( (String) infoMap.get( "dataCountOfUnit" ) );

		if ( dataCountOfUnit < 2 )
		{
			throw new IllegalArgumentException( "the dataCountOfUnit of "
					+ dataCountOfUnit + " is less than 2 "
					+ ", and then merge sort on file can not be done" );
		}

		rowBuffer = new IResultObject[dataCountOfUnit];

		tempFileUtil = new MergeTempFileUtil( (String) ( infoMap.get( "tempDir" ) ),
				resultObjectUtil );

		mergeSortUtil = MergeSortUtil.getUtil( comparator );
		
		this.tempRowFiles = new ArrayList( );
		this.inMemoryPos = -1;
	}

	/*
	 * @see org.eclipse.birt.data.engine.executor.resultset.DataBaseExport#exportStartDataToDisk(org.eclipse.birt.data.engine.executor.ResultObject[])
	 */
	public void exportStartDataToDisk( IResultObject[] resultObjects )
			throws IOException
	{
		dataCountOfTotal = resultObjects.length;
		System.arraycopy( resultObjects, 0, rowBuffer, 0, resultObjects.length );
		mergeSortUtil.sortSelf( rowBuffer );
		prepareNewTempRowFile( 0 );
	}

	/*
	 * @see org.eclipse.birt.data.engine.executor.cache.DataBaseExport#exportRestDataToDisk(org.eclipse.birt.data.engine.odi.IResultObject,
	 *      org.eclipse.birt.data.engine.executor.cache.RowResultSet)
	 */
	public int exportRestDataToDisk( IResultObject resultObject,
			IRowResultSet rs ) throws DataException, IOException
	{
		// sort the raw data to unit
		int dataCountOfRest = innerExportRestData( resultObject,
				rs,
				dataCountOfUnit );
		dataCountOfTotal += dataCountOfRest;

		MergeSortImpl mergeSortImpl = new MergeSortImpl( this.dataCountOfUnit,
				this.mergeSortUtil,
				this.tempFileUtil,
				this.tempRowFiles );
		this.goalRowIterator = mergeSortImpl.mergeSortOnUnits( );

		return dataCountOfRest;
	}

	/*
	 * A util method for sub class
	 * 
	 * @see org.eclipse.birt.data.engine.executor.cache.DataBaseExport#innerExportRestData(org.eclipse.birt.data.engine.odi.IResultObject,
	 *      org.eclipse.birt.data.engine.executor.cache.IRowResultSet, int)
	 */
	protected int innerExportRestData( IResultObject resultObject,
			IRowResultSet rs, int dataCountOfUnit ) throws DataException,
			IOException
	{
		exportLastRow( );
		rowBuffer[inMemoryPos] = resultObject;

		int columnCount = rs.getMetaData( ).getFieldCount( );
		int currDataCount = 1;
		IResultObject odaObject = null;
		
		while ( ( odaObject = rs.next( ) ) != null )
		{
			Object[] ob = new Object[columnCount];
			for ( int i = 0; i < columnCount; i++ )
				ob[i] = odaObject.getFieldValue( i + 1 );

			IResultObject rowData = resultObjectUtil.newResultObject( ob );

			exportLastRow( );
			rowBuffer[inMemoryPos] = rowData;
			currDataCount++;

			if ( inMemoryPos == dataCountOfUnit - 1 )
			{
				prepareNewTempRowFile( 0 );
				mergeSortUtil.sortSelf( rowBuffer );
			}
		}

		// Now all the rest rows exist in memory.
		rowBuffer = interchange( rowBuffer, inMemoryPos );
		mergeSortUtil.sortSelf( rowBuffer );
		if ( tempRowFiles.size( ) <= dataCountOfUnit )
		{
			prepareNewTempRowFile( dataCountOfUnit - tempRowFiles.size( ) );
		}
		else
		{
			prepareNewTempRowFile( 0 );
		}
		// Output the rest rows
		inMemoryPos = -1;
		getLastTempFile( tempRowFiles ).writeRows( rowBuffer, rowBuffer.length );
		getLastTempFile( tempRowFiles ).endWrite( );

		return currDataCount;
	}

	/**
	 * End write operation of the last temporary file and create a new temporary
	 * file and initialize row buffer.
	 * 
	 * @param cacheSize
	 */
	private void prepareNewTempRowFile( int cacheSize )
	{
		RowFile rowFile = null;
		if ( tempRowFiles.size( ) > 0 )
		{
			rowFile = (RowFile) ( tempRowFiles.get( tempRowFiles.size( ) - 1 ) );
			rowFile.endWrite( );
		}

		rowFile = tempFileUtil.newTempFile( cacheSize );
		tempRowFiles.add( rowFile );

		inMemoryPos = -1;
	}

	/**
	 * Write current result object to the current temparary file.
	 * 
	 * @throws IOException
	 */
	private void exportLastRow( ) throws IOException
	{
		inMemoryPos++;
		getLastTempFile( tempRowFiles ).write( rowBuffer[inMemoryPos] );
	}

	/**
	 * To switch the place of rows in array by a postion.
	 * 
	 * @param objectArray
	 * @param position
	 * @return
	 */
	private static IResultObject[] interchange( IResultObject[] objectArray,
			int position )
	{
		IResultObject[] tempBuffer = new IResultObject[objectArray.length];
		System.arraycopy( objectArray,
				position + 1,
				tempBuffer,
				0,
				objectArray.length - ( position + 1 ) );
		System.arraycopy( objectArray, 0, tempBuffer, objectArray.length
				- ( position + 1 ), ( position + 1 ) );
		return tempBuffer;
	}

	/**
	 * 
	 * @return
	 */
	private static RowFile getLastTempFile( List files )
	{
		return (RowFile) ( files.get( files.size( ) - 1 ) );
	}

	/*
	 * @see org.eclipse.birt.sort4.DataBaseExport#outputRowsUnit(org.eclipse.birt.sort4.RowData[],
	 *      int)
	 */
	protected void outputResultObjects( IResultObject[] resultObjects,
			int indexOfUnit ) throws IOException
	{
	}

	/*
	 * get a iterator on the result rows
	 * 
	 * @see org.eclipse.birt.data.engine.executor.cache.DataBaseExport#getRowIterator()
	 */
	public IRowIterator getRowIterator( )
	{
		return goalRowIterator;
	}

	/*
	 * close the merge sort row
	 * 
	 * @see org.eclipse.birt.data.engine.executor.cache.DataBaseExport#close()
	 */
	public void close( )
	{
		tempFileUtil.clearTempDir( );
	}
	
}