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

package org.eclipse.birt.report.designer.internal.ui.command;

import org.eclipse.core.commands.AbstractParameterValueConverter;
import org.eclipse.core.commands.ParameterValueConversionException;

/**
 * 
 */

public class IntegerConverter extends AbstractParameterValueConverter {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.commands.AbstractParameterValueConverter#convertToObject(
	 * java.lang.String)
	 */
	public Object convertToObject(String parameterValue) throws ParameterValueConversionException {
		return new Integer(parameterValue);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.commands.AbstractParameterValueConverter#convertToString(
	 * java.lang.Object)
	 */
	public String convertToString(Object parameterValue) throws ParameterValueConversionException {
		String retString = ""; //$NON-NLS-1$
		Integer integer = (Integer) parameterValue;
		retString = integer.toString();
		return retString;
	}

}
