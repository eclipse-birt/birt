/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.adapter.oda.impl;

import org.eclipse.birt.report.model.adapter.oda.IReportParameterAdapter;
import org.eclipse.birt.report.model.api.AbstractScalarParameterHandle;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.OdaDataSetParameterHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.OdaDataSetParameter;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.datatools.connectivity.oda.design.DataElementAttributes;
import org.eclipse.datatools.connectivity.oda.design.DataElementUIHints;
import org.eclipse.datatools.connectivity.oda.design.DataSetDesign;
import org.eclipse.datatools.connectivity.oda.design.DynamicValuesQuery;
import org.eclipse.datatools.connectivity.oda.design.InputElementAttributes;
import org.eclipse.datatools.connectivity.oda.design.InputElementUIHints;
import org.eclipse.datatools.connectivity.oda.design.InputParameterAttributes;
import org.eclipse.datatools.connectivity.oda.design.InputPromptControlStyle;
import org.eclipse.datatools.connectivity.oda.design.ParameterDefinition;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * Converts values between a report scalar parameter and ODA Design Session
 * Request.
 *
 */

class ReportParameterAdapter extends AbstractReportParameterAdapter implements IReportParameterAdapter {

	/**
	 * The data type of the scalar parameter
	 */
	private String dataType;

	/*
	 * (non-Javadoc)
	 *
	 *
	 * @seeorg.eclipse.birt.report.model.adapter.oda.IReportParameterAdapter #
	 * updateLinkedReportParameter
	 * (org.eclipse.birt.report.model.api.ScalarParameterHandle,
	 * org.eclipse.birt.report.model.api.OdaDataSetParameterHandle)
	 */
	@Override
	public void updateLinkedReportParameter(ScalarParameterHandle reportParam, OdaDataSetParameterHandle dataSetParam)
			throws SemanticException {
		if (reportParam == null || dataSetParam == null) {
			return;
		}

		updateLinkedReportParameterFromROMParameter(reportParam, dataSetParam, true);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.model.adapter.oda.IReportParameterAdapter#
	 * updateLinkedReportParameter
	 * (org.eclipse.birt.report.model.api.ScalarParameterHandle,
	 * org.eclipse.birt.report.model.api.OdaDataSetParameterHandle,
	 * org.eclipse.datatools.connectivity.oda.design.DataSetDesign)
	 */

	@Override
	public void updateLinkedReportParameter(ScalarParameterHandle reportParam, OdaDataSetParameterHandle dataSetParam,
			DataSetDesign dataSetDesign) throws SemanticException {
		if (reportParam == null || dataSetParam == null) {
			return;
		}

		ParameterDefinition matchedParam = null;

		String dataType = null;

		OdaDataSetHandle setHandle = (OdaDataSetHandle) dataSetParam.getElementHandle();

		if (dataSetDesign != null) {
			matchedParam = getValidParameterDefinition(dataSetParam, dataSetDesign.getParameters());

			dataType = DataSetParameterAdapter.getROMDataType(dataSetDesign.getOdaExtensionDataSourceId(),
					dataSetDesign.getOdaExtensionDataSetId(), (OdaDataSetParameter) dataSetParam.getStructure(),
					setHandle == null ? null : setHandle.parametersIterator());
		}

		CommandStack cmdStack = reportParam.getModuleHandle().getCommandStack();

		cmdStack.startTrans(null);
		try {
			if (matchedParam != null) {
				updateLinkedReportParameter(reportParam, matchedParam, null, dataType,
						(OdaDataSetHandle) dataSetParam.getElementHandle());
			}

			updateLinkedReportParameterFromROMParameter(reportParam, dataSetParam, false);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	/**
	 * Checks whether the given report parameter is updated. This method checks
	 * values of report parameters and values in data set design.
	 * <p>
	 * If any input argument is null or the matched ODA parameter definition cannot
	 * be found, return <code>true</code>.
	 *
	 * @param reportParam the report parameter
	 * @param odaParam    the ODA parameter definition
	 * @param newDataType the data type
	 *
	 * @return <code>true</code> if the report paramter is updated or has no
	 *         parameter definition in the data set design. Otherwise
	 *         <code>false</code>.
	 */

	boolean isUpdatedReportParameter(ScalarParameterHandle reportParam, ParameterDefinition odaParam,
			String newDataType) {
		if (reportParam == null || odaParam == null) {
			return true;
		}

		DataElementAttributes dataAttrs = odaParam.getAttributes();
		Boolean odaAllowNull = AdapterUtil.getROMNullability(dataAttrs.getNullability());
		boolean allowNull = getReportParamAllowMumble(reportParam, ALLOW_NULL_PROP_NAME);

		if (odaAllowNull != null && allowNull != odaAllowNull.booleanValue()) {
			return false;
		}

		if (!DesignChoiceConstants.PARAM_TYPE_ANY.equalsIgnoreCase(newDataType)) {
			if (!CompareUtil.isEquals(newDataType, reportParam.getDataType())) {
				return false;
			}
		}

		DataElementUIHints dataUiHints = dataAttrs.getUiHints();
		if (dataUiHints != null) {
			String newPromptText = dataUiHints.getDisplayName();
			String newHelpText = dataUiHints.getDescription();

			if (!CompareUtil.isEquals(newPromptText, reportParam.getPromptText()) || !CompareUtil.isEquals(newHelpText, reportParam.getHelpText())) {
				return false;
			}
		}

		InputParameterAttributes paramAttrs = odaParam.getInputAttributes();
		InputParameterAttributes tmpParamDefn = null;
		DataSetDesign tmpDataSet = null;

		if (paramAttrs != null) {
			tmpParamDefn = (InputParameterAttributes) EcoreUtil.copy(paramAttrs);

			DynamicValuesQuery tmpDynamicQuery = tmpParamDefn.getElementAttributes().getDynamicValueChoices();

			if (tmpDynamicQuery != null) {
				tmpDataSet = tmpDynamicQuery.getDataSetDesign();
				tmpDynamicQuery.setDataSetDesign(null);
			}

			if (tmpParamDefn.getUiHints() != null) {
				tmpParamDefn.setUiHints(null);
			}
		} else {
			tmpParamDefn = designFactory.createInputParameterAttributes();
		}

		InputParameterAttributes tmpParamDefn1 = designFactory.createInputParameterAttributes();

		updateInputElementAttrs(tmpParamDefn1, reportParam, null);
		if (tmpParamDefn1.getUiHints() != null) {
			tmpParamDefn1.setUiHints(null);
		}
		DynamicValuesQuery tmpDynamicQuery1 = tmpParamDefn1.getElementAttributes().getDynamicValueChoices();
		DataSetDesign tmpDataSet1 = null;
		if (tmpDynamicQuery1 != null) {
			tmpDataSet1 = tmpDynamicQuery1.getDataSetDesign();
			tmpDynamicQuery1.setDataSetDesign(null);
		}

		if (!EcoreUtil.equals(tmpDataSet, tmpDataSet1)) {
			return false;
		}

		return EcoreUtil.equals(tmpParamDefn, tmpParamDefn1);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.adapter.oda.impl.AbstractReportParameterAdapter
	 * # updateLinkedReportParameterFromROMParameter(org.eclipse.birt.report.model
	 * .api.AbstractScalarParameterHandle,
	 * org.eclipse.birt.report.model.api.OdaDataSetParameterHandle, boolean)
	 */

	@Override
	protected void updateLinkedReportParameterFromROMParameter(AbstractScalarParameterHandle reportParam,
			OdaDataSetParameterHandle dataSetParam, boolean updateDefaultValue) throws SemanticException {
		assert reportParam instanceof ScalarParameterHandle;

		ScalarParameterHandle scalarParam = (ScalarParameterHandle) reportParam;

		String dataType = dataSetParam.getParameterDataType();
		if (!StringUtil.isBlank(dataType)) {

			if (!DesignChoiceConstants.PARAM_TYPE_ANY.equalsIgnoreCase(dataType)) {
				scalarParam.setDataType(dataType);
			} else {
				scalarParam.setDataType(DesignChoiceConstants.PARAM_TYPE_STRING);
			}
		}
		super.updateLinkedReportParameterFromROMParameter(reportParam, dataSetParam, updateDefaultValue);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.adapter.oda.impl.AbstractReportParameterAdapter
	 * #updateInputElementAttrs(org.eclipse.datatools.connectivity.oda.design.
	 * InputParameterAttributes,
	 * org.eclipse.birt.report.model.api.AbstractScalarParameterHandle,
	 * org.eclipse.datatools.connectivity.oda.design.DataSetDesign)
	 */
	@Override
	protected InputParameterAttributes updateInputElementAttrs(InputParameterAttributes inputParamAttrs,
			AbstractScalarParameterHandle paramHandle, DataSetDesign dataSetDesign) {
		assert paramHandle instanceof ScalarParameterHandle;
		ScalarParameterHandle scalarParam = (ScalarParameterHandle) paramHandle;

		InputParameterAttributes retInputParamAttrs = super.updateInputElementAttrs(inputParamAttrs, paramHandle,
				dataSetDesign);
		InputElementAttributes inputAttrs = retInputParamAttrs.getElementAttributes();
		inputAttrs.setMasksValue(scalarParam.isConcealValue());

		InputElementUIHints uiHints = designFactory.createInputElementUIHints();
		uiHints.setPromptStyle(AdapterUtil.newPromptStyle(scalarParam.getControlType(), scalarParam.isMustMatch()));

		// not set the ROM default value on ODA objects.

		PropertyHandle tmpPropHandle = paramHandle.getPropertyHandle(ScalarParameterHandle.AUTO_SUGGEST_THRESHOLD_PROP);
		if (tmpPropHandle.isSet()) {
			uiHints.setAutoSuggestThreshold(scalarParam.getAutoSuggestThreshold());
		}
		inputAttrs.setUiHints(uiHints);

		return retInputParamAttrs;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.adapter.oda.impl.AbstractReportParameterAdapter
	 * #setReportParamIsRequired(org.eclipse.birt.report.model.api.
	 * AbstractScalarParameterHandle, java.lang.String, boolean)
	 */
	@Override
	protected void setReportParamIsRequired(AbstractScalarParameterHandle param, String obsoletePropName, boolean value)
			throws SemanticException {
		assert param instanceof ScalarParameterHandle;
		if (ALLOW_NULL_PROP_NAME.equalsIgnoreCase(obsoletePropName)) {
			((ScalarParameterHandle) param).setAllowNull(value);
		} else if (ALLOW_BLANK_PROP_NAME.equalsIgnoreCase(obsoletePropName)) {
			((ScalarParameterHandle) param).setAllowBlank(value);
		} else {
			super.setReportParamIsRequired(param, obsoletePropName, value);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.adapter.oda.impl.AbstractReportParameterAdapter
	 * #getReportParamAllowMumble(org.eclipse.birt.report.model.api.
	 * AbstractScalarParameterHandle, java.lang.String)
	 */
	@Override
	protected boolean getReportParamAllowMumble(AbstractScalarParameterHandle param, String propName) {
		assert param instanceof ScalarParameterHandle;
		if (ALLOW_NULL_PROP_NAME.equalsIgnoreCase(propName)) {
			return ((ScalarParameterHandle) param).allowNull();
		} else if (ALLOW_BLANK_PROP_NAME.equalsIgnoreCase(propName)) {
			return ((ScalarParameterHandle) param).allowBlank();
		}
		return super.getReportParamAllowMumble(param, propName);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.adapter.oda.impl.AbstractReportParameterAdapter
	 * #updateLinkedReportParameter(org.eclipse.birt.report.model.api.
	 * AbstractScalarParameterHandle,
	 * org.eclipse.datatools.connectivity.oda.design.ParameterDefinition,
	 * org.eclipse.datatools.connectivity.oda.design.ParameterDefinition,
	 * java.lang.String, org.eclipse.birt.report.model.api.OdaDataSetHandle)
	 */
	void updateLinkedReportParameter(ScalarParameterHandle reportParam, ParameterDefinition paramDefn,
			ParameterDefinition cachedParamDefn, String dataType, OdaDataSetHandle setHandle) throws SemanticException {

		if (isUpdatedReportParameter(reportParam, paramDefn, dataType)) {
			return;
		}
		this.dataType = dataType;

		updateLinkedReportParameter(reportParam, paramDefn, cachedParamDefn, setHandle);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.adapter.oda.impl.AbstractReportParameterAdapter
	 * #updateAbstractScalarParameter(org.eclipse.birt.report.model.api.
	 * AbstractScalarParameterHandle,
	 * org.eclipse.datatools.connectivity.oda.design.ParameterDefinition,
	 * org.eclipse.datatools.connectivity.oda.design.ParameterDefinition,
	 * org.eclipse.birt.report.model.api.OdaDataSetHandle)
	 */
	@Override
	protected void updateAbstractScalarParameter(AbstractScalarParameterHandle reportParam,
			ParameterDefinition paramDefn, ParameterDefinition cachedParamDefn, OdaDataSetHandle setHandle)
			throws SemanticException {
		assert reportParam instanceof ScalarParameterHandle;
		// any type is not support in report parameter data type.

		if (dataType == null) {
			if (!DesignChoiceConstants.PARAM_TYPE_ANY.equalsIgnoreCase(dataType)) {
				((ScalarParameterHandle) reportParam).setDataType(dataType);
			} else {
				((ScalarParameterHandle) reportParam).setDataType(DesignChoiceConstants.PARAM_TYPE_STRING);
			}
		}
		super.updateAbstractScalarParameter(reportParam, paramDefn, cachedParamDefn, setHandle);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.adapter.oda.impl.AbstractReportParameterAdapter
	 * # updateInputElementAttrsToReportParam(org.eclipse.datatools.connectivity.
	 * oda .design.InputElementAttributes,
	 * org.eclipse.datatools.connectivity.oda.design.InputElementAttributes,
	 * org.eclipse.birt.report.model.api.AbstractScalarParameterHandle,
	 * org.eclipse.birt.report.model.api.OdaDataSetHandle)
	 */
	@Override
	protected void updateInputElementAttrsToReportParam(InputElementAttributes elementAttrs,
			InputElementAttributes cachedElementAttrs, AbstractScalarParameterHandle reportParam,
			OdaDataSetHandle setHandle) throws SemanticException {
		assert reportParam instanceof ScalarParameterHandle;
		ScalarParameterHandle param = (ScalarParameterHandle) reportParam;

		// update conceal value

		Boolean masksValue = elementAttrs.isMasksValue();
		Boolean cachedMasksValues = cachedElementAttrs == null ? null
				: cachedElementAttrs.isMasksValue();

		if (!CompareUtil.isEquals(cachedMasksValues, masksValue)) {
			param.setConcealValue(masksValue.booleanValue());
		}

		InputElementUIHints uiHints = elementAttrs.getUiHints();
		if (uiHints != null) {
			InputElementUIHints cachedUiHints = cachedElementAttrs == null ? null : cachedElementAttrs.getUiHints();
			InputPromptControlStyle style = uiHints.getPromptStyle();

			InputPromptControlStyle cachedStyle = cachedUiHints == null ? null : cachedUiHints.getPromptStyle();

			if (cachedStyle == null || (style != null && cachedStyle.getValue() != style.getValue())) {
				if (isAutoSuggest(elementAttrs)) {
					param.setControlType(DesignChoiceConstants.PARAM_CONTROL_AUTO_SUGGEST);
				} else {
					param.setControlType(style == null ? null : AdapterUtil.newROMControlType(style));
				}
			}

			param.setAutoSuggestThreshold(uiHints.getAutoSuggestThreshold());
		} else if (cachedElementAttrs == null || cachedElementAttrs.getUiHints() == null) {
			param.setControlType(DesignChoiceConstants.PARAM_CONTROL_TEXT_BOX);
		}

		super.updateInputElementAttrsToReportParam(elementAttrs, cachedElementAttrs, reportParam, setHandle);
	}

	/**
	 * Determines whether the report parameter is auto-suggest ticked. It is true if
	 * and only if isSetAutoSuggestThreshold is TRUE, _mAutoSuggestThreshold is
	 * larger than 0, prompt control style is SELECTABLE_LIST_WITH_TEXT_FIELD and
	 * DynamicValuesQuery is not null.
	 *
	 * @param elementAttrs
	 * @return
	 */
	private boolean isAutoSuggest(InputElementAttributes elementAttrs) {
		if (elementAttrs == null) {
			return false;
		}
		InputElementUIHints uiHints = elementAttrs.getUiHints();
		if (uiHints == null) {
			return false;
		}
		boolean isSetAutoSuggestThreshold = uiHints.isSetAutoSuggestThreshold();
		if (!isSetAutoSuggestThreshold) {
			return false;
		}
		int threshold = uiHints.getAutoSuggestThreshold();
		if (threshold <= 0) {
			return false;
		}
		InputPromptControlStyle style = uiHints.getPromptStyle();
		if (style == null) {
			return false;
		}
		int styleMode = style.getValue();
		if (InputPromptControlStyle.SELECTABLE_LIST_WITH_TEXT_FIELD == styleMode
				&& elementAttrs.getDynamicValueChoices() != null) {
			return true;
		}

		return false;
	}

}
