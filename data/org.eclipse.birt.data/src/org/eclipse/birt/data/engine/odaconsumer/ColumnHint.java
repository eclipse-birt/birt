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

import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.logging.Level;

import org.eclipse.birt.data.engine.i18n.DataResourceHandle;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.datatools.connectivity.oda.IBlob;
import org.eclipse.datatools.connectivity.oda.IClob;

/**
 * <code>ColumnHint</code> provides hints to merge static design time columns
 * with runtime result set column metadata.
 */
public class ColumnHint {
	private String m_name;
	private int m_position;
	private Class m_dataType;
	private int m_nativeDataType = UNKNOWN_NATIVE_TYPE;
	private String m_alias;

	private static final int UNKNOWN_NATIVE_TYPE = 0;

	// trace logging variables
	private static String sm_className = ColumnHint.class.getName();
	private static String sm_loggerName = ConnectionManager.sm_packageName;
	private static LogHelper sm_logger = LogHelper.getInstance(sm_loggerName);

	/**
	 * Constructs a <code>ColumnHint</code> with the specified column name.
	 *
	 * @param columnName the column name of this <code>ColumnHint</code>.
	 * @throws IllegalArgumentException if <code>columnName</code> is null or empty.
	 */
	public ColumnHint(String columnName) {
		final String methodName = "ColumnHint(String)"; //$NON-NLS-1$
		sm_logger.entering(sm_className, methodName, columnName);

		if (columnName == null || columnName.length() == 0) {
			String localizedMessage = DataResourceHandle.getInstance()
					.getMessage(ResourceConstants.COLUMN_NAME_CANNOT_BE_EMPTY_OR_NULL);
			throw new IllegalArgumentException(localizedMessage);
		}

		m_name = columnName;

		sm_logger.exiting(sm_className, methodName, this);
	}

	/**
	 * Returns the column name for this <code>ColumnHint</code>.
	 *
	 * @return the name of the column.
	 */
	public String getName() {
		return m_name;
	}

	/**
	 * Sets the 1-based column position for this <code>ColumnHint</code>.
	 *
	 * @param position the 1-based column position.
	 * @throws IllegalArgumentException if the column position is less than 1.
	 */
	public void setPosition(int position) {
		final String methodName = "setPosition(int)"; //$NON-NLS-1$

		if (position < 1) {
			String localizedMessage = DataResourceHandle.getInstance()
					.getMessage(ResourceConstants.COLUMN_POSITION_CANNOT_BE_LESS_THAN_ONE);
			RuntimeException ex = new IllegalArgumentException(localizedMessage);

			if (sm_logger.isLoggable(Level.SEVERE)) {
				sm_logger.logp(Level.SEVERE, sm_className, methodName, "Invalid column position {0}.", //$NON-NLS-1$
						Integer.valueOf(position));
			}
			throw ex;
		}

		m_position = position;
	}

	/**
	 * Returns the 1-based column position for this <code>ColumnHint</code>.
	 *
	 * @return the 1-based column position; 0 if no position was not specified.
	 */
	public int getPosition() {
		return m_position;
	}

	/**
	 * Sets the data type for this <code>ColumnHint</code>.
	 *
	 * @param dataType the data type.
	 */
	public void setDataType(Class dataType) {
		// data type for a hint may be null
		assert (dataType == null || dataType == Integer.class || dataType == Double.class || dataType == String.class
				|| dataType == BigDecimal.class || dataType == java.util.Date.class || dataType == java.sql.Date.class
				|| dataType == Time.class || dataType == Timestamp.class || dataType == Blob.class
				|| dataType == IBlob.class || dataType == Clob.class || dataType == IClob.class
				|| dataType == Boolean.class || dataType == Object.class);

		m_dataType = dataType;
	}

	/**
	 * Returns the column data type specified in this <code>ColumnHint</code>. Note
	 * that this may not be the most effective data type to use.
	 *
	 * @see #getEffectiveDataType(String, String)
	 * @return the column data type.
	 */
	public Class getDataType() {
		return m_dataType;
	}

	/**
	 * Sets the native data type for this <code>ColumnHint</code>.
	 *
	 * @param typeCode the native data type of the column.
	 */
	public void setNativeDataType(int typeCode) {
		m_nativeDataType = typeCode;
	}

	/**
	 * Returns the native data type for this <code>ColumnHint</code>. The native
	 * data type code value is implementation-specific, and collected at design
	 * time. Default value is 0 for none or unknown value.
	 *
	 * @return the native data type of the column.
	 */
	public int getNativeDataType() {
		return m_nativeDataType;
	}

	/**
	 * Sets the column alias for this <code>ColumnHint</code>.
	 *
	 * @param alias the column alias.
	 */
	public void setAlias(String alias) {
		final String methodName = "setAlias(String)"; //$NON-NLS-1$

		// ok to set the alias as null, meaning no alias, but
		// not ok to have an empty alias
		if (alias != null && alias.length() == 0) {
			String localizedMessage = DataResourceHandle.getInstance()
					.getMessage(ResourceConstants.COLUMN_ALIAS_CANNOT_BE_EMPTY);
			RuntimeException ex = new IllegalArgumentException(localizedMessage);

			if (sm_logger.isLoggable(Level.SEVERE)) {
				sm_logger.logp(Level.SEVERE, sm_className, methodName,
						"The alias is empty; must be either null or non-empty value."); //$NON-NLS-1$
			}
			throw ex;
		}

		m_alias = alias;
	}

	/**
	 * Returns the column alias for this <code>ColumnHint</code>.
	 *
	 * @return the column alias.
	 */
	public String getAlias() {
		return m_alias;
	}

	/**
	 * Returns the most effective ODI data type defined in this hint. It determines
	 * the best type to use based on the native data type defined.
	 *
	 * @param odaDataSourceId underlying ODA driver's data source id that defines
	 *                        the native data type mappings
	 * @param dataSetType     type of data set; may be null if the ODA data source
	 *                        has only one type of data set
	 * @return the most effective ODI data type to use
	 */
	public Class getEffectiveDataType(String odaDataSourceId, String dataSetType) {
		/*
		 * The BIRT DtE ODI data type specified in a column hint may be based on
		 * reversed mapping from a DtE API type. Since multiple ODI types may be mapped
		 * to the same DtE API type, the reverse mapped value may not be the original
		 * one. Using the native data type, if available, to determine the effective ODI
		 * type is thus more reliable. So we first try to use the native data type for
		 * the effective ODI type.
		 */
		if (getNativeDataType() != UNKNOWN_NATIVE_TYPE) {
			Class effectiveType = DataTypeUtil
					.toTypeClass(DataTypeUtil.toOdaType(getNativeDataType(), odaDataSourceId, dataSetType));
			if (effectiveType != null) {
				return effectiveType; // found valid native to odi type mapping
			}
		}

		// no native data type mapping info, use the BIRT DtE ODI API data type instead
		return getDataType();
	}

}
