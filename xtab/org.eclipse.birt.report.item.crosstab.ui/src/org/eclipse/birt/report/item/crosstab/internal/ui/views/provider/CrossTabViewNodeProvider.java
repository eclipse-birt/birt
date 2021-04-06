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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.views.DefaultNodeProvider;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.internal.ui.util.CrosstabUIHelper;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.graphics.Image;

public class CrossTabViewNodeProvider extends DefaultNodeProvider {

	/**
	 * Gets the children element of the given model using visitor.
	 * 
	 * @param model the model
	 */
	public Object[] getChildren(Object model) {
		ExtendedItemHandle handle = (ExtendedItemHandle) model;
		try {
			CrosstabViewHandle crossTabViewHandle = (CrosstabViewHandle) handle.getReportItem();
			int dimensionCount = crossTabViewHandle.getDimensionCount();
			List list = new ArrayList();
			for (int i = 0; i < dimensionCount; i++) {
				DimensionViewHandle dimension = crossTabViewHandle.getDimension(i);
				int levelCount = dimension.getLevelCount();
				for (int j = 0; j < levelCount; j++) {
					list.add(dimension.getLevel(j).getModelHandle());
				}
			}
			if (crossTabViewHandle.getGrandTotal() != null) {
				list.add(crossTabViewHandle.getGrandTotal().getModelHandle());
			}
			return list.toArray();
		} catch (ExtendedElementException e) {
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
	public void createContextMenu(TreeViewer sourceViewer, Object object, IMenuManager menu) {
		// do nothing

	}

	public Image getNodeIcon(Object model) {
		ExtendedItemHandle handle = (ExtendedItemHandle) model;

		try {
			CrosstabViewHandle crossTabViewHandle = (CrosstabViewHandle) handle.getReportItem();
			if (crossTabViewHandle.getAxisType() == ICrosstabConstants.COLUMN_AXIS_TYPE) {
				return CrosstabUIHelper.getImage(CrosstabUIHelper.COLUMNS_AREA_IMAGE);
			}
			if (crossTabViewHandle.getAxisType() == ICrosstabConstants.ROW_AXIS_TYPE) {
				return CrosstabUIHelper.getImage(CrosstabUIHelper.ROWS_AREA_IMAGE);
			}
		} catch (ExtendedElementException e) {
		}
		return super.getNodeIcon(model);
	}

	public String getNodeDisplayName(Object model) {
		ExtendedItemHandle handle = (ExtendedItemHandle) model;
		try {
			CrosstabViewHandle crossTabViewHandle = (CrosstabViewHandle) handle.getReportItem();
			if (crossTabViewHandle.getAxisType() == ICrosstabConstants.COLUMN_AXIS_TYPE) {
				return Messages.getString("CrossTabViewNodeProvider.ColumnArea"); //$NON-NLS-1$
			}
			if (crossTabViewHandle.getAxisType() == ICrosstabConstants.ROW_AXIS_TYPE) {
				return Messages.getString("CrossTabViewNodeProvider.RowArea"); //$NON-NLS-1$
			}
		} catch (ExtendedElementException e) {
		}
		return super.getNodeDisplayName(model);
	}

	public boolean hasChildren(Object model) {
		return getChildren(model).length != 0;
	}

	public Object getParent(Object model) {
		ExtendedItemHandle handle = (ExtendedItemHandle) model;
		try {
			CrosstabViewHandle crossTabViewHandle = (CrosstabViewHandle) handle.getReportItem();
			return crossTabViewHandle.getCrosstab().getModelHandle();
		} catch (ExtendedElementException e) {
		}
		return null;
	}
}
