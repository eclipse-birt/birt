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

package org.eclipse.birt.chart.device;

import org.eclipse.birt.chart.model.attribute.ScriptValue;

import com.ibm.icu.util.ULocale;

/**
 * The interface defines method(s) to generate scripts for Action Value of chart
 * model.
 * 
 * @since 2.5.2
 */

public interface IScriptMenuHelper {

	/**
	 * Returns scripts of 'Invoke Script' action.
	 * 
	 * @param index  index of action.
	 * @param sv     script action.
	 * @param locale
	 * @return string script value js
	 */
	public String getScriptValueJS(int index, ScriptValue sv, ULocale locale);
}
