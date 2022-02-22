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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.AggregationArgument;
import org.eclipse.birt.report.model.api.elements.structures.CalculationArgument;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;

/**
 * Represents the handle of computed column. A computed column is a 'virtual'
 * column produced as an expression of other columns within the data set. It
 * includes the column name and the expression used to define a computed column.
 *
 */

public class ComputedColumnHandle extends StructureHandle {

	/**
	 * Constructs the handle of computed column.
	 *
	 * @param valueHandle the value handle for computed column list of one property
	 * @param index       the position of this computed column in the list
	 */

	public ComputedColumnHandle(SimpleValueHandle valueHandle, int index) {
		super(valueHandle, index);
	}

	/**
	 * Returns the column name.
	 *
	 * @return the column name
	 * @deprecated using {@link #getName()} instead.
	 */

	@Deprecated
	public String getColumnName() {
		return getName();
	}

	/**
	 * Returns column display name.
	 *
	 * @return column display name.
	 */

	public String getDisplayName() {
		return getStringProperty(ComputedColumn.DISPLAY_NAME_MEMBER);
	}

	/**
	 * Gets column display name id.
	 *
	 * @return column display name id.
	 */
	public String getDisplayNameID() {
		return getStringProperty(ComputedColumn.DISPLAY_NAME_ID_MEMBER);
	}

	/**
	 * Sets column display name id.
	 *
	 * @param displayNameID the column display name id.
	 * @throws SemanticException
	 */
	public void setDisplayNameID(String displayNameID) throws SemanticException {
		setProperty(ComputedColumn.DISPLAY_NAME_ID_MEMBER, displayNameID);
	}

	/**
	 * Returns the localized text for the computed column. If the localized text for
	 * the text resource key is found, it will be returned. Otherwise, the static
	 * text will be returned.
	 *
	 * @return the localized display name.
	 *
	 */
	public String getDisplayText() {
		return getExternalizedValue(ComputedColumn.DISPLAY_NAME_ID_MEMBER, ComputedColumn.DISPLAY_NAME_MEMBER);
	}

	/**
	 * Returns the column name.
	 *
	 * @return the column name
	 */

	public String getName() {
		return getStringProperty(ComputedColumn.NAME_MEMBER);
	}

	/**
	 * Sets the column name.
	 *
	 * @param columnName the column name to set
	 * @deprecated using {@link #setName(String)} instead.
	 */

	@Deprecated
	public void setColumnName(String columnName) {
	}

	/**
	 * Sets the column display name.
	 *
	 * @param columnDisplayName the column display name to set.
	 * @throws SemanticException if the new column display name duplicates with the
	 *                           existed ones.
	 *
	 */

	public void setDisplayName(String columnDisplayName) throws SemanticException {
		setProperty(ComputedColumn.DISPLAY_NAME_MEMBER, columnDisplayName);
	}

	/**
	 * Sets the column name.
	 *
	 * @param columnName the column name to set.
	 * @throws SemanticException if the new column name duplicates with the existed
	 *                           ones.
	 *
	 */

	public void setName(String columnName) throws SemanticException {
		setProperty(ComputedColumn.NAME_MEMBER, columnName);
	}

	/**
	 * Returns the expression used to define this computed column.
	 *
	 * @return the expression used to define this computed column
	 */

	public String getExpression() {
		return getStringProperty(ComputedColumn.EXPRESSION_MEMBER);
	}

	/**
	 * Sets the expression used to define this computed column.
	 *
	 * @param expression the expression to set
	 * @throws SemanticException value required exception
	 */

	public void setExpression(String expression) throws SemanticException {
		setProperty(ComputedColumn.EXPRESSION_MEMBER, expression);
	}

	/**
	 * Returns the aggregateOn expression used to define this computed column.
	 *
	 * @return the aggregateOn expression used to define this computed column
	 *
	 * @deprecated by {@link #getAggregateOn()}
	 */

	@Deprecated
	public String getAggregrateOn() {
		return getAggregateOn();
	}

	/**
	 * Sets the aggregateOn expression used to define this computed column.
	 *
	 * @param aggregateOn the aggregateOn expression to set
	 * @deprecated by {@link #setAggregateOn(String)}
	 */

	@Deprecated
	public void setAggregrateOn(String aggregateOn) {
		setAggregateOn(aggregateOn);
	}

