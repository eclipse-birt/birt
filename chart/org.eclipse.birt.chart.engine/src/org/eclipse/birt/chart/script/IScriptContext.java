/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.script;

import java.io.Serializable;
import java.util.Locale;

import org.eclipse.birt.chart.log.ILogger;

import com.ibm.icu.util.ULocale;

/**
 * The interface declares methods to provide appropriate context in scripts.
 * 
 * @see 2.5
 */

public interface IScriptContext extends Serializable {
	/**
	 * @return Returns the locale of current context.
	 * @deprecated Use {@link #getULocale()} instead.
	 */
	public Locale getLocale();

	/**
	 * @return Returns the locale of current context.
	 * @since 2.1
	 */
	public ULocale getULocale();

	/**
	 * @return Returns the external context.
	 */
	public IExternalContext getExternalContext();

	/**
	 * @return Returns an ILogger instance, to allow logging from script.
	 * @see org.eclipse.birt.chart.log.ILogger
	 */
	public ILogger getLogger();

	/**
	 * Returns property value.
	 * 
	 * @param key
	 * @return
	 * @since 2.5
	 */
	public Object getProperty(Object key);

	/**
	 * Saves property value.
	 * 
	 * @param key
	 * @param value
	 * @since 2.5
	 */
	public void setProperty(Object key, Object value);
}
