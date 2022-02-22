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

import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.core.runtime.content.IContentTypeManager;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowPulldownDelegate2;

/**
 * PreviewCascadingMenuGroup
 */
public class PreviewCascadingMenuGroup extends PreviewSupport implements IWorkbenchWindowPulldownDelegate2 {

	/**
	 * The constructor.
	 */
	public PreviewCascadingMenuGroup() {
		super();
	}

	/**
	 * @see org.eclipse.ui.IWorkbenchWindowPulldownDelegate#getMenu(org.eclipse.swt
	 *      .widgets.Control)
	 */
	@Override
	public Menu getMenu(Control parent) {
		return getPreviewMenu(parent, false);
	}

	/**
	 * @see org.eclipse.jface.action.IMenuCreator#getMenu(org.eclipse.swt.widgets
	 *      .Menu)
	 */
	@Override
	public Menu getMenu(Menu parent) {
		return getPreviewMenu(parent, false);
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
		if (editor == null) {
			return false;
		}
		IEditorInput input = editor.getEditorInput();
		if (input == null) {
			return false;
		}
		String name = input.getName();
		if (name == null) {
			return false;
		}
		IContentTypeManager manager = Platform.getContentTypeManager();
		if (manager != null) {
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

	/**
	 * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#init(org.eclipse.ui.
	 *      IWorkbenchWindow)
	 */
	@Override
	public void init(IWorkbenchWindow window) {
	}

	/**
	 * @see org.eclipse.birt.report.designer.ui.actions.PreviewAction#dispose()
	 */
	@Override
	public void dispose() {
	}

	/**
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	@Override
	public void run(IAction action) {
		// do nothing - this is just a menu
	}
}
