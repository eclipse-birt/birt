/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.item.crosstab.internal.ui.views.provider;

import org.eclipse.birt.report.designer.internal.ui.views.DefaultNodeProvider;
import org.eclipse.birt.report.item.crosstab.core.IMeasureViewConstants;
import org.eclipse.birt.report.item.crosstab.core.de.MeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.internal.ui.util.CrosstabUIHelper;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.graphics.Image;

public class MeasureSubNodeProvider extends DefaultNodeProvider {

	public void createContextMenu(TreeViewer sourceViewer, Object object, IMenuManager menu) {

	}

	public Object[] getChildren(Object model) {
		PropertyHandle handle = ((CrosstabPropertyHandleWrapper) model).getModel();
		ExtendedItemHandle element = (ExtendedItemHandle) handle.getElementHandle();
		try {
			MeasureViewHandle measure = (MeasureViewHandle) element.getReportItem();
			String propertyName = handle.getPropertyDefn().getName();
			Object value = handle.getValue();
			if (value == null)
				return new Object[0];

			if (propertyName.equals(IMeasureViewConstants.HEADER_PROP)) {
				return new Object[] { measure.getHeader().getModelHandle() };
			} else if (propertyName.equals(IMeasureViewConstants.DETAIL_PROP)) {
				return new Object[] { measure.getCell().getModelHandle() };
			} else if (propertyName.equals(IMeasureViewConstants.AGGREGATIONS_PROP)) {
				int count = measure.getAggregationCount();
				Object[] aggs = new Object[count];
				for (int i = 0; i < count; i++) {
					aggs[i] = measure.getAggregationCell(i).getModelHandle();
				}
				return aggs;
			}
		} catch (ExtendedElementException e) {
		}
		return new Object[0];
	}

	public Object getParent(Object model) {
		PropertyHandle handle = ((CrosstabPropertyHandleWrapper) model).getModel();
		return handle.getElementHandle();
	}

	public boolean hasChildren(Object model) {
		return getChildren(model).length != 0;
	}

	public String getNodeDisplayName(Object element) {
		PropertyHandle handle = ((CrosstabPropertyHandleWrapper) element).getModel();
		String propertyName = handle.getPropertyDefn().getName();

		if (propertyName.equals(IMeasureViewConstants.HEADER_PROP)) {
			return Messages.getString("MeasureSubNodeProvider.Header"); //$NON-NLS-1$
		} else if (propertyName.equals(IMeasureViewConstants.DETAIL_PROP)) {
			return Messages.getString("MeasureSubNodeProvider.Detail"); //$NON-NLS-1$
		} else if (propertyName.equals(IMeasureViewConstants.AGGREGATIONS_PROP)) {
			return Messages.getString("MeasureSubNodeProvider.Aggregation"); //$NON-NLS-1$
		}
		return super.getNodeDisplayName(element);
	}

	public Image getNodeIcon(Object element) {
		PropertyHandle handle = ((CrosstabPropertyHandleWrapper) element).getModel();
		String propertyName = handle.getPropertyDefn().getName();
		if (propertyName.equals(IMeasureViewConstants.HEADER_PROP)) {
			return CrosstabUIHelper.getImage(CrosstabUIHelper.HEADER_IMAGE);
		} else if (propertyName.equals(IMeasureViewConstants.DETAIL_PROP)) {
			return CrosstabUIHelper.getImage(CrosstabUIHelper.DETAIL_IMAGE);
		} else if (propertyName.equals(IMeasureViewConstants.AGGREGATIONS_PROP)) {
			return CrosstabUIHelper.getImage(CrosstabUIHelper.AGGREGATION_IMAGE);
		}
		return super.getNodeIcon(element);
	}
}
