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
import org.eclipse.birt.report.model.elements.interfaces.IMeasureModel;
import org.eclipse.birt.report.model.util.impl.ActionHelper;

/**
 * This class represents a measure element.
 */

public abstract class MeasureHandle extends ReportElementHandle implements IMeasureModel {

	/**
	 * Constructs a handle for the given design and design element. The application
	 * generally does not create handles directly. Instead, it uses one of the
	 * navigation methods available on other element handles.
	 *
	 * @param module  the module
	 * @param element the model representation of the element
	 */

	public MeasureHandle(Module module, DesignElement element) {
		super(module, element);
	}

	/**
	 * Gets the function defined in this measure.
	 *
	 * @return function for this measure
	 */

	public String getFunction() {
		return getStringProperty(FUNCTION_PROP);
	}

	/**
	 * Sets the function for this measure.
	 *
	 * @param function the function to set
	 * @throws SemanticException property is locked or value is invalid
	 */
	public void setFunction(String function) throws SemanticException {
		setStringProperty(FUNCTION_PROP, function);
	}

	/**
	 * Gets the measure expression of this measure element.
	 *
	 * @return measure expression of this measure element
	 */

	public String getMeasureExpression() {
		return getStringProperty(MEASURE_EXPRESSION_PROP);
	}

	/**
	 * Sets the measure expression for this measure.
	 *
	 * @param expression the measure expression to set
	 * @throws SemanticException property is locked
	 */
	public void setMeasureExpression(String expression) throws SemanticException {
		setStringProperty(MEASURE_EXPRESSION_PROP, expression);
	}

	/**
	 * Indicates whether this measure is computed by other measures or not.
	 *
	 * @return true if this measure is computed by other measures, otherwise false
	 */

	public boolean isCalculated() {
		return getBooleanProperty(IS_CALCULATED_PROP);
	}

	/**
	 * Sets whether this measure is computed by other measures or not.
	 *
	 * @param isCalculated true if this measure is computed by other measures,
	 *                     otherwise false
	 * @throws SemanticException property is locked
	 */

	public void setCalculated(boolean isCalculated) throws SemanticException {
		setBooleanProperty(IS_CALCULATED_PROP, isCalculated);
	}

	/**
	 * Returns the data type information of this measure. The possible values are
	 * defined in
	 * {@link org.eclipse.birt.report.model.api.elements.DesignChoiceConstants}, and
	 * they are:
	 * <ul>
	 * <li>COLUMN_DATA_TYPE_INTEGER
	 * <li>COLUMN_DATA_TYPE_STRING
	 * <li>COLUMN_DATA_TYPE_DATETIME
	 * <li>COLUMN_DATA_TYPE_DECIMAL
	 * <li>COLUMN_DATA_TYPE_FLOAT
	 * <li>COLUMN_DATA_TYPE_STRUCTURE
	 * <li>COLUMN_DATA_TYPE_TABLE
	 * </ul>
	 *
	 * @return the data type of this measure.
	 */

	public String getDataType() {
		return getStringProperty(DATA_TYPE_PROP);
	}

	/**
	 * Sets the data type of this measure. The allowed values are defined in
	 * {@link org.eclipse.birt.report.model.api.elements.DesignChoiceConstants}, and
	 * they are:
	 * <ul>
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
		setProperty(DATA_TYPE_PROP, dataType);
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
	 * Returns a handle to work with the action property, action is a structure that
	 * defines a hyperlink.
	 *
	 * @return a handle to the action property, return <code>null</code> if the
	 *         action has not been set on the measure.
	 * @see ActionHandle
	 */

	public ActionHandle getActionHandle() {
		return new ActionHelper(this, ACTION_PROP).getActionHandle();
	}

	/**
	 * Set an action on the measure.
	 *
	 * @param action new action to be set on the measure, it represents a bookmark
	 *               link, hyper-link, and drill through etc.
	 * @return a handle to the action property, return <code>null</code> if the
	 *         action has not been set on the measure.
	 *
	 * @throws SemanticException if member of the action is not valid.
	 */

	public ActionHandle setAction(Action action) throws SemanticException {
		return new ActionHelper(this, ACTION_PROP).setAction(action);
	}

	/**
	 * Returns the iterator for action defined on this measure.
	 *
	 * @return the iterator for <code>Action</code> structure list defined on this
	 *         measure
	 */

	public Iterator<ActionHandle> actionsIterator() {
		return new ActionHelper(this, ACTION_PROP).actionsIterator();
	}

	/**
	 * Gets the format of the measure.
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
	 * Sets the format of the measure.
	 *
	 * @param format the format to set.
	 * @throws SemanticException
	 */
	public void setFormat(FormatValue format) throws SemanticException {
		setProperty(FORMAT_PROP, format);
	}

	/**
	 * Gets the alignment of the measure. The returned value may be one of the
	 * following constants defined in <code>DesignChoiceConstants<code>:
	 *
	 * <ul>
	 * <li>TEXT_ALIGN_LEFT
	 * <li>TEXT_ALIGN_CENTER
	 * <li>TEXT_ALIGN_RIGHT
	 * <li>TEXT_ALIGN_JUSTIFY
	 * </ul>
	 *
	 * @return the alignment of the measure.
	 */
	public String getAlignment() {
		return getStringProperty(ALIGNMENT_PROP);
	}

	/**
	 * Sets the alignment of the measure. The value to set should be one of the
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

	/**
	 * Gets the status whether the measure element is visible or not. By default, it
	 * is true.
	 *
	 * @return
	 */
	public boolean isVisible() {
		return getBooleanProperty(IS_VISIBLE_PROP);
	}

	/**
	 * Sets the status whether the measure element is visible or not.
	 *
	 * @param isVisible
	 * @throws SemanticException
	 */
	public void setVisible(boolean isVisible) throws SemanticException {
		setBooleanProperty(IS_VISIBLE_PROP, isVisible);
	}
}
