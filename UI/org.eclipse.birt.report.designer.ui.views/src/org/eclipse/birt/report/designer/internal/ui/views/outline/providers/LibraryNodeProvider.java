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

import java.util.ArrayList;

import org.eclipse.birt.report.designer.core.model.views.outline.EmbeddedImageNode;
import org.eclipse.birt.report.designer.core.model.views.outline.LibraryNode;
import org.eclipse.birt.report.designer.core.model.views.outline.ScriptsNode;
import org.eclipse.birt.report.designer.internal.ui.views.actions.PublishLibraryToResourceFolderAction;
import org.eclipse.birt.report.designer.internal.ui.views.actions.RefreshModuleHandleAction;
import org.eclipse.birt.report.designer.internal.ui.views.actions.RemoveLibraryAction;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.TreeViewer;

/**
 * Root node - Report Library node provider - Implements the getChildren -
 * Implements the getNodeDiplayName
 * 
 */
public class LibraryNodeProvider extends ReportDesignNodeProvider {

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
			list.add(handle.getParameters());
			// Add the children handle - Components
			// list.add( new ReportElementModel( handle.getComponents( ) ) );
			list.add(handle.getComponents());
			// Add the children handle - Master Pages
			// Remove master pages node.
			list.add(handle.getMasterPages());
			// Add the children handle - Themes
			list.add(handle.getThemes());
			// Add the children handle - Embedded Images
			list.add(new EmbeddedImageNode(handle));
			list.add(new LibraryNode(handle));
			list.add(new ScriptsNode(handle));
		}
		return list.toArray();

	}

	public void createContextMenu(TreeViewer sourceViewer, Object object, IMenuManager menu) {
		menu.add(new RefreshModuleHandleAction(object));
		if (sourceViewer.getInput() instanceof Object[]) {
			Object[] inputs = (Object[]) sourceViewer.getInput();
			if (inputs.length == 1) {
				if (object instanceof LibraryHandle) {
					LibraryHandle handle = ((LibraryHandle) object);

					if (handle.getNamespace() == null) {
						menu.add(new PublishLibraryToResourceFolderAction(object));
					}

					if (handle.getNamespace() != null) {
						menu.add(new RemoveLibraryAction(object));
					}
				}
			}
		}
	}

	public String getNodeDisplayName(Object model) {
		LibraryHandle handle = (LibraryHandle) model;
		if (handle.getName() != null && !handle.getName().equals("")) //$NON-NLS-1$
		{
			return handle.getName();
		}
		if (handle.getNamespace() != null) {
			return handle.getNamespace();
		}
		return super.getNodeDisplayName(model);
	}

	public Object getParent(Object model) {
		LibraryHandle handle = ((LibraryHandle) model);
		if (handle.getNamespace() == null) {
			return null;
		}
		return new LibraryNode(handle.getHostHandle());
	}

	public String getIconName(Object model) {
		LibraryHandle handle = ((LibraryHandle) model);
		if (handle.getNamespace() != null) {
			return IReportGraphicConstants.ICON_ELEMENT_LIBRARY_REFERENCED;
		}
		return IReportGraphicConstants.ICON_ELEMENT_LIBRARY;
	}

	public String getNodeTooltip(Object model) {
		LibraryHandle handle = ((LibraryHandle) model);
		if (handle.getNamespace() != null) {
			return handle.getRelativeFileName();
		}
		return super.getNodeTooltip(model);
	}

}
