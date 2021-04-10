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

package org.eclipse.birt.report.designer.testutil;

import junit.framework.TestCase;

import org.eclipse.birt.report.designer.tests.ITestConstants;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

/**
 * Base class of BIRT GUI Features test
 */
public abstract class BirtUITestCase extends TestCase implements ITestConstants {

	protected IWorkbench tWorkbench;

	protected IWorkbenchWindow tWindow;

	protected IWorkbenchPage tPage;

	protected IPerspectiveDescriptor tPerspectiveDescriptor;

	protected IEditorPart tEditor = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		tWorkbench = PlatformUI.getWorkbench();
		tWindow = tWorkbench.getActiveWorkbenchWindow();
		tPage = tWindow.getActivePage();
		tPerspectiveDescriptor = tWorkbench.getPerspectiveRegistry().findPerspectiveWithId(PERSPECTIVE_ID);
	}

	/**
	 * Switch to the Report Designer Perspective
	 */

	protected void showPerspective() throws Exception {
		tWorkbench.showPerspective(PERSPECTIVE_ID, tWindow);
	}

	/**
	 * Opens the ReportEditor
	 * 
	 * @return the Report Editor
	 */

	protected IEditorPart openEditor() throws Exception {
		if (tEditor == null) {
			IProject p = FileUtil.createProject(TEST_PROJECT_NAME);

			IFile f = FileUtil.createFile(TEST_DESIGN_FILE, p);
			tEditor = tPage.openEditor(new FileEditorInput(f), EDITOR_ID);
		}
		return tEditor;
	}

	/**
	 * Saves the opened editor
	 */
	protected void saveEditor() {
		if (tEditor != null) {
			tEditor.doSave(null);
		}
	}

	/**
	 * Closes the opened editor without saving changes
	 */
	protected void closeEditor() {
		if (tEditor != null) {
			tPage.closeEditor(tEditor, false);
			tEditor = null;
		}
	}

	/**
	 * Gets the ViewPart with the specified id
	 * 
	 * @param id the id of view part
	 * 
	 * @return Returns the view part, or null if not found
	 */

	protected IViewPart getView(String id) {
		IViewReference[] v = tPage.getViewReferences();
		int i;
		for (i = 0; i < v.length; i++) {
			if (v[i].getId().equals(id))
				return (IViewPart) v[i].getPart(true);
		}
		return null;
	}
}
