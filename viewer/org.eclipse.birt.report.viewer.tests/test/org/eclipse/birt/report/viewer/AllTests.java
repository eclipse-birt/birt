/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.viewer;

import java.io.File;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.birt.report.viewer.util.BaseTestCase;

/**
 * Tests cases run in the build script.
 * 
 */
public class AllTests extends BaseTestCase {

	/**
	 * Create Test Suite
	 */
	public static Test suite() {
		AllTests creator = new AllTests();
		List tmpClasses = creator.createCases();

		TestSuite test = new TestSuite();
		for (int i = 0; i < tmpClasses.size(); i++) {
			try {
				String className = (String) tmpClasses.get(i);
				if (className.endsWith("AllTests")) //$NON-NLS-1$
					continue;

				Class clazz = Class.forName(className);

				int modifier = clazz.getModifiers();

				if (Modifier.isAbstract(modifier) || !Modifier.isPublic(modifier))
					continue;

				test.addTestSuite(clazz);
			} catch (ClassNotFoundException e) {
				assert false;
			}
		}

		return test;
	}

	/**
	 * Returns all class names in the test directories.
	 * 
	 * @return a list containing all cases.
	 */

	private List createCases() {
		String pkgPrefix = "org.eclipse.birt.report.viewer"; //$NON-NLS-1$

		List tmpClasses = new ArrayList();
		tmpClasses.addAll(getClasses("context", pkgPrefix)); //$NON-NLS-1$
		tmpClasses.addAll(getClasses("service", pkgPrefix)); //$NON-NLS-1$
		tmpClasses.addAll(getClasses("utility", pkgPrefix)); //$NON-NLS-1$

		return tmpClasses;

	}

	/**
	 * Returns all class names in the certain directory
	 * 
	 * @param pckgname
	 * @param pkgPrefix
	 * @return
	 */
	private List getClasses(String pckgname, String pkgPrefix) {
		List classes = new ArrayList();

		// Get a File object for the package
		File directory = null;

		String path = pckgname.replace('.', '/');

		String pkgFolder = getClassFolder();
		directory = new File(pkgFolder, path);

		if (directory.exists()) {
			// Get the list of the files contained in the package
			String[] files = directory.list();
			for (int i = 0; i < files.length; i++) {
				// we are only interested in .class files
				if (files[i].endsWith(".java")) //$NON-NLS-1$
				{
					// removes the .class extension

					classes.add(pkgPrefix + '.' + pckgname + '.' + files[i].substring(0, files[i].length() - 5));
				}
			}
		}

		return classes;
	}
}
