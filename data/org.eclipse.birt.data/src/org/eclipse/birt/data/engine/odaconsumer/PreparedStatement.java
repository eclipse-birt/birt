/*
 *****************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *
 ******************************************************************************
 */ 

package org.eclipse.birt.data.engine.odaconsumer;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.logging.Level;
import org.eclipse.birt.data.engine.core.DataException;
//import org.eclipse.birt.data.engine.executor.ParameterMetaData;
import org.eclipse.birt.data.engine.executor.ResultClass;
import org.eclipse.birt.data.engine.i18n.DataResourceHandle;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.datatools.connectivity.oda.IAdvancedQuery;
import org.eclipse.datatools.connectivity.oda.IBlob;
import org.eclipse.datatools.connectivity.oda.IClob;
import org.eclipse.datatools.connectivity.oda.IParameterMetaData;
import org.eclipse.datatools.connectivity.oda.IResultSet;
import org.eclipse.datatools.connectivity.oda.IResultSetMetaData;
import org.eclipse.datatools.connectivity.oda.IQuery;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.SortSpec;

/**
 * <code>PreparedStatement</code> represents a statement query that can be executed without 
 * input parameter values and returns the results and output parameters values it produces.
 * <br>
 * Blob and Clob data types are only supported in output data returned
 * in result columns and output parameters.
 */
public class PreparedStatement
{
	private String m_dataSetType;
	private Connection m_connection;
	private String m_query;
	
	private IQuery m_statement;
	private ArrayList m_properties;
	private int m_maxRows;
	private ArrayList m_sortSpecs;
	
	private int m_supportsNamedResults;
	private int m_supportsOutputParameters;
	private int m_supportsNamedParameters;
	private Boolean m_supportsInputParameters;
	
	private ArrayList m_parameterHints;
	// cached Collection of parameter metadata
	private Collection m_parameterMetaData;
	
	private ProjectedColumns m_projectedColumns;
	private IResultClass m_currentResultClass;
	private ResultSet m_currentResultSet;
	private IResultSet m_driverResultSet;
	
	// projected columns for the un-named result set needs to be updated 
	// next time it's needed
	private boolean m_updateProjectedColumns;
	
	// mappings of result set name to their corresponding projected columns 
	// and result set class
	private Hashtable m_namedProjectedColumns;
	private Hashtable m_namedCurrentResultClasses;
	private Hashtable m_namedCurrentResultSets;
	// set of named projected columns that need to be updated next time 
	// it's needed
	private HashSet m_updateNamedProjectedColumns;
	
	private static final int UNKNOWN = -1;
	private static final int FALSE = 0;
	private static final int TRUE = 1;
	
    // trace logging variables
	private static String sm_className = PreparedStatement.class.getName();
	private static String sm_loggerName = ConnectionManager.sm_packageName;
	private static LogHelper sm_logger = LogHelper.getInstance( sm_loggerName );
	
	PreparedStatement( IQuery statement, String dataSetType, 
	                   Connection connection, String query )
	{
		String methodName = "PreparedStatement";		
		if( sm_logger.isLoggingEnterExitLevel() )
			sm_logger.entering( sm_className, methodName, 
								new Object[] { statement, dataSetType, 
											   connection, query } );
		
		assert( statement != null && connection != null );
		m_statement = statement;
		m_dataSetType = dataSetType;
		m_connection = connection;
		m_query = query;
		
		m_supportsNamedResults = UNKNOWN;
		m_supportsOutputParameters = UNKNOWN;
		m_supportsNamedParameters = UNKNOWN;
		m_supportsInputParameters = null;	// for unknown
		
		sm_logger.exiting( sm_className, methodName, this );
	}
	
	/**
	 * Sets the named property with the specified value.
	 * @param name	the property name.
	 * @param value	the property value.
	 * @throws DataException	if data source error occurs.
	 */
	public void setProperty( String name, String value ) throws DataException
	{
		String methodName = "setProperty";
		if( sm_logger.isLoggingEnterExitLevel() )
			sm_logger.entering( sm_className, methodName, new Object[] { name, value } );
		
		doSetProperty( name, value );
		
		// save the properties in a list in case we need them later, 
		// i.e. support clearParameterValues() for drivers that don't support
		// the ODA operation
		getPropertiesList().add( new Property( name, value ) );
		
		sm_logger.exiting( sm_className, methodName );
	}

	private void doSetProperty( String name, String value ) throws DataException
	{
		String methodName = "doSetProperty";
		if( sm_logger.isLoggingEnterExitLevel() )
			sm_logger.entering( sm_className, methodName, new Object[] { name, value } );
		
		try
		{
			m_statement.setProperty( name, value );
		}
		catch( OdaException ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName, 
							"Cannot set statement property.", ex );
			
			throw new DataException( ResourceConstants.CANNOT_SET_STATEMENT_PROPERTY, ex );
		}
		catch( UnsupportedOperationException ex )
		{
			sm_logger.logp( Level.WARNING, sm_className, methodName, 
							"Cannot set statement property.", ex );			
		}
		
