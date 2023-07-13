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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.adapter.oda.IODADesignFactory;
import org.eclipse.birt.report.model.adapter.oda.ODADesignFactory;
import org.eclipse.birt.report.model.adapter.oda.util.ParameterValueUtil;
import org.eclipse.birt.report.model.api.AbstractScalarParameterHandle;
import org.eclipse.birt.report.model.api.CascadingParameterGroupHandle;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.ExpressionHandle;
import org.eclipse.birt.report.model.api.ExpressionType;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.OdaDataSetParameterHandle;
import org.eclipse.birt.report.model.api.ParameterGroupHandle;
import org.eclipse.birt.report.model.api.SelectionChoiceHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.DataSetParameter;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.elements.interfaces.IInternalAbstractScalarParameterModel;
import org.eclipse.birt.report.model.elements.interfaces.IScalarParameterModel;
import org.eclipse.datatools.connectivity.oda.design.DataElementAttributes;
import org.eclipse.datatools.connectivity.oda.design.DataElementUIHints;
import org.eclipse.datatools.connectivity.oda.design.DataSetDesign;
import org.eclipse.datatools.connectivity.oda.design.DataSetParameters;
import org.eclipse.datatools.connectivity.oda.design.DynamicValuesQuery;
import org.eclipse.datatools.connectivity.oda.design.InputElementAttributes;
import org.eclipse.datatools.connectivity.oda.design.InputParameterAttributes;
import org.eclipse.datatools.connectivity.oda.design.InputParameterUIHints;
import org.eclipse.datatools.connectivity.oda.design.ParameterDefinition;
import org.eclipse.datatools.connectivity.oda.design.ScalarValueChoices;
import org.eclipse.datatools.connectivity.oda.design.ScalarValueDefinition;
import org.eclipse.datatools.connectivity.oda.design.StaticValues;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * Converts values between a report parameter and ODA Design Session Request.
 *
 */

abstract class AbstractReportParameterAdapter {

	/**
	 * Deprecated allowNull property.
	 *
	 * @deprecated
	 */

	@Deprecated
	protected static final String ALLOW_NULL_PROP_NAME = IScalarParameterModel.ALLOW_NULL_PROP;

	/**
	 * Deprecated allowBlank property.
	 *
	 * @deprecated
	 */

	@Deprecated
	protected static final String ALLOW_BLANK_PROP_NAME = IScalarParameterModel.ALLOW_BLANK_PROP;

	/**
	 *
	 */

	protected final IODADesignFactory designFactory;

	/**
	 * Default constructor.
	 */

	AbstractReportParameterAdapter() {
		designFactory = ODADesignFactory.getFactory();
	}

	/**
	 * Updates values in report parameter by given ROM data set parameter.
	 *
	 * @param reportParam        the report parameter
	 * @param dataSetParam       the data set parameter
	 * @param updateDefaultValue the flag which indicates if the default value need
	 *                           to be forced updated.
	 * @throws SemanticException
	 */

	protected void updateLinkedReportParameterFromROMParameter(AbstractScalarParameterHandle reportParam,
			OdaDataSetParameterHandle dataSetParam, boolean updateDefaultValue) throws SemanticException {
		assert reportParam != null;

		if (dataSetParam == null) {
			return;
		}

		// should not convert report parameter name here.

		Object defaultValue = dataSetParam.getExpressionProperty(DataSetParameter.DEFAULT_VALUE_MEMBER).getValue();
		String paramName = dataSetParam.getParamName();

		if (StringUtil.isBlank(paramName)) {
			dataSetParam.setParamName(reportParam.getName());
		}
		if (updateDefaultValue) {
			setROMDefaultValue(reportParam, defaultValue);
		}
	}

	/**
	 * Sets the default value for ROM data set parameter.
	 *
	 * @param setParam     the ROM data set parameter
	 * @param literalValue the value
	 */

	private void setROMDefaultValue(AbstractScalarParameterHandle setParam, Object value) throws SemanticException {
		List<Expression> newValues = null;
		if (!AdapterUtil.isNullExpression(value)) {
			assert value instanceof Expression;
			newValues = new ArrayList<>();
			newValues.add(
					new Expression(((Expression) value).getExpression(), ((Expression) value).getUserDefinedType()));
		}
		setParam.setDefaultValueList(newValues);
	}

