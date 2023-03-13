/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
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
package org.eclipse.birt.report.engine.adapter;

import java.util.List;

import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.report.engine.ir.Expression;

/**
 * The instance of this class define a data structure used by engine.
 */
public interface ITotalExprBindings {
	/**
	 * This method returns the "new expression" in which all reference to "Total"
	 * expressions are replaced with "row" expressions. Say, "Total.count()+1" ->
	 * "row[\"TOTAL_COLUMN_1\"]+1".
	 *
	 * @return
	 */
	List<Expression> getNewExpression();

	/**
	 * This method returns an array of IColumnBinding instance, the column names of
	 * which will appears in the return of getNewExpression() method.
	 *
	 * @return
	 */
	IBinding[] getColumnBindings();

}
