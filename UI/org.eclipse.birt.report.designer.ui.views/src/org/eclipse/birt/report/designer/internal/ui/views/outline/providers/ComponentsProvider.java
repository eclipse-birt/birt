/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation .
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

package org.eclipse.birt.report.designer.internal.ui.views.outline.providers;

import java.util.Arrays;

import org.eclipse.birt.report.designer.internal.ui.views.DefaultNodeProvider;
import org.eclipse.birt.report.designer.internal.ui.views.actions.InsertAction;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.util.DesignElementComparator;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.TreeViewer;

/**
 * aImplements the provider for the Components node
 *
 */
public class ComponentsProvider extends DefaultNodeProvider {

	/**
	 * the text of the new action
	 */

	/**
	 * Creates the context menu for body node
	 *
	 * @param menu   the menu
	 * @param object the object
	 */
	@Override
	public void createContextMenu(TreeViewer sourceViewer, Object object, IMenuManager menu) {
		menu.add(new InsertAction(object));

		super.createContextMenu(sourceViewer, object, menu);
	}

	@Override
	public String getNodeDisplayName(Object object) {
		return COMPONENTS;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.views.INodeProvider#getIconName(
	 * java.lang.Object)
	 */
	@Override
	public String getIconName(Object model) {
		return IReportGraphicConstants.ICON_NODE_BODY;
	}

	/**
	 * Gets the children element of the given model using visitor.
	 *
	 * @param model the model
	 */
	@Override
	public Object[] getChildren(Object model) {
		if (model instanceof SlotHandle) {

			Object[] children = this.getChildrenBySlotHandle((SlotHandle) model);
			Arrays.sort(children, new DesignElementComparator());
			return children;

		}
		return new Object[] {};
	}
}