		sm_logger.exiting( sm_className, methodName );
	}
	
	private ArrayList getPropertiesList()
	{
		if( m_properties == null )
			m_properties = new ArrayList();
		
		return m_properties;
	}
	
	/**
	 * Specifies the sort specification for this <code>Statement</code>.  Must be 
	 * called prior to <code>Statement.execute</code> for the sort specification to 
	 * apply to the result set(s) returned.
	 * 
	 * @param sortBy	the sort specification to assign to the <code>Statement</code>.
	 * @throws DataException	if data source error occurs.
	 */
	public void setSortSpec( SortSpec sortBy ) throws DataException
	{
		String methodName = "setSortSpec";	
		sm_logger.entering( sm_className, methodName, sortBy );
		
		doSetSortSpec( sortBy );		
		getSortSpecsList().add( sortBy );
		
		sm_logger.exiting( sm_className, methodName );
	}

	private void doSetSortSpec( SortSpec sortBy ) throws DataException
	{
		String methodName = "doSetSortSpec";
		sm_logger.entering( sm_className, methodName, sortBy );
		
		try
		{
			m_statement.setSortSpec( sortBy );
		}
		catch( OdaException ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName, 
							"Cannot set sort spec.", ex );
			
			throw new DataException( ResourceConstants.CANNOT_SET_SORT_SPEC, ex );
		}
		catch( UnsupportedOperationException ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName, 
							"Cannot set sort spec.", ex );
			throw new DataException( ResourceConstants.CANNOT_SET_SORT_SPEC, ex );
		}
		
		sm_logger.exiting( sm_className, methodName );
	}
	
	private ArrayList getSortSpecsList()
	{
		if( m_sortSpecs == null )
			m_sortSpecs = new ArrayList();
		
		return m_sortSpecs;
	}
	
	/**
	 * Specifies the maximum number of <code>IResultObjects</code> that can be fetched 
	 * from each <code>ResultSet</code> of this <code>Statement</code>.
	 * @param max	the maximum number of <code>IResultObjects</code> that can be 
	 * 				fetched from each <code>ResultSet</code>.
	 * @throws DataException	if data source error occurs.
	 */
	public void setMaxRows( int max ) throws DataException
	{
		String methodName = "setMaxRows";
		sm_logger.entering( sm_className, methodName, max );
		
		doSetMaxRows( max );
		
		m_maxRows = max;
		
		sm_logger.exiting( sm_className, methodName );
	}

	private void doSetMaxRows( int max ) throws DataException
	{
		String methodName = "doSetMaxRows";
		sm_logger.entering( sm_className, methodName, max );
		
		try
		{
			m_statement.setMaxRows( max );
		}
		catch( OdaException ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName, 
							"Cannot set max rows.", ex );
			
			throw new DataException( ResourceConstants.CANNOT_SET_MAX_ROWS, ex );
		}
		catch( UnsupportedOperationException ex )
		{
			sm_logger.logp( Level.WARNING, sm_className, methodName, 
							"Cannot set max rows.", ex );
			// non-critical operation, ignore and proceed
		}
		
		sm_logger.exiting( sm_className, methodName );
	}
	
	/**
	 * Returns an <code>IResultClass</code> representing the metadata of the 
	 * result set for this <code>Statement</code>.
	 * @return	the <code>IResultClass</code> for the result set.
	 * @throws DataException	if data source error occurs.
	 */
	public IResultClass getMetaData( ) throws DataException
	{
		String methodName = "getMetaData";
		sm_logger.entering( sm_className, methodName );
		
		IResultClass ret = null;
		
		// we can get the current result set's metadata directly from the 
		// current result set handle rather than go through ODA
		if( m_currentResultSet != null )
			ret = m_currentResultSet.getMetaData();
		else
			ret = doGetMetaData();
		
		sm_logger.exiting( sm_className, methodName, ret );
		return ret;
	}
	
	private IResultClass doGetMetaData() throws DataException
	{	
		String methodName = "doGetMetaData";
		sm_logger.entering( sm_className, methodName );
	
		if( m_currentResultClass == null )
		{
			List projectedColumns = getProjectedColumns().getColumnsMetadata();
			m_currentResultClass = doGetResultClass( projectedColumns );
		}
		
		sm_logger.exiting( sm_className, methodName, m_currentResultClass );
		
		return m_currentResultClass;
	}

	private ResultClass doGetResultClass( List projectedColumns ) 
	{
		String methodName = "doGetResultClass";
		sm_logger.entering( sm_className, methodName, projectedColumns );
		
		assert( projectedColumns != null );
		ResultClass ret = new ResultClass( projectedColumns );
		
		sm_logger.exiting( sm_className, methodName, ret );
		
		return ret;
	}
	
	private ProjectedColumns getProjectedColumns() 
		throws DataException
	{
		String methodName = "getProjectedColumns";
		sm_logger.entering( sm_className, methodName );
		
		if( m_projectedColumns == null )
		{
			IResultSetMetaData odaMetadata = getRuntimeMetaData();
			m_projectedColumns = doGetProjectedColumns( odaMetadata );
		}	
		else if( m_updateProjectedColumns )
		{
			// need to update the projected columns of the un-named result 
			// set with the newest runtime metadata, don't use the cached 
			// one
			IResultSetMetaData odaMetadata = getRuntimeMetaData();
			ProjectedColumns newProjectedColumns = 
				doGetProjectedColumns( odaMetadata );
			updateProjectedColumns( newProjectedColumns, m_projectedColumns );
			m_projectedColumns = newProjectedColumns;
			
			// reset the update flag
			m_updateProjectedColumns = false;
		}
		
		sm_logger.exiting( sm_className, methodName, m_projectedColumns );
		
		return m_projectedColumns;
	}
	
	private IResultSetMetaData getRuntimeMetaData() throws DataException
	{
		String methodName = "getRuntimeMetaData";
		sm_logger.entering( sm_className, methodName );
		
		try
		{
			IResultSetMetaData ret = m_statement.getMetaData();
			
			sm_logger.exiting( sm_className, methodName, ret );			
			return ret;
		}
		catch( OdaException ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName, 
							"Cannot get resultset metadata.", ex );
			
			throw new DataException( ResourceConstants.CANNOT_GET_RESULTSET_METADATA, ex );
		}
		catch( UnsupportedOperationException ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName, 
							"Cannot get resultset metadata.", ex );
			
			throw new DataException( ResourceConstants.CANNOT_GET_RESULTSET_METADATA, ex );
		}
	}
	
	private ProjectedColumns doGetProjectedColumns( IResultSetMetaData odaMetadata )
		throws DataException
	{
		String methodName = "doGetProjectedColumns";
		sm_logger.entering( sm_className, methodName, odaMetadata );
		
		ResultSetMetaData metadata = 
			new ResultSetMetaData( odaMetadata, 
								   m_connection.getDataSourceId(),
								   m_dataSetType );
		ProjectedColumns ret = new ProjectedColumns( metadata );
		
		sm_logger.exiting( sm_className, methodName, ret );
		return ret;
	}

	/**
	 * Returns an <code>IResultClass</code> representing the metadata of the 
	 * named result set for this <code>Statement</code>.
	 * @param resultSetName	the name of the result set.
	 * @return	the <code>IResultClass</code> for the named result set.
	 * @throws DataException	if data source error occurs.
	 */
	public IResultClass getMetaData( String resultSetName ) throws DataException
	{
		String methodName = "getMetaData";
		sm_logger.entering( sm_className, methodName, resultSetName );
		
		checkNamedResultsSupport();
		
		// we can get the current result set's metadata directly from the 
		// current result set handle rather than go through ODA
		ResultSet resultset = 
			(ResultSet) getNamedCurrentResultSets().get( resultSetName );
		
		IResultClass ret = null;
		
		if( resultset != null )
			ret = resultset.getMetaData();
		else
			ret = doGetMetaData( resultSetName );
		
		sm_logger.exiting( sm_className, methodName, ret );
		return ret;
	}
	
	private IResultClass doGetMetaData( String resultSetName ) throws DataException
	{
		String methodName = "doGetMetaData";
		sm_logger.entering( sm_className, methodName, resultSetName );
		
		IResultClass resultClass = 
			(IResultClass) getNamedCurrentResultClasses().get( resultSetName );

		if( resultClass == null )
		{
			List projectedColumns = 
				getProjectedColumns( resultSetName ).getColumnsMetadata();	
			resultClass = doGetResultClass( projectedColumns );
			getNamedCurrentResultClasses().put( resultSetName, resultClass );
		}
		
		sm_logger.exiting( sm_className, methodName, resultClass );
		
		return resultClass;
	}

	private ProjectedColumns getProjectedColumns( String resultSetName )
		throws DataException
	{
		String methodName = "getProjectedColumns";
		sm_logger.entering( sm_className, methodName, resultSetName );
		
		ProjectedColumns projectedColumns = 
			(ProjectedColumns) getNamedProjectedColumns().get( resultSetName );
		if( projectedColumns == null )
		{
			IResultSetMetaData odaMetadata = getRuntimeMetaData( resultSetName );
			projectedColumns = doGetProjectedColumns( odaMetadata );	
			getNamedProjectedColumns().put( resultSetName, projectedColumns );
		}
		else if( m_updateNamedProjectedColumns != null && 
				 m_updateNamedProjectedColumns.contains( resultSetName ) )
		{
			// there's an existing ProjectedColumns from the same result set, 
			// and it needs to be updated with the newest runtime metadata
			IResultSetMetaData odaMetadata = getRuntimeMetaData( resultSetName );
			ProjectedColumns newProjectedColumns = 
				doGetProjectedColumns( odaMetadata );
			updateProjectedColumns( newProjectedColumns, projectedColumns );
			getNamedProjectedColumns().put( resultSetName, newProjectedColumns );
			
			// reset the update flag for this result set name
			m_updateNamedProjectedColumns.remove( resultSetName );
		}
		
		sm_logger.exiting( sm_className, methodName, projectedColumns );
		
		return projectedColumns;
	}
	
	private IResultSetMetaData getRuntimeMetaData( String resultSetName ) 
		throws DataException
	{
		String methodName = "getRuntimeMetaData";
		sm_logger.entering( sm_className, methodName, resultSetName );
		
		try
		{
			IResultSetMetaData ret = getAdvancedStatement().getMetaDataOf( resultSetName );
			
			sm_logger.exiting( sm_className, methodName, ret );
			
			return ret;
		}
		catch( OdaException ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName, 
							"Cannot get metadata for named resultset.", ex );
			
			throw new DataException( ResourceConstants.CANNOT_GET_METADATA_FOR_NAMED_RESULTSET, ex, 
			                         resultSetName );
		}
		catch( UnsupportedOperationException ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName, 
							"Cannot get metadata for named resultset.", ex );
			
			throw new DataException( ResourceConstants.CANNOT_GET_METADATA_FOR_NAMED_RESULTSET, ex, 
			                         resultSetName );
		}
	}

	/**
	 * Executes the statement's query.
	 * @return	true if this has at least one result set; false otherwise
	 * @throws DataException	if data source error occurs.
	 */
	public boolean execute( ) throws DataException
	{
		String methodName = "execute";
		sm_logger.entering( sm_className, methodName );
		
		// when the statement is re-executed, then the previous result set(s)
		// needs to be invalidated.
		resetCurrentResultSets();
		
		// this will get the result set metadata for the ResultSet in a subsequent
		// getResultSet() call.  Getting the underlying metadata after the statement 
		// has been executed may reset its state which will cause the result set not 
		// to have any data
		doGetMetaData();
		
		try
		{
		    boolean ret= false;

			if ( isAdvancedQuery() )
		        ret = getAdvancedStatement().execute();
			else // simple statement
			{
			    // hold onto its returned result set
			    // for subsequent call to getResultSet()
			    m_driverResultSet = m_statement.executeQuery( );
			    ret = true;
			}

			if( sm_logger.isLoggingEnterExitLevel() )
				sm_logger.exiting( sm_className, methodName, Boolean.valueOf( ret ) );

		    return ret;
		}
		catch( OdaException ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName, 
							"Cannot execute statement.", ex );
			
			throw new DataException( ResourceConstants.CANNOT_EXECUTE_STATEMENT, ex );
		}
		catch( UnsupportedOperationException ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName, 
							"Cannot execute statement.", ex );
			
			throw new DataException( ResourceConstants.CANNOT_EXECUTE_STATEMENT, ex );
		}
	}
	
	// clear all cached references to the current result sets, 
	// applies to named and un-named result sets
	private void resetCurrentResultSets()
	{
	    m_driverResultSet = null;
		m_currentResultSet = null;
		
		if( m_namedCurrentResultSets != null )
			m_namedCurrentResultSets.clear();
	}

	/**
	 * Returns the <code>ResultSet</code> instance.
	 * @return	a <code>ResultSet</code> instance.
	 * @throws DataException	if data source error occurs.
	 */
	public ResultSet getResultSet( ) throws DataException
	{
		String methodName = "getResultSet";
		sm_logger.entering( sm_className, methodName );
		
		IResultSet resultSet = null;
		
		try
		{
			if ( isAdvancedQuery() )
			    resultSet = getAdvancedStatement().getResultSet();
			else
			{
			    resultSet = m_driverResultSet;
			    m_driverResultSet = null;
			}	        		
		}
		catch( OdaException ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName,
							"Cannot get resultset.", ex );
			
			throw new DataException( ResourceConstants.CANNOT_GET_RESULTSET, ex );
		}
		catch( UnsupportedOperationException ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName,
							"Cannot get resultset.", ex );
			
			throw new DataException( ResourceConstants.CANNOT_GET_RESULTSET, ex );
		}
		
		ResultSet rs = 
			new ResultSet( resultSet, doGetMetaData() );
		
		// keep a pointer to the current result set in case the caller wants 
		// to get the metadata from the current result set of the statement
		m_currentResultSet = rs;
		
		// reset this for the statement since the caller can 
		// subsequently change this and the changes won't apply 
		// to the existing result set
		m_currentResultClass = null;
		
		sm_logger.exiting( sm_className, methodName, rs );
		
		return rs;
	}
	
	/**
	 * Returns the specified named <code>ResultSet</code>.
	 * @param resultSetName	the name of the result set.
	 * @return	the named <code>ResultSet</code>.
	 * @throws DataException	if data source error occurs.
	 */
	public ResultSet getResultSet( String resultSetName ) throws DataException
	{
		String methodName = "getResultSet";
		sm_logger.entering( sm_className, methodName, resultSetName );
		
		checkNamedResultsSupport();
		
		IResultSet resultset = null;
		
		try
		{
			resultset = 
			    getAdvancedStatement().getResultSet( resultSetName );
		}
		catch( OdaException ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName, 
							"Cannot get named resultset.", ex );
			
			throw new DataException( ResourceConstants.CANNOT_GET_NAMED_RESULTSET, ex, 
			                         resultSetName );
		}
		catch( UnsupportedOperationException ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName, 
							"Cannot get named resultset.", ex );
			
			throw new DataException( ResourceConstants.CANNOT_GET_NAMED_RESULTSET, ex, 
			                         resultSetName );
		}
		
		ResultSet rs = 
			new ResultSet( resultset, doGetMetaData( resultSetName ) );
		
		// keep this as the current named result set, so the caller can 
		// get the metadata from the result set from the statement
		getNamedCurrentResultSets().put( resultSetName, rs );
		
		// reset the current result class for the given result set name, so 
		// subsequent changes won't apply to the existing result set
		getNamedCurrentResultClasses().remove( resultSetName );
		
		sm_logger.exiting( sm_className, methodName, rs );
		
		return rs;
	}

	/**
	 * Returns the 1-based index of the specified output parameter.
	 * @param paramName	the name of the parameter.
	 * @return	the 1-based index of the output parameter.
	 * @throws DataException	if data source error occurs.
	 */
	public int findOutParameter( String paramName ) throws DataException
	{
		String methodName = "findOutParameter";
		sm_logger.entering( sm_className, methodName, paramName );
		
		checkOutputParameterSupport( );
		
		try
		{
			int ret = getAdvancedStatement().findOutParameter( paramName );
			
			sm_logger.exiting( sm_className, methodName, ret );			
			return ret;
		}
		catch( OdaException ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName, 
							"Cannot find out parameter.", ex );
			
			throw new DataException( ResourceConstants.CANNOT_FIND_OUT_PARAMETER, ex, 
			                         paramName );
		}
		catch( UnsupportedOperationException ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName, 
							"Cannot find out parameter.", ex );
			
			throw new DataException( ResourceConstants.CANNOT_FIND_OUT_PARAMETER, ex, 
			                         paramName );
		}
	}
	
	/**
	 * Returns the ODA data type code for the specified parameter.
	 * @param paramIndex	the 1-based index of the parameter.
	 * @return	the ODA <code>java.sql.Types</code> code of the parameter.
	 * @throws DataException	if data source error occurs.
	 */
	public int getParameterType( int paramIndex ) throws DataException
	{
		String methodName = "getParameterType";
		sm_logger.entering( sm_className, methodName, paramIndex );

	    ParameterMetaData paramMD = getParameterMetaData( paramIndex );
		assert( paramMD != null );	// invalid paramIndex would have thrown exception
		int ret = paramMD.getDataType();
		
		sm_logger.exiting( sm_className, methodName, ret );
		return ret;
	}
	
	/**
	 * Returns the ODA data type code for the specified parameter.
	 * @param paramName	the name of the parameter.
	 * @return	the ODA <code>java.sql.Types</code> code of the parameter.
	 * @throws DataException	if data source error occurs.
	 */
	public int getParameterType( String paramName ) throws DataException
	{
		String methodName = "getParameterType";
		sm_logger.entering( sm_className, methodName, paramName );
		
		// find corresponding parameter index
		int paramIndex = getIndexFromParamHints( paramName );
		if( paramIndex <= 0 )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName, 
					"Cannot get parameter type by name." );
	
			throw new DataException( ResourceConstants.CANNOT_GET_PARAMETER_TYPE,
	                         			paramName );
		}

		int ret = getParameterType( paramIndex );
		
		sm_logger.exiting( sm_className, methodName, ret );		
		return ret;
	}

	/**
	 * Returns the specified output parameter value.
	 * @param paramIndex	the 1-based index of the parameter.
	 * @return	the output value for the specified parameter.
	 * @throws DataException	if data source error occurs.
	 */
	public Object getParameterValue( int paramIndex ) throws DataException
	{
		String methodName = "getParameterValue";
		sm_logger.entering( sm_className, methodName, paramIndex );
		
		checkOutputParameterSupport( );
		
		Object ret = getParameterValue( null /* n/a paramName */, paramIndex );
		
		sm_logger.exiting( sm_className, methodName, ret );
		return ret;
	}
	
	/**
	 * Returns the specified output parameter value.
	 * @param paramName	the name of the parameter.
	 * @return	the output value for the specified parameter.
	 * @throws DataException	if data source error occurs.
	 */
	public Object getParameterValue( String paramName ) throws DataException
	{
		String methodName = "getParameterValue";
		sm_logger.entering( sm_className, methodName, paramName );
		
		checkOutputParameterSupport( );
		
		Object ret = getParameterValue( paramName, 0 /* n/a paramIndex */ );
		
		sm_logger.exiting( sm_className, methodName, ret );
		return ret;
	}
	
	/**
	 * Closes this <code>Statement</code>.
	 * @throws DataException	if data source error occurs.
	 */
	public void close( ) throws DataException
	{
		String methodName = "close";
		sm_logger.entering( sm_className, methodName );
		
		resetCachedMetadata();
		
		try
		{
			m_statement.close( );
		}
		catch( OdaException ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName, 
							"Cannot close statement.", ex );
			
			throw new DataException( ResourceConstants.CANNOT_CLOSE_STATEMENT, ex );
		}
		catch( UnsupportedOperationException ex )
		{
			sm_logger.logp( Level.WARNING, sm_className, methodName, 
							"Cannot close statement.", ex );
		}
		
		sm_logger.exiting( sm_className, methodName );
	}
	
	private void resetCachedMetadata()
	{
		String methodName = "resetCachedMetaData";
		sm_logger.entering( sm_className, methodName );
		
		resetCurrentMetaData();
		
		if( m_namedCurrentResultSets != null )
			m_namedCurrentResultSets.clear();
		
		if( m_namedCurrentResultClasses != null )
			m_namedCurrentResultClasses.clear();
		
		sm_logger.exiting( sm_className, methodName );
	}

	/**
	 * Adds a <code>ColumnHint</code> for this statement to map design time 
	 * column projections with runtime result set metadata.
	 * @param columnHint	a <code>ColumnHint</code> instance.
	 * @throws DataException	if data source error occurs.
	 */
	public void addColumnHint( ColumnHint columnHint ) throws DataException
	{
		String methodName = "addColumnHint";
		sm_logger.entering( sm_className, methodName, columnHint );
		
		if( columnHint != null )
		{
			// no need to reset the current metadata because adding a column 
			// hint doesn't change the existing columns that are being projected, 
			// it just updates some of the column metadata
			getProjectedColumns().addHint( columnHint );
		}
		
		sm_logger.exiting( sm_className, methodName );
	}
	
	/**
	 * Adds a <code>ColumnHint</code> for this statement to map design time 
	 * column projections with the named runtime result set metadata.
	 * @param resultSetName		the name of the result set.
	 * @param columnHint		a <code>ColumnHint</code> instance.
	 * @throws DataException	if data source error occurs.
	 */
	public void addColumnHint( String resultSetName, ColumnHint columnHint )
		throws DataException
	{
		String methodName = "addColumnHint";
		if( sm_logger.isLoggingEnterExitLevel() )
			sm_logger.entering( sm_className, methodName, 
								new Object[] { resultSetName, columnHint } );
		
		checkNamedResultsSupport();
		
		if( columnHint != null )
		{
			// no need to reset the current metadata because adding a column 
			// hint doesn't change the existing columns that are being projected, 
			// it just updates some of the column metadata
			getProjectedColumns( resultSetName ).addHint( columnHint );
		}
		
		sm_logger.exiting( sm_className, methodName );
	}

	private ArrayList getParameterHints()
	{
		if( m_parameterHints == null )
			m_parameterHints = new ArrayList();
		
		return m_parameterHints;
	}
	
	/**
	 * Adds a <code>ParameterHint</code> for this statement to map static 
	 * parameter definitions with the runtime parameter metadata.
	 * @param paramHint	a <code>ParameterHint</code> instance.
	 * @throws DataException	if data source error occurs.
	 */
	public void addParameterHint( ParameterHint paramHint ) throws DataException
	{
		String methodName = "addParameterHint";		
		sm_logger.entering( sm_className, methodName, paramHint );
		
		if( paramHint != null )
		{
			validateAndAddParameterHint( paramHint );
			
			// if we've successfully added a parameter hint, then we need to invalidate 
			// previous version of parameter metadata
			m_parameterMetaData = null;
		}
		
		sm_logger.exiting( sm_className, methodName );
	}

	private void validateAndAddParameterHint( ParameterHint newParameterHint )
		throws DataException
	{
		String methodName = "validateAndAddParameterHint";		
		sm_logger.entering( sm_className, methodName, newParameterHint );
		
		ArrayList parameterHintsList = getParameterHints();	
		String newParamHintName = newParameterHint.getName();
		int newParamHintIndex = newParameterHint.getPosition();
		for( int i = 0, n = parameterHintsList.size(); i < n; i++ )
		{
			ParameterHint existingParamHint = 
				(ParameterHint) parameterHintsList.get( i );
			
			String existingParamHintName = existingParamHint.getName();
			if( ! existingParamHintName.equals( newParamHintName ) )
			{
				int existingParamHintPosition = existingParamHint.getPosition();
				
				// different names and parameter index is either 0 or didn't 
				// match, so keep on looking
				if( newParamHintIndex == 0 ||
					existingParamHintPosition != newParamHintIndex )
					continue;

				// we don't want to allow different parameter hint name with the 
				// same parameter hint position
				sm_logger.logp( Level.SEVERE, sm_className, methodName, 
								"Different parameter name {0} for same position {1}.",
								new Object[] { existingParamHintName, 
												new Integer( existingParamHintPosition ) } );
				
				throw new DataException( ResourceConstants.DIFFERENT_PARAM_NAME_FOR_SAME_POSITION, 
										 new Object[] { existingParamHintName, 
														new Integer( existingParamHintPosition ) } );
			}

			// the name of the existing hint matches the new hint, 
			// but the parameter index didn't match.  Ignore the parameter 
			// index mismatch if either index is 0
			int existingParamHintIndex = existingParamHint.getPosition();
			if( existingParamHintIndex != newParamHintIndex && 
				existingParamHintIndex > 0 && newParamHintIndex > 0 )
			{
				sm_logger.logp( Level.SEVERE, sm_className, methodName, 
								"Same parameter name {0} for different hints.", existingParamHintName );
				
				throw new DataException( ResourceConstants.SAME_PARAM_NAME_FOR_DIFFERENT_HINTS,
										 existingParamHintName );
			}

			// same parameter hint name and parameter hint index, so we're 
			// referring to the same hint, just update the existing one with 
			// the new info
			existingParamHint.updateHint( newParameterHint );
			
			sm_logger.exiting( sm_className, methodName );			
			return;
		}
		
		// new hint name didn't match any of the existing hints, so we'll need to add 
		// it to the list.
		parameterHintsList.add( newParameterHint );
		
		sm_logger.exiting( sm_className, methodName );
	}
	
	/**
	 * Sets the names of all projected columns. If this method is not called, 
	 * then all columns in the runtime metadata are projected. The specified 
	 * projected names can be either a column name or column alias.
	 * @param projectedNames	the projected column names.
	 * @throws DataException	if data source error occurs.
	 */
	public void setColumnsProjection( String[] projectedNames ) 
		throws DataException
	{
		String methodName = "setColumnsProjection";		
		sm_logger.entering( sm_className, methodName, projectedNames );
		
		resetCurrentMetaData();
		getProjectedColumns().setProjectedNames( projectedNames );
		
		sm_logger.exiting( sm_className, methodName );
	}
	
	/**
	 * Sets the names of all projected columns for the specified result set. If 
	 * this method is not called, then all columns in the specified result set 
	 * metadata are projected.  The specified projected names can be either a 
	 * column name or column alias.
	 * @param resultSetName	the name of the result set.
	 * @param projectedNames	the projected column names.
	 * @throws DataException	if data source error occurs.
	 */
	public void setColumnsProjection( String resultSetName, String[] projectedNames ) 
		throws DataException
	{
		String methodName = "setColumnsProjection";
		if( sm_logger.isLoggingEnterExitLevel() )
			sm_logger.entering( sm_className, methodName, 
								new Object[] { resultSetName, projectedNames } );
		
		checkNamedResultsSupport();
		resetCurrentMetaData( resultSetName );
		getProjectedColumns( resultSetName ).setProjectedNames( projectedNames );
		
		sm_logger.exiting( sm_className, methodName );
	}
	
	/**
	 * Declares a new custom column for the corresponding 
	 * <code>IResultClass</code>.
	 * @param columnName	the custom column name.
	 * @param columnType	the custom column type.
	 * @throws DataException	if data source error occurs.
	 */
	public void declareCustomColumn( String columnName, Class columnType )
		throws DataException
	{
		String methodName = "declareCustomColumn";		
		if( sm_logger.isLoggingEnterExitLevel() )
			sm_logger.entering( sm_className, methodName, 
								new Object[] { columnName, columnType } );
		
		assert columnName != null;
		assert columnName.length() != 0;

		// need to reset current metadata because a custom column could be 
		// declared after we projected all columns, which means we would 
		// want to project the newly declared custom column as well
		resetCurrentMetaData();
		getProjectedColumns().addCustomColumn( columnName, columnType);
		
		sm_logger.exiting( sm_className, methodName );
	}
	
	/**
	 * Declares a new custom column for the <code>IResultClass</code> of the 
	 * specified result set.
	 * @param resultSetName	the name of the result set.
	 * @param columnName	the custom column name.
	 * @param columnType	the custom column type.
	 * @throws DataException	if data source error occurs.
	 */
	public void declareCustomColumn( String resultSetName, String columnName, 
									 Class columnType ) throws DataException
	{
		String methodName = "declareCustomColumn";		
		if( sm_logger.isLoggingEnterExitLevel() )
			sm_logger.entering( sm_className, methodName, 
								new Object[] { resultSetName, columnName, columnType } );
		
		checkNamedResultsSupport();
		
		assert columnName != null;
		assert columnName.length() != 0;
		
		// need to reset current metadata because a custom column could be 
		// declared after we projected all columns, which means we would 
		// want to project the newly declared custom column as well
		resetCurrentMetaData( resultSetName );
		getProjectedColumns( resultSetName ).addCustomColumn( columnName, 
		                                                      columnType );
		
		sm_logger.exiting( sm_className, methodName );
	}
	
	// if a caller tries to add custom columns or sets a new set of 
	// column projection, then we want to generate a new set of metadata 
	// for m_currentResultClass or the specified result set name.  we also 
	// no longer want to keep the reference to the m_currentResultSet or 
	// the reference associated with the result set name because we would 
	// no longer be interested in its metadata afterwards.
	private void resetCurrentMetaData()
	{
		String methodName = "resetCurrentMetaData";
		sm_logger.entering( sm_className, methodName );
		
		m_currentResultClass = null;
		m_currentResultSet = null;
		
		sm_logger.exiting( sm_className, methodName );
	}
	
	private void resetCurrentMetaData( String resultSetName )
	{
		String methodName = "resetCurrentMetaData";
		sm_logger.entering( sm_className, methodName, resultSetName );
		
		getNamedCurrentResultClasses().remove( resultSetName );
		getNamedCurrentResultSets().remove( resultSetName );
		
		sm_logger.exiting( sm_className, methodName );
	}
	
	private Hashtable getNamedProjectedColumns()
	{
		if( m_namedProjectedColumns == null )
			m_namedProjectedColumns = new Hashtable();
		
		return m_namedProjectedColumns;
	}
	
	private Hashtable getNamedCurrentResultClasses()
	{
		if( m_namedCurrentResultClasses == null )
			m_namedCurrentResultClasses = new Hashtable();
		
		return m_namedCurrentResultClasses;
	}
	
	private Hashtable getNamedCurrentResultSets()
	{
		if( m_namedCurrentResultSets == null )
			m_namedCurrentResultSets = new Hashtable();
		
		return m_namedCurrentResultSets;
	}
	
	private IQuery getStatement( )
	{
		return m_statement;
	}
	
	private IAdvancedQuery getAdvancedStatement()
	{
	    assert ( isAdvancedQuery() );
	    return (IAdvancedQuery) m_statement;
	}
	
	private boolean isAdvancedQuery( )
	{
		return ( m_statement instanceof IAdvancedQuery );
	}
	
	private boolean supportsNamedResults() throws DataException
	{
		String methodName = "supportsNamedResults";
		sm_logger.entering( sm_className, methodName );
		
		if( m_supportsNamedResults != UNKNOWN )
		{
			boolean ret = ( m_supportsNamedResults == TRUE );
			
			if( sm_logger.isLoggingEnterExitLevel() )
				sm_logger.exiting( sm_className, methodName, Boolean.valueOf( ret ) );
			
			return ret;
		}

		// else it's unknown right now
		boolean b = 
			m_connection.getMetaData( m_dataSetType ).supportsNamedResultSets( );
		m_supportsNamedResults = b ? TRUE : FALSE;

		if( sm_logger.isLoggingEnterExitLevel() )
			sm_logger.exiting( sm_className, methodName, Boolean.valueOf( b ) );
		return b;
	}
	
	private boolean supportsInputParameter() throws DataException
	{
		String methodName = "supportsInputParameter";
		sm_logger.entering( sm_className, methodName );
		
		if( m_supportsInputParameters == null )	// unknown
		{
			m_supportsInputParameters =
				Boolean.valueOf( m_connection.getMetaData( m_dataSetType ).supportsInParameters() );
		}
		
		if( sm_logger.isLoggingEnterExitLevel() )
			sm_logger.exiting( sm_className, methodName, m_supportsInputParameters );
		
		return m_supportsInputParameters.booleanValue();
	}

	private boolean supportsOutputParameter() throws DataException
	{
		String methodName = "supportsOutputParameter";
		sm_logger.entering( sm_className, methodName );
		
		if( m_supportsOutputParameters != UNKNOWN )
		{
			boolean ret = ( m_supportsOutputParameters == TRUE );
			
			if( sm_logger.isLoggingEnterExitLevel() )
				sm_logger.exiting( sm_className, methodName, Boolean.valueOf( ret ) );
			
			return ret;
		}
		
		// else it's unknown
		boolean b =
			m_connection.getMetaData( m_dataSetType ).supportsOutParameters();
		m_supportsOutputParameters = b ? TRUE : FALSE;
		
		if( sm_logger.isLoggingEnterExitLevel() )
			sm_logger.exiting( sm_className, methodName, Boolean.valueOf( b ) );
		return b;
	}
	
	private boolean supportsNamedParameter() throws DataException
	{
		String methodName = "supportsNamedParameter";	
		sm_logger.entering( sm_className, methodName );
		
		if( m_supportsNamedParameters != UNKNOWN )
		{
			boolean ret = ( m_supportsNamedParameters == TRUE );
			
			if( sm_logger.isLoggingEnterExitLevel() )
				sm_logger.exiting( sm_className, methodName, Boolean.valueOf( ret ) );
			
			return ret;
		}
		
		// else it's unknown
		boolean b =
			m_connection.getMetaData( m_dataSetType ).supportsNamedParameters();
		m_supportsNamedParameters = b ? TRUE : FALSE;
		
		if( sm_logger.isLoggingEnterExitLevel() )
			sm_logger.exiting( sm_className, methodName, Boolean.valueOf( b ) );	
		return b;
	}
	
	private void checkNamedResultsSupport( ) 
		throws DataException
	{
		String methodName = "checkNamedResultsSupport";
		// this can only support named result sets if the underlying object is at 
		// least an IAdvancedQuery
		if( ! isAdvancedQuery( ) || ! supportsNamedResults() )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName, 
							"Named resultsets is not supported." );
			
			throw new DataException( ResourceConstants.NAMED_RESULTSETS_UNSUPPORTED, 
									 new UnsupportedOperationException() );
		}
	}
	
	/**
	 * Returns a collection of <code>ParameterMetaData</code>, which contains 
	 * the parameter metadata information for each parameter that is known at 
	 * the time that <code>getParameterMetaData()</code> is called.  The 
	 * collection is retrieved from the ODA runtime driver's <code>IParameterMetaData</code>, 
	 * if available.  In addition, it includes the supplemental metadata defined in the 
	 * <code>InputParameterHint</code> and <code>OutputParameterHint</code> provided to this 
	 * <code>PreparedStatement</code>.   
	 * @return	a collection of <code>ParameterMetaData</code>, or null 
	 * 			if no parameter metadata is available.
	 * @throws DataException	if data source error occurs.
	 */
	
	public Collection getParameterMetaData() throws DataException
	{
		String methodName = "getParameterMetaData";	
		sm_logger.entering( sm_className, methodName );
		
		if( m_parameterMetaData == null )
		{
		    if( ! supportsInputParameter() &&
		        ! supportsOutputParameter() )
		    {
				sm_logger.logp( Level.INFO, sm_className, methodName, 
						"The ODA driver does not support any type of parameters (IDataSetMetaData); no metadata is available." );
				sm_logger.exiting( sm_className, methodName, null );	
				return null;
		    }
		    
		    // the ODA driver supports in/out parameters
			IParameterMetaData odaParamMetaData = null;
            try
            {
                odaParamMetaData = getOdaDriverParamMetaData();
            }
            catch( DataException e )
            {
                // if parameter hints exist, proceed with
                // returning its metadata; otherwise, throw exception
        		if( m_parameterHints == null || m_parameterHints.size() <= 0 )
        		    throw e;
            }
     
            m_parameterMetaData = ( odaParamMetaData == null ) ?
								  mergeParamHints() :
								  mergeParamHintsWithMetaData( odaParamMetaData );
		}
		
		sm_logger.exiting( sm_className, methodName, m_parameterMetaData );	
		return m_parameterMetaData;
	}
	
	private ParameterMetaData getParameterMetaData( int paramIndex ) throws DataException
	{
		String methodName = "getParameterMetaData";	
		sm_logger.entering( sm_className, methodName, paramIndex );

		Collection allParamsMetadata = null; 
	    if ( paramIndex > 0 )	// index is 1-based
	        allParamsMetadata = getParameterMetaData();
	    if ( allParamsMetadata != null )
	    {	    
		    Iterator paramMDIter = allParamsMetadata.iterator();
	        while ( paramMDIter.hasNext() )
	        {
	            ParameterMetaData aParamMetaData = (ParameterMetaData) paramMDIter.next();
	            if ( aParamMetaData.getPosition() == paramIndex )
	            {
	        		sm_logger.exiting( sm_className, methodName, aParamMetaData );	
	                return aParamMetaData;
	            }
	        }
	    }
	    
	    // no parameters defined, or didn't find matching parameter index position
		throw new DataException( ResourceConstants.CANNOT_GET_PARAMETER_METADATA, 
								new Integer( paramIndex ) );
	}
	
	private IParameterMetaData getOdaDriverParamMetaData() throws DataException
	{
		String methodName = "getOdaDriverParamMetaData";	
		sm_logger.entering( sm_className, methodName );
			    
		IParameterMetaData odaParamMetaData = null;
	    try
	    {
	        odaParamMetaData = m_statement.getParameterMetaData();
	    }
		catch( OdaException ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName, 
							"Cannot get driver's parameter metadata.", ex );
			throw new DataException( ResourceConstants.CANNOT_GET_PARAMETER_METADATA, ex );
		}
		catch( UnsupportedOperationException ex )
		{
			sm_logger.logp( Level.WARNING, sm_className, methodName, 
							"The ODA driver is not capable of providing parameter metadata.", ex );
			// ignore, and continue to return null metadata
		}

		sm_logger.exiting( sm_className, methodName, odaParamMetaData );
		return odaParamMetaData;
	}
	
	private Collection mergeParamHints() throws DataException
	{
		String methodName = "mergeParamHints";	
		sm_logger.entering( sm_className, methodName );
		
		ArrayList parameterMetaData = null;
		
		// add the parameter hints, if any.
		if( m_parameterHints != null && m_parameterHints.size() > 0 )
		{
			parameterMetaData = new ArrayList();
			addParameterHints( parameterMetaData, m_parameterHints );
		}
	
		sm_logger.exiting( sm_className, methodName, parameterMetaData );
		
		return parameterMetaData;
	}

	private void addParameterHints( List parameterMetaData, List parameterHints )
	{
		String methodName = "addParameterHints";
		if( sm_logger.isLoggingEnterExitLevel() )
			sm_logger.entering( sm_className, methodName, 
								new Object[] { parameterMetaData, parameterHints } );
		
		ListIterator iter = parameterHints.listIterator();
		while( iter.hasNext() )
		{
			ParameterHint paramHint = (ParameterHint) iter.next();
			ParameterMetaData paramMd = new ParameterMetaData( paramHint );
			parameterMetaData.add( paramMd );						
		}
		
		sm_logger.exiting( sm_className, methodName );
	}
	
	private Collection mergeParamHintsWithMetaData( IParameterMetaData runtimeParamMetaData )
		throws DataException
	{
		String methodName = "mergeParamHintsWithMetaData";
		sm_logger.entering( sm_className, methodName, runtimeParamMetaData );
		
		assert( runtimeParamMetaData != null );
		
		int numOfParameters = doGetParameterCount( runtimeParamMetaData );
		ArrayList paramMetaData = new ArrayList( numOfParameters );
		
		for( int i = 1; i <= numOfParameters; i++ )
		{
			ParameterMetaData paramMd = 
				new ParameterMetaData( runtimeParamMetaData, i, 
				                       m_connection.getDataSourceId(), 
				                       m_dataSetType );
			paramMetaData.add( paramMd );
		}
		
		if( m_parameterHints != null && m_parameterHints.size() > 0 )
			updateWithParameterHints( paramMetaData, m_parameterHints );

		sm_logger.exiting( sm_className, methodName, paramMetaData );
		
		return paramMetaData;
	}
	
	private int doGetParameterCount( IParameterMetaData runtimeParamMetaData )
		throws DataException
	{
		String methodName = "doGetParameterCount";	
		sm_logger.entering( sm_className, methodName, runtimeParamMetaData );
		
		try
		{
			int ret = runtimeParamMetaData.getParameterCount();
			
			sm_logger.exiting( sm_className, methodName, ret );			
			return ret;
		}
		catch( OdaException ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName, 
							"Cannot get parameter count.", ex );
			
			throw new DataException( ResourceConstants.CANNOT_GET_PARAMETER_COUNT, ex );
		}
		catch( UnsupportedOperationException ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName, 
							"Cannot get parameter count.", ex );
			
			throw new DataException( ResourceConstants.CANNOT_GET_PARAMETER_COUNT, ex );
		}
	}
	
	private void updateWithParameterHints( List parameterMetaData, 
									   	   List parameterHints )
		throws DataException
	{
		String methodName = "updateWithParameterHints";
		if( sm_logger.isLoggingEnterExitLevel() )
			sm_logger.entering( sm_className, methodName, 
								new Object[] { parameterMetaData, parameterHints } );
		
		assert( parameterHints != null );
		
		int numOfRuntimeParameters = parameterMetaData.size();
		ListIterator iter = parameterHints.listIterator();
		while( iter.hasNext() )
		{
			ParameterHint paramHint = (ParameterHint) iter.next();
			String paramHintName = paramHint.getName();
			// try to get the parameter index from the runtime first.  if that fails, 
			// then use the position in the hint itself.
			int position = 0;
			if( paramHint.isInputMode() )
				position = 
					getRuntimeParameterIndexFromName( paramHintName, true /* forInput */ );
			
			if( position <= 0 || position > numOfRuntimeParameters )
			{
				if( paramHint.isOutputMode() )
					position = getRuntimeParameterIndexFromName( paramHintName, false /* forInput */ );

				if( position <= 0 || position > numOfRuntimeParameters )
				{
					// couldn't find the index for the param name
					position = paramHint.getPosition();
					if( position <= 0 || position > numOfRuntimeParameters )
						continue;	// can't match to runtime parameter id
				}
			}
			
			ParameterMetaData paramMd = 
				(ParameterMetaData) parameterMetaData.get( position - 1 );
			paramMd.updateWith( paramHint );
		}
		
		sm_logger.exiting( sm_className, methodName );
	}

	private int getRuntimeParameterIndexFromName( String paramName, boolean forInput )
		throws DataException
	{
		String methodName = "getRuntimeParameterIndexFromName";
		if( sm_logger.isLoggingEnterExitLevel() )
			sm_logger.entering( sm_className, methodName, 
								new Object[] { paramName, Boolean.valueOf( forInput ) } );
		
		if( forInput )
		{
			try
			{	
				int ret = findInParameter( paramName );
				
				sm_logger.exiting( sm_className, methodName, ret );				
				return ret;
			}
			catch( DataException ex )
			{
				// findInParameter is not supported by underlying ODA driver
				if( ex.getCause() instanceof UnsupportedOperationException )
				{
					sm_logger.exiting( sm_className, methodName, 0 );					
					return 0;
				}
				
				sm_logger.logp( Level.SEVERE, sm_className, methodName, 
								"Cannot get runtime parameter index.", ex );
				
				throw ex;
			}
		}
		
		try
		{
			int ret = findOutParameter( paramName );
			
			sm_logger.exiting( sm_className, methodName, ret );			
			return ret;
		}
		catch( DataException ex )
		{
			// findOutParameter is not supported
			if( ex.getCause() instanceof UnsupportedOperationException )
			{
				sm_logger.exiting( sm_className, methodName, 0 );				
				return 0;
			}
			
			sm_logger.logp( Level.SEVERE, sm_className, methodName, 
							"Cannot get runtime parameter index.", ex );
			
			throw ex;
		}
	}
	
	private void checkOutputParameterSupport( ) 
		throws DataException
	{
		String methodName = "checkOutputParameterSupport";
		
		// this can only support output parameter if the underlying object is at 
		// least an IAdvancedQuery
		if( ! isAdvancedQuery( ) || ! supportsOutputParameter() ) 
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName, 
							"Output parameters is not supported." );
			
			throw new DataException( ResourceConstants.OUTPUT_PARAMETERS_UNSUPPORTED, 
									 new UnsupportedOperationException() );
		}
	}
	
	private Object getParameterValue( String paramName, int paramIndex ) 
		throws DataException
	{
		String methodName = "getParameterValue";	
		if( sm_logger.isLoggingEnterExitLevel() )
			sm_logger.entering( sm_className, methodName, 
								new Object[] { paramName, new Integer( paramIndex ) } );
		
		checkOutputParameterSupport( );
		
		Object paramValue = null;
		int paramType = ( paramName == null ) ? getParameterType( paramIndex ) :
												getParameterType( paramName );
		
		switch( paramType )
		{
			case Types.INTEGER:
				int i = ( paramName == null ) ?
						doGetInt( paramIndex ) :
						getInt( paramName );
				if( ! wasNull() )
					paramValue = new Integer( i );
				break;
				
			case Types.DOUBLE:
				double d = ( paramName == null ) ?
						   doGetDouble( paramIndex ) :
						   getDouble( paramName );
				if( ! wasNull() )
					paramValue = new Double( d );
				break;
					
			case Types.CHAR:
				paramValue = ( paramName == null ) ?
							 doGetString( paramIndex ) :
							 getString( paramName );
				break;
			
			case Types.DECIMAL:
				paramValue = ( paramName == null ) ?
							 doGetBigDecimal( paramIndex ) :
							 getBigDecimal( paramName );
				break;
				
			case Types.DATE:
				paramValue = ( paramName == null ) ?
							 doGetDate( paramIndex ) :
							 getDate( paramName );
				break;
				
			case Types.TIME:
				paramValue = ( paramName == null ) ?
							 doGetTime( paramIndex ) :
							 getTime( paramName );
				break;
				
			case Types.TIMESTAMP:
				paramValue = ( paramName == null ) ?
							 doGetTimestamp( paramIndex ) :
							 getTimestamp( paramName );
				break;
				
			case Types.BLOB:
				paramValue = ( paramName == null ) ?
							 doGetBlob( paramIndex ) :
							 getBlob( paramName );
				break;
				
			case Types.CLOB:
				paramValue = ( paramName == null ) ?
							 doGetClob( paramIndex ) :
							 getClob( paramName );
				break;
				
			default:
				assert false;	// exception now thrown by DriverManager
		}
		
		Object ret = ( wasNull( ) ) ? null : paramValue;
		
		sm_logger.exiting( sm_className, methodName, ret );	
		return ret;
	}
	
	// the following six getters are by name and need additional processing in the 
	// case where a named parameter is not supported by the underlying data source.  
	// In that case, we will look at the output parameter hints to get the name to 
	// id mapping
	private int getInt( String paramName ) throws DataException
	{
		String methodName = "getInt";		
		sm_logger.entering( sm_className, methodName, paramName );
		
		int ret = 0;
		
		if( ! supportsNamedParameter() )
		{
			int paramIndex = getIndexFromParamHints( paramName );
			if( paramIndex > 0 )
				ret = doGetInt( paramIndex );
		}
		else
		{
			ret = doGetInt( paramName );
		}
		
		sm_logger.exiting( sm_className, methodName, ret );	
		return ret;
	}

	private double getDouble( String paramName ) throws DataException
	{
		String methodName = "getDouble";	
		sm_logger.entering( sm_className, methodName, paramName );
		
		double ret = 0;
		
		if( ! supportsNamedParameter() )
		{
			int paramIndex = getIndexFromParamHints( paramName );
			if( paramIndex > 0 )
				ret = doGetDouble( paramIndex );
		}
		else
		{
			ret = doGetDouble( paramName );
		}
		
		if( sm_logger.isLoggingEnterExitLevel() )
			sm_logger.exiting( sm_className, methodName, new Double( ret ) );	
		return ret;
	}
	
	private String getString( String paramName ) throws DataException
	{
		String methodName = "getString";
		sm_logger.entering( sm_className, methodName, paramName );
		
		String ret = null;
		
		if( ! supportsNamedParameter() )
		{
			int paramIndex = getIndexFromParamHints( paramName );
			if( paramIndex > 0 )
				ret = doGetString( paramIndex );
		}
		else
		{
			ret = doGetString( paramName );
		}
		
		sm_logger.exiting( sm_className, methodName, ret );
		return ret;
	}
	
	private BigDecimal getBigDecimal( String paramName ) throws DataException
	{
		String methodName = "getBigDecimal";
		sm_logger.entering( sm_className, methodName, paramName );
		
		BigDecimal ret = null;
		
		if( ! supportsNamedParameter() )
		{
			int paramIndex = getIndexFromParamHints( paramName );
			if( paramIndex > 0 )
				ret = doGetBigDecimal( paramIndex );
		}
		else
		{
			ret = doGetBigDecimal( paramName );
		}
		
		sm_logger.exiting( sm_className, methodName, ret );
		return ret;
	}

	private java.util.Date getDate( String paramName ) throws DataException
	{
		String methodName = "getDate";
		sm_logger.entering( sm_className, methodName, paramName );
		
		java.util.Date ret = null;
		
		if( ! supportsNamedParameter() )
		{
			int paramIndex = getIndexFromParamHints( paramName );
			if( paramIndex > 0 )
				ret = doGetDate( paramIndex );
		}
		else
		{
			ret = doGetDate( paramName );
		}
		
		sm_logger.exiting( sm_className, methodName, ret );
		return ret;
	}
	
	private Time getTime( String paramName ) throws DataException
	{
		String methodName = "getTime";
		sm_logger.entering( sm_className, methodName, paramName );
		
		Time ret = null;
		
		if( ! supportsNamedParameter() )
		{
			int paramIndex = getIndexFromParamHints( paramName );
			if( paramIndex > 0 )
				ret = doGetTime( paramIndex );
		}
		else
		{
			ret = doGetTime( paramName );
		}
		
		sm_logger.exiting( sm_className, methodName, ret );
		return ret;
	}
	
	private Timestamp getTimestamp( String paramName ) throws DataException
	{
		String methodName = "getTimestamp";
		sm_logger.entering( sm_className, methodName, paramName );
		
		Timestamp ret = null;
		
		if( ! supportsNamedParameter() )
		{
			int paramIndex = getIndexFromParamHints( paramName );
			if( paramIndex > 0 )
				ret = doGetTimestamp( paramIndex );
		}
		else
		{
			ret = doGetTimestamp( paramName );
		}
		
		sm_logger.exiting( sm_className, methodName, ret );
		return ret;
	}
	
	private IBlob getBlob( String paramName ) throws DataException
	{
		final String methodName = "getBlob";
		sm_logger.entering( sm_className, methodName, paramName );
		
		IBlob ret = null;
		
		if( ! supportsNamedParameter() )
		{
			int paramIndex = getIndexFromParamHints( paramName );
			if( paramIndex > 0 )
				ret = doGetBlob( paramIndex );
		}
		else
		{
			ret = doGetBlob( paramName );
		}
		
		sm_logger.exiting( sm_className, methodName, ret );
		return ret;
	}
	
	private IClob getClob( String paramName ) throws DataException
	{
		final String methodName = "getClob";
		sm_logger.entering( sm_className, methodName, paramName );
		
		IClob ret = null;
		
		if( ! supportsNamedParameter() )
		{
			int paramIndex = getIndexFromParamHints( paramName );
			if( paramIndex > 0 )
				ret = doGetClob( paramIndex );
		}
		else
		{
			ret = doGetClob( paramName );
		}
		
		sm_logger.exiting( sm_className, methodName, ret );
		return ret;
	}
	
	private int doGetInt( int paramIndex ) throws DataException
	{
		String methodName = "doGetInt";
		sm_logger.entering( sm_className, methodName, paramIndex );
		
		try
		{
			int ret = getAdvancedStatement().getInt( paramIndex );
			
			sm_logger.exiting( sm_className, methodName, ret );			
			return ret;
		}
		catch( OdaException ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName, 
							"Cannot get integer from parameter.", ex );
			
			throw new DataException( ResourceConstants.CANNOT_GET_INT_FROM_PARAMETER, ex, 
			                         new Integer( paramIndex ) );
		}
		catch( UnsupportedOperationException ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName, 
							"Cannot get integer from parameter.", ex );
			
			throw new DataException( ResourceConstants.CANNOT_GET_INT_FROM_PARAMETER, ex, 
			                         new Integer( paramIndex ) );
		}
	}
	
	private int doGetInt( String paramName ) throws DataException
	{
		String methodName = "doGetInt";		
		sm_logger.entering( sm_className, methodName, paramName );
		
		try
		{
			int ret = getAdvancedStatement().getInt( paramName );
			
			sm_logger.exiting( sm_className, methodName, ret );
			
			return ret;
		}
		catch( OdaException ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName, 
							"Cannot get integer from parameter.", ex );
			
			throw new DataException( ResourceConstants.CANNOT_GET_INT_FROM_PARAMETER, ex, 
			                         paramName );
		}
		catch( UnsupportedOperationException ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName, 
							"Cannot get integer from parameter.", ex );
			
			throw new DataException( ResourceConstants.CANNOT_GET_INT_FROM_PARAMETER, ex, 
			                         paramName );
		}
	}
	
	private double doGetDouble( int paramIndex ) throws DataException
	{
		String methodName = "doGetDouble";		
		sm_logger.entering( sm_className, methodName, paramIndex );
		
		try
		{
			double ret = getAdvancedStatement().getDouble( paramIndex );
			
			if( sm_logger.isLoggingEnterExitLevel() )
				sm_logger.exiting( sm_className, methodName, new Double( ret ) );
			
			return ret;
		}
		catch( OdaException ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName,
							"Cannot get double from parameter.", ex );
			
			throw new DataException( ResourceConstants.CANNOT_GET_DOUBLE_FROM_PARAMETER, ex, 
			                         new Integer( paramIndex ) );
		}
		catch( UnsupportedOperationException ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName,
							"Cannot get double from parameter.", ex );
			
			throw new DataException( ResourceConstants.CANNOT_GET_DOUBLE_FROM_PARAMETER, ex, 
			                         new Integer( paramIndex ) );
		}
	}
	
	private double doGetDouble( String paramName ) throws DataException
	{
		String methodName = "doGetDouble";		
		sm_logger.entering( sm_className, methodName, paramName );
		
		try
		{
			double ret = getAdvancedStatement().getDouble( paramName );
			
			if( sm_logger.isLoggingEnterExitLevel() )
				sm_logger.exiting( sm_className, methodName, new Double( ret ) );
			
			return ret;
		}
		catch( OdaException ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName,
							"Cannot get double from parameter.", ex );
			
			throw new DataException( ResourceConstants.CANNOT_GET_DOUBLE_FROM_PARAMETER, ex, 
			                         paramName );
		}
		catch( UnsupportedOperationException ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName,
							"Cannot get double from parameter.", ex );
			
			throw new DataException( ResourceConstants.CANNOT_GET_DOUBLE_FROM_PARAMETER, ex, 
			                         paramName );
		}
	}
	
	private String doGetString( int paramIndex ) throws DataException
	{
		String methodName = "doGetString";
		sm_logger.entering( sm_className, methodName, paramIndex );
		
		try
		{
			String ret = getAdvancedStatement().getString( paramIndex );
			
			sm_logger.exiting( sm_className, methodName, ret );
			
			return ret;
		}
		catch( OdaException ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName, 
							"Cannot get string from parameter.", ex );
			
			throw new DataException( ResourceConstants.CANNOT_GET_STRING_FROM_PARAMETER, ex, 
			                         new Integer( paramIndex ) );
		}
		catch( UnsupportedOperationException ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName, 
							"Cannot get string from parameter.", ex );
			
			throw new DataException( ResourceConstants.CANNOT_GET_STRING_FROM_PARAMETER, ex, 
			                         new Integer( paramIndex ) );			
		}
	}
	
	private String doGetString( String paramName ) throws DataException
	{
		String methodName = "doGetString";
		sm_logger.entering( sm_className, methodName, paramName );
		
		try
		{
			String ret = getAdvancedStatement().getString( paramName );
			
			sm_logger.exiting( sm_className, methodName, ret );
			
			return ret;
		}
		catch( OdaException ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName, 
							"Cannot get string from parameter.", ex );
			
			throw new DataException( ResourceConstants.CANNOT_GET_STRING_FROM_PARAMETER, ex, 
			                         paramName );
		}
		catch( UnsupportedOperationException ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName, 
							"Cannot get string from parameter.", ex );
			
			throw new DataException( ResourceConstants.CANNOT_GET_STRING_FROM_PARAMETER, ex, 
			                         paramName );
		}
	}
	
	private BigDecimal doGetBigDecimal( int paramIndex ) throws DataException
	{
		String methodName = "doGetBigDecimal";
		sm_logger.entering( sm_className, methodName, paramIndex );
		
		try
		{
			BigDecimal ret = getAdvancedStatement().getBigDecimal( paramIndex );
			
			sm_logger.exiting( sm_className, methodName, ret );
			return ret;
		}
		catch( OdaException ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName, 
							"Cannot get decimal from parameter.", ex );
			
			throw new DataException( ResourceConstants.CANNOT_GET_BIGDECIMAL_FROM_PARAMETER, ex, 
			                         new Integer( paramIndex ) );
		}
		catch( UnsupportedOperationException ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName, 
							"Cannot get decimal from parameter.", ex );
			
			throw new DataException( ResourceConstants.CANNOT_GET_BIGDECIMAL_FROM_PARAMETER, ex, 
			                         new Integer( paramIndex ) );
		}
	}
	
	private BigDecimal doGetBigDecimal( String paramName ) throws DataException
	{
		String methodName = "doGetBigDecimal";		
		sm_logger.entering( sm_className, methodName, paramName );
		
		try
		{
			BigDecimal ret = getAdvancedStatement().getBigDecimal( paramName );
			
			sm_logger.exiting( sm_className, methodName, ret );			
			return ret;
		}
		catch( OdaException ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName, 
							"Cannot get decimal from parameter.", ex );
			
			throw new DataException( ResourceConstants.CANNOT_GET_BIGDECIMAL_FROM_PARAMETER, ex, 
			                         paramName );
		}
		catch( UnsupportedOperationException ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName, 
							"Cannot get decimal from parameter.", ex );
			
			throw new DataException( ResourceConstants.CANNOT_GET_BIGDECIMAL_FROM_PARAMETER, ex, 
			                         paramName );
		}
	}
	
	private java.util.Date doGetDate( int paramIndex ) throws DataException
	{
		String methodName = "doGetDate";		
		sm_logger.entering( sm_className, methodName, paramIndex );
		
		try
		{
			java.util.Date ret = getAdvancedStatement().getDate( paramIndex );
			
			sm_logger.exiting( sm_className, methodName, ret );			
			return ret;
		}
		catch( OdaException ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName,
							"Cannot get date from parameter.", ex );
			
			throw new DataException( ResourceConstants.CANNOT_GET_DATE_FROM_PARAMETER, ex, 
			                         new Integer( paramIndex ) );
		}
		catch( UnsupportedOperationException ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName,
							"Cannot get date from parameter.", ex );
			
			throw new DataException( ResourceConstants.CANNOT_GET_DATE_FROM_PARAMETER, ex, 
			                         new Integer( paramIndex ) );
		}
	}
	
	private java.util.Date doGetDate( String paramName ) throws DataException
	{
		String methodName = "doGetDate";		
		sm_logger.entering( sm_className, methodName, paramName );
		
		try
		{
			java.util.Date ret = getAdvancedStatement().getDate( paramName );
			
			sm_logger.exiting( sm_className, methodName, ret );
			return ret;
		}
		catch( OdaException ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName,
							"Cannot get date from parameter.", ex );
			
			throw new DataException( ResourceConstants.CANNOT_GET_DATE_FROM_PARAMETER, ex, 
			                         new Object[] { paramName } );
		}
		catch( UnsupportedOperationException ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName,
							"Cannot get date from parameter.", ex );
			
			throw new DataException( ResourceConstants.CANNOT_GET_DATE_FROM_PARAMETER, ex, 
			                         new Object[] { paramName } );
		}
	}
	
	private Time doGetTime( int paramIndex ) throws DataException
	{
		String methodName = "doGetTime";
		sm_logger.entering( sm_className, methodName, paramIndex );
		
		try
		{
			Time ret = getAdvancedStatement().getTime( paramIndex );
			
			sm_logger.exiting( sm_className, methodName, ret );
			return ret;
		}
		catch( OdaException ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName,
							"Cannot get time from parameter.", ex );
			
			throw new DataException( ResourceConstants.CANNOT_GET_TIME_FROM_PARAMETER, ex, 
			                         new Object[] { new Integer( paramIndex ) } );
		}
		catch( UnsupportedOperationException ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName,
							"Cannot get time from parameter.", ex );
			
			throw new DataException( ResourceConstants.CANNOT_GET_TIME_FROM_PARAMETER, ex, 
			                         new Object[] { new Integer( paramIndex ) } );
		}
	}
	
	private Time doGetTime( String paramName ) throws DataException
	{
		String methodName = "doGetTime";
		sm_logger.entering( sm_className, methodName, paramName );
		
		try
		{
			Time ret = getAdvancedStatement().getTime( paramName );

			sm_logger.exiting( sm_className, methodName, ret );
			return ret;
		}
		catch( OdaException ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName,
							"Cannot get time from parameter.", ex );
			
			throw new DataException( ResourceConstants.CANNOT_GET_TIME_FROM_PARAMETER, ex, 
			                         new Object[] { paramName } );
		}
		catch( UnsupportedOperationException ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName,
							"Cannot get time from parameter.", ex );
			
			throw new DataException( ResourceConstants.CANNOT_GET_TIME_FROM_PARAMETER, ex, 
			                         new Object[] { paramName } );
		}
	}
	
	private Timestamp doGetTimestamp( int paramIndex ) throws DataException
	{
		String methodName = "doGetTimestamp";
		sm_logger.entering( sm_className, methodName, paramIndex );

		try
		{
		    Timestamp ret = getAdvancedStatement().getTimestamp( paramIndex );

			sm_logger.exiting( sm_className, methodName, ret );
			return ret;
		}
		catch( OdaException ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName, 
							"Cannot get timestamp from parameter.", ex );
			
			throw new DataException( ResourceConstants.CANNOT_GET_TIMESTAMP_FROM_PARAMETER, ex, 
			                         new Object[] { new Integer( paramIndex ) } );
		}
		catch( UnsupportedOperationException ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName, 
							"Cannot get timestamp from parameter.", ex );
			
			throw new DataException( ResourceConstants.CANNOT_GET_TIMESTAMP_FROM_PARAMETER, ex, 
			                         new Object[] { new Integer( paramIndex ) } );
		}
	}
	
	private Timestamp doGetTimestamp( String paramName ) throws DataException
	{
		String methodName = "doGetTimestamp";
		sm_logger.entering( sm_className, methodName, paramName );
		
		try
		{
			Timestamp ret = getAdvancedStatement().getTimestamp( paramName );

			sm_logger.exiting( sm_className, methodName, ret );
			return ret;
		}
		catch( OdaException ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName, 
							"Cannot get timestamp from parameter.", ex );
			
			throw new DataException( ResourceConstants.CANNOT_GET_TIMESTAMP_FROM_PARAMETER, ex,
			                         new Object[] { paramName } );
		}
		catch( UnsupportedOperationException ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName, 
							"Cannot get timestamp from parameter.", ex );
			
			throw new DataException( ResourceConstants.CANNOT_GET_TIMESTAMP_FROM_PARAMETER, ex,
			                         new Object[] { paramName } );
		}
	}
	
	private IBlob doGetBlob( int paramIndex ) throws DataException
	{
		final String methodName = "doGetBlob( int )";
		sm_logger.entering( sm_className, methodName, paramIndex );

		try
		{
		    IBlob ret = getAdvancedStatement().getBlob( paramIndex );

			sm_logger.exiting( sm_className, methodName, ret );
			return ret;
		}
		catch( OdaException ex )
		{
		    return logAndThrowGetBlobParamException( methodName, 
		            			new Integer( paramIndex ), ex );
		}
		catch( UnsupportedOperationException ex )
		{
		    return logAndThrowGetBlobParamException( methodName, 
        						new Integer( paramIndex ), ex );
		}
	}
	
	private IBlob doGetBlob( String paramName ) throws DataException
	{
		final String methodName = "doGetBlob( String )";
		sm_logger.entering( sm_className, methodName, paramName );
		
		try
		{
		    IBlob ret = getAdvancedStatement().getBlob( paramName );

			sm_logger.exiting( sm_className, methodName, ret );
			return ret;
		}
		catch( OdaException ex )
		{
		    return logAndThrowGetBlobParamException( methodName, 
		            						paramName, ex );
		}
		catch( UnsupportedOperationException ex )
		{
		    return logAndThrowGetBlobParamException( methodName, 
											paramName, ex );
		}
	}
	
	private IBlob logAndThrowGetBlobParamException( String methodName, 
	        Object parameterId, Exception ex ) throws DataException
	{
		sm_logger.logp( Level.SEVERE, sm_className, methodName, 
				"Cannot get BLOB data from parameter.", ex );

		throw new DataException( ResourceConstants.CANNOT_GET_BLOB_FROM_PARAMETER, 
		        				ex, parameterId );
	}
	
	private IClob doGetClob( int paramIndex ) throws DataException
	{
		final String methodName = "doGetClob( int )";
		sm_logger.entering( sm_className, methodName, paramIndex );

		try
		{
		    IClob ret = getAdvancedStatement().getClob( paramIndex );

			sm_logger.exiting( sm_className, methodName, ret );
			return ret;
		}
		catch( OdaException ex )
		{
		    return logAndThrowGetClobParamException( methodName, 
		            			new Integer( paramIndex ), ex );
		}
		catch( UnsupportedOperationException ex )
		{
		    return logAndThrowGetClobParamException( methodName, 
        						new Integer( paramIndex ), ex );
		}
	}
	
	private IClob doGetClob( String paramName ) throws DataException
	{
		final String methodName = "doGetClob( String )";
		sm_logger.entering( sm_className, methodName, paramName );
		
		try
		{
		    IClob ret = getAdvancedStatement().getClob( paramName );

			sm_logger.exiting( sm_className, methodName, ret );
			return ret;
		}
		catch( OdaException ex )
		{
		    return logAndThrowGetClobParamException( methodName, 
		            						paramName, ex );
		}
		catch( UnsupportedOperationException ex )
		{
		    return logAndThrowGetClobParamException( methodName, 
											paramName, ex );
		}
	}
	
	private IClob logAndThrowGetClobParamException( String methodName, 
	        Object parameterId, Exception ex ) throws DataException
	{
		sm_logger.logp( Level.SEVERE, sm_className, methodName, 
				"Cannot get CLOB data from parameter.", ex );

		throw new DataException( ResourceConstants.CANNOT_GET_CLOB_FROM_PARAMETER, 
		        				ex, parameterId );
	}
	
	private boolean wasNull() throws DataException
	{
		String methodName = "wasNull";
		
		try
		{
			return getAdvancedStatement().wasNull();
		}
		catch( OdaException ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName,
							"Cannot determine was null.", ex );
			
			throw new DataException( ResourceConstants.CANNOT_DETERMINE_WAS_NULL, ex );
		}
		catch( UnsupportedOperationException ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName,
							"Cannot determine was null.", ex );
			
			throw new DataException( ResourceConstants.CANNOT_DETERMINE_WAS_NULL, ex );
		}
	}
	
	private int getOdaTypeFromParamHints( String paramName, 
										  int paramIndex )
	{
		if( m_parameterHints == null )
			return Types.CHAR;
		
		ListIterator iter = m_parameterHints.listIterator();
		boolean useParamName = ( paramName != null );
		while( iter.hasNext() )
		{
			ParameterHint paramHint = (ParameterHint) iter.next();
			
			Class typeInHint = null;
			if( ( useParamName && paramHint.getName().equals( paramName ) ) ||
				( ! useParamName && paramHint.getPosition() == paramIndex ) )
			{
				typeInHint = paramHint.getDataType();
				return DataTypeUtil.toOdaType( typeInHint );
			}
		}
		
		// didn't have a hint for the specified parameter
		return Types.CHAR;
	}

	// returns 0 if the parameter hint doesn't exist for the specified parameter 
	// name or if the caller didn't specify a position for the specified parameter name
	private int getIndexFromParamHints( String paramName )
	{
		if( m_parameterHints == null )
			return 0;
		
		ListIterator iter = m_parameterHints.listIterator();
		while( iter.hasNext() )
		{
			ParameterHint paramHint = 
				(ParameterHint) iter.next();
			
			if( paramHint.getName().equals( paramName ) )
				return paramHint.getPosition();
		}
		
		return 0;	// no parameter hint to give us the position
	}

	/**
	 * Clears the current input parameter values immediately.
	 * @throws DataException	if data source error occurs.
	 */
	public void clearParameterValues() throws DataException
	{
		String methodName = "clearParameterValues";
		sm_logger.entering( sm_className, methodName );
		
		try
		{
			getStatement().clearInParameters();
		}
		catch( OdaException ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName,
							"Cannot clear input parameters.", ex );
			
			throw new DataException( ResourceConstants.CANNOT_CLEAR_IN_PARAMETERS, ex );
		}
		catch( AbstractMethodError err )
		{
			sm_logger.logp( Level.WARNING, sm_className, methodName,
							"clearInParameters method undefined.", err );
			
			handleUnsupportedClearInParameters();
		}
		catch( UnsupportedOperationException ex )
		{
			sm_logger.logp( Level.WARNING, sm_className, methodName,
							"clearInParameters is not supported.", ex );
			
			handleUnsupportedClearInParameters();
		}
		
		// after clearing the parameter values, the underlying 
		// metadata may change, so we need to invalidate 
		// our states that are used to maintain the current 
		// ResultClass
		resetCachedMetadata();
		
		// optimization to keep the invalidated cached ProjectedColumns, 
		// rather than getting new ones to replace them all immediately 
		// If needed, after clearParameterValues() is called, we will go get 
		// a new set of runtime metadata and incorporate the custom column/
		// colum hints/projections info from the invalidated ProjectedColumn
		m_updateProjectedColumns = true;
		
		if( m_namedProjectedColumns != null )
		{
			Set keys = m_namedProjectedColumns.keySet();
			if( m_updateNamedProjectedColumns == null )
				m_updateNamedProjectedColumns = new HashSet( keys );
			else
				m_updateNamedProjectedColumns.addAll( keys );
		}

		sm_logger.exiting( sm_className, methodName );
	}

	// provide a work-around for older ODA drivers or ODA drivers that 
	// don't support the clearInParameters call
	// the workaround involves creating a new instance of the underlying 
	// ODA statement and setting it back up to the state of the current 
	// statement
	private void handleUnsupportedClearInParameters() throws DataException
	{
		m_statement = m_connection.prepareOdaQuery( m_query, 
		                                                m_dataSetType );

		// getting the new statement back into the previous statement's
		// state
		if( m_properties != null )
		{
			ListIterator iter = m_properties.listIterator();
			while( iter.hasNext() )
			{
				Property property = (Property) iter.next();
				doSetProperty( property.getName(), property.getValue() );
			}
		}
		
		doSetMaxRows( m_maxRows );
		
		if( m_sortSpecs != null )
		{
			ListIterator iter = m_sortSpecs.listIterator();
			while( iter.hasNext() )
			{
				SortSpec sortBy = (SortSpec) iter.next();
				doSetSortSpec( sortBy );
			}
		}
	}

	private void updateProjectedColumns( ProjectedColumns newProjectedColumns,
										 ProjectedColumns oldProjectedColumns )
		throws DataException
	{
		ArrayList customColumns = oldProjectedColumns.getCustomColumns();
		ArrayList columnHints = oldProjectedColumns.getColumnHints();
		String[] projections = oldProjectedColumns.getProjections();
		
		if( customColumns != null )
		{
			ListIterator iter = customColumns.listIterator();
			while( iter.hasNext() )
			{
				CustomColumn customColumn = (CustomColumn) iter.next();
				newProjectedColumns.addCustomColumn( customColumn.getName(), 
				                                     customColumn.getType() );
			}
		}
		
		if( columnHints != null )
		{
			ListIterator iter = columnHints.listIterator();
			while( iter.hasNext() )
			{
				ColumnHint columnHint = (ColumnHint) iter.next();
				newProjectedColumns.addHint( columnHint );
			}
		}
		
		newProjectedColumns.setProjectedNames( projections );
	}
	
	/**
	 * Returns the 1-based index of the specified input parameter.
	 * @param paramName	the name of the parameter.
	 * @return	the 1-based index of the input parameter.
	 * @throws DataException	if data source error occurs.
	 */
	public int findInParameter( String paramName ) throws DataException
	{
		final String methodName = "findInParameter"; //$NON-NLS-1$
		sm_logger.entering( sm_className, methodName, paramName );
		
		try
		{
			int ret = getStatement( ).findInParameter( paramName );

			sm_logger.exiting( sm_className, methodName, ret );			
			return ret;
		}
		catch( OdaException ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName, 
							"Cannot find input parameter by name.", ex ); //$NON-NLS-1$
			
			throw new DataException( ResourceConstants.CANNOT_FIND_IN_PARAMETER, ex, 
			                         new Object[] { paramName } );
		}
		catch( UnsupportedOperationException ex )
		{
            // this is common, and may be ignored by caller
			sm_logger.logp( Level.INFO, sm_className, methodName, 
							"Cannot find input parameter by name.", ex ); //$NON-NLS-1$
			
			throw new DataException( ResourceConstants.CANNOT_FIND_IN_PARAMETER, ex, 
			                         new Object[] { paramName } );
		}
	}

	/**
	 * Sets the value of the specified input parameter.
	 * @param paramIndex	the 1-based index of the parameter.
	 * @param paramValue	the input parameter value.
	 * @throws DataException	if data source error occurs.
	 */
	public void setParameterValue( int paramIndex, Object paramValue ) throws DataException
	{
		setParameterValue( null /* n/a paramName */, paramIndex, paramValue );
	}

	/**
	 * Sets the value of the specified input parameter.
	 * @param paramName		the name of the parameter.
	 * @param paramValue	the input parameter value.
	 * @throws DataException	if data source error occurs.
	 */
	public void setParameterValue( String paramName, Object paramValue ) throws DataException
	{
		setParameterValue( paramName, 0 /* n/a paramIndex */, paramValue );
	}

	private void setParameterValue( String paramName, int paramIndex, 
									Object paramValue ) throws DataException
	{
		String methodName = "setParameterValue";
		
		if( paramValue == null )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName, 
							"Input parameter value is null." );
			
			String localizedMessage = 
				DataResourceHandle.getInstance().getMessage( ResourceConstants.PARAMETER_VALUE_IS_NULL );
			throw new NullPointerException( localizedMessage );
		}
		
		try
		{
			// the following calls the setters may fail due to a type 
			// mismatch with the parameter value's type.  so we'll 
			// catch all RuntimeException's and OdaException's because 
			// those are the exceptions that the ODA driver may throw in 
			// such cases.  If we catch one, then we'll try to use alternative 
			// mappings based on the runtime parameter metadata or the 
			// parameter hints to call another setter method
			if( paramValue instanceof Integer )
			{
				int i = ( (Integer) paramValue ).intValue( );
				setInt( paramName, paramIndex, i );
				return;
			}
			
			if( paramValue instanceof Double )
			{
				double d = ( (Double) paramValue ).doubleValue( );
				setDouble( paramName, paramIndex, d );
				return;
			}
	
			if( paramValue instanceof String )
			{
				String string = (String) paramValue;
				setString( paramName, paramIndex, string );
				return;
			}
			
			if( paramValue instanceof BigDecimal )
			{
				BigDecimal decimal = (BigDecimal) paramValue;
				setBigDecimal( paramName, paramIndex, decimal );
				return;
			}
			
			if( paramValue instanceof java.util.Date )
			{
				// need to convert the java.util.Date to the java.sql.Date supported 
				// by ODA
				java.util.Date date = (java.util.Date) paramValue;
				Date sqlDate = new Date( date.getTime() );
				setDate( paramName, paramIndex, sqlDate );
				return;
			}
	
			if( paramValue instanceof Time )
			{
				Time time = (Time) paramValue;
				setTime( paramName, paramIndex, time );
				return;
			}
			
			if( paramValue instanceof Timestamp )
			{
				Timestamp timestamp = (Timestamp) paramValue;
				setTimestamp( paramName, paramIndex, timestamp );
				return;
			}
		}
		catch( RuntimeException ex )
		{
			retrySetParameterValue( paramName, paramIndex, paramValue, ex );
			return;
		}
		catch( DataException ex )
		{
			retrySetParameterValue( paramName, paramIndex, paramValue, ex );
			return;
		}

		sm_logger.logp( Level.SEVERE, sm_className, methodName,
						"Unsupported parameter value type." );
		
		throw new DataException( ResourceConstants.UNSUPPORTED_PARAMETER_VALUE_TYPE, 
                                 new Object[] { paramValue.getClass() } );
	}

	// retry setting the parameter value by using an alternate setter method 
	// using the runtime parameter metadata. Or if the runtime parameter metadata 
	// is not available, then use the input parameter hints, if available.  
	// It will default to calling setString() we can't get the info from 
	// the runtime parameter metadata or the parameter hints.
	private void retrySetParameterValue( String paramName, int paramIndex, 
										 Object paramValue, 
										 Exception lastException ) throws DataException
	{
		String methodName = "retrySetParameterValue";
		
		int parameterType = Types.NULL;
		
		try
		{
			// try to get the runtime parameter type
			parameterType = ( paramName == null ) ?
							getParameterType( paramIndex ) :
							getParameterType( paramName );
		}
		catch( Exception ex )
		{
			// data source can't get the type, try to get it from the hints
		}
		
		// if the runtime parameter metadata returns the unknown type, then 
		// try to get the type from the parameter hints
		if( parameterType == Types.NULL )
			parameterType = getOdaTypeFromParamHints( paramName, paramIndex );
		
		Class paramValueClass = paramValue.getClass();
		
		// the runtime parameter metadata or hint would lead us to call the same 
		// setXXX method again, so the last exception that got returned could be info 
		// regarding problems with the data, so throw that
		if( ( parameterType == Types.INTEGER && paramValueClass == Integer.class ) ||
			( parameterType == Types.DOUBLE && paramValueClass == Double.class ) ||
			( parameterType == Types.CHAR && paramValueClass == String.class ) ||
			( parameterType == Types.DECIMAL && paramValueClass == BigDecimal.class ) ||
			( parameterType == Types.DATE && paramValueClass == java.util.Date.class ) ||
			( parameterType == Types.TIME && paramValueClass == Time.class ) ||
			( parameterType == Types.TIMESTAMP && paramValueClass == Timestamp.class ) )
		{
			if( lastException instanceof RuntimeException )
			{
				sm_logger.logp( Level.SEVERE, sm_className, methodName, 
								"Cannot set input parameter.", lastException );
				
				throw (RuntimeException) lastException;
			}
			else if( lastException instanceof DataException )
			{
				sm_logger.logp( Level.SEVERE, sm_className, methodName, 
								"Cannot set input parameter.", lastException );
				
				throw (DataException) lastException;
			}
			else
			{
				String localizedMessage = 
					DataResourceHandle.getInstance().getMessage( ResourceConstants.UNKNOWN_EXCEPTION_THROWN );
				IllegalStateException ex = 
					new IllegalStateException( localizedMessage );
				ex.initCause( lastException );
				
				sm_logger.logp( Level.SEVERE, sm_className, methodName, 
								"Cannot set input parameter.", lastException );
				
				throw ex;
			}
		}
		
		if( paramValueClass == Integer.class )
		{
			retrySetIntegerParamValue( paramName, paramIndex, paramValue, 
			                           parameterType );
			return;
		}
		
		if( paramValueClass == Double.class )
		{
			retrySetDoubleParamValue( paramName, paramIndex, paramValue, 
			                          parameterType );
			return;
		}
		
		if( paramValueClass == String.class )
		{	
			retrySetStringParamValue( paramName, paramIndex, paramValue, 
			                          parameterType );
			return;
		}
		
		if( paramValueClass == BigDecimal.class )
		{
			retryBigDecimalParamValue( paramName, paramIndex, paramValue, 
			                           parameterType );
			return;
		}
		
		if( paramValueClass == java.util.Date.class )
		{
			retrySetDateParamValue( paramName, paramIndex, paramValue, 
			                        parameterType );
			return;
		}
		
		if( paramValueClass == Time.class )
		{
			retrySetTimeParamValue( paramName, paramIndex, paramValue, 
			                        parameterType );
			return;
		}
		
		if( paramValueClass == Timestamp.class )
		{
			retrySetTimestampParamValue( paramName, paramIndex, paramValue, 
			                             parameterType );
			return;
		}
		
		assert false;	// unsupported parameter value type was checked earlier
	}

	private void retrySetIntegerParamValue( String paramName, int paramIndex, 
											Object paramValue, int parameterType ) 
		throws DataException
	{
		switch( parameterType )
		{
			case Types.DOUBLE:
			{
				double d = ( (Integer) paramValue ).doubleValue();
				setDouble( paramName, paramIndex, d );
				return;
			}
			
			case Types.CHAR:
			{
				String s = ( (Integer) paramValue ).toString();
				setString( paramName, paramIndex, s );
				return;
			}
			
			case Types.DECIMAL:
			{
				int i = ( (Integer) paramValue ).intValue();
				BigDecimal bd = new BigDecimal( i );
				setBigDecimal( paramName, paramIndex, bd );
				return;
			}
			
			default:
				conversionError( paramName, paramIndex, paramValue, 
				                 parameterType, null /* cause */ );
				return;
		}
	}

	private void retrySetDoubleParamValue( String paramName, int paramIndex, 
										   Object paramValue, int parameterType ) 
		throws DataException
	{
		switch( parameterType )
		{
			case Types.INTEGER:
			{
				int i = ( (Double) paramValue ).intValue();
				Double intValue = new Double( i );
				// this could be due to loss of precision or the double is 
				// outside the range of an integer
				if( ! paramValue.equals( intValue ) )
					conversionError( paramName, paramIndex, paramValue, 
					                 parameterType, null /* cause */ );
				
				setInt( paramName, paramIndex, i );
				return;
			}
			
			case Types.CHAR:
			{
				String s = ( (Double) paramValue ).toString();
				setString( paramName, paramIndex, s );
				return;
			}
			
			case Types.DECIMAL:
			{
				double d = ( (Double) paramValue ).doubleValue();
				BigDecimal bd = new BigDecimal( d );
				setBigDecimal( paramName, paramIndex, bd );
				return;
			}
			
			default:
				conversionError( paramName, paramIndex, paramValue, 
				                 parameterType, null /* cause */ );
				return;
		}
	}

	private void retrySetStringParamValue( String paramName, int paramIndex, 
										   Object paramValue, int parameterType ) 
		throws DataException
	{
		switch( parameterType )
		{
			case Types.INTEGER:
			{
				try
				{
					int i = Integer.parseInt( (String) paramValue );
					setInt( paramName, paramIndex, i );
					return;
				}
				catch( NumberFormatException ex )
				{
					conversionError( paramName, paramIndex, paramValue, 
					                 parameterType, ex );
					return;
				}
			}
			
			case Types.DOUBLE:
			{
				try
				{
					double d = Double.parseDouble( (String) paramValue );
					setDouble( paramName, paramIndex, d );
					return;
				}
				catch( NumberFormatException ex )
				{
					conversionError( paramName, paramIndex, paramValue, 
					                 parameterType, ex );
					return;
				}
			}
			
			case Types.DECIMAL:
			{
				try
				{
					BigDecimal bd = new BigDecimal( (String) paramValue );
					setBigDecimal( paramName, paramIndex, bd );
					return;
				}
				catch( NumberFormatException ex )
				{
					conversionError( paramName, paramIndex, paramValue, 
					                 parameterType, ex );
					return;
				}
			}
			
			case Types.DATE:
			{
				try
				{
					Date d = Date.valueOf( (String) paramValue );
					setDate( paramName, paramIndex, d );
					return;
				}
				catch( IllegalArgumentException ex )
				{
					conversionError( paramName, paramIndex, paramValue, 
					                 parameterType, ex );
					return;
				}
			}
			
			case Types.TIME:
			{
				try
				{
					Time t = Time.valueOf( (String) paramValue );
					setTime( paramName, paramIndex, t );
					return;
				}
				catch( IllegalArgumentException ex )
				{
					conversionError( paramName, paramIndex, paramValue, 
					                 parameterType, ex );
					return;
				}
			}

			case Types.TIMESTAMP:
			{
				try
				{
					Timestamp ts = Timestamp.valueOf( (String) paramValue );
					setTimestamp( paramName, paramIndex, ts );
					return;
				}
				catch( IllegalArgumentException ex )
				{
					conversionError( paramName, paramIndex, paramValue, 
					                 parameterType, ex );
					return;
				}
			}
			
			default:
				conversionError( paramName, paramIndex, paramValue, 
				                 parameterType, null /* cause */ );
				return;
		}
	}

	private void retryBigDecimalParamValue( String paramName, int paramIndex, 
											Object paramValue, int parameterType ) 
		throws DataException
	{
		switch( parameterType )
		{
			case Types.INTEGER:
			{
				int i = ( (BigDecimal) paramValue ).intValue();
				BigDecimal intValue = new BigDecimal( i );
				// this could occur if there is a loss in precision or 
				// if the BigDecimal value is outside the range of an integer
				if( ! paramValue.equals( intValue ) )
					conversionError( paramName, paramIndex, paramValue, 
					                 parameterType, null /* cause */ );
				
				setInt( paramName, paramIndex, i );
				return;
			}
			
			case Types.DOUBLE:
			{
				double d = ( (BigDecimal) paramValue ).doubleValue();
				BigDecimal doubleValue = new BigDecimal( d );
				// this could occur if there is a loss in precision or 
				// if the BigDecimal value is outside the range of a double
				if( ! paramValue.equals( doubleValue ) )
					conversionError( paramName, paramIndex, paramValue, 
					                 parameterType, null /* cause */ );
				
				setDouble( paramName, paramIndex, d );
				return;
			}
			
			case Types.CHAR:
			{
				String s = ( (BigDecimal) paramValue ).toString();
				setString( paramName, paramIndex, s );
				return;
			}
			
			default:
				conversionError( paramName, paramIndex, paramValue, 
				                 parameterType, null /* cause */ );
				return;
		}
	}

	private void retrySetDateParamValue( String paramName, int paramIndex, 
										 Object paramValue, int parameterType ) 
		throws DataException
	{
		switch( parameterType )
		{
			case Types.CHAR:
			{
				// need to convert the java.util.Date to a java.sql.Date, 
				// so that we can get the ISO format date string
				Date sqlDate = new Date( ( (java.util.Date) paramValue ).getTime() );
				String s = sqlDate.toString();
				setString( paramName, paramIndex, s );
				return;
			}
			
			case Types.TIMESTAMP:
			{
				long time = ( (java.util.Date) paramValue ).getTime();
				Timestamp ts = new Timestamp( time );
				setTimestamp( paramName, paramIndex, ts );
				return;
			}
			
			default:
				conversionError( paramName, paramIndex, paramValue, 
				                 parameterType, null /* cause */ );
				return;
		}
	}

	private void retrySetTimeParamValue( String paramName, int paramIndex, 
										 Object paramValue, int parameterType ) 
		throws DataException
	{
		switch( parameterType )
		{
			case Types.CHAR:
			{
				String s = ( (Time) paramValue ).toString();
				setString( paramName, paramIndex, s );
				return;
			}
			
			default:
				conversionError( paramName, paramIndex, paramValue, 
				                 parameterType, null /* cause */ );
				return;
		}
	}

	private void retrySetTimestampParamValue( String paramName, int paramIndex, 	
											  Object paramValue, int parameterType ) 
		throws DataException
	{
		switch( parameterType )
		{
			case Types.CHAR:
			{
				String s = ( (Timestamp) paramValue ).toString();
				setString( paramName, paramIndex, s );
				return;
			}
			
			case Types.DATE:
			{
				long time = ( (Timestamp) paramValue ).getTime();
				Date d = new Date( time );
				setDate( paramName, paramIndex, d );
				return;
			}
			
			default:
				conversionError( paramName, paramIndex, paramValue, 
				                 parameterType, null /* cause */ );
				return;
		}
	}

	private void conversionError( String paramName, int paramIndex, 
								  Object paramValue, int odaType, 
								  Exception cause ) throws DataException
	{
		String methodName = "conversionError";
		
		DataException exception = null;
		if( paramName == null )
			exception = new DataException( ResourceConstants.CANNOT_CONVERT_INDEXED_PARAMETER_VALUE, 
				                           new Object[] { paramValue, new Integer( paramIndex ), 
														  paramValue.getClass(), new Integer( odaType ) } );
		else
			exception = new DataException( ResourceConstants.CANNOT_CONVERT_NAMED_PARAMETER_VALUE, 
                                           new Object[] { paramValue, paramName, paramValue.getClass(), 
														  new Integer( odaType ) } );
		
		if( cause != null )
			exception.initCause( cause );
		
		sm_logger.logp( Level.SEVERE, sm_className, methodName, 
						"Data type conversion error.", exception );
		
		throw exception;
	}

	private void setInt( String paramName, int paramIndex, int i ) throws DataException
	{
		if( paramName == null )
			doSetInt( paramIndex, i );
		else
			setInt( paramName, i );
	}

	private void setInt( String paramName, int i ) throws DataException
	{
		String methodName = "setInt";
		
		if( supportsNamedParameter() )
		{
			doSetInt( paramName, i );
			return;
		}
		
		if( ! setIntUsingHints( paramName, i ) )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName,
							"Cannot set integer value {0} to parameter {1}.",
							new Object[] { new Integer(i), paramName } );
			
			throw new DataException( ResourceConstants.CANNOT_SET_INT_PARAMETER, 
									 new Object[] { paramName } );
		}
	}
	
	private boolean setIntUsingHints( String paramName, int i ) throws DataException
	{
		int paramIndex = getIndexFromParamHints( paramName );
		if( paramIndex <= 0 )
			return false;
		
		doSetInt( paramIndex, i );
		return true;
	}

	private void setDouble( String paramName, int paramIndex, double d ) throws DataException
	{
		if( paramName == null )
			doSetDouble( paramIndex, d );
		else
			setDouble( paramName, d );
	}
	
	private void setDouble( String paramName, double d ) throws DataException
	{
		String methodName = "setDouble";
		
		if( supportsNamedParameter() )
		{
			doSetDouble( paramName, d );
			return;
		}
		
		if( ! setDoubleUsingHints( paramName, d ) )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName, 
							"Cannot set double value {0} to parameter {1}.",
							new Object[] { new Double( d ), paramName } );
			
			throw new DataException( ResourceConstants.CANNOT_SET_DOUBLE_PARAMETER, 
									 new Object[] { paramName } );
		}
	}
	
	private boolean setDoubleUsingHints( String paramName, double d ) throws DataException
	{
		int paramIndex = getIndexFromParamHints( paramName );
		if( paramIndex <= 0 )
			return false;
		
		doSetDouble( paramIndex, d );
		return true;
	}

	private void setString( String paramName, int paramIndex, String stringValue ) throws DataException
	{
		if( paramName == null )
			doSetString( paramIndex, stringValue );
		else
			setString( paramName, stringValue );
	}

	private void setString( String paramName, String stringValue ) throws DataException
	{
		String methodName = "setString";
		
		if( supportsNamedParameter() )
		{
			doSetString( paramName, stringValue );
			return;
		}
		
		if( ! setStringUsingHints( paramName, stringValue ) )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName, 
							"Cannot set string value {0} to parameter {1}.",
							new Object[] { stringValue, paramName } );
			
			throw new DataException( ResourceConstants.CANNOT_SET_STRING_PARAMETER, 
									 new Object[] { paramName } );
		}
	}

	private boolean setStringUsingHints( String paramName, String stringValue ) throws DataException
	{
		int paramIndex = getIndexFromParamHints( paramName );
		if( paramIndex <= 0 )
			return false;
		
		doSetString( paramIndex, stringValue );
		return true;
	}
	
	private void setBigDecimal( String paramName, int paramIndex, BigDecimal decimal ) throws DataException
	{
		if( paramName == null )
			doSetBigDecimal( paramIndex, decimal );
		else
			setBigDecimal( paramName, decimal );
	}

	private void setBigDecimal( String paramName, BigDecimal decimal ) throws DataException
	{
		String methodName = "setBigDecimal";
		
		if( supportsNamedParameter() )
		{
			doSetBigDecimal( paramName, decimal );
			return;
		}
		
		if( ! setBigDecimalUsingHints( paramName, decimal ) )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName, 
							"Cannot set decimal value {0} to parameter {1}.",
							new Object[] { decimal, paramName } );
			
			throw new DataException( ResourceConstants.CANNOT_SET_BIGDECIMAL_PARAMETER, 
									 new Object[] { paramName } );
		}
	}	

	private boolean setBigDecimalUsingHints( String paramName, BigDecimal decimal ) throws DataException
	{
		int paramIndex = getIndexFromParamHints( paramName );
		if( paramIndex <= 0 )
			return false;
		
		doSetBigDecimal( paramIndex, decimal );
		return true;
	}
	
	private void setDate( String paramName, int paramIndex, Date date ) throws DataException
	{
		if( paramName == null )
			doSetDate( paramIndex, date );
		else 
			setDate( paramName, date );
	}

	private void setDate( String paramName, Date date ) throws DataException
	{
		String methodName = "setDate";
		
		if( supportsNamedParameter() )
		{
			doSetDate( paramName, date );
			return;
		}
		
		if( ! setDateUsingHints( paramName, date ) )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName, 
							"Cannot set date value {0} to parameter {1}.",
							new Object[] { date, paramName } );
			
			throw new DataException( ResourceConstants.CANNOT_SET_DATE_PARAMETER, 
									 new Object[] { paramName } );
		}
	}

	private boolean setDateUsingHints( String paramName, Date date ) throws DataException
	{
		int paramIndex = getIndexFromParamHints( paramName );
		if( paramIndex <= 0 )
			return false;
		
		doSetDate( paramIndex, date );
		return true;
	}
	
	private void setTime( String paramName, int paramIndex, Time time ) throws DataException
	{
		if( paramName == null )
			doSetTime( paramIndex, time );
		else
			setTime( paramName, time );
	}

	private void setTime( String paramName, Time time ) throws DataException
	{
		String methodName = "setTime";
		
		if( supportsNamedParameter() )
		{
			doSetTime( paramName, time );
			return;
		}
		
		if( ! setTimeUsingHints( paramName, time ) )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName, 
							"Cannot set time value {0} to parameter {1}.",
							new Object[] { time, paramName } );
			
			throw new DataException( ResourceConstants.CANNOT_SET_TIME_PARAMETER, 
									 new Object[] { paramName } );
		}
	}

	private boolean setTimeUsingHints( String paramName, Time time ) throws DataException
	{
		int paramIndex = getIndexFromParamHints( paramName );
		if( paramIndex <= 0 )
			return false;
		
		doSetTime( paramIndex, time );
		return true;
	}

	private void setTimestamp( String paramName, int paramIndex, Timestamp timestamp ) 
		throws DataException
	{
		if( paramName == null )
			doSetTimestamp( paramIndex, timestamp );
		else
			setTimestamp( paramName, timestamp );
	}

	private void setTimestamp( String paramName, Timestamp timestamp ) throws DataException
	{
		String methodName = "setTimestamp";
		
		if( supportsNamedParameter() )
		{
			doSetTimestamp( paramName, timestamp );
			return;
		}
		
		if( ! setTimestampUsingHints( paramName, timestamp ) )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName, 
							"Cannot set timestamp value {0} to parameter {1}.",
							new Object[] { timestamp, paramName } );
			
			throw new DataException( ResourceConstants.CANNOT_SET_TIMESTAMP_PARAMETER, 
									 new Object[] { paramName } );
		}
	}

	private boolean setTimestampUsingHints( String paramName, Timestamp timestamp ) 
		throws DataException
	{
		int paramIndex = getIndexFromParamHints( paramName );
		if( paramIndex <= 0 )
			return false;
		
		doSetTimestamp( paramIndex, timestamp );
		return true;
	}

	private void doSetInt( int paramIndex, int i ) throws DataException
	{
		String methodName = "doSetInt";
		
		try
		{
			getStatement().setInt( paramIndex, i );
		}
		catch( OdaException ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName,
							"Cannot set integer parameter.", ex );
			
			throw new DataException( ResourceConstants.CANNOT_SET_INT_PARAMETER, ex, 
			                         new Object[] { new Integer( paramIndex ) } );
		}
		catch( UnsupportedOperationException ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName,
							"Cannot set integer parameter.", ex );
			
			throw new DataException( ResourceConstants.CANNOT_SET_INT_PARAMETER, ex, 
			                         new Object[] { new Integer( paramIndex ) } );
		}
	}
	
	private void doSetInt( String paramName, int i ) throws DataException
	{
		String methodName = "doSetInt";
		
		try
		{
			getStatement().setInt( paramName, i );
		}
		catch( OdaException ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName,
							"Cannot set integer parameter.", ex );
			
			throw new DataException( ResourceConstants.CANNOT_SET_INT_PARAMETER, ex, 
			                         new Object[] { paramName } );
		}
		catch( UnsupportedOperationException ex )
		{
			// first try to set value by position if the parameter hints provide name-to-position mapping,  
			// otherwise we need to wrap the UnsupportedOperationException up and throw it
			if( ! setIntUsingHints( paramName, i ) )
			{
				sm_logger.logp( Level.SEVERE, sm_className, methodName,
								"Cannot set integer parameter.", ex );
				
				throw new DataException( ResourceConstants.CANNOT_SET_INT_PARAMETER, ex, 
										 new Object[] { paramName } );
			}
		}
	}

	private void doSetDouble( int paramIndex, double d ) throws DataException
	{
		String methodName = "doSetDouble";
		
		try
		{
			getStatement().setDouble( paramIndex, d );
		}
		catch( OdaException ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName, 
							"Cannot set double parameter.", ex );
			
			throw new DataException( ResourceConstants.CANNOT_SET_DOUBLE_PARAMETER, ex, 
			                         new Object[] { new Integer( paramIndex ) } );
		}
		catch( UnsupportedOperationException ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName, 
							"Cannot set double parameter.", ex );
			
			throw new DataException( ResourceConstants.CANNOT_SET_DOUBLE_PARAMETER, ex, 
			                         new Object[] { new Integer( paramIndex ) } );
		}
	}
	
	
	private void doSetDouble( String paramName, double d ) throws DataException
	{
		String methodName = "doSetDouble";
		
		try
		{
			getStatement().setDouble( paramName, d );
		}
		catch( OdaException ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName, 
							"Cannot set double parameter.", ex );
			
			throw new DataException( ResourceConstants.CANNOT_SET_DOUBLE_PARAMETER, ex, 
			                         new Object[] { paramName } );
		}
		catch( UnsupportedOperationException ex )
		{
			// first try to set value by position if the parameter hints provide name-to-position mapping,  
			// otherwise we need to wrap the UnsupportedOperationException up and throw it
			if( ! setDoubleUsingHints( paramName, d ) )
			{
				sm_logger.logp( Level.SEVERE, sm_className, methodName, 
								"Cannot set double parameter.", ex );
				
				throw new DataException( ResourceConstants.CANNOT_SET_DOUBLE_PARAMETER, ex, 
				                         new Object[] { paramName } );
			}
		}
	}
	
	private void doSetString( int paramIndex, String stringValue ) throws DataException
	{
		String methodName = "doSetString";
		
		try
		{
			getStatement().setString( paramIndex, stringValue );
		}
		catch( OdaException ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName,
							"Cannot set string parameter.", ex );
			
			throw new DataException( ResourceConstants.CANNOT_SET_STRING_PARAMETER, ex, 
			                         new Object[] { new Integer( paramIndex ) } );
		}
		catch( UnsupportedOperationException ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName,
							"Cannot set string parameter.", ex );
			
			throw new DataException( ResourceConstants.CANNOT_SET_STRING_PARAMETER, ex, 
			                         new Object[] { new Integer( paramIndex ) } );
		}
	}
	
	private void doSetString( String paramName, String stringValue ) throws DataException
	{
		String methodName = "doSetString";
		
		try
		{
			getStatement().setString( paramName, stringValue );
		}
		catch( OdaException ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName,
							"Cannot set string parameter.", ex );
			
			throw new DataException( ResourceConstants.CANNOT_SET_STRING_PARAMETER, ex, 
			                         new Object[] { paramName } );
		}
		catch( UnsupportedOperationException ex )
		{
			// first try to set value by position if the parameter hints provide name-to-position mapping,  
			// otherwise we need to wrap the UnsupportedOperationException up and throw it
			if( ! setStringUsingHints( paramName, stringValue ) )
			{
				sm_logger.logp( Level.SEVERE, sm_className, methodName,
								"Cannot set string parameter.", ex );
				
				throw new DataException( ResourceConstants.CANNOT_SET_STRING_PARAMETER, ex, 
				                         new Object[] { paramName } );
			}
		}
	}
	
	private void doSetBigDecimal( int paramIndex, BigDecimal decimal ) throws DataException
	{
		String methodName = "doSetBigDecimal";
		
		try
		{
			getStatement().setBigDecimal( paramIndex, decimal );
		}
		catch( OdaException ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName, 
							"Cannot set decimal parameter.", ex );
			
			throw new DataException( ResourceConstants.CANNOT_SET_BIGDECIMAL_PARAMETER, ex,
			                         new Object[] { new Integer( paramIndex ) } );
		}
		catch( UnsupportedOperationException ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName, 
							"Cannot set decimal parameter.", ex );
			
			throw new DataException( ResourceConstants.CANNOT_SET_BIGDECIMAL_PARAMETER, ex,
			                         new Object[] { new Integer( paramIndex ) } );
		}
	}
	
	private void doSetBigDecimal( String paramName, BigDecimal decimal ) throws DataException
	{
		String methodName = "doSetBigDecimal";
		
		try
		{
			getStatement().setBigDecimal( paramName, decimal );
		}
		catch( OdaException ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName, 
							"Cannot set decimal parameter.", ex );
			
			throw new DataException( ResourceConstants.CANNOT_SET_BIGDECIMAL_PARAMETER, ex, 
			                         new Object[] { paramName } );
		}
		catch( UnsupportedOperationException ex )
		{
			// first try to set value by position if the parameter hints provide name-to-position mapping,  
			// otherwise we need to wrap the UnsupportedOperationException up and throw it
			if( ! setBigDecimalUsingHints( paramName, decimal ) )
			{
				sm_logger.logp( Level.SEVERE, sm_className, methodName, 
								"Cannot set decimal parameter.", ex );
				
				throw new DataException( ResourceConstants.CANNOT_SET_BIGDECIMAL_PARAMETER, ex, 
				                         new Object[] { paramName } );
			}
		}
	}
	
	private void doSetDate( int paramIndex, Date date ) throws DataException
	{
		String methodName = "doSetDate";
		
		try
		{
			getStatement().setDate( paramIndex, date );
		}
		catch( OdaException ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName, 
							"Cannot set date parameter.", ex );
			
			throw new DataException( ResourceConstants.CANNOT_SET_DATE_PARAMETER, ex,
			                         new Object[] { new Integer( paramIndex ) } );
		}
		catch( UnsupportedOperationException ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName, 
							"Cannot set date parameter.", ex );
			
			throw new DataException( ResourceConstants.CANNOT_SET_DATE_PARAMETER, ex,
			                         new Object[] { new Integer( paramIndex ) } );
		}
	}
	
	private void doSetDate( String paramName, Date date ) throws DataException
	{
		String methodName = "doSetDate";
		
		try
		{
			getStatement().setDate( paramName, date );
		}
		catch( OdaException ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName, 
							"Cannot set date parameter.", ex );
			
			throw new DataException( ResourceConstants.CANNOT_SET_DATE_PARAMETER, ex, 
			                         new Object[] { paramName } );
		}
		catch( UnsupportedOperationException ex )
		{
			// first try to set value by position if the parameter hints provide name-to-position mapping,  
			// otherwise we need to wrap the UnsupportedOperationException up and throw it
			if( ! setDateUsingHints( paramName, date ) )
			{
				sm_logger.logp( Level.SEVERE, sm_className, methodName, 
								"Cannot set date parameter.", ex );
				
				throw new DataException( ResourceConstants.CANNOT_SET_DATE_PARAMETER, ex, 
				                         new Object[] { paramName } );
			}
		}
	}
	
	private void doSetTime( int paramIndex, Time time ) throws DataException
	{
		String methodName = "doSetTime";
		
		try
		{
			getStatement().setTime( paramIndex, time );
		}
		catch( OdaException ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName, 
							"Cannot set time parameter.", ex );
			
			throw new DataException( ResourceConstants.CANNOT_SET_TIME_PARAMETER, ex, 
			                         new Object[] { new Integer( paramIndex ) } );
		}
		catch( UnsupportedOperationException ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName, 
							"Cannot set time parameter.", ex );
			
			throw new DataException( ResourceConstants.CANNOT_SET_TIME_PARAMETER, ex, 
			                         new Object[] { new Integer( paramIndex ) } );
		}
	}
	
	private void doSetTime( String paramName, Time time ) throws DataException
	{
		String methodName = "doSetTime";
		
		try
		{
			getStatement().setTime( paramName, time );
		}
		catch( OdaException ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName, 
							"Cannot set time parameter.", ex );
			
			throw new DataException( ResourceConstants.CANNOT_SET_TIME_PARAMETER, ex, 
			                         new Object[] { paramName } );
		}
		catch( UnsupportedOperationException ex )
		{
			// first try to set value by position if the parameter hints provide name-to-position mapping,  
			// otherwise we need to wrap the UnsupportedOperationException up and throw it
			if( ! setTimeUsingHints( paramName, time ) )
			{
				sm_logger.logp( Level.SEVERE, sm_className, methodName, 
								"Cannot set time parameter.", ex );
				
				throw new DataException( ResourceConstants.CANNOT_SET_TIME_PARAMETER, ex, 
				                         new Object[] { paramName } );
			}
		}
	}
	
	private void doSetTimestamp( int paramIndex, Timestamp timestamp ) throws DataException
	{
		String methodName = "doSetTimestamp";
		
		try
		{
			getStatement().setTimestamp( paramIndex, timestamp );
		}
		catch( OdaException ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName, 
							"Cannot set timestamp parameter.", ex );
			
			throw new DataException( ResourceConstants.CANNOT_SET_TIMESTAMP_PARAMETER, ex, 
			                         new Object[] { new Integer( paramIndex ) } );
		}
		catch( UnsupportedOperationException ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName, 
							"Cannot set timestamp parameter.", ex );
			
			throw new DataException( ResourceConstants.CANNOT_SET_TIMESTAMP_PARAMETER, ex, 
			                         new Object[] { new Integer( paramIndex ) } );
		}
	}
	
	private void doSetTimestamp( String paramName, Timestamp timestamp ) throws DataException
	{
		String methodName = "doSetTimestamp";
		
		try
		{
			getStatement().setTimestamp( paramName, timestamp );
		}
		catch( OdaException ex )
		{
			sm_logger.logp( Level.SEVERE, sm_className, methodName, 
							"Cannot set timestamp parameter.", ex );
			
			throw new DataException( ResourceConstants.CANNOT_SET_TIMESTAMP_PARAMETER, ex, 
			                         new Object[] { paramName } );
		}
		catch( UnsupportedOperationException ex )
		{
			// first try to set value by position if the parameter hints provide name-to-position mapping,  
			// otherwise we need to wrap the UnsupportedOperationException up and throw it
			if( ! setTimestampUsingHints( paramName, timestamp ) )
			{
				sm_logger.logp( Level.SEVERE, sm_className, methodName, 
								"Cannot set timestamp parameter.", ex );
				
				throw new DataException( ResourceConstants.CANNOT_SET_TIMESTAMP_PARAMETER, ex, 
				                         new Object[] { paramName } );
			}
		}
	}
	
	private static final class Property
	{
		private String m_name;
		private String m_value;
		
		private Property( String name, String value )
		{
			m_name = name;
			m_value = value;
		}
		
		private String getName()
		{
			return m_name;
		}
		
		private String getValue()
		{
			return m_value;
		}
	}
	
	static final class CustomColumn
	{
		private String m_name;
		private Class m_type;
		
		CustomColumn( String name, Class type )
		{
			m_name = name;
			m_type = type;
		}
		
		private String getName()
		{
			return m_name;
		}
		
		private Class getType()
		{
			return m_type;
		}
	}
}
