/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.data.oda.jdbc.dbprofile.ui.internal.sqb;

import java.sql.Types;
import java.util.Iterator;

import org.eclipse.birt.report.data.oda.jdbc.dbprofile.impl.Driver;
import org.eclipse.datatools.connectivity.IConnectionProfile;
import org.eclipse.datatools.connectivity.oda.IConnection;
import org.eclipse.datatools.connectivity.oda.IDriver;
import org.eclipse.datatools.connectivity.oda.IParameterMetaData;
import org.eclipse.datatools.connectivity.oda.IQuery;
import org.eclipse.datatools.connectivity.oda.IResultSetMetaData;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.design.DataSetDesign;
import org.eclipse.datatools.connectivity.oda.design.DataSetParameters;
import org.eclipse.datatools.connectivity.oda.design.DesignFactory;
import org.eclipse.datatools.connectivity.oda.design.ParameterDefinition;
import org.eclipse.datatools.connectivity.oda.design.ParameterMode;
import org.eclipse.datatools.connectivity.oda.design.ResultSetColumns;
import org.eclipse.datatools.connectivity.oda.design.ResultSetDefinition;
import org.eclipse.datatools.connectivity.oda.design.ui.designsession.DesignSessionUtil;
import org.eclipse.datatools.modelbase.sql.query.QueryStatement;
import org.eclipse.datatools.modelbase.sql.query.helper.StatementHelper;

/**
 * An utility class to process the metadata of a SQL query statement.
 */
public class SQLQueryUtility
{
    public static void updateDataSetDesign( DataSetDesign dataSetDesign, 
            QueryStatement queryStmt, IConnectionProfile connProfile )
    {
        // first update design with raw SQL query text ready for prepare by a JDBC driver; 
        // any extra stuff in QueryStatement, such as comments are preserved in designer state
        dataSetDesign.setQueryText( getSQLForPrepare( queryStmt ) );

        // obtain query's current runtime metadata, and maps it to the dataSetDesign
        IConnection customConn = null;
        try
        {
            // instantiate the custom ODA runtime driver class
            IDriver customDriver = new Driver();
            
            // obtain and open a live connection
            customConn = customDriver.getConnection( null );
            java.util.Properties connProps = 
                DesignSessionUtil.getEffectiveDataSourceProperties( 
                        dataSetDesign.getDataSourceDesign() );
            customConn.open( connProps );

            // update the data set design with the 
            // query's current runtime metadata
            updateDesign( dataSetDesign, customConn, queryStmt );
        }
        catch( OdaException e )
        {
            // not able to get current metadata, reset previous derived metadata
            dataSetDesign.setResultSets( null );
            dataSetDesign.setParameters( null );
            
            e.printStackTrace();
        }
        finally
        {
            closeConnection( customConn );
        }
    }

    /**
     * Updates the given dataSetDesign with the queryText and its derived metadata
     * obtained from the ODA runtime connection.
     */
    private static void updateDesign( DataSetDesign dataSetDesign,
                               IConnection conn, QueryStatement queryStmt )
        throws OdaException
    {
        IQuery query = conn.newQuery( null );
        query.prepare( getSQLForPrepare( queryStmt ) );
                
        try
        {
            IResultSetMetaData md = query.getMetaData();
            updateResultSetDesign( md, dataSetDesign );
        }
        catch( OdaException e )
        {
            // no result set definition available, reset previous derived metadata
            dataSetDesign.setResultSets( null );
            e.printStackTrace();
        }
        
        // proceed to get parameter design definition
        // TODO update parameter definition with metadata from paramVariables
//        List paramVariables = StatementHelper.getAllVariablesInQueryStatement( queryStmt );
//        List paramVariables = StatementHelper.getAllParameterMarkersInQueryStatement( queryStmt );
        
        try
        {
            IParameterMetaData paramMd = query.getParameterMetaData();
            updateParameterDesign( paramMd, dataSetDesign );
        }
        catch( OdaException ex )
        {
            // no parameter definition available, reset previous derived metadata
            dataSetDesign.setParameters( null );
            ex.printStackTrace();
        }
        
        /*
         * See DesignSessionUtil for more convenience methods
         * to define a data set design instance.  
         */     
    }

    /**
     * Returns the SQL query text ready for prepare by a JDBC driver.
     * @param queryStmt
     * @return
     */
    private static String getSQLForPrepare( QueryStatement queryStmt )
    {
        // TODO - replace named parameter markers
        String queryText = StatementHelper.getSQLWithoutComments( queryStmt );
        return queryText;
    }

