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

package org.eclipse.birt.report.model.api;

import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.elements.structures.FormatValue;
import org.eclipse.birt.report.model.api.elements.structures.ParameterFormatValue;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.interfaces.IAbstractScalarParameterModel;
import org.eclipse.birt.report.model.elements.interfaces.IScalarParameterModel;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.metadata.PropertyType;
import org.eclipse.birt.report.model.util.BoundDataColumnUtil;
import org.eclipse.birt.report.model.util.UnusedBoundColumnsMgr;

/**
 * Represents a scalar (single-value) report parameter. If the user enters no
 * value for a parameter, then the default value is used. If there is no default
 * value, then BIRT checks if <code>null</code> is allowed. If so, the value of
 * the parameter is null. If nulls are not allowed, then the user must enter a
 * value.
 * <p>
 * Scalar parameters can have static or dynamic selection lists.
 * <ul>
 * <li>The parameter static selection list provides a developer-defined list of
 * choices. Every choice has two parts: a choice and a label. The label can be
 * externalized and appears in the UI. The choice is the value passed to the
 * report.
 * <li>This parameter can define a dynamic selection list for the parameter. The
 * data set can reference other parameters by referring to a data set. The data
 * set must return a column that contains the choice values. It may also contain
 * a column that returns the labels for the values. All other columns are
 * ignored.
 * </ul>
 * <p>
 * 
 * 
 * @see org.eclipse.birt.report.model.elements.ScalarParameter
 * @see ParameterHandle
 */

public class ScalarParameterHandle extends AbstractScalarParameterHandle implements IScalarParameterModel {

	/**
	 * Constructs a handle for the ScalarParamter with the given design and the
	 * parameter. The application generally does not create handles directly.
	 * Instead, it uses one of the navigation methods available on other element
	 * handles.
	 * 
	 * @param module  the module
	 * @param element the model representation of the element
	 */

	public ScalarParameterHandle(Module module, DesignElement element) {
		super(module, element);
	}

	/**
	 * Returns the first default value of the parameter. The default value can be an
	 * expression, but cannot reference any other parameters. The default value of
	 * this parameter can be a list. This method returns the first value if exists.
	 * 
	 * @return the default value
	 * @deprecated since 2.5 replaced by {@link #getDefaultValueList()}
	 */

	public String getDefaultValue() {
		List<Expression> valueList = getDefaultValueList();
		if (valueList == null || valueList.isEmpty())
			return null;

		PropertyType tmpType = MetaDataDictionary.getInstance().getPropertyType(IPropertyType.EXPRESSION_TYPE);
		PropertyDefn tmpPropDefn = (PropertyDefn) getPropertyDefn(DEFAULT_VALUE_PROP);

		return tmpType.toString(getModule(), tmpPropDefn, valueList.get(0));
	}

	/**
	 * Sets the default value of the parameter. The default value can be an
	 * expression, but cannot reference any other parameters.
	 * 
	 * @param defaultValue the default value for the parameter
	 * @throws SemanticException if the property is locked.
	 * @deprecated since 2.5 replaced by {@link #setDefaultValueList(List)}
	 */

	public void setDefaultValue(String defaultValue) throws SemanticException {
		setProperty(DEFAULT_VALUE_PROP, defaultValue);
	}

	/**
	 * Tests whether hides the user's entry by displaying asterisks. Often used for
	 * passwords.
	 * 
	 * @return <code>true</code> if hides the user's entry by asterisks,
	 *         <code>false</code> if shows characters as usual.
	 */

	public boolean isConcealValue() {
		return getBooleanProperty(CONCEAL_VALUE_PROP);
	}

	/**
	 * Sets the attribute that's hides the user's entry by displaying asterisks.
	 * Often used for passwords.
	 * 
	 * @param concealValue <code>true</code> if hides the user's entry by asterisks,
	 *                     <code>false</code> if shows characters as usual.
	 * @throws SemanticException if the property is locked.
	 */

	public void setConcealValue(boolean concealValue) throws SemanticException {
		setBooleanProperty(CONCEAL_VALUE_PROP, concealValue);
	}

	/**
	 * Tests whether the value of the parameter can be <code>null</code>.
	 * 
	 * @return <code>true</code> if the value can be <code>null</code>,
	 *         <code>false</code> if the value can not be <code>null</code>.
	 * 
	 * @deprecated by {@link #isRequired()}
	 */

	public boolean allowNull() {
		return !getBooleanProperty(IS_REQUIRED_PROP);
	}

