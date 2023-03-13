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

package org.eclipse.birt.report.debug.internal.ui.launcher;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.debug.internal.ui.launcher.util.WorkspaceClassPathFinder;
import org.eclipse.birt.report.designer.ui.editors.ReportEditorProxy;
import org.eclipse.birt.report.designer.ui.preview.editors.ReportPreviewFormPage;
import org.eclipse.birt.report.viewer.utilities.WorkspaceClasspathManager;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.ide.IDE;

import com.ibm.icu.util.StringTokenizer;

/**
 * Copy the seletion of the project in the debug lauch.The key name is
 * user.projectname.
 *
 * @deprecated
 */
@Deprecated
public class DebugStartupClass implements IStartup {

	private static final String WORKSPACE_CLASSPATH_KEY = "workspace.projectclasspath"; //$NON-NLS-1$

	private static Logger logger = Logger.getLogger(DebugStartupClass.class.getName());

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.IStartup#earlyStartup()
	 */
	@Override
	public void earlyStartup() {
		WorkspaceClassPathFinder finder = new WorkspaceClassPathFinder();
		// Register a classpath finder class to the viewer
		WorkspaceClasspathManager.registerClassPathFinder(finder);

		// Set the classpath property (used in Java scripting)
		String projectClassPaths = finder.getClassPath();

		// HashTable doesn't accept null value
		if (projectClassPaths == null) {
			projectClassPaths = ""; //$NON-NLS-1$
		}
		System.setProperty(WORKSPACE_CLASSPATH_KEY, projectClassPaths);

		String value = System.getProperty("user.projectname"); //$NON-NLS-1$
		if (value == null || value.length() == 0) {
			return;
		}
		StringTokenizer token = new StringTokenizer(value, ";"); //$NON-NLS-1$
		while (token.hasMoreTokens()) {
			String str = token.nextToken();
			try {
				// DebugUtil.importProject( str );
			} catch (Exception e1) {
				// do nothing, the project has inport to the workspace1
				logger.log(Level.SEVERE, e1.getMessage(), e1);
			}
		}
		Display.getDefault().asyncExec(new Runnable() {

			@Override
			public void run() {
				int openCount = 0;
				try {
					String value = System.getProperty("user.openfiles"); //$NON-NLS-1$
					if (value == null || value.length() == 0) {
						return;
					}
					StringTokenizer token = new StringTokenizer(value, ";"); //$NON-NLS-1$
					while (token.hasMoreTokens()) {
						String str = token.nextToken();
						final IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(str));

						IWorkbench workbench = PlatformUI.getWorkbench();
						IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
						IWorkbenchPage page = window.getActivePage();
						IDE.openEditor(page, file, true);
						openCount++;
					}
				} catch (PartInitException e) {
					logger.log(Level.SEVERE, e.getMessage(), e);
				}
				if (openCount == 1) {
					FormEditor editor = getActiveReportEditor();
					editor.setActivePage(ReportPreviewFormPage.ID);
				}

			}

		});

	}

	/**
	 * Returns the current active report editor in current active page or current
	 * active workbench.
	 *
	 * @return
	 */
	public static FormEditor getActiveReportEditor() {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();

		if (window != null) {

			IWorkbenchPage pg = window.getActivePage();

			if (pg != null) {
				IEditorPart editor = pg.getActiveEditor();

				if (editor != null) {
					if (editor instanceof ReportEditorProxy) {
						IEditorPart part = ((ReportEditorProxy) editor).getEditorPart();
						if (part instanceof FormEditor) {
							return (FormEditor) part;
						}
					} else if (editor instanceof FormEditor) {
						return (FormEditor) editor;
					}
				}

			}
		}
		return null;

	}
}
