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

import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.views.DefaultNodeProvider;
import org.eclipse.birt.report.designer.internal.ui.views.actions.InsertAction;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.model.api.ListGroupHandle;
import org.eclipse.birt.report.model.api.ListHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.TableGroupHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.TreeViewer;

/**
 * Provider for the ListBand - List header, footer, detail. - Populates the
 * menus for the list band node type - Implements the getDisplayName.
 *
 *
 */
public class ListBandProvider extends DefaultNodeProvider {

	/**
	 * Creates the context menu for the given object.
	 *
	 * @param menu   the menu
	 * @param object the object
	 */
	@Override
	public void createContextMenu(TreeViewer sourceViewer, Object object, IMenuManager menu) {
		if (!(object instanceof SlotHandle)) {
			return;
		}
		SlotHandle model = (SlotHandle) object;
		if (model.getElementHandle() instanceof ListHandle && model.getSlotID() == ListHandle.GROUP_SLOT) {
			InsertAction insertAction = new InsertAction(object, Messages.getString("ListBandProvider.action.text"), //$NON-NLS-1$
					ReportDesignConstants.LIST_GROUP_ELEMENT);
			menu.add(insertAction);
		} else {
			menu.add(new InsertAction(object));
		}
		super.createContextMenu(sourceViewer, object, menu);
	}

	/**
	 * Gets the display name of the node
	 *
	 * @param object the object
	 */
	@Override
	public String getNodeDisplayName(Object object) {
		SlotHandle model = (SlotHandle) object;
		if (model.getElementHandle() instanceof ListHandle) {
			switch (model.getSlotID()) {
			case ListHandle.HEADER_SLOT:
				return HEADER_DISPALYNAME;
			case ListHandle.FOOTER_SLOT:
				return FOOTER_DISPALYNAME;
			case ListHandle.DETAIL_SLOT:
				return DETAIL_DISPALYNAME;
			case ListHandle.GROUP_SLOT:
				return GROUPS_DISPALYNAME;
			}
		} else if (model.getElementHandle() instanceof ListGroupHandle) {
			switch (model.getSlotID()) {
			case ListGroupHandle.HEADER_SLOT:
				return HEADER_DISPALYNAME;
			case ListGroupHandle.FOOTER_SLOT:
				return FOOTER_DISPALYNAME;
			}
		}
		return super.getNodeDisplayName(model);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.views.INodeProvider#getIconName(
	 * java.lang.Object)
	 */
	@Override
	public String getIconName(Object object) {
		SlotHandle model = (SlotHandle) object;
		if (model.getElementHandle() instanceof ListHandle) {
			switch (model.getSlotID()) {
			case TableHandle.HEADER_SLOT:
				return IReportGraphicConstants.ICON_NODE_HEADER;
			case TableHandle.FOOTER_SLOT:
				return IReportGraphicConstants.ICON_NODE_FOOTER;
			case TableHandle.DETAIL_SLOT:
				return IReportGraphicConstants.ICON_NODE_DETAILS;
			case TableHandle.GROUP_SLOT:
				return IReportGraphicConstants.ICON_NODE_GROUPS;
			}
		} else if (model.getElementHandle() instanceof ListGroupHandle) {
			switch (model.getSlotID()) {
			case TableGroupHandle.HEADER_SLOT:
				return IReportGraphicConstants.ICON_NODE_GROUP_HEADER;
			case TableGroupHandle.FOOTER_SLOT:
				return IReportGraphicConstants.ICON_NODE_GROUP_FOOTER;
			}
		}
		return super.getIconName(model);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.internal.ui.views.DefaultNodeProvider#
	 * performInsert(java.lang.Object, org.eclipse.birt.report.model.api.SlotHandle,
	 * java.lang.String, java.lang.String, java.util.Map)
	 */
	@Override
	protected boolean performInsert(Object model, SlotHandle slotHandle, String type, String position, Map extendData)
			throws Exception {
		if (ReportDesignConstants.LIST_GROUP_ELEMENT.equals(type)) {
			return UIUtil.createGroup(((SlotHandle) model).getElementHandle());
		}
		return super.performInsert(model, slotHandle, type, position, extendData);
	}
}
