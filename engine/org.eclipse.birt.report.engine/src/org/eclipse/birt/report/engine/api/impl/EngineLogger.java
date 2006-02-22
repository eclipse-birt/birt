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


package org.eclipse.birt.report.engine.api.impl;

import java.io.IOException;
import com.ibm.icu.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * BIRT Logger is a logger associated with the global "org.eclipse.birt" name space. "org.eclipse.birt" is the ancestor of all of the BIRT packages.
 * According to Java 1.4 Logging mechnism, by default all Loggers also send their output to their parent Logger.
 * Thus, in any BIRT package, if developer uses Logger.getLogger( theBIRTClass.class.getName() ) to create a logger, 
 * the logger will send the logging requests to the BIRTLogger.  
 * And BIRTLogger will log the informatin into the global BIRT log file. The global log file is specified by main application.
 * If developer doesn't want the log of his module is logged into the global BIRT log file, 
 * he can simply use logger.setUseParentHandlers(false) to stop sending the logging request to the BIRTLogger.
 * <br>
 * Note: Because of a Java API's bug, an additional .lck file will be created for each log file. 
 * 		 Please see http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4775533 for detail.    
 */
public class EngineLogger {
	
	static private final String BIRT_NAME_SPACE = "org.eclipse.birt" ; //$NON-NLS-1$;

	/**
	 * This function should only called by the main application that starts BIRT. It will add a new log handler to the global BIRT logger. 
	 * @param directoryName - the directory name of the log file (e.g. C:\Log). The final file name will be the directory name plus an unique file name.
	 * 						  For example, if the directory name is C:\Log, the log file name will be something like C:\Log\ReportEngine_2005_02_26_11_26_56.log
	 * @param logLevel - the log level to be set. If logLevel is null, it will be ignored.
	 */
	public static void startEngineLogging( String directoryName, Level logLevel )
	{
		Logger logger = Logger.getLogger( BIRT_NAME_SPACE );
		assert (logger != null);
		
		if ( logLevel != null )
			logger.setLevel( logLevel );
			
		FileHandler logFileHandler = null;	
		try {
			logFileHandler = new FileHandler( generateUniqueLogFileName(directoryName), true );
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if ( logFileHandler != null )
		{
			logFileHandler.setFormatter( new SimpleFormatter() ); // In BIRT log, we should always use the simple format.
			logger.addHandler( logFileHandler );
		}
		
		logger.setUseParentHandlers( false );
	}

	/**
	 * Stop BIRT Logging and close all of the handlers. This function should only by called by the main application that started BIRT.
	 * Note: Because of a Java API's bug, an additional .lck file will be created for each log file. 
	 * 		 Please see http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4775533 for detail.    
	 */
	public static void stopEngineLogging()
	{
		Logger logger = Logger.getLogger( BIRT_NAME_SPACE );
		assert (logger != null);
		
		Handler[] handlers = logger.getHandlers();
		if ( (handlers != null) &&
			 (handlers.length > 0) )
		{
			for ( int i=0; i<handlers.length; i++ )
			{
				handlers[i].close();
				logger.removeHandler( handlers[i] );
			}
		}
	}

	/**
	 * Change the log level to the newLevel
	 * @param newLevel - new log level
	 */
	public static void changeLogLevel( Level newLevel )
	{
		Logger logger = Logger.getLogger( BIRT_NAME_SPACE );
		assert (logger != null);

		if ( newLevel != null )
			logger.setLevel( newLevel );		
	}
	
	/**
	 * This is a utility function that will create an unique file name with the timestamp information in the file name and
	 * append the file name into the directory name.
	 * For example, if the directory name is C:\Log, the returned file name will be C:\Log\ReportEngine_2005_02_26_11_26_56.log.
	 * @param directoryName - the directory name of the log file. 
	 * @return An unique Log file name which is the directory name plus the file name.
	 */
	private static String generateUniqueLogFileName( String directoryName )
	{
		SimpleDateFormat df = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss"); //$NON-NLS-1$
		String dateTimeString = df.format( new Date() );
		
		if ( directoryName == null )
			directoryName = ""; //$NON-NLS-1$
		else if ( directoryName.length() > 0 )
			directoryName += System.getProperty("file.separator"); //$NON-NLS-1$
		
		return new String( directoryName + "ReportEngine_" + dateTimeString + ".log" ); //$NON-NLS-1$; $NON-NLS-2$;
	}

}