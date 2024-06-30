/*
 *************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation - initial API and implementation
 *
 *************************************************************************
 */

package org.eclipse.birt.data.oda.mongodb.impl;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

import org.eclipse.birt.data.oda.mongodb.internal.impl.DriverUtil;
import org.eclipse.birt.data.oda.mongodb.internal.impl.QueryModel;
import org.eclipse.birt.data.oda.mongodb.internal.impl.QueryProperties;
import org.eclipse.birt.data.oda.mongodb.nls.Messages;
import org.eclipse.datatools.connectivity.oda.IParameterMetaData;
import org.eclipse.datatools.connectivity.oda.IQuery;
import org.eclipse.datatools.connectivity.oda.IResultSet;
import org.eclipse.datatools.connectivity.oda.IResultSetMetaData;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.SortSpec;
import org.eclipse.datatools.connectivity.oda.spec.QuerySpecification;

/**
 * Implementation class of IQuery for the MongoDB ODA runtime driver.
 */
public class MDbQuery implements IQuery {
	public static final String ODA_DATA_SET_ID = "org.eclipse.birt.data.oda.mongodb.dataSet"; //$NON-NLS-1$
	static UnsupportedOperationException sm_unSupportedOpEx = new UnsupportedOperationException();

	private MDbConnection m_mdbConn;

	private QueryModel m_model;
	private QuerySpecification m_querySpec;
	private int m_maxRows;

	MDbQuery(MDbConnection mdbConn) {
		if (mdbConn == null) {
			throw new NullPointerException("null connection"); //$NON-NLS-1$
		}
		m_mdbConn = mdbConn;
	}

