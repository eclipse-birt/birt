/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.reportitem;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.elements.structures.FormatValue;

/**
 * This class process extra styles to chart.
 * 
 * @since 2.6.2
 */

public class ChartStyleProcessorProxy
{
	/** The chart's report item handle. */
	protected DesignElementHandle handle;

	private FormatInfo categoryFormat = null;

	/**
	 * Sets chart's report handle.
	 * 
	 * @param handle
	 */
	public void setHandle( DesignElementHandle handle )
	{
		this.handle = handle;
	}

	/**
	 * Applies extra styles onto chart.
	 * 
	 * @param cm
	 */
	public void processDataSetStyle( Chart cm )
	{
		// No code here, just return
		return;
	}

	/**
	 * Sets format info of chart's category.
	 * 
	 * @param formatInfo
	 */
	protected void setCategoryFormat( FormatInfo formatInfo )
	{
		this.categoryFormat = formatInfo;
	}

	/**
	 * Returns format info of chart's category.
	 * 
	 * @return
	 */
	public FormatInfo getCategoryFormat( )
	{
		return this.categoryFormat;
	}

	/**
	 * The class stores format information.
	 */
	public static class FormatInfo
	{
		public FormatValue formatValue = null;
		public String dataType = null;
	}
}
