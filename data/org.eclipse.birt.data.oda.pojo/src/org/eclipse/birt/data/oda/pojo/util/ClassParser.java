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
package org.eclipse.birt.data.oda.pojo.util;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Mainly for UI to construct the Class structure tree
 */

public class ClassParser {
	private ClassLoader cl;

	public ClassParser(ClassLoader cl) {
		this.cl = cl;
	}

	private static Logger logger = Logger.getLogger(ClassParser.class.getName());

	@SuppressWarnings("unchecked")
	public static Field[] getPublicFields(Class c) {
		assert c != null;
		Field[] fields = c.getFields();
		Arrays.sort(fields, new MemberComparator());
		return fields;
	}

	@SuppressWarnings("unchecked")
	public static Method[] getPublicMethods(Class c, String nameRegex) {
		assert c != null;
		Method[] methods = c.getMethods();
		List<Method> result = new ArrayList<Method>();
		for (Method m : methods) {
			if (isMappable(m) && isNameMatch(m.getName(), nameRegex)) {
				result.add(m);
			}
		}
		Collections.sort(result, new MemberComparator());
		return result.toArray(new Method[0]);
	}

	private static boolean isMappable(Method m) {
		assert m != null;
		return !(m.getReturnType().equals(Void.TYPE));
	}

	@SuppressWarnings("unchecked")
	public static Member[] getPublicMembers(Class c, String methodNameRegex) {
		assert c != null;
		Field[] fields = getPublicFields(c);
		Method[] methods = getPublicMethods(c, methodNameRegex);
		Member[] members = new Member[fields.length + methods.length];
		System.arraycopy(fields, 0, members, 0, fields.length);
		System.arraycopy(methods, 0, members, fields.length, methods.length);
		return members;
	}

	private static class MemberComparator implements Comparator<Member>, Serializable {
		private static final long serialVersionUID = 1L;

		public int compare(Member o1, Member o2) {
			return o1.getName().compareTo(o2.getName());
		}
	}

