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

package org.eclipse.birt.report.engine.api.script.element;

import org.eclipse.birt.report.engine.api.script.ScriptException;
import org.eclipse.birt.report.model.api.core.IStructure;

/**
 * Represents the design of an HighRule in the scripting environment
 *
 */

public interface IHideRule {

	/**
	 * Returns format
	 *
	 * @return format
	 */
	String getFormat();

	/**
	 * Sets format
	 *
	 * @param format
	 * @throws ScriptException
	 */
	void setFormat(String format) throws ScriptException;

	/**
	 * Returns value expression
	 *
	 * @return value expression
	 */
	String getValueExpr();

	/**
	 * Sets value expression.
	 *
	 * @param valueExpr
	 * @throws ScriptException
	 */
	void setValueExpr(String valueExpr) throws ScriptException;

	/**
	 * Returns structure.
	 *
	 * @return structure
	 */

	IStructure getStructure();
}
