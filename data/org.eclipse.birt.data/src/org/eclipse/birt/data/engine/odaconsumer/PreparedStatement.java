/*
 *****************************************************************************
 * Copyright (c) 2004, 2010 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation - initial API and implementation
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.core.security.PropertySecurity;
import org.eclipse.birt.data.engine.executor.ResultClass;
import org.eclipse.birt.data.engine.i18n.DataResourceHandle;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.datatools.connectivity.oda.IAdvancedQuery;
import org.eclipse.datatools.connectivity.oda.IBlob;
import org.eclipse.datatools.connectivity.oda.IClob;
import org.eclipse.datatools.connectivity.oda.IParameterMetaData;
import org.eclipse.datatools.connectivity.oda.IQuery;
import org.eclipse.datatools.connectivity.oda.IResultSet;
import org.eclipse.datatools.connectivity.oda.IResultSetMetaData;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.SortSpec;
import org.eclipse.datatools.connectivity.oda.spec.QuerySpecification;

/**
 * <code>PreparedStatement</code> represents a statement query that can be
 * executed without input parameter values and returns the results and output
 * parameters values it produces. <br>
 * Blob and Clob data types are only supported in output data returned in result
 * columns and output parameters.
 */
public class PreparedStatement extends ExceptionHandler {
	private String m_dataSetType;
	private Connection m_connection;
	private String m_queryText;

	private IQuery m_statement;
	private ArrayList<Property> m_properties;
	private int m_maxRows;
	private ArrayList<SortSpec> m_sortSpecs;

	private Boolean m_supportsNamedResults;
	private Boolean m_supportsOutputParameters;
	private Boolean m_supportsNamedParameters;
	private Boolean m_supportsInputParameters;
	private Boolean m_supportsMultipleResultSets;

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

	private SequentialResultSetHandler m_seqResultSetHdlr;

	// trace logging variables
	private static String sm_className = PreparedStatement.class.getName();

	PreparedStatement(IQuery statement, String dataSetType, Connection connection, String query) {
		super(sm_className);
		String methodName = "PreparedStatement"; //$NON-NLS-1$
		if (getLogger().isLoggingEnterExitLevel())
			getLogger().entering(sm_className, methodName, new Object[] { statement, dataSetType, connection, query });

		assert (statement != null && connection != null);
		m_statement = statement;
		m_dataSetType = dataSetType;
		m_connection = connection;
		m_queryText = query;

		getLogger().exiting(sm_className, methodName, this);
	}

	/**
	 * Gets the current effective query text prepared by the underlying ODA driver.
	 * 
	 * @return the current effective query text, or null if no query text is
	 *         effective or available at the current query state
	 */
	public String getEffectiveQueryText() {
		final String methodName = "getEffectiveQueryText"; //$NON-NLS-1$
		getLogger().entering(sm_className, methodName);

		String queryText = null;
		try {
			queryText = m_statement.getEffectiveQueryText();
		} catch (Exception ex) {
			// ignore exception; simply log warning and return null
			getLogger().logp(Level.WARNING, sm_className, methodName, "Unable to get effective query text.", ex); //$NON-NLS-1$
		}

		getLogger().exiting(sm_className, methodName, queryText);
		return queryText;
	}

	/**
	 * Gets the current specification of characteristics to apply when executing
	 * this.
	 * 
	 * @return the current QuerySpecification, or null if none is effective
	 */
	@SuppressWarnings("restriction")
	public QuerySpecification getQuerySpecification() {
		final String methodName = "getQuerySpecification"; //$NON-NLS-1$
		getLogger().entering(sm_className, methodName);

		QuerySpecification querySpec = null;
		try {
			querySpec = m_statement.getSpecification();
		} catch (Exception ex) {
			// ignore exception; simply log warning and return null
			getLogger().logp(Level.WARNING, sm_className, methodName, "Unable to get effective query specification.", //$NON-NLS-1$
					ex);
		}

		getLogger().exiting(sm_className, methodName, querySpec);
		return querySpec;
	}

	/**
	 * Sets the named property with the specified value.
	 * 
	 * @param name  the property name.
	 * @param value the property value.
	 * @throws DataException if data source error occurs.
	 */
	public void setProperty(String name, String value) throws DataException {
		String methodName = "setProperty"; //$NON-NLS-1$
		if (getLogger().isLoggingEnterExitLevel())
			getLogger().entering(sm_className, methodName, new Object[] { name, value });

		doSetProperty(name, value);

		// save the properties in a list in case we need them later,
		// i.e. support clearParameterValues() for drivers that don't support
		// the ODA operation
		getPropertiesList().add(new Property(name, value));

		getLogger().exiting(sm_className, methodName);
	}

	private void doSetProperty(String name, String value) throws DataException {
		String methodName = "doSetProperty"; //$NON-NLS-1$
		if (getLogger().isLoggingEnterExitLevel())
			getLogger().entering(sm_className, methodName, new Object[] { name, value });

		try {
			m_statement.setProperty(name, value);
		} catch (OdaException ex) {
			throwException(ex, ResourceConstants.CANNOT_SET_STATEMENT_PROPERTY, methodName);
		} catch (UnsupportedOperationException ex) {
			getLogger().logp(Level.WARNING, sm_className, methodName, "Cannot set statement property.", ex); //$NON-NLS-1$
		}

		getLogger().exiting(sm_className, methodName);
	}

	private ArrayList<Property> getPropertiesList() {
		if (m_properties == null)
			m_properties = new ArrayList<Property>();

		return m_properties;
	}

	/**
	 * Specifies the sort specification for this <code>Statement</code>. Must be
	 * called prior to <code>Statement.execute</code> for the sort specification to
	 * apply to the result set(s) returned.
	 * 
	 * @param sortBy the sort specification to assign to the <code>Statement</code>.
	 * @throws DataException if data source error occurs.
	 */
	public void setSortSpec(SortSpec sortBy) throws DataException {
		String methodName = "setSortSpec"; //$NON-NLS-1$
		getLogger().entering(sm_className, methodName, sortBy);

		doSetSortSpec(sortBy);
		getSortSpecsList().add(sortBy);

		getLogger().exiting(sm_className, methodName);
	}

	private void doSetSortSpec(SortSpec sortBy) throws DataException {
		String methodName = "doSetSortSpec"; //$NON-NLS-1$
		getLogger().entering(sm_className, methodName, sortBy);

		try {
			m_statement.setSortSpec(sortBy);
		} catch (OdaException ex) {
			throwException(ex, ResourceConstants.CANNOT_SET_SORT_SPEC, methodName);
		} catch (UnsupportedOperationException ex) {
			throwException(ex, ResourceConstants.CANNOT_SET_SORT_SPEC, methodName);
		}

		getLogger().exiting(sm_className, methodName);
	}

	private ArrayList<SortSpec> getSortSpecsList() {
		if (m_sortSpecs == null)
			m_sortSpecs = new ArrayList<SortSpec>();

		return m_sortSpecs;
	}

	/**
	 * Specifies the maximum number of <code>IResultObjects</code> that can be
	 * fetched from each <code>ResultSet</code> of this <code>Statement</code>.
	 * 
	 * @param max the maximum number of <code>IResultObjects</code> that can be
	 *            fetched from each <code>ResultSet</code>.
	 * @throws DataException if data source error occurs.
	 */
	public void setMaxRows(int max) throws DataException {
		String methodName = "setMaxRows"; //$NON-NLS-1$
		getLogger().entering(sm_className, methodName, max);

		doSetMaxRows(max);

		m_maxRows = max;

		getLogger().exiting(sm_className, methodName);
	}

	private void doSetMaxRows(int max) throws DataException {
		String methodName = "doSetMaxRows"; //$NON-NLS-1$
		getLogger().entering(sm_className, methodName, max);

		try {
			m_statement.setMaxRows(max);
		} catch (OdaException ex) {
			throwException(ex, ResourceConstants.CANNOT_SET_MAX_ROWS, methodName);
		} catch (UnsupportedOperationException ex) {
			getLogger().logp(Level.WARNING, sm_className, methodName, "Cannot set max rows.", ex); //$NON-NLS-1$
			// non-critical operation, ignore and proceed
		}

		getLogger().exiting(sm_className, methodName);
	}

	/**
	 * Returns an <code>IResultClass</code> representing the metadata of the result
	 * set for this <code>Statement</code>.
	 * 
	 * @return the <code>IResultClass</code> for the result set.
	 * @throws DataException if data source error occurs.
	 */
	public IResultClass getMetaData() throws DataException {
		String methodName = "getMetaData"; //$NON-NLS-1$
		getLogger().entering(sm_className, methodName);

		IResultClass ret = null;

		// we can get the current result set's metadata directly from the
		// current result set handle rather than go through ODA
		if (m_currentResultSet != null)
			ret = m_currentResultSet.getMetaData();
		else
			ret = doGetMetaData();

		getLogger().exiting(sm_className, methodName, ret);
		return ret;
	}

	private IResultClass doGetMetaData() throws DataException {
		String methodName = "doGetMetaData"; //$NON-NLS-1$
		getLogger().entering(sm_className, methodName);

		if (m_currentResultClass == null) {
			List projectedColumns = getProjectedColumns().getColumnsMetadata();
			m_currentResultClass = doGetResultClass(projectedColumns);
		}

		getLogger().exiting(sm_className, methodName, m_currentResultClass);

		return m_currentResultClass;
	}

	private ResultClass doGetResultClass(List projectedColumns) throws DataException {
		String methodName = "doGetResultClass"; //$NON-NLS-1$
		getLogger().entering(sm_className, methodName, projectedColumns);

		assert (projectedColumns != null);
		ResultClass ret = new ResultClass(projectedColumns);

		getLogger().exiting(sm_className, methodName, ret);

		return ret;
	}

	private ProjectedColumns getProjectedColumns() throws DataException {
		String methodName = "getProjectedColumns"; //$NON-NLS-1$
		getLogger().entering(sm_className, methodName);

		if (m_projectedColumns == null) {
			IResultSetMetaData odaMetadata = getRuntimeMetaData();
			m_projectedColumns = doGetProjectedColumns(odaMetadata);
		} else if (m_updateProjectedColumns) {
			// need to update the projected columns of the un-named result
			// set with the newest runtime metadata, don't use the cached
			// one
			IResultSetMetaData odaMetadata = getRuntimeMetaData();
			ProjectedColumns newProjectedColumns = doGetProjectedColumns(odaMetadata);
			updateProjectedColumns(newProjectedColumns, m_projectedColumns);
			m_projectedColumns = newProjectedColumns;

			// reset the update flag
			m_updateProjectedColumns = false;
		}

		getLogger().exiting(sm_className, methodName, m_projectedColumns);

		return m_projectedColumns;
	}

	private IResultSetMetaData getRuntimeMetaData() throws DataException {
		String methodName = "getRuntimeMetaData"; //$NON-NLS-1$
		getLogger().entering(sm_className, methodName);

		try {
			IResultSetMetaData ret = m_statement.getMetaData();

			getLogger().exiting(sm_className, methodName, ret);
			return ret;
		} catch (OdaException ex) {
			throwException(ex, ResourceConstants.CANNOT_GET_RESULTSET_METADATA, methodName);
		} catch (UnsupportedOperationException ex) {
			throwUnsupportedException(ResourceConstants.CANNOT_GET_RESULTSET_METADATA, methodName);
		}
		return null;
	}

	private ProjectedColumns doGetProjectedColumns(IResultSetMetaData odaMetadata) throws DataException {
		final String methodName = "doGetProjectedColumns( IResultSetMetaData )"; //$NON-NLS-1$
		getLogger().entering(sm_className, methodName, odaMetadata);

		ResultSetMetaData metadata = new ResultSetMetaData(odaMetadata, m_connection.getDataSourceId(), m_dataSetType);
		ProjectedColumns ret = new ProjectedColumns(metadata);

		getLogger().exiting(sm_className, methodName, ret);
		return ret;
	}

	/**
	 * Returns an <code>IResultClass</code> representing the metadata of the named
	 * result set for this <code>Statement</code>.
	 * 
	 * @param resultSetName the name of the result set.
	 * @return the <code>IResultClass</code> for the named result set.
	 * @throws DataException if data source error occurs.
	 */
	public IResultClass getMetaData(String resultSetName) throws DataException {
		String methodName = "getMetaData"; //$NON-NLS-1$
		getLogger().entering(sm_className, methodName, resultSetName);

		validateNamedResultsSupport();

		// we can get the current result set's metadata directly from the
		// current result set handle rather than go through ODA
		ResultSet resultset = (ResultSet) getNamedCurrentResultSets().get(resultSetName);

		IResultClass ret = null;

		ret = doGetMetaData(resultSetName);

		getLogger().exiting(sm_className, methodName, ret);
		return ret;
	}

	private IResultClass doGetMetaData(String resultSetName) throws DataException {
		String methodName = "doGetMetaData"; //$NON-NLS-1$
		getLogger().entering(sm_className, methodName, resultSetName);

		IResultClass resultClass = (IResultClass) getNamedCurrentResultClasses().get(resultSetName);

		if (resultClass == null) {
			List projectedColumns = getProjectedColumns(resultSetName).getColumnsMetadata();
			resultClass = doGetResultClass(projectedColumns);
			getNamedCurrentResultClasses().put(resultSetName, resultClass);
		}

		getLogger().exiting(sm_className, methodName, resultClass);

		return resultClass;
	}

	private ProjectedColumns getProjectedColumns(String resultSetName) throws DataException {
		String methodName = "getProjectedColumns"; //$NON-NLS-1$
		getLogger().entering(sm_className, methodName, resultSetName);

		ProjectedColumns projectedColumns = (ProjectedColumns) getNamedProjectedColumns().get(resultSetName);
		if (projectedColumns == null) {
			IResultSetMetaData odaMetadata = getRuntimeMetaData(resultSetName);
			projectedColumns = doGetProjectedColumns(odaMetadata);
			getNamedProjectedColumns().put(resultSetName, projectedColumns);
		} else if (m_updateNamedProjectedColumns != null && m_updateNamedProjectedColumns.contains(resultSetName)) {
			// there's an existing ProjectedColumns from the same result set,
			// and it needs to be updated with the newest runtime metadata
			IResultSetMetaData odaMetadata = getRuntimeMetaData(resultSetName);
			ProjectedColumns newProjectedColumns = doGetProjectedColumns(odaMetadata);
			updateProjectedColumns(newProjectedColumns, projectedColumns);
			getNamedProjectedColumns().put(resultSetName, newProjectedColumns);

			// reset the update flag for this result set name
			m_updateNamedProjectedColumns.remove(resultSetName);
		}

		getLogger().exiting(sm_className, methodName, projectedColumns);

		return projectedColumns;
	}

	private IResultSetMetaData getRuntimeMetaData(String resultSetName) throws DataException {
		String methodName = "getRuntimeMetaData"; //$NON-NLS-1$
		getLogger().entering(sm_className, methodName, resultSetName);

		try {
			IResultSetMetaData ret = getAdvancedStatement().getMetaDataOf(resultSetName);

			getLogger().exiting(sm_className, methodName, ret);

			return ret;
		} catch (OdaException ex) {
			throwException(ex, ResourceConstants.CANNOT_GET_METADATA_FOR_NAMED_RESULTSET, resultSetName, methodName);
		} catch (UnsupportedOperationException ex) {
			throwUnsupportedException(ResourceConstants.CANNOT_GET_METADATA_FOR_NAMED_RESULTSET, resultSetName,
					methodName);
		}
		return null;
	}

	/**
	 * Executes the statement's query.
	 * 
	 * @return true if this has at least one result set; false otherwise
	 * @throws DataException if data source error occurs.
	 */
	public boolean execute() throws DataException {
		String methodName = "execute"; //$NON-NLS-1$
		getLogger().entering(sm_className, methodName);

		// when the statement is re-executed, then the previous result set(s)
		// needs to be invalidated.
		resetCachedResultSets();

		// this will get the result set metadata for the ResultSet in a subsequent
		// getResultSet() call. Getting the underlying metadata after the statement
		// has been executed may reset its state which will cause the result set not
		// to have any data
		doGetMetaData();

		try {
			boolean ret = false;

			if (isAdvancedQuery())
				ret = getAdvancedStatement().execute();
			else // simple statement
			{
				// hold onto its returned result set
				// for subsequent call to getResultSet()
				m_driverResultSet = m_statement.executeQuery();
				ret = true;
			}

			if (getLogger().isLoggingEnterExitLevel())
				getLogger().exiting(sm_className, methodName, Boolean.valueOf(ret));

			return ret;
		} catch (OdaException ex) {
			throwException(ex, ResourceConstants.CANNOT_EXECUTE_STATEMENT, methodName);
		} catch (UnsupportedOperationException ex) {
			throwException(ex, ResourceConstants.CANNOT_EXECUTE_STATEMENT, methodName);
		}
		return false;
	}

	// clear all cached references to the current result sets,
	// applies to named and un-named result sets
	private void resetCachedResultSets() {
		m_driverResultSet = null;
		m_currentResultSet = null;

		if (m_namedCurrentResultSets != null)
			m_namedCurrentResultSets.clear();

		if (m_seqResultSetHdlr != null)
			m_seqResultSetHdlr.resetResultSetsState();
	}

	/**
	 * Returns the <code>ResultSet</code> instance.
	 * 
	 * @return a <code>ResultSet</code> instance.
	 * @throws DataException if data source error occurs.
	 */
	public ResultSet getResultSet() throws DataException {
		String methodName = "getResultSet"; //$NON-NLS-1$
		getLogger().entering(sm_className, methodName);

		IResultSet resultSet = null;

		try {
			if (isAdvancedQuery())
				resultSet = getAdvancedStatement().getResultSet();
			else {
				resultSet = m_driverResultSet;
				m_driverResultSet = null;
			}
		} catch (OdaException ex) {
			throwException(ex, ResourceConstants.CANNOT_GET_RESULTSET, methodName);
		} catch (UnsupportedOperationException ex) {
			throwException(ex, ResourceConstants.CANNOT_GET_RESULTSET, methodName);
		}

		ResultSet rs = new ResultSet(resultSet, doGetMetaData());

		// keep a pointer to the current result set in case the caller wants
		// to get the metadata from the current result set of the statement
		m_currentResultSet = rs;

		// reset this for the statement since the caller can
		// subsequently change this and the changes won't apply
		// to the existing result set
		m_currentResultClass = null;

		getLogger().exiting(sm_className, methodName, rs);

		return rs;
	}

	/**
	 * Returns the specified named <code>ResultSet</code>.
	 * 
	 * @param resultSetName the name of the result set.
	 * @return the named <code>ResultSet</code>.
	 * @throws DataException if data source error occurs.
	 */
	public ResultSet getResultSet(String resultSetName) throws DataException {
		final String methodName = "getResultSet(String)"; //$NON-NLS-1$
		getLogger().entering(sm_className, methodName, resultSetName);

		validateNamedResultsSupport();

		IResultSet resultset = null;

		try {
			resultset = getAdvancedStatement().getResultSet(resultSetName);
		} catch (OdaException ex) {
			throwException(ex, ResourceConstants.CANNOT_GET_NAMED_RESULTSET, resultSetName, methodName);
		} catch (UnsupportedOperationException ex) {
			throwException(ex, ResourceConstants.CANNOT_GET_NAMED_RESULTSET, resultSetName, methodName);
		}

		ResultSet rs = new ResultSet(resultset, doGetMetaData(resultSetName));

		// keep this as the current named result set, so the caller can
		// get the metadata from the result set from the statement
		getNamedCurrentResultSets().put(resultSetName, rs);

		// reset the current result class for the given result set name, so
		// subsequent changes won't apply to the existing result set
		getNamedCurrentResultClasses().remove(resultSetName);

		getLogger().exiting(sm_className, methodName, rs);

		return rs;
	}

