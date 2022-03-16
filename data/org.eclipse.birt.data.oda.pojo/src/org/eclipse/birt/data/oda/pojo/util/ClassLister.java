/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
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
package org.eclipse.birt.data.oda.pojo.util;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 *
 */
public class ClassLister {
	private static Logger logger = Logger.getLogger(ClassLister.class.getName());

	private static String ANONYMOUS_CLASS_REGEX = ".*\\Q$\\E[0-9]+.*"; //$NON-NLS-1$

	public static String[] listClasses(URL[] urls) {
		if (urls == null) {
			return new String[0];
		}
		Set<String> result = new HashSet<>();
		for (URL url : urls) {
			File f = null;
			try {
				f = new File(url.toURI());
			} catch (URISyntaxException e) {
				logger.log(Level.WARNING, "Failed to transfer to file:" + url, e); //$NON-NLS-1$
				continue;
			}
			if (f.isFile()) {
				result.addAll(listClassesFromJar(f));
			} else if (f.isDirectory()) {
				result.addAll(listClassesFromDir(f, "")); //$NON-NLS-1$
			}
		}
		return result.toArray(new String[0]);
	}

	private static Set<String> listClassesFromDir(File classFolder, String prefix) {
		Set<String> result = new HashSet<>();
		for (File f : classFolder.listFiles()) {
			if (f.isFile() && f.getName().endsWith(".class")) //$NON-NLS-1$
			{
				String className = prefix + f.getName().substring(0, f.getName().length() - 6);
				className = process$(className);
				if (className != null) {
					result.add(className);
				}
			} else if (f.isDirectory()) {
				String newPrefix = prefix + f.getName() + "."; //$NON-NLS-1$
				result.addAll(listClassesFromDir(f, newPrefix));
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	private static Set<String> listClassesFromJar(File jarFile) {
		Set<String> result = new HashSet<>();
		List<String> entries = new ArrayList<>();
		try {
			ZipFile zf = new ZipFile(jarFile);
			Enumeration e = zf.entries();
			while (e.hasMoreElements()) {
				ZipEntry ze = (ZipEntry) e.nextElement();
				if (!ze.isDirectory()) {
					entries.add(ze.getName());
				}
			}
			zf.close();
		} catch (IOException e1) {
			logger.log(Level.WARNING, "Failed to read file: " + jarFile, e1); //$NON-NLS-1$
		}
		for (String entry : entries) {
			if (entry.endsWith(".class")) //$NON-NLS-1$
			{
				String className = packagify(entry);
				className = process$(className);
				if (className != null) {
					result.add(className);
				}
			}
		}
		return result;
	}

	private static String packagify(String resourceName) {
		resourceName = (resourceName.replace('/', '.')).substring(0, resourceName.length() - 6); // $NON-NLS-1$
																									// //$NON-NLS-2$
		return resourceName;
	}

	private static String process$(String className) {
		if (className.matches(ANONYMOUS_CLASS_REGEX)) {
			return null;
		}
		return className.replace('$', '.');
	}
}
