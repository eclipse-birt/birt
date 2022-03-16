/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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
	@Override
	public IScriptContextInfo[] getScriptContext(String contextName) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.designer.ui.scripts.IScriptContextProvider#
	 * getScriptContext(java.lang.String, java.lang.String)
	 */
	@Override
	public IScriptContextInfo[] getScriptContext(String contextName, String methodName) {
		return null;
	}

}
