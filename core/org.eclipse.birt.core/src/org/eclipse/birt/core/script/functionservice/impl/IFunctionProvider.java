/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   See git history
 *******************************************************************************/
package org.eclipse.birt.core.script.functionservice.impl;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.functionservice.IScriptFunction;
import org.eclipse.birt.core.script.functionservice.IScriptFunctionCategory;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

public interface IFunctionProvider {
	/**
	 * Return all the categories defined by extensions.
	 * 
	 * @return
	 * @throws BirtException
	 */
	public IScriptFunctionCategory[] getCategories() throws BirtException;

	/**
	 * Return the functions that defined in a category.
	 * 
	 * @param categoryName
	 * @return
	 * @throws BirtException
	 */
	public IScriptFunction[] getFunctions(String categoryName) throws BirtException;

	/**
	 * Register script functions to scope.
	 * 
	 * @param cx
	 * @param scope
	 * @throws BirtException
	 */
	public void registerScriptFunction(Context cx, Scriptable scope) throws BirtException;
}
