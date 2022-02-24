/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
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

package org.eclipse.birt.core.script.functionservice;

import org.eclipse.birt.core.exception.BirtException;

/**
 * This interface is the entry point of an Script Function Service extension. It
 * provides information such as Categories and ScriptFunctions that can be
 * provided by the extension.
 * 
 */
public interface IScriptFunctionFactory {
	/**
	 * Return an array of script function executors according to the function name.
	 * 
	 * @param category
	 * @return
	 */
	public IScriptFunctionExecutor getFunctionExecutor(String functionName) throws BirtException;

}
