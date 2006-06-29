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

package org.eclipse.birt.chart.script;

import java.io.Serializable;
import java.util.Locale;

import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.model.Chart;

import com.ibm.icu.util.ULocale;

/**
 * This interface allows the script to get access to common chart varialbes and
 * communicate with an external context. It is available in version 2, both in
 * Java and JavaScript. It deprecates the JavaScript global functions.
 * 
 */
public interface IChartScriptContext extends Serializable
{

	/**
	 * @return Returns a runtime instance of the chart,or null if not available
	 *         yet.
	 */
	Chart getChartInstance( );

	/**
	 * @return Returns the locale of current context.
	 * @deprecated Use {@link #getULocale()} instead.
	 */
	Locale getLocale( );

	/**
	 * @return Returns the locale of current context.
	 * @since 2.1
	 */
	ULocale getULocale( );

	/**
	 * @return Returns the external context.
	 */
	IExternalContext getExternalContext( );

	/**
	 * @return Returns an ILogger instance, to allow logging from script.
	 * @see org.eclipse.birt.chart.log.ILogger
	 */
	ILogger getLogger( );
}
