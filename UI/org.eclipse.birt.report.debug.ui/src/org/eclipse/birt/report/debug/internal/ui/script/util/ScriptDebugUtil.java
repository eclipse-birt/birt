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

package org.eclipse.birt.report.debug.internal.ui.script.util;

import java.io.File;

import org.eclipse.birt.report.debug.internal.ui.script.editor.DebugJsEditor;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.variables.IStringVariableManager;
import org.eclipse.core.variables.VariablesPlugin;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Region;
import org.eclipse.pde.core.plugin.IPluginLibrary;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.pde.core.plugin.PluginRegistry;
import org.eclipse.pde.core.plugin.TargetPlatform;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/**
 * ScriptDebugUtil
 */
public class ScriptDebugUtil {

	private static final char fgSeparator = File.separatorChar;
	private static final String[] fgCandidateJavaFiles = { "javaw", "javaw.exe", "java", "java.exe", "j9w", "j9w.exe", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
			"j9", "j9.exe" }; //$NON-NLS-1$ //$NON-NLS-2$
	private static final String[] fgCandidateJavaLocations = { "bin" + fgSeparator, //$NON-NLS-1$
			"jre" + fgSeparator + "bin" + fgSeparator }; //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * Gets the default work space.
	 *
	 * @return
	 */
	public static IResource getDefaultResource() {
		return ResourcesPlugin.getWorkspace().getRoot();
	}

	/**
	 * Find java exe file.
	 *
	 * @param vmInstallLocation
	 * @return
	 */
	public static File findJavaExecutable(File vmInstallLocation) {
		for (int i = 0; i < fgCandidateJavaFiles.length; i++) {
			for (int j = 0; j < fgCandidateJavaLocations.length; j++) {
				File javaFile = new File(vmInstallLocation, fgCandidateJavaLocations[j] + fgCandidateJavaFiles[i]);
				if (javaFile.isFile()) {
					return javaFile;
				}
			}
		}
		return null;
	}

	/**
	 * Get the java project through the name.
	 *
	 * @param projectName
	 * @return
	 * @throws CoreException
	 */
	public static IJavaProject getJavaProject(String projectName) throws CoreException {

		if ((projectName == null) || (projectName.trim().length() < 1)) {
			return null;
		}
		IJavaProject javaProject = getJavaModel().getJavaProject(projectName);

		return javaProject;
	}

	/**
	 * Convenience method to get the java model.
	 */
	private static IJavaModel getJavaModel() {
		return JavaCore.create(ResourcesPlugin.getWorkspace().getRoot());
	}

	/**
	 *
	 * @param source
	 * @return
	 */
	public static String expandLibraryName(String source) {
		if (source == null || source.length() == 0) {
			return ""; //$NON-NLS-1$
		}
		if (source.indexOf("$ws$") != -1) { // $NON-NLS-1$
			source = source.replaceAll("\\$ws\\$", //$NON-NLS-1$
					"ws" + IPath.SEPARATOR + TargetPlatform.getWS()); //$NON-NLS-1$
		}
		if (source.indexOf("$os$") != -1) { // $NON-NLS-1$
			source = source.replaceAll("\\$os\\$", //$NON-NLS-1$
					"os" + IPath.SEPARATOR + TargetPlatform.getOS()); //$NON-NLS-1$
		}
		if (source.indexOf("$nl$") != -1) { // $NON-NLS-1$
			source = source.replaceAll("\\$nl\\$", //$NON-NLS-1$
					"nl" + IPath.SEPARATOR + TargetPlatform.getNL()); //$NON-NLS-1$
		}
		if (source.indexOf("$arch$") != -1) { // $NON-NLS-1$
			source = source.replaceAll("\\$arch\\$", //$NON-NLS-1$
					"arch" + IPath.SEPARATOR + TargetPlatform.getOSArch()); //$NON-NLS-1$
		}
		return source;
	}

