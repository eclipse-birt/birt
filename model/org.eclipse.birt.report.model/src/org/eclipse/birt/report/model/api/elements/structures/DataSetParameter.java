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

package org.eclipse.birt.report.model.api.elements.structures;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.model.api.DataSetParameterHandle;
import org.eclipse.birt.report.model.api.SimpleValueHandle;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.PropertyStructure;
import org.eclipse.birt.report.model.util.DataTypeConversionUtil;

/**
 * Represents the parameter for ODA drivers. The parameter is the part of the
 * data set definition, if defined. A parameter can be an input or output
 * parameter. A parameter can also be input and output parameter. Each data set
 * parameter has the following properties:
 * 
 * <p>
 * <dl>
 * <dt><strong>Name </strong></dt>
 * <dd>a data set parameter has a required name.</dd>
 * 
 * <dt><strong>Position </strong></dt>
 * <dd>a data set parameter has an optional position for it.</dd>
 * 
 * <dt><strong>Data Type </strong></dt>
 * <dd>a data set parameter has a choice data type: any, integer, string, data
 * time, decimal, float, structure or table.</dd>
 * 
 * <dt><strong>Is optional </strong></dt>
 * <dd>whether this parameter is optional.</dd>
 * 
 * <dt><strong>Allow Null </strong></dt>
 * <dd>whether the value of this parameter can be nullable.</dd>
 * 
 * <dt><strong>Is Input </strong></dt>
 * <dd>whether this parameter is an input parameter.</dd>
 * 
 * <dt><strong>Is Output </strong></dt>
 * <dd>whether this parameter is an output parameter.</dd>
 * </dl>
 * 
 * 
 */

public class DataSetParameter extends PropertyStructure {

	/**
	 * Name of this structure. Matches the definition in the meta-data dictionary.
	 */

	public static final String STRUCT_NAME = "DataSetParam"; //$NON-NLS-1$

	/**
	 * Name of the position member.
	 */

	public static final String POSITION_MEMBER = "position"; //$NON-NLS-1$

	/**
	 * Name of the parameter name member.
	 */

	public static final String NAME_MEMBER = "name"; //$NON-NLS-1$

	/**
	 * Name of the parameter data type member.
	 */

	public static final String DATA_TYPE_MEMBER = "dataType"; //$NON-NLS-1$

	/**
	 * Name of the member indicating that whether the report must provide a value
	 * for this parameter.
	 */

	public static final String IS_OPTIONAL_MEMBER = "isOptional"; //$NON-NLS-1$

	/**
	 * Name of the parameter default value member.
	 */

	public static final String DEFAULT_VALUE_MEMBER = "defaultValue"; //$NON-NLS-1$

	/**
	 * Name of the member indicating that whether the value of this parameter can be
	 * <code>null</code>.
	 * 
	 * @deprecated
	 */

	public static final String IS_NULLABLE_MEMBER = "isNullable"; //$NON-NLS-1$

	/**
	 * Name of the member indicating that whether the value of this parameter can be
	 * <code>null</code>.
	 */

	public static final String ALLOW_NULL_MEMBER = "allowNull"; //$NON-NLS-1$

	/**
	 * Name of the member indicating that whether this is an input parameter.
	 */

	public static final String IS_INPUT_MEMBER = "isInput"; //$NON-NLS-1$

	/**
	 * Name of the member indicating that whether this is an output parameter.
	 */

	public static final String IS_OUTPUT_MEMBER = "isOutput"; //$NON-NLS-1$

	/**
	 * Name of the member indicating the native (database) data type code.
	 */

	public static final String NATIVE_DATA_TYPE_MEMBER = "nativeDataType"; //$NON-NLS-1$

	/**
	 * Name of the member indicating the display name of the parameter
	 */
	public static final String DISPLAY_NAME_MEMBER = "displayName"; //$NON-NLS-1$

	/**
	 * Name of the member indicating the display name id of the parameter
	 */
	public static final String DISPLAY_NAME_ID_MEMBER = "displayNameID"; //$NON-NLS-1$

	/**
	 * Name of the member indicating the heading of the parameter
	 */
	public static final String HEADING_MEMBER = "heading"; //$NON-NLS-1$

