/*
 *************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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
 *  
 *************************************************************************
 */

package org.eclipse.birt.data.engine.api.querydefn;

import org.eclipse.birt.data.engine.api.IScriptDataSetDesign;

/**
 * Default implementation of
 * {@link org.eclipse.birt.data.engine.api.IScriptDataSetDesign} interface.
 * <p>
 */
public class ScriptDataSetDesign extends BaseDataSetDesign implements IScriptDataSetDesign {
	private String openScript;
	private String fetchScript;
	private String closeScript;
	private String describeScript;

	/**
	 * Constructs a script data set with the specified name
	 */
	public ScriptDataSetDesign(String name) {
		super(name);
	}

	/**
	 * Constructs a script data set with the specified name and data source name
	 */
	public ScriptDataSetDesign(String name, String dataSourceName) {
		super(name, dataSourceName);
	}

	/**
	 * @see org.eclipse.birt.data.engine.api.IScriptDataSetDesign#getOpenScript()
	 */
	public String getOpenScript() {
		return openScript;
	}

	/**
	 * Specifies the <code>open</code> script for opening the data set.
	 */
	public void setOpenScript(String script) {
		openScript = script;
	}

	/**
	 * @see org.eclipse.birt.data.engine.api.IScriptDataSetDesign#getFetchScript()
	 */
	public String getFetchScript() {
		return fetchScript;
	}

	/**
	 * Specifies the <code>fetch</code> script for fetching each data row.
	 */
	public void setFetchScript(String script) {
		fetchScript = script;
	}

	/**
	 * @see org.eclipse.birt.data.engine.api.IScriptDataSetDesign#getCloseScript()
	 */
	public String getCloseScript() {
		return closeScript;
	}

	/**
	 * Specifies the <code>close</code> script for closing the data set.
	 */
	public void setCloseScript(String script) {
		closeScript = script;
	}

	/**
	 * @see org.eclipse.birt.data.engine.api.IScriptDataSetDesign#getDescribeScript()
	 */
	public String getDescribeScript() {
		return describeScript;
	}

	/**
	 * Specifies the <code>describe</code> script for providing the data set
	 * metadata
	 */
	public void setDescribeScript(String script) {
		describeScript = script;
	}

}
