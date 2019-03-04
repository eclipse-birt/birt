/*
 *************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  
 *************************************************************************
 */

package org.eclipse.birt.report.data.oda.jdbc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.eclipse.datatools.connectivity.oda.LogConfiguration;
import org.eclipse.datatools.connectivity.oda.util.logging.Level;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *  Unit tests for setting the driver's trace logging configuration.
 */
public class LogConfigurationTest {
	private Logger m_pkgLogger = Logger.getLogger( LogConfigurationTest.class.getName( )
			.substring( 0,
					LogConfigurationTest.class.getName( ).lastIndexOf( "." ) ) );
    private String m_simpleFormatterName = "java.util.logging.SimpleFormatter";
    private OdaJdbcDriver m_driver = new OdaJdbcDriver();
    private String m_logDirectory = "./OdaLogs";
    private File m_dir = new File( m_logDirectory );
    
    /*
     * @see TestCase#setUp()
     */
	@Before
    public void logConfigurationSetUp() throws Exception
    {
        // create directory to store logs
	    if ( m_dir.exists() )
	    {
	        clearDirectory( m_dir );
	    }
	    else
	    {
	        boolean created = m_dir.mkdir();
	        if ( ! created )
	            throw new IOException( "Cannot create directory: " + m_dir.getName() ); 
	    }
    }

    /*
     * @see TestCase#tearDown()
     */
	@After
    public void logConfigurationTearDown() throws Exception
    {
        clearHandlers( m_pkgLogger ); 
        
        // remove directory
        clearDirectory( m_dir );
        boolean deleted = m_dir.delete();
        if ( ! deleted )
            throw new IOException( "Cannot delete directory: " + m_dir.getName() ); 
    }
    
    /*
     * Test that setLogConfiguration doesn't fail when passing valid parameters
     */ 
	@Test
    public void testValidLogConfig() throws Exception
    {		
        // null strings: default handler and formatter will be created
        clearHandlers( m_pkgLogger ); 
        LogConfiguration config = new LogConfiguration( Level.WARNING, null, null, null );
        m_driver.setLogConfiguration( config );
        Handler[] handlers = m_pkgLogger.getHandlers();
        assertSingleHandlerAndSimpleFormatter( handlers );
        assertLoggerAndHandlerLevels( handlers, java.util.logging.Level.WARNING );
        assertHandlerType( handlers, true /* isConsoleHandler */ );

        // empty strings: default handler and formatter will be created
        clearHandlers( m_pkgLogger );       
        config = new LogConfiguration( Level.FINER, "", "", "" );
        m_driver.setLogConfiguration( config );
        handlers = m_pkgLogger.getHandlers();
        assertSingleHandlerAndSimpleFormatter( handlers );
        assertLoggerAndHandlerLevels( handlers, java.util.logging.Level.FINER );
        assertHandlerType( handlers, true /* isConsoleHandler */ );

        // null directory and prefix, valid formatter class
        clearHandlers( m_pkgLogger ); 
        config = new LogConfiguration( Level.FINE, null, "", m_simpleFormatterName );
        m_driver.setLogConfiguration( config );
        handlers = m_pkgLogger.getHandlers();
        assertSingleHandlerAndSimpleFormatter( handlers );
        assertLoggerAndHandlerLevels( handlers, java.util.logging.Level.FINE );      
        assertHandlerType( handlers, true /* isConsoleHandler */ );
        
        // test re-use of the existing handler: 
        config = new LogConfiguration( Level.ALL, "", null, m_simpleFormatterName );
        m_driver.setLogConfiguration( config );
        handlers = m_pkgLogger.getHandlers();
        assertSingleHandlerAndSimpleFormatter( handlers );   
        assertLoggerAndHandlerLevels( handlers, java.util.logging.Level.ALL ); 
        assertHandlerType( handlers, true /* isConsoleHandler */ );

        // test OFF level: log level should not have added any handler
        clearHandlers( m_pkgLogger );         
        config = new LogConfiguration( Level.OFF, null, null, m_simpleFormatterName );
        m_driver.setLogConfiguration( config );
        assertEquals( java.util.logging.Level.OFF, m_pkgLogger.getLevel() );
        assertEquals( 0, m_pkgLogger.getHandlers().length );

        // use a log level value greater than SEVERE, 
        // expects to be interpreted as Level.OFF 
        clearHandlers( m_pkgLogger );         
        config = new LogConfiguration( 1002, null, null, m_simpleFormatterName );
        m_driver.setLogConfiguration( config );
        assertEquals( java.util.logging.Level.OFF, m_pkgLogger.getLevel() );
        // OFF log level should not have added any handler
        assertEquals( 0, m_pkgLogger.getHandlers().length );
    }
    