	/**
	 * Returns the data type of this column. The possible values are defined in
	 * {@link org.eclipse.birt.report.model.api.elements.DesignChoiceConstants}, and
	 * they are:
	 * <ul>
	 * <li>COLUMN_DATA_TYPE_ANY
	 * <li>COLUMN_DATA_TYPE_INTEGER
	 * <li>COLUMN_DATA_TYPE_STRING
	 * <li>COLUMN_DATA_TYPE_DATETIME
	 * <li>COLUMN_DATA_TYPE_DECIMAL
	 * <li>COLUMN_DATA_TYPE_FLOAT
	 * <li>COLUMN_DATA_TYPE_STRUCTURE
	 * <li>COLUMN_DATA_TYPE_TABLE
	 * </ul>
	 *
	 * @return the data type of this column.
	 */

	public String getDataType() {
		return getStringProperty(ComputedColumn.DATA_TYPE_MEMBER);
	}

	/**
	 * Sets the data type of this column. The allowed values are defined in
	 * {@link org.eclipse.birt.report.model.api.elements.DesignChoiceConstants}, and
	 * they are:
	 * <ul>
	 * <li>COLUMN_DATA_TYPE_ANY
	 * <li>COLUMN_DATA_TYPE_INTEGER
	 * <li>COLUMN_DATA_TYPE_STRING
	 * <li>COLUMN_DATA_TYPE_DATETIME
	 * <li>COLUMN_DATA_TYPE_DECIMAL
	 * <li>COLUMN_DATA_TYPE_FLOAT
	 * <li>COLUMN_DATA_TYPE_STRUCTURE
	 * <li>COLUMN_DATA_TYPE_TABLE
	 * </ul>
	 *
	 * @param dataType the data type to set
	 * @throws SemanticException if the dataType is not in the choice list.
	 */

	public void setDataType(String dataType) throws SemanticException {
		setProperty(ComputedColumn.DATA_TYPE_MEMBER, dataType);
	}

	/**
	 * Adds an aggregate level to the list.
	 *
	 * @param aggreValue the aggregate name. For listing elements, this can be "All"
	 *                   or the name of a single group.
	 * @throws SemanticException
	 */

	public void addAggregateOn(String aggreValue) throws SemanticException {
		MemberHandle aggreHandle = getMember(ComputedColumn.AGGREGATEON_MEMBER);
		aggreHandle.addItem(aggreValue);
	}

	/**
	 * Adds an arguments to list.
	 *
	 * @param argument the aggregate function argument
	 * @return aggregation argument handle.
	 * @throws SemanticException
	 */

	public AggregationArgumentHandle addArgument(AggregationArgument argument) throws SemanticException {
		MemberHandle aggreHandle = getMember(ComputedColumn.ARGUMENTS_MEMBER);
		return (AggregationArgumentHandle) aggreHandle.addItem(argument);
	}

	/**
	 * Returns the expression used to define this computed column. The function is
	 * one of following values:
	 *
	 * <ul>
	 * <li>DesignChoiceConstants.MEASURE_FUNCTION_SUM
	 * <li>DesignChoiceConstants.MEASURE_FUNCTION_COUNT
	 * <li>DesignChoiceConstants.MEASURE_FUNCTION_MIN
	 * <li>DesignChoiceConstants.MEASURE_FUNCTION_MAX
	 * </ul>
	 *
	 * @return the expression used to define this computed column
	 */

	public String getAggregateFunction() {
		return getStringProperty(ComputedColumn.AGGREGATEON_FUNCTION_MEMBER);
	}

	/**
	 * Returns the aggregateOn expression used to define this computed column.
	 *
	 * @return the aggregateOn expression used to define this computed column
	 */

	public String getAggregateOn() {
		List aggres = getAggregateOnList();
		if (aggres == null || aggres.isEmpty()) {
			return null;
		}

		return (String) aggres.get(0);
	}

	/**
	 * Returns the list containing levels to be aggregated on.
	 *
	 * @return the list containing levels to be aggregated on
	 */

	public List getAggregateOnList() {
		List aggregateOns = (List) getProperty(ComputedColumn.AGGREGATEON_MEMBER);
		if (aggregateOns == null) {
			return Collections.EMPTY_LIST;
		}

		return Collections.unmodifiableList(aggregateOns);
	}

