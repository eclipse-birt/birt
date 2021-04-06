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

package org.eclipse.birt.report.designer.ui.ide.navigator;

import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

/**
 * Support the add the reprot project nature to the any project.
 */

public class SetupReportProjectAction implements IViewActionDelegate {

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
		Object[] targets = getTargetObjects();
		if (targets == null) {
			return;
		}

		for (int i = 0; i < targets.length; i++) {
			if (!(targets[i] instanceof IProject)) {
				continue;
			}

			IProject project = (IProject) targets[i];
			try {
				if (project.hasNature(ReportPlugin.NATURE_ID)) {
					continue;
				}
			} catch (CoreException e) {
				continue;
			}

			IProjectDescription description;
			try {
				description = project.getDescription();

				String[] prevNatures = description.getNatureIds();
				String[] newNatures = new String[prevNatures.length + 1];
				System.arraycopy(prevNatures, 0, newNatures, 0, prevNatures.length);

				newNatures[prevNatures.length] = ReportPlugin.NATURE_ID;

				description.setNatureIds(newNatures);
				project.setDescription(description, new NullProgressMonitor());
			} catch (CoreException e) {
				ExceptionUtil.handle(e);
			}

		}
	}

	private Object[] getTargetObjects() {
		if (navigator != null) {
			IStructuredSelection selection = (IStructuredSelection) navigator.getViewSite().getSelectionProvider()
					.getSelection();
			return selection.toArray();
		}

		return null;
	}

	public void selectionChanged(IAction action, ISelection selection) {
		// do nothing now
		// Note maybe get the selction frow here is a better way
	}

}
