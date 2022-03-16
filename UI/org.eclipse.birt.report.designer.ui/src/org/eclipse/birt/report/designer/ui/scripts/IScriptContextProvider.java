/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.report.designer.ui.scripts;

/**
 * IScriptContextProvider
 */
public interface IScriptContextProvider {

	/**
	 * Returns associated script context info for specific context.
	 *
	 * @param contextName
	 * @return
	 */
	IScriptContextInfo[] getScriptContext(String contextName);

	/**
	 * Returns associated script context info for specific context and method.
	 *
	 * @param contextName
	 * @param methodName
	 * @return
	 */
	IScriptContextInfo[] getScriptContext(String contextName, String methodName);
}
