/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.ui.scripts;

/**
 * IScriptContextProvider
 */
public interface IScriptContextProvider {

	/**
	 * Returns associated script context info for specific context.
	 * 
	 * @param contextName
	 * @return
	 */
	IScriptContextInfo[] getScriptContext(String contextName);

	/**
	 * Returns associated script context info for specific context and method.
	 * 
	 * @param contextName
	 * @param methodName
	 * @return
	 */
	IScriptContextInfo[] getScriptContext(String contextName, String methodName);
}
