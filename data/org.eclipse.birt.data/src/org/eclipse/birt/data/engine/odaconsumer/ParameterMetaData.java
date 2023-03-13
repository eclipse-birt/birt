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
import org.eclipse.datatools.connectivity.oda.IParameterMetaData;
import org.eclipse.datatools.connectivity.oda.OdaException;

/**
 * Contains the effective metadata information of a single parameter. Metadata
 * is obtained from ODA driver's runtime metadata, if available, and
 * supplemented with design hints.
 */
public class ParameterMetaData extends ExceptionHandler {
	private int m_position = -1;
	private String m_name; // ROM data set parameter name
	private String m_nativeName; // ODA query parameter name
	private int m_dataType = Types.NULL; // ODA data type
	private String m_nativeTypeName;
	private Object m_defaultValue;
	private Boolean m_isOptional;
	private Boolean m_isInput;
	private Boolean m_isOutput;
	private int m_scale = -1;
	private int m_precision = -1;
	private Boolean m_isNullable;

	// trace logging variables
	private static String sm_className = ParameterMetaData.class.getName();

	/**
	 * Instantiate a parameter metadata based on design-time metadata specified in a
	 * parameter hint.
	 */
	ParameterMetaData(ParameterHint paramHint, String odaDataSourceId, String dataSetType) {
		super(sm_className);
		final String methodName = "ParameterMetaData( ParameterHint, String, String )"; //$NON-NLS-1$
		getLogger().entering(sm_className, methodName, paramHint);

		m_name = paramHint.getName();
		m_nativeName = paramHint.getNativeName();

		int position = paramHint.getPosition();
		m_position = (position > 0) ? position : -1;

		m_dataType = paramHint.getEffectiveOdaType(odaDataSourceId, dataSetType);

		m_isInput = paramHint.isInputMode();
		m_isOutput = paramHint.isOutputMode();

		m_isOptional = paramHint.isInputOptional();
		// TODO - paramHint.isMultiInputValuesAllowed();

		m_defaultValue = paramHint.getDefaultInputValue();
		m_isNullable = paramHint.isNullable();

		getLogger().exiting(sm_className, methodName, this);
	}

	/**
	 * Instantiate a parameter metadata based on runtime metadata of the specified
	 * oda data source and data set type.
	 *
	 * @throws DataException
	 */
	ParameterMetaData(IParameterMetaData parameterMetaData, int index, String odaDataSourceId, String dataSetType)
			throws DataException {
		super(sm_className);
		final String methodName = "ParameterMetaData( IParameterMetaData, int, String, String )"; //$NON-NLS-1$
		if (getLogger().isLoggingEnterExitLevel()) {
			getLogger().entering(sm_className, methodName,
					new Object[] { parameterMetaData, Integer.valueOf(index), odaDataSourceId, dataSetType });
		}
		m_position = index;
		m_nativeName = getRuntimeParameterName(parameterMetaData, index);

		int nativeType = getRuntimeParameterType(parameterMetaData, index);

		// if the native type of the parameter is unknown (Types.NULL) at runtime,
		// we can't simply default to the ODA character type because we may
		// have a design hint that could provide the type
		if (nativeType != Types.NULL) {
			m_dataType = DataTypeUtil.toOdaType(nativeType, odaDataSourceId, dataSetType);
		}

		m_nativeTypeName = getRuntimeParamTypeName(parameterMetaData, index);

		int mode = getRuntimeParameterMode(parameterMetaData, index);
		if (mode == IParameterMetaData.parameterModeIn) {
			m_isInput = Boolean.TRUE;
			m_isOutput = Boolean.FALSE;
		} else if (mode == IParameterMetaData.parameterModeOut) {
			m_isInput = Boolean.FALSE;
			m_isOutput = Boolean.TRUE;
		} else if (mode == IParameterMetaData.parameterModeInOut) {
			m_isInput = Boolean.TRUE;
			m_isOutput = Boolean.TRUE;
		}

		m_scale = getRuntimeParameterScale(parameterMetaData, index);
		m_precision = getRuntimeParameterPrecision(parameterMetaData, index);

		int isNullable = getRuntimeIsNullable(parameterMetaData, index);
		if (isNullable == IParameterMetaData.parameterNullable) {
			m_isNullable = Boolean.TRUE;
		} else if (isNullable == IParameterMetaData.parameterNoNulls) {
			m_isNullable = Boolean.FALSE;
			m_isOptional = Boolean.FALSE;
		}

		getLogger().exiting(sm_className, methodName, this);
	}

