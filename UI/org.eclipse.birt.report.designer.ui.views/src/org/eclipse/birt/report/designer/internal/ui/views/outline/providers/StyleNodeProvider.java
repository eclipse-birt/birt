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
import org.eclipse.birt.report.designer.internal.ui.views.actions.EditAction;
import org.eclipse.birt.report.designer.internal.ui.views.actions.ImportCSSStyleAction;
import org.eclipse.birt.report.designer.internal.ui.views.actions.InsertAction;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.dialogs.StyleBuilder;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.AbstractThemeHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;

/**
 * Deals with style node
 *
 */
public class StyleNodeProvider extends DefaultNodeProvider {

	/**
	 * Creates the context menu for the given object.
	 *
	 * @param object the object
	 * @param menu   the menu
	 */
	@Override
	public void createContextMenu(TreeViewer sourceViewer, Object object, IMenuManager menu) {
		Object parent = getParent(object);
		if (parent != null) {
			menu.add(new InsertAction(parent, Messages.getString("StyleNodeProvider.action.New"))); //$NON-NLS-1$
		}

		super.createContextMenu(sourceViewer, object, menu);

		if (((StyleHandle) object).canEdit()) {
			menu.insertAfter(IWorkbenchActionConstants.MB_ADDITIONS,
					new EditAction(object, Messages.getString("StyleNodeProvider.action.Edit"))); //$NON-NLS-1$
		}

		menu.insertAfter(IWorkbenchActionConstants.MB_ADDITIONS, new Separator());

		menu.insertAfter(IWorkbenchActionConstants.MB_ADDITIONS, new ImportCSSStyleAction(object)); // $NON-NLS-1$

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.internal.ui.views.INodeProvider#
	 * getNodeDisplayName(java.lang.Object)
	 */
	@Override
	protected boolean performEdit(ReportElementHandle handle) {

		StyleBuilder builder = new StyleBuilder(PlatformUI.getWorkbench().getDisplay().getActiveShell(), handle,
				handle.getContainer() instanceof AbstractThemeHandle ? (AbstractThemeHandle) handle.getContainer()
						: null,
				StyleBuilder.DLG_TITLE_EDIT);
		return builder.open() == Window.OK;
	}

	@Override
	public String getNodeDisplayName(Object model) {
		return DEUtil.getDisplayLabel(model, false);
	}

	@Override
	public String getNodeTooltip(Object model) {
		if (model instanceof StyleHandle) {
			return ((StyleHandle) model).getName();
		} else {
			return super.getNodeTooltip(model);
		}

	}
}
