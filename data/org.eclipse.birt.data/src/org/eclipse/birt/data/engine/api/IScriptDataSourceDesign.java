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

package org.eclipse.birt.data.engine.api;

/**
 * Describes the static design of a scripted Data Source. The data source is to
 * be accessed via user-defined scripts defined in this specialized interface.
 */
public interface IScriptDataSourceDesign extends IBaseDataSourceDesign {
	/**
	 * Gets the <code>open</code> script for opening the data source (connection).
	 * 
	 * @return The <code>open</code> script.
	 */
	public abstract String getOpenScript();

	/**
	 * Gets the <code>close</code> script for closing the data source.
	 * 
	 * @return The <code>close</code> script
	 */
	public abstract String getCloseScript();

}
