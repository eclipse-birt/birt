/*
 *************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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
package org.eclipse.birt.data.engine.executor;

import java.util.Map;
import java.util.Properties;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.odaconsumer.Connection;
import org.eclipse.birt.data.engine.odaconsumer.ConnectionManager;
import org.eclipse.birt.data.engine.odi.ICandidateQuery;
import org.eclipse.birt.data.engine.odi.IDataSource;
import org.eclipse.birt.data.engine.odi.IDataSourceQuery;

/**
 * Implementation of ODI's IDataSource interface
 */
class DataSource implements IDataSource
{
    protected String 		driverName;
    protected Connection	odaConnection;
    protected Properties	connectionProps = new Properties();

    // not all data source can be cached, it depends on whether
    // odaConnection supports multiple use, this value indicates
    // cache count of an instance
    private int usedCount = 1;
    
    DataSource( String driverName )
    {
        this.driverName = driverName;
    }
    
    DataSource( String driverName, Map connProperties )
	{
    	assert driverName != null;
    	this.driverName = driverName;
    	if ( connProperties != null )
    		this.connectionProps.putAll( connProperties );
	}

    /**
     * @see org.eclipse.birt.data.engine.odi.IDataSource#getDriverName()
     */
    public String getDriverName()
    {
        return driverName;
    }

    /**
     * @see org.eclipse.birt.data.engine.odi.IDataSource#getProperties()
     */
    public Map getProperties()
    {
        return connectionProps;
    }

    /**
     * @see org.eclipse.birt.data.engine.odi.IDataSource#addProperty(java.lang.String, java.lang.String)
     */
    public void addProperty(String name, String value) throws DataException
    {
        // Cannot change connection properties if connection is open
        checkState( false );
        connectionProps.put( name, value );
    }

    /**
     * @see org.eclipse.birt.data.engine.odi.IDataSource#newQuery(java.lang.String, java.lang.String)
     */
    public IDataSourceQuery newQuery(String queryType, String queryText) throws DataException
    {
    	// Allow a query to be created on an unopened data source
        return new DataSourceQuery(this, queryType, queryText);
    }

    /**
     * @see org.eclipse.birt.data.engine.odi.IDataSource#newCandidateQuery()
     */
    public ICandidateQuery newCandidateQuery()
    {
       	// Allow a query to be created on an unopened data source
		return new CandidateQuery( );
    }

    /**
     * @see org.eclipse.birt.data.engine.odi.IDataSource#isOpen()
     */
    public boolean isOpen()
    {
        return odaConnection != null;
    }

    /**
     * @see org.eclipse.birt.data.engine.odi.IDataSource#open()
     */
    public void open() throws DataException
    {
        // No op if we are already open
        if ( odaConnection != null )
            return;
        
        // If no driver name is specified, this is an empty data source used soley for
        // processing candidate queries. Open() is a no-op.
        if ( driverName == null || driverName.length() == 0 )
        	return;
        
		ConnectionManager connManager = ConnectionManager.getInstance();
		odaConnection = connManager.openConnection( driverName, connectionProps );
    }

    /*
     * @see org.eclipse.birt.data.engine.odi.IDataSource#canBeCached(boolean)
     */
	public boolean canBeReused( boolean toUse ) throws DataException
	{
		if ( odaConnection != null )
		{
			if ( odaConnection.getMaxQueries( ) <= 0 )
			{
				return true;
			}
			else
			{
				// odaConnection.getMaxQueries( ) means this instance can be
				// used count.
				if ( usedCount <  odaConnection.getMaxQueries( ) )
				{
					if ( toUse )
						usedCount++;
					return true;
				}
			}
		}
		return false;
	}
    
    /**
     * Gets the Oda connection associated with this data source 
     */
    Connection getConnection()
    {
        assert isOpen();
        return odaConnection;
    }
    
    /**
     * @see org.eclipse.birt.data.engine.odi.IDataSource#close()
     */
    public void close()
    {
        // TODO: should also close all open DataSourceQuery

        if ( odaConnection == null )
            return;		// nothing to close

		try
		{		
            // initiates immediate release of resources
            odaConnection.close();	
		}
		catch ( DataException e ) 
		{
		    // ignore close exception
		}
        odaConnection = null;        
    }

    // Checks that 
    private void checkState( boolean open ) throws DataException
    {
        if ( ! open && odaConnection != null )
            throw new DataException( ResourceConstants.DS_HAS_OPENED );
        else if ( open && odaConnection == null )
            throw new DataException( ResourceConstants.DS_NOT_OPEN );
    }
}
