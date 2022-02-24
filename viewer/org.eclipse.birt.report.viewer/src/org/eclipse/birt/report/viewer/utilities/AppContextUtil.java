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

package org.eclipse.birt.report.viewer.utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.internal.util.EclipseUtil;
import org.eclipse.birt.report.viewer.api.AppContextExtension;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

/**
 *
 * Utility class is used to retrieve appcontext from extension
 *
 */
public class AppContextUtil {

	/**
	 * AppContext extension point id
	 */
	public static String APPCONTEXT_EXTENSION_ID = "org.eclipse.birt.report.viewer.appcontext"; //$NON-NLS-1$

	/**
	 * Returns all appcontext extension names
	 *
	 * @return
	 */
	public static List getAppContextExtensionNames() {
		List list = new ArrayList();

		// get all configuration elements
		IConfigurationElement[] elements = getConfigurationElements();
		if (elements != null && elements.length > 0) {
			for (int i = 0; i < elements.length; i++) {
				// get class name
				String className = (String) elements[i].getAttribute("class"); //$NON-NLS-1$
				if (className == null) {
					continue;
				}

				// get AppContextExtension object
				AppContextExtension appContextExtension = getAppContextExtension(elements[i], className);
				if (appContextExtension != null && appContextExtension.getName() != null) {
					list.add(appContextExtension.getName());
				}
			}
		}

		return list;
	}

	/**
	 * Returns the AppContextExtension object by name
	 *
	 * @param appContextName
	 * @return
	 */
	public static AppContextExtension getAppContextExtensionByName(String appContextName) {
		if (appContextName == null) {
			return null;
		}

		// get all configuration elements
		IConfigurationElement[] elements = getConfigurationElements();
		if (elements != null && elements.length > 0) {
			for (int i = 0; i < elements.length; i++) {
				// get class name
				String className = (String) elements[i].getAttribute("class"); //$NON-NLS-1$
				if (className == null) {
					continue;
				}

				// get AppContextExtension object
				AppContextExtension appContextExtension = getAppContextExtension(elements[i], className);
				if (appContextExtension == null) {
					continue;
				}

				if (appContextName.equals(appContextExtension.getName())) {
					return appContextExtension;
				}
			}
		}

		return null;
	}

	/**
	 * Returns appcontext object
	 *
	 * @param appContextName
	 * @param appContext
	 * @return
	 */
	public static Map getAppContext(String appContextName, Map appContext) {
		AppContextExtension context = getAppContextExtensionByName(appContextName);
		if (context != null) {
			appContext = context.getAppContext(appContext);
		}

		return appContext;
	}

	/**
	 * Returns AppContextExtension object by name
	 *
	 * @param appContextName
	 * @return
	 */
	private static AppContextExtension getAppContextExtension(IConfigurationElement element, String className) {
		if (element == null || className == null) {
			return null;
		}

		// get bundle name
		String bundleName = getContributingPlugin(element);
		if (bundleName != null) {
			try {
				// load class
				Class clz = loadClass(bundleName, className);
				if (clz != null) {
					return (AppContextExtension) clz.newInstance();
				}
			} catch (Exception e) {
			}
		}

		return null;
	}

	/**
	 * Returns all configuration elements of appcontext extension point
	 *
	 * @return
	 */
	private static IConfigurationElement[] getConfigurationElements() {
		// load extension point entry
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IExtensionPoint extensionPoint = registry.getExtensionPoint(APPCONTEXT_EXTENSION_ID);

		if (extensionPoint != null) {
			// get all configuration elements
			return extensionPoint.getConfigurationElements();
		}

		return null;
	}

	/**
	 * Load class by certain bundle name
	 *
	 * @param bundleName
	 * @param className
	 * @return
	 */
	private static Class loadClass(String bundleName, String className) {
		try {
			Bundle bundle = EclipseUtil.getBundle(bundleName);
			if (bundle != null) {
				if (bundle.getState() == Bundle.RESOLVED) {
					bundle.start(Bundle.START_TRANSIENT);
				}
			}

			if (bundle != null) {
				return bundle.loadClass(className);
			}
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * Returns the bundle name by configuration element
	 *
	 * @param configurationElement
	 * @return
	 */
	private static String getContributingPlugin(IConfigurationElement configurationElement) {
		Object parent = configurationElement;
		while (parent != null) {
			if (parent instanceof IExtension) {
				return ((IExtension) parent).getNamespaceIdentifier();
			}
			parent = ((IConfigurationElement) parent).getParent();
		}
		return null;
	}
}
