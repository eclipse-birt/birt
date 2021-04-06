/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.eclipse.birt.report.designer.core.model.schematic.HandleAdapterFactory;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;

/**
 * TODO: Please document
 * 
 * @version $Revision: 1.5 $ $Date: 2005/04/19 01:14:21 $
 */
public final class Utility {

	/**
	 * 
	 */
	private Utility() {
	}

	public static void setProperty(Object obj, String propertyName, Object value)
			throws IntrospectionException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		PropertyDescriptor descriptor = getPropertyDescriptor(obj, propertyName);
		if (descriptor != null) {
			Method method = descriptor.getWriteMethod();
			if (method != null) {
				method.invoke(obj, new Object[] { value });
			}
		}
	}

	public static Object getProperty(Object obj, String propertyName)
			throws IntrospectionException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Object value = null;
		PropertyDescriptor descriptor = getPropertyDescriptor(obj, propertyName);
		if (descriptor != null) {
			Method method = descriptor.getReadMethod();
			if (method != null) {
				value = method.invoke(obj, null);
			}
		}

		return value;
	}

	private static PropertyDescriptor getPropertyDescriptor(Object obj, String propertyName)
			throws IntrospectionException {
		PropertyDescriptor[] descriptors = getPropertyDescriptors(obj);
		for (int n = 0; n < descriptors.length; n++) {
			if (descriptors[n].getName().equals(propertyName)) {
				return descriptors[n];
			}
		}
		return null;
	}

	private static PropertyDescriptor[] getPropertyDescriptors(Object obj) throws IntrospectionException {
		BeanInfo info = Introspector.getBeanInfo(obj.getClass());
		return info.getPropertyDescriptors();
	}

	public static String getUniqueDataSetName(String baseName) {
		String finalName = baseName;
		int n = 1;

		while (HandleAdapterFactory.getInstance().getReportDesignHandleAdapter().checkDataSetName(finalName)) {
			finalName = baseName + n++;
		}

		return finalName;
	}

	public static String getUniqueDataSourceName(String baseName) {
		String finalName = baseName;
		int n = 1;

		while (HandleAdapterFactory.getInstance().getReportDesignHandleAdapter().checkDataSourceName(finalName)) {
			finalName = baseName + n++;
		}

		return finalName;
	}

	public static boolean doesDataSourceModelExtensionExist(String extensionName) {
		IConfigurationElement[] elements = Platform.getExtensionRegistry()
				.getConfigurationElementsFor("org.eclipse.birt.report.model.odaDriverModel"); //$NON-NLS-1$
		if (elements != null) {
			for (int n = 0; n < elements.length; n++) {
				if (elements[n].getName().equals("odaDataSource") //$NON-NLS-1$
						&& extensionName.equals(elements[n].getAttribute("extensionName"))) //$NON-NLS-1$
				{
					return true;
				}
			}
		}

		return false;
	}

	public static boolean doesDataSetModelExtensionExist(String extensionName) {
		IConfigurationElement[] elements = Platform.getExtensionRegistry()
				.getConfigurationElementsFor("org.eclipse.birt.report.model.odaDriverModel"); //$NON-NLS-1$
		if (elements != null) {
			for (int n = 0; n < elements.length; n++) {
				if (elements[n].getName().equals("odaDataSet") //$NON-NLS-1$
						&& extensionName.equals(elements[n].getAttribute("extensionName"))) //$NON-NLS-1$
				{
					return true;
				}
			}
		}

		return false;
	}

}
