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

import org.eclipse.birt.data.engine.api.IBaseDataSourceDesign;
import org.eclipse.birt.data.engine.api.script.IBaseDataSourceEventHandler;

/**
 * Default implementation of
 * {@link org.eclipse.birt.data.engine.api.IBaseDataSourceDesign} interface.
 * <p>
 *
 * Describes the static design of a data source (connection) to be used by the
 * Data Engine. Each subclass defines a specific type of data source.
 */
public class BaseDataSourceDesign implements IBaseDataSourceDesign {
	private String name;
	private String beforeOpenScript;
	private String afterOpenScript;
	private String beforeCloseScript;
	private String afterCloseScript;
	private IBaseDataSourceEventHandler eventHandler;

	/**
	 * Constructor: Creates a data source with specified name
	 */
	public BaseDataSourceDesign(String name) {
		this.name = name;
	}

	/**
	 * Gets the name of this data source
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * Gets the <code>beforeOpen<code> Script of the data source
	 */
	@Override
	public String getBeforeOpenScript() {
		return beforeOpenScript;
	}

	/**
	 * Sets the <code>beforeOpen</code> script of the data source.
	 */
	public void setBeforeOpenScript(String script) {
		beforeOpenScript = script;
	}

	/**
	 * Gets the <code>afterOpen</code> script of the data source
	 */
	@Override
	public String getAfterOpenScript() {
		return afterOpenScript;
	}

	/**
	 * Sets the <code>afterOpen</code> script of the data source.
	 *
	 * @param script afterOpen script
	 */
	public void setAfterOpenScript(String script) {
		afterOpenScript = script;
	}

	/**
	 * Gets the <code>beforeClose</code> script of the data source
	 */
	@Override
	public String getBeforeCloseScript() {
		return beforeCloseScript;
	}

	/**
	 * Sets the <code>beforeClose</code> script of the data source.
	 */
	public void setBeforeCloseScript(String script) {
		beforeCloseScript = script;
	}

	/**
	 * Gets the <code>afterClose</code> script of the data source
	 */
	@Override
	public String getAfterCloseScript() {
		return afterCloseScript;
	}

	/**
	 * Specifies the <code>afterClose</code> script of the data source.
	 */
	public void setAfterCloseScript(String script) {
		afterCloseScript = script;
	}

	/**
	 * @see org.eclipse.birt.data.engine.api.IBaseDataSourceDesign#getEventHandler()
	 */
	@Override
	public IBaseDataSourceEventHandler getEventHandler() {
		return eventHandler;
	}

	/**
	 * Sets the event handler for this data source
	 */
	public void setEventHandler(IBaseDataSourceEventHandler handler) {
		this.eventHandler = handler;
	}

}