	/**
	 * Sets the flag that indicates whether the value of the parameter can be
	 * <code>null</code>.
	 * 
	 * @param allowNull <code>true</code> if the value can be <code>null</code>,
	 *                  <code>false</code> if the value can not be
	 *                  <code>null</code>.
	 * @throws SemanticException if the property is locked.
	 * 
	 * @deprecated by {@link #setIsRequired(boolean)}
	 */

	public void setAllowNull(boolean allowNull) throws SemanticException {
		setBooleanProperty(ALLOW_NULL_PROP, allowNull);
	}

	/**
	 * Tests whether the string value of the parameter can be <code>null</code>.
	 * 
	 * @return <code>true</code> if the value can be <code>null</code>,
	 *         <code>false</code> if the value can not be <code>null</code>.
	 * 
	 * @deprecated by {@link #isRequired()}
	 */

	public boolean allowBlank() {
		String dataType = getStringProperty(DATA_TYPE_PROP);
		if (DesignChoiceConstants.PARAM_TYPE_STRING.equalsIgnoreCase(dataType))
			return !getBooleanProperty(IS_REQUIRED_PROP);

		return false;
	}

	/**
	 * Sets the flag that indicates whether the string value of the parameter can be
	 * <code>null</code>.
	 * 
	 * @param allowBlank <code>true</code> if the value can be <code>null</code>,
	 *                   <code>false</code> if the value can not be
	 *                   <code>null</code>.
	 * @throws SemanticException if the property is locked.
	 * 
	 * @deprecated by {@link #setIsRequired(boolean)}
	 */

	public void setAllowBlank(boolean allowBlank) throws SemanticException {
		setBooleanProperty(ALLOW_BLANK_PROP, allowBlank);
	}

	/**
	 * Returns the format instructions for the parameter value. The format is used
	 * by the UI to display the value.
	 * 
	 * @return the format for the parameter value
	 * @deprecated replaced by getPattern and getCategory.
	 */

	public String getFormat() {
		return getPattern();
	}

	/**
	 * Returns the pattern of format instructions for the parameter value. The
	 * format is used by the UI to display the value.
	 * 
	 * @return the pattern of format for the parameter value
	 */

	public String getPattern() {
		Object value = getProperty(FORMAT_PROP);
		if (value == null)
			return null;

		assert value instanceof ParameterFormatValue;

		return ((ParameterFormatValue) value).getPattern();
	}

	/**
	 * Returns the category for the parameter format. The format is used by the UI
	 * to display the value.
	 * 
	 * @return the category for the parameter format
	 */

	public String getCategory() {
		Object value = getProperty(FORMAT_PROP);
		if (value == null)
			return null;

		assert value instanceof ParameterFormatValue;

		return ((ParameterFormatValue) value).getCategory();
	}

	/**
	 * Sets the format instructions for the parameter value. The format is used by
	 * the UI to display the value.
	 * 
	 * @param format the format for the parameter value
	 * @throws SemanticException if the property is locked.
	 * @deprecated replaced by setPattern and setCategory.
	 */

	public void setFormat(String format) throws SemanticException {
		setPattern(format);
	}

	/**
	 * Sets the pattern of format instructions for the parameter value. The format
	 * is used by the UI to display the value.
	 * 
	 * @param pattern the format for the parameter value
	 * @throws SemanticException if the property is locked.
	 */

	public void setPattern(String pattern) throws SemanticException {

		Object value = element.getLocalProperty(module, FORMAT_PROP);

		if (value == null) {
			FormatValue formatValueToSet = new ParameterFormatValue();
			formatValueToSet.setPattern(pattern);
			setProperty(FORMAT_PROP, formatValueToSet);
		} else {
			PropertyHandle propHandle = getPropertyHandle(FORMAT_PROP);
			FormatValue formatValueToSet = (FormatValue) value;
			FormatValueHandle formatHandle = (FormatValueHandle) formatValueToSet.getHandle(propHandle);

			formatHandle.setPattern(pattern);
		}
	}

	/**
	 * Sets the category for the parameter format. The format is used by the UI to
	 * display the value.
	 * 
	 * @param category the category for the format
	 * @throws SemanticException if the property is locked.
	 */