	private void resetPreparedQuery() {
		m_model = null;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#prepare(java.lang.String)
	 */
	@Override
	public void prepare(String queryText) throws OdaException {
		resetPreparedQuery();

		QueryProperties queryProps = QueryProperties.deserialize(queryText);
		m_model = new QueryModel(queryProps, m_mdbConn.getConnectedDB());
		if (hasValidModel()) {
			m_model.addQuerySpec(getSpecification());
		}

		// #prepare may be called multiple times;
		// defer format of prepared query content till call to #getEffectiveQueryText
	}

	private boolean hasValidModel() {
		return m_model != null && m_model.isValid();
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IQuery#setAppContext(java.lang.Object)
	 */
	@Override
	public void setAppContext(Object context) throws OdaException {
		// do nothing; no support for pass-through context
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#close()
	 */
	@Override
	public void close() throws OdaException {
		resetPreparedQuery();
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#getMetaData()
	 */
	@Override
	public IResultSetMetaData getMetaData() throws OdaException {
		if (!hasValidModel()) {
			throw new OdaException(new IllegalStateException(Messages.mDbQuery_invalidQueryGetMD));
		}

		return m_model.getResultSetMetaData();
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#executeQuery()
	 */
	@Override
	public IResultSet executeQuery() throws OdaException {
		if (!hasValidModel()) {
			throw new OdaException(Messages.mDbQuery_invalidQueryExecQuery);
		}

		IResultSet resultSet = m_model.execute();
		resultSet.setMaxRows(getMaxRows());
		return resultSet;
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IQuery#setProperty(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void setProperty(String name, String value) throws OdaException {
		// do nothing; only supports setting data set properties once
		// via #setSpecification(QuerySpecification), which is called before #prepare
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setMaxRows(int)
	 */
	@Override
	public void setMaxRows(int max) throws OdaException {
		m_maxRows = max;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#getMaxRows()
	 */
	@Override
	public int getMaxRows() throws OdaException {
		return m_maxRows;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#clearInParameters()
	 */
	@Override
	public void clearInParameters() throws OdaException {
		// do nothing; input parameter is not supported
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setInt(java.lang.String,
	 * int)
	 */
	@Override
	public void setInt(String parameterName, int value) throws OdaException {
		throw sm_unSupportedOpEx;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setInt(int, int)
	 */
	@Override
	public void setInt(int parameterId, int value) throws OdaException {
		throw sm_unSupportedOpEx;
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IQuery#setDouble(java.lang.String,
	 * double)
	 */
	@Override
	public void setDouble(String parameterName, double value) throws OdaException {
		throw sm_unSupportedOpEx;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setDouble(int, double)
	 */
	@Override
	public void setDouble(int parameterId, double value) throws OdaException {
		throw sm_unSupportedOpEx;
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IQuery#setBigDecimal(java.lang.String,
	 * java.math.BigDecimal)
	 */
	@Override
	public void setBigDecimal(String parameterName, BigDecimal value) throws OdaException {
		throw sm_unSupportedOpEx;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setBigDecimal(int,
	 * java.math.BigDecimal)
	 */
	@Override
	public void setBigDecimal(int parameterId, BigDecimal value) throws OdaException {
		throw sm_unSupportedOpEx;
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IQuery#setString(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void setString(String parameterName, String value) throws OdaException {
		throw sm_unSupportedOpEx;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setString(int,
	 * java.lang.String)
	 */
	@Override
	public void setString(int parameterId, String value) throws OdaException {
		throw sm_unSupportedOpEx;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setDate(java.lang.String,
	 * java.sql.Date)
	 */
	@Override
	public void setDate(String parameterName, Date value) throws OdaException {
		throw sm_unSupportedOpEx;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setDate(int,
	 * java.sql.Date)
	 */
	@Override
	public void setDate(int parameterId, Date value) throws OdaException {
		throw sm_unSupportedOpEx;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setTime(java.lang.String,
	 * java.sql.Time)
	 */
	@Override
	public void setTime(String parameterName, Time value) throws OdaException {
		throw sm_unSupportedOpEx;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setTime(int,
	 * java.sql.Time)
	 */
	@Override
	public void setTime(int parameterId, Time value) throws OdaException {
		throw sm_unSupportedOpEx;
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IQuery#setTimestamp(java.lang.String,
	 * java.sql.Timestamp)
	 */
	@Override
	public void setTimestamp(String parameterName, Timestamp value) throws OdaException {
		throw sm_unSupportedOpEx;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setTimestamp(int,
	 * java.sql.Timestamp)
	 */
	@Override
	public void setTimestamp(int parameterId, Timestamp value) throws OdaException {
		throw sm_unSupportedOpEx;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IQuery#setBoolean(java.lang.String,
	 * boolean)
	 */
	@Override
	public void setBoolean(String parameterName, boolean value) throws OdaException {
		throw sm_unSupportedOpEx;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setBoolean(int, boolean)
	 */
	@Override
	public void setBoolean(int parameterId, boolean value) throws OdaException {
		throw sm_unSupportedOpEx;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IQuery#setObject(java.lang.String,
	 * java.lang.Object)
	 */
	@Override
	public void setObject(String parameterName, Object value) throws OdaException {
		throw sm_unSupportedOpEx;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setObject(int,
	 * java.lang.Object)
	 */
	@Override
	public void setObject(int parameterId, Object value) throws OdaException {
		throw sm_unSupportedOpEx;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setNull(java.lang.String)
	 */
	@Override
	public void setNull(String parameterName) throws OdaException {
		throw sm_unSupportedOpEx;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setNull(int)
	 */
	@Override
	public void setNull(int parameterId) throws OdaException {
		throw sm_unSupportedOpEx;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#findInParameter(java.lang.
	 * String)
	 */
	@Override
	public int findInParameter(String parameterName) throws OdaException {
		return 0;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#getParameterMetaData()
	 */
	@Override
	public IParameterMetaData getParameterMetaData() throws OdaException {
		return new ParameterMetaData();
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setSortSpec(org.eclipse.
	 * datatools.connectivity.oda.SortSpec)
	 */
	@Override
	public void setSortSpec(SortSpec sortBy) throws OdaException {
		// no push-down support;
		// only supports user-defined MongoDB Sort Expression defined in the data set
		throw new UnsupportedOperationException();
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#getSortSpec()
	 */
	@Override
	public SortSpec getSortSpec() throws OdaException {
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IQuery#setSpecification(org.eclipse.
	 * datatools.connectivity.oda.spec.QuerySpecification)
	 */
	@Override
	public void setSpecification(QuerySpecification querySpec) throws OdaException, UnsupportedOperationException {
		m_querySpec = querySpec;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#getSpecification()
	 */
	@Override
	public QuerySpecification getSpecification() {
		return m_querySpec;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#getEffectiveQueryText()
	 */
	@Override
	public String getEffectiveQueryText() {
		if (!hasValidModel()) {
			return DriverUtil.EMPTY_STRING;
		}

		return m_model.getEffectiveQueryText();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#cancel()
	 */
	@Override
	public void cancel() throws OdaException, UnsupportedOperationException {
		// does not support cancel while executing a query
		throw new UnsupportedOperationException();
	}

	/*
	 * For internal use only.
	 */
	public void setMetaDataSearchLimit(int searchLimit) {
		if (m_model != null) {
			m_model.setMetaDataSearchLimit(searchLimit);
		}
	}

}
