
/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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

import java.io.Serializable;

import org.eclipse.birt.core.exception.BirtException;

/**
 * Execute the function using given arguments.
 */

public interface IScriptFunctionExecutor extends Serializable {
	/**
	 * Execute the Script Function with an array of arguments.
	 * 
	 * @param arguments
	 * @return
	 */
	public Object execute(Object[] arguments, IScriptFunctionContext context) throws BirtException;

}
