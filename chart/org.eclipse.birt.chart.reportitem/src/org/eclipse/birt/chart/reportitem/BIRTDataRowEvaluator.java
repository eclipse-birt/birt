
package org.eclipse.birt.chart.reportitem;

/***********************************************************************
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
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

import org.eclipse.birt.chart.factory.DataRowExpressionEvaluatorAdapter;
import org.eclipse.birt.report.engine.extension.IRowSet;

/**
 * A BIRT implementation of IDataRowExpressionEvaluator.
 */
public class BIRTDataRowEvaluator extends DataRowExpressionEvaluatorAdapter {

	private IRowSet set;

	/**
	 * The constructor.
	 *
	 * @param set
	 * @param definition
	 */
	public BIRTDataRowEvaluator(IRowSet set) {
		this.set = set;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator#evaluate(java.lang
	 * .String)
	 */
	@Override
	public Object evaluate(String expression) {
		return set.evaluate(expression);
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
	 * @see org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator#next()
	 */
	@Override
	public boolean next() {
		return set.next();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator#close()
	 */
	@Override
	public void close() {
		set.close();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator#first()
	 */
	@Override
	public boolean first() {
		return set.next();
	}

}
