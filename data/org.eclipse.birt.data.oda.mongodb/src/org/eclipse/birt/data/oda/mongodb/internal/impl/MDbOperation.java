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

package org.eclipse.birt.data.oda.mongodb.internal.impl;

import java.util.logging.Level;

import org.eclipse.birt.data.oda.mongodb.impl.MDbResultSet;
import org.eclipse.birt.data.oda.mongodb.impl.MDbResultSetMetaData;
import org.eclipse.birt.data.oda.mongodb.nls.Messages;
import org.eclipse.datatools.connectivity.oda.OdaException;

import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.Bytes;
import com.mongodb.CommandResult;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MapReduceOutput;
import com.mongodb.ReadPreference;

/**
 * Base class for MongoDB findQuery and command operations.
 * Delegated by QueryModel to handle its operations.
 */
public class MDbOperation
{
    private QueryModel m_model;
    
    private DBCollection m_queryCollection;
    private DBObject m_fieldsObj;
    private DBObject m_queryObj;
    private MDbResultSetMetaData m_rsMetaData;
    
    private static final MDbOperation sm_factory = new MDbOperation();
    
    static MDbOperation createQueryOperation( QueryModel queryModel )
    {
        if( queryModel == null || ! queryModel.isValid() )
            throw new IllegalArgumentException( "null/invalid QueryModel" ); //$NON-NLS-1$

        return queryModel.getQueryProperties().hasValidCommandOperation() ?
                sm_factory.new CommandOperation( queryModel ) :
                new MDbOperation( queryModel );        
    }
    
    private MDbOperation(){}

    private MDbOperation( QueryModel queryModel )
    {
        m_model = queryModel;      
    }

    protected QueryModel getModel()
    {
        return m_model;
    }

    protected void setResultSetMetaData( MDbResultSetMetaData rsmd )
    {
        m_rsMetaData = rsmd;
    }

    MDbResultSetMetaData getResultSetMetaData()
    {
        return m_rsMetaData;
    }

    protected void resetPreparedState()
    {
        m_queryCollection = null;
        m_fieldsObj = null;
        m_queryObj = null;
        m_rsMetaData = null;
    }

    protected void prepare( DBCollection dbCollection ) 
        throws OdaException
    {
        resetPreparedState();
        
        // get individual query components from data set properties
        QueryProperties queryProps = getModel().getQueryProperties();
        
        // handle findQueryExpr property
        DBObject queryObj = queryProps.getFindQueryExprAsParsedObject();

        if( queryObj == null )
            queryObj = new BasicDBObject();

        // specify fields to retrieve
        DBObject fieldsObj = queryProps.getSelectedFieldsAsProjectionKeys();

        try
        {
            // find search-limited rows to validate the queryObj and 
            // obtain result set metadata on the specified dbCollection
            DBCursor mdRowsCursor = dbCollection.find( queryObj, fieldsObj );

            // set data set query properties on DBCursor
            // to get result set metadata; no need to include sortExpr for getting metadata
            applyPropertiesToCursor( mdRowsCursor, queryProps, true, false );

            m_rsMetaData = new MDbResultSetMetaData( mdRowsCursor, queryProps.getSelectedFieldNames(),
                                    queryProps.isAutoFlattening() );

            // no exception; the find arguments on specified dbCollection are valid
            m_queryCollection = dbCollection;  // could be different, e.g. mapReduce output collection
            m_fieldsObj = fieldsObj;
            m_queryObj = queryObj;
        }
        catch( RuntimeException ex )
        {
            DriverUtil.getLogger().log( Level.SEVERE, "Encountered RuntimeException in QueryModel#prepareQuery(DBCollection).", ex ); //$NON-NLS-1$
            throw new OdaException( ex );
        }
    }
    
    protected MDbResultSet execute() throws OdaException
    {
        if( m_queryObj == null || m_queryCollection == null )
            throw new OdaException( Messages.mDbOp_invalidQueryExpr );

        try
        {
            DBCursor rowsCursor = m_queryCollection.find( m_queryObj, m_fieldsObj );

            // no search limit applies here; 
            // defer to MDbResultSet to set DBCursor#limit based on its maxRows
            applyPropertiesToCursor( rowsCursor, getModel().getQueryProperties(), false, true );

            return new MDbResultSet( rowsCursor, getResultSetMetaData(), getModel().getQueryProperties() );
        }
        catch( RuntimeException ex )
        {
            DriverUtil.getLogger().log( Level.SEVERE, "Encountered RuntimeException: ", ex ); //$NON-NLS-1$
            throw new OdaException( ex );
        }        
    }

