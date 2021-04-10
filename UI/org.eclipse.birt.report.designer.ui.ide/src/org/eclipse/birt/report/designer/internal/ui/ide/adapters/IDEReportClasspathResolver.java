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

package org.eclipse.birt.report.designer.internal.ui.ide.adapters;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.birt.report.designer.ui.IReportClasspathResolver;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.preferences.PreferenceFactory;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

/**
 * IDEReportClasspathResolver
 */
public class IDEReportClasspathResolver implements IReportClasspathResolver {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.ui.IReportClasspathProvider#resolveClasspath
	 * (java.lang.Object)
	 */
	public String[] resolveClasspath(Object adaptable) {
		IProject project = adaptProject(adaptable);

//		IWorkspace space = ResourcesPlugin.getWorkspace( );
//		IWorkspaceRoot root = space.getRoot( );
		String value = PreferenceFactory.getInstance().getPreferences(ReportPlugin.getDefault(), project)
				.getString(ReportPlugin.CLASSPATH_PREFERENCE);

		List<IClasspathEntry> list = IDEClassPathBlock.getEntries(value);
		List<String> strs = getAllClassPathFromEntries(list);

		try {
			if (project == null || !project.hasNature(JavaCore.NATURE_ID)) {
				return strs.toArray(new String[strs.size()]);
			}
		} catch (CoreException e) {
			return strs.toArray(new String[strs.size()]);
		}

		// Set<String> paths = getProjectClasspath( project );

		List<String> temp = getProjectClasspath(project, true, true);
		for (int i = 0; i < temp.size(); i++) {
			addToList(strs, temp.get(i));
		}
		return strs.toArray(new String[strs.size()]);
	}

	private List<String> getAllClassPathFromEntries(List<IClasspathEntry> list) {
		List<String> retValue = new ArrayList();
		IWorkspace space = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = space.getRoot();

		for (int i = 0; i < list.size(); i++) {
			IClasspathEntry curr = list.get(i);
			boolean inWorkSpace = true;

			if (space == null || space.getRoot() == null) {
				inWorkSpace = false;
			}

			IPath path = curr.getPath();
			if (curr.getEntryKind() == IClasspathEntry.CPE_VARIABLE) {
				path = JavaCore.getClasspathVariable(path.segment(0));
			} else {
				path = JavaCore.getResolvedClasspathEntry(curr).getPath();
			}

			if (curr.getEntryKind() == IClasspathEntry.CPE_PROJECT) {
				if (root.findMember(path) instanceof IProject) {
					List<String> strs = getProjectClasspath((IProject) root.findMember(path), false, true);
					for (int j = 0; j < strs.size(); j++) {
						addToList(retValue, strs.get(j));
					}
				}
			} else {
				if (root.findMember(path) == null) {
					inWorkSpace = false;
				}

				if (inWorkSpace) {
					String absPath = getFullPath(path, root.findMember(path).getProject());

					// retValue.add( absPath );
					addToList(retValue, absPath);
				} else {
					// retValue.add( path.toFile( ).getAbsolutePath( ));
					addToList(retValue, path.toFile().getAbsolutePath());
				}
			}

			// strs.add( JavaCore.getResolvedClasspathEntry( entry ).getPath( ).toFile(
			// ).getAbsolutePath( ) );
		}
		return retValue;
	}

	private void addToList(List<String> list, String str) {
		if (!list.contains(str)) {
			list.add(str);
		}
	}

	private IProject adaptProject(Object adaptable) {
		if (adaptable instanceof IProject) {
			return (IProject) adaptable;
		} else if (adaptable instanceof IResource) {
			return ((IResource) adaptable).getProject();
		} else if (adaptable instanceof URI) {
			// this should be the absolute report file path
			IFile[] files = ResourcesPlugin.getWorkspace().getRoot().findFilesForLocationURI((URI) adaptable);

			if (files != null && files.length > 0) {
				return files[0].getProject();
			}
		} else if (adaptable instanceof IPath) {
			// this should be the absolute report file path
			IFile[] files = ResourcesPlugin.getWorkspace().getRoot().findFilesForLocation((IPath) adaptable);

			if (files != null && files.length > 0) {
				return files[0].getProject();
			}
		} else if (adaptable instanceof String) {
			// this should be the absolute report file path
			IFile[] files = ResourcesPlugin.getWorkspace().getRoot()
					.findFilesForLocation(Path.fromOSString((String) adaptable));

			if (files != null && files.length > 0) {
				return files[0].getProject();
			}
		}

		return null;
	}

	private List<String> getProjectClasspath(IProject project, boolean needExported, boolean needDepend) {

		List<String> retValue = new ArrayList<String>();
		if (project == null) {
			return Collections.emptyList();
		}

		if (needDepend) {
			List<String> paths = getProjectDependentClasspath(project, needExported);

			for (int j = 0; j < paths.size(); j++) {
				addToList(retValue, paths.get(j));
			}
		}

		String url = getProjectOutputClassPath(project);
		if (url != null) {
			// retValue.add( url );
			addToList(retValue, url);
		}

		return retValue;
	}

