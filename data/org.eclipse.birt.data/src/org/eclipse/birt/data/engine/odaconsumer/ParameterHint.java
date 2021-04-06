/*
 *****************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.logging.Level;

import org.eclipse.birt.data.engine.i18n.DataResourceHandle;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.datatools.connectivity.oda.IBlob;
import org.eclipse.datatools.connectivity.oda.IClob;

/**
 * <code>ParameterHint</code> provides hints to map static parameter definitions
 * to runtime parameters.
 */
public class ParameterHint {
	private String m_name;
	private String m_nativeName;
	private int m_position;
	private Class m_dataType;
	private int m_nativeDataType = UNKNOWN_NATIVE_TYPE;
	private boolean m_isInputOptional;
	private boolean m_isMultiInputValuesAllowed = false;
	private Object m_defaultInputValue;
	private boolean m_isInputMode;
	private boolean m_isOutputMode;
	private boolean m_isNullable;

	private static final int UNKNOWN_NATIVE_TYPE = 0;

	// trace logging variables
	private static String sm_className = ParameterHint.class.getName();
	private static String sm_loggerName = ConnectionManager.sm_packageName;
	private static LogHelper sm_logger = LogHelper.getInstance(sm_loggerName);

	/**
	 * Constructs a <code>ParameterHint</code> with the specified name.
	 * 
	 * @param parameterName the parameter name.
	 * @param isInputMode   whether this is an input parameter.
	 * @param isOutputMode  whether this is an output parameter.
	 * @throws IllegalArgumentException if the parameter name is null or empty.
	 */
	public ParameterHint(String parameterName, boolean isInputMode, boolean isOutputMode) {
		final String methodName = "ParameterHint( String, boolean, boolean )"; //$NON-NLS-1$
		if (sm_logger.isLoggingEnterExitLevel())
			sm_logger.entering(sm_className, methodName,
					new Object[] { parameterName, Boolean.valueOf(isInputMode), Boolean.valueOf(isOutputMode) });

		if (parameterName == null || parameterName.length() == 0) {
			String localizedMessage = DataResourceHandle.getInstance()
					.getMessage(ResourceConstants.PARAMETER_NAME_CANNOT_BE_EMPTY_OR_NULL);

			sm_logger.logp(Level.SEVERE, sm_className, methodName, "The given parameter is null or empty."); //$NON-NLS-1$
			throw new IllegalArgumentException(localizedMessage);
		}

		m_name = parameterName;
		m_isInputMode = isInputMode;
		m_isOutputMode = isOutputMode;

		m_isInputOptional = true;
		m_isNullable = true;

		sm_logger.exiting(sm_className, methodName, this);
	}

	/**
	 * Returns the parameter name for this parameter hint.
	 * 
	 * @return the name of the parameter.
	 */
	public String getName() {
		return m_name;
	}

	/**
	 * Sets the parameter 1-based position for this parameter hint.
	 * 
	 * @param position the 1-based position of the parameter.
	 * @throws IllegalArgumentException if the parameter position is less than 1.
	 */
	public void setPosition(int position) {
		final String methodName = "setPosition( int )"; //$NON-NLS-1$

		if (position < 1) {
			String localizedMessage = DataResourceHandle.getInstance()
					.getMessage(ResourceConstants.PARAMETER_POSITION_CANNOT_BE_LESS_THAN_ONE);
			sm_logger.logp(Level.SEVERE, sm_className, methodName, "Invalid parameter position {0} ", //$NON-NLS-1$
					Integer.valueOf(position));
			throw new IllegalArgumentException(localizedMessage);
		}

		m_position = position;
	}

	/**
	 * Returns the parameter 1-based position for this parameter hint.
	 * 
	 * @return the 1-based position of the parameter; 0 if no position was
	 *         specified.
	 */
	public int getPosition() {
		return m_position;
	}