	/**
	 * Returns the <code>ResultSet</code> at the specified sequence in the multiple
	 * result sets available from this statement. This method may be called
	 * specifying a value of 1 in the argument, even if this statement does not
	 * support multiple result sets. In such case, it is equivalent to calling
	 * {@link #getResultSet()}. If this statement supports multiple result sets, the
	 * statement must have been executed before calling this method. Furthermore,
	 * this method also implicitly closes the current ResultSet object obtained from
	 * the previous call to {@link #getResultSet()} or {@link #getResultSet(int)}.
	 * 
	 * @param resultSetNum a 1-based index number that indicates the sequence of a
	 *                     result set among a sequential set of multiple result sets
	 * @return the specified <code>ResultSet</code> if available; may return null if
	 *         no result set is available at the specified resultSetNum
	 * @throws DataException if specified argument is invalid or data source error
	 *                       occurs.
	 */
	public ResultSet getResultSet(int resultSetNum) throws DataException {
		return getSequentialResultHandler().getResultSet(resultSetNum);
	}

	/**
	 * Returns an <code>IResultClass</code> representing the metadata of the
	 * specified result set for this <code>PreparedStatement</code>. The statement
	 * must have been executed before calling this method.
	 * 
	 * @param resultSetNum a 1-based index number that indicates the sequence of a
	 *                     result set among a sequential set of multiple result sets
	 * @return the <code>IResultClass</code> for the specified result set.
	 * @throws DataException if data source error occurs.
	 */
	public IResultClass getMetaData(int resultSetNum) throws DataException {
		return getSequentialResultHandler().getMetaData(resultSetNum);
	}

	/**
	 * Moves to the statement's next result set. It is intended for use together
	 * with {@link #getResultSet()}. The statement must have been executed before
	 * calling this method. If the underlying query supports multiple result sets,
	 * this method also implicitly closes the current ResultSet object obtained from
	 * the previous call to {@link #getResultSet()}
	 * 
	 * @return true if there are more results in this query object; false otherwise
	 * @throws DataException if data source error occurs.
	 */
	public boolean getMoreResults() throws DataException {
		return getSequentialResultHandler().getMoreResults();
	}

	/**
	 * Returns the 1-based index of the specified output parameter.
	 * 
	 * @param paramName the name of the parameter.
	 * @return the 1-based index of the output parameter.
	 * @throws DataException if data source error occurs.
	 */
	public int findOutParameter(String paramName) throws DataException {
		String methodName = "findOutParameter"; //$NON-NLS-1$
		getLogger().entering(sm_className, methodName, paramName);

		validateOutputParameterSupport();

		try {
			int ret = getAdvancedStatement().findOutParameter(paramName);

			getLogger().exiting(sm_className, methodName, ret);
			return ret;
		} catch (OdaException ex) {
			throwException(ex, ResourceConstants.CANNOT_FIND_OUT_PARAMETER, paramName, methodName);
		} catch (UnsupportedOperationException ex) {
			// this is common, and may be ignored by caller
			getLogger().logp(Level.INFO, sm_className, methodName, "Cannot find output parameter by name.", ex); //$NON-NLS-1$

			throw newException(ResourceConstants.CANNOT_FIND_OUT_PARAMETER, paramName, ex);
		}
		return 0;
	}

	/**
	 * Returns the effective ODA data type code for the specified parameter.
	 * 
	 * @param paramIndex the 1-based index of the parameter.
	 * @return the ODA <code>java.sql.Types</code> code of the parameter.
	 * @throws DataException if data source error occurs.
	 */
	public int getParameterType(int paramIndex) throws DataException {
		final String methodName = "getParameterType( int )"; //$NON-NLS-1$
		getLogger().entering(sm_className, methodName, paramIndex);

		ParameterMetaData paramMD = getParameterMetaData(paramIndex);
		assert (paramMD != null); // invalid paramIndex would have thrown exception
		int ret = paramMD.getDataType();

		getLogger().exiting(sm_className, methodName, ret);
		return ret;
	}

	/**
	 * Returns the effective ODA data type code for the specified parameter.
	 * 
	 * @param paramName the name of the data set parameter in model.
	 * @return the ODA <code>java.sql.Types</code> code of the parameter.
	 * @throws DataException if data source error occurs.
	 */
	public int getParameterType(String paramName) throws DataException {
		ParameterName paramNameObj = new ParameterName(paramName, this);
		return getParameterType(paramNameObj, true);
	}

	private int getParameterType(ParameterName paramName, boolean retryByIndex) throws DataException {
		final String methodName = "getParameterType( ParameterName )"; //$NON-NLS-1$
		getLogger().entering(sm_className, methodName, paramName);

		ParameterMetaData paramMD = getParameterMetaData(paramName);
		if (paramMD != null) {
			int dataTypeCode = paramMD.getDataType();

			getLogger().exiting(sm_className, methodName, dataTypeCode);
			return dataTypeCode;
		}

		// couldn't find matching merged parameter metadata directly by name

		int parameterType = Types.NULL;
		if (retryByIndex) {
			// try to find corresponding position in design hints
			int paramPos = getIndexFromParamHints(paramName.getRomName());
			if (paramPos <= 0) // invalid position
			{
				throwError(ResourceConstants.CANNOT_GET_PARAMETER_TYPE, paramName, methodName);
			}

			parameterType = getParameterType(paramPos);
		} else // probably no info available on the 1-based index position
		{
			// get the data type by name from the parameter design hints
			parameterType = getOdaTypeFromParamHints(paramName.getRomName(), 0);
		}

		getLogger().exiting(sm_className, methodName, parameterType);
		return parameterType;
	}

	/**
	 * Returns the specified output parameter value.
	 * 
	 * @param paramIndex the 1-based index of the parameter.
	 * @return the output value for the specified parameter.
	 * @throws DataException if data source error occurs.
	 */
	public Object getParameterValue(int paramIndex) throws DataException {
		final String methodName = "getParameterValue( int )"; //$NON-NLS-1$
		getLogger().entering(sm_className, methodName, paramIndex);

		Object ret = getParameterValue(null /* n/a paramName */, paramIndex);

		getLogger().exiting(sm_className, methodName, ret);
		return ret;
	}

	/**
	 * Returns the specified output parameter value.
	 * 
	 * @param paramName the name of the parameter.
	 * @return the output value for the specified parameter.
	 * @throws DataException if data source error occurs.
	 */
	public Object getParameterValue(String paramName) throws DataException {
		final String methodName = "getParameterValue( String )"; //$NON-NLS-1$
		getLogger().entering(sm_className, methodName, paramName);

		Object ret = getParameterValue(paramName, 0 /* n/a paramIndex */ );

		getLogger().exiting(sm_className, methodName, ret);
		return ret;
	}

	/**
	 * Cancel the statement execution.
	 * 
	 * @throws DataException
	 */
	public void cancel() throws DataException {
		final String methodName = "cancel"; //$NON-NLS-1$
		getLogger().entering(sm_className, methodName);

		try {
			m_statement.cancel();
		} catch (OdaException ex) {
			throwException(ex, ResourceConstants.CANNOT_CANCEL_STATEMENT, methodName);
		} catch (UnsupportedOperationException ex) {
			throwUnsupportedException(ResourceConstants.CANNOT_CANCEL_STATEMENT, methodName);
		}

		getLogger().exiting(sm_className, methodName);
	}

	/**
	 * Closes this <code>Statement</code>.
	 * 
	 * @throws DataException if data source error occurs.
	 */
	public void close() throws DataException {
		final String methodName = "close"; //$NON-NLS-1$
		getLogger().entering(sm_className, methodName);

		flushResultSets();
		resetCachedResultSets();
		resetResultsAndMetaData();

		try {
			m_statement.close();
		} catch (OdaException ex) {
			throwException(ex, ResourceConstants.CANNOT_CLOSE_STATEMENT, methodName);
		} catch (UnsupportedOperationException ex) {
			getLogger().logp(Level.WARNING, sm_className, methodName, "Cannot close statement.", ex); //$NON-NLS-1$
		}

		getLogger().exiting(sm_className, methodName);
	}

	private void flushResultSets() {
		try {
			// advance the result sets in query until no more results
			while (getMoreResults()) {
			}
		} catch (DataException ex) {
			// ignore
		}
	}

	private void resetResultsAndMetaData() {
		final String methodName = "resetResultsAndMetaData"; //$NON-NLS-1$
		getLogger().entering(sm_className, methodName);

		resetCurrentResultAndMetaData();

		if (m_namedCurrentResultSets != null)
			m_namedCurrentResultSets.clear();
		if (m_namedCurrentResultClasses != null)
			m_namedCurrentResultClasses.clear();

		if (m_seqResultSetHdlr != null)
			m_seqResultSetHdlr.resetResultSetsState();

		getLogger().exiting(sm_className, methodName);
	}

	/**
	 * Adds a <code>ColumnHint</code> for this statement to map design time column
	 * projections with runtime result set metadata.
	 * 
	 * @param columnHint a <code>ColumnHint</code> instance.
	 * @throws DataException if data source error occurs.
	 */
	public void addColumnHint(ColumnHint columnHint) throws DataException {
		String methodName = "addColumnHint"; //$NON-NLS-1$
		getLogger().entering(sm_className, methodName, columnHint);

		if (columnHint != null) {
			// no need to reset the current metadata because adding a column
			// hint doesn't change the existing columns that are being projected,
			// it just updates some of the column metadata
			getProjectedColumns().addHint(columnHint);
		}

		getLogger().exiting(sm_className, methodName);
	}

	/**
	 * Adds a <code>ColumnHint</code> for this statement to map design time column
	 * projections with the named runtime result set metadata.
	 * 
	 * @param resultSetName the name of the result set.
	 * @param columnHint    a <code>ColumnHint</code> instance.
	 * @throws DataException if data source error occurs.
	 */
	public void addColumnHint(String resultSetName, ColumnHint columnHint) throws DataException {
		String methodName = "addColumnHint"; //$NON-NLS-1$
		if (getLogger().isLoggingEnterExitLevel())
			getLogger().entering(sm_className, methodName, new Object[] { resultSetName, columnHint });

		validateNamedResultsSupport();

		if (columnHint != null) {
			// no need to reset the current metadata because adding a column
			// hint doesn't change the existing columns that are being projected,
			// it just updates some of the column metadata
			getProjectedColumns(resultSetName).addHint(columnHint);
		}

		getLogger().exiting(sm_className, methodName);
	}

	/**
	 * Adds a <code>ColumnHint</code> for this statement to map design time column
	 * projections with the runtime result set metadata of the specified sequential
	 * result set.
	 * 
	 * @param resultSetNum a 1-based index number that indicates the sequence of a
	 *                     result set among a sequential set of multiple result sets
	 * @param columnHint   a <code>ColumnHint</code> instance.
	 * @throws DataException if data source error occurs.
	 */
	public void addColumnHint(int resultSetNum, ColumnHint columnHint) throws DataException {
		getSequentialResultHandler().addColumnHint(resultSetNum, columnHint);
	}

	private ArrayList getParameterHints() {
		if (m_parameterHints == null)
			m_parameterHints = new ArrayList();

		return m_parameterHints;
	}

	/**
	 * Adds a <code>ParameterHint</code> for this statement to map static parameter
	 * definitions with the runtime parameter metadata.
	 * 
	 * @param paramHint a <code>ParameterHint</code> instance.
	 * @throws DataException if data source error occurs.
	 */
	public void addParameterHint(ParameterHint paramHint) throws DataException {
		String methodName = "addParameterHint"; //$NON-NLS-1$
		getLogger().entering(sm_className, methodName, paramHint);

		if (paramHint != null) {
			validateAndAddParameterHint(paramHint);

			// if we've successfully added a parameter hint, then we need to invalidate
			// previous version of parameter metadata
			m_parameterMetaData = null;
		}

		getLogger().exiting(sm_className, methodName);
	}

	private void validateAndAddParameterHint(ParameterHint newParameterHint) throws DataException {
		String methodName = "validateAndAddParameterHint"; //$NON-NLS-1$
		getLogger().entering(sm_className, methodName, newParameterHint);

		ArrayList parameterHintsList = getParameterHints();
		String newParamHintName = newParameterHint.getName();
		int newParamHintIndex = newParameterHint.getPosition();
		for (int i = 0, n = parameterHintsList.size(); i < n; i++) {
			ParameterHint existingParamHint = (ParameterHint) parameterHintsList.get(i);

			String existingParamHintName = existingParamHint.getName();
			if (!existingParamHintName.equals(newParamHintName)) {
				int existingParamHintPosition = existingParamHint.getPosition();

				// different names and parameter index is either 0 or didn't
				// match, so keep on looking
				if (newParamHintIndex == 0 || existingParamHintPosition != newParamHintIndex)
					continue;

				// we don't want to allow different parameter hint name with the
				// same parameter hint position
				throwError(ResourceConstants.DIFFERENT_PARAM_NAME_FOR_SAME_POSITION,
						new Object[] { existingParamHintName, Integer.valueOf(existingParamHintPosition) }, methodName);
			}

			// the name of the existing hint matches the new hint,
			// but the parameter index didn't match. Ignore the parameter
			// index mismatch if either index is 0
			int existingParamHintIndex = existingParamHint.getPosition();
			if (existingParamHintIndex != newParamHintIndex && existingParamHintIndex > 0 && newParamHintIndex > 0) {
				throwError(ResourceConstants.SAME_PARAM_NAME_FOR_DIFFERENT_HINTS, existingParamHintName, methodName);
			}

			// no validation is done on their native names, even if both are defined,
			// as it is considered a hint attribute, and not an unique identifier

			// same parameter hint name and parameter hint index, so we're
			// referring to the same hint, just update the existing one with
			// the new info
			existingParamHint.updateHint(newParameterHint);
			getLogger().logp(Level.FINER, sm_className, methodName,
					"Updating parameter hint with attributes in another hint that has the same name ({0}).", //$NON-NLS-1$
					existingParamHintName);

			getLogger().exiting(sm_className, methodName);
			return;
		}

		// new hint name didn't match any of the existing hints, so we'll need to add
		// it to the list.
		parameterHintsList.add(newParameterHint);

		getLogger().exiting(sm_className, methodName);
	}

	/**
	 * Sets the names of all projected columns. If this method is not called, then
	 * all columns in the runtime metadata are projected. The specified projected
	 * names can be either a column name or column alias.
	 * 
	 * @param projectedNames the projected column names.
	 * @throws DataException if data source error occurs.
	 */
	public void setColumnsProjection(String[] projectedNames) throws DataException {
		String methodName = "setColumnsProjection"; //$NON-NLS-1$
		getLogger().entering(sm_className, methodName, projectedNames);

		resetCurrentResultAndMetaData();
		getProjectedColumns().setProjectedNames(projectedNames);

		getLogger().exiting(sm_className, methodName);
	}

	/**
	 * Sets the names of all projected columns for the specified result set. If this
	 * method is not called, then all columns in the specified result set metadata
	 * are projected. The specified projected names can be either a column name or
	 * column alias.
	 * 
	 * @param resultSetName  the name of the result set.
	 * @param projectedNames the projected column names.
	 * @throws DataException if data source error occurs.
	 */
	public void setColumnsProjection(String resultSetName, String[] projectedNames) throws DataException {
		String methodName = "setColumnsProjection"; //$NON-NLS-1$
		if (getLogger().isLoggingEnterExitLevel())
			getLogger().entering(sm_className, methodName, new Object[] { resultSetName, projectedNames });

		validateNamedResultsSupport();
		resetResultAndMetaData(resultSetName);
		getProjectedColumns(resultSetName).setProjectedNames(projectedNames);

		getLogger().exiting(sm_className, methodName);
	}

	/**
	 * Sets the names of all projected columns for the specified result set. If this
	 * method is not called, then all columns in the specified result set metadata
	 * are projected. The specified projected names can be either a column name or
	 * column alias. The method can be called before this statement is executed.
	 * 
	 * @param resultSetNum   a 1-based index number that indicates the sequence of a
	 *                       result set among a sequential set of multiple result
	 *                       sets
	 * @param projectedNames the projected column names.
	 * @throws DataException if data source error occurs, such as if the underlying
	 *                       ODA driver does not support calling
	 *                       {@link IQuery#getMetaData()} before the query is
	 *                       executed.
	 */
	public void setColumnsProjection(int resultSetNum, String[] projectedNames) throws DataException {
		getSequentialResultHandler().setColumnsProjection(resultSetNum, projectedNames);
	}

	/**
	 * Declares a new custom column for the corresponding <code>IResultClass</code>.
	 * 
	 * @param columnName the custom column name.
	 * @param columnType the custom column type.
	 * @throws DataException if data source error occurs.
	 */
	public void declareCustomColumn(String columnName, Class columnType) throws DataException {
		String methodName = "declareCustomColumn"; //$NON-NLS-1$
		if (getLogger().isLoggingEnterExitLevel())
			getLogger().entering(sm_className, methodName, new Object[] { columnName, columnType });

		assert columnName != null;
		assert columnName.length() != 0;

		// need to reset current metadata because a custom column could be
		// declared after we projected all columns, which means we would
		// want to project the newly declared custom column as well
		resetCurrentResultAndMetaData();
		getProjectedColumns().addCustomColumn(columnName, columnType);

		getLogger().exiting(sm_className, methodName);
	}

	/**
	 * Declares a new custom column for the <code>IResultClass</code> of the
	 * specified result set.
	 * 
	 * @param resultSetName the name of the result set.
	 * @param columnName    the custom column name.
	 * @param columnType    the custom column type.
	 * @throws DataException if data source error occurs.
	 */
	public void declareCustomColumn(String resultSetName, String columnName, Class columnType) throws DataException {
		String methodName = "declareCustomColumn"; //$NON-NLS-1$
		if (getLogger().isLoggingEnterExitLevel())
			getLogger().entering(sm_className, methodName, new Object[] { resultSetName, columnName, columnType });

		validateNamedResultsSupport();

		assert columnName != null;
		assert columnName.length() != 0;

		// need to reset current metadata because a custom column could be
		// declared after we projected all columns, which means we would
		// want to project the newly declared custom column as well
		resetResultAndMetaData(resultSetName);
		getProjectedColumns(resultSetName).addCustomColumn(columnName, columnType);

		getLogger().exiting(sm_className, methodName);
	}

	/**
	 * Declares a new custom column for the <code>IResultClass</code> of the
	 * specified sequential result set. The method can be called before this
	 * statement is executed.
	 * 
	 * @param resultSetNum a 1-based index number that indicates the sequence of a
	 *                     result set among a sequential set of multiple result sets
	 * @param columnName   the custom column name.
	 * @param columnType   the custom column type.
	 * @throws DataException if data source error occurs.
	 */
	public void declareCustomColumn(int resultSetNum, String columnName, Class columnType) throws DataException {
		getSequentialResultHandler().declareCustomColumn(resultSetNum, columnName, columnType);
	}

	// if a caller tries to add custom columns or sets a new set of
	// column projection, then we want to generate a new set of metadata
	// for m_currentResultClass or the specified result set name. we also
	// no longer want to keep the reference to the m_currentResultSet or
	// the reference associated with the result set name because we would
	// no longer be interested in its metadata afterwards.
	private void resetCurrentResultAndMetaData() {
		final String methodName = "resetCurrentResultAndMetaData"; //$NON-NLS-1$
		getLogger().entering(sm_className, methodName);

		m_currentResultClass = null;
		m_currentResultSet = null;

		getLogger().exiting(sm_className, methodName);
	}

	private void resetResultAndMetaData(String resultSetName) {
		final String methodName = "resetResultAndMetaData(String)"; //$NON-NLS-1$
		getLogger().entering(sm_className, methodName, resultSetName);

		getNamedCurrentResultClasses().remove(resultSetName);
		getNamedCurrentResultSets().remove(resultSetName);

		getLogger().exiting(sm_className, methodName);
	}