	/**
	 * Name of the member indicating the heading id of the parameter
	 */
	public static final String HEADING_ID_MEMBER = "headingID"; //$NON-NLS-1$

	/**
	 * Name of the member indicating the help text of the parameter
	 */
	public static final String HELP_TEXT_MEMBER = "helpText"; //$NON-NLS-1$

	/**
	 * Name of the member indicating the help text id of the parameter
	 */
	public static final String HELP_TEXT_ID_MEMBER = "helpTextID"; //$NON-NLS-1$

	/**
	 * Name of the member indicating the description of the parameter
	 */
	public static final String DESCRIPTION_MEMBER = "description"; //$NON-NLS-1$

	/**
	 * Name of the member indicating the description id of the parameter
	 */
	public static final String DESCRIPTION_ID_MEMBER = "descriptionID"; //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.OutputParameter#getStructName()
	 */

	public String getStructName() {
		return STRUCT_NAME;
	}

	/**
	 * Whether the parameter is optional.
	 * 
	 * @return whether the parameter is optional
	 */

	public boolean isOptional() {
		return ((Boolean) getProperty(null, IS_OPTIONAL_MEMBER)).booleanValue();
	}

	/**
	 * Sets whether the parameter is optional.
	 * 
	 * @param value the value to set
	 */

	public void setIsOptional(boolean value) {
		setProperty(IS_OPTIONAL_MEMBER, Boolean.valueOf(value));
	}

	/**
	 * Sets the default value of the input parameter.
	 * 
	 * @param expr the default value
	 */

	public void setDefaultValue(String expr) {
		setProperty(DEFAULT_VALUE_MEMBER, expr);
	}

	/**
	 * Gets the default value of the input parameter.
	 * 
	 * @return the default value
	 */

	public String getDefaultValue() {
		return getStringProperty(null, DEFAULT_VALUE_MEMBER);
	}

	/**
	 * Checks whether this parameter is an input parameter.
	 * 
	 * @return <code>true</code> if it is an input parameter. Otherwise
	 *         <code>false</code>.
	 */

	public boolean isInput() {
		return ((Boolean) getProperty(null, IS_INPUT_MEMBER)).booleanValue();
	}

	/**
	 * Sets whether this parameter is an input parameter.
	 * 
	 * @param isInput <code>true</code> if it is an input parameter. Otherwise
	 *                <code>false</code>.
	 */

	public void setIsInput(boolean isInput) {
		setProperty(IS_INPUT_MEMBER, Boolean.valueOf(isInput));
	}

	/**
	 * Checks whether the value of this parameter can be <code>null</code>.
	 * 
	 * @return <code>true</code> if the value can be <code>null</code>. Otherwise
	 *         <code>false</code>.
	 * @deprecated Use <code>allowNull()</code>
	 */

	public boolean isNullable() {
		return allowNull();
	}

	/**
	 * Sets whether the value of this parameter can be <code>null</code>.
	 * 
	 * @param isNullable <code>true</code> if the value can be <code>null</code>.
	 *                   Otherwise <code>false</code>.
	 * @deprecated Use <code>setAllowNull(boolean)</code>
	 */

	public void setIsNullable(boolean isNullable) {
		setAllowNull(isNullable);
	}

	/**
	 * Checks whether the value of this parameter can be <code>null</code>.
	 * 
	 * @return <code>true</code> if the value can be <code>null</code>. Otherwise
	 *         <code>false</code>.
	 */

	public boolean allowNull() {
		return ((Boolean) getProperty(null, ALLOW_NULL_MEMBER)).booleanValue();
	}

	/**
	 * Sets whether the value of this parameter can be <code>null</code>.
	 * 
	 * @param allowNull <code>true</code> if the value can be <code>null</code>.
	 *                  Otherwise <code>false</code>.
	 */

	public void setAllowNull(boolean allowNull) {
		setProperty(ALLOW_NULL_MEMBER, Boolean.valueOf(allowNull));
	}

	/**
	 * Checks whether this parameter is an output parameter.
	 * 
	 * @return <code>true</code> if it is an output parameter. Otherwise
	 *         <code>false</code>.
	 */

