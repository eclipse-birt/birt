/*
 *************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
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

package org.eclipse.birt.data.oda.mongodb.impl;

import java.util.Properties;

import org.eclipse.birt.data.oda.mongodb.nls.Messages;
import org.eclipse.datatools.connectivity.oda.IConnection;
import org.eclipse.datatools.connectivity.oda.IDataSetMetaData;
import org.eclipse.datatools.connectivity.oda.IQuery;
import org.eclipse.datatools.connectivity.oda.OdaException;

import com.ibm.icu.util.ULocale;
import com.mongodb.CommandResult;
import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

/**
 * Implementation class of IConnection for the MongoDB ODA runtime driver.
 * Each connection is to a Mongo database for specific 
 * Mongo Connection URI plus additional options not definable in the URI.
 */
public class MDbConnection implements IConnection
{

    private DB m_mongoDbInstance;
    private boolean m_useRequestSession = false;    // default is false
        
	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#open(java.util.Properties)
	 */
	public void open( Properties connProperties ) throws OdaException
	{
        if( isOpen() )
            return;     // already open
        
        DB dbInstance = getMongoDatabase( connProperties );
       
        // no exception thrown thus far; accept the db instance for use
        m_mongoDbInstance = dbInstance;
        
        // check and apply the request session property setting
        Boolean useRequestSession = MongoDBDriver.getBooleanPropValue( connProperties, MongoDBDriver.REQUEST_SESSION_PROP );
        m_useRequestSession = useRequestSession != null ? 
                                useRequestSession.booleanValue() : false;   // reset to default
        if( m_mongoDbInstance != null )
        {
            // DB instance may be re-used from connection pool
            if( m_useRequestSession )
                m_mongoDbInstance.requestStart();   // starts a request session, if not already started
            else
                m_mongoDbInstance.requestDone();    // ends a request session, in case previously started but never closed
        }
 	}

	public static DB getMongoDatabase( Properties connProperties ) throws OdaException
	{
        Mongo mongoInstance = MongoDBDriver.getMongoNode( connProperties );
        if( ! mongoInstance.getConnector().isOpen() )
            throw new OdaException( Messages.mDbConnection_failedToOpenConn );
        
        // to avoid potential conflict in shared DB, ReadPreference is exposed
        // as cursorReadPreference in data set property
        
        String dbName = MongoDBDriver.getDatabaseName( connProperties );
        if( dbName == null || dbName.isEmpty() )
            throw new OdaException( Messages.mDbConnection_missingValueDBName );

        // validate whether dbName exists
        Boolean dbExists = existsDatabase( mongoInstance, dbName, connProperties );
        if( dbExists != null && !dbExists  )    // does not exist for sure
        {
            // do not proceed to create new database instance
             throw new OdaException( 
                     Messages.bind( Messages.mDbConnection_invalidDatabaseName, dbName )); 
        }

        DB dbInstance = mongoInstance.getDB( dbName );
        authenticateDB( dbInstance, connProperties );
        return dbInstance;	    
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#setAppContext(java.lang.Object)
	 */
	public void setAppContext( Object context ) throws OdaException
	{
	    // do nothing; no support for pass-through context
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#close()
	 */
	public void close() throws OdaException
	{
        if( m_useRequestSession && m_mongoDbInstance != null )
            m_mongoDbInstance.requestDone();

        m_mongoDbInstance = null;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#isOpen()
	 */
	public boolean isOpen() throws OdaException
	{
        return m_mongoDbInstance != null;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#getMetaData(java.lang.String)
	 */
	public IDataSetMetaData getMetaData( String dataSetType ) throws OdaException
	{
	    // this driver supports only one type of data set,
        // ignores the specified dataSetType
		return new DataSetMetaData( this );
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#newQuery(java.lang.String)
	 */
	public IQuery newQuery( String dataSetType ) throws OdaException
	{
        if( ! isOpen() )
            throw new OdaException( Messages.mDbConnection_noConnection );
        // this driver supports only one type of data set,
        // ignores the specified dataSetType
        return new MDbQuery( this );
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#getMaxQueries()
	 */
	public int getMaxQueries() throws OdaException
	{
		return 0;	// no limit
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#commit()
	 */
	public void commit() throws OdaException
	{
	    // do nothing; no transaction support needed
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#rollback()
	 */
	public void rollback() throws OdaException
	{
        // do nothing; no transaction support needed
	}

    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IConnection#setLocale(com.ibm.icu.util.ULocale)
     */
    public void setLocale( ULocale locale ) throws OdaException
    {
        // do nothing; no locale support
    }
    
    DB getConnectedDB()
    {
        return m_mongoDbInstance;
    }

    static void authenticateDB( DB mongoDb, Properties connProps )
        throws OdaException
    {
        if( mongoDb.isAuthenticated() )
            return;     // already authenticated
        
        String username = MongoDBDriver.getUserName( connProps );
        if( username == null || username.isEmpty() )
            return;     // nothing to authenticate
        
        String passwd = MongoDBDriver.getPassword( connProps );
        char[] passwdChars = passwd != null ? 
                passwd.toCharArray() : new char[0];

        CommandResult result = null;
        try
        {
            result = mongoDb.authenticateCommand( username, passwdChars );
        }
        catch( Exception ex )
        {
            OdaException odaEx = null;
            if( result != null )
            {
                odaEx = new OdaException( result.getErrorMessage() );
                odaEx.initCause( ex );
            }
            else
                odaEx = new OdaException( ex );
            
            MongoDBDriver.getLogger().info( 
                    Messages.bind( "Unable to authenticate user (${0}) in database (${1}).\n ${2}",  //$NON-NLS-1$
                            new Object[]{ username, mongoDb, odaEx.getCause().getMessage() } ));
            throw odaEx;
        }
    }

    private static Boolean existsDatabase( Mongo mongoInstance,
            String dbName, Properties connProps ) 
        throws OdaException
    {
        // check if user authentication is needed
        String username = MongoDBDriver.getUserName( connProps );
        if( username != null && ! username.isEmpty() )
        {
            DB adminDb = mongoInstance.getDB( "admin" ); //$NON-NLS-1$
            try
            {
                // login to admin db, so to get the existing database names
                authenticateDB( adminDb, connProps );
            }
            catch( OdaException ex )
            {
                // not able to determine if db exists; specified user is probably not a valid login user in admin db
                return null;    
            }
        }
        
        try
        {
            return mongoInstance.getDatabaseNames().contains( dbName );
        }
        catch( MongoException ex )
        {
            throw new OdaException( ex );   // unable to get db names
        }
    }

}