	private Hashtable getNamedProjectedColumns() {
		if (m_namedProjectedColumns == null)
			m_namedProjectedColumns = PropertySecurity.createHashtable();

		return m_namedProjectedColumns;
	}

	private Hashtable getNamedCurrentResultClasses() {
		if (m_namedCurrentResultClasses == null)
			m_namedCurrentResultClasses = PropertySecurity.createHashtable();

		return m_namedCurrentResultClasses;
	}

	private Hashtable getNamedCurrentResultSets() {
		if (m_namedCurrentResultSets == null)
			m_namedCurrentResultSets = PropertySecurity.createHashtable();

		return m_namedCurrentResultSets;
	}

	private IQuery getStatement() {
		return m_statement;
	}

	private IAdvancedQuery getAdvancedStatement() {
		assert (isAdvancedQuery());
		return (IAdvancedQuery) m_statement;
	}

	private boolean isAdvancedQuery() {
		return (m_statement instanceof IAdvancedQuery);
	}

	/**
	 * Indicates whether this statement supports accessing its result set(s) by
	 * name. This can only support named result sets if the underlying ODA driver
	 * has indicated so in its implementation of
	 * {@link org.eclipse.datatools.connectivity.oda.IDataSetMetaData#supportsNamedResultSets()},
	 * and this underlying object is an IAdvancedQuery.
	 * 
	 * @return true if result sets can be accessed by name; false otherwise
	 * @throws DataException
	 */
	public boolean supportsNamedResults() throws DataException {
		final String methodName = "supportsNamedResults"; //$NON-NLS-1$
		getLogger().entering(sm_className, methodName);

		if (m_supportsNamedResults == null) // unknown
		{
			boolean isSupported = isAdvancedQuery()
					&& m_connection.getMetaData(m_dataSetType).supportsNamedResultSets();
			m_supportsNamedResults = Boolean.valueOf(isSupported);
		}

		getLogger().exiting(sm_className, methodName, m_supportsNamedResults);

		return m_supportsNamedResults.booleanValue();
	}

	/**
	 * Indicates whether this statement supports input parameter(s). This can only
	 * support input parameters if the underlying ODA driver has indicated so in its
	 * implementation of
	 * {@link org.eclipse.datatools.connectivity.oda.IDataSetMetaData#supportsInParameters()}.
	 * Support for accessing an input parameter by name must be checked separately.
	 * 
	 * @return true if the query may have input parameter(s); false otherwise
	 * @throws DataException
	 */
	private boolean supportsInputParameter() throws DataException {
		final String methodName = "supportsInputParameter"; //$NON-NLS-1$
		getLogger().entering(sm_className, methodName);

		if (m_supportsInputParameters == null) // unknown
		{
			m_supportsInputParameters = Boolean.valueOf(m_connection.getMetaData(m_dataSetType).supportsInParameters());
		}

		getLogger().exiting(sm_className, methodName, m_supportsInputParameters);

		return m_supportsInputParameters.booleanValue();
	}

	/**
	 * Indicates whether this statement supports output parameter(s). This can only
	 * support output parameters if the underlying ODA driver has indicated so in
	 * its implementation of
	 * {@link org.eclipse.datatools.connectivity.oda.IDataSetMetaData#supportsOutParameters()},
	 * and this underlying object is an IAdvancedQuery. Support for accessing an
	 * output parameter by name must be checked separately.
	 * 
	 * @return true if the query may have output parameter(s); false otherwise
	 * @throws DataException
	 */
	private boolean supportsOutputParameter() throws DataException {
		final String methodName = "supportsOutputParameter"; //$NON-NLS-1$
		getLogger().entering(sm_className, methodName);

		if (m_supportsOutputParameters == null) // unknown
		{
			boolean isSupported = isAdvancedQuery() && m_connection.getMetaData(m_dataSetType).supportsOutParameters();
			m_supportsOutputParameters = Boolean.valueOf(isSupported);
		}

		getLogger().exiting(sm_className, methodName, m_supportsOutputParameters);

		return m_supportsOutputParameters.booleanValue();
	}

	/**
	 * Indicates whether this statement supports accessing an input/output parameter
	 * by name. This can only support named parameters if the underlying ODA driver
	 * has indicated so in its implementation of
	 * {@link org.eclipse.datatools.connectivity.oda.IDataSetMetaData#supportsNamedParameters()}.
	 * Support for input or output parameters must be checked separately.
	 * 
	 * @return true if an input/output parameter can be accessed by name; false
	 *         otherwise
	 * @throws DataException
	 */
	public boolean supportsNamedParameter() throws DataException {
		final String methodName = "supportsNamedParameter"; //$NON-NLS-1$
		getLogger().entering(sm_className, methodName);

		if (m_supportsNamedParameters == null) // unknown
		{
			boolean isSupported = m_connection.getMetaData(m_dataSetType).supportsNamedParameters();
			m_supportsNamedParameters = Boolean.valueOf(isSupported);
		}

		getLogger().exiting(sm_className, methodName, m_supportsNamedParameters);

		return m_supportsNamedParameters.booleanValue();
	}

	/**
	 * Indicates whether this statement supports retrieving multiple result set(s).
	 * This can only support multiple result sets if the underlying ODA driver has
	 * indicated so in its implementation of
	 * {@link org.eclipse.datatools.connectivity.oda.IDataSetMetaData#supportsMultipleResultSets()},
	 * and this underlying object is an IAdvancedQuery.
	 * 
	 * @return true if multiple result sets can be accessed; false otherwise
	 * @throws DataException
	 */
	public boolean supportsMultipleResultSets() throws DataException {
		final String methodName = "supportsMultipleResultSets"; //$NON-NLS-1$
		getLogger().entering(sm_className, methodName);

		if (m_supportsMultipleResultSets == null) // unknown
		{
			boolean isSupported = isAdvancedQuery()
					&& m_connection.getMetaData(m_dataSetType).supportsMultipleResultSets();
			m_supportsMultipleResultSets = Boolean.valueOf(isSupported);
		}

		getLogger().exiting(sm_className, methodName, m_supportsMultipleResultSets);

		return m_supportsMultipleResultSets.booleanValue();
	}

	private void validateNamedResultsSupport() throws DataException {
		final String methodName = "validateNamedResultsSupport"; //$NON-NLS-1$
		// this can only support named result sets if the underlying object is at
		// least an IAdvancedQuery
		if (!supportsNamedResults()) {
			throwUnsupportedException(ResourceConstants.NAMED_RESULTSETS_UNSUPPORTED, methodName);
		}
	}

	/**
	 * Returns a collection of <code>ParameterMetaData</code>, which contains the
	 * parameter metadata information for each parameter that is known at the time
	 * that <code>getParameterMetaData()</code> is called. The collection is
	 * retrieved from the ODA runtime driver's <code>IParameterMetaData</code>, if
	 * available. In addition, it includes the supplemental metadata defined in the
	 * <code>InputParameterHint</code> and <code>OutputParameterHint</code> provided
	 * to this <code>PreparedStatement</code>.
	 * 
	 * @return a collection of <code>ParameterMetaData</code>, or null if no
	 *         parameter metadata is available.
	 * @throws DataException if data source error occurs.
	 */

	public Collection getParameterMetaData() throws DataException {
		String methodName = "getParameterMetaData"; //$NON-NLS-1$
		getLogger().entering(sm_className, methodName);

		if (m_parameterMetaData == null) {
			if (!supportsInputParameter() && !supportsOutputParameter()) {
				getLogger().logp(Level.INFO, sm_className, methodName,
						"The ODA driver does not support any type of parameters (IDataSetMetaData); no metadata is available."); //$NON-NLS-1$
				getLogger().exiting(sm_className, methodName, null);
				return null;
			}

			// the ODA driver supports in/out parameters
			IParameterMetaData odaParamMetaData = null;
			try {
				odaParamMetaData = getOdaDriverParamMetaData();
			} catch (DataException e) {
				// if parameter hints exist, proceed with
				// returning its metadata; otherwise, throw exception
				if (m_parameterHints == null || m_parameterHints.size() <= 0)
					throw e;
			}

			m_parameterMetaData = (odaParamMetaData == null) ? mergeParamHints()
					: mergeParamHintsWithMetaData(odaParamMetaData);
		}

		getLogger().exiting(sm_className, methodName, m_parameterMetaData);
		return m_parameterMetaData;
	}

	private ParameterMetaData getParameterMetaData(int paramIndex) throws DataException {
		final String methodName = "getParameterMetaData( int )"; //$NON-NLS-1$
		getLogger().entering(sm_className, methodName, paramIndex);

		Collection allParamsMetadata = null;
		if (paramIndex > 0) // index is 1-based
			allParamsMetadata = getParameterMetaData();
		if (allParamsMetadata != null) {
			Iterator paramMDIter = allParamsMetadata.iterator();
			while (paramMDIter.hasNext()) {
				ParameterMetaData aParamMetaData = (ParameterMetaData) paramMDIter.next();
				if (aParamMetaData.getPosition() == paramIndex) {
					getLogger().exiting(sm_className, methodName, aParamMetaData);
					return aParamMetaData;
				}
			}
		}

		// no parameters defined, or didn't find matching parameter index position
		throwError(ResourceConstants.CANNOT_GET_PARAMETER_METADATA, Integer.valueOf(paramIndex), methodName);
		return null;
	}

	private ParameterMetaData getParameterMetaData(ParameterName paramName) throws DataException {
		final String methodName = "getParameterMetaData( ParameterName )"; //$NON-NLS-1$
		getLogger().entering(sm_className, methodName, paramName);

		ParameterMetaData aParamMetaData = findParameterMetaDataByName(getParameterMetaData(), paramName);

		getLogger().exiting(sm_className, methodName, aParamMetaData);
		return aParamMetaData;
	}

	/**
	 * Lookup corresponding native name in merged parameter runtime metadata and
	 * design hints.
	 */
	private String getNativeNameFromParameterMetaData(String romParamName) {
		ParameterMetaData effectiveParamMd = null;
		try {
			effectiveParamMd = findParameterMetaDataByName(getParameterMetaData(), romParamName, false);

		} catch (DataException ex) {
			// ignore; up to caller to decide if it should proceed
		}

		return (effectiveParamMd != null) ? effectiveParamMd.getNativeName() : null;
	}

	private IParameterMetaData getOdaDriverParamMetaData() throws DataException {
		String methodName = "getOdaDriverParamMetaData"; //$NON-NLS-1$
		getLogger().entering(sm_className, methodName);

		IParameterMetaData odaParamMetaData = null;
		try {
			odaParamMetaData = m_statement.getParameterMetaData();
		} catch (OdaException ex) {
			throwException(ex, ResourceConstants.CANNOT_GET_PARAMETER_METADATA, methodName);
		} catch (UnsupportedOperationException ex) {
			getLogger().logp(Level.WARNING, sm_className, methodName,
					"The ODA driver is not capable of providing parameter metadata.", ex); //$NON-NLS-1$
			// ignore, and continue to return null metadata
		}

		getLogger().exiting(sm_className, methodName, odaParamMetaData);
		return odaParamMetaData;
	}

	private Collection mergeParamHints() throws DataException {
		String methodName = "mergeParamHints"; //$NON-NLS-1$
		getLogger().entering(sm_className, methodName);

		ArrayList parameterMetaData = null;

		// add the parameter hints, if any.
		if (m_parameterHints != null && m_parameterHints.size() > 0) {
			parameterMetaData = new ArrayList();
			addParameterHints(parameterMetaData, m_parameterHints);
		}

		getLogger().exiting(sm_className, methodName, parameterMetaData);

		return parameterMetaData;
	}

	private void addParameterHints(List parameterMetaData, List parameterHints) {
		String methodName = "addParameterHints"; //$NON-NLS-1$
		if (getLogger().isLoggingEnterExitLevel())
			getLogger().entering(sm_className, methodName, new Object[] { parameterMetaData, parameterHints });

		ListIterator iter = parameterHints.listIterator();
		while (iter.hasNext()) {
			ParameterHint paramHint = (ParameterHint) iter.next();
			ParameterMetaData paramMd = new ParameterMetaData(paramHint, m_connection.getDataSourceId(), m_dataSetType);
			parameterMetaData.add(paramMd);
		}

		getLogger().exiting(sm_className, methodName);
	}

	private Collection mergeParamHintsWithMetaData(IParameterMetaData runtimeParamMetaData) throws DataException {
		String methodName = "mergeParamHintsWithMetaData"; //$NON-NLS-1$
		getLogger().entering(sm_className, methodName, runtimeParamMetaData);

		assert (runtimeParamMetaData != null);

		// first create a ParameterMetaData for each parameter,
		// based on runtime metadata
		int numOfParameters = doGetParameterCount(runtimeParamMetaData);
		ArrayList paramMetaData = new ArrayList(numOfParameters);

		for (int i = 1; i <= numOfParameters; i++) {
			ParameterMetaData paramMd = new ParameterMetaData(runtimeParamMetaData, i, m_connection.getDataSourceId(),
					m_dataSetType);
			paramMetaData.add(paramMd);
		}

		// then supplement all parameters' runtime metadata with design hints
		if (m_parameterHints != null && m_parameterHints.size() > 0)
			updateWithParameterHints(paramMetaData, m_parameterHints);

		getLogger().exiting(sm_className, methodName, paramMetaData);

		return paramMetaData;
	}

	private int doGetParameterCount(IParameterMetaData runtimeParamMetaData) throws DataException {
		String methodName = "doGetParameterCount"; //$NON-NLS-1$
		getLogger().entering(sm_className, methodName, runtimeParamMetaData);

		try {
			int ret = runtimeParamMetaData.getParameterCount();

			getLogger().exiting(sm_className, methodName, ret);
			return ret;
		} catch (OdaException ex) {
			throwException(ex, ResourceConstants.CANNOT_GET_PARAMETER_COUNT, methodName);
		} catch (UnsupportedOperationException ex) {
			throwUnsupportedException(ResourceConstants.CANNOT_GET_PARAMETER_COUNT, methodName);
		}
		return 0;
	}

	/**
	 * Supplement runtime parameter metadata with design hints.
	 * 
	 * @param parametersMetaData
	 * @param parameterHints
	 * @throws DataException
	 */
	private void updateWithParameterHints(List parametersMetaData, List parameterHints) throws DataException {
		final String methodName = "updateWithParameterHints"; //$NON-NLS-1$
		if (getLogger().isLoggingEnterExitLevel())
			getLogger().entering(sm_className, methodName, new Object[] { parametersMetaData, parameterHints });

		if (parametersMetaData == null || parametersMetaData.isEmpty() || parameterHints == null
				|| parameterHints.isEmpty()) {
			getLogger().exiting(sm_className, methodName);
			return; // nothing to update or update with
		}

		ListIterator iter = parameterHints.listIterator();
		while (iter.hasNext()) {
			ParameterHint paramHint = (ParameterHint) iter.next();

			// find corresponding parameter metadata to update
			ParameterMetaData paramMd = findParameterMetaData(parametersMetaData, paramHint);
			if (paramMd == null)
				continue; // can't find a runtime parameter metadata that matches the hint

			// found matching runtime parameter metadata and design hint,
			// merge design hint into runtime metadata
			paramMd.updateWith(paramHint, m_connection.getDataSourceId(), m_dataSetType);
		}

		getLogger().exiting(sm_className, methodName);
	}

	private ParameterMetaData findParameterMetaData(List parametersMetaData, ParameterHint paramHint)
			throws DataException {
		String paramHintNativeName = paramHint.getNativeName();
		int position = 0;
		if (hasValue(paramHintNativeName)) {
			ParameterMetaData paramMd = findParameterMetaDataByName(parametersMetaData, paramHintNativeName, true);
			if (paramMd != null) // found a match by native name
				return paramMd; // done

			// next try to get the parameter index by native name from the runtime driver
			if (paramHint.isInputMode())
				position = getRuntimeParameterIndexFromName(paramHintNativeName, true /* forInput */ );

			if (paramHint.isOutputMode() && (position <= 0 || position > parametersMetaData.size())) {
				position = getRuntimeParameterIndexFromName(paramHintNativeName, false /* forInput */ );
			}
		}

		// couldn't find the index by the param native name,
		// use the position in the hint itself.
		int numOfRuntimeParameters = parametersMetaData.size();
		if (position <= 0 || position > numOfRuntimeParameters) // position not yet found
			position = paramHint.getPosition();

		// can't find a match of the given hint among runtime parameter metadata
		if (position <= 0 || position > numOfRuntimeParameters) // invalid position value
			return null;

		// has valid 1-based position, return corresponding metadata
		return (ParameterMetaData) parametersMetaData.get(position - 1);
	}

	private static ParameterMetaData findParameterMetaDataByName(Collection parametersMetaData,
			ParameterName paramName) {
		if (paramName == null)
			return null; // nothing to match against

		// first try to find a match by its native name
		ParameterMetaData paramMd = findParameterMetaDataByName(parametersMetaData, paramName.getNativeName(), true);

		// if not found, or no native name defined,
		// next find a match by its ROM name
		if (paramMd == null)
			paramMd = findParameterMetaDataByName(parametersMetaData, paramName.getRomName(), false);

		// still not found, try find a match by its effective name that will be used to
		// interact with underlying ODA driver
		if (paramMd == null)
			paramMd = findParameterMetaDataByName(parametersMetaData, paramName.getEffectiveName(), true);

		return paramMd;
	}

	private static ParameterMetaData findParameterMetaDataByName(Collection parametersMetaData, String paramName,
			boolean useNativeName) {
		// empty name is not unique and cannot be used to find a unique match
		if (parametersMetaData == null || parametersMetaData.isEmpty() || !hasValue(paramName))
			return null; // nothing to match against

		Iterator iter = parametersMetaData.iterator();
		while (iter.hasNext()) {
			ParameterMetaData paramMd = (ParameterMetaData) iter.next();

			if (useNativeName && paramName.equals(paramMd.getNativeName()))
				return paramMd;
			if (!useNativeName && paramName.equals(paramMd.getName()))
				return paramMd;
		}
		return null;
	}

	private int getRuntimeParameterIndexFromName(String paramName, boolean forInput) throws DataException {
		String methodName = "getRuntimeParameterIndexFromName"; //$NON-NLS-1$
		if (getLogger().isLoggingEnterExitLevel())
			getLogger().entering(sm_className, methodName, new Object[] { paramName, Boolean.valueOf(forInput) });

		if (forInput) {
			try {
				int ret = findInParameter(paramName);

				getLogger().exiting(sm_className, methodName, ret);
				return ret;
			} catch (DataException ex) {
				// findInParameter is not supported by underlying ODA driver
				if (ex.getCause() instanceof UnsupportedOperationException) {
					getLogger().exiting(sm_className, methodName, 0);
					return 0;
				}

				getLogger().logp(Level.SEVERE, sm_className, methodName, "Cannot get runtime parameter index.", ex); //$NON-NLS-1$

				throw ex;
			}
		}

		// for output parameter
		try {
			int ret = findOutParameter(paramName);

			getLogger().exiting(sm_className, methodName, ret);
			return ret;
		} catch (DataException ex) {
			// findOutParameter is not supported
			if (ex.getCause() instanceof UnsupportedOperationException) {
				getLogger().exiting(sm_className, methodName, 0);
				return 0;
			}

			getLogger().logp(Level.SEVERE, sm_className, methodName, "Cannot get runtime parameter index.", ex); //$NON-NLS-1$

			throw ex;
		}
	}

	private void validateOutputParameterSupport() throws DataException {
		final String methodName = "validateOutputParameterSupport"; //$NON-NLS-1$

		// this can only support output parameter if the underlying object is at
		// least an IAdvancedQuery
		if (!supportsOutputParameter()) {
			throwUnsupportedException(ResourceConstants.OUTPUT_PARAMETERS_UNSUPPORTED, methodName);
		}
	}

