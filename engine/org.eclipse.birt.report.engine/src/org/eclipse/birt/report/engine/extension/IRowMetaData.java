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
import org.eclipse.birt.data.engine.core.DataException;

/**
 * Defines a row that the extension might receive. Each column in a row comes
 * from the evaluation of a expression. The getColumnExpression method returns
 * such an expression.
 */
public interface IRowMetaData {

	/**
	 * Returns the number of columns in a detail row of the result set.
	 * 
	 * @return the number of columns in a detail row.
	 */
	public int getColumnCount();

	/**
	 * Returns the column name at the specified index.
	 * 
	 * @param index The projected column index.
	 * @return The name of the specified column.
	 * @throws DataException if given index is invalid.
	 */
	public String getColumnName(int index) throws BirtException;

	public int getColumnType(int index) throws BirtException;

	/**
	 * Returns the column expression that results in the data at the specified
	 * index.
	 * 
	 * @param index The projected column index.
	 * @return The name of the specified column.
	 * @throws DataException if given index is invalid.
	 */
	// public String getColumnExpression( int index ) throws BirtException;
	// TODO removed, DTE doesn't support this feature.

}
