/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.extension;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBaseExpression;

/**
 * defines an interface that wraps around a row set.
 */
public interface IRowSet {

	/**
	 * returns the definition for the data row
	 * 
	 * @return the definition for the data row
	 */
	public abstract IRowMetaData getMetaData();

	/**
	 * move the row to next.
	 * 
	 * @return true successful, false for no more rows
	 */
	boolean next();

	/**
	 * Returns the 1-based index of the outermost group in which the current row is
	 * the last row. For example, if a query contain N groups (group with index 1
	 * being the outermost group, and group with index N being the innermost group),
	 * and this function returns a value M, it indicates that the current row is the
	 * last row in groups with indexes (M, M+1, ..., N ). -1 represents current row
	 * is a detail row. 0 represents the end of whole resultset
	 * 
	 * @return 1-based index of the outermost group in which the current row is the
	 *         first row; (N+1) if the current row is not at the start of any group;
	 *         0 if the result set has no groups.
	 */
	int getStartingGroupLevel();

	/**
	 * Returns the 1-based index of the outermost group in which the current row is
	 * the first row. For example, if a query contain N groups (group with index 1
	 * being the outermost group, and group with index N being the innermost group),
	 * and this function returns a value M, it indicates that the current row is the
	 * first row in groups with indexes (M, M+1, ..., N ).
	 * 
	 * @return 1-based index of the outermost group in which the current row is the
	 *         first row; (N+1) if the current row is not at the start of any group;
	 *         0 if the result set has no groups.
	 */
	int getEndingGroupLevel();

	/**
	 * evaluate the expression using current row.
	 * 
	 * @param expr expression, must be prepared in DTE.prepared().
	 * @return result of the expression.
	 */
	Object evaluate(String expr);

	Object evaluate(IBaseExpression expr);

	/**
	 * clse the row set
	 */
	void close();

	/**
	 * Judges if the IRowSet is empty or not
	 * 
	 * @return true if IRowSet is empty. false if it is not empty.
	 */
	boolean isEmpty() throws BirtException;
}