	/**
	 * Sets the data type for this parameter hint.
	 * 
	 * @param dataType the data type of the parameter.
	 * @throws IllegalArgumentException if the parameter data type is invalid
	 */
	public void setDataType(Class dataType) {
		final String methodName = "setDataType( Class )"; //$NON-NLS-1$

		// validate given data type;
		// data type for a hint may be null
		boolean isValid = false;
		if (dataType == null || dataType == Integer.class || dataType == Double.class || dataType == String.class
				|| dataType == BigDecimal.class || dataType == java.util.Date.class || dataType == java.sql.Date.class
				|| dataType == Time.class || dataType == Timestamp.class || dataType == IBlob.class
				|| dataType == IClob.class || dataType == Boolean.class || dataType == Object.class) {
			isValid = true;
		}

		// input parameter does not support Blob/Clob data type
		if (isValid && isInputMode() && dataType != null) {
			if (dataType == IBlob.class || dataType == IClob.class) {
				isValid = false;
			}
		}

		if (isValid == false) {
			String localizedMessage = DataResourceHandle.getInstance()
					.getMessage(ResourceConstants.UNSUPPORTED_PARAMETER_VALUE_TYPE, new Object[] { dataType });
			sm_logger.logp(Level.SEVERE, sm_className, methodName, "Invalid parameter data type {0}.", dataType); //$NON-NLS-1$
			throw new IllegalArgumentException(localizedMessage);
		}

		m_dataType = dataType;
	}

	/**
	 * Returns the parameter ODI data type specified in this parameter hint. Note
	 * that this may not be the most effective data type to use.
	 * 
	 * @see #getEffectiveDataType(String, String)
	 * @return the data type of the parameter.
	 */
	public Class getDataType() {
		return m_dataType;
	}

	/**
	 * Sets the native data type for this parameter hint.
	 * 
	 * @param typeCode the native data type of the parameter.
	 */
	public void setNativeDataType(int typeCode) {
		m_nativeDataType = typeCode;
	}

	/**
	 * Returns the native data type in this parameter hint. The native data type
	 * code value is implementation-specific, and collected at design time. Default
	 * value is 0 for none or unknown value.
	 * 
	 * @return the native data type of the parameter.
	 */
	public int getNativeDataType() {
		return m_nativeDataType;
	}

	/**
	 * Sets whether the input parameter is optional. Has no effect on non-input
	 * parameters.
	 * 
	 * @param isInputOptional whether this input parameter is optional.
	 */
	public void setIsInputOptional(boolean isInputOptional) {
		if (m_isInputMode)
			m_isInputOptional = isInputOptional;
	}

	/**
	 * Returns whether the input parameter is optional. Returns true for non-input
	 * parameters.
	 * 
	 * @return true if the input parameter is optional or if this is a non-input
	 *         parameter, false otherwise.
	 */
	public boolean isInputOptional() {
		return (m_isInputMode) ? m_isInputOptional : true;
	}

	/**
	 * Sets whether the parameter can be null.
	 * 
	 * @param isNullable whether this parameter can be null.
	 */
	public void setIsNullable(boolean isNullable) {
		m_isNullable = isNullable;
	}

	/**
	 * Returns whether the parameter can be null.
	 * 
	 * @return true if the parameter can be null, false otherwise.
	 */
	public boolean isNullable() {
		return m_isNullable;
	}

	/**
	 * Sets the default value of the input parameter. Has no effect on non-input
	 * parameters.
	 * 
	 * @param defaultInputValue the default value.
	 */
	public void setDefaultInputValue(Object defaultInputValue) {
		if (m_isInputMode)
			m_defaultInputValue = defaultInputValue;
	}

	/**
	 * Gets the default vlaue of the input parameter. Returns null for non-input
	 * parameters.
	 * 
	 * @return the default value of the input parameter, or null for non-input
	 *         parameters.
	 */
	public Object getDefaultInputValue() {
		return (m_isInputMode) ? m_defaultInputValue : null;
	}

	/**
	 * Returns whether the parameter is an input parameter.
	 * 
	 * @return true if the parameter is an input parameter, false otherwise.
	 */
	public boolean isInputMode() {
		return m_isInputMode;
	}

	/**
	 * Returns whether the parameter is an output parameter.
	 * 
	 * @return true if the parameter is an output parameter, false otherwise.
	 */
	public boolean isOutputMode() {
		return m_isOutputMode;
	}