    /* 
     * Applies data set query properties and hints on DBCursor.
     */
    private void applyPropertiesToCursor( DBCursor rowsCursor, QueryProperties queryProps, 
            boolean includeMetaDataSearchLimit, boolean includeSortExpr )
    {
        if( includeMetaDataSearchLimit )
        {
            Integer searchLimit = getModel().getEffectiveMDSearchLimit( queryProps ); 
            if( searchLimit > 0 )
                rowsCursor.limit( searchLimit );            
        }
        
        applyPropertiesToCursor( rowsCursor, queryProps, includeSortExpr );
    }

    /**
     * Applies data set query properties and hints on DBCursor, except
     * for cursor limit.
     * @see #applyPropertiesToCursor(DBCursor,QueryProperties,boolean,boolean)
     */
    static void applyPropertiesToCursor( DBCursor rowsCursor, QueryProperties queryProps, 
            boolean includeSortExpr )
    {
        if( includeSortExpr )   // normally done only when executing a query to get full result set
        {
            DBObject sortExprObj = null;
            try
            {
                sortExprObj = queryProps.getSortExprAsParsedObject();
            }
            catch( OdaException ex )
            {
                // log warning and ignore
                DriverUtil.getLogger().log( Level.WARNING, 
                        Messages.bind( "Unable to parse the user-defined Sort Expression: {0}", queryProps.getSortExpr() ),  //$NON-NLS-1$
                        ex );
            }

            if( sortExprObj != null )
                rowsCursor.sort( sortExprObj );
        }

        ReadPreference readPref = queryProps.getTaggableReadPreference();
        if( readPref != null )
            rowsCursor.setReadPreference( readPref );
  
        if( queryProps.getBatchSize() > 0 )
            rowsCursor.batchSize( queryProps.getBatchSize() );

        if( queryProps.getNumDocsToSkip() > 0 )
            rowsCursor.skip( queryProps.getNumDocsToSkip() );

        DBObject hintObj = queryProps.getIndexHintsAsParsedObject();
        if( hintObj != null )
            rowsCursor.hint( hintObj );
        else    // try to pass the hint string value as is
        {
            String hintValue = queryProps.getIndexHints();
            if( ! hintValue.isEmpty() )
                rowsCursor.hint( hintValue );
        }
        
        if( queryProps.hasNoTimeOut() )
            rowsCursor.addOption( Bytes.QUERYOPTION_NOTIMEOUT );
        if( queryProps.isPartialResultsOk() )
            rowsCursor.addOption( Bytes.QUERYOPTION_PARTIAL );
    }

    protected QueryProperties getEffectiveProperties()
    {
        return getModel().getQueryProperties();
    }

    /* 
     * Extends MDbOperation to handle MongoDB commands.
     */
    public class CommandOperation extends MDbOperation
    {
        private Iterable<DBObject> m_cmdResultObjs;
        private boolean m_hasOutputCollection;

        private CommandOperation( QueryModel queryModel )
        {
            super( queryModel );            
        }
        
        protected void resetPreparedState()
        {
            m_cmdResultObjs = null;
            m_hasOutputCollection = false;
            super.resetPreparedState();
        }

        protected void prepare( DBCollection dbCollection )
            throws OdaException
        {
            resetPreparedState();
            
            QueryProperties queryProps = getModel().getQueryProperties();
            QueryModel.validateCommandSyntax( queryProps.getOperationType(), queryProps.getOperationExpression() );

            // call the specified command
            Iterable<DBObject> cmdResults = null;
            if( queryProps.hasAggregateCommand() )
                cmdResults = callAggregateCmd( dbCollection, queryProps );
            else if( queryProps.hasRunCommand() )
                cmdResults = callDBCommand( getModel().getConnectedDB(), queryProps );
            else if( queryProps.hasMapReduceCommand() )
            {
                MapReduceOutput mapReduceOut = callMapReduceCmd( dbCollection, queryProps );
                DBCollection mapReduceCollection = mapReduceOut.getOutputCollection();
                if( mapReduceCollection != null )
                {
                    // run query on the output collection
                    super.prepare( mapReduceCollection );
                    m_hasOutputCollection = true;
                    return;
                }
                
                cmdResults = mapReduceOut.results();
            }

            if( cmdResults == null )
                return;
            
            setResultSetMetaData( new MDbResultSetMetaData( cmdResults, 
                    getModel().getEffectiveMDSearchLimit( queryProps ), 
                    queryProps.getSelectedFieldNames(),
                    queryProps.isAutoFlattening() ));
            
            // no exception thus far, ok to cache the command result objects
            m_cmdResultObjs = cmdResults;
        }

        protected MDbResultSet execute() throws OdaException
        {
            if( m_cmdResultObjs != null )
                return getCommandResults();
        
            return super.execute();     // default operation type
        }
        
        private MDbResultSet getCommandResults() throws OdaException
        { 
            if( m_cmdResultObjs == null )
                throw new OdaException( Messages.mDbOp_noCmdResults );

            return new MDbResultSet( m_cmdResultObjs.iterator(), 
                    getResultSetMetaData(), getModel().getQueryProperties() );        
        }

