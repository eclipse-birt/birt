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

package org.eclipse.birt.report.designer.data.ui.providers;

import org.eclipse.birt.report.designer.data.ui.actions.NewDataSourceAction;
import org.eclipse.birt.report.designer.internal.ui.views.data.providers.DataSourcesNodeProvider;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.TreeViewer;

/**
 * Deals with data sources node
 */
public class EditableDataSourcesNodeProvider extends DataSourcesNodeProvider {

	/**
	 * Creates the context menu for the given object. Gets the action from the
	 * actionRegistry and adds them to the given menu.
	 * 
	 * @param menu   the menu
	 * @param object the object
	 */
	public void createContextMenu(TreeViewer sourceViewer, Object object, IMenuManager menu) {
		menu.add(new NewDataSourceAction(Messages.getString("datasource.action.new"))); //$NON-NLS-1$

		super.createContextMenu(sourceViewer, object, menu);
	}

}