	/**
	 * Sets the native name of the parameter as known to the underlying data source.
	 * 
	 * @param nativeName The native name to set; may be a null value if the name is
	 *                   not available or this parameter is not named.
	 */
	public void setNativeName(String nativeName) {
		m_nativeName = nativeName;
	}

	/**
	 * Returns the native name of the parameter as known to the underlying data
	 * source.
	 * 
	 * @return the parameter native name, or null if the name is not available or
	 *         this parameter is not named.
	 */
	public String getNativeName() {
		return m_nativeName;
	}

	/**
	 * Sets hether this parameter allows binding with multiple input values. Has no
	 * effect on non-input parameters.
	 * 
	 * @param isMultiInputValuesAllowed true if multiple input values are allowed;
	 *                                  false if at most a single input value is
	 *                                  allowed.
	 */
	public void setMultiInputValuesAllowed(boolean isMultiInputValuesAllowed) {
		if (m_isInputMode)
			m_isMultiInputValuesAllowed = isMultiInputValuesAllowed;
	}

	/**
	 * Indicates whether this parameter allows binding with multiple input values.
	 * Only applies to input parameters.
	 * 
	 * @return true if multiple input values are allowed; false if at most a single
	 *         input value is allowed.
	 */
	public boolean isMultiInputValuesAllowed() {
		return (m_isInputMode) ? m_isMultiInputValuesAllowed : false;
	}

	/**
	 * Helper method to update this <code>ParameterHint</code> with information from
	 * another <code>ParameterHint</code>.
	 * 
	 * @param hint the <code>ParameterHint</code> instance.
	 */
	void updateHint(ParameterHint hint) {
		final String methodName = "updateHint( ParameterHint )"; //$NON-NLS-1$
		sm_logger.entering(sm_className, methodName, hint);

		m_name = hint.m_name;

		// don't update if the specified hint has default values

		if (hint.m_nativeName != null)
			m_nativeName = hint.m_nativeName;

		if (hint.m_position != 0)
			m_position = hint.m_position;

		if (hint.m_dataType != null)
			m_dataType = hint.m_dataType;

		if (hint.m_nativeDataType != UNKNOWN_NATIVE_TYPE)
			m_nativeDataType = hint.m_nativeDataType;

		m_isInputOptional = hint.m_isInputOptional;
		m_defaultInputValue = hint.m_defaultInputValue;
		m_isInputMode = hint.m_isInputMode;
		m_isOutputMode = hint.m_isOutputMode;
		m_isNullable = hint.m_isNullable;

		sm_logger.exiting(sm_className, methodName, this);
	}

	/**
	 * Returns the most effective ODI data type defined in the hint. It determines
	 * the best type to use based on the native data type defined.
	 * 
	 * @param odaDataSourceId underlying ODA driver's data source id that defines
	 *                        the native data type mappings
	 * @param dataSetType     type of data set; may be null if the oda data source
	 *                        has only one type of data set
	 * @return the most effective ODI data type to use
	 */
	public Class getEffectiveDataType(String odaDataSourceId, String dataSetType) {
		return DataTypeUtil.toTypeClass(getEffectiveOdaType(odaDataSourceId, dataSetType));
	}

	int getEffectiveOdaType(String odaDataSourceId, String dataSetType) {
		/*
		 * The BIRT DtE ODI data type specified in a parameter hint may be based on what
		 * an user manually entered in BIRT host designer, and was not necessarily
		 * validated against an underlying ODA driver. Whereas, the native data type in
		 * hint, if available, is provided by a custom ODA designer. It is thus a more
		 * reliable type to use in a retry. So we first try to use the native data type
		 * for the effective ODA data type.
		 */
		if (getNativeDataType() != UNKNOWN_NATIVE_TYPE) {
			int odaType = DataTypeUtil.toOdaType(getNativeDataType(), odaDataSourceId, dataSetType);
			if (odaType != Types.NULL)
				return odaType; // found valid native to oda type mapping
		}

		// no native data type mapping info, use the BIRT DtE ODI API data type instead
		Class typeInHint = getDataType();
		return DataTypeUtil.toOdaType(typeInHint);
	}

}
