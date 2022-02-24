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
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.AbstractScalarParameter;
import org.eclipse.birt.report.model.elements.interfaces.IAbstractScalarParameterModel;

/**
 * 
 * Represents the abstract scalar parameter types.
 * 
 * @see org.eclipse.birt.report.model.elements.AbstractScalarParameter
 */

public abstract class AbstractScalarParameterHandleImpl extends ParameterHandle
		implements IAbstractScalarParameterModel {

	/**
	 * Constructor.
	 * 
	 * @param module  the module
	 * @param element the model representation of the element
	 */

	public AbstractScalarParameterHandleImpl(Module module, DesignElement element) {
		super(module, element);
	}

	/**
	 * Sets the flag that indicates whether the value of the parameter is required.
	 * For string type parameter, if the value is required, it cannot be
	 * <code>null</code> or empty. For other type parameters, required value cannot
	 * be <code>null</code>.
	 * 
	 * @param isRequired <code>true</code> if the value is required. Otherwise
	 *                   <code>false</code>.
	 * @throws SemanticException if the property is locked.
	 */

	public void setIsRequired(boolean isRequired) throws SemanticException {
		setBooleanProperty(IS_REQUIRED_PROP, isRequired);
	}

	/**
	 * Tests whether the string value of the parameter is required. For string type
	 * parameter, if the value is required, it cannot be <code>null</code> or empty.
	 * For other type parameters, required value cannot be <code>null</code>.
	 * 
	 * @return <code>true</code> if the value is required. Otherwise
	 *         <code>false</code>.
	 */

	public boolean isRequired() {
		return getBooleanProperty(IS_REQUIRED_PROP);
	}

	/**
	 * Sets the expression by which the result sorts.
	 * 
	 * @param sortByColumn expression by which the result sorts
	 * @throws SemanticException
	 */
	public void setSortByColumn(String sortByColumn) throws SemanticException {
		setStringProperty(SORT_BY_COLUMN_PROP, sortByColumn);
	}

	/**
	 * Gets the expression by which the result sorts.
	 * 
	 * @return the expression by which the result sorts
	 */
	public String getSortByColumn() {
		return getStringProperty(SORT_BY_COLUMN_PROP);
	}

	/**
	 * Sets the sort order for parameter values when preview. The input argument can
	 * be
	 * 
	 * <ul>
	 * <li>DesignChoiceConstants.SORT_DIRECTION_ASC
	 * <li>DesignChoiceConstants.SORT_DIRECTION_DESC
	 * <li><code>null</code>
	 * </ul>
	 * 
	 * @param direction
	 * 
	 * @throws SemanticException if the property is locked.
	 */

	public void setSortDirection(String direction) throws SemanticException {
		setProperty(SORT_DIRECTION_PROP, direction);
	}

	/**
	 * Gets the sort order for parameter values when preview. The return value can
	 * be
	 * 
	 * <ul>
	 * <li>DesignChoiceConstants.SORT_DIRECTION_ASC
	 * <li>DesignChoiceConstants.SORT_DIRECTION_DESC
	 * <li><code>null</code>
	 * </ul>
	 * 
	 * @return the sort order for parameter values
	 */

	public String getSortDirection() {
		return getStringProperty(SORT_DIRECTION_PROP);
	}

	/**
	 * Sets the sort key for parameter values when preview. The input argument can
	 * be
	 * 
	 * <ul>
	 * <li>DesignChoiceConstants.PARAM_SORT_VALUES_VALUE
	 * <li>DesignChoiceConstants.PARAM_SORT_VALUES_LABEL
	 * </ul>
	 * 
	 * @param sortValue
	 * 
	 * @throws SemanticException if the property is locked.
	 */

	public void setSortBy(String sortValue) throws SemanticException {
		setProperty(SORT_BY_PROP, sortValue);
	}

	/**
	 * Gets the sort key for parameter values when preview. The return value can be
	 * 
	 * <ul>
	 * <li>DesignChoiceConstants.PARAM_SORT_VALUES_VALUE
	 * <li>DesignChoiceConstants.PARAM_SORT_VALUES_LABEL
	 * </ul>
	 * 
	 * @return the sort key for parameter values
	 */

	public String getSortBy() {
		return getStringProperty(SORT_BY_PROP);
	}

	/**
	 * Returns the parameter type for this scalar parameter. Types are defined in
	 * <code>DesignChoiceConstants</code> can be one of the followings:
	 * 
	 * <ul>
	 * <li><code>PARAM_VALUE_TYPE_STATIC</code>
	 * <li><code>
	 * PARAM_VALUE_TYPE_DYNAMIC</code>
	 * </ul>
	 * 
	 * @return the type for the scalar parameter
	 * 
	 * @see #setValueType(String)
	 * @see org.eclipse.birt.report.model.api.elements.DesignChoiceConstants
	 * 
	 */

	public String getValueType() {
		return getStringProperty(VALUE_TYPE_PROP);
	}

	/**
	 * Sets the parameter value type for this scalar parameter. Types are defined in
	 * <code>DesignChoiceConstants</code> can be one of the followings:
	 * 
	 * <ul>
	 * <li><code>PARAM_TYPE_STATIC</code>
	 * <li><code>PARAM_TYPE_DYNAMIC</code>
	 * </ul>
	 * 
	 * @param type the type for the scalar parameter
	 * 
	 * @throws SemanticException if the input type is not one of above choices.
	 * @see #getValueType()
	 * @see org.eclipse.birt.report.model.api.elements.DesignChoiceConstants
	 * 
	 */

	public void setValueType(String type) throws SemanticException {
		setStringProperty(VALUE_TYPE_PROP, type);
	}

	/**
	 * Set the value for the list limitation number. This property is used to limit
	 * the parameter display list.
	 * 
	 * @param listLimit The limited number.
	 * 
	 * @throws SemanticException
	 * 
	 */

	public void setListlimit(int listLimit) throws SemanticException {
		setIntProperty(LIST_LIMIT_PROP, listLimit);
	}

	/**
	 * get the list limited number.
	 * 
	 * @return the display prompt.
	 * 
	 */

	public int getListlimit() {
		return getIntProperty(LIST_LIMIT_PROP);
	}

	/**
	 * Returns an expression on the data row from the dynamic list data set that
	 * returns the value for the choice.
	 * 
	 * @return the expression that returns the parameter value for each row in the
	 *         dynamic list.
	 */

	public String getValueExpr() {
		return getStringProperty(VALUE_EXPR_PROP);
	}

	/**
	 * Sets an expression on the data row from the dynamic list data set that
	 * returns the value for the choice.
	 * 
	 * @param valueExpr the expression that returns the parameter value for each row
	 *                  in the dynamic list.
	 * 
	 * @throws SemanticException if the property is locked.
	 */

	public void setValueExpr(String valueExpr) throws SemanticException {
		setStringProperty(VALUE_EXPR_PROP, valueExpr);
	}

	/**
	 * Returns an expression on the data row from the dynamic list data set that
	 * returns the prompt for the choice.
	 * 
	 * @return an expression that returns the display value for each row in the
	 *         dynamic list.
	 */

	public String getLabelExpr() {
		return getStringProperty(LABEL_EXPR_PROP);
	}

	/**
	 * Sets an expression on the data row from the dynamic list data set that
	 * returns the prompt for the choice.
	 * 
	 * @param labelExpr an expression that returns the display value for each row in
	 *                  the dynamic list.
	 * 
	 * @throws SemanticException if the property is locked.
	 */

	public void setLabelExpr(String labelExpr) throws SemanticException {
		setStringProperty(LABEL_EXPR_PROP, labelExpr);
	}

	/**
	 * Sets the data set name of the dynamic list for this parameter.
	 * 
	 * @param dataSetName the data set name of the dynamic list
	 * @throws SemanticException if the property is locked.
	 */

	public void setDataSetName(String dataSetName) throws SemanticException {
		setStringProperty(DATASET_NAME_PROP, dataSetName);
	}

	/**
	 * Sets the data set of the report item.
	 * 
	 * @param handle the handle of the data set, if <code>handle</code> is null,
	 *               data set property will be cleared.
	 * 
	 * @throws SemanticException if the property is locked.
	 */
	public void setDataSet(DataSetHandle handle) throws SemanticException {
		if (handle == null)
			setStringProperty(DATASET_NAME_PROP, null);
		else {
			ModuleHandle moduleHandle = handle.getRoot();
			String valueToSet = handle.getElement().getFullName();
			if (moduleHandle instanceof LibraryHandle) {
				String namespace = ((LibraryHandle) moduleHandle).getNamespace();
				valueToSet = StringUtil.buildQualifiedReference(namespace, valueToSet);
			}
			setStringProperty(DATASET_NAME_PROP, valueToSet);
		}
	}

	/**
	 * Returns the iterator for the static selection list defined on this scalar
	 * parameter. Each element in the iterator is the an instance of
	 * <code>SelectionChoiceHandle</code>.
	 * 
	 * @return the iterator for selection list defined on this scalar parameter.
	 * 
	 * @see org.eclipse.birt.report.model.api.elements.structures.SelectionChoice
	 */

	public Iterator choiceIterator() {
		PropertyHandle propHandle = getPropertyHandle(SELECTION_LIST_PROP);
		return propHandle.iterator();
	}

	/**
	 * Returns the data set name of the dynamic list for this parameter.
	 * 
	 * @return the data set name of the dynamic list
	 */

	public String getDataSetName() {
		return getStringProperty(DATASET_NAME_PROP);
	}

	/**
	 * Returns the handle for the data set defined on the parameter. If the
	 * parameter do not define the data set name or if the data set is not defined
	 * in the design/library scope, return <code>null</code>.
	 * 
	 * @return the handle to the data set
	 */

	public DataSetHandle getDataSet() {
		DesignElement dataSet = ((AbstractScalarParameter) getElement()).getDataSetElement(module);
		if (dataSet == null)
			return null;

		return (DataSetHandle) dataSet.getHandle(dataSet.getRoot());
	}

	/**
	 * Sets the default value list of the parameter. Each item in the list can be an
	 * expression, but cannot reference any other parameters.
	 * 
	 * @param defaultValueList the default value for the parameter
	 * @throws SemanticException if the property is locked.
	 */

	public void setDefaultValueList(List<? extends Object> defaultValueList) throws SemanticException {
		setProperty(DEFAULT_VALUE_PROP, defaultValueList);
	}

	/**
	 * Returns the default value list of the parameter. Each item in this list can
	 * be an expression, but cannot reference any other parameters.
	 * 
	 * @return the default value
	 */

	public List getDefaultValueList() {
		return getListProperty(DEFAULT_VALUE_PROP);
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
	 * @see #setDataType(String)
	 * @see org.eclipse.birt.report.model.api.elements.DesignChoiceConstants
	 */

	public String getDataType() {
		return getStringProperty(DATA_TYPE_PROP);
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
	 * @see #getDataType()
	 * @see org.eclipse.birt.report.model.api.elements.DesignChoiceConstants
	 */

	public void setDataType(String type) throws SemanticException {
		setStringProperty(DATA_TYPE_PROP, type);
	}

	/**
	 * Sets the flag that indicates whether duplicate values should be shown when
	 * preview.
	 * 
	 * @param distinct <code>true</code> if duplicate values only show once.
	 *                 Otherwise <code>false</code>.
	 * @throws SemanticException if the property is locked.
	 */

	public void setDistinct(boolean distinct) throws SemanticException {
		setBooleanProperty(DISTINCT_PROP, distinct);
	}

	/**
	 * Checks whether duplicate values should be shown when preview.
	 * 
	 * @return <code>true</code> if duplicate values only show once. Otherwise
	 *         <code>false</code>.
	 */

	public boolean distinct() {
		return getBooleanProperty(DISTINCT_PROP);
	}

}
