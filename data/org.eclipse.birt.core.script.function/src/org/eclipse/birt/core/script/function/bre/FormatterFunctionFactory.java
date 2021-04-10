/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.core.script.function.bre;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.functionservice.IScriptFunctionExecutor;
import org.eclipse.birt.core.script.functionservice.IScriptFunctionFactory;

public class FormatterFunctionFactory implements IScriptFunctionFactory {

	public IScriptFunctionExecutor getFunctionExecutor(String functionName) throws BirtException {
		return new Formatter(functionName);
	}

}