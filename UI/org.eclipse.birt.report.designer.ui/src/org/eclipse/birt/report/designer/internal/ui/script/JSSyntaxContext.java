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

package org.eclipse.birt.report.designer.internal.ui.script;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.metadata.IClassInfo;

/**
 * A JSSyntaxContext represents a variables container. JSSyntaxContext also
 * provides methods to access avaible Type meta-data.
 */

public class JSSyntaxContext {

	/**
	 * BIRT engine objects defined in DesignEngine.
	 */
	private static Map<String, JSObjectMetaData> engineObjectMap = new HashMap<>();

	// Java class object cache
	private static Map<String, JSObjectMetaData> javaObjectMap = new HashMap<>();

	/**
	 * Context variables map.
	 */
	private Map<String, JSObjectMetaData> objectMetaMap = new HashMap<>();

	static {
		List engineClassesList = DEUtil.getClasses();
		for (Iterator iter = engineClassesList.iterator(); iter.hasNext();) {
			IClassInfo element = (IClassInfo) iter.next();
			engineObjectMap.put(element.getName(), new EngineClassJSObject(element));
		}
	}

	// static methods

	public static JSObjectMetaData getEnginJSObject(String classType) {
		return engineObjectMap.containsKey(classType) ? (JSObjectMetaData) engineObjectMap.get(classType) : null;
	}

	public static JSObjectMetaData[] getAllEnginJSObjects() {
		return engineObjectMap.values().toArray(new JSObjectMetaData[engineObjectMap.size()]);
	}

	public static JSObjectMetaData getJavaClassMeta(Class<?> clazz) {
		if (clazz == null) {
			return null;
		}

		JSObjectMetaData meta = null;
		if (!javaObjectMap.containsKey(clazz.getName())) {
			meta = new JavaClassJSObject(clazz);
			javaObjectMap.put(clazz.getName(), meta);
		} else {
			meta = javaObjectMap.get(clazz.getName());
		}
		return meta;
	}

	public static JSObjectMetaData getJavaClassMeta(String className) throws ClassNotFoundException {
		if (className == null) {
			return null;
		}

		JSObjectMetaData meta = null;
		if (!javaObjectMap.containsKey(className)) {
			meta = new JavaClassJSObject(className);
			javaObjectMap.put(className, meta);
		} else {
			meta = javaObjectMap.get(className);
		}
		return meta;
	}

	public boolean setVariable(String name, String className) {
		JSObjectMetaData engineObj = getEnginJSObject(className);

		if (engineObj != null) {
			objectMetaMap.put(name, engineObj);

			return true;
		} else {
			try {
				objectMetaMap.put(name, getJavaClassMeta(className));

				return true;
			} catch (Exception e) {
				removeVariable(name);

				return false;
			}
		}
	}

	public void setVariable(String name, Class<?> clazz) throws ClassNotFoundException {
		objectMetaMap.put(name, new JavaClassJSObject(clazz));
	}

	public void setVariable(String name, IClassInfo classInfo) {
		if (classInfo == null) {
			objectMetaMap.put(name, null);
		} else {
			objectMetaMap.put(name, new ExtensionClassJSObject(classInfo));
		}
	}

	public void setVariable(String name, JSObjectMetaData meta) {
		objectMetaMap.put(name, meta);
	}

	public void removeVariable(String name) {
		objectMetaMap.remove(name);
	}

	public void clear() {
		objectMetaMap.clear();
	}

	public JSObjectMetaData getVariableMeta(String variableName) {
		if (objectMetaMap.containsKey(variableName)) {
			return objectMetaMap.get(variableName);
		} else {
			return getEnginJSObject(variableName);
		}
	}

}
