/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.engine.api.aggregation;

/**
 *
 */
public interface IParameterDefn {

	/**
	 * get the native name of this parameter.
	 * 
	 * @return
	 */
	public String getName();

	/**
	 * to indicate whether this parameter is optional.
	 * 
	 * @return
	 */
	public boolean isOptional();

	/**
	 * to indicate whether this parameter reference to a column or binding of BIRT.
	 * 
	 * @return
	 */
	public boolean isDataField();

	/**
	 * get the display name of this parameter.
	 * 
	 * @return
	 */
	public String getDisplayName();

	/**
	 * get the description of this parameter.
	 * 
	 * @return
	 */
	public String getDescription();

	/**
	 * check whether this parameter support specified data type.
	 * 
	 * @return
	 */
	public boolean supportDataType(int dataType);
}
