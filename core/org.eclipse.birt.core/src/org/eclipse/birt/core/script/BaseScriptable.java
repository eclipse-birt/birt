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
package org.eclipse.birt.core.script;

import org.mozilla.javascript.Scriptable;

/**
 * An empty implementation of interface <code>Scriptable</code>
 */
public abstract class BaseScriptable implements Scriptable {

	Scriptable prototype;
	Scriptable parent;

	public BaseScriptable() {
	}

	public BaseScriptable(Scriptable parent) {
		setParentScope(parent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mozilla.javascript.Scriptable#delete(java.lang.String)
	 */
	public void delete(String arg0) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mozilla.javascript.Scriptable#delete(int)
	 */
	public void delete(int arg0) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mozilla.javascript.Scriptable#get(int,
	 * org.mozilla.javascript.Scriptable)
	 */
	public Object get(int arg0, Scriptable arg1) {
		return NOT_FOUND;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mozilla.javascript.Scriptable#getDefaultValue(java.lang.Class)
	 */
	public Object getDefaultValue(Class arg0) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mozilla.javascript.Scriptable#getIds()
	 */
	public Object[] getIds() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mozilla.javascript.Scriptable#getParentScope()
	 */
	public Scriptable getParentScope() {
		return parent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mozilla.javascript.Scriptable#getPrototype()
	 */
	public Scriptable getPrototype() {
		return prototype;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mozilla.javascript.Scriptable#has(int,
	 * org.mozilla.javascript.Scriptable)
	 */
	public boolean has(int arg0, Scriptable arg1) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mozilla.javascript.Scriptable#hasInstance(org.mozilla.javascript.
	 * Scriptable)
	 */
	public boolean hasInstance(Scriptable arg0) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mozilla.javascript.Scriptable#put(int,
	 * org.mozilla.javascript.Scriptable, java.lang.Object)
	 */
	public void put(int arg0, Scriptable arg1, Object arg2) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mozilla.javascript.Scriptable#setParentScope(org.mozilla.javascript.
	 * Scriptable)
	 */
	public void setParentScope(Scriptable parent) {
		this.parent = parent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mozilla.javascript.Scriptable#setPrototype(org.mozilla.javascript.
	 * Scriptable)
	 */
	public void setPrototype(Scriptable prototype) {
		this.prototype = prototype;
	}
}
