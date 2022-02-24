/*
 *****************************************************************************
 * Copyright (c) 2004, 2010 Actuate Corporation.
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
 ******************************************************************************
 */

package org.eclipse.birt.data.engine.odaconsumer;

import java.sql.Types;
import java.util.logging.Level;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.datatools.connectivity.oda.IResultSetMetaData;
import org.eclipse.datatools.connectivity.oda.OdaException;

/**
 * ResultSetMetaData contains the result set metadata retrieved during runtime.
 */
public class ResultSetMetaData extends ExceptionHandler {
	private IResultSetMetaData m_metadata;
	private String m_driverName;
	private String m_dataSetType;

	// trace logging variables
	private static final String sm_className = ResultSetMetaData.class.getName();

	ResultSetMetaData(IResultSetMetaData metadata, String driverName, String dataSetType) {
		super(sm_className);
		final String methodName = "ResultSetMetaData"; //$NON-NLS-1$
		if (getLogger().isLoggingEnterExitLevel())
			getLogger().entering(sm_className, methodName, new Object[] { metadata, driverName, dataSetType });

		m_metadata = metadata;
		m_driverName = driverName;
		m_dataSetType = dataSetType;

		getLogger().exiting(sm_className, methodName, this);
	}

	/**
	 * Returns the number of columns in the corresponding result set.
	 * 
	 * @return the number of columns in the result set.
	 * @throws DataException if data source error occurs.
	 */
	public int getColumnCount() throws DataException {
		final String methodName = "getColumnCount"; //$NON-NLS-1$
		try {
			if (m_metadata == null)
				return 0;
			return m_metadata.getColumnCount();
		} catch (OdaException ex) {
			throwException(ex, ResourceConstants.CANNOT_GET_COLUMN_COUNT, methodName);
		} catch (UnsupportedOperationException ex) {
			throwException(ex, ResourceConstants.CANNOT_GET_COLUMN_COUNT, methodName);
		}
		return 0;
	}

	/**
	 * Returns the column name at the specified column index.
	 * 
	 * @param index the column index.
	 * @return the column name at the specified column index.
	 * @throws DataException if data source error occurs.
	 */
	public String getColumnName(int index) throws DataException {
		final String methodName = "getColumnName"; //$NON-NLS-1$

		verifyHasRuntimeMetaData(methodName);
		try {
			return m_metadata.getColumnName(index);
		} catch (OdaException ex) {
			throwException(ex, ResourceConstants.CANNOT_GET_COLUMN_NAME, index, methodName);
		} catch (UnsupportedOperationException ex) {
			getLogger().logp(Level.WARNING, sm_className, methodName, "Cannot get column name.", ex); //$NON-NLS-1$
		}
		return EMPTY_STRING;
	}

	/**
	 * Returns the column label at the specified column index.
	 * 
	 * @param index the column index.
	 * @return the column label at the specified column index.
	 * @throws DataException if data source error occurs.
	 */
	public String getColumnLabel(int index) throws DataException {
		final String methodName = "getColumnLabel"; //$NON-NLS-1$

		verifyHasRuntimeMetaData(methodName);
		try {
			return m_metadata.getColumnLabel(index);
		} catch (OdaException ex) {
			throwException(ex, ResourceConstants.CANNOT_GET_COLUMN_LABEL, index, methodName);
		} catch (UnsupportedOperationException ex) {
			getLogger().logp(Level.INFO, sm_className, methodName, "Cannot get column label.", ex); //$NON-NLS-1$
		}
		return EMPTY_STRING;
	}

	/**
	 * Returns the ODA type at the specified column index.
	 * 
	 * @param index the column index.
	 * @return the ODA type, in <code>java.sql.Types</code> value, at the specified
	 *         column index; or Types.NULL if runtime data type is unknown
	 * @throws DataException if data source error occurs.
	 */
	public int getColumnType(int index) throws DataException {
		final String methodName = "getColumnType"; //$NON-NLS-1$

		int nativeType = doGetNativeColumnType(index);

		// if the native type of the column is unknown (Types.NULL) at runtime,
		// we can't simply default to the ODA character type because we may
		// have a design hint that could provide the type
		int odaType = (nativeType == Types.NULL) ? Types.NULL
				: DataTypeUtil.toOdaType(nativeType, m_driverName, m_dataSetType);

		if (getLogger().isLoggable(Level.FINEST))
			getLogger().logp(Level.FINEST, sm_className, methodName, "Column at index {0} has ODA data type {1}.", //$NON-NLS-1$
					new Object[] { Integer.valueOf(index), Integer.valueOf(odaType) });

		return odaType;
	}

	/**
	 * Returns the native type name at the specified column index.
	 * 
	 * @param index the column index.
	 * @return the native type name.
	 * @throws DataException if data source error occurs.
	 */
	public String getColumnNativeTypeName(int index) throws DataException {
		final String methodName = "getColumnNativeTypeName"; //$NON-NLS-1$

		verifyHasRuntimeMetaData(methodName);
		try {
			return m_metadata.getColumnTypeName(index);
		} catch (OdaException ex) {
			throwException(ex, ResourceConstants.CANNOT_GET_COLUMN_NATIVE_TYPE_NAME, index, methodName);
		} catch (UnsupportedOperationException ex) {
			getLogger().logp(Level.WARNING, sm_className, methodName, "Cannot get column native type name.", ex); //$NON-NLS-1$
		}
		return EMPTY_STRING;
	}

	private int doGetNativeColumnType(int index) throws DataException {
		final String methodName = "doGetNativeColumnType"; //$NON-NLS-1$

		verifyHasRuntimeMetaData(methodName);
		try {
			return m_metadata.getColumnType(index);
		} catch (OdaException ex) {
			throwException(ex, ResourceConstants.CANNOT_GET_COLUMN_TYPE, index, methodName);
		} catch (UnsupportedOperationException ex) {
			throwException(ex, ResourceConstants.CANNOT_GET_COLUMN_TYPE, index, methodName);
		}
		return Types.NULL;
	}

	Class getColumnTypeAsJavaClass(int index) throws DataException {
		int odaType = getColumnType(index);
		return DataTypeUtil.toTypeClass(odaType);
	}

	private void verifyHasRuntimeMetaData(String methodName) throws DataException {
		if (m_metadata == null)
			throwError(ResourceConstants.CANNOT_GET_RESULTSET_METADATA, null, methodName);
	}

	String getOdaDataSourceId() {
		return m_driverName;
	}

	String getDataSetType() {
		return m_dataSetType;
	}

}