	/**
	 * Refreshes property values of the given report parameter by the given
	 * parameter definition and cached parameter definition. If values in cached
	 * parameter definition is null or values in cached parameter definition are not
	 * equal to values in parameter defnition, update values in given report
	 * parameter.
	 *
	 * @param reportParam     the report parameter
	 * @param paramDefn       the ODA parameter definition
	 * @param cachedParamDefn the cached ODA parameter definition in designerValues
	 * @param dataType        the updated data type
	 * @param setHandle       the ROM data set that has the corresponding data set
	 *                        parameter
	 * @throws SemanticException if value in the data set design is invalid
	 */

	void updateLinkedReportParameter(AbstractScalarParameterHandle reportParam, ParameterDefinition paramDefn,
			ParameterDefinition cachedParamDefn, OdaDataSetHandle setHandle) throws SemanticException {
		if (paramDefn == null) {
			return;
		}

		CommandStack cmdStack = reportParam.getModuleHandle().getCommandStack();
		try {
			cmdStack.startTrans(null);

			updateAbstractScalarParameter(reportParam, paramDefn, cachedParamDefn, setHandle);

		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	/**
	 * Refreshes property values of the given report parameter by the given
	 * parameter definition and cached parameter definition. If values in cached
	 * parameter definition is null or values in cached parameter definition are not
	 * equal to values in parameter defnition, update values in given report
	 * parameter.
	 *
	 * @param reportParam     the report parameter
	 * @param paramDefn       the ODA parameter definition
	 * @param cachedParamDefn the cached ODA parameter definition in designerValues
	 * @param setHandle       the ROM data set that has the corresponding data set
	 *                        parameter
	 * @throws SemanticException if value in the data set design is invalid
	 */
	protected void updateAbstractScalarParameter(AbstractScalarParameterHandle reportParam,
			ParameterDefinition paramDefn, ParameterDefinition cachedParamDefn, OdaDataSetHandle setHandle)
			throws SemanticException {
		updateDataElementAttrsToReportParam(paramDefn.getAttributes(),
				cachedParamDefn == null ? null : cachedParamDefn.getAttributes(), reportParam);

		updateInputParameterAttrsToReportParam(paramDefn.getInputAttributes(),
				cachedParamDefn == null ? null : cachedParamDefn.getInputAttributes(), reportParam, setHandle);
	}

	/**
	 * Returns the matched ODA data set parameter by the given ROM data set
	 * parameter and data set design.
	 *
	 * @param param     the ROM data set parameter
	 * @param odaParams
	 * @return the matched ODA parameter definition
	 */
	protected static ParameterDefinition getValidParameterDefinition(OdaDataSetParameterHandle param,
			DataSetParameters odaParams) {
		if (param == null || odaParams == null || odaParams.getParameterDefinitions().isEmpty()) {
			return null;
		}

		ParameterDefinition matchedParam = DataSetParameterAdapter.findParameterDefinition(odaParams,
				param.getNativeName(), param.getPosition());
		return matchedParam;
	}

	/**
	 * Updates values in DataElementAttributes to the given report parameter.
	 *
	 * @param dataAttrs       the latest data element attributes
	 * @param cachedDataAttrs the cached data element attributes
	 * @param reportParam     the report parameter
	 * @throws SemanticException
	 */

	private void updateDataElementAttrsToReportParam(DataElementAttributes dataAttrs,
			DataElementAttributes cachedDataAttrs, AbstractScalarParameterHandle reportParam) throws SemanticException {

		if (dataAttrs == null) {
			return;
		}

		boolean allowsNull = dataAttrs.allowsNull();
		if (cachedDataAttrs == null || cachedDataAttrs.allowsNull() != allowsNull) {
			setReportParamIsRequired(reportParam, ALLOW_NULL_PROP_NAME, dataAttrs.allowsNull());
		}

		// reportParam.setAllowNull( dataAttrs.allowsNull( ) );

		DataElementUIHints dataUiHints = dataAttrs.getUiHints();
		DataElementUIHints cachedDataUiHints = (cachedDataAttrs == null ? null : cachedDataAttrs.getUiHints());
		if (dataUiHints != null) {
			String displayName = dataUiHints.getDisplayName();
			String cachedDisplayName = cachedDataUiHints == null ? null : cachedDataUiHints.getDisplayName();

			boolean isChanged = false;

			if (cachedDisplayName == null || !cachedDisplayName.equals(displayName)) {
				isChanged = true;
				reportParam.setPromptText(displayName);
				reportParam.setPromptTextID(dataUiHints.getDisplayNameKey());
			}
			if (!isChanged && cachedDataUiHints != null && cachedDisplayName != null) {
				reportParam.setPromptText(cachedDisplayName);
				reportParam.setPromptTextID(cachedDataUiHints.getDisplayNameKey());
			}

			isChanged = false;
			String description = dataUiHints.getDescription();
			String cachedDescription = cachedDataUiHints == null ? null : cachedDataUiHints.getDescription();
			if (cachedDescription == null || !cachedDescription.equals(description)) {
				isChanged = true;
				reportParam.setHelpText(description);
				reportParam.setHelpTextKey(dataUiHints.getDescriptionKey());
			}

			if (!isChanged && cachedDataUiHints != null && cachedDescription != null) {
				reportParam.setHelpText(cachedDescription);
				reportParam.setHelpTextKey(cachedDataUiHints.getDescriptionKey());
			}
		}

	}

	/**
	 * Updates values in InputParameterAttributes to the given report parameter.
	 *
	 * @param dataAttrs       the latest input parameter attributes
	 * @param cachedDataAttrs the cached input parameter attributes
	 * @param reportParam     the report parameter
	 * @param setHandle       the ROM data set that has the corresponding data set
	 *                        parameter
	 * @throws SemanticException
	 */

	private void updateInputParameterAttrsToReportParam(InputParameterAttributes inputParamAttrs,
			InputParameterAttributes cachedInputParamAttrs, AbstractScalarParameterHandle reportParam,
			OdaDataSetHandle setHandle) throws SemanticException {
		if (inputParamAttrs == null) {
			return;
		}

		InputParameterUIHints paramUiHints = inputParamAttrs.getUiHints();
		if (paramUiHints != null && reportParam.getContainer() instanceof ParameterGroupHandle) {
			ParameterGroupHandle paramGroup = (ParameterGroupHandle) reportParam.getContainer();

			InputParameterUIHints cachedParamUiHints = cachedInputParamAttrs == null ? null
					: cachedInputParamAttrs.getUiHints();

			String cachedGroupPromptDisplayName = cachedParamUiHints == null ? null
					: cachedParamUiHints.getGroupPromptDisplayName();

			String groupPromptDisplayName = paramUiHints.getGroupPromptDisplayName();

			if (cachedGroupPromptDisplayName == null || !cachedGroupPromptDisplayName.equals(groupPromptDisplayName)) {
				paramGroup.setDisplayName(groupPromptDisplayName);

				paramGroup.setDisplayNameKey(paramUiHints.getGroupPromptDisplayNameKey());
			}
		}

		updateInputElementAttrsToReportParam(inputParamAttrs.getElementAttributes(),
				cachedInputParamAttrs == null ? null : cachedInputParamAttrs.getElementAttributes(), reportParam,
				setHandle);
	}

	/**
	 * Updates values in InputElementAttributes to the given report parameter.
	 *
	 * @param elementAttrs       the latest input element attributes
	 * @param cachedElementAttrs the cached input element attributes
	 * @param reportParam        the report parameter
	 * @param setHandle          the ROM data set that has the corresponding data
	 *                           set parameter
	 * @throws SemanticException
	 */

	protected void updateInputElementAttrsToReportParam(InputElementAttributes elementAttrs,
			InputElementAttributes cachedElementAttrs, AbstractScalarParameterHandle reportParam,
			OdaDataSetHandle setHandle) throws SemanticException {
		if (elementAttrs == null) {
			return;
		}

		// update default values.

		updateDefaultValueToReportParam(elementAttrs, cachedElementAttrs, reportParam);

		// update isOptional value
		Boolean isOptional = elementAttrs.isOptional();
		Boolean cachedIsOptional = cachedElementAttrs == null ? null : cachedElementAttrs.isOptional();
		if (!CompareUtil.isEquals(cachedIsOptional, isOptional)) {
			setReportParamIsRequired(reportParam, ALLOW_BLANK_PROP_NAME, isOptional.booleanValue());
		}

		// update selection choices
		updateROMSelectionList(elementAttrs.getStaticValueChoices(),
				cachedElementAttrs == null ? null : cachedElementAttrs.getStaticValueChoices(), reportParam);

		// update dynamic list
		DynamicValuesQuery valueQuery = elementAttrs.getDynamicValueChoices();
		AdapterUtil.updateROMDyanmicList(valueQuery,
				cachedElementAttrs == null ? null : cachedElementAttrs.getDynamicValueChoices(), reportParam,
				setHandle);

		// for dynamic parameter, the flag is in DynamicValuesQuery is true
		// for static parameter, the DynamicValuesQuery is null or the flag is
		// false

		DynamicValuesQuery cachedValueQuery = cachedElementAttrs == null ? null
				: cachedElementAttrs.getDynamicValueChoices();

		boolean isEnabled = (valueQuery == null) ? false : valueQuery.isEnabled();

		if (reportParam.getContainer() != null && reportParam.getContainer() instanceof CascadingParameterGroupHandle) {
			isEnabled = true;
		}

		if (cachedValueQuery == null || cachedValueQuery.isEnabled() != isEnabled) {
			if (isEnabled) {
				reportParam.setValueType(DesignChoiceConstants.PARAM_VALUE_TYPE_DYNAMIC);
			} else {
				DataSetHandle dataSet = reportParam.getDataSet();
				if (dataSet instanceof OdaDataSetHandle) {
					reportParam.setValueType(DesignChoiceConstants.PARAM_VALUE_TYPE_STATIC);
				}
			}

		}
	}

	/**
	 * @param elementAttrs
	 * @param cachedElementAttrs
	 * @param reportParam
	 * @throws SemanticException
	 */

	protected void updateDefaultValueToReportParam(InputElementAttributes elementAttrs,
			InputElementAttributes cachedElementAttrs, AbstractScalarParameterHandle reportParam)
			throws SemanticException {
		// update default values.

		StaticValues defaultValues = elementAttrs.getDefaultValues();
		StaticValues cachedDefaultValues = cachedElementAttrs == null ? null : cachedElementAttrs.getDefaultValues();

		if (!new EcoreUtil.EqualityHelper().equals(cachedDefaultValues, defaultValues)) {
			AdapterUtil.updateROMDefaultValues(defaultValues, reportParam);
		}
	}

	/**
	 * Updates values in ScalarValueChoices to the given report parameter.
	 *
	 * @param dataAttrs       the latest scalar values
	 * @param cachedDataAttrs the cached scalar value
	 * @param reportParam     the report parameter
	 * @throws SemanticException
	 */

	private void updateROMSelectionList(ScalarValueChoices staticChoices, ScalarValueChoices cachedStaticChoices,
			AbstractScalarParameterHandle paramHandle) throws SemanticException {
		if (staticChoices == null) {
			return;
		}

		String newChoiceStr = DesignObjectSerializer.toExternalForm(staticChoices);
		String latestChoiceStr = DesignObjectSerializer.toExternalForm(cachedStaticChoices);

		if (latestChoiceStr != null && latestChoiceStr.equals(newChoiceStr)) {
			return;
		}

		AdapterUtil.updateROMSelectionList(staticChoices, cachedStaticChoices, paramHandle);
	}

	/**
	 * Creates an ParameterDefinition with the given report parameter.
	 *
	 * @param paramDefn     the ROM report parameter.
	 * @param paramHandle   the report parameter
	 * @param dataSetDesign the data set design
	 * @return the created ParameterDefinition
	 */

	ParameterDefinition updateParameterDefinitionFromReportParam(ParameterDefinition paramDefn,
			AbstractScalarParameterHandle paramHandle, DataSetDesign dataSetDesign) {

		assert paramHandle != null;
		if (paramDefn == null) {
			return null;
		}

		paramDefn.setAttributes(updateDataElementAttrs(paramDefn.getAttributes(), paramHandle));

		paramDefn.setInputAttributes(
				updateInputElementAttrs(paramDefn.getInputAttributes(), paramHandle, dataSetDesign));
		return paramDefn;
	}

	/**
	 * Creates an DataElementAttributes with the given ROM report parameter.
	 *
	 * @param paramHandle the ROM report parameter.
	 * @return the created DataElementAttributes
	 */

	private DataElementAttributes updateDataElementAttrs(DataElementAttributes dataAttrs,
			AbstractScalarParameterHandle paramHandle) {
		DataElementAttributes retDataAttrs = dataAttrs;

		if (retDataAttrs == null) {
			retDataAttrs = designFactory.createDataElementAttributes();
		}

		// retDataAttrs.setNullability( DataSetParameterAdapter
		// .newElementNullability( paramHandle.allowNll( ) ) );

		retDataAttrs.setNullability(DataSetParameterAdapter
				.newElementNullability(getReportParamAllowMumble(paramHandle, ALLOW_NULL_PROP_NAME)));

		DataElementUIHints uiHints = designFactory.createDataElementUIHints();

		String text = paramHandle.getPromptText();
		String textKey = paramHandle.getPromptTextID();

		if (text != null || textKey != null) {
			uiHints.setDisplayName(text);
			uiHints.setDisplayNameKey(textKey);
		}

		text = paramHandle.getHelpText();
		textKey = paramHandle.getHelpTextKey();

		if (text != null || textKey != null) {
			uiHints.setDescription(text);
			uiHints.setDescriptionKey(textKey);
		}

		retDataAttrs.setUiHints(uiHints);

		return retDataAttrs;

	}

	/**
	 * Creates a ODA InputParameterAttributes with the given ROM report parameter.
	 *
	 * @param inputParamAttrs
	 *
	 * @param paramHandle     the ROM report parameter.
	 * @param dataSetDesign
	 *
	 * @return the created <code>InputParameterAttributes</code>.
	 */

	protected InputParameterAttributes updateInputElementAttrs(InputParameterAttributes inputParamAttrs,
			AbstractScalarParameterHandle paramHandle, DataSetDesign dataSetDesign) {
		InputParameterAttributes retInputParamAttrs = inputParamAttrs;

		if (inputParamAttrs == null) {
			retInputParamAttrs = designFactory.createInputParameterAttributes();
		}

		InputElementAttributes inputAttrs = retInputParamAttrs.getElementAttributes();
		if (inputAttrs == null) {
			inputAttrs = designFactory.createInputElementAttributes();
		}

		// update default values.

		updateDefaultStaticValues(inputAttrs, paramHandle);

		// inputAttrs.setOptional( paramHandle.allowBlank( ) );
		inputAttrs.setOptional(getReportParamAllowMumble(paramHandle, ALLOW_BLANK_PROP_NAME));

		ScalarValueChoices staticChoices = null;
		Iterator<?> selectionList = paramHandle.choiceIterator();
		while (selectionList.hasNext()) {
			if (staticChoices == null) {
				staticChoices = designFactory.createScalarValueChoices();
			}
			SelectionChoiceHandle choice = (SelectionChoiceHandle) selectionList.next();

			ScalarValueDefinition valueDefn = designFactory.createScalarValueDefinition();
			valueDefn.setValue(choice.getValue());

			String label = choice.getLabel();
			String labelKey = choice.getLabelKey();

			if (label != null || labelKey != null) {
				valueDefn.setDisplayName(label);
				valueDefn.setDisplayNameKey(labelKey);
			}

			staticChoices.getScalarValues().add(valueDefn);
		}
		inputAttrs.setStaticValueChoices(staticChoices);

		ExpressionHandle valueExpr = paramHandle
				.getExpressionProperty(IInternalAbstractScalarParameterModel.VALUE_EXPR_PROP);
		ExpressionHandle labelExpr = paramHandle
				.getExpressionProperty(IInternalAbstractScalarParameterModel.LABEL_EXPR_PROP);

		DynamicValuesQuery valueQuery = updateDynamicValueQuery(paramHandle.getDataSet(), valueExpr.getValue(),
				labelExpr.getValue(), dataSetDesign,
				DesignChoiceConstants.PARAM_VALUE_TYPE_DYNAMIC.equalsIgnoreCase(paramHandle.getValueType()));
		inputAttrs.setDynamicValueChoices(valueQuery);

		if (paramHandle.getContainer() instanceof ParameterGroupHandle) {
			ParameterGroupHandle groupHandle = (ParameterGroupHandle) paramHandle.getContainer();

			InputParameterUIHints paramUiHints = designFactory.createInputParameterUIHints();

			String text = groupHandle.getDisplayName();
			String textKey = groupHandle.getDisplayNameKey();

			if (text != null || textKey != null) {
				paramUiHints.setGroupPromptDisplayName(text);
				paramUiHints.setGroupPromptDisplayNameKey(textKey);
			}

			retInputParamAttrs.setUiHints(paramUiHints);
		}

		retInputParamAttrs.setElementAttributes(inputAttrs);
		return retInputParamAttrs;
	}

	protected DynamicValuesQuery updateDynamicValueQuery(DataSetHandle setHandle, Object valueColumn,
			Object labelColumn, DataSetDesign dataSetDesign, boolean isEnabled) {
		DynamicValuesQuery valueQuery = null;

		if (setHandle instanceof OdaDataSetHandle && valueColumn != null) {
			valueQuery = designFactory.createDynamicValuesQuery();

			if (dataSetDesign != null) {
				DataSetDesign targetDataSetDesign = EcoreUtil.copy(dataSetDesign);
				if (!setHandle.getName().equals(dataSetDesign.getName())) {
					targetDataSetDesign = new ModelOdaAdapter().createDataSetDesign((OdaDataSetHandle) setHandle);
				}
				valueQuery.setDataSetDesign(targetDataSetDesign);
			} else {
				DataSetDesign targetDataSetDesign = new ModelOdaAdapter()
						.createDataSetDesign((OdaDataSetHandle) setHandle);
				valueQuery.setDataSetDesign(targetDataSetDesign);
			}
			valueQuery.setDisplayNameColumn(AdapterUtil.extractColumnName(labelColumn));
			valueQuery.setValueColumn(AdapterUtil.extractColumnName(valueColumn));
			valueQuery.setEnabled(isEnabled);
		}

		return valueQuery;
	}

	/**
	 * Update default static values
	 *
	 * @param inputAttrs  attributes of the input element
	 * @param paramHandle parameter handle
	 */
	protected void updateDefaultStaticValues(InputElementAttributes inputAttrs,
			AbstractScalarParameterHandle paramHandle) {
		// update default values.

		StaticValues newValues = null;
		List<Expression> tmpValues = paramHandle.getDefaultValueList();
		if (tmpValues != null) {
			for (int i = 0; i < tmpValues.size(); i++) {
				if (newValues == null) {
					newValues = designFactory.createStaticValues();
				}

				Expression tmpExpr = tmpValues.get(i);
				String odaValue = null;

				// for the constant, there is no need to remove quotes
				if (ExpressionType.CONSTANT.equalsIgnoreCase(tmpExpr.getType())) {
					odaValue = tmpExpr.getStringExpression();
				} else {
					odaValue = ParameterValueUtil.toODAValue(tmpValues.get(i).getStringExpression(),
							paramHandle.getDataType());
				}
				newValues.add(odaValue);
			}
		}
		inputAttrs.setDefaultValues(newValues);
	}

	/**
	 * Returns the boolean value of allowMumble properties. Only support "allowNull"
	 * and "allowBlank" properties.
	 * <p>
	 * "allowMumble" properties has been removed ROM. However, to do conversion,
	 * still need to know their values.
	 *
	 * @param param    the parameter
	 * @param propName either "allowNull" or "allowBlank".
	 * @return <code>true</code> if the parameter allows the value. Otherwise
	 *         <code>false</code>.
	 */

	protected boolean getReportParamAllowMumble(AbstractScalarParameterHandle param, String propName) {
		if (ALLOW_NULL_PROP_NAME.equalsIgnoreCase(propName) || ALLOW_BLANK_PROP_NAME.equalsIgnoreCase(propName)) {
			return !param.isRequired();
		}

		assert false;
		return false;

	}

	/**
	 * Returns the boolean value of allowMumble properties. Only support "allowNull"
	 * and "allowBlank" properties.
	 * <p>
	 * "allowMumble" properties has been removed ROM. However, to do conversion,
	 * still need to know their values.
	 *
	 * @param param            the parameter
	 * @param obsoletePropName either "allowNull" or "allowBlank".
	 * @param value
	 * @throws SemanticException
	 */

	protected void setReportParamIsRequired(AbstractScalarParameterHandle param, String obsoletePropName, boolean value)
			throws SemanticException {
		if (ALLOW_NULL_PROP_NAME.equalsIgnoreCase(obsoletePropName)
				|| ALLOW_BLANK_PROP_NAME.equalsIgnoreCase(obsoletePropName)) {
			param.setIsRequired(!value);
		} else {
			assert false;
		}
	}
}
