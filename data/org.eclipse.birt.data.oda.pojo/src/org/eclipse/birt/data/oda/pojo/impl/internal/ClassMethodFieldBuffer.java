/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.oda.pojo.impl.internal;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.datatools.connectivity.oda.OdaException;

/**
 * A buffer used to save loaded Classes and Methods and Fields. The buffer is
 * created when Connection is opened and released when Connection is closed.
 */
public class ClassMethodFieldBuffer {
	@SuppressWarnings("unchecked")
	private Map<Class, Map<MethodIdentifier, Method>> classMethods;

	@SuppressWarnings("unchecked")
	private Map<Class, Map<String, Field>> classFields;

	public ClassMethodFieldBuffer() {
		classMethods = new HashMap<Class, Map<MethodIdentifier, Method>>();
		classFields = new HashMap<Class, Map<String, Field>>();
	}

	public void release() {
		for (Map<MethodIdentifier, Method> methods : classMethods.values()) {
			methods.clear();
		}
		classMethods.clear();
		for (Map<String, Field> fields : classFields.values()) {
			fields.clear();
		}
		classFields.clear();
	}

	@SuppressWarnings("unchecked")
	public Method getMethod(Class c, MethodIdentifier mi) throws OdaException {
		Method m = findMethod(c, mi);
		return m == null ? saveMethod(c, mi) : m;
	}

	@SuppressWarnings("unchecked")
	public Field getField(Class c, String fieldName) throws OdaException {
		Field f = findField(c, fieldName);
		return f == null ? saveField(c, fieldName) : f;
	}

	@SuppressWarnings("unchecked")
	private Method findMethod(Class c, MethodIdentifier mi) {
		assert c != null && mi != null;

		Map<MethodIdentifier, Method> methods = classMethods.get(c);
		return methods == null ? null : methods.get(mi);
	}

	/**
	 * @param c
	 * @param methodName
	 * @return: the saved method
	 * @throws OdaException
	 */
	@SuppressWarnings("unchecked")
	private Method saveMethod(Class c, MethodIdentifier mi) throws OdaException {
		assert c != null && mi != null;

		try {
			Method m = c.getMethod(mi.getName(), mi.getParams());
			Map<MethodIdentifier, Method> methods = classMethods.get(c);
			if (methods == null) {
				methods = new HashMap<MethodIdentifier, Method>();
				classMethods.put(c, methods);
			}
			methods.put(mi, m);
			return m;
		} catch (SecurityException e) {
			throw new OdaException(e);
		} catch (NoSuchMethodException e) {
			throw new OdaException(e);
		}
	}

	@SuppressWarnings("unchecked")
	private Field findField(Class c, String fieldName) {
		assert c != null && fieldName != null;

		Map<String, Field> fields = classFields.get(c);
		return fields == null ? null : fields.get(fieldName);
	}

	/**
	 * 
	 * @param c
	 * @param fieldName
	 * @return the saved field
	 * @throws OdaException
	 */
	@SuppressWarnings("unchecked")
	private Field saveField(Class c, String fieldName) throws OdaException {
		assert c != null && fieldName != null;

		try {
			Field f = c.getField(fieldName);
			Map<String, Field> fields = classFields.get(c);
			if (fields == null) {
				fields = new HashMap<String, Field>();
				classFields.put(c, fields);
			}
			fields.put(fieldName, f);
			return f;
		} catch (SecurityException e) {
			throw new OdaException(e);
		} catch (NoSuchFieldException e) {
			throw new OdaException(e);
		}
	}
}