    // Test that setLogConfiguration doesn't fail when passed invalid parameters
	@Test
    public void testInvalidLogConfig() throws Exception
    {
	    // invalid logging level will be ignored, default handler and formatter will be created
        clearHandlers( m_pkgLogger ); 
        java.util.logging.Level priorLogLevel = java.util.logging.Level.CONFIG;
        m_pkgLogger.setLevel( priorLogLevel );
	    LogConfiguration config = new LogConfiguration( -100, null, null, null );  
        m_driver.setLogConfiguration( config );
        Handler[] handlers = m_pkgLogger.getHandlers();
        assertSingleHandlerAndSimpleFormatter( handlers );
        assertLoggerAndHandlerLevels( handlers, priorLogLevel );
        assertHandlerType( handlers, true /* isConsoleHandler */ );
  
        // test that a formatter gets set even if 
        // the given log level is not valid, as long as
        // the pre-existing log level is not OFF
        clearHandlers( m_pkgLogger ); 
        priorLogLevel = java.util.logging.Level.CONFIG;
        m_pkgLogger.setLevel( priorLogLevel );
	    config = new LogConfiguration( -100, null, null, m_simpleFormatterName );
        m_driver.setLogConfiguration( config );
        handlers = m_pkgLogger.getHandlers();
        assertSingleHandlerAndSimpleFormatter( handlers );
        assertLoggerAndHandlerLevels( handlers, priorLogLevel );
        assertHandlerType( handlers, true /* isConsoleHandler */ );

        // invalid level specified, prior level is OFF, should not have added any handler
        clearHandlers( m_pkgLogger ); 
        priorLogLevel = java.util.logging.Level.OFF;
        m_pkgLogger.setLevel( priorLogLevel );
	    config = new LogConfiguration( 50, null, null, m_simpleFormatterName );
        m_driver.setLogConfiguration( config );
        assertEquals( priorLogLevel, m_pkgLogger.getLevel() );
        assertEquals( 0, m_pkgLogger.getHandlers().length );

        // invalid level specified, prior level is null, package logger level should be null
        clearHandlers( m_pkgLogger ); 
        m_pkgLogger.setLevel( null );
	    config = new LogConfiguration( -100, null, null, m_simpleFormatterName );
        m_driver.setLogConfiguration( config );
        handlers = m_pkgLogger.getHandlers();
        assertSingleHandlerAndSimpleFormatter( handlers );
        assertEquals( null, m_pkgLogger.getLevel() );
        assertHandlerType( handlers, true /* isConsoleHandler */ );

        // invalid directory and prefix: only package logger level set, no default handler/formatter created
        clearHandlers( m_pkgLogger ); 
        config = new LogConfiguration( Level.WARNING, "invalid directory?", "invalid prefix?", 
                m_simpleFormatterName );
        m_driver.setLogConfiguration( config );
        assertEquals( java.util.logging.Level.WARNING, m_pkgLogger.getLevel() );
        assertEquals( 0, m_pkgLogger.getHandlers().length );

        // invalid formatter class: handler created, formatter not set
        clearHandlers( m_pkgLogger );         
        config = new LogConfiguration( Level.WARNING, null, null, "invalid formatter class!" );
        m_driver.setLogConfiguration( config );
        handlers = m_pkgLogger.getHandlers();
        assertSingleHandlerAndSimpleFormatter( handlers );
        assertLoggerAndHandlerLevels( handlers, java.util.logging.Level.WARNING );
        assertHandlerType( handlers, true /* isConsoleHandler */ );
    }