	private String getProjectOutputClassPath(IProject project) {
		if (!hasJavaNature(project)) {
			return null;
		}

		IJavaProject fCurrJProject = JavaCore.create(project);
		IPath path = null;
		boolean projectExists = (project.exists() && project.getFile(".classpath").exists()); //$NON-NLS-1$
		if (projectExists) {
			if (path == null) {
				path = fCurrJProject.readOutputLocation();
				// String curPath = path.toOSString( );
				// String directPath = project.getLocation( ).toOSString( );
				// int index = directPath.lastIndexOf( File.separator );
				if (path == null) {
					return null;
				}
				String absPath = getFullPath(path, project);

				return absPath;
			}
		}

		return null;
	}

	private List<String> getProjectDependentClasspath(IProject project, boolean needExported) {
		if (!hasJavaNature(project)) {
			return Collections.emptyList();
		}

		List<String> retValue = new ArrayList<String>();

		IJavaProject fCurrJProject = JavaCore.create(project);
		IClasspathEntry[] classpathEntries = null;

		boolean projectExists = (project.exists() && project.getFile(".classpath").exists()); //$NON-NLS-1$

		if (projectExists) {
			if (classpathEntries == null) {
				classpathEntries = fCurrJProject.readRawClasspath();
			}
		}

		if (classpathEntries != null) {
			retValue = resolveClasspathEntries(classpathEntries, needExported, fCurrJProject);
		}

		return retValue;
	}

	private List<String> resolveClasspathEntries(IClasspathEntry[] classpathEntries, boolean needExported,
			IJavaProject project) {
		ArrayList<String> newClassPath = new ArrayList<String>();
		IWorkspace space = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = space.getRoot();
		for (int i = 0; i < classpathEntries.length; i++) {
			IClasspathEntry curr = classpathEntries[i];
			if (!needExported && !curr.isExported() && curr.getEntryKind() != IClasspathEntry.CPE_VARIABLE) {
				continue;
			}
			IPath path = curr.getPath();
//			if (curr.getEntryKind( ) == IClasspathEntry.CPE_VARIABLE)
//			{
//				path = JavaCore.getClasspathVariable( path.segment( 0 ) );
//			}
//			else
//			{
			path = JavaCore.getResolvedClasspathEntry(curr).getPath();
//			}

			if (project != null && curr.getEntryKind() == IClasspathEntry.CPE_CONTAINER) {
				try {
					IClasspathContainer contianer = JavaCore.getClasspathContainer(path, project);
					if (contianer != null && contianer.getKind() == IClasspathContainer.K_APPLICATION) {
						IClasspathEntry[] entrys = contianer.getClasspathEntries();
						List<String> list = resolveClasspathEntries(entrys, needExported, project);
						for (int j = 0; j < list.size(); j++) {
							addToList(newClassPath, list.get(j));
						}
					}
				} catch (JavaModelException e) {
					// do nothing
				}
				continue;
			}
			if (curr.getEntryKind() == IClasspathEntry.CPE_SOURCE) {
				path = curr.getOutputLocation();
			}
			if (path == null) {
				continue;
			}
			if (curr.getEntryKind() == IClasspathEntry.CPE_PROJECT) {
				if (root.findMember(path) instanceof IProject) {
					List<String> strs = getProjectClasspath((IProject) root.findMember(path), false, false);
					for (int j = 0; j < strs.size(); j++) {
						addToList(newClassPath, strs.get(j));
					}
				}
			} else if (curr.getEntryKind() == IClasspathEntry.CPE_LIBRARY
					|| curr.getEntryKind() == IClasspathEntry.CPE_VARIABLE
					|| curr.getEntryKind() == IClasspathEntry.CPE_SOURCE) {

				boolean inWorkSpace = true;
				if (space == null || space.getRoot() == null) {
					inWorkSpace = false;
				}

				if (root.findMember(path) == null) {
					inWorkSpace = false;
				}

				if (inWorkSpace) {
					String absPath = getFullPath(path, root.findMember(path).getProject());

					// URL url = new URL( "file:///" + absPath );//$NON-NLS-1$//file:/
					// newClassPath.add( url.getPath( ) );
					newClassPath.add(absPath);
				} else {
//						newClassPath.add( curr.getPath( )
//								.toFile( )
//								.toURI( )
//								.toURL( ) );
					newClassPath.add(path.toFile().getAbsolutePath());
				}

			}

		}
		return newClassPath;
	}

	private String getFullPath(IPath path, IProject project) {
		// String curPath = path.toOSString( );
		// String directPath = project.getLocation( ).toOSString( );
		// int index = directPath.lastIndexOf( File.separator );
		// String absPath = directPath.substring( 0, index ) + curPath;
		// return absPath;

		String directPath;
		try {

			directPath = project.getDescription().getLocationURI().toURL().getPath();
		} catch (Exception e) {
			directPath = project.getLocation().toOSString();
		}
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		if (root.findMember(path) == project) {
			return directPath;
		}
		String curPath = path.toOSString();
		int index = curPath.substring(1).indexOf(File.separator);
		String absPath = directPath + curPath.substring(index + 1);
		return absPath;
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
}
