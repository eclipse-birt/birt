/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
	public List<Expression> getNewExpression();

	/**
	 * This method returns an array of IColumnBinding instance, the column names of
	 * which will appears in the return of getNewExpression() method.
	 * 
	 * @return
	 */
	public IBinding[] getColumnBindings();

}
