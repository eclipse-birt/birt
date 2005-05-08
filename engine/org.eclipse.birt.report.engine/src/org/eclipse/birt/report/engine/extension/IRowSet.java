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

import org.eclipse.birt.data.engine.api.IBaseExpression;

/**
 * defines an interface that wraps around a row set. 
 */
public interface IRowSet {
	
	/**
	 * returns the next row in the row set, or null if no more rows.
	 * 
	 * @return the next row in the row set, or null if no more rows.
	 * @throws BIRTException
	 */
	//public abstract Object[] nextRow() throws BirtException;
	
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
	 * evaluate the expression using current row.
	 * @param expr expression, must be prepared in DTE.prepared().
	 * @return result of the expression.
	 */
	Object evaluate(IBaseExpression expr);
	
	void close();
}
