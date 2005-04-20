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
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.ResultClass;
import org.eclipse.birt.data.engine.i18n.DataResourceHandle;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.oda.IAdvancedStatement;
import org.eclipse.birt.data.oda.IParameterMetaData;
import org.eclipse.birt.data.oda.IResultSet;
import org.eclipse.birt.data.oda.IResultSetMetaData;
import org.eclipse.birt.data.oda.IStatement;
import org.eclipse.birt.data.oda.OdaException;
import org.eclipse.birt.data.oda.SortSpec;

/**
 * <code>Statement</code> represents a statement query that can be executed without 
 * input parameter values and returns the results and output parameters values it produces.
 */
public class PreparedStatement
{	
	private String m_dataSetType;
	private Connection m_connection;
	private String m_query;
	
	private IStatement m_statement;
	private ArrayList m_properties;
	private int m_maxRows;
	private ArrayList m_sortSpecs;
	
	private int m_supportsNamedResults;
	private int m_supportsOutputParameters;
	private int m_supportsNamedParameters;
	
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
	
	PreparedStatement( IStatement statement, String dataSetType, 
	                   Connection connection, String query )
	{
		assert( statement != null && connection != null );
		m_statement = statement;
		m_dataSetType = dataSetType;
		m_connection = connection;
		m_query = query;
		
		m_supportsNamedResults = UNKNOWN;
		m_supportsOutputParameters = UNKNOWN;
		m_supportsNamedParameters = UNKNOWN;
	}
	
	/**
	 * Sets the named property with the specified value.
	 * @param name	the property name.
	 * @param value	the property value.
	 * @throws DataException	if data source error occurs.
	 */
	public void setProperty( String name, String value ) throws DataException
	{
		doSetProperty( name, value );
		
		// save the properties in a list in case we need them later, 
		// i.e. support clearParameterValues() for drivers that don't support
		// the ODA operation
		getPropertiesList().add( new Property( name, value ) );
	}

