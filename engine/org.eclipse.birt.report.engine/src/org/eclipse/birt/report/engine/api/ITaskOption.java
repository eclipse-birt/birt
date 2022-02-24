/*******************************************************************************
 * Copyright (c)2007 Actuate Corporation.
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

package org.eclipse.birt.report.engine.api;

import java.util.Map;

public interface ITaskOption {

	/**
	 * get all the options defined in this object
	 *
	 * @return
	 */
	Map getOptions();

	/**
	 * set the option value.
	 *
	 * @param name  option name.
	 * @param value value
	 */
	void setOption(String name, Object value);

	/**
	 * get the option value defined by the name.
	 *
	 * @param name option name.
	 * @return value, null if not defined
	 */
	Object getOption(String name);

	/**
	 * if there exits an option named by name.
	 *
	 * @param name option name.
	 * @return true if user has defined an option with this name, even if the value
	 *         is NULL. false otherwise.
	 */
	boolean hasOption(String name);

}
