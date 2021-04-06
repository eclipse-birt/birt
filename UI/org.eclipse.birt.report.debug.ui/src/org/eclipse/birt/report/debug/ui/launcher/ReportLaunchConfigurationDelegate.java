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

package org.eclipse.birt.report.debug.ui.launcher;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.debug.internal.ui.launcher.IReportLauncherSettings;
import org.eclipse.birt.report.debug.internal.ui.launcher.util.WorkspaceClassPathFinder;
import org.eclipse.birt.report.viewer.utilities.WebViewer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.sourcelookup.ISourceContainer;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.launching.JavaSourceLookupDirector;
import org.eclipse.jdt.launching.sourcelookup.containers.JavaProjectSourceContainer;
import org.eclipse.pde.ui.launcher.EclipseApplicationLaunchConfiguration;

/**
 * ReportLaunchConfigurationDelegate
 * 
 * @deprecated
 */
public class ReportLaunchConfigurationDelegate extends EclipseApplicationLaunchConfiguration
		implements IReportLauncherSettings {

	/**
	 * It is roperty key.
	 */
	private static final String PROJECT_NAMES_KEY = "user.projectname"; //$NON-NLS-1$

	private static final String PROJECT_CLASSPATH_KEY = "user.projectclasspath"; //$NON-NLS-1$

	private static final String PROJECT_OPENFILES_KEY = "user.openfiles"; //$NON-NLS-1$

	private static WorkspaceClassPathFinder finder = new WorkspaceClassPathFinder();

	public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor)
			throws CoreException {
		configLaunch(launch, configuration);
		super.launch(configuration, mode, launch, monitor);
	}

	/**
	 * @param launch
	 */
	private void configLaunch(ILaunch launch, ILaunchConfiguration configuration) {
		if (launch.getSourceLocator() instanceof JavaSourceLookupDirector) {
			JavaSourceLookupDirector director = (JavaSourceLookupDirector) launch.getSourceLocator();
			ISourceContainer[] contains = director.getSourceContainers();
			List list = new ArrayList();

			if (contains != null && contains.length != 0) {
				for (int i = 0; i < contains.length; i++) {
					list.add(contains[i]);
				}
			}

			try {
				List sourcePaths = getAllProjectSourcePaths(configuration.getAttribute(IMPORTPROJECTNAMES, "")); //$NON-NLS-1$
				for (int i = 0; i < sourcePaths.size(); i++) {
					// String source = ( String ) sourcePaths.get( i );
					// ISourceContainer temp = new DirectorySourceContainer(
					// new Path( source ), true );
					// list.add( temp );

					IJavaProject source = (IJavaProject) sourcePaths.get(i);
					ISourceContainer temp = new JavaProjectSourceContainer(source);
					list.add(i, temp);
				}
			} catch (CoreException e) {

			}

			ISourceContainer[] retValue = new ISourceContainer[list.size()];
			retValue = (ISourceContainer[]) list.toArray(retValue);
			director.setSourceContainers(retValue);
		}
	}

	/**
	 * @param path no use now
	 * @return
	 */
	private List getAllProjectSourcePaths(String path) {
		List retValue = new ArrayList();
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		if (projects == null || projects.length == 0) {
			return retValue;
		}
		for (int i = 0; i < projects.length; i++) {
			IProject project = projects[i];
			if (project != null && hasJavaNature(project)) {
				retValue.add(JavaCore.create(project));
			}
		}
		return retValue;
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
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.pde.ui.launcher.AbstractPDELaunchConfiguration#getVMArguments(org
	 * .eclipse.debug.core.ILaunchConfiguration)
	 */
	public String[] getVMArguments(ILaunchConfiguration configuration) throws CoreException {
		String[] temp = super.getVMArguments(configuration);
		String[] othersArguments = getOthersVMArguments(configuration);

		String[] rt;

		if (temp == null || temp.length == 0) {
			rt = new String[othersArguments.length];
			System.arraycopy(othersArguments, 0, rt, 0, rt.length);
		} else {
			rt = new String[othersArguments.length + temp.length];

			System.arraycopy(temp, 0, rt, 0, temp.length);
			System.arraycopy(othersArguments, 0, rt, temp.length, othersArguments.length);
		}

		return rt;
	}

	private String[] getOthersVMArguments(ILaunchConfiguration configuration) throws CoreException {
		// String temp[] = ( new ExecutionArguments( configuration.getAttribute(
		// "vmargs", "" ), "" ) ).getVMArgumentsArray( ); //$NON-NLS-1$
		// //$NON-NLS-2$ //$NON-NLS-3$
		String path = configuration.getAttribute(IMPORTPROJECT, ""); //$NON-NLS-1$

		String append = "-D" + PROJECT_NAMES_KEY + "=" + path; //$NON-NLS-1$ //$NON-NLS-2$

		String projectClassPaths = finder.getClassPath();

		String classPath = ""; //$NON-NLS-1$
		// String sourcePath = "";
		if (projectClassPaths != null && projectClassPaths.length() != 0) {
			classPath = "-D" + PROJECT_CLASSPATH_KEY + "=" + projectClassPaths; //$NON-NLS-1$ //$NON-NLS-2$
		}

		String openFiles = "-D" //$NON-NLS-1$
				+ PROJECT_OPENFILES_KEY + "=" //$NON-NLS-1$
				+ configuration.getAttribute(OPENFILENAMES, ""); //$NON-NLS-1$

		String mode = "-D" + WebViewer.REPORT_DEBUT_MODE + "=" + "TRUE"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		return new String[] { append, classPath, openFiles, mode };
	}
}
