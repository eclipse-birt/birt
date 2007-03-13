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

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.attribute.DataType;

/**
 * Data service provider for chart wizard, to provide all necessary data.
 */

public interface IDataServiceProvider
{

	public static final int COMMAND_NEW_DATASET = 0;
	public static final int COMMAND_EDIT_FILTER = 1;
	public static final int COMMAND_EDIT_PARAMETER = 2;
	public static final int COMMAND_EDIT_BINDING = 3;

	/**
	 * Returns all available datasets to choose.
	 */
	public String[] getAllDataSets( );

	/**
	 * Returns the bound dataset currently, or null if there's no dataset bound.
	 */
	public String getBoundDataSet( );

	/**
	 * Returns the dataset bound by parents, or null if there's no dataset bound
	 * there.
	 */
	public String getReportDataSet( );

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
	 * Returns the preview data header, e.g. column display names.
	 * 
	 * @return meta data in form of string
	 * @throws ChartException
	 */
	public String[] getPreviewHeader( ) throws ChartException;

	/**
	 * Returns the preview data
	 * 
	 * @return data list which includes string array as an element
	 * @throws ChartException
	 */
	public List getPreviewData( ) throws ChartException;

	/**
	 * Sets the context object
	 */
	public void setContext( Object context );

	/**
	 * Binds dataset for chart, and updates related settings, such as column
	 * bindings, filters, parameters.
	 * 
	 * @param datasetName
	 *            Dataset name. Null means inheriting from container.
	 */
	public void setDataSet( String datasetName );

	/**
	 * Sets current used style by specified style name.
	 */
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
	 * Disposes all resources.
	 * 
	 */
	public void dispose( );

	/**
	 * Returns whether live preview is enabled
	 * 
	 * @return whether live preview is enabled
	 */
	public boolean isLivePreviewEnabled( );

	/**
	 * Returns whether all outside builder invokings are supported
	 * 
	 * @return whether all invokings are supported
	 * @since 2.1
	 */
	public boolean isInvokingSupported( );
	
	/**
	 * Returns whether the application is running under Eclipse Mode.
	 * @since 2.2
	 */
	public boolean isEclipseModeSupported( );

	/**
	 * Returns the data type according to the query expression.
	 * 
	 * @param expression
	 * @return 2.2
	 */
	public DataType getDataType( String expression );

}
