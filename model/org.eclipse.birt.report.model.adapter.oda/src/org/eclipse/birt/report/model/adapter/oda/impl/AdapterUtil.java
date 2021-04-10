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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.model.api.AbstractScalarParameterHandle;
import org.eclipse.birt.report.model.api.ColumnHintHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.ExpressionHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.OdaResultSetColumn;
import org.eclipse.birt.report.model.api.elements.structures.SelectionChoice;
import org.eclipse.birt.report.model.api.simpleapi.IExpressionType;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.elements.interfaces.IAbstractScalarParameterModel;
import org.eclipse.datatools.connectivity.oda.design.CustomData;
import org.eclipse.datatools.connectivity.oda.design.DataSetDesign;
import org.eclipse.datatools.connectivity.oda.design.DynamicValuesQuery;
import org.eclipse.datatools.connectivity.oda.design.ElementNullability;
import org.eclipse.datatools.connectivity.oda.design.InputPromptControlStyle;
import org.eclipse.datatools.connectivity.oda.design.ParameterMode;
import org.eclipse.datatools.connectivity.oda.design.ScalarValueChoices;
import org.eclipse.datatools.connectivity.oda.design.ScalarValueDefinition;
import org.eclipse.datatools.connectivity.oda.design.StaticValues;
import org.eclipse.emf.common.util.EList;

/**
 * Utility class to provide some method used for adapter classes.
 * 
 */
public class AdapterUtil {

	private static ColumnNameHelper columnNameHelper = new ColumnNameHelper();

	/**
	 * Creates a ODA ParameterMode with the given parameter input/output flags.
	 * 
	 * @param isInput  the parameter is inputable.
	 * @param isOutput the parameter is outputable
	 * @return the created <code>ParameterMode</code>.
	 */

	static ParameterMode newParameterMode(boolean isInput, boolean isOutput) {
		int mode = ParameterMode.IN;
		if (isOutput && isInput)
			mode = ParameterMode.IN_OUT;
		else if (isOutput)
			mode = ParameterMode.OUT;
		else if (isInput)
			mode = ParameterMode.IN;

		return ParameterMode.get(mode);
	}

	/**
	 * Updates allowNull property for the given data set parameter definition.
	 * 
	 * @param romParamDefn the data set parameter definition.
	 * @param nullability  the ODA object indicates nullability.
	 * @return <code>true</code> if is nullable. <code>false</code> if not nullable.
	 */

	static Boolean getROMNullability(ElementNullability nullability) {
		if (nullability == null)
			return null;

		switch (nullability.getValue()) {
		case ElementNullability.NULLABLE:
			return Boolean.TRUE;
		case ElementNullability.NOT_NULLABLE:
			return Boolean.FALSE;
		case ElementNullability.UNKNOWN:
			return null;
		}

		return null;
	}

	/**
	 * Returns the prompty style with the given ROM defined parameter type.
	 * 
	 * @param controlType the ROM defined parameter type
	 * @param mustMatch   <code>true</code> if means list box, <code>false</code>
	 *                    means combo box.
	 * @return the new InputPromptControlStyle
	 */

	static InputPromptControlStyle newPromptStyle(String controlType, boolean mustMatch) {
		if (controlType == null)
			return null;

		int type = -1;
		if (DesignChoiceConstants.PARAM_CONTROL_CHECK_BOX.equalsIgnoreCase(controlType))
			type = InputPromptControlStyle.CHECK_BOX;
		else if (DesignChoiceConstants.PARAM_CONTROL_LIST_BOX.equalsIgnoreCase(controlType)) {
			if (mustMatch)
				type = InputPromptControlStyle.SELECTABLE_LIST;
			else
				type = InputPromptControlStyle.SELECTABLE_LIST_WITH_TEXT_FIELD;
		} else if (DesignChoiceConstants.PARAM_CONTROL_RADIO_BUTTON.equalsIgnoreCase(controlType))
			type = InputPromptControlStyle.RADIO_BUTTON;
		else if (DesignChoiceConstants.PARAM_CONTROL_TEXT_BOX.equalsIgnoreCase(controlType))
			type = InputPromptControlStyle.TEXT_FIELD;

		return InputPromptControlStyle.get(type);
	}

	/**
	 * Returns ROM defined control type by given ODA defined prompt style.
	 * 
	 * @param style the ODA defined prompt style
	 * @return the ROM defined control type
	 */

	static String newROMControlType(InputPromptControlStyle style) {
		if (style == null)
			return null;
		switch (style.getValue()) {
		case InputPromptControlStyle.CHECK_BOX:
			return DesignChoiceConstants.PARAM_CONTROL_CHECK_BOX;
		case InputPromptControlStyle.SELECTABLE_LIST:
		case InputPromptControlStyle.SELECTABLE_LIST_WITH_TEXT_FIELD:
			return DesignChoiceConstants.PARAM_CONTROL_LIST_BOX;
		case InputPromptControlStyle.RADIO_BUTTON:
			return DesignChoiceConstants.PARAM_CONTROL_RADIO_BUTTON;
		case InputPromptControlStyle.TEXT_FIELD:
			return DesignChoiceConstants.PARAM_CONTROL_TEXT_BOX;
		}

		return null;
	}

