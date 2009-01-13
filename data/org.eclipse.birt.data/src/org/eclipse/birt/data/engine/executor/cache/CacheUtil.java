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
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.util.Calendar;
import java.util.Map;

import org.eclipse.birt.data.engine.api.DataEngine;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.IncreDataSetCacheObject;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.DataEngineSession;
import org.eclipse.birt.data.engine.impl.IIncreCacheDataSetDesign;

/**
 * 
 */
public class CacheUtil
{
	private static final int MAX_DIR_CREATION_ATTEMPT = 1000;
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
	private static IntegerHolder cacheCounter1 = new IntegerHolder( 0 );
	private static IntegerHolder cacheCounter2 = new IntegerHolder( 0 );
	
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

	public static String createTempRootDir( final String tempDir )
	{
		try
		{
			return (String) AccessController.doPrivileged( new PrivilegedExceptionAction<Object>( ) {

				public Object run( ) throws Exception
				{
					String rootDirStr = null;

					// system default temp dir is used
					File tempDtEDir = null;
					tempDtEDir = new File( tempDir, "BirtDataTemp"
							+ System.currentTimeMillis( )
							+ cacheCounter1.intValue( ) );
					cacheCounter1.add( 1 );
					int x = 0;
					while ( tempDtEDir.exists( ) )
					{
						x++;
						tempDtEDir = new File( tempDir, "BirtDataTemp"
								+ System.currentTimeMillis( )
								+ cacheCounter1.intValue( ) + "_" + x );
					}
					tempDtEDir.mkdirs( );
					tempDtEDir.deleteOnExit( );
					rootDirStr = getCanonicalPath( tempDtEDir );
					return rootDirStr;

				}
			} );
		}
		catch ( Exception e )
		{
			return null;// throw new DataException( e.getLocalizedMessage( ) );
		}
	}

	/**
	 * @return session temp dir
	 * @throws DataException 
	 * @throws IOException 
	 */
	public static String createSessionTempDir( final String tempRootDir ) throws DataException
	{
		try
		{
			return (String) AccessController.doPrivileged( new PrivilegedExceptionAction<Object>( ) {

				public Object run( ) throws Exception
				{
					final String prefix = "session_";
					File sessionFile = null;
					
					// Here we use complex algorithm so that to avoid the repeating of
					// dir names in 1.same jvm but different threads 2.different jvm.
					String sessionTempDir = tempRootDir + File.separator + prefix
							+ System.currentTimeMillis() + cacheCounter2.intValue();
					cacheCounter2.add(1);
					sessionFile = new File(sessionTempDir);

					int i = 0;
					String tempDir = sessionTempDir;
					while (sessionFile.exists( ) )
					{
						i++;
						sessionTempDir = tempDir + "_" + i;
						sessionFile = new File(sessionTempDir);
						if (i > MAX_DIR_CREATION_ATTEMPT)
						{
							throw new DataException(
									ResourceConstants.FAIL_TO_CREATE_TEMP_DIR, diagnosticMkdirs( sessionFile ) );
						}
					}
					if ( !sessionFile.mkdirs( ) )
					{
						throw new DataException(
								ResourceConstants.FAIL_TO_CREATE_TEMP_DIR, diagnosticMkdirs( sessionFile ) );
					}
					sessionFile.deleteOnExit();
					return getCanonicalPath( sessionFile );

				}
			} );
		}
		catch ( Exception e )
		{
			return null;// throw new DataException( e.getLocalizedMessage( ) );
		}
		
	}

	/**
	 * 
	 * @param directory
	 * @return
	 */
	private static String diagnosticMkdirs( final File directory )
	{
		try
		{
			return (String) AccessController.doPrivileged( new PrivilegedExceptionAction<Object>( ) {

				public Object run( ) throws Exception
				{
					while ( true )
					{
						File canonFile = null;
				        try 
				        {
				            canonFile = directory.getCanonicalFile();
				        }
				        catch (IOException e) 
				        {
				            return directory.getAbsolutePath();
				        }
				        String parent = canonFile.getParent();
				        if( parent == null )
				        {
				        	return directory.getAbsolutePath();
				        }
				        File newDirectory = new File( parent );
				        if( newDirectory.exists( ) || newDirectory.mkdirs( ) )
				        {
				        	try
				        	{
								return canonFile.getCanonicalPath( );
							}
				        	catch (IOException e)
				        	{
								return newDirectory.getAbsolutePath();
							}
				        }
					}
				}
			} );
		}
		catch ( Exception e )
		{
			return null;// throw new DataException( e.getLocalizedMessage( ) );
		}
		
	}
	
