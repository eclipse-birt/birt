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

import org.eclipse.birt.data.engine.api.script.IJavascriptContext;
import org.eclipse.birt.data.engine.core.DataException;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

/**
 * Implements the JavaScript DataSource[] array, obtained from
 * "report.dataSources"
 */
public class JSDataSources extends ScriptableObject {
	private Map dataSources;

	private static Logger logger = Logger.getLogger(JSDataSources.class.getName());
	private static final long serialVersionUID = -3214290672302098993L;

	/**
	 * Constructor.
	 *
	 * @param dataSourceMap A map of data source name (String) to DataSourceRuntime
	 *                      objects
	 */
	public JSDataSources(Map dataSourceMap) {
		logger.entering(JSDataSources.class.getName(), "JSDataSources");
		assert dataSourceMap != null;
		this.dataSources = dataSourceMap;

		// This object is not modifiable
		sealObject();
	}

	/**
	 * @see org.mozilla.javascript.Scriptable#getClassName()
	 */
	@Override
	public String getClassName() {
		return "DataSources";
	}

	/**
	 * @see org.mozilla.javascript.Scriptable#get(java.lang.String,
	 *      org.mozilla.javascript.Scriptable)
	 */
	@Override
	public Object get(String name, Scriptable start) {
		try {
			logger.entering(JSDataSources.class.getName(), "get", name);
			IJavascriptContext ds = (IJavascriptContext) dataSources.get(name);
			if (ds != null) {
				if (logger.isLoggable(Level.FINER)) {
					logger.exiting(JSDataSources.class.getName(), "get", ds.getScriptScope());
				}
				return ds.getScriptScope();
			} else {
				if (logger.isLoggable(Level.FINER)) {
					logger.exiting(JSDataSources.class.getName(), "get", super.get(name, start));
				}
				return super.get(name, start);
			}
		} catch (DataException e) {
			throw new RuntimeException(e.getLocalizedMessage(), e);
		}
	}

	/**
	 * @see org.mozilla.javascript.Scriptable#getIds()
	 */
	@Override
	public Object[] getIds() {
		// Returns all data source names
		return dataSources.keySet().toArray(new String[0]);
	}

	/**
	 * @see org.mozilla.javascript.Scriptable#has(java.lang.String,
	 *      org.mozilla.javascript.Scriptable)
	 */
	@Override
	public boolean has(String name, Scriptable start) {
		logger.entering(JSDataSources.class.getName(), "has", name);
		if (dataSources.containsKey(name)) {
			logger.exiting(JSDataSources.class.getName(), "has", Boolean.valueOf(true));
			return true;
		} else {
			if (logger.isLoggable(Level.FINER)) {
				logger.exiting(JSDataSources.class.getName(), "has", Boolean.valueOf(super.has(name, start)));
			}
			return super.has(name, start);
		}
	}
}
