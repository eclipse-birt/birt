/*
 *************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation - initial API and implementation
 *  
 *************************************************************************
 */

package org.eclipse.birt.report.data.oda.jdbc;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.eclipse.birt.report.data.oda.i18n.JdbcResourceHandle;
import org.eclipse.birt.report.data.oda.i18n.ResourceConstants;
import org.eclipse.datatools.connectivity.oda.OdaException;

import com.ibm.icu.text.MessageFormat;
import com.ibm.icu.util.ULocale;

/**
 * Internal implementation class for JNDI Data Source connection factory.
 * This supports the use of a JNDI Name Service to look up a Data Source 
 * resource factory to get a JDBC pooled connection. 
 * <p> 
 * A new connection property for the JNDI name to look up a Data Source name service 
 * is added to the ODA JDBC data source definition.  
 * This optional property expects the full URL path, for use by a 
 * JNDI initial context to look up a Data Source resource factory.  
 * A JNDI name URL path is specific to individual JNDI service provider.  
 * For example, "java:comp/env/jdbc/<dataSourceName>" for Tomcat.
 * <p>
 * A text field is also added to the oda.jdbc.ui data source designer pages 
 * for user input of the JNDI Data Source Name URL property value.
 * <p>  
 * Some JNDI service providers do not support client-side access.<br>
 * During design time when using the BIRT report designer, a JDBC data set 
 * would need to be designed using direct access to a JDBC driver connection.  
 * The ODA JDBC data set query builder continues to use direct JDBC connection to 
 * obtain its metadata.  Only those oda.jdbc.ui functions directly related to a 
 * data source design, such as Test Connection and Preview Results of a data set, 
 * are enhanced to use this factory to first attempt to use a JNDI Name URL, if specified.  
 * And if not successful for any reason, it falls back to use the JDBC driver URL.
 * <p>
 * Similarly at report runtime, such as during Report Preview, when a non-blank 
 * JNDI Name URL value is specified, the oda.jdbc run-time driver attempts to look up 
 * its JNDI data source name service to get a pooled JDBC connection.  
 * If such lookup is not successful for any reason, it falls back to use the 
 * JDBC driver URL directly to create a JDBC connection.
 * <p>
 * To simplify the task of setting up the JNDI initial context environment 
 * required by individual JNDI application, the oda.jdbc JNDI feature supports 
 * the use of a "jndi.properties" file, installed in the drivers sub-folder 
 * of the oda.jdbc plugin.  
 * When deployed within a web application, it looks for the file in the 
 * web application's folder tree for the oda.jdbc's drivers sub-folder.
 * <br>Its use is optional.  When such file is not found or problem reading from it, 
 * an initial context adopts the default behavior to locate any JNDI resource files, 
 * as defined by <code>javax.naming.Context</code>.
 * Future enhancement may support the use of a driver-specific resource file.
 * <p>
 * Additional note: <br>
 * when a custom connection factory is associated with a specific JDBC driver class, 
 * by implementating the oda.jdbc.driverinfo extension point, it is responsible 
 * for the handling of the JNDI look up service, as appropriate.  
 * In other words, a custom connection factory defined in a driverinfo extension 
 * overrides the default JNDI URL handling.
 */
class JndiDataSource implements IConnectionFactory
{
    private static final String JNDI_PROPERTIES = "jndi.properties";    //$NON-NLS-1$
    
    private static final Logger sm_logger = Logger.getLogger( JndiDataSource.class.getName() );
    private static final String sm_sourceClass = "JndiDataSource";  //$NON-NLS-1$
    private static JdbcResourceHandle sm_resourceHandle;
    
    /* (non-Javadoc)
     * @see org.eclipse.birt.report.data.oda.jdbc.IConnectionFactory#getConnection(java.lang.String, java.lang.String, java.util.Properties)
     */
    public Connection getConnection( String driverClass, String jndiNameUrl, 
                                    Properties connectionProperties ) 
        throws SQLException
    {
        final String methodName = "getConnection"; //$NON-NLS-1$
        sm_logger.entering( sm_sourceClass, methodName, jndiNameUrl );
        
        // perform JNDI lookup to obtain resource manager connection factory
        Context initCtx = null;
        Object namedObject = null;
        try
        {
            initCtx = new InitialContext( getDriverJndiProperties() );
            namedObject = initCtx.lookup( jndiNameUrl );
        }
        catch( Exception ex )
        {
            sm_logger.info( ex.toString() );
            
            SQLException sqlEx = new SQLException( ex.getLocalizedMessage() );
            sqlEx.initCause( ex );
            throw sqlEx;
        }
        finally
        {
            closeContext( initCtx );
        }

        // check if specified url's object is of a DataSource type
        validateDataSourceType( namedObject, jndiNameUrl );
        
        // obtain a java.sql.Connection resource from the data source pool
        Connection conn = getDataSourceConnection( (DataSource) namedObject, 
                                connectionProperties );
        
        sm_logger.exiting( sm_sourceClass, methodName, conn );
        return conn;
    }

