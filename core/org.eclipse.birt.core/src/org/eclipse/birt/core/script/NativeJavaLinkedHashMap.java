/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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

	public Object get(int index, Scriptable start) {
		List list = new ArrayList(((Map) javaObject).values());
		if (list.size() > index)
			return list.get(index);

		return Scriptable.NOT_FOUND;
	}
}