	private void doSetProperty( String name, String value ) throws DataException
	{
		try
		{
			m_statement.setProperty( name, value );
		}
		catch( OdaException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_SET_STATEMENT_PROPERTY, ex );
		}
		catch( UnsupportedOperationException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_SET_STATEMENT_PROPERTY, ex );
		}
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
		doSetSortSpec( sortBy );
		
		getSortSpecsList().add( sortBy );
	}

	private void doSetSortSpec( SortSpec sortBy ) throws DataException
	{
		try
		{
			m_statement.setSortSpec( sortBy );
		}
		catch( OdaException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_SET_SORT_SPEC, ex );
		}
		catch( UnsupportedOperationException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_SET_SORT_SPEC, ex );
		}
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
		doSetMaxRows( max );
		
		m_maxRows = max;
	}

	private void doSetMaxRows( int max ) throws DataException
	{
		try
		{
			m_statement.setMaxRows( max );
		}
		catch( OdaException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_SET_MAX_ROWS, ex );
		}
		catch( UnsupportedOperationException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_SET_MAX_ROWS, ex );
		}
	}
	
	/**
	 * Returns an <code>IResultClass</code> representing the metadata of the 
	 * result set for this <code>Statement</code>.
	 * @return	the <code>IResultClass</code> for the result set.
	 * @throws DataException	if data source error occurs.
	 */
	public IResultClass getMetaData( ) throws DataException
	{
		// we can get the current result set's metadata directly from the 
		// current result set handle rather than go through ODA
		if( m_currentResultSet != null )
			return m_currentResultSet.getMetaData();

		return doGetMetaData();
	}
	
	private IResultClass doGetMetaData() throws DataException
	{	
		if( m_currentResultClass != null )
			return m_currentResultClass;
		
		List projectedColumns = getProjectedColumns().getColumnsMetadata();
		m_currentResultClass = doGetResultClass( projectedColumns );
		return m_currentResultClass;
	}

	private ResultClass doGetResultClass( List projectedColumns ) 
	{
		assert( projectedColumns != null );
		return new ResultClass( projectedColumns );
	}
	
	private ProjectedColumns getProjectedColumns() 
		throws DataException
	{
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
		
		return m_projectedColumns;
	}
	
	private IResultSetMetaData getRuntimeMetaData() throws DataException
	{
		try
		{
			return m_statement.getMetaData();
		}
		catch( OdaException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_GET_RESULTSET_METADATA, ex );
		}
		catch( UnsupportedOperationException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_GET_RESULTSET_METADATA, ex );
		}
	}
	
	private ProjectedColumns doGetProjectedColumns( IResultSetMetaData odaMetadata )
		throws DataException
	{
		ResultSetMetaData metadata = 
			new ResultSetMetaData( odaMetadata, 
								   m_connection.getDriverName(),
								   m_dataSetType );
		return new ProjectedColumns( metadata );
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
		checkNamedResultsSupport();
		
		// we can get the current result set's metadata directly from the 
		// current result set handle rather than go through ODA
		ResultSet resultset = 
			(ResultSet) getNamedCurrentResultSets().get( resultSetName );
		
		if( resultset != null )
			return resultset.getMetaData();
		
		return doGetMetaData( resultSetName );
	}
	
	private IResultClass doGetMetaData( String resultSetName ) throws DataException
	{
		IResultClass resultClass = 
			(IResultClass) getNamedCurrentResultClasses().get( resultSetName );
		
		if( resultClass != null )
			return resultClass;
		
		List projectedColumns = 
			getProjectedColumns( resultSetName ).getColumnsMetadata();	
		resultClass = doGetResultClass( projectedColumns );
		getNamedCurrentResultClasses().put( resultSetName, resultClass );		
		return resultClass;
	}

	private ProjectedColumns getProjectedColumns( String resultSetName )
		throws DataException
	{
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
		
		return projectedColumns;
	}
	
	private IResultSetMetaData getRuntimeMetaData( String resultSetName ) 
		throws DataException
	{
		try
		{
			return getAdvancedStatement().getMetaDataOf( resultSetName );
		}
		catch( OdaException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_GET_METADATA_FOR_NAMED_RESULTSET, ex, 
			                         new Object[] { resultSetName } );
		}
		catch( UnsupportedOperationException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_GET_METADATA_FOR_NAMED_RESULTSET, ex, 
			                         new Object[] { resultSetName } );
		}
	}

	/**
	 * Executes the statement's query.
	 * @return	true if this has at least one result set; false otherwise
	 * @throws DataException	if data source error occurs.
	 */
	public boolean execute( ) throws DataException
	{
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
		    if ( isAdvancedStatement() )
		        return getAdvancedStatement().execute();
		    
		    // simple statement; hold onto its returned result set
		    // for subsequent call to getResultSet()
		    m_driverResultSet = m_statement.executeQuery( );
		    return true;
		}
		catch( OdaException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_EXECUTE_STATEMENT, ex );
		}
		catch( UnsupportedOperationException ex )
		{
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
		IResultSet resultSet = null;
		
		try
		{
			if ( isAdvancedStatement() )
			    resultSet = getAdvancedStatement().getResultSet();
			else
			{
			    resultSet = m_driverResultSet;
			    m_driverResultSet = null;
			}	        		
		}
		catch( OdaException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_GET_RESULTSET, ex );
		}
		catch( UnsupportedOperationException ex )
		{
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
		checkNamedResultsSupport();
		
		IResultSet resultset = null;
		
		try
		{
			resultset = 
			    getAdvancedStatement().getResultSet( resultSetName );
		}
		catch( OdaException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_GET_NAMED_RESULTSET, ex, 
			                         new Object[] { resultSetName } );
		}
		catch( UnsupportedOperationException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_GET_NAMED_RESULTSET, ex, 
			                         new Object[] { resultSetName } );
		}
		
		ResultSet rs = 
			new ResultSet( resultset, doGetMetaData( resultSetName ) );
		
		// keep this as the current named result set, so the caller can 
		// get the metadata from the result set from the statement
		getNamedCurrentResultSets().put( resultSetName, rs );
		
		// reset the current result class for the given result set name, so 
		// subsequent changes won't apply to the existing result set
		getNamedCurrentResultClasses().remove( resultSetName );
		
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
		checkOutputParameterSupport( );
		
		try
		{
			return getAdvancedStatement().findOutParameter( paramName );
		}
		catch( OdaException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_FIND_OUT_PARAMETER, ex, 
			                         new Object[] { paramName } );
		}
		catch( UnsupportedOperationException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_FIND_OUT_PARAMETER, ex, 
			                         new Object[] { paramName } );
		}
	}
	
	/**
	 * Returns the <code>java.sql.Types</code> type for the specified parameter.
	 * @param paramIndex	the 1-based index of the parameter.
	 * @return	the <code>java.sql.Types</code> type of the parameter.
	 * @throws DataException	if data source error occurs.
	 */
	public int getParameterType( int paramIndex ) throws DataException
	{
		try
		{
			return m_statement.getParameterType( paramIndex );
		}
		catch( OdaException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_GET_PARAMETER_TYPE, ex, 
			                         new Object[] { new Integer( paramIndex ) } );
		}
		catch( UnsupportedOperationException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_GET_PARAMETER_TYPE, ex, 
			                         new Object[] { new Integer( paramIndex ) } );
		}
	}
	
	/**
	 * Returns the <code>java.sql.Types</code> type for the specified parameter.
	 * @param paramName	the name of the parameter.
	 * @return	the <code>java.sql.Types</code> type of the parameter.
	 * @throws DataException	if data source error occurs.
	 */
	public int getParameterType( String paramName ) throws DataException
	{
		if( ! supportsNamedParameter() )
		{
			int paramIndex = getIndexFromParamHints( paramName );
			if( paramIndex > 0 )
				return getParameterType( paramIndex );
		}
		
		try
		{
			// either the data source supports named parameters or there were not any hints 
			// for the parameter's position, so we will try the parameter name
			return m_statement.getParameterType( paramName );
		}
		catch( OdaException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_GET_PARAMETER_TYPE, ex, 
			                         new Object[] { paramName } );
		}
		catch( UnsupportedOperationException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_GET_PARAMETER_TYPE, ex, 
			                         new Object[] { paramName } );
		}
	}

	/**
	 * Returns the specified output parameter value.
	 * @param paramIndex	the 1-based index of the parameter.
	 * @return	the output value for the specified parameter.
	 * @throws DataException	if data source error occurs.
	 */
	public Object getParameterValue( int paramIndex ) throws DataException
	{
		checkOutputParameterSupport( );
		
		return getParameterValue( null /* n/a paramName */, paramIndex );
	}
	
	/**
	 * Returns the specified output parameter value.
	 * @param paramName	the name of the parameter.
	 * @return	the output value for the specified parameter.
	 * @throws DataException	if data source error occurs.
	 */
	public Object getParameterValue( String paramName ) throws DataException
	{
		checkOutputParameterSupport( );
		
		return getParameterValue( paramName, 0 /* n/a paramIndex */ );
	}
	
	/**
	 * Closes this <code>Statement</code>.
	 * @throws DataException	if data source error occurs.
	 */
	public void close( ) throws DataException
	{
		resetCachedMetadata();
		
		try
		{
			m_statement.close( );
		}
		catch( OdaException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_CLOSE_STATEMENT, ex );
		}
		catch( UnsupportedOperationException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_CLOSE_STATEMENT, ex );
		}
	}
	
	private void resetCachedMetadata()
	{
		resetCurrentMetaData();
		
		if( m_namedCurrentResultSets != null )
			m_namedCurrentResultSets.clear();
		
		if( m_namedCurrentResultClasses != null )
			m_namedCurrentResultClasses.clear();
	}

	/**
	 * Adds a <code>ColumnHint</code> for this statement to map design time 
	 * column projections with runtime result set metadata.
	 * @param columnHint	a <code>ColumnHint</code> instance.
	 * @throws DataException	if data source error occurs.
	 */
	public void addColumnHint( ColumnHint columnHint ) throws DataException
	{
		if( columnHint == null )
			return;
		
		// no need to reset the current metadata because adding a column 
		// hint doesn't change the existing columns that are being projected, 
		// it just updates some of the column metadata
		getProjectedColumns().addHint( columnHint );
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
		checkNamedResultsSupport();
		
		if( columnHint == null )
			return;
		
		// no need to reset the current metadata because adding a column 
		// hint doesn't change the existing columns that are being projected, 
		// it just updates some of the column metadata
		getProjectedColumns( resultSetName ).addHint( columnHint );
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
		if( paramHint == null )
			return;
		
		validateAndAddParameterHint( paramHint );
		
		// if we've successfully added a parameter hint, then we need to invalidate 
		// previous version of parameter metadata
		m_parameterMetaData = null;
	}

	private void validateAndAddParameterHint( ParameterHint newParameterHint )
		throws DataException
	{
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
				throw new DataException( ResourceConstants.SAME_PARAM_NAME_FOR_DIFFERENT_HINTS,
										 new Object[] { existingParamHintName } );

			// same parameter hint name and parameter hint index, so we're 
			// referring to the same hint, just update the existing one with 
			// the new info
			existingParamHint.updateHint( newParameterHint );
			return;
		}
		
		// new hint name didn't match any of the existing hints, so we'll need to add 
		// it to the list.
		parameterHintsList.add( newParameterHint );
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
		resetCurrentMetaData();
		getProjectedColumns().setProjectedNames( projectedNames );
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
		checkNamedResultsSupport();
		resetCurrentMetaData( resultSetName );
		getProjectedColumns( resultSetName ).setProjectedNames( projectedNames );
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
		assert columnName != null;
		assert columnName.length() != 0;

		// need to reset current metadata because a custom column could be 
		// declared after we projected all columns, which means we would 
		// want to project the newly declared custom column as well
		resetCurrentMetaData();
		getProjectedColumns().addCustomColumn( columnName, columnType);
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
		checkNamedResultsSupport();
		
		assert columnName != null;
		assert columnName.length() != 0;
		
		// need to reset current metadata because a custom column could be 
		// declared after we projected all columns, which means we would 
		// want to project the newly declared custom column as well
		resetCurrentMetaData( resultSetName );
		getProjectedColumns( resultSetName ).addCustomColumn( columnName, 
		                                                      columnType );
	}
	
	// if a caller tries to add custom columns or sets a new set of 
	// column projection, then we want to generate a new set of metadata 
	// for m_currentResultClass or the specified result set name.  we also 
	// no longer want to keep the reference to the m_currentResultSet or 
	// the reference associated with the result set name because we would 
	// no longer be interested in its metadata afterwards.
	private void resetCurrentMetaData()
	{
		m_currentResultClass = null;
		m_currentResultSet = null;
	}
	
	private void resetCurrentMetaData( String resultSetName )
	{
		getNamedCurrentResultClasses().remove( resultSetName );
		getNamedCurrentResultSets().remove( resultSetName );
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
	
	private IStatement getStatement( )
	{
		return m_statement;
	}
	
	private IAdvancedStatement getAdvancedStatement()
	{
	    assert ( isAdvancedStatement() );
	    return (IAdvancedStatement) m_statement;
	}
	
	private boolean isAdvancedStatement( )
	{
		return ( m_statement instanceof IAdvancedStatement );
	}
	
	private boolean supportsNamedResults() throws DataException
	{
		if( m_supportsNamedResults != UNKNOWN )
			return ( m_supportsNamedResults == TRUE );

		// else it's unknown right now
		boolean b = 
			m_connection.getMetaData( m_dataSetType ).supportsNamedResultSets( );
		m_supportsNamedResults = b ? TRUE : FALSE;
		return b;
	}

	private boolean supportsOutputParameter() throws DataException
	{
		if( m_supportsOutputParameters != UNKNOWN )
			return ( m_supportsOutputParameters == TRUE );
		
		// else it's unknown
		boolean b =
			m_connection.getMetaData( m_dataSetType ).supportsOutParameters();
		m_supportsOutputParameters = b ? TRUE : FALSE;
		return b;
	}
	
	private boolean supportsNamedParameter() throws DataException
	{
		if( m_supportsNamedParameters != UNKNOWN )
			return ( m_supportsNamedParameters == TRUE );
		
		// else it's unknown
		boolean b =
			m_connection.getMetaData( m_dataSetType ).supportsNamedParameters();
		m_supportsNamedParameters = b ? TRUE : FALSE;
		return b;
	}
	
	private void checkNamedResultsSupport( ) 
		throws DataException
	{
		// this can only support named result sets if the underlying object is at 
		// least an ICallStatement
		if( ! isAdvancedStatement( ) || ! supportsNamedResults() )
			throw new DataException( ResourceConstants.NAMED_RESULTSETS_UNSUPPORTED, 
									 new UnsupportedOperationException() );
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
		if( m_parameterMetaData != null )
			return m_parameterMetaData;
		
		// there may be two ways that the underlying driver may indicate that 
		// the driver doesn't support the optional IParameterMetaData interface: 
		// 1. returns a null for getParameterMetaData()
		// 2. throws an UnsupportedOperationException or OdaException (ODA MySQL JDBC) 
		//	  for getParameterMetaData()
		// 
		IParameterMetaData runtimeParamMetaData = null;
		try
		{
			runtimeParamMetaData = m_statement.getParameterMetaData();
		}
		catch( UnsupportedOperationException ex )
		{
			runtimeParamMetaData = null;
		}
		catch( OdaException ex )
		{
			runtimeParamMetaData = null;
		}
		
		m_parameterMetaData = ( runtimeParamMetaData == null ) ?
							  mergeParamHints() :
							  mergeParamHintsWithMetaData( runtimeParamMetaData );
									   
		return m_parameterMetaData;
	}
	
	private Collection mergeParamHints() throws DataException
	{
		ArrayList parameterMetaData = null;
		
		// add the parameter hints, if any.
		if( m_parameterHints != null && m_parameterHints.size() > 0 )
		{
			parameterMetaData = new ArrayList();
			addParameterHints( parameterMetaData, m_parameterHints );
		}
	
		return parameterMetaData;
	}

	private void addParameterHints( List parameterMetaData, List parameterHints )
	{
		ListIterator iter = parameterHints.listIterator();
		while( iter.hasNext() )
		{
			ParameterHint paramHint = (ParameterHint) iter.next();
			ParameterMetaData paramMd = new ParameterMetaData( paramHint );
			parameterMetaData.add( paramMd );						
		}
	}
	
	private Collection mergeParamHintsWithMetaData( IParameterMetaData runtimeParamMetaData )
		throws DataException
	{
		assert( runtimeParamMetaData != null );
		
		int numOfParameters = doGetParameterCount( runtimeParamMetaData );
		ArrayList paramMetaData = new ArrayList( numOfParameters );
		
		for( int i = 1; i <= numOfParameters; i++ )
		{
			ParameterMetaData paramMd = 
				new ParameterMetaData( runtimeParamMetaData, i, 
				                       m_connection.getDriverName(), 
				                       m_dataSetType );
			paramMetaData.add( paramMd );
		}
		
		if( m_parameterHints != null && m_parameterHints.size() > 0 )
			updateWithParameterHints( paramMetaData, m_parameterHints );

		return paramMetaData;
	}
	
	private int doGetParameterCount( IParameterMetaData runtimeParamMetaData )
		throws DataException
	{
		try
		{
			return runtimeParamMetaData.getParameterCount();
		}
		catch( OdaException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_GET_PARAMETER_COUNT, ex );
		}
		catch( UnsupportedOperationException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_GET_PARAMETER_COUNT, ex );
		}
	}
	
	private void updateWithParameterHints( List parameterMetaData, 
									   	   List parameterHints )
		throws DataException
	{
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
	}

	private int getRuntimeParameterIndexFromName( String paramName, boolean forInput )
		throws DataException
	{
		if( forInput )
		{
			try
			{	
				return findInParameter( paramName );
			}
			catch( DataException ex )
			{
				// findInParameter is not supported
				if( ex.getCause() instanceof UnsupportedOperationException )
					return 0;
				
				throw ex;
			}
		}
		
		try
		{
			return findOutParameter( paramName );
		}
		catch( DataException ex )
		{
			// findOutParameter is not supported
			if( ex.getCause() instanceof UnsupportedOperationException )
				return 0;
			
			throw ex;
		}
	}
	
	private void checkOutputParameterSupport( ) 
		throws DataException
	{
		// this can only support output parameter if the underlying object is at 
		// least an ICallStatement
		if( ! isAdvancedStatement( ) || ! supportsOutputParameter() ) 
			throw new DataException( ResourceConstants.OUTPUT_PARAMETERS_UNSUPPORTED, 
									 new UnsupportedOperationException() );
	}
	
	private Object getParameterValue( String paramName, int paramIndex ) 
		throws DataException
	{
		checkOutputParameterSupport( );
		
		Object paramValue = null;
		int nativeType = ( paramName == null ) ? getParameterType( paramIndex ) :
												 getParameterType( paramName );
		
		String driverName = m_connection.getDriverName( );
		
		// NULL means that the driver doesn't know or care about the type of the 
		// parameter, so try to look for it in the hints.  If the hints doesn't have 
		// any type info, then getOdaTypeFromParamHints() will return the safest default 
		// type, Types.CHAR.
		int paramType = ( nativeType == Types.NULL ) ?
						getOdaTypeFromParamHints( paramName, paramIndex ) :
						DriverManager.getInstance().getNativeToOdaMapping( driverName,
																		   m_dataSetType, 
																		   nativeType );

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
				
			default:
				assert false;	// exception now thrown by DriverManager
		}
		
		return ( wasNull( ) ) ? null : paramValue;
	}
	
	// the following six getters are by name and need additional processing in the 
	// case where a named parameter is not supported by the underlying data source.  
	// In that case, we will look at the output parameter hints to get the name to 
	// id mapping
	private int getInt( String paramName ) throws DataException
	{
		if( ! supportsNamedParameter() )
		{
			int paramIndex = getIndexFromParamHints( paramName );
			if( paramIndex > 0 )
				return doGetInt( paramIndex );
		}
		
		return doGetInt( paramName );
	}

	private double getDouble( String paramName ) throws DataException
	{
		if( ! supportsNamedParameter() )
		{
			int paramIndex = getIndexFromParamHints( paramName );
			if( paramIndex > 0 )
				return doGetDouble( paramIndex );
		}
		
		return doGetDouble( paramName );
	}
	
	private String getString( String paramName ) throws DataException
	{
		if( ! supportsNamedParameter() )
		{
			int paramIndex = getIndexFromParamHints( paramName );
			if( paramIndex > 0 )
				return doGetString( paramIndex );
		}
		
		return doGetString( paramName );
	}
	
	private BigDecimal getBigDecimal( String paramName ) throws DataException
	{
		if( ! supportsNamedParameter() )
		{
			int paramIndex = getIndexFromParamHints( paramName );
			if( paramIndex > 0 )
				return doGetBigDecimal( paramIndex );
		}
		
		return doGetBigDecimal( paramName );
	}

	private java.util.Date getDate( String paramName ) throws DataException
	{
		if( ! supportsNamedParameter() )
		{
			int paramIndex = getIndexFromParamHints( paramName );
			if( paramIndex > 0 )
				return doGetDate( paramIndex );
		}
		
		return doGetDate( paramName );
	}
	
	private Time getTime( String paramName ) throws DataException
	{
		if( ! supportsNamedParameter() )
		{
			int paramIndex = getIndexFromParamHints( paramName );
			if( paramIndex > 0 )
				return doGetTime( paramIndex );
		}
		
		return doGetTime( paramName );
	}
	
	private Timestamp getTimestamp( String paramName ) throws DataException
	{
		if( ! supportsNamedParameter() )
		{
			int paramIndex = getIndexFromParamHints( paramName );
			if( paramIndex > 0 )
				return doGetTimestamp( paramIndex );
		}
		
		return doGetTimestamp( paramName );
	}
	
	private int doGetInt( int paramIndex ) throws DataException
	{
		try
		{
			return getAdvancedStatement().getInt( paramIndex );
		}
		catch( OdaException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_GET_INT_FROM_PARAMETER, ex, 
			                         new Object[] { new Integer( paramIndex ) } );
		}
		catch( UnsupportedOperationException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_GET_INT_FROM_PARAMETER, ex, 
			                         new Object[] { new Integer( paramIndex ) } );
		}
	}
	
	private int doGetInt( String paramName ) throws DataException
	{
		try
		{
			return getAdvancedStatement().getInt( paramName );
		}
		catch( OdaException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_GET_INT_FROM_PARAMETER, ex, 
			                         new Object[] { paramName } );
		}
		catch( UnsupportedOperationException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_GET_INT_FROM_PARAMETER, ex, 
			                         new Object[] { paramName } );
		}
	}
	
	private double doGetDouble( int paramIndex ) throws DataException
	{
		try
		{
			return getAdvancedStatement().getDouble( paramIndex );
		}
		catch( OdaException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_GET_DOUBLE_FROM_PARAMETER, ex, 
			                         new Object[] { new Integer( paramIndex ) } );
		}
		catch( UnsupportedOperationException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_GET_DOUBLE_FROM_PARAMETER, ex, 
			                         new Object[] { new Integer( paramIndex ) } );
		}
	}
	
	private double doGetDouble( String paramName ) throws DataException
	{
		try
		{
			return getAdvancedStatement().getDouble( paramName );
		}
		catch( OdaException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_GET_DOUBLE_FROM_PARAMETER, ex, 
			                         new Object[] { paramName } );
		}
		catch( UnsupportedOperationException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_GET_DOUBLE_FROM_PARAMETER, ex, 
			                         new Object[] { paramName } );
		}
	}
	
	private String doGetString( int paramIndex ) throws DataException
	{
		try
		{
			return getAdvancedStatement().getString( paramIndex );
		}
		catch( OdaException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_GET_STRING_FROM_PARAMETER, ex, 
			                         new Object[] { new Integer( paramIndex ) } );
		}
		catch( UnsupportedOperationException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_GET_STRING_FROM_PARAMETER, ex, 
			                         new Object[] { new Integer( paramIndex ) } );			
		}
	}
	
	private String doGetString( String paramName ) throws DataException
	{
		try
		{
			return getAdvancedStatement().getString( paramName );
		}
		catch( OdaException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_GET_STRING_FROM_PARAMETER, ex, 
			                         new Object[] { paramName } );
		}
		catch( UnsupportedOperationException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_GET_STRING_FROM_PARAMETER, ex, 
			                         new Object[] { paramName } );
		}
	}
	
	private BigDecimal doGetBigDecimal( int paramIndex ) throws DataException
	{
		try
		{
			return getAdvancedStatement().getBigDecimal( paramIndex );
		}
		catch( OdaException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_GET_BIGDECIMAL_FROM_PARAMETER, ex, 
			                         new Object[] { new Integer( paramIndex ) } );
		}
		catch( UnsupportedOperationException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_GET_BIGDECIMAL_FROM_PARAMETER, ex, 
			                         new Object[] { new Integer( paramIndex ) } );
		}
	}
	
	private BigDecimal doGetBigDecimal( String paramName ) throws DataException
	{
		try
		{
			return getAdvancedStatement().getBigDecimal( paramName );
		}
		catch( OdaException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_GET_BIGDECIMAL_FROM_PARAMETER, ex, 
			                         new Object[] { paramName } );
		}
		catch( UnsupportedOperationException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_GET_BIGDECIMAL_FROM_PARAMETER, ex, 
			                         new Object[] { paramName } );
		}
	}
	
	private java.util.Date doGetDate( int paramIndex ) throws DataException
	{
		try
		{
			return getAdvancedStatement().getDate( paramIndex );
		}
		catch( OdaException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_GET_DATE_FROM_PARAMETER, ex, 
			                         new Object[] { new Integer( paramIndex ) } );
		}
		catch( UnsupportedOperationException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_GET_DATE_FROM_PARAMETER, ex, 
			                         new Object[] { new Integer( paramIndex ) } );
		}
	}
	
	private java.util.Date doGetDate( String paramName ) throws DataException
	{
		try
		{
			return getAdvancedStatement().getDate( paramName );
		}
		catch( OdaException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_GET_DATE_FROM_PARAMETER, ex, 
			                         new Object[] { paramName } );
		}
		catch( UnsupportedOperationException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_GET_DATE_FROM_PARAMETER, ex, 
			                         new Object[] { paramName } );
		}
	}
	
	private Time doGetTime( int paramIndex ) throws DataException
	{
		try
		{
			return getAdvancedStatement().getTime( paramIndex );
		}
		catch( OdaException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_GET_TIME_FROM_PARAMETER, ex, 
			                         new Object[] { new Integer( paramIndex ) } );
		}
		catch( UnsupportedOperationException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_GET_TIME_FROM_PARAMETER, ex, 
			                         new Object[] { new Integer( paramIndex ) } );
		}
	}
	
	private Time doGetTime( String paramName ) throws DataException
	{
		try
		{
			return getAdvancedStatement().getTime( paramName );
		}
		catch( OdaException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_GET_TIME_FROM_PARAMETER, ex, 
			                         new Object[] { paramName } );
		}
		catch( UnsupportedOperationException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_GET_TIME_FROM_PARAMETER, ex, 
			                         new Object[] { paramName } );
		}
	}
	
	private Timestamp doGetTimestamp( int paramIndex ) throws DataException
	{
		try
		{
			return getAdvancedStatement().getTimestamp( paramIndex );
		}
		catch( OdaException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_GET_TIMESTAMP_FROM_PARAMETER, ex, 
			                         new Object[] { new Integer( paramIndex ) } );
		}
		catch( UnsupportedOperationException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_GET_TIMESTAMP_FROM_PARAMETER, ex, 
			                         new Object[] { new Integer( paramIndex ) } );
		}
	}
	
	private Timestamp doGetTimestamp( String paramName ) throws DataException
	{
		try
		{
			return getAdvancedStatement().getTimestamp( paramName );
		}
		catch( OdaException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_GET_TIMESTAMP_FROM_PARAMETER, ex,
			                         new Object[] { paramName } );
		}
		catch( UnsupportedOperationException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_GET_TIMESTAMP_FROM_PARAMETER, ex,
			                         new Object[] { paramName } );
		}
	}
	
	private boolean wasNull() throws DataException
	{
		try
		{
			return getAdvancedStatement().wasNull();
		}
		catch( OdaException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_DETERMINE_WAS_NULL, ex );
		}
		catch( UnsupportedOperationException ex )
		{
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
		try
		{
			getStatement().clearInParameters();
		}
		catch( OdaException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_CLEAR_IN_PARAMETERS, ex );
		}
		catch( AbstractMethodError err )
		{
			handleUnsupportedClearInParameters();
		}
		catch( UnsupportedOperationException ex )
		{
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
	}

	// provide a work-around for older ODA drivers or ODA drivers that 
	// don't support the clearInParameters call
	// the workaround involves creating a new instance of the underlying 
	// ODA statement and setting it back up to the state of the current 
	// statement
	private void handleUnsupportedClearInParameters() throws DataException
	{
		m_statement = m_connection.prepareOdaStatement( m_query, 
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
		try
		{
			return getStatement( ).findInParameter( paramName );
		}
		catch( OdaException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_FIND_IN_PARAMETER, ex, 
			                         new Object[] { paramName } );
		}
		catch( UnsupportedOperationException ex )
		{
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
		if( paramValue == null )
		{
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
				throw (RuntimeException) lastException;
			else if( lastException instanceof DataException )
				throw (DataException) lastException;
			else
			{
				String localizedMessage = 
					DataResourceHandle.getInstance().getMessage( ResourceConstants.UNKNOWN_EXCEPTION_THROWN );
				IllegalStateException ex = 
					new IllegalStateException( localizedMessage );
				ex.initCause( lastException );
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
		if( supportsNamedParameter() )
		{
			doSetInt( paramName, i );
			return;
		}
		
		if( ! setIntUsingHints( paramName, i ) )
			throw new DataException( ResourceConstants.CANNOT_SET_INT_PARAMETER, 
									 new Object[] { paramName } );
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
		if( supportsNamedParameter() )
		{
			doSetDouble( paramName, d );
			return;
		}
		
		if( ! setDoubleUsingHints( paramName, d ) )
			throw new DataException( ResourceConstants.CANNOT_SET_DOUBLE_PARAMETER, 
									 new Object[] { paramName } );
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
		if( supportsNamedParameter() )
		{
			doSetString( paramName, stringValue );
			return;
		}
		
		if( ! setStringUsingHints( paramName, stringValue ) )
			throw new DataException( ResourceConstants.CANNOT_SET_STRING_PARAMETER, 
									 new Object[] { paramName } );
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
		if( supportsNamedParameter() )
		{
			doSetBigDecimal( paramName, decimal );
			return;
		}
		
		if( ! setBigDecimalUsingHints( paramName, decimal ) )
			throw new DataException( ResourceConstants.CANNOT_SET_BIGDECIMAL_PARAMETER, 
									 new Object[] { paramName } );
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
		if( supportsNamedParameter() )
		{
			doSetDate( paramName, date );
			return;
		}
		
		if( ! setDateUsingHints( paramName, date ) )
			throw new DataException( ResourceConstants.CANNOT_SET_DATE_PARAMETER, 
									 new Object[] { paramName } );
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
		if( supportsNamedParameter() )
		{
			doSetTime( paramName, time );
			return;
		}
		
		if( ! setTimeUsingHints( paramName, time ) )
			throw new DataException( ResourceConstants.CANNOT_SET_TIME_PARAMETER, 
									 new Object[] { paramName } );
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
		if( supportsNamedParameter() )
		{
			doSetTimestamp( paramName, timestamp );
			return;
		}
		
		if( ! setTimestampUsingHints( paramName, timestamp ) )
			throw new DataException( ResourceConstants.CANNOT_SET_TIMESTAMP_PARAMETER, 
									 new Object[] { paramName } );
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
		try
		{
			getStatement().setInt( paramIndex, i );
		}
		catch( OdaException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_SET_INT_PARAMETER, ex, 
			                         new Object[] { new Integer( paramIndex ) } );
		}
		catch( UnsupportedOperationException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_SET_INT_PARAMETER, ex, 
			                         new Object[] { new Integer( paramIndex ) } );
		}
	}
	
	private void doSetInt( String paramName, int i ) throws DataException
	{
		try
		{
			getStatement().setInt( paramName, i );
		}
		catch( OdaException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_SET_INT_PARAMETER, ex, 
			                         new Object[] { paramName } );
		}
		catch( UnsupportedOperationException ex )
		{
			// first try to set value by position if the parameter hints provide name-to-position mapping,  
			// otherwise we need to wrap the UnsupportedOperationException up and throw it
			if( ! setIntUsingHints( paramName, i ) )
				throw new DataException( ResourceConstants.CANNOT_SET_INT_PARAMETER, ex, 
										 new Object[] { paramName } );
		}
	}

	private void doSetDouble( int paramIndex, double d ) throws DataException
	{
		try
		{
			getStatement().setDouble( paramIndex, d );
		}
		catch( OdaException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_SET_DOUBLE_PARAMETER, ex, 
			                         new Object[] { new Integer( paramIndex ) } );
		}
		catch( UnsupportedOperationException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_SET_DOUBLE_PARAMETER, ex, 
			                         new Object[] { new Integer( paramIndex ) } );
		}
	}
	
	
	private void doSetDouble( String paramName, double d ) throws DataException
	{
		try
		{
			getStatement().setDouble( paramName, d );
		}
		catch( OdaException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_SET_DOUBLE_PARAMETER, ex, 
			                         new Object[] { paramName } );
		}
		catch( UnsupportedOperationException ex )
		{
			// first try to set value by position if the parameter hints provide name-to-position mapping,  
			// otherwise we need to wrap the UnsupportedOperationException up and throw it
			if( ! setDoubleUsingHints( paramName, d ) )
				throw new DataException( ResourceConstants.CANNOT_SET_DOUBLE_PARAMETER, ex, 
				                         new Object[] { paramName } );
		}
	}
	
	private void doSetString( int paramIndex, String stringValue ) throws DataException
	{
		try
		{
			getStatement().setString( paramIndex, stringValue );
		}
		catch( OdaException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_SET_STRING_PARAMETER, ex, 
			                         new Object[] { new Integer( paramIndex ) } );
		}
		catch( UnsupportedOperationException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_SET_STRING_PARAMETER, ex, 
			                         new Object[] { new Integer( paramIndex ) } );
		}
	}
	
	private void doSetString( String paramName, String stringValue ) throws DataException
	{
		try
		{
			getStatement().setString( paramName, stringValue );
		}
		catch( OdaException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_SET_STRING_PARAMETER, ex, 
			                         new Object[] { paramName } );
		}
		catch( UnsupportedOperationException ex )
		{
			// first try to set value by position if the parameter hints provide name-to-position mapping,  
			// otherwise we need to wrap the UnsupportedOperationException up and throw it
			if( ! setStringUsingHints( paramName, stringValue ) )
				throw new DataException( ResourceConstants.CANNOT_SET_STRING_PARAMETER, ex, 
				                         new Object[] { paramName } );
		}
	}
	
	private void doSetBigDecimal( int paramIndex, BigDecimal decimal ) throws DataException
	{
		try
		{
			getStatement().setBigDecimal( paramIndex, decimal );
		}
		catch( OdaException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_SET_BIGDECIMAL_PARAMETER, ex,
			                         new Object[] { new Integer( paramIndex ) } );
		}
		catch( UnsupportedOperationException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_SET_BIGDECIMAL_PARAMETER, ex,
			                         new Object[] { new Integer( paramIndex ) } );
		}
	}
	
	private void doSetBigDecimal( String paramName, BigDecimal decimal ) throws DataException
	{
		try
		{
			getStatement().setBigDecimal( paramName, decimal );
		}
		catch( OdaException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_SET_BIGDECIMAL_PARAMETER, ex, 
			                         new Object[] { paramName } );
		}
		catch( UnsupportedOperationException ex )
		{
			// first try to set value by position if the parameter hints provide name-to-position mapping,  
			// otherwise we need to wrap the UnsupportedOperationException up and throw it
			if( ! setBigDecimalUsingHints( paramName, decimal ) )
				throw new DataException( ResourceConstants.CANNOT_SET_BIGDECIMAL_PARAMETER, ex, 
				                         new Object[] { paramName } );
		}
	}
	
	private void doSetDate( int paramIndex, Date date ) throws DataException
	{
		try
		{
			getStatement().setDate( paramIndex, date );
		}
		catch( OdaException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_SET_DATE_PARAMETER, ex,
			                         new Object[] { new Integer( paramIndex ) } );
		}
		catch( UnsupportedOperationException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_SET_DATE_PARAMETER, ex,
			                         new Object[] { new Integer( paramIndex ) } );
		}
	}
	
	private void doSetDate( String paramName, Date date ) throws DataException
	{
		try
		{
			getStatement().setDate( paramName, date );
		}
		catch( OdaException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_SET_DATE_PARAMETER, ex, 
			                         new Object[] { paramName } );
		}
		catch( UnsupportedOperationException ex )
		{
			// first try to set value by position if the parameter hints provide name-to-position mapping,  
			// otherwise we need to wrap the UnsupportedOperationException up and throw it
			if( ! setDateUsingHints( paramName, date ) )
				throw new DataException( ResourceConstants.CANNOT_SET_DATE_PARAMETER, ex, 
				                         new Object[] { paramName } );
		}
	}
	
	private void doSetTime( int paramIndex, Time time ) throws DataException
	{
		try
		{
			getStatement().setTime( paramIndex, time );
		}
		catch( OdaException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_SET_TIME_PARAMETER, ex, 
			                         new Object[] { new Integer( paramIndex ) } );
		}
		catch( UnsupportedOperationException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_SET_TIME_PARAMETER, ex, 
			                         new Object[] { new Integer( paramIndex ) } );
		}
	}
	
	private void doSetTime( String paramName, Time time ) throws DataException
	{
		try
		{
			getStatement().setTime( paramName, time );
		}
		catch( OdaException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_SET_TIME_PARAMETER, ex, 
			                         new Object[] { paramName } );
		}
		catch( UnsupportedOperationException ex )
		{
			// first try to set value by position if the parameter hints provide name-to-position mapping,  
			// otherwise we need to wrap the UnsupportedOperationException up and throw it
			if( ! setTimeUsingHints( paramName, time ) )
				throw new DataException( ResourceConstants.CANNOT_SET_TIME_PARAMETER, ex, 
				                         new Object[] { paramName } );
		}
	}
	
	private void doSetTimestamp( int paramIndex, Timestamp timestamp ) throws DataException
	{
		try
		{
			getStatement().setTimestamp( paramIndex, timestamp );
		}
		catch( OdaException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_SET_TIMESTAMP_PARAMETER, ex, 
			                         new Object[] { new Integer( paramIndex ) } );
		}
		catch( UnsupportedOperationException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_SET_TIMESTAMP_PARAMETER, ex, 
			                         new Object[] { new Integer( paramIndex ) } );
		}
	}
	
	private void doSetTimestamp( String paramName, Timestamp timestamp ) throws DataException
	{
		try
		{
			getStatement().setTimestamp( paramName, timestamp );
		}
		catch( OdaException ex )
		{
			throw new DataException( ResourceConstants.CANNOT_SET_TIMESTAMP_PARAMETER, ex, 
			                         new Object[] { paramName } );
		}
		catch( UnsupportedOperationException ex )
		{
			// first try to set value by position if the parameter hints provide name-to-position mapping,  
			// otherwise we need to wrap the UnsupportedOperationException up and throw it
			if( ! setTimestampUsingHints( paramName, timestamp ) )
				throw new DataException( ResourceConstants.CANNOT_SET_TIMESTAMP_PARAMETER, ex, 
				                         new Object[] { paramName } );
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
