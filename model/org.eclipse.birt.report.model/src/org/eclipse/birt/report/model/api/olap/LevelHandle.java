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

package org.eclipse.birt.report.model.api.olap;

import java.util.Iterator;

import org.eclipse.birt.report.model.api.ActionHandle;
import org.eclipse.birt.report.model.api.ExpressionHandle;
import org.eclipse.birt.report.model.api.FormatValueHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.Action;
import org.eclipse.birt.report.model.api.elements.structures.FormatValue;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.interfaces.ILevelModel;
import org.eclipse.birt.report.model.util.impl.ActionHelper;

/**
 * Represents a level element.
 *
 * @see org.eclipse.birt.report.model.elements.olap.Level
 */

public abstract class LevelHandle extends ReportElementHandle implements ILevelModel {

	/**
	 * Constructs a handle for the given design and design element. The application
	 * generally does not create handles directly. Instead, it uses one of the
	 * navigation methods available on other element handles.
	 *
	 * @param module  the module
	 * @param element the model representation of the element
	 */

	public LevelHandle(Module module, DesignElement element) {
		super(module, element);
	}

	/**
	 * Returns the iterator of attributes. The element in the iterator is a
	 * <code>LevelAttributeHandle</code> for TabularLevel. To OdaLevel, each member
	 * is <code>OdaLevelAttributeHandle</code>.
	 *
	 * @return the iterator of attribute string list
	 */

	public Iterator attributesIterator() {
		PropertyHandle propHandle = getPropertyHandle(ATTRIBUTES_PROP);

		assert propHandle != null;

		return propHandle.iterator();
	}

	/**
	 * Returns the iterator of static values. The element in the iterator is
	 * instanceof <code>RuleHandle</code>.
	 *
	 * @return iterator of static values
	 */
	public Iterator staticValuesIterator() {
		PropertyHandle propHandle = getPropertyHandle(STATIC_VALUES_PROP);
		assert propHandle != null;
		return propHandle.iterator();
	}

	/**
	 * Sets the base of the interval property of this level.IntervalBase, in
	 * conjunction with Interval and IntervalRange, determines how data is divided
	 * into levels.
	 *
	 * @param intervalBase interval base property value.
	 * @throws SemanticException if the property is locked.
	 */

	public void setIntervalBase(String intervalBase) throws SemanticException {
		setStringProperty(INTERVAL_BASE_PROP, intervalBase);
	}

	/**
	 * Return the interval base property value of this level.
	 *
	 * @return interval baseF property value of this level.
	 */

	public String getIntervalBase() {
		return getStringProperty(INTERVAL_BASE_PROP);
	}

	/**
	 * Returns the interval of this level. The return value is defined in
	 * <code>DesignChoiceConstants</code> and can be one of:
	 *
	 * <ul>
	 * <li><code>INTERVAL_NONE</code>
	 * <li><code>INTERVAL_PREFIX</code>
	 * <li><code>INTERVAL_INTERVAL</code>
	 *
	 * </ul>
	 *
	 * @return the interval value as a string
	 */

	public String getInterval() {
		return getStringProperty(INTERVAL_PROP);
	}

	/**
	 * Returns the interval of this level. The input value is defined in
	 * <code>DesignChoiceConstants</code> and can be one of:
	 *
	 * <ul>
	 * <li><code>INTERVAL_NONE</code>
	 * <li><code>INTERVAL_PREFIX</code>
	 * <li><code>INTERVAL_INTERVAL</code>
	 *
	 * </ul>
	 *
	 * @param interval the interval value as a string
	 * @throws SemanticException if the property is locked or the input value is not
	 *                           one of the above.
	 */

	public void setInterval(String interval) throws SemanticException {
		setStringProperty(INTERVAL_PROP, interval);
	}

	/**
	 * Returns the interval range of this level.
	 *
	 * @return the interval range value as a double
	 */

	public double getIntervalRange() {
		return this.getFloatProperty(INTERVAL_RANGE_PROP);
	}

	/**
	 * Returns the interval range of this level.
	 *
	 * @param intervalRange the interval range value as a double
	 * @throws SemanticException if the property is locked.
	 */

	public void setIntervalRange(double intervalRange) throws SemanticException {
		setFloatProperty(INTERVAL_RANGE_PROP, intervalRange);
	}

