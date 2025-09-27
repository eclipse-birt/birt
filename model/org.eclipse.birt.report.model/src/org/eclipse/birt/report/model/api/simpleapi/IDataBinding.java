/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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

package org.eclipse.birt.report.model.api.simpleapi;

import jakarta.script.ScriptException;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.core.IStructure;

/**
 * Represents the design of an DataBinding in the scripting environment
 *
 */

public interface IDataBinding {

	/**
	 * Returns the name of column binding.
	 *
	 * @return name name of column binding.
	 */

	String getName();

	/**
	 * Sets the name of column binding.
	 *
	 * @param name name of column binding.
	 * @exception ScriptException
	 */

	void setName(String name) throws SemanticException;

	/**
	 * Returns expression of column binding
	 *
	 * @return expression of column binding
	 */

	String getExpression();

	/**
	 * Sets expression of column binding.
	 *
	 * @param expression expression of column binding.
	 * @exception ScriptException
	 */

	void setExpression(String expression) throws SemanticException;

	/**
	 * Returns data type of column binding.
	 *
	 * <p>
	 * <ul>
	 * <li><code>any</code>
	 * <li><code>integer</code>
	 * <li><code>string</code>
	 * <li><code>date-time</code>
	 * <li><code>decimal</code>
	 * <li><code>float</code>
	 * <li><code>boolean</code>
	 * </ul>
	 *
	 * @return data type of column binding
	 */

	String getDataType();

	/**
	 * Sets data type of column binding
	 *
	 * @param dataType
	 * @exception ScriptException
	 */

	void setDataType(String dataType) throws SemanticException;

	/**
	 * Returns aggregateOn of column binding
	 *
	 * @return aggregateOn of column binding
	 */

	String getAggregateOn();

	/**
	 * Sets aggregateOn of column binding.
	 *
	 * @param on aggregateOn of column binding.
	 * @exception ScriptException
	 */

	void setAggregateOn(String on) throws SemanticException;

	/**
	 * Returns structure.
	 *
	 * @return structure
	 */

	IStructure getStructure();

	/**
	 * Gets the expression type of the column binding.
	 *
	 * @return the expression type of the column binding.
	 */
	String getExpressionType();

	/**
	 * Sets the expression type of the column binding.
	 *
	 * @param expressionType the expression type of the column binding.
	 * @throws SemanticException
	 */
	void setExpressionType(String expressionType) throws SemanticException;

}
