/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/
package org.eclipse.birt.report.data.adapter.impl;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.framework.IBundle;
import org.eclipse.birt.core.framework.IConfigurationElement;
import org.eclipse.birt.core.framework.IExtension;
import org.eclipse.birt.core.framework.IExtensionPoint;
import org.eclipse.birt.core.framework.IExtensionRegistry;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.data.adapter.api.ICubeInterceptor;
import org.eclipse.birt.report.model.api.olap.CubeHandle;

public class CubeInterceptorFinder {
	private static String EXTENSION_POINT = "org.eclipse.birt.report.data.adapter.CubeInterceptor";
	private static String ELEMENT_Interceptor = "Interceptor";
	private static String ATTR_interceptorImplClass = "interceptorImplClass";
	private static String ATTR_cubeHandleClass = "cubeHandleClass";

	private static Logger logger = Logger.getLogger(CubeInterceptorFinder.class.getName());

	public static ICubeInterceptor find(CubeHandle cubeHandle) {
		if (cubeHandle == null) {
			return null;
		}
		IExtensionRegistry extReg = Platform.getExtensionRegistry();
		IExtensionPoint extPoint = extReg.getExtensionPoint(EXTENSION_POINT);
		if (extPoint == null) {
			return null;
		}
		IExtension[] exts = extPoint.getExtensions();
		if (exts == null) {
			return null;
		}
		for (IExtension ext : exts) {
			IConfigurationElement[] configElems = ext.getConfigurationElements();
			if (configElems != null) {
				for (IConfigurationElement ele : configElems) {
					if (ELEMENT_Interceptor.equals(ele.getName())) {
						String cubeHandleClass = ele.getAttribute(ATTR_cubeHandleClass);
						String interceptorImplClass = ele.getAttribute(ATTR_interceptorImplClass);
						if (cubeHandle.getClass().getName().equals(cubeHandleClass)) {
							if (interceptorImplClass != null && interceptorImplClass.length() > 0) {
								IBundle bundle = Platform.getBundle(ext.getNamespace());
								try {
									Class driverClass = bundle.loadClass(interceptorImplClass);
									Object o = driverClass.newInstance();
									if (o instanceof ICubeInterceptor) {
										return (ICubeInterceptor) o;
									}
								} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
									logger.log(Level.WARNING, "", e);
								}
							}
						}
					}
				}
			}
		}
		return null;
	}
}
