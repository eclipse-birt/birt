package org.eclipse.birt.report.debug.internal.ui.script.actions;

import org.eclipse.birt.report.debug.internal.ui.script.launcher.ScriptLaunchShortcut;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.ui.editors.MultiPageReportEditor;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.forms.editor.FormEditor;

/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

/**
 * 
 */

public class DebugScriptAction implements IWorkbenchWindowActionDelegate {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#dispose()
	 */
	public void dispose() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#init(org.eclipse.ui.
	 * IWorkbenchWindow)
	 */
	public void init(IWorkbenchWindow window) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run(IAction action) {
		ModuleHandle handle = null;

		if (handle == null) {
			FormEditor editor = UIUtil.getActiveReportEditor(false);
			if (editor instanceof MultiPageReportEditor) {
				handle = ((MultiPageReportEditor) editor).getModel();
			}
		}
		if (handle != null) {
			String fileName = handle.getFileName();

			ILaunchConfiguration config = ScriptLaunchShortcut.findLaunchConfiguration(fileName,
					ScriptLaunchShortcut.getConfigurationType());
			if (config != null) {
				DebugUITools.launch(config, "debug");//$NON-NLS-1$
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.
	 * IAction, org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {

	}

}
