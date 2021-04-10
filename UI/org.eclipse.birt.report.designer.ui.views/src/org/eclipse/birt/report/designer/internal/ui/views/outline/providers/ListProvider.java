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

import java.util.ArrayList;
import org.eclipse.birt.report.designer.internal.ui.views.DefaultNodeProvider;
import org.eclipse.birt.report.model.api.ListHandle;
import org.eclipse.birt.report.model.api.ListingHandle;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.TreeViewer;

/**
 * 
 * 
 * Provider for the List element. - Generates the context menu - Implements the
 * getChildren method.
 * 
 */

public class ListProvider extends DefaultNodeProvider {

	/**
	 * Creates the context menu for the given object. Gets the action from the
	 * actionRegistry for the given object and adds them to the menu
	 * 
	 * @param menu   the menu
	 * @param object the object
	 */
	public void createContextMenu(TreeViewer sourceViewer, Object object, IMenuManager menu) {
		super.createContextMenu(sourceViewer, object, menu);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.outline.providers.
	 * INodeProvider#getChildren(org.eclipse.birt.report.designer.core.facade.model.
	 * ModelContainer)
	 */
	public Object[] getChildren(Object object) {
		if (object instanceof ListHandle) {
			// List element children.
			ArrayList list = new ArrayList();
			ListHandle listHdl = (ListHandle) object;
			list.add(listHdl.getSlot(ListingHandle.HEADER_SLOT));
			list.add(listHdl.getSlot(ListingHandle.DETAIL_SLOT));
			list.add(listHdl.getSlot(ListingHandle.FOOTER_SLOT));
			list.add(listHdl.getSlot(ListingHandle.GROUP_SLOT));
			return list.toArray();
		}
		return super.getChildren(object);
	}
}