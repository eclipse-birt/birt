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

package org.eclipse.birt.report.designer.internal.ui.views.data.providers;

import java.util.ArrayList;

import org.eclipse.birt.report.designer.internal.ui.views.outline.providers.LibraryNodeProvider;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.TreeViewer;

/**
 * Root node - Report design node provider - Implements the getChildren -
 * Implements the getNodeDiplayName
 * 
 * 
 */
public class LibraryDataNodeProvider extends LibraryNodeProvider {

	/**
	 * Gets the children of the given model. The default children element include
	 * following: Body,Styles,MasterPage
	 * 
	 * @param model the given report design
	 * @return the result list that contains the model
	 */
	public Object[] getChildren(Object model) {

		// Report design may not be the current, use model to get.

		LibraryHandle handle = ((LibraryHandle) model);
		ArrayList list = new ArrayList();
		if (handle.getNamespace() == null) {
			list.add(handle.getDataSources());
			list.add(handle.getDataSets());
		}

		return list.toArray();
	}

	public void createContextMenu(TreeViewer sourceViewer, Object object, IMenuManager menu) {
	}

}