	private Object getParameterValue(String paramName, int paramIndex) throws DataException {
		final String methodName = "getParameterValue( String, int )"; //$NON-NLS-1$
		if (getLogger().isLoggingEnterExitLevel())
			getLogger().entering(sm_className, methodName, new Object[] { paramName, Integer.valueOf(paramIndex) });

		validateOutputParameterSupport();

		// delegate to ParameterName for the proper name to use when
		// interacting with underlying oda runtime driver
		ParameterName paramNameObj = null;
		if (paramName != null) // getting parameter value by name
		{
			paramNameObj = new ParameterName(paramName, this);

			// log if not able to find corresponding native name
			paramNameObj.logNullNativeName();
		}

		Object paramValue = null;
		int paramType = (paramNameObj == null) ? getParameterType(paramIndex) : getParameterType(paramNameObj, false);

		switch (paramType) {
		case Types.INTEGER:
			int i = (paramNameObj == null) ? doGetInt(paramIndex) : getInt(paramNameObj);
			if (!wasNull())
				paramValue = Integer.valueOf(i);
			break;

		case Types.DOUBLE:
			double d = (paramNameObj == null) ? doGetDouble(paramIndex) : getDouble(paramNameObj);
			if (!wasNull())
				paramValue = new Double(d);
			break;

		case Types.CHAR:
			paramValue = (paramNameObj == null) ? doGetString(paramIndex) : getString(paramNameObj);
			break;

		case Types.DECIMAL:
			paramValue = (paramNameObj == null) ? doGetBigDecimal(paramIndex) : getBigDecimal(paramNameObj);
			break;

		case Types.DATE:
			paramValue = (paramNameObj == null) ? doGetDate(paramIndex) : getDate(paramNameObj);
			break;

		case Types.TIME:
			paramValue = (paramNameObj == null) ? doGetTime(paramIndex) : getTime(paramNameObj);
			break;

		case Types.TIMESTAMP:
			paramValue = (paramNameObj == null) ? doGetTimestamp(paramIndex) : getTimestamp(paramNameObj);
			break;

		case Types.BLOB:
			paramValue = (paramNameObj == null) ? doGetBlob(paramIndex) : getBlob(paramNameObj);
			break;

		case Types.CLOB:
			paramValue = (paramNameObj == null) ? doGetClob(paramIndex) : getClob(paramNameObj);
			break;

		case Types.BOOLEAN:
			paramValue = (paramNameObj == null) ? doGetBoolean(paramIndex) : getBoolean(paramNameObj);
			break;

		case Types.JAVA_OBJECT:
			paramValue = (paramNameObj == null) ? doGetObject(paramIndex) : getObject(paramNameObj);
			break;

		default:
			assert false; // exception now thrown by DriverManager
		}

		Object ret = (wasNull()) ? null : paramValue;

		getLogger().exiting(sm_className, methodName, ret);
		return ret;
	}

	// the following data type getters are by name and need additional processing in
	// the
	// case where a named parameter is not supported by the underlying data source.
	// In that case, we will look at the output parameter hints to get the name to
	// id mapping

	private int getInt(ParameterName paramName) throws DataException {
		final String methodName = "getInt( ParameterName )"; //$NON-NLS-1$
		getLogger().entering(sm_className, methodName, paramName);

		int ret = 0;

		if (!supportsNamedParameter()) {
			int paramIndex = getIndexFromParamHints(paramName.getRomName());
			if (paramIndex > 0)
				ret = doGetInt(paramIndex);
		} else {
			ret = doGetInt(paramName);
		}

		getLogger().exiting(sm_className, methodName, ret);
		return ret;
	}

	private double getDouble(ParameterName paramName) throws DataException {
		final String methodName = "getDouble( ParameterName )"; //$NON-NLS-1$
		getLogger().entering(sm_className, methodName, paramName);

		double ret = 0;

		if (!supportsNamedParameter()) {
			int paramIndex = getIndexFromParamHints(paramName.getRomName());
			if (paramIndex > 0)
				ret = doGetDouble(paramIndex);
		} else {
			ret = doGetDouble(paramName);
		}

		if (getLogger().isLoggingEnterExitLevel())
			getLogger().exiting(sm_className, methodName, new Double(ret));
		return ret;
	}

	private String getString(ParameterName paramName) throws DataException {
		final String methodName = "getString( ParameterName )"; //$NON-NLS-1$
		getLogger().entering(sm_className, methodName, paramName);

		String ret = null;

		if (!supportsNamedParameter()) {
			int paramIndex = getIndexFromParamHints(paramName.getRomName());
			if (paramIndex > 0)
				ret = doGetString(paramIndex);
		} else {
			ret = doGetString(paramName);
		}

		getLogger().exiting(sm_className, methodName, ret);
		return ret;
	}

	private BigDecimal getBigDecimal(ParameterName paramName) throws DataException {
		final String methodName = "getBigDecimal( ParameterName )"; //$NON-NLS-1$
		getLogger().entering(sm_className, methodName, paramName);

		BigDecimal ret = null;

		if (!supportsNamedParameter()) {
			int paramIndex = getIndexFromParamHints(paramName.getRomName());
			if (paramIndex > 0)
				ret = doGetBigDecimal(paramIndex);
		} else {
			ret = doGetBigDecimal(paramName);
		}

		getLogger().exiting(sm_className, methodName, ret);
		return ret;
	}

	private java.util.Date getDate(ParameterName paramName) throws DataException {
		final String methodName = "getDate( ParameterName )"; //$NON-NLS-1$
		getLogger().entering(sm_className, methodName, paramName);

		java.util.Date ret = null;

		if (!supportsNamedParameter()) {
			int paramIndex = getIndexFromParamHints(paramName.getRomName());
			if (paramIndex > 0)
				ret = doGetDate(paramIndex);
		} else {
			ret = doGetDate(paramName);
		}

		getLogger().exiting(sm_className, methodName, ret);
		return ret;
	}

	private Time getTime(ParameterName paramName) throws DataException {
		final String methodName = "getTime( ParameterName )"; //$NON-NLS-1$
		getLogger().entering(sm_className, methodName, paramName);

		Time ret = null;

		if (!supportsNamedParameter()) {
			int paramIndex = getIndexFromParamHints(paramName.getRomName());
			if (paramIndex > 0)
				ret = doGetTime(paramIndex);
		} else {
			ret = doGetTime(paramName);
		}

		getLogger().exiting(sm_className, methodName, ret);
		return ret;
	}

	private Timestamp getTimestamp(ParameterName paramName) throws DataException {
		final String methodName = "getTimestamp( ParameterName )"; //$NON-NLS-1$
		getLogger().entering(sm_className, methodName, paramName);

		Timestamp ret = null;

		if (!supportsNamedParameter()) {
			int paramIndex = getIndexFromParamHints(paramName.getRomName());
			if (paramIndex > 0)
				ret = doGetTimestamp(paramIndex);
		} else {
			ret = doGetTimestamp(paramName);
		}

		getLogger().exiting(sm_className, methodName, ret);
		return ret;
	}

	private IBlob getBlob(ParameterName paramName) throws DataException {
		final String methodName = "getBlob( ParameterName )"; //$NON-NLS-1$
		getLogger().entering(sm_className, methodName, paramName);

		IBlob ret = null;

		if (!supportsNamedParameter()) {
			int paramIndex = getIndexFromParamHints(paramName.getRomName());
			if (paramIndex > 0)
				ret = doGetBlob(paramIndex);
		} else {
			ret = doGetBlob(paramName);
		}

		getLogger().exiting(sm_className, methodName, ret);
		return ret;
	}

	private IClob getClob(ParameterName paramName) throws DataException {
		final String methodName = "getClob( ParameterName )"; //$NON-NLS-1$
		getLogger().entering(sm_className, methodName, paramName);

		IClob ret = null;

		if (!supportsNamedParameter()) {
			int paramIndex = getIndexFromParamHints(paramName.getRomName());
			if (paramIndex > 0)
				ret = doGetClob(paramIndex);
		} else {
			ret = doGetClob(paramName);
		}

		getLogger().exiting(sm_className, methodName, ret);
		return ret;
	}

	private Boolean getBoolean(ParameterName paramName) throws DataException {
		final String methodName = "getBoolean( ParameterName )"; //$NON-NLS-1$
		getLogger().entering(sm_className, methodName, paramName);

		Boolean ret = null;

		if (!supportsNamedParameter()) {
			int paramIndex = getIndexFromParamHints(paramName.getRomName());
			if (paramIndex > 0)
				ret = doGetBoolean(paramIndex);
		} else {
			ret = doGetBoolean(paramName);
		}

		getLogger().exiting(sm_className, methodName, ret);
		return ret;
	}

	private Object getObject(ParameterName paramName) throws DataException {
		final String methodName = "getObject( ParameterName )"; //$NON-NLS-1$
		getLogger().entering(sm_className, methodName, paramName);

		Object ret = null;

		if (!supportsNamedParameter()) {
			int paramIndex = getIndexFromParamHints(paramName.getRomName());
			if (paramIndex > 0)
				ret = doGetObject(paramIndex);
		} else {
			ret = doGetObject(paramName);
		}

		getLogger().exiting(sm_className, methodName, ret);
		return ret;
	}

	private int doGetInt(int paramIndex) throws DataException {
		final String methodName = "doGetInt( int )"; //$NON-NLS-1$
		final String errorCode = ResourceConstants.CANNOT_GET_INT_FROM_PARAMETER;
		getLogger().entering(sm_className, methodName, paramIndex);

		try {
			int ret = getAdvancedStatement().getInt(paramIndex);

			getLogger().exiting(sm_className, methodName, ret);
			return ret;
		} catch (OdaException ex) {
			throwException(ex, errorCode, paramIndex, methodName);
		} catch (UnsupportedOperationException ex) {
			throwException(ex, errorCode, paramIndex, methodName);
		}
		return 0;
	}

	private int doGetInt(ParameterName paramName) throws DataException {
		final String methodName = "doGetInt( ParameterName )"; //$NON-NLS-1$
		final String errorCode = ResourceConstants.CANNOT_GET_INT_FROM_PARAMETER;
		getLogger().entering(sm_className, methodName, paramName);

		String effectiveParamName = paramName.getEffectiveName();
		try {
			int ret = getAdvancedStatement().getInt(effectiveParamName);

			getLogger().exiting(sm_className, methodName, ret);

			return ret;
		} catch (OdaException ex) {
			throwException(ex, errorCode, effectiveParamName, methodName);
		} catch (UnsupportedOperationException ex) {
			throwException(ex, errorCode, effectiveParamName, methodName);
		}
		return 0;
	}

	private double doGetDouble(int paramIndex) throws DataException {
		final String methodName = "doGetDouble( int )"; //$NON-NLS-1$
		final String errorCode = ResourceConstants.CANNOT_GET_DOUBLE_FROM_PARAMETER;
		getLogger().entering(sm_className, methodName, paramIndex);

		try {
			double ret = getAdvancedStatement().getDouble(paramIndex);

			if (getLogger().isLoggingEnterExitLevel())
				getLogger().exiting(sm_className, methodName, new Double(ret));

			return ret;
		} catch (OdaException ex) {
			throwException(ex, errorCode, paramIndex, methodName);
		} catch (UnsupportedOperationException ex) {
			throwException(ex, errorCode, paramIndex, methodName);
		}
		return 0;
	}

	private double doGetDouble(ParameterName paramName) throws DataException {
		final String methodName = "doGetDouble( ParameterName )"; //$NON-NLS-1$
		final String errorCode = ResourceConstants.CANNOT_GET_DOUBLE_FROM_PARAMETER;
		getLogger().entering(sm_className, methodName, paramName);

		String effectiveParamName = paramName.getEffectiveName();
		try {
			double ret = getAdvancedStatement().getDouble(effectiveParamName);

			if (getLogger().isLoggingEnterExitLevel())
				getLogger().exiting(sm_className, methodName, new Double(ret));

			return ret;
		} catch (OdaException ex) {
			throwException(ex, errorCode, effectiveParamName, methodName);
		} catch (UnsupportedOperationException ex) {
			throwException(ex, errorCode, effectiveParamName, methodName);
		}
		return 0;
	}

	private String doGetString(int paramIndex) throws DataException {
		final String methodName = "doGetString( int )"; //$NON-NLS-1$
		final String errorCode = ResourceConstants.CANNOT_GET_STRING_FROM_PARAMETER;
		getLogger().entering(sm_className, methodName, paramIndex);

		try {
			String ret = getAdvancedStatement().getString(paramIndex);

			getLogger().exiting(sm_className, methodName, ret);

			return ret;
		} catch (OdaException ex) {
			throwException(ex, errorCode, paramIndex, methodName);
		} catch (UnsupportedOperationException ex) {
			throwException(ex, errorCode, paramIndex, methodName);
		}
		return null;
	}

	private String doGetString(ParameterName paramName) throws DataException {
		final String methodName = "doGetString( ParameterName )"; //$NON-NLS-1$
		final String errorCode = ResourceConstants.CANNOT_GET_STRING_FROM_PARAMETER;
		getLogger().entering(sm_className, methodName, paramName);

		String effectiveParamName = paramName.getEffectiveName();
		try {
			String ret = getAdvancedStatement().getString(effectiveParamName);

			getLogger().exiting(sm_className, methodName, ret);

			return ret;
		} catch (OdaException ex) {
			throwException(ex, errorCode, effectiveParamName, methodName);
		} catch (UnsupportedOperationException ex) {
			throwException(ex, errorCode, effectiveParamName, methodName);
		}
		return null;
	}

	private BigDecimal doGetBigDecimal(int paramIndex) throws DataException {
		final String methodName = "doGetBigDecimal( int )"; //$NON-NLS-1$
		final String errorCode = ResourceConstants.CANNOT_GET_BIGDECIMAL_FROM_PARAMETER;
		getLogger().entering(sm_className, methodName, paramIndex);

		try {
			BigDecimal ret = getAdvancedStatement().getBigDecimal(paramIndex);

			getLogger().exiting(sm_className, methodName, ret);
			return ret;
		} catch (OdaException ex) {
			throwException(ex, errorCode, paramIndex, methodName);
		} catch (UnsupportedOperationException ex) {
			throwException(ex, errorCode, paramIndex, methodName);
		}
		return null;
	}

	private BigDecimal doGetBigDecimal(ParameterName paramName) throws DataException {
		final String methodName = "doGetBigDecimal( ParameterName )"; //$NON-NLS-1$
		final String errorCode = ResourceConstants.CANNOT_GET_BIGDECIMAL_FROM_PARAMETER;
		getLogger().entering(sm_className, methodName, paramName);

		String effectiveParamName = paramName.getEffectiveName();
		try {
			BigDecimal ret = getAdvancedStatement().getBigDecimal(effectiveParamName);

			getLogger().exiting(sm_className, methodName, ret);
			return ret;
		} catch (OdaException ex) {
			throwException(ex, errorCode, effectiveParamName, methodName);
		} catch (UnsupportedOperationException ex) {
			throwException(ex, errorCode, effectiveParamName, methodName);
		}
		return null;
	}

	private java.util.Date doGetDate(int paramIndex) throws DataException {
		final String methodName = "doGetDate( int )"; //$NON-NLS-1$
		final String errorCode = ResourceConstants.CANNOT_GET_DATE_FROM_PARAMETER;
		getLogger().entering(sm_className, methodName, paramIndex);

		try {
			java.util.Date ret = getAdvancedStatement().getDate(paramIndex);

			getLogger().exiting(sm_className, methodName, ret);
			return ret;
		} catch (OdaException ex) {
			throwException(ex, errorCode, paramIndex, methodName);
		} catch (UnsupportedOperationException ex) {
			throwException(ex, errorCode, paramIndex, methodName);
		}
		return null;
	}

	private java.util.Date doGetDate(ParameterName paramName) throws DataException {
		final String methodName = "doGetDate( ParameterName )"; //$NON-NLS-1$
		final String errorCode = ResourceConstants.CANNOT_GET_DATE_FROM_PARAMETER;
		getLogger().entering(sm_className, methodName, paramName);

		String effectiveParamName = paramName.getEffectiveName();
		try {
			java.util.Date ret = getAdvancedStatement().getDate(effectiveParamName);

			getLogger().exiting(sm_className, methodName, ret);
			return ret;
		} catch (OdaException ex) {
			throwException(ex, errorCode, effectiveParamName, methodName);
		} catch (UnsupportedOperationException ex) {
			throwException(ex, errorCode, effectiveParamName, methodName);
		}
		return null;
	}

	private Time doGetTime(int paramIndex) throws DataException {
		final String methodName = "doGetTime( int )"; //$NON-NLS-1$
		final String errorCode = ResourceConstants.CANNOT_GET_TIME_FROM_PARAMETER;
		getLogger().entering(sm_className, methodName, paramIndex);

		try {
			Time ret = getAdvancedStatement().getTime(paramIndex);

			getLogger().exiting(sm_className, methodName, ret);
			return ret;
		} catch (OdaException ex) {
			throwException(ex, errorCode, paramIndex, methodName);
		} catch (UnsupportedOperationException ex) {
			throwException(ex, errorCode, paramIndex, methodName);
		}
		return null;
	}

	private Time doGetTime(ParameterName paramName) throws DataException {
		final String methodName = "doGetTime( ParameterName )"; //$NON-NLS-1$
		final String errorCode = ResourceConstants.CANNOT_GET_TIME_FROM_PARAMETER;
		getLogger().entering(sm_className, methodName, paramName);

		String effectiveParamName = paramName.getEffectiveName();
		try {
			Time ret = getAdvancedStatement().getTime(effectiveParamName);

			getLogger().exiting(sm_className, methodName, ret);
			return ret;
		} catch (OdaException ex) {
			throwException(ex, errorCode, effectiveParamName, methodName);
		} catch (UnsupportedOperationException ex) {
			throwException(ex, errorCode, effectiveParamName, methodName);
		}
		return null;
	}

	private Timestamp doGetTimestamp(int paramIndex) throws DataException {
		final String methodName = "doGetTimestamp( int )"; //$NON-NLS-1$
		final String errorCode = ResourceConstants.CANNOT_GET_TIMESTAMP_FROM_PARAMETER;
		getLogger().entering(sm_className, methodName, paramIndex);

		try {
			Timestamp ret = getAdvancedStatement().getTimestamp(paramIndex);

			getLogger().exiting(sm_className, methodName, ret);
			return ret;
		} catch (OdaException ex) {
			throwException(ex, errorCode, paramIndex, methodName);
		} catch (UnsupportedOperationException ex) {
			throwException(ex, errorCode, paramIndex, methodName);
		}
		return null;
	}

	private Timestamp doGetTimestamp(ParameterName paramName) throws DataException {
		final String methodName = "doGetTimestamp( ParameterName )"; //$NON-NLS-1$
		final String errorCode = ResourceConstants.CANNOT_GET_TIMESTAMP_FROM_PARAMETER;
		getLogger().entering(sm_className, methodName, paramName);

		String effectiveParamName = paramName.getEffectiveName();
		try {
			Timestamp ret = getAdvancedStatement().getTimestamp(effectiveParamName);

			getLogger().exiting(sm_className, methodName, ret);
			return ret;
		} catch (OdaException ex) {
			throwException(ex, errorCode, effectiveParamName, methodName);
		} catch (UnsupportedOperationException ex) {
			throwException(ex, errorCode, effectiveParamName, methodName);
		}
		return null;
	}

	private IBlob doGetBlob(int paramIndex) throws DataException {
		final String methodName = "doGetBlob( int )"; //$NON-NLS-1$
		final String errorCode = ResourceConstants.CANNOT_GET_BLOB_FROM_PARAMETER;
		getLogger().entering(sm_className, methodName, paramIndex);

		try {
			IBlob ret = getAdvancedStatement().getBlob(paramIndex);

			getLogger().exiting(sm_className, methodName, ret);
			return ret;
		} catch (OdaException ex) {
			throwException(ex, errorCode, paramIndex, methodName);
		} catch (UnsupportedOperationException ex) {
			throwException(ex, errorCode, paramIndex, methodName);
		}
		return null;
	}

