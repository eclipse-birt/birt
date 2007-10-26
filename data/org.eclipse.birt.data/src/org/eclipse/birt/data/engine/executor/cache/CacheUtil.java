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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.util.Calendar;
import java.util.Map;

import org.eclipse.birt.data.engine.api.DataEngine;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.IncreDataSetCacheObject;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;

/**
 * 
 */
public class CacheUtil
{
	private static final int MAX_DIR_CREATION_ATTEMPT = 100000;
	private static final String PATH_SEP = File.separator;
	private static final String TEST_MEM_BUFFER_SIZE = "birt.data.engine.test.memcachesize";
	
	/**
	 * timestamp.data file will be used in incremental cache, while
	 * time.data file will be used in disk cache.
	 */
	private static final String TIME_DATA = "time.data";
	/**
	 * root directory for incremental cache, which should under the user
	 * specified cache directory.
	 */
	private static final String PS_ = "PS_"; 
	
	/**
	 * counters for creating unique temporary directory.
	 */
	private static Integer cacheCounter1 = new Integer( 0 );
	private static Integer cacheCounter2 = new Integer( 0 );
	
	private CacheUtil( )
	{
	}
	
	// --------------------service for SmartCache----------------------
	/**
	 * @return
	 */
	public static long computeMemoryBufferSize( Map appContext )
	{
		//here a simple assumption, that 1M memory can accommodate 2000 rows
		if ( appContext == null )
			return 10*1024*1024;
		if ( appContext.get( TEST_MEM_BUFFER_SIZE )!= null )
		{
			//For unit test.The unit is 1 byte.
			return populateMemBufferSize( appContext.get( TEST_MEM_BUFFER_SIZE ) );
		}
		
		//The unit is 1M.
		return populateMemBufferSize( appContext.get( DataEngine.MEMORY_BUFFER_SIZE )) * 1024 * 1024;
	}

	/**
	 * 
	 * @param propValue
	 * @return
	 */
	private static long populateMemBufferSize( Object propValue )
	{
		String targetBufferSize =  propValue == null
				? "10" : propValue
						.toString( );
		
		long memoryCacheSize = 10; 
		
		if ( targetBufferSize != null )
			memoryCacheSize = Long.parseLong( targetBufferSize );

		return memoryCacheSize == 0?1:memoryCacheSize;
	}
	
	// ------------------------service for DiskCache-------------------------

