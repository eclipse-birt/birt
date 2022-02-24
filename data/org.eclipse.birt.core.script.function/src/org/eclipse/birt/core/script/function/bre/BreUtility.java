
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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
package org.eclipse.birt.core.script.function.bre;

import java.io.Serializable;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.function.i18n.Messages;
import org.eclipse.birt.core.script.functionservice.IScriptFunctionContext;
import org.eclipse.birt.core.script.functionservice.IScriptFunctionExecutor;

/**
 *
 */
abstract class Function_temp implements IScriptFunctionExecutor, Serializable {
	private static final long serialVersionUID = 1L;
	protected int minParamCount;
	protected int maxParamCount;

	@Override
	public Object execute(Object[] args, IScriptFunctionContext context) throws BirtException {
		if (args == null) {
			throw new BirtException("org.eclipse.birt.core.script.function", //$NON-NLS-1$
					"error.arguement.cannot.empty", //$NON-NLS-1$
					Messages.RESOURCE_BUNDLE);
		}
		if (args.length < minParamCount || args.length > maxParamCount) {
			throw new BirtException("org.eclipse.birt.core.script.function", //$NON-NLS-1$
					"error.argument.number.outofValidRange", //$NON-NLS-1$
					new Object[] { minParamCount, maxParamCount, args.length }, Messages.RESOURCE_BUNDLE);
		}
		return getValue(args, context);

	}

	/**
	 * Gets the evaluated the value. By default it will call
	 * {@link #getValue(Object[])}. You need to override this method or
	 * {@link #getValue(Object[])}
	 *
	 * @param args    arguments
	 * @param context script function context
	 * @return evaluated value
	 * @throws BirtException
	 */
	protected Object getValue(Object[] args, IScriptFunctionContext context) throws BirtException {
		return getValue(args);
	}

	/**
	 * Gets the evaluated the value. You need to override this method or
	 * {@link #getValue(Object[], IScriptFunctionContext)}
	 *
	 * @param args arguments
	 * @return evaluated value
	 * @throws BirtException
	 */
	protected Object getValue(Object[] args) throws BirtException {
		return null;
	}
}
