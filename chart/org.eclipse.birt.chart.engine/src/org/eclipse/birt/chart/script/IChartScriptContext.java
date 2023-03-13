/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

import org.eclipse.birt.chart.model.Chart;

/**
 * This interface allows the script to get access to common chart varialbes and
 * communicate with an external context. It is available in version 2, both in
 * Java and JavaScript. It deprecates the JavaScript global functions.
 *
 */
public interface IChartScriptContext extends IScriptContext {

	/**
	 * @return Returns a runtime instance of the chart,or null if not available yet.
	 */
	Chart getChartInstance();
}
