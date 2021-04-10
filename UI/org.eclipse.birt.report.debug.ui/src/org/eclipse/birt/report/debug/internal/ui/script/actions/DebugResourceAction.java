/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.debug.internal.ui.script.actions;

import org.eclipse.birt.report.debug.internal.ui.script.launcher.ScriptLaunchShortcut;
import org.eclipse.core.resources.IFile;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

/**
 * Add the debug report action in the navigate view
 */

public class DebugResourceAction implements IViewActionDelegate {

	private IViewPart navigator;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IViewActionDelegate#init(org.eclipse.ui.IViewPart)
	 */
	public void init(IViewPart view) {
		navigator = view;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run(IAction action) {
		IFile file = getSelectedFile();
		if (file == null) {
			return;
		}

		String fileName = file.getLocation().toOSString();
		ILaunchConfiguration config = ScriptLaunchShortcut.findLaunchConfiguration(fileName,
				ScriptLaunchShortcut.getConfigurationType());
		if (config != null) {
			DebugUITools.launch(config, "debug");//$NON-NLS-1$
		}

	}

	private IFile getSelectedFile() {
		if (navigator != null) {
			IStructuredSelection selection = (IStructuredSelection) navigator.getViewSite().getSelectionProvider()
					.getSelection();
			if (selection.size() == 1 && selection.getFirstElement() instanceof IFile) {
				return (IFile) selection.getFirstElement();
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.
	 * IAction, org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		// do nothing now

	}

}
