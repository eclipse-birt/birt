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
package org.eclipse.birt.data.engine.executor.cache;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;
import java.util.logging.Logger;

import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.DataEngine;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.odi.IEventHandler;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.odi.IResultObject;

/**
 * 
 */
public class CacheUtil
{
	/**
	 * how many rows could be cached in memory when transformation is done, the
	 * minimum value for it is 500. It is strongly recommended to give a high
	 * value to it so as to get a good performance. If this value is not set,
	 * the default value is 20,000.
	 */
	private static String TEST_MEM_BUFFER_SIZE = "birt.data.engine.test.memcachesize";	
	private static Integer cacheCounter1 = new Integer(0);
	private static Integer cacheCounter2 = new Integer(0);
	
	private String tempDir;
	
	/**
	 * 
	 * @param context
	 */
	public CacheUtil ( String tempDir )
	{
		this.tempDir = tempDir;
	}
	
	//--------------------service for SmartCache----------------------
	
	/**
	 * @return
	 * @throws DataException 
	 */
	static int computeMemoryBufferSize( Map appContext ) throws DataException
	{
		//here a simple assumption, that 1M memory can accomondate 2000 rows
		if ( appContext == null )
			return 10*1024*1024;

		if ( appContext.get( TEST_MEM_BUFFER_SIZE )!= null )
		{
			//For unit test.
			return populateMemBufferSize( appContext.get( TEST_MEM_BUFFER_SIZE ) );
		}
		
		return populateMemBufferSize( appContext.get( DataEngine.MEMORY_BUFFER_SIZE )) * 1024 * 1024;
	}

	/**
	 * 
	 * @param propValue
	 * @return
	 * @throws DataException 
	 */
	private static int populateMemBufferSize( Object propValue ) throws DataException
	{
		String targetBufferSize =  propValue == null
				? "1" : propValue
						.toString( );
		
		int memoryCacheSize = 10; 
		
		if ( targetBufferSize != null )
			memoryCacheSize = Integer.parseInt( targetBufferSize );

		if ( memoryCacheSize <= 0 )
			throw new DataException( ResourceConstants.INVALID_MEMORY_BUFFER_SIZE,
					new Object[]{
						new Integer( memoryCacheSize )
					} );
		return memoryCacheSize;
	}
	
	/**
	 * @param sortSpec
	 * @return Comparator based on specified sortSpec, null indicates there is
	 *         no need to do sorting
	 */
	static Comparator getComparator( SortSpec sortSpec,
			final IEventHandler eventHandler )
	{
		if ( sortSpec == null )
			return null;

		final int[] sortKeyIndexes = sortSpec.sortKeyIndexes;
		final String[] sortKeyColumns = sortSpec.sortKeyColumns;
		
		if ( sortKeyIndexes == null || sortKeyIndexes.length == 0 )
			return null;

		final boolean[] sortAscending = sortSpec.sortAscending;

		Comparator comparator = new Comparator( ) {

			/**
			 * compares two row indexes, actually compares two rows pointed by
			 * the two row indexes
			 */
			public int compare( Object obj1, Object obj2 )
			{
				IResultObject row1 = (IResultObject) obj1;
				IResultObject row2 = (IResultObject) obj2;

				// compare group keys first
				for ( int i = 0; i < sortKeyIndexes.length; i++ )
				{
					int colIndex = sortKeyIndexes[i];
					String colName = sortKeyColumns[i];
					try
					{
						Object colObj1 = null;
						Object colObj2 = null;
						
						if ( eventHandler != null )
						{
							colObj1 = eventHandler.getValue( row1,
									colIndex,
									colName );
							colObj2 = eventHandler.getValue( row2,
									colIndex,
									colName );
						}
						else
						{
							colObj1 = row1.getFieldValue( colIndex );
							colObj2 = row2.getFieldValue( colIndex );
						}
						
						int result = compareObjects( colObj1, colObj2 );
						if ( result != 0 )
						{
							return sortAscending[i] ? result : -result;
						}
					}
					catch ( DataException e )
					{
						// Should never get here
						// colIndex is always valid
					}
				}

				// all equal, so return 0
				return 0;
			}
		};

		return comparator;
	}
	
