/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.views.outline.providers;

import java.util.Map;

import org.eclipse.birt.report.designer.core.model.schematic.GridHandleAdapter;
import org.eclipse.birt.report.designer.core.model.schematic.HandleAdapterFactory;
import org.eclipse.birt.report.designer.internal.ui.views.DefaultNodeProvider;
import org.eclipse.birt.report.designer.internal.ui.views.actions.InsertAction;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.TreeViewer;

/**
 * Provider for GridItem
 *
 *
 */

public class GridProvider extends DefaultNodeProvider {

	/**
	 * Creates the context menu for the Grid.
	 *
	 * @param menu   the menu
	 * @param object the object
	 */
	@Override
	public void createContextMenu(TreeViewer sourceViewer, Object object, IMenuManager menu) {
		if (((ReportElementHandle) object).isValidLayoutForCompoundElement()) {
			menu.add(new InsertAction(object, Messages.getString("GridProvider.action.text"), //$NON-NLS-1$
					ReportDesignConstants.ROW_ELEMENT));
		}
		super.createContextMenu(sourceViewer, object, menu);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.internal.ui.views.outline.providers.
	 * INodeProvider#getChildren(java.lang.Object)
	 */
	@Override
	public Object[] getChildren(Object object) {
		GridHandle gridHdl = (GridHandle) object;
		return this.getChildrenBySlotHandle(gridHdl.getRows());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.internal.ui.views.DefaultNodeProvider#
	 * performInsert(java.lang.Object, org.eclipse.birt.model.api.SlotHandle,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	protected boolean performInsert(Object model, SlotHandle slotHandle, String type, String position, Map extendData)
			throws Exception {
		Assert.isLegal(type.equals(ReportDesignConstants.ROW_ELEMENT));

		GridHandleAdapter adapter = HandleAdapterFactory.getInstance().getGridHandleAdapter(model);

		if (slotHandle.getCount() > 0) {
			int rowNumber = HandleAdapterFactory.getInstance()
					.getRowHandleAdapter(slotHandle.get(slotHandle.getCount() - 1)).getRowNumber();
			adapter.insertRow(1, rowNumber);
		} else {
			adapter.insertRowInSlotHandle(slotHandle.getSlotID());
		}
		return true;
	}
}
