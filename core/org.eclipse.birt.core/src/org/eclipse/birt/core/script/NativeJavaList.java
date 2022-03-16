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

import java.util.List;

import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.Scriptable;

/**
 *
 */
public class NativeJavaList extends NativeJavaObject {

	/**
	 *
	 */
	private static final long serialVersionUID = 1207726021220583694L;

	public NativeJavaList() {
	}

	public NativeJavaList(Scriptable scope, Object javaObject, Class staticType) {
		super(scope, javaObject, staticType);
	}

	@Override
	public boolean has(int index, Scriptable start) {
		return index >= 0 && index < ((List) javaObject).size();
	}

	@Override
	public Object get(int index, Scriptable start) {
		return ((List) javaObject).get(index);
	}

	@Override
	public void put(int index, Scriptable start, Object value) {
		((List) javaObject).add(index, value);
	}
}
