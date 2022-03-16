/*******************************************************************************
 * Copyright (c) 2006, 2007 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
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

public class ChartViewNodeProvider extends DefaultNodeProvider {

	@Override
	public Object[] getChildren(Object object) {
		return new Object[] {};
	}

	@Override
	public Image getNodeIcon(Object model) {
		DesignElementHandle handle = (DesignElementHandle) model;
		String iconPath = ChartUIConstants.IMAGE_OUTLINE;
//		if ( DEUtil.isLinkedElement( handle ) )
//		{
//			iconPath = ChartUIConstants.IMAGE_OUTLINE_LIB;
//		}
		if (handle.getSemanticErrors().size() > 0 && !ChartReportItemBuilderImpl.isChartWizardOpen()) {
			iconPath = ChartUIConstants.IMAGE_OUTLINE_ERROR;
		}
		return decorateImage(UIHelper.getImage(iconPath), model);
	}

	@Override
	public boolean hasChildren(Object object) {
		return false;
	}

}