	/**
	 * Sets the interval range of this level.
	 *
	 * @param intervalRange the interval range value as a string.value is locale
	 *                      dependent.
	 * @throws SemanticException if the property is locked.
	 */

	public void setIntervalRange(String intervalRange) throws SemanticException {
		setStringProperty(INTERVAL_RANGE_PROP, intervalRange);
	}

	/**
	 * Returns the level type of this level. The returned value is one of:
	 *
	 * <ul>
	 * <li><code>LEVEL_TYPE_DYNAMIC</code>
	 * <li><code>LEVEL_TYPE_MIRRORED</code>
	 * </ul>
	 *
	 * @return the level type
	 */

	public String getLevelType() {
		return getStringProperty(LEVEL_TYPE_PROP);
	}

	/**
	 * Sets the level type. The input value is defined in
	 * <code>DesignChoiceConstants</code> and can be one of:
	 *
	 * <ul>
	 * <li><code>LEVEL_TYPE_DYNAMIC</code>
	 * <li><code>LEVEL_TYPE_MIRRORED</code>
	 * </ul>
	 *
	 * @param levelType
	 * @throws SemanticException
	 */
	public void setLevelType(String levelType) throws SemanticException {
		setStringProperty(LEVEL_TYPE_PROP, levelType);
	}

	/**
	 * Returns the data type of this level. The possible values are defined in
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
	 * @return the data type of this level.
	 */

	public String getDataType() {
		return getStringProperty(DATA_TYPE_PROP);
	}

	/**
	 * Sets the data type of this level. The allowed values are defined in
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
		setStringProperty(DATA_TYPE_PROP, dataType);
	}

	/**
	 * Returns an iterator for the value access controls. Each object returned is of
	 * type <code>ValueAccessControlHandle</code>.
	 *
	 * @return the iterator for user accesses defined on this cube.
	 */

	public Iterator valueAccessControlsIterator() {
		PropertyHandle propHandle = getPropertyHandle(VALUE_ACCESS_CONTROLS_PROP);
		return propHandle.getContents().iterator();
	}

	/**
	 * Returns the date-time type of this level. The possible values are defined in
	 * {@link org.eclipse.birt.report.model.api.elements.DesignChoiceConstants}, and
	 * they are:
	 * <ul>
	 * <li>DATE_TIME_LEVEL_TYPE_YEAR
	 * <li>DATE_TIME_LEVEL_TYPE_MONTH
	 * <li>DATE_TIME_LEVEL_TYPE_QUARTER
	 * <li>DATE_TIME_LEVEL_TYPE_WEEK
	 * <li>DATE_TIME_LEVEL_TYPE_DAY
	 * <li>DATE_TIME_LEVEL_TYPE_HOUR
	 * <li>DATE_TIME_LEVEL_TYPE_MINUTE
	 * <li>DATE_TIME_LEVEL_TYPE_SECOND
	 * </ul>
	 *
	 * @return the date-time type of this level.
	 */

	public String getDateTimeLevelType() {
		return getStringProperty(DATE_TIME_LEVEL_TYPE);
	}

	/**
	 * Sets the date-time type of this level. The allowed values are defined in
	 * {@link org.eclipse.birt.report.model.api.elements.DesignChoiceConstants}, and
	 * they are:
	 * <ul>
	 * <li>DATE_TIME_LEVEL_TYPE_YEAR
	 * <li>DATE_TIME_LEVEL_TYPE_MONTH
	 * <li>DATE_TIME_LEVEL_TYPE_QUARTER
	 * <li>DATE_TIME_LEVEL_TYPE_WEEK
	 * <li>DATE_TIME_LEVEL_TYPE_DAY
	 * <li>DATE_TIME_LEVEL_TYPE_HOUR
	 * <li>DATE_TIME_LEVEL_TYPE_MINUTE
	 * <li>DATE_TIME_LEVEL_TYPE_SECOND
	 * </ul>
	 *
	 * @param dateTimeType the date-time type to set
	 * @throws SemanticException if the dateTimeType is not in the choice list.
	 */

	public void setDateTimeLevelType(String dateTimeType) throws SemanticException {
		setStringProperty(DATE_TIME_LEVEL_TYPE, dateTimeType);
	}

	/**
	 * Returns the date-time format of this level.
	 *
	 * @return the date-time format of this level.
	 */

	public String getDateTimeFormat() {
		return getStringProperty(DATE_TIME_FORMAT_PROP);
	}

