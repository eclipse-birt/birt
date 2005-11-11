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
package org.eclipse.birt.data.engine.executor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.odaconsumer.Connection;
import org.eclipse.birt.data.engine.odaconsumer.ConnectionManager;
import org.eclipse.birt.data.engine.odaconsumer.PreparedStatement;
import org.eclipse.birt.data.engine.odi.ICandidateQuery;
import org.eclipse.birt.data.engine.odi.IDataSource;
import org.eclipse.birt.data.engine.odi.IDataSourceQuery;

/**
 * Implementation of ODI's IDataSource interface
 */
class DataSource implements IDataSource
{
	private static String className = DataSource.class.getName();
	private static Logger logger = Logger.getLogger( className ); 
    protected String 		driverName;
    
    // Information about an open oda connection 
    static private final class OpenConnection
	{
    	Connection connection;
    	int maxStatements = Integer.MAX_VALUE;		// max # of supported concurrent statements
    	int currentStatements = 0;	// # of currently active statements
	};
	
    // A pool of open odaconsumer.Connection. Since each connection may support a limited
	// # of statements, we may need to use more than one connection to handle concurrent statements
	// This is a set of OpenConnection
	private HashSet odaConnections = new HashSet();
	
	// Currently active oda Statements. This is a map from PreparedStatement to OpenConnection
	private HashMap statementMap = new HashMap();
	
    protected Properties	connectionProps = new Properties();
    private Map appContext;

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

	/*
	 * @see org.eclipse.birt.data.engine.odi.IDataSource#setAppContext(java.util.Map)
	 */
	public void setAppContext( Map context ) throws DataException
	{
	    appContext = context;
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
        return odaConnections.size() > 0;
    }

    /**
     * @see org.eclipse.birt.data.engine.odi.IDataSource#open()
     */
    public void open() throws DataException
    {
        // No op if we are already open
        if ( isOpen() )
            return;
        
        // If no driver name is specified, this is an empty data source used soley for
        // processing candidate queries. Open() is a no-op.
        if ( driverName == null || driverName.length() == 0 )
        	return;
        
        // Create first open connection
        newConnection();
    }
    
    /** Opens a new Connection and add it to the pool */
    private OpenConnection newConnection() throws DataException
    {
    	OpenConnection conn = new OpenConnection();
    	conn.connection = ConnectionManager.getInstance().openConnection( 
    			driverName, connectionProps, appContext );
    	int max = conn.connection.getMaxQueries();
    	if ( max != 0 )		//	0 means no limit
    		conn.maxStatements = max;
    	this.odaConnections.add( conn );
    	return conn;
    }
    
    /** 
     * Find a connection available for new statements in the pool, or create
     * a new one if none available 
     */
    private OpenConnection getAvailableConnection() throws DataException
	{
    	Iterator it = odaConnections.iterator();
    	while ( it.hasNext() )
    	{
    		OpenConnection c = (OpenConnection) (it.next());
    		if ( c.currentStatements < c.maxStatements )
    			return c;
    	}
    	
    	// No more available connections; create a new one
    	return newConnection();
	}

    /**
     * Prepares an ODA Statement. May use an existing Connection from the pool
     * which has free active statements, or a new connection if all connections
     * in pool have readed their maximum active statements.
     * Returned PreparedStatement must be closed by calling closeStatement.
     */
    synchronized PreparedStatement prepareStatement ( String queryText, String dataSetType )
    	throws DataException
    {
        assert isOpen();
        OpenConnection conn = getAvailableConnection();
        assert conn.currentStatements < conn.maxStatements;
        ++ conn.currentStatements;
        PreparedStatement stmt = conn.connection.prepareStatement( queryText, dataSetType );
        
        // Map statement to the open connection, so we can release the connection
        // when statement is closed
        this.statementMap.put( stmt, conn );
        return stmt;
    }
    
    /**
     * Closes a PreparedStatement returned by the prepareStatement call. Frees the associated
     * ODA Connection and make it available for new statements. 
     */
    synchronized void closeStatement ( PreparedStatement stmt )
    {
    	assert stmt != null;
    	// Find the associated connection
    	OpenConnection conn = (OpenConnection) statementMap.remove( stmt );
    	if ( conn == null )
    	{
    		// unexpected error: stmt not created by us
    		logger.logp( Level.WARNING, className, "closeStatement",
    				"statement not found");
    		// Fall through and call close() on stmt any way
    	}
    	else
    	{
    		-- conn.currentStatements;
    		if ( conn.currentStatements < 0 )
        		logger.warning( DataSource.class.getName() + ".closeStatement: negative statement count for connection.");
    		
    		// TODO: consider releasing connections here if we have more than 1 free connections
    	}
    	
    	try
		{
    		stmt.close();
		}
        catch ( DataException e )
        {
    		logger.logp( Level.FINE, className, "closeStatement",
    					"Exception at PreparedStatement.close()", e );
        }
    }
    
    
    /**
     * @see org.eclipse.birt.data.engine.odi.IDataSource#close()
     */
    public void close()
    {
    	// At this time all statements should have been closed
    	// Any remaining statement indicate resource leak, or obnormal shtudown
    	if ( statementMap.size() > 0 )
    	{
    		logger.logp( Level.WARNING, className, "close",
    				statementMap.size() + " statements still active.");

    		statementMap.clear();
    	}


    	// Close all open connections
    	Iterator it = odaConnections.iterator();
    	while ( it.hasNext() )
    	{
    		OpenConnection c = (OpenConnection) (it.next());
    		
    		try
			{
    			c.connection.close();
			}
    		catch ( DataException e ) 
			{
    			logger.logp( Level.FINE, className, "close",
    				 "Exception at Connection.close()", e );
			}
    	}
        odaConnections.clear();        
    }

    // Checks the open state 
    private void checkState( boolean checkOpen ) throws DataException
    {
        if ( ! checkOpen && isOpen() )
            throw new DataException( ResourceConstants.DS_HAS_OPENED );
        else if ( checkOpen && ! isOpen() )
            throw new DataException( ResourceConstants.DS_NOT_OPEN );
    }
    
    public void finalize()
    {
    	// Makes sure no connection is leaked
    	if ( isOpen() )
    	{
    		close();
    	}
    }
   
}