	public void setCategory(String category) throws SemanticException {

		Object value = element.getLocalProperty(module, FORMAT_PROP);

		if (value == null) {
			FormatValue formatValueToSet = new ParameterFormatValue();
			formatValueToSet.setCategory(category);
			setProperty(FORMAT_PROP, formatValueToSet);
		} else {
			PropertyHandle propHandle = getPropertyHandle(FORMAT_PROP);
			FormatValue formatValueToSet = (FormatValue) value;
			FormatValueHandle formatHandle = (FormatValueHandle) formatValueToSet.getHandle(propHandle);

			formatHandle.setCategory(category);
		}
	}

	/**
	 * Returns the control type for this parameter. Control types are one of
	 * constants defined in <code>DesignChoiceConstants</code>:
	 * 
	 * <ul>
	 * <li>PARAM_CONTROL_TEXT_BOX
	 * <li>PARAM_CONTROL_LIST_BOX
	 * <li>PARAM_CONTROL_COMBOBOX
	 * <li>PARAM_CONTROL_RADIO_BUTTON
	 * <li>PARAM_CONTROL_CHECK_BOX
	 * <li>PARAM_CONTROL_AUTO_SUGGEST
	 * </ul>
	 * 
	 * @return the control type for the UI to display the parameter
	 * 
	 * @see org.eclipse.birt.report.model.api.elements.DesignChoiceConstants
	 */

	public String getControlType() {
		return getStringProperty(IAbstractScalarParameterModel.CONTROL_TYPE_PROP);
	}

	/**
	 * get the list limited number.
	 * 
	 * @return the display prompt.
	 * 
	 */

	public int getListlimit() {
		// if the parameter control type is auto-suggest, the list limit should
		// return 0.
		if (DesignChoiceConstants.PARAM_CONTROL_AUTO_SUGGEST.equals(getControlType()))
			return 0;
		return getIntProperty(LIST_LIMIT_PROP);
	}

	/**
	 * Sets the control type for this parameter. Control types are one of constants
	 * defined in <code>DesignChoiceConstants</code>:
	 * 
	 * <ul>
	 * <li>PARAM_CONTROL_TEXT_BOX
	 * <li>PARAM_CONTROL_LIST_BOX
	 * <li>PARAM_CONTROL_COMBOBOX
	 * <li>PARAM_CONTROL_RADIO_BUTTON
	 * <li>PARAM_CONTROL_CHECK_BOX
	 * <li>PARAM_CONTROL_AUTO_SUGGEST
	 * </ul>
	 * 
	 * @param controlType the control type for the UI to display the parameter
	 * 
	 * @throws SemanticException if the input type is not one of above choices.
	 * @see #getDataType()
	 * @see org.eclipse.birt.report.model.api.elements.DesignChoiceConstants
	 */

	public void setControlType(String controlType) throws SemanticException {
		setStringProperty(IAbstractScalarParameterModel.CONTROL_TYPE_PROP, controlType);
	}

	/**
	 * Returns the alignment for this parameter. Alignments can be one of the
	 * constants defined in <code>DesignChoiceConstants</code>:
	 * 
	 * <ul>
	 * <li><code>SCALAR_PARAM_ALIGN_AUTO</code>
	 * <li><code>
	 * SCALAR_PARAM_ALIGN_LEFT</code>
	 * <li><code>SCALAR_PARAM_ALIGN_CENTER</code>
	 * <li><code>SCALAR_PARAM_ALIGN_RIGHT</code>
	 * </ul>
	 * 
	 * @return the alignment for the UI to display the parameter
	 * 
	 * @see org.eclipse.birt.report.model.api.elements.DesignChoiceConstants
	 */

	public String getAlignment() {
		return getStringProperty(ALIGNMENT_PROP);
	}

	/**
	 * Sets the alignment for this parameter. Alignments can be one of the constants
	 * defined in <code>DesignChoiceConstants</code>:
	 * 
	 * <ul>
	 * <li><code>SCALAR_PARAM_ALIGN_AUTO</code>
	 * <li><code>
	 * SCALAR_PARAM_ALIGN_LEFT</code>
	 * <li><code>SCALAR_PARAM_ALIGN_CENTER</code>
	 * <li><code>SCALAR_PARAM_ALIGN_RIGHT</code>
	 * </ul>
	 * 
	 * @param align the alignment for the UI to display the parameter
	 * 
	 * @throws SemanticException if the input type is not one of above choices.
	 * @see org.eclipse.birt.report.model.api.elements.DesignChoiceConstants
	 */

	public void setAlignment(String align) throws SemanticException {
		setStringProperty(ALIGNMENT_PROP, align);
	}

