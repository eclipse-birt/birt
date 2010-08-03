/*******************************************************************************
 * Copyright (c) 2004,2010 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.api.impl;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Filter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import com.ibm.icu.text.SimpleDateFormat;

/**
 * BIRT Logger is a logger associated with the global "org.eclipse.birt" name
 * space. "org.eclipse.birt" is the ancestor of all of the BIRT packages.
 * According to Java 1.4 Logging mechnism, by default all Loggers also send
 * their output to their parent Logger. Thus, in any BIRT package, if developer
 * uses Logger.getLogger( theBIRTClass.class.getName() ) to create a logger, the
 * logger will send the logging requests to the BIRTLogger. And BIRTLogger will
 * log the informatin into the global BIRT log file. The global log file is
 * specified by main application. If developer doesn't want the log of his
 * module is logged into the global BIRT log file, he can simply use
 * logger.setUseParentHandlers(false) to stop sending the logging request to the
 * BIRTLogger. <br>
 * Note: Because of a Java API's bug, an additional .lck file will be created
 * for each log file. Please see
 * http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4775533 for detail.
 */
public class EngineLogger
{

	static private final String BIRT_NAME_SPACE = "org.eclipse.birt"; //$NON-NLS-1$;

	static private final Logger ROOT_LOGGER = Logger
			.getLogger( BIRT_NAME_SPACE );

	/**
	 * the user defined logger output file.
	 */
	static private String logFileName;
	static private int logRollingSize;
	static private int logMaxBackupIndex;

	/**
	 * the log record are delegated to the adapter handler
	 */
	static private AdapterHandler adapterHandler;

	/**
	 * This function should only called by the main application that starts
	 * BIRT. It will add a new log handler to the global BIRT logger.
	 * 
	 * @param logger
	 *            - the user defined logger.
	 * @param directoryName
	 *            - the directory name of the log file (e.g. C:\Log). The final
	 *            file name will be the directory name plus an unique file name.
	 *            For example, if the directory name is C:\Log, the log file
	 *            name will be something like
	 *            C:\Log\ReportEngine_2005_02_26_11_26_56.log
	 * @param logLevel
	 *            - the log level to be set. If logLevel is null, it will be
	 *            ignored.
	 * @param rollingSize
	 * @param maxBackupIndex
	 */
	public static void startEngineLogging( Logger logger, String directoryName,
			String fileName, Level logLevel, int rollingSize, int maxBackupIndex )
	{
		// first setup the user defined logger
		if ( logger != null )
		{
			if ( !isValidLogger( logger ) )
			{
				logger.log( Level.WARNING,
						"the logger can't be the child of org.eclipse.birt" );
			}
			else
			{
				AdapterHandler adapter = getAdapterHandler( );
				adapter.setUserLogger( logger );
			}
		}
		// then setup the file logger
		if ( directoryName != null || fileName != null )
		{
			logFileName = generateUniqueLogFileName( directoryName, fileName );
			logRollingSize = rollingSize;
			logMaxBackupIndex = maxBackupIndex;
			if ( logLevel != Level.OFF )
			{
				Handler fileHandler = createFileLogger( logFileName,
						logRollingSize, logMaxBackupIndex );
				if ( fileHandler != null )
				{
					AdapterHandler adapter = getAdapterHandler( );
					adapter.setUserLogger( logger );
				}
			}
		}

		// finally we setup the log level, NULL means use the parent's level
		ROOT_LOGGER.setLevel( logLevel );
	}

	public static void setLogger( Logger logger )
	{
		if ( logger != null )
		{
			if ( !isValidLogger( logger ) )
			{
				logger.log( Level.WARNING,
						"the logger can't be the child of org.eclipse.birt" );
			}
		}
		AdapterHandler adapter = getAdapterHandler( );
		adapter.setUserLogger( logger );
	}

	public static boolean isValidLogger( Logger logger )
	{

		while ( logger != null )
		{
			if ( logger == ROOT_LOGGER )
			{
				return false;
			}
			logger = logger.getParent( );
		}
		return true;
	}

	/**
	 * Stop BIRT Logging and close all of the handlers. This function should
	 * only by called by the main application that started BIRT. Note: Because
	 * of a Java API's bug, an additional .lck file will be created for each log
	 * file. Please see
	 * http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4775533 for detail.
	 */
	public static void stopEngineLogging( )
	{
		java.security.AccessController
				.doPrivileged( new java.security.PrivilegedAction<Object>( ) {

					public Object run( )
					{
						doStopEngineLogging( );
						return null;
					}
				} );
	}

	private static void doStopEngineLogging( )
	{
		if ( adapterHandler != null )
		{
			ROOT_LOGGER.setUseParentHandlers( true );
			ROOT_LOGGER.removeHandler( adapterHandler );
			adapterHandler.close( );
			adapterHandler = null;
		}
		logFileName = null;
		logRollingSize = 0;
		logMaxBackupIndex = 0;
	}

	/**
	 * Change the log level to the newLevel
	 * 
	 * @param newLevel
	 *            - new log level
	 */
	public static void changeLogLevel( Level newLevel )
	{
		if ( logFileName != null && newLevel != Level.OFF )
		{
			AdapterHandler adapter = getAdapterHandler( );
			if ( adapter.fileHandler == null )
			{
				FileHandler fileHandler = createFileLogger( logFileName,
						logRollingSize, logMaxBackupIndex );
				if ( fileHandler != null )
				{
					adapter.setFileHandler( fileHandler );
				}
			}
		}
		ROOT_LOGGER.setLevel( newLevel );
	}

