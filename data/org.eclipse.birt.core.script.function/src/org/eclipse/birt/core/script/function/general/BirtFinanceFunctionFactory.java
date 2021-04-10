
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
package org.eclipse.birt.core.script.function.general;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.functionservice.IScriptFunctionExecutor;
import org.eclipse.birt.core.script.functionservice.IScriptFunctionFactory;

/**
 * 
 */

public class BirtFinanceFunctionFactory implements IScriptFunctionFactory {
	public IScriptFunctionExecutor getFunctionExecutor(String functionName) throws BirtException {
		return Finance.getExecutor(functionName);
	}

}
