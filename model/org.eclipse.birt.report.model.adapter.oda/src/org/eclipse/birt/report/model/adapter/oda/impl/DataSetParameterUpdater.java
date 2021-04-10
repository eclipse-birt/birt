/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.adapter.oda.impl;

import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.OdaDataSetParameterHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.DataSetParameter;
import org.eclipse.birt.report.model.api.elements.structures.OdaDataSetParameter;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.datatools.connectivity.oda.design.DataElementAttributes;
import org.eclipse.datatools.connectivity.oda.design.ElementNullability;
import org.eclipse.datatools.connectivity.oda.design.InputElementAttributes;
import org.eclipse.datatools.connectivity.oda.design.ParameterDefinition;
import org.eclipse.datatools.connectivity.oda.design.ParameterMode;
import org.eclipse.datatools.connectivity.oda.design.StaticValues;

/**
 * Updates the specified oda data set parameter handle with the given data set
 * parameter design.
 */
class DataSetParameterUpdater {

	/**
	 * The data set handle defined parameters.
	 */

	private List<OdaDataSetParameterHandle> setDefinedParams = null;

	OdaDataSetParameter newParam = null;

	private String dataSourceId = null;
	private String dataSetId = null;

	protected ParameterDefinition paramDefn = null;

	private ModuleHandle module = null;

	private OdaDataSetHandle setHandle;

	/**
	 * @param setDesign
	 * @param cachedParameters
	 * @param setHandle
	 */

	DataSetParameterUpdater(OdaDataSetParameter param, ParameterDefinition paramDefn, OdaDataSetHandle setHandle,
			String dataSourceId, String dataSetId, List<OdaDataSetParameterHandle> setDefinedParams) {
		this.paramDefn = paramDefn;
		this.newParam = param;

		this.module = setHandle.getModuleHandle();
		this.setHandle = setHandle;

		this.dataSourceId = dataSourceId;
		this.dataSetId = dataSetId;
		this.setDefinedParams = setDefinedParams;
	}

	/**
	 * @param param
	 * @throws SemanticException
	 */

	void process() throws SemanticException {
		DataElementAttributes dataAttrs = paramDefn.getAttributes();
		processDataElementAttributes(dataAttrs);

		ParameterMode inOutMode = paramDefn.getInOutMode();
		processInOutMode(inOutMode);

		InputElementAttributes inputElementAttrs = paramDefn.getEditableInputElementAttributes();
		processInputElementAttributes(inputElementAttrs);

		// must visit data element attributes first since matching is done with
		// fields on the data attributes.

		processLinkedReportParameter();
	}

	/**
	 * 
	 * @param dataAttrs
	 */
	private void processDataElementAttributes(DataElementAttributes dataAttrs) {
		if (dataAttrs == null)
			return;

		String nativeName = dataAttrs.getName();

		// make sure the OdaDataSetParameter must have a name. This is a
		// requirement in ROM. If the name in the data set handle is obsolete,
		// also need to be updated.

		String name = newParam.getName();
		if (StringUtil.isBlank(name) || (!StringUtil.isBlank(nativeName) && !name.equalsIgnoreCase(nativeName))) {
			newParam.setName(nativeName);
		}

		newParam.setNativeName(nativeName);

		newParam.setPosition(Integer.valueOf(dataAttrs.getPosition()));

		// if the position is still null. This is possible in the oda design
		// spec. we should make the position as index+1.

		Integer newPos = Integer.valueOf(0);
		Integer pos = newParam.getPosition();
		if (pos == null || pos.intValue() < 0)
			newParam.setPosition(newPos);

		newParam.setNativeDataType(Integer.valueOf(dataAttrs.getNativeDataTypeCode()));

		// boolean is not supported in data set parameter yet.

		String dataType = getROMDataType(dataSourceId, dataSetId, newParam, setDefinedParams.iterator());
		if (dataType == null || !DesignChoiceConstants.PARAM_TYPE_BOOLEAN.equalsIgnoreCase(dataType))
			newParam.setParameterDataType(dataType);

		ElementNullability nullability = dataAttrs.getNullability();
		processElementNullability(nullability);
	}

	/**
	 * 
	 * @param nullability
	 */
	private void processElementNullability(ElementNullability nullability) {

		switch (nullability.getValue()) {
		case ElementNullability.NULLABLE:
			newParam.setAllowNull(true);
			break;
		case ElementNullability.NOT_NULLABLE:
			newParam.setAllowNull(false);
			break;
		case ElementNullability.UNKNOWN:
			break;
		}
	}

	/**
	 * 
	 * @param mode
	 */

	private void processInOutMode(ParameterMode mode) {
		if (mode == null)
			return;

		switch (mode.getValue()) {
		case ParameterMode.IN_OUT:
			newParam.setIsInput(true);
			newParam.setIsOutput(true);
			break;
		case ParameterMode.IN:
			newParam.setIsInput(true);
			break;
		case ParameterMode.OUT:
			newParam.setIsOutput(true);
			break;
		}
	}

	/**
	 * 
	 * @param attrs
	 */

	private void processInputElementAttributes(InputElementAttributes attrs) {
		boolean withLinkedParameter = !StringUtil.isBlank(newParam.getParamName());

		StaticValues newValues = attrs.getDefaultValues();
		Object newValue = null;
		if (newValues != null && !newValues.isEmpty())
			newValue = newValues.getValues().get(0);

		if (!withLinkedParameter)
			setROMDefaultValue(newParam, newValue);

		newParam.setIsOptional(Boolean.valueOf(attrs.isOptional()));
	}