	/**
	 * This method is meant to update the runtime parameter metadata with static
	 * design-time metadata specified in a parameter hint.
	 *
	 * @throws DataException if data source error occurs.
	 */
	void updateWith(ParameterHint paramHint, String odaDataSourceId, String dataSetType) throws DataException {
		final String methodName = "updateWith( ParameterHint, String, String )"; //$NON-NLS-1$
		getLogger().entering(sm_className, methodName, paramHint);

		// Based on previous experience, runtime parameter metadata can often
		// be incorrect about the parameter's input and output modes. Therefore,
		// we want to use the static parameter hints provided by the user to override
		// those specified by the runtime.
		m_isInput = paramHint.isInputMode();
		m_isOutput = paramHint.isOutputMode();

		// check that the position in the parameter hint either has not been
		// set or this parameter metadata (from a hint) has not been set, or
		// it's the same as the current parameter metadata.
		// note that if this parameter metadata came from the runtime parameter
		// metadata, then it will have a valid position index.
		int position = paramHint.getPosition();
		assert (position <= 0 || m_position <= 0 || position == m_position);

		// if this parameter metadata (from runtime metadata or a previous hint) doesn't
		// have the
		// position set and the new hint has a valid position, then update
		if (m_position <= 0 && position > 0) {
			m_position = position;
		}

		String name = paramHint.getName();
		assert (name != null && name.length() > 0);

		// if the name is already set, then ensure they're the same name
		assert (m_name == null || m_name.equals(name));

		if (m_name == null) {
			m_name = name;
		}

		// if the parameter native name was previously unknown, then use the value from
		// the specified hint
		if (m_nativeName == null) {
			m_nativeName = paramHint.getNativeName();
		}

		// if the parameter type was previously unknown, then use the type from
		// the hint if present or default to the character type
		if (m_dataType == Types.NULL) {
			m_dataType = paramHint.getEffectiveOdaType(odaDataSourceId, dataSetType);
		}

		if (m_isOptional == null) { // was unknown
			m_isOptional = paramHint.isInputOptional();
		}

		// TODO - paramHint.isMultiInputValuesAllowed();

		if (m_isNullable == null) { // was unknown
			m_isNullable = paramHint.isNullable();
		}

		m_defaultValue = paramHint.getDefaultInputValue();

		getLogger().exiting(sm_className, methodName, this);
	}

	private String getRuntimeParameterName(IParameterMetaData parameterMetaData, int index) throws DataException {
		final String methodName = "getRuntimeParameterName( IParameterMetaData, int )"; //$NON-NLS-1$
		try {
			return parameterMetaData.getParameterName(index);
		} catch (OdaException ex) {
			throwException(ex, ResourceConstants.CANNOT_GET_PARAMETER_NAME, index, methodName);
		} catch (UnsupportedOperationException ex) {
			getLogger().logp(Level.INFO, sm_className, methodName, "Unsupported driver-defined parameter name.", ex); //$NON-NLS-1$
		}
		return null;
	}

	private int getRuntimeParameterType(IParameterMetaData parameterMetaData, int index) throws DataException {
		final String methodName = "getRuntimeParameterType"; //$NON-NLS-1$
		try {
			return parameterMetaData.getParameterType(index);
		} catch (OdaException | UnsupportedOperationException ex) {
			// minimum required parameter metadata is not available
			throwException(ex, ResourceConstants.CANNOT_GET_PARAMETER_TYPE, index, methodName);
		}
		return Types.NULL; // unknown
	}

	private String getRuntimeParamTypeName(IParameterMetaData parameterMetaData, int index) throws DataException {
		final String methodName = "getRuntimeParamTypeName"; //$NON-NLS-1$
		try {
			return parameterMetaData.getParameterTypeName(index);
		} catch (OdaException ex) {
			throwException(ex, ResourceConstants.CANNOT_GET_PARAMETER_TYPE_NAME, index, methodName);
		} catch (UnsupportedOperationException ex) {
			getLogger().logp(Level.INFO, sm_className, methodName, "Cannot get parameter type name.", ex); //$NON-NLS-1$
		}
		return EMPTY_STRING;
	}

	private int getRuntimeParameterMode(IParameterMetaData parameterMetaData, int index) throws DataException {
		final String methodName = "getRuntimeParameterMode"; //$NON-NLS-1$
		try {
			return parameterMetaData.getParameterMode(index);
		} catch (OdaException ex) {
			throwException(ex, ResourceConstants.CANNOT_GET_PARAMETER_MODE, index, methodName);
		} catch (UnsupportedOperationException ex) {
			getLogger().logp(Level.WARNING, sm_className, methodName, "Cannot get parameter mode.", ex); //$NON-NLS-1$
		}
		return IParameterMetaData.parameterModeUnknown;
	}

	private int getRuntimeParameterScale(IParameterMetaData parameterMetaData, int index) throws DataException {
		final String methodName = "getRuntimeParameterScale"; //$NON-NLS-1$
		try {
			return parameterMetaData.getScale(index);
		} catch (OdaException ex) {
			throwException(ex, ResourceConstants.CANNOT_GET_PARAMETER_SCALE, index, methodName);
		} catch (UnsupportedOperationException ex) {
			getLogger().logp(Level.INFO, sm_className, methodName, "Cannot get parameter scale.", ex); //$NON-NLS-1$
		}
		return -1;
	}

