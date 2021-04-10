/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.core.internal.function.impl;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.eclipse.birt.core.framework.IConfigurationElement;
import org.eclipse.birt.core.framework.IExtension;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.core.script.functionservice.impl.FunctionProviderBaseImpl;
import org.osgi.framework.Bundle;

/**
 * 
 */

public class FunctionProviderImpl extends FunctionProviderBaseImpl {

	public FunctionProviderImpl() {
		super(Platform.getExtensionRegistry().getExtensionPoint(EXTENSION_POINT));
	}

	/**
	 * Populate library resources. The library resources includes .js script lib and
	 * .jar java lib.
	 * 
	 * @param libs
	 * @param suffix
	 * @param confElement
	 */
	protected void populateResources(List<URL> libs, String suffix, IConfigurationElement confElement) {
		String source = confElement.getAttribute(ATTRIBUTE_LOCATION);
		IExtension extension = confElement.getDeclaringExtension();
		String namespace = extension.getNamespace();
		Bundle bundle = org.eclipse.core.runtime.Platform.getBundle(namespace);
		// available on OSGi platform
		if (bundle != null) {
			Enumeration<String> files = bundle.getEntryPaths(source);

			if (files != null) {
				// In this case, the bundle denotes to a directory.
				while (files.hasMoreElements()) {
					String filePath = files.nextElement();
					if (filePath.toLowerCase().endsWith(suffix)) {
						URL url = bundle.getEntry(filePath);
						if (url != null) {
							libs.add(url);
						}
					}
				}
			} else {
				// the bundle denotes to a file.
				if (source.toLowerCase().endsWith(suffix)) {
					URL url = bundle.getEntry(source);
					if (url != null) {
						libs.add(url);
					}
				}
			}
		}
		// if in non-osgi mode, take it as absolute path
		if (bundle == null || libs.isEmpty()) {
			File file = new File(source);
			if (file.exists() && file.isDirectory()) {
				libs.addAll(findFileList(file, suffix));
			} else {
				if (source.toLowerCase().endsWith(suffix))
					try {
						libs.add(new URL(source));
					} catch (MalformedURLException e) {
						// ignore it
					}
			}
		}
	}

	/**
	 * 
	 * @param file
	 * @param suffix
	 * @return
	 */
	private static List<URL> findFileList(File file, String suffix) {
		List<URL> fileList = new ArrayList<URL>();
		for (File f : file.listFiles()) {
			if (f.isFile() && f.getName().endsWith(suffix)) {
				try {
					fileList.add(f.toURI().toURL());
				} catch (MalformedURLException e) {
					// ignore it
				}
			} else if (f.isDirectory()) {
				fileList.addAll(findFileList(f, suffix));
			}
		}
		return fileList;
	}
}
