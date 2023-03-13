/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors: Actuate Corporation - Initial implementation.
 ******************************************************************************/

package org.eclipse.birt.report.designer.ui.actions;

import org.eclipse.birt.report.designer.internal.ui.util.Policy;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowPulldownDelegate;

/**
 * PreviewToolbarMenuAction
 */
public class PreviewToolbarMenuAction extends PreviewSupport implements IWorkbenchWindowPulldownDelegate {

	/**
	 * The constructor.
	 */
	public PreviewToolbarMenuAction() {
		super();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.IWorkbenchWindowPulldownDelegate#getMenu(org.eclipse.swt
	 * .widgets.Control)
	 */
	@Override
	public Menu getMenu(Control parent) {
		return getPreviewMenu(parent, true);
	}

	/**
	 * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#init(org.eclipse.ui.
	 *      IWorkbenchWindow)
	 */
	@Override
	public void init(IWorkbenchWindow window) {

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.ui.actions.PreviewAction#dispose()
	 */
	@Override
	public void dispose() {
	}

	@Override
	public void run(IAction action) {
		if (Policy.TRACING_ACTIONS) {
			System.out.println("Preview action >> Run ..."); //$NON-NLS-1$
		}
		preview(TYPE_HTML, true);
	}

	/**
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action
	 *      .IAction, org.eclipse.jface.viewers.ISelection)
	 */
	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		action.setEnabled(isEnable());
	}

	private boolean isEnable() {
		IEditorPart editor = UIUtil.getActiveEditor(true);
		if (editor != null) {
			IContentType[] contentTypes = Platform.getContentTypeManager()
					.findContentTypesFor(editor.getEditorInput().getName());
			for (IContentType type : contentTypes) {
				if (type.getId().equals("org.eclipse.birt.report.designer.ui.editors.reportdesign") //$NON-NLS-1$
						|| type.getId().equals("org.eclipse.birt.report.designer.ui.editors.reporttemplate")) {
					return true;
				}
			}
		}
		return false;
	}
}
