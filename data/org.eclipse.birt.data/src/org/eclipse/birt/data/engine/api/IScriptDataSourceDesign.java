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
	String getOpenScript();

	/**
	 * Gets the <code>close</code> script for closing the data source.
	 *
	 * @return The <code>close</code> script
	 */
	String getCloseScript();

}
