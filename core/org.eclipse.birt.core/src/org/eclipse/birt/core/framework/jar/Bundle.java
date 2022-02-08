/*******************************************************************************
 * Copyright (c) 2010,2011 Actuate Corporation.
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

package org.eclipse.birt.core.framework.jar;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;

import org.eclipse.birt.core.framework.IBundle;
import org.eclipse.core.runtime.IContributor;

public class Bundle implements IBundle {

	protected ServicePlatform platform;
	protected URL root;
	protected Contributor contributor;
	protected String version;
	protected Extension[] extensions;
	protected ExtensionPoint[] extensionPoints;
	protected String stateLocation;
	static final Extension[] EMPTY_EXTENSIONS = new Extension[] {};
	static final ExtensionPoint[] EMPTY_EXTENSION_POINTS = new ExtensionPoint[] {};

	Bundle(ServicePlatform platform, URL root, String name) {
		this.platform = platform;
		this.root = root;
		this.contributor = new Contributor(name);
	}

	public String getSymbolicName() {
		return contributor.getName();
	}

	public IContributor getContributor() {
		return contributor;
	}

	public String getVersion() {
		return version;
	}

	public URL getEntry(String path) {
		try {
			return new URL(root, path);
		} catch (MalformedURLException ex) {
			return null;
		}
	}

	public Enumeration<URL> getEntryPaths(String path) {
		ArrayList<URL> urls = new ArrayList<URL>();

		// try
		// {
		// return new URL( root, path );
		// }
		// catch ( MalformedURLException ex )
		// {
		// return null;
		// }

		return Collections.enumeration(urls);
	}

	Extension[] getExtensions() {
		if (extensions == null) {
			return EMPTY_EXTENSIONS;
		}
		return extensions;
	}

	ExtensionPoint[] getExtensionPoints() {
		if (extensionPoints == null) {
			return EMPTY_EXTENSION_POINTS;
		}
		return extensionPoints;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(contributor.getName());
		sb.append("@");
		sb.append(root.toString());
		return sb.toString();
	}

	public Class loadClass(String name) throws ClassNotFoundException {
		return this.getClass().getClassLoader().loadClass(name);
	}

	public synchronized String getStateLocation() {
		if (stateLocation == null) {
			File workspace = platform.getWorkspace();
			String folderName = getSymbolicName();
			if (folderName == null) {
				folderName = String.valueOf(this.hashCode());
			}
			File state = new File(workspace, folderName);
			state.mkdirs();
			stateLocation = state.getAbsolutePath();
		}
		return stateLocation;
	}
}