	private IBlob doGetBlob(ParameterName paramName) throws DataException {
		final String methodName = "doGetBlob( ParameterName )"; //$NON-NLS-1$
		final String errorCode = ResourceConstants.CANNOT_GET_BLOB_FROM_PARAMETER;
		getLogger().entering(sm_className, methodName, paramName);

		String effectiveParamName = paramName.getEffectiveName();
		try {
			IBlob ret = getAdvancedStatement().getBlob(effectiveParamName);

			getLogger().exiting(sm_className, methodName, ret);
			return ret;
		} catch (OdaException ex) {
			throwException(ex, errorCode, effectiveParamName, methodName);
		} catch (UnsupportedOperationException ex) {
			throwException(ex, errorCode, effectiveParamName, methodName);
		}
		return null;
	}

	private IClob doGetClob(int paramIndex) throws DataException {
		final String methodName = "doGetClob( int )"; //$NON-NLS-1$
		final String errorCode = ResourceConstants.CANNOT_GET_CLOB_FROM_PARAMETER;
		getLogger().entering(sm_className, methodName, paramIndex);

		try {
			IClob ret = getAdvancedStatement().getClob(paramIndex);

			getLogger().exiting(sm_className, methodName, ret);
			return ret;
		} catch (OdaException ex) {
			throwException(ex, errorCode, paramIndex, methodName);
		} catch (UnsupportedOperationException ex) {
			throwException(ex, errorCode, paramIndex, methodName);
		}
		return null;
	}

	private IClob doGetClob(ParameterName paramName) throws DataException {
		final String methodName = "doGetClob( ParameterName )"; //$NON-NLS-1$
		final String errorCode = ResourceConstants.CANNOT_GET_CLOB_FROM_PARAMETER;
		getLogger().entering(sm_className, methodName, paramName);

		String effectiveParamName = paramName.getEffectiveName();
		try {
			IClob ret = getAdvancedStatement().getClob(effectiveParamName);

			getLogger().exiting(sm_className, methodName, ret);
			return ret;
		} catch (OdaException ex) {
			throwException(ex, errorCode, effectiveParamName, methodName);
		} catch (UnsupportedOperationException ex) {
			throwException(ex, errorCode, effectiveParamName, methodName);
		}
		return null;
	}

	private Boolean doGetBoolean(int paramIndex) throws DataException {
		final String methodName = "doGetBoolean( int )"; //$NON-NLS-1$
		final String errorCode = ResourceConstants.CANNOT_GET_BOOLEAN_FROM_PARAMETER;
		getLogger().entering(sm_className, methodName, paramIndex);

		try {
			boolean ret = getAdvancedStatement().getBoolean(paramIndex);

			Boolean retObj = wasNull() ? null : Boolean.valueOf(ret);
			getLogger().exiting(sm_className, methodName, retObj);
			return retObj;
		} catch (OdaException ex) {
			throwException(ex, errorCode, paramIndex, methodName);
		} catch (UnsupportedOperationException ex) {
			throwException(ex, errorCode, paramIndex, methodName);
		}
		return null;
	}

	private Boolean doGetBoolean(ParameterName paramName) throws DataException {
		final String methodName = "doGetBoolean( ParameterName )"; //$NON-NLS-1$
		final String errorCode = ResourceConstants.CANNOT_GET_BOOLEAN_FROM_PARAMETER;
		getLogger().entering(sm_className, methodName, paramName);

		String effectiveParamName = paramName.getEffectiveName();
		try {
			boolean ret = getAdvancedStatement().getBoolean(effectiveParamName);

			Boolean retObj = wasNull() ? null : Boolean.valueOf(ret);
			getLogger().exiting(sm_className, methodName, retObj);
			return retObj;
		} catch (OdaException ex) {
			throwException(ex, errorCode, effectiveParamName, methodName);
		} catch (UnsupportedOperationException ex) {
			throwException(ex, errorCode, effectiveParamName, methodName);
		}
		return null;
	}

	private Object doGetObject(int paramIndex) throws DataException {
		final String methodName = "doGetObject( int )"; //$NON-NLS-1$
		final String errorCode = ResourceConstants.CANNOT_GET_OBJECT_FROM_PARAMETER;
		getLogger().entering(sm_className, methodName, paramIndex);

		try {
			Object ret = getAdvancedStatement().getObject(paramIndex);

			getLogger().exiting(sm_className, methodName, ret);

			return ret;
		} catch (OdaException ex) {
			throwException(ex, errorCode, paramIndex, methodName);
		} catch (UnsupportedOperationException ex) {
			throwException(ex, errorCode, paramIndex, methodName);
		}
		return null;
	}

	private Object doGetObject(ParameterName paramName) throws DataException {
		final String methodName = "doGetObject( ParameterName )"; //$NON-NLS-1$
		final String errorCode = ResourceConstants.CANNOT_GET_OBJECT_FROM_PARAMETER;
		getLogger().entering(sm_className, methodName, paramName);

		String effectiveParamName = paramName.getEffectiveName();
		try {
			Object ret = getAdvancedStatement().getObject(effectiveParamName);

			getLogger().exiting(sm_className, methodName, ret);

			return ret;
		} catch (OdaException ex) {
			throwException(ex, errorCode, effectiveParamName, methodName);
		} catch (UnsupportedOperationException ex) {
			throwException(ex, errorCode, effectiveParamName, methodName);
		}
		return null;
	}

	private boolean wasNull() throws DataException {
		final String methodName = "wasNull"; //$NON-NLS-1$
		final String errorCode = ResourceConstants.CANNOT_DETERMINE_WAS_NULL;

		try {
			return getAdvancedStatement().wasNull();
		} catch (OdaException ex) {
			throwException(ex, errorCode, methodName);
		} catch (UnsupportedOperationException ex) {
			throwException(ex, errorCode, methodName);
		}
		return false;
	}

	private int getOdaTypeFromParamHints(String paramName, int paramIndex) {
		if (m_parameterHints == null)
			return Types.CHAR;

		// first find the parameter hint for the specified parameter
		ListIterator iter = m_parameterHints.listIterator();
		boolean useParamName = (paramName != null);
		while (iter.hasNext()) {
			ParameterHint paramHint = (ParameterHint) iter.next();

			if ((useParamName && paramHint.getName().equals(paramName))
					|| (!useParamName && paramHint.getPosition() == paramIndex)) {
				// found parameter's corresponding design hint
				return paramHint.getEffectiveOdaType(m_connection.getDataSourceId(), m_dataSetType);
			}
		}

		// do not have a design hint for the specified parameter
		return Types.CHAR; // default to a String oda type
	}

	// Returns 0 if the parameter hint doesn't exist for the specified parameter
	// name or if the caller didn't specify a position for the specified parameter
	// name
	private int getIndexFromParamHints(String paramName) {
		if (m_parameterHints == null)
			return 0;

		ListIterator iter = m_parameterHints.listIterator();
		while (iter.hasNext()) {
			ParameterHint paramHint = (ParameterHint) iter.next();

			if (paramHint.getName().equals(paramName))
				return paramHint.getPosition();
		}

		return 0; // no matching parameter hint to give us the position
	}

	/**
	 * Returns the driver-defined name defined in design hints for the specified
	 * data set parameter's model name.
	 * 
	 * @param paramName
	 * @return driver-defined parameter name; may be null
	 */
	private String getNativeNameFromParamHints(String paramName) {
		if (m_parameterHints == null)
			return null;

		ListIterator iter = m_parameterHints.listIterator();
		while (iter.hasNext()) {
			ParameterHint paramHint = (ParameterHint) iter.next();

			if (paramHint.getName().equals(paramName))
				return paramHint.getNativeName();
		}

		return null; // no matching parameter hint to give us the native name
	}

	/**
	 * Clears the current input parameter values immediately.
	 * 
	 * @throws DataException if data source error occurs.
	 */
	public void clearParameterValues() throws DataException {
		String methodName = "clearParameterValues"; //$NON-NLS-1$
		getLogger().entering(sm_className, methodName);

		try {
			getStatement().clearInParameters();
		} catch (OdaException ex) {
			throwException(ex, ResourceConstants.CANNOT_CLEAR_IN_PARAMETERS, methodName);
		} catch (AbstractMethodError err) {
			getLogger().logp(Level.WARNING, sm_className, methodName, "clearInParameters method undefined.", err); //$NON-NLS-1$

			handleUnsupportedClearInParameters();
		} catch (UnsupportedOperationException ex) {
			getLogger().logp(Level.WARNING, sm_className, methodName, "clearInParameters is not supported.", ex); //$NON-NLS-1$

			handleUnsupportedClearInParameters();
		}

		// after clearing the parameter values, the underlying
		// metadata may change, so we need to invalidate
		// our states that are used to maintain the current
		// ResultClass
		resetResultsAndMetaData();

		// optimization to keep the invalidated cached ProjectedColumns,
		// rather than getting new ones to replace them all immediately
		// If needed, after clearParameterValues() is called, we will go get
		// a new set of runtime metadata and incorporate the custom column/
		// colum hints/projections info from the invalidated ProjectedColumn
		m_updateProjectedColumns = true;

		if (m_namedProjectedColumns != null) {
			Set keys = m_namedProjectedColumns.keySet();
			if (m_updateNamedProjectedColumns == null)
				m_updateNamedProjectedColumns = new HashSet(keys);
			else
				m_updateNamedProjectedColumns.addAll(keys);
		}

		getLogger().exiting(sm_className, methodName);
	}

	// Provides a work-around for older ODA drivers or ODA drivers that
	// don't support the clearInParameters call.
	// The workaround involves creating a new instance of the underlying
	// ODA statement and setting it back up to the state of the current
	// statement.
	private void handleUnsupportedClearInParameters() throws DataException {
		m_statement = m_connection.prepareOdaQuery(m_queryText, m_dataSetType, getQuerySpecification());

		// getting the new statement back into the previous statement's
		// state
		if (m_properties != null) {
			ListIterator<Property> iter = m_properties.listIterator();
			while (iter.hasNext()) {
				Property property = iter.next();
				doSetProperty(property.getName(), property.getValue());
			}
		}

		doSetMaxRows(m_maxRows);

		if (m_sortSpecs != null) {
			ListIterator<SortSpec> iter = m_sortSpecs.listIterator();
			while (iter.hasNext()) {
				SortSpec sortBy = iter.next();
				doSetSortSpec(sortBy);
			}
		}
	}

	private void updateProjectedColumns(ProjectedColumns newProjectedColumns, ProjectedColumns oldProjectedColumns)
			throws DataException {
		ArrayList customColumns = oldProjectedColumns.getCustomColumns();
		ArrayList columnHints = oldProjectedColumns.getColumnHints();
		String[] projections = oldProjectedColumns.getProjections();

		if (customColumns != null) {
			ListIterator iter = customColumns.listIterator();
			while (iter.hasNext()) {
				CustomColumn customColumn = (CustomColumn) iter.next();
				newProjectedColumns.addCustomColumn(customColumn.getName(), customColumn.getType());
			}
		}

		if (columnHints != null) {
			ListIterator iter = columnHints.listIterator();
			while (iter.hasNext()) {
				ColumnHint columnHint = (ColumnHint) iter.next();
				newProjectedColumns.addHint(columnHint);
			}
		}

		newProjectedColumns.setProjectedNames(projections);
	}

	/**
	 * Returns the 1-based index of the specified input parameter.
	 * 
	 * @param paramName the name of the parameter.
	 * @return the 1-based index of the input parameter.
	 * @throws DataException if data source error occurs.
	 */
	public int findInParameter(String paramName) throws DataException {
		final String methodName = "findInParameter(String)"; //$NON-NLS-1$
		getLogger().entering(sm_className, methodName, paramName);

		try {
			int ret = getStatement().findInParameter(paramName);

			getLogger().exiting(sm_className, methodName, ret);
			return ret;
		} catch (OdaException ex) {
			throwException(ex, ResourceConstants.CANNOT_FIND_IN_PARAMETER, paramName, methodName);
		} catch (UnsupportedOperationException ex) {
			// this is common, and may be ignored by caller
			getLogger().logp(Level.INFO, sm_className, methodName, "Cannot find input parameter by name.", ex); //$NON-NLS-1$
			throw newException(ResourceConstants.CANNOT_FIND_IN_PARAMETER, paramName, ex);
		}
		return 0;
	}

	/**
	 * Sets the value of the specified input parameter.
	 * 
	 * @param paramIndex the 1-based index of the parameter.
	 * @param paramValue the input parameter value.
	 * @throws DataException if data source error occurs.
	 */
	public void setParameterValue(int paramIndex, Object paramValue) throws DataException {
		setParameterValue(null /* n/a paramName */, paramIndex, paramValue);
	}

	/**
	 * Sets the value of the specified input parameter.
	 * 
	 * @param paramName  the name of the parameter.
	 * @param paramValue the input parameter value.
	 * @throws DataException if data source error occurs.
	 */
	public void setParameterValue(String paramName, Object paramValue) throws DataException {
		setParameterValue(paramName, 0 /* n/a paramIndex */, paramValue);
	}

	private void setParameterValue(String paramName, int paramIndex, Object paramValue) throws DataException {
		final String methodName = "setParameterValue( String, int, Object )"; //$NON-NLS-1$

		// delegate to ParameterName for the proper name to use when
		// interacting with underlying oda runtime driver
		ParameterName paramNameObj = null;
		if (paramName != null) // setting parameter by name
		{
			paramNameObj = new ParameterName(paramName, this);

			// log if not able to find corresponding native name
			paramNameObj.logNullNativeName();
		}

		try {

			// the following calls the setters may fail due to a type
			// mismatch with the parameter value's type. so we'll
			// catch all RuntimeException's and OdaException's because
			// those are the exceptions that the ODA driver may throw in
			// such cases. If we catch one, then we'll try to use alternative
			// mappings based on the runtime parameter metadata or the
			// parameter hints to call another setter method

			if (paramValue == null) {
				setNull(paramNameObj, paramIndex);
				return;
			}

			if (paramValue instanceof Integer) {
				int i = ((Integer) paramValue).intValue();
				setInt(paramNameObj, paramIndex, i);
				return;
			}

			if (paramValue instanceof Double) {
				double d = ((Double) paramValue).doubleValue();
				setDouble(paramNameObj, paramIndex, d);
				return;
			}

			if (paramValue instanceof String) {
				String string = (String) paramValue;
				setString(paramNameObj, paramIndex, string);
				return;
			}

			if (paramValue instanceof BigDecimal) {
				BigDecimal decimal = (BigDecimal) paramValue;
				setBigDecimal(paramNameObj, paramIndex, decimal);
				return;
			}

			// check for subclasses before its java.util.Date base class type
			if (paramValue instanceof Time) {
				Time time = (Time) paramValue;
				setTime(paramNameObj, paramIndex, time);
				return;
			}

			if (paramValue instanceof Timestamp) {
				Timestamp timestamp = (Timestamp) paramValue;
				setTimestamp(paramNameObj, paramIndex, timestamp);
				return;
			}

			if (paramValue instanceof java.sql.Date) {
				Date sqlDate = (Date) paramValue;
				setDate(paramNameObj, paramIndex, sqlDate);
				return;
			}

			if (paramValue instanceof Boolean) {
				boolean val = ((Boolean) paramValue).booleanValue();
				setBoolean(paramNameObj, paramIndex, val);
				return;
			}

			// for all other types of value, try to set by Object type
			// regardless of the data type defined in its hint
			{
				setObject(paramNameObj, paramIndex, paramValue);
				return;
			}
		} catch (RuntimeException ex) {
			retrySetParameterValue(paramNameObj, paramIndex, paramValue, ex);
			return;
		} catch (DataException ex) {
			retrySetParameterValue(paramNameObj, paramIndex, paramValue, ex);
			return;
		}

		/*
		 * throwError( ResourceConstants.UNSUPPORTED_PARAMETER_VALUE_TYPE, new Object[]
		 * { paramValue.getClass() }, methodName );
		 */ }

	// Retry setting the parameter value by using an alternate setter method
	// using the runtime parameter metadata. Or if the runtime parameter metadata
	// is not available, then use the input parameter hints, if available.
	// It will default to calling setString() if we can't get the info from
	// the runtime parameter metadata or the parameter hints.
	private void retrySetParameterValue(ParameterName paramName, int paramIndex, Object paramValue,
			Exception lastException) throws DataException {
		int parameterType = Types.NULL;

		try {
			// try to get the effective parameter type
			parameterType = (paramName == null) ? getParameterType(paramIndex) : getParameterType(paramName, false);
		} catch (Exception ex) {
			// data source can't get the type, try to get it from the hints
		}

		// if not able to get the effective parameter metadata for any reason,
		// try to get the type directly from the parameter design hints
		if (parameterType == Types.NULL) {
			String paramModelName = (paramName != null) ? paramName.getRomName() : null;
			parameterType = getOdaTypeFromParamHints(paramModelName, paramIndex);
		}

		// the following conditions of runtime parameter metadata or hint
		// would have led us to call the same set<type> method again;
		// thus the last exception that got thrown by underlying ODA driver
		// could be info regarding problems with the data, so throw that
		if ((parameterType == Types.INTEGER && paramValue instanceof Integer)
				|| (parameterType == Types.DOUBLE && paramValue instanceof Double)
				|| (parameterType == Types.CHAR && paramValue instanceof String)
				|| (parameterType == Types.DECIMAL && paramValue instanceof BigDecimal)
				|| (parameterType == Types.TIME && paramValue instanceof Time)
				|| (parameterType == Types.TIMESTAMP && paramValue instanceof Timestamp)
				|| (parameterType == Types.DATE && paramValue instanceof Date)
				|| (parameterType == Types.BOOLEAN && paramValue instanceof Boolean)) {
			throwSetParamValueLastException(lastException, "retrySetParameterValue"); //$NON-NLS-1$
		}

		if (paramValue == null) {
			retrySetNullParamValue(paramName, paramIndex, parameterType, lastException);
			return;
		}

		// Explicitly defined object type takes precedence over the actual type of value
		// in retry
		if (parameterType == Types.JAVA_OBJECT) {
			// no type conversion is needed
			setObject(paramName, paramIndex, paramValue);
			return;
		}

		if (paramValue instanceof Integer) {
			retrySetIntegerParamValue(paramName, paramIndex, (Integer) paramValue, parameterType);
			return;
		}

		if (paramValue instanceof Double) {
			retrySetDoubleParamValue(paramName, paramIndex, (Double) paramValue, parameterType);
			return;
		}

		if (paramValue instanceof String) {
			retrySetStringParamValue(paramName, paramIndex, (String) paramValue, parameterType);
			return;
		}

		if (paramValue instanceof BigDecimal) {
			retrySetBigDecimalParamValue(paramName, paramIndex, (BigDecimal) paramValue, parameterType);
			return;
		}

		// check for subclasses before its java.util.Date base class type
		if (paramValue instanceof Time) {
			retrySetTimeParamValue(paramName, paramIndex, (Time) paramValue, parameterType);
			return;
		}

		if (paramValue instanceof Timestamp) {
			retrySetTimestampParamValue(paramName, paramIndex, (Timestamp) paramValue, parameterType);
			return;
		}

		if (paramValue instanceof Date) {
			retrySetDateParamValue(paramName, paramIndex, (Date) paramValue, parameterType);
			return;
		}

		if (paramValue instanceof Boolean) {
			retrySetBooleanParamValue(paramName, paramIndex, (Boolean) paramValue, parameterType);
			return;
		}

		assert false; // unsupported parameter value type was checked earlier
	}