	/**
	 * Checks whether the data type needs quote.
	 * 
	 * @param romDataType the ROM defined data type
	 * @return <code>true</code> if data type is string. Otherwise
	 *         <code>false</code>.
	 */

	static boolean needsQuoteDelimiters(String romDataType) {
		boolean needs = false;

		if (DesignChoiceConstants.PARAM_TYPE_STRING.equals(romDataType))
			needs = true;
		else if (DesignChoiceConstants.PARAM_TYPE_DATETIME.equals(romDataType))
			needs = true;
		else if (DesignChoiceConstants.PARAM_TYPE_DATE.equals(romDataType))
			needs = true;
		else if (DesignChoiceConstants.PARAM_TYPE_TIME.equals(romDataType))
			needs = true;
		else if (DesignChoiceConstants.PARAM_TYPE_ANY.equals(romDataType))
			needs = true;
		return needs;
	}

	/**
	 * Updates the static values to report parameter handle.
	 * 
	 * @param defaultValues
	 * @param reportParam
	 * @throws SemanticException
	 */
	static void updateROMDefaultValues(StaticValues defaultValues, AbstractScalarParameterHandle reportParam)
			throws SemanticException {
		if (defaultValues == null || reportParam == null)
			return;

		List<Expression> newValues = null;
		if (defaultValues != null && !defaultValues.isEmpty()) {
			newValues = new ArrayList<Expression>();
			for (Object tmpValue : defaultValues.getValues()) {
				newValues.add(AdapterUtil.createExpression(tmpValue));
			}
		}
		reportParam.setDefaultValueList(newValues);
	}

	/**
	 * Updates values in ScalarValueChoices to the given report parameter.
	 * 
	 * @param staticChoices the latest scalar values
	 * @param cachedChoices the cached scalar value
	 * @param paramHandle   the report parameter
	 * @throws SemanticException
	 */

	static void updateROMSelectionList(ScalarValueChoices staticChoices, ScalarValueChoices cachedChoices,
			AbstractScalarParameterHandle paramHandle) throws SemanticException {
		if (staticChoices == null || paramHandle == null)
			return;

		List retList = new ArrayList();

		EList choiceList = staticChoices.getScalarValues();
		EList cachedChoiceList = null;

		if (cachedChoices != null)
			cachedChoiceList = cachedChoices.getScalarValues();

		boolean useCached = false;
		if (cachedChoiceList != null && choiceList.size() == cachedChoiceList.size())
			useCached = true;

		for (int i = 0; i < choiceList.size(); i++) {
			ScalarValueDefinition valueDefn = (ScalarValueDefinition) choiceList.get(i);

			SelectionChoice choice = StructureFactory.createSelectionChoice();
			choice.setValue(valueDefn.getValue());

			String label = valueDefn.getDisplayName();
			String labelKey = valueDefn.getDisplayNameKey();

			if (label != null || labelKey != null) {
				choice.setLabel(label);
				choice.setLabelResourceKey(labelKey);
			} else if (useCached) {
				// use cached values

				valueDefn = (ScalarValueDefinition) cachedChoiceList.get(i);
				label = valueDefn.getDisplayName();
				labelKey = valueDefn.getDisplayNameKey();

				if (label != null || labelKey != null) {
					choice.setLabel(label);
					choice.setLabelResourceKey(labelKey);
				}
			}

			retList.add(choice);
		}

		PropertyHandle propHandle = paramHandle.getPropertyHandle(AbstractScalarParameterHandle.SELECTION_LIST_PROP);

		propHandle.clearValue();

		for (int i = 0; i < retList.size(); i++) {
			propHandle.addItem(retList.get(i));
		}
	}

	/**
	 * Updates values in DynamicValuesQuery to the given report parameter.
	 * 
	 * @param valueQuery       the latest dynamic values
	 * @param cachedValueQuery the cached dynamic values
	 * @param reportParam      the report parameter
	 * @param setHandle
	 * @throws SemanticException
	 */