	/**
	 * Returns additional arguments to the aggregate function. Each item in the list
	 * is instance of <code>AggregationArgumentHandle</code>.
	 *
	 * @return a list containing additional arguments
	 */

	public Iterator argumentsIterator() {
		MemberHandle propHandle = getMember(ComputedColumn.ARGUMENTS_MEMBER);
		assert propHandle != null;
		return propHandle.iterator();
	}

	/**
	 * Returns the expression used to define this computed column.
	 *
	 * @return the expression used to define this computed column
	 */

	public String getFilterExpression() {
		return getStringProperty(ComputedColumn.FILTER_MEMBER);
	}

	/**
	 * Removes an aggregate level from the list.
	 *
	 * @param aggreValue the aggregate name. For listing elements, this can be "All"
	 *                   or the name of a single group.
	 * @throws SemanticException
	 */

	public void removeAggregateOn(String aggreValue) throws SemanticException {
		MemberHandle aggreHandle = getMember(ComputedColumn.AGGREGATEON_MEMBER);
		aggreHandle.removeItem(aggreValue);
	}

	/**
	 * Removes an arguments from list.
	 *
	 * @param argument the aggregate function argument
	 * @throws SemanticException
	 */

	public void removeArgument(AggregationArgument argument) throws SemanticException {
		MemberHandle aggreHandle = getMember(ComputedColumn.ARGUMENTS_MEMBER);
		aggreHandle.removeItem(argument);
	}

	/**
	 * Sets the expression used to define this computed column. The function is one
	 * of following values:
	 *
	 * <ul>
	 * <li>DesignChoiceConstants.MEASURE_FUNCTION_SUM
	 * <li>DesignChoiceConstants.MEASURE_FUNCTION_COUNT
	 * <li>DesignChoiceConstants.MEASURE_FUNCTION_MIN
	 * <li>DesignChoiceConstants.MEASURE_FUNCTION_MAX
	 * </ul>
	 *
	 *
	 * @param expression the expression to set
	 * @throws SemanticException if the <code>expression</code> is not one of above
	 *                           values.
	 */

	public void setAggregateFunction(String expression) throws SemanticException {
		setProperty(ComputedColumn.AGGREGATEON_FUNCTION_MEMBER, expression);
	}

	/**
	 * Sets the aggregateOn expression used to define this computed column.
	 *
	 * @param aggregateOn the aggregateOn expression to set
	 *
	 */

	public void setAggregateOn(String aggregateOn) {
		MemberHandle aggreHandle = getMember(ComputedColumn.AGGREGATEON_MEMBER);
		if (aggregateOn == null) {
			try {
				aggreHandle.clearValue();
			} catch (SemanticException e) {
				assert false;
			}

			return;
		}

		List<String> newValue = new ArrayList<>();
		newValue.add(aggregateOn);

		try {
			aggreHandle.setValue(newValue);
		} catch (SemanticException e) {
			assert false;
		}
	}

	/**
	 * Sets the expression used to define this computed column.
	 *
	 * @param expression the expression to set
	 * @throws SemanticException value required exception
	 */

	public void setFilterExpression(String expression) throws SemanticException {
		setProperty(ComputedColumn.FILTER_MEMBER, expression);
	}

	/**
	 * Clears the aggregate on list.
	 *
	 * @throws SemanticException
	 */

	public void clearAggregateOnList() throws SemanticException {
		setProperty(ComputedColumn.AGGREGATEON_MEMBER, null);
	}

	/**
	 * Clears the argument list.
	 *
	 * @throws SemanticException
	 */

	public void clearArgumentList() throws SemanticException {
		setProperty(ComputedColumn.ARGUMENTS_MEMBER, null);
	}

	/**
	 * Gets the flag which indicates whether the computed column supports export.
	 *
	 * @return true if it allows, otherwise false.
	 */
	public boolean allowExport() {
		Boolean value = (Boolean) getProperty(ComputedColumn.ALLOW_EXPORT_MEMBER);
		if (value != null) {
			return value.booleanValue();
		}
		return true;
	}

	/**
	 * Sets the flag which indicates whether the computed column supports export.
	 *
	 * @param allowExport the flag to set
	 * @throws SemanticException
	 */
	public void setAllowExport(boolean allowExport) throws SemanticException {
		setProperty(ComputedColumn.ALLOW_EXPORT_MEMBER, allowExport);
	}

