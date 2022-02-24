/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

import org.eclipse.birt.report.designer.core.model.views.outline.LibraryNode;
import org.eclipse.birt.report.designer.internal.ui.views.DefaultNodeProvider;
import org.eclipse.birt.report.designer.internal.ui.views.actions.ImportLibraryAction;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.TreeViewer;

/**
 * Node provider for embedded images resources node
 */

public class LibrariesNodeProvider extends DefaultNodeProvider {

	// private static final String SUPPORTED_IMAGE_FILE_EXTS = Messages
	// .getString( "ImageBuilderDialog.FileDialog.FilterMessage" );
	// //$NON-NLS-1$

	private static final String LIBARIES = Messages.getString("LibrariesNodeProvider.DisplayLabel.Libraries"); //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.views.INodeProvider#getChildren(
	 * java.lang.Object)
	 */
	@Override
	public Object[] getChildren(Object model) {
		return ((LibraryNode) model).getChildren();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.internal.ui.views.DefaultNodeProvider#
	 * getIconName(java.lang.Object)
	 */
	@Override
	public String getIconName(Object model) {
		return IReportGraphicConstants.ICON_NODE_LIBRARIES;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.internal.ui.views.INodeProvider#
	 * getNodeDisplayName(java.lang.Object)
	 */
	@Override
	public String getNodeDisplayName(Object model) {
		return LIBARIES;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.views.INodeProvider#getParent(
	 * java.lang.Object)
	 */
	@Override
	public Object getParent(Object model) {
		return ((LibraryNode) model).getReportDesignHandle();
	}

	/**
	 * Creates the context menu for the given object.
	 *
	 * @param object the object
	 * @param menu   the menu
	 */
	@Override
	public void createContextMenu(TreeViewer sourceViewer, Object object, IMenuManager menu) {
		menu.add(new ImportLibraryAction());
	}

}
