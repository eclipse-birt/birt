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
package org.eclipse.birt.data.engine.api;

import org.eclipse.birt.data.engine.api.script.IBaseDataSourceEventHandler;

/**
 * Describes the static design of any data source (connection) to be used by the
 * Data Engine. Each sub-interface defines a specific type of data source.
 */
public interface IBaseDataSourceDesign {
	/**
	 * Gets the name of this data source.
	 */
	public abstract String getName();

	/**
	 * Gets the <code>beforeOpen</code> script to be called just before opening the
	 * data source (connection).
	 * 
	 * @return The <code>beforeOpen</code> script. Null if none is defined.
	 */
	public abstract String getBeforeOpenScript();

	/**
	 * Gets the <code>afterOpen</code> script of the data source.
	 * 
	 * @return The <code>afterOpen</code> script. Null if none is defined.
	 */
	public abstract String getAfterOpenScript();

	/**
	 * Gets the <code>beforeClose</code> script to be called just before closing the
	 * data source (connection).
	 * 
	 * @return The <code>beforeClose</code> script. Null if none is defined.
	 */
	public abstract String getBeforeCloseScript();

	/**
	 * Gets the <code>afterClose</code> script of the data source.
	 * 
	 * @return The <code>afterClose</code> script. Null if none is defined.
	 */
	public abstract String getAfterCloseScript();

	/**
	 * Gets the event handler for the data source
	 */
	public abstract IBaseDataSourceEventHandler getEventHandler();
}
