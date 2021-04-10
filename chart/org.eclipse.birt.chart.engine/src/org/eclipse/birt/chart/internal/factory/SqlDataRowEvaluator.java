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

package org.eclipse.birt.chart.internal.factory;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.eclipse.birt.chart.factory.DataRowExpressionEvaluatorAdapter;

/**
 * A java.sql.ResultSet implementation for IDataRowExpressionEvaluator.
 */
public class SqlDataRowEvaluator extends DataRowExpressionEvaluatorAdapter {

	private ResultSet set;

	/**
	 * The constructor.
	 * 
	 * @param resultSet
	 */
	public SqlDataRowEvaluator(ResultSet resultSet) {
		if (resultSet == null)
			throw new IllegalArgumentException();
		this.set = resultSet;

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
			return set.getObject(expression);
		} catch (SQLException e) {
			return null;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator#next()
	 */
	public boolean next() {
		try {
			return set.next();
		} catch (SQLException e) {
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator#close()
	 */
	public void close() {
		try {
			set.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator#first()
	 */
	public boolean first() {
		try {
			return set.first();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

}