	/**
	 * Sets the date-time format of this level.
	 *
	 * @param dateTimeFormat the date-time format to set
	 * @throws SemanticException if the date-time-format is locked
	 */

	public void setDateTimeFormat(String dateTimeFormat) throws SemanticException {
		setStringProperty(DATE_TIME_FORMAT_PROP, dateTimeFormat);
	}

	/**
	 * Sets the default value of this level.
	 *
	 * @param defaultValue the default value.
	 * @throws SemanticException
	 */
	public void setDefaultValue(String defaultValue) throws SemanticException {
		setStringProperty(DEFAULT_VALUE_PROP, defaultValue);
	}

	/**
	 * Gets the default value of this level.
	 *
	 * @return the default value.
	 */
	public String getDefaultValue() {
		return getStringProperty(DEFAULT_VALUE_PROP);
	}

	/**
	 * Gets the expression handle for the <code>ACLExpression</code> property.
	 *
	 * @return
	 */
	public ExpressionHandle getACLExpression() {
		return getExpressionProperty(ACL_EXPRESSION_PROP);
	}

	/**
	 * Gets the expression handle for the ACL expression for any member of this
	 * level.
	 *
	 * @return
	 */
	public ExpressionHandle getMemberACLExpression() {
		return getExpressionProperty(MEMBER_ACL_EXPRESSION_PROP);
	}

	/**
	 * Returns a handle to work with the action property, action is a structure that
	 * defines a hyperlink.
	 *
	 * @return a handle to the action property, return <code>null</code> if the
	 *         action has not been set on the level.
	 * @see ActionHandle
	 */

	public ActionHandle getActionHandle() {
		return new ActionHelper(this, ACTION_PROP).getActionHandle();
	}

	/**
	 * Set an action on the level.
	 *
	 * @param action new action to be set on the level, it represents a bookmark
	 *               link, hyper-link, and drill through etc.
	 * @return a handle to the action property, return <code>null</code> if the
	 *         action has not been set on the level.
	 *
	 * @throws SemanticException if member of the action is not valid.
	 */

	public ActionHandle setAction(Action action) throws SemanticException {
		return new ActionHelper(this, ACTION_PROP).setAction(action);
	}

	/**
	 * Returns the iterator for action defined on this level.
	 *
	 * @return the iterator for <code>Action</code> structure list defined on this
	 *         level
	 */

	public Iterator<ActionHandle> actionsIterator() {
		return new ActionHelper(this, ACTION_PROP).actionsIterator();
	}

	/**
	 * Gets the format of the level.
	 *
	 * @return the format, or null if not set.
	 */
	public FormatValueHandle getFormat() {
		PropertyHandle propHandle = getPropertyHandle(FORMAT_PROP);
		FormatValue format = (FormatValue) propHandle.getValue();

		if (format == null) {
			return null;
		}
		return (FormatValueHandle) format.getHandle(propHandle);
	}

	/**
	 * Sets the format of the level.
	 *
	 * @param format the format to set.
	 * @throws SemanticException
	 */
	public void setFormat(FormatValue format) throws SemanticException {
		setProperty(FORMAT_PROP, format);
	}

	/**
	 * Gets the alignment of the level. The returned value may be one of the
	 * following constants defined in <code>DesignChoiceConstants<code>:
	 *
	 * <ul>
	 * <li>TEXT_ALIGN_LEFT
	 * <li>TEXT_ALIGN_CENTER
	 * <li>TEXT_ALIGN_RIGHT
	 * <li>TEXT_ALIGN_JUSTIFY
	 * </ul>
	 *
	 * @return the alignment of the level.
	 */
	public String getAlignment() {
		return getStringProperty(ALIGNMENT_PROP);
	}

	/**
	 * Sets the alignment of the level. The value to set should be one of the
	 * following constants defined in <code>DesignChoiceConstants<code>:
	 *
	 * <ul>
	 * <li>TEXT_ALIGN_LEFT
	 * <li>TEXT_ALIGN_CENTER
	 * <li>TEXT_ALIGN_RIGHT
	 * <li>TEXT_ALIGN_JUSTIFY
	 * </ul>
	 *
	 * @param alignment the new alignment to set.
	 * @throws SemanticException
	 */
	public void setAlignment(String alignment) throws SemanticException {
		setStringProperty(ALIGNMENT_PROP, alignment);
	}

}
