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
 * ScriptContextProviderAdapter
 */
abstract public class ScriptContextProviderAdapter implements IScriptContextProvider {

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.designer.ui.scripts.IScriptContextProvider#
	 * getScriptContext(java.lang.String)
	 */
	public IScriptContextInfo[] getScriptContext(String contextName) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.designer.ui.scripts.IScriptContextProvider#
	 * getScriptContext(java.lang.String, java.lang.String)
	 */
	public IScriptContextInfo[] getScriptContext(String contextName, String methodName) {
		return null;
	}

}
