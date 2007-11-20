/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.factory;

/**
 * The interface is declared to wrap <code>IQueryResultSet</code> and evaluate
 * grouped, uni-dimensional data.
 */
public interface IGroupedDataRowExpressionEvaluator
		extends
			IDataRowExpressionEvaluator
{

	/**
	 * Evaluate expression/binding name.
	 * 
	 * @param expression
	 *            expression/binding name.
	 * @param isValueSeries
	 *            <code>true</code> means the expression should be as binding
	 *            name to get value.
	 * 
	 * @return evaluator result
	 */
	public Object evaluate( String expression, boolean isValueSeries );

	/**
	 * Returns the group breaks of specified group level. <code>null</code>
	 * means no group breaks.
	 * 
	 * @param groupExp
	 * @return group breaks
	 */
	public int[] getGroupBreaks( int groupLevel );
}