	public static String createTempRootDir( String tempDir )
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
		rootDirStr = getCanonicalPath( tempDtEDir );
		return rootDirStr;
	}

	/**
	 * @return session temp dir
	 * @throws DataException 
	 * @throws IOException 
	 */
	public static String createSessionTempDir( String tempRootDir ) throws DataException
	{

		final String prefix = "session_";
		File sessionFile = null;
		synchronized ( cacheCounter2 )
		{
			// Here we use complex algorithm so that to avoid the repeating of
			// dir names in 1.same jvm but different threads 2.different jvm.
			String sessionTempDir = tempRootDir
					+ File.separator + prefix + System.currentTimeMillis( )
					+ cacheCounter2.intValue( );
			cacheCounter2 = new Integer( cacheCounter2.intValue( ) + 1 );
			sessionFile = new File( sessionTempDir );

			int i = 0;
			while ( sessionFile.exists( ) || !sessionFile.mkdir( ) )
			{
				i++;
				sessionTempDir = sessionTempDir + "_" + i;
				sessionFile = new File( sessionTempDir );
				if( i > MAX_DIR_CREATION_ATTEMPT )
					throw new DataException( ResourceConstants.FAIL_TO_CREATE_TEMP_DIR,
							sessionFile.getAbsolutePath( ) );
			}
			sessionFile.deleteOnExit( );
		}
		return getCanonicalPath( sessionFile );
	}

	/**
	 * get the canonical path without exception.
	 * @param file
	 * @return
	 * @throws IOException
	 */
	private static String getCanonicalPath( File file )
	{
		try
		{
			return file.getCanonicalPath( );
		}
		catch ( IOException e )
		{
			return file.getAbsolutePath( );
		}
	}
	
	// ------------------------service for incremental cache-------------------------
	/**
	 * 
	 * @param tempDir
	 * @param dataSetDesign
	 * @return
	 */
	public static String createIncrementalTempDir( String tempDir,
			String configFilePath, String dataSetName )
	{
		final String prefix = PS_;
		File cacheDir = new File( tempDir
				+ PATH_SEP + prefix + PATH_SEP
				+ Md5Util.getMD5( configFilePath ) + PATH_SEP + dataSetName );
		if ( cacheDir.exists( ) == false )
		{
			cacheDir.mkdirs( );
		}
		return getCanonicalPath( cacheDir );
	}


	/**
	 * To get the last time doing caching or merging
	 * 
	 * @param folder
	 * @return String that contains the last time doing caching or merging
	 * @throws DataException
	 * @throws ClassNotFoundException
	 */
	public static String getLastTime( String folder ) throws DataException
	{
		try
		{
			File file = new File( folder + PATH_SEP + TIME_DATA );
			if ( !file.exists( ) )
			{
				return null;
			}
			FileInputStream fis = new FileInputStream( file );
			ObjectInputStream ois = new ObjectInputStream( fis );

			String lastTime = (String) ois.readObject( );

			fis.close( );
			ois.close( );

			return lastTime;
		}
		catch ( IOException e )
		{
			throw new DataException( ResourceConstants.DATASETCACHE_SAVE_ERROR,
					e );
		}
		catch ( ClassNotFoundException e )
		{
			e.printStackTrace( );
			assert false;
		}
		return null;
	}

	/**
	 * To save the current time doing caching or merging
	 * 
	 * @param folder
	 */
	public static void saveCurrentTime( String folder ) throws DataException
	{
		try
		{
			FileOutputStream fos;

			fos = new FileOutputStream( new File( folder + PATH_SEP + TIME_DATA ) );

			ObjectOutputStream oos = new ObjectOutputStream( fos );

			Calendar calendar = Calendar.getInstance( );

			StringBuffer buffer = new StringBuffer( );

			buffer.append( populate2DigitString( calendar.get( Calendar.YEAR ) ) );
			buffer.append( populate2DigitString( calendar.get( Calendar.MONTH ) + 1 ) );
			buffer.append( populate2DigitString( calendar.get( Calendar.DATE ) ) );
			if ( calendar.get( Calendar.AM_PM ) == Calendar.PM )
				buffer.append( populate2DigitString( calendar.get( Calendar.HOUR ) + 12 ) );
			buffer.append( populate2DigitString( calendar.get( Calendar.MINUTE ) ) );
			buffer.append( populate2DigitString( calendar.get( Calendar.SECOND ) ) );

			oos.writeObject( buffer.toString( ) );

			fos.close( );
			oos.close( );
		}
		catch ( IOException e )
		{
			throw new DataException( e.getLocalizedMessage( ) );
		}

	}
	
	/**
	 * 
	 * @param value
	 * @return
	 */
	private static String populate2DigitString( int value )
	{
		if ( value < 10 )
			return "0" + value;
		else
			return String.valueOf( value );
	}
	
	/**
	 * To get the last timestamp in incremental cache.
	 * 
	 * @param folder
	 * @return String that contains the last time doing caching or merging
	 * @throws DataException
	 * @throws ClassNotFoundException
	 */
	public static long getLastTimestamp( String folder ) throws DataException
	{
		try
		{
			RandomAccessFile raf = new RandomAccessFile( folder
					+ PATH_SEP + IncreDataSetCacheObject.TIMESTAMP_DATA, "r" );
			long timestamp = raf.readLong( );
			raf.close( );
			return timestamp;
		}
		catch ( Exception e )
		{
			throw new DataException( e.getMessage( ) );
		}
	}

	/**
	 * To save the current timestamp in incremental cache.
	 * 
	 * @param folder
	 */
	public static void saveCurrentTimestamp( String folder )
			throws DataException
	{
		try
		{
			RandomAccessFile raf = new RandomAccessFile( folder
					+ PATH_SEP + IncreDataSetCacheObject.TIMESTAMP_DATA, "rw" );
			Calendar calendar = Calendar.getInstance( );
			raf.writeLong( calendar.getTimeInMillis( ) );
			raf.close( );
		}
		catch ( Exception e )
		{
			throw new DataException( e.getMessage( ) );
		}
	}
}