	private void retrySetIntegerParamValue(ParameterName paramName, int paramIndex, Integer paramValue,
			int parameterType) throws DataException {
		switch (parameterType) {
		case Types.DOUBLE: {
			double d = paramValue.doubleValue();
			setDouble(paramName, paramIndex, d);
			return;
		}

		case Types.CHAR: {
			String s = paramValue.toString();
			setString(paramName, paramIndex, s);
			return;
		}

		case Types.DECIMAL: {
			int i = paramValue.intValue();
			BigDecimal bd = new BigDecimal(i);
			setBigDecimal(paramName, paramIndex, bd);
			return;
		}

		case Types.BOOLEAN: {
			boolean val = (paramValue.intValue() != 0);
			setBoolean(paramName, paramIndex, val);
			return;
		}

		default:
			throwConversionError(paramName, paramIndex, paramValue, parameterType, null /* cause */ );
			return;
		}
	}

	private void retrySetDoubleParamValue(ParameterName paramName, int paramIndex, Double paramValue, int parameterType)
			throws DataException {
		switch (parameterType) {
		case Types.INTEGER: {
			int i = paramValue.intValue();
			Double intValue = new Double(i);
			// this could be due to loss of precision or the double is
			// outside the range of an integer
			if (!paramValue.equals(intValue))
				throwConversionError(paramName, paramIndex, paramValue, parameterType, null /* cause */ );

			setInt(paramName, paramIndex, i);
			return;
		}

		case Types.CHAR: {
			String s = paramValue.toString();
			setString(paramName, paramIndex, s);
			return;
		}

		case Types.DECIMAL: {
			double d = paramValue.doubleValue();
			BigDecimal bd = new BigDecimal(d);
			setBigDecimal(paramName, paramIndex, bd);
			return;
		}

		case Types.BOOLEAN: {
			boolean val = (paramValue.doubleValue() != 0);
			setBoolean(paramName, paramIndex, val);
			return;
		}

		default:
			throwConversionError(paramName, paramIndex, paramValue, parameterType, null /* cause */ );
			return;
		}
	}

	private void retrySetStringParamValue(ParameterName paramName, int paramIndex, String paramValue, int parameterType)
			throws DataException {
		switch (parameterType) {
		case Types.INTEGER: {
			try {
				int i = Integer.parseInt(paramValue);
				setInt(paramName, paramIndex, i);
				return;
			} catch (NumberFormatException ex) {
				throwConversionError(paramName, paramIndex, paramValue, parameterType, ex);
				return;
			}
		}

		case Types.DOUBLE: {
			try {
				double d = Double.parseDouble(paramValue);
				setDouble(paramName, paramIndex, d);
				return;
			} catch (NumberFormatException ex) {
				throwConversionError(paramName, paramIndex, paramValue, parameterType, ex);
				return;
			}
		}

		case Types.DECIMAL: {
			try {
				BigDecimal bd = new BigDecimal(paramValue);
				setBigDecimal(paramName, paramIndex, bd);
				return;
			} catch (NumberFormatException ex) {
				throwConversionError(paramName, paramIndex, paramValue, parameterType, ex);
				return;
			}
		}

		case Types.DATE: {
			try {
				Date d = Date.valueOf(paramValue);
				setDate(paramName, paramIndex, d);
				return;
			} catch (IllegalArgumentException ex) {
				throwConversionError(paramName, paramIndex, paramValue, parameterType, ex);
				return;
			}
		}

		case Types.TIME: {
			try {
				Time t = Time.valueOf(paramValue);
				setTime(paramName, paramIndex, t);
				return;
			} catch (IllegalArgumentException ex) {
				throwConversionError(paramName, paramIndex, paramValue, parameterType, ex);
				return;
			}
		}

		case Types.TIMESTAMP: {
			try {
				Timestamp ts = Timestamp.valueOf(paramValue);
				setTimestamp(paramName, paramIndex, ts);
				return;
			} catch (IllegalArgumentException ex) {
				throwConversionError(paramName, paramIndex, paramValue, parameterType, ex);
				return;
			}
		}

		case Types.BOOLEAN: {
			boolean val = Boolean.valueOf(paramValue).booleanValue();
			setBoolean(paramName, paramIndex, val);
			return;
		}

		default:
			throwConversionError(paramName, paramIndex, paramValue, parameterType, null /* cause */ );
			return;
		}
	}

	private void retrySetBigDecimalParamValue(ParameterName paramName, int paramIndex, BigDecimal paramValue,
			int parameterType) throws DataException {
		switch (parameterType) {
		case Types.INTEGER: {
			int i = paramValue.intValue();
			BigDecimal intValue = new BigDecimal(i);
			// this could occur if there is a loss in precision or
			// if the BigDecimal value is outside the range of an integer
			if (!paramValue.equals(intValue))
				throwConversionError(paramName, paramIndex, paramValue, parameterType, null /* cause */ );

			setInt(paramName, paramIndex, i);
			return;
		}

		case Types.DOUBLE: {
			double d = paramValue.doubleValue();
			BigDecimal doubleValue = new BigDecimal(d);
			// this could occur if there is a loss in precision or
			// if the BigDecimal value is outside the range of a double
			if (!paramValue.equals(doubleValue))
				throwConversionError(paramName, paramIndex, paramValue, parameterType, null /* cause */ );

			setDouble(paramName, paramIndex, d);
			return;
		}

		case Types.CHAR: {
			String s = paramValue.toString();
			setString(paramName, paramIndex, s);
			return;
		}

		case Types.BOOLEAN: {
			boolean val = (paramValue.compareTo(BigDecimal.valueOf(0)) != 0);
			setBoolean(paramName, paramIndex, val);
			return;
		}

		default:
			throwConversionError(paramName, paramIndex, paramValue, parameterType, null /* cause */ );
			return;
		}
	}

	private void retrySetDateParamValue(ParameterName paramName, int paramIndex, Date paramValue, int parameterType)
			throws DataException {
		switch (parameterType) {
		case Types.CHAR: {
			// need to convert the java.util.Date to a java.sql.Date,
			// so that we can get the ISO format date string
			Date sqlDate = new Date(paramValue.getTime());
			String s = sqlDate.toString();
			setString(paramName, paramIndex, s);
			return;
		}

		case Types.TIME: {
			// ignores the date portion
			Time timeValue = new Time(paramValue.getTime());
			setTime(paramName, paramIndex, timeValue);
			return;
		}

		case Types.TIMESTAMP: {
			Timestamp ts = new Timestamp(paramValue.getTime());
			setTimestamp(paramName, paramIndex, ts);
			return;
		}

		default:
			throwConversionError(paramName, paramIndex, paramValue, parameterType, null /* cause */ );
			return;
		}
	}

	private void retrySetTimeParamValue(ParameterName paramName, int paramIndex, Time paramValue, int parameterType)
			throws DataException {
		switch (parameterType) {
		case Types.CHAR: {
			String s = paramValue.toString();
			setString(paramName, paramIndex, s);
			return;
		}

		case Types.DATE: {
			Date d = new Date(paramValue.getTime());
			setDate(paramName, paramIndex, d);
			return;
		}

		case Types.TIMESTAMP: {
			Timestamp ts = new Timestamp(paramValue.getTime());
			setTimestamp(paramName, paramIndex, ts);
			return;
		}

		default:
			throwConversionError(paramName, paramIndex, paramValue, parameterType, null /* cause */ );
			return;
		}
	}

	private void retrySetTimestampParamValue(ParameterName paramName, int paramIndex, Timestamp paramValue,
			int parameterType) throws DataException {
		switch (parameterType) {
		case Types.CHAR: {
			String s = paramValue.toString();
			setString(paramName, paramIndex, s);
			return;
		}

		case Types.DATE: {
			long time = paramValue.getTime();
			Date d = new Date(time);
			setDate(paramName, paramIndex, d);
			return;
		}

		case Types.TIME: {
			// ignores the date portion
			Time timeValue = new Time(paramValue.getTime());
			setTime(paramName, paramIndex, timeValue);
			return;
		}

		default:
			throwConversionError(paramName, paramIndex, paramValue, parameterType, null /* cause */ );
			return;
		}
	}

	private void retrySetBooleanParamValue(ParameterName paramName, int paramIndex, Boolean paramValue,
			int parameterType) throws DataException {
		switch (parameterType) {
		case Types.INTEGER: {
			int i = paramValue.booleanValue() ? 1 : 0;
			setInt(paramName, paramIndex, i);
			return;
		}

		case Types.DOUBLE: {
			double d = paramValue.booleanValue() ? 1 : 0;
			setDouble(paramName, paramIndex, d);
			return;
		}

		case Types.CHAR: {
			String s = paramValue.toString();
			setString(paramName, paramIndex, s);
			return;
		}

		case Types.DECIMAL: {
			int i = paramValue.booleanValue() ? 1 : 0;
			BigDecimal bd = new BigDecimal(i);
			setBigDecimal(paramName, paramIndex, bd);
			return;
		}

		default:
			throwConversionError(paramName, paramIndex, paramValue, parameterType, null /* cause */ );
			return;
		}
	}

	private void retrySetNullParamValue(ParameterName paramName, int paramIndex, int parameterType,
			Exception lastException) throws DataException {
		switch (parameterType) {
		case Types.CHAR: {
			setString(paramName, paramIndex, null);
			return;
		}

		case Types.DECIMAL: {
			setBigDecimal(paramName, paramIndex, null);
			return;
		}
		case Types.DATE: {
			setDate(paramName, paramIndex, null);
			return;
		}

		case Types.TIME: {
			setTime(paramName, paramIndex, null);
			return;
		}

		case Types.TIMESTAMP: {
			setTimestamp(paramName, paramIndex, null);
			return;
		}

		case Types.JAVA_OBJECT: {
			setObject(paramName, paramIndex, null);
			return;
		}

		default:
			// metadata indicates primitive data types or types not supported for input
			// parameter,
			// cannot retry with a different ODA API setter to assign
			// a null input parameter value

			getLogger().logp(Level.SEVERE, sm_className, "retrySetNullParamValue", //$NON-NLS-1$
					"Input parameter value is null; not able to retry, throws exception from underlying ODA driver."); //$NON-NLS-1$

			// not able to retry, throw last exception thrown by
			// the underlying ODA driver
			throwSetParamValueLastException(lastException, "retrySetNullParamValue"); //$NON-NLS-1$
		}
	}

	private void throwConversionError(ParameterName paramName, int paramIndex, Object paramValue, int odaType,
			Exception cause) throws DataException {
		final String methodName = "conversionError"; //$NON-NLS-1$

		Object paramValueArg = (paramValue == null) ? (Object) "" : paramValue; //$NON-NLS-1$
		Object paramClassArg = (paramValue == null) ? (Object) "null" : paramValue.getClass(); //$NON-NLS-1$

		Object[] errMsgArgs = null;
		if (paramName == null)
			errMsgArgs = new Object[] { paramValueArg, Integer.valueOf(paramIndex), paramClassArg,
					Integer.valueOf(odaType) };
		else
			errMsgArgs = new Object[] { paramValueArg, paramName, paramClassArg, Integer.valueOf(odaType) };
		throwException(cause, ResourceConstants.CANNOT_CONVERT_INDEXED_PARAMETER_VALUE, errMsgArgs, methodName);
	}

	/**
	 * Throws the specified exception last thrown by an underlying ODA driver during
	 * a call to set input parameter value.
	 */
	private void throwSetParamValueLastException(Exception lastException, final String methodName)
			throws RuntimeException, DataException, IllegalStateException {
		assert (lastException != null);

		String logContextMsg = "Cannot set input parameter."; //$NON-NLS-1$
		if (lastException instanceof RuntimeException) {
			getLogger().logp(Level.SEVERE, sm_className, methodName, logContextMsg, lastException);

			throw (RuntimeException) lastException;
		} else if (lastException instanceof DataException) {
			getLogger().logp(Level.SEVERE, sm_className, methodName, logContextMsg, lastException);

			throw (DataException) lastException;
		} else {
			String localizedMessage = DataResourceHandle.getInstance()
					.getMessage(ResourceConstants.UNKNOWN_EXCEPTION_THROWN);
			IllegalStateException ex = new IllegalStateException(localizedMessage);
			ex.initCause(lastException);

			getLogger().logp(Level.SEVERE, sm_className, methodName, logContextMsg, lastException);

			throw ex;
		}
	}

	private void setInt(ParameterName paramName, int paramIndex, int i) throws DataException {
		if (paramName == null)
			doSetInt(paramIndex, i);
		else
			setInt(paramName, i);
	}

	private void setInt(ParameterName paramName, int i) throws DataException {
		final String methodName = "setInt( ParameterName, int )"; //$NON-NLS-1$

		if (supportsNamedParameter()) {
			doSetInt(paramName, i);
			return;
		}

		if (!setIntUsingHints(paramName, i)) {
			final String errorCode = ResourceConstants.CANNOT_SET_INT_PARAMETER;
			Object[] msgArgs = new Object[] { Integer.valueOf(i), paramName };
			throwError(errorCode, msgArgs, methodName);
		}
	}

	private boolean setIntUsingHints(ParameterName paramName, int i) throws DataException {
		int paramIndex = getIndexFromParamHints(paramName.getRomName());
		if (paramIndex <= 0)
			return false;

		doSetInt(paramIndex, i);
		return true;
	}

	private void setDouble(ParameterName paramName, int paramIndex, double d) throws DataException {
		if (paramName == null)
			doSetDouble(paramIndex, d);
		else
			setDouble(paramName, d);
	}

	private void setDouble(ParameterName paramName, double d) throws DataException {
		final String methodName = "setDouble( ParameterName, double )"; //$NON-NLS-1$

		if (supportsNamedParameter()) {
			doSetDouble(paramName, d);
			return;
		}

		if (!setDoubleUsingHints(paramName, d)) {
			final String errorCode = ResourceConstants.CANNOT_SET_DOUBLE_PARAMETER;
			Object[] msgArgs = new Object[] { new Double(d), paramName };
			throwError(errorCode, msgArgs, methodName);
		}
	}

	private boolean setDoubleUsingHints(ParameterName paramName, double d) throws DataException {
		int paramIndex = getIndexFromParamHints(paramName.getRomName());
		if (paramIndex <= 0)
			return false;

		doSetDouble(paramIndex, d);
		return true;
	}

	private void setString(ParameterName paramName, int paramIndex, String stringValue) throws DataException {
		if (paramName == null)
			doSetString(paramIndex, stringValue);
		else
			setString(paramName, stringValue);
	}

	private void setString(ParameterName paramName, String stringValue) throws DataException {
		final String methodName = "setString( ParameterName, String )"; //$NON-NLS-1$

		if (supportsNamedParameter()) {
			doSetString(paramName, stringValue);
			return;
		}

		if (!setStringUsingHints(paramName, stringValue)) {
			final String errorCode = ResourceConstants.CANNOT_SET_STRING_PARAMETER;
			Object[] msgArgs = new Object[] { stringValue, paramName };
			throwError(errorCode, msgArgs, methodName);
		}
	}

	private boolean setStringUsingHints(ParameterName paramName, String stringValue) throws DataException {
		int paramIndex = getIndexFromParamHints(paramName.getRomName());
		if (paramIndex <= 0)
			return false;

		doSetString(paramIndex, stringValue);
		return true;
	}

	private void setBigDecimal(ParameterName paramName, int paramIndex, BigDecimal decimal) throws DataException {
		if (paramName == null)
			doSetBigDecimal(paramIndex, decimal);
		else
			setBigDecimal(paramName, decimal);
	}

	private void setBigDecimal(ParameterName paramName, BigDecimal decimal) throws DataException {
		final String methodName = "setBigDecimal( ParameterName, BigDecimal )"; //$NON-NLS-1$

		if (supportsNamedParameter()) {
			doSetBigDecimal(paramName, decimal);
			return;
		}

		if (!setBigDecimalUsingHints(paramName, decimal)) {
			final String errorCode = ResourceConstants.CANNOT_SET_BIGDECIMAL_PARAMETER;
			Object[] msgArgs = new Object[] { decimal, paramName };
			throwError(errorCode, msgArgs, methodName);
		}
	}

	private boolean setBigDecimalUsingHints(ParameterName paramName, BigDecimal decimal) throws DataException {
		int paramIndex = getIndexFromParamHints(paramName.getRomName());
		if (paramIndex <= 0)
			return false;

		doSetBigDecimal(paramIndex, decimal);
		return true;
	}

	private void setDate(ParameterName paramName, int paramIndex, Date date) throws DataException {
		if (paramName == null)
			doSetDate(paramIndex, date);
		else
			setDate(paramName, date);
	}

	private void setDate(ParameterName paramName, Date date) throws DataException {
		final String methodName = "setDate( ParameterName, Date )"; //$NON-NLS-1$

		if (supportsNamedParameter()) {
			doSetDate(paramName, date);
			return;
		}

		if (!setDateUsingHints(paramName, date)) {
			final String errorCode = ResourceConstants.CANNOT_SET_DATE_PARAMETER;
			Object[] msgArgs = new Object[] { date, paramName };
			throwError(errorCode, msgArgs, methodName);
		}
	}

	private boolean setDateUsingHints(ParameterName paramName, Date date) throws DataException {
		int paramIndex = getIndexFromParamHints(paramName.getRomName());
		if (paramIndex <= 0)
			return false;

		doSetDate(paramIndex, date);
		return true;
	}

	private void setTime(ParameterName paramName, int paramIndex, Time time) throws DataException {
		if (paramName == null)
			doSetTime(paramIndex, time);
		else
			setTime(paramName, time);
	}

	private void setTime(ParameterName paramName, Time time) throws DataException {
		final String methodName = "setTime( ParameterName, Time )"; //$NON-NLS-1$

		if (supportsNamedParameter()) {
			doSetTime(paramName, time);
			return;
		}

		if (!setTimeUsingHints(paramName, time)) {
			final String errorCode = ResourceConstants.CANNOT_SET_TIME_PARAMETER;
			Object[] msgArgs = new Object[] { time, paramName };
			throwError(errorCode, msgArgs, methodName);
		}
	}

	private boolean setTimeUsingHints(ParameterName paramName, Time time) throws DataException {
		int paramIndex = getIndexFromParamHints(paramName.getRomName());
		if (paramIndex <= 0)
			return false;

		doSetTime(paramIndex, time);
		return true;
	}

	private void setTimestamp(ParameterName paramName, int paramIndex, Timestamp timestamp) throws DataException {
		if (paramName == null)
			doSetTimestamp(paramIndex, timestamp);
		else
			setTimestamp(paramName, timestamp);
	}

	private void setTimestamp(ParameterName paramName, Timestamp timestamp) throws DataException {
		final String methodName = "setTimestamp( ParameterName, Timestamp )"; //$NON-NLS-1$

		if (supportsNamedParameter()) {
			doSetTimestamp(paramName, timestamp);
			return;
		}

		if (!setTimestampUsingHints(paramName, timestamp)) {
			final String errorCode = ResourceConstants.CANNOT_SET_TIMESTAMP_PARAMETER;
			Object[] msgArgs = new Object[] { timestamp, paramName };
			throwError(errorCode, msgArgs, methodName);
		}
	}

	private boolean setTimestampUsingHints(ParameterName paramName, Timestamp timestamp) throws DataException {
		int paramIndex = getIndexFromParamHints(paramName.getRomName());
		if (paramIndex <= 0)
			return false;

		doSetTimestamp(paramIndex, timestamp);
		return true;
	}

	private void setBoolean(ParameterName paramName, int paramIndex, boolean val) throws DataException {
		if (paramName == null)
			doSetBoolean(paramIndex, val);
		else
			setBoolean(paramName, val);
	}

	private void setBoolean(ParameterName paramName, boolean val) throws DataException {
		final String methodName = "setBoolean( ParameterName, boolean )"; //$NON-NLS-1$

		if (supportsNamedParameter()) {
			doSetBoolean(paramName, val);
			return;
		}

		if (!setBooleanUsingHints(paramName, val)) {
			final String errorCode = ResourceConstants.CANNOT_SET_BOOLEAN_PARAMETER;
			Object[] msgArgs = new Object[] { Boolean.valueOf(val), paramName };
			throwError(errorCode, msgArgs, methodName);
		}
	}

