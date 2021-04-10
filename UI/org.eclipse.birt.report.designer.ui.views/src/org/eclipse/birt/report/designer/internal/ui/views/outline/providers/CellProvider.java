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

package org.eclipse.birt.report.designer.internal.ui.views.outline.providers;

import org.eclipse.birt.report.designer.internal.ui.views.DefaultNodeProvider;
import org.eclipse.birt.report.designer.internal.ui.views.actions.CopyCellContentsAction;
import org.eclipse.birt.report.designer.internal.ui.views.actions.InsertAction;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.TreeViewer;

/**
 * Provider for the Cell node
 * 
 */
public class CellProvider extends DefaultNodeProvider {

	/**
	 * Creates the context menu for the given object.
	 * 
	 * @param menu   the menu
	 * @param object the object
	 */
	public void createContextMenu(TreeViewer sourceViewer, Object object, IMenuManager menu) {
		menu.add(new InsertAction(object));
		menu.add(new CopyCellContentsAction(object));
		super.createContextMenu(sourceViewer, object, menu);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.outline.providers.
	 * INodeProvider#getChildren(java.lang.Object)
	 */
	public Object[] getChildren(Object model) {
		CellHandle cell = (CellHandle) model;
		return this.getChildrenBySlotHandle(cell.getContent());
	}
}