        protected QueryProperties getEffectiveProperties()
        {
            // Command operations do not apply the queryExpr and sortExpr, 
            // unless a MapReduce op has output to a collection
            QueryProperties queryProps = super.getEffectiveProperties();
            if( m_hasOutputCollection ) 
                return queryProps;

            // remove n/a queryExpr and sortExpr, if exists, from effective properties
            if( queryProps.getFindQueryExpr().isEmpty() &&
                    queryProps.getSortExpr().isEmpty() )
                return queryProps;  // nothing to remove
       
            QueryProperties effectiveProps = QueryProperties.copy( queryProps );
            effectiveProps.setFindQueryExpr( null );
            effectiveProps.setSortExpr( null );
            return effectiveProps;
        }
    }

    static Iterable<DBObject> callAggregateCmd( DBCollection dbCollection,
            QueryProperties queryProps ) throws OdaException
    {
        if( ! queryProps.hasAggregateCommand() )
            return null;
        DBObject operationExprObj = queryProps.getOperationExprAsParsedObject( true );
        if( operationExprObj == null )
            return null;
        
        // convert user-specified operation expression to operation pipeline
        DBObject firstOp = QueryProperties.getFirstObjectSet( operationExprObj );
        if( firstOp == null )
            return null;     // no valid DBObject operation

        DBObject[] addlOps = QueryProperties.getSecondaryObjectSets( operationExprObj );

        // aggregation $limit and $skip operators applies to the number 
        // of documents in the *input* pipeline, and thus cannot be used to apply
        // the searchLimit and numSkipDocuments properties defined for data set
        
        // $match and $sort pipeline operators are built in an aggregate command

        // execute the aggregate command
        AggregationOutput output;
        try
        {
            output = addlOps != null ?
                        dbCollection.aggregate( firstOp, addlOps ) :
                        dbCollection.aggregate( firstOp );
            output.getCommandResult().throwOnError();
            return output.results();
        }
        catch( RuntimeException ex )
        {
            OdaException odaEx = new OdaException( Messages.mDbOp_aggrCmdFailed );
            odaEx.initCause( ex );
            throw odaEx;
        }        
    }

    static MapReduceOutput callMapReduceCmd( DBCollection dbCollection,
            QueryProperties queryProps ) throws OdaException
    {
        if( ! queryProps.hasMapReduceCommand() )
            return null;
        DBObject command = queryProps.getOperationExprAsParsedObject( false );
        if( command == null )
            return null;
        
        // check if mapreduce key is already specified in user-defined expression
        DBObject mapReduceCmd;
        if( command.containsField( QueryModel.MAP_REDUCE_CMD_KEY ) || 
                command.containsField( QueryModel.MAP_REDUCE_CMD_KEY2 ) )
            mapReduceCmd = command;
        else
        {
            // add MapReduce input collection as first entry in command Map 
            mapReduceCmd = new BasicDBObject( QueryModel.MAP_REDUCE_CMD_KEY, dbCollection.getName() );
            mapReduceCmd.putAll( command );     // copy existing command entries
        }

        // mapReduce command's optional "limit" parameter applies to the number 
        // of documents in the *input* collection, and thus cannot be used to apply
        // the searchLimit property defined for data set
        
        // execute the mapreduce command
        MapReduceOutput output;
        try
        {
            output = dbCollection.mapReduce( mapReduceCmd );
            output.getCommandResult().throwOnError();
            return output;
        }
        catch( RuntimeException ex )
        {
            OdaException odaEx = new OdaException( 
                    Messages.bind( Messages.mDbOp_mapReduceCmdFailed, queryProps.getOperationExpression() ) );
            odaEx.initCause( ex );
            throw odaEx;
        }                
    }

    @SuppressWarnings("unchecked")
    static Iterable<DBObject> callDBCommand( DB connectedDB,
            QueryProperties queryProps ) throws OdaException
    {
        if( ! queryProps.hasRunCommand() )
            return null;
        DBObject command = queryProps.getOperationExprAsParsedObject( false );
        if( command == null )
            return null;
        
        try
        {
            CommandResult cmdResult = connectedDB.command( command );
            cmdResult.throwOnError();

            // wrap the commandResult DBObject in an Iterable
            BasicDBList resultList = new BasicDBList();
            resultList.add( cmdResult );
            Object resultObject = resultList;
            return (Iterable<DBObject>) resultObject;
        }
        catch( RuntimeException ex )
        {
            OdaException odaEx = new OdaException( 
                    Messages.bind( Messages.mDbOp_dbCmdFailed, queryProps.getOperationExpression() ) );
            odaEx.initCause( ex );
            throw odaEx;
        }
    }

}