	/**
	 * get the canonical path without exception.
	 * @param file
	 * @return
	 * @throws IOException
	 */
	private static String getCanonicalPath( final File file )
	{
		try
		{
			return (String) AccessController.doPrivileged( new PrivilegedExceptionAction<Object>( ) {

				public Object run( ) throws Exception
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
			} );
		}
		catch ( Exception e )
		{
			return null;// throw new DataException( e.getLocalizedMessage( ) );
		}
	}
	
	// ------------------------service for incremental cache-------------------------
	/**
	 * 
	 * @param tempDir
	 * @param dataSetDesign
	 * @return
	 */
	public static String createIncrementalTempDir( final DataEngineSession session, 
			final IIncreCacheDataSetDesign dataSetDesign)
	{
		try
		{
			return (String) AccessController.doPrivileged( new PrivilegedExceptionAction<Object>( ) {

				public Object run( ) throws Exception
				{
					final String prefix = PS_;
					File cacheDir = new File( session.getTempDir( )
							+ PATH_SEP + prefix + PATH_SEP
							+ Md5Util.getMD5( dataSetDesign.getConfigFileUrl( ).toString( ) ) + PATH_SEP + dataSetDesign.getName( ) );
					if ( cacheDir.exists( ) == false )
					{
						cacheDir.mkdirs( );
					}
					return getCanonicalPath( cacheDir );
				}
			} );
		}
		catch ( Exception e )
		{
			return null;// throw new DataException( e.getLocalizedMessage( ) );
		}
		
	}


	/**
	 * To get the last time doing caching or merging
	 * 
	 * @param folder
	 * @return String that contains the last time doing caching or merging
	 * @throws DataException
	 * @throws ClassNotFoundException
	 */
	public static String getLastTime( final String folder ) throws DataException
	{
		try
		{
			return (String) AccessController.doPrivileged( new PrivilegedExceptionAction<Object>( ) {

				public Object run( ) throws Exception
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
			} );
		}
		catch ( Exception e )
		{
			throw new DataException( ResourceConstants.DATASETCACHE_SAVE_ERROR,
					e );
		}
	}

	/**
	 * To save the current time doing caching or merging
	 * 
	 * @param folder
	 */
	public static void saveCurrentTime( final String folder ) throws DataException
	{
		try
		{
			AccessController.doPrivileged( new PrivilegedExceptionAction<Object>( ) {

				public Object run( ) throws Exception
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
					return null;
				}
			} );
		}
		catch ( Exception e )
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
	public static long getLastTimestamp( final String folder ) throws DataException
	{
		try
		{
			return (Long) AccessController.doPrivileged( new PrivilegedExceptionAction<Object>( ) {

				public Object run( ) throws Exception
				{
					RandomAccessFile raf = new RandomAccessFile( folder
							+ PATH_SEP + IncreDataSetCacheObject.TIMESTAMP_DATA, "r" );
					long timestamp = raf.readLong( );
					raf.close( );
					return timestamp;
				}
			} );
		}
		catch ( Exception e )
		{
			throw new DataException( e.getLocalizedMessage( ) );
		}
	}

	/**
	 * To save the current timestamp in incremental cache.
	 * 
	 * @param folder
	 */
	public static void saveCurrentTimestamp( final String folder )
			throws DataException
	{
		try
		{
			AccessController.doPrivileged( new PrivilegedExceptionAction<Object>( ) {

				public Object run( ) throws Exception
				{
					RandomAccessFile raf = new RandomAccessFile( folder
							+ PATH_SEP + IncreDataSetCacheObject.TIMESTAMP_DATA, "rw" );
					Calendar calendar = Calendar.getInstance( );
					raf.writeLong( calendar.getTimeInMillis( ) );
					raf.close( );
					return null;
				}
			} );
		}
		catch ( Exception e )
		{
			throw new DataException( e.getLocalizedMessage( ) );
		}
	}
}

class IntegerHolder
{ 
	private int value;
	
	IntegerHolder( int value )
	{
		this.value = value;
	}
	
	synchronized void add( int i )
	{
		this.value += i;
	}
	
	synchronized int intValue( )
	{
		return this.value;
	}
}