/*******************************************************************************
* Copyright (c) 2004, 2005 Actuate Corporation.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*  Actuate Corporation  - initial API and implementation
*******************************************************************************/ 

package org.eclipse.birt.data.engine.odaconsumer;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.ResultClass;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.oda.ICallStatement;
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
	
	private ArrayList m_inputParameterHints;
	private ArrayList m_outputParameterHints;
	
	private ProjectedColumns m_projectedColumns;
	private IResultClass m_currentResultClass;
	private ResultSet m_currentResultSet;
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
		throws OdaException
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
	 * @throws OdaException	if data source error occurs.
	 */
	public void setProperty( String name, String value ) throws OdaException
	{
		m_statement.setProperty( name, value );
		
		// save the properties in a list in case we need them later, 
		// i.e. support clearParameterValues() for drivers that don't support
		// the ODA operation
		getPropertiesList().add( new Property( name, value ) );
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
	 * @throws OdaException	if data source error occurs.
	 */
	public void setSortSpec( SortSpec sortBy ) throws OdaException
	{
		m_statement.setSortSpec( sortBy );
		
		getSortSpecsList().add( sortBy );
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
	 * @throws OdaException	if data source error occurs.
	 */
	public void setMaxRows( int max ) throws OdaException
	{
		m_statement.setMaxRows( max );
		
		m_maxRows = max;
	}
	
	/**
	 * Returns an <code>IResultClass</code> representing the metadata of the 
	 * result set for this <code>Statement</code>.
	 * @return	the <code>IResultClass</code> for the result set.
	 * @throws OdaException	if data source error occurs.
	 */
	public IResultClass getMetaData( ) throws OdaException
	{
		// we can get the current result set's metadata directly from the 
		// current result set handle rather than go through ODA
		if( m_currentResultSet != null )
			return m_currentResultSet.getMetaData();
		
		return doGetMetaData();
	}
	
	private IResultClass doGetMetaData() throws OdaException
	{	
		if( m_currentResultClass != null )
			return m_currentResultClass;
		
		List projectedColumns = 
			getProjectedColumns().getColumnsMetadata();
		m_currentResultClass = doGetResultClass( projectedColumns );
		return m_currentResultClass;
	}

	private ResultClass doGetResultClass( List projectedColumns ) 
		throws OdaException
	{
		assert( projectedColumns != null );
		try
		{
			return new ResultClass( projectedColumns );
		}
		catch ( DataException e )
		{
			// TODO: this violates the layering of exception classes
			throw new OdaException( e.getLocalizedMessage() );
		}
	}
	
	private ProjectedColumns getProjectedColumns() 
		throws OdaException
	{
		if( m_projectedColumns == null )
		{
			IResultSetMetaData odaMetadata = m_statement.getMetaData();
			m_projectedColumns = doGetProjectedColumns( odaMetadata );
		}	
		else if( m_updateProjectedColumns )
		{
			// need to update the projected columns of the un-named result 
			// set with the newest runtime metadata, don't use the cached 
			// one
			IResultSetMetaData odaMetadata = m_statement.getMetaData();
			ProjectedColumns newProjectedColumns = 
				doGetProjectedColumns( odaMetadata );
			updateProjectedColumns( newProjectedColumns, m_projectedColumns );
			m_projectedColumns = newProjectedColumns;
			
			// reset the update flag
			m_updateProjectedColumns = false;
		}
		
		return m_projectedColumns;
	}
	
	private ProjectedColumns doGetProjectedColumns( IResultSetMetaData odaMetadata )
		throws OdaException
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
	 * @throws OdaException	if data source error occurs.
	 */
	public IResultClass getMetaData( String resultSetName ) throws OdaException
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
	
	private IResultClass doGetMetaData( String resultSetName ) throws OdaException
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
		throws OdaException
	{
		ProjectedColumns projectedColumns = 
			(ProjectedColumns) getNamedProjectedColumns().get( resultSetName );
		if( projectedColumns == null )
		{
			IResultSetMetaData odaMetadata = 
				( (ICallStatement) m_statement ).getMetaDataOf( resultSetName );
			projectedColumns = doGetProjectedColumns( odaMetadata );	
			getNamedProjectedColumns().put( resultSetName, projectedColumns );
		}
		else if( m_updateNamedProjectedColumns != null && 
				 m_updateNamedProjectedColumns.contains( resultSetName ) )
		{
			// there's an existing ProjectedColumns from the same result set, 
			// and it needs to be updated with the newest runtime metadata
			IResultSetMetaData odaMetadata = 
				( (ICallStatement) m_statement ).getMetaDataOf( resultSetName );
			ProjectedColumns newProjectedColumns = 
				doGetProjectedColumns( odaMetadata );
			updateProjectedColumns( newProjectedColumns, projectedColumns );
			getNamedProjectedColumns().put( resultSetName, newProjectedColumns );
			
			// reset the update flag for this result set name
			m_updateNamedProjectedColumns.remove( resultSetName );
		}
		
		return projectedColumns;
	}

	/**
	 * Executes the statement's query.
	 * @return	true if this has at least one result set; false otherwise
	 * @throws OdaException	if data source error occurs.
	 */
	public boolean execute( ) throws OdaException
	{
		// when the statement is re-executed, then the previous result set(s)
		// needs to be invalidated.
		resetCurrentResultSets();
		
		// this will get the result set metadata for the ResultSet in a subsequent
		// getResultSet() call.  Getting the underlying metadata after the statement 
		// has been executed may reset its state which will cause the result set not 
		// to have any data
		doGetMetaData();
		return m_statement.execute( );
	}
	
	// clear all cached references to the current result sets, 
	// applies to named and un-named result sets
	private void resetCurrentResultSets()
	{
		m_currentResultSet = null;
		
		if( m_namedCurrentResultSets != null )
			m_namedCurrentResultSets.clear();
	}

	/**
	 * Returns the <code>ResultSet</code> instance.
	 * @return	a <code>ResultSet</code> instance.
	 * @throws OdaException	if data source error occurs.
	 */
	public ResultSet getResultSet( ) throws OdaException
	{
		IResultSet resultSet = m_statement.getResultSet( );
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
	 * @throws OdaException	if data source error occurs.
	 */
	public ResultSet getResultSet( String resultSetName ) throws OdaException
	{
		checkNamedResultsSupport();
		
		IResultSet resultset = 
			( (ICallStatement) m_statement ).getResultSet( resultSetName );
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
	 * @throws OdaException	if data source error occurs.
	 */
	public int findOutParameter( String paramName ) throws OdaException
	{
		checkOutputParameterSupport( );
		
		return ( (ICallStatement) m_statement ).findOutParameter( paramName );
	}
	
	/**
	 * Returns the <code>java.sql.Types</code> type for the specified parameter.
	 * @param paramIndex	the 1-based index of the parameter.
	 * @return	the <code>java.sql.Types</code> type of the parameter.
	 * @throws OdaException	if data source error occurs.
	 */
	public int getParameterType( int paramIndex ) throws OdaException
	{
		return m_statement.getParameterType( paramIndex );
	}
	
	/**
	 * Returns the <code>java.sql.Types</code> type for the specified parameter.
	 * @param paramName	the name of the parameter.
	 * @return	the <code>java.sql.Types</code> type of the parameter.
	 * @throws OdaException	if data source error occurs.
	 */
	public int getParameterType( String paramName ) throws OdaException
	{
		if( ! supportsNamedParameter() )
		{
			int paramIndex = doGetIndexFromParamHints( paramName );
			if( paramIndex > 0 )
				return m_statement.getParameterType( paramIndex );
		}
		
		// either the data source supports named parameters or there were not any hints 
		// for the parameter's position, so we will try the parameter name
		return m_statement.getParameterType( paramName );
	}
	
	// need to check through two different sets of hints to get the parameter 
	// index from the hints
	private int doGetIndexFromParamHints( String paramName )
	{
		int index = getIndexFromParamHints( getOutputParameterHints(), paramName );
		if( index == 0 )
			index = getIndexFromParamHints( getInputParameterHints(), paramName );
		
		return index;
	}

	/**
	 * Returns the specified output parameter value.
	 * @param paramIndex	the 1-based index of the parameter.
	 * @return	the output value for the specified parameter.
	 * @throws OdaException	if data source error occurs.
	 */
	public Object getParameterValue( int paramIndex ) throws OdaException
	{
		checkOutputParameterSupport( );
		
		return getParameterValue( null /* n/a paramName */, paramIndex );
	}
	
	/**
	 * Returns the specified output parameter value.
	 * @param paramName	the name of the parameter.
	 * @return	the output value for the specified parameter.
	 * @throws OdaException	if data source error occurs.
	 */
	public Object getParameterValue( String paramName ) throws OdaException
	{
		checkOutputParameterSupport( );
		
		return getParameterValue( paramName, 0 /* n/a paramIndex */ );
	}
	
	/**
	 * Closes this <code>Statement</code>.
	 * @throws OdaException	if data source error occurs.
	 */
	public void close( ) throws OdaException
	{
		resetCachedMetadata();
		m_statement.close( );
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
	 * @throws OdaException	if data source error occurs.
	 */
	public void addColumnHint( ColumnHint columnHint ) throws OdaException
	{
		if( columnHint == null )
			return;
		
		// no need to reset the current metadata because adding a column 
		// hint doesn't change the existing columns that are being projected, 
		// it just updates some of the column metadata
		getProjectedColumns().addHint( columnHint );
	}
	
	public void addColumnHint( String resultSetName, ColumnHint columnHint )
		throws OdaException
	{
		checkNamedResultsSupport();
		
		if( columnHint == null )
			return;
		
		// no need to reset the current metadata because adding a column 
		// hint doesn't change the existing columns that are being projected, 
		// it just updates some of the column metadata
		getProjectedColumns( resultSetName ).addHint( columnHint );
	}

	/**
	 * Adds an <code>OutputParameterHint</code> for this statement to map 
	 * static output parameter definitions with runtime output parameter 
	 * metadata.
	 * @param outputParamHint	an <code>OutputParameterHint</code> instance.
	 * @throws OdaException	if data source error occurs.
	 */
	public void addOutputParameterHint( OutputParameterHint outputParamHint )
		throws OdaException
	{
		if( outputParamHint == null )
			return;
		
		validateAndAddParameterHint( getOutputParameterHints(), outputParamHint );
	}
	
	private ArrayList getOutputParameterHints()
	{
		if( m_outputParameterHints == null )
			m_outputParameterHints = new ArrayList();
		
		return m_outputParameterHints;
	}
	
	private void validateAndAddParameterHint( ArrayList parameterHintsList, 
											  ParameterHint newParameterHint )
		throws OdaException
	{
		String newParamHintName = newParameterHint.getName();
		int newParamHintIndex = newParameterHint.getPosition();
		for( int i = 0, n = parameterHintsList.size(); i < n; i++ )
		{
			ParameterHint existingParamHint = 
				(ParameterHint) parameterHintsList.get( i );
			
			if( ! existingParamHint.getName().equals( newParamHintName ) )
			{
				// different names and parameter index is either 0 or didn't 
				// match, so keep on looking
				if( newParamHintIndex == 0 ||
					existingParamHint.getPosition() != newParamHintIndex )
					continue;
				
				// new parameter hint has a different name for the same 
				// non-zero parameter index, so update the existing one
				existingParamHint.updateHint( newParameterHint );
				return;
			}

			// the name of the existing hint matches the new hint, 
			// but the parameter index didn't match
			// TODO externalize message text
			if( existingParamHint.getPosition() != newParamHintIndex )
				throw new OdaException( "Cannot use the same parameter name " +
				                        "to represent different parameter hints." );

			// same parameter hint name and parameter hint index, so we're 
			// referring to the same hint, just update the existing one with 
			// the new info
			existingParamHint.updateHint( newParameterHint );
			return;
		}
		
		// new hint name didn't match any of the existing hints, so add it to the 
		// list
		parameterHintsList.add( newParameterHint );
	}
	
	/**
	 * Sets the names of all projected columns. If this method is not called, 
	 * then all columns in the runtime metadata are projected. The specified 
	 * projected names can be either a column name or column alias.
	 * @param projectedNames	the projected column names.
	 * @throws OdaException	if data source error occurs.
	 */
	public void setColumnsProjection( String[] projectedNames ) 
		throws OdaException
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
	 * @throws OdaException	if data source error occurs.
	 */
	public void setColumnsProjection( String resultSetName, String[] projectedNames ) 
		throws OdaException
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
	 * @throws OdaException	if data source error occurs.
	 */
	public void declareCustomColumn( String columnName, Class columnType )
		throws OdaException
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
	 * @throws OdaException	if data source error occurs.
	 */
	public void declareCustomColumn( String resultSetName, String columnName, 
									 Class columnType ) throws OdaException
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
	
	private boolean isCallStatement( )
	{
		return ( m_statement instanceof ICallStatement );
	}
	
	private boolean supportsNamedResults() throws OdaException
	{
		if( m_supportsNamedResults != UNKNOWN )
			return ( m_supportsNamedResults == TRUE );

		// else it's unknown right now
		boolean b = 
			m_connection.getMetaData( m_dataSetType ).supportsNamedResultSets( );
		m_supportsNamedResults = b ? TRUE : FALSE;
		return b;
	}
	
	private boolean supportsOutputParameter() throws OdaException
	{
		if( m_supportsOutputParameters != UNKNOWN )
			return ( m_supportsOutputParameters == TRUE );
		
		// else it's unknown
		boolean b =
			m_connection.getMetaData( m_dataSetType ).supportsOutParameters();
		m_supportsOutputParameters = b ? TRUE : FALSE;
		return b;
	}
	
	private boolean supportsNamedParameter() throws OdaException
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
		throws OdaException, UnsupportedOperationException
	{
		// this can only support named result sets if the underlying object is at 
		// least an ICallStatement
		if( ! isCallStatement( ) || ! supportsNamedResults() )
			throw new UnsupportedOperationException( "This statement does not support " +
													 "named result sets." );
	}
	
	private void checkOutputParameterSupport( ) 
		throws OdaException, UnsupportedOperationException
	{
		// this can only support output parameter if the underlying object is at 
		// least an ICallStatement
		if( ! isCallStatement( ) || ! supportsOutputParameter() )
			throw new UnsupportedOperationException( "This statement does not support " + 
													 "output parameters." );
	}
	
	private Object getParameterValue( String paramName, int paramIndex ) 
		throws OdaException
	{
		checkOutputParameterSupport( );
		ICallStatement callStatement = (ICallStatement) m_statement;
		
		Object paramValue = null;
		int nativeType = ( paramName == null ) ? getParameterType( paramIndex ) :
												 getParameterType( paramName );
		
		String driverName = m_connection.getDriverName( );
		
		// NULL means that the driver doesn't know or care about the type of the 
		// parameter, so try to look for it in the hints.  If the hints doesn't have 
		// any type info, then getOdaTypeFromParamHints() will return the safest default 
		// type, Types.CHAR.
		int paramType = ( nativeType == Types.NULL ) ?
						getOdaTypeFromParamHints( getOutputParameterHints(), 
						                          paramName, paramIndex ) :
						DriverManager.getInstance().getNativeToOdaMapping( driverName,
																		   m_dataSetType, 
																		   nativeType );

		switch( paramType )
		{
			case Types.INTEGER:
				int i = ( paramName == null ) ?
						callStatement.getInt( paramIndex ) :
						getInt( paramName );
				if( ! callStatement.wasNull() )
					paramValue = new Integer( i );
				break;
				
			case Types.DOUBLE:
				double d = ( paramName == null ) ?
						   callStatement.getDouble( paramIndex ) :
						   getDouble( paramName );
				if( ! callStatement.wasNull() )
					paramValue = new Double( d );
				break;
					
			case Types.CHAR:
				paramValue = ( paramName == null ) ?
							 callStatement.getString( paramIndex ) :
							 getString( paramName );
				break;
			
			case Types.DECIMAL:
				paramValue = ( paramName == null ) ?
							 callStatement.getBigDecimal( paramIndex ) :
							 getBigDecimal( paramName );
				break;
				
			case Types.DATE:
				paramValue = ( paramName == null ) ?
							 callStatement.getDate( paramIndex ) :
							 getDate( paramName );
				break;
				
			case Types.TIME:
				paramValue = ( paramName == null ) ?
							 callStatement.getTime( paramIndex ) :
							 getTime( paramName );
				break;
				
			case Types.TIMESTAMP:
				paramValue = ( paramName == null ) ?
							 callStatement.getTimestamp( paramIndex ) :
							 getTimestamp( paramName );
				break;
				
			default:
				assert false;	// exception now thrown by DriverManager
		}
		
		return ( callStatement.wasNull( ) ) ? null : paramValue;
	}
	
	// the following six getters are by name and need additional processing in the 
	// case where a named parameter is not supported by the underlying data source.  
	// In that case, we will look at the output parameter hints to get the name to 
	// id mapping
	private int getInt( String paramName ) throws OdaException
	{
		ICallStatement callStatement = (ICallStatement) m_statement;
		if( ! supportsNamedParameter() )
		{
			int paramIndex = getIndexFromParamHints( getOutputParameterHints(), paramName );
			if( paramIndex > 0 )
				return callStatement.getInt( paramIndex );
		}
		
		return callStatement.getInt( paramName );
	}
	
	private double getDouble( String paramName ) throws OdaException
	{
		ICallStatement callStatement = (ICallStatement) m_statement;
		if( ! supportsNamedParameter() )
		{
			int paramIndex = getIndexFromParamHints( getOutputParameterHints(), paramName );
			if( paramIndex > 0 )
				return callStatement.getDouble( paramIndex );
		}
		
		return callStatement.getDouble( paramName );
	}
	
	private String getString( String paramName ) throws OdaException
	{
		ICallStatement callStatement = (ICallStatement) m_statement;
		if( ! supportsNamedParameter() )
		{
			int paramIndex = getIndexFromParamHints( getOutputParameterHints(), paramName );
			if( paramIndex > 0 )
				return callStatement.getString( paramIndex );
		}
		
		return callStatement.getString( paramName );
	}
	
	private BigDecimal getBigDecimal( String paramName ) throws OdaException
	{
		ICallStatement callStatement = (ICallStatement) m_statement;
		if( ! supportsNamedParameter() )
		{
			int paramIndex = getIndexFromParamHints( getOutputParameterHints(), paramName );
			if( paramIndex > 0 )
				return callStatement.getBigDecimal( paramIndex );
		}
		
		return callStatement.getBigDecimal( paramName );
	}

	private Date getDate( String paramName ) throws OdaException
	{
		ICallStatement callStatement = (ICallStatement) m_statement;
		if( ! supportsNamedParameter() )
		{
			int paramIndex = getIndexFromParamHints( getOutputParameterHints(), paramName );
			if( paramIndex > 0 )
				return callStatement.getDate( paramIndex );
		}
		
		return callStatement.getDate( paramName );
	}
	
	private Time getTime( String paramName ) throws OdaException
	{
		ICallStatement callStatement = (ICallStatement) m_statement;
		if( ! supportsNamedParameter() )
		{
			int paramIndex = getIndexFromParamHints( getOutputParameterHints(), paramName );
			if( paramIndex > 0 )
				return callStatement.getTime( paramIndex );
		}
		
		return callStatement.getTime( paramName );
	}
	
	private Timestamp getTimestamp( String paramName ) throws OdaException
	{
		ICallStatement callStatement = (ICallStatement) m_statement;
		if( ! supportsNamedParameter() )
		{
			int paramIndex = getIndexFromParamHints( getOutputParameterHints(), paramName );
			if( paramIndex > 0 )
				return callStatement.getTimestamp( paramIndex );
		}
		
		return callStatement.getTimestamp( paramName );
	}
	
	private int getOdaTypeFromParamHints( List parameterHints, 
										  String paramName, 
										  int paramIndex )
	{
		assert( parameterHints != null );
		
		ListIterator iter = parameterHints.listIterator();
		boolean useParamName = ( paramName != null );
		while( iter.hasNext() )
		{
			ParameterHint paramHint = (ParameterHint) iter.next();
			
			Class typeInHint = null;
			if( ( useParamName && paramHint.getName().equals( paramName ) ) ||
				( ! useParamName && paramHint.getPosition() == paramIndex ) )
			{
				typeInHint = paramHint.getDataType();
				return convertHintTypeToOdaType( typeInHint );
			}
		}
		
		// didn't have a hint for the specified parameter
		return Types.CHAR;
	}
	
	private int convertHintTypeToOdaType( Class typeInHint )
	{
		// returns Types.CHAR if the hint didn't have data type information
		if( typeInHint == null )
			return Types.CHAR;
		
		if( typeInHint == Integer.class )
			return Types.INTEGER;
		else if( typeInHint == Double.class )
			return Types.DOUBLE;
		else if( typeInHint == BigDecimal.class )
			return Types.DECIMAL;
		else if( typeInHint == String.class )
			return Types.CHAR;
		else if( typeInHint == Date.class )
			return Types.DATE;
		else if( typeInHint == Time.class )
			return Types.TIME;
		else if( typeInHint == Timestamp.class )
			return Types.TIMESTAMP;
		else
			return Types.CHAR;
	}
	
	// returns 0 if the parameter hint doesn't exist for the specified parameter 
	// name or if the caller didn't specify a position for the specified parameter name
	private int getIndexFromParamHints( List parameterHints, String paramName )
	{
		assert( parameterHints != null );
		
		ListIterator iter = parameterHints.listIterator();
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
	 * @throws OdaException	if data source error occurs.
	 */
	public void clearParameterValues() throws OdaException
	{
		try
		{
			getStatement().clearInParameters();
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
	private void handleUnsupportedClearInParameters() throws OdaException
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
				m_statement.setProperty( property.getName(), 
				                         property.getValue() );
			}
		}
		
		m_statement.setMaxRows( m_maxRows );
		
		if( m_sortSpecs != null )
		{
			ListIterator iter = m_sortSpecs.listIterator();
			while( iter.hasNext() )
			{
				SortSpec sortBy = (SortSpec) iter.next();
				m_statement.setSortSpec( sortBy );
			}
		}
	}

	private void updateProjectedColumns( ProjectedColumns newProjectedColumns,
										 ProjectedColumns oldProjectedColumns )
		throws OdaException
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
	 * @throws OdaException	if data source error occurs.
	 */
	public int findInParameter( String paramName ) throws OdaException
	{
		return getStatement( ).findInParameter( paramName );
	}

	/**
	 * Sets the value of the specified input parameter.
	 * @param paramIndex	the 1-based index of the parameter.
	 * @param paramValue	the input parameter value.
	 * @throws OdaException	if data source error occurs.
	 */
	public void setParameterValue( int paramIndex, Object paramValue ) throws OdaException
	{
		setParameterValue( null /* n/a paramName */, paramIndex, paramValue );
	}

	/**
	 * Sets the value of the specified input parameter.
	 * @param paramName		the name of the parameter.
	 * @param paramValue	the input parameter value.
	 * @throws OdaException	if data source error occurs.
	 */
	public void setParameterValue( String paramName, Object paramValue ) throws OdaException
	{
		setParameterValue( paramName, 0 /* n/a paramIndex */, paramValue );
	}

	/**
	 * Adds an <code>InputParameterHint</code> for this statement to map 
	 * static input parameter definitions with runtime input parameter 
	 * metadata.
	 * @param inputParamHint	an <code>InputParameterHint</code> instance.
	 * @throws OdaException	if data source error occurs.
	 */
	public void addInputParameterHint( InputParameterHint inputParamHint ) throws OdaException
	{
		if( inputParamHint == null )
			return;
		
		validateAndAddParameterHint( getInputParameterHints(), inputParamHint );
	}

	private ArrayList getInputParameterHints()
	{
		if( m_inputParameterHints == null )
			m_inputParameterHints = new ArrayList();
		
		return m_inputParameterHints;
	}

	private void setParameterValue( String paramName, int paramIndex, 
									Object paramValue ) throws OdaException
	{
		// TODO externalize message text
		if( paramValue == null )
			throw new NullPointerException( "Parameter value is null" );
		
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
			
			if( paramValue instanceof Date )
			{
				Date date = (Date) paramValue;
				setDate( paramName, paramIndex, date );
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
		catch( OdaException ex )
		{
			retrySetParameterValue( paramName, paramIndex, paramValue, ex );
			return;
		}

		// TODO externalize message text
		throw new OdaException( "Unsupported parameter value type: " +
								paramValue.getClass() );
	}

	// retry setting the parameter value by using an alternate setter method 
	// using the runtime parameter metadata. Or if the runtime parameter metadata 
	// is not available, then use the input parameter hints, if available.  
	// It will default to calling setString() we can't get the info from 
	// the runtime parameter metadata or the parameter hints.
	private void retrySetParameterValue( String paramName, int paramIndex, 
										 Object paramValue, 
										 Exception lastException ) throws OdaException
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
			parameterType = getOdaTypeFromParamHints( getInputParameterHints(), 
			                                          paramName, paramIndex );
		
		Class paramValueClass = paramValue.getClass();
		
		// the runtime parameter metadata or hint would lead us to call the same 
		// setXXX method again, so the last exception that got returned could be info 
		// regarding problems with the data, so throw that
		if( ( parameterType == Types.INTEGER && paramValueClass == Integer.class ) ||
			( parameterType == Types.DOUBLE && paramValueClass == Double.class ) ||
			( parameterType == Types.CHAR && paramValueClass == String.class ) ||
			( parameterType == Types.DECIMAL && paramValueClass == BigDecimal.class ) ||
			( parameterType == Types.DATE && paramValueClass == Date.class ) ||
			( parameterType == Types.TIME && paramValueClass == Time.class ) ||
			( parameterType == Types.TIMESTAMP && paramValueClass == Timestamp.class ) )
		{
			if( lastException instanceof RuntimeException )
				throw (RuntimeException) lastException;
			else if( lastException instanceof OdaException )
				throw (OdaException) lastException;
			else
			{
				// TODO externalize message text
				IllegalStateException ex = 
					new IllegalStateException( "Unknown exception thrown in the last setter call." );
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
		
		if( paramValueClass == Date.class )
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
		throws OdaException
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
		throws OdaException
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
		throws OdaException
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
		throws OdaException
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
		throws OdaException
	{
		switch( parameterType )
		{
			case Types.CHAR:
			{
				String s = ( (Date) paramValue ).toString();
				setString( paramName, paramIndex, s );
				return;
			}
			
			case Types.TIMESTAMP:
			{
				long time = ( (Date) paramValue ).getTime();
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
		throws OdaException
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
		throws OdaException
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
								  Exception cause ) throws OdaException
	{
		// TODO externalize message text
		OdaException exception = null;
		if( paramName == null )
			exception = new OdaException( "Cannot convert the parameter value " + paramValue + " at index (" + 
			                              paramIndex + ") from the value type of (" + 
			                              paramValue.getClass() + ") to the ODA type of (" + odaType + ")." );
		else
			exception =  new OdaException( "Cannot convert the parameter value " + paramValue + " with the parameter " +
			                               "name of (" + paramName + ") from the value type of (" +
			                               paramValue.getClass() + ") to the ODA type of (" + odaType + ")." );
		
		if( cause != null )
			exception.initCause( cause );
		
		throw exception;
	}

	private void setInt( String paramName, int paramIndex, int i ) throws OdaException
	{
		if( paramName == null )
			getStatement( ).setInt( paramIndex, i );
		else
			setInt( paramName, i );
	}

	private void setInt( String paramName, int i ) throws OdaException
	{
		if( ! supportsNamedParameter() )
		{
			int paramIndex = getIndexFromParamHints( getInputParameterHints(), paramName );
			if( paramIndex > 0 )
			{
				getStatement().setInt( paramIndex, i );
				return;
			}
		}
		
		getStatement().setInt( paramName, i );
	}

	private void setDouble( String paramName, int paramIndex, double d ) throws OdaException
	{
		if( paramName == null )
			getStatement( ).setDouble( paramIndex, d );
		else
			setDouble( paramName, d );
	}
	
	private void setDouble( String paramName, double d ) throws OdaException
	{
		if( ! supportsNamedParameter() )
		{
			int paramIndex = getIndexFromParamHints( getInputParameterHints(), paramName );
			if( paramIndex > 0 )
			{
				getStatement().setDouble( paramIndex, d );
				return;
			}
		}
		
		getStatement().setDouble( paramName, d );
	}

	private void setString( String paramName, int paramIndex, String string ) throws OdaException
	{
		if( paramName == null )
			getStatement( ).setString( paramIndex, string );
		else
			setString( paramName, string );
	}

	private void setString( String paramName, String string ) throws OdaException
	{
		if( ! supportsNamedParameter() )
		{
			int paramIndex = getIndexFromParamHints( getInputParameterHints(), paramName );
			if( paramIndex > 0 )
			{
				getStatement().setString( paramIndex, string );
				return;
			}
		}
		
		getStatement().setString( paramName, string );
	}

	private void setBigDecimal( String paramName, int paramIndex, BigDecimal decimal ) throws OdaException
	{
		if( paramName == null )
			getStatement( ).setBigDecimal( paramIndex, decimal );
		else
			setBigDecimal( paramName, decimal );
	}

	private void setBigDecimal( String paramName, BigDecimal decimal ) throws OdaException
	{
		if( ! supportsNamedParameter() )
		{
			int paramIndex = getIndexFromParamHints( getInputParameterHints(), paramName );
			if( paramIndex > 0 )
			{
				getStatement().setBigDecimal( paramIndex, decimal );
				return;
			}
		}
		
		getStatement().setBigDecimal( paramName, decimal );
	}

	private void setDate( String paramName, int paramIndex, Date date ) throws OdaException
	{
		if( paramName == null )
			getStatement( ).setDate( paramIndex, date );
		else 
			setDate( paramName, date );
	}

	private void setDate( String paramName, Date date ) throws OdaException
	{
		if( ! supportsNamedParameter() )
		{
			int paramIndex = getIndexFromParamHints( getInputParameterHints(), paramName );
			if( paramIndex > 0 )
			{
				getStatement().setDate( paramIndex, date );
				return;
			}
		}
		
		getStatement().setDate( paramName, date );
	}

	private void setTime( String paramName, int paramIndex, Time time ) throws OdaException
	{
		if( paramName == null )
			getStatement( ).setTime( paramIndex, time );
		else
			setTime( paramName, time );
	}

	private void setTime( String paramName, Time time ) throws OdaException
	{
		if( ! supportsNamedParameter() )
		{
			int paramIndex = getIndexFromParamHints( getInputParameterHints(), paramName );
			if( paramIndex > 0 )
			{
				getStatement().setTime( paramIndex, time );
				return;
			}
		}
		
		getStatement().setTime( paramName, time );
	}

	private void setTimestamp( String paramName, int paramIndex, Timestamp timestamp ) throws OdaException
	{
		if( paramName == null )
			getStatement( ).setTimestamp( paramIndex, timestamp );
		else
			setTimestamp( paramName, timestamp );
	}

	private void setTimestamp( String paramName, Timestamp timestamp ) throws OdaException
	{
		if( ! supportsNamedParameter() )
		{
			int paramIndex = getIndexFromParamHints( getInputParameterHints(), paramName );
			if( paramIndex > 0 )
			{
				getStatement().setTimestamp( paramIndex, timestamp );
				return;
			}
		}
		
		getStatement().setTimestamp( paramName, timestamp );
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