	public boolean isOutput() {
		return ((Boolean) getProperty(null, IS_OUTPUT_MEMBER)).booleanValue();
	}

	/**
	 * Sets whether this parameter is an output parameter.
	 * 
	 * @param isOutput <code>true</code> if it is an output parameter. Otherwise
	 *                 <code>false</code>.
	 */

	public void setIsOutput(boolean isOutput) {
		setProperty(IS_OUTPUT_MEMBER, Boolean.valueOf(isOutput));
	}

	/**
	 * Returns the parameter data type.
	 * 
	 * @return the parameter dataType
	 */

	public String getDataType() {
		String paramType = (String) getProperty(null, DATA_TYPE_MEMBER);

		// convert value in parameter type to column data type

		return DataTypeConversionUtil.converToColumnDataType(paramType);
	}

	/**
	 * Sets the parameter data type.
	 * 
	 * @param dataType the data type to set
	 */

	public void setDataType(String dataType) {
		// convert column data type to parameter type.

		String paramType = DataTypeConversionUtil.converToParamType(dataType);

		setProperty(DataSetParameter.DATA_TYPE_MEMBER, paramType);
	}

	/**
	 * Returns the parameter name.
	 * 
	 * @return the parameter name
	 */

	public String getName() {
		return getStringProperty(null, NAME_MEMBER);
	}

	/**
	 * Sets the parameter name.
	 * 
	 * @param name the name to set
	 */

	public void setName(String name) {
		setProperty(NAME_MEMBER, name);
	}

	/**
	 * Returns the position of this parameter.
	 * 
	 * @return the position of this parameter
	 */

	public Integer getPosition() {
		return (Integer) getProperty(null, POSITION_MEMBER);
	}

	/**
	 * Sets the position of this parameter.
	 * 
	 * @param position the position to set
	 */

	public void setPosition(Integer position) {
		setProperty(POSITION_MEMBER, position);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Structure#handle(org.eclipse.birt.
	 * report.model.api.SimpleValueHandle, int)
	 */