	private int getRuntimeParameterPrecision(IParameterMetaData parameterMetaData, int index) throws DataException {
		final String methodName = "getRuntimeParameterPrecision"; //$NON-NLS-1$
		try {
			return parameterMetaData.getPrecision(index);
		} catch (OdaException ex) {
			throwException(ex, ResourceConstants.CANNOT_GET_PARAMETER_PRECISION, index, methodName);
		} catch (UnsupportedOperationException ex) {
			getLogger().logp(Level.INFO, sm_className, methodName, "Cannot get parameter precision.", ex); //$NON-NLS-1$
		}
		return -1;
	}

	private int getRuntimeIsNullable(IParameterMetaData parameterMetaData, int index) throws DataException {
		final String methodName = "getRuntimeIsNullable"; //$NON-NLS-1$
		try {
			return parameterMetaData.isNullable(index);
		} catch (OdaException ex) {
			throwException(ex, ResourceConstants.CANNOT_GET_PARAMETER_ISNULLABLE, index, methodName);
		} catch (UnsupportedOperationException ex) {
			getLogger().logp(Level.INFO, sm_className, methodName, "Cannot get parameter nullability.", ex); //$NON-NLS-1$
		}
		return IParameterMetaData.parameterNullableUnknown;
	}

	/**
	 * Returns the 1-based parameter index of this parameter.
	 *
	 * @return the 1-based parameter index of this parameter, or -1 if the index is
	 *         unspecified or unknown.
	 */
	public int getPosition() {
		return m_position;
	}

	/**
	 * Returns the parameter name of this parameter.
	 *
	 * @return the parameter name of this parameter, or null if the name is
	 *         unspecified or unknown.
	 */
	public String getName() {
		return m_name;
	}

	/**
	 * Returns the parameter's native name as known to the underlying ODA runtime
	 * driver.
	 *
	 * @return the parameter native name, or null if the name is not available or
	 *         this parameter is not named.
	 */
	public String getNativeName() {
		return m_nativeName;
	}

	/**
	 * Returns the ODA type code of this parameter.
	 *
	 * @return the ODA data type of this parameter.
	 */
	public int getDataType() {
		// if this is still an unknown parameter type, that means there were
		// no hints or the hints didn't provide a data type. so default to
		// the ODA character type
		return (m_dataType == Types.NULL) ? Types.CHAR : m_dataType;
	}

	/**
	 * Returns the data provider specific type name of this parameter.
	 *
	 * @return the native data type name, or null if the type name is unspecified or
	 *         unknown.
	 */
	public String getNativeTypeName() {
		return m_nativeTypeName;
	}

	/**
	 * Returns whether this parameter is optional.
	 *
	 * @return Boolean.TRUE if this parameter is optional. Boolean.FALSE if this
	 *         this parameter is not optional. Null if it is unspecified or unknown
	 *         whether this parameter is optional. The default value is null.
	 */
	public Boolean isOptional() {
		return m_isOptional;
	}

	/**
	 * Returns the default input value for this parameter.
	 *
	 * @return the default input value, or null if the default input value is
	 *         unspecified or unknown.
	 */
	public Object getDefaultValue() {
		return m_defaultValue;
	}

	/**
	 * Returns whether this parameter is an input parameter. A parameter can be of
	 * both input and output modes.
	 *
	 * @return Boolean.TRUE if this parameter is an input parameter. Boolean.FALSE
	 *         if this parameter is not an input parameter. Null if it is
	 *         unspecified or unknown whether this parameter is an input parameter.
	 *         The default value is null.
	 */
	public Boolean isInputMode() {
		return m_isInput;
	}

	/**
	 * Returns whether this parameter is an output parameter. A parameter can be of
	 * both input and output modes.
	 *
	 * @return Boolean.TRUE if this parameter is an output parameter. Boolean.FALSE
	 *         if this parameter is not an output parameter. Null if it is
	 *         unspecified or unknown whether this parameter is an output parameter.
	 *         The default value is null.
	 */
	public Boolean isOutputMode() {
		return m_isOutput;
	}

	/**
	 * Returns the maximum number of digits to the right of the decimal point for
	 * this parameter.
	 *
	 * @return the scale of the parameter, or -1 if the scale is unspecified or
	 *         unknown.
	 */
	public int getScale() {
		return m_scale;
	}

	/**
	 * Returns the maximum number of decimal digits for this parameter.
	 *
	 * @return the precision of the parameter, or -1 if the precision is unspecified
	 *         or unknown.
	 */
	public int getPrecision() {
		return m_precision;
	}

	/**
	 * Returns whether null values are allowed for this parameter.
	 *
	 * @return Boolean.TRUE if null is allowed for this parameter. Boolean.FALSE if
	 *         null is not allowed for this parameter. Null if it is unspecified or
	 *         unknown whether null is allowed for this parameter. The default value
	 *         is null.
	 */
	public Boolean isNullable() {
		return m_isNullable;
	}

}
