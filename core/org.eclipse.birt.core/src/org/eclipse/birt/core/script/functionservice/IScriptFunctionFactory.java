/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
