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
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.TreeViewer;

/**
 * Provider for the MasterPages node
 * 
 * 
 */
public class MasterPagesNodeProvider extends DefaultNodeProvider {

	/**
	 * the text for new action
	 */
	public static final String ACTION_NEW = "MasterPageProcess.action.New"; //$NON-NLS-1$

	/**
	 * the text for edit action
	 */
	public static final String ACTION_EDIT = "MasterPageProcess.action.Edit"; //$NON-NLS-1$

	/**
	 * the text for display action
	 */
	public static final String MSG_DISPLAY = "MasterPageProcess.text.Display"; //$NON-NLS-1$

	public static final String MSG_CANNOTDEL = "MasterPageProcess.text.CannotDelete"; //$NON-NLS-1$

	public static final String DISPLAYNAME = "MasterPageProcess.text.DisplayName"; //$NON-NLS-1$

	/**
	 * Creates the context menu for the given object
	 * 
	 * @param menu   the menu
	 * @param object the object
	 */
	public void createContextMenu(TreeViewer sourceViewer, Object object, IMenuManager menu) {

		menu.add(new InsertAction(object));
		super.createContextMenu(sourceViewer, object, menu);
	}

	/**
	 * Gets the node display name of the given object.
	 * 
	 * @param object the object
	 * @return the display name
	 */
	public String getNodeDisplayName(Object object) {
		return MASTERPAGE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.views.INodeProvider#getIconName(
	 * java.lang.Object)
	 */
	public String getIconName(Object model) {
		return IReportGraphicConstants.ICON_NODE_MASTERPAGES;
	}
}
