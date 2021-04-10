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

package org.eclipse.birt.chart.reportitem;

import org.eclipse.birt.chart.factory.DataRowExpressionEvaluatorAdapter;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.extension.IQueryResultSet;

/**
 * 
 */

public final class BIRTQueryResultSetEvaluator extends DataRowExpressionEvaluatorAdapter {

	private static ILogger logger = Logger.getLogger("org.eclipse.birt.chart.reportitem/trace"); //$NON-NLS-1$

	private IQueryResultSet set;

	/**
	 * The constructor.
	 * 
	 * @param set
	 * @param definition
	 */
	public BIRTQueryResultSetEvaluator(IQueryResultSet set) {
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
		try {
			exprCodec.decode(expression);
			if (exprCodec.isConstant()) {
				return exprCodec.getExpression();
			}
			return set.evaluate(exprCodec.getType(), exprCodec.getExpression());
		} catch (BirtException e) {
			logger.log(e);
			return null;
		}
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
		try {
			return set.next();
		} catch (BirtException e) {
			logger.log(e);
			return false;
		}
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
		try {
			return set.next();
		} catch (BirtException e) {
			logger.log(e);
			return false;
		}
	}

}
