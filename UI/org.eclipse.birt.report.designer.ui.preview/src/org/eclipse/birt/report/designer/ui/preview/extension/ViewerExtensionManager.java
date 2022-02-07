/*******************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
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

package org.eclipse.birt.report.designer.ui.preview.extension;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.core.framework.FrameworkException;
import org.eclipse.birt.core.framework.IConfigurationElement;
import org.eclipse.birt.core.framework.IExtension;
import org.eclipse.birt.core.framework.IExtensionPoint;
import org.eclipse.birt.core.framework.IExtensionRegistry;
import org.eclipse.birt.core.framework.Platform;

/**
 * This is an utility class to retrieve viewer extension points.
 */
public class ViewerExtensionManager {

	/** The ID of this viewer extension manager. */
	public static final String VIEWER_EXTENSION_MANAGER_ID = "org.eclipse.birt.report.designer.ui.preview.ViewerExtensionManager"; //$NON-NLS-1$

	/** The extension point ID of viewers. */
	private static final String EXTENSION_VIEWER_CONTRIBUTOR = "org.eclipse.birt.report.designer.ui.preview.viewers"; //$NON-NLS-1$

	private static List getExtensionElements(String extensionPointID) {
		IExtensionRegistry registry = Platform.getExtensionRegistry();

		if (registry == null) {
			return Collections.EMPTY_LIST;
		}

		IExtensionPoint extensionPoint = registry.getExtensionPoint(extensionPointID);

		if (extensionPoint == null) {
			return Collections.EMPTY_LIST;
		}

		return Arrays.asList(extensionPoint.getExtensions());
	}

	public IViewer createViewer() throws FrameworkException {
		return createViewer(null);
	}

	public IViewer createViewer(String viewerID) throws FrameworkException {
		for (Iterator iter = getExtensionElements(EXTENSION_VIEWER_CONTRIBUTOR).iterator(); iter.hasNext();) {
			IExtension extension = (IExtension) iter.next();
			IConfigurationElement[] elements = extension.getConfigurationElements();

			if (elements != null) {
				IConfigurationElement element = null;

				for (int i = 0; i < elements.length; i++) {
					if (viewerID == null || viewerID.equalsIgnoreCase(elements[i].getAttribute("id"))) //$NON-NLS-1$
					{
						element = elements[i];
						break;
					}
				}

				if (element == null && elements.length > 0) {
					element = elements[0];
				}

				return element == null ? null : (IViewer) element.createExecutableExtension("class"); //$NON-NLS-1$
			}
		}
		return null;
	}
}
