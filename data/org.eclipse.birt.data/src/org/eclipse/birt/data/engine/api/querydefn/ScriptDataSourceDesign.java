/*
 *************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  
 *************************************************************************
 */

package org.eclipse.birt.data.engine.api.querydefn;

import org.eclipse.birt.data.engine.api.IScriptDataSourceDesign;

/**
 * Default implementation of
 * {@link org.eclipse.birt.data.engine.api.IScriptDataSourceDesign} interface.
 * <p>
 */
public class ScriptDataSourceDesign extends BaseDataSourceDesign implements IScriptDataSourceDesign {
	private String openScript;
	private String closeScript;

	/**
	 * Constructs a script data source with specified name
	 * 
	 * @param name
	 */
	public ScriptDataSourceDesign(String name) {
		super(name);
	}

	/**
	 * @see org.eclipse.birt.data.engine.api.IScriptDataSourceDesign#getOpenScript()
	 */
	public String getOpenScript() {
		return openScript;
	}

	/**
	 * Specifies the <code>open</code> script for opening the data source
	 * (connection).
	 */
	public void setOpenScript(String script) {
		openScript = script;
	}

	/**
	 * @see org.eclipse.birt.data.engine.api.IScriptDataSourceDesign#getCloseScript()
	 */
	public String getCloseScript() {
		return closeScript;
	}

	/**
	 * Specifies the <code>close</code> script for opening the data source
	 * (connection).
	 */
	public void setCloseScript(String script) {
		closeScript = script;
	}

}