	/**
	 * Tests whether the value must match one of values in the selection list.
	 * 
	 * 
	 * @return <code>true</code> if the value must match one of values in the list,
	 *         otherwise <code>false</code>.
	 */

	public boolean isMustMatch() {
		return getBooleanProperty(MUCH_MATCH_PROP);
	}

	/**
	 * Sets the flag indicates that whether the value must match one of values in
	 * the selection list.
	 * 
	 * @param mustMatch <code>true</code> if the value must match one of values in
	 *                  the list, otherwise <code>false</code>.
	 * @throws SemanticException if the property is locked.
	 */

	public void setMustMatch(boolean mustMatch) throws SemanticException {
		setBooleanProperty(MUCH_MATCH_PROP, mustMatch);
	}

	/**
	 * Tests whether to display values in the order defined in the list.
	 * 
	 * 
	 * @return <code>true</code> if to display value in the order, otherwise
	 *         <code>false</code>.
	 */

	public boolean isFixedOrder() {
		return getBooleanProperty(FIXED_ORDER_PROP);
	}

	/**
	 * Sets the flag indicates that whether to display values in the order defined
	 * in the list.
	 * 
	 * @param fixedOrder <code>true</code> if to display values in the order,
	 *                   otherwise <code>false</code>.
	 * @throws SemanticException if the property is locked.
	 */

	public void setFixedOrder(boolean fixedOrder) throws SemanticException {
		setBooleanProperty(FIXED_ORDER_PROP, fixedOrder);
	}

	/**
	 * Returns the name of the query column that returns values for the choice of
	 * the dynamic list for this parameter.
	 * 
	 * @return the the name of the query column
	 * 
	 * @deprecated Replaced by the method {@link #getValueExpr()}
	 */

	public String getValueColumn() {
		return getValueExpr();
	}

	/**
	 * Sets the name of the query column that returns values for the choice of the
	 * dynamic list for this parameter.
	 * 
	 * @param valueColumn the name of the query column
	 * @throws SemanticException if the property is locked.
	 * 
	 * @deprecated Replaced by the method {@link #setValueExpr(String)}
	 */

	public void setValueColumn(String valueColumn) throws SemanticException {
		setValueExpr(valueColumn);
	}

	/**
	 * Returns the name of the query column that returns the prompt for the choice
	 * of the dynamic list for this parameter.
	 * 
	 * @return the the name of the query column
	 * 
	 * @deprecated Replaced by the method {@link #getLabelExpr()}
	 */

	public String getLabelColumn() {
		return getLabelExpr();
	}

	/**
	 * Sets the name of the query column that returns the prompt for the choice of
	 * the dynamic list for this parameter.
	 * 
	 * @param labelColumn the name of the query column
	 * @throws SemanticException if the property is locked.
	 * 
	 * @deprecated Replaced by the method {@link #setLabelExpr(String)}
	 */

	public void setLabelColumn(String labelColumn) throws SemanticException {
		setLabelExpr(labelColumn);
	}

	/**
	 * Returns the data type for this parameter. The data type controls how the
	 * requester formats, parses and validates the parameter. Types are defined in
	 * <code>DesignChoiceConstants</code> can be one of the followings:
	 * 
	 * <ul>
	 * <li><code>PARAM_TYPE_STRING</code>
	 * <li><code>PARAM_TYPE_FLOAT</code>
	 * <li><code>PARAM_TYPE_DECIMAL</code>
	 * <li><code>PARAM_TYPE_INTEGER</code>
	 * <li><code>PARAM_TYPE_DATETIME</code>
	 * <li><code>PARAM_TYPE_DATE</code>
	 * <li><code>PARAM_TYPE_TIME</code>
	 * <li><code>PARAM_TYPE_BOOLEAN</code>
	 * </ul>
	 * 
	 * @return the type for the parameter
	 * 
	 * @see #setType(String)
	 * @see org.eclipse.birt.report.model.api.elements.DesignChoiceConstants
	 * 
	 * @deprecated Replaced by the method {@link #getDataType()}
	 */

	public String getType() {
		return getDataType();
	}