	// Tests proper logging of messages into files
	@Test
    public void testFileLogging() throws Exception
	{	    
	    if ( true )
			return;
		
		// ensure no file is created when level set to OFF
	    clearHandlers( m_pkgLogger );
	    new SimpleFormatter();	// verify that class is available
        LogConfiguration config = new LogConfiguration( Level.OFF, m_logDirectory, 
                "OdaJdbcDriver", m_simpleFormatterName );
        m_driver.setLogConfiguration( config );
        assertEquals( 0, m_dir.listFiles().length );
        clearDirectory( m_dir );
	    
        // for each logger level, log messages from each level, 
	    // and ensure correct number of messages get logged
	    int[] loggerLevels = 		{ Level.SEVERE, Level.WARNING, Level.INFO, Level.CONFIG, Level.FINE, Level.FINER, Level.FINEST, Level.ALL };
	    int[] expectedLinesLogged = { 2,            4,             6,          8,            10,         12,          14,           16        };

	    for ( int i = 0; i < loggerLevels.length; i += 1 )
	    {
		    clearHandlers( m_pkgLogger );         
	        config = new LogConfiguration( loggerLevels[i], m_logDirectory, 
	                "OdaJdbcDriver", m_simpleFormatterName );
	        m_driver.setLogConfiguration( config );
	        
	        // parameter checking for package logger
	        Handler[] handlers = m_pkgLogger.getHandlers();   
	        assertSingleHandlerAndSimpleFormatter( handlers );
	        assertLoggerAndHandlerLevels( handlers, convertLogLevel( loggerLevels[i] ) ); 
	        assertHandlerType( handlers, false /* isConsoleHandler */ );
	        
	        // get logger for this class
	        Logger logger = Logger.getLogger( OdaJdbcDriver.class.getName() );
	        logMessageForAllLevels( logger );
	        handlers[0].close();
	      
	        // one log file & one lock file are generated
	        File[] files = m_dir.listFiles();
	        assertEquals( 2, files.length );
	        
	        // find the log file, ignore the lock file
	        File logFile;
	        if ( ! isLockFile( files[0] ) )
	            logFile = files[0];
	        else
	            logFile = files[1];
	           
	        // create file reader
	        BufferedReader fileReader = new BufferedReader( new FileReader( logFile ) );
	        int lineCount = 0;
	        String line = fileReader.readLine(); 
	        while ( line != null ) 
	        {
	            lineCount += 1;
	            line = fileReader.readLine();
	        }	        
	        fileReader.close();
	        assertEquals( expectedLinesLogged[i], lineCount );
	        clearDirectory( m_dir );
	    }

	}
	
    private void clearHandlers( Logger logger )
    {
        Handler[] handlers = logger.getHandlers();
        for ( int i = 0; i < handlers.length; i += 1 )
        {
            logger.removeHandler( handlers[i] );
        }
    }
    
    private void assertSingleHandlerAndSimpleFormatter( Handler[] handlers )
    {
        assertEquals( 1, handlers.length );
        if ( handlers.length < 1 )
            return;

        assertNotNull( handlers[0].getFormatter() );
        assertEquals( m_simpleFormatterName,
                handlers[0].getFormatter().getClass().getName() );
    }
    
    private void assertHandlerType( Handler[] handlers, boolean isConsoleHandler )
    {
        if( handlers.length < 1 )
        {
            fail( "Expecting at least one handler; found none." );
            return;
        }       
        
        if( isConsoleHandler == true )
            assertTrue( handlers[0] instanceof ConsoleHandler );
        else
            assertTrue( handlers[0] instanceof FileHandler );
    }

    private void assertLoggerAndHandlerLevels( Handler[] handlers, 
            java.util.logging.Level level )
    {
        if ( handlers.length < 1 )
            return;
        
        assertEquals( level, m_pkgLogger.getLevel() );
        assertEquals( level, handlers[0].getLevel() );
    }
    
    private void clearDirectory( File dir ) throws IOException
    {
        File[] files = dir.listFiles();
        for ( int i = 0; i < files.length; i += 1 )
        {
            boolean deleted = files[i].delete();
            if ( ! deleted )
                throw new IOException( "Cannot delete file: " + files[i].getName() ); 
        }
    }
    
    private java.util.logging.Level convertLogLevel( int level )
    {
        switch ( level )
        {
        case Level.ALL:
            return java.util.logging.Level.ALL;
        case Level.FINEST:
            return java.util.logging.Level.FINEST;
        case Level.FINER:
            return java.util.logging.Level.FINER;
        case Level.FINE:
            return java.util.logging.Level.FINE;
        case Level.CONFIG:
            return java.util.logging.Level.CONFIG;
        case Level.INFO:
            return java.util.logging.Level.INFO;
        case Level.WARNING:
            return java.util.logging.Level.WARNING;
        case Level.SEVERE:
            return java.util.logging.Level.SEVERE;
        case Level.OFF:
            return java.util.logging.Level.OFF;
        default:
            if( level > Level.SEVERE )
            {
                return java.util.logging.Level.OFF;
            }
            else
            {
            	fail( "No valid level specified." );
            	return null;
            }	
        }
    }
    
    private void logMessageForAllLevels( Logger logger )
    {
        logger.log( java.util.logging.Level.SEVERE, "SEVERE log message" );
        logger.log( java.util.logging.Level.WARNING, "WARNING log message" );
        logger.log( java.util.logging.Level.INFO, "INFO log message" );
        logger.log( java.util.logging.Level.CONFIG, "CONFIG log message" );
        logger.log( java.util.logging.Level.FINE, "FINE log message" );
        logger.log( java.util.logging.Level.FINER, "FINER log message" );
        logger.log( java.util.logging.Level.FINEST, "FINEST log message" );
        logger.log( java.util.logging.Level.ALL, "ALL log message" );
    }
    
    private boolean isLockFile( File file )
    {
        String fileName = file.getName();
        String fileExtension = fileName.substring( fileName.length() - 3 );
        return fileExtension.equals("lck");
    }
    
}
