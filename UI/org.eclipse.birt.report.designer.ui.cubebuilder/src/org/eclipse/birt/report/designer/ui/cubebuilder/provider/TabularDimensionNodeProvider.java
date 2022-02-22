/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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

package org.eclipse.birt.report.designer.ui.cubebuilder.provider;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.views.DefaultNodeProvider;
import org.eclipse.birt.report.designer.internal.ui.views.actions.RefreshAction;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.ui.actions.ShowPropertyAction;
import org.eclipse.birt.report.designer.ui.cubebuilder.action.EditCubeDimensionAction;
import org.eclipse.birt.report.designer.ui.cubebuilder.nls.Messages;
import org.eclipse.birt.report.designer.ui.cubebuilder.page.CubeBuilder;
import org.eclipse.birt.report.designer.ui.cubebuilder.util.BuilderConstants;
import org.eclipse.birt.report.designer.ui.cubebuilder.util.UIHelper;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.HierarchyHandle;
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
public class TabularDimensionNodeProvider extends DefaultNodeProvider {

	/**
	 * Creates the context menu for the given object. Gets the action from the
	 * actionRegistry and adds the action to the menu.
	 *
	 * @param menu   the menu
	 * @param object the object
	 */
	@Override
	public void createContextMenu(TreeViewer sourceViewer, Object object, IMenuManager menu) {
		super.createContextMenu(sourceViewer, object, menu);

		if (((DimensionHandle) object).canEdit()) {
			menu.insertAfter(IWorkbenchActionConstants.MB_ADDITIONS,
					new EditCubeDimensionAction(object, Messages.getString("CubeDimensionNodeProvider.menu.text"))); //$NON-NLS-1$
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
	@Override
	public String getNodeDisplayName(Object model) {
		DimensionHandle dimension = (DimensionHandle) model;
		return dimension.getName();
	}

	/**
	 * Gets the children element of the given model using visitor.
	 *
	 * @param object the handle
	 */
	@Override
	public Object[] getChildren(Object object) {
		HierarchyHandle hierarchy = (HierarchyHandle) ((DimensionHandle) object)
				.getContent(DimensionHandle.HIERARCHIES_PROP, 0);
		List list = new ArrayList();
		for (int i = 0; i < hierarchy.getLevelCount(); i++) {
			list.add(hierarchy.getLevel(i));
		}
		return list.toArray();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.internal.ui.views.DefaultNodeProvider
	 * #hasChildren(java.lang.Object)
	 */
	@Override
	public boolean hasChildren(Object object) {
		return getChildren(object).length > 0;
	}

	@Override
	public Object getParent(Object model) {
		DimensionHandle dimension = (DimensionHandle) model;
		CubeHandle cube = (CubeHandle) dimension.getContainer();
		if (cube != null) {
			return cube.getPropertyHandle(ICubeModel.DIMENSIONS_PROP);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.designer.internal.ui.views.INodeProvider#
	 * getNodeDisplayName(java.lang.Object)
	 */
	@Override
	protected boolean performEdit(ReportElementHandle handle) {
		DimensionHandle dimensionHandle = (DimensionHandle) handle;
		CubeBuilder dialog = new CubeBuilder(PlatformUI.getWorkbench().getDisplay().getActiveShell(),
				(TabularCubeHandle) dimensionHandle.getContainer());

		dialog.showPage(CubeBuilder.GROUPPAGE);

		return dialog.open() == Dialog.OK;
	}

	@Override
	public Image getNodeIcon(Object model) {
		if (model instanceof DesignElementHandle && ((DesignElementHandle) model).getSemanticErrors().size() > 0) {
			return ReportPlatformUIImages.getImage(ISharedImages.IMG_OBJS_ERROR_TSK);
		}
		return UIHelper.getImage(BuilderConstants.IMAGE_DIMENSION);
	}

}
