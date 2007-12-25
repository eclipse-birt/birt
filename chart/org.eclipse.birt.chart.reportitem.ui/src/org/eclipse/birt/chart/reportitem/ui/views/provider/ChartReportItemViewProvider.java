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

package org.eclipse.birt.chart.reportitem.ui.views.provider;

import org.eclipse.birt.report.designer.ui.extensions.IReportItemViewProvider;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.TableHandle;

/**
 * 
 */

public class ChartReportItemViewProvider implements IReportItemViewProvider
{

	public DesignElementHandle createView( DesignElementHandle host )
	{
		if ( host instanceof TableHandle )
		{
			// Create chart
		}
		return null;
	}

	public String getViewName( )
	{
		return "Chart"; //$NON-NLS-1$
	}

}
