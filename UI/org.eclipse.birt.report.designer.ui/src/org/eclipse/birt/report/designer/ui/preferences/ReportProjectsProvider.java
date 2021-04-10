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

package org.eclipse.birt.report.designer.ui.preferences;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;

public class ReportProjectsProvider extends LabelProvider implements ITreeContentProvider {

	public Image getImage(Object element) {
		return new ReportImageDescriptor(ReportPlugin.getDefault().getWorkbench().getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_OBJ_PROJECT)).createImage();
	}

	/**
	 * The <code>LabelProvider</code> implementation of this
	 * <code>ILabelProvider</code> method returns the element's
	 * <code>toString</code> string. Subclasses may override.
	 */
	public String getText(Object element) {
		if (element instanceof IProject)
			return ((IProject) element).getName();
		else
			return super.getText(element);
	}

	public Object[] getChildren(Object parentElement) {
		return new Object[0];
	}

	public Object getParent(Object element) {
		if (element instanceof IProject)
			return ((IProject) element).getWorkspace();
		else
			return null;
	}

	public boolean hasChildren(Object element) {
		return false;
	}

	public Object[] getElements(Object inputElement) {
		List projectList = new ArrayList();
		if (inputElement instanceof IWorkspaceRoot) {
			IProject[] projects = ((IWorkspaceRoot) inputElement).getProjects();
			for (int i = 0; i < projects.length; i++) {
				try {
					if (projects[i].getNature(ReportPlugin.NATURE_ID) != null) // $NON-NLS-1$
						projectList.add(projects[i]);
				} catch (CoreException e) {
					// TODO Don't do anything.
				}
			}
		}
		return projectList.toArray();
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub

	}

}
