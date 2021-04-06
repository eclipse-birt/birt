/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
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
 * This interface provide user to pass their own specified property during the
 * execution of script function.
 * 
 */
public interface IScriptFunctionContext {
	/**
	 * @deprecated Replaced by FUNCTION_BEAN_NAME
	 */
	public static final String FUNCITON_BEAN_NAME = "org.eclipse.birt.core.script.functionservice.context.functionBean";

	public static final String FUNCTION_BEAN_NAME = "org.eclipse.birt.core.script.functionservice.context.functionBean";

	public static final String LOCALE = "org.eclipse.birt.core.script.functionservice.context.locale";
	public static final String TIMEZONE = "org.eclipse.birt.core.script.functionservice.context.timeZone";

	/**
	 * find the property value with specified name
	 * 
	 * @param name
	 * @return
	 */
	public Object findProperty(String name);
}
