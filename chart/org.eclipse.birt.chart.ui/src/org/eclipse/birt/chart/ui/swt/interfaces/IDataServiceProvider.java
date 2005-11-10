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

package org.eclipse.birt.chart.ui.swt.interfaces;

import java.util.List;

/**
 * 
 */

public interface IDataServiceProvider
{

	public static final int COMMAND_NEW_DATASET = 0;
	public static final int COMMAND_EDIT_FILTER = 1;
	public static final int COMMAND_EDIT_PARAMETER = 2;

	public String[] getAllDataSets( );

	public String getCurrentDataSet( );

	public String[] getAllStyles( );

	public String getCurrentStyle( );

	public String[] getPreviewHeader( );

	public List getPreviewData( );

	public void setContext( Object context );

	public void setDataSet( String datasetName );

	public void setStyle( String styleName );

	/**
	 * Invokes specific dialogue. The return codes are window-specific, although
	 * two standard return codes are predefined: <code>OK</code> and
	 * <code>CANCEL</code>.
	 * </p>
	 * 
	 * @param command
	 *            dialogue type, predefined:<code>COMMAND_NEW_DATASET</code>,
	 *            <code>COMMAND_EDIT_FILTER</code> and
	 *            <code>COMMAND_EDIT_PARAMETER</code>
	 * @return the return code
	 * 
	 */
	public int invoke( int command );

	/**
	 * Fetches data from dataset.
	 * 
	 * @param sExpressions
	 *            column expression array in the form of javascript. Null will
	 *            return all columns of dataset.
	 * @param iMaxRecords
	 *            max row count. -1 returns default count.
	 * @param byRow
	 *            true: by row first, false: by column first
	 * @return Data array. if type is by row, array length is row length; if
	 *         type is by column, array length is column length
	 */
	public Object[] getDataForColumns( String[] sExpressions, int iMaxRecords,
			boolean byRow );

}
