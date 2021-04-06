
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

/**
 * This interface defines a Script Function that can be used in java script
 * expression.
 */

public interface IScriptFunction extends IDescribable, INamedObject, IScriptFunctionExecutor {
	/**
	 * Return the Category this Script Function belongs to.
	 * 
	 * @return
	 */
	public IScriptFunctionCategory getCategory();

	/**
	 * Return the expected return data type of this Script Function. The return type
	 * must be one of Birt Supported Data Type as listed in
	 * org.eclipse.birt.core.data.DataType class.
	 * 
	 * @return
	 */
	public String getDataTypeName();

	/**
	 * Return the argument definitions of this Script Function.
	 * 
	 * @return
	 */
	public IScriptFunctionArgument[] getArguments();

	/**
	 * 
	 */
	public boolean allowVarArguments();

	/**
	 * Returns whether the function is visible.
	 * 
	 */
	public boolean isVisible();

	/**
	 * 
	 * @return
	 */
	public boolean isStatic();

	/**
	 * 
	 * @return
	 */
	public boolean isConstructor();

}