	private boolean setBooleanUsingHints(ParameterName paramName, boolean val) throws DataException {
		int paramIndex = getIndexFromParamHints(paramName.getRomName());
		if (paramIndex <= 0)
			return false;

		doSetBoolean(paramIndex, val);
		return true;
	}

	private void setObject(ParameterName paramName, int paramIndex, Object value) throws DataException {
		if (paramName == null)
			doSetObject(paramIndex, value);
		else
			setObject(paramName, value);
	}

	private void setObject(ParameterName paramName, Object value) throws DataException {
		final String methodName = "setObject( ParameterName, Object )"; //$NON-NLS-1$

		if (supportsNamedParameter()) {
			doSetObject(paramName, value);
			return;
		}

		if (!setObjectUsingHints(paramName, value)) {
			final String errorCode = ResourceConstants.CANNOT_SET_OBJECT_PARAMETER;
			Object[] msgArgs = new Object[] { value, paramName };
			throwError(errorCode, msgArgs, methodName);
		}
	}

	private boolean setObjectUsingHints(ParameterName paramName, Object value) throws DataException {
		int paramIndex = getIndexFromParamHints(paramName.getRomName());
		if (paramIndex <= 0)
			return false;

		doSetObject(paramIndex, value);
		return true;
	}

	private void setNull(ParameterName paramName, int paramIndex) throws DataException {
		if (paramName == null)
			doSetNull(paramIndex);
		else
			setNull(paramName);
	}

	private void setNull(ParameterName paramName) throws DataException {
		final String methodName = "setNull( ParameterName )"; //$NON-NLS-1$

		if (supportsNamedParameter()) {
			doSetNull(paramName);
			return;
		}

		if (!setNullUsingHints(paramName)) {
			final String errorCode = ResourceConstants.CANNOT_SET_NULL_PARAMETER;
			Object[] msgArgs = new Object[] { paramName };
			throwError(errorCode, msgArgs, methodName);
		}
	}

	private boolean setNullUsingHints(ParameterName paramName) throws DataException {
		int paramIndex = getIndexFromParamHints(paramName.getRomName());
		if (paramIndex <= 0)
			return false;

		doSetNull(paramIndex);
		return true;
	}

	private void doSetInt(int paramIndex, int i) throws DataException {
		final String methodName = "doSetInt( int, int )"; //$NON-NLS-1$
		final String errorCode = ResourceConstants.CANNOT_SET_INT_PARAMETER;

		try {
			getStatement().setInt(paramIndex, i);
		} catch (OdaException ex) {
			Object[] msgArgs = new Object[] { Integer.valueOf(i), Integer.valueOf(paramIndex) };
			throwException(ex, errorCode, msgArgs, methodName);
		} catch (UnsupportedOperationException ex) {
			Object[] msgArgs = new Object[] { Integer.valueOf(i), Integer.valueOf(paramIndex) };
			throwException(ex, errorCode, msgArgs, methodName);
		}
	}

	private void doSetInt(ParameterName paramName, int i) throws DataException {
		final String methodName = "doSetInt( ParameterName, int )"; //$NON-NLS-1$
		final String errorCode = ResourceConstants.CANNOT_SET_INT_PARAMETER;

		String effectiveParamName = paramName.getEffectiveName();
		try {
			getStatement().setInt(effectiveParamName, i);
		} catch (OdaException ex) {
			Object[] msgArgs = new Object[] { Integer.valueOf(i), effectiveParamName };
			throwException(ex, errorCode, msgArgs, methodName);
		} catch (UnsupportedOperationException ex) {
			// first try to set value by position if the parameter hints provide
			// name-to-position mapping,
			// otherwise we need to wrap the UnsupportedOperationException up and throw it
			if (!setIntUsingHints(paramName, i)) {
				Object[] msgArgs = new Object[] { Integer.valueOf(i), effectiveParamName };
				throwException(ex, errorCode, msgArgs, methodName);
			}
		}
	}

	private void doSetDouble(int paramIndex, double d) throws DataException {
		final String methodName = "doSetDouble( int, double )"; //$NON-NLS-1$
		final String errorCode = ResourceConstants.CANNOT_SET_DOUBLE_PARAMETER;

		try {
			getStatement().setDouble(paramIndex, d);
		} catch (OdaException ex) {
			Object[] msgArgs = new Object[] { new Double(d), Integer.valueOf(paramIndex) };
			throwException(ex, errorCode, msgArgs, methodName);
		} catch (UnsupportedOperationException ex) {
			Object[] msgArgs = new Object[] { new Double(d), Integer.valueOf(paramIndex) };
			throwException(ex, errorCode, msgArgs, methodName);
		}
	}

	private void doSetDouble(ParameterName paramName, double d) throws DataException {
		final String methodName = "doSetDouble( ParameterName, double )"; //$NON-NLS-1$
		final String errorCode = ResourceConstants.CANNOT_SET_DOUBLE_PARAMETER;

		String effectiveParamName = paramName.getEffectiveName();
		try {
			getStatement().setDouble(effectiveParamName, d);
		} catch (OdaException ex) {
			Object[] msgArgs = new Object[] { new Double(d), effectiveParamName };
			throwException(ex, errorCode, msgArgs, methodName);
		} catch (UnsupportedOperationException ex) {
			// first try to set value by position if the parameter hints provide
			// name-to-position mapping,
			// otherwise we need to wrap the UnsupportedOperationException up and throw it
			if (!setDoubleUsingHints(paramName, d)) {
				Object[] msgArgs = new Object[] { new Double(d), effectiveParamName };
				throwException(ex, errorCode, msgArgs, methodName);
			}
		}
	}

	private void doSetString(int paramIndex, String stringValue) throws DataException {
		final String methodName = "doSetString( int, String )"; //$NON-NLS-1$
		final String errorCode = ResourceConstants.CANNOT_SET_STRING_PARAMETER;

		try {
			getStatement().setString(paramIndex, stringValue);
		} catch (OdaException ex) {
			Object[] msgArgs = new Object[] { stringValue, Integer.valueOf(paramIndex) };
			throwException(ex, errorCode, msgArgs, methodName);
		} catch (UnsupportedOperationException ex) {
			Object[] msgArgs = new Object[] { stringValue, Integer.valueOf(paramIndex) };
			throwException(ex, errorCode, msgArgs, methodName);
		}
	}

	private void doSetString(ParameterName paramName, String stringValue) throws DataException {
		final String methodName = "doSetString( ParameterName, String )"; //$NON-NLS-1$
		final String errorCode = ResourceConstants.CANNOT_SET_STRING_PARAMETER;

		String effectiveParamName = paramName.getEffectiveName();
		try {
			getStatement().setString(effectiveParamName, stringValue);
		} catch (OdaException ex) {
			Object[] msgArgs = new Object[] { stringValue, effectiveParamName };
			throwException(ex, errorCode, msgArgs, methodName);
		} catch (UnsupportedOperationException ex) {
			// first try to set value by position if the parameter hints provide
			// name-to-position mapping,
			// otherwise we need to wrap the UnsupportedOperationException up and throw it
			if (!setStringUsingHints(paramName, stringValue)) {
				Object[] msgArgs = new Object[] { stringValue, effectiveParamName };
				throwException(ex, errorCode, msgArgs, methodName);
			}
		}
	}

	private void doSetBigDecimal(int paramIndex, BigDecimal decimal) throws DataException {
		final String methodName = "doSetBigDecimal( int, BigDecimal )"; //$NON-NLS-1$
		final String errorCode = ResourceConstants.CANNOT_SET_BIGDECIMAL_PARAMETER;

		try {
			getStatement().setBigDecimal(paramIndex, getScaleValue(decimal, paramIndex));
		} catch (OdaException ex) {
			Object[] msgArgs = new Object[] { decimal, Integer.valueOf(paramIndex) };
			throwException(ex, errorCode, msgArgs, methodName);
		} catch (UnsupportedOperationException ex) {
			Object[] msgArgs = new Object[] { decimal, Integer.valueOf(paramIndex) };
			throwException(ex, errorCode, msgArgs, methodName);
		}
	}

	private void doSetBigDecimal(ParameterName paramName, BigDecimal decimal) throws DataException {
		final String methodName = "doSetBigDecimal( ParameterName, BigDecimal )"; //$NON-NLS-1$
		final String errorCode = ResourceConstants.CANNOT_SET_BIGDECIMAL_PARAMETER;

		String effectiveParamName = paramName.getEffectiveName();
		try {
			getStatement().setBigDecimal(effectiveParamName, getScaleValue(decimal, paramName));
		} catch (OdaException ex) {
			Object[] msgArgs = new Object[] { decimal, effectiveParamName };
			throwException(ex, errorCode, msgArgs, methodName);
		} catch (UnsupportedOperationException ex) {
			// first try to set value by position if the parameter hints provide
			// name-to-position mapping,
			// otherwise we need to wrap the UnsupportedOperationException up and throw it
			if (!setBigDecimalUsingHints(paramName, decimal)) {
				Object[] msgArgs = new Object[] { decimal, effectiveParamName };
				throwException(ex, errorCode, msgArgs, methodName);
			}
		}
	}

	/**
	 * 
	 * check the passed bigDecimal scale, make sure the scale <= data base scale.
	 * 
	 * @param value
	 * @param parameterId
	 * @return
	 */
	private BigDecimal getScaleValue(BigDecimal value, int parameterId) {
		int scale = 0;
		try {
			scale = this.getParameterMetaData(parameterId).getScale();
			if (scale == 0) {
				return value;
			}
		} catch (Exception ignore) {
			return value;
		}
		return value.setScale(scale, BigDecimal.ROUND_HALF_UP);
	}

	private BigDecimal getScaleValue(BigDecimal value, ParameterName parameterName) {
		int scale = 0;
		try {
			scale = this.getParameterMetaData(parameterName).getScale();
			if (scale == 0) {
				return value;
			}
		} catch (Exception ignore) {
			return value;
		}
		return value.setScale(scale, BigDecimal.ROUND_HALF_UP);
	}

	private void doSetDate(int paramIndex, Date date) throws DataException {
		final String methodName = "doSetDate( int, Date )"; //$NON-NLS-1$
		final String errorCode = ResourceConstants.CANNOT_SET_DATE_PARAMETER;

		try {
			getStatement().setDate(paramIndex, date);
		} catch (OdaException ex) {
			Object[] msgArgs = new Object[] { date, Integer.valueOf(paramIndex) };
			throwException(ex, errorCode, msgArgs, methodName);
		} catch (UnsupportedOperationException ex) {
			Object[] msgArgs = new Object[] { date, Integer.valueOf(paramIndex) };
			throwException(ex, errorCode, msgArgs, methodName);
		}
	}

	private void doSetDate(ParameterName paramName, Date date) throws DataException {
		final String methodName = "doSetDate( ParameterName, Date )"; //$NON-NLS-1$
		final String errorCode = ResourceConstants.CANNOT_SET_DATE_PARAMETER;

		String effectiveParamName = paramName.getEffectiveName();
		try {
			getStatement().setDate(effectiveParamName, date);
		} catch (OdaException ex) {
			Object[] msgArgs = new Object[] { date, effectiveParamName };
			throwException(ex, errorCode, msgArgs, methodName);
		} catch (UnsupportedOperationException ex) {
			// first try to set value by position if the parameter hints provide
			// name-to-position mapping,
			// otherwise we need to wrap the UnsupportedOperationException up and throw it
			if (!setDateUsingHints(paramName, date)) {
				Object[] msgArgs = new Object[] { date, effectiveParamName };
				throwException(ex, errorCode, msgArgs, methodName);
			}
		}
	}

	private void doSetTime(int paramIndex, Time time) throws DataException {
		final String methodName = "doSetTime( int, Time )"; //$NON-NLS-1$
		final String errorCode = ResourceConstants.CANNOT_SET_TIME_PARAMETER;

		try {
			getStatement().setTime(paramIndex, time);
		} catch (OdaException ex) {
			Object[] msgArgs = new Object[] { time, Integer.valueOf(paramIndex) };
			throwException(ex, errorCode, msgArgs, methodName);
		} catch (UnsupportedOperationException ex) {
			Object[] msgArgs = new Object[] { time, Integer.valueOf(paramIndex) };
			throwException(ex, errorCode, msgArgs, methodName);
		}
	}

	private void doSetTime(ParameterName paramName, Time time) throws DataException {
		final String methodName = "doSetTime( ParameterName, Time )"; //$NON-NLS-1$
		final String errorCode = ResourceConstants.CANNOT_SET_TIME_PARAMETER;

		String effectiveParamName = paramName.getEffectiveName();
		try {
			getStatement().setTime(effectiveParamName, time);
		} catch (OdaException ex) {
			Object[] msgArgs = new Object[] { time, effectiveParamName };
			throwException(ex, errorCode, msgArgs, methodName);
		} catch (UnsupportedOperationException ex) {
			// first try to set value by position if the parameter hints provide
			// name-to-position mapping,
			// otherwise we need to wrap the UnsupportedOperationException up and throw it
			if (!setTimeUsingHints(paramName, time)) {
				Object[] msgArgs = new Object[] { time, effectiveParamName };
				throwException(ex, errorCode, msgArgs, methodName);
			}
		}
	}

	private void doSetTimestamp(int paramIndex, Timestamp timestamp) throws DataException {
		final String methodName = "doSetTimestamp( int, Timestamp )"; //$NON-NLS-1$
		final String errorCode = ResourceConstants.CANNOT_SET_TIMESTAMP_PARAMETER;

		try {
			getStatement().setTimestamp(paramIndex, timestamp);
		} catch (OdaException ex) {
			Object[] msgArgs = new Object[] { timestamp, Integer.valueOf(paramIndex) };
			throwException(ex, errorCode, msgArgs, methodName);
		} catch (UnsupportedOperationException ex) {
			Object[] msgArgs = new Object[] { timestamp, Integer.valueOf(paramIndex) };
			throwException(ex, errorCode, msgArgs, methodName);
		}
	}

	private void doSetTimestamp(ParameterName paramName, Timestamp timestamp) throws DataException {
		final String methodName = "doSetTimestamp( ParameterName, Timestamp )"; //$NON-NLS-1$
		final String errorCode = ResourceConstants.CANNOT_SET_TIMESTAMP_PARAMETER;

		String effectiveParamName = paramName.getEffectiveName();
		try {
			getStatement().setTimestamp(effectiveParamName, timestamp);
		} catch (OdaException ex) {
			Object[] msgArgs = new Object[] { timestamp, effectiveParamName };
			throwException(ex, errorCode, msgArgs, methodName);
		} catch (UnsupportedOperationException ex) {
			// first try to set value by position if the parameter hints provide
			// name-to-position mapping,
			// otherwise we need to wrap the UnsupportedOperationException up and throw it
			if (!setTimestampUsingHints(paramName, timestamp)) {
				Object[] msgArgs = new Object[] { timestamp, effectiveParamName };
				throwException(ex, errorCode, msgArgs, methodName);
			}
		}
	}

	private void doSetBoolean(int paramIndex, boolean val) throws DataException {
		final String methodName = "doSetBoolean( int, boolean )"; //$NON-NLS-1$
		final String errorCode = ResourceConstants.CANNOT_SET_BOOLEAN_PARAMETER;

		try {
			getStatement().setBoolean(paramIndex, val);
		} catch (OdaException ex) {
			Object[] msgArgs = new Object[] { Boolean.valueOf(val), Integer.valueOf(paramIndex) };
			throwException(ex, errorCode, msgArgs, methodName);
		} catch (UnsupportedOperationException ex) {
			Object[] msgArgs = new Object[] { Boolean.valueOf(val), Integer.valueOf(paramIndex) };
			throwException(ex, errorCode, msgArgs, methodName);
		}
	}

	private void doSetBoolean(ParameterName paramName, boolean val) throws DataException {
		final String methodName = "doSetBoolean( ParameterName, boolean )"; //$NON-NLS-1$
		final String errorCode = ResourceConstants.CANNOT_SET_BOOLEAN_PARAMETER;

		String effectiveParamName = paramName.getEffectiveName();
		try {
			getStatement().setBoolean(effectiveParamName, val);
		} catch (OdaException ex) {
			Object[] msgArgs = new Object[] { Boolean.valueOf(val), effectiveParamName };
			throwException(ex, errorCode, msgArgs, methodName);
		} catch (UnsupportedOperationException ex) {
			// first try to set value by position if the parameter hints provide
			// name-to-position mapping,
			// otherwise we need to wrap the UnsupportedOperationException up and throw it
			if (!setBooleanUsingHints(paramName, val)) {
				Object[] msgArgs = new Object[] { Boolean.valueOf(val), effectiveParamName };
				throwException(ex, errorCode, msgArgs, methodName);
			}
		}
	}

	private void doSetObject(int paramIndex, Object value) throws DataException {
		final String methodName = "doSetObject( int, Object )"; //$NON-NLS-1$
		final String errorCode = ResourceConstants.CANNOT_SET_OBJECT_PARAMETER;

		try {
			getStatement().setObject(paramIndex, value);
		} catch (OdaException ex) {
			Object[] msgArgs = new Object[] { value, Integer.valueOf(paramIndex) };
			throwException(ex, errorCode, msgArgs, methodName);
		} catch (UnsupportedOperationException ex) {
			Object[] msgArgs = new Object[] { value, Integer.valueOf(paramIndex) };
			throwException(ex, errorCode, msgArgs, methodName);
		}
	}

	private void doSetObject(ParameterName paramName, Object value) throws DataException {
		final String methodName = "doSetObject( ParameterName, Object )"; //$NON-NLS-1$
		final String errorCode = ResourceConstants.CANNOT_SET_OBJECT_PARAMETER;

		String effectiveParamName = paramName.getEffectiveName();
		try {
			getStatement().setObject(effectiveParamName, value);
		} catch (OdaException ex) {
			Object[] msgArgs = new Object[] { value, effectiveParamName };
			throwException(ex, errorCode, msgArgs, methodName);
		} catch (UnsupportedOperationException ex) {
			// first try to set value by position if the parameter hints provide
			// name-to-position mapping,
			// otherwise we need to wrap the UnsupportedOperationException up and throw it
			if (!setObjectUsingHints(paramName, value)) {
				Object[] msgArgs = new Object[] { value, effectiveParamName };
				throwException(ex, errorCode, msgArgs, methodName);
			}
		}
	}

	private void doSetNull(int paramIndex) throws DataException {
		final String methodName = "doSetNull( int )"; //$NON-NLS-1$
		final String errorCode = ResourceConstants.CANNOT_SET_NULL_PARAMETER;

		try {
			getStatement().setNull(paramIndex);
		} catch (OdaException ex) {
			Object[] msgArgs = new Object[] { Integer.valueOf(paramIndex) };
			throwException(ex, errorCode, msgArgs, methodName);
		} catch (UnsupportedOperationException ex) {
			Object[] msgArgs = new Object[] { Integer.valueOf(paramIndex) };
			throwException(ex, errorCode, msgArgs, methodName);
		}
	}

	private void doSetNull(ParameterName paramName) throws DataException {
		final String methodName = "doSetNull( ParameterName )"; //$NON-NLS-1$
		final String errorCode = ResourceConstants.CANNOT_SET_NULL_PARAMETER;

		String effectiveParamName = paramName.getEffectiveName();
		try {
			getStatement().setNull(effectiveParamName);
		} catch (OdaException ex) {
			Object[] msgArgs = new Object[] { effectiveParamName };
			throwException(ex, errorCode, msgArgs, methodName);
		} catch (UnsupportedOperationException ex) {
			// first try to set value by position if the parameter hints provide
			// name-to-position mapping,
			// otherwise we need to wrap the UnsupportedOperationException up and throw it
			if (!setNullUsingHints(paramName)) {
				Object[] msgArgs = new Object[] { effectiveParamName };
				throwException(ex, errorCode, msgArgs, methodName);
			}
		}
	}

