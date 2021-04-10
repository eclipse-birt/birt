/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.views.data.providers;

import org.eclipse.birt.report.designer.internal.ui.views.DefaultNodeProvider;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.TreeViewer;

/**
 * Deals with data sources node
 */
public class DataSourcesNodeProvider extends DefaultNodeProvider {

	/**
	 * Creates the context menu for the given object. Gets the action from the
	 * actionRegistry and adds them to the given menu.
	 * 
	 * @param menu   the menu
	 * @param object the object
	 */
	public void createContextMenu(TreeViewer sourceViewer, Object object, IMenuManager menu) {
		super.createContextMenu(sourceViewer, object, menu);
	}

	/**
	 * Gets the display name of the node
	 * 
	 * @param object the model
	 * @return Returns the display name of the node
	 */
	public String getNodeDisplayName(Object object) {
		return DefaultNodeProvider.DATASOURCES;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.views.INodeProvider#getIconName(
	 * java.lang.Object)
	 */
	public String getIconName(Object model) {
		return IReportGraphicConstants.ICON_NODE_DATA_SOURCES;
	}

	public Object[] getChildren(Object model) {
		if (model instanceof ModuleHandle)
			return new Object[] { ((ModuleHandle) model).getDataSources() };
		return ((SlotHandle) model).getElementHandle().getModuleHandle().getVisibleDataSources().toArray();
	}
}