    /**
     * Updates the specified data set design's result set definition based on the
     * specified runtime metadata.
     * @param md    runtime result set metadata instance
     * @param dataSetDesign     data set design instance to update
     * @throws OdaException
     */
    private static void updateResultSetDesign( IResultSetMetaData md,
            DataSetDesign dataSetDesign ) 
        throws OdaException
    {
        ResultSetColumns columns = DesignSessionUtil.toResultSetColumnsDesign( md );

        ResultSetDefinition resultSetDefn = DesignFactory.eINSTANCE
                .createResultSetDefinition();
        // resultSetDefn.setName( value );  // result set name
        resultSetDefn.setResultSetColumns( columns );

        // no exception in conversion; go ahead and assign to specified dataSetDesign
        dataSetDesign.setPrimaryResultSet( resultSetDefn );
        dataSetDesign.getResultSets().setDerivedMetaData( true );
    }

    /**
     * Updates the specified data set design's parameter definition based on the
     * specified runtime metadata.
     * @param paramMd   runtime parameter metadata instance
     * @param dataSetDesign     data set design instance to update
     * @throws OdaException
     */
    private static void updateParameterDesign( IParameterMetaData paramMd,
            DataSetDesign dataSetDesign ) 
        throws OdaException
    {
        DataSetParameters paramDesign = 
            DesignSessionUtil.toDataSetParametersDesign( paramMd, 
                    DesignSessionUtil.toParameterModeDesign( IParameterMetaData.parameterModeIn ) );
        
        // no exception in conversion; go ahead and assign to specified dataSetDesign
        dataSetDesign.setParameters( paramDesign );        
        if( paramDesign == null )
            return;     // no parameter definitions; done with update
        
        paramDesign.setDerivedMetaData( true );
    }

    /**
     * Attempts to close given ODA connection.
     */
    private static void closeConnection( IConnection conn )
    {
        try
        {
            if( conn != null && conn.isOpen() )
                conn.close();
        }
        catch ( OdaException e )
        {
            // ignore
            e.printStackTrace();
        }
    }

    /**
	 * save the dataset design's metadata info
	 * 
	 * @param design
	 */
	public static void saveDataSetDesign( DataSetDesign design,
			IResultSetMetaData meta, IParameterMetaData paramMeta )
	{
		try
		{
			setParameterMetaData( design, paramMeta );
			// set resultset metadata
			setResultSetMetaData( design, meta );
		}
		catch ( OdaException e )
		{
			// no result set definition available, reset in dataSetDesign
			design.setResultSets( null );
		}
	}

	/**
	 * Set parameter metadata in dataset design
	 * 
	 * @param design
	 * @param query
	 */
	private static void setParameterMetaData( DataSetDesign dataSetDesign,
			IParameterMetaData paramMeta )
	{
		try
		{
			// set parameter metadata
			mergeParameterMetaData( dataSetDesign, paramMeta );
		}
		catch ( OdaException e )
		{
			// do nothing, to keep the parameter definition in dataset design
			// dataSetDesign.setParameters( null );
		}
	}

	/**
	 * Merge paramter meta data between dataParameter and datasetDesign's
	 * parameter.
	 * 
	 * @param dataSetDesign
	 * @param md
	 * @throws OdaException
	 */
	private static void mergeParameterMetaData( DataSetDesign dataSetDesign,
			IParameterMetaData md ) throws OdaException
	{
		if ( md == null || dataSetDesign == null )
			return;
		DataSetParameters dataSetParameter = DesignSessionUtil.toDataSetParametersDesign( md,
				ParameterMode.IN_LITERAL );

		if ( dataSetParameter != null )
		{
			Iterator iter = dataSetParameter.getParameterDefinitions( )
					.iterator( );
			while ( iter.hasNext( ) )
			{
				ParameterDefinition defn = (ParameterDefinition) iter.next( );
				proccessParamDefn( defn, dataSetParameter );
			}
		}
		dataSetDesign.setParameters( dataSetParameter );
	}

	/**
	 * Process the parameter definition for some special case
	 * 
	 * @param defn
	 * @param parameters
	 */
	private static void proccessParamDefn( ParameterDefinition defn,
			DataSetParameters parameters )
	{
		if ( defn.getAttributes( ).getNativeDataTypeCode( ) == Types.NULL )
		{
			defn.getAttributes( ).setNativeDataTypeCode( Types.CHAR );
		}
	}

	/**
	 * Set the resultset metadata in dataset design
	 * 
	 * @param dataSetDesign
	 * @param md
	 * @throws OdaException
	 */
	private static void setResultSetMetaData( DataSetDesign dataSetDesign,
			IResultSetMetaData md ) throws OdaException
	{

		ResultSetColumns columns = DesignSessionUtil.toResultSetColumnsDesign( md );

		if ( columns != null )
		{
			ResultSetDefinition resultSetDefn = DesignFactory.eINSTANCE.createResultSetDefinition( );
			// jdbc does not support result set name
			resultSetDefn.setResultSetColumns( columns );
			// no exception; go ahead and assign to specified dataSetDesign
			dataSetDesign.setPrimaryResultSet( resultSetDefn );
			dataSetDesign.getResultSets( ).setDerivedMetaData( true );
		}
		else
		{
			dataSetDesign.setResultSets( null );
		}
	}
}
