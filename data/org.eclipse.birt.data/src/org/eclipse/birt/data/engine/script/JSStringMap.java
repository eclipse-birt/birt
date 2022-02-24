/*
 *************************************************************************
 * Copyright (c) 2004-2005 Actuate Corporation.
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
 *
 *************************************************************************
 */
package org.eclipse.birt.data.engine.script;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

/**
 * Javascript wrapper of a Map object which maps either String to String, or
 * String to an array of Strings.
 *
 * It is used to implement ROM script access to the public properties of
 * extended data set and data source.
 */
public class JSStringMap extends ScriptableObject {
	private Map map;

	private static Logger logger = Logger.getLogger(JSStringMap.class.getName());
	private static final long serialVersionUID = -4866037635884065761L;

	/**
	 * Constructor
	 *
	 * @param map      Java Map to wrap
	 * @param mapToSet If true, map is a mapping from String to Set
	 */
	public JSStringMap(Map map) {
		assert map != null;
		logger.entering(JSStringMap.class.getName(), "JSStringMap");
		this.map = map;
	}

	/**
	 * @see org.mozilla.javascript.Scriptable#getClassName()
	 */
	@Override
	public String getClassName() {
		return "StringMap";
	}

	/**
	 * @see org.mozilla.javascript.Scriptable#delete(java.lang.String)
	 */
	@Override
	public void delete(String name) {
		logger.entering(JSStringMap.class.getName(), "delete", name);
		if (map.containsKey(name)) {
			map.remove(name);
		}
		logger.exiting(JSStringMap.class.getName(), "delete");
	}

	/**
	 * @see org.mozilla.javascript.Scriptable#get(java.lang.String,
	 *      org.mozilla.javascript.Scriptable)
	 */
	@Override
	public Object get(String name, Scriptable start) {
		logger.entering(JSStringMap.class.getName(), "get", name);
		if (map.containsKey(name)) {
			Object result = map.get(name);
			logger.exiting(JSStringMap.class.getName(), "get", result);
			return result;
		} else {
			if (logger.isLoggable(Level.FINER)) {
				logger.exiting(JSStringMap.class.getName(), "get", super.get(name, start));
			}
			return super.get(name, start);
		}
	}

	/**
	 * @see org.mozilla.javascript.Scriptable#getIds()
	 */
	@Override
	public Object[] getIds() {
		return map.keySet().toArray(new String[0]);
	}

	/**
	 * @see org.mozilla.javascript.Scriptable#has(java.lang.String,
	 *      org.mozilla.javascript.Scriptable)
	 */
	@Override
	public boolean has(String name, Scriptable start) {
		logger.entering(JSStringMap.class.getName(), "has", name);
		if (logger.isLoggable(Level.FINER)) {
			logger.exiting(JSStringMap.class.getName(), "has", Boolean.valueOf(map.containsKey(name)));
		}
		return map.containsKey(name);
	}

	/**
	 * @see org.mozilla.javascript.Scriptable#put(java.lang.String,
	 *      org.mozilla.javascript.Scriptable, java.lang.Object)
	 */
	@Override
	public void put(String name, Scriptable start, Object value) {
		logger.entering(JSStringMap.class.getName(), "put", name);
		String valStr = value.toString();
		map.put(name, valStr);
		logger.exiting(JSStringMap.class.getName(), "put");
	}
}
