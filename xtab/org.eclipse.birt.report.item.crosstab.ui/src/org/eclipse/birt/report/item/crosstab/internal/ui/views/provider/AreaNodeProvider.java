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

import java.util.List;

import org.eclipse.birt.report.item.crosstab.core.ICrosstabReportItemConstants;
import org.eclipse.birt.report.item.crosstab.internal.ui.util.CrosstabUIHelper;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.graphics.Image;

public class AreaNodeProvider extends CrosstabWrapperNodeProvider {

	@Override
	public Object[] getChildren(Object model) {
		PropertyHandle handle = ((CrosstabPropertyHandleWrapper) model).getModel();
		String propertyName = handle.getPropertyDefn().getName();
		Object value = handle.getValue();
		if (value == null) {
			return new Object[0];
		}

		if (propertyName.equals(ICrosstabReportItemConstants.MEASURES_PROP)) {
			List measures = (List) value;
			return measures.toArray();
		}
		if (propertyName.equals(ICrosstabReportItemConstants.HEADER_PROP)) {
			return handle.getContents().toArray();
		}
		return new Object[0];
	}

	/**
	 * Creates the context menu
	 *
	 * @param sourceViewer the source viewer
	 * @param object       the object
	 * @param menu         the menu
	 */
	@Override
	public void createContextMenu(TreeViewer sourceViewer, Object object, IMenuManager menu) {
		// do nothing
	}

	@Override
	public Object getParent(Object model) {
		PropertyHandle handle = ((CrosstabPropertyHandleWrapper) model).getModel();
		return handle.getElementHandle();
	}

	@Override
	public boolean hasChildren(Object model) {
		return getChildren(model).length != 0;
	}

	@Override
	public String getNodeDisplayName(Object element) {
		PropertyHandle handle = ((CrosstabPropertyHandleWrapper) element).getModel();
		String propertyName = handle.getPropertyDefn().getName();

		if (propertyName.equals(ICrosstabReportItemConstants.COLUMNS_PROP)) {
			return Messages.getString("AreaNodeProvider.ColumnArea"); //$NON-NLS-1$
		}
		if (propertyName.equals(ICrosstabReportItemConstants.ROWS_PROP)) {
			return Messages.getString("AreaNodeProvider.RowArea"); //$NON-NLS-1$
		}
		if (propertyName.equals(ICrosstabReportItemConstants.MEASURES_PROP)) {
			return Messages.getString("AreaNodeProvider.DetailArea"); //$NON-NLS-1$
		}
		if (propertyName.equals(ICrosstabReportItemConstants.HEADER_PROP)) {
			return Messages.getString("AreaNodeProvider.Headers"); //$NON-NLS-1$
		}

		return null;
	}

	@Override
	public Image getNodeIcon(Object element) {
		PropertyHandle handle = ((CrosstabPropertyHandleWrapper) element).getModel();
		String propertyName = handle.getPropertyDefn().getName();

		if (propertyName.equals(ICrosstabReportItemConstants.COLUMNS_PROP)) {
			return CrosstabUIHelper.getImage(CrosstabUIHelper.COLUMNS_AREA_IMAGE);
		}
		if (propertyName.equals(ICrosstabReportItemConstants.ROWS_PROP)) {
			return CrosstabUIHelper.getImage(CrosstabUIHelper.ROWS_AREA_IMAGE);
		}
		if (propertyName.equals(ICrosstabReportItemConstants.MEASURES_PROP)) {
			return CrosstabUIHelper.getImage(CrosstabUIHelper.DETAIL_AREA_IMAGE);
		}
		if (propertyName.equals(ICrosstabReportItemConstants.HEADER_PROP)) {
			return CrosstabUIHelper.getImage(CrosstabUIHelper.HEADERS_AREA_IMAGE);
		}
		return super.getNodeIcon(element);
	}
}