	static void updateROMDyanmicList(DynamicValuesQuery valueQuery, DynamicValuesQuery cachedValueQuery,
			AbstractScalarParameterHandle reportParam, OdaDataSetHandle setHandle) throws SemanticException {
		if (valueQuery == null)
			return;

		String value = valueQuery.getDataSetDesign().getName();
		String cachedValue = null;
		DataSetDesign dataSetDesign = null;
		if (cachedValueQuery != null)
			dataSetDesign = cachedValueQuery.getDataSetDesign();
		if (dataSetDesign != null)
			cachedValue = dataSetDesign.getName();

		if (cachedValue == null || !cachedValue.equals(value)) {

			reportParam.setDataSetName(value);

			// update the data set instance. To avoid recursively convert,
			// compare set handle instances.

			ModuleHandle module = setHandle.getModuleHandle();
			DataSetHandle target = module.findDataSet(value);
			if (target instanceof OdaDataSetHandle && target != setHandle)
				new ModelOdaAdapter().updateLinkedParameterDataSetHandle(valueQuery.getDataSetDesign(),
						(OdaDataSetHandle) target, false, setHandle.getDataSource());

			// if there is no corresponding data set, creates a new one.

			if (target == null) {
				OdaDataSetHandle nestedDataSet = new ModelOdaAdapter().createLinkedParameterDataSetHandle(
						valueQuery.getDataSetDesign(), module, setHandle.getDataSource());
				module.getDataSets().add(nestedDataSet);
			}
		}

		value = valueQuery.getValueColumn();
		cachedValue = cachedValueQuery == null ? null : cachedValueQuery.getValueColumn();
		if (cachedValue == null || !cachedValue.equals(value)) {
			ExpressionHandle exprHandle = reportParam
					.getExpressionProperty(IAbstractScalarParameterModel.VALUE_EXPR_PROP);
			exprHandle.setExpression(columnNameHelper.createColumnExpression(value, exprHandle.getType()));
		}

		// the label need to follow the value expression

		value = valueQuery.getDisplayNameColumn();
		cachedValue = cachedValueQuery == null ? null : cachedValueQuery.getDisplayNameColumn();
		if (cachedValue == null || !cachedValue.equals(value)) {
			ExpressionHandle exprHandle = reportParam
					.getExpressionProperty(IAbstractScalarParameterModel.LABEL_EXPR_PROP);
			exprHandle.setExpression(columnNameHelper.createColumnExpression(value, exprHandle.getType()));
		}

	}

	/**
	 * Converts the ODA native data type code to rom data type.
	 * 
	 * @param dataSourceId       the id of the data source
	 * @param dataSetId          the ide of the data set
	 * @param nativeDataTypeCode the oda data type code
	 * @param romDataType        the rom data type
	 * @return the rom data type in string
	 */

	static String convertNativeTypeToROMDataType(String dataSourceId, String dataSetId, int nativeDataTypeCode,
			String romDataType) {
		String newRomDataType = null;

		try {
			newRomDataType = NativeDataTypeUtil.getUpdatedDataType(dataSourceId, dataSetId, nativeDataTypeCode,
					romDataType, DesignChoiceConstants.CHOICE_COLUMN_DATA_TYPE);
		} catch (BirtException e) {

		}

		return newRomDataType;
	}

	/**
	 * Returns the matched column hint with the given result set column.
	 * 
	 * @param setColumn   the result set column
	 * @param columnHints the iterator that includes column hints
	 * @return the matched column hint
	 */

	static ColumnHintHandle findColumnHint(OdaResultSetColumn setColumn, Iterator columnHints) {
		assert setColumn != null;

		return findColumnHint(setColumn.getColumnName(), columnHints);
	}

	/**
	 * Returns the matched column hint with the given result set column.
	 * 
	 * @param name        the name of the column hint
	 * @param columnHints the iterator that includes column hints
	 * @return the matched column hint
	 */

	static ColumnHintHandle findColumnHint(String name, Iterator columnHints) {
		if (name == null)
			return null;

		while (columnHints.hasNext()) {
			ColumnHintHandle hint = (ColumnHintHandle) columnHints.next();
			if (name.equals(hint.getColumnName()))
				return hint;
		}

		return null;
	}

	/**
	 * Creates expression value from ODA value
	 * 
	 * @param value the ODA value
	 * @return the expression created
	 */
	static Expression createExpression(Object value) {
		String exprType = null;
		if (value instanceof String) {
			if (StringUtil.isBlank((String) value)) {
				exprType = IExpressionType.JAVASCRIPT;
				value = "\"" + value + "\"";
			} else
				exprType = IExpressionType.CONSTANT;
		}
		if (value instanceof CustomData) {
			CustomData customData = (CustomData) value;
			if (DataSetParameterAdapter.PROVIDER_ID.equals(customData.getProviderId())
					&& customData.getValue() != null) {
				exprType = IExpressionType.JAVASCRIPT;
				value = customData.getValue();
			}
		}

		if (exprType != null)
			return new Expression(value, exprType);

		return null;
	}

	static boolean isNullExpression(Object value) {
		if (value == null)
			return true;
		if (value instanceof Expression && ((Expression) value).getExpression() == null)
			return true;
		return false;
	}

	/**
	 * Extracts the column name from the given column.
	 * 
	 * @param column the column to extract
	 * @return the column name, or null if it cannot be extracted.
	 */
	static String extractColumnName(Object column) {
		return columnNameHelper.extractColumnName(column);
	}
}
