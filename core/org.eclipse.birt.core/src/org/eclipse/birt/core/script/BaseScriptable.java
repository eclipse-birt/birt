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
	@Override
	public void delete(String arg0) {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.mozilla.javascript.Scriptable#delete(int)
	 */
	@Override
	public void delete(int arg0) {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.mozilla.javascript.Scriptable#get(int,
	 * org.mozilla.javascript.Scriptable)
	 */
	@Override
	public Object get(int arg0, Scriptable arg1) {
		return NOT_FOUND;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.mozilla.javascript.Scriptable#getDefaultValue(java.lang.Class)
	 */
	@Override
	public Object getDefaultValue(Class arg0) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.mozilla.javascript.Scriptable#getIds()
	 */
	@Override
	public Object[] getIds() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.mozilla.javascript.Scriptable#getParentScope()
	 */
	@Override
	public Scriptable getParentScope() {
		return parent;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.mozilla.javascript.Scriptable#getPrototype()
	 */
	@Override
	public Scriptable getPrototype() {
		return prototype;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.mozilla.javascript.Scriptable#has(int,
	 * org.mozilla.javascript.Scriptable)
	 */
	@Override
	public boolean has(int arg0, Scriptable arg1) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.mozilla.javascript.Scriptable#hasInstance(org.mozilla.javascript.
	 * Scriptable)
	 */
	@Override
	public boolean hasInstance(Scriptable arg0) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.mozilla.javascript.Scriptable#put(int,
	 * org.mozilla.javascript.Scriptable, java.lang.Object)
	 */
	@Override
	public void put(int arg0, Scriptable arg1, Object arg2) {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.mozilla.javascript.Scriptable#setParentScope(org.mozilla.javascript.
	 * Scriptable)
	 */
	@Override
	public void setParentScope(Scriptable parent) {
		this.parent = parent;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.mozilla.javascript.Scriptable#setPrototype(org.mozilla.javascript.
	 * Scriptable)
	 */
	@Override
	public void setPrototype(Scriptable prototype) {
		this.prototype = prototype;
	}
}
