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

package org.eclipse.birt.report.designer.testutil;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.birt.report.designer.tests.ITestConstants;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;

/**
 *
 * Utility for creating test project and file Based on Eclipse UI Test Utility
 *
 *
 *
 */
public class FileUtil {

	/**
	 * Creates a new project.
	 *
	 * @param name the project name
	 */
	public static IProject createProject(String name) throws CoreException {
		IWorkspace ws = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = ws.getRoot();
		IProject proj = root.getProject(name);
		if (!proj.exists()) {
			proj.create(null);
		}
		if (!proj.isOpen()) {
			proj.open(null);
		}
		return proj;
	}

	/**
	 * Deletes a project.
	 *
	 * @param proj the project
	 */
	public static void deleteProject(IProject proj) throws CoreException {
		proj.delete(true, null);
	}

	/**
	 * Creates a new file in a project.
	 *
	 * @param name the new file name
	 * @param proj the existing project
	 * @return the new file
	 * @throws CoreException,FileNotFoundException
	 */
	public static IFile createFile(String name, IProject proj) throws CoreException

	{
		IFile file = proj.getFile(name);
		if (!file.exists()) {

			InputStream in = getNewStream();
			file.create(in, true, null);
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file;
	}

	/**
	 * Return a new file as a InputStream
	 */
	public static InputStream getNewStream() {
		return BaseTestCase.class.getResourceAsStream(ITestConstants.TEST_DESIGN_FILE);
	}
}