	/**
	 * Sets the data type for this parameter. The data type controls how the
	 * Requester formats, parses and validates the parameter. Types are defined in
	 * <code>DesignChoiceConstants</code> can be one of the followings:
	 * 
	 * <ul>
	 * <li><code>PARAM_TYPE_STRING</code>
	 * <li><code>PARAM_TYPE_FLOAT</code>
	 * <li><code>PARAM_TYPE_DECIMAL</code>
	 * <li><code>PARAM_TYPE_INTEGER</code>
	 * <li><code>PARAM_TYPE_DATETIME</code>
	 * <li><code>PARAM_TYPE_DATE</code>
	 * <li><code>PARAM_TYPE_TIME</code>
	 * <li><code>PARAM_TYPE_BOOLEAN</code>
	 * </ul>
	 * 
	 * @param type the type for the parameter
	 * 
	 * @throws SemanticException if the input type is not one of above choices.
	 * @see #getType()
	 * @see org.eclipse.birt.report.model.api.elements.DesignChoiceConstants
	 * 
	 * @deprecated Replaced by the method {@link #setDataType(String)}
	 */

	public void setType(String type) throws SemanticException {
		setDataType(type);
	}

	/**
	 * Returns the bound columns that binds the data set columns. The item in the
	 * iterator is the corresponding <code>ComputedColumnHandle</code>.
	 * 
	 * @return a list containing the bound columns.
	 */

	public Iterator columnBindingsIterator() {
		PropertyHandle propHandle = getPropertyHandle(BOUND_DATA_COLUMNS_PROP);
		return propHandle.iterator();
	}

	/**
	 * Get a handle to deal with the bound column.
	 * 
	 * @return a handle to deal with the bound data column.
	 */

	public PropertyHandle getColumnBindings() {
		return getPropertyHandle(BOUND_DATA_COLUMNS_PROP);
	}

	/**
	 * Adds a bound column to the list.
	 * 
	 * @param addColumn the bound column to add
	 * @param inForce   <code>true</code> the column is added to the list regardless
	 *                  of duplicate expression. <code>false</code> do not add the
	 *                  column if the expression already exist
	 * @param column    the bound column
	 * @return the newly created <code>ComputedColumnHandle</code> or the existed
	 *         <code>ComputedColumnHandle</code> in the list
	 * @throws SemanticException if expression is not duplicate but the name
	 *                           duplicates the existing bound column. Or, if the
	 *                           both name/expression are duplicate, but
	 *                           <code>inForce</code> is <code>true</code>.
	 */

	public ComputedColumnHandle addColumnBinding(ComputedColumn addColumn, boolean inForce) throws SemanticException {
		if (addColumn == null)
			return null;

		List columns = (List) getProperty(BOUND_DATA_COLUMNS_PROP);
		if (columns == null)
			return (ComputedColumnHandle) getPropertyHandle(BOUND_DATA_COLUMNS_PROP).addItem(addColumn);
		ComputedColumn column = BoundDataColumnUtil.getColumn(columns, addColumn);
		if (column != null && !inForce) {
			return (ComputedColumnHandle) column.handle(getPropertyHandle(BOUND_DATA_COLUMNS_PROP),
					columns.indexOf(column));
		}
		return (ComputedColumnHandle) getPropertyHandle(BOUND_DATA_COLUMNS_PROP).addItem(addColumn);
	}

	/**
	 * Removed unused bound columns from the parameter. Bound columns of nested
	 * elements will not be removed.
	 * 
	 * @throws SemanticException if bound column property is locked.
	 */