	/**
	 * Sets the default value for ROM data set parameter.
	 * 
	 * @param setParam the ROM data set parameter
	 * @param newValue the new value
	 */

	private void setROMDefaultValue(DataSetParameter setParam, Object newValue) {
		setParam.setExpressionProperty(DataSetParameter.DEFAULT_VALUE_MEMBER, AdapterUtil.createExpression(newValue));
	}

	/**
	 * 
	 * @throws SemanticException
	 */

	private void processLinkedReportParameter() throws SemanticException {
		String reportParamName = newParam.getParamName();
		if (StringUtil.isBlank(reportParamName))
			return;

		ScalarParameterHandle reportParam = (ScalarParameterHandle) module.findParameter(reportParamName);

		if (reportParam == null)
			return;

		CommandStack cmdStack = reportParam.getModuleHandle().getCommandStack();

		cmdStack.startTrans(null);

		ReportParamUpdater tmpUpdater = new ReportParamUpdater(paramDefn, newParam, reportParam, setHandle);

		tmpUpdater.process();

		cmdStack.commit();
	}

	/**
	 * Returns the rom data type in string.
	 * 
	 * @param dataSourceId    the id of the data source
	 * @param dataSetId       the ide of the data set
	 * @param param           the rom data set parameter
	 * @param setHandleParams params defined in data set handle
	 * @return the rom data type in string
	 */

	private String getROMDataType(String dataSourceId, String dataSetId, OdaDataSetParameter param,
			Iterator setHandleParams) {
		String name = param.getNativeName();
		Integer position = param.getPosition();
		Integer nativeType = param.getNativeDataType();
		if (nativeType == null)
			return param.getParameterDataType();

		OdaDataSetParameterHandle tmpParam = findDataSetParameterByName(name, position, nativeType, setHandleParams);

		if (tmpParam == null)
			return convertNativeTypeToROMDataType(dataSourceId, dataSetId, nativeType.intValue());

		Integer tmpPosition = tmpParam.getPosition();
		if (tmpPosition == null)
			return convertNativeTypeToROMDataType(dataSourceId, dataSetId, nativeType.intValue());

		if (!tmpPosition.equals(param.getPosition()))
			return convertNativeTypeToROMDataType(dataSourceId, dataSetId, nativeType.intValue());

		// Compare its original native type in session request with the latest
		// native type in response. If they are the same, preserve the existing
		// ROM data type.

		Integer tmpNativeCodeType = tmpParam.getNativeDataType();
		if (tmpNativeCodeType == null || tmpNativeCodeType.equals(nativeType))
			return tmpParam.getParameterDataType();

		// If they are different, check if the latest native type in response is
		// compatible/convertible to the existing ROM data type. If compatible
		// (e.g. an unnknown native type is always compatible to any one of the
		// ROM data types), it should preserve the parameter's existing ROM data
		// type value. If not compatible, update its ROM data type to the value
		// that maps from the latest native data type.

		String oldDataType = tmpParam.getParameterDataType();
		return convertNativeTypeToROMDataType(dataSourceId, dataSetId, nativeType.intValue(), oldDataType);
	}

	/**
	 * Converts the ODA native data type code to rom data type.
	 * 
	 * @param dataSourceId       the id of the data source
	 * @param dataSetId          the ide of the data set
	 * @param nativeDataTypeCode the oda data type code
	 * @return the rom data type in string
	 */

	private static String convertNativeTypeToROMDataType(String dataSourceId, String dataSetId,
			int nativeDataTypeCode) {
		return convertNativeTypeToROMDataType(dataSourceId, dataSetId, nativeDataTypeCode, null);
	}

	/**
	 * Converts the ODA native data type code to rom data type.
	 * 
	 * @param dataSourceId       the id of the data source
	 * @param dataSetId          the ide of the data set
	 * @param nativeDataTypeCode the oda data type code
	 * @return the rom data type in string
	 */

	private static String convertNativeTypeToROMDataType(String dataSourceId, String dataSetId, int nativeDataTypeCode,
			String romDataType) {
		String romNewDataType = null;

		try {
			romNewDataType = NativeDataTypeUtil.getUpdatedDataType(dataSourceId, dataSetId, nativeDataTypeCode,
					romDataType, DesignChoiceConstants.CHOICE_PARAM_TYPE);
		} catch (BirtException e) {

		}

		return romNewDataType;
	}

	/**
	 * Returns the matched data set parameter by given name and position.
	 * 
	 * @param dataSetParamName the data set parameter name
	 * @param position         the position
	 * @param params           the iterator of data set parameters
	 * @return the matched data set parameter
	 */

	private OdaDataSetParameterHandle findDataSetParameterByName(String dataSetParamName, Integer position,
			Integer nativeDataType, Iterator params) {
		if (position == null)
			return null;

		while (params.hasNext()) {
			OdaDataSetParameterHandle param = (OdaDataSetParameterHandle) params.next();

			Integer tmpNativeDataType = param.getNativeDataType();
			String tmpNativeName = param.getNativeName();

			// nativeName/name, position and nativeDataType should match.

			// case 1: if the native name is not blank, just use it.

			if (!StringUtil.isBlank(tmpNativeName) && tmpNativeName.equals(dataSetParamName))
				return param;

			// case 2: if the native name is blank, match native data type and
			// position

			if (StringUtil.isBlank(tmpNativeName) && position.equals(param.getPosition())
					&& (tmpNativeDataType == null || tmpNativeDataType.equals(nativeDataType))) {
				return param;
			}
		}

		return null;
	}
}
