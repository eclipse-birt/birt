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
import org.eclipse.birt.report.designer.ui.actions.ShowPropertyAction;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.IWorkbenchActionConstants;

/**
 * Deals with the data source node
 */
public class DataSourceNodeProvider extends DefaultNodeProvider {

	/**
	 * Creates the context menu for the given object.
	 * 
	 * @param menu   the menu
	 * @param object the object
	 */
	public void createContextMenu(TreeViewer sourceViewer, Object object, IMenuManager menu) {
		super.createContextMenu(sourceViewer, object, menu);

		menu.insertBefore(IWorkbenchActionConstants.MB_ADDITIONS + "-refresh", //$NON-NLS-1$
				new ShowPropertyAction(object));
	}

	/**
	 * Gets the children list of the given model
	 * 
	 * @param model the model
	 */
	public Object[] getChildren(Object model) {
		return new Object[] {};
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.INodeProvider#
	 * getNodeDisplayName(java.lang.Object)
	 */
	public String getNodeDisplayName(Object model) {
		return DEUtil.getDisplayLabel(model, false);
	}

}