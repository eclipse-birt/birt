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

package org.eclipse.birt.report.item.crosstab.internal.ui.views.provider;

import org.eclipse.birt.report.designer.internal.ui.views.DefaultNodeProvider;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabReportItemConstants;
import org.eclipse.birt.report.item.crosstab.core.IMeasureViewConstants;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.MeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.internal.ui.util.CrosstabUIHelper;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;

public class CrossTabMeasureNodeProvider extends DefaultNodeProvider {

	public Object[] getChildren(Object model) {
		ExtendedItemHandle element = (ExtendedItemHandle) model;
		return new Object[] {
				new CrosstabPropertyHandleWrapper(element.getPropertyHandle(IMeasureViewConstants.HEADER_PROP)),
				new CrosstabPropertyHandleWrapper(element.getPropertyHandle(IMeasureViewConstants.DETAIL_PROP)),
				new CrosstabPropertyHandleWrapper(element.getPropertyHandle(IMeasureViewConstants.AGGREGATIONS_PROP)) };

	}

	public void createContextMenu(TreeViewer sourceViewer, Object object, IMenuManager menu) {
		// do nothing

	}

	public Object getParent(Object model) {
		ExtendedItemHandle element = (ExtendedItemHandle) model;
		try {
			MeasureViewHandle measure = (MeasureViewHandle) element.getReportItem();
			if (measure.getContainer() != null) {
				CrosstabReportItemHandle crossTab = (CrosstabReportItemHandle) measure.getContainer();
				return new CrosstabPropertyHandleWrapper(
						crossTab.getModelHandle().getPropertyHandle(ICrosstabReportItemConstants.MEASURES_PROP));
			}
		} catch (ExtendedElementException e) {
		}

		return null;
	}

	public boolean hasChildren(Object model) {
		return getChildren(model).length != 0;
	}

	public String getNodeDisplayName(Object model) {
		ExtendedItemHandle element = (ExtendedItemHandle) model;
		try {
			MeasureViewHandle measure = (MeasureViewHandle) element.getReportItem();
			return Messages.getString("CrossTabMeasureNodeProvider.Detail") + measure.getCubeMeasureName(); //$NON-NLS-1$
		} catch (ExtendedElementException e) {
		}
		return super.getNodeDisplayName(model);
	}

	public Image getNodeIcon(Object element) {
		if (element instanceof DesignElementHandle && ((DesignElementHandle) element).getSemanticErrors().size() > 0) {
			return ReportPlatformUIImages.getImage(ISharedImages.IMG_OBJS_ERROR_TSK);
		}
		return CrosstabUIHelper.getImage(CrosstabUIHelper.DETAIL_IMAGE);
	}
}
