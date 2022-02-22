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

package org.eclipse.birt.chart.factory;

import org.eclipse.birt.chart.model.impl.ChartModelHelper;
import org.eclipse.birt.chart.util.ChartExpressionUtil.ExpressionCodec;

/**
 * An adapter class for IDataRowExpressionEvaluator
 */
public class DataRowExpressionEvaluatorAdapter implements IDataRowExpressionEvaluator {

	protected final ExpressionCodec exprCodec = ChartModelHelper.instance().createExpressionCodec();

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator#evaluate(java.lang
	 * .String)
	 */
	@Override
	public Object evaluate(String expression) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator#evaluateGlobal(
	 * java.lang.String)
	 */
	@Override
	public Object evaluateGlobal(String expression) {
		return evaluate(expression);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator#first()
	 */
	@Override
	public boolean first() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator#next()
	 */
	@Override
	public boolean next() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator#close()
	 */
	@Override
	public void close() {
		// Doing nothing.
	}

}
