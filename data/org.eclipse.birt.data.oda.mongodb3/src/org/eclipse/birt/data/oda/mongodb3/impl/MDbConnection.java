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

package org.eclipse.birt.data.oda.mongodb3.impl;

import java.util.Properties;
import java.util.logging.Level;

import org.bson.Document;
import org.eclipse.birt.data.oda.mongodb3.nls.Messages;
import org.eclipse.datatools.connectivity.oda.IConnection;
import org.eclipse.datatools.connectivity.oda.IDataSetMetaData;
import org.eclipse.datatools.connectivity.oda.IQuery;
import org.eclipse.datatools.connectivity.oda.OdaException;

import com.ibm.icu.util.ULocale;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;

/**
 * Implementation class of IConnection for the MongoDB ODA runtime driver.
 * Each connection is to a Mongo database for specific 
 * Mongo Connection URI plus additional options not definable in the URI.
 */
public class MDbConnection implements IConnection
{

    private MongoDatabase m_mongoDbInstance;
        
	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#open(java.util.Properties)
	 */
	public void open( Properties connProperties ) throws OdaException
	{
        if( isOpen() )
            return;     // already open
        
        MongoDatabase dbInstance = getMongoDatabase( connProperties );
       
        // no exception thrown thus far; accept the db instance for use
        m_mongoDbInstance = dbInstance;
        
 	}

	public static MongoDatabase getMongoDatabase( Properties connProperties ) throws OdaException
	{
        MongoClient mongoClient = MongoDBDriver.getMongoNode( connProperties );
         // to avoid potential conflict in shared DB, ReadPreference is exposed
        // as cursorReadPreference in data set property
        
        String dbName = MongoDBDriver.getDatabaseName( connProperties );
        if( dbName == null || dbName.isEmpty() )
            throw new OdaException( Messages.mDbConnection_missingValueDBName );
        MongoDatabase dbInstance = null;
        try {
	        Boolean dbExists = existsDatabase( mongoClient, dbName, connProperties );
	        if( dbExists != null && !dbExists  )    // does not exist for sure
	        {
	            // do not proceed to create new database instance
	             throw new OdaException( 
	                     Messages.bind( Messages.mDbConnection_invalidDatabaseName, dbName )); 
	        }
	
	        dbInstance = mongoClient.getDatabase( dbName );
	        authenticateDB( dbInstance, connProperties );
        } catch (Exception ex) {
        	   MongoDBDriver.getLogger().log(Level.SEVERE, "Unable to get Database " + dbName + ". " + ex.getMessage(), ex);
        	   throw new OdaException(ex);
        }
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
    
    MongoDatabase getConnectedDB()
    {
        return m_mongoDbInstance;
    }

    static void authenticateDB( MongoDatabase mongoDb, Properties connProps )
        throws OdaException
    {
        
        try
        {
        	mongoDb.runCommand(new Document("ping", 1));        }
        catch( Exception ex )
        {
            OdaException odaEx = new OdaException( ex );
            String username = MongoDBDriver.getUserName( connProps );
            //String dbName = MongoDBDriver.getDatabaseName( connProps );
            
            MongoDBDriver.getLogger().info( 
                    Messages.bind( "Unable to authenticate user (${0}) in database (${1}).\n ${2}",  //$NON-NLS-1$
                            new Object[]{ username, mongoDb, odaEx.getCause().getMessage() } ));
            throw odaEx;
        }
    }

    private static Boolean existsDatabase( MongoClient mongoClient,
            String dbName, Properties connProps ) 
        throws OdaException
    {
        if (dbName == null) {
        	return false;
        }
        try
        {
    		MongoIterable<String> databaseNameIterable = mongoClient.listDatabaseNames();
    		for (String databaseName : databaseNameIterable) {
    			if (dbName.equals(databaseName)) {
    				return true;
    			}
    		}        	
        }
        catch( MongoException ex )
        {
        	MongoDBDriver.getLogger().log(Level.SEVERE,"Unable to get listDatabaseNames", ex );   // unable to get db names
            // user may not have permission for listDatabaseName, return true, let the getDatabase() handle it.
        }
        return true;
    }
}
