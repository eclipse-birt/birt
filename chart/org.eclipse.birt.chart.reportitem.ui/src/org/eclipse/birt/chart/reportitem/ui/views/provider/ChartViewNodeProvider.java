/*******************************************************************************
 * Copyright (c) 2006, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.reportitem.ui.views.provider;

import org.eclipse.birt.chart.reportitem.ui.ChartReportItemBuilderImpl;
import org.eclipse.birt.chart.ui.util.ChartUIConstants;
import org.eclipse.birt.chart.ui.util.UIHelper;
import org.eclipse.birt.report.designer.internal.ui.views.DefaultNodeProvider;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.swt.graphics.Image;

/**
 * Node provider for charts in outline view
 */

public class ChartViewNodeProvider extends DefaultNodeProvider
{

	public Object[] getChildren( Object object )
	{
		return new Object[]{};
	}

	public Image getNodeIcon( Object model )
	{
		DesignElementHandle handle = (DesignElementHandle) model;
		String iconPath = ChartUIConstants.IMAGE_OUTLINE;
//		if ( DEUtil.isLinkedElement( handle ) )
//		{
//			iconPath = ChartUIConstants.IMAGE_OUTLINE_LIB;
//		}
		if ( handle.getSemanticErrors( ).size( ) > 0
				&& !ChartReportItemBuilderImpl.isChartWizardOpen( ) )
		{
			iconPath = ChartUIConstants.IMAGE_OUTLINE_ERROR;
		}
		return decorateImage( UIHelper.getImage( iconPath ), model);
	}

	public boolean hasChildren( Object object )
	{
		return false;
	}

}
