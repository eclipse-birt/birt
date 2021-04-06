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

package org.eclipse.birt.report.debug.internal.ui.script;

import org.eclipse.birt.report.debug.internal.script.model.ScriptDebugElement;
import org.eclipse.birt.report.debug.ui.DebugUI;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.debug.ui.contexts.DebugContextEvent;
import org.eclipse.debug.ui.contexts.IDebugContextListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWindowListener;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/**
 * ScriptEvaluationContextManager
 */
public class ScriptEvaluationContextManager implements IWindowListener, IDebugContextListener {

	private static final String KEY = DebugUI.getUniqueIdentifier() + ".debuggerActive"; //$NON-NLS-1$
	private static ScriptEvaluationContextManager fgManager;
	// TODO get the stack from the fActiveWindow
	private IWorkbenchWindow fActiveWindow;

	public ScriptEvaluationContextManager() {
		DebugUITools.getDebugContextManager().addDebugContextListener(this);
	}

	/**
	 * Start
	 */
	public static void startup() {
		Runnable r = new Runnable() {

			public void run() {
				if (fgManager == null) {
					fgManager = new ScriptEvaluationContextManager();
					IWorkbench workbench = PlatformUI.getWorkbench();
					IWorkbenchWindow[] windows = workbench.getWorkbenchWindows();
					for (int i = 0; i < windows.length; i++) {
						fgManager.windowOpened(windows[i]);
					}
					workbench.addWindowListener(fgManager);
					fgManager.fActiveWindow = workbench.getActiveWorkbenchWindow();
				}
			}
		};
		DebugUI.getStandardDisplay().asyncExec(r);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IWindowListener#windowOpened(org.eclipse.ui.IWorkbenchWindow)
	 */
	public void windowOpened(IWorkbenchWindow window) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWindowListener#windowActivated(org.eclipse.ui.
	 * IWorkbenchWindow)
	 */
	public void windowActivated(IWorkbenchWindow window) {
		fActiveWindow = window;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IWindowListener#windowClosed(org.eclipse.ui.IWorkbenchWindow)
	 */
	public void windowClosed(IWorkbenchWindow window) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWindowListener#windowDeactivated(org.eclipse.ui.
	 * IWorkbenchWindow)
	 */
	public void windowDeactivated(IWorkbenchWindow window) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.debug.ui.contexts.IDebugContextListener#debugContextChanged(org.
	 * eclipse.debug.ui.contexts.DebugContextEvent)
	 */
	public void debugContextChanged(DebugContextEvent event) {
		ISelection selection = event.getContext();
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection ss = (IStructuredSelection) selection;
			if (ss.size() == 1) {
				Object element = ss.getFirstElement();
				if (element instanceof IAdaptable) {
					ScriptDebugElement frame = (ScriptDebugElement) ((IAdaptable) element)
							.getAdapter(ScriptDebugElement.class);

					if (frame != null) {
						System.setProperty(KEY, "true"); //$NON-NLS-1$
						return;
					}
				}
			}
		}
		System.setProperty(KEY, "false"); //$NON-NLS-1$
	}
}
