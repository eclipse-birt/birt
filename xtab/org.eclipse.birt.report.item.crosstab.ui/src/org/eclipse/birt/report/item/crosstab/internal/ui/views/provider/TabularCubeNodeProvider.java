/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.item.crosstab.internal.ui.views.provider;

import org.eclipse.birt.report.designer.internal.ui.views.DefaultNodeProvider;
import org.eclipse.birt.report.designer.internal.ui.views.actions.RefreshAction;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.ui.actions.ShowPropertyAction;
import org.eclipse.birt.report.designer.ui.cubebuilder.action.EditCubeAction;
import org.eclipse.birt.report.designer.ui.cubebuilder.page.CubeBuilder;
import org.eclipse.birt.report.designer.ui.cubebuilder.util.BuilderConstants;
import org.eclipse.birt.report.designer.ui.cubebuilder.util.UIHelper;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.action.InsertCubeInLayoutAction;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.TabularCubeHandle;
import org.eclipse.birt.report.model.elements.interfaces.ICubeModel;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;

/**
 * Deals with dataset node
 * 
 */
public class TabularCubeNodeProvider extends DefaultNodeProvider {

	/**
	 * Creates the context menu for the given object. Gets the action from the
	 * actionRegistry and adds the action to the menu.
	 * 
	 * @param menu   the menu
	 * @param object the object
	 */
	public void createContextMenu(TreeViewer sourceViewer, Object object, IMenuManager menu) {

		menu.add(new Separator());

		InsertCubeInLayoutAction insertAction = new InsertCubeInLayoutAction(object);
		if (insertAction.isEnabled()) {
			menu.add(insertAction);
		}

		super.createContextMenu(sourceViewer, object, menu);

		if (((CubeHandle) object).canEdit()) {
			menu.insertAfter(IWorkbenchActionConstants.MB_ADDITIONS,
					new EditCubeAction(object, Messages.getString("InsertCubeInLayoutAction.menu.text"))); //$NON-NLS-1$
		}

		menu.insertBefore(IWorkbenchActionConstants.MB_ADDITIONS + "-refresh", //$NON-NLS-1$
				new ShowPropertyAction(object));

		menu.insertAfter(IWorkbenchActionConstants.MB_ADDITIONS + "-refresh", new Separator()); //$NON-NLS-1$
		IAction action = new RefreshAction(sourceViewer);
		if (action.isEnabled()) {
			menu.insertAfter(IWorkbenchActionConstants.MB_ADDITIONS + "-refresh", action); //$NON-NLS-1$
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.designer.internal.ui.views.INodeProvider#
	 * getNodeDisplayName(java.lang.Object)
	 */
	public String getNodeDisplayName(Object model) {
		CubeHandle handle = (CubeHandle) model;
		return handle.getName();
	}

	/**
	 * Gets the children element of the given model using visitor.
	 * 
	 * @param object the handle
	 */

	public Object[] getChildren(Object object) {
		CubeHandle handle = (CubeHandle) object;
		return new Object[] { handle.getPropertyHandle(ICubeModel.DIMENSIONS_PROP),
				handle.getPropertyHandle(ICubeModel.MEASURE_GROUPS_PROP) };
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.DefaultNodeProvider
	 * #hasChildren(java.lang.Object)
	 */
	public boolean hasChildren(Object object) {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.designer.internal.ui.views.INodeProvider#
	 * getNodeDisplayName(java.lang.Object)
	 */
	protected boolean performEdit(ReportElementHandle handle) {
		TabularCubeHandle cubeHandle = (TabularCubeHandle) handle;
		CubeBuilder dialog = new CubeBuilder(PlatformUI.getWorkbench().getDisplay().getActiveShell(), cubeHandle);

		return dialog.open() == Dialog.OK;
	}

	public Image getNodeIcon(Object model) {
		if (model instanceof DesignElementHandle && ((DesignElementHandle) model).getSemanticErrors().size() > 0) {
			return ReportPlatformUIImages.getImage(ISharedImages.IMG_OBJS_ERROR_TSK);
		}

		return decorateImage(UIHelper.getImage(BuilderConstants.IMAGE_CUBE), model);
	}
}