	/**
	 * @param ob1
	 * @param ob2
	 * @return
	 */
	public static int compareObjects( Object ob1, Object ob2 )
	{
		// default value is 0
		int result = 0;
		
		// 1: the case of reference is the same
		if ( ob1 == ob2 )
		{
			return result;
		}
		
		// 2: the case of one of two object is null
		if ( ob1 == null || ob2 == null )
		{
			// keep null value at the first position in ascending order
			if ( ob1 == null )
			{
				result = -1;
			}
			else
			{
				result = 1;
			}
			return result;
		}
		
		// 3: other cases
		if ( ob1.equals( ob2 ) )
		{
			return result;
		}
		else if ( ob1 instanceof Comparable
				&& ob2 instanceof Comparable )
		{
			Comparable comp1 = (Comparable) ob1;
			Comparable comp2 = (Comparable) ob2;
			
//			 Integer can not be compared with Double.
			if ( ob1.getClass( ) != ob2.getClass( ) )
			{
				try
				{
					if ( ob1 instanceof Number && ob2 instanceof Number )
					{

						comp1 = DataTypeUtil.toDouble( ob1 );
						comp2 = DataTypeUtil.toDouble( ob2 );
					}
					else if ( ob1 instanceof Date && ob2 instanceof Date )
					{
						comp1 = DataTypeUtil.toDate( ob1 );
						comp2 = DataTypeUtil.toDate( ob1 );
					}
				}
				catch ( BirtException ex )
				{
					// impossible
				}
			}
				
			result = comp1.compareTo( comp2 );					
		}
		else if ( ob1 instanceof Boolean
				&& ob2 instanceof Boolean )
		{
			// false is less than true
			Boolean bool = (Boolean) ob1;
			if ( bool.equals( Boolean.TRUE ) )
				result = 1;
			else
				result = -1;
		}
		else
		{
			// Should never get here
			//throw new UnsupportedOperationException( );
		}

		return result;
	}
	
	//------------------------service for DiskCache-------------------------
	
	/**
	 * @return
	 */
	public String doCreateTempRootDir( Logger logger )
	{
		String rootDirStr = null;

		// system default temp dir is used
		File tempDtEDir = null;
		synchronized ( cacheCounter1 )
		{
			tempDtEDir = new File( tempDir, "BirtDataTemp"
					+ System.currentTimeMillis( ) + cacheCounter1 );
			cacheCounter1 = new Integer( cacheCounter1.intValue( ) + 1 );
			int x = 0;
			while ( tempDtEDir.exists( ) )
			{
				x++;
				tempDtEDir = new File( tempDir, "BirtDataTemp"
						+ System.currentTimeMillis( ) + cacheCounter1 + "_" + x );
			}
			tempDtEDir.mkdir( );
			tempDtEDir.deleteOnExit( );
		}
		
		try
		{
			rootDirStr = tempDtEDir.getCanonicalPath( );
		}
		catch ( IOException e )
		{
			// normally this exception will never be thrown
		}
		logger.info( "Temp directory used to cache data is " + rootDirStr );

		return rootDirStr;
	}
	
	/**
	 * @return session temp dir
	 */
	public String createSessionTempDir( String tempRootDirStr )
	{
		String sessionTempDirStr;

		final String prefix = "session_";

		synchronized ( cacheCounter2 )
		{
			//Here we use complex algorithm so that to avoid the repeating of 
			//dir names in 1.same jvm but different threads 2.different jvm.
			sessionTempDirStr = tempRootDirStr
					+ File.separator + prefix + System.currentTimeMillis( )
					+ cacheCounter2.intValue( );
			cacheCounter2 = new Integer(cacheCounter2.intValue( )+1);
			File file = new File( sessionTempDirStr );
			
			int i = 0;
			while ( file.exists( ) || !file.mkdir( ) )
			{
				i++;
				sessionTempDirStr = sessionTempDirStr + "_" + i;
				file = new File( sessionTempDirStr );
			}	
		}
		return sessionTempDirStr;

	}
		
	/**
	 * @param rsMeta
	 * @return
	 */
	public static SortSpec getSortSpec( IResultClass rsMeta )
	{
		int fieldCount = rsMeta.getFieldCount( );
		int[] sortKeyIndexs = new int[fieldCount];
		String[] sortKeyNames = new String[fieldCount];
		boolean[] ascending = new boolean[fieldCount];
		for ( int i = 0; i < fieldCount; i++ )
		{
			sortKeyIndexs[i] = i + 1; // 1-based
			ascending[i] = true;
		}

		return new SortSpec( sortKeyIndexs, sortKeyNames, ascending );
	}
	
}
