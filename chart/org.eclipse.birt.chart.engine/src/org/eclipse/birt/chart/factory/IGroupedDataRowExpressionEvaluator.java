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

import java.util.Map;

/**
 * The interface is declared to wrap <code>IQueryResultSet</code> and evaluate
 * grouped, uni-dimensional data.
 */
public interface IGroupedDataRowExpressionEvaluator
		extends
			IDataRowExpressionEvaluator
{

	/**
	 * Returns the group breaks of specified group level. <code>null</code>
	 * means no group breaks.
	 * 
	 * @param groupExp
	 * @return group breaks
	 */
	public int[] getGroupBreaks( int groupLevel );
	
	
	/**
	 * Commonly, the passed argument is a mapping of expression key and
	 * expression, the implementation of the interface will use expression key
	 * or expression in the map to evaluate expression.
	 * <p>
	 * For some aggregation case, the expression key should be used to evaluate
	 * instead of expression at live preview. And for other cases, it should
	 * still use expression to evaluate.
	 * 
	 * @param exprMap
	 */
	public void setExpressionsMap( Map exprMap );
}