	/**
	 * @param model
	 * @param libraryName
	 * @return
	 */
	public static IPath getPath(IPluginModelBase model, String libraryName) {
		IResource resource = model.getUnderlyingResource();
		if (resource != null) {
			IResource jarFile = resource.getProject().findMember(libraryName);
			return (jarFile != null) ? jarFile.getFullPath() : null;
		}
		File file = new File(model.getInstallLocation(), libraryName);
		return file.exists() ? new Path(file.getAbsolutePath()) : null;
	}

	/**
	 * @param id
	 * @return
	 */
	public static String getPlugInFile(String id) {

		IPluginModelBase model = PluginRegistry.findModel(id);
		if (model == null) {
			return null;
		}
		File file = new File(model.getInstallLocation());
		if (file.isFile()) {
			return file.getAbsolutePath();
		} else {
			IPluginLibrary[] libraries = model.getPluginBase().getLibraries();
			for (int i = 0; i < libraries.length; i++) {
				if (IPluginLibrary.RESOURCE.equals(libraries[i].getType())) {
					continue;
				}
				model = (IPluginModelBase) libraries[i].getModel();
				String name = libraries[i].getName();
				String expandedName = expandLibraryName(name);
				IPath path = getPath(model, expandedName);

				if (path != null && !path.toFile().isDirectory()) {
					return path.toFile().getAbsolutePath();
				}
			}
		}

		return null;
	}

	/**
	 * @param project
	 * @return
	 */
	public static String getOutputFolder(IJavaProject project) {
		if (project == null) {
			return null;
		}
		IPath path = project.readOutputLocation();
		String curPath = path.toOSString();
		String directPath = project.getProject().getLocation().toOSString();
		int index = directPath.lastIndexOf(File.separator);
		String absPath = directPath.substring(0, index) + curPath;

		return absPath;
	}

	/**
	 * @return
	 */
	public static DebugJsEditor getActiveJsEditor() {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();

		if (window != null) {

			IWorkbenchPage pg = window.getActivePage();

			if (pg != null) {
				IEditorPart editor = pg.getActiveEditor();

				if (editor != null) {
					if (editor instanceof DebugJsEditor) {
						return (DebugJsEditor) editor;
					}
				}
			}
		}
		return null;
	}

	/**
	 * @param text
	 * @return
	 * @throws CoreException
	 */
	public static String getSubstitutedString(String text) throws CoreException {
		if (text == null) {
			return ""; //$NON-NLS-1$
		}
		IStringVariableManager mgr = VariablesPlugin.getDefault().getStringVariableManager();
		return mgr.performStringSubstitution(text);
	}

	/**
	 * Returns the IRegion containing the java identifier ("word") enclosing the
	 * specified offset or <code>null</code> if the document or offset is invalid.
	 * Checks characters before and after the offset to see if they are allowed java
	 * identifier characters until a separator character (period, space, etc) is
	 * found.
	 *
	 * @param document The document to search
	 * @param offset   The offset to start looking for the word
	 * @return IRegion containing the word or <code>null</code>
	 */
	public static IRegion findWord(IDocument document, int offset) {

		if (document == null) {
			return null;
		}

		int start = -2;
		int end = -1;

		try {

			int pos = offset;
			char c;

			while (pos >= 0) {
				c = document.getChar(pos);
				if (!Character.isJavaIdentifierPart(c)) {
					break;
				}
				--pos;
			}

			start = pos;

			pos = offset;
			int length = document.getLength();

			while (pos < length) {
				c = document.getChar(pos);
				if (!Character.isJavaIdentifierPart(c)) {
					break;
				}
				++pos;
			}

			end = pos;

		} catch (BadLocationException x) {
		}

		if (start >= -1 && end > -1) {
			if (start == offset && end == offset) {
				return new Region(offset, 0);
			} else if (start == offset) {
				return new Region(start, end - start);
			} else {
				return new Region(start + 1, end - start - 1);
			}
		}

		return null;
	}

}