	private static boolean isNameMatch(String name, String filter) {
		assert name != null;
		if (filter == null || filter.trim().length() == 0) {
			return true;
		}
		try {
			String pattern = Utils.toRegexPattern(filter);
			return name.toUpperCase().matches(pattern.toUpperCase());
		} catch (Exception e) {
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	public Member[] getPublicMembers(Field f, String methodNameRegex) {
		assert f != null;
		Class c = getEssentialClass(f.getGenericType());
		return getPublicMembers(c, methodNameRegex);
	}

	@SuppressWarnings("unchecked")
	public Member[] getPublicMembers(Method m, String methodNameRegex) {
		assert m != null;
		Class c = getEssentialClass(m.getGenericReturnType());
		return getPublicMembers(c, methodNameRegex);
	}

	@SuppressWarnings("unchecked")
	private static boolean isContainer(Class c) {
		assert c != null;
		return Collection.class.isAssignableFrom(c) || Iterable.class.isAssignableFrom(c);
	}

	@SuppressWarnings("unchecked")
	public Class getEssentialClass(Type type) {
		assert type != null;
		if (type instanceof Class) {
			Class c = (Class) type;
			if (isContainer(c)) // not Parameterized container
			{
				return Object.class;
			} else if (c.isArray()) {
				return getEssentialClass(getEssentialClassFromArray(c));
			}
			return c;
		} else if (type instanceof ParameterizedType) {
			ParameterizedType pt = (ParameterizedType) type;
			if (pt.getRawType() instanceof Class && isContainer((Class) pt.getRawType())) {
				Type[] actualTypes = pt.getActualTypeArguments();
				if (actualTypes.length == 1) {
					return getEssentialClass(actualTypes[0]);
				}
			}
			if (pt.getRawType() instanceof Class) {
				return (Class) pt.getRawType();
			}
		} else if (type instanceof GenericArrayType) {
			GenericArrayType gat = (GenericArrayType) type;
			return getEssentialClass(gat.getGenericComponentType());
		}
		return Object.class;
	}

	@SuppressWarnings("unchecked")
	public static String getTypeLabel(Type type) {
		assert type != null;
		if (type instanceof Class) {
			Class c = (Class) type;
			if (c.isArray()) {
				return getTypeLabelFromArray(c);
			}
			return c.getName();
		}
		return type.toString();
	}

	@SuppressWarnings("unchecked")
	public Class getEssentialClassFromArray(Class c) {
		assert c.isArray();
		String name = c.getName();
		int last = name.lastIndexOf('[');
		String className = name.substring(last + 1);
		if (className.length() > 1) {
			try {
				// remove the first "L" identity and last ";"
				className = className.substring(1, className.length() - 1);
				if (cl != null) {
					return cl.loadClass(className);
				} else {
					return Class.forName(className);
				}
			} catch (Throwable e) {
				logger.log(Level.SEVERE, "Failed to get the essential class for array", e); //$NON-NLS-1$
				return Object.class;
			}
		}
		if ("Z".equals(className)) //$NON-NLS-1$
		{
			return Boolean.TYPE;
		}
		if ("B".equals(className)) //$NON-NLS-1$
		{
			return Byte.TYPE;
		}
		if ("C".equals(className)) //$NON-NLS-1$
		{
			return Character.TYPE;
		}
		if ("D".equals(className)) //$NON-NLS-1$
		{
			return Double.TYPE;
		}
		if ("F".equals(className)) //$NON-NLS-1$
		{
			return Float.TYPE;
		}
		if ("I".equals(className)) //$NON-NLS-1$
		{
			return Integer.TYPE;
		}
		if ("J".equals(className)) //$NON-NLS-1$
		{
			return Long.TYPE;
		}
		if ("S".equals(className)) //$NON-NLS-1$
		{
			return Short.TYPE;
		}
		logger.log(Level.SEVERE, "Failed to get the essential class for array"); //$NON-NLS-1$
		return Object.class;
	}

	@SuppressWarnings("unchecked")
	public static String getTypeLabelFromArray(Class c) {
		assert c.isArray();
		String name = c.getName();
		int last = name.lastIndexOf('[');
		String className = name.substring(last + 1);
		if (className.length() > 1) {
			className = className.substring(1, className.length() - 1);

		} else if ("Z".equals(className)) //$NON-NLS-1$
		{
			className = Boolean.TYPE.getName();
		} else if ("B".equals(className)) //$NON-NLS-1$
		{
			className = Byte.TYPE.getName();
		} else if ("C".equals(className)) //$NON-NLS-1$
		{
			className = Character.TYPE.getName();
		} else if ("D".equals(className)) //$NON-NLS-1$
		{
			className = Double.TYPE.getName();
		} else if ("F".equals(className)) //$NON-NLS-1$
		{
			className = Float.TYPE.getName();
		} else if ("I".equals(className)) //$NON-NLS-1$
		{
			className = Integer.TYPE.getName();
		} else if ("J".equals(className)) //$NON-NLS-1$
		{
			className = Long.TYPE.getName();
		} else if ("S".equals(className)) //$NON-NLS-1$
		{
			className = Short.TYPE.getName();
		} else {
			assert false;
		}
		StringBuffer label = new StringBuffer(className);
		for (int i = 0; i <= last; i++) {
			label.append("[]"); //$NON-NLS-1$
		}
		return label.toString();
	}

	public static String getParametersLabel(Method m) {
		assert m != null && isMappable(m);

		StringBuffer sb = new StringBuffer();
		for (Type t : m.getGenericParameterTypes()) {
			sb.append(", ").append(getTypeLabel(t)); //$NON-NLS-1$
		}
		String result = sb.toString();
		if (result.length() > 0) {
			result = result.substring(2); // cut ", " at the beginning
		}
		return result;

	}
}