	public void removedUnusedColumnBindings() throws SemanticException {
		UnusedBoundColumnsMgr.removedUnusedBoundColumns(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.DesignElementHandle#setProperty(java
	 * .lang.String, java.lang.Object)
	 */

	public void setProperty(String propName, Object value) throws SemanticException {
		if (ALLOW_BLANK_PROP.equalsIgnoreCase(propName) || ALLOW_NULL_PROP.equalsIgnoreCase(propName)) {
			Boolean newValue = (Boolean) value;
			if (newValue != null) {
				newValue = Boolean.valueOf(!((Boolean) value).booleanValue());
			}

			// allowBlank only applies to string type.

			if (ALLOW_BLANK_PROP.equalsIgnoreCase(propName)) {
				String dataType = super.getStringProperty(DATA_TYPE_PROP);
				if (DesignChoiceConstants.PARAM_TYPE_STRING.equalsIgnoreCase(dataType))
					super.setProperty(IS_REQUIRED_PROP, newValue);

				return;
			}

			super.setProperty(IS_REQUIRED_PROP, newValue);

			return;
		}

		super.setProperty(propName, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.DesignElementHandle#getProperty(java
	 * .lang.String)
	 */

	public Object getProperty(String propName) {
		if (ALLOW_BLANK_PROP.equalsIgnoreCase(propName)) {
			Boolean retValue = null;
			String dataType = super.getStringProperty(DATA_TYPE_PROP);
			if (DesignChoiceConstants.PARAM_TYPE_STRING.equalsIgnoreCase(dataType))
				retValue = Boolean.valueOf(!getBooleanProperty(IS_REQUIRED_PROP));
			else
				retValue = Boolean.FALSE;

			return retValue;
		} else if (ALLOW_NULL_PROP.equalsIgnoreCase(propName)) {
			return Boolean.valueOf(!getBooleanProperty(IS_REQUIRED_PROP));
		}

		return super.getProperty(propName);
	}

	/**
	 * Returns the parameter type for this scalar parameter. Types are defined in
	 * <code>DesignChoiceConstants</code> can be one of the followings:
	 * 
	 * <ul>
	 * <li><code>SCALAR_PARAM_TYPE_SIMPLE</code>
	 * <li><code>
	 * SCALAR_PARAM_TYPE_MULTI_VALUE</code>
	 * <li><code>SCALAR_PARAM_TYPE_AD_HOC
	 * </code>
	 * </ul>
	 * 
	 * @return the type for the parameter
	 * 
	 * @see #setParamType(String)
	 * @see org.eclipse.birt.report.model.api.elements.DesignChoiceConstants
	 */

	public String getParamType() {
		return getStringProperty(PARAM_TYPE_PROP);
	}

	/**
	 * Sets the parameter type for this scalar parameter. Types are defined in
	 * <code>DesignChoiceConstants</code> can be one of the followings:
	 * 
	 * <ul>
	 * <li><code>SCALAR_PARAM_TYPE_SIMPLE</code>
	 * <li><code>
	 * SCALAR_PARAM_TYPE_MULTI_VALUE</code>
	 * <li><code>SCALAR_PARAM_TYPE_AD_HOC
	 * </code>
	 * </ul>
	 * 
	 * @param type the type for the parameter
	 * 
	 * @throws SemanticException if the input type is not one of above choices.
	 * @see #getParamType()
	 * @see org.eclipse.birt.report.model.api.elements.DesignChoiceConstants
	 */

	public void setParamType(String type) throws SemanticException {
		setStringProperty(PARAM_TYPE_PROP, type);
	}

	/**
	 * Returns the maximal number of of entries a report parameter pick list can
	 * have.
	 * 
	 * @return the threshold number.
	 */

	public int getAutoSuggestThreshold() {
		return getIntProperty(AUTO_SUGGEST_THRESHOLD_PROP);
	}

	/**
	 * Sets the maximal number of of entries a report parameter pick list can have.
	 * 
	 * @param number the threshold number.
	 * @throws SemanticException
	 */

	public void setAutoSuggestThreshold(int number) throws SemanticException {
		setIntProperty(AUTO_SUGGEST_THRESHOLD_PROP, number);
	}

	/**
	 * Gets the method content of <code>getDefaultValueList</code>.
	 * 
	 * @return the method content of <code>getDefaultValueList</code>
	 */
	public String getDefaultValueListMethod() {
		return getStringProperty(GET_DEFAULT_VALUE_LIST_PROP);
	}

	/**
	 * Sets the method content of <code>getDefaultValueList</code>.
	 * 
	 * @param getDefaultValueListMethod the method content of
	 *                                  <code>getDefaultValueList</code> to set
	 * @throws SemanticException
	 */
	public void setDefaultValueListMethod(String getDefaultValueListMethod) throws SemanticException {
		setStringProperty(GET_DEFAULT_VALUE_LIST_PROP, getDefaultValueListMethod);
	}

	/**
	 * Gets the method content of <code>getSelectionValueList</code>.
	 * 
	 * @return the method content of <code>getSelectionValueList</code>
	 */
	public String getSelectionValueListMethod() {
		return getStringProperty(GET_SELECTION_VALUE_LIST_PROP);
	}

	/**
	 * Sets the method content of <code>getSelectionValueList</code>.
	 * 
	 * @param getSelectionValueListMethod the method content of
	 *                                    <code>getSelectionValueList</code> to set
	 * @throws SemanticException
	 */
	public void setSelectionValueListMethod(String getSelectionValueListMethod) throws SemanticException {
		setStringProperty(GET_SELECTION_VALUE_LIST_PROP, getSelectionValueListMethod);
	}

}
