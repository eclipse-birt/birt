/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
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

/**
 * This interface provide user to pass their own specified property during the
 * execution of script function.
 *
 */
public interface IScriptFunctionContext {
	/**
	 * @deprecated Replaced by FUNCTION_BEAN_NAME
	 */
	@Deprecated
	String FUNCITON_BEAN_NAME = "org.eclipse.birt.core.script.functionservice.context.functionBean";

	String FUNCTION_BEAN_NAME = "org.eclipse.birt.core.script.functionservice.context.functionBean";

	String LOCALE = "org.eclipse.birt.core.script.functionservice.context.locale";
	String TIMEZONE = "org.eclipse.birt.core.script.functionservice.context.timeZone";

	/**
	 * find the property value with specified name
	 *
	 * @param name
	 * @return
	 */
	Object findProperty(String name);
}
