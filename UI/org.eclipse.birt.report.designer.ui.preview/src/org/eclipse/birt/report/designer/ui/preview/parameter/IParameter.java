/*******************************************************************************
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
 *******************************************************************************/

package org.eclipse.birt.report.designer.ui.preview.parameter;

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

	public String getDefaultValue();

	/**
	 * Sets parameter group
	 * 
	 * @param group
	 */

	public void setParentGroup(IParamGroup group);

	/**
	 * Gets parameter group
	 * 
	 * @return
	 */
	public IParamGroup getParentGroup();

	/**
	 * Sets selection value.
	 * 
	 * @param value
	 */
	public void setSelectionValue(String value);

	/**
	 * Gets selection value.
	 * 
	 * @return selection value.
	 */
	public String getSelectionValue();

	/**
	 * Gets isRequired property.
	 * 
	 * @return
	 */
	public boolean isRequired();

}
