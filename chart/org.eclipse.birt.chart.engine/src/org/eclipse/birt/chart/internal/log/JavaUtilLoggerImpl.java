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

package org.eclipse.birt.chart.internal.log;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.plugin.ChartEnginePlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

/**
 * An ILogger implementation using java.util.logging.Logger
 */

public class JavaUtilLoggerImpl implements ILogger
{
	private Logger logger;

	private Level javaLevel = Level.WARNING;

	private static StreamHandler fileHandler;

	/**
	 * The constructor.
	 * 
	 * @param name
	 */
	public JavaUtilLoggerImpl( String name )
	{
		this.logger = Logger.getLogger( name );
		
		if ( Platform.isRunning( ) )
		{
			// Only log exception to file when eclipse is running
			this.logger.addHandler( getFileHandler( ) );
			this.logger.setUseParentHandlers( false );
		}
		
		if ( this.logger.getLevel( ) == null )
		{
			this.logger.setLevel( javaLevel );
		}
	}

	/**
	 * The constructor.
	 * 
	 * @param name
	 */
	public JavaUtilLoggerImpl( String name, int verboseLevel )
	{
		this.logger = Logger.getLogger( name );
		setVerboseLevel( verboseLevel );
	}

	/**
	 * @return the inner java.util.logging.Logger
	 */
	public Logger getJavaLogger( )
	{
		return this.logger;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.log.ILogger#setVerboseLevel(int)
	 */
	public void setVerboseLevel( int iVerboseLevel )
	{
		this.javaLevel = toJavaUtilLevel( iVerboseLevel );

		this.logger.setLevel( this.javaLevel );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.log.ILogger#log(int, java.lang.String)
	 */
	public void log( int iCode, String sMessage )
	{
		Level level = toJavaUtilLevel( iCode );

		if ( logger.isLoggable( level ) )
		{
			LogRecord lr = new LogRecord( level, sMessage );
			String[] rt = inferCaller( );
			lr.setSourceClassName( rt[0] );
			lr.setSourceMethodName( rt[1] );
			logger.log( lr );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.log.ILogger#log(java.lang.Exception)
	 */
	public void log( Exception ex )
	{
		if ( logger.isLoggable( Level.WARNING ) )
		{
			LogRecord lr = new LogRecord( Level.WARNING, "Exception" ); //$NON-NLS-1$
			lr.setThrown( ex );
			String[] rt = inferCaller( );
			lr.setSourceClassName( rt[0] );
			lr.setSourceMethodName( rt[1] );
			logger.log( lr );
		}
	}

	// Private method to infer the caller's class and method names
	private String[] inferCaller( )
	{
		String[] rt = new String[2];
		rt[0] = this.getClass( ).getName( );
		rt[1] = "log"; //$NON-NLS-1$

		// Get the stack trace.
		StackTraceElement stack[] = ( new Throwable( ) ).getStackTrace( );
		// First, search back to a method in the JavaUtilLoggerImpl class.
		int ix = 0;
		while ( ix < stack.length )
		{
			StackTraceElement frame = stack[ix];
			String cname = frame.getClassName( );
			if ( cname.equals( this.getClass( ).getName( ) ) )
			{
				break;
			}
			ix++;
		}
		// Now search for the first frame before the "JavaUtilLoggerImpl" class.
		while ( ix < stack.length )
		{
			StackTraceElement frame = stack[ix];
			String cname = frame.getClassName( );
			if ( !cname.equals( this.getClass( ).getName( ) ) )
			{
				// We've found the relevant frame.
				rt[0] = cname;
				rt[1] = frame.getMethodName( );
				return rt;
			}
			ix++;
		}
		// We haven't found a suitable frame, so just punt. This is
		// OK as we are only commited to making a "best effort" here.
		return rt;
	}

	private static Level toJavaUtilLevel( int chartLevel )
	{
		if ( chartLevel <= ILogger.ALL )
		{
			return Level.ALL;
		}
		if ( chartLevel <= ILogger.TRACE )
		{
			return Level.FINER;
		}
		if ( chartLevel <= ILogger.INFORMATION )
		{
			return Level.INFO;
		}
		if ( chartLevel <= ILogger.WARNING )
		{
			return Level.WARNING;
		}

		// Default to SEVERE.
		return Level.SEVERE;
	}
	
	private StreamHandler getFileHandler( )
	{
		if ( fileHandler == null )
		{
			try
			{
				String sName = ChartEnginePlugin.ID
						+ new SimpleDateFormat( "_yyyy_MM_dd_HH_mm_ss_SSS" ).format( new Date( ) ); //$NON-NLS-1$
				String sDir = getLogFolder( );
				fileHandler = new FileHandler( sDir
						+ System.getProperty( "file.separator" ) //$NON-NLS-1$
						+ sName
						+ ".log", true ); //$NON-NLS-1$
			}
			catch ( SecurityException e )
			{
				this.logger.log( new LogRecord( Level.WARNING, e.getMessage( ) ) );
			}
			catch ( IOException e )
			{
				this.logger.log( new LogRecord( Level.WARNING, e.getMessage( ) ) );
			}
			fileHandler.setFormatter( new SimpleFormatter( ) );
			fileHandler.setLevel( Level.FINEST );
		}
		return fileHandler;
	}
	
	private static String getLogFolder( )
	{
		Bundle bundle = Platform.getBundle( ChartEnginePlugin.ID );
		IPath path = Platform.getStateLocation( bundle );
		String sPath = path.toString( );
		if ( sPath.lastIndexOf( System.getProperty( "file.separator" ) ) == sPath.length( ) - 1 ) //$NON-NLS-1$
		{
			sPath = sPath.substring( 0, sPath.length( ) - 1 );
		}
		return sPath;
	}
	
	
}
