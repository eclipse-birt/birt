/*******************************************************************************
 * Copyright (c) 2004-2008 Actuate Corporation.
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

package org.eclipse.birt.report.designer.internal.ui.resourcelocator;

import java.io.File;
import java.net.URL;

import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.model.api.util.URIUtil;

/**
 *
 */

public class ResourceLocator {

	public static ResourceEntry[] getRootEntries() {
		return getRootEntries(null);
	}

	public static ResourceEntry[] getRootEntries(String[] fileNamePattern) {
		ResourceEntry systemResource = new FragmentResourceEntry(fileNamePattern);
		ResourceEntry sharedResource = new PathResourceEntry(fileNamePattern);

		// System Resources node should not be shown if no file is contained in
		// this node.
		if (systemResource.hasChildren()) {
			return new ResourceEntry[] { systemResource, sharedResource };
		} else {
			return new ResourceEntry[] { sharedResource };
		}
	}

	public static ResourceEntry[] getResourceFolder(String[] fileNamePattern) {
		return new ResourceEntry[] { new PathResourceEntry(fileNamePattern) };
	}

	public static ResourceEntry getResourceEntry(String path, String[] fileNamePattern, String name) {
		return new PathResourceEntry(fileNamePattern,
				ReportPlugin.getDefault().getResourceFolder() + File.separator + path, name);
	}

	public static String relativize(String filePath) {
		return URIUtil.getRelativePath(ReportPlugin.getDefault().getResourceFolder(), filePath);
	}

	public static String relativize(URL url) {
		if (url.getProtocol().equals("file")) //$NON-NLS-1$
		{
			return relativize(url.toString());
		} else { // $NON-NLS-1$
			return url.getFile();
		}
	}
}
