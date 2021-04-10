
package org.eclipse.birt.chart.reportitem;

/***********************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
	public Object evaluateGlobal(String expression) {
		return evaluate(expression);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator#next()
	 */
	public boolean next() {
		return set.next();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator#close()
	 */
	public void close() {
		set.close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator#first()
	 */
	public boolean first() {
		return set.next();
	}

}
