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
	 * @throws SemanticException
	 */
	void setFormat(String format) throws SemanticException;

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
	 * @throws SemanticException
	 */
	void setValueExpr(String valueExpr) throws SemanticException;

	/**
	 * Returns structure.
	 *
	 * @return structure
	 */

	IStructure getStructure();
}
