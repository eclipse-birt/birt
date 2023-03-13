/*
 *************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
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

package org.eclipse.birt.data.engine.odaconsumer.testdriver;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

import org.eclipse.datatools.connectivity.oda.IAdvancedQuery;
import org.eclipse.datatools.connectivity.oda.IBlob;
import org.eclipse.datatools.connectivity.oda.IClob;
import org.eclipse.datatools.connectivity.oda.IParameterMetaData;
import org.eclipse.datatools.connectivity.oda.IParameterRowSet;
import org.eclipse.datatools.connectivity.oda.IResultSet;
import org.eclipse.datatools.connectivity.oda.IResultSetMetaData;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.SortSpec;
import org.eclipse.datatools.connectivity.oda.spec.QuerySpecification;

/**
 * A tester ODA driver to test the behavior of odaconsumer, calling on an ODA
 * driver's IQuery implementation. Behavior being tested include: setAppContext
 * get<Type>( int ) - retrieving output parameter values by position sequential
 * multiple result sets named result sets
 */
public class TestAdvQueryImpl implements IAdvancedQuery {
	public static final String TEST_CASE_OUTPUTPARAM = "1"; //$NON-NLS-1$
	public static final String TEST_CASE_IN_PARAM_NAME = "2"; //$NON-NLS-1$
	public static final String TEST_CASE_SEQ_RESULT_SETS = "3"; //$NON-NLS-1$
	public static final String TEST_CASE_NAMED_RESULT_SETS = "4"; //$NON-NLS-1$
	public static final int MAX_RESULT_SETS = 3;

	private Object m_appContext;
	private boolean m_isPrepareCalled = false;
	private IParameterMetaData m_paramMetaData;
	private int m_currentTestCase = 0;
	private int m_currentResultSet = 1;
	private boolean m_hasExecuted = false;

