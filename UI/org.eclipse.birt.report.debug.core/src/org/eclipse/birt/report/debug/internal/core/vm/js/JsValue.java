/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  Others: See git history
 *******************************************************************************/

package org.eclipse.birt.report.debug.internal.core.vm.js;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.eclipse.birt.report.debug.internal.core.vm.VMConstants;
import org.eclipse.birt.report.debug.internal.core.vm.VMValue;
import org.eclipse.birt.report.debug.internal.core.vm.VMVariable;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextAction;
import org.mozilla.javascript.NativeJavaConstructor;
import org.mozilla.javascript.NativeJavaMethod;
import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.NativeJavaPackage;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.Undefined;
import org.mozilla.javascript.debug.DebuggableObject;

/**
 * JsValue
 */
public class JsValue implements VMValue, VMConstants {

	private boolean isPrimitive;
	private Object value;
	private String reservedValueType;

	public JsValue(Object value) {
		this(value, false);
	}

	JsValue(Object value, String fixedValueType) {
		this(value, false);

		this.reservedValueType = fixedValueType;
	}

	JsValue(Object value, boolean isPrimitive) {
		this.value = value;
		this.isPrimitive = isPrimitive;
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof JsValue)) {
			return false;
		}

		JsValue that = (JsValue) obj;

		if (this.isPrimitive != that.isPrimitive) {
			return false;
		}

		if (this.value == null) {
			if (that.value != null) {
				return false;
			}
		} else {
			if (!this.value.equals(that.value)) {
				return false;
			}
		}

		if (this.reservedValueType == null) {
			return that.reservedValueType == null;
		} else {
			return this.reservedValueType.equals(that.reservedValueType);
		}

	}

	public int hashCode() {
		int hash = Boolean.valueOf(isPrimitive).hashCode();

		if (value != null) {
			hash ^= value.hashCode();
		}

		if (reservedValueType != null) {
			hash ^= reservedValueType.hashCode();
		}
		return hash;
	}

	public VMVariable[] getMembers() {
		return (VMVariable[]) Context.call(new ContextAction() {

			public Object run(Context arg0) {
				try {
					return getMembersImpl(arg0);
				} catch (Exception e) {
					StringWriter sw = new StringWriter();
					e.printStackTrace(new PrintWriter(sw));

					return new VMVariable[] { new JsVariable(sw.toString(), ERROR_LITERAL, EXCEPTION_TYPE) };
				}
			}

		});
	}

	static boolean isValidJsValue(Object val) {
		return (val != Scriptable.NOT_FOUND && !(val instanceof Undefined) && !(val instanceof NativeJavaMethod)
				&& !(val instanceof NativeJavaConstructor) && !(val instanceof NativeJavaPackage));

	}

	private VMVariable[] getMembersImpl(Context cx) {
		if (reservedValueType != null) {
			return NO_CHILD;
		}

		Object valObj = value;

		if (value instanceof NativeJavaObject) {
			valObj = ((NativeJavaObject) value).unwrap();
		}

		if (valObj == null || valObj.getClass().isPrimitive() || isPrimitive) {
			return NO_CHILD;
		}

		List children = new ArrayList();

		if (valObj.getClass().isArray()) {
			int len = Array.getLength(valObj);

			boolean primitive = valObj.getClass().getComponentType().isPrimitive();

			for (int i = 0; i < len; i++) {
				Object aobj = Array.get(valObj, i);

				if (isValidJsValue(aobj)) {
					children.add(new JsVariable(aobj, "[" //$NON-NLS-1$
							+ children.size() + "]", primitive)); //$NON-NLS-1$
				}
			}
		} else if (valObj instanceof Scriptable) {
			Object[] ids;

			if (valObj instanceof DebuggableObject) {
				ids = ((DebuggableObject) valObj).getAllIds();
			} else {
				ids = ((Scriptable) valObj).getIds();
			}

			if (ids == null || ids.length == 0) {
				return NO_CHILD;
			}

			for (int i = 0; i < ids.length; i++) {
				if (ids[i] instanceof String) {
					Object val = ScriptableObject.getProperty((Scriptable) valObj, (String) ids[i]);

					if (val instanceof NativeJavaObject) {
						val = ((NativeJavaObject) val).unwrap();
					}

					if (isValidJsValue(val)) {
						children.add(new JsVariable(val, (String) ids[i]));
					}
				}
			}
		} else {
			// refelct native java objects
			reflectMembers(valObj, children);
		}

		if (children.size() == 0) {
			return NO_CHILD;
		}

		Collections.sort(children);

		return (VMVariable[]) children.toArray(new VMVariable[children.size()]);
	}

	private void reflectMembers(Object obj, List children) {
		HashMap names = new HashMap();

		Class clz = obj.getClass();
		Field fd = null;

		try {
			while (clz != null) {
				Field[] fds = clz.getDeclaredFields();

				for (int i = 0; i < fds.length; i++) {
					fd = fds[i];
					fd.setAccessible(true);

					if (Modifier.isStatic(fd.getModifiers()) || names.containsKey(fd.getName())) {
						continue;
					}

					// special fix for Java 5 LinkedHashMap.Entry hashCode()
					// implementation error, which is fixed in 6 though.
					if (obj instanceof LinkedHashMap && "header".equals(fd.getName())) //$NON-NLS-1$
					{
						continue;
					}

					JsVariable jsVar = new JsVariable(fd.get(obj), fd.getName(), fd.getType().isPrimitive());

					jsVar.setTypeName(convertArrayTypeName(fd.getType(), fd.getType().isPrimitive()));

					children.add(jsVar);

					names.put(fd.getName(), null);
				}

				clz = clz.getSuperclass();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String convertArrayTypeName(Class clz, boolean explicitPrimitive) {
		if (clz.isArray()) {
			return convertPrimativeTypeName(clz.getComponentType(), explicitPrimitive) + "[]"; //$NON-NLS-1$
		} else {
			return convertPrimativeTypeName(clz, explicitPrimitive);
		}
	}

	private static String convertPrimativeTypeName(Class clz, boolean explictPrimitive) {
		if (clz.isPrimitive() || explictPrimitive) {
			if (Boolean.class.equals(clz) || Boolean.TYPE.equals(clz)) {
				return "boolean"; //$NON-NLS-1$
			}

			if (Character.class.equals(clz) || Character.TYPE.equals(clz)) {
				return "char"; //$NON-NLS-1$
			}

			if (Byte.class.equals(clz) || Byte.TYPE.equals(clz)) {
				return "byte"; //$NON-NLS-1$
			}

			if (Short.class.equals(clz) || Short.TYPE.equals(clz)) {
				return "short"; //$NON-NLS-1$
			}

			if (Integer.class.equals(clz) || Integer.TYPE.equals(clz)) {
				return "int"; //$NON-NLS-1$
			}

			if (Long.class.equals(clz) || Long.TYPE.equals(clz)) {
				return "long"; //$NON-NLS-1$
			}

			if (Float.class.equals(clz) || Float.TYPE.equals(clz)) {
				return "float"; //$NON-NLS-1$
			}

			if (Double.class.equals(clz) || Double.TYPE.equals(clz)) {
				return "double"; //$NON-NLS-1$
			}

			if (Void.class.equals(clz) || Void.TYPE.equals(clz)) {
				return "void"; //$NON-NLS-1$
			}
		}

		return clz.getName();
	}

	public String getTypeName() {
		if (reservedValueType != null) {
			return reservedValueType;
		}

		Object valObj = value;

		if (value instanceof NativeJavaObject) {
			valObj = ((NativeJavaObject) value).unwrap();
		}

		if (valObj != null) {
			return convertArrayTypeName(valObj.getClass(), isPrimitive);
		}

		return "null"; //$NON-NLS-1$
	}

	public String getValueString() {
		Object valObj = value;

		if (value instanceof NativeJavaObject) {
			valObj = ((NativeJavaObject) value).unwrap();
		}

		if (valObj instanceof String) {
			return "\"" + valObj + "\""; //$NON-NLS-1$ //$NON-NLS-2$
		}

		if (valObj instanceof Character) {
			return "'" + valObj + "'"; //$NON-NLS-1$ //$NON-NLS-2$
		}

		if (valObj instanceof Number || valObj instanceof Boolean || valObj == null) {
			return String.valueOf(valObj);
		}

		if (valObj.getClass().isArray()) {
			Class compType = valObj.getClass().getComponentType();

			int len = Array.getLength(valObj);

			return convertPrimativeTypeName(compType, isPrimitive) + "[" //$NON-NLS-1$
					+ len + "]"; //$NON-NLS-1$

		}

		return convertPrimativeTypeName(valObj.getClass(), isPrimitive);
	}

	public String toString() {
		return getTypeName() + ": " + getValueString(); //$NON-NLS-1$
	}

}
