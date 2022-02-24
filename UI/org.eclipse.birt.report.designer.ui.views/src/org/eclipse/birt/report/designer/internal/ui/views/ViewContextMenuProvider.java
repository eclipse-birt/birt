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

package org.eclipse.birt.report.designer.internal.ui.views;

import java.util.Iterator;

import org.eclipse.birt.report.designer.internal.ui.util.Policy;
import org.eclipse.birt.report.designer.ui.ContextMenuProvider;
import org.eclipse.birt.report.designer.ui.views.ProviderFactory;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.IWorkbenchActionConstants;

/**
 * This class provides the context menu for the single selection and multiple
 * selection
 * 
 * 
 */
public class ViewContextMenuProvider extends ContextMenuProvider {

	/**
	 * constructor
	 * 
	 * @param viewer   the viewer
	 * @param registry the registry
	 */
	public ViewContextMenuProvider(ISelectionProvider viewer) {
		super(viewer);
	}

	/**
	 * Builds the context menu. Single selection menu and multiple selection menu
	 * are created while selecting just single element or multiple elements
	 * 
	 * 
	 * @param menu the menu
	 */
	public void buildContextMenu(IMenuManager menu) {
		menu.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		TreeViewer treeViewer = (TreeViewer) getViewer();

		IStructuredSelection selection = (IStructuredSelection) treeViewer.getSelection();

		// temporary solution
		Object input = treeViewer.getInput();
		if (input instanceof Object[]) {
			Object[] inputs = (Object[]) input;
			if (inputs.length == 1 && inputs[0] instanceof ReportDesignHandle) {
				for (Iterator iter = selection.iterator(); iter.hasNext();) {
					if (isIncludedLibrary(iter.next())) {
						return;
					}
				}
			}
		}

		if (selection.size() == 1) {
			// Create Single Selection Menu
			Object obj = selection.getFirstElement();
			if (ProviderFactory.createProvider(obj) != null) {
				ProviderFactory.createProvider(obj).createContextMenu(treeViewer, obj, menu);
			}
			if (Policy.TRACING_MENU_SHOW) {
				System.out.println(
						"Menu(for Views) >> Shows for " + ProviderFactory.createProvider(obj).getNodeDisplayName(obj)); //$NON-NLS-1$
			}
		} else {
			// Added by ywang on 2004.9.15
			// Create Multiple Selection Menu
			if (ProviderFactory.getDefaultProvider() != null) {
				ProviderFactory.getDefaultProvider().createContextMenu(treeViewer, selection, menu);
			}

			if (Policy.TRACING_MENU_SHOW) {
				System.out.println("Menu(for Views) >> Shows for multi-selcetion."); //$NON-NLS-1$
			}
		}
	}

	private boolean isIncludedLibrary(Object model) {
		if (model instanceof ReportElementHandle) {
			if (((ReportElementHandle) model).getModuleHandle() instanceof LibraryHandle) {
				return true;
			}
		}
		// else if ( model instanceof ReportElementModel )
		// {
		// return ( (ReportElementModel) model ).getElementHandle( )
		// .getModuleHandle( ) instanceof LibraryHandle;
		// }
		else if (model instanceof SlotHandle) {
			return ((SlotHandle) model).getElementHandle().getModuleHandle() instanceof LibraryHandle;
		}
		return false;

	}
}