	/**
	 * Gets the calculation function name. The value is defined by customer DB
	 * calculation executor.
	 *
	 * @param calculationType
	 * @throws SemanticException
	 */
	public void setCalculationType(String calculationType) throws SemanticException {
		setProperty(ComputedColumn.CALCULATION_TYPE_MEMBER, calculationType);
	}

	/**
	 * Gets the calculation function name. The value is defined by customer DB
	 * calculation executor.
	 *
	 * @return
	 */
	public String getCalculationType() {
		return getStringProperty(ComputedColumn.CALCULATION_TYPE_MEMBER);
	}

	/**
	 * Returns a iterator of calculation arguments for specific calculation type.
	 * Each item in the list is instance of
	 * <code>CalculationAggregationArgumentHandle</code>.
	 *
	 * @return a list containing calculation arguments
	 */

	public Iterator calculationArgumentsIterator() {
		MemberHandle propHandle = getMember(ComputedColumn.CALCULATION_ARGUMENTS_MEMBER);
		assert propHandle != null;
		return propHandle.iterator();
	}

	/**
	 * Adds a calculation argument to list.
	 *
	 * @param argument the calculation argument for specific calculation type
	 * @return calculation argument handle.
	 * @throws SemanticException
	 */

	public CalculationArgumentHandle addCalculationArgument(CalculationArgument argument) throws SemanticException {
		MemberHandle aggreHandle = getMember(ComputedColumn.CALCULATION_ARGUMENTS_MEMBER);
		return (CalculationArgumentHandle) aggreHandle.addItem(argument);
	}

	/**
	 * Removes a calculation argument from list.
	 *
	 * @param argument the calculation argument
	 * @throws SemanticException
	 */

	public void removeCalculationArgument(CalculationArgument argument) throws SemanticException {
		MemberHandle aggreHandle = getMember(ComputedColumn.CALCULATION_ARGUMENTS_MEMBER);
		aggreHandle.removeItem(argument);
	}

	/**
	 *
	 * Sets reference date type for the calculation in this column. The type is one
	 * of following values:
	 *
	 * <ul>
	 * <li>DesignChoiceConstants.REFERENCE_DATE_TYPE_TODAY
	 * <li>DesignChoiceConstants.REFERENCE_DATE_TYPE_FIXED_DATE
	 * <li>DesignChoiceConstants.REFERENCE_DATE_TYPE_ENDING_DATE_IN_DIMENSION
	 * </ul>
	 *
	 *
	 * @param refDateType the reference date type to set
	 * @throws SemanticException if the <code>refDateType</code> is not one of above
	 *                           values.
	 */
	public void setReferenceDateType(String refDateType) throws SemanticException {
		setProperty(ComputedColumn.REFERENCE_DATE_TYPE_MEMBER, refDateType);
	}

	/**
	 *
	 * Returns reference date type for the calculation in this column. The type is
	 * one of following values:
	 *
	 * <ul>
	 * <li>DesignChoiceConstants.REFERENCE_DATE_TYPE_TODAY
	 * <li>DesignChoiceConstants.REFERENCE_DATE_TYPE_FIXED_DATE
	 * <li>DesignChoiceConstants.REFERENCE_DATE_TYPE_ENDING_DATE_IN_DIMENSION
	 * </ul>
	 *
	 * @return the reference date type
	 */
	public String getReferenceDateType() {
		return getStringProperty(ComputedColumn.REFERENCE_DATE_TYPE_MEMBER);
	}

	/**
	 * Gets the expression handle for the reference date value member. Then use the
	 * returned handle to do get/set action.
	 *
	 * @return
	 */
	public ExpressionHandle getReferenceDateValue() {
		return getExpressionProperty(ComputedColumn.REFERENCE_DATE_VALUE_MEMBER);
	}

	/**
	 * Gets the time dimension string value. It is the name of the referenced time
	 * dimension.
	 *
	 * @return the referred time dimension name
	 */
	public String getTimeDimension() {
		return getStringProperty(ComputedColumn.TIME_DIMENSION_MEMBER);
	}

	/**
	 * Sets the time dimension string value. It is the name of the referred time
	 * dimension element.
	 *
	 * @param timeDimension
	 * @throws SemanticException
	 */
	public void setTimeDimension(String timeDimension) throws SemanticException {
		setProperty(ComputedColumn.TIME_DIMENSION_MEMBER, timeDimension);
	}
}
