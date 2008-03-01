/*
 *************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

import org.eclipse.birt.report.data.oda.jdbc.Statement;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.datatools.connectivity.IConnectionProfile;
import org.eclipse.datatools.connectivity.IManagedConnection;
import org.eclipse.datatools.connectivity.oda.IConnection;
import org.eclipse.datatools.connectivity.oda.IQuery;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.profile.OdaProfileExplorer;

/**
 * Extends the behavior of the oda.jdbc runtime driver to use a database connection profile.
 */
public class Connection extends org.eclipse.birt.report.data.oda.jdbc.Connection 
    implements IConnection
{
    private static final String JDBC_CONN_TYPE = "java.sql.Connection"; //$NON-NLS-1$

    private IConnectionProfile m_dbProfile;
    
	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#open(java.util.Properties)
	 */
	public void open( Properties connProperties ) throws OdaException
	{
	    // find and load the db profile defined in connection properties
        // TODO supports the use the appContext, if exists, in open
	    m_dbProfile = OdaProfileExplorer.getInstance()
	                    .getProfileByName( connProperties, null );
	    if( m_dbProfile != null )
	    {
	        // connect via the db profile
	        IStatus connectStatus = m_dbProfile.connect();
	        if( connectStatus.getSeverity() <= IStatus.INFO )
	        {
    	        IManagedConnection mgtConn =
    	            m_dbProfile.getManagedConnection( JDBC_CONN_TYPE );
    	        if( mgtConn != null )
    	        {
    	            org.eclipse.datatools.connectivity.IConnection connObj = 
    	                mgtConn.getConnection();
    	            if( connObj != null )
    	            {
        	            super.jdbcConn = (java.sql.Connection) connObj.getRawConnection();
    	            }
    	        }
	        }
	    }
	    
        // no DTP managed JDBC connection available, use local properties to connect
        if( super.jdbcConn == null )  
            super.open( connProperties );	    
 	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#setAppContext(java.lang.Object)
	 */
	public void setAppContext( Object context ) throws OdaException
	{
	    // TODO use the appContext in open
	    super.setAppContext( context );
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#close()
	 */
	public void close() throws OdaException
	{
        if( m_dbProfile != null )
        {
            m_dbProfile.disconnect();
            return;
        }
        
        super.close();
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
        // as this driver's same SQB data set supports both select and stored proc statements
        // TODO - check whether the query is a stored procedure, and instantiate CallStatement instead 
        return new Statement( super.jdbcConn );
    }

    /**
     * Returns the connection profile instance for this db connection.
     * @return 
     */
    protected IConnectionProfile getDbProfile()
    {
        return m_dbProfile;
    }
    
}
