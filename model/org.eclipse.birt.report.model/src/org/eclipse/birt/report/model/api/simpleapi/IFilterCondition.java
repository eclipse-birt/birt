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

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.core.IStructure;

/**
 * Represents the design of an FilterCondition in the scripting environment
 *
 */

public interface IFilterCondition {

	/**
	 * Returns operator
	 *
	 * @return operator
	 */

	String getOperator();

	/**
	 * Sets operator
	 *
	 * @param operator
	 * @throws SemanticException
	 */

	void setOperator(String operator) throws SemanticException;

	/**
	 * Returns value1
	 *
	 * @return value1
	 */

	String getValue1();

	/**
	 * Sets value1
	 *
	 * @param value1
	 * @throws SemanticException
	 */

	void setValue1(String value1) throws SemanticException;

	/**
	 * Returns value2
	 *
	 * @return value2
	 */

	String getValue2();

	/**
	 * Sets value2
	 *
	 * @param value2
	 * @throws SemanticException
	 */

	void setValue2(String value2) throws SemanticException;

	/**
	 * Returns structure.
	 *
	 * @return structure
	 */

	IStructure getStructure();

	/**
	 * Sets expr
	 *
	 * @param expr
	 */

	void setExpr(String expr) throws SemanticException;

	/**
	 * Returns expr
	 *
	 * @return expr
	 */

	String getExpr();
}
