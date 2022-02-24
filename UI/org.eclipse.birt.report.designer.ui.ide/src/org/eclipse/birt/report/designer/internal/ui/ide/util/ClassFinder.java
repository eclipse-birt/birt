/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.designer.internal.ui.ide.util;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.designer.internal.ui.ide.dialog.HandlerClassSelectionDialog;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IRegion;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.SelectionDialog;

/**
 * Find the class name through the parent class name.
 */

public class ClassFinder {
	protected static final Logger logger = Logger.getLogger(ClassFinder.class.getName());

	private static final String TASK_START = Messages.getString("ClassFinder.TaskStart"); //$NON-NLS-1$
	private static final String DIALOG_TITLE = Messages.getString("ClassFinder.DialogTitle"); //$NON-NLS-1$
	private static final String DIALOG_MESSAGE = Messages.getString("ClassFinder.DialogMessage"); //$NON-NLS-1$
	private static final String ERROR_MESSAGE = Messages.getString("ClassFinder.ErrorMessage"); //$NON-NLS-1$
	private static final String ID = "Hnadle class finder"; //$NON-NLS-1$

	/**
	 * Parent class name
	 */
	private String parentClassName = null;

	/**
	 * Find the class
	 * 
	 * @param elements
	 * @param result
	 * @param pm
	 * @throws InterruptedException
	 */
	public void doFindClasses(Object[] elements, Set result, IProgressMonitor pm) throws InterruptedException {
		int nElements = elements.length;
		pm.beginTask(TASK_START, nElements);

		try {
			for (int i = 0; i < nElements; i++) {
				try {
					collectTypes(elements[i], new SubProgressMonitor(pm, 1), result);
				} catch (CoreException e) {
					logger.log(Level.SEVERE, e.getMessage(), e);
				}
				if (pm.isCanceled()) {
					throw new InterruptedException();
				}
			}
		} finally {
			pm.done();
		}
	}

	private void collectTypes(Object element, IProgressMonitor pm, Set result) throws CoreException {
		pm.beginTask(TASK_START, 10);

		if (element instanceof IProject && hasJavaNature((IProject) element)) {
			IJavaElement javaElement = JavaCore.create((IProject) element);
			List testCases = findCLasses(javaElement, new SubProgressMonitor(pm, 7));
			result.addAll(testCases);
			pm.done();
		}
	}

	private List findCLasses(IJavaElement element, IProgressMonitor pm) throws JavaModelException {
		List found = new ArrayList();
		IJavaProject javaProject = element.getJavaProject();

		IType testCaseType = classType(javaProject);
		if (testCaseType == null)
			return found;

		IType[] subtypes = javaProject.newTypeHierarchy(testCaseType, getRegion(element), pm)
				.getAllSubtypes(testCaseType);

		if (subtypes == null)
			throw new JavaModelException(new CoreException(new Status(IStatus.ERROR, ID,
					IJavaLaunchConfigurationConstants.ERR_UNSPECIFIED_MAIN_TYPE, ERROR_MESSAGE, null)));

		for (int i = 0; i < subtypes.length; i++) {
			try {
				if (hasValidModifiers(subtypes[i]))
					found.add(subtypes[i]);
			} catch (JavaModelException e) {
				logger.log(Level.SEVERE, e.getMessage(), e);
			}
		}
		return found;
	}

	private IType classType(IJavaProject javaProject) {
		try {
			if (getParentClassName() == null || getParentClassName().length() == 0) {
				return null;
			}
			return javaProject.findType(getParentClassName());
		} catch (JavaModelException e) {

			return null;
		}
	}

	private IRegion getRegion(IJavaElement element) throws JavaModelException {
		IRegion result = JavaCore.newRegion();
		if (element.getElementType() == IJavaElement.JAVA_PROJECT) {
			// for projects only add the contained source folders
			IPackageFragmentRoot[] roots = ((IJavaProject) element).getPackageFragmentRoots();
			for (int i = 0; i < roots.length; i++) {
				if (!roots[i].isArchive()) {
					result.add(roots[i]);
				}
			}
		} else {
			result.add(element);
		}
		return result;
	}

	private boolean hasValidModifiers(IType type) throws JavaModelException {
		if (Flags.isAbstract(type.getFlags()))
			return false;
		if (!Flags.isPublic(type.getFlags()))
			return false;
		return true;
	}

	/**
	 * Returns true if the given project is accessible and it has a java nature,
	 * otherwise false.
	 * 
	 * @param project IProject
	 * @return boolean
	 */
	private boolean hasJavaNature(IProject project) {
		try {
			return project.hasNature(JavaCore.NATURE_ID);
		} catch (CoreException e) {
			// project does not exist or is not open
		}
		return false;
	}

	/*
	 * Convenience method to get the workspace root.
	 */
	private IWorkspaceRoot getWorkspaceRoot() {
		return ResourcesPlugin.getWorkspace().getRoot();
	}

	/**
	 * @param elements
	 * @return
	 * @throws InvocationTargetException
	 * @throws InterruptedException
	 */
	public IType[] findClasses(final Object[] elements) throws InvocationTargetException, InterruptedException {
		final Set result = new HashSet();

		if (elements.length > 0) {
			IRunnableWithProgress runnable = new IRunnableWithProgress() {

				public void run(IProgressMonitor pm) throws InterruptedException {
					doFindClasses(elements, result, pm);
				}
			};
			PlatformUI.getWorkbench().getProgressService().busyCursorWhile(runnable);
		}
		return (IType[]) result.toArray(new IType[result.size()]);
	}

	/**
	 * @return
	 */
	public String getFinderClassName() {
		IProject[] projects = getWorkspaceRoot().getProjects();
		if (projects == null || projects.length == 0) {
			return null;
		}
		IType[] types = null;
		try {
			// fix for 66922 Wrong radio behaviour when switching
			types = findClasses(projects);
			// types = findTests( new Object[]{projects[0]} );
		} catch (InterruptedException e) {
			return null;
		} catch (InvocationTargetException e) {
			return null;
		} finally {
		}

		Shell shell = UIUtil.getDefaultShell();
		SelectionDialog dialog = new HandlerClassSelectionDialog(shell, types);
		dialog.setTitle(DIALOG_TITLE);
		dialog.setMessage(DIALOG_MESSAGE);

		if (dialog.open() == Window.CANCEL) {
			return null;
		}

		Object[] results = dialog.getResult();
		if ((results == null) || (results.length < 1)) {
			return null;
		}
		IType type = (IType) results[0];

		if (type != null) {
			return type.getFullyQualifiedName('.');
		}
		return null;
	}

	/**
	 * @return
	 */
	public String getParentClassName() {
		return parentClassName;
	}

	/**
	 * @param parentClassName
	 */
	public void setParentClassName(String parentClassName) {
		this.parentClassName = parentClassName;
	}

}
