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

package org.eclipse.birt.core.script;

import java.util.Map;

import org.eclipse.birt.core.i18n.CoreMessages;
import org.eclipse.birt.core.i18n.ResourceConstants;
import org.mozilla.javascript.JavaScriptException;
import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.Scriptable;

/**
 * Represents the scriptable object for Java object which implements the
 * interface <code>Map</code>.
 * 
 */
public class NativeJavaMap extends NativeJavaObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3988584321233636629L;

	public NativeJavaMap() {
	}

	public NativeJavaMap(Scriptable scope, Object javaObject, Class staticType) {
		super(scope, javaObject, staticType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mozilla.javascript.Scriptable#has(java.lang.String,
	 * org.mozilla.javascript.Scriptable)
	 */

	public boolean has(String name, Scriptable start) {
		return ((Map) javaObject).containsKey(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mozilla.javascript.Scriptable#get(java.lang.String,
	 * org.mozilla.javascript.Scriptable)
	 */

	public Object get(String name, Scriptable start) {
		if ("length".equals(name)) {
			return Integer.valueOf(((Map) javaObject).size());
		}
		if (has(name, start)) {
			return ((Map) javaObject).get(name);
		}
		String errorMessage = CoreMessages.getFormattedString(ResourceConstants.JAVASCRIPT_NATIVE_NOT_FOUND, name);
		throw new JavaScriptException(errorMessage, "<unknown>", -1); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mozilla.javascript.Scriptable#put(java.lang.String,
	 * org.mozilla.javascript.Scriptable, java.lang.Object)
	 */

	public void put(String name, Scriptable start, Object value) {
		((Map) javaObject).put(name, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mozilla.javascript.Scriptable#delete(java.lang.String)
	 */

	public void delete(String name) {
		((Map) javaObject).remove(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mozilla.javascript.Scriptable#get(int,
	 * org.mozilla.javascript.Scriptable)
	 */

	public Object get(int index, Scriptable start) {
		String key = Integer.valueOf(index).toString();
		if (has(key, start)) {
			return ((Map) javaObject).get(key);
		}
		String errorMessage = CoreMessages.getFormattedString(ResourceConstants.JAVASCRIPT_NATIVE_NOT_FOUND, index);
		throw new JavaScriptException(errorMessage, "<unknown>", -1); //$NON-NLS-1$
	}

	public void put(int index, Scriptable start, Object value) {
		((Map) javaObject).put(Integer.valueOf(index).toString(), value);
	}

	public Object[] getIds() {
		return ((Map) javaObject).keySet().toArray();
	}

}
