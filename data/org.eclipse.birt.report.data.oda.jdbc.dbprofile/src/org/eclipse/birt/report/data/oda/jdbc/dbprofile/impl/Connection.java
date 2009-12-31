/*
 *************************************************************************
 * Copyright (c) 2008, 2009 Actuate Corporation.
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

package org.eclipse.birt.report.data.oda.jdbc.dbprofile.impl;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.data.oda.jdbc.dbprofile.nls.Messages;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.datatools.connectivity.IConnectionProfile;
import org.eclipse.datatools.connectivity.IManagedConnection;
import org.eclipse.datatools.connectivity.oda.IConnection;
import org.eclipse.datatools.connectivity.oda.IQuery;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.profile.OdaProfileExplorer;
import org.eclipse.datatools.connectivity.oda.profile.internal.OdaConnectionProfile;
import org.eclipse.datatools.connectivity.oda.profile.internal.OdaProfileFactory;

/**
 * Extends the behavior of the oda.jdbc runtime driver to use a database connection profile.
 */
@SuppressWarnings("restriction")
public class Connection extends org.eclipse.birt.report.data.oda.jdbc.Connection 
    implements IConnection
{
    protected static final String SQB_DATA_SET_TYPE = "org.eclipse.birt.report.data.oda.jdbc.dbprofile.sqbDataSet"; //$NON-NLS-1$
    private static final String JDBC_CONN_TYPE = "java.sql.Connection"; //$NON-NLS-1$

    private static final String CLASS_NAME = Connection.class.getName();
    private static final Logger sm_logger = Logger.getLogger( CLASS_NAME );

    private IConnectionProfile m_dbProfile;
    
	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#open(java.util.Properties)
	 */
	public void open( Properties connProperties ) throws OdaException
	{
	    OdaException originalEx = null;
	    try
        {
	        // find and load the db profile defined in connection properties
	        IConnectionProfile dbProfile = getProfile( connProperties );
            open( dbProfile );
        }
        catch( OdaException ex )
        {
            // log warning with connect status
            sm_logger.logp( Level.WARNING, CLASS_NAME, "open(Properties)",  //$NON-NLS-1$
                    Messages.connection_openFailed, ex );
            originalEx = ex;
        }
        
        // check if no DTP managed JDBC connection available, try use local properties to connect
        try
        {
            if( ! isOpen() )  
                openJdbcConnection( connProperties );
        }
        catch( OdaException ex )
        {
            // not able to open with local properties; throw the original exception if exists
            if( originalEx != null )
                throw originalEx;
            throw new OdaException( Messages.connection_openFailed );
        }
	}
	
	protected IConnectionProfile getProfile( Properties connProperties ) 
	    throws OdaException
	{
	    return loadProfileFromProperties( connProperties );
	}
	
	protected void openJdbcConnection( Properties profileProperties ) throws OdaException
	{
        // TODO - adapt db profile properties to oda.jdbc properties
        super.open( profileProperties );
	}
	
	/**
	 * Internal method to open a connection based on the specified database connection profile.
	 * @param dbProfile
	 * @throws OdaException
	 */
	public void open( IConnectionProfile dbProfile ) throws OdaException
	{
	    super.jdbcConn = null;
	    m_dbProfile = dbProfile;
	    if( m_dbProfile == null )
	        throw new OdaException( Messages.connection_nullProfile );

        if( m_dbProfile.getConnectionState() != IConnectionProfile.CONNECTED_STATE )
        {
	        // connect via the db profile
	        IStatus connectStatus = m_dbProfile.connect();
	        
            if( connectStatus == null || 
                connectStatus.getSeverity() > IStatus.INFO )
                throw new OdaException( getStatusException( connectStatus ));
        }
        
        super.jdbcConn = getJDBCConnection( m_dbProfile );
 	}

    private java.sql.Connection getJDBCConnection( IConnectionProfile dbProfile )
    {
        if( dbProfile == null )
            return null;
        
        IManagedConnection mgtConn = dbProfile.getManagedConnection( JDBC_CONN_TYPE );
        if( mgtConn == null )
            return null;

        org.eclipse.datatools.connectivity.IConnection connObj = mgtConn.getConnection();
        if( connObj == null ) 
            return null;

        java.sql.Connection jdbcConn = (java.sql.Connection) connObj.getRawConnection();
        return jdbcConn;
    }
    
    /**
     * Returns a connection profile based on the specified connection properties.
     * If a profile store file is specified, load the referenced profile instance from the profile store.
     * Otherwise, create a transient profile instance if profile base properties are available.
     * @param connProperties
     * @return  the loaded connection profile; may be null if properties are invalid or insufficient
     * @throws OdaException 
     */
    public static IConnectionProfile loadProfileFromProperties( Properties connProperties ) 
        throws OdaException
    {
        // find and load the db profile defined in connection properties;
        // note: driver class path specified in appContext, if exists, is not relevant
        // when connecting with the properties defined in a connection profile instance
        // (i.e. a profile instance uses its own jarList property)
        IConnectionProfile dbProfile =  OdaProfileExplorer.getInstance()
                                            .getProfileByName( connProperties, null );
        if( dbProfile != null )
            return dbProfile;   // found referenced external profile instance

        // no external profile instance is specified or available;
        // try create a transient profile if the connection properties contains profile properties
        return createTransientProfile( connProperties );
    }
    
    private static IConnectionProfile createTransientProfile( Properties connProperties ) 
        throws OdaException
    {
        Properties profileProps = PropertyAdapter.adaptToDbProfilePropertyNames( connProperties );
        
        return OdaProfileFactory.createTransientProfile( profileProps );
    }
    
	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#close()
	 */
	public void close() throws OdaException
	{
        if( m_dbProfile != null )
        {
            closeProfile( m_dbProfile );
            m_dbProfile = null;
            super.jdbcConn = null;
            return;
        }
        
        super.close();
	}

	/**
	 * Close the specified connection profile.
	 * @param dbProfile
	 * @deprecated     As of 2.5.2, replaced by {@link #closeProfile(IConnectionProfile)}
	 */
	protected static void close( IConnectionProfile dbProfile )
	{
	    closeProfile( dbProfile );
	}
	
	/**
	 * Utility method to close the specified connection profile.
	 * @param connProfile
	 * @since 2.5.2
	 */
	public static void closeProfile( IConnectionProfile connProfile )
	{
        if( connProfile == null )
            return;     // nothing to close
        
        if( connProfile instanceof OdaConnectionProfile )
            ((OdaConnectionProfile)connProfile).close();
        else
            connProfile.disconnect( null );   // does nothing if already disconnected
	}
	
	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#isOpen()
	 */
	public boolean isOpen() throws OdaException
	{
        if( m_dbProfile != null )
            return ( m_dbProfile.getConnectionState() == IConnectionProfile.CONNECTED_STATE );

        return super.isOpen();
	}

    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IConnection#newQuery(java.lang.String)
     */
    public IQuery newQuery( String dataSetType ) throws OdaException
    {
        // ignores the specified dataSetType, 
        // as this driver currently supports only one data set type, and
        // the SQB data set type supports Select statements only
        return new DBProfileStatement( getRawConnection() );
    }

    /**
     * Returns the connection profile instance for this db connection.
     * @return 
     */
    protected IConnectionProfile getDbProfile()
    {
        return m_dbProfile;
    }

    protected java.sql.Connection getRawConnection()
    {
        return super.jdbcConn;
    }
    
    /**
     * Internal method to collect the first exception from the specified status.
     * @param status    may be null
     */
    public static Throwable getStatusException( IStatus status )
    {
        if( status == null )
            return null;
        Throwable ex = status.getException( );
        if( ex != null )
            return ex;

        // find first exception from its children
        IStatus[] childrenStatus = status.getChildren();
        for( int i=0; i < childrenStatus.length && ex == null; i++ )
        {
            ex = childrenStatus[i].getException( );
        }
        return ex;
    }
    
}
