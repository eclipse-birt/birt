
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
 * The script functions are grouped based on the Category they belong to. To
 * invoke a script function, both Category name and function name must be
 * included in the expression.
 *
 * Say, for a script function "foo" under Category "Sample", the expression
 * should be like " Sample.foo() ". An direct reference to "foo()" is not a
 * valid function invoke so that will lead to exception.
 */

public interface IScriptFunctionCategory extends IDescribable, INamedObject {
	IScriptFunction[] getFunctions() throws BirtException;

	/**
	 * Returns whether the category is visible.
	 *
	 */
	boolean isVisible();
}