	public StructureHandle handle(SimpleValueHandle valueHandle, int index) {
		return new DataSetParameterHandle(valueHandle, index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Structure#validate(org.eclipse.birt
	 * .report.model.elements.ReportDesign,
	 * org.eclipse.birt.report.model.core.DesignElement)
	 */

	public List validate(Module module, DesignElement element) {
		ArrayList list = new ArrayList();

		if (StringUtil.isBlank(getName())) {
			list.add(new PropertyValueException(element, getDefn().getMember(NAME_MEMBER), getName(),
					PropertyValueException.DESIGN_EXCEPTION_VALUE_REQUIRED));
		}
		return list;
	}

	/**
	 * Returns the native data type.
	 * 
	 * @return the parameter native data type.
	 */

	public Integer getNativeDataType() {
		return (Integer) getProperty(null, NATIVE_DATA_TYPE_MEMBER);
	}

	/**
	 * Sets the parameter native data type.
	 * 
	 * @param dataType the native data type to set.
	 */

	public void setNativeDataType(Integer dataType) {
		setProperty(NATIVE_DATA_TYPE_MEMBER, dataType);
	}

	/**
	 * Returns the data type in parameter type choices of this parameter. The
	 * possible values are:
	 * 
	 * <ul>
	 * <li>PARAM_TYPE_ANY
	 * <li>PARAM_TYPE_INTEGER
	 * <li>PARAM_TYPE_STRING
	 * <li>PARAM_TYPE_DATETIME
	 * <li>PARAM_TYPE_DECIMAL
	 * <li>PARAM_TYPE_FLOAT
	 * <li>PARAM_TYPE_BOOLEAN
	 * </ul>
	 * 
	 * @return the data type of this parameter.
	 */

	public String getParameterDataType() {
		return getStringProperty(null, DATA_TYPE_MEMBER);
	}

	/**
	 * Sets the data type in parameter type choices to this parameter. The allowed
	 * values are:
	 * 
	 * <ul>
	 * <li>PARAM_TYPE_ANY
	 * <li>PARAM_TYPE_INTEGER
	 * <li>PARAM_TYPE_STRING
	 * <li>PARAM_TYPE_DATETIME
	 * <li>PARAM_TYPE_DECIMAL
	 * <li>PARAM_TYPE_FLOAT
	 * <li>PARAM_TYPE_BOOLEAN
	 * </ul>
	 * 
	 * @param dataType the data type to set
	 * @throws SemanticException if the value is not in the above list.
	 */

	public void setParameterDataType(String dataType) {
		setProperty(DATA_TYPE_MEMBER, dataType);
	}

	/**
	 * Gets the display name of this parameter.
	 * 
	 * @return the display name of this parameter
	 */
	public String getDisplayName() {
		return getStringProperty(null, DISPLAY_NAME_MEMBER);
	}

	/**
	 * Sets the display name of this parameter.
	 * 
	 * @param displayName the new display name of this parameter
	 */
	public void setDisplayName(String displayName) {
		setProperty(DISPLAY_NAME_MEMBER, displayName);
	}

	/**
	 * Gets the resource key of the display name of this parameter.
	 * 
	 * @return the resource key of the display name
	 */
	public String getDisplayNameKey() {
		return getStringProperty(null, DISPLAY_NAME_ID_MEMBER);
	}

	/**
	 * Sets the resource key of the display name id of this parameter.
	 * 
	 * @param displayNameID the new resource key of the display name
	 */
	public void setDisplayNameKey(String displayNameID) {
		setProperty(DISPLAY_NAME_ID_MEMBER, displayNameID);
	}

	/**
	 * Gets the heading of this parameter.
	 * 
	 * @return the heading of this parameter
	 */
	public String getHeading() {
		return getStringProperty(null, HEADING_MEMBER);
	}

	/**
	 * Sets the heading of this parameter.
	 * 
	 * @param heading the new heading of this parameter
	 */
	public void setHeading(String heading) {
		setProperty(HEADING_MEMBER, heading);
	}

	/**
	 * Gets the resource key of the heading of this parameter.
	 * 
	 * @return the resource key of the heading
	 */
	public String getHeadingKey() {
		return getStringProperty(null, HEADING_ID_MEMBER);
	}

	/**
	 * Sets the resource key of the heading of this parameter.
	 * 
	 * @param headingID the new resource key of the heading
	 */
	public void setHeadingKey(String headingID) {
		setProperty(HEADING_ID_MEMBER, headingID);
	}

	/**
	 * Gets the help text of this parameter.
	 * 
	 * @return the help text of this parameter
	 */
	public String getHelpText() {
		return getStringProperty(null, HELP_TEXT_MEMBER);
	}

	/**
	 * Sets the help text of this parameter.
	 * 
	 * @param helpText the new help text of this parameter
	 */
	public void setHelpText(String helpText) {
		setProperty(HELP_TEXT_MEMBER, helpText);
	}

	/**
	 * Gets the resource key of the help text of this parameter.
	 * 
	 * @return the resource key of the help text
	 */
	public String getHelpTextKey() {
		return getStringProperty(null, HELP_TEXT_ID_MEMBER);
	}

	/**
	 * Sets the resource key of the help text of this parameter.
	 * 
	 * @param helpTextID the new resource key of the heading
	 */
	public void setHelpTextKey(String helpTextID) {
		setProperty(HELP_TEXT_ID_MEMBER, helpTextID);
	}

	/**
	 * Gets the description of this parameter.
	 * 
	 * @return the description of this parameter
	 */
	public String getDescription() {
		return getStringProperty(null, DESCRIPTION_MEMBER);
	}

	/**
	 * Sets the description of this parameter.
	 * 
	 * @param description the new description of this parameter
	 */
	public void setDescription(String description) {
		setProperty(DESCRIPTION_MEMBER, description);
	}

	/**
	 * Gets the resource key of the description of this parameter.
	 * 
	 * @return the resource key
	 */
	public String getDescriptionKey() {
		return getStringProperty(null, DESCRIPTION_ID_MEMBER);
	}

	/**
	 * Sets the resource key of the description of this parameter.
	 * 
	 * @param descriptionID the new resource key of the description
	 */
	public void setDescriptionKey(String descriptionID) {
		setProperty(DESCRIPTION_ID_MEMBER, descriptionID);
	}
}
