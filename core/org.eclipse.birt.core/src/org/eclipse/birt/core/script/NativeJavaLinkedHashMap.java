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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.mozilla.javascript.Scriptable;

/**
 * Represents the scriptable object for Java object, <code>LinkedHashMap</code>.
 * This class supports to access hash map with index.
 *
 */

public class NativeJavaLinkedHashMap extends NativeJavaMap {

	/**
	 *
	 */
	private static final long serialVersionUID = 5212200683521932832L;

	public NativeJavaLinkedHashMap() {
	}

	public NativeJavaLinkedHashMap(Scriptable scope, Object javaObject, Class staticType) {
		super(scope, javaObject, staticType);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.mozilla.javascript.Scriptable#get(int,
	 * org.mozilla.javascript.Scriptable)
	 */

	@Override
	public Object get(int index, Scriptable start) {
		List list = new ArrayList(((Map) javaObject).values());
		if (list.size() > index) {
			return list.get(index);
		}

		return Scriptable.NOT_FOUND;
	}
}
