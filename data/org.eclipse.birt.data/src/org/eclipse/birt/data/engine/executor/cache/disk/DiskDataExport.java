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
import java.util.Comparator;
import java.util.Map;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.cache.IRowResultSet;
import org.eclipse.birt.data.engine.executor.cache.ResultObjectUtil;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.odi.IResultObject;

/**
 * An abstract class for data export to file, which has two sub classes. One is
 * the disk direct output class without any data opertaion, the other is with
 * exteranl data sorting.
 */
abstract class DiskDataExport
{
	protected ResultObjectUtil resultObjectUtil;
	
	/**
	 * According to the parameter of comparator to generate the instance, which
	 * is disk-based direct output instance or disk-based merge instance.
	 * 
	 * @param infoMap
	 * @param comparator
	 * @param rsMetaData
	 * @return a instance of DataBaseExport
	 */
	static DiskDataExport newInstance( Map infoMap,
			Comparator comparator, IResultClass rsMetaData,
			ResultObjectUtil resultObjectUtil )
	{
		DiskDataExport dbExport;
		if ( comparator != null )
			dbExport = new DiskSortExport( infoMap, comparator, resultObjectUtil );
		else
			dbExport = new DiskDirectExport( infoMap, resultObjectUtil );

		dbExport.resultObjectUtil = resultObjectUtil;

		return dbExport;
	}
	
	/**
	 * Export data which is stored in the resultObjects array to disk, which is
	 * the first step of export.
	 * 
	 * @param rs
	 * @throws IOException, file writer exception
	 */
	public abstract void exportStartDataToDisk( IResultObject[] resultObjects )
			throws IOException;
	
	/**
	 * Export data which is fetched form RowResultSet, which is the second step
	 * of export following the first step of exportStartDataToDisk.
	 * 
	 * @param resultObject, the start resultObject
	 * @param rs, follows the resultObject
	 * @throws DataException, fetch data exception
	 * @throws IOException, file writer exception
	 */
	public abstract int exportRestDataToDisk( IResultObject resultObject,
			IRowResultSet rs ) throws DataException, IOException;
	
	/**
	 * A util method for sub class
	 * 
	 * @throws IOException, file writer exception
	 */
	protected int innerExportStartData( IResultObject[] resultObjects )
			throws IOException
	{
		outputResultObjects( resultObjects, 0 );
		return resultObjects.length;
	}
	 
	/**
	 * A util method for sub class
	 * 
	 * @param resultObject
	 * @param rs
	 * @param dataCountOfUnit
	 * @throws DataException, fetch data exception
	 * @throws IOException, file writer exception
	 * @return how much data is exported
	 */
	protected int innerExportRestData( IResultObject resultObject,
			IRowResultSet rs, int dataCountOfUnit ) throws DataException,
			IOException
	{
		int columnCount = rs.getMetaData( ).getFieldCount( );

		IResultObject[] rowDatas = new IResultObject[dataCountOfUnit];
		rowDatas[0] = resultObject;
		int currDataCount = 1;
		int dataIndex = 1;
		
		IResultObject odaObject = null;
		while ( ( odaObject = rs.next( ) ) != null )
		{
			Object[] ob = new Object[columnCount];
			for ( int i = 0; i < columnCount; i++ )
				ob[i] = odaObject.getFieldValue( i + 1 );

			IResultObject rowData = resultObjectUtil.newResultObject( ob );
			if ( currDataCount % dataCountOfUnit == 0 )
			{
				int indexOfUnit = currDataCount / dataCountOfUnit - 1;
				if ( indexOfUnit >= 0 )
					outputResultObjects( rowDatas, indexOfUnit + 1 );
				dataIndex = 0;
			}

			rowDatas[dataIndex++] = rowData;
			currDataCount++;
		}

		// process the last unit
		IResultObject[] rowDatas2 = rowDatas;
		int indexOfUnit = currDataCount / dataCountOfUnit - 1;
		if ( currDataCount % dataCountOfUnit != 0 )
		{
			indexOfUnit++;
			int length = currDataCount % dataCountOfUnit;
			rowDatas2 = new IResultObject[length];
			System.arraycopy( rowDatas, 0, rowDatas2, 0, length );
		}
		outputResultObjects( rowDatas2, indexOfUnit + 1 );

		return currDataCount;
	}

	/**
	 * Output fetched data to file. When sort is needed, the data will be first
	 * sorted before it is exported.
	 * 
	 * @param resultObjects
	 * @param indexOfUnit
	 * @throws IOException, file writer exception
	 */
	protected abstract void outputResultObjects( IResultObject[] resultObjects,
			int indexOfUnit ) throws IOException;

}