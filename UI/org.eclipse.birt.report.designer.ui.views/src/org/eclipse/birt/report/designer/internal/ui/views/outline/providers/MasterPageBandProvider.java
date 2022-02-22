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

import org.eclipse.birt.report.designer.internal.ui.views.DefaultNodeProvider;
import org.eclipse.birt.report.designer.internal.ui.views.actions.InsertAction;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.util.DNDUtil;
import org.eclipse.birt.report.model.api.SimpleMasterPageHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.TreeViewer;

/**
 * Provider for the MasterPageBand - MasterPage header, footer. - Populates the
 * menus for the list band node type - Implements the getDisplayName.
 *
 *
 */
public class MasterPageBandProvider extends DefaultNodeProvider {

	/**
	 * Creates the context menu for the given object.
	 *
	 * @param menu   the menu
	 * @param object the object
	 */
	@Override
	public void createContextMenu(TreeViewer sourceViewer, Object object, IMenuManager menu) {
		if ((object instanceof SlotHandle)) {
			SlotHandle model = (SlotHandle) object;
			if (model.getElementHandle() instanceof SimpleMasterPageHandle) {
				if (DNDUtil.handleValidateTargetCanContainMore(model, 0)) {
					// New subSelection
					menu.add(new InsertAction(object));
				}
			}
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
		if (model.getElementHandle() instanceof SimpleMasterPageHandle) {
			switch (model.getSlotID()) {
			case SimpleMasterPageHandle.PAGE_HEADER_SLOT:
				return HEADER_DISPALYNAME;
			case SimpleMasterPageHandle.PAGE_FOOTER_SLOT:
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
		if (model.getElementHandle() instanceof SimpleMasterPageHandle) {
			switch (model.getSlotID()) {
			case SimpleMasterPageHandle.PAGE_HEADER_SLOT:
				return IReportGraphicConstants.ICON_NODE_HEADER;
			case SimpleMasterPageHandle.PAGE_FOOTER_SLOT:
				return IReportGraphicConstants.ICON_NODE_FOOTER;
			}
		}
		return super.getIconName(model);
	}
}
