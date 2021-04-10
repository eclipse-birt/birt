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

package org.eclipse.birt.report.designer.ui.parameters;

import java.util.List;

import org.eclipse.birt.core.exception.BirtException;

/**
 * Parameter interface
 * 
 */
public interface IParameter {

	/**
	 * Gets parameter values
	 * 
	 * @return parameter values.
	 */

	public List getValueList();

	/**
	 * Formats input value.
	 * 
	 * @param input
	 * @return formatted input value
	 */
	public String format(String input) throws BirtException;

	/**
	 * Converts the value to chosen data type
	 * 
	 * @param value
	 * @param type
	 * @return value of chosen data type
	 * @throws BirtException
	 */
	public Object converToDataType(Object value) throws BirtException;

	/**
	 * Gets default value.
	 * 
	 * @return default value.
	 */

	public Object getDefaultValue();

	/**
	 * Sets parameter group
	 * 
	 * @param group
	 */

	public void setParentGroup(IParameterGroup group);

	/**
	 * Gets parameter group
	 * 
	 * @return
	 */
	public IParameterGroup getParentGroup();

	/**
	 * Sets selection value.
	 * 
	 * @param value
	 */
	public void setSelectionValue(Object value);

	/**
	 * Gets selection value.
	 * 
	 * @return selection value.
	 */
	public Object getSelectionValue();

	/**
	 * Gets isRequired property.
	 * 
	 * @return
	 */
	public boolean isRequired();

}