	static boolean hasValue(String value) {
		return (value != null && value.length() > 0);
	}

	private static final class Property {
		private String m_name;
		private String m_value;

		private Property(String name, String value) {
			m_name = name;
			m_value = value;
		}

		private String getName() {
			return m_name;
		}

		private String getValue() {
			return m_value;
		}
	}

	static final class CustomColumn {
		private String m_name;
		private Class m_type;

		CustomColumn(String name, Class type) {
			m_name = name;
			m_type = type;
		}

		private String getName() {
			return m_name;
		}

		private Class getType() {
			return m_type;
		}
	}

	private static final class ParameterName {
		private String m_romName;
		private String m_nativeName;
		private boolean m_hasCheckedNativeName = false;
		private PreparedStatement m_stmt;

		private ParameterName(String romName, PreparedStatement stmt) {
			m_romName = romName;
			m_stmt = stmt;
		}

		private String getRomName() {
			return m_romName;
		}

		private String getNativeName() {
			if (m_nativeName == null && !m_hasCheckedNativeName) {
				// first try to get from merged runtime metadata and design hints
				m_nativeName = m_stmt.getNativeNameFromParameterMetaData(m_romName);

				// if not found, it could be that runtime param metadata has no info;
				// see if it is available from design hints
				if (m_nativeName == null)
					m_nativeName = m_stmt.getNativeNameFromParamHints(m_romName);

				m_hasCheckedNativeName = true; // optimize to avoid repeated checking
			}

			return m_nativeName;
		}

		private String getEffectiveName() {
			String nativeName = getNativeName();
			return (nativeName != null && nativeName.length() > 0) ? nativeName : getRomName();
		}

		private void logNullNativeName() {
			if (getNativeName() != null)
				return; // exists

			// no native name available, log info
			getLogger().logp(Level.FINER, sm_className + ".ParameterName", //$NON-NLS-1$
					"logNullNativeName()", //$NON-NLS-1$
					"No native name available for parameter " + getRomName() + "."); //$NON-NLS-1$ //$NON-NLS-2$
		}

		public String toString() {
			DataException resourceMsgHandler = newException(ResourceConstants.PARAMETER_NAMES_INFO,
					new Object[] { m_romName, m_nativeName });
			return resourceMsgHandler.getLocalizedMessage();
		}
	}

	private SequentialResultSetHandler getSequentialResultHandler() {
		if (m_seqResultSetHdlr == null)
			m_seqResultSetHdlr = new SequentialResultSetHandler(this);
		return m_seqResultSetHdlr;
	}

	/**
	 * Handler of multiple result sets for this statement.
	 */
	private final class SequentialResultSetHandler {
		private final String m_nestedClassName = SequentialResultSetHandler.class.getName();

		private PreparedStatement m_stmt;

		private int m_currentResultSetNum = 1;
		private Map<Integer, ResultSet> m_resultSets;
		private Map<Integer, ProjectedColumns> m_seqProjectedColumns;
		// a set of resultSetNum whose ProjectedColumns in m_seqProjectedColumns need to
		// merge with runtime metadata
		private HashSet<Integer> m_incompleteProjectedColumns;
		private ExceptionHandler m_exceptionHandler;

		private SequentialResultSetHandler(PreparedStatement stmt) {
			m_stmt = stmt;
			m_resultSets = new HashMap<Integer, ResultSet>();
			m_seqProjectedColumns = new HashMap<Integer, ProjectedColumns>();
			m_incompleteProjectedColumns = new HashSet<Integer>();
			m_exceptionHandler = new ExceptionHandler(m_nestedClassName);
		}

		private void resetResultSetsState() {
			m_currentResultSetNum = 1;
			closeAllResultSets();
		}

		private void closeAllResultSets() {
			Iterator<Integer> resultSetKeyIter = m_resultSets.keySet().iterator();
			while (resultSetKeyIter.hasNext()) {
				Integer resultSetKey = resultSetKeyIter.next();
				closeResultSet(resultSetKey);
			}
			m_resultSets.clear();

			// m_projectedColumns is not cleared, but simply
			// set the incomplete flag for all the cached ProjectedColumns,
			// so to avoid having to get new ones to replace them all immediately.
			// When needed, the incomplete flag will trigger merging
			// a new set of runtime metadata with the custom column/
			// colum hints/projections info from the cached ProjectedColumn
			m_incompleteProjectedColumns.addAll(m_seqProjectedColumns.keySet());
		}

		private void closeResultSet(Integer resultSetKey) {
			ResultSet rs = getCachedResultSet(resultSetKey);
			try {
				if (rs != null)
					rs.close();
			} catch (DataException ex) {
				// ignore
			}

			// m_projectedColumns of the result set if exists, is not cleared, but simply
			// set the incomplete flag on this result set
			if (m_seqProjectedColumns.containsKey(resultSetKey))
				m_incompleteProjectedColumns.add(resultSetKey);
		}

		private void clearAndCloseResultSet(Integer resultSetKey) {
			closeResultSet(resultSetKey);
			m_resultSets.remove(resultSetKey);
		}

		/**
		 * If specified result set exists in cache, update its metadata with the latest
		 * projected columns.
		 */
		private void refreshResultSetMetaData(Integer resultSetKey) throws DataException {
			ResultSet cachedRS = getCachedResultSet(resultSetKey);
			if (cachedRS == null)
				return; // nothing to refresh, done

			final String methodName = "refreshResultSetMetaData(Integer)"; //$NON-NLS-1$
			getLogger().entering(m_nestedClassName, methodName, resultSetKey);

			// create new result class with the latest projected columns and the original
			// oda result set;
			// re-use original oda result set because an oda driver might not support
			// getting it again
			IResultSet odaRS = cachedRS.getRuntimeResultSet();
			ResultSet refreshedRS = new ResultSet(odaRS, doGetMetaData(resultSetKey, odaRS));

			// replace the cached copy
			cacheResultSet(resultSetKey, refreshedRS);

			getLogger().exiting(m_nestedClassName, methodName);
		}

		/**
		 * @see PreparedStatement#getResultSet(int)
		 */
		ResultSet getResultSet(int resultSetNum) throws DataException {
			final String methodName = "getResultSet(int)"; //$NON-NLS-1$
			if (getLogger().isLoggingEnterExitLevel())
				getLogger().entering(m_nestedClassName, methodName, Integer.valueOf(resultSetNum));

			Integer resultSetKey = Integer.valueOf(resultSetNum);

			// TODO - adds support for oda data source that supportsMultipleOpenResults;
			// for now, only the current result set can be accessed

			// first validate the index value to be within range and is at/beyond current
			// result set count;
			// it is valid that this has iterated to the specified index, but has not yet
			// retrieved its result set
			if (resultSetNum <= 0 || resultSetNum < m_currentResultSetNum)
				throwInvalidArgException(methodName, resultSetKey);

			// first see if the result set at given index is the current one and was already
			// retrieved
			ResultSet rs = null;
			if (resultSetNum == m_currentResultSetNum) {
				rs = getCachedResultSet(resultSetKey);
				if (rs != null) {
					getLogger().logp(Level.FINEST, m_nestedClassName, methodName, "Found cached result set."); //$NON-NLS-1$
					getLogger().exiting(m_nestedClassName, methodName, rs);
					return rs;
				}
			}

			// result set is not retrieved yet, get it from the ODA driver

			if (!supportsMultipleResultSets()) {
				if (resultSetNum == 1) {
					rs = m_stmt.getResultSet(); // equivalent to calling getResultSet() directly
					m_currentResultSetNum = resultSetNum;
				} else
					throwInvalidArgException(methodName, resultSetKey);
			} else // supports multiple result sets
			{
				boolean hasMoreResults = moveToResultSet(resultSetNum);
				if (!hasMoreResults) // not able to skip to specified index
					throwInvalidArgException(methodName, resultSetKey);

				// has successfully iterated to the specified result set
				rs = doGetResultSet(resultSetKey);
			}

			// cache the retrieved result set for repeated call to the same index
			cacheResultSet(resultSetKey, rs);

			getLogger().exiting(m_nestedClassName, methodName, rs);

			return rs;
		}

		private boolean moveToResultSet(int resultSetNum) throws DataException {
			int skipResultsCount = resultSetNum - m_currentResultSetNum;
			boolean hasMoreResults = true;
			for (int i = 0; hasMoreResults && i < skipResultsCount; i++) {
				hasMoreResults = getMoreResults(); // updates m_currentResultSetNum as well
			}
			return hasMoreResults;
		}

		private ResultSet doGetResultSet(Integer resultSetKey) throws DataException {
			final String methodName = "doGetResultSet(Integer)"; //$NON-NLS-1$
			getLogger().entering(m_nestedClassName, methodName);

			IResultSet resultSet = null;

			try {
				resultSet = getAdvancedStatement().getResultSet();
			} catch (OdaException ex) {
				throwDataException(ex, ResourceConstants.CANNOT_GET_RESULTSET, methodName);
			} catch (UnsupportedOperationException ex) {
				throwDataException(ex, ResourceConstants.CANNOT_GET_RESULTSET, methodName);
			}

			ResultSet rs = (resultSet != null) ? new ResultSet(resultSet, doGetMetaData(resultSetKey, resultSet))
					: null;

			getLogger().exiting(m_nestedClassName, methodName, rs);

			return rs;
		}

		private ResultSet getCachedResultSet(Integer resultSetNum) {
			// assume the argument has already been validated
			return m_resultSets.get(resultSetNum);
		}

		private void cacheResultSet(Integer resultSetNum, ResultSet newResultSet) {
			m_resultSets.put(resultSetNum, newResultSet);
		}

		/**
		 * @see PreparedStatement#getMoreResults()
		 */
		boolean getMoreResults() throws DataException {
			final String methodName = "getMoreResults"; //$NON-NLS-1$
			getLogger().entering(m_nestedClassName, methodName);

			if (!supportsMultipleResultSets()) {
				getLogger().exiting(m_nestedClassName, methodName, Boolean.FALSE);
				return false;
			}

			// this supports multiple result sets
			boolean hasMoreResults = false;
			try {
				hasMoreResults = getAdvancedStatement().getMoreResults();
			} catch (OdaException ex) {
				throwDataException(ex, ResourceConstants.CANNOT_GET_MORE_RESULTS, methodName);
			} catch (UnsupportedOperationException ex) {
				throwDataException(ex, ResourceConstants.CANNOT_GET_MORE_RESULTS, methodName);
			}

			if (hasMoreResults) {
				m_currentResultSetNum++;

				// TODO - adds support for oda data source that supportsMultipleOpenResults;
				// for now, assume calling underlying data source to iterate result sets would
				// implicitly close its current result set, so any previously retrieved result
				// sets
				// should be considered closed and can no longer be referenced
				closeAllResultSets();
			}

			if (getLogger().isLoggingEnterExitLevel())
				getLogger().exiting(m_nestedClassName, methodName, Boolean.valueOf(hasMoreResults));

			return hasMoreResults;
		}

		/**
		 * @see PreparedStatement#setColumnsProjection(int, String[])
		 */
		void setColumnsProjection(int resultSetNum, String[] projectedNames) throws DataException {
			final String methodName = "setColumnsProjection(int, String[])"; //$NON-NLS-1$
			Integer resultSetKey = Integer.valueOf(resultSetNum);
			if (getLogger().isLoggingEnterExitLevel())
				getLogger().entering(m_nestedClassName, methodName, new Object[] { resultSetKey, projectedNames });

			validateMultipleResultsSupport();
			clearAndCloseResultSet(resultSetKey);

			getProjectedColumns(resultSetKey, null).setProjectedNames(projectedNames);

			getLogger().exiting(m_nestedClassName, methodName);
		}

		/**
		 * @see PreparedStatement#declareCustomColumn(int, String, Class)
		 */
		void declareCustomColumn(int resultSetNum, String columnName, Class columnType) throws DataException {
			final String methodName = "declareCustomColumn(int, String, Class)"; //$NON-NLS-1$
			Integer resultSetKey = Integer.valueOf(resultSetNum);
			if (getLogger().isLoggingEnterExitLevel())
				getLogger().entering(m_nestedClassName, methodName,
						new Object[] { resultSetKey, columnName, columnType });

			validateMultipleResultsSupport();

			assert columnName != null;
			assert columnName.length() != 0;

			// need to reset specified result set and metadata because a custom column could
			// be
			// declared after we projected all columns, which means we would
			// want to project the newly declared custom column as well
			clearAndCloseResultSet(resultSetKey);

			getProjectedColumns(resultSetKey, null).addCustomColumn(columnName, columnType);

			// since call to getProjectedColumns above may have the side effect of caching a
			// result set before the
			// custom column is added, update the cached result set's metadata with the
			// latest projected columns
			refreshResultSetMetaData(resultSetKey);

			getLogger().exiting(m_nestedClassName, methodName);
		}

		/**
		 * @see PreparedStatement#addColumnHint(int, ColumnHint)
		 */
		void addColumnHint(int resultSetNum, ColumnHint columnHint) throws DataException {
			final String methodName = "addColumnHint(int, ColumnHint)"; //$NON-NLS-1$
			Integer resultSetKey = Integer.valueOf(resultSetNum);
			if (getLogger().isLoggingEnterExitLevel())
				getLogger().entering(m_nestedClassName, methodName, new Object[] { resultSetKey, columnHint });

			validateMultipleResultsSupport();

			if (columnHint != null) {
				// no need to reset the current metadata because adding a column
				// hint doesn't change the existing columns that are being projected,
				// it just updates some of the column metadata
				getProjectedColumns(resultSetKey, null).addHint(columnHint);
			}

			getLogger().exiting(m_nestedClassName, methodName);
		}

		private ProjectedColumns getProjectedColumns(Integer resultSetNum, IResultSet odaResultSet)
				throws DataException {
			final String methodName = "getProjectedColumns(Integer,IResultSet)"; //$NON-NLS-1$
			getLogger().entering(m_nestedClassName, methodName, resultSetNum);

			ProjectedColumns projectedColumns = (ProjectedColumns) m_seqProjectedColumns.get(resultSetNum);

			// has existing up-to-date ProjectedColumns
			if (projectedColumns != null && !m_incompleteProjectedColumns.contains(resultSetNum)) {
				getLogger().exiting(m_nestedClassName, methodName, projectedColumns);
				return projectedColumns;
			}

			// has no existing ProjectedColumns or it is incomplete

			IResultSetMetaData odaRuntimeMetadata = tryGetRuntimeMetaData(resultSetNum, odaResultSet);
			boolean hasOdaRuntimeMetadata = odaRuntimeMetadata != null;

			// no result set available yet, probably has not yet executed;
			// try use the statement's current result metadata, assuming it has same
			// metadata as the index one
			if (!hasOdaRuntimeMetadata) {
				getLogger().logp(Level.INFO, m_nestedClassName, methodName,
						"Using the statement's current result set for result set " + resultSetNum); //$NON-NLS-1$
				odaRuntimeMetadata = m_stmt.getRuntimeMetaData();
			}

			ProjectedColumns newProjectedColumns = doGetProjectedColumns(odaRuntimeMetadata);

			if (projectedColumns == null) {
				// use the new ProjectedColumns at resultSetNum
				projectedColumns = newProjectedColumns;
				if (!hasOdaRuntimeMetadata) // no actual result set available yet
					m_incompleteProjectedColumns.add(resultSetNum);
			} else if (m_incompleteProjectedColumns.contains(resultSetNum)) {
				// there is an existing ProjectedColumns for this result set,
				// which may be out-dated and needs to be merged with the latest runtime
				// metadata
				updateProjectedColumns(newProjectedColumns, projectedColumns);
				projectedColumns = newProjectedColumns;

				// now that it is up-to-date with latest runtime result set,
				// remove the incomplete flag for this result set
				if (hasOdaRuntimeMetadata)
					m_incompleteProjectedColumns.remove(resultSetNum);
			}

			m_seqProjectedColumns.put(resultSetNum, projectedColumns);

			getLogger().exiting(m_nestedClassName, methodName, projectedColumns);

			return projectedColumns;
		}

		private IResultSetMetaData tryGetRuntimeMetaData(Integer resultSetNum, IResultSet odaResultSet)
				throws DataException {
			if (odaResultSet != null)
				return getRuntimeMetaData(odaResultSet);

			// if interested in first result set, and statement has not advanced its result
			// sets,
			// try use the statement's current result metadata without having to execute
			if (resultSetNum.intValue() == 1 && m_currentResultSetNum == 1) {
				try {
					return m_stmt.getRuntimeMetaData();
				} catch (DataException ex1) {
					// ignore, continue to try below
				}
			}

			// next try to get the result set at the index for its metadata
			IResultSetMetaData rsmd = tryGetRuntimeMetaData(resultSetNum);
			return rsmd;
		}

		private IResultSetMetaData getRuntimeMetaData(IResultSet odaResultSet) throws DataException {
			IResultSetMetaData rsmd = null;
			try {
				rsmd = odaResultSet.getMetaData();
			} catch (OdaException ex) {
				throwDataException(ex, ResourceConstants.CANNOT_GET_RESULTSET_METADATA,
						"getRuntimeMetaData(IResultSet)"); //$NON-NLS-1$
			}
			return rsmd;
		}

		/*
		 * Try to get the result set at the index for its metadata. This has the side
		 * effect of advancing the result sets to the specified index. Ignores all
		 * errors if caught and returns null instead.
		 */
		private IResultSetMetaData tryGetRuntimeMetaData(Integer resultSetNum) {
			IResultSetMetaData rsmd = null;
			try {
				ResultSet rs = getResultSet(resultSetNum);
				if (rs != null)
					rsmd = rs.getRuntimeMetaData();
			} catch (DataException ex) {
				// ignore
			}

			return rsmd;
		}

		/**
		 * @see PreparedStatement#getMetaData(int)
		 */
		IResultClass getMetaData(int resultSetNum) throws DataException {
			final String methodName = "getMetaData(int)"; //$NON-NLS-1$
			if (getLogger().isLoggingEnterExitLevel())
				getLogger().entering(m_nestedClassName, methodName, Integer.valueOf(resultSetNum));

			validateMultipleResultsSupport();

			// the only way to get the metadata of a sequential result set
			// is through the result set itself
			ResultSet resultset = getResultSet(resultSetNum);

			IResultClass resultClass = null;
			if (resultset != null)
				resultClass = resultset.getMetaData();

			getLogger().exiting(m_nestedClassName, methodName, resultClass);
			return resultClass;
		}

		private IResultClass doGetMetaData(Integer resultSetKey, IResultSet resultSet) throws DataException {
			final String methodName = "doGetMetaData(Integer,IResultSet)"; //$NON-NLS-1$
			getLogger().entering(m_nestedClassName, methodName, resultSetKey);

			List projectedColumns = getProjectedColumns(resultSetKey, resultSet).getColumnsMetadata();
			IResultClass resultClass = doGetResultClass(projectedColumns);

			getLogger().exiting(m_nestedClassName, methodName, resultClass);

			return resultClass;
		}

		private void validateMultipleResultsSupport() throws DataException {
			if (supportsMultipleResultSets())
				return; // is valid

			throwDataException(new UnsupportedOperationException(), ResourceConstants.UNSUPPORTED_MULTIPLE_RESULTS,
					"validateMultipleResultsSupport"); //$NON-NLS-1$
		}

		private void throwInvalidArgException(final String methodName, Integer resultSetKey) throws OdaDataException {
			m_exceptionHandler.throwError(ResourceConstants.INVALID_METHOD_ARGUMENT,
					new Object[] { methodName, resultSetKey }, methodName);
		}

		private void throwDataException(Throwable ex, String errorCode, final String methodName)
				throws OdaDataException {
			m_exceptionHandler.throwException(ex, errorCode, methodName);
		}
	}

	public void checkColumnsNaming() throws DataException {
		getProjectedColumns().checkColumnsNaming();
	}

}