    /**
     * Invoke factory to obtain a java.sql.Connection resource 
     * from the data source pool.
     */
    private Connection getDataSourceConnection( DataSource ds, Properties connProps )
        throws SQLException
    {
        // check if specified connection properties contain user authentication properties
        String username = connProps.getProperty( JDBCDriverManager.JDBC_USER_PROP_NAME );
        String password = connProps.getProperty( JDBCDriverManager.JDBC_PASSWORD_PROP_NAME );
        
        if( username != null )  // user name is explicitly specified
        {
            if( sm_logger.isLoggable( Level.FINER ) )
                sm_logger.finer( "getDataSourceConnection: using getConnection( username, password ) from data source pool." ); //$NON-NLS-1$
            
            Connection conn = null;
            try
            {
                conn = ds.getConnection( username, password );
            }
            catch( SQLException ex )
            {
                sm_logger.info( ex.toString() );
            }
            catch( UnsupportedOperationException unEx )
            {
                sm_logger.fine( unEx.toString() );
            }
            
            if( conn != null )  // successful
                return conn;    // done
            // else try again below without explicit username and password
        }
            
        if( sm_logger.isLoggable( Level.FINER ) )
            sm_logger.finer( "getDataSourceConnection: using getConnection() from data source pool." ); //$NON-NLS-1$

        return ds.getConnection();     
    }

    /**
     * Validate whether specified url's object is of a DataSource type.
     * @throws SQLException if unexpected resource type is found
     */
    private void validateDataSourceType( Object namedObject, String jndiNameUrl )
        throws SQLException
    {
        if( namedObject != null && namedObject instanceof DataSource )
            return;     // is of expected resource type

        // TODO - externalize new message when PII checkin is re-opened
        final String newMsg = "Found JNDI resource type ({0}); expecting javax.sql.DataSource type.";
        String newMsgText = formatMessage( newMsg, 
                ( namedObject != null ) ? 
                    namedObject.getClass().getName() : "null" ); //$NON-NLS-1$
        String localizedMsg = getMessage( ResourceConstants.CONN_GET_ERROR );
        String localizedMsgText = formatMessage( localizedMsg, jndiNameUrl );
        
        if( sm_logger.isLoggable( Level.INFO ) )
            sm_logger.info( localizedMsg + ". " + newMsgText ); //$NON-NLS-1$
        throw new SQLException( localizedMsgText );
    }
    
    private void closeContext( Context ctx )
    {
        if( ctx == null )
            return;     // nothing to close
        
        try
        {
            ctx.close();
        }
        catch( Exception e )
        {
            // log and ignore exception
            if( sm_logger.isLoggable( Level.INFO ) )
                sm_logger.info( "closeContext(): " + e.toString() ); //$NON-NLS-1$
        }
    }
    
    /**
     * Obtains the JNDI initial context environment properties.
     * @return  the jndi properties specified in the plugin drivers sub-directory;
     *          may return null if no such file exists or have problem reading file
     */
    protected Properties getDriverJndiProperties()
    {
        File jndiPropFile = getDriverJndiPropertyFile();
        if( jndiPropFile == null )      // no readable properties file found
            return null;
        
        Properties jndiProps = new Properties();
        try
        {
            jndiProps.load( new FileInputStream( jndiPropFile ) );
        }
        catch( Exception ex )
        {
            // log and ignore exception
            if( sm_logger.isLoggable( Level.INFO ) )
                sm_logger.info( "getDriverJndiProperties(): " + ex.toString() ); //$NON-NLS-1$
            jndiProps = null;
        }
        
        return jndiProps;
    }
    
    /**
     * Finds and returns the file representation of the jndi.properties file
     * in the oda.jdbc plugin's drivers sub-directory.
     * Validates that the file exists and readable.
     * @return  the jndi.properties file that is readable and exists
     *          in the drivers sub-directory
     */
    protected File getDriverJndiPropertyFile()
    {
        final String methodName = "getDriverJndiPropertyFile() "; //$NON-NLS-1$
        File driversDir = null;
        try
        {
            driversDir = OdaJdbcDriver.getDriverDirectory();
        }
        catch( OdaException ex )
        {
            // log and ignore exception
            sm_logger.info( methodName + ex.toString() );
        }
        catch( IOException ioEx )
        {
            // log and ignore exception
            sm_logger.info( methodName + ioEx.toString() );
        }
        
        if( driversDir == null || ! driversDir.isDirectory() )
            return null;
        
        // jndi properties file in bundle's drivers sub-directory
        
        // TODO - add support of driver-specific property file name
        File jndiPropFile = new File( driversDir, JNDI_PROPERTIES );
        
        boolean isValidFile = false;
        try
        {
            isValidFile = ( jndiPropFile.isFile() && jndiPropFile.canRead() );
        }
        catch( SecurityException e )
        {
            // log and ignore exception
            sm_logger.info( methodName + e.toString() );
        }
        
        return isValidFile ? jndiPropFile : null;
    }
    
    /**
     * Interim implementation until new user message can be externalized.
     */
    private String getMessage( String errorCode )
    {
        if( sm_resourceHandle == null )
            sm_resourceHandle = new JdbcResourceHandle( ULocale.getDefault() );

        return sm_resourceHandle.getMessage( errorCode );
    }

    private String formatMessage( String msgText, String argument )
    {
        return MessageFormat.format( msgText, new Object[]{ argument } );
    }
    
}
