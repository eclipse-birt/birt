/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
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

package org.eclipse.birt.chart.script;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.eclipse.birt.chart.log.ILogger;

import com.ibm.icu.util.ULocale;

/**
 * The abstract class implements common methods to store script contexts.
 * 
 * @since 2.5
 */

public abstract class AbstractScriptContext implements IScriptContext {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 1L;

	protected transient IExternalContext externalContext;

	protected ULocale locale;

	protected transient ILogger logger;

	protected Map<Object, Object> propertyMap;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.script.IScriptContext#getExternalContext()
	 */
	public IExternalContext getExternalContext() {
		return externalContext;
	}

	/**
	 * @param externalContext the context of script
	 */
	public void setExternalContext(IExternalContext externalContext) {
		this.externalContext = externalContext;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.script.IScriptContext#getLocale()
	 * @deprecated Use {@link #getULocale()} instead.
	 */
	public Locale getLocale() {
		return locale == null ? null : locale.toLocale();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.script.IScriptContext#getLogger()
	 */
	public ILogger getLogger() {
		return logger;
	}

	/**
	 * Sets associated logger.
	 * 
	 * @param logger Logger
	 */
	public void setLogger(ILogger logger) {
		this.logger = logger;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.script.IScriptContext#getULocale()
	 */
	public ULocale getULocale() {
		return locale;
	}

	/**
	 * Sets associated locale.
	 * 
	 * @param locale Locale
	 */
	public void setULocale(ULocale locale) {
		this.locale = locale;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.script.IScriptContext#getProperty(java.lang.Object )
	 */
	public Object getProperty(Object key) {
		if (propertyMap == null) {
			return null;
		}
		return propertyMap.get(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.script.IScriptContext#setProperty(java.lang.Object ,
	 * java.lang.Object)
	 */
	public void setProperty(Object key, Object value) {
		if (propertyMap == null) {
			propertyMap = new HashMap<Object, Object>();
		}

		propertyMap.put(key, value);
	}
}
