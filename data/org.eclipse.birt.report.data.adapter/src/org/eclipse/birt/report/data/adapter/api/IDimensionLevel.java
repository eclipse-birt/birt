/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.data.adapter.api;

/**
 * This interface provide api user a convenient way to access a cube dimension
 * as well as level and attribute.
 * 
 * @author Administrator
 *
 */
public interface IDimensionLevel {
	/**
	 * 
	 * @return
	 */
	public String getDimensionName();

	/**
	 * 
	 * @return
	 */
	public String getLevelName();

	/**
	 * 
	 * @return
	 */
	public String getAttributeName();
}
