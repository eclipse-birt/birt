/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.api.simpleapi;

/**
 * 
 * Represents the design of an ResultSetColumn in the scripting environment
 * 
 */

public interface IResultSetColumn {

	/**
	 * Gets column name.
	 * 
	 * @return column name
	 */

	String getName();

	/**
	 * Gets native data type.
	 * 
	 * @return native data type.
	 */

	Integer getNativeDataType();

	/**
	 * Gets position.
	 * 
	 * @return position
	 */

	Integer getPosition();

	/**
	 * Gets column data type.
	 * 
	 * @return column data type.
	 */

	String getColumnDataType();
}
