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
	String getScriptValueJS(int index, ScriptValue sv, ULocale locale);
}
