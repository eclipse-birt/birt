
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
package org.eclipse.birt.core.script.function.general;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.functionservice.IScriptFunctionExecutor;
import org.eclipse.birt.core.script.functionservice.IScriptFunctionFactory;

/**
 *
 */

public class BirtDateTimeSpanFunctionFactory implements IScriptFunctionFactory {

	@Override
	public IScriptFunctionExecutor getFunctionExecutor(String functionName) throws BirtException {
		return DateTimeSpan.getExecutor(functionName);
	}

}
