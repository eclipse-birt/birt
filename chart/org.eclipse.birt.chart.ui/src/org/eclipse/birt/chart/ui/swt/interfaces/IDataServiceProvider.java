/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.ui.swt.interfaces;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.attribute.DataType;

/**
 * Data service provider for chart wizard, to provide all necessary data.
 */

public interface IDataServiceProvider
{

	/**
	 * Returns all available style names.
	 */
	public String[] getAllStyles( );

	/**
	 * Returns all available style display names. Note the count should be
	 * identical with getAllStyles().
	 * 
	 * @since 2.1
	 */
	public String[] getAllStyleDisplayNames( );

	/**
	 * Returns the name of current used style.
	 */
	public String getCurrentStyle( );

	/**
	 * Sets current used style by specified style name.
	 */
	public void setStyle( String styleName );

	/**
	 * Fetches data from dataset.
	 * 
	 * @param sExpressions
	 *            column expression array in the form of javascript. Null will
	 *            return all columns of dataset.
	 * @param iMaxRecords
	 *            max row count. -1 returns default count or the preference
	 *            value.
	 * @param byRow
	 *            true: by row first, false: by column first
	 * @return Data array. if type is by row, array length is row length; if
	 *         type is by column, array length is column length
	 */
	public Object[] getDataForColumns( String[] sExpressions, int iMaxRecords,
			boolean byRow ) throws ChartException;

	/**
	 * Returns whether live preview is enabled
	 * 
	 * @return whether live preview is enabled
	 */
	public boolean isLivePreviewEnabled( );

	/**
	 * Returns the data type according to the query expression.
	 * 
	 * @param expression
	 * @return 2.2
	 */
	public DataType getDataType( String expression );

}
