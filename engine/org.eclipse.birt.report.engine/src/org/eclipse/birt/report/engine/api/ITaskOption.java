/*******************************************************************************
 * Copyright (c)2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
	public Map getOptions();

	/**
	 * set the option value.
	 * 
	 * @param name  option name.
	 * @param value value
	 */
	public void setOption(String name, Object value);

	/**
	 * get the option value defined by the name.
	 * 
	 * @param name option name.
	 * @return value, null if not defined
	 */
	public Object getOption(String name);

	/**
	 * if there exits an option named by name.
	 * 
	 * @param name option name.
	 * @return true if user has defined an option with this name, even if the value
	 *         is NULL. false otherwise.
	 */
	public boolean hasOption(String name);

}
