/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.engine.api.script;

import org.eclipse.birt.report.engine.api.script.instance.IDataSetInstance;

public interface IDataSetRow
{
	/**
	 * Gets the data set runtime instance which contains this row
	 */
	IDataSetInstance getDataSet( );

	/**
	 * Gets the column data by index. Data row column index starts from 1.
	 * 
	 * @param index
	 *            1-based index of column. If value is 0, an internal index of
	 *            the current row (if available) is returned
	 */
	Object getColumnValue( int index );

	/**
	 * Sets the column data by index. Column index starts from 1.
	 * 
	 * @param index
	 *            1-based index of column. Value must be between 1 and the
	 *            number of columns
	 * @param value
	 *            New value for column (can be null)
	 */
	void setColumnValue( int index, Object value );

	/**
	 * Gets the column data by column name.
	 * 
	 * @param name
	 *            of column
	 */
	Object getColumnValue( String name );

	/**
	 * Sets the column data by column name.
	 * 
	 * @param name
	 *            of column
	 */
	void setColumnValue( String name, Object value );
}