	/**
	 * This is a utility function that will create an unique file name with the
	 * timestamp information in the file name and append the file name into the
	 * directory name. For example, if the directory name is C:\Log, the
	 * returned file name will be C:\Log\ReportEngine_2005_02_26_11_26_56.log.
	 * 
	 * @param directoryName
	 *            - the directory name of the log file.
	 * @param fileName
	 *            the log file name
	 * @return An unique Log file name which is the directory name plus the file
	 *         name.
	 */
	private static String generateUniqueLogFileName( String directoryName,
			String fileName )
	{
		if ( fileName == null )
		{
			SimpleDateFormat df = new SimpleDateFormat( "yyyy_MM_dd_HH_mm_ss" ); //$NON-NLS-1$
			String dateTimeString = df.format( new Date( ) );
			fileName = "ReportEngine_" + dateTimeString + ".log"; //$NON-NLS-1$; $NON-NLS-2$;
		}

		if ( directoryName == null || directoryName.length( ) == 0 )
		{
			return fileName;
		}

		File folder = new File( directoryName );
		File file = new File( folder, fileName );
		return file.getPath( );
	}

	private static FileHandler createFileLogger( String fileName,
			int rollingSize, int logMaxBackupIndex )
	{
		try
		{
			File path = new File( fileName ).getParentFile( );
			if ( path != null )
			{
				path.mkdirs( );
			}
			if ( logMaxBackupIndex <= 0 )
			{
				logMaxBackupIndex = 1;
			}
			FileHandler logFileHandler = new FileHandler( fileName,
					rollingSize, logMaxBackupIndex, true );
			// In BIRT log, we should always use the simple format.
			logFileHandler.setFormatter( new SimpleFormatter( ) );
			logFileHandler.setLevel( Level.FINEST );
			logFileHandler.setEncoding( "utf-8" );
			return logFileHandler;
		}
		catch ( SecurityException e )
		{
			ROOT_LOGGER.log( Level.WARNING, e.getMessage( ), e );
		}
		catch ( IOException e )
		{
			ROOT_LOGGER.log( Level.WARNING, e.getMessage( ), e );
		}
		return null;
	}

	protected static AdapterHandler getAdapterHandler( )
	{
		if ( adapterHandler == null )
		{
			synchronized ( EngineLogger.class )
			{
				if ( adapterHandler == null )
				{
					adapterHandler = new AdapterHandler(
							ROOT_LOGGER.getParent( ) );
					ROOT_LOGGER.addHandler( adapterHandler );
					ROOT_LOGGER.setUseParentHandlers( false );
				}
			}
		}
		return adapterHandler;
	}

	public static void setThreadLogger( Logger logger )
	{
		if ( logger == null && adapterHandler == null )
		{
			return;
		}
		AdapterHandler adapter = getAdapterHandler( );
		adapter.setThreadLogger( logger );
	}

	static class AdapterHandler extends Handler
	{

		private Logger parent;
		private Logger userLogger;
		private Handler fileHandler;
		private ThreadLocal<Logger> threadLoggers;

		public AdapterHandler( Logger logger )
		{
			this.parent = logger;
		}

		public void setUserLogger( Logger logger )
		{
			this.userLogger = logger;
		}

		public void setFileHandler( FileHandler fileHandler )
		{
			this.fileHandler = fileHandler;
		}

		public void setThreadLogger( Logger logger )
		{
			if ( logger != null )
			{
				if ( threadLoggers == null )
				{
					synchronized ( this )
					{
						if ( threadLoggers == null )
						{
							threadLoggers = new ThreadLocal<Logger>( );
						}
					}
				}
				threadLoggers.set( logger );
			}
			else if ( threadLoggers != null )
			{
				threadLoggers.set( null );
			}
		}

		public void publish( LogRecord record )
		{
			// first try the threadLogger
			if ( threadLoggers != null )
			{
				Logger logger = threadLoggers.get( );
				if ( logger != null )
				{
					publishToLogger( logger, record );
					return;
				}
			}
			// then try the user and file handler
			if ( userLogger != null || fileHandler != null )
			{
				if ( userLogger != null )
				{
					// publish using the logger's handler
					publishToLogger( userLogger, record );
				}
				if ( fileHandler != null )
				{
					fileHandler.publish( record );
				}
				return;
			}
			// delegate to the parent
			publishToLogger( parent, record );
		}

		public void close( ) throws SecurityException
		{
			if ( fileHandler != null )
			{
				fileHandler.close( );
				fileHandler = null;
			}
		}

		public void flush( )
		{
			if ( fileHandler != null )
			{
				fileHandler.flush( );
			}
		}

		// This API is used to push the log record to intern handler. If we
		// invoke the log() directly, it may mass the invoking stack, see the
		// implementation of LogRecord#inferCaller()
		private void publishToLogger( Logger logger, LogRecord record )
		{
			if ( !logger.isLoggable( record.getLevel( ) ) )
			{
				return;
			}
			synchronized ( logger )
			{
				Filter filter = logger.getFilter( );
				if ( filter != null && !filter.isLoggable( record ) )
				{
					return;
				}
			}
			// Post the LogRecord to all our Handlers, and then to
			// our parents' handlers, all the way up the tree.

			while ( logger != null )
			{
				Handler targets[] = logger.getHandlers( );

				if ( targets != null )
				{
					for ( int i = 0; i < targets.length; i++ )
					{
						targets[i].publish( record );
					}
				}

				if ( !logger.getUseParentHandlers( ) )
				{
					break;
				}

				logger = logger.getParent( );
			}
		}
	}
}