	public TestAdvQueryImpl(int testCaseId) {
		m_currentTestCase = testCaseId;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#prepare(java.lang.String)
	 */
	@Override
	public void prepare(String queryText) throws OdaException {
		m_isPrepareCalled = true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IQuery#setAppContext(java.lang.Object)
	 */
	@Override
	public void setAppContext(Object context) throws OdaException {
		// a new this instance should be created each time by odaconsumer,
		// when it opens a connection;
		// so the state should be initialized properly each time
		if (m_isPrepareCalled) {
			throw new OdaException("Error: setAppContext should have been called *before* IQuery.prepare."); //$NON-NLS-1$
		}
		m_appContext = context;
	}

	public Object getAppContext() {
		return m_appContext;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IQuery#setProperty(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void setProperty(String name, String value) throws OdaException {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#close()
	 */
	@Override
	public void close() throws OdaException {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setMaxRows(int)
	 */
	@Override
	public void setMaxRows(int max) throws OdaException {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#getMaxRows()
	 */
	@Override
	public int getMaxRows() throws OdaException {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#getMetaData()
	 */
	@Override
	public IResultSetMetaData getMetaData() throws OdaException {
		return new TestResultSetMetaDataImpl();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#executeQuery()
	 */
	@Override
	public IResultSet executeQuery() throws OdaException {
		m_hasExecuted = true;
		return getResultSet();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#clearInParameters()
	 */
	@Override
	public void clearInParameters() throws OdaException {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setInt(java.lang.String,
	 * int)
	 */
	@Override
	public void setInt(String parameterName, int value) throws OdaException {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setInt(int, int)
	 */
	@Override
	public void setInt(int parameterId, int value) throws OdaException {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IQuery#setDouble(java.lang.String,
	 * double)
	 */
	@Override
	public void setDouble(String parameterName, double value) throws OdaException {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setDouble(int, double)
	 */
	@Override
	public void setDouble(int parameterId, double value) throws OdaException {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IQuery#setBigDecimal(java.lang.String,
	 * java.math.BigDecimal)
	 */
	@Override
	public void setBigDecimal(String parameterName, BigDecimal value) throws OdaException {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setBigDecimal(int,
	 * java.math.BigDecimal)
	 */
	@Override
	public void setBigDecimal(int parameterId, BigDecimal value) throws OdaException {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IQuery#setString(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void setString(String parameterName, String value) throws OdaException {
		if (m_currentTestCase == 2) {
			if (parameterName.endsWith("1")) //$NON-NLS-1$
			{
				if (!value.equals("stringValue")) { // $NON-NLS-1$
					throw new OdaException("Error in setString by name with value: " + value); //$NON-NLS-1$
				}
			} else if (parameterName.endsWith("3")) //$NON-NLS-1$
			{
				if (!value.equals("true")) { // $NON-NLS-1$
					throw new OdaException("Error in setString by name with value: " + value); //$NON-NLS-1$
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setString(int,
	 * java.lang.String)
	 */
	@Override
	public void setString(int parameterId, String value) throws OdaException {
		if (m_currentTestCase == 2) {
			throw new UnsupportedOperationException("Unable to setString by index"); //$NON-NLS-1$
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setDate(java.lang.String,
	 * java.sql.Date)
	 */
	@Override
	public void setDate(String parameterName, Date value) throws OdaException {
		if (m_currentTestCase == 2) {
			if (parameterName.endsWith("2")) { // $NON-NLS-1$
				if (!value.equals(Date.valueOf("2005-11-13"))) //$NON-NLS-1$
					throw new OdaException("Error in setDate by name"); //$NON-NLS-1$
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setDate(int,
	 * java.sql.Date)
	 */
	@Override
	public void setDate(int parameterId, Date value) throws OdaException {
		if (m_currentTestCase == 2) {
			throw new UnsupportedOperationException("Unable to setDate by index"); //$NON-NLS-1$
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setTime(java.lang.String,
	 * java.sql.Time)
	 */
	@Override
	public void setTime(String parameterName, Time value) throws OdaException {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setTime(int,
	 * java.sql.Time)
	 */
	@Override
	public void setTime(int parameterId, Time value) throws OdaException {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IQuery#setTimestamp(java.lang.String,
	 * java.sql.Timestamp)
	 */
	@Override
	public void setTimestamp(String parameterName, Timestamp value) throws OdaException {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setTimestamp(int,
	 * java.sql.Timestamp)
	 */
	@Override
	public void setTimestamp(int parameterId, Timestamp value) throws OdaException {
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
		if (m_currentTestCase == 2) {
			throw new UnsupportedOperationException("Unable to setBoolean by name"); //$NON-NLS-1$
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setBoolean(int, boolean)
	 */
	@Override
	public void setBoolean(int parameterId, boolean value) throws OdaException {
		if (m_currentTestCase == 2) {
			throw new OdaException("Unable to setBoolean by index"); //$NON-NLS-1$
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setNull(java.lang.String)
	 */
	@Override
	public void setNull(String parameterName) throws OdaException {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setNull(int)
	 */
	@Override
	public void setNull(int parameterId) throws OdaException {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#findInParameter(java.lang.
	 * String)
	 */
	@Override
	public int findInParameter(String parameterName) throws OdaException {
		if (m_currentTestCase == 1) {
			// test case does not handle input parameters
			throw new UnsupportedOperationException();
		}

		throw new OdaException("Bad test case"); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#getParameterMetaData()
	 */
	@Override
	public IParameterMetaData getParameterMetaData() throws OdaException {
		if (m_paramMetaData == null) {
			m_paramMetaData = new TestParamMetaDataImpl(m_currentTestCase);
		}
		return m_paramMetaData;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setSortSpec(org.eclipse.
	 * datatools.connectivity.oda.SortSpec)
	 */
	@Override
	public void setSortSpec(SortSpec sortBy) throws OdaException {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#getSortSpec()
	 */
	@Override
	public SortSpec getSortSpec() throws OdaException {
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#execute()
	 */
	@Override
	public boolean execute() throws OdaException {
		m_hasExecuted = true;
		return m_hasExecuted;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IAdvancedQuery#findOutParameter(java.
	 * lang.String)
	 */
	@Override
	public int findOutParameter(String parameterName) throws OdaException {
		// test driver does not support this
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getBoolean(int)
	 */
	@Override
	public boolean getBoolean(int parameterId) throws OdaException {
		// test driver does not support this
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getBoolean(java.lang.
	 * String)
	 */
	@Override
	public boolean getBoolean(String parameterName) throws OdaException {
		// test driver does not support this
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getBigDecimal(int)
	 */
	@Override
	public BigDecimal getBigDecimal(int parameterId) throws OdaException {
		// test driver does not support this
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getBigDecimal(java.lang
	 * .String)
	 */
	@Override
	public BigDecimal getBigDecimal(String parameterName) throws OdaException {
		// test driver does not support this
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getBlob(int)
	 */
	@Override
	public IBlob getBlob(int parameterId) throws OdaException {
		// test driver does not support this
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getBlob(java.lang.
	 * String)
	 */
	@Override
	public IBlob getBlob(String parameterName) throws OdaException {
		// test driver does not support this
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getClob(int)
	 */
	@Override
	public IClob getClob(int parameterId) throws OdaException {
		// test driver does not support this
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getClob(java.lang.
	 * String)
	 */
	@Override
	public IClob getClob(String parameterName) throws OdaException {
		// test driver does not support this
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getDate(int)
	 */
	@Override
	public Date getDate(int parameterId) throws OdaException {
		validateOutputParamId(parameterId);

		if (m_currentTestCase == 1) {
			if (parameterId == 2) {
				return Date.valueOf("2005-11-13"); //$NON-NLS-1$
			}
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getDate(java.lang.
	 * String)
	 */
	@Override
	public Date getDate(String parameterName) throws OdaException {
		if (m_currentTestCase == 1) {
			if (parameterName.endsWith("2")) { // $NON-NLS-1$
				return Date.valueOf("2005-11-13"); //$NON-NLS-1$
			}
		}

		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getDouble(int)
	 */
	@Override
	public double getDouble(int parameterId) throws OdaException {
		// test driver does not support this
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getDouble(java.lang.
	 * String)
	 */
	@Override
	public double getDouble(String parameterName) throws OdaException {
		// test driver does not support this
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getInt(int)
	 */
	@Override
	public int getInt(int parameterId) throws OdaException {
		// test driver does not support this
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getInt(java.lang.
	 * String)
	 */
	@Override
	public int getInt(String parameterName) throws OdaException {
		// test driver does not support this
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getMetaDataOf(java.lang
	 * .String)
	 */
	@Override
	public IResultSetMetaData getMetaDataOf(String resultSetName) throws OdaException {
		// test driver does not support this
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getMoreResults()
	 */
	@Override
	public boolean getMoreResults() throws OdaException {
		if (m_currentTestCase == 3) {
			if (!m_hasExecuted) {
				throw new OdaException("Illegal sequence; cannot getMoreResults before having executed."); //$NON-NLS-1$
			}

			if (m_currentResultSet + 1 <= MAX_RESULT_SETS) {
				m_currentResultSet++;
				return true;
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getResultSet()
	 */
	@Override
	public IResultSet getResultSet() throws OdaException {
		if (m_hasExecuted) {
			return new TestResultSetImpl(true, new TestResultSetMetaDataImpl());
		} else {
			throw new OdaException("Illegal sequence; cannot getResultSet before having executed."); //$NON-NLS-1$
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getResultSet(java.lang.
	 * String)
	 */
	@Override
	public IResultSet getResultSet(String resultSetName) throws OdaException {
		if (m_hasExecuted) {
			return new TestResultSetImpl(true, new TestResultSetMetaDataImpl());
		} else {
			throw new OdaException("Illegal sequence; cannot getResultSet(String) before having executed."); //$NON-NLS-1$
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getResultSetNames()
	 */
	@Override
	public String[] getResultSetNames() throws OdaException {
		return new String[] { "set1", "set2" }; //$NON-NLS-1$ //$NON-NLS-2$
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getRow(int)
	 */
	@Override
	public IParameterRowSet getRow(int parameterId) throws OdaException {
		// test driver does not support this
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getRow(java.lang.
	 * String)
	 */
	@Override
	public IParameterRowSet getRow(String parameterName) throws OdaException {
		// test driver does not support this
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getSortSpec(java.lang.
	 * String)
	 */
	@Override
	public SortSpec getSortSpec(String resultSetName) throws OdaException {
		// test driver does not support this
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getString(int)
	 */
	@Override
	public String getString(int parameterId) throws OdaException {
		validateOutputParamId(parameterId);

		if (m_currentTestCase == 1) {
			// return appContext object for parameter 1
			if (parameterId == 1 && getAppContext() != null) {
				return getAppContext().toString();
			}
			if (parameterId == 3) {
				return "parameter 3 value as a String"; //$NON-NLS-1$
			}
		}

		return null;
	}

	private IParameterMetaData validateParamId(int parameterId) throws OdaException {
		IParameterMetaData paramMD = getParameterMetaData();

		if (paramMD == null) {
			throw new OdaException("Problem with getting query's paramter meta-data."); //$NON-NLS-1$
		}
		if (parameterId > paramMD.getParameterCount()) {
			throw new OdaException("Given paramter id does not match parameter meta-data."); //$NON-NLS-1$
		}
		return paramMD;
	}

	/**
	 * @param parameterId
	 * @throws OdaException
	 */
	private void validateOutputParamId(int parameterId) throws OdaException {
		IParameterMetaData paramMD = validateParamId(parameterId);
		if (paramMD.getParameterMode(parameterId) == IParameterMetaData.parameterModeIn) {
			throw new OdaException("Given paramter id is not an output parameter."); //$NON-NLS-1$
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getString(java.lang.
	 * String)
	 */
	@Override
	public String getString(String parameterName) throws OdaException {
		if (m_currentTestCase == 1) {
			return parameterName + " value as a String"; //$NON-NLS-1$
		}

		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getTime(int)
	 */
	@Override
	public Time getTime(int parameterId) throws OdaException {
		// test driver does not support this
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getTime(java.lang.
	 * String)
	 */
	@Override
	public Time getTime(String parameterName) throws OdaException {
		// test driver does not support this
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getTimestamp(int)
	 */
	@Override
	public Timestamp getTimestamp(int parameterId) throws OdaException {
		// test driver does not support this
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IAdvancedQuery#getTimestamp(java.lang.
	 * String)
	 */
	@Override
	public Timestamp getTimestamp(String parameterName) throws OdaException {
		// test driver does not support this
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#setNewRow(int)
	 */
	@Override
	public IParameterRowSet setNewRow(int parameterId) throws OdaException {
		// test driver does not support this
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IAdvancedQuery#setNewRow(java.lang.
	 * String)
	 */
	@Override
	public IParameterRowSet setNewRow(String parameterName) throws OdaException {
		// test driver does not support this
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#setNewRowSet(int)
	 */
	@Override
	public IParameterRowSet setNewRowSet(int parameterId) throws OdaException {
		// test driver does not support this
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IAdvancedQuery#setNewRowSet(java.lang.
	 * String)
	 */
	@Override
	public IParameterRowSet setNewRowSet(String parameterName) throws OdaException {
		// test driver does not support this
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IAdvancedQuery#setSortSpec(java.lang.
	 * String, org.eclipse.datatools.connectivity.oda.SortSpec)
	 */
	@Override
	public void setSortSpec(String resultSetName, SortSpec sortBy) throws OdaException {
		// test driver does not support this
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.oda.IAdvancedQuery#wasNull()
	 */
	@Override
	public boolean wasNull() throws OdaException {
		// use whatever value was obtained, which could be null
		return false;
	}

	@Override
	public Object getObject(String parameterName) throws OdaException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getObject(int parameterId) throws OdaException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void cancel() throws OdaException, UnsupportedOperationException {
		// TODO Auto-generated method stub

	}

	@Override
	public String getEffectiveQueryText() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QuerySpecification getSpecification() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setObject(String parameterName, Object value) throws OdaException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setObject(int parameterId, Object value) throws OdaException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSpecification(QuerySpecification querySpec) throws OdaException, UnsupportedOperationException {
		// TODO Auto-generated method stub

	